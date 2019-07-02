package com.kuyou.train.http;

import com.kuyou.train.http.annotation.HttpService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import retrofit2.CallAdapter;
import retrofit2.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * ClassPathHttpServiceScanner
 *
 * @author liujia33
 * @date 2018/9/29
 */
@Slf4j
@Setter
public class ClassPathHttpServiceScanner extends ClassPathBeanDefinitionScanner {

    private OkHttpClient okHttpClient;
    private List<Converter.Factory> converterFactories = new ArrayList<>();
    private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
    private Executor callbackExecutor;
    private boolean validateEagerly;

    public ClassPathHttpServiceScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void registerDefaultFilters() {
        // 添加过滤器，只扫描annotationClass注解指定的Type
        this.addIncludeFilter(new AnnotationTypeFilter(HttpService.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... packageNames) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(packageNames);
        if (beanDefinitions.isEmpty()) {
            log.warn("No Http service was found in '" + Arrays.toString(packageNames) +
                    "' package. Please check your configuration.");
            return beanDefinitions;
        }

        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getPropertyValues().add("httpServiceInterface", getBeanClass(definition.getBeanClassName()));
            definition.setBeanClass(HttpServiceFactoryBean.class);
            definition.getPropertyValues().add("okHttpClient", okHttpClient);
            definition.getPropertyValues().add("callAdapterFactories", callAdapterFactories);
            definition.getPropertyValues().add("callbackExecutor", callbackExecutor);
            definition.getPropertyValues().add("converterFactories", converterFactories);
            definition.getPropertyValues().add("validateEagerly", validateEagerly);
        }
        return beanDefinitions;
    }

    private Class<?> getBeanClass(String beanClassName) {
        try {
            return Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
