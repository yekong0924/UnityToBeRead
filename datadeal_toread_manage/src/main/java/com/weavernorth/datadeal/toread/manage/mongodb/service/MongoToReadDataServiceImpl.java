package com.weavernorth.datadeal.toread.manage.mongodb.service;

import com.alibaba.fastjson.JSONObject;
import com.weavernorth.datadeal.toread.manage.mongodb.dao.MongoToReadDataDao;
import com.weavernorth.datadeal.toread.manage.mongodb.pojo.MongoToReadDataObject;
import com.weavernorth.datadeal.toread.manage.util.DealDataUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.weaver.com.cn
 *
 * @version V1.0
 * @Title: dealDataServiceImpl
 * @Package com.weavernorth.datadeal.toread.manage.mongodb.service
 * @Description:(用一句话描述该文件做什么)
 * @author: liupeng(1203153229 @ qq.com)
 * @date: 2020/7/6 17:43
 * @Copyright: 2020 www.weaver.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于上海泛微网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service("MongoToReadDataService")
public class MongoToReadDataServiceImpl implements MongoToReadDataService {
@Autowired
private DealDataUtil dealDataUtil;
@Autowired
private MongoToReadDataDao mongoToReadDataDao;
    /**
     *@描述 将数据新增至MongoDb中
     *@方法名称  insertDataByMap
     *@参数  [reqMapData]
     *@返回值  com.alibaba.fastjson.JSONObject
     *@创建人  liupeng(1203153229@qq.com)
     *@创建时间  2020/7/8
     *@修改人和其它信息

     */
    @Override
    public JSONObject insertToReadDataByMap(Map<String, String> reqMapData){
        JSONObject rspJSONObject=new JSONObject();
        rspJSONObject.put("returnCode","0000");
        rspJSONObject.put("returnMsg","成功");
            try {
                MongoToReadDataObject mongoToReadDataObjectBO=new MongoToReadDataObject();
                BeanUtils.populate(mongoToReadDataObjectBO, reqMapData);
                //数据 flowid
                String flowId=mongoToReadDataObjectBO.getFlowId();
                //数据系统标识
                String sysCode=mongoToReadDataObjectBO.getSysCode();
                //获取集合名称，将数据插入到指定集合
                String dataType=mongoToReadDataObjectBO.getDateType();
                //获取接收人账号信息
                String receiver=mongoToReadDataObjectBO.getReceiver();
                //待阅系统标识 0
                String isRemark="0";
                //mongodb数据主键
                String  mongdbDataID="";
                mongoToReadDataObjectBO.setIsRemark(isRemark);
                if(!"".equals(dataType)){
                    //根据数据主键判断数据是否存在
                      mongdbDataID= mongoToReadDataDao.findByQuery(flowId,sysCode,isRemark,receiver,dataType);
                    if(!"".equals(mongdbDataID)){
                        rspJSONObject.put("returnCode","8888");
                        rspJSONObject.put("returnMsg","系统:"+sysCode+"-->flowId:"+flowId+"[新增待阅失败,待阅数据已存在!]");
                    }else {
                        mongoToReadDataDao.insert(mongoToReadDataObjectBO,dataType);
                    }
                }else{
                    rspJSONObject.put("returnCode","8888");
                    rspJSONObject.put("returnMsg","系统:"+sysCode+"-->flowId:"+flowId+"[新增待阅失败,数据datatype为空]");
                }
            }catch (Exception e){
                rspJSONObject.put("returnCode","9999");
                rspJSONObject.put("returnMsg","新增待阅数据接口异常:"+e.getMessage());
            }

        return rspJSONObject;
    }

    @Override
    public JSONObject updateToReadDataByMap(Map<String, String> reqMapData) {
        JSONObject rspJSONObject=new JSONObject();
        rspJSONObject.put("returnCode","0000");
        rspJSONObject.put("returnMsg","成功");
        try {
            MongoToReadDataObject mongoToReadDataObjectBO=new MongoToReadDataObject();
            BeanUtils.populate(mongoToReadDataObjectBO, reqMapData);
            //数据 flowid
            String flowId=mongoToReadDataObjectBO.getFlowId();
            //数据系统标识
            String sysCode=mongoToReadDataObjectBO.getSysCode();
            //获取集合名称，将数据插入到指定集合
            String dataType=mongoToReadDataObjectBO.getDateType();
            //获取接收人账号信息
            String receiver=mongoToReadDataObjectBO.getReceiver();
            //待阅系统标识 0
            String isRemark="0";
            //已阅标识
            String doneRemark="2";
            //mongodb数据主键
            String  mongdbDataID="";
            if(!"".equals(dataType)){
                //根据数据主键判断数据是否存在
                  mongdbDataID= mongoToReadDataDao.findByQuery(flowId,sysCode,isRemark,receiver,dataType);
                if("".equals(mongdbDataID)){
                    rspJSONObject.put("returnCode","8888");
                    rspJSONObject.put("returnMsg","系统:"+sysCode+"-->flowid:"+flowId+"[更新已阅数据失败,待阅数据不存在!]");
                }else {
                   //更新数据状态为2
                    mongoToReadDataDao.updateDataByMongoDBId(mongdbDataID,doneRemark,dataType);
                }
            }else{
                rspJSONObject.put("returnCode","8888");
                rspJSONObject.put("returnMsg","系统:"+sysCode+"-->flowid:"+flowId+"[更新已阅数据失败,数据datatype为空]");
            }
        }catch (Exception e){
            rspJSONObject.put("returnCode","9999");
            rspJSONObject.put("returnMsg","更新已阅数据接口异常:"+e.getMessage());
        }

        return rspJSONObject;
    }

    @Override
    public JSONObject deleteDataByMap(Map<String, String> reqMapData) {
        return null;
    }

    @Override
    public JSONObject insertDataByJson(JSONObject reqJsonObjectData) {
        return null;
    }

    @Override
    public JSONObject updateDataByJson(JSONObject reqJsonObjectData) {
        return null;
    }

    @Override
    public JSONObject deleteDataByJson(JSONObject reqJsonObjectData) {
        return null;
    }
    /**根据第三方系统标识获取回调接口信息
     *@描述
     *@方法名称  getResponseConfig
     *@参数  [reqSysCode]
     *@返回值  com.alibaba.fastjson.JSONObject
     *@创建人  liupeng(1203153229@qq.com)
     *@创建时间  2020/7/10
     *@修改人和其它信息

     */
    @Override
    public JSONObject getResponseConfig(String reqSysCode) {
        return null;
    }


}
