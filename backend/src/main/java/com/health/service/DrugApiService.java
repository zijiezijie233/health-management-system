package com.health.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.health.entity.Drug;
import com.health.mapper.ApiCallLogMapper;
import com.health.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DrugApiService {

    private static final Logger logger = LoggerFactory.getLogger(DrugApiService.class);

    @Value("${drug.api.host}")
    private String apiHost;

    @Value("${drug.api.appcode}")
    private String appCode;

    @Autowired
    private ApiCallLogMapper apiCallLogMapper;

    /**
     * 根据药品ID查询详细信息
     */
    public Drug queryDrugDetail(String drugId) {
        String path = "/drug/detail";
        Map<String, String> bodys = new HashMap<>();
        bodys.put("id", drugId);

        try {
            String response = callApi(path, bodys);
            return parseDrugFromResponse(response, "detail");
        } catch (Exception e) {
            logger.error("查询药品详情失败: drugId={}", drugId, e);
            return null;
        }
    }

    /**
     * 根据关键词搜索药品
     */
    public List<Drug> searchDrugs(String keyword, int page, int size) {
        String path = "/tmcx/drug/query";
        Map<String, String> bodys = new HashMap<>();
        bodys.put("key", keyword);
        bodys.put("type", "1"); // 1表示按药品名称搜索
        bodys.put("pageNo", String.valueOf(page));

        try {
            String response = callApi(path, bodys);
            return parseDrugListFromResponse(response);
        } catch (Exception e) {
            logger.error("搜索药品失败: keyword={}, page={}", keyword, page, e);
            return new ArrayList<>();
        }
    }

    /**
     * 根据条形码查询药品信息
     */
    public Drug queryDrugByBarcode(String barcode) {
        String path = "/brugs/barCode/query";
        Map<String, String> bodys = new HashMap<>();
        bodys.put("code", barcode);

        try {
            String response = callApi(path, bodys);
            return parseBarcodeResponse(response);
        } catch (Exception e) {
            logger.error("条形码查询失败: barcode={}", barcode, e);
            return null;
        }
    }

    /**
     * 调用第三方API
     */
    private String callApi(String path, Map<String, String> bodys) throws Exception {
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appCode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();

        // 记录API调用日志
        logApiCall(path, bodys.toString());

        HttpResponse response = HttpUtils.doPost(apiHost, path, method, headers, querys, bodys);
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 解析药品详情响应
     */
    private Drug parseDrugFromResponse(String response, String type) {
        try {
            JSONObject jsonResponse = JSONObject.parseObject(response);
            if (jsonResponse.getInteger("code") == 200) {
                JSONObject data = jsonResponse.getJSONObject("data");
                if (data != null) {
                    if ("detail".equals(type)) {
                        return convertJsonToDrugFromDetail(data);
                    } else {
                        return convertJsonToDrugFromSearch(data);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("解析药品响应失败: response={}", response, e);
        }
        return null;
    }

    /**
     * 解析条形码查询响应
     */
    private Drug parseBarcodeResponse(String response) {
        try {
            JSONObject jsonResponse = JSONObject.parseObject(response);
            if (jsonResponse.getInteger("code") == 200) {
                JSONObject data = jsonResponse.getJSONObject("data");
                if (data != null) {
                    return convertJsonToDrugFromBarcode(data);
                }
            }
        } catch (Exception e) {
            logger.error("解析条形码响应失败: response={}", response, e);
        }
        return null;
    }

    /**
     * 解析药品列表响应
     */
    private List<Drug> parseDrugListFromResponse(String response) {
        List<Drug> drugs = new ArrayList<>();
        try {
            JSONObject jsonResponse = JSONObject.parseObject(response);
            if (jsonResponse.getInteger("code") == 200) {
                JSONObject data = jsonResponse.getJSONObject("data");
                if (data != null) {
                    JSONArray list = data.getJSONArray("list");
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject drugJson = list.getJSONObject(i);
                            Drug drug = convertJsonToDrugFromSearch(drugJson);
                            if (drug != null) {
                                drugs.add(drug);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("解析药品列表响应失败: response={}", response, e);
        }
        return drugs;
    }

    /**
     * 将详情API的JSON转换为Drug对象
     */
    private Drug convertJsonToDrugFromDetail(JSONObject drugJson) {
        Drug drug = new Drug();
        drug.setDrugId(drugJson.getString("id"));
        drug.setDrugName(drugJson.getString("name"));
        drug.setTrademark(drugJson.getString("trademark"));
        drug.setManufacturer(drugJson.getString("manuName"));
        drug.setSpecification(drugJson.getString("spec"));
        drug.setDosageForm(drugJson.getString("character"));
        drug.setMainIngredients(drugJson.getString("basis"));
        drug.setIndications(drugJson.getString("purpose"));
        drug.setUsageAndDosage(drugJson.getString("dosage"));
        drug.setContraindications(drugJson.getString("taboo"));
        drug.setPrecautions(drugJson.getString("consideration"));
        drug.setStorageConditions(drugJson.getString("storage"));
        drug.setValidityPeriod(drugJson.getString("validity"));
        drug.setApprovalNumber(drugJson.getString("approval"));
        drug.setImageUrl(drugJson.getString("img"));
        
        // 设置价格，如果有的话
        if (drugJson.getBigDecimal("price") != null) {
            drug.setPrice(drugJson.getBigDecimal("price"));
        }
        
        drug.setCreatedAt(LocalDateTime.now());
        drug.setUpdatedAt(LocalDateTime.now());
        return drug;
    }

    /**
     * 将搜索API的JSON转换为Drug对象
     */
    private Drug convertJsonToDrugFromSearch(JSONObject drugJson) {
        Drug drug = new Drug();
        drug.setDrugId(drugJson.getString("id"));
        drug.setDrugName(drugJson.getString("name"));
        drug.setTrademark(drugJson.getString("trademark"));
        drug.setManufacturer(drugJson.getString("manufacturer"));
        drug.setSpecification(drugJson.getString("specification"));
        drug.setDosageForm(drugJson.getString("dosageForm"));
        drug.setMainIngredients(drugJson.getString("ingredients"));
        drug.setIndications(drugJson.getString("indications"));
        drug.setUsageAndDosage(drugJson.getString("usage"));
        drug.setContraindications(drugJson.getString("contraindications"));
        drug.setPrecautions(drugJson.getString("precautions"));
        drug.setStorageConditions(drugJson.getString("storage"));
        drug.setValidityPeriod(drugJson.getString("validity"));
        drug.setApprovalNumber(drugJson.getString("approvalNumber"));
        drug.setImageUrl(drugJson.getString("imageUrl"));
        
        // 设置价格
        if (drugJson.getBigDecimal("price") != null) {
            drug.setPrice(drugJson.getBigDecimal("price"));
        }
        
        drug.setCreatedAt(LocalDateTime.now());
        drug.setUpdatedAt(LocalDateTime.now());
        return drug;
    }

    /**
     * 将条形码API的JSON转换为Drug对象
     */
    private Drug convertJsonToDrugFromBarcode(JSONObject drugJson) {
        Drug drug = new Drug();
        drug.setDrugId(drugJson.getString("code")); // 条形码作为ID
        drug.setDrugName(drugJson.getString("name"));
        drug.setTrademark(drugJson.getString("trademark"));
        drug.setManufacturer(drugJson.getString("manuName"));
        drug.setSpecification(drugJson.getString("spec"));
        drug.setDosageForm(drugJson.getString("character"));
        drug.setMainIngredients(drugJson.getString("basis"));
        drug.setIndications(drugJson.getString("purpose"));
        drug.setUsageAndDosage(drugJson.getString("dosage"));
        drug.setContraindications(drugJson.getString("taboo"));
        drug.setPrecautions(drugJson.getString("consideration"));
        drug.setStorageConditions(drugJson.getString("storage"));
        drug.setValidityPeriod(drugJson.getString("validity"));
        drug.setApprovalNumber(drugJson.getString("approval"));
        drug.setImageUrl(drugJson.getString("img"));
        
        drug.setCreatedAt(LocalDateTime.now());
        drug.setUpdatedAt(LocalDateTime.now());
        return drug;
    }

    /**
     * 记录API调用日志
     */
    private void logApiCall(String apiPath, String requestParams) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("apiPath", apiPath);
            logData.put("requestParams", requestParams);
            logData.put("callTime", LocalDateTime.now());
            
            apiCallLogMapper.insertApiCallLog(logData);
        } catch (Exception e) {
            logger.error("记录API调用日志失败", e);
        }
    }
}