package com.kuyou.train.http;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import retrofit2.CallAdapter;
import retrofit2.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static org.springframework.util.Assert.notNull;

/**
 * HttpServiceScannerConfigurer
 *
 * @author liujia33
 * @date 2018/9/29
 */
@Slf4j
@Setter
public class HttpServiceScannerConfigurer implements BeanFactoryPostProcessor, InitializingBean,
        ApplicationContextAware {

    private ApplicationContext applicationContext;
    private OkHttpClient okHttpClient;
    private List<Converter.Factory> converterFactories = new ArrayList<>();
    private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
    private Executor callbackExecutor;
    private boolean validateEagerly;
    private String packageNames;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.packageNames, "Property 'packageNames' is required");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("register HTTPBeanScannerConfigurer.");

        ClassPathHttpServiceScanner scanner = new ClassPathHttpServiceScanner((BeanDefinitionRegistry) beanFactory);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setOkHttpClient(this.okHttpClient);
        scanner.setCallAdapterFactories(this.callAdapterFactories);
        scanner.setCallbackExecutor(this.callbackExecutor);
        scanner.setConverterFactories(this.converterFactories);
        scanner.setValidateEagerly(this.validateEagerly);
        scanner.scan(StringUtils
                .tokenizeToStringArray(this.packageNames, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }
}
