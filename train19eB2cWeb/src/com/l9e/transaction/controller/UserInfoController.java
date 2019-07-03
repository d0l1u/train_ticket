package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 处理代理商以及联系人信息
 */
@Controller
@RequestMapping("/userIdsCardInfo")
public class UserInfoController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(UserInfoController.class);

	@Resource
	private UserInfoService userInfoService;

	@Value("#{propertiesReader[real_name_verify_url]}")
	private String real_name_verify_url;

	/**
	 * 身份证核验，直接去12306添加联系人核验
	 * 
	 */
	@RequestMapping("/checkUserIdsCardInfo.jhtml")
	public void checkUserIdsCard(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("开始验证身份证数据！");
		String data = this.getParam(request, "data");
		if (StringUtils.isEmpty(data)) {
			logger.error("校验数据data为空！");
			write2Response(response, "SUCCESS");
			return;
		}
		try {
			logger.info("passengers=" + data.toString());
			// passengers=[{"user_name":"杨超","cert_no":"522222199007112835","cert_type":"2"},{"user_name":"杨三","cert_no":"110101198001010117","cert_type":"2"}]
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("command", "verify");// 请求核验用户信息接口
			maps.put("passengers", data.toString());
			maps.put("channel", "web");
			String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
			String result = HttpUtil.sendRealNameByPost(real_name_verify_url,
					reqParams, "UTF-8");
			// String result =
			// "[{\"cert_no\":\"410521199004010692\",\"cert_type\":\"2\",\"check_status\":\"1\",\"user_name\":\"李海潮\",\"user_type\":\"0\"},{\"cert_no\":\"320481198702116616\",\"cert_type\":\"2\",\"check_status\":\"2\",\"user_name\":\"张俊哥\",\"user_type\":\"0\"}]";
			logger.info("实名制核验接口返回result=" + result);
			if (result == "" || result.length() == 0) {
				write2Response(response, "SUCCESS");
				return;
			}
			JSONArray pasArr = JSONArray.fromObject(data);
			Map<String, String> param = new HashMap<String, String>();
			LoginUserInfo loginUser = this.getLoginUser(request);
			for (int i = 0; i < pasArr.size(); i++) {
				param.put("user_id", loginUser.getUser_id());
				param.put("user_ids", (String) pasArr.getJSONObject(i).get(
						"cert_no"));
				param.put("ids_type", (String) pasArr.getJSONObject(i).get(
						"cert_type"));
				param.put("link_name", (String) pasArr.getJSONObject(i).get(
						"user_name"));
				userInfoService.updateAgentPassBuyNum(param);
			}
			// 实名制核验接口返回result=[{"cert_no":"522222199007112835","cert_type":"2","check_status":"0","user_name":"杨超","user_type":"0"},{"cert_no":"110101198001010117","cert_type":"2","check_status":"0","user_name":"杨三","user_type":"0"}]
			JSONArray jarray = JSONArray.fromObject(result.trim());
			List<Map<String, String>> idCard = JSONArray.toList(jarray,
					new HashMap<String, String>(), new JsonConfig());
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, String>> resultJsonList = new ArrayList<Map<String, String>>();
			if (idCard != null && idCard.size() > 0) {
				for (int i = 0; i < idCard.size(); i++) {
					if (idCard.get(i).get("cert_no") != null
							&& idCard.get(i).get("user_name") != null
							&& idCard.get(i).get("check_status") != null) {
						String check_status = idCard.get(i).get("check_status")
								.trim();
						String cert_no = idCard.get(i).get("cert_no").trim();
						String user_name = idCard.get(i).get("user_name")
								.trim();
						if ("2".equals(check_status)) {
							// 未通过
							logger.info("身份证数据姓名:" + user_name + " 身份证号:"
									+ cert_no + "--未通过!");
							Map<String, String> resultMap = new HashMap<String, String>();
							resultMap.put("ids_card", cert_no);
							resultMap.put("userName", user_name);
							resultMap.put("failInfo", "未通过");
							resultMap.put("status", "2");
							resultJsonList.add(resultMap);
						} else if ("1".equals(check_status)) {
							logger.info("身份证数据姓名:" + user_name + " 身份证号:"
									+ cert_no + "--待核验!");
							Map<String, String> resultMap = new HashMap<String, String>();
							resultMap.put("ids_card", cert_no);
							resultMap.put("userName", user_name);
							resultMap.put("failInfo", "待审核");
							resultMap.put("status", "1");
							resultJsonList.add(resultMap);
						} else {
							logger.info("身份证数据姓名:" + user_name + " 身份证号:"
									+ cert_no + "--验证通过!");
						}

					} else {
						write2Response(response, "SUCCESS");
					}
				}
				if (resultJsonList != null && resultJsonList.size() > 0) {
					map.put("errorData", resultJsonList);
					JSONObject jsonStr = JSONObject.fromObject(map);
					write2Response(response, jsonStr.toString());
				} else {
					write2Response(response, "SUCCESS");
				}
			} else {
				logger.info("解析返回的json,得到的数据为空！直接返回成功");
				write2Response(response, "SUCCESS");
				return;
			}
		} catch (Exception e) {
			logger.error("校验数据时出现异常！直接成功");
			write2Response(response, "SUCCESS");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 保存为常用乘车人
	 * 
	 */
	@RequestMapping("/saveUserIdsCardInfo.jhtml")
	public void saveUserIdsCardInfo(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("开始保存常用乘客信息！");
		String data = this.getParam(request, "data");
		if (StringUtils.isEmpty(data)) {
			logger.error("校验数据data为空！");
			return;
		}
		try {
			LoginUserInfo loginUser = this.getLoginUser(request);
			logger.info("passengers=" + data.toString());
			JSONArray pasArr = JSONArray.fromObject(data);
			// 查询该代理商目前的常用乘客总数
			Integer sum = userInfoService.queryAgentPassTotalNum(loginUser
					.getUser_id());
			// 判断要添加的乘客数，若大于100，则删除多余数量
			int size = pasArr.size();
			if (size + sum > 100) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user_id", loginUser.getUser_id());
				map.put("num", size + sum - 100);
				userInfoService.deleteAgentPass(map);
			}
			;
			Map<String, String> param = new HashMap<String, String>();
			for (int i = 0; i < size; i++) {
				param.put("user_id", loginUser.getUser_id());
				param.put("user_ids", (String) pasArr.getJSONObject(i).get(
						"card_num"));
				param.put("ids_type", (String) pasArr.getJSONObject(i).get(
						"card_type"));
				param.put("link_name", (String) pasArr.getJSONObject(i).get(
						"user_name"));
				Integer num = userInfoService.queryPassNumByCard(param);
				if (num == 0) {
					userInfoService.addAgentPassInfo(param);
				}

			}
		} catch (Exception e) {
			logger.error("校验数据时出现异常！直接成功");
			e.printStackTrace();
		}
	}

	/**
	 * 删除常用乘车人
	 * 
	 */
	@RequestMapping("/deleteUserIdsCardInfo.jhtml")
	public void deleteUserIdsCardInfo(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("开始删除常用乘客信息！");
		String data = this.getParam(request, "data");
		if (StringUtils.isEmpty(data)) {
			logger.error("校验数据data为空！");
			return;
		}
		try {
			LoginUserInfo loginUser = this.getLoginUser(request);
			logger.info("passengers=" + data.toString());
			// JSONArray pasArr = JSONArray.fromObject(data);
			JSONObject jsonObject = JSONObject.fromObject(data);
			Map map = new HashMap();
			for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
				String key = (String) iter.next();
				map.put(key, jsonObject.get(key));
			}
			System.out.println("要删除的常用乘客信息：" + map);
			// 查询该代理商目前的常用乘客总数
			Map<String, String> param = new HashMap<String, String>();
			param.put("user_id", loginUser.getUser_id());
			param.put("user_ids", map.get("user_ids").toString());
			param.put("ids_type", map.get("ids_type").toString());
			param.put("link_name", map.get("link_name").toString());
			Integer num = userInfoService.queryPassNumByCard(param);
			if (num > 0) {
				userInfoService.deleteAgentPassInfo(param);
			}
		} catch (Exception e) {
			logger.error("校验数据时出现异常！直接成功");
			e.printStackTrace();
		}
	}


}
