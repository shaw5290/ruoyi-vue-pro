package cn.iocoder.yudao.module.project.bom.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.math.BigDecimal;

@TableName("bom_solution_selection")
@KeySequence("bom_solution_selection_seq")
@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
@Builder @NoArgsConstructor @AllArgsConstructor
public class BomSolutionSelectionDO extends TenantBaseDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long projectSolutionId;
    private Long bomGroupId;
    private Long bomGroupVersionId;
    private Long bomVariantId;
    private BigDecimal quantity;
    private String parameterOverrides;
    private Integer sortOrder;
}

