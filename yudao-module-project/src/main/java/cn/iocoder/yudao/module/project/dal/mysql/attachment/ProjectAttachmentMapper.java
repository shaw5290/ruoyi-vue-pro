package cn.iocoder.yudao.module.project.dal.mysql.attachment;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.dal.dataobject.attachment.ProjectAttachmentDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectAttachmentMapper extends BaseMapperX<ProjectAttachmentDO> {

    default List<ProjectAttachmentDO> selectListByOwner(Long projectId, Long bomGroupId) {
        LambdaQueryWrapperX<ProjectAttachmentDO> query = new LambdaQueryWrapperX<ProjectAttachmentDO>()
                .eq(ProjectAttachmentDO::getProjectId, projectId)
                .eqIfPresent(ProjectAttachmentDO::getBomGroupId, bomGroupId);
        return selectList(query.orderByAsc(ProjectAttachmentDO::getSort)
                .orderByDesc(ProjectAttachmentDO::getId));
    }

    default int moveToGroup(Long bomGroupId, Long targetBomGroupId) {
        return update(null, new LambdaUpdateWrapper<ProjectAttachmentDO>()
                .eq(ProjectAttachmentDO::getBomGroupId, bomGroupId)
                .set(ProjectAttachmentDO::getBomGroupId, targetBomGroupId));
    }

}
