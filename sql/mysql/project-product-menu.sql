-- 项目商品目录菜单与权限，可重复执行。
START TRANSACTION;

SET @project_menu_id = (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = 0 AND `path` = '/project' AND `deleted` = b'0'
    ORDER BY `id` LIMIT 1
);

INSERT INTO `system_menu`
(`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`,
 `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '商品管理', '', 2, 2, @project_menu_id, 'product', 'ep:goods',
       'project/product/index', 'ProjectProduct', 0, b'1', b'1', b'1',
       '1', NOW(), '1', NOW(), b'0'
WHERE @project_menu_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM `system_menu`
    WHERE `component` = 'project/product/index' AND `deleted` = b'0'
  );

SET @product_menu_id = (
    SELECT `id` FROM `system_menu`
    WHERE `component` = 'project/product/index' AND `deleted` = b'0'
    ORDER BY `id` LIMIT 1
);

INSERT INTO `system_menu`
(`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`,
 `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT permission_data.`name`, permission_data.`permission`, 3, permission_data.`sort`, @product_menu_id,
       '', '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
FROM (
    SELECT '商品查询' AS `name`, 'project:product:query' AS `permission`, 1 AS `sort`
    UNION ALL SELECT '商品维护', 'project:product:write', 2
) permission_data
WHERE @product_menu_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM `system_menu` existing
    WHERE existing.`permission` = permission_data.`permission` AND existing.`deleted` = b'0'
  );

-- 已有项目查询/更新角色自动获得相应商品权限。
INSERT INTO `system_role_menu`
(`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT DISTINCT grant_row.`role_id`, target_menu.`id`, '1', NOW(), '1', NOW(), b'0', grant_row.`tenant_id`
FROM `system_role_menu` grant_row
JOIN `system_menu` source_menu ON source_menu.`id` = grant_row.`menu_id`
 AND source_menu.`permission` IN ('project:info:query', 'project:info:update')
 AND source_menu.`deleted` = b'0'
JOIN `system_menu` target_menu ON target_menu.`permission` = CASE
    WHEN source_menu.`permission` = 'project:info:update' THEN 'project:product:write'
    ELSE 'project:product:query'
END AND target_menu.`deleted` = b'0'
WHERE grant_row.`deleted` = b'0'
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` existing
    WHERE existing.`role_id` = grant_row.`role_id`
      AND existing.`menu_id` = target_menu.`id`
      AND existing.`tenant_id` = grant_row.`tenant_id`
      AND existing.`deleted` = b'0'
  );

COMMIT;
