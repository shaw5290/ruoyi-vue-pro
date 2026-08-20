package cn.iocoder.yudao.module.project.api.project;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.api.project.dto.ProjectRespDTO;
import cn.iocoder.yudao.module.project.service.project.ProjectService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ProjectApiImpl implements ProjectApi {

    @Resource
    private ProjectService projectService;

    @Override
    public ProjectRespDTO getProject(Long id) {
        return BeanUtils.toBean(projectService.getProject(id), ProjectRespDTO.class);
    }

    @Override
    public ProjectRespDTO validateProjectExists(Long id) {
        return BeanUtils.toBean(projectService.validateProjectExists(id), ProjectRespDTO.class);
    }

}
