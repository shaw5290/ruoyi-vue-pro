package cn.iocoder.yudao.module.project.service.project;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.project.controller.admin.project.vo.ProjectPageReqVO;
import cn.iocoder.yudao.module.project.controller.admin.project.vo.ProjectOverviewRespVO;
import cn.iocoder.yudao.module.project.controller.admin.project.vo.ProjectSaveReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.project.ProjectDO;

public interface ProjectService {

    Long createProject(ProjectSaveReqVO createReqVO);
    void updateProject(ProjectSaveReqVO updateReqVO);
    void deleteProject(Long id);
    ProjectDO getProject(Long id);
    ProjectDO validateProjectExists(Long id);
    PageResult<ProjectDO> getProjectPage(ProjectPageReqVO pageReqVO);
    ProjectOverviewRespVO getProjectOverview(Long id);

}
