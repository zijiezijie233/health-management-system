# 健康管理小程序

## 一、系统概述

本健康管理小程序旨在为用户提供健康管理与检测服务，帮助用户在生病时检测并记录当前状况数据，指导科学用药。同时，后端管理端可对用户数据进行管理和分析。

## 二、系统功能

### （一）用户端模块

1. 药物识别模块
   - 支持打开相机扫描识别药物盒条形码，显示相关用药信息。
   - 可手动输入药品名称进行查询。
   - 展示用药知识图谱，直观呈现药品成分、适应症、禁忌症关联
2. 用药计划模块
   - 用户可手动输入用药名称、时间和剂量，并设置计划任务提醒。
   - 优化后具备智能推荐用药时间功能，根据用户生活习惯自动调整提醒时段；能进行用药历史分析，生成用药依从性报告，可视化展示漏服 / 重复用药情况。
3. 体征检测模块
   - **体征显示**：ESP32通过连接通过MQTT协议接入阿里云平台，小程序通过阿里云平台控制其中的体温、血氧、心率体征检测模块运行，并读取心率、血氧、体温等数据。
   - **体征分析**：综合分析检测的数据，评估当前身体状态。
4. 个人中心模块
   - **账号管理**：用户使用微信号登录，用户完善个人资料时，可以通过微信提供的头像昵称填写能力快速完善
   - **目前健康详情**：展示用户当前的健康状况综合信息。

### （二）后端管理端模块

1. 后台数据总览模块
   - 展示访问量、访问时长、新增用户、活动监测等数据。
   - 构建数据看板，可视化展示用户健康趋势、用药依从率、设备使用热力图。
2. 用户管理模块
   - 对用户的用药管理和用户体征数据进行管理。
   - 整合用药记录、体征数据生成健康评分，辅助管理员制定干预策略。
3. 功能调用总览模块
   - 记录识别调用记录和用药管理调用记录。
   - 具备自动化运营工具，如消息推送模板支持批量发送用药提醒、健康贴士

### **三、前端技术栈（用户端 + 管理端）**

#### 1. 用户端（微信小程序）

- **核心框架**：Uniapp（Vue3）
- **必要补充**：
  - **状态管理**：使用 Uniapp 内置的`Vuex`，管理用户登录状态、当前体征数据等全局信息，无需引入复杂工具。
  - **UI 组件**：`ColorUI`（轻量级健康医疗场景适配组件库），提供数据卡片、表单、图表等基础组件，减少自定义开发。
  - 扫码与图表：
    - 扫码：直接使用微信原生`wx.scanCode`API，满足药物条形码识别需求，无需额外引入解析库。
    - 数据可视化：`wx-charts`（微信小程序专用轻量图表库），实现用药依从性曲线、体征趋势图等基础可视化。

#### 2. 管理后台

- **核心框架**：Vue3 + Vite（快速构建工具）
- **UI 组件**：`Element Plus`（基础版），快速搭建数据表格、表单、看板等后台页面，无需复杂配置。
- **图表工具**：直接使用`Element Plus`实现访问量、健康趋势等基础数据可视化。

### **二、后端技术栈**

- **核心框架**：Spring Boot 3.x + MyBatis
- **必要补充**：
  - **MQTT 客户端**：对接阿里云 IoT 平台，接收 ESP32 设备的体征数据。
  - **定时任务**：Spring Boot 内置`@Scheduled`，实现用药提醒推送、健康数据汇总等定时任务，无需引入分布式调度框架。
  - **HTTP 客户端**：`RestTemplate`（Spring 内置），调用药智数据 API

### **三、数据存储**

- **主数据库**：MySQL 8.0.42
  负责存储所有结构化数据：用户信息、药品数据、用药计划、体征记录等。

### **四、第三方服务**

- **必选**：
  - 微信开放平台：提供登录（`wx.login`）、头像昵称获取、订阅消息（用药提醒）能力。
  - 微信扫一扫 API：药物条形码识别核心能力。
  - 药智数据 API：药品信息查询、冲突检测。
  - 阿里云 IoT 平台：ESP32 设备接入、体征数据转发（通过 MQTT 协议）。

### **五、开发与部署**

- **开发工具**：
  - 前端：HBuilderX（Uniapp 官方工具，一键运行微信小程序模拟器）。
  - 后端：IntelliJ IDEA 社区版。
  - 数据库：Navicat 。
- **部署**：
  - 后端：打包为 Jar 包，部署到云服务器。
  - 小程序：通过微信开发者工具直接上传审核发布。
  - 数据库：云服务器上安装 MySQL，无需复杂集群配置。

附录：

以下为药智数据 API的使用说明书

这是药品条码查询的请求示例

```java
	public static void main(String[] args) {
	    String host = "https://jumbarcode.market.alicloudapi.com";
	    String path = "/bar-code/query";
	    String method = "POST";
	    String appcode = "你自己的AppCode";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //根据API的要求，定义相对应的Content-Type
	    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    Map<String, String> bodys = new HashMap<String, String>();
	    bodys.put("code", "code");


	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	System.out.println(response.toString());
	    	//获取response的body
	    	System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

```

## 接口信息

调用地址：https://jumbarcode.market.alicloudapi.com/brugs/barCode/query**

请求方式：POST

返回类型：JSON

API 调用：[API 简单身份认证调用方法（APPCODE）](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)

## 接口参数

请求参数（Body）

| 字段名称        | 必填 | 字段详情     |
| :-------------- | :--- | :----------- |
| code **string** | Y    | 药品的条形码 |

这是药品条码查询的成功响应

```
{
  "code": 200, //成功为200，其他为失败返回码（非http返回状态码）
  "msg": "成功", //code对应的描述
  "taskNo": "04668850957017868821",  //唯一业务号
  "data": {
    "dosage": "",//用法用量
    "manuName": "",//生产厂家
    "note": "",//
    "img": "",//图片，24小时有效
    "other": "",//其它注意事项
    "code": "",//条形码
    "purpose": "",//功能主治/适用范围
    "taboo": "。",//禁忌
    "approval": "",//批准文号
    "storage": "",//贮藏补充说明
    "basis": "",//主要成分
    "manuAddress": "",//生产地址
    "spec": "",//规格
    "character": "",//性状
    "name": "",//产品名称
    "trademark": "",//商品名/商标
    "consideration": "",//注意事项
    "validity": ""//有效期
  }
}
```

这是药品搜索的请求示例

```java
	public static void main(String[] args) {
	    String host = "https://jumbarcode.market.alicloudapi.com";
	    String path = "/tmcx/drug/query";
	    String method = "POST";
	    String appcode = "你自己的AppCode";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //根据API的要求，定义相对应的Content-Type
	    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    Map<String, String> bodys = new HashMap<String, String>();
	    bodys.put("key", "key");
	    bodys.put("type", "type");
	    bodys.put("pageNo", "pageNo");


	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	System.out.println(response.toString());
	    	//获取response的body
	    	//System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

```

## 接口信息

调用地址：https://jumbarcode.market.alicloudapi.com/tmcx/drug/query**

请求方式：POST

返回类型：JSON

API 调用：[API 简单身份认证调用方法（APPCODE）](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)[API 签名认证调用方法（AppKey & AppSecret）](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)

获取认证：[AppKey & AppCode](https://market.console.aliyun.com/?productCode=cmapi00049717)

## 接口参数

请求参数（Header）

请求参数（Query）

请求参数（Body）

| 字段名称        | 必填 | 字段详情                                   |
| :-------------- | :--- | :----------------------------------------- |
| key **string**  | Y    | 关键字                                     |
| type **string** | Y    | 关键字的类型:1药品名称 2药企名称 3药准字号 |
| pageNo **int**  | N    | 页数,每页20条数据,默认为1                  |

这是药品搜索的成功响应

```
{
  "code": 200,//返回码，详见返回码说明
  "msg": "成功",//返回码对应描述
  "taskNo": "737619926157342200644693",//本次请求号
  "data": {
    "pageNo": "1",//页数
    "pageSize": "20",//每页多少条
    "totalRecord": 587,//总记录数
    "drugList": [
      {
        "id": "5e6ad8d89484a8b112ee39aa",//id
        "blfy": "尚不明确。",//不良反应
        "drugName": "阿莫西林克拉维酸钾(7:1)片",//药品名称
        "ggxh": "0.375g(C16H19N3O5S 0.25g与C8H9NO5 0.125g)",//规格型号
        "jj": "尚不明确。",//禁忌
        "manu": "湘北威尔曼制药股份有限公司",//生产企业
        "price": "13.20",//参考价格
        "pzwh": "国药准字H20051710",//批准文号
        "type": "",//药品类别
        "xz": "本品白色椭圆形薄膜衣片，除去包衣后显白色或类黄色；味微苦，有引湿性。",//性状
        "yfyl": "口服。 1.成人和12岁以上小儿，一次1片，一日3次,严重感染时剂量可加倍。 2.未经重新检查，连续治疗期不超过14日。",//用法用量
        "ywxhzy": "如与其他药物同时使用可能会发生药物相互作用，详情请咨询医师或药师。",//药物相互作用
        "yxq": "暂定24个月",//有效期
        "zc": "",//贮藏
        "img": "",//图片地址，有效期30天。建议自行下载保存，避免丢失
        "zxbz": "",//执行标准
        "zysx": "",//注意事项
        "zycf": "",//主要成份
        "zzjb": ""//主治疾病
      }
    ]
  }
}
```

这是药品详情的请求示例

```java
	public static void main(String[] args) {
	    String host = "https://jumbarcode.market.alicloudapi.com";
	    String path = "/tmcx/drug/detail";
	    String method = "POST";
	    String appcode = "你自己的AppCode";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //根据API的要求，定义相对应的Content-Type
	    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    Map<String, String> bodys = new HashMap<String, String>();
	    bodys.put("pzwh", "pzwh");
	    bodys.put("drugId", "drugId");


	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	System.out.println(response.toString());
	    	//获取response的body
	    	//System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

```

## 接口信息

调用地址：https://jumbarcode.market.alicloudapi.com/tmcx/drug/detail**

请求方式：POST

返回类型：JSON

API 调用：[API 简单身份认证调用方法（APPCODE）](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)[API 签名认证调用方法（AppKey & AppSecret）](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)

获取认证：[AppKey & AppCode](https://market.console.aliyun.com/?productCode=cmapi00049717)

## 接口参数

请求参数（Header）

请求参数（Query）

请求参数（Body）

| 字段名称          | 必填 | 字段详情                                                     |
| :---------------- | :--- | :----------------------------------------------------------- |
| pzwh **string**   | N    | 批准文号,譬如：国药准字H20045760，药品ID和批准文号至少要输入一个 |
| drugId **string** | N    | 药品ID，药品搜索接口返回的ID                                 |

这是药品详情的成功响应

```
{
  "code": 200,//返回码，详见返回码说明
  "msg": "成功",//返回码对应描述
  "taskNo": "017844227229319772912580",//本次请求号
  "data": {
    "id": "5d6e69fa9484a4e7842d9cd6",//id
    "blfy": "xxxx",//不良反应
    "drugName": "阿莫西林胶囊",//药品名称
    "ggxh": "0.25g*50粒",//规格型号
    "img": "xxx",//图片地址，有效期30天。建议自行下载保存，避免丢失
    "jj": "青霉素过敏及青霉素皮肤试验阳性患者禁用。",//禁忌
    "manu": "圣大(张家口)药业有限公司",//生产企业
    "price": "14.50",//参考价格
    "pzwh": "国药准字H13020473",//批准文号
    "syz": "xxxx",//适应症
    "type": "",//药品类别
    "xz": "本品内容物为白色至黄色粉末或颗粒。",//性状
    "yfyl": "xxx",//用法用量
    "ywxhzy": "xxxx",//药物相互作用
    "yxq": "24个月",//有效期
    "zc": "遮光，密封保存。",//贮藏
    "zxbz": "中国药典2010年版二部",//执行标准
    "zysx": "xxxx",//注意事项
    "zzjb": "xxxx",//主治疾病
    "zycf": "阿莫西林。" //主要成份
  }
}
```

