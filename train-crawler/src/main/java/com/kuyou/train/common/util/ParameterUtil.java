package com.kuyou.train.common.util;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.MethodUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * ParameterUtil
 *
 * @author liujia33
 * @date 2018/9/30
 */
@Slf4j
public class ParameterUtil {

    public static Map<String, String> convertToMap(Object bean, ParameterFilter filter) {
        Map<String, String> description = new LinkedHashMap<>();

        try {
            if (bean instanceof DynaBean) {
                DynaProperty[] descriptors = ((DynaBean) bean).getDynaClass().getDynaProperties();
                for (int i = 0; i < descriptors.length; ++i) {
                    String name = descriptors[i].getName();
                    description.put(filter.processKey(name), BeanUtilsBean.getInstance().getProperty(bean, name));
                }
            } else {
                PropertyDescriptor[] descriptors = BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(bean);
                Class clazz = bean.getClass();

                for (int i = 0; i < descriptors.length; ++i) {
                    String name = descriptors[i].getName();
                    if (MethodUtils.getAccessibleMethod(clazz, descriptors[i].getReadMethod()) != null) {
                        description.put(filter.processKey(name), BeanUtilsBean.getInstance().getProperty(bean, name));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return description;
    }

    /**
     * 对象转Map 将 property与JSONField
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> convertToMap(Object bean) {
        Map<String, Object> description = new LinkedHashMap<>();
        Map<Integer, Integer> sortCount = new HashMap<>();
        Map<Integer, Field> sortMap = new TreeMap<>((o1, o2) -> o1 - o2);
        Field[] fields = bean.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                JSONField annotation = field.getAnnotation(JSONField.class);
                if (annotation == null) {
                    Object value = field.get(bean);
                    if(field.getType().equals(String.class) && null == value){
                        value = "";
                    }
                    description.put(field.getName().toString(), value);
                } else {
                    Integer index = annotation.ordinal();

                    boolean serialize = annotation.serialize();
                    if (!serialize) {
                        continue;
                    }
                    int count = 0;
                    if (sortCount.containsKey(index)) {
                        count = sortCount.get(index);
                    }
                    count++;
                    sortCount.put(index, count);
                    Integer sortMapKey = index * 1000 + count;
                    sortMap.put(sortMapKey, field);
                }
            }
            for (Integer key : sortMap.keySet()) {
                Field field = sortMap.get(key);
                JSONField annotation = field.getAnnotation(JSONField.class);
                Object value = field.get(bean);
                Class<?> clazz = field.getType();
                if(value == null){
                    value = "";
                }else{
                    //日期格式化
                    if(clazz.equals(Date.class)){
                        String format = annotation.format();
                        value = DateUtil.format((Date)value,format);
                    }
                    //其他类型
                }
                description.put(annotation.name(), value);
            }
        } catch (IllegalAccessException e) {
            log.error("对象转Map方法异常",e);
            return null;
        }
        return description;
    }
}



