package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BomWmsBomBindingSaveReqVO {
    @NotNull private Long projectBomId;
    @NotNull @Valid private List<Item> bindings;

    @Data
    public static class Item {
        @NotNull private Long wmsBomId;
        private String wmsBomCodeSnapshot;
        private String wmsBomNameSnapshot;
        @NotNull @DecimalMin("0.000001") private BigDecimal quantity;
        @NotNull private Integer sort;
    }
}

