package cn.iocoder.yudao.module.project.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 项目任务状态修改 Request VO")
@Data
public class ProjectTaskStatusUpdateReqVO {

    @NotNull(message = "任务 ID 不能为空")
    private Long id;

    @NotNull(message = "项目 ID 不能为空")
    private Long projectId;

    @NotNull(message = "任务状态不能为空")
    @Min(value = 0, message = "任务状态不能小于 0")
    @Max(value = 2, message = "任务状态不能大于 2")
    private Integer status;

}
