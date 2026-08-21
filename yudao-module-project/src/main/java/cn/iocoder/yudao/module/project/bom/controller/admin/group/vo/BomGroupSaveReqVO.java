package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BomGroupSaveReqVO {
    private Long id;
    @NotNull private Long projectId;
    private Long parentId;
    @NotBlank @Size(max = 50) private String code;
    @NotBlank @Size(max = 100) private String name;
    @Size(max = 100) private String category;
    private String description;
    @NotNull private Integer sort;
    @NotNull private Boolean enabled;
}

