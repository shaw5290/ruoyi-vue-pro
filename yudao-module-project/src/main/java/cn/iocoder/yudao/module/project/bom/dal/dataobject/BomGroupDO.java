package cn.iocoder.yudao.module.project.bom.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

@TableName("bom_group")
@KeySequence("bom_group_seq")
@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
@Builder @NoArgsConstructor @AllArgsConstructor
public class BomGroupDO extends TenantBaseDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long projectId;
    /** 父分组；为空表示根分组，支持任意层级嵌套。 */
    private Long parentId;
    private String code;
    private String name;
    private String category;
    private String description;
    private Integer sort;
    private Boolean enabled;
}

