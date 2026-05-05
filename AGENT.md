# AGENT.md

## 1. 文档目的

本文档用于约束本仓库后端开发与自动化代理的工作方式，目标是让后续改动保持一致的分层、命名、鉴权、数据库迁移和交付习惯。

适用范围仅限后端模块，不包含 `ruoyi-ui` 前端。

适用模块：

- `ruoyi-admin`
- `ruoyi-framework`
- `ruoyi-system`
- `ruoyi-common`
- `ruoyi-quartz`
- `ruoyi-generator`


## 2. 当前项目事实

### 2.1 技术栈

- 基础框架：RuoYi Vue 定制版
- JDK：`1.8`
- Spring Boot：`2.5.15`
- 持久层：MyBatis XML
- 分页：PageHelper
- 安全：Spring Security + JWT + Redis
- 数据源：MySQL + Druid
- 缓存：Redis
- 数据库变更：Flyway
- 定时任务：Spring `@Scheduled` + Quartz 模块

### 2.2 启动与运行配置

后端关键配置位于：

- `ruoyi-admin/src/main/resources/application.yml`
- `ruoyi-admin/src/main/resources/application-druid.yml`

当前默认运行事实：

- 服务端口：`9201`
- `context-path`：`/`
- Spring profile：`druid`
- MySQL 默认库：`stock`
- MySQL 默认地址：`localhost:3306`
- Redis 默认地址：`localhost:6379`
- Token 有效期：`1440` 分钟
- Flyway：默认开启

### 2.3 时区约定

主启动类 `ruoyi-admin/src/main/java/com/ruoyi/RuoYiApplication.java` 中显式设置了 JVM 默认时区：

- `America/Toronto`

这意味着：

- 所有与时间相关的后端改动，必须先确认是业务时间、数据库时间、服务器时间，还是客户端展示时间
- 新增定时任务、结算逻辑、过期逻辑、日报逻辑时，不能默认按本机时区理解
- 如果新增代码依赖 `new Date()`、`Calendar`、`LocalDateTime.now()` 等，必须先确认是否需要和当前系统时区保持一致


## 3. 模块职责

### 3.1 `ruoyi-admin`

职责：

- Spring Boot 启动入口
- Controller 暴露
- 应用配置
- Flyway migration

目录重点：

- `com.ruoyi.web.controller.system`：后台管理接口
- `com.ruoyi.web.controller.api`：APP/API 接口
- `com.ruoyi.web.controller.common`：通用接口
- `com.ruoyi.web.controller.monitor`：监控相关接口

### 3.2 `ruoyi-framework`

职责：

- Spring Security 配置
- JWT 过滤器
- 全局异常处理
- AOP 能力，如 `@DataScope`
- 拦截器，如重复提交拦截

关键点：

- `SecurityConfig` 中后台接口默认需要认证
- `/api/**/**` 被配置为 `permitAll`
- 但 JWT 过滤器仍会尝试从请求中解析登录态，因此 APP 登录态依旧依赖 token

### 3.3 `ruoyi-system`

职责：

- 业务领域模型
- Service / ServiceImpl
- Mapper 接口
- MyBatis XML
- 定时任务
- 各类业务工具类与三方接入工具

关键目录：

- `domain`
- `domain/vo`
- `service`
- `service/impl`
- `mapper`
- `task`
- `utils`

### 3.4 `ruoyi-common`

职责：

- 通用实体
- 通用控制器基类
- AjaxResult
- 常量、工具类、注解
- Redis、Token 等通用能力

注意：

- 一部分核心实体并不在 `ruoyi-system/domain`，而是在 `ruoyi-common/core/domain/entity`
- 例如：`UserInfo`、`UserAmount`、`SysUser`、`SysRole`

### 3.5 `ruoyi-quartz`

职责：

- Quartz 管理任务
- 后台可配置调度任务

注意：

- 项目中同时存在 Spring `@Scheduled` 任务和 Quartz 任务
- 修改任务逻辑时，先确认该任务是“代码定时”还是“后台配置定时”


## 4. 后端分层规范

### 4.1 推荐调用链

标准调用链：

`Controller -> Service 接口 -> ServiceImpl -> Mapper -> Mapper.xml -> DB`

要求：

- Controller 不直接写 SQL，不直接访问 Mapper
- 业务规则放在 Service 层
- SQL 细节放在 Mapper XML
- 多表联动、账务联动、状态流转必须由 Service 统一编排

### 4.2 Controller 规范

Controller 分两类：

- 后台管理接口：通常位于 `controller/system`
- APP/API 接口：通常位于 `controller/api`

后台管理接口惯例：

- 使用 `@PreAuthorize`
- 使用 `BaseController` 的 `startPage()`、`getDataTable()`、`toAjax()`
- 变更类接口通常配合 `@Log` 和 `@RepeatSubmit`

APP/API 接口惯例：

- 路径一般以 `/api/...` 开头
- 许多接口虽然在 Spring Security 层面 `permitAll`，但业务上仍依赖 token 解析后的 `SecurityUtils.getUserId()`
- 参数校验多在 Controller 手写完成
- 错误通常使用 `AjaxResult.error(...)` 或抛 `LangException`

Controller 层必须做的事：

- 参数合法性校验
- 基础格式规整，如 `trim()`
- 登录用户身份提取
- 分页和排序入口控制
- 将复杂业务委托给 Service

Controller 层不应做的事：

- 编排大量跨表业务
- 长事务处理
- 直接拼 SQL
- 把缓存、外部调用、资金变动散落在多个控制器里

### 4.3 Service 规范

Service 层负责：

- 业务规则
- 状态变更
- 资金、订单、账户等核心流程
- 跨表更新
- 缓存更新
- 调用第三方服务

适合放在 Service 层的逻辑：

- 账户状态校验
- 交易前置校验
- 多张表写入顺序
- 风控与开关判断
- 日志扩展参数整理

需要事务的方法：

- 涉及多表写操作
- 涉及账户余额、持仓、订单、流水联动
- 任何“成功一半会造成脏数据”的逻辑

约定：

- 优先在 `ServiceImpl` 上使用 `@Transactional(rollbackFor = Exception.class)`
- 不要把事务边界下沉到 Controller
- 不要把一个完整业务拆成多个 Controller 分别更新数据库

### 4.4 Mapper 与 XML 规范

当前项目使用 MyBatis XML，而不是 JPA。

规则：

- `Mapper.java` 只声明方法
- SQL 写在 `src/main/resources/mapper/**/*.xml`
- 查询、动态条件、批量操作优先放 XML
- SQL 别名、字段名、逻辑删除条件要与现有风格保持一致

当前项目已存在的特点：

- 常用 `resultMap`
- 常用 `<if>` 做动态查询
- 查询条件经常依赖 `BaseEntity.params`
- 多数业务表使用逻辑删除字段，如 `is_del = 0`

注意：

- 如果 Mapper 使用了 `@DataScope`，其入参第一个参数必须能承载 `BaseEntity.params`
- 现有 XML 中大量使用 `${params.dataScope}`，新增涉及数据权限的查询时要按同样模式接入
- 新增 SQL 时不要绕开现有逻辑删除约定


## 5. 命名与放置约定

### 5.1 类命名

- Controller：`XxxController` / `ApiXxxController`
- Service 接口：`IXxxService`
- Service 实现：`XxxServiceImpl`
- Mapper：`XxxMapper`
- 实体：`Xxx`
- 视图对象：`XxxVo`
- 定时任务：`XxxTask`

### 5.2 包放置

新增后台管理接口：

- 放到 `ruoyi-admin/src/main/java/com/ruoyi/web/controller/system`

新增 APP 接口：

- 放到 `ruoyi-admin/src/main/java/com/ruoyi/web/controller/api`

新增业务实体、服务、Mapper：

- 放到 `ruoyi-system`

新增通用能力、通用实体、基础工具：

- 只有在明确跨业务复用时，才放到 `ruoyi-common`

原则：

- 业务代码优先放 `ruoyi-system`
- 不要把单一业务逻辑放进 `ruoyi-common`
- 不要把 Controller 相关代码放到 `ruoyi-system`


## 6. 返回值、异常与分页规范

### 6.1 返回值规范

当前项目统一返回：

- 普通接口：`AjaxResult`
- 列表接口：`TableDataInfo`

列表接口推荐写法：

- `startPage()`
- `startOrderBy(...)` 或前端传排序
- `getDataTable(list)`

变更接口推荐写法：

- `return toAjax(service.xxx(...));`

注意：

- 当前 `BaseController.toAjax(int rows)` 在 `rows <= 0` 时会抛出 `ServiceException("System is busy")`
- 因此 Service 返回 `0` 不会默默失败，而会转成异常
- 新代码如果依赖 `toAjax(int)`，必须明确“0 行影响”是不是业务失败

### 6.2 异常规范

当前项目主要使用：

- `ServiceException`
- `LangException`
- 全局异常处理器 `GlobalExceptionHandler`

建议：

- 后台管理场景优先抛 `ServiceException`
- 面向 APP 多语言提示的场景优先抛 `LangException`
- 不要在业务代码中吞异常后直接返回模糊成功
- 不要随意 `catch (Exception)` 后忽略

当前异常处理特点：

- `/api/` 路径下的异常消息会走 APP 风格返回
- 后台路径返回更偏管理端错误提示


## 7. 鉴权与权限规范

### 7.1 后台管理接口

后台接口默认原则：

- 必须认证
- 必须按权限点控制

推荐：

- 列表：`@PreAuthorize("@ss.hasPermi('xxx:list')")`
- 查询：`@PreAuthorize("@ss.hasPermi('xxx:query')")`
- 新增：`@PreAuthorize("@ss.hasPermi('xxx:add')")`
- 修改：`@PreAuthorize("@ss.hasPermi('xxx:edit')")`
- 删除：`@PreAuthorize("@ss.hasPermi('xxx:remove')")`

如果一个接口服务多个菜单权限，可参考现有代码使用：

- `@ss.hasAnyPermi(...)`

### 7.2 APP/API 接口

必须特别注意：

- `SecurityConfig` 中 `/api/**/**` 已放行
- 这不等于 APP 接口天然匿名
- 实际上很多 APP 接口仍通过 JWT 过滤器解析 token，并在业务中调用 `SecurityUtils.getUserId()`

因此新增 APP 接口时必须先判断：

- 是匿名接口，还是登录后接口
- 如果是登录后接口，必须验证当前实现路径下 `SecurityUtils.getUserId()` 能成立
- 如果接口允许匿名访问，就不能直接无保护地读取用户上下文

常见匿名接口：

- 登录
- 注册
- 验证码
- 公共配置类读取接口

### 7.3 数据权限

后台列表查询如果需要受部门、角色或代理层级限制，优先复用：

- `@DataScope`

现有项目特点：

- `isUserInfo = true` 时会对 `user_info` 查询做特殊数据范围拼接
- 相关 SQL 通过 `params.dataScope` 注入 XML

规则：

- 新增后台列表查询时，先判断是否应受数据权限约束
- 不能只在前端按钮层做权限控制


## 8. 日志、防重与审计规范

### 8.1 `@Log`

项目中大量变更接口已使用 `@Log`。

适用场景：

- 新增
- 修改
- 删除
- 审核
- 充值、扣款、交易、订单提交等关键业务动作

建议：

- 新增关键业务写操作时，优先补 `@Log`
- `title` 直接写业务动作
- `businessType` 选择准确类型
- 如已有字典类，继续沿用 `dict = XxxLogDict.class`

### 8.2 `@RepeatSubmit`

项目中对提交类接口广泛使用 `@RepeatSubmit`。

规则：

- 会触发状态变更、资金变更、订单提交、审核、回调受理的接口，优先加上
- 纯查询接口不要滥用

注意：

- `/api/` 路径下重复提交提示会被改写为 APP 端错误码风格


## 9. 数据库与 Flyway 规范

### 9.1 数据库变更入口

本项目数据库变更优先走 Flyway：

- `ruoyi-admin/src/main/resources/db/migration`

当前库中已存在大量迁移脚本，版本已连续增长。

### 9.2 迁移规则

新增表结构、字段、索引、初始化数据时：

- 优先新增 Flyway 脚本
- 不要只改 `sql/` 下的初始化脚本然后假设线上会同步

建议延续当前命名风格：

- `V196__xxx.sql`
- `V197__xxx.sql`

命名要求：

- 版本号必须递增
- 文件名要表达本次变更的业务目的
- 一个 migration 聚焦一组相关变更

### 9.3 SQL 变更要求

- 对账务、订单、用户状态相关表变更，必须评估历史数据兼容性
- 给新字段提供合理默认值或回填策略
- 索引变更要考虑现有高频查询 XML
- 逻辑删除表优先兼容 `is_del`


## 10. 定时任务规范

项目中有两类任务：

- Spring `@Scheduled` 代码任务
- Quartz 配置任务

### 10.1 新增 Spring 定时任务

放置位置：

- `ruoyi-system/src/main/java/com/ruoyi/system/task/...`

规则：

- 类名使用 `XxxTask`
- 需要考虑时区影响
- 需要考虑幂等性
- 异常不能影响整个调度线程

### 10.2 新增 Quartz 任务

适用于：

- 需要在后台配置周期
- 需要人工启停
- 需要操作日志留痕

规则：

- 先确认是否真的需要可配置调度
- 不要把强实时任务都塞进 Quartz


## 11. 缓存、Token 与登录态规范

### 11.1 Token

当前项目同时存在：

- `com.ruoyi.common.service.TokenService`
- `com.ruoyi.framework.web.service.SystemTokenService`

两者都围绕 Redis + JWT 工作，且都使用：

- `token.header`
- `token.secret`
- `token.expireTime`

注意：

- 登录会按 `admin` / `app` 区分 token map
- 同一用户重新登录会踢掉旧 token

### 11.2 缓存改动要求

如果业务改动涉及：

- 用户登录态
- 配置缓存
- Redis 中的临时状态
- 汇率、行情、业务开关缓存

则必须同时考虑：

- 写入时机
- 失效时机
- 更新路径
- 登录退出链路

不要只改数据库而忘记同步缓存。


## 12. 第三方集成规范

当前项目已集成或预留以下能力：

- Polygon
- Financial Modeling Prep
- 邮件
- 短信
- Telegram
- Udun
- 若干支付/中间服务能力

规范：

- 第三方调用逻辑优先放 `ruoyi-system/utils` 或对应 Service 中
- 外部地址、密钥、开关放配置文件，不要硬编码
- 超时、异常、空返回必须做兜底
- 回调类接口要考虑重复提交与幂等


## 13. 新增或修改后端功能时的推荐流程

### 13.1 新增后台 CRUD

推荐步骤：

1. 新增或扩展 domain
2. 新增 Mapper 接口
3. 新增 Mapper XML
4. 新增 Service 接口与实现
5. 在 `controller/system` 新增接口
6. 补齐 `@PreAuthorize`、`@Log`、`@RepeatSubmit`
7. 如涉及表结构，新增 Flyway migration

### 13.2 新增 APP 接口

推荐步骤：

1. 明确接口是否匿名
2. 在 `controller/api` 增加入口
3. 明确是否需要 `SecurityUtils.getUserId()`
4. 参数校验尽量前置
5. 关键写操作加 `@RepeatSubmit`
6. 根据提示需求选择 `ServiceException` 或 `LangException`
7. 涉及多表写入时加事务

### 13.3 修改资金、订单、持仓逻辑

必须额外检查：

- 事务是否完整
- 是否会重复提交
- 是否影响流水
- 是否影响冻结金额/可用金额
- 是否影响定时任务后续处理
- 是否需要补偿逻辑


## 14. 编码约束

### 14.1 保持现有风格

- 沿用当前包结构
- 沿用当前分层
- 沿用当前命名方式
- 沿用当前 `AjaxResult` / `TableDataInfo` 返回模型

### 14.2 不建议的做法

- 不要把新业务直接堆进 Controller
- 不要在多个地方复制同一段资金逻辑
- 不要在 XML 外拼接大段 SQL
- 不要绕过 Flyway 直接假设数据库手工改过
- 不要在不知道时区影响的情况下新增定时逻辑
- 不要把仅一处使用的业务工具塞进 `ruoyi-common`

### 14.3 参数处理

- 字符串入参按现有风格做非空判断和 `trim`
- 金额使用 `BigDecimal`
- 枚举型状态值如果仍为整数，至少在代码中写清含义
- 对 `BaseEntity.params` 的动态参数使用前要说明用途，避免变成隐式协议


## 15. 验证与交付要求

当前仓库几乎没有现成的 `src/test` 测试代码，因此每次后端改动至少要做以下之一：

- 相关模块编译通过
- 核心接口手工验证
- 核心 SQL 检查
- 定时任务/回调逻辑走读验证

推荐最低验证清单：

- 受影响模块可编译
- 新增 SQL 与 XML 命名、参数一致
- 新增接口路径、权限点、日志注解正确
- 新增 migration 版本号连续
- 涉及时间逻辑时确认 Toronto 时区影响
- 涉及登录态时确认 token 获取链路正常


## 16. 给后续代理或开发者的执行指令

当你在本仓库处理后端需求时，请默认遵守以下优先级：

1. 先判断改动属于哪个模块，不要跨模块随意塞代码
2. 先确认接口属于后台管理还是 APP/API，再决定鉴权方式
3. 先确认是否涉及数据库结构变更，再决定是否新增 Flyway migration
4. 先确认是否涉及事务、缓存、登录态、定时任务，再动核心业务
5. 优先复用现有 Service、Mapper、工具类和注解体系，不重复造轮子
6. 修改用户、订单、资金相关逻辑时，以“幂等、安全、可回滚”为第一原则


## 17. 当前项目的关键风险提醒

- `/api/**` 在 Spring Security 层是放行的，但不少接口仍隐式依赖登录态，新增接口时容易误判成“天然匿名”
- 系统默认时区是 `America/Toronto`，任何时间相关需求都可能和开发机时区不一致
- `BaseEntity.params` 被广泛用于隐式传参，改查询条件时要同步检查 XML
- `@DataScope` 通过 `${params.dataScope}` 注入 SQL，改后台列表查询时要警惕权限遗漏
- 当前测试覆盖薄弱，涉及资金和订单的改动必须主动加强验证


## 18. 文档维护规则

以下场景发生时，应同步更新本文件：

- 后端模块结构变化
- 鉴权规则变化
- 配置中心或环境变量策略变化
- 数据库迁移方式变化
- 定时任务体系变化
- 统一返回模型或异常模型变化

