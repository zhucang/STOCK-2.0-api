# 账号IP白名单功能说明

## 需求背景

APP账号登录增加IP白名单控制。账号当前登录IP没有配置在账号IP白名单中时，登录失败。

白名单维度支持三种命中方式：

- `account_type + ip_address`：该账号类型的账号在此IP允许登录
- `account_id + ip_address`：该账号ID在此IP允许登录
- `account_type + account_id + ip_address`：该账号类型下的指定账号ID在此IP允许登录

新增或修改白名单时，`account_type` 和 `account_id` 至少填写一个。

## 数据表

新增表：`account_ip_white_list`

字段：

- `id`：主键
- `account_type`：账号类型，可为空
- `account_id`：账号ID，可为空
- `ip_address`：IP地址
- `create_time`：创建时间
- `update_time`：更新时间
- `remark`：备注

索引：

- `idx_account_ip_white_list_type_ip(account_type, ip_address)`
- `idx_account_ip_white_list_id_ip(account_id, ip_address)`
- `idx_account_ip_white_list_account_ip(account_type, account_id, ip_address)`

## 后台接口

新增后台管理接口：`/system/accountIpWhiteList`

- `GET /list`：查询列表
- `GET /{id}`：查询详情
- `POST /`：新增
- `PUT /`：修改
- `DELETE /{ids}`：删除

权限标识：

- `system:accountIpWhiteList:list`
- `system:accountIpWhiteList:query`
- `system:accountIpWhiteList:add`
- `system:accountIpWhiteList:edit`
- `system:accountIpWhiteList:remove`

## 登录流程变化

普通APP账号登录：

1. 保留原有IP黑名单、地区限制、账号状态、密码校验流程。
2. 密码校验成功后，使用 `account_type + 当前IP`、`account_id + 当前IP`、`account_type + account_id + 当前IP` 查询白名单。
3. 未命中白名单时返回登录失败，不创建token。

Reown已绑定账号登录：

1. 保留原有IP黑名单、地区限制、账号状态流程。
2. 创建token前校验 `account_type + 当前IP`、`account_id + 当前IP`、`account_type + account_id + 当前IP`。
3. 未命中白名单时返回登录失败，不创建token。

新注册后自动登录流程暂未强制校验白名单，因为账号ID需要创建后才存在，无法预先配置到账号维度白名单。
