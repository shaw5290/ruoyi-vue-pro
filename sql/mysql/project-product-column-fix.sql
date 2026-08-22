-- 修复已经由 AutoTable 创建的项目商品字段长度。
-- 新环境会根据 ProjectProductDO 的 @ColumnType("text") 自动创建，无需执行本文件。
ALTER TABLE `project_product`
    MODIFY COLUMN `purchase_url` TEXT NULL COMMENT '购买链接',
    MODIFY COLUMN `image_urls` TEXT NULL COMMENT '商品图片地址 JSON',
    MODIFY COLUMN `remark` TEXT NULL COMMENT '备注';
