package cn.iocoder.yudao.module.project.service.project;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomGroupDO;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomItemDO;
import cn.iocoder.yudao.module.project.bom.service.BomLibraryService;
import cn.iocoder.yudao.module.project.controller.admin.project.vo.ProjectOverviewRespVO;
import cn.iocoder.yudao.module.project.controller.admin.project.vo.ProjectPageReqVO;
import cn.iocoder.yudao.module.project.controller.admin.project.vo.ProjectSaveReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.project.ProjectDO;
import cn.iocoder.yudao.module.project.dal.mysql.project.ProjectMapper;
import cn.iocoder.yudao.module.project.dal.mysql.solution.ProjectSolutionMapper;
import cn.iocoder.yudao.module.project.dal.mysql.task.ProjectTaskMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @Resource
    private ProjectTaskMapper projectTaskMapper;
    @Resource
    private BomLibraryService bomLibraryService;

    @Override
    public Long createProject(ProjectSaveReqVO createReqVO) {
        validateProjectCodeUnique(null, createReqVO.getCode());
        ProjectDO project = BeanUtils.toBean(createReqVO, ProjectDO.class);
        if (project.getPriority() == null) {
            project.setPriority(1);
        }
        if (project.getStage() == null) {
            project.setStage(0);
        }
        if (project.getBudget() == null) {
            project.setBudget(BigDecimal.ZERO);
        }
        if (project.getProgress() == null) {
            project.setProgress(0);
        }
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
        if (projectTaskMapper.selectCountByProjectId(id) > 0) {
            throw exception(PROJECT_HAS_TASK);
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

    @Override
    public ProjectOverviewRespVO getProjectOverview(Long id) {
        validateProjectExists(id);
        long taskCount = projectTaskMapper.selectCountByProjectId(id);
        long completedTaskCount = projectTaskMapper.selectCompletedCount(id);

        Set<Long> itemIds = new HashSet<>();
        BigDecimal plannedBomCost = BigDecimal.ZERO;
        for (BomGroupDO group : bomLibraryService.getGroupList(id)) {
            for (BomItemDO item : bomLibraryService.getItemListByGroup(group.getId())) {
                if (item.getId() == null || !itemIds.add(item.getId())) {
                    continue;
                }
                BigDecimal quantity = item.getQuantity() == null ? BigDecimal.ZERO : item.getQuantity();
                BigDecimal unitPrice = item.getPlannedUnitPrice() == null
                        ? BigDecimal.ZERO : item.getPlannedUnitPrice();
                plannedBomCost = plannedBomCost.add(quantity.multiply(unitPrice));
            }
        }
        return ProjectOverviewRespVO.builder()
                .projectId(id)
                .taskCount(taskCount)
                .completedTaskCount(completedTaskCount)
                .openTaskCount(taskCount - completedTaskCount)
                .overdueTaskCount(projectTaskMapper.selectOverdueCount(id, LocalDate.now()))
                .highPriorityOpenTaskCount(projectTaskMapper.selectHighPriorityOpenCount(id))
                .bomItemCount((long) itemIds.size())
                .plannedBomCost(plannedBomCost)
                .build();
    }

}
