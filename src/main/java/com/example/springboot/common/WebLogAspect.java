package com.example.springboot.common;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class WebLogAspect {

    private final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect .class);

    @Pointcut("@annotation(com.example.springboot.common.WebLog)")
    public void WebLog() {}

    @Around("WebLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        //打印出参
        LOGGER.info("Response Args : {}", JSON.toJSONString(result));
        //执行耗时
        LOGGER.warn("Time-Consuming : {} ms", System.currentTimeMillis()-startTime);
        return result;
    }

    @Before("WebLog()")
    public void doBefore(JoinPoint joinPoint) {
        //开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //打印相关请求参数
        LOGGER.info("=============== Start ===============");
        // 打印请求 url
        LOGGER.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        LOGGER.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        LOGGER.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        LOGGER.info("IP             : {}", request.getLocalAddr());
        // 打印请求入参
        LOGGER.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    @After("WebLog()")
    public void doAfter() throws Throwable{
        //接口结束后换行，方便分割查看
        LOGGER.info("=============== End ===============");
    }

}
