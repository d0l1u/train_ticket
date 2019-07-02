package com.train.system.center.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * HthyService
 *
 * @author taokai3
 * @date 2018/7/9
 */
public interface HthyService {

	String order(JSONObject paramJson, String logid);

	String confirm(Map<String, Object> paramMap, String logid);

	String refund(JSONObject paramJson, String logid);

	String changeOrder(JSONObject paramJson, String logid);

	String changeConfirm(JSONObject paramJson, String logid);
}
