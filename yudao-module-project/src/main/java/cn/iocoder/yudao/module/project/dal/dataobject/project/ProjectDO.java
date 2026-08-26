package cn.iocoder.yudao.module.project.dal.dataobject.project;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
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
    /** 项目分类 */
    private String category;
    /** 优先级：0 低，1 中，2 高 */
    private Integer priority;
    /** 执行阶段：0 规划，1 备料，2 制作，3 测试，4 暂停 */
    private Integer stage;
    /** 计划预算 */
    private BigDecimal budget;
    /** 综合进度，0-100 */
    private Integer progress;
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
