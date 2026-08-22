package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.ProjectProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectProductMapper extends BaseMapperX<ProjectProductDO> {
    default List<ProjectProductDO> selectList(Long projectId, String keyword) {
        return selectList(new LambdaQueryWrapperX<ProjectProductDO>()
                .eqIfPresent(ProjectProductDO::getProjectId, projectId)
                .and(keyword != null && !keyword.isBlank(), query -> query
                        .like(ProjectProductDO::getName, keyword)
                        .or().like(ProjectProductDO::getMerchant, keyword))
                .orderByDesc(ProjectProductDO::getId));
    }
}
