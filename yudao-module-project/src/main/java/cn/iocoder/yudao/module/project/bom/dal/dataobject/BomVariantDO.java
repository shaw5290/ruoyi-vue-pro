package cn.iocoder.yudao.module.project.bom.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

@TableName("bom_variant")
@KeySequence("bom_variant_seq")
@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
@Builder @NoArgsConstructor @AllArgsConstructor
public class BomVariantDO extends TenantBaseDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long groupId;
    private Long groupVersionId;
    private String name;
    private String description;
    private Boolean defaultVariant;
    private Integer sort;
}

