package cn.iocoder.yudao.module.project.api.solution;

import cn.iocoder.yudao.module.project.api.solution.dto.ProjectSolutionRespDTO;

public interface ProjectSolutionApi {
    ProjectSolutionRespDTO validateProjectSolutionExists(Long id);
}
