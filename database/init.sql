-- 健康管理小程序数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS Health_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE Health_DB;

-- 用户信息表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    openid VARCHAR(100) UNIQUE NOT NULL COMMENT '微信openid',
    unionid VARCHAR(100) COMMENT '微信unionid',
    nickname VARCHAR(100) COMMENT '用户昵称',
    avatar_url VARCHAR(500) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    age INT COMMENT '年龄',
    phone VARCHAR(20) COMMENT '手机号',
    emergency_contact VARCHAR(100) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系人电话',
    medical_history TEXT COMMENT '病史信息',
    allergies TEXT COMMENT '过敏史',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户信息表';

-- 药品信息表
CREATE TABLE IF NOT EXISTS drugs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '药品ID',
    name VARCHAR(200) NOT NULL COMMENT '药品名称',
    barcode VARCHAR(50) COMMENT '条形码',
    approval_number VARCHAR(100) COMMENT '批准文号',
    manufacturer VARCHAR(200) COMMENT '生产厂家',
    specification VARCHAR(100) COMMENT '规格',
    dosage_form VARCHAR(50) COMMENT '剂型',
    main_ingredient TEXT COMMENT '主要成分',
    indications TEXT COMMENT '适应症',
    contraindications TEXT COMMENT '禁忌症',
    adverse_reactions TEXT COMMENT '不良反应',
    dosage_usage TEXT COMMENT '用法用量',
    precautions TEXT COMMENT '注意事项',
    drug_interactions TEXT COMMENT '药物相互作用',
    storage_conditions VARCHAR(200) COMMENT '贮藏条件',
    validity_period VARCHAR(50) COMMENT '有效期',
    image_url VARCHAR(500) COMMENT '药品图片URL',
    price DECIMAL(10,2) COMMENT '参考价格',
    status TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name),
    INDEX idx_barcode (barcode),
    INDEX idx_approval_number (approval_number)
) COMMENT='药品信息表';

-- 用药计划表
CREATE TABLE IF NOT EXISTS medication_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '计划ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    drug_id BIGINT COMMENT '药品ID',
    drug_name VARCHAR(200) NOT NULL COMMENT '药品名称',
    dosage VARCHAR(100) NOT NULL COMMENT '单次剂量',
    frequency VARCHAR(100) NOT NULL COMMENT '服药频率',
    duration_days INT COMMENT '用药天数',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    reminder_times JSON COMMENT '提醒时间点',
    notes TEXT COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态：0-已停止，1-进行中，2-已完成',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (drug_id) REFERENCES drugs(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) COMMENT='用药计划表';

-- 用药记录表
CREATE TABLE IF NOT EXISTS medication_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    plan_id BIGINT NOT NULL COMMENT '计划ID',
    drug_name VARCHAR(200) NOT NULL COMMENT '药品名称',
    dosage VARCHAR(100) NOT NULL COMMENT '实际剂量',
    scheduled_time DATETIME NOT NULL COMMENT '计划服药时间',
    actual_time DATETIME COMMENT '实际服药时间',
    status TINYINT NOT NULL COMMENT '状态：0-未服用，1-已服用，2-漏服，3-延迟服用',
    notes TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (plan_id) REFERENCES medication_plans(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_plan_id (plan_id),
    INDEX idx_scheduled_time (scheduled_time),
    INDEX idx_status (status)
) COMMENT='用药记录表';

-- 体征数据表
CREATE TABLE IF NOT EXISTS vital_signs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    device_id VARCHAR(100) COMMENT '设备ID',
    temperature DECIMAL(4,1) COMMENT '体温(°C)',
    heart_rate INT COMMENT '心率(次/分)',
    blood_oxygen INT COMMENT '血氧饱和度(%)',
    blood_pressure_systolic INT COMMENT '收缩压(mmHg)',
    blood_pressure_diastolic INT COMMENT '舒张压(mmHg)',
    measurement_time DATETIME NOT NULL COMMENT '测量时间',
    data_source TINYINT DEFAULT 1 COMMENT '数据来源：1-设备自动，2-手动输入',
    notes TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_measurement_time (measurement_time),
    INDEX idx_device_id (device_id)
) COMMENT='体征数据表';

-- 系统日志表
CREATE TABLE IF NOT EXISTS system_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    action_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    action_description TEXT COMMENT '操作描述',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    request_data JSON COMMENT '请求数据',
    response_data JSON COMMENT '响应数据',
    status TINYINT DEFAULT 1 COMMENT '状态：0-失败，1-成功',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_action_type (action_type),
    INDEX idx_created_at (created_at)
) COMMENT='系统日志表';

-- 管理员表
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    role TINYINT DEFAULT 1 COMMENT '角色：1-普通管理员，2-超级管理员',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username)
) COMMENT='管理员表';

-- 插入默认管理员账号
INSERT INTO admins (username, password, real_name, role) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIGfkPNi/tgfvtjNgKLNZ.B.Sm', '系统管理员', 2);
-- 默认密码：admin123

-- 创建索引优化查询性能
CREATE INDEX idx_users_openid ON users(openid);
CREATE INDEX idx_medication_plans_user_date ON medication_plans(user_id, start_date);
CREATE INDEX idx_medication_records_user_time ON medication_records(user_id, scheduled_time);
CREATE INDEX idx_vital_signs_user_time ON vital_signs(user_id, measurement_time);