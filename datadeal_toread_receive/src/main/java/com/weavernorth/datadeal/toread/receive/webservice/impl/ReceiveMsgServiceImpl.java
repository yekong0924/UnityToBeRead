package com.weavernorth.datadeal.toread.receive.webservice.impl;

import com.weavernorth.datadeal.toread.receive.config.RabbitMQConfig;
import com.weavernorth.datadeal.toread.receive.webservice.ReceiveMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * All rights Reserved, Designed By www.weaver.com.cn
 *
 * @version V1.0
 * @Title: ReceiveMsgServiceImpl
 * @Package com.weavernorth.datadeal.toread.receive.webservice.impl
 * @Description:异构系统统一待阅接口。接收消息，传递到MQ中
 * @author: liupeng(1203153229 @ qq.com)
 * @date: 2020/7/6 11:04
 * @Copyright: 2020 www.weaver.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于上海泛微网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@WebService(targetNamespace = "http://UnifyRequest.weavernorth.com/", endpointInterface = "com.weavernorth.datadeal.toread.receive.webservice.ReceiveMsgService")
public class ReceiveMsgServiceImpl implements ReceiveMsgService, RabbitTemplate.ConfirmCallback {
    private static final Logger log = LoggerFactory.getLogger(ReceiveMsgServiceImpl.class);
    //对于发送消息：我们一般可以使用 RabbitTemplate，这个是 Spring 封装给了我们，便于我们发送信息，我们调用 rabbitTemplate.convertAndSend("spring-boot", xxx); 即可发送信息

    //由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public ReceiveMsgServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setConfirmCallback(this);
    }
    /**
     * Map形式接收异构系统调用的统一待阅接口信息。
     * 消息生产者，把接收到的Map放到MQ队列中。
     *
     * @param mapData 统一待办信息数据
     * @return 返回Map形式的报文
     */
    @Override
    public Map<String, String> receiveToReadRequestByMap(Map<String, String> mapData) {
        // 获取开始时间
        long longStartTime = System.currentTimeMillis();

        // 预先声明，返回的Map对象
        Map<String, String> mapReturn = new HashMap<String, String>();
        mapReturn.put("syscode", mapData.get("sysCode"));
        mapReturn.put("flowid", mapData.get("flowId"));
        mapReturn.put("dateType", "ofsToRead");
        mapReturn.put("operType", "AutoNew");
        mapReturn.put("operResult", "0");
        mapReturn.put("message", "");
        mapReturn.put("function", "receiveToReadRequestByMap");
        //异常处理次数 接收处理服务需要根据times进行重新推送
        mapData.put("times","0");
        try {
        //CorrelationData对象的作用是作为消息的附加信息传递，通常我们用它来保存消息的自定义id
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXECHANGE_RECEIVE_READDATA, RabbitMQConfig.ROUTING_RECEIVE_READDATA, mapData,correlationId);
        mapReturn.put("operResult", "1");
        long longEndTime = System.currentTimeMillis();    //获取结束时间
        log.info("待阅消息Map发送成功！receiveToReadRequestByMap(第1步，调用WebService接口，将数据放入MQ。ID:"+correlationId.getId()+")耗时："+(longEndTime - longStartTime)+" ms");
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            mapReturn.put("message", e.getMessage());
        }
        return mapReturn;
    }
    /**
     * 回调反馈消费者消费信息
     * @param correlationData  消息相关的数据，一般用于获取 唯一标识 id
     * @param b true 消息确认成功，false 失败
     * @param msg 确认失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String msg) {
// 获取开始时间
        long longStartTime = System.currentTimeMillis();
        log.info(" 回调id:" + correlationData);
        if (b) {
            log.info("消息成功消费");
        } else {
            log.info("消息消费失败:" + msg);
        }
        long longEndTime = System.currentTimeMillis();    //获取结束时间
        log.info("回调ID("+correlationData.getId()+")！confirm 耗时："+(longEndTime - longStartTime)+" ms");
    }
}
