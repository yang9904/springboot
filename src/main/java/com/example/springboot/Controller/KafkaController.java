package com.example.springboot.Controller;

import com.example.springboot.common.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KafkaController {

    private final static String TOPIC_NAME = "my-replicated-topic";

    @Autowired
    private Producer producer;

    @RequestMapping("/send")
    public String send(@RequestParam("msg") String msg) {
        producer.send(msg);
        return "redirect:listCategory";
    }
}
