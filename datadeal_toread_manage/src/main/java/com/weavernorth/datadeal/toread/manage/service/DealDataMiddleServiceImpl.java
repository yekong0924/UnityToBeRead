package com.weavernorth.datadeal.toread.manage.service;

import com.alibaba.fastjson.JSONObject;
import com.weavernorth.datadeal.toread.manage.config.RabbitMQConfig;
import com.weavernorth.datadeal.toread.manage.mongodb.service.MongoToReadDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * All rights Reserved, Designed By www.weaver.com.cn
 *
 * @version V1.0
 * @Title: DealDataMiddleServiceImpl
 * @Package com.weavernorth.datadeal.toread.manage.service
 * @Description:(用一句话描述该文件做什么)
 * @author: liupeng(1203153229 @ qq.com)
 * @date: 2020/7/8 12:30
 * @Copyright: 2020 www.weaver.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于上海泛微网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service("DealDataMiddleService")
public class DealDataMiddleServiceImpl implements DealDataMiddleService {

    private static final Logger log = LoggerFactory.getLogger(DealDataMiddleServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MongoToReadDataService mongoToReadDataService;

    //由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public DealDataMiddleServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }

    @Override
    public void receiveToReadRequestByMap(Map<String, String> mapData, String strApi) {
        log.info("开始处理-接收列表-任务");
        long start = System.currentTimeMillis();
        log.info("请求参数:" + mapData.toString());
        //定义返回值
        JSONObject rspJsonData = new JSONObject();
        //获取接口返回值

        try {
            //根据参数判断调用接口名称 可以进行接口名称判断调用那个接口
            if ("insertToReadRequestByMap".equals(strApi)) {
                //调用数据新增接口 将数据存储至MongoDB中
                rspJsonData = mongoToReadDataService.insertToReadDataByMap(mapData);
            }else if ("updateToReadRequestByMap".equals(strApi)) {
                //调用数据新增接口 将数据存储至MongoDB中
                rspJsonData = mongoToReadDataService.updateToReadDataByMap(mapData);
            }
            String rspJsonDataCode=rspJsonData.getString("returnCode");
            //rspJsonDataCode 0000 成功，8888失败(数据异常)，9999异常
            if (rspJsonDataCode.equals("0000")||rspJsonDataCode.equals("8888")) {
                rspJsonData.put("flowId", mapData.get("flowid"));
                rspJsonData.put("function", strApi);
                rspJsonData.put("sysCode", mapData.get("sysCode"));
                rspJsonData.put("receiver", mapData.get("receiver"));
                log.info("OA返回rspJsonData:" + rspJsonData);
                CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
                // 把接口返回的结果放入反馈队列
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXECHANGE_RESPONSE_TODO,
                        RabbitMQConfig.ROUTING_RESPONSE_TODO,
                        rspJsonData, correlationId);
                log.info(strApi + "  反馈rspJsonData发送成功！");
            } else if (rspJsonDataCode.equals("9999")){
                // 异常数据 存储至异常队列中 并尝试重新推送 3次
                int times = Integer.parseInt(mapData.get("times")) + 1;
                mapData.put("function", strApi);
                rspJsonData.put("flowid", mapData.get("flowid"));
                mapData.put("produce", "receiveToReadRequestByMap");
                mapData.put("exception", rspJsonData.getString("returnMsg"));
                mapData.put("times", String.valueOf(times));
                CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXECHANGE_EXCEPTION,
                        RabbitMQConfig.ROUTING_EXCEPTION,
                        mapData, correlationId);
            }
        } catch (Exception e) {
            log.error("数据存储发生异常");
            log.info("e.getCause():" + e.getCause());
            //放入原请求队列继续请求
            int times = Integer.parseInt(mapData.get("times")) + 1;
            mapData.put("function", strApi);
            rspJsonData.put("flowId", mapData.get("flowId"));
            mapData.put("produce", "receiveToReadRequestByMap");
            mapData.put("exception", e.getMessage());
            mapData.put("times", String.valueOf(times));
            CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXECHANGE_EXCEPTION,
                    RabbitMQConfig.ROUTING_EXCEPTION,
                    mapData, correlationId);
            e.printStackTrace();
            log.error(e.getMessage());
        }
        long end = System.currentTimeMillis();
        log.info("完成-接收列表-任务，耗时：" + (end - start) + "毫秒");
    }


    @Override
    public void doTaskReponse(Map<String, String> mapReturn) {
        log.info("开始处理-返回列表-任务");
        long start = System.currentTimeMillis();
        // mapReturn就是队列里的返回值
            try {
                //获取回调系统标识
                String strSysCode = mapReturn.get("sysCode");
                //回调接口类型
                String strResponseMethodType="";
                //回调接口地址
                String strResponseUrl="";
                //当请求为restful时，response_method 值为 get/post, 当请求为webservices时，response_method为 具体方法名称
                if(!"".equals(strSysCode)&&null!=strSysCode){
                    //获取回调接口信息
                    JSONObject responseConfigJsonData=mongoToReadDataService.getResponseConfig(strSysCode);
                    //strResponseUrl=responseConfigJsonData.getString("responseUrl");
                    //strResponseMethodType =responseConfigJsonData.getString("responseMethodType");
                    strResponseUrl="http://127.0.0.1:6870/request/receiveResponseData";
                    strResponseMethodType="post";
                }
                 if (!"".equals(strResponseMethodType)||!"".equals(strResponseUrl)) {
                     @SuppressWarnings("rawtypes")
                     ResponseEntity<Map> re = null;
                     if ("post".equalsIgnoreCase(strResponseMethodType)) {
                         re = restTemplate.postForEntity(strResponseUrl, mapReturn, Map.class);
                     } else if ("get".equalsIgnoreCase(strResponseMethodType)) {
                         re = restTemplate.getForEntity(strResponseUrl, Map.class, mapReturn);
                     }
                     if (re.getStatusCode() == HttpStatus.OK) {
                         log.info("反馈Map发送成功！");
                     } else {
                         log.info("反馈Map发送失败！Http状态码：" + re.getStatusCode());
                     }
                 }
            } catch (Exception e) {
                e.printStackTrace();
            }
        long end = System.currentTimeMillis();
        log.info("完成-返回列表-任务，耗时：" + (end - start) + "毫秒");
    }
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String msg) {
        log.info(" 回调id:" + correlationData);
        if (b) {
            log.info("消息成功消费");
        } else {
            log.info("消息消费失败:" + msg);
        }
    }
}
