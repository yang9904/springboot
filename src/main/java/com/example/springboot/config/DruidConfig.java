package com.example.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Druid连接池配置
 */
@Configuration
public class DruidConfig {

    @Value("${spring.datasource.username}")
    private String loginUsername;

    @Value("${spring.datasource.password}")
    private String loginPassword;

    //加载application.yaml中的Druid配置
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid() {
        return new DruidDataSource();
    }

    //配置Druid的监控
    //1、配置一个管理后台的Servlet
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();

        initParams.put("loginUsername", loginUsername);// druid的密码
        initParams.put("loginPassword", loginPassword); // druid的用户名
        initParams.put("allow", ""); // 默认就是允许所有访问 IP白名单 (没有配置或者为空，则允许所有访问)
        initParams.put("deny", "");  // IP黑名单 (存在共同时，deny优先于allow)

        bean.setInitParameters(initParams);
        return bean;
    }

    /**
     * 配置一个web监控的filter
     * @return
     */
    @Bean
    public FilterRegistrationBean webStatFilter() {

        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        Map<String, String> initParams = new HashMap<>(1);
        // 设置忽略请求
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        bean.setInitParameters(initParams);
        bean.addInitParameter("profileEnable", "true");
        bean.addInitParameter("principalCookieName", "USER_COOKIE");
        bean.addInitParameter("principalSessionName", "");
        bean.addInitParameter("aopPatterns", "com.example.demo.service");
        // 验证所有请求
        bean.addUrlPatterns("/*");
        return bean;
    }
}