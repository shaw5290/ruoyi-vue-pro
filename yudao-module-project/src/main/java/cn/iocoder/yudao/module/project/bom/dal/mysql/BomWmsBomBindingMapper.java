package cn.iocoder.yudao.module.project.bom.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomWmsBomBindingDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BomWmsBomBindingMapper extends BaseMapperX<BomWmsBomBindingDO> {
    default List<BomWmsBomBindingDO> selectListByProjectBomId(Long projectBomId) {
        return selectList(new LambdaQueryWrapperX<BomWmsBomBindingDO>()
                .eq(BomWmsBomBindingDO::getProjectBomId, projectBomId)
                .orderByAsc(BomWmsBomBindingDO::getSort));
    }
    default void deleteByProjectBomId(Long projectBomId) {
        delete(BomWmsBomBindingDO::getProjectBomId, projectBomId);
    }
}

