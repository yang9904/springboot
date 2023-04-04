package com.example.springboot.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/hello")
    public String hello() {
        LOGGER.trace("hello, I am trace!");
        LOGGER.debug("hello, I am debug!");
        LOGGER.info("hello, I am info!");
        LOGGER.warn("hello, I am {}!", "warn");
        LOGGER.error("hello, I am {}!", "error");
        return "Hello Spring Boot hhhhh!";
    }

}
