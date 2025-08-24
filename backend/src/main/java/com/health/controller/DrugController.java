package com.health.controller;

import com.health.common.Result;
import com.health.entity.Drug;
import com.health.service.DrugService;
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
 * 药品控制器
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/drug")
@Validated
public class DrugController {
    
    @Autowired
    private DrugService drugService;
    
    /**
     * 根据条形码查询药品
     */
    @GetMapping("/barcode/{barcode}")
    public Result<Drug> getDrugByBarcode(@PathVariable @NotBlank String barcode) {
        try {
            Drug drug = drugService.getDrugByBarcode(barcode);
            if (drug == null) {
                return Result.error("未找到该药品信息");
            }
            return Result.success(drug);
        } catch (Exception e) {
            log.error("根据条形码查询药品失败: barcode={}", barcode, e);
            return Result.error("查询药品失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索药品
     */
    @GetMapping("/search")
    public Result<Map<String, Object>> searchDrugs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            List<Drug> drugs = drugService.searchDrugs(keyword, manufacturer, status, page, size);
            Long total = drugService.getDrugCount(keyword, manufacturer, status);
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", drugs);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("搜索药品失败: keyword={}", keyword, e);
            return Result.error("搜索药品失败");
        }
    }
    
    /**
     * 根据ID查询药品详情
     */
    @GetMapping("/detail/{id}")
    public Result<Drug> getDrugDetail(@PathVariable @NotNull Long id) {
        try {
            Drug drug = drugService.getDrugById(id);
            if (drug == null) {
                return Result.error("药品不存在");
            }
            return Result.success(drug);
        } catch (Exception e) {
            log.error("获取药品详情失败: id={}", id, e);
            return Result.error("获取药品详情失败");
        }
    }
    
    /**
     * 根据药品名称模糊查询
     */
    @GetMapping("/suggest")
    public Result<List<Drug>> getDrugSuggestions(
            @RequestParam @NotBlank String name,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Drug> drugs = drugService.getDrugsByNameLike(name, limit);
            return Result.success(drugs);
        } catch (Exception e) {
            log.error("获取药品建议失败: name={}", name, e);
            return Result.error("获取药品建议失败");
        }
    }
    
    /**
     * 获取热门药品
     */
    @GetMapping("/popular")
    public Result<List<Drug>> getPopularDrugs(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Drug> drugs = drugService.getPopularDrugs(limit);
            return Result.success(drugs);
        } catch (Exception e) {
            log.error("获取热门药品失败", e);
            return Result.error("获取热门药品失败");
        }
    }
    
    /**
     * 添加药品（管理端）
     */
    @PostMapping("/add")
    public Result<Drug> addDrug(@RequestBody @Valid Drug drug) {
        try {
            Drug savedDrug = drugService.saveDrug(drug);
            return Result.success(savedDrug);
        } catch (Exception e) {
            log.error("添加药品失败", e);
            return Result.error("添加药品失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新药品信息（管理端）
     */
    @PutMapping("/update")
    public Result<Drug> updateDrug(@RequestBody @Valid Drug drug) {
        try {
            Drug updatedDrug = drugService.updateDrug(drug);
            return Result.success(updatedDrug);
        } catch (Exception e) {
            log.error("更新药品信息失败", e);
            return Result.error("更新药品信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除药品（管理端）
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDrug(@PathVariable @NotNull Long id) {
        try {
            boolean success = drugService.deleteDrug(id);
            if (success) {
                return Result.success();
            } else {
                return Result.error("删除药品失败");
            }
        } catch (Exception e) {
            log.error("删除药品失败: id={}", id, e);
            return Result.error("删除药品失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新药品状态（管理端）
     */
    @PutMapping("/status")
    public Result<Void> updateDrugStatus(@RequestBody @Valid UpdateStatusRequest request) {
        try {
            boolean success = drugService.updateDrugStatus(request.getId(), request.getStatus());
            if (success) {
                return Result.success();
            } else {
                return Result.error("更新药品状态失败");
            }
        } catch (Exception e) {
            log.error("更新药品状态失败", e);
            return Result.error("更新药品状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取药品统计信息（管理端）
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getDrugStatistics() {
        try {
            Long totalDrugs = drugService.getTotalDrugCount();
            Long todayNewDrugs = drugService.getTodayNewDrugCount();
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalDrugs", totalDrugs);
            statistics.put("todayNewDrugs", todayNewDrugs);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取药品统计信息失败", e);
            return Result.error("获取药品统计信息失败");
        }
    }
    
    /**
     * 更新状态请求对象
     */
    public static class UpdateStatusRequest {
        @NotNull(message = "药品ID不能为空")
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