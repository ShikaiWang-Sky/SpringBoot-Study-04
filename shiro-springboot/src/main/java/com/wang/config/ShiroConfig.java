package com.wang.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean : 3
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //关联SecurityManager, 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的内置过滤器
        /*
            anon: 无需认证, 就可以访问
            authc: 必须认证了才能访问
            user: 必须拥有 记住我 功能才能访问
            perms: 拥有对于某个资源的权限才能访问
            role: 拥有某个角色权限才能访问
         */
        //拦截
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

//        filterChainDefinitionMap.put("/user/add", "authc");
//        filterChainDefinitionMap.put("/user/update", "authc");
        //授权
        filterChainDefinitionMap.put("/user/add", "perms[user:add]");
        filterChainDefinitionMap.put("/user/update", "perms[user:update]");

        filterChainDefinitionMap.put("/user/*", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        //设置登录的请求
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        //未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noauth");


        return shiroFilterFactoryBean;
    }

    //DefaultWebSecurityManager : 2
    //@Qualifier() 利用bean的id注入, 在注解托管中即为方法名
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //关联UserRealm
        securityManager.setRealm(userRealm);

        return securityManager;
    }

    //创建realm对象, 需要自定义 : 1
    //将自己定义的Realm注册为Bean, 被SpringBoot托管
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    //整合ShiroDialect: 用来整合 shiro thymeleaf
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
