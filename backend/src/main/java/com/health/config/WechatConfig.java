package com.health.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置类
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Data
@Component
@ConfigurationProperties(prefix = "health.wechat")
public class WechatConfig {
    
    /**
     * 小程序AppID
     */
    private String appid;
    
    /**
     * 小程序AppSecret
     */
    private String secret;
    
    /**
     * 授权类型
     */
    private String grantType;
    
    /**
     * 登录接口URL
     */
    private String loginUrl;
}