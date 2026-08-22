package cn.iocoder.yudao.module.project.bom.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import org.dromara.autotable.annotation.ColumnType;

import java.math.BigDecimal;
import java.util.List;

/** 项目商品目录。商品是零件的采购信息，不属于 WMS 库存物料。 */
@TableName(value = "project_product", autoResultMap = true)
@KeySequence("project_product_seq")
@Data @EqualsAndHashCode(callSuper = true) @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectProductDO extends TenantBaseDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String name;
    private String merchant;
    private BigDecimal price;
    @ColumnType("text")
    private String purchaseUrl;
    @ColumnType("text")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> imageUrls;
    @ColumnType("text")
    private String remark;
    private Boolean enabled;
}
