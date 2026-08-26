package cn.iocoder.yudao.module.project.service.task;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.project.controller.admin.task.vo.ProjectTaskSaveReqVO;
import cn.iocoder.yudao.module.project.controller.admin.task.vo.ProjectTaskStatusUpdateReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.task.ProjectTaskDO;
import cn.iocoder.yudao.module.project.dal.mysql.task.ProjectTaskMapper;
import cn.iocoder.yudao.module.project.service.project.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.PROJECT_TASK_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProjectTaskServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private ProjectTaskServiceImpl taskService;

    @Mock
    private ProjectTaskMapper taskMapper;
    @Mock
    private ProjectService projectService;

    @Test
    void createTask_appliesDefaults() {
        ProjectTaskSaveReqVO request = new ProjectTaskSaveReqVO();
        request.setProjectId(10L);
        request.setTitle("采购控制器");
        doAnswer(invocation -> {
            ProjectTaskDO task = invocation.getArgument(0);
            task.setId(100L);
            return 1;
        }).when(taskMapper).insert(any(ProjectTaskDO.class));

        Long id = taskService.createTask(request);

        assertEquals(100L, id);
        verify(projectService).validateProjectExists(10L);
        ArgumentCaptor<ProjectTaskDO> captor = ArgumentCaptor.forClass(ProjectTaskDO.class);
        verify(taskMapper).insert(captor.capture());
        assertEquals(1, captor.getValue().getPriority());
        assertEquals(0, captor.getValue().getStatus());
        assertEquals(0, captor.getValue().getSort());
    }

    @Test
    void updateTask_rejectsTaskFromAnotherProject() {
        ProjectTaskSaveReqVO request = new ProjectTaskSaveReqVO();
        request.setId(100L);
        request.setProjectId(10L);
        request.setTitle("装配机架");
        when(taskMapper.selectById(100L)).thenReturn(ProjectTaskDO.builder()
                .id(100L).projectId(20L).build());

        assertServiceException(() -> taskService.updateTask(request), PROJECT_TASK_NOT_EXISTS);
        verify(taskMapper, never()).updateById(any(ProjectTaskDO.class));
    }

    @Test
    void updateTaskStatus_updatesOnlyStatusAfterOwnershipCheck() {
        ProjectTaskStatusUpdateReqVO request = new ProjectTaskStatusUpdateReqVO();
        request.setId(100L);
        request.setProjectId(10L);
        request.setStatus(2);
        when(taskMapper.selectById(100L)).thenReturn(ProjectTaskDO.builder()
                .id(100L).projectId(10L).build());

        taskService.updateTaskStatus(request);

        ArgumentCaptor<ProjectTaskDO> captor = ArgumentCaptor.forClass(ProjectTaskDO.class);
        verify(taskMapper).updateById(captor.capture());
        assertEquals(100L, captor.getValue().getId());
        assertEquals(2, captor.getValue().getStatus());
    }

    @Test
    void deleteTask_rejectsMissingTask() {
        when(taskMapper.selectById(100L)).thenReturn(null);

        assertServiceException(() -> taskService.deleteTask(100L, 10L), PROJECT_TASK_NOT_EXISTS);
        verify(taskMapper, never()).deleteById(anyLong());
    }

}
