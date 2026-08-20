package cn.iocoder.yudao.module.project.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 项目 Response VO")
@Data
public class ProjectRespVO {

    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer visibility;
    private Integer status;
    private Long ownerUserId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String remark;
    private LocalDateTime createTime;

}
