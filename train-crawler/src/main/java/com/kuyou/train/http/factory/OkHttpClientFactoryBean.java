package com.kuyou.train.http.factory;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.FactoryBean;

/**
 * OkHttpClientFactoryBean
 *
 * @author liujia33
 * @date 2018/9/29
 */
public abstract class OkHttpClientFactoryBean implements FactoryBean<OkHttpClient> {

    @Override
    public Class<?> getObjectType() {
        return OkHttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
