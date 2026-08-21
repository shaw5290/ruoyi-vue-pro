package cn.iocoder.yudao.module.project.bom.service;

import cn.iocoder.yudao.module.project.bom.controller.admin.group.vo.*;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.*;
import java.util.List;

public interface BomLibraryService {
    Long saveGroup(BomGroupSaveReqVO reqVO);
    void deleteGroup(Long id);
    List<BomGroupDO> getGroupList(Long projectId);
    Long saveVersion(BomGroupVersionSaveReqVO reqVO);
    List<BomGroupVersionDO> getVersionList(Long groupId);
    Long saveVariant(BomVariantSaveReqVO reqVO);
    void deleteVariant(Long id);
    List<BomVariantDO> getVariantList(Long versionId);
    Long saveItem(BomItemSaveReqVO reqVO);
    void deleteItem(Long id);
    void bindWmsItem(BomItemBindWmsReqVO reqVO);
    void unbindWmsItem(Long id);
    List<BomItemDO> getItemList(Long variantId);
    List<BomItemDO> getItemListByGroup(Long groupId);
    int importItems(Long projectId, Long groupId, Long variantId, List<BomItemImportExcelVO> rows);
    void saveWmsBomBindings(BomWmsBomBindingSaveReqVO reqVO);
    List<BomWmsBomBindingDO> getWmsBomBindings(Long projectBomId);
}

