package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomVariantDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BomVariantMapper extends BaseMapperX<BomVariantDO> {
    default List<BomVariantDO> selectListByVersionId(Long versionId) {
        return selectList(new LambdaQueryWrapperX<BomVariantDO>()
                .eq(BomVariantDO::getGroupVersionId, versionId)
                .orderByAsc(BomVariantDO::getSort).orderByDesc(BomVariantDO::getId));
    }
    default BomVariantDO selectDefaultByVersionId(Long versionId) {
        return selectOne(new LambdaQueryWrapperX<BomVariantDO>()
                .eq(BomVariantDO::getGroupVersionId, versionId)
                .eq(BomVariantDO::getDefaultVariant, true));
    }
}

