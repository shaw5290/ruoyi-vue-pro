package cn.iocoder.yudao.module.project.service.solution;

import cn.iocoder.yudao.module.project.dal.dataobject.solution.ProjectSolutionDO;
import cn.iocoder.yudao.module.project.service.solution.dto.ProjectSolutionSaveReqDTO;

import java.util.List;

public interface ProjectSolutionService {

    Long createProjectSolution(ProjectSolutionSaveReqDTO reqDTO);
    void updateProjectSolution(ProjectSolutionSaveReqDTO reqDTO);
    void deleteProjectSolution(Long id);
    void adoptProjectSolution(Long id);
    ProjectSolutionDO getProjectSolution(Long id);

    ProjectSolutionDO validateProjectSolutionExists(Long id);
    List<ProjectSolutionDO> getProjectSolutionList(Long projectId);

}
