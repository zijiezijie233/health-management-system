package com.health.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.health.config.WechatConfig;
import com.health.entity.User;
import com.health.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 微信小程序服务类
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Slf4j
@Service
public class WechatService {
    
    @Autowired
    private WechatConfig wechatConfig;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 微信小程序登录
     * 
     * @param code 微信授权码
     * @return 用户信息
     */
    public User login(String code) {
        try {
            // 调用微信接口获取openid和session_key
            String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=%s",
                    wechatConfig.getLoginUrl(),
                    wechatConfig.getAppid(),
                    wechatConfig.getSecret(),
                    code,
                    wechatConfig.getGrantType());
            
            String response = restTemplate.getForObject(url, String.class);
            log.info("微信登录响应: {}", response);
            
            JSONObject jsonObject = JSON.parseObject(response);
            
            // 检查是否有错误
            if (jsonObject.containsKey("errcode")) {
                Integer errcode = jsonObject.getInteger("errcode");
                String errmsg = jsonObject.getString("errmsg");
                log.error("微信登录失败: errcode={}, errmsg={}", errcode, errmsg);
                throw new RuntimeException("微信登录失败: " + errmsg);
            }
            
            String openid = jsonObject.getString("openid");
            String sessionKey = jsonObject.getString("session_key");
            String unionid = jsonObject.getString("unionid");
            
            if (openid == null || openid.isEmpty()) {
                throw new RuntimeException("获取openid失败");
            }
            
            // 查询用户是否已存在
            User user = userMapper.selectByOpenid(openid);
            
            if (user == null) {
                // 创建新用户
                user = new User();
                user.setOpenid(openid);
                user.setUnionid(unionid);
                user.setStatus(User.Status.ACTIVE.getValue());
                
                userMapper.insert(user);
                log.info("创建新用户: openid={}, userId={}", openid, user.getId());
            } else {
                // 更新unionid（如果有）
                if (unionid != null && !unionid.equals(user.getUnionid())) {
                    user.setUnionid(unionid);
                    userMapper.updateById(user);
                }
                log.info("用户登录: openid={}, userId={}", openid, user.getId());
            }
            
            return user;
            
        } catch (Exception e) {
            log.error("微信登录异常", e);
            throw new RuntimeException("微信登录失败: " + e.getMessage());
        }
    }
    
    /**
     * 解密微信用户信息
     * 
     * @param encryptedData 加密数据
     * @param iv 初始向量
     * @param sessionKey 会话密钥
     * @return 解密后的用户信息
     */
    public JSONObject decryptUserInfo(String encryptedData, String iv, String sessionKey) {
        // TODO: 实现微信用户信息解密
        // 这里需要使用AES解密算法
        log.warn("微信用户信息解密功能待实现");
        return new JSONObject();
    }
    
    /**
     * 验证微信数据签名
     * 
     * @param rawData 原始数据
     * @param signature 签名
     * @param sessionKey 会话密钥
     * @return 验证结果
     */
    public boolean verifySignature(String rawData, String signature, String sessionKey) {
        // TODO: 实现微信数据签名验证
        log.warn("微信数据签名验证功能待实现");
        return true;
    }
}