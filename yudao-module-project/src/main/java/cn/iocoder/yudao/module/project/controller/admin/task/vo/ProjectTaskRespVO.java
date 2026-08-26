package cn.iocoder.yudao.module.project.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 项目任务 Response VO")
@Data
public class ProjectTaskRespVO {

    private Long id;
    private Long projectId;
    private String phase;
    private String title;
    private Long assigneeUserId;
    private String assigneeName;
    private LocalDate dueDate;
    private Integer priority;
    private Integer status;
    private Integer sort;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
