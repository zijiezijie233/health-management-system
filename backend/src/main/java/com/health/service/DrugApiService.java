package com.health.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.health.config.DrugApiConfig;
import com.health.entity.Drug;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 药智数据API服务类
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@Slf4j
@Service
public class DrugApiService {
    
    @Autowired
    private DrugApiConfig drugApiConfig;
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 根据条形码查询药品信息
     */
    public Drug queryDrugByBarcode(String barcode) {
        try {
            String url = drugApiConfig.getBarcodeQueryUrl() + "?barcode=" + barcode;
            
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            log.info("条形码查询响应: {}", response.getBody());
            
            return parseDrugFromResponse(response.getBody());
            
        } catch (Exception e) {
            log.error("条形码查询药品失败: barcode={}", barcode, e);
            throw new RuntimeException("查询药品信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索药品
     */
    public List<Drug> searchDrugs(String keyword, int page, int size) {
        try {
            String url = String.format("%s?keyword=%s&page=%d&size=%d", 
                    drugApiConfig.getDrugSearchUrl(), keyword, page, size);
            
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            log.info("药品搜索响应: {}", response.getBody());
            
            return parseDrugListFromResponse(response.getBody());
            
        } catch (Exception e) {
            log.error("搜索药品失败: keyword={}", keyword, e);
            throw new RuntimeException("搜索药品失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取药品详细信息
     */
    public Drug getDrugDetail(String drugId) {
        try {
            String url = drugApiConfig.getDrugDetailUrl() + "?id=" + drugId;
            
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            log.info("药品详情响应: {}", response.getBody());
            
            return parseDrugFromResponse(response.getBody());
            
        } catch (Exception e) {
            log.error("获取药品详情失败: drugId={}", drugId, e);
            throw new RuntimeException("获取药品详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建请求头
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "APPCODE " + drugApiConfig.getAppCode());
        headers.set("Content-Type", "application/json; charset=utf-8");
        return headers;
    }
    
    /**
     * 解析单个药品响应
     */
    private Drug parseDrugFromResponse(String responseBody) {
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return null;
        }
        
        try {
            JSONObject jsonObject = JSON.parseObject(responseBody);
            
            // 检查响应状态
            if (!isSuccessResponse(jsonObject)) {
                log.warn("API响应失败: {}", responseBody);
                return null;
            }
            
            JSONObject data = jsonObject.getJSONObject("data");
            if (data == null) {
                return null;
            }
            
            return convertJsonToDrug(data);
            
        } catch (Exception e) {
            log.error("解析药品响应失败", e);
            return null;
        }
    }
    
    /**
     * 解析药品列表响应
     */
    private List<Drug> parseDrugListFromResponse(String responseBody) {
        List<Drug> drugs = new ArrayList<>();
        
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return drugs;
        }
        
        try {
            JSONObject jsonObject = JSON.parseObject(responseBody);
            
            // 检查响应状态
            if (!isSuccessResponse(jsonObject)) {
                log.warn("API响应失败: {}", responseBody);
                return drugs;
            }
            
            JSONArray dataArray = jsonObject.getJSONArray("data");
            if (dataArray == null) {
                return drugs;
            }
            
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject drugJson = dataArray.getJSONObject(i);
                Drug drug = convertJsonToDrug(drugJson);
                if (drug != null) {
                    drugs.add(drug);
                }
            }
            
        } catch (Exception e) {
            log.error("解析药品列表响应失败", e);
        }
        
        return drugs;
    }
    
    /**
     * 检查响应是否成功
     */
    private boolean isSuccessResponse(JSONObject jsonObject) {
        Integer code = jsonObject.getInteger("code");
        return code != null && code == 200;
    }
    
    /**
     * 将JSON对象转换为Drug实体
     */
    private Drug convertJsonToDrug(JSONObject drugJson) {
        if (drugJson == null) {
            return null;
        }
        
        Drug drug = new Drug();
        
        // 基本信息
        drug.setName(drugJson.getString("name"));
        drug.setBarcode(drugJson.getString("barcode"));
        drug.setApprovalNumber(drugJson.getString("approvalNumber"));
        drug.setManufacturer(drugJson.getString("manufacturer"));
        drug.setSpecification(drugJson.getString("specification"));
        drug.setDosageForm(drugJson.getString("dosageForm"));
        drug.setMainIngredient(drugJson.getString("mainIngredient"));
        
        // 详细信息
        drug.setIndications(drugJson.getString("indications"));
        drug.setContraindications(drugJson.getString("contraindications"));
        drug.setAdverseReactions(drugJson.getString("adverseReactions"));
        drug.setUsage(drugJson.getString("usage"));
        drug.setPrecautions(drugJson.getString("precautions"));
        drug.setDrugInteractions(drugJson.getString("drugInteractions"));
        drug.setStorageConditions(drugJson.getString("storageConditions"));
        drug.setValidityPeriod(drugJson.getString("validityPeriod"));
        
        // 其他信息
        drug.setImageUrl(drugJson.getString("imageUrl"));
        drug.setPrice(drugJson.getBigDecimal("price"));
        
        // 设置默认状态
        drug.setStatus(Drug.Status.ACTIVE.getValue());
        
        return drug;
    }
}