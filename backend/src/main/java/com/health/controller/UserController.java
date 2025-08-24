package com.health.controller;

import com.health.common.Result;
import com.health.entity.User;
import com.health.service.UserService;
import com.health.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private WechatService wechatService;
    
    /**
     * 微信小程序登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody @Valid LoginRequest request) {
        try {
            User user = wechatService.login(request.getCode());
            return Result.success(user);
        } catch (Exception e) {
            log.error("用户登录失败", e);
            return Result.error("登录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/info/{id}")
    public Result<User> getUserInfo(@PathVariable @NotNull Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }
            return Result.success(user);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败");
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<User> updateUser(@RequestBody @Valid User user) {
        try {
            User updatedUser = userService.updateUser(user);
            return Result.success(updatedUser);
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新用户信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户列表（管理端）
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getUserList(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            List<User> users = userService.getUserList(nickname, status, page, size);
            Long total = userService.getUserCount(nickname, status);
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", users);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取用户列表失败", e);
            return Result.error("获取用户列表失败");
        }
    }
    
    /**
     * 更新用户状态（管理端）
     */
    @PutMapping("/status")
    public Result<Void> updateUserStatus(@RequestBody @Valid UpdateStatusRequest request) {
        try {
            boolean success = userService.updateUserStatus(request.getId(), request.getStatus());
            if (success) {
                return Result.success();
            } else {
                return Result.error("更新用户状态失败");
            }
        } catch (Exception e) {
            log.error("更新用户状态失败", e);
            return Result.error("更新用户状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除用户（管理端）
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteUser(@PathVariable @NotNull Long id) {
        try {
            boolean success = userService.deleteUser(id);
            if (success) {
                return Result.success();
            } else {
                return Result.error("删除用户失败");
            }
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return Result.error("删除用户失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户统计信息（管理端）
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getUserStatistics() {
        try {
            Long totalUsers = userService.getUserCount(null, null);
            Long todayNewUsers = userService.getTodayNewUserCount();
            Long activeUsers = userService.getActiveUserCount(7);
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalUsers", totalUsers);
            statistics.put("todayNewUsers", todayNewUsers);
            statistics.put("activeUsers", activeUsers);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取用户统计信息失败", e);
            return Result.error("获取用户统计信息失败");
        }
    }
    
    /**
     * 登录请求对象
     */
    public static class LoginRequest {
        @NotBlank(message = "授权码不能为空")
        private String code;
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
    }
    
    /**
     * 更新状态请求对象
     */
    public static class UpdateStatusRequest {
        @NotNull(message = "用户ID不能为空")
        private Long id;
        
        @NotNull(message = "状态不能为空")
        private Integer status;
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public Integer getStatus() {
            return status;
        }
        
        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}