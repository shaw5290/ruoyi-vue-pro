package cn.iocoder.yudao.module.project.controller.admin.solution.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 项目方案 Response VO")
@Data
public class ProjectSolutionRespVO {

    private Long id;
    private Long projectId;
    private String name;
    private String description;
    private Boolean adopted;
    private Integer sort;
    private String remark;
    private LocalDateTime createTime;

}
