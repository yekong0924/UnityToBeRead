package com.weavernorth.datadeal.demo.controller;

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


@RequestMapping(value="/receiveResponseData",method= RequestMethod.POST)
public Map<String,String> receiveTodoRequest(@RequestBody Map<String, String> mapData) {
    // 返回参数
    Map<String,String> mapReturn=new HashMap<String,String>();
    System.out.println(mapData.toString());
    try {

    } catch (Exception e) {
        log.error(e.getMessage(), e);
    }
    return mapReturn;
}
}
