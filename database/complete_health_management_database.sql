-- =============================================
-- 健康管理小程序完整数据库初始化脚本
-- 基于药品搜索与用药计划模块设计和健康管理小程序设计说明书
-- 创建时间: 2025-01-27
-- =============================================

-- 删除数据库（如果存在）
DROP DATABASE IF EXISTS health_management;

-- 创建数据库
CREATE DATABASE health_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE health_management;

-- =============================================
-- 1. 用户管理模块
-- =============================================

-- 用户表
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY COMMENT '用户ID（UUID）',
    openid VARCHAR(100) UNIQUE NOT NULL COMMENT '微信openid',
    unionid VARCHAR(100) COMMENT '微信unionid',
    nickname VARCHAR(100) COMMENT '用户昵称',
    avatar_url VARCHAR(500) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    birthday DATE COMMENT '生日',
    height DECIMAL(5,2) COMMENT '身高(cm)',
    weight DECIMAL(5,2) COMMENT '体重(kg)',
    blood_type VARCHAR(10) COMMENT '血型',
    allergies TEXT COMMENT '过敏史',
    medical_history TEXT COMMENT '病史',
    emergency_contact VARCHAR(100) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系人电话',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';

-- =============================================
-- 2. 药品信息模块
-- =============================================

-- 药品信息表
CREATE TABLE drugs (
    id VARCHAR(36) PRIMARY KEY COMMENT '数据库主键（UUID）',
    drug_id VARCHAR(100) UNIQUE NOT NULL COMMENT '药品业务ID（来自API）',
    name VARCHAR(200) NOT NULL COMMENT '药品名称',
    barcode VARCHAR(50) COMMENT '条形码',
    approval_number VARCHAR(100) COMMENT '批准文号',
    manufacturer VARCHAR(200) COMMENT '生产企业',
    specification VARCHAR(200) COMMENT '规格型号',
    dosage_form VARCHAR(100) COMMENT '剂型',
    main_ingredient TEXT COMMENT '主要成分',
    indications TEXT COMMENT '适应症',
    contraindications TEXT COMMENT '禁忌',
    adverse_reactions TEXT COMMENT '不良反应',
    dosage_usage TEXT COMMENT '用法用量',
    precautions TEXT COMMENT '注意事项',
    drug_interactions TEXT COMMENT '药物相互作用',
    storage_conditions VARCHAR(200) COMMENT '贮藏条件',
    validity_period VARCHAR(100) COMMENT '有效期',
    image_url VARCHAR(500) COMMENT '药品图片URL',
    price DECIMAL(10,2) COMMENT '参考价格',
    drug_type VARCHAR(100) COMMENT '药品类别',
    characteristics TEXT COMMENT '性状',
    execution_standard VARCHAR(200) COMMENT '执行标准',
    main_diseases TEXT COMMENT '主治疾病',
    is_complete BOOLEAN DEFAULT FALSE COMMENT '信息是否完整',
    data_source VARCHAR(50) DEFAULT 'yaozhi_api' COMMENT '数据来源：yaozhi_api, manual, barcode',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_drug_id (drug_id),
    INDEX idx_name_manufacturer (name, manufacturer),
    INDEX idx_complete_source (is_complete, data_source),
    INDEX idx_barcode (barcode)
) COMMENT='药品信息表';

-- 药品搜索缓存表
CREATE TABLE drug_search_cache (
    id VARCHAR(36) PRIMARY KEY COMMENT '缓存ID（UUID）',
    search_keyword VARCHAR(200) NOT NULL COMMENT '搜索关键词',
    search_type TINYINT NOT NULL COMMENT '搜索类型：1-药品名称，2-药企名称，3-药准字号',
    api_source VARCHAR(50) DEFAULT 'yaozhi_api' COMMENT 'API来源',
    cache_data JSON COMMENT '缓存的搜索结果数据',
    result_count INT DEFAULT 0 COMMENT '结果数量',
    hit_count INT DEFAULT 0 COMMENT '命中次数',
    last_hit_time TIMESTAMP NULL COMMENT '最后命中时间',
    expire_time TIMESTAMP NOT NULL COMMENT '过期时间',
    status TINYINT DEFAULT 1 COMMENT '状态：0-失效，1-有效',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_keyword_type (search_keyword, search_type),
    INDEX idx_expire_status (expire_time, status)
) COMMENT='药品搜索缓存表';

-- =============================================
-- 3. 用药计划模块
-- =============================================

-- 用药计划表
CREATE TABLE medication_plans (
    id VARCHAR(36) PRIMARY KEY COMMENT '计划ID（UUID）',
    user_id VARCHAR(36) NOT NULL COMMENT '用户ID',
    drug_id VARCHAR(36) COMMENT '药品ID（关联drugs表）',
    custom_drug_name VARCHAR(200) COMMENT '自定义药品名称',
    custom_specification VARCHAR(200) COMMENT '自定义规格',
    custom_manufacturer VARCHAR(200) COMMENT '自定义厂商',
    dosage VARCHAR(100) NOT NULL COMMENT '单次剂量',
    frequency VARCHAR(100) NOT NULL COMMENT '服用频次',
    daily_times INT NOT NULL COMMENT '每日次数',
    reminder_times JSON COMMENT '提醒时间点数组',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    duration_days INT COMMENT '持续天数',
    meal_relation TINYINT DEFAULT 0 COMMENT '与餐食关系：0-无关，1-饭前，2-饭后，3-餐中',
    reminder_advance_minutes INT DEFAULT 5 COMMENT '提前提醒分钟数',
    reminder_enabled BOOLEAN DEFAULT TRUE COMMENT '是否启用提醒',
    subscription_enabled BOOLEAN DEFAULT FALSE COMMENT '是否已订阅消息',
    notes TEXT COMMENT '备注信息',
    plan_status TINYINT DEFAULT 1 COMMENT '计划状态：0-暂停，1-进行中，2-已完成，3-已取消',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (drug_id) REFERENCES drugs(id) ON DELETE SET NULL,
    INDEX idx_user_status (user_id, plan_status),
    INDEX idx_date_range (start_date, end_date)
) COMMENT='用药计划表';

-- 用药记录表
CREATE TABLE medication_records (
    id VARCHAR(36) PRIMARY KEY COMMENT '记录ID（UUID）',
    plan_id VARCHAR(36) NOT NULL COMMENT '计划ID',
    user_id VARCHAR(36) NOT NULL COMMENT '用户ID',
    scheduled_time DATETIME NOT NULL COMMENT '计划服药时间',
    actual_time DATETIME COMMENT '实际服药时间',
    planned_dosage VARCHAR(100) COMMENT '计划剂量',
    actual_dosage VARCHAR(100) COMMENT '实际剂量',
    record_status TINYINT DEFAULT 0 COMMENT '记录状态：0-未服用，1-已服用，2-延迟服用，3-跳过',
    reminder_sent BOOLEAN DEFAULT FALSE COMMENT '是否已发送提醒',
    reminder_sent_time DATETIME COMMENT '提醒发送时间',
    body_reaction TEXT COMMENT '身体反应',
    notes TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (plan_id) REFERENCES medication_plans(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_plan_scheduled (plan_id, scheduled_time),
    INDEX idx_user_time (user_id, scheduled_time),
    INDEX idx_status_reminder (record_status, reminder_sent)
) COMMENT='用药记录表';

-- =============================================
-- 4. 体征检测模块
-- =============================================

-- 体征数据表
CREATE TABLE vital_signs (
    id VARCHAR(36) PRIMARY KEY COMMENT '记录ID（UUID）',
    user_id VARCHAR(36) NOT NULL COMMENT '用户ID',
    device_id VARCHAR(100) COMMENT '设备ID（ESP32设备标识）',
    measurement_time DATETIME NOT NULL COMMENT '测量时间',
    body_temperature DECIMAL(4,2) COMMENT '体温（摄氏度）',
    heart_rate INT COMMENT '心率（次/分钟）',
    blood_oxygen INT COMMENT '血氧饱和度（%）',
    systolic_pressure INT COMMENT '收缩压（mmHg）',
    diastolic_pressure INT COMMENT '舒张压（mmHg）',
    blood_sugar DECIMAL(5,2) COMMENT '血糖（mmol/L）',
    weight DECIMAL(5,2) COMMENT '体重（kg）',
    bmi DECIMAL(4,2) COMMENT 'BMI指数',
    measurement_environment VARCHAR(100) COMMENT '测量环境',
    data_quality TINYINT DEFAULT 1 COMMENT '数据质量：1-良好，2-一般，3-较差',
    sync_status TINYINT DEFAULT 1 COMMENT '同步状态：0-未同步，1-已同步',
    notes TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_time (user_id, measurement_time),
    INDEX idx_device_time (device_id, measurement_time)
) COMMENT='体征数据表';

-- 设备管理表
CREATE TABLE devices (
    id VARCHAR(36) PRIMARY KEY COMMENT '设备ID（UUID）',
    device_code VARCHAR(100) UNIQUE NOT NULL COMMENT '设备编码',
    device_name VARCHAR(200) NOT NULL COMMENT '设备名称',
    device_type VARCHAR(50) NOT NULL COMMENT '设备类型：ESP32, 血压计, 血糖仪等',
    user_id VARCHAR(36) COMMENT '绑定用户ID',
    mqtt_client_id VARCHAR(100) COMMENT 'MQTT客户端ID',
    device_status TINYINT DEFAULT 1 COMMENT '设备状态：0-离线，1-在线，2-故障',
    last_online_time DATETIME COMMENT '最后在线时间',
    firmware_version VARCHAR(50) COMMENT '固件版本',
    battery_level INT COMMENT '电池电量（%）',
    location VARCHAR(200) COMMENT '设备位置',
    bind_time DATETIME COMMENT '绑定时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user_device (user_id, device_status),
    INDEX idx_device_code (device_code)
) COMMENT='设备管理表';

-- 用药提醒任务表
CREATE TABLE medication_reminder_task (
    id VARCHAR(36) PRIMARY KEY COMMENT '任务ID（UUID）',
    user_id VARCHAR(36) NOT NULL COMMENT '用户ID',
    plan_id VARCHAR(36) NOT NULL COMMENT '用药计划ID',
    drug_name VARCHAR(200) NOT NULL COMMENT '药品名称',
    dosage VARCHAR(100) NOT NULL COMMENT '用药剂量',
    taking_method VARCHAR(200) COMMENT '用药方式',
    scheduled_time DATETIME NOT NULL COMMENT '计划提醒时间',
    sent_time DATETIME COMMENT '实际发送时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '任务状态：1-待发送，2-已发送，3-发送失败，4-已取消，5-已过期',
    template_id VARCHAR(100) COMMENT '订阅消息模板ID',
    message_content TEXT COMMENT '发送的消息内容（JSON格式）',
    wechat_msg_id VARCHAR(100) COMMENT '微信返回的消息ID',
    error_message TEXT COMMENT '错误信息',
    retry_count INT DEFAULT 0 COMMENT '重试次数',
    max_retries INT DEFAULT 3 COMMENT '最大重试次数',
    next_retry_time DATETIME COMMENT '下次重试时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_plan_id (plan_id),
    INDEX idx_status (status),
    INDEX idx_scheduled_time (scheduled_time),
    INDEX idx_retry (status, retry_count, next_retry_time),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (plan_id) REFERENCES medication_plans(id) ON DELETE CASCADE
) COMMENT='用药提醒任务表';

-- =============================================
-- 5. 搜索历史和日志模块
-- =============================================

-- 搜索历史表
CREATE TABLE search_history (
    id VARCHAR(36) PRIMARY KEY COMMENT '历史ID（UUID）',
    user_id VARCHAR(36) NOT NULL COMMENT '用户ID',
    search_keyword VARCHAR(200) NOT NULL COMMENT '搜索关键词',
    search_type TINYINT NOT NULL COMMENT '搜索类型：1-药品名称，2-条码搜索',
    search_result_count INT DEFAULT 0 COMMENT '搜索结果数量',
    api_called BOOLEAN DEFAULT FALSE COMMENT '是否调用了API',
    response_time_ms INT COMMENT '响应时间（毫秒）',
    search_source VARCHAR(50) COMMENT '搜索来源：cache, api',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '搜索时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_time (user_id, created_at),
    INDEX idx_keyword_type (search_keyword, search_type)
) COMMENT='搜索历史表';

-- API调用日志表
CREATE TABLE api_call_logs (
    id VARCHAR(36) PRIMARY KEY COMMENT '日志ID（UUID）',
    user_id VARCHAR(36) COMMENT '用户ID',
    api_type VARCHAR(50) NOT NULL COMMENT 'API类型：drug_search, drug_detail, barcode_query',
    api_url VARCHAR(500) NOT NULL COMMENT 'API地址',
    request_params JSON COMMENT '请求参数',
    response_code INT COMMENT '响应状态码',
    response_data JSON COMMENT '响应数据',
    response_time_ms INT COMMENT '响应时间（毫秒）',
    success BOOLEAN DEFAULT TRUE COMMENT '是否成功',
    error_message TEXT COMMENT '错误信息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '调用时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user_api (user_id, api_type),
    INDEX idx_time_success (created_at, success)
) COMMENT='API调用日志表';

-- =============================================
-- 6. 系统管理模块
-- =============================================

-- 管理员表
CREATE TABLE admins (
    id VARCHAR(36) PRIMARY KEY COMMENT '管理员ID（UUID）',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    role VARCHAR(50) DEFAULT 'admin' COMMENT '角色：super_admin, admin, operator',
    permissions JSON COMMENT '权限列表',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='管理员表';

-- 系统配置表
CREATE TABLE system_configs (
    id VARCHAR(36) PRIMARY KEY COMMENT '配置ID（UUID）',
    config_key VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_type VARCHAR(50) DEFAULT 'string' COMMENT '配置类型：string, number, boolean, json',
    description VARCHAR(500) COMMENT '配置描述',
    is_public BOOLEAN DEFAULT FALSE COMMENT '是否公开（前端可访问）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE operation_logs (
    id VARCHAR(36) PRIMARY KEY COMMENT '日志ID（UUID）',
    operator_id VARCHAR(36) COMMENT '操作者ID',
    operator_type TINYINT NOT NULL COMMENT '操作者类型：1-用户，2-管理员',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    operation_desc VARCHAR(500) COMMENT '操作描述',
    target_type VARCHAR(50) COMMENT '目标类型',
    target_id VARCHAR(36) COMMENT '目标ID',
    request_ip VARCHAR(50) COMMENT '请求IP',
    user_agent VARCHAR(500) COMMENT '用户代理',
    request_params JSON COMMENT '请求参数',
    response_result JSON COMMENT '响应结果',
    execution_time_ms INT COMMENT '执行时间（毫秒）',
    success BOOLEAN DEFAULT TRUE COMMENT '是否成功',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_operator (operator_id, operator_type),
    INDEX idx_time_type (created_at, operation_type)
) COMMENT='操作日志表';

-- =============================================
-- 7. 数据库视图
-- =============================================

-- 药品搜索基础视图
CREATE VIEW v_drug_search_basic AS
SELECT 
    id,
    drug_id,
    name,
    manufacturer,
    specification,
    is_complete,
    data_source,
    created_at
FROM drugs
WHERE status = 1;

-- 用药计划详情视图
CREATE VIEW v_medication_plan_detail AS
SELECT 
    mp.id,
    mp.user_id,
    u.nickname as user_nickname,
    COALESCE(d.name, mp.custom_drug_name) as drug_name,
    COALESCE(d.manufacturer, mp.custom_manufacturer) as manufacturer,
    COALESCE(d.specification, mp.custom_specification) as specification,
    mp.dosage,
    mp.frequency,
    mp.daily_times,
    mp.reminder_times,
    mp.start_date,
    mp.end_date,
    mp.plan_status,
    mp.created_at
FROM medication_plans mp
LEFT JOIN users u ON mp.user_id = u.id
LEFT JOIN drugs d ON mp.drug_id = d.id
WHERE mp.plan_status != 3;

-- 用户健康概览视图
CREATE VIEW v_user_health_overview AS
SELECT 
    u.id as user_id,
    u.nickname,
    u.gender,
    u.birthday,
    u.height,
    u.weight,
    COUNT(DISTINCT mp.id) as active_medication_plans,
    COUNT(DISTINCT vs.id) as vital_sign_records,
    MAX(vs.measurement_time) as last_measurement_time,
    AVG(vs.body_temperature) as avg_temperature,
    AVG(vs.heart_rate) as avg_heart_rate,
    AVG(vs.blood_oxygen) as avg_blood_oxygen
FROM users u
LEFT JOIN medication_plans mp ON u.id = mp.user_id AND mp.plan_status = 1
LEFT JOIN vital_signs vs ON u.id = vs.user_id AND vs.measurement_time >= DATE_SUB(NOW(), INTERVAL 30 DAY)
WHERE u.status = 1
GROUP BY u.id;

-- =============================================
-- 8. 存储过程
-- =============================================

DELIMITER //

-- 智能药品搜索存储过程
CREATE PROCEDURE sp_intelligent_drug_search(
    IN p_keyword VARCHAR(200),
    IN p_search_type TINYINT,
    IN p_user_id VARCHAR(36)
)
BEGIN
    DECLARE v_cache_id VARCHAR(36);
    DECLARE v_cache_data JSON;
    DECLARE v_hit_count INT DEFAULT 0;
    
    -- 记录搜索历史
    INSERT INTO search_history (id, user_id, search_keyword, search_type, created_at)
    VALUES (UUID(), p_user_id, p_keyword, p_search_type, NOW());
    
    -- 检查缓存
    SELECT id, cache_data, hit_count INTO v_cache_id, v_cache_data, v_hit_count
    FROM drug_search_cache 
    WHERE search_keyword = p_keyword 
      AND search_type = p_search_type 
      AND status = 1 
      AND expire_time > NOW()
    LIMIT 1;
    
    -- 如果有缓存，更新命中信息并返回
    IF v_cache_id IS NOT NULL THEN
        UPDATE drug_search_cache 
        SET hit_count = hit_count + 1, 
            last_hit_time = NOW()
        WHERE id = v_cache_id;
        
        -- 更新搜索历史
        UPDATE search_history 
        SET search_source = 'cache', 
            search_result_count = JSON_LENGTH(v_cache_data)
        WHERE user_id = p_user_id 
          AND search_keyword = p_keyword 
          AND search_type = p_search_type 
          AND created_at >= DATE_SUB(NOW(), INTERVAL 1 MINUTE)
        ORDER BY created_at DESC 
        LIMIT 1;
        
        SELECT 'cache' as data_source, v_cache_data as result_data;
    ELSE
        -- 没有缓存，返回需要调用API的标识
        SELECT 'api_required' as data_source, NULL as result_data;
    END IF;
END //

-- 获取药品详情存储过程
CREATE PROCEDURE sp_get_drug_detail_by_drug_id(
    IN p_drug_id VARCHAR(100)
)
BEGIN
    DECLARE v_is_complete BOOLEAN DEFAULT FALSE;
    DECLARE v_has_indications BOOLEAN DEFAULT FALSE;
    DECLARE v_has_dosage_usage BOOLEAN DEFAULT FALSE;
    DECLARE v_has_contraindications BOOLEAN DEFAULT FALSE;
    DECLARE v_has_adverse_reactions BOOLEAN DEFAULT FALSE;
    
    -- 检查药品是否存在
    SELECT 
        is_complete,
        (indications IS NOT NULL AND indications != '' AND indications != '尚不明确') as has_indications,
        (dosage_usage IS NOT NULL AND dosage_usage != '' AND dosage_usage != '详见说明书') as has_dosage_usage,
        (contraindications IS NOT NULL AND contraindications != '') as has_contraindications,
        (adverse_reactions IS NOT NULL AND adverse_reactions != '') as has_adverse_reactions
    INTO v_is_complete, v_has_indications, v_has_dosage_usage, v_has_contraindications, v_has_adverse_reactions
    FROM drugs 
    WHERE drug_id = p_drug_id AND status = 1;
    
    -- 如果药品不存在
    IF v_is_complete IS NULL THEN
        SELECT 'not_found' as status, NULL as drug_data;
    -- 如果信息完整或任意一个关键字段有内容
    ELSEIF v_is_complete = TRUE OR v_has_indications OR v_has_dosage_usage OR v_has_contraindications OR v_has_adverse_reactions THEN
        -- 如果还未标记为完整，则更新标记
        IF v_is_complete = FALSE THEN
            UPDATE drugs SET is_complete = TRUE WHERE drug_id = p_drug_id;
        END IF;
        
        SELECT 'complete' as status, JSON_OBJECT(
            'id', id,
            'drug_id', drug_id,
            'name', name,
            'manufacturer', manufacturer,
            'specification', specification,
            'indications', indications,
            'dosage_usage', dosage_usage,
            'contraindications', contraindications,
            'adverse_reactions', adverse_reactions,
            'precautions', precautions,
            'drug_interactions', drug_interactions,
            'storage_conditions', storage_conditions,
            'validity_period', validity_period,
            'image_url', image_url,
            'price', price
        ) as drug_data
        FROM drugs WHERE drug_id = p_drug_id;
    ELSE
        -- 信息不完整，需要调用API
        SELECT 'incomplete' as status, JSON_OBJECT(
            'id', id,
            'drug_id', drug_id,
            'name', name,
            'manufacturer', manufacturer
        ) as drug_data
        FROM drugs WHERE drug_id = p_drug_id;
    END IF;
END //

-- 清理过期缓存存储过程
CREATE PROCEDURE sp_cleanup_expired_cache()
BEGIN
    -- 删除过期的搜索缓存
    DELETE FROM drug_search_cache 
    WHERE expire_time < NOW() OR status = 0;
    
    -- 删除30天前的搜索历史
    DELETE FROM search_history 
    WHERE created_at < DATE_SUB(NOW(), INTERVAL 30 DAY);
    
    -- 删除90天前的API调用日志
    DELETE FROM api_call_logs 
    WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY);
    
    -- 删除180天前的操作日志
    DELETE FROM operation_logs 
    WHERE created_at < DATE_SUB(NOW(), INTERVAL 180 DAY);
END //

DELIMITER ;

-- =============================================
-- 9. 初始化数据
-- =============================================

-- 插入默认管理员账号
INSERT INTO admins (id, username, password, real_name, role, status) VALUES 
(UUID(), 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIgLbJJbYoKXOYGGqPOyTJr.3G', '系统管理员', 'super_admin', 1);
-- 默认密码: admin123

-- 插入系统配置
INSERT INTO system_configs (id, config_key, config_value, config_type, description, is_public) VALUES 
(UUID(), 'app_name', '健康管理小程序', 'string', '应用名称', TRUE),
(UUID(), 'app_version', '1.0.0', 'string', '应用版本', TRUE),
(UUID(), 'api_timeout', '30000', 'number', 'API超时时间（毫秒）', FALSE),
(UUID(), 'cache_expire_hours', '24', 'number', '缓存过期时间（小时）', FALSE),
(UUID(), 'max_search_results', '20', 'number', '最大搜索结果数', FALSE),
(UUID(), 'reminder_advance_minutes', '5', 'number', '默认提前提醒分钟数', TRUE),
(UUID(), 'yaozhi_api_host', 'https://jumbarcode.market.alicloudapi.com', 'string', '药智API主机地址', FALSE),
(UUID(), 'mqtt_broker_host', 'iot-06z00xxx.mqtt.iothub.aliyuncs.com', 'string', 'MQTT代理主机', FALSE),
(UUID(), 'mqtt_broker_port', '1883', 'number', 'MQTT代理端口', FALSE);

-- =============================================
-- 10. 创建定时事件
-- =============================================

-- 启用事件调度器
SET GLOBAL event_scheduler = ON;

-- 创建清理过期数据的定时事件
CREATE EVENT IF NOT EXISTS evt_cleanup_expired_data
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP
DO
  CALL sp_cleanup_expired_cache();

-- =============================================
-- 11. 权限设置
-- =============================================

-- 创建应用用户（用于应用连接数据库）
-- CREATE USER 'health_app'@'%' IDENTIFIED BY 'health_app_password_2025';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON health_management.* TO 'health_app'@'%';
-- GRANT EXECUTE ON health_management.* TO 'health_app'@'%';
-- FLUSH PRIVILEGES;

-- =============================================
-- 脚本执行完成
-- =============================================

SELECT '健康管理小程序数据库初始化完成！' as message;
SELECT CONCAT('数据库名称: health_management') as info;
SELECT CONCAT('默认管理员账号: admin / admin123') as admin_info;
SELECT CONCAT('创建时间: ', NOW()) as created_time;

-- 显示表统计信息
SELECT 
    TABLE_NAME as '表名',
    TABLE_COMMENT as '表说明',
    TABLE_ROWS as '预估行数'
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'health_management' 
  AND TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;