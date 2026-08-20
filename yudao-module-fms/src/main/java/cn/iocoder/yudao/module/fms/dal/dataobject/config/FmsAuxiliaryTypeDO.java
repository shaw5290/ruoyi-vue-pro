package cn.iocoder.yudao.module.fms.dal.dataobject.config;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * FMS 辅助核算类别 DO
 *
 * @author 芋道源码
 */
@TableName("fms_auxiliary_type")
@KeySequence("fms_auxiliary_type_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FmsAuxiliaryTypeDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 辅助核算名称
     */
    private String name;
    /**
     * 是否系统预置
     */
    private Boolean systemPreset;
    /**
     * 账套编号
     *
     * 关联 {@link cn.iocoder.yudao.module.fms.dal.dataobject.config.FmsAccountSetDO#getId()}
     */
    private Long accountSetId;
    /**
     * 类型
     *
     * 枚举 {@link cn.iocoder.yudao.module.fms.enums.config.FmsAuxiliaryTypeEnum}
     */
    private Integer type;

}
