package com.l9e.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.PropertyFilter;

public class JsonlibUtil {

	private static final JsonConfig config = new JsonConfig();
	
	static {
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			
			private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			@Override
			public Object processObjectValue(String arg0, Object value, JsonConfig arg2) {
				return format.format(value);
			}
			
			@Override
			public Object processArrayValue(Object value, JsonConfig arg1) {
				return format.format(value);
			}
		});
		config.setJsonPropertyFilter(new PropertyFilter() {
			
			@Override
			public boolean apply(Object source, String name, Object value) {
				return value == null;
			}
		});
	}
	
	/**
	 * 对象转json串
	 * @param bean
	 * @return
	 */
	public static String bean2json(Object bean) {
		return JSONObject.fromObject(bean, config).toString();
	}
	
	/**
	 * 集合转json串
	 * @param bean
	 * @return
	 */
	public static String list2json(Object bean) {
		return JSONArray.fromObject(bean, config).toString();
	}
	
	/**
	 * json字符串转对象
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T json2Bean(String json, Class<T> clazz) {
		JSONObject jsonObj = JSONObject.fromObject(json, config);
		return (T) JSONObject.toBean(jsonObj, clazz);
	}
	
	/**
	 * json集合字符串转list
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> json2list(String json, Class<T> clazz) {
		JSONArray jsonArr = JSONArray.fromObject(json, config);
		return (List<T>) JSONArray.toCollection(jsonArr, clazz);
	}

}
