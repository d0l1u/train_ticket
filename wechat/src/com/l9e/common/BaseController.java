package com.l9e.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.l9e.transaction.service.CommonService;
import com.l9e.util.MemcachedUtil;

public class BaseController {

	@Resource
	protected CommonService commonService;

	/**
	 * 用户登录信息
	 * 
	 * @param request
	 * @return
	 */
	public LoginUserInfo getLoginUser(HttpServletRequest request) {
		return (LoginUserInfo) request.getSession().getAttribute(
				TrainConsts.INF_LOGIN_USER);
	}

	/**
	 * 获取系统配置信息
	 * 
	 * @param provinceId
	 * @return
	 */
	public SystemConfInfo getSysConf(String provinceId) {
		SystemConfInfo sysConf = null;
		String key = "sys_conf_" + provinceId;
		if (null == MemcachedUtil.getInstance().getAttribute(key)) {
			sysConf = commonService.querySysConf(provinceId);
			if (null == sysConf) {
				// 无开通信息
				sysConf = new SystemConfInfo();
			}
			MemcachedUtil.getInstance().setAttribute(key, sysConf,
					5 * 60 * 1000);

		} else {
			sysConf = (SystemConfInfo) MemcachedUtil.getInstance()
					.getAttribute(key);
		}
		return sysConf;
	}

	/**
	 * 获取系统设置属性值集合
	 * 
	 * @param setting_name
	 * @param setting_status
	 * @param key
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getSysSettingValueAttr(String setting_name,
			String setting_status, String key) {
		List<String> list = null;
		if (null == MemcachedUtil.getInstance().getAttribute(key)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("setting_name", setting_name);
			paramMap.put("setting_status", setting_status);
			list = commonService.querySysSettingValue(paramMap);
			MemcachedUtil.getInstance().setAttribute(key, list, 5 * 60 * 1000);

		} else {
			list = (List<String>) MemcachedUtil.getInstance().getAttribute(key);
		}
		return list;
	}

	/**
	 * 获取系统设置属性值
	 * 
	 * @param setting_name
	 * @param setting_status
	 * @param key
	 * @return String
	 */
	public String getSysSettingValue(String setting_name, String key) {
		String value = null;
		if (null == MemcachedUtil.getInstance().getAttribute(key)) {
			value = commonService.querySysSettingByKey(setting_name);
			MemcachedUtil.getInstance().setAttribute(key, value, 5 * 60 * 1000);

		} else {
			value = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return value;
	}

	/**
	 * 获取列车类型中文名称（eg 高铁 动车..）
	 * 
	 * @param train_no
	 * @return
	 */
	public String getTrainTypeCn(String train_no) {
		/*
		 * G 高铁 D 动车 K 快车 T 特快 Z 直达 C 城际 数字 普快 L 临客 Y 旅游 剩下的就写其他
		 */
		String type = "";
		if (!StringUtils.isEmpty(train_no)) {
			String pre = train_no.substring(0, 1);
			if ("G".equalsIgnoreCase(pre)) {
				type = "高铁";
			} else if ("D".equalsIgnoreCase(pre)) {
				type = "动车";
			} else if ("K".equalsIgnoreCase(pre)) {
				type = "快车";
			} else if ("T".equalsIgnoreCase(pre)) {
				type = "特快";
			} else if ("Z".equalsIgnoreCase(pre)) {
				type = "直达";
			} else if ("C".equalsIgnoreCase(pre)) {
				type = "城际";
			} else if ("L".equalsIgnoreCase(pre)) {
				type = "临客";
			} else if ("Y".equalsIgnoreCase(pre)) {
				type = "旅游";
			} else if (pre.charAt(0) >= '0' && pre.charAt(0) <= '9') {
				type = "普快";
			} else {
				type = "其他";
			}
		}
		return type;
	}

	/**
	 * request.getParameter
	 * 
	 * @param param
	 * @return
	 */
	public String getParam(HttpServletRequest request, String param) {
		return request.getParameter(param) == null ? "" : request.getParameter(
				param).toString().trim();
	}

	/**
	 * null转空
	 * 
	 * @param str
	 * @return
	 */
	public String nullToEmpty(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			return "";
		} else {
			return str.trim();
		}
	}

	/**
	 * 空转0
	 * 
	 * @param str
	 * @return
	 */
	public String emptyToZeroStr(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			return "0";
		} else {
			return str.trim();
		}
	}

	/**
	 * 空转0
	 * 
	 * @param str
	 * @return
	 */
	public int emptyToZero(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			return 0;
		} else {
			return Integer.parseInt(str.trim());
		}
	}

	/**
	 * 值写入response
	 * 
	 * @param response
	 * @param StatusStr
	 * @throws IOException 
	 */
	public void write2Response(HttpServletResponse response, String StatusStr) throws IOException {
		response.getWriter().write(StatusStr);
		response.getWriter().flush();
		response.getWriter().close();
	}

}
