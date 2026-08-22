package cn.iocoder.yudao.module.project.bom.controller.admin.group.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Schema(description = "管理后台 - 新增 BOM 物料图片 Request VO")
@Data
public class BomItemImageCreateReqVO {

    @NotNull(message = "BOM 物料编号不能为空")
    private Long bomItemId;
    @NotBlank(message = "文件名不能为空")
    private String fileName;
    @NotBlank(message = "图片地址不能为空")
    private String imageUrl;
    @NotNull(message = "文件大小不能为空")
    @PositiveOrZero(message = "文件大小不能小于 0")
    private Long fileSize;
    private String fileType;

}
