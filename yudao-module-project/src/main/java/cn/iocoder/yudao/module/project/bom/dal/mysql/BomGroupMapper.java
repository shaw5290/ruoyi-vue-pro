package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomGroupDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BomGroupMapper extends BaseMapperX<BomGroupDO> {
    default List<BomGroupDO> selectListByProjectId(Long projectId) {
        return selectList(new LambdaQueryWrapperX<BomGroupDO>()
                .eq(BomGroupDO::getProjectId, projectId)
                .orderByAsc(BomGroupDO::getSort).orderByDesc(BomGroupDO::getId));
    }
    default BomGroupDO selectByProjectIdAndCode(Long projectId, String code) {
        return selectOne(BomGroupDO::getProjectId, projectId, BomGroupDO::getCode, code);
    }
    default List<BomGroupDO> selectListByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapperX<BomGroupDO>()
                .eq(BomGroupDO::getParentId, parentId)
                .orderByAsc(BomGroupDO::getSort).orderByDesc(BomGroupDO::getId));
    }
}

