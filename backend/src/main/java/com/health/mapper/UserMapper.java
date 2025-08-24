package com.health.mapper;

import com.health.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问层
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") Long id);
    
    /**
     * 根据openid查询用户
     */
    User selectByOpenid(@Param("openid") String openid);
    
    /**
     * 根据unionid查询用户
     */
    User selectByUnionid(@Param("unionid") String unionid);
    
    /**
     * 插入用户
     */
    int insert(User user);
    
    /**
     * 更新用户信息
     */
    int updateById(User user);
    
    /**
     * 根据ID删除用户
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 查询用户列表
     */
    List<User> selectList(@Param("nickname") String nickname, 
                         @Param("status") Integer status,
                         @Param("offset") Integer offset, 
                         @Param("limit") Integer limit);
    
    /**
     * 查询用户总数
     */
    Long selectCount(@Param("nickname") String nickname, 
                    @Param("status") Integer status);
    
    /**
     * 更新用户状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 批量查询用户
     */
    List<User> selectByIds(@Param("ids") List<Long> ids);
    
    /**
     * 根据手机号查询用户
     */
    User selectByPhone(@Param("phone") String phone);
    
    /**
     * 统计今日新增用户数
     */
    Long countTodayNewUsers();
    
    /**
     * 统计活跃用户数
     */
    Long countActiveUsers(@Param("days") Integer days);
}