package cn.iocoder.yudao.module.project.bom.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * 项目 BOM 物料需求快照。
 *
 * <p>与 MES/WMS 物料表中的产品 BOM 完全独立；可选 SKU ID 仅作逻辑绑定。</p>
 */
@TableName("bom_item")
@KeySequence("bom_item_seq")
@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
@Builder @NoArgsConstructor @AllArgsConstructor
public class BomItemDO extends TenantBaseDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long bomVariantId;
    private String itemNameSnapshot;
    private String itemCodeSnapshot;
    private String skuCodeSnapshot;
    private String specificationSnapshot;
    private String unitSnapshot;
    private BigDecimal quantity;
    private BigDecimal plannedUnitPrice;
    private String purpose;
    private String supplier;
    private String purchaseUrl;
    /** 物料来源：NONE / WMS / ERP / EXTERNAL */
    private String itemSource;
    /** 可空；仅在 WMS 可用且完成绑定时保存 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long wmsItemSkuId;
    /** UNBOUND / BOUND / DISABLED / UNAVAILABLE */
    private String bindingStatus;
    private Integer sort;
    private String remark;
}

