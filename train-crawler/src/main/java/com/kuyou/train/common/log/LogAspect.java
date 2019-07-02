package com.kuyou.train.common.log;

import com.kuyou.train.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * LogAspect
 *
 * @author taokai3
 * @date 2018/11/2
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * 切入点，切入@Log注解
     */
    @Pointcut(value = "@annotation(com.kuyou.train.common.log.MDCLog)")
    private void pointcut() {
    }


    /**
     * 在方法执行前后
     *
     * @param point
     * @return
     */
    @Around(value = "pointcut() && @annotation(com.kuyou.train.common.log.MDCLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String logId = RandomUtil.getLogId();
        Throwable throwable = null;
        try {
            MDC.put("LOGID", logId);
            return point.proceed();
        } catch (Throwable te) {
            throwable = te;
        } finally {
            MDC.clear();
            if (throwable != null) {
                throw throwable;
            }
        }
        return null;
    }
}
