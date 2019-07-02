package com.l9e.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库实体转换工具
 * @author licheng
 *
 */
public class DBObjectUtil {

	/**
	 * 封装实体集合
	 * @param <T>
	 * @param rs
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> rs2List(ResultSet rs, Class<T> clazz) {
		
		List<T> list = new ArrayList<T>();
		
		try {
			Field[] fields = clazz.getDeclaredFields();
			while (rs.next()) {
				T t = clazz.newInstance();
				for(Field field : fields) {
					Object value = null;
					try {
						value = rs.getObject(field.getName());
					} catch (Exception e) {
					}
					if(value == null)
						continue;
					field.setAccessible(true);
					field.set(t, value);
				}
				list.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 封装单例实体
	 * @param <T>
	 * @param rs
	 * @param clazz
	 * @return
	 */
	public static <T> T rs2Bean(ResultSet rs, Class<T> clazz) {
		T t = null;
		
		try {
			if(rs.next()) {
				Field[] fields = clazz.getDeclaredFields();
				t = clazz.newInstance();
				for(Field field : fields) {
					Object value = null;
					try {
						value = rs.getObject(field.getName());
					} catch (Exception e) {
					}
					if(value == null)
						continue;
					field.setAccessible(true);
					field.set(t, value);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return t;
	}
}
