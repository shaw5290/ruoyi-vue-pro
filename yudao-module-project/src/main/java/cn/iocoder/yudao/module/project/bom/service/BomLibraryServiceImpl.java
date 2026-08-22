package cn.iocoder.yudao.module.project.bom.service;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.bom.controller.admin.group.vo.*;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.*;
import cn.iocoder.yudao.module.project.bom.dal.mysql.*;
import cn.iocoder.yudao.module.project.api.project.ProjectApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.bom.enums.ErrorCodeConstants.*;

@Service @Validated
public class BomLibraryServiceImpl implements BomLibraryService {
    private static final String DEFAULT_GROUP_CODE = "DEFAULT";
    private static final String DEFAULT_GROUP_NAME = "默认分组";
    @Resource private BomGroupMapper groupMapper;
    @Resource private BomGroupVersionMapper versionMapper;
    @Resource private BomVariantMapper variantMapper;
    @Resource private BomItemMapper itemMapper;
    @Resource private ProjectApi projectApi;
    @Resource private BomWmsBomBindingMapper wmsBomBindingMapper;
    @Resource private BomSolutionSelectionMapper selectionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveGroup(BomGroupSaveReqVO reqVO) {
        projectApi.validateProjectExists(reqVO.getProjectId());
        validateGroupParent(reqVO.getId(), reqVO.getProjectId(), reqVO.getParentId());
        BomGroupDO duplicate = groupMapper.selectByProjectIdAndCode(reqVO.getProjectId(), reqVO.getCode());
        if (duplicate != null && ObjUtil.notEqual(duplicate.getId(), reqVO.getId())) {
            throw exception(BOM_GROUP_CODE_DUPLICATE);
        }
        BomGroupDO entity = BeanUtils.toBean(reqVO, BomGroupDO.class);
        if (reqVO.getId() == null) {
            groupMapper.insert(entity);
            versionMapper.insert(BomGroupVersionDO.builder().groupId(entity.getId())
                    .versionNo("v1.0").description("初始版本").published(true).build());
        } else {
            validateGroup(reqVO.getId());
            groupMapper.updateById(entity);
        }
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGroup(Long id) {
        BomGroupDO group = validateGroup(id);
        if (DEFAULT_GROUP_CODE.equals(group.getCode())) {
            throw exception(BOM_DEFAULT_GROUP_DELETE_FORBIDDEN);
        }
        Long defaultVariantId = getOrCreateGroupStorageVariant(group.getProjectId(), null).getId();
        deleteGroupTree(id, defaultVariantId, new java.util.HashSet<>());
    }

    private void deleteGroupTree(Long id, Long defaultVariantId, java.util.Set<Long> visited) {
        if (!visited.add(id)) throw exception(BOM_GROUP_PARENT_INVALID);
        for (BomGroupDO child : groupMapper.selectListByParentId(id)) {
            deleteGroupTree(child.getId(), defaultVariantId, visited);
        }
        for (BomGroupVersionDO version : versionMapper.selectListByGroupId(id)) {
            for (BomVariantDO variant : variantMapper.selectListByVersionId(version.getId())) {
                itemMapper.updateVariantId(variant.getId(), defaultVariantId);
                deleteVariantData(variant.getId());
            }
            versionMapper.deleteById(version.getId());
        }
        selectionMapper.deleteByGroupId(id);
        groupMapper.deleteById(id);
    }

    private void validateGroupParent(Long groupId, Long projectId, Long parentId) {
        if (parentId == null) return;
        if (parentId.equals(groupId)) throw exception(BOM_GROUP_PARENT_INVALID);
        BomGroupDO parent = validateGroup(parentId);
        if (!projectId.equals(parent.getProjectId())) throw exception(BOM_GROUP_PARENT_INVALID);
        java.util.Set<Long> visited = new java.util.HashSet<>();
        while (parent != null) {
            if (!visited.add(parent.getId()) || parent.getId().equals(groupId)) {
                throw exception(BOM_GROUP_PARENT_INVALID);
            }
            parent = parent.getParentId() == null ? null : validateGroup(parent.getParentId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BomGroupDO> getGroupList(Long projectId) {
        projectApi.validateProjectExists(projectId);
        getOrCreateDefaultGroup(projectId);
        return groupMapper.selectListByProjectId(projectId);
    }

    @Override
    public Long saveVersion(BomGroupVersionSaveReqVO reqVO) {
        validateGroup(reqVO.getGroupId());
        BomGroupVersionDO entity = BeanUtils.toBean(reqVO, BomGroupVersionDO.class);
        if (reqVO.getId() == null) versionMapper.insert(entity); else {
            validateVersion(reqVO.getId());
            versionMapper.updateById(entity);
        }
        return entity.getId();
    }

    @Override public List<BomGroupVersionDO> getVersionList(Long groupId) {
        validateGroup(groupId);
        return versionMapper.selectListByGroupId(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveVariant(BomVariantSaveReqVO reqVO) {
        projectApi.validateProjectExists(reqVO.getProjectId());
        BomGroupDO group;
        BomGroupVersionDO version;
        if (reqVO.getGroupId() == null) {
            group = getOrCreateDefaultGroup(reqVO.getProjectId());
            version = versionMapper.selectListByGroupId(group.getId()).get(0);
            reqVO.setGroupId(group.getId());
            reqVO.setGroupVersionId(version.getId());
        } else {
            group = validateGroup(reqVO.getGroupId());
            if (!group.getProjectId().equals(reqVO.getProjectId()) || reqVO.getGroupVersionId() == null) {
                throw exception(BOM_SELECTION_MISMATCH);
            }
            version = validateVersion(reqVO.getGroupVersionId());
        }
        if (!version.getGroupId().equals(group.getId())) throw exception(BOM_SELECTION_MISMATCH);
        BomVariantDO entity = BeanUtils.toBean(reqVO, BomVariantDO.class);
        if (reqVO.getId() == null) variantMapper.insert(entity); else {
            validateVariant(reqVO.getId());
            variantMapper.updateById(entity);
        }
        if (Boolean.TRUE.equals(entity.getDefaultVariant())) {
            for (BomVariantDO item : variantMapper.selectListByVersionId(entity.getGroupVersionId())) {
                if (!item.getId().equals(entity.getId()) && Boolean.TRUE.equals(item.getDefaultVariant())) {
                    variantMapper.updateById(new BomVariantDO().setId(item.getId()).setDefaultVariant(false));
                }
            }
        }
        return entity.getId();
    }

    private BomGroupDO getOrCreateDefaultGroup(Long projectId) {
        BomGroupDO group = groupMapper.selectByProjectIdAndCode(projectId, DEFAULT_GROUP_CODE);
        if (group != null) return group;
        group = BomGroupDO.builder().projectId(projectId).code(DEFAULT_GROUP_CODE)
                .name(DEFAULT_GROUP_NAME).category("DEFAULT").description("未指定分组的 BOM 物料")
                .sort(Integer.MAX_VALUE).enabled(true).build();
        groupMapper.insert(group);
        versionMapper.insert(BomGroupVersionDO.builder().groupId(group.getId())
                .versionNo("v1.0").description("初始版本").published(true).build());
        return group;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVariant(Long id) {
        validateVariant(id);
        deleteVariantData(id);
        variantMapper.deleteById(id);
    }

    private void deleteVariantData(Long variantId) {
        for (BomItemDO item : itemMapper.selectListByVariantId(variantId)) itemMapper.deleteById(item.getId());
        wmsBomBindingMapper.deleteByProjectBomId(variantId);
        selectionMapper.deleteByVariantId(variantId);
    }

    @Override public List<BomVariantDO> getVariantList(Long versionId) {
        validateVersion(versionId);
        return variantMapper.selectListByVersionId(versionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveItem(BomItemSaveReqVO reqVO) {
        BomItemDO existing = reqVO.getId() == null ? null : itemMapper.selectById(reqVO.getId());
        if (reqVO.getId() != null && existing == null) throw exception(BOM_ITEM_NOT_EXISTS);
        Long variantId;
        if (reqVO.getGroupId() != null || reqVO.getProjectId() != null) {
            variantId = getOrCreateGroupStorageVariant(reqVO.getProjectId(), reqVO.getGroupId()).getId();
        } else if (reqVO.getBomVariantId() != null) {
            variantId = validateVariant(reqVO.getBomVariantId()).getId();
        } else if (existing != null) {
            variantId = existing.getBomVariantId();
        } else {
            throw exception(BOM_SELECTION_MISMATCH);
        }
        BomItemDO entity = BeanUtils.toBean(reqVO, BomItemDO.class);
        entity.setBomVariantId(variantId);
        if (entity.getWmsItemSkuId() == null) {
            entity.setItemSource("NONE");
            entity.setBindingStatus("UNBOUND");
        }
        if (reqVO.getId() == null) itemMapper.insert(entity); else itemMapper.updateById(entity);
        return entity.getId();
    }

    @Override
    public void deleteItem(Long id) {
        if (itemMapper.selectById(id) == null) throw exception(BOM_ITEM_NOT_EXISTS);
        itemMapper.deleteById(id);
    }

    @Override
    public void bindWmsItem(BomItemBindWmsReqVO reqVO) {
        BomItemDO item = itemMapper.selectById(reqVO.getBomItemId());
        if (item == null) throw exception(BOM_ITEM_NOT_EXISTS);
        itemMapper.updateById(new BomItemDO().setId(item.getId())
                .setWmsItemSkuId(reqVO.getWmsItemSkuId()).setItemSource("WMS").setBindingStatus("BOUND")
                .setItemCodeSnapshot(reqVO.getItemCode()).setSkuCodeSnapshot(reqVO.getSkuCode())
                .setItemNameSnapshot(reqVO.getItemName()).setSpecificationSnapshot(reqVO.getSpecification())
                .setUnitSnapshot(reqVO.getUnit()));
    }

    @Override
    public void unbindWmsItem(Long id) {
        BomItemDO item = itemMapper.selectById(id);
        if (item == null) throw exception(BOM_ITEM_NOT_EXISTS);
        itemMapper.updateById(new BomItemDO().setId(id).setWmsItemSkuId(null)
                .setItemSource("NONE").setBindingStatus("UNBOUND"));
    }

    @Override public List<BomItemDO> getItemList(Long variantId) {
        validateVariant(variantId);
        return itemMapper.selectListByVariantId(variantId);
    }

    @Override
    public List<BomItemDO> getItemListByGroup(Long groupId) {
        validateGroup(groupId);
        List<BomItemDO> result = new java.util.ArrayList<>();
        List<BomGroupVersionDO> versions = versionMapper.selectListByGroupId(groupId);
        if (versions.isEmpty()) return result;
        for (BomVariantDO variant : variantMapper.selectListByVersionId(versions.get(0).getId())) {
            result.addAll(itemMapper.selectListByVariantId(variant.getId()));
        }
        result.sort(java.util.Comparator
                .comparing(BomItemDO::getSort, java.util.Comparator.nullsLast(Integer::compareTo))
                .thenComparing(BomItemDO::getId, java.util.Comparator.nullsLast(Long::compareTo)));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importItems(Long projectId, Long groupId, Long variantId, List<BomItemImportExcelVO> rows) {
        Long targetVariantId;
        if (groupId != null || projectId != null) {
            targetVariantId = getOrCreateGroupStorageVariant(projectId, groupId).getId();
        } else if (variantId != null) {
            targetVariantId = validateVariant(variantId).getId();
        } else {
            throw exception(BOM_SELECTION_MISMATCH);
        }
        int sort = itemMapper.selectListByVariantId(targetVariantId).size();
        for (BomItemImportExcelVO row : rows) {
            BomItemDO item = BomItemDO.builder()
                    .bomVariantId(targetVariantId)
                    .itemNameSnapshot(row.getItemName())
                    .itemCodeSnapshot(row.getItemCode())
                    .specificationSnapshot(row.getSpecification())
                    .quantity(row.getQuantity())
                    .unitSnapshot(row.getUnit())
                    .plannedUnitPrice(row.getPlannedUnitPrice())
                    .purpose(row.getPurpose()).supplier(row.getSupplier())
                    .purchaseUrl(row.getPurchaseUrl()).remark(row.getRemark())
                    .itemSource("NONE").bindingStatus("UNBOUND").sort(sort++).build();
            itemMapper.insert(item);
        }
        return rows.size();
    }

    /**
     * 当前产品模型按分组直接维护 BOM 物料。为兼容既有表结构，服务端在分组内复用或创建
     * 一个不可见的存储容器，前端和新接口均不再暴露该层级。
     */
    private BomVariantDO getOrCreateGroupStorageVariant(Long projectId, Long groupId) {
        BomGroupDO group;
        if (groupId == null) {
            if (projectId == null) throw exception(BOM_SELECTION_MISMATCH);
            projectApi.validateProjectExists(projectId);
            group = getOrCreateDefaultGroup(projectId);
        } else {
            group = validateGroup(groupId);
            if (projectId != null && !projectId.equals(group.getProjectId())) {
                throw exception(BOM_SELECTION_MISMATCH);
            }
        }
        List<BomGroupVersionDO> versions = versionMapper.selectListByGroupId(group.getId());
        BomGroupVersionDO version;
        if (versions.isEmpty()) {
            version = BomGroupVersionDO.builder().groupId(group.getId()).versionNo("v1.0")
                    .description("兼容存储版本").published(true).build();
            versionMapper.insert(version);
        } else {
            version = versions.get(0);
        }
        List<BomVariantDO> variants = variantMapper.selectListByVersionId(version.getId());
        BomVariantDO variant = variants.stream().filter(item -> Boolean.TRUE.equals(item.getDefaultVariant()))
                .findFirst().orElse(variants.isEmpty() ? null : variants.get(0));
        if (variant != null) return variant;
        variant = BomVariantDO.builder().groupId(group.getId())
                .groupVersionId(version.getId()).name("项目 BOM 物料")
                .description("分组物料兼容存储容器").defaultVariant(true).sort(0).build();
        variantMapper.insert(variant);
        return variant;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWmsBomBindings(BomWmsBomBindingSaveReqVO reqVO) {
        validateVariant(reqVO.getProjectBomId());
        java.util.Set<Long> wmsBomIds = new java.util.HashSet<>();
        for (BomWmsBomBindingSaveReqVO.Item row : reqVO.getBindings()) {
            if (!wmsBomIds.add(row.getWmsBomId())) throw exception(BOM_WMS_BOM_BINDING_DUPLICATE);
        }
        wmsBomBindingMapper.deleteByProjectBomId(reqVO.getProjectBomId());
        for (BomWmsBomBindingSaveReqVO.Item row : reqVO.getBindings()) {
            wmsBomBindingMapper.insert(BomWmsBomBindingDO.builder()
                    .projectBomId(reqVO.getProjectBomId()).wmsBomId(row.getWmsBomId())
                    .wmsBomCodeSnapshot(row.getWmsBomCodeSnapshot())
                    .wmsBomNameSnapshot(row.getWmsBomNameSnapshot())
                    .quantity(row.getQuantity()).bindingStatus("BOUND").sort(row.getSort()).build());
        }
    }

    @Override
    public List<BomWmsBomBindingDO> getWmsBomBindings(Long projectBomId) {
        validateVariant(projectBomId);
        return wmsBomBindingMapper.selectListByProjectBomId(projectBomId);
    }

    BomGroupDO validateGroup(Long id) {
        BomGroupDO entity = groupMapper.selectById(id);
        if (entity == null) throw exception(BOM_GROUP_NOT_EXISTS);
        return entity;
    }
    BomGroupVersionDO validateVersion(Long id) {
        BomGroupVersionDO entity = versionMapper.selectById(id);
        if (entity == null) throw exception(BOM_VERSION_NOT_EXISTS);
        return entity;
    }
    BomVariantDO validateVariant(Long id) {
        BomVariantDO entity = variantMapper.selectById(id);
        if (entity == null) throw exception(BOM_VARIANT_NOT_EXISTS);
        return entity;
    }

    BomVariantDO getDefaultVariant(Long versionId) {
        BomVariantDO entity = variantMapper.selectDefaultByVersionId(versionId);
        if (entity == null) throw exception(BOM_VARIANT_NOT_EXISTS);
        return entity;
    }
}

