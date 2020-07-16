package com.weavernorth.datadeal.toread.receive.controller;

import com.weavernorth.datadeal.toread.receive.webservice.ReceiveMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.weaver.com.cn
 *
 * @version V1.0
 * @Title: ReceiveMsgController
 * @Package com.weavernorth.datadeal.toread.receive.controller
 * @Description:(用一句话描述该文件做什么)
 * @author: liupeng(1203153229 @ qq.com)
 * @date: 2020/7/6 10:56
 * @Copyright: 2020 www.weaver.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于上海泛微网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@RestController
@RequestMapping("/request")
public class ReceiveMsgController {
    private static final Logger log = LoggerFactory.getLogger(ReceiveMsgController.class);

    @Autowired
    private ReceiveMsgService receiveMsgService;
    /**
 * 接收待阅流程
 * @param mapData
 * 	{
 * 		"syscode":"0001",
 * 		"flowid":"A00000000007",
 * 		"requestname":">&#x5458;&#x5DE5;&#x8BF7;&#x5047;&#x6D41;&#x7A0B;_0001_-1_A00000000007_luffy",
 *		"workflowname":"&#x5458;&#x5DE5;&#x8BF7;&#x5047;&#x6D41;&#x7A0B;",
 * 		"nodename":"&#x67E5;&#x770B;&#x8282;&#x70B9;",
 * 		"pcurl":"/showtask.aspx?id=A00000000007",
 * 		"appurl":"/showtask.aspx?id=A00000000007",
 * 		"creator":"wld",
 * 		"createdatetime":"2019-3-10 12:00:00",
 * 		"receiver":"luffy",
 * 		"receivedatetime":"2019-3-10 12:00:00"
 * 	}
 * @return
 */
@RequestMapping(value="/receiveToReadRequest",method= RequestMethod.POST)
public Map<String,String> receiveTodoRequest(@RequestBody Map<String, String> mapData) {
    // 返回参数
    Map<String,String> mapReturn=new HashMap<String,String>();

    try {
        mapReturn=receiveMsgService.receiveToReadRequestByMap(mapData);
    } catch (Exception e) {
        log.error(e.getMessage(), e);
    }
    return mapReturn;
}
}
