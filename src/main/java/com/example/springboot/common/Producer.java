package com.example.springboot.common;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //发送消息方法
    public void send(String msg) {
        kafkaTemplate.send("test", msg);
    }
}
