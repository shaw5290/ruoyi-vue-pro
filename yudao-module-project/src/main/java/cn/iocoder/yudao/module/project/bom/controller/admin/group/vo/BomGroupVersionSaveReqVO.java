package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BomGroupVersionSaveReqVO {
    private Long id;
    @NotNull private Long groupId;
    @NotBlank @Size(max = 50) private String versionNo;
    private String description;
    @NotNull private Boolean published;
}

