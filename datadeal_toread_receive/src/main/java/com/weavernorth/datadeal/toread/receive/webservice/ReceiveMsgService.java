package com.weavernorth.datadeal.toread.receive.webservice;

import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Map;

@Service
@WebService
public interface ReceiveMsgService {
    /**
     * Map形式，接收异构系统调用的统一待阅接口信息。
     * @param mapData 统一待办信息数据
     * @return 返回Map形式的报文
     */
    @WebMethod
    Map<String,String> receiveToReadRequestByMap(Map<String, String> mapData);
}
