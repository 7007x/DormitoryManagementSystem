# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 提供在此代码库中工作的指导。

## 项目概述

这是一个**学生宿舍管理系统** - 一个毕业设计项目，技术栈包括：
- **后端**: Spring Boot 2.6.3 + MyBatis-Plus 3.5.1 (Java 11)
- **前端**: Vue 3.5.26 + Element Plus
- **数据库**: MySQL 5.7

系统服务三种用户角色：
1. **系统管理员** - 全权限访问，用户管理，数据统计
2. **宿舍管理员**（宿管） - 楼宇/房间管理，访客管理
3. **学生** - 查看宿舍信息，申请调宿，提交报修

---

## 开发命令

### 环境要求
- JDK 11
- MySQL 5.7
- Node.js 和 npm
- Maven

### 数据库配置
1. 使用 `sql.txt` 中的 SQL 脚本创建数据库 `Dormitory`
2. 在 `backend/src/main/resources/application.properties` 中配置数据库凭据

### 后端 (Spring Boot)
```bash
cd backend
mvn clean install          # 构建依赖
# 运行 SpringbootApplication.java (端口 9090)
```

### 前端 (Vue)
```bash
cd vue
npm install                # 安装依赖
npm run serve              # 开发服务器
npm run build              # 生产构建
```

---

## 架构概览

### 后端结构 (`backend/src/main/java/com/example/springboot/`)

```
├── SpringbootApplication.java    # 主入口，包含 @MapperScan
├── controller/                     # REST API 端点
│   ├── MainController            # 认证：loadIdentity, loadUserInfo, signOut
│   ├── AdminController           # 管理员增删改查
│   ├── StudentController         # 学生增删改查 + 登录
│   ├── DormManagerController     # 宿管增删改查 + 登录
│   ├── DormBuildController       # 楼宇管理
│   ├── DormRoomController        # 房间管理 + getMyRoom()
│   ├── RepairController          # 报修请求
│   ├── AdjustRoomController      # 调宿申请
│   ├── NoticeController          # 公告发布
│   └── VisitorController         # 访客记录
├── service/                        # 业务逻辑层
├── mapper/                         # MyBatis-Plus 数据访问层
├── entity/                         # 实体类 (Admin, Student, DormManager 等)
└── common/                         # 工具类 (CORS配置，UID生成器，床位名称验证)
```

### 前端结构 (`vue/src/`)

```
├── main.js                         # 入口文件，配置 Element Plus
├── router/index.js                 # Vue Router 路由守卫
├── views/                          # 页面组件
│   ├── Login.vue                   # 登录页
│   ├── Home.vue                    # 首页仪表盘（ECharts 可视化）
│   ├── StuInfo.vue                 # 学生管理
│   ├── BuildingInfo.vue            # 楼宇管理
│   ├── RoomInfo.vue                # 房间管理 + 分配
│   ├── DormManagerInfo.vue         # 宿管管理
│   ├── RepairInfo.vue              # 报修管理
│   ├── AdjustRoomInfo.vue          # 调宿审批
│   ├── VisitorInfo.vue             # 访客管理
│   ├── NoticeInfo.vue              # 公告管理
│   ├── SelfInfo.vue                # 个人中心 + 头像上传
│   └── ... (学生端页面)
├── components/                     # 可复用组件 (Aside, Header)
└── utils/request.js                # Axios 实例，配置基础 URL
```

---

## 认证与会话机制

**重要提示**: 本系统使用双重存储认证模式：

### 服务端 (HttpSession)
- 登录时，后端在 `HttpSession` 中存储 `Identity` 和 `User`
- 每个用户拥有独立的会话（通过 JSESSIONID cookie 区分）
- 会话在 30 分钟无活动后过期

### 客户端 (sessionStorage)
- 前端在登录时将 `user` 和 `identity` 存储在 `sessionStorage` 中
- 用于 `router/index.js:34-51` 中的路由守卫
- 关闭浏览器标签页时清空（页面刷新后保留）

### 认证流程
1. 用户提交登录 → 后端验证凭据 → 创建会话 → 返回 `Set-Cookie: JSESSIONID`
2. 前端接收响应 → 将用户数据存入 `sessionStorage`
3. 后续请求自动包含 JSESSIONID cookie
4. 后端使用 JSESSIONID 定位用户会话

**关键文件**：
- `StudentController.java:78-92` - 登录端点
- `MainController.java:17-26` - loadIdentity 端点
- `MainController.java:31-40` - loadUserInfo 端点
- `MainController.java:45-50` - signOut 端点
- `vue/src/router/index.js:34-51` - 路由守卫

---

## 核心业务逻辑

### 房间分配系统
- 房间使用**床位命名规范**：`bed1_name`, `bed2_name`, `bed3_name`, `bed4_name`
- 验证逻辑位于 `common/utils/BedNameValidator.java`
- 学生分配到房间时，姓名存储在相应的床位字段中
- 调宿申请 (`AdjustRoom`) 审批通过后更新床位分配

### 请求/审批工作流
- **调宿申请**: 学生申请 → 管理员审批 → 系统更新房间分配
- **报修**: 学生提交 → 管理员/宿管更新状态
- 状态模式: `待审核` → `已通过` / `已拒绝`

---

## 默认账号密码

请查看数据库表获取凭据：
- **管理员**: `admin` / `123456`
- **学生**: `20230001` / `123456`
- **宿管**: (查看 `dorm_manager` 表)

---

## 关键配置文件

| 文件 | 用途 |
|------|------|
| `backend/pom.xml` | Maven 依赖 (Spring Boot, MyBatis-Plus, Hutool, FastJSON) |
| `backend/src/main/resources/application.properties` | 服务器端口 (9090)，MySQL 连接 |
| `vue/package.json` | npm 依赖 (Vue 3, Element Plus, ECharts, WangEditor) |

---

## 重要说明

1. **语言**: 代码注释和界面均为中文
2. **CORS**: 后端在 `common/config/CorsConfig.java` 中配置了跨域
3. **文件上传**: 头像上传由 `FileController` 处理
4. **数据可视化**: 首页使用 ECharts (见 `Home.vue`, `home_echarts.vue`)
5. **富文本编辑器**: 使用 WangEditor 编辑公告

---

## 相关文档

- `业务地图_系统认知视角.md` - 业务架构概览
- `业务流程图.md` - 业务流程图（Mermaid 图表）
- `Session机制完整讲解.md` - 会话机制详细讲解
- `权限分析报告.md` - 权限分析
