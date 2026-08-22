package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProjectProductSaveReqVO {
    private Long id;
    @NotNull private Long projectId;
    @NotBlank @Size(max = 200) private String name;
    @Size(max = 200) private String merchant;
    @DecimalMin("0") private BigDecimal price;
    @Size(max = 5000) private String purchaseUrl;
    private List<@Size(max = 1000) String> imageUrls;
    @Size(max = 10000) private String remark;
    private Boolean enabled;
}
