package cn.iocoder.yudao.module.project.dal.dataobject.project;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 项目 DO
 */
@TableName("project_info")
@KeySequence("project_info_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDO extends TenantBaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 项目编号 */
    private String code;
    /** 项目名称 */
    private String name;
    /** 项目说明 */
    private String description;
    /** 可见性：0 私有，1 公开 */
    private Integer visibility;
    /** 状态：0 进行中，1 已完成，2 已归档 */
    private Integer status;
    /** 项目负责人，逻辑关联 system_users.id */
    private Long ownerUserId;
    /** 计划开始日期 */
    private LocalDate startDate;
    /** 计划结束日期 */
    private LocalDate endDate;
    /** 备注 */
    private String remark;

}
