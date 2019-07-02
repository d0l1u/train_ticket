package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.l9e.transaction.vo.ChangeInfo;

public interface ChangeService {
	/**
	 * 请求改签
	 * @param parameter
	 * @return
	 */
	JSONObject addChange(JSONObject webJson) ;
	/**
	 * 取消改签
	 * @param parameter
	 * @return
	 */
	JSONObject cancelChange(JSONObject webJson);
	/**
	 * 确认改签
	 * @param parameter
	 * @return
	 */
	JSONObject confirmChange(JSONObject webJson) ;
	/**
	 * 获取待通知的改签信息
	 * @param merchantId
	 * @return
	 */
	List<ChangeInfo> getNoticeChangeInfo(String merchantId);
	/**
	 * 通知改签
	 * @param changeInfos
	 */
	void callbackChangeNotice(List<ChangeInfo> notifyList);
	/**
	 * 查询改签车票信息
	 * @param param
	 * @return
	 */
	Map<String,Object> queryChangeCpInfo(Map<String,Object> param);
	
}
