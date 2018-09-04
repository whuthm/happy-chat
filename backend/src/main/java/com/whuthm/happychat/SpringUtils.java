package com.whuthm.happychat;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    public void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        if (ctx == null) {
            throw new RuntimeException("spring 上下文对象未初始化，无法完成bean的查找！");
        }

        return ctx.getBean(requiredType);
    }

    public static Object getBean(String name) throws BeansException {
        if (ctx == null) {
            throw new RuntimeException("spring 上下文对象未初始化，无法完成bean的查找！");
        }

        return ctx.getBean(name);
    }

    /**
     * @return the ctx
     */
    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
}
