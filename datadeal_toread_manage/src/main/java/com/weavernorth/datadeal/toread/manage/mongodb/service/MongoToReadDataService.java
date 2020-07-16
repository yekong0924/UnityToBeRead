package com.weavernorth.datadeal.toread.manage.mongodb.service;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface MongoToReadDataService {
    JSONObject  insertToReadDataByMap(Map<String,String> reqMapData) throws InvocationTargetException, IllegalAccessException;
    JSONObject  updateToReadDataByMap(Map<String,String> reqMapData);
    JSONObject  deleteDataByMap(Map<String,String> reqMapData);
    JSONObject insertDataByJson(JSONObject reqJsonObjectData);
    JSONObject updateDataByJson(JSONObject reqJsonObjectData);
    JSONObject deleteDataByJson(JSONObject reqJsonObjectData);
    /**
     *@描述 根据系统标识获取 第三方系统回调接口信息
     *@方法名称  getResponseConfig
     *@参数  [reqSysCode]
     *@返回值  com.alibaba.fastjson.JSONObject
     *@创建人  liupeng(1203153229@qq.com)
     *@创建时间  2020/7/10
     *@修改人和其它信息

     */
    JSONObject getResponseConfig(String reqSysCode);

}
