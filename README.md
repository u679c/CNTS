# 校园网故障管理系统（CNTS）

CNTS 基于 Spring Boot 2.5 与 Vue 2 构建，提供前后端分离的校园网故障上报、调度与运维管理能力。项目延续了若依（RuoYi）框架的安全、权限与代码生成体系，适合快速二次开发与教学演示。

## 技术栈
- **后端**：Spring Boot 2.5、Spring Security、MyBatis、Redis、Quartz、Druid 数据源、Swagger 3。
- **前端**：Vue 2、Element UI、Axios，开发/预览脚本基于 Vue CLI 4。
- **构建工具**：Maven 3、Node.js/npm。

## 仓库结构
| 模块 | 作用 |
| --- | --- |
| `CNTS-admin` | Spring Boot 入口服务，聚合系统、定时任务与代码生成能力。 |
| `CNTS-framework` | 核心框架封装（安全、日志、异常、通用配置等）。 |
| `CNTS-system` | 业务领域模块（用户、菜单、岗位、字典等后台能力）。 |
| `CNTS-quartz` | 任务调度模块，提供基于 Quartz 的定时任务支持。 |
| `CNTS-generator` | 代码生成器，快速生成前后端 CRUD 脚手架。 |
| `CNTS-common` | 通用工具与基础依赖。 |
| `CNTS-ui` | Vue 2 前端工程，默认代理到本地 8080 后端接口。 |

## 环境要求
- JDK 8+
- Maven 3.6+
- Node.js 12+ 与 npm
- MySQL 5.7+/8.0+（建议导入 `sql/ry_20250522.sql` 与 `sql/quartz.sql` 初始化数据）
- 本地或远程 Redis 服务

## 快速开始
### 1. 配置后端
1. 在 `CNTS-admin/src/main/resources/application-druid.yml` 中配置数据库连接（默认使用本地 `cnts` 库、账号 `cnts`/`cntsadmin`）。
2. 在 `CNTS-admin/src/main/resources/application.yml` 中调整 Redis 地址、端口和密码等参数。
3. 启动方式：
   ```bash
   cd CNTS-admin
   mvn spring-boot:run
   ```
   默认服务运行在 `http://localhost:8080`。

### 2. 配置前端
1. 安装依赖并启动开发服务器：
   ```bash
   cd CNTS-ui
   npm install
   npm run dev
   ```
2. 前端开发服务器默认运行在端口 `80`，通过代理将 `/dev-api` 请求转发到后端接口地址 `http://localhost:8080`。

### 3. 生产构建
- 后端：`mvn clean package -DskipTests`
- 前端：`npm run build:prod`（生成资源位于 `CNTS-ui/dist`，可交由 Nginx 等静态服务器部署）

## 反馈与贡献
欢迎提交 Issue 或 Pull Request 帮助改进本项目。提交前请确保代码通过基本构建或单元测试。
