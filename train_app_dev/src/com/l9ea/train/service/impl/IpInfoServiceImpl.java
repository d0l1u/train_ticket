package com.l9ea.train.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.l9e.train.po.IpInfo;
import com.l9e.train.util.Consts;
import com.l9ea.train.service.BaseHttpService;
import com.l9ea.train.service.IpInfoService;

import net.sf.json.JSONObject;


@Service("ipInfoService")
public class IpInfoServiceImpl extends BaseHttpService implements IpInfoService{

	private Logger logger = LoggerFactory.getLogger(IpInfoServiceImpl.class);

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
				String result = requestPost(Consts.GET_IP_BY_TYPE, "type="+ type, "utf-8", 3, 500);
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

}
