package com.l9e.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.l9e.util.MemcachedUtil;

/**
 * 车票预订
 * 
 * @author zhangjun
 *
 */
public class ExternalBase extends BaseController {

	protected static final Logger logger = Logger.getLogger(ExternalBase.class);

	protected static Random random = new Random();

	/**
	 * 12306 URL
	 * 
	 * @param map
	 * @return
	 */
	protected String get12306Url(Map<String, String> map, String interfaceUrl) {
		String url = new String(interfaceUrl);
		StringBuffer sb = new StringBuffer();
		sb.append(map.get("travel_time")).append("|").append(map.get("from_station")).append("|").append(map.get("arrive_station"));
		String param = "";
		try {
			param = URLEncoder.encode(sb.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info("[机器人session_id]=" + session_id);
		return url.replace("$session_id", session_id).replace("$param1", param);
	}

	/**
	 * 获取文件名(eg:ext_北京_上海_2013-5-22_DGTrain)
	 * 
	 * @return
	 */
	protected String getFileName(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("elong_").append(map.get("from_station")).append("_").append(map.get("arrive_station")).append("_").append(map.get("travel_time"))
				.append("_");
		sb.append(map.get("method"));
		return sb.toString();

	}

	/**
	 * 获取系统接口频道
	 * 
	 * @param provinceId
	 * @return
	 */
	protected String getSysInterfaceChannel(String key) {
		String channel = null;// 1:12306接口 2:SOUKD接口,3:新接口
		if (null == MemcachedUtil.getInstance().getAttribute(key)) {
			channel = commonService.querySysSettingByKey(key);
			MemcachedUtil.getInstance().setAttribute(key, channel, 60 * 1000);

		} else {
			channel = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return channel;
	}

	/**
	 * 获取系统配置的启用的12306接口url
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected String getSysInterface12306Url(String key) {
		String url = "";
		List<String> urlList = null;
		if (null == MemcachedUtil.getInstance().getAttribute(key)) {
			urlList = this.getSysSettingValueAttr(key, "1", key);
			MemcachedUtil.getInstance().setAttribute(key, urlList, 10 * 60 * 1000);
		} else {
			urlList = (List<String>) MemcachedUtil.getInstance().getAttribute(key);
		}

		if (urlList == null || urlList.size() == 0) {
			logger.info("[12306 interface selected]为空...");
		} else {
			int index = random.nextInt(urlList.size());
			url = urlList.get(index);
		}
		logger.info("[12306 interface selected]" + url);
		return url;
	}

	// 余票阀值控制
	protected String replaceNumVal(String str, String limit_num) {
		if (!StringUtils.isEmpty(str)) {
			if (!"-".equals(str) && Integer.parseInt(str) <= Integer.parseInt(limit_num)) {
				str = "-";
			}
		}
		return str;
	}
}
