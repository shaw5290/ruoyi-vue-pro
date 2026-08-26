package cn.iocoder.yudao.module.project.controller.admin.project.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 项目分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectPageReqVO extends PageParam {

    @Schema(description = "项目编号")
    private String code;
    @Schema(description = "项目名称")
    private String name;
    @Schema(description = "项目分类")
    private String category;
    @Schema(description = "优先级")
    private Integer priority;
    @Schema(description = "执行阶段")
    private Integer stage;
    @Schema(description = "可见性")
    private Integer visibility;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "负责人用户 ID")
    private Long ownerUserId;

}
