package com.easy.cloud.cache.redis.aspect;

import com.easy.cloud.cache.redis.selector.RedisProxySelector;
import com.easy.cloud.basic.util.Assert;
import com.easy.cloud.cache.redis.pojo.bo.RedisBO;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * <p>
 * redis切面类
 * </p>
 *
 * @author daiqi 创建时间 2018年4月12日 下午1:51:49
 */
@Aspect
@Order
//@Component
@Data
public class RedisAspect {
    private RedisProxySelector redisProxySelector;

    public RedisAspect(RedisProxySelector redisProxySelector) {
        Assert.verifyObjNull(redisProxySelector, "redis代理选择器:redisProxySelector");
        this.redisProxySelector = redisProxySelector;
    }

    @Pointcut("@annotation(com.easy.cloud.cache.redis.annotation.RedisAnnotation)")
    public void redisAspect() {

    }

    public void daoAspect() {

    }

    @Around(value = "redisAspect()")
    public Object annotationAround(ProceedingJoinPoint joinPoint) {
        return doAround(joinPoint);
    }


    public Object doAround(ProceedingJoinPoint joinPoint) {
        RedisBO redisBO = new RedisBO();
        redisBO.buildRedisData(joinPoint).buildRedisProxySelector(redisProxySelector);
        return redisBO.handle();
    }

    @AfterThrowing(value = "redisAspect()", throwing = "ex")
    public void afterThrowing(Throwable ex) {
        throw new RuntimeException(ex);
    }
}
