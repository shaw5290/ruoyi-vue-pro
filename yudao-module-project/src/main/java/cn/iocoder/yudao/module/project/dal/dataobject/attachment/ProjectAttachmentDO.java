package cn.iocoder.yudao.module.project.dal.dataobject.attachment;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.dromara.autotable.annotation.ColumnType;

/**
 * 项目附件 DO。
 *
 * 所有附件都归属 BOM 分组；项目附件页面是各分组附件的汇总视图。
 */
@TableName("project_attachment")
@KeySequence("project_attachment_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAttachmentDO extends TenantBaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 归属项目，逻辑关联 project_info.id */
    private Long projectId;
    /** 归属 BOM 分组，逻辑关联 bom_group.id */
    private Long bomGroupId;
    /** 所属 BOM 分组名称，仅用于返回汇总视图 */
    @TableField(exist = false)
    private String bomGroupName;
    /** 上传时的原始文件名 */
    private String fileName;
    /** 基础设施文件服务返回的访问地址 */
    private String fileUrl;
    private Long fileSize;
    private String fileType;
    /** 富文本附件描述 */
    @ColumnType("text")
    private String remark;
    private Integer sort;

}
