package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomSolutionSelectionDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BomSolutionSelectionMapper extends BaseMapperX<BomSolutionSelectionDO> {
    default List<BomSolutionSelectionDO> selectListBySolutionId(Long solutionId) {
        return selectList(new LambdaQueryWrapperX<BomSolutionSelectionDO>()
                .eq(BomSolutionSelectionDO::getProjectSolutionId, solutionId)
                .orderByAsc(BomSolutionSelectionDO::getSortOrder));
    }
    default void deleteBySolutionId(Long solutionId) {
        delete(BomSolutionSelectionDO::getProjectSolutionId, solutionId);
    }
    default void deleteByGroupId(Long groupId) {
        delete(BomSolutionSelectionDO::getBomGroupId, groupId);
    }
    default void deleteByVariantId(Long variantId) {
        delete(BomSolutionSelectionDO::getBomVariantId, variantId);
    }
}

