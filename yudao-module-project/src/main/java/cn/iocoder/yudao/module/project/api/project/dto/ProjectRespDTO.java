package cn.iocoder.yudao.module.project.api.project.dto;

import lombok.Data;

/**
 * 项目模块对其他业务模块公开的项目 DTO
 */
@Data
public class ProjectRespDTO {

    private Long id;
    private String code;
    private String name;
    private Integer visibility;
    private Integer status;

}
