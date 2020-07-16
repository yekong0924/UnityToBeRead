package com.weavernorth.datadeal.toread.manage.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

public interface DealDataMiddleService extends RabbitTemplate.ConfirmCallback {
    void receiveToReadRequestByMap(Map<String, String> mapData, String strApi);
    void doTaskReponse(Map<String, String> mapReturn);
}
