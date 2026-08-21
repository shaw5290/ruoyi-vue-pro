package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.*;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BomItemImportExcelVO {
    @ExcelProperty("物料名称") private String itemName;
    @ExcelProperty("物料编码") private String itemCode;
    @ExcelProperty("规格型号") private String specification;
    @ExcelProperty("数量") private BigDecimal quantity;
    @ExcelProperty("单位") private String unit;
    @ExcelProperty("计划单价") private BigDecimal plannedUnitPrice;
    @ExcelProperty("用途") private String purpose;
    @ExcelProperty("供应商") private String supplier;
    @ExcelProperty("购买链接") private String purchaseUrl;
    @ExcelProperty("备注") private String remark;
}

