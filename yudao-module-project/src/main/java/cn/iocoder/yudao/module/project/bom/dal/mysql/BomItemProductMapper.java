package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomItemProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BomItemProductMapper extends BaseMapperX<BomItemProductDO> {
    default List<BomItemProductDO> selectListByItemId(Long bomItemId) {
        return selectList(new LambdaQueryWrapperX<BomItemProductDO>()
                .eq(BomItemProductDO::getBomItemId, bomItemId)
                .orderByAsc(BomItemProductDO::getSort).orderByAsc(BomItemProductDO::getId));
    }
    default int deleteByItemId(Long bomItemId) {
        return delete(new LambdaQueryWrapperX<BomItemProductDO>().eq(BomItemProductDO::getBomItemId, bomItemId));
    }
    default int deleteByItemIdAndProductId(Long bomItemId, Long productId) {
        return delete(new LambdaQueryWrapperX<BomItemProductDO>()
                .eq(BomItemProductDO::getBomItemId, bomItemId)
                .eq(BomItemProductDO::getProductId, productId));
    }
    default long selectCountByProductId(Long productId) {
        return selectCount(new LambdaQueryWrapperX<BomItemProductDO>().eq(BomItemProductDO::getProductId, productId));
    }
}
