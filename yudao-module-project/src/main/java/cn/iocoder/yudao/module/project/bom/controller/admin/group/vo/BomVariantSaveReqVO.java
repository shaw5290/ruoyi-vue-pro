package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BomVariantSaveReqVO {
    private Long id;
    @NotNull private Long projectId;
    /** 可空；为空时自动归入项目的“默认分组”。 */
    private Long groupId;
    /** 选择明确分组时传入；默认分组由服务端自动确定当前版本。 */
    private Long groupVersionId;
    @NotBlank @Size(max = 100) private String name;
    private String description;
    @NotNull private Boolean defaultVariant;
    @NotNull private Integer sort;
}

