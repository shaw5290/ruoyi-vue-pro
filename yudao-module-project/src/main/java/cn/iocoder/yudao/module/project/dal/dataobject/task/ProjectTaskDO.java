package cn.iocoder.yudao.module.project.dal.dataobject.task;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * 项目任务 DO。
 */
@TableName("project_task")
@KeySequence("project_task_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTaskDO extends TenantBaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 归属项目，逻辑关联 project_info.id */
    private Long projectId;
    /** 制作阶段，例如准备、采购、加工、装配、接线、调试、验收 */
    private String phase;
    private String title;
    /** 可空，逻辑关联 system_users.id */
    private Long assigneeUserId;
    /** 负责人展示快照 */
    private String assigneeName;
    private LocalDate dueDate;
    /** 优先级：0 低，1 中，2 高 */
    private Integer priority;
    /** 状态：0 待开始，1 进行中，2 已完成 */
    private Integer status;
    private Integer sort;
    private String remark;

}
