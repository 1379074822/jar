package com.mall.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: shrio配置类
 */
@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * shrio拦截器配置
     * @param securityManager
     * @return
     */
    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        logger.debug("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager); // 必须设置 SecurityManager
        shiroFilterFactoryBean.setLoginUrl("/login");// 设置login URL
        shiroFilterFactoryBean.setSuccessUrl("/index");// 登录成功后要跳转的链接
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");// 未授权的页面

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();// 拦截器

        filterChainDefinitionMap.put("/login","anon");
        filterChainDefinitionMap.put("/getVerify","anon");
        filterChainDefinitionMap.put("/isExitAccount","anon");//登录时 ajax验证用户名是否存在
        filterChainDefinitionMap.put("/report","anon");
        filterChainDefinitionMap.put("/mall/**","anon");
        filterChainDefinitionMap.put("/system/**","anon");
        filterChainDefinitionMap.put("/user/**","anon");
        filterChainDefinitionMap.put("/shop/**","anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/mallStatic/**","anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/layui/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");// 退出系统的过滤器
        filterChainDefinitionMap.put("/", "anon");
        //filterChainDefinitionMap.put("/index","roles[superAdmin]");
        //filterChainDefinitionMap.put("/index1", "roles[admin]");// 现在资源的角色
        //filterChainDefinitionMap.put("/403", "roles[user]");
        filterChainDefinitionMap.put("/403","roles[user]");
        filterChainDefinitionMap.put("/index","roles[superAdmin]");
        filterChainDefinitionMap.put("/**", "authc");// 最后一般都，固定格式

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码; )
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1024);// 散列的次数，比如散列两次，相当于md5(md5(""));
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义realm;
     * @return
     */
    @Bean
    public AdminRealm adminShiroRealm() {
        AdminRealm adminRealm = new AdminRealm();
        return adminRealm;
    }

    /**
     * shrio缓存管理器
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(adminShiroRealm());// 注入自定义的realm;
        return securityManager;
    }

    /**
     * 开启shiro aop注解支持 使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

}
