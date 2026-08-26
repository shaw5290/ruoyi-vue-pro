package cn.iocoder.yudao.module.project.controller.admin.task;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.controller.admin.task.vo.ProjectTaskRespVO;
import cn.iocoder.yudao.module.project.controller.admin.task.vo.ProjectTaskSaveReqVO;
import cn.iocoder.yudao.module.project.controller.admin.task.vo.ProjectTaskStatusUpdateReqVO;
import cn.iocoder.yudao.module.project.service.task.ProjectTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 项目任务")
@RestController
@RequestMapping("/project/task")
@Validated
public class ProjectTaskController {

    @Resource
    private ProjectTaskService projectTaskService;

    @GetMapping("/list")
    @Operation(summary = "获得项目任务列表")
    @PreAuthorize("@ss.hasPermission('project:task:query')")
    public CommonResult<List<ProjectTaskRespVO>> getTaskList(@RequestParam("projectId") Long projectId) {
        return success(BeanUtils.toBean(projectTaskService.getTaskList(projectId), ProjectTaskRespVO.class));
    }

    @PostMapping("/create")
    @Operation(summary = "创建项目任务")
    @PreAuthorize("@ss.hasAnyPermissions('project:task:write', 'project:info:update')")
    public CommonResult<Long> createTask(@Valid @RequestBody ProjectTaskSaveReqVO reqVO) {
        return success(projectTaskService.createTask(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新项目任务")
    @PreAuthorize("@ss.hasAnyPermissions('project:task:write', 'project:info:update')")
    public CommonResult<Boolean> updateTask(@Valid @RequestBody ProjectTaskSaveReqVO reqVO) {
        projectTaskService.updateTask(reqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新项目任务状态")
    @PreAuthorize("@ss.hasAnyPermissions('project:task:write', 'project:info:update')")
    public CommonResult<Boolean> updateTaskStatus(@Valid @RequestBody ProjectTaskStatusUpdateReqVO reqVO) {
        projectTaskService.updateTaskStatus(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除项目任务")
    @Parameter(name = "id", required = true)
    @PreAuthorize("@ss.hasAnyPermissions('project:task:write', 'project:info:update')")
    public CommonResult<Boolean> deleteTask(@RequestParam("id") Long id,
                                            @RequestParam("projectId") Long projectId) {
        projectTaskService.deleteTask(id, projectId);
        return success(true);
    }

}
