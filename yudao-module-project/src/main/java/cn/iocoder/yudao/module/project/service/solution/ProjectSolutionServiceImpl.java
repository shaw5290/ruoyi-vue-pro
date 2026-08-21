package cn.iocoder.yudao.module.project.service.solution;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.dal.dataobject.solution.ProjectSolutionDO;
import cn.iocoder.yudao.module.project.dal.mysql.solution.ProjectSolutionMapper;
import cn.iocoder.yudao.module.project.service.project.ProjectService;
import cn.iocoder.yudao.module.project.service.solution.dto.ProjectSolutionSaveReqDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

@Service
@Validated
public class ProjectSolutionServiceImpl implements ProjectSolutionService {

    @Resource
    private ProjectSolutionMapper projectSolutionMapper;
    @Resource
    private ProjectService projectService;

    @Override
    public Long createProjectSolution(ProjectSolutionSaveReqDTO reqDTO) {
        projectService.validateProjectExists(reqDTO.getProjectId());
        validateNameUnique(null, reqDTO.getProjectId(), reqDTO.getName());
        ProjectSolutionDO solution = BeanUtils.toBean(reqDTO, ProjectSolutionDO.class)
                .setAdopted(false);
        projectSolutionMapper.insert(solution);
        return solution.getId();
    }

    @Override
    public void updateProjectSolution(ProjectSolutionSaveReqDTO reqDTO) {
        ProjectSolutionDO current = validateExists(reqDTO.getId());
        projectService.validateProjectExists(reqDTO.getProjectId());
        validateNameUnique(reqDTO.getId(), reqDTO.getProjectId(), reqDTO.getName());
        ProjectSolutionDO updateObj = BeanUtils.toBean(reqDTO, ProjectSolutionDO.class)
                .setAdopted(current.getAdopted());
        projectSolutionMapper.updateById(updateObj);
    }

    private void validateNameUnique(Long id, Long projectId, String name) {
        ProjectSolutionDO solution = projectSolutionMapper.selectByProjectIdAndName(projectId, name);
        if (solution != null && ObjUtil.notEqual(solution.getId(), id)) {
            throw exception(PROJECT_SOLUTION_NAME_DUPLICATE);
        }
    }

    @Override
    public void deleteProjectSolution(Long id) {
        validateExists(id);
        projectSolutionMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adoptProjectSolution(Long id) {
        ProjectSolutionDO solution = validateExists(id);
        List<ProjectSolutionDO> solutions = projectSolutionMapper.selectListByProjectId(solution.getProjectId());
        for (ProjectSolutionDO item : solutions) {
            boolean adopted = item.getId().equals(id);
            if (!ObjUtil.equal(item.getAdopted(), adopted)) {
                projectSolutionMapper.updateById(new ProjectSolutionDO().setId(item.getId()).setAdopted(adopted));
            }
        }
    }

    private ProjectSolutionDO validateExists(Long id) {
        ProjectSolutionDO solution = projectSolutionMapper.selectById(id);
        if (solution == null) {
            throw exception(PROJECT_SOLUTION_NOT_EXISTS);
        }
        return solution;
    }

    @Override
    public ProjectSolutionDO getProjectSolution(Long id) {
        return projectSolutionMapper.selectById(id);
    }

    @Override
    public ProjectSolutionDO validateProjectSolutionExists(Long id) {
        return validateExists(id);
    }

    @Override
    public List<ProjectSolutionDO> getProjectSolutionList(Long projectId) {
        projectService.validateProjectExists(projectId);
        return projectSolutionMapper.selectListByProjectId(projectId);
    }

}
