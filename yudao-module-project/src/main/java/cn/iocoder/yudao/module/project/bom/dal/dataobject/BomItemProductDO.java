package cn.iocoder.yudao.module.project.bom.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

/** BOM 零件与商品的逻辑关联。 */
@TableName("bom_item_product")
@KeySequence("bom_item_product_seq")
@Data @EqualsAndHashCode(callSuper = true) @Builder @NoArgsConstructor @AllArgsConstructor
public class BomItemProductDO extends TenantBaseDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long bomItemId;
    private Long productId;
    private Integer sort;
}
