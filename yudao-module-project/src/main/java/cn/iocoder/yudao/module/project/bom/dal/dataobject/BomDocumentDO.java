package cn.iocoder.yudao.module.project.bom.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.dromara.autotable.annotation.ColumnType;

/** BOM 分组 Markdown 文档。 */
@TableName("bom_document")
@KeySequence("bom_document_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BomDocumentDO extends TenantBaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private Long bomGroupId;
    @TableField(exist = false)
    private String bomGroupName;
    private String title;
    @ColumnType("text")
    private String content;
    private Integer sort;

}
