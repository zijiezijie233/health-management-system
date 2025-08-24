package com.health.service;

import com.health.entity.User;
import com.health.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务类
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Slf4j
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 根据ID查询用户
     */
    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }
        return userMapper.selectById(id);
    }
    
    /**
     * 根据openid查询用户
     */
    public User getUserByOpenid(String openid) {
        if (openid == null || openid.trim().isEmpty()) {
            return null;
        }
        return userMapper.selectByOpenid(openid);
    }
    
    /**
     * 根据手机号查询用户
     */
    public User getUserByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        return userMapper.selectByPhone(phone);
    }
    
    /**
     * 创建用户
     */
    @Transactional
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }
        
        // 检查openid是否已存在
        if (user.getOpenid() != null) {
            User existingUser = userMapper.selectByOpenid(user.getOpenid());
            if (existingUser != null) {
                throw new RuntimeException("该微信用户已存在");
            }
        }
        
        // 检查手机号是否已存在
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            User existingUser = userMapper.selectByPhone(user.getPhone());
            if (existingUser != null) {
                throw new RuntimeException("该手机号已被注册");
            }
        }
        
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(User.Status.ACTIVE.getValue());
        }
        
        int result = userMapper.insert(user);
        if (result > 0) {
            log.info("创建用户成功: userId={}, openid={}", user.getId(), user.getOpenid());
            return user;
        } else {
            throw new RuntimeException("创建用户失败");
        }
    }
    
    /**
     * 更新用户信息
     */
    @Transactional
    public User updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 检查用户是否存在
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查手机号是否被其他用户使用
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            User phoneUser = userMapper.selectByPhone(user.getPhone());
            if (phoneUser != null && !phoneUser.getId().equals(user.getId())) {
                throw new RuntimeException("该手机号已被其他用户使用");
            }
        }
        
        int result = userMapper.updateById(user);
        if (result > 0) {
            log.info("更新用户信息成功: userId={}", user.getId());
            return userMapper.selectById(user.getId());
        } else {
            throw new RuntimeException("更新用户信息失败");
        }
    }
    
    /**
     * 删除用户
     */
    @Transactional
    public boolean deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        int result = userMapper.deleteById(id);
        if (result > 0) {
            log.info("删除用户成功: userId={}", id);
            return true;
        } else {
            throw new RuntimeException("删除用户失败");
        }
    }
    
    /**
     * 更新用户状态
     */
    @Transactional
    public boolean updateUserStatus(Long id, Integer status) {
        if (id == null || status == null) {
            throw new IllegalArgumentException("用户ID和状态不能为空");
        }
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        int result = userMapper.updateStatus(id, status);
        if (result > 0) {
            log.info("更新用户状态成功: userId={}, status={}", id, status);
            return true;
        } else {
            throw new RuntimeException("更新用户状态失败");
        }
    }
    
    /**
     * 分页查询用户列表
     */
    public List<User> getUserList(String nickname, Integer status, Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        
        int offset = (page - 1) * size;
        return userMapper.selectList(nickname, status, offset, size);
    }
    
    /**
     * 查询用户总数
     */
    public Long getUserCount(String nickname, Integer status) {
        return userMapper.selectCount(nickname, status);
    }
    
    /**
     * 批量查询用户
     */
    public List<User> getUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return userMapper.selectByIds(ids);
    }
    
    /**
     * 统计今日新增用户数
     */
    public Long getTodayNewUserCount() {
        return userMapper.countTodayNewUsers();
    }
    
    /**
     * 统计活跃用户数
     */
    public Long getActiveUserCount(Integer days) {
        if (days == null || days < 1) {
            days = 7; // 默认7天
        }
        return userMapper.countActiveUsers(days);
    }
}