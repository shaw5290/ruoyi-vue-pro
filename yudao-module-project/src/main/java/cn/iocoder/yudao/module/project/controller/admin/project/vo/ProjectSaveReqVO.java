package cn.iocoder.yudao.module.project.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - 项目新增/修改 Request VO")
@Data
public class ProjectSaveReqVO {

    @Schema(description = "项目 ID", example = "1024")
    private Long id;

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PRJ-2026-001")
    @NotEmpty(message = "项目编号不能为空")
    @Size(max = 50, message = "项目编号长度不能超过 50 个字符")
    private String code;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "项目名称不能为空")
    @Size(max = 100, message = "项目名称长度不能超过 100 个字符")
    private String name;

    @Schema(description = "项目说明")
    private String description;

    @Schema(description = "项目分类", example = "机器人")
    @Size(max = 50, message = "项目分类长度不能超过 50 个字符")
    private String category;

    @Schema(description = "优先级：0 低，1 中，2 高")
    @Min(value = 0, message = "项目优先级不能小于 0")
    @Max(value = 2, message = "项目优先级不能大于 2")
    private Integer priority;

    @Schema(description = "执行阶段：0 规划，1 备料，2 制作，3 测试，4 暂停")
    @Min(value = 0, message = "项目阶段不能小于 0")
    @Max(value = 4, message = "项目阶段不能大于 4")
    private Integer stage;

    @Schema(description = "计划预算")
    @DecimalMin(value = "0", message = "计划预算不能小于 0")
    private BigDecimal budget;

    @Schema(description = "综合进度，0-100")
    @Min(value = 0, message = "项目进度不能小于 0")
    @Max(value = 100, message = "项目进度不能大于 100")
    private Integer progress;

    @Schema(description = "可见性：0 私有，1 公开", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "可见性不能为空")
    private Integer visibility;

    @Schema(description = "状态：0 进行中，1 已完成，2 已归档", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "负责人用户 ID")
    private Long ownerUserId;

    @Schema(description = "计划开始日期")
    private LocalDate startDate;

    @Schema(description = "计划结束日期")
    private LocalDate endDate;

    @Schema(description = "备注")
    private String remark;

}
