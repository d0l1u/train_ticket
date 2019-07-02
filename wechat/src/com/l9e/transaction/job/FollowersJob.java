package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;

import com.l9e.weixin.pojo.AccessToken;
import com.l9e.weixin.util.WeChatUtil;

public class FollowersJob {

	// 拉取关注者列表的url
	public static final String GET_FOLLOWERS_URL = "https://api.weixin.qq.com/cgi-bin/user/get";

	@Value("#{propertiesReader[appid]}")
	private String appid;// 退款结果通知地址

	@Value("#{propertiesReader[secret]}")
	private String secret;// 验签key

	@SuppressWarnings("unchecked")
	public List<String> getFollowers() {

		AccessToken accessToken = WeChatUtil.getAccessToken(appid, secret);
		String token = accessToken.getToken();
		JSONObject jsObject = WeChatUtil.httpRequest(GET_FOLLOWERS_URL, "GET",
				"access_token=" + token);
		Iterator<String> iterator = jsObject.keys();
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> openidList= new ArrayList<String>();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = jsObject.get(key);
			map.put(key, value);
			if ("data".equals(key)) {
				Map<String, List<String>> openidMap = (Map<String, List<String>>) jsObject.get(key);
				openidList = openidMap.get("openid");
			}
		}
		return openidList;
	}
}
