package cn.iocoder.yudao.module.project.bom.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import java.math.BigDecimal;

/** 项目 BOM 与 WMS BOM 的多对多逻辑关联及显示快照。 */
@TableName("bom_wms_bom_binding")
@KeySequence("bom_wms_bom_binding_seq")
@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
@Builder @NoArgsConstructor @AllArgsConstructor
public class BomWmsBomBindingDO extends TenantBaseDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 项目 BOM，关联 BomVariantDO.id */
    private Long projectBomId;
    /** WMS BOM 逻辑 ID；不建立物理外键 */
    private Long wmsBomId;
    /** WMS 不可用时用于降级展示 */
    private String wmsBomCodeSnapshot;
    private String wmsBomNameSnapshot;
    /** 每套项目 BOM 使用该 WMS BOM 的数量/倍率 */
    private BigDecimal quantity;
    /** BOUND / DISABLED / UNAVAILABLE */
    private String bindingStatus;
    private Integer sort;
}

