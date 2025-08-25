# 智能健康管理系统

基于微信小程序的智能健康管理平台，提供用药提醒、健康监测、药物识别等功能。

## 项目结构

```
Health_project/
├── backend/          # Spring Boot 后端服务
├── miniprogram/      # UniApp 微信小程序
├── admin-web/        # Vue3 管理后台（待开发）
├── database/         # 数据库初始化脚本
└── docs/            # 项目文档
```

## 功能特性

### 已完成功能
- ✅ 用户认证模块（微信登录）
- ✅ 个人资料管理（头像上传、信息编辑）
- ✅ 基础项目架构搭建

### 开发中功能
- 🚧 药物识别模块
- 🚧 用药计划管理
- 🚧 健康数据监测
- 🚧 管理后台系统

## 技术栈

### 后端
- Spring Boot 2.7+
- MySQL 8.0
- MyBatis Plus
- JWT 认证

### 前端
- UniApp
- Vue 3
- Element Plus（管理后台）

## 快速开始

### 后端启动
```bash
cd backend
mvn spring-boot:run
```

### 小程序开发
```bash
cd miniprogram
npm install
npm run dev:mp-weixin
```

## 开发进度

- [x] 项目环境搭建
- [x] 数据库设计
- [x] 用户认证模块
- [x] 个人资料编辑功能
- [ ] 药物识别模块
- [ ] 用药计划管理
- [ ] 健康数据监测
- [ ] 管理后台开发

## 贡献

欢迎提交 Issue 和 Pull Request！

## 许可证

MIT License