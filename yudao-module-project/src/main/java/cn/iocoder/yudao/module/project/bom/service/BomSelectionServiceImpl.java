package cn.iocoder.yudao.module.project.bom.service;

import cn.iocoder.yudao.module.project.bom.controller.admin.selection.vo.BomSelectionSaveReqVO;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.*;
import cn.iocoder.yudao.module.project.bom.dal.mysql.BomSolutionSelectionMapper;
import cn.iocoder.yudao.module.project.api.solution.ProjectSolutionApi;
import cn.iocoder.yudao.module.project.api.solution.dto.ProjectSolutionRespDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.bom.enums.ErrorCodeConstants.*;

@Service
public class BomSelectionServiceImpl implements BomSelectionService {
    @Resource private BomSolutionSelectionMapper selectionMapper;
    @Resource private BomLibraryServiceImpl libraryService;
    @Resource private ProjectSolutionApi projectSolutionApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSelections(BomSelectionSaveReqVO reqVO) {
        ProjectSolutionRespDTO solution = projectSolutionApi.validateProjectSolutionExists(reqVO.getProjectSolutionId());
        Set<Long> groupIds = new HashSet<>();
        List<BomSolutionSelectionDO> entities = new ArrayList<>();
        for (BomSelectionSaveReqVO.Item item : reqVO.getSelections()) {
            if (!groupIds.add(item.getBomGroupId())) throw exception(BOM_SELECTION_DUPLICATE_GROUP);
            BomGroupDO group = libraryService.validateGroup(item.getBomGroupId());
            BomGroupVersionDO version = libraryService.validateVersion(item.getBomGroupVersionId());
            BomVariantDO variant = item.getBomVariantId() == null
                    ? libraryService.getDefaultVariant(item.getBomGroupVersionId())
                    : libraryService.validateVariant(item.getBomVariantId());
            if (!group.getProjectId().equals(solution.getProjectId())
                    || !version.getGroupId().equals(item.getBomGroupId())
                    || !variant.getGroupId().equals(item.getBomGroupId())
                    || !variant.getGroupVersionId().equals(item.getBomGroupVersionId())) {
                throw exception(BOM_SELECTION_MISMATCH);
            }
            entities.add(BomSolutionSelectionDO.builder()
                    .projectSolutionId(reqVO.getProjectSolutionId())
                    .bomGroupId(item.getBomGroupId()).bomGroupVersionId(item.getBomGroupVersionId())
                    .bomVariantId(variant.getId()).quantity(item.getQuantity())
                    .parameterOverrides(item.getParameterOverrides()).sortOrder(item.getSortOrder()).build());
        }
        selectionMapper.deleteBySolutionId(reqVO.getProjectSolutionId());
        entities.forEach(selectionMapper::insert);
    }

    @Override
    public List<BomSolutionSelectionDO> getSelections(Long projectSolutionId) {
        projectSolutionApi.validateProjectSolutionExists(projectSolutionId);
        return selectionMapper.selectListBySolutionId(projectSolutionId);
    }
}

