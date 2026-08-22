package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 保存 BOM 分组 Markdown 文档 Request VO")
@Data
public class BomDocumentSaveReqVO {

    private Long id;
    @NotNull(message = "项目编号不能为空")
    private Long projectId;
    @NotNull(message = "BOM 分组编号不能为空")
    private Long bomGroupId;
    @NotBlank(message = "文档标题不能为空")
    private String title;
    @Schema(description = "Markdown 正文")
    private String content;
    private Integer sort;

}
