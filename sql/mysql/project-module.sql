-- 项目模块菜单与权限。业务表由 AutoTable 根据 ProjectDO、ProjectSolutionDO 创建。
-- 菜单 ID 使用数据库自增值；脚本可重复执行，并会修复旧脚本产生的错误 parent_id。

START TRANSACTION;

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
    'project:solution:delete', 'project:solution:adopt'
) AND `deleted` = b'0';

COMMIT;

-- 检查项目菜单层级
SELECT `id`, `name`, `type`, `parent_id`, `path`, `component`, `permission`
FROM `system_menu`
WHERE `id` IN (@project_menu_id, @project_info_menu_id)
   OR `parent_id` IN (@project_menu_id, @project_info_menu_id)
ORDER BY `type`, `sort`, `id`;
