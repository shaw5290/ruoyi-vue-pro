package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomGroupVersionDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BomGroupVersionMapper extends BaseMapperX<BomGroupVersionDO> {
    default List<BomGroupVersionDO> selectListByGroupId(Long groupId) {
        return selectList(new LambdaQueryWrapperX<BomGroupVersionDO>()
                .eq(BomGroupVersionDO::getGroupId, groupId).orderByDesc(BomGroupVersionDO::getId));
    }
}

