package com.health.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 药品实体类
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Drug {
    
    /**
     * 药品ID
     */
    private Long id;
    
    /**
     * 药品名称
     */
    private String name;
    
    /**
     * 条形码
     */
    private String barcode;
    
    /**
     * 批准文号
     */
    private String approvalNumber;
    
    /**
     * 生产厂家
     */
    private String manufacturer;
    
    /**
     * 规格
     */
    private String specification;
    
    /**
     * 剂型
     */
    private String dosageForm;
    
    /**
     * 主要成分
     */
    private String mainIngredient;
    
    /**
     * 适应症
     */
    private String indications;
    
    /**
     * 禁忌症
     */
    private String contraindications;
    
    /**
     * 不良反应
     */
    private String adverseReactions;
    
    /**
     * 用法用量
     */
    private String dosageUsage;
    
    /**
     * 注意事项
     */
    private String precautions;
    
    /**
     * 药物相互作用
     */
    private String drugInteractions;
    
    /**
     * 贮藏条件
     */
    private String storageConditions;
    
    /**
     * 有效期
     */
    private String validityPeriod;
    
    /**
     * 药品图片URL
     */
    private String imageUrl;
    
    /**
     * 参考价格
     */
    private BigDecimal price;
    
    /**
     * 状态：0-下架，1-正常
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /**
     * 药品状态枚举
     */
    public enum Status {
        OFFLINE(0, "下架"),
        NORMAL(1, "正常");
        
        private final Integer code;
        private final String desc;
        
        Status(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static Status getByCode(Integer code) {
            for (Status status : values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
            return NORMAL;
        }
    }
}