package cn.iocoder.yudao.module.project.service.solution;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.controller.admin.solution.vo.ProjectSolutionSaveReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.solution.ProjectSolutionDO;
import cn.iocoder.yudao.module.project.dal.mysql.solution.ProjectSolutionMapper;
import cn.iocoder.yudao.module.project.service.project.ProjectService;
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
    public Long createProjectSolution(ProjectSolutionSaveReqVO reqVO) {
        projectService.validateProjectExists(reqVO.getProjectId());
        validateNameUnique(null, reqVO.getProjectId(), reqVO.getName());
        ProjectSolutionDO solution = BeanUtils.toBean(reqVO, ProjectSolutionDO.class)
                .setAdopted(false);
        projectSolutionMapper.insert(solution);
        return solution.getId();
    }

    @Override
    public void updateProjectSolution(ProjectSolutionSaveReqVO reqVO) {
        ProjectSolutionDO current = validateExists(reqVO.getId());
        projectService.validateProjectExists(reqVO.getProjectId());
        validateNameUnique(reqVO.getId(), reqVO.getProjectId(), reqVO.getName());
        ProjectSolutionDO updateObj = BeanUtils.toBean(reqVO, ProjectSolutionDO.class)
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
    public List<ProjectSolutionDO> getProjectSolutionList(Long projectId) {
        projectService.validateProjectExists(projectId);
        return projectSolutionMapper.selectListByProjectId(projectId);
    }

}
