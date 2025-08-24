package com.health.mapper;

import com.health.entity.Drug;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 药品数据访问层
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Mapper
public interface DrugMapper {
    
    /**
     * 根据ID查询药品
     */
    Drug selectById(@Param("id") Long id);
    
    /**
     * 根据条形码查询药品
     */
    Drug selectByBarcode(@Param("barcode") String barcode);
    
    /**
     * 根据批准文号查询药品
     */
    Drug selectByApprovalNumber(@Param("approvalNumber") String approvalNumber);
    
    /**
     * 插入药品
     */
    int insert(Drug drug);
    
    /**
     * 更新药品信息
     */
    int updateById(Drug drug);
    
    /**
     * 根据ID删除药品
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 搜索药品
     */
    List<Drug> searchDrugs(@Param("keyword") String keyword,
                          @Param("manufacturer") String manufacturer,
                          @Param("status") Integer status,
                          @Param("offset") Integer offset,
                          @Param("limit") Integer limit);
    
    /**
     * 查询药品总数
     */
    Long selectCount(@Param("keyword") String keyword,
                    @Param("manufacturer") String manufacturer,
                    @Param("status") Integer status);
    
    /**
     * 更新药品状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 批量查询药品
     */
    List<Drug> selectByIds(@Param("ids") List<Long> ids);
    
    /**
     * 根据药品名称模糊查询
     */
    List<Drug> selectByNameLike(@Param("name") String name, @Param("limit") Integer limit);
    
    /**
     * 根据主要成分查询药品
     */
    List<Drug> selectByMainIngredient(@Param("mainIngredient") String mainIngredient);
    
    /**
     * 查询热门药品
     */
    List<Drug> selectPopularDrugs(@Param("limit") Integer limit);
    
    /**
     * 统计药品总数
     */
    Long countTotalDrugs();
    
    /**
     * 统计今日新增药品数
     */
    Long countTodayNewDrugs();
    
    /**
     * 检查条形码是否存在
     */
    boolean existsByBarcode(@Param("barcode") String barcode);
    
    /**
     * 检查批准文号是否存在
     */
    boolean existsByApprovalNumber(@Param("approvalNumber") String approvalNumber);
}