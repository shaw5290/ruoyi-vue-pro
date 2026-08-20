package cn.iocoder.yudao.module.project.dal.mysql.solution;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.solution.ProjectSolutionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectSolutionMapper extends BaseMapperX<ProjectSolutionDO> {

    default List<ProjectSolutionDO> selectListByProjectId(Long projectId) {
        return selectList(new LambdaQueryWrapperX<ProjectSolutionDO>()
                .eq(ProjectSolutionDO::getProjectId, projectId)
                .orderByAsc(ProjectSolutionDO::getSort)
                .orderByDesc(ProjectSolutionDO::getId));
    }

    default long selectCountByProjectId(Long projectId) {
        return selectCount(ProjectSolutionDO::getProjectId, projectId);
    }

    default ProjectSolutionDO selectByProjectIdAndName(Long projectId, String name) {
        return selectOne(ProjectSolutionDO::getProjectId, projectId,
                ProjectSolutionDO::getName, name);
    }

}
