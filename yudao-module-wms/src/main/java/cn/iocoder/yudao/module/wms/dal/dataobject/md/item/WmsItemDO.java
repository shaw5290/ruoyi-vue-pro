package cn.iocoder.yudao.module.wms.dal.dataobject.md.item;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.dromara.autotable.annotation.ColumnType;

import java.util.List;

/**
 * WMS 商品 DO
 *
 * @author 芋道源码
 */
@TableName(value = "wms_item", autoResultMap = true)
@KeySequence("wms_item_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WmsItemDO extends TenantBaseDO {

    /**
     * 主键编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品编号
     */
    private String code;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 单位
     */
    private String unit;
    /**
     * 商品分类编号
     *
     * 关联 {@link WmsItemCategoryDO#getId()}
     */
    private Long categoryId;
    /**
     * 商品品牌编号
     *
     * 关联 {@link WmsItemBrandDO#getId()}
     */
    private Long brandId;
    /** 供应商/商家 */
    private String merchant;
    /** 采购链接 */
    @ColumnType("text")
    private String purchaseUrl;
    /** 商品展示图片 */
    @ColumnType("text")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> imageUrls;
    /**
     * 备注
     */
    private String remark;

}
