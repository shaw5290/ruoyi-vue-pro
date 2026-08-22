package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomItemImageDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BomItemImageMapper extends BaseMapperX<BomItemImageDO> {

    default List<BomItemImageDO> selectListByBomItemId(Long bomItemId) {
        return selectList(new LambdaQueryWrapperX<BomItemImageDO>()
                .eq(BomItemImageDO::getBomItemId, bomItemId)
                .orderByDesc(BomItemImageDO::getPrimaryImage)
                .orderByAsc(BomItemImageDO::getSort)
                .orderByAsc(BomItemImageDO::getId));
    }

    default int clearPrimary(Long bomItemId) {
        return update(new BomItemImageDO().setPrimaryImage(false),
                new LambdaUpdateWrapper<BomItemImageDO>()
                        .eq(BomItemImageDO::getBomItemId, bomItemId));
    }

    default int deleteByBomItemId(Long bomItemId) {
        return delete(new LambdaQueryWrapperX<BomItemImageDO>()
                .eq(BomItemImageDO::getBomItemId, bomItemId));
    }

}
