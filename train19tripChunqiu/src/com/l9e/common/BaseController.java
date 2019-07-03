package com.l9e.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.jiexun.iface.util.StringUtil;
import com.l9e.transaction.service.CommonService;
import com.l9e.util.MemcachedUtil;

public class BaseController {
	
	@Value("#{propertiesReader[merchant_id]}")
	protected String merchant_id;
	
	@Value("#{propertiesReader[merchant_key]}")
	protected String merchant_key;//验签key
	
	@Value("#{propertiesReader[mx_home_page]}")
	protected String mx_home_page;//重新支付页面
	
	@Value("#{propertiesReader[req_url]}")
	protected String req_url;//cbcpay请求地址
	
	@Value("#{propertiesReader[refund_url]}")
	protected String refund_url;//cbcpay退款地址
	
	protected String characterSet_GBK = "GBK";
	
	protected String characterSet_UTF = "UTF-8";
	
	@Value("#{propertiesReader[callbackUrl]}")
	protected String callbackUrl;//cbcpay下单同步调用接口地址
	
	@Value("#{propertiesReader[notifyUrl]}")
	protected String notifyUrl;//cbcpay下单异步调用接口地址
	
	@Value("#{propertiesReader[showUrl]}")
	protected String showUrl;//cbcpay查看订单信息
	
	@Value("#{propertiesReader[refund_notifyUrl]}")
	protected String refund_notifyUrl;//cbcpay异步退款结果响应地址
	
	@Resource
	protected CommonService commonService;
	
	public static int SORT_DES = 0;
	public static int SORT_ASC = 1;

	/**
	 * 用户登录信息
	 * @param request
	 * @return
	 */
	public LoginUserInfo getLoginUser(HttpServletRequest request) {
		return (LoginUserInfo) request.getSession().getAttribute(
				TrainConsts.INF_LOGIN_USER);
	}
	
	/**
	 * 获取系统配置信息
	 * @param provinceId
	 * @return
	 */
	public SystemConfInfo getSysConf(String provinceId){
		SystemConfInfo sysConf = null;
		String key = "sys_conf_" + provinceId;
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			sysConf = commonService.querySysConf(provinceId);
			if(null == sysConf){
				//无开通信息
				sysConf = new SystemConfInfo();
			}
			MemcachedUtil.getInstance().setAttribute(key, sysConf, 5*60*1000);
			
		}else{
			sysConf = (SystemConfInfo) MemcachedUtil.getInstance().getAttribute(key);
		}
		return sysConf;
	}
	
	/**
	 * 获取系统设置属性值集合
	 * @param setting_name
	 * @param setting_status
	 * @param key
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getSysSettingValueAttr(String setting_name, 
				String setting_status, String key){
		List<String> list = null;
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("setting_name", setting_name);
			paramMap.put("setting_status", setting_status);
			list = commonService.querySysSettingValue(paramMap);
			MemcachedUtil.getInstance().setAttribute(key, list, 5*60*1000);
			
		}else{
			list = (List<String>) MemcachedUtil.getInstance().getAttribute(key);
		}
		return list;
	}
	
	/**
	 * 获取系统设置属性值
	 * @param setting_name
	 * @param setting_status
	 * @param key
	 * @return String
	 */
	public String getSysSettingValue(String setting_name, String key){
		String value = null;
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			value = commonService.querySysSettingByKey(setting_name);
			MemcachedUtil.getInstance().setAttribute(key, value, 60*1000);
			
		}else{
			value = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return value;
	}
	
	/**
	 * 获取列车类型中文名称（eg 高铁 动车..）
	 * @param train_no
	 * @return
	 */
	public String getTrainTypeCn(String train_no){
		/*G 高铁 D 动车 K 快车 T 特快 Z 直达 C 城际
		数字 普快 L 临客 Y 旅游 剩下的就写其他*/
		String type = "";
		if(!StringUtils.isEmpty(train_no)){
			String pre = train_no.substring(0, 1);
			if("G".equalsIgnoreCase(pre)){
				type = "高铁";
			}else if("D".equalsIgnoreCase(pre)){
				type = "动车";
			}else if("K".equalsIgnoreCase(pre)){
				type = "快车";
			}else if("T".equalsIgnoreCase(pre)){
				type = "特快";
			}else if("Z".equalsIgnoreCase(pre)){
				type = "直达";
			}else if("C".equalsIgnoreCase(pre)){
				type = "城际";
			}else if("L".equalsIgnoreCase(pre)){
				type = "临客";
			}else if("Y".equalsIgnoreCase(pre)){
				type = "旅游";
			}else if(pre.charAt(0) >= '0' && pre.charAt(0) <= '9'){
				type = "普快";
			}else{
				type = "其他";
			}
		}
		return type;
	}
	
	/**
	 * request.getParameter
	 * @param param
	 * @return
	 */
	public String getParam(HttpServletRequest request, String param){
		return request.getParameter(param) == null ? "" : request.getParameter(param).toString().trim();
	}
	
	/**
	 * null转空
	 * @param str
	 * @return
	 */
	public String nullToEmpty(String str){
		if(str==null || "".equals(str.trim()) || "null".equals(str.trim())){
			return "";
		}else{
			return str.trim();
		}
	}
	
	/**
	 * 空转0
	 * @param str
	 * @return
	 */
	public String emptyToZeroStr(String str){
		if(str==null || "".equals(str.trim()) || "null".equals(str.trim())){
			return "0";
		}else{
			return str.trim();
		}
	}
	
	/**
	 * 空转0
	 * @param str
	 * @return
	 */
	public int emptyToZero(String str){
		if(str==null || "".equals(str.trim()) || "null".equals(str.trim())){
			return 0;
		}else{
			return Integer.parseInt(str.trim());
		}
	}
	
	/**
	 * 值写入response
	 * @param response
	 * @param StatusStr
	 */
	public void write2Response(HttpServletResponse response, String StatusStr){
		try {
			response.getWriter().write(StatusStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static String getValuesBySort(Map<String, String> map, String rejectKey) {
	       return getValuesBySort(map, rejectKey, SORT_ASC, null);
	}

	public static String getValuesBySort(Map<String, String> map, String rejectKey, int sort, String link) {
       String result = null;
       try {
           link = (StringUtil.isEmpty(link) ? "&" : link);
           if (sort == SORT_ASC) {
              // 升序
              result = getValuesBySort_asc(map, rejectKey, link);
           } else if (sort == SORT_DES) {
              // 降序
              result = getValuesBySort_des(map, rejectKey, link);
           }
       } catch (Exception e) {
       }
       return result;
    }
	
	private static String getValuesBySort_asc(Map<String, String> map, String rejectKey, String link) {
       if (map == null) {
           return null;
       }
       String[] keyArray = map.keySet().toArray(new String[0]);
       Arrays.sort(keyArray);
       StringBuilder result = new StringBuilder();
       for (String key : keyArray) {
           if (key != null && key.equals(rejectKey)) {
              continue;
           }
           String value = map.get(key);
           result.append(key).append("=").append(value).append(link);
       }
       return (result.length() > 0 ? result.subSequence(0, result.length() - link.length()).toString() : null);
    }

    private static String getValuesBySort_des(Map<String, String> map, String rejectKey, String link) {
       if (map == null) {
           return null;
       }
       String[] keyArray = map.keySet().toArray(new String[0]);
       Arrays.sort(keyArray);
       StringBuilder result = new StringBuilder();
       for (int i = keyArray.length - 1; i >= 0; i--) {
           String key = keyArray[i];
           if (key != null && key.equals(rejectKey)) {
              continue;
           }
           String value = map.get(key);
           result.append(key).append("=").append(value).append(link);
       }
       return (result.length() > 0 ? result.subSequence(0, result.length() - link.length()).toString() : null);
    }
}
