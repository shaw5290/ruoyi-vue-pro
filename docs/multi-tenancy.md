# 多租户表设计与模块支持说明

## 1. 多租户功能

项目通过请求头 `tenant-id` 确定当前租户，并由 MyBatis-Plus 租户拦截器自动完成数据隔离：

- 查询、更新、删除时自动追加 `tenant_id = 当前租户编号`；
- 新增数据时自动写入当前 `tenant_id`；
- `TenantBaseDO` 为 AutoTable 提供 `tenant_id` 字段定义；
- `@TenantIgnore` 用于声明平台共享表，不参与租户隔离；
- 第三方组件表通过 `yudao.tenant.ignore-tables` 排除。

开启多租户的表必须包含 `tenant_id BIGINT NOT NULL`。建议为高频查询表建立包含 `tenant_id` 的索引；业务唯一索引应把 `tenant_id` 放入联合唯一键，避免不同租户之间互相占用编码、名称等唯一值。

## 2. 实体约定

### 租户业务表

```java
public class ExampleDO extends TenantBaseDO {
}
```

继承 `TenantBaseDO` 后，AutoTable 可以创建和维护 `tenant_id` 字段。

### 平台共享表

```java
@TenantIgnore
public class ExampleDO extends BaseDO {
}
```

共享表不能继承 `TenantBaseDO`，也不能由租户拦截器追加 `tenant_id` 条件。

## 3. 模块支持情况

本次共检查 468 张实体表，其中 442 张业务表开启多租户，26 张平台共享表明确忽略。所有未标记 `@TenantIgnore` 的实体均已继承 `TenantBaseDO`。

| 模块 | 实体表 | 多租户表 | 共享表 | 说明 |
|---|---:|---:|---:|---|
| AI | 14 | 14 | 0 | 会话、知识库、模型配置等按租户隔离 |
| BPM | 8 | 8 | 0 | 项目自定义流程业务表按租户隔离 |
| CRM | 21 | 21 | 0 | 客户、商机、合同、回款等按租户隔离 |
| ERP | 33 | 33 | 0 | 商品、采购、销售、库存、财务等按租户隔离 |
| FMS | 30 | 28 | 2 | 租户账套数据隔离，系统模板共享 |
| HRM | 50 | 49 | 1 | 人事、薪酬等隔离，薪资选项模板共享 |
| IM | 17 | 17 | 0 | 会话、消息和群组按租户隔离 |
| Infra | 16 | 7 | 9 | 日志等业务数据隔离，基础设施配置共享 |
| IoT | 15 | 15 | 0 | 产品、设备、规则等按租户隔离 |
| Mall | 49 | 49 | 0 | 商品、营销、交易、统计等按租户隔离 |
| Member | 11 | 11 | 0 | 会员、等级、标签、分组等按租户隔离 |
| MES | 133 | 133 | 0 | 生产、质量、仓储等按租户隔离 |
| MP | 8 | 8 | 0 | 公众号账号及其素材、用户、消息按租户隔离 |
| Pay | 14 | 14 | 0 | 应用、渠道、订单、退款、钱包等按租户隔离 |
| Report | 1 | 1 | 0 | GoView 项目按租户隔离 |
| System | 32 | 18 | 14 | 用户、角色、部门等隔离，平台配置共享 |
| WMS | 16 | 16 | 0 | 仓库、物料、库存和单据按租户隔离 |

## 4. 平台共享表

以下表明确不使用多租户字段。

### FMS

- `fms_report_template`
- `fms_subject_template`

### HRM

- `hrm_salary_option_template`

### Infra

- `infra_codegen_column`
- `infra_codegen_table`
- `infra_config`
- `infra_data_source_config`
- `infra_file`
- `infra_file_config`
- `infra_file_content`
- `infra_job`
- `infra_job_log`

### System

- `system_dict_data`
- `system_dict_type`
- `system_mail_account`
- `system_mail_log`
- `system_mail_template`
- `system_menu`
- `system_notify_template`
- `system_oauth2_client`
- `system_sms_channel`
- `system_sms_code`
- `system_sms_log`
- `system_sms_template`
- `system_tenant`
- `system_tenant_package`

## 5. 第三方组件表

第三方组件表不应改为 `TenantBaseDO`，也不应由业务 AutoTable 实体接管，例如：

- Flowable 的 `ACT_*` 表；
- Quartz 的 `QRTZ_*` 表；
- JimuReport、JimuBI 内部表；
- AutoTable 自身的版本或变更记录表；
- 其他中间件、工作流或报表组件内部表。

这些表应加入 `yudao.tenant.ignore-tables`。如果第三方组件本身需要多租户，应使用组件提供的隔离机制，不能直接依赖 MyBatis-Plus SQL 改写。

## 6. 旧数据库升级

AutoTable `update` 模式可以根据 `TenantBaseDO` 给现有业务表补充 `tenant_id`，但已有数据无法自动判断属于哪个租户。升级前应先备份数据库，并按实际归属回填租户编号。

以租户 `1` 为例：

```sql
ALTER TABLE member_group
    ADD COLUMN tenant_id BIGINT NULL COMMENT '租户编号';

UPDATE member_group
SET tenant_id = 1
WHERE tenant_id IS NULL;

ALTER TABLE member_group
    MODIFY COLUMN tenant_id BIGINT NOT NULL COMMENT '租户编号';

CREATE INDEX idx_member_group_tenant_id
    ON member_group (tenant_id);
```

不要直接给有历史数据的表添加错误的固定租户默认值。生产环境建议先通过迁移脚本回填，再启动 AutoTable 校验表结构。

## 7. 开发检查清单

新增实体表时确认：

1. 该数据是否应被不同租户共享；
2. 租户业务表是否继承 `TenantBaseDO`；
3. 平台共享表是否标记 `@TenantIgnore`；
4. 数据库是否存在 `tenant_id BIGINT NOT NULL`；
5. 唯一索引是否包含 `tenant_id`；
6. 历史数据是否已经正确回填租户编号；
7. 第三方表是否配置在 `yudao.tenant.ignore-tables`。
