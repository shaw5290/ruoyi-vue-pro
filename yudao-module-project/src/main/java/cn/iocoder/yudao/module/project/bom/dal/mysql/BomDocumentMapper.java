package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomDocumentDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BomDocumentMapper extends BaseMapperX<BomDocumentDO> {

    default List<BomDocumentDO> selectListByOwner(Long projectId, Long bomGroupId) {
        return selectList(new LambdaQueryWrapperX<BomDocumentDO>()
                .eq(BomDocumentDO::getProjectId, projectId)
                .eqIfPresent(BomDocumentDO::getBomGroupId, bomGroupId)
                .orderByAsc(BomDocumentDO::getSort)
                .orderByDesc(BomDocumentDO::getId));
    }

    default int moveToGroup(Long bomGroupId, Long targetBomGroupId) {
        return update(null, new LambdaUpdateWrapper<BomDocumentDO>()
                .eq(BomDocumentDO::getBomGroupId, bomGroupId)
                .set(BomDocumentDO::getBomGroupId, targetBomGroupId));
    }

}
