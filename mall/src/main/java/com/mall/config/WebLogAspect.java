package com.mall.config;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

import com.mall.tools.IPUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: web接口日志 spring boot aop 方法请求参数日志打印
 */
@Aspect
@Component
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * 申明一个切点 里面是 execution表达式
     */
    @Pointcut("execution( * com.mall.controller..*.*(..))")//两个..代表所有controller子目录，最后括号里的两个..代表所有参数
    public void logPointCut() {
    }

    /**
     *请求method前打印内容
     * @param joinPoint
     * @throws Throwable
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("===============aop WebLogAspect记录日志开始===============");
        logger.info("请求地址 : " + request.getRequestURL().toString());
        logger.info("请求方式 : " + request.getMethod());
        logger.info("IP : " + IPUtils.getIpAdrress(request));// 获取真实的ip地址
        logger.info("请求类方法 :  : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        logger.info("请求类方法参数 : " + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 请求method后打印内容
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "logPointCut()")// returning的值和doAfterReturning的参数名一致
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容(返回值太复杂时，打印的是物理存储空间的地址)
        logger.debug("返回值 : " + ret);
        logger.info("===============aop WebLogAspect记录日志结束===============\r\n");
    }

    /**
     * 环绕增强
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ob = pjp.proceed();// ob 为方法的返回值
        logger.info("耗时 : " + (System.currentTimeMillis() - startTime));
        return ob;
    }
}
