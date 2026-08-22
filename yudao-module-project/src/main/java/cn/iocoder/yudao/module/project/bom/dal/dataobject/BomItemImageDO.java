package cn.iocoder.yudao.module.project.bom.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/** BOM 物料图片。与 BOM 物料仅建立逻辑关联，不使用数据库物理外键。 */
@TableName("bom_item_image")
@KeySequence("bom_item_image_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BomItemImageDO extends TenantBaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long bomItemId;
    private String fileName;
    private String imageUrl;
    private Long fileSize;
    private String fileType;
    private Boolean primaryImage;
    private Integer sort;

}
