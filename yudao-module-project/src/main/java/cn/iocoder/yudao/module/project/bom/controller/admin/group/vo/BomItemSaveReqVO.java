package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BomItemSaveReqVO {
    private Long id;
    /** 所属项目。新增物料时必传，用于分组留空时归入默认分组。 */
    private Long projectId;
    /** 所属 BOM 分组；可空，空值表示默认分组。 */
    private Long groupId;
    /** 历史兼容字段。当前页面按分组维护物料，不再暴露 BOM 容器层。 */
    private Long bomVariantId;
    @NotBlank @Size(max = 200) private String itemNameSnapshot;
    @Size(max = 100) private String itemCodeSnapshot;
    @Size(max = 500) private String specificationSnapshot;
    @NotBlank @Size(max = 50) private String unitSnapshot;
    @NotNull @DecimalMin("0.000001") private BigDecimal quantity;
    @DecimalMin("0") private BigDecimal plannedUnitPrice;
    private String purpose;
    private String supplier;
    private String purchaseUrl;
    private String itemSource;
    private Long wmsItemSkuId;
    private String bindingStatus;
    @NotNull private Integer sort;
    private String remark;
}

