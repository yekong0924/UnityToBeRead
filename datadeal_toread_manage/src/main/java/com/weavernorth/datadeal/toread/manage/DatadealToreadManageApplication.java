package com.weavernorth.datadeal.toread.manage;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//开启计划任务的支持
@EnableScheduling
//开启基于注解的RabbitMQ模式
@EnableRabbit
//@PropertySource是为了方便引入properties配置文件提供的一个注解 可以标注在启动类上
@PropertySource(value = {"classpath:responseInfo.properties"})
public class DatadealToreadManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatadealToreadManageApplication.class, args);
    }
}
