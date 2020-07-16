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
 * @Title: ResponseMsgMqListen
 * @Package com.weavernorth.datadeal.toread.manage.listener
 * @Description:回调队列监听
 * @author: liupeng(1203153229 @ qq.com)
 * @date: 2020/7/7 9:48
 * @Copyright: 2020 www.weaver.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于上海泛微网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Component
public class ResponseMsgMqListen {
    private static final Logger log = LoggerFactory.getLogger(ResponseMsgMqListen.class);

    @Autowired
    private DealDataMiddleService dealDataMiddleService;
    /**
     * 新的多队列处理
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_RESPONSE_TODO)
    public void reponseProcessTodo(Map<String,String> mapReturn) {
        try {
            // 获取开始时间
            long longStartTime = System.currentTimeMillis();

            dealDataMiddleService.doTaskReponse(mapReturn);
//	      Thread.currentThread().join();
            long longEndTime = System.currentTimeMillis();    //获取结束时间

            log.info("第3步，回调异构系统接口处理数据。reponseProcessTodo 处理完毕！耗时："+(longEndTime - longStartTime)+" ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
