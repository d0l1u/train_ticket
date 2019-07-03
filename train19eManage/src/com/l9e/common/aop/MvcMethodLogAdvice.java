package com.l9e.common.aop;

import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.util.JvmUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author meizs
 * @create 2018-04-17 14:38
 **/
@Aspect
@Component
public class MvcMethodLogAdvice {

    private static Logger log = Logger.getLogger(MvcMethodLogAdvice.class);


    @Before("execution(* com.l9e.transaction.controller.*.*(..))")
    public void beforeAdvice() throws Throwable {

        //   log.info("执行前当前内存占有比率：" + JvmUtil.getUseRateMemoryMXBean() * 100 + "%");

    }


    @Around("execution(* com.l9e.transaction.controller.*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object args[] = joinPoint.getArgs();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();

            //    log.info("【className】::" + className + ",【methodName】::" + methodName);


            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();

            LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

            if (null != loginUserVo) {
                StringBuilder builderStr = new StringBuilder();
                builderStr.append(loginUserVo.getReal_name() + "|" + loginUserVo.getUser_name() + "|" + loginUserVo.getPassword() + "|")
                        .append(className + "|" + methodName).append("当前内存占比|" + JvmUtil.getUseRateMemoryMXBean() * 100 + "%");
                log.info("操作记录：" + builderStr);
            } else {
                log.info("操作记录： 当前用户session为空");
            }



           /* Enumeration enu = request.getParameterNames();
            StringBuilder params = new StringBuilder();
            while (enu.hasMoreElements()) {
                String paraName = (String) enu.nextElement();
                params.append(paraName).append("=").append(request.getParameter(paraName)).append("&");
            }
            String paramStr = params.substring(0, params.length() - 1);
            // 获取请求地址
            StringBuffer requestPath = request.getRequestURL();
            log.info("【requestPath】::" + requestPath + ",【paramStr】::" + paramStr);*/


            // 获取输入参数
           /* Map map = request.getParameterMap();
            StringBuilder params = new StringBuilder();
            Set keSet = map.entrySet();
            for (Iterator itr = keSet.iterator(); itr.hasNext(); ) {
                Map.Entry me = (Map.Entry) itr.next();
                Object ok = me.getKey();
                Object ov = me.getValue();
                String[] value = new String[1];
                if (ov instanceof String[]) {
                    value = (String[]) ov;
                } else {
                    value[0] = ov.toString();
                }

                for (int k = 0; k < value.length; k++) {
                    params.append(ok).append("=").append(value[k]).append("&");
                }
            }
            String paramStr = params.substring(0, params.length() - 1);
            // 获取请求地址
            StringBuffer requestPath = request.getRequestURL();
            log.info("【requestPath】::" + requestPath + ",【paramStr】::" + paramStr);*/
        } catch (Throwable e) {
            log.info(e);
        }

        try {
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            log.info("+++++around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
            return result;

        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            log.info("+++++around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
            throw e;
        }

    }


    @After("execution(* com.l9e.transaction.controller.*.*(..))")
    public void afterAdvice() throws Throwable {

        //   log.info("执行后当前内存占有比率：" + JvmUtil.getUseRateMemoryMXBean() * 100 + "%");

    }


}
