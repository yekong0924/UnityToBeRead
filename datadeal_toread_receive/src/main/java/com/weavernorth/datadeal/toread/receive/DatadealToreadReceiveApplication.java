package com.weavernorth.datadeal.toread.receive;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//开启基于注解的RabbitMQ模式
@EnableRabbit
public class DatadealToreadReceiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatadealToreadReceiveApplication.class, args);
    }

}
