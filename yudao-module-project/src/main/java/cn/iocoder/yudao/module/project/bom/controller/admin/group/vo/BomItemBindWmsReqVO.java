package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import jakarta.validation.constraints.*;
import lombok.Data;

/** 选择 WMS 标准物料后，用标准字段重置项目 BOM 快照。 */
@Data
public class BomItemBindWmsReqVO {
    @NotNull private Long bomItemId;
    @NotNull private Long wmsItemSkuId;
    @NotBlank private String itemCode;
    @NotBlank private String skuCode;
    @NotBlank private String itemName;
    @NotBlank private String specification;
    @NotBlank private String unit;
}
