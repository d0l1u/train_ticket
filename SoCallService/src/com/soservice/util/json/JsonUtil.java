/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package com.soservice.util.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 转化器
 * 
 * @date 2015年9月6日
 */
public class JsonUtil {
	// 初始化对象转换器
	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		//允许出现特殊字符和转义符
		objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true); 
	}

	/**
	 * 对象转化为json
	 * 
	 * @param bean
	 * @return
	 */
	public static String objectToJson(Object bean) {
		String json = null;

		try {
			json = objectMapper.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;

	}

	/**
	 * * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
	 * (1)转换为普通JavaBean：readValue(json,Student.class)
	 * 
	 * @param <T>
	 * @param json
	 * @param beanClass
	 * @return
	 */
	public static <T> Object jsonToObject(String json, Class<T> beanClass) {
		Object bean = null;
		try {
			bean = objectMapper.readValue(json, beanClass);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	/**
	 * json转化成List集合
	 * 
	 * @param <T>
	 * @param json
	 * @param beanClass
	 *            集合泛型类型
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static <T> Object jsonToList(String json, Class<T> beanClass) {
		Object bean = null;
		JavaType jsonType = objectMapper.getTypeFactory()
				.constructParametricType(List.class, beanClass);
		try {
			bean = objectMapper.readValue(json, jsonType);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;

	}

	/**
	 * 将json转换成Map对象
	 * 
	 * @param json
	 * @param clazzk
	 *            Map中key的类型
	 * @param clazzv
	 *            Map中value的类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> JsonToMap(String json) {
		Map<K, V> jsonMap = null;
		try {
			jsonMap = objectMapper.readValue(json, Map.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(json);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonMap;

	}

	/**
	 * 取出外边的括号
	 * 
	 * @param str
	 * @return
	 */
	public static String removeOutsideBracket(String str) {
		str = str.replaceAll("^\\[", "");
		str = str.replaceAll("\\]$", "");
		return str;
	}
}
