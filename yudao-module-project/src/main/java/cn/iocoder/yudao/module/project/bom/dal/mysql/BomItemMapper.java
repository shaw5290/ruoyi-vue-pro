package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomItemDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BomItemMapper extends BaseMapperX<BomItemDO> {
    default List<BomItemDO> selectListByVariantId(Long variantId) {
        return selectList(new LambdaQueryWrapperX<BomItemDO>()
                .eq(BomItemDO::getBomVariantId, variantId)
                .orderByAsc(BomItemDO::getSort).orderByAsc(BomItemDO::getId));
    }
}

