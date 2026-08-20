package cn.iocoder.yudao.module.project.service.solution;

import cn.iocoder.yudao.module.project.controller.admin.solution.vo.ProjectSolutionSaveReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.solution.ProjectSolutionDO;

import java.util.List;

public interface ProjectSolutionService {

    Long createProjectSolution(ProjectSolutionSaveReqVO reqVO);
    void updateProjectSolution(ProjectSolutionSaveReqVO reqVO);
    void deleteProjectSolution(Long id);
    void adoptProjectSolution(Long id);
    ProjectSolutionDO getProjectSolution(Long id);
    List<ProjectSolutionDO> getProjectSolutionList(Long projectId);

}
