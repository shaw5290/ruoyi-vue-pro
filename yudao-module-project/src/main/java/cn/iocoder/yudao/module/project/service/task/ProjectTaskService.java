package cn.iocoder.yudao.module.project.service.task;

import cn.iocoder.yudao.module.project.controller.admin.task.vo.ProjectTaskSaveReqVO;
import cn.iocoder.yudao.module.project.controller.admin.task.vo.ProjectTaskStatusUpdateReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.task.ProjectTaskDO;

import java.util.List;

public interface ProjectTaskService {

    Long createTask(ProjectTaskSaveReqVO createReqVO);
    void updateTask(ProjectTaskSaveReqVO updateReqVO);
    void updateTaskStatus(ProjectTaskStatusUpdateReqVO updateReqVO);
    void deleteTask(Long id, Long projectId);
    List<ProjectTaskDO> getTaskList(Long projectId);

}
