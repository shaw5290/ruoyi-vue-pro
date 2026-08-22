package cn.iocoder.yudao.module.project.service.attachment;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.project.api.project.ProjectApi;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomGroupDO;
import cn.iocoder.yudao.module.project.bom.dal.mysql.BomGroupMapper;
import cn.iocoder.yudao.module.project.controller.admin.attachment.vo.ProjectAttachmentCreateReqVO;
import cn.iocoder.yudao.module.project.controller.admin.attachment.vo.ProjectAttachmentUpdateReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.attachment.ProjectAttachmentDO;
import cn.iocoder.yudao.module.project.dal.mysql.attachment.ProjectAttachmentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.project.enums.ErrorCodeConstants.*;

@Service
@Validated
public class ProjectAttachmentServiceImpl implements ProjectAttachmentService {

    @Resource
    private ProjectAttachmentMapper attachmentMapper;
    @Resource
    private BomGroupMapper bomGroupMapper;
    @Resource
    private ProjectApi projectApi;

    @Override
    public Long createAttachment(ProjectAttachmentCreateReqVO reqVO) {
        validateOwner(reqVO.getProjectId(), reqVO.getBomGroupId());
        ProjectAttachmentDO attachment = BeanUtils.toBean(reqVO, ProjectAttachmentDO.class);
        if (attachment.getSort() == null) {
            attachment.setSort(0);
        }
        attachmentMapper.insert(attachment);
        return attachment.getId();
    }

    @Override
    public void updateAttachmentRemark(ProjectAttachmentUpdateReqVO reqVO) {
        if (attachmentMapper.selectById(reqVO.getId()) == null) {
            throw exception(PROJECT_ATTACHMENT_NOT_EXISTS);
        }
        attachmentMapper.updateById(ProjectAttachmentDO.builder()
                .id(reqVO.getId())
                .remark(reqVO.getRemark())
                .build());
    }

    @Override
    public void deleteAttachment(Long id) {
        if (attachmentMapper.selectById(id) == null) {
            throw exception(PROJECT_ATTACHMENT_NOT_EXISTS);
        }
        // 只删除项目侧的附件关系，不删除基础设施文件，避免共享 URL 被误删。
        attachmentMapper.deleteById(id);
    }

    @Override
    public List<ProjectAttachmentDO> getAttachmentList(Long projectId, Long bomGroupId) {
        validateOwner(projectId, bomGroupId);
        List<ProjectAttachmentDO> attachments = attachmentMapper.selectListByOwner(projectId, bomGroupId);
        for (ProjectAttachmentDO attachment : attachments) {
            BomGroupDO group = bomGroupMapper.selectById(attachment.getBomGroupId());
            attachment.setBomGroupName(group == null ? "已删除分组" : group.getName());
        }
        return attachments;
    }

    private void validateOwner(Long projectId, Long bomGroupId) {
        projectApi.validateProjectExists(projectId);
        if (bomGroupId == null) {
            // bomGroupId 为空只用于项目附件汇总查询，创建时由请求校验保证必填。
            return;
        }
        BomGroupDO group = bomGroupMapper.selectById(bomGroupId);
        if (group == null) {
            throw exception(PROJECT_ATTACHMENT_BOM_GROUP_NOT_EXISTS);
        }
        if (!projectId.equals(group.getProjectId())) {
            throw exception(PROJECT_ATTACHMENT_OWNER_MISMATCH);
        }
    }

}
