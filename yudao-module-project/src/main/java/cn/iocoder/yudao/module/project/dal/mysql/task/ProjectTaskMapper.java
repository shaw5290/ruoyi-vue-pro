package cn.iocoder.yudao.module.project.dal.mysql.task;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.task.ProjectTaskDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ProjectTaskMapper extends BaseMapperX<ProjectTaskDO> {

    default List<ProjectTaskDO> selectListByProjectId(Long projectId) {
        return selectList(new LambdaQueryWrapperX<ProjectTaskDO>()
                .eq(ProjectTaskDO::getProjectId, projectId)
                .orderByAsc(ProjectTaskDO::getSort)
                .orderByAsc(ProjectTaskDO::getDueDate)
                .orderByDesc(ProjectTaskDO::getId));
    }

    default long selectCountByProjectId(Long projectId) {
        return selectCount(ProjectTaskDO::getProjectId, projectId);
    }

    default long selectCompletedCount(Long projectId) {
        return selectCount(new LambdaQueryWrapperX<ProjectTaskDO>()
                .eq(ProjectTaskDO::getProjectId, projectId)
                .eq(ProjectTaskDO::getStatus, 2));
    }

    default long selectOverdueCount(Long projectId, LocalDate today) {
        return selectCount(new LambdaQueryWrapperX<ProjectTaskDO>()
                .eq(ProjectTaskDO::getProjectId, projectId)
                .ne(ProjectTaskDO::getStatus, 2)
                .lt(ProjectTaskDO::getDueDate, today));
    }

    default long selectHighPriorityOpenCount(Long projectId) {
        return selectCount(new LambdaQueryWrapperX<ProjectTaskDO>()
                .eq(ProjectTaskDO::getProjectId, projectId)
                .eq(ProjectTaskDO::getPriority, 2)
                .ne(ProjectTaskDO::getStatus, 2));
    }

}
