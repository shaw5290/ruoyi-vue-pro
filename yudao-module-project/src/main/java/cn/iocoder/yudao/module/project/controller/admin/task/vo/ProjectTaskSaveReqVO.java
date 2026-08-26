package cn.iocoder.yudao.module.project.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "管理后台 - 项目任务新增/修改 Request VO")
@Data
public class ProjectTaskSaveReqVO {

    private Long id;

    @NotNull(message = "项目 ID 不能为空")
    private Long projectId;

    @Size(max = 30, message = "任务阶段长度不能超过 30 个字符")
    private String phase;

    @NotEmpty(message = "任务标题不能为空")
    @Size(max = 200, message = "任务标题长度不能超过 200 个字符")
    private String title;

    private Long assigneeUserId;

    @Size(max = 100, message = "负责人名称长度不能超过 100 个字符")
    private String assigneeName;

    private LocalDate dueDate;

    @Min(value = 0, message = "任务优先级不能小于 0")
    @Max(value = 2, message = "任务优先级不能大于 2")
    private Integer priority;

    @Min(value = 0, message = "任务状态不能小于 0")
    @Max(value = 2, message = "任务状态不能大于 2")
    private Integer status;

    private Integer sort;
    private String remark;

}
