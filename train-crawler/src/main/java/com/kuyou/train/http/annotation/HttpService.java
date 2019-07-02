package com.kuyou.train.http.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * Created by liujia33 on 2017/12/24.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface HttpService {

    String value() default "";

}
