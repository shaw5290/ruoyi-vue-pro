package cn.iocoder.yudao.module.project.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 项目概览统计 Response VO")
@Data
@Builder
public class ProjectOverviewRespVO {

    private Long projectId;
    private Long taskCount;
    private Long completedTaskCount;
    private Long openTaskCount;
    private Long overdueTaskCount;
    private Long highPriorityOpenTaskCount;
    private Long bomItemCount;
    private BigDecimal plannedBomCost;

}
