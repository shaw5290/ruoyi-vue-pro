package cn.iocoder.yudao.module.project.api.solution;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.api.solution.dto.ProjectSolutionRespDTO;
import cn.iocoder.yudao.module.project.service.solution.ProjectSolutionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ProjectSolutionApiImpl implements ProjectSolutionApi {
    @Resource private ProjectSolutionService projectSolutionService;

    @Override
    public ProjectSolutionRespDTO validateProjectSolutionExists(Long id) {
        return BeanUtils.toBean(projectSolutionService.validateProjectSolutionExists(id), ProjectSolutionRespDTO.class);
    }
}
