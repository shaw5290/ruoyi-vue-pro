-- 将项目商品的采购展示能力合并到 WMS 统一商品主档。
-- 兼容 MySQL 5.7 / 8.0，并支持重复执行。
DROP PROCEDURE IF EXISTS `upgrade_wms_item_purchase_fields`;

DELIMITER $$
CREATE PROCEDURE `upgrade_wms_item_purchase_fields`()
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'wms_item'
          AND COLUMN_NAME = 'merchant'
    ) THEN
        ALTER TABLE `wms_item`
            ADD COLUMN `merchant` varchar(200) NULL COMMENT '供应商/商家' AFTER `brand_id`;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'wms_item'
          AND COLUMN_NAME = 'purchase_url'
    ) THEN
        ALTER TABLE `wms_item`
            ADD COLUMN `purchase_url` text NULL COMMENT '采购链接' AFTER `merchant`;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'wms_item'
          AND COLUMN_NAME = 'image_urls'
    ) THEN
        ALTER TABLE `wms_item`
            ADD COLUMN `image_urls` text NULL COMMENT '商品展示图片(JSON)' AFTER `purchase_url`;
    END IF;
END$$
DELIMITER ;

CALL `upgrade_wms_item_purchase_fields`();
DROP PROCEDURE IF EXISTS `upgrade_wms_item_purchase_fields`;

-- BOM 的 bom_item_product.product_id 自此逻辑关联 wms_item.id。
