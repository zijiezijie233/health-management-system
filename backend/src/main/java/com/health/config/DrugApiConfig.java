package com.health.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 药智数据API配置类
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Data
@Component
@ConfigurationProperties(prefix = "health.drug-api")
public class DrugApiConfig {
    
    /**
     * API主机地址
     */
    private String host;
    
    /**
     * AppKey
     */
    private String appKey;
    
    /**
     * AppSecret
     */
    private String appSecret;
    
    /**
     * AppCode
     */
    private String appCode;
    
    /**
     * 条形码查询接口路径
     */
    private String barcodeQueryPath;
    
    /**
     * 药品搜索接口路径
     */
    private String drugSearchPath;
    
    /**
     * 药品详情接口路径
     */
    private String drugDetailPath;
    
    /**
     * 获取完整的条形码查询URL
     */
    public String getBarcodeQueryUrl() {
        return host + barcodeQueryPath;
    }
    
    /**
     * 获取完整的药品搜索URL
     */
    public String getDrugSearchUrl() {
        return host + drugSearchPath;
    }
    
    /**
     * 获取完整的药品详情URL
     */
    public String getDrugDetailUrl() {
        return host + drugDetailPath;
    }
}