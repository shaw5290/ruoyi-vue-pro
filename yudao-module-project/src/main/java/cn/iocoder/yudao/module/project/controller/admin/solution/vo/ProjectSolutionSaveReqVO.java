package cn.iocoder.yudao.module.project.controller.admin.solution.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "管理后台 - 项目方案新增/修改 Request VO")
@Data
public class ProjectSolutionSaveReqVO {

    private Long id;
    @NotNull(message = "项目 ID 不能为空")
    private Long projectId;
    @NotEmpty(message = "方案名称不能为空")
    @Size(max = 100, message = "方案名称长度不能超过 100 个字符")
    private String name;
    private String description;
    @NotNull(message = "排序不能为空")
    private Integer sort;
    private String remark;

}
