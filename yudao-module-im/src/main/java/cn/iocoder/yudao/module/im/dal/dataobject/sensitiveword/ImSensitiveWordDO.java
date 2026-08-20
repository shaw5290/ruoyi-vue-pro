package cn.iocoder.yudao.module.im.dal.dataobject.sensitiveword;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * IM 敏感词 DO
 *
 * @author 芋道源码
 */
@TableName("im_sensitive_word")
@KeySequence("im_sensitive_word_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImSensitiveWordDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 敏感词
     */
    private String word;
    /**
     * 状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
