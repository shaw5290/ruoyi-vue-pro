-- 项目 BOM 权限与角色授权（MySQL，可重复执行）
-- 规则：
-- 1. 拥有 project:info:query 的角色，自动获得 BOM 查询与方案 BOM 查询权限；
-- 2. 拥有 project:info:update 的角色，自动获得 BOM 维护与方案 BOM 配置权限；
-- 3. 执行后用户需要重新登录，以刷新前端权限列表。

START TRANSACTION;

SET @project_info_menu_id = (
    SELECT `id` FROM `system_menu`
    WHERE `component` = 'project/info/index' AND `deleted` = b'0'
    ORDER BY `id` LIMIT 1
);

INSERT INTO `system_menu`
(`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`,
 `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT permission_data.`name`, permission_data.`permission`, 3, permission_data.`sort`, @project_info_menu_id,
       '', '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
FROM (
    SELECT 'BOM 查询' AS `name`, 'bom:library:query' AS `permission`, 10 AS `sort`
    UNION ALL SELECT 'BOM 维护', 'bom:library:write', 11
    UNION ALL SELECT '方案 BOM 查询', 'bom:selection:query', 12
    UNION ALL SELECT '方案 BOM 配置', 'bom:selection:write', 13
) permission_data
WHERE NOT EXISTS (
    SELECT 1 FROM `system_menu` existing
    WHERE existing.`permission` = permission_data.`permission`
      AND existing.`deleted` = b'0'
);

UPDATE `system_menu`
SET `parent_id` = @project_info_menu_id,
    `updater` = '1',
    `update_time` = NOW()
WHERE `permission` IN (
    'bom:library:query', 'bom:library:write', 'bom:selection:query', 'bom:selection:write'
) AND `deleted` = b'0';

-- 查询权限继承
INSERT INTO `system_role_menu`
(`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT DISTINCT project_grant.`role_id`, bom_permission.`id`, '1', NOW(), '1', NOW(), b'0', project_grant.`tenant_id`
FROM `system_role_menu` project_grant
JOIN `system_menu` project_permission
  ON project_permission.`id` = project_grant.`menu_id`
 AND project_permission.`permission` = 'project:info:query'
 AND project_permission.`deleted` = b'0'
JOIN `system_menu` bom_permission
  ON bom_permission.`permission` IN ('bom:library:query', 'bom:selection:query')
 AND bom_permission.`deleted` = b'0'
WHERE project_grant.`deleted` = b'0'
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` existing
    WHERE existing.`role_id` = project_grant.`role_id`
      AND existing.`menu_id` = bom_permission.`id`
      AND existing.`tenant_id` = project_grant.`tenant_id`
      AND existing.`deleted` = b'0'
  );

-- 维护权限继承
INSERT INTO `system_role_menu`
(`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT DISTINCT project_grant.`role_id`, bom_permission.`id`, '1', NOW(), '1', NOW(), b'0', project_grant.`tenant_id`
FROM `system_role_menu` project_grant
JOIN `system_menu` project_permission
  ON project_permission.`id` = project_grant.`menu_id`
 AND project_permission.`permission` = 'project:info:update'
 AND project_permission.`deleted` = b'0'
JOIN `system_menu` bom_permission
  ON bom_permission.`permission` IN ('bom:library:write', 'bom:selection:write')
 AND bom_permission.`deleted` = b'0'
WHERE project_grant.`deleted` = b'0'
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` existing
    WHERE existing.`role_id` = project_grant.`role_id`
      AND existing.`menu_id` = bom_permission.`id`
      AND existing.`tenant_id` = project_grant.`tenant_id`
      AND existing.`deleted` = b'0'
  );

COMMIT;

SELECT menu.`id`, menu.`name`, menu.`permission`, role.`id` AS `role_id`,
       role.`name` AS `role_name`, role_menu.`tenant_id`
FROM `system_menu` menu
LEFT JOIN `system_role_menu` role_menu
  ON role_menu.`menu_id` = menu.`id` AND role_menu.`deleted` = b'0'
LEFT JOIN `system_role` role
  ON role.`id` = role_menu.`role_id` AND role.`deleted` = b'0'
WHERE menu.`permission` IN (
    'bom:library:query', 'bom:library:write', 'bom:selection:query', 'bom:selection:write'
)
  AND menu.`deleted` = b'0'
ORDER BY menu.`permission`, role_menu.`tenant_id`, role_menu.`role_id`;
