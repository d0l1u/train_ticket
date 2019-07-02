package com.kuyou.train.http;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * HttpServiceFactoryBean
 * 主要作用是在Spring中轻松的创建HttpServiceFactory
 *
 * @author liujia33
 * @date 2018/8/16
 */
@Slf4j
@Setter
public class HttpServiceFactoryBean implements InitializingBean, FactoryBean {

    private Retrofit retrofit;

    private Class httpServiceInterface;

    private OkHttpClient okHttpClient;
    private List<Converter.Factory> converterFactories = new ArrayList<>();
    private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
    private Executor callbackExecutor;
    private boolean validateEagerly;

    @Override
    public Object getObject() throws Exception {
        return retrofit.create(httpServiceInterface);
    }

    @Override
    public Class getObjectType() {
        return httpServiceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.retrofit = buildRetrofit();
    }

    private Retrofit buildRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder().client(okHttpClient).baseUrl("https://kyfw.12306.cn")
                .validateEagerly(validateEagerly);

        if (callbackExecutor != null) {
            builder.callbackExecutor(callbackExecutor);
        }

        for (Converter.Factory converterFactory : converterFactories) {
            builder.addConverterFactory(converterFactory);
        }
        for (CallAdapter.Factory callAdapterFactory : callAdapterFactories) {
            builder.addCallAdapterFactory(callAdapterFactory);
        }

        return builder.build();
    }
}
