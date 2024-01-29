# Business-Central-Platform

## 1. 安装指南

### 1.1 配置 hosts 文件

为了顺利进行开发，您需要预先配置系统的 `hosts` 文件，在 windows 系统中，该文件通常位于`C:\Windows\System32\drivers\etc`
请按照以下格式添加或修改内容：
```
127.0.0.1 local.atnibam.space
{NacosIP} dev-bcp.com
{RedisIP} dev-redis.com
```
请注意将 `{NacosIP}` 和 `{RedisIP}` 替换为实际的 Nacos 服务器和 Redis 服务器的 IP 地址。

## 2. 项目结构

```
Business-Central-Platform
├── bcp-api                 - API服务接口层，定义与外界交互的端点
│   ├── api-cms             - 内容相关API定义
│   ├── api-ims             - 即时通讯相关API定义
│   ├── api-system          - 系统相关API定义
│   ├── api-transaction     - 交易相关API定义
│   └── api-ums             - 用户相关API定义
│   └── pom.xml             - Maven项目管理配置文件，用于bcp-api模块
├── bcp-auth                - 认证模块，处理用户登录验证和授权
├── bcp-common              - 共享资源和通用库模块
│   ├── common-core         - 核心公共功能，如工具类、基础代码
│   ├── common-datasource   - 数据源配置和数据库交互
│   ├── common-log          - 日志处理的公共配置和实现
│   ├── common-minio        - 公共的MinIO对象存储模块
│   ├── common-rabbitmq     - RabbitMQ消息队列的集成和配置
│   ├── common-redis        - Redis缓存的集成和配置
│   ├── common-rocketmq     - RocketMQ消息队列的集成和配置
│   ├── common-security     - 安全性公共库
│   ├── common-service-aop  - 面向切面编程的公共服务
│   └── common-swagger      - API文档自动生成工具Swagger的集成
│   └── pom.xml             - Maven项目管理配置文件，用于bcp-common模块
├── bcp-gateway             - API网关，管理请求路由、负载均衡等
│   └── pom.xml             - Maven项目管理配置文件，用于bcp-gateway模块
├── bcp-modules             - 应用程序核心业务模块
│   ├── modules-cms         - 内容管理系统，包括评论模块
│   ├── modules-ims         - 即时通讯管理系统，包括即时通讯模块、用户好友关系管理模块
│   ├── modules-pms         - 商品管理系统，包括商品管理操作模块
│   ├── modules-system      - 系统核心业务逻辑模块
│   ├── modules-transaction - 交易处理业务逻辑模块，包括支付模块
│   └── modules-ums         - 用户管理系统，包括用户信息管理模块
│   └── pom.xml             - Maven项目管理配置文件，用于bcp-modules模块
├── .gitignore              - Git忽略文件列表，指示Git跳过版本控制的文件和目录
├── pom.xml                 - Maven父项目管理配置文件，用于整个项目
└── README.md
```