package cn.iocoder.yudao.module.project.dal.dataobject.solution;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.project.dal.dataobject.project.ProjectDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 项目选配方案 DO
 */
@TableName("project_solution")
@KeySequence("project_solution_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSolutionDO extends TenantBaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 项目 ID，关联 {@link ProjectDO#getId()} */
    private Long projectId;
    /** 方案名称 */
    private String name;
    /** 方案说明 */
    private String description;
    /** 是否当前采用方案 */
    private Boolean adopted;
    /** 排序 */
    private Integer sort;
    /** 备注 */
    private String remark;

}
