package com.l9e.transaction.vo.model;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 入参规范
 * @author licheng
 *
 */
public interface Parameter {

	/**
	 * 获取字符串参数
	 * @param key
	 * @return
	 */
	String getString(String key);
	/**
	 * 获取布尔参数
	 * @param key
	 * @return
	 */
	Boolean getBoolean(String key);
	/**
	 * 获取整型参数
	 * @param key
	 * @return
	 */
	Integer getInt(String key);
	/**
	 * 获取list集合参数
	 * @param <T> list的元素类型
	 * @param key
	 * @param clazz
	 * @return
	 */
	<T> List<T> getList(String key, Class<T> clazz);
	/**
	 * 
	 * @param key
	 */
	public JSONArray getJSONArray(String key);
	/**
	 * 
	 * @param key
	 */
	public JSONObject getJSONObject(String key);
}
