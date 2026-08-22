package cn.iocoder.yudao.module.project.controller.admin.attachment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 更新项目附件描述 Request VO")
@Data
public class ProjectAttachmentUpdateReqVO {

    @Schema(description = "附件编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "附件编号不能为空")
    private Long id;

    @Schema(description = "附件描述，支持富文本")
    private String remark;

}
