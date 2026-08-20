package cn.iocoder.yudao.module.project.api.project;

import cn.iocoder.yudao.module.project.api.project.dto.ProjectRespDTO;

/**
 * 项目模块公开 API，供 BOM、拼团等可插拔模块进行逻辑关联校验。
 */
public interface ProjectApi {

    ProjectRespDTO getProject(Long id);

    ProjectRespDTO validateProjectExists(Long id);

}
