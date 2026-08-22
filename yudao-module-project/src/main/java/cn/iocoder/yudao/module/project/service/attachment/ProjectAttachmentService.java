package cn.iocoder.yudao.module.project.service.attachment;

import cn.iocoder.yudao.module.project.controller.admin.attachment.vo.ProjectAttachmentCreateReqVO;
import cn.iocoder.yudao.module.project.controller.admin.attachment.vo.ProjectAttachmentUpdateReqVO;
import cn.iocoder.yudao.module.project.dal.dataobject.attachment.ProjectAttachmentDO;

import java.util.List;

public interface ProjectAttachmentService {

    Long createAttachment(ProjectAttachmentCreateReqVO reqVO);

    void updateAttachmentRemark(ProjectAttachmentUpdateReqVO reqVO);

    void deleteAttachment(Long id);

    List<ProjectAttachmentDO> getAttachmentList(Long projectId, Long bomGroupId);

}
