package cn.iocoder.yudao.module.project.controller.admin.attachment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.project.controller.admin.attachment.vo.ProjectAttachmentCreateReqVO;
import cn.iocoder.yudao.module.project.controller.admin.attachment.vo.ProjectAttachmentUpdateReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.attachment.ProjectAttachmentDO;
import cn.iocoder.yudao.module.project.service.attachment.ProjectAttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 项目附件")
@RestController
@RequestMapping("/project/attachment")
@Validated
public class ProjectAttachmentController {

    @Resource
    private ProjectAttachmentService attachmentService;

    @PostMapping("/create")
    @Operation(summary = "记录已上传的项目附件")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Long> createAttachment(@Valid @RequestBody ProjectAttachmentCreateReqVO reqVO) {
        return success(attachmentService.createAttachment(reqVO));
    }

    @PutMapping("/update-remark")
    @Operation(summary = "更新项目附件描述")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> updateAttachmentRemark(@Valid @RequestBody ProjectAttachmentUpdateReqVO reqVO) {
        attachmentService.updateAttachmentRemark(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除项目附件关系")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> deleteAttachment(@RequestParam Long id) {
        attachmentService.deleteAttachment(id);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获得项目 BOM 附件汇总或指定分组附件")
    @PreAuthorize("@ss.hasPermission('bom:library:query')")
    public CommonResult<List<ProjectAttachmentDO>> getAttachmentList(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long bomGroupId) {
        return success(attachmentService.getAttachmentList(projectId, bomGroupId));
    }

}
