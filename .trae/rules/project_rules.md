# ��������С����

## һ��ϵͳ����

����������С����ּ��Ϊ�û��ṩ��������������񣬰����û�������ʱ��Ⲣ��¼��ǰ״�����ݣ�ָ����ѧ��ҩ��ͬʱ����˹���˿ɶ��û����ݽ��й���ͷ�����

## ����ϵͳ����

### ��һ���û���ģ��

1. ҩ��ʶ��ģ��
   - ֧�ִ����ɨ��ʶ��ҩ��������룬��ʾ�����ҩ��Ϣ��
   - ���ֶ�����ҩƷ���ƽ��в�ѯ��
   - չʾ��ҩ֪ʶͼ�ף�ֱ�۳���ҩƷ�ɷ֡���Ӧ֢������֢����
2. ��ҩ�ƻ�ģ��
   - �û����ֶ�������ҩ���ơ�ʱ��ͼ����������üƻ��������ѡ�
   - �Ż���߱������Ƽ���ҩʱ�书�ܣ������û�����ϰ���Զ���������ʱ�Σ��ܽ�����ҩ��ʷ������������ҩ�����Ա��棬���ӻ�չʾ©�� / �ظ���ҩ�����
3. �������ģ��
   - **������ʾ**��ESP32ͨ������ͨ��MQTTЭ����밢����ƽ̨��С����ͨ��������ƽ̨�������е����¡�Ѫ���������������ģ�����У�����ȡ���ʡ�Ѫ�������µ����ݡ�
   - **��������**���ۺϷ����������ݣ�������ǰ����״̬��
4. ��������ģ��
   - **�˺Ź���**���û�ʹ��΢�źŵ�¼���û����Ƹ�������ʱ������ͨ��΢���ṩ��ͷ���ǳ���д������������
   - **Ŀǰ��������**��չʾ�û���ǰ�Ľ���״���ۺ���Ϣ��

### ��������˹����ģ��

1. ��̨��������ģ��
   - չʾ������������ʱ���������û�����������ݡ�
   - �������ݿ��壬���ӻ�չʾ�û��������ơ���ҩ�����ʡ��豸ʹ������ͼ��
2. �û�����ģ��
   - ���û�����ҩ������û��������ݽ��й���
   - ������ҩ��¼�������������ɽ������֣���������Ա�ƶ���Ԥ���ԡ�
3. ���ܵ�������ģ��
   - ��¼ʶ����ü�¼����ҩ������ü�¼��
   - �߱��Զ�����Ӫ���ߣ�����Ϣ����ģ��֧������������ҩ���ѡ�������ʿ

### **����ǰ�˼���ջ���û��� + ����ˣ�**

#### 1. �û��ˣ�΢��С����

- **���Ŀ��**��Uniapp��Vue3��
- **��Ҫ����**��
  - **״̬����**��ʹ�� Uniapp ���õ�`Vuex`�������û���¼״̬����ǰ�������ݵ�ȫ����Ϣ���������븴�ӹ��ߡ�
  - **UI ���**��`ColorUI`������������ҽ�Ƴ�����������⣩���ṩ���ݿ�Ƭ������ͼ��Ȼ�������������Զ��忪����
  - ɨ����ͼ��
    - ɨ�룺ֱ��ʹ��΢��ԭ��`wx.scanCode`API������ҩ��������ʶ���������������������⡣
    - ���ݿ��ӻ���`wx-charts`��΢��С����ר������ͼ��⣩��ʵ����ҩ���������ߡ���������ͼ�Ȼ������ӻ���

#### 2. �����̨

- **���Ŀ��**��Vue3 + Vite�����ٹ������ߣ�
- **UI ���**��`Element Plus`�������棩�����ٴ���ݱ�񡢱�������Ⱥ�̨ҳ�棬���踴�����á�
- **ͼ����**��ֱ��ʹ��`Element Plus`ʵ�ַ��������������ƵȻ������ݿ��ӻ���

### **������˼���ջ**

- **���Ŀ��**��Spring Boot 3.x + MyBatis
- **��Ҫ����**��
  - **MQTT �ͻ���**���ԽӰ����� IoT ƽ̨������ ESP32 �豸���������ݡ�
  - **��ʱ����**��Spring Boot ����`@Scheduled`��ʵ����ҩ�������͡��������ݻ��ܵȶ�ʱ������������ֲ�ʽ���ȿ�ܡ�
  - **HTTP �ͻ���**��`RestTemplate`��Spring ���ã�������ҩ������ API

### **�������ݴ洢**

- **�����ݿ�**��MySQL 8.0.42
  ����洢���нṹ�����ݣ��û���Ϣ��ҩƷ���ݡ���ҩ�ƻ���������¼�ȡ�

### **�ġ�����������**

- **��ѡ**��
  - ΢�ſ���ƽ̨���ṩ��¼��`wx.login`����ͷ���ǳƻ�ȡ��������Ϣ����ҩ���ѣ�������
  - ΢��ɨһɨ API��ҩ��������ʶ�����������
  - ҩ������ API��ҩƷ��Ϣ��ѯ����ͻ��⡣
  - ������ IoT ƽ̨��ESP32 �豸���롢��������ת����ͨ�� MQTT Э�飩��

### **�塢�����벿��**

- **��������**��
  - ǰ�ˣ�HBuilderX��Uniapp �ٷ����ߣ�һ������΢��С����ģ��������
  - ��ˣ�IntelliJ IDEA �����档
  - ���ݿ⣺Navicat ��
- **����**��
  - ��ˣ����Ϊ Jar ���������Ʒ�������
  - С����ͨ��΢�ſ����߹���ֱ���ϴ���˷�����
  - ���ݿ⣺�Ʒ������ϰ�װ MySQL�����踴�Ӽ�Ⱥ���á�

��¼��

����Ϊҩ������ API��ʹ��˵����

����ҩƷ�����ѯ������ʾ��

```java
	public static void main(String[] args) {
	    String host = "https://jumbarcode.market.alicloudapi.com";
	    String path = "/bar-code/query";
	    String method = "POST";
	    String appcode = "���Լ���AppCode";
	    Map<String, String> headers = new HashMap<String, String>();
	    //�����header�еĸ�ʽ(�м���Ӣ�Ŀո�)ΪAuthorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //����API��Ҫ�󣬶������Ӧ��Content-Type
	    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    Map<String, String> bodys = new HashMap<String, String>();
	    bodys.put("code", "code");


	    try {
	    	/**
	    	* ��Ҫ��ʾ����:
	    	* HttpUtils���
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* ����
	    	*
	    	* ��Ӧ�����������
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	System.out.println(response.toString());
	    	//��ȡresponse��body
	    	System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

```

## �ӿ���Ϣ

���õ�ַ��https://jumbarcode.market.alicloudapi.com/brugs/barCode/query*��*

����ʽ��POST

�������ͣ�JSON

API ���ã�[API �������֤���÷�����APPCODE��](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)

## �ӿڲ���

���������Body��

| �ֶ�����        | ���� | �ֶ�����     |
| :-------------- | :--- | :----------- |
| code **string** | Y    | ҩƷ�������� |

����ҩƷ�����ѯ�ĳɹ���Ӧ

```
{
  "code": 200, //�ɹ�Ϊ200������Ϊʧ�ܷ����루��http����״̬�룩
  "msg": "�ɹ�", //code��Ӧ������
  "taskNo": "04668850957017868821",  //Ψһҵ���
  "data": {
    "dosage": "",//�÷�����
    "manuName": "",//��������
    "note": "",//
    "img": "",//ͼƬ��24Сʱ��Ч
    "other": "",//����ע������
    "code": "",//������
    "purpose": "",//��������/���÷�Χ
    "taboo": "��",//����
    "approval": "",//��׼�ĺ�
    "storage": "",//���ز���˵��
    "basis": "",//��Ҫ�ɷ�
    "manuAddress": "",//������ַ
    "spec": "",//���
    "character": "",//��״
    "name": "",//��Ʒ����
    "trademark": "",//��Ʒ��/�̱�
    "consideration": "",//ע������
    "validity": ""//��Ч��
  }
}
```

����ҩƷ����������ʾ��

```java
	public static void main(String[] args) {
	    String host = "https://jumbarcode.market.alicloudapi.com";
	    String path = "/tmcx/drug/query";
	    String method = "POST";
	    String appcode = "���Լ���AppCode";
	    Map<String, String> headers = new HashMap<String, String>();
	    //�����header�еĸ�ʽ(�м���Ӣ�Ŀո�)ΪAuthorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //����API��Ҫ�󣬶������Ӧ��Content-Type
	    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    Map<String, String> bodys = new HashMap<String, String>();
	    bodys.put("key", "key");
	    bodys.put("type", "type");
	    bodys.put("pageNo", "pageNo");


	    try {
	    	/**
	    	* ��Ҫ��ʾ����:
	    	* HttpUtils���
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* ����
	    	*
	    	* ��Ӧ�����������
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	System.out.println(response.toString());
	    	//��ȡresponse��body
	    	//System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

```

## �ӿ���Ϣ

���õ�ַ��https://jumbarcode.market.alicloudapi.com/tmcx/drug/query*��*

����ʽ��POST

�������ͣ�JSON

API ���ã�[API �������֤���÷�����APPCODE��](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)[API ǩ����֤���÷�����AppKey & AppSecret��](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)

��ȡ��֤��[AppKey & AppCode](https://market.console.aliyun.com/?productCode=cmapi00049717)

## �ӿڲ���

���������Header��

���������Query��

���������Body��

| �ֶ�����        | ���� | �ֶ�����                                   |
| :-------------- | :--- | :----------------------------------------- |
| key **string**  | Y    | �ؼ���                                     |
| type **string** | Y    | �ؼ��ֵ�����:1ҩƷ���� 2ҩ������ 3ҩ׼�ֺ� |
| pageNo **int**  | N    | ҳ��,ÿҳ20������,Ĭ��Ϊ1                  |

����ҩƷ�����ĳɹ���Ӧ

```
{
  "code": 200,//�����룬���������˵��
  "msg": "�ɹ�",//�������Ӧ����
  "taskNo": "737619926157342200644693",//���������
  "data": {
    "pageNo": "1",//ҳ��
    "pageSize": "20",//ÿҳ������
    "totalRecord": 587,//�ܼ�¼��
    "drugList": [
      {
        "id": "5e6ad8d89484a8b112ee39aa",//id
        "blfy": "�в���ȷ��",//������Ӧ
        "drugName": "��Ī���ֿ���ά���(7:1)Ƭ",//ҩƷ����
        "ggxh": "0.375g(C16H19N3O5S 0.25g��C8H9NO5 0.125g)",//����ͺ�
        "jj": "�в���ȷ��",//����
        "manu": "�汱��������ҩ�ɷ����޹�˾",//������ҵ
        "price": "13.20",//�ο��۸�
        "pzwh": "��ҩ׼��H20051710",//��׼�ĺ�
        "type": "",//ҩƷ���
        "xz": "��Ʒ��ɫ��Բ�α�Ĥ��Ƭ����ȥ���º��԰�ɫ�����ɫ��ζ΢�࣬����ʪ�ԡ�",//��״
        "yfyl": "�ڷ��� 1.���˺�12������С����һ��1Ƭ��һ��3��,���ظ�Ⱦʱ�����ɼӱ��� 2.δ�����¼�飬���������ڲ�����14�ա�",//�÷�����
        "ywxhzy": "��������ҩ��ͬʱʹ�ÿ��ܻᷢ��ҩ���໥���ã���������ѯҽʦ��ҩʦ��",//ҩ���໥����
        "yxq": "�ݶ�24����",//��Ч��
        "zc": "",//����
        "img": "",//ͼƬ��ַ����Ч��30�졣�����������ر��棬���ⶪʧ
        "zxbz": "",//ִ�б�׼
        "zysx": "",//ע������
        "zycf": "",//��Ҫ�ɷ�
        "zzjb": ""//���μ���
      }
    ]
  }
}
```

����ҩƷ���������ʾ��

```java
	public static void main(String[] args) {
	    String host = "https://jumbarcode.market.alicloudapi.com";
	    String path = "/tmcx/drug/detail";
	    String method = "POST";
	    String appcode = "���Լ���AppCode";
	    Map<String, String> headers = new HashMap<String, String>();
	    //�����header�еĸ�ʽ(�м���Ӣ�Ŀո�)ΪAuthorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //����API��Ҫ�󣬶������Ӧ��Content-Type
	    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    Map<String, String> bodys = new HashMap<String, String>();
	    bodys.put("pzwh", "pzwh");
	    bodys.put("drugId", "drugId");


	    try {
	    	/**
	    	* ��Ҫ��ʾ����:
	    	* HttpUtils���
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* ����
	    	*
	    	* ��Ӧ�����������
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	System.out.println(response.toString());
	    	//��ȡresponse��body
	    	//System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

```

## �ӿ���Ϣ

���õ�ַ��https://jumbarcode.market.alicloudapi.com/tmcx/drug/detail*��*

����ʽ��POST

�������ͣ�JSON

API ���ã�[API �������֤���÷�����APPCODE��](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)[API ǩ����֤���÷�����AppKey & AppSecret��](https://help.aliyun.com/zh/api-gateway/traditional-api-gateway/use-cases/call-apis)

��ȡ��֤��[AppKey & AppCode](https://market.console.aliyun.com/?productCode=cmapi00049717)

## �ӿڲ���

���������Header��

���������Query��

���������Body��

| �ֶ�����          | ���� | �ֶ�����                                                     |
| :---------------- | :--- | :----------------------------------------------------------- |
| pzwh **string**   | N    | ��׼�ĺ�,Ʃ�磺��ҩ׼��H20045760��ҩƷID����׼�ĺ�����Ҫ����һ�� |
| drugId **string** | N    | ҩƷID��ҩƷ�����ӿڷ��ص�ID                                 |

����ҩƷ����ĳɹ���Ӧ

```
{
  "code": 200,//�����룬���������˵��
  "msg": "�ɹ�",//�������Ӧ����
  "taskNo": "017844227229319772912580",//���������
  "data": {
    "id": "5d6e69fa9484a4e7842d9cd6",//id
    "blfy": "xxxx",//������Ӧ
    "drugName": "��Ī���ֽ���",//ҩƷ����
    "ggxh": "0.25g*50��",//����ͺ�
    "img": "xxx",//ͼƬ��ַ����Ч��30�졣�����������ر��棬���ⶪʧ
    "jj": "��ù�ع�������ù��Ƥ���������Ի��߽��á�",//����
    "manu": "ʥ��(�żҿ�)ҩҵ���޹�˾",//������ҵ
    "price": "14.50",//�ο��۸�
    "pzwh": "��ҩ׼��H13020473",//��׼�ĺ�
    "syz": "xxxx",//��Ӧ֢
    "type": "",//ҩƷ���
    "xz": "��Ʒ������Ϊ��ɫ����ɫ��ĩ�������",//��״
    "yfyl": "xxx",//�÷�����
    "ywxhzy": "xxxx",//ҩ���໥����
    "yxq": "24����",//��Ч��
    "zc": "�ڹ⣬�ܷⱣ�档",//����
    "zxbz": "�й�ҩ��2010������",//ִ�б�׼
    "zysx": "xxxx",//ע������
    "zzjb": "xxxx",//���μ���
    "zycf": "��Ī���֡�" //��Ҫ�ɷ�
  }
}
```

