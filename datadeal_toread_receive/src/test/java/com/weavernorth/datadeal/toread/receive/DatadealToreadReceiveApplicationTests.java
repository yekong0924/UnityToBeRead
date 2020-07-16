package com.weavernorth.datadeal.toread.receive;

import com.alibaba.fastjson.JSONObject;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootTest
class DatadealToreadReceiveApplicationTests {

    @Test
    void contextLoads() {
    }
    public static void main(String[] args) throws Exception
    {
        DatadealToreadReceiveApplicationTests testMain = new DatadealToreadReceiveApplicationTests();
//        TestOAOfs();
        TestMQReceive();
 //       testMain.tetDempRest();
//        testMain.testDemoWebservice();


    }



    /*
     * 测试中间件接口
     * @throws Exception
     */
    public static void TestMQReceive() throws Exception
    {
        Map<String,String> mapData = new HashMap<String, String>();
        Map<String,String> mapResult = new HashMap<String, String>();
        mapData.put("syscode","BZPT");
        mapData.put("flowid","111A");
        mapData.put("requestname","w");
        mapData.put("workflowname","w");
        mapData.put("nodename","w");
        mapData.put("viewtype","1");
        mapData.put("pcurl","w");
        mapData.put("appurl","w");
        mapData.put("creator","26");
        mapData.put("createdatetime","2019-03-26 10:00:00");
        mapData.put("receiver","yangyifeng");
        mapData.put("receivedatetime","2019-03-26 10:00:00");
        // 使用“动态调用”的方式创建WebService客户端，并调用接口
        JaxWsDynamicClientFactory dcflient = JaxWsDynamicClientFactory.newInstance();
        //为了解决:Cannot create a secure XMLInputFactory
        Properties props = System.getProperties();
        props.setProperty("org.apache.cxf.stax.allowInsecureParser", "1");
        props.setProperty("UseSunHttpHandler", "true");

        String url ="http://127.0.0.1:6868/services/receivemsg?wsdl";
        // 创建WebService客户端
        Client client = dcflient.createClient(url);
        // 访问接口方法
        Object[] objects = client.invoke("receiveToReadRequestByMap", mapData);
        // 将返回的结果转换为Map
        if (objects != null && objects.length > 0)
        {
            mapResult = (Map<String, String>) objects[0];
        }
        System.out.println("返回报文:"+mapResult.toString());
        // 把接口返回的结果放入反馈队列
    }


/*
    *//**
     * 测试本地rest回调接口
     *//*
    public  void tetDempRest()
    {
        HttpsClient tHttpsClient = new HttpsClient();

        ResponseEntity<Map> re = null;
        String url = "http://192.168.60.11:8080/api/oa/OACallBack";
        Map reqJson = new JSONObject();
        reqJson.put("syscode","LJTHCM");
        reqJson.put("flowid","12345A");
        reqJson.put("dataType","WfData!");
        reqJson.put("operType","AutoNew");
        reqJson.put("function","receiveTodoRequestByMap");
        reqJson.put("operResult","1");
        reqJson.put("message","流程数据【 员工请假流程_0001_ - 1 _A00000000007_luffy】 自动更新待办成功");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("address","http://192.168.60.39/LJTHCMTEST/WebService/ExternalInterface/LJTHOACallBackWebService.asmx");
        jsonObj.put("type","Webservice");
        jsonObj.put("methodname","CallBack");
        reqJson.put("callback", JSONObject.toJSONString(jsonObj));

        try
        {
            String res = tHttpsClient.post(url, reqJson, null);
            System.out.println(res);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    */
/*
    *//**
     * 测试本地webservice接口
     *//*
    public void testDemoWebservice() throws Exception
    {
        String strURL = "http://127.0.0.1:88/services/CallBackSwevice";
        JSONObject reqJson = new JSONObject();
        reqJson.put("message","成功!");
        String jsonStr = JSONObject.toJSONString(reqJson);
        // 使用“动态调用”的方式创建WebService客户端，并调用接口
        JaxWsDynamicClientFactory dcflient = JaxWsDynamicClientFactory.newInstance();

        // 创建WebService客户端
        Client client = dcflient.createClient(strURL + "?wsdl");
        // 访问接口方法
        String strMethod = "CallBack";
        Object[] objects = client.invoke(strMethod, jsonStr);
        // 将返回的结果转换为Map
        if (objects != null && objects.length > 0)
        {
            System.out.println(objects[0]);
        }
    }

    */
}
