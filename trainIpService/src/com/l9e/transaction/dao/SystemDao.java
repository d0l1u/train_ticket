package com.l9e.transaction.dao;

/**
 * 系统设置持久接口
 * @author licheng
 *
 */
public interface SystemDao {

	/**
	 * 查询系统参数值
	 * @param settingName
	 * @return
	 */
	String selectSettingValue(String settingName);
}
