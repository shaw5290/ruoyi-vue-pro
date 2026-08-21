package cn.iocoder.yudao.module.project.service.solution.dto;

import lombok.Data;

/** 项目方案应用服务保存参数，避免 Service 反向依赖 Controller VO。 */
@Data
public class ProjectSolutionSaveReqDTO {
    private Long id;
    private Long projectId;
    private String name;
    private String description;
    private Integer sort;
    private String remark;
}
