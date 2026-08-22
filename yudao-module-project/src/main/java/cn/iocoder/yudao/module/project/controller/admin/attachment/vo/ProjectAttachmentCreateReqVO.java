package cn.iocoder.yudao.module.project.controller.admin.attachment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Schema(description = "管理后台 - 创建项目附件 Request VO")
@Data
public class ProjectAttachmentCreateReqVO {

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目编号不能为空")
    private Long projectId;

    @Schema(description = "BOM 分组编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "BOM 分组编号不能为空")
    private Long bomGroupId;

    @Schema(description = "原始文件名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文件名不能为空")
    private String fileName;

    @Schema(description = "文件访问地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文件地址不能为空")
    private String fileUrl;

    @Schema(description = "文件大小，单位为字节", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件大小不能为空")
    @PositiveOrZero(message = "文件大小不能小于 0")
    private Long fileSize;

    @Schema(description = "MIME 类型")
    private String fileType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序")
    private Integer sort;

}
