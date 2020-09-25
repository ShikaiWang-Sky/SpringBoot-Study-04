package com.wang.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        HashMap<String, String> initParameters = new HashMap<>();

        //登录的key 是固定的 loginUsername loginPassword
        initParameters.put("loginUsername", "admin");
        initParameters.put("loginPassword", "123456");

        //允许谁可以访问, 值为空则所有人都可以访问
        initParameters.put("allow", "");

        bean.setInitParameters(initParameters);

        return bean;
    }

    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>(new WebStatFilter());

        HashMap<String, String> initParameters = new HashMap<>();

        initParameters.put("exclusions", "*.js, *.css, /druid/*");

        bean.setInitParameters(initParameters);

        return bean;
    }
}
