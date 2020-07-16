package com.weavernorth.datadeal.toread.manage.listener;

import com.weavernorth.datadeal.toread.manage.config.RabbitMQConfig;
import com.weavernorth.datadeal.toread.manage.service.DealDataMiddleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.weaver.com.cn
 *
 * @version V1.0
 * @Title: MsgMqListen
 * @Package com.weavernorth.datadeal.toread.manage.listener
 * @Description:注册为消息的消费者，处理“接收队列”中待处理的“统一待阅消息”
 * @author: liupeng(1203153229 @ qq.com)
 * @date: 2020/7/6 16:49
 * @Copyright: 2020 www.weaver.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于上海泛微网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Component
public class RequetMsgMqListen {
    private static final Logger log = LoggerFactory.getLogger(RequetMsgMqListen.class);
    @Autowired
    private DealDataMiddleService dealDataMiddleService;

    /**
     * 待办-队列消费者
     *
     * @param mapData 队列中放入的Map
     */
    @RabbitListener(queues  = RabbitMQConfig.QUEUE_RECEIVE_TODO)
    public void receiveToReadRequestByMap(Map<String, String> mapData)
    {
        try
        {
            // 获取开始时间
            long longStartTime = System.currentTimeMillis();
            String strApi=mapData.get("dataApi");
            dealDataMiddleService.receiveToReadRequestByMap(mapData, strApi);
            //获取结束时间
            long longEndTime = System.currentTimeMillis();
            log.info("第2步，调用MongoDB接口处理数据。receiveTodoRequestByMap 处理完毕！耗时："+(longEndTime - longStartTime)+" ms");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }



}
