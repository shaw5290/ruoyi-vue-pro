-- 项目模块菜单与权限。业务表由 AutoTable 根据 ProjectDO、ProjectSolutionDO 创建。
-- 菜单 ID 使用数据库自增值；脚本可重复执行，并会修复旧脚本产生的错误 parent_id。

START TRANSACTION;

-- 0. 项目 BOM 物料单位字典。ID 使用数据库自增值，脚本可重复执行。
INSERT INTO `system_dict_type`
(`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
SELECT '项目 BOM 物料单位', 'project_bom_unit', 0, '项目 BOM 物料的计量单位',
       '1', NOW(), '1', NOW(), b'0', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM `system_dict_type`
    WHERE `type` = 'project_bom_unit' AND `deleted` = b'0'
);

INSERT INTO `system_dict_data`
(`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`,
 `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT unit_data.`sort`, unit_data.`label`, unit_data.`value`, 'project_bom_unit', 0, '', '', '',
       '1', NOW(), '1', NOW(), b'0'
FROM (
    SELECT 1 AS `sort`, '件' AS `label`, '件' AS `value`
    UNION ALL SELECT 2, '个', '个'
    UNION ALL SELECT 3, '套', '套'
    UNION ALL SELECT 4, '台', '台'
    UNION ALL SELECT 5, '只', '只'
    UNION ALL SELECT 6, '块', '块'
    UNION ALL SELECT 7, '组', '组'
    UNION ALL SELECT 8, '根', '根'
    UNION ALL SELECT 9, '条', '条'
    UNION ALL SELECT 10, '张', '张'
    UNION ALL SELECT 11, '卷', '卷'
    UNION ALL SELECT 12, '包', '包'
    UNION ALL SELECT 13, '箱', '箱'
    UNION ALL SELECT 14, '米', '米'
    UNION ALL SELECT 15, '厘米', '厘米'
    UNION ALL SELECT 16, '毫米', '毫米'
    UNION ALL SELECT 17, '千克', '千克'
    UNION ALL SELECT 18, '克', '克'
    UNION ALL SELECT 19, '升', '升'
    UNION ALL SELECT 20, '毫升', '毫升'
) unit_data
WHERE NOT EXISTS (
    SELECT 1 FROM `system_dict_data` existing
    WHERE existing.`dict_type` = 'project_bom_unit'
      AND existing.`value` = unit_data.`value`
      AND existing.`deleted` = b'0'
);

-- 1. 插入项目管理根菜单，并获取真实自增 ID
INSERT INTO `system_menu`
(`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`,
 `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '项目管理', '', 1, 300, 0, '/project', 'ep:folder-opened', '', '',
       0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (
    SELECT 1 FROM `system_menu`
    WHERE `parent_id` = 0 AND `path` = '/project' AND `deleted` = b'0'
);

-- 新插入时 LAST_INSERT_ID() 是真实 ID；已存在时按路径查询真实 ID
SET @project_menu_id = IF(
    ROW_COUNT() > 0,
    LAST_INSERT_ID(),
    (SELECT `id` FROM `system_menu`
     WHERE `parent_id` = 0 AND `path` = '/project' AND `deleted` = b'0'
     ORDER BY `id` LIMIT 1)
);

-- 2. 插入项目列表，并获取真实自增 ID
INSERT INTO `system_menu`
(`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`,
 `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '项目列表', '', 2, 1, @project_menu_id, 'info', 'ep:list', 'project/info/index', 'ProjectInfo',
       0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (
    SELECT 1 FROM `system_menu`
    WHERE `component` = 'project/info/index' AND `deleted` = b'0'
);

SET @project_info_menu_id = IF(
    ROW_COUNT() > 0,
    LAST_INSERT_ID(),
    (SELECT `id` FROM `system_menu`
     WHERE `component` = 'project/info/index' AND `deleted` = b'0'
     ORDER BY `id` LIMIT 1)
);

-- 修复旧脚本已经插入、但关联 ID 错误的项目列表
UPDATE `system_menu`
SET `parent_id` = @project_menu_id,
    `path` = 'info',
    `updater` = '1',
    `update_time` = NOW()
WHERE `id` = @project_info_menu_id;

-- 3. 插入项目与方案权限按钮，parent_id 使用项目列表的真实 ID
INSERT INTO `system_menu`
(`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`,
 `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT permission_data.`name`, permission_data.`permission`, 3, permission_data.`sort`, @project_info_menu_id,
       '', '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
FROM (
    SELECT '项目查询' AS `name`, 'project:info:query' AS `permission`, 1 AS `sort`
    UNION ALL SELECT '项目创建', 'project:info:create', 2
    UNION ALL SELECT '项目更新', 'project:info:update', 3
    UNION ALL SELECT '项目删除', 'project:info:delete', 4
    UNION ALL SELECT '方案查询', 'project:solution:query', 5
    UNION ALL SELECT '方案创建', 'project:solution:create', 6
    UNION ALL SELECT '方案更新', 'project:solution:update', 7
    UNION ALL SELECT '方案删除', 'project:solution:delete', 8
    UNION ALL SELECT '方案采用', 'project:solution:adopt', 9
    UNION ALL SELECT 'BOM 模块库查询', 'bom:library:query', 10
    UNION ALL SELECT 'BOM 模块库维护', 'bom:library:write', 11
    UNION ALL SELECT '方案 BOM 查询', 'bom:selection:query', 12
    UNION ALL SELECT '方案 BOM 配置', 'bom:selection:write', 13
) permission_data
WHERE NOT EXISTS (
    SELECT 1 FROM `system_menu` existing
    WHERE existing.`permission` = permission_data.`permission`
      AND existing.`deleted` = b'0'
);

-- 修复旧脚本已经插入、但关联 ID 错误的权限按钮
UPDATE `system_menu`
SET `parent_id` = @project_info_menu_id,
    `updater` = '1',
    `update_time` = NOW()
WHERE `permission` IN (
    'project:info:query', 'project:info:create', 'project:info:update', 'project:info:delete',
    'project:solution:query', 'project:solution:create', 'project:solution:update',
    'project:solution:delete', 'project:solution:adopt',
    'bom:library:query', 'bom:library:write', 'bom:selection:query', 'bom:selection:write'
) AND `deleted` = b'0';

-- 4. 将项目权限映射到项目内置 BOM 权限
-- 已有“项目查询”的角色获得 BOM 查询；已有“项目更新”的角色获得 BOM 维护。
-- 使用 NOT EXISTS 保证脚本可重复执行，不会产生重复授权。
INSERT INTO `system_role_menu`
(`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT DISTINCT project_grant.`role_id`, bom_query.`id`, '1', NOW(), '1', NOW(), b'0', project_grant.`tenant_id`
FROM `system_role_menu` project_grant
JOIN `system_menu` project_query
  ON project_query.`id` = project_grant.`menu_id`
 AND project_query.`permission` = 'project:info:query'
 AND project_query.`deleted` = b'0'
JOIN `system_menu` bom_query
  ON bom_query.`permission` IN ('bom:library:query', 'bom:selection:query')
 AND bom_query.`deleted` = b'0'
WHERE project_grant.`deleted` = b'0'
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` existing
    WHERE existing.`role_id` = project_grant.`role_id`
      AND existing.`menu_id` = bom_query.`id`
      AND existing.`tenant_id` = project_grant.`tenant_id`
      AND existing.`deleted` = b'0'
  );

INSERT INTO `system_role_menu`
(`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT DISTINCT project_grant.`role_id`, bom_write.`id`, '1', NOW(), '1', NOW(), b'0', project_grant.`tenant_id`
FROM `system_role_menu` project_grant
JOIN `system_menu` project_update
  ON project_update.`id` = project_grant.`menu_id`
 AND project_update.`permission` = 'project:info:update'
 AND project_update.`deleted` = b'0'
JOIN `system_menu` bom_write
  ON bom_write.`permission` IN ('bom:library:write', 'bom:selection:write')
 AND bom_write.`deleted` = b'0'
WHERE project_grant.`deleted` = b'0'
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` existing
    WHERE existing.`role_id` = project_grant.`role_id`
      AND existing.`menu_id` = bom_write.`id`
      AND existing.`tenant_id` = project_grant.`tenant_id`
      AND existing.`deleted` = b'0'
  );

COMMIT;

-- 检查项目菜单层级
SELECT `id`, `name`, `type`, `parent_id`, `path`, `component`, `permission`
FROM `system_menu`
WHERE `id` IN (@project_menu_id, @project_info_menu_id)
   OR `parent_id` IN (@project_menu_id, @project_info_menu_id)
ORDER BY `type`, `sort`, `id`;

-- 检查 BOM 权限及角色授权
SELECT menu.`id`, menu.`name`, menu.`permission`, role_menu.`role_id`, role_menu.`tenant_id`
FROM `system_menu` menu
LEFT JOIN `system_role_menu` role_menu
  ON role_menu.`menu_id` = menu.`id` AND role_menu.`deleted` = b'0'
WHERE menu.`permission` IN (
  'bom:library:query', 'bom:library:write', 'bom:selection:query', 'bom:selection:write'
)
  AND menu.`deleted` = b'0'
ORDER BY menu.`permission`, role_menu.`tenant_id`, role_menu.`role_id`;
