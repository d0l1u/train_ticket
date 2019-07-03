package com.l9e.transaction.service;

/**
 * 系统业务
 * @author licheng
 *
 */
public interface SystemService {

	/**
	 * 查询系统参数值
	 * @param settingName
	 * @return
	 */
	String getSystemSettingValue(String settingName);
}
