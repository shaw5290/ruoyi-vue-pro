package cn.iocoder.yudao.module.project.service.project;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.controller.admin.project.vo.ProjectPageReqVO;
import cn.iocoder.yudao.module.project.controller.admin.project.vo.ProjectSaveReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.project.ProjectDO;
import cn.iocoder.yudao.module.project.dal.mysql.project.ProjectMapper;
import cn.iocoder.yudao.module.project.dal.mysql.solution.ProjectSolutionMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

@Service
@Validated
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private ProjectSolutionMapper projectSolutionMapper;

    @Override
    public Long createProject(ProjectSaveReqVO createReqVO) {
        validateProjectCodeUnique(null, createReqVO.getCode());
        ProjectDO project = BeanUtils.toBean(createReqVO, ProjectDO.class);
        if (project.getOwnerUserId() == null) {
            project.setOwnerUserId(getLoginUserId());
        }
        projectMapper.insert(project);
        return project.getId();
    }

    @Override
    public void updateProject(ProjectSaveReqVO updateReqVO) {
        validateProjectExists(updateReqVO.getId());
        validateProjectCodeUnique(updateReqVO.getId(), updateReqVO.getCode());
        projectMapper.updateById(BeanUtils.toBean(updateReqVO, ProjectDO.class));
    }

    private void validateProjectCodeUnique(Long id, String code) {
        ProjectDO project = projectMapper.selectByCode(code);
        if (project != null && ObjUtil.notEqual(project.getId(), id)) {
            throw exception(PROJECT_CODE_DUPLICATE);
        }
    }

    @Override
    public void deleteProject(Long id) {
        validateProjectExists(id);
        if (projectSolutionMapper.selectCountByProjectId(id) > 0) {
            throw exception(PROJECT_HAS_SOLUTION);
        }
        projectMapper.deleteById(id);
    }

    @Override
    public ProjectDO getProject(Long id) {
        return projectMapper.selectById(id);
    }

    @Override
    public ProjectDO validateProjectExists(Long id) {
        ProjectDO project = projectMapper.selectById(id);
        if (project == null) {
            throw exception(PROJECT_NOT_EXISTS);
        }
        return project;
    }

    @Override
    public PageResult<ProjectDO> getProjectPage(ProjectPageReqVO pageReqVO) {
        return projectMapper.selectPage(pageReqVO);
    }

}
