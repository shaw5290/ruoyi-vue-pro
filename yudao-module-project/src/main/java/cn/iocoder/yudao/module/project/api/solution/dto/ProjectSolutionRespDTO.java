package cn.iocoder.yudao.module.project.api.solution.dto;

import lombok.Data;

@Data
public class ProjectSolutionRespDTO {
    private Long id;
    private Long projectId;
    private String name;
    private Boolean adopted;
}
