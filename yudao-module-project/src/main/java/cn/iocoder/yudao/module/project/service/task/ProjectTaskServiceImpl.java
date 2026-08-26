package cn.iocoder.yudao.module.project.service.task;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.controller.admin.task.vo.ProjectTaskSaveReqVO;
import cn.iocoder.yudao.module.project.controller.admin.task.vo.ProjectTaskStatusUpdateReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.task.ProjectTaskDO;
import cn.iocoder.yudao.module.project.dal.mysql.task.ProjectTaskMapper;
import cn.iocoder.yudao.module.project.service.project.ProjectService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.PROJECT_TASK_NOT_EXISTS;

@Service
@Validated
public class ProjectTaskServiceImpl implements ProjectTaskService {

    @Resource
    private ProjectTaskMapper projectTaskMapper;
    @Resource
    private ProjectService projectService;

    @Override
    public Long createTask(ProjectTaskSaveReqVO createReqVO) {
        projectService.validateProjectExists(createReqVO.getProjectId());
        ProjectTaskDO task = BeanUtils.toBean(createReqVO, ProjectTaskDO.class);
        if (task.getPriority() == null) {
            task.setPriority(1);
        }
        if (task.getStatus() == null) {
            task.setStatus(0);
        }
        if (task.getSort() == null) {
            task.setSort(0);
        }
        projectTaskMapper.insert(task);
        return task.getId();
    }

    @Override
    public void updateTask(ProjectTaskSaveReqVO updateReqVO) {
        validateTaskBelongsToProject(updateReqVO.getId(), updateReqVO.getProjectId());
        projectTaskMapper.updateById(BeanUtils.toBean(updateReqVO, ProjectTaskDO.class));
    }

    @Override
    public void updateTaskStatus(ProjectTaskStatusUpdateReqVO updateReqVO) {
        validateTaskBelongsToProject(updateReqVO.getId(), updateReqVO.getProjectId());
        projectTaskMapper.updateById(new ProjectTaskDO()
                .setId(updateReqVO.getId())
                .setStatus(updateReqVO.getStatus()));
    }

    @Override
    public void deleteTask(Long id, Long projectId) {
        validateTaskBelongsToProject(id, projectId);
        projectTaskMapper.deleteById(id);
    }

    @Override
    public List<ProjectTaskDO> getTaskList(Long projectId) {
        projectService.validateProjectExists(projectId);
        return projectTaskMapper.selectListByProjectId(projectId);
    }

    private ProjectTaskDO validateTaskBelongsToProject(Long id, Long projectId) {
        if (id == null) {
            throw exception(PROJECT_TASK_NOT_EXISTS);
        }
        ProjectTaskDO task = projectTaskMapper.selectById(id);
        if (task == null || !projectId.equals(task.getProjectId())) {
            throw exception(PROJECT_TASK_NOT_EXISTS);
        }
        return task;
    }

}
