package cn.iocoder.yudao.module.project.controller.admin.solution;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.controller.admin.solution.vo.ProjectSolutionRespVO;
import cn.iocoder.yudao.module.project.controller.admin.solution.vo.ProjectSolutionSaveReqVO;
import cn.iocoder.yudao.module.project.service.solution.ProjectSolutionService;
import cn.iocoder.yudao.module.project.service.solution.dto.ProjectSolutionSaveReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 项目方案")
@RestController
@RequestMapping("/project/solution")
@Validated
public class ProjectSolutionController {

    @Resource
    private ProjectSolutionService projectSolutionService;

    @PostMapping("/create")
    @Operation(summary = "创建项目方案")
    @PreAuthorize("@ss.hasPermission('project:solution:create')")
    public CommonResult<Long> create(@Valid @RequestBody ProjectSolutionSaveReqVO reqVO) {
        return success(projectSolutionService.createProjectSolution(
                BeanUtils.toBean(reqVO, ProjectSolutionSaveReqDTO.class)));
    }

    @PutMapping("/update")
    @Operation(summary = "更新项目方案")
    @PreAuthorize("@ss.hasPermission('project:solution:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody ProjectSolutionSaveReqVO reqVO) {
        projectSolutionService.updateProjectSolution(BeanUtils.toBean(reqVO, ProjectSolutionSaveReqDTO.class));
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除项目方案")
    @PreAuthorize("@ss.hasPermission('project:solution:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        projectSolutionService.deleteProjectSolution(id);
        return success(true);
    }

    @PutMapping("/adopt")
    @Operation(summary = "采用项目方案")
    @PreAuthorize("@ss.hasPermission('project:solution:adopt')")
    public CommonResult<Boolean> adopt(@RequestParam("id") Long id) {
        projectSolutionService.adoptProjectSolution(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得项目方案")
    @PreAuthorize("@ss.hasPermission('project:solution:query')")
    public CommonResult<ProjectSolutionRespVO> get(@RequestParam("id") Long id) {
        return success(BeanUtils.toBean(projectSolutionService.getProjectSolution(id), ProjectSolutionRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得项目方案列表")
    @PreAuthorize("@ss.hasPermission('project:solution:query')")
    public CommonResult<List<ProjectSolutionRespVO>> list(@RequestParam("projectId") Long projectId) {
        return success(BeanUtils.toBean(projectSolutionService.getProjectSolutionList(projectId), ProjectSolutionRespVO.class));
    }

}
