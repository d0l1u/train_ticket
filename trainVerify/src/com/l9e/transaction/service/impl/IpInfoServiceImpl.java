package com.l9e.transaction.service.impl;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.service.IpInfoService;
import com.l9e.transaction.vo.IpInfo;
import com.l9e.util.ConfigUtil;
import com.l9e.util.HttpUtil;

@Service("ipInfoService")
public class IpInfoServiceImpl  implements IpInfoService{
	
	private static final Logger logger = Logger.getLogger(IpInfoServiceImpl.class);

	/**
	 * 根据IP类型获取代理IP
	 */
	@Override
	public IpInfo getIpInfoByType(Integer type) {
		// TODO Auto-generated method stub
		IpInfo ipInfo = null;
		int count = 0;
		do {
			try {
				String result = HttpUtil.sendByPost(ConfigUtil.getProperty("getIpByType"), "type="+type, "utf-8");
				logger.info("get IP by type, IP result : " + result);
				if (!StringUtils.isEmpty(result)) {
					JSONObject resultJsonObject = JSONObject.fromObject(result);
					if (resultJsonObject.has("success")
							&& resultJsonObject.getBoolean("success")) {
						ipInfo = (IpInfo) JSONObject.toBean(resultJsonObject.getJSONObject("data"), IpInfo.class);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			count++;
		} while (ipInfo == null && count < 5);

		return ipInfo;
	}

	/**
	 * 根据IP主键获取代理IP
	 */
	@Override
	public IpInfo queryIpInfoById(Integer ipId) {
		// TODO Auto-generated method stub
		IpInfo ipInfo = null;
		try {
			String result = HttpUtil.sendByPost(ConfigUtil.getProperty("getIpById"), "ipId=" + ipId, "utf-8");
			logger.info("get IP by id , ipInfo result : " + result);
			if (!StringUtils.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				if (resultJsonObject.has("success")
						&& resultJsonObject.getBoolean("success")) {
					ipInfo = (IpInfo) JSONObject.toBean(resultJsonObject.getJSONObject("data"), IpInfo.class);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipInfo;
	}


}
