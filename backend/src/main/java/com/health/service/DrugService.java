package com.health.service;

import com.health.entity.Drug;
import com.health.mapper.DrugMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 药品服务类
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Slf4j
@Service
public class DrugService {
    
    @Autowired
    private DrugMapper drugMapper;
    
    @Autowired
    private DrugApiService drugApiService;
    
    /**
     * 根据ID查询药品
     */
    public Drug getDrugById(Long id) {
        if (id == null) {
            return null;
        }
        return drugMapper.selectById(id);
    }
    
    /**
     * 根据条形码查询药品（优先从本地数据库查询，如果没有则调用API）
     */
    public Drug getDrugByBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            return null;
        }
        
        // 先从本地数据库查询
        Drug drug = drugMapper.selectByBarcode(barcode);
        
        if (drug == null) {
            // 本地没有，调用API查询
            try {
                drug = drugApiService.queryDrugByBarcode(barcode);
                if (drug != null) {
                    // 保存到本地数据库
                    saveDrug(drug);
                }
            } catch (Exception e) {
                log.error("API查询药品失败: barcode={}", barcode, e);
            }
        }
        
        return drug;
    }
    
    /**
     * 搜索药品（优先从本地数据库搜索，如果结果不足则调用API补充）
     */
    public List<Drug> searchDrugs(String keyword, String manufacturer, Integer status, Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        
        int offset = (page - 1) * size;
        
        // 先从本地数据库搜索
        List<Drug> localDrugs = drugMapper.searchDrugs(keyword, manufacturer, status, offset, size);
        
        // 如果本地结果不足，调用API补充
        if (localDrugs.size() < size && keyword != null && !keyword.trim().isEmpty()) {
            try {
                List<Drug> apiDrugs = drugApiService.searchDrugs(keyword, page, size - localDrugs.size());
                
                // 保存API返回的药品到本地数据库
                for (Drug drug : apiDrugs) {
                    if (!drugMapper.existsByBarcode(drug.getBarcode())) {
                        saveDrug(drug);
                        localDrugs.add(drug);
                    }
                }
            } catch (Exception e) {
                log.error("API搜索药品失败: keyword={}", keyword, e);
            }
        }
        
        return localDrugs;
    }
    
    /**
     * 保存药品
     */
    @Transactional
    public Drug saveDrug(Drug drug) {
        if (drug == null) {
            throw new IllegalArgumentException("药品信息不能为空");
        }
        
        // 检查条形码是否已存在
        if (drug.getBarcode() != null && drugMapper.existsByBarcode(drug.getBarcode())) {
            throw new RuntimeException("该条形码的药品已存在");
        }
        
        // 检查批准文号是否已存在
        if (drug.getApprovalNumber() != null && drugMapper.existsByApprovalNumber(drug.getApprovalNumber())) {
            throw new RuntimeException("该批准文号的药品已存在");
        }
        
        // 设置默认状态
        if (drug.getStatus() == null) {
            drug.setStatus(Drug.Status.ACTIVE.getValue());
        }
        
        int result = drugMapper.insert(drug);
        if (result > 0) {
            log.info("保存药品成功: drugId={}, name={}", drug.getId(), drug.getName());
            return drug;
        } else {
            throw new RuntimeException("保存药品失败");
        }
    }
    
    /**
     * 更新药品信息
     */
    @Transactional
    public Drug updateDrug(Drug drug) {
        if (drug == null || drug.getId() == null) {
            throw new IllegalArgumentException("药品ID不能为空");
        }
        
        // 检查药品是否存在
        Drug existingDrug = drugMapper.selectById(drug.getId());
        if (existingDrug == null) {
            throw new RuntimeException("药品不存在");
        }
        
        // 检查条形码是否被其他药品使用
        if (drug.getBarcode() != null) {
            Drug barcodeDrug = drugMapper.selectByBarcode(drug.getBarcode());
            if (barcodeDrug != null && !barcodeDrug.getId().equals(drug.getId())) {
                throw new RuntimeException("该条形码已被其他药品使用");
            }
        }
        
        int result = drugMapper.updateById(drug);
        if (result > 0) {
            log.info("更新药品信息成功: drugId={}", drug.getId());
            return drugMapper.selectById(drug.getId());
        } else {
            throw new RuntimeException("更新药品信息失败");
        }
    }
    
    /**
     * 删除药品
     */
    @Transactional
    public boolean deleteDrug(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("药品ID不能为空");
        }
        
        Drug drug = drugMapper.selectById(id);
        if (drug == null) {
            throw new RuntimeException("药品不存在");
        }
        
        int result = drugMapper.deleteById(id);
        if (result > 0) {
            log.info("删除药品成功: drugId={}", id);
            return true;
        } else {
            throw new RuntimeException("删除药品失败");
        }
    }
    
    /**
     * 更新药品状态
     */
    @Transactional
    public boolean updateDrugStatus(Long id, Integer status) {
        if (id == null || status == null) {
            throw new IllegalArgumentException("药品ID和状态不能为空");
        }
        
        Drug drug = drugMapper.selectById(id);
        if (drug == null) {
            throw new RuntimeException("药品不存在");
        }
        
        int result = drugMapper.updateStatus(id, status);
        if (result > 0) {
            log.info("更新药品状态成功: drugId={}, status={}", id, status);
            return true;
        } else {
            throw new RuntimeException("更新药品状态失败");
        }
    }
    
    /**
     * 根据药品名称模糊查询
     */
    public List<Drug> getDrugsByNameLike(String name, Integer limit) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        if (limit == null || limit < 1) {
            limit = 10;
        }
        return drugMapper.selectByNameLike(name, limit);
    }
    
    /**
     * 获取热门药品
     */
    public List<Drug> getPopularDrugs(Integer limit) {
        if (limit == null || limit < 1) {
            limit = 10;
        }
        return drugMapper.selectPopularDrugs(limit);
    }
    
    /**
     * 查询药品总数
     */
    public Long getDrugCount(String keyword, String manufacturer, Integer status) {
        return drugMapper.selectCount(keyword, manufacturer, status);
    }
    
    /**
     * 统计药品总数
     */
    public Long getTotalDrugCount() {
        return drugMapper.countTotalDrugs();
    }
    
    /**
     * 统计今日新增药品数
     */
    public Long getTodayNewDrugCount() {
        return drugMapper.countTodayNewDrugs();
    }
    
    /**
     * 批量查询药品
     */
    public List<Drug> getDrugsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return drugMapper.selectByIds(ids);
    }
}