package cn.iocoder.yudao.module.project.bom.controller.admin.selection.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BomSelectionSaveReqVO {
    @NotNull private Long projectSolutionId;
    @NotNull @Valid private List<Item> selections;

    @Data
    public static class Item {
        @NotNull private Long bomGroupId;
        @NotNull private Long bomGroupVersionId;
        private Long bomVariantId;
        @NotNull @DecimalMin("0.000001") private BigDecimal quantity;
        private String parameterOverrides;
        @NotNull private Integer sortOrder;
    }
}

