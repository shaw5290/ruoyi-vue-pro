package cn.iocoder.yudao.module.project.bom.service;

import cn.iocoder.yudao.module.project.bom.controller.admin.selection.vo.BomSelectionSaveReqVO;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomSolutionSelectionDO;
import java.util.List;

public interface BomSelectionService {
    void saveSelections(BomSelectionSaveReqVO reqVO);
    List<BomSolutionSelectionDO> getSelections(Long projectSolutionId);
}

