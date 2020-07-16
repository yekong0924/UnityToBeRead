package com.weavernorth.datadeal.toread.manage.listener;

import com.weavernorth.datadeal.toread.manage.config.RabbitMQConfig;
import com.weavernorth.datadeal.toread.manage.service.DealDataMiddleService;
import com.weavernorth.datadeal.toread.manage.util.WriteExceptionLogUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.weaver.com.cn
 *
 * @version V1.0
 * @Title: ExceptionListen
 * @Package com.weavernorth.datadeal.toread.manage.listener
 * @Description:异常信息处理队列
 * @author: liupeng(1203153229 @ qq.com)
 * @date: 2020/7/6 18:59
 * @Copyright: 2020 www.weaver.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于上海泛微网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Component
public class ExceptionMqListen {
    private static final Logger log = LoggerFactory.getLogger(ExceptionMqListen.class);

    @Autowired
    private DealDataMiddleService dealDataMiddleService;
    //异常数据重推次数
    public static int reTryTimes;

    //获取配置文件中的值
    @Value("${task.errordata.retry.times}")
    public void setReTryTimes(int intTimes) {
        ExceptionMqListen.reTryTimes = intTimes;
    }

    /**
     * 处理异常错误
     *
     * @param mapData 队列中放入的Map
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_EXCEPTION)
    public void receiveRequestInfoByJson(Map<String, String> mapData) {
        try {
            int intTimes = Integer.parseInt(mapData.get("times")) + 1;
            if (intTimes <= reTryTimes) {
                intTimes = intTimes + 1;
                if ("insertToReadRequestByMap".equals(mapData.get("produce"))||"updateToReadRequestByMap".equals(mapData.get("produce"))) {
                    mapData.put("times", intTimes + "");
                    dealDataMiddleService.receiveToReadRequestByMap(mapData, mapData.get("function"));
                }
            } else {
                // 如果超出重试次数，则将错误数据写入到错误日志文件中。
                WriteExceptionLogUtil.writeLog(mapData.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
