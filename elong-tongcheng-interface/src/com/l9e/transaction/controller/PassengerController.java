package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.Consts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.VerifySuccPassenger;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;
import com.l9e.util.elong.ElongConsts;
import com.l9e.util.elong.ElongMd5Util;
import com.l9e.util.elong.StrUtil;
import com.l9e.util.tongcheng.TongChengConsts;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/passenger")
public class PassengerController extends BaseController {

	private static final Logger logger = Logger.getLogger(PassengerController.class);

	@Value("#{propertiesReader[l9helper_url]}")
	private String l9helper_url;
	@Resource
	private CommonService commonService;

	/**
	 * 核验乘客审核状态
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/verify.jhtml")
	public void verify(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> countMap = new HashMap<String, Object>();
		countMap.put("channel", "elong");
		countMap.put("source", "train19tripElong"); // 请求来源，项目名
		countMap.put("type", "02"); // 核验乘客信息
		countMap.put("code", "00"); // 一次请求
		countMap.put("message", "艺龙发送了一次核验乘客请求");
		requestCountServer(countMap);
		long start = System.currentTimeMillis();
		JSONObject returnJson = new JSONObject();// 接口返回信息

		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");// 7:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");// 23:00
		if (date.before(begin) || date.after(end)) {
			logger.info("网络购票时间为6：00～23：00 ，其余时间不提供查询");
			returnJson.put("ret", false);
			returnJson.put("errMsg", "网络购票时间为6：00～23：00 ，其余时间不提供查询");
			returnJson.put("errCode", "101");
			printJson(response, returnJson.toString());
			countMap.remove("code");
			countMap.remove("message");
			countMap.put("code", "03"); // 发送数据不符合要求
			countMap.put("message", "23:00~06:00之间不允许核验乘客信息");
			requestCountServer(countMap);
			return;
		}
		String datas = this.getParam(request, "datas");// 待验证参数
		List<Map<String, String>> userlist = new ArrayList<Map<String, String>>();
		try {
			logger.info("request verify param:datas=" + datas);
			JSONObject jsonDatas = JSONObject.fromObject(datas);
			int total = jsonDatas.getInt("total");
			JSONArray passengers = jsonDatas.getJSONArray("passengers");
			int jsonSize = passengers.size();
			// 实际出票坐席数量与证件数不符
			if (total != jsonSize) {
				returnJson.put("ret", false);
				returnJson.put("errMsg", "实际出票坐席数量与证件数不符");
				returnJson.put("errCode", "104");
				printJson(response, returnJson.toString());
				countMap.remove("code");
				countMap.remove("message");
				countMap.put("code", "03"); // 发送数据不符合要求
				countMap.put("message", "实际出票坐席数量与证件数不符");
				requestCountServer(countMap);
				return;
			}
			JSONObject jsonPassenger = null;

			Map<String, String> map = null;
			String cert_no = null;// 证件号
			String cert_type = null;// 证件类别
			String user_name = null;// 乘客姓名

			// CONCAT(cp_id, '|', user_name, '|',CONVERT(ticket_type,CHAR),
			// '|',CONVERT(cert_type,CHAR), '|',cert_no,
			// '|',CONVERT(seat_type,CHAR))
			String verify_value = commonService.queryElongSysValueByName("elong_check");
			List<VerifySuccPassenger> vsps = new ArrayList<VerifySuccPassenger>();
			for (int i = 0; i < passengers.size(); i++) {
				// "passenger_id_no": "130105199210291211",
				// "passenger_name": "闫益恒",
				// "passenger_id_type_code": "1"
				map = new HashMap<String, String>();

				jsonPassenger = passengers.getJSONObject(i);
				cert_no = jsonPassenger.getString("passenger_id_no");
				cert_type = ElongConsts.get19eIdsType(jsonPassenger.getString("passenger_id_type_code"), cert_no);
				user_name = jsonPassenger.getString("passenger_name");
				map.put("cert_no", cert_no);
				map.put("cert_type", cert_type);
				map.put("user_name", user_name);
				userlist.add(map);
				if ("0".equalsIgnoreCase(verify_value)) {
					VerifySuccPassenger vsp = new VerifySuccPassenger();
					vsp.setPassenger_id_no(cert_no);
					vsp.setPassenger_id_type_code(cert_type);
					vsp.setPassenger_name(user_name);
					vsp.setVerification_status("1");
					vsp.setVerification_status_name("已通过");
					vsps.add(vsp);
				}
			}
			if (vsps.size() > 0) {
				returnJson.put("status", true);
				returnJson.put("passenger_total", vsps.size());
				returnJson.put("passengers", JSONArray.fromObject(vsps));
				printJson(response, returnJson.toString());
				countMap.remove("code");
				countMap.remove("message");
				countMap.put("code", "11");
				countMap.put("message", "核验接口关闭，默认核验成功");
				requestCountServer(countMap);
				return;
			}
		} catch (net.sf.json.JSONException e) {
			logger.error("JSON解析异常", e);
			returnJson.put("ret", false);
			returnJson.put("errMsg", "Datas不是有效的JSON数据");
			returnJson.put("errCode", "105");
			printJson(response, returnJson.toString());
			countMap.remove("code");
			countMap.remove("message");
			countMap.put("code", "03"); // 发送数据不符合要求
			countMap.put("message", "Datas不是有效的JSON数据");
			requestCountServer(countMap);
			return;
		}
		// 发送核验请求，解析核验结果
		String result = null;
		try {
			result = queryVerifyInteface(userlist, "elong");
			List<VerifySuccPassenger> vsps = this.getVerifySuccPassengerList(result);
			logger.info("verity passengers total take " + (System.currentTimeMillis() - start) + "ms");
			returnJson.put("status", true);
			returnJson.put("passenger_total", vsps.size());
			returnJson.put("passengers", JSONArray.fromObject(vsps));
			printJson(response, returnJson.toString());
			boolean verify_flag = true;
			String message = "";
			for (VerifySuccPassenger vsp : vsps) {
				if (!"1".equalsIgnoreCase(vsp.getVerification_status())) {
					verify_flag = false;
					message = vsp.getPassenger_id_no() + "未通过12306核验";
					break;
				}
			}
			countMap.remove("code");
			countMap.remove("message");
			if (verify_flag) {
				countMap.put("code", "01");
				countMap.put("message", System.currentTimeMillis() - start);
			} else {
				countMap.put("code", "02");
				countMap.put("message", message);
			}
			requestCountServer(countMap);
		} catch (Exception e) {
			logger.error("核验用户数据发生异常", e);
			returnJson.put("ret", false);
			returnJson.put("errMsg", "系统错误，未知服务异常");
			returnJson.put("errCode", "102");
			printJson(response, result);
			countMap.remove("code");
			countMap.remove("message");
			countMap.put("code", "04"); // 系统错误导致失败
			countMap.put("message", "系统错误，未知服务异常");
			requestCountServer(countMap);
		}
	}

	private void requestCountServer(Map<String, Object> map) {
		try {
			JSONObject countJson = new JSONObject();
			countJson.putAll(map);
			String param = "command=count&result=" + countJson.toString();
			// 超时时间设置下
			HttpUtil.sendByPost(l9helper_url, param, "UTF-8");
		} catch (Exception e) {
			logger.info("核验统计异常" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 向验证接口发送验证
	 * 
	 * @param userlist
	 * @throws Exception
	 */
	private String queryVerifyInteface(List<Map<String, String>> userlist, String channel) throws Exception {
		JSONArray users = JSONArray.fromObject(userlist);
		logger.info("our passengers=" + users.toString());
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("command", "verify");// 请求核验用户信息接口
		maps.put("passengers", users.toString());
		maps.put("channel", channel);

		String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
		String result = HttpUtil.sendByPost(l9helper_url, reqParams, "UTF-8");
		logger.info("实名制核验接口返回result=" + result);
		return result;
	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 * @return
	 */
	private List<VerifySuccPassenger> getVerifySuccPassengerList(String result) {
		List<VerifySuccPassenger> vsps = new ArrayList<VerifySuccPassenger>();
		JSONArray jsonRs = JSONArray.fromObject(result);
		JSONObject jsonPassenger = null;
		VerifySuccPassenger vsp = null;
		String check_status = null;
		for (int i = 0; i < jsonRs.size(); i++) {
			vsp = new VerifySuccPassenger();
			// [{"cert_no":"410521199004010692","cert_type":"2","check_status":"0","user_name":"李海潮","user_type":"0"}]
			jsonPassenger = jsonRs.getJSONObject(i);

			check_status = jsonPassenger.getString("check_status");
			if (StringUtils.isEmpty(check_status)) {
				vsp.setVerification_status("1");
				vsp.setVerification_status_name("已通过");
			} else if ("0".equals(check_status)) {// 已通过
				vsp.setVerification_status("1");
				vsp.setVerification_status_name("已通过");

			} else if ("1".equals(check_status)) {// 审核中
				vsp.setVerification_status("0");
				vsp.setVerification_status_name("待核验");
			} else if ("2".equals(check_status)) {// 未通过
				vsp.setVerification_status("-1");
				vsp.setVerification_status_name("未通过");
			} else {
				vsp.setVerification_status("1");
				vsp.setVerification_status_name("已通过");
			}
			vsp.setPassenger_type("1");
			vsp.setPassenger_type_name("成人");
			vsp.setPassenger_id_no(jsonPassenger.getString("cert_no"));
			vsp.setPassenger_id_type_code(ElongConsts.getElongIdsType(jsonPassenger.getString("cert_type")));
			vsp.setPassenger_id_type_name(ElongConsts.getElongIdsTypeName(jsonPassenger.getString("cert_type")));
			vsp.setPassenger_name(jsonPassenger.getString("user_name"));
			vsps.add(vsp);
		}
		return vsps;
	}

	/**
	 * 核验乘客审核状态
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/verifyForTc.jhtml")
	public void verifyForTc(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> countMap = new HashMap<String, Object>();
		countMap.put("channel", "tongcheng");
		countMap.put("source", "train19tripElong"); // 请求来源，项目名
		countMap.put("type", "02"); // 核验乘客信息
		countMap.put("code", "00"); // 一次请求
		countMap.put("message", "同程发送了一次核验乘客请求");
		requestCountServer(countMap);

		long start = System.currentTimeMillis();
		String sysMerchantCode = Consts.TC_PARTNERID;
		String merchantCode = getParam(request, "merchantCode");
		String serviceId = getParam(request, "serviceId");
		String version = getParam(request, "version");
		String timestamp = getParam(request, "timestamp");
		String dataStr = getParam(request, "data");
		String sign = getParam(request, "sign");

		JSONObject json = new JSONObject();
		if (StrUtil.isEmpty(merchantCode) || StrUtil.isEmpty(serviceId) || StrUtil.isEmpty(version) || StrUtil.isEmpty(timestamp) || StrUtil.isEmpty(dataStr)
				|| StrUtil.isEmpty(sign)) {
			logger.info("同城verify-通用参数缺失:" + merchantCode + "|" + serviceId + "|" + version + "|" + timestamp + "|" + dataStr + "|" + sign);
			json.put("code", 1200);
			json.put("message", "通用参数缺失");
			json.put("data", null);
			printJson(response, json.toString());
			return;
		}
		if (!sysMerchantCode.equals(merchantCode)) {
			logger.info("商户号不存在sysMerchantCode:" + sysMerchantCode + ",merchantCode:" + merchantCode);
			json.put("code", 1200);
			json.put("message", "商户号不存在");
			json.put("data", null);
			printJson(response, json.toString());

			countMap.remove("code");
			countMap.remove("message");
			countMap.put("code", "03"); // 发送数据不符合要求
			countMap.put("message", "商户号不存在");
			requestCountServer(countMap);
			return;
		}

		String key = Consts.TC_SIGNKEY;
		String sysSign = ElongMd5Util.md5_32(merchantCode + serviceId + timestamp + dataStr + (ElongMd5Util.md5_32(key, "UTF-8")), "UTF-8");
		if (!sysSign.equals(sign)) {
			logger.info("验签失败sysSign:" + sysSign + ",sign:" + sign + ",param:"
					+ (merchantCode + serviceId + timestamp + dataStr + (ElongMd5Util.md5_32(key, "UTF-8").toLowerCase())) + ",key:" + key);
			json.put("code", 1200);
			json.put("message", "验签失败");
			json.put("data", null);
			printJson(response, json.toString());

			countMap.remove("code");
			countMap.remove("message");
			countMap.put("code", "03"); // 发送数据不符合要求
			countMap.put("message", "验签失败");
			requestCountServer(countMap);
			return;
		}

		try {
			/** 返回json */
			JSONObject data = JSONObject.fromObject(dataStr);

			JSONObject passenger;
			JSONArray passengers = new JSONArray();
			JSONObject return_data = new JSONObject();
			String verify_value = commonService.queryElongSysValueByName("tongcheng_check");

			if ("0".equals(verify_value)) {
				/** 默认核验成功 */
				if (data != null) {
					/*
					 * {"passengers":[{ "passenger_id_no":"352129197702143527",
					 * "passenger_id_type_code":"1", "passenger_id_type_name":
					 * "二代身份证", "passenger_name":"陈晓蓉"}]}
					 */
					logger.info(data.toString());
					JSONArray arr = JSONArray.fromObject(data.getString("passengers"));
					int size = arr.size();
					Map<String, String> map = null;
					String cert_no = null;// 证件号
					String cert_type = null;// 证件类别
					String user_name = null;// 乘客姓名
					for (int i = 0; i < size; i++) {
						passenger = new JSONObject();
						JSONObject p = JSONObject.fromObject(arr.get(i));
						/*
						 * cert_no = p.getString("passenger_id_no"); cert_type =
						 * TongChengConsts.get19eIdsType(p
						 * .getString("passenger_id_type_code")); user_name =
						 * p.getString("passenger_name"); map = new
						 * HashMap<String, String>(); map.put("cert_no",
						 * cert_no); map.put("cert_type", cert_type);
						 * map.put("user_name", user_name); userlist.add(map);
						 */
						passenger.put("verification_status", 1);
						passenger.put("verification_status_name", "通过");
						passenger.put("passenger_type_name", "成人票");
						passenger.put("passenger_id_type_code", p.get("passenger_id_type_code"));
						passenger.put("passenger_id_no", p.get("passenger_id_no"));
						passenger.put("passenger_type", "1");
						passenger.put("passenger_id_type_name", p.get("passenger_id_type_name"));
						passenger.put("passenger_name", p.get("passenger_name"));

						passengers.add(passenger);
					}
				}

				return_data.put("status", true);
				return_data.put("passenger_total", passengers.size());
				return_data.put("passengers", passengers.toString());

				json.put("code", 1100);
				json.put("message", "操作成功");
				json.put("data", return_data.toString());

				logger.info("默认核验tc printJson" + json.toString());
				printJson(response, json.toString());

				countMap.remove("code");
				countMap.remove("message");
				countMap.put("code", "11");
				countMap.put("message", "核验接口关闭，默认核验成功");
				requestCountServer(countMap);
				return;
			}

			List<Map<String, String>> userlist = new ArrayList<Map<String, String>>();

			if (data != null) {
				/*
				 * {"passengers":[{ "passenger_id_no":"352129197702143527",
				 * "passenger_id_type_code":"1", "passenger_id_type_name":
				 * "二代身份证", "passenger_name":"陈晓蓉"}]}
				 */
				logger.info(data.toString());
				JSONArray arr = JSONArray.fromObject(data.getString("passengers"));
				int size = arr.size();
				Map<String, String> map = null;
				String cert_no = null;// 证件号
				String cert_type = null;// 证件类别
				String user_name = null;// 乘客姓名

				for (int i = 0; i < size; i++) {
					JSONObject p = JSONObject.fromObject(arr.get(i));
					cert_no = p.getString("passenger_id_no");
					cert_type = TongChengConsts.get19eIdsType(p.getString("passenger_id_type_code"));
					user_name = p.getString("passenger_name");
					map = new HashMap<String, String>();
					map.put("cert_no", cert_no);
					map.put("cert_type", cert_type);
					map.put("user_name", user_name);
					userlist.add(map);
				}
			}

			String result = null;

			// String result
			// ="[{\"cert_no\":\"3521291977021435271\",\"cert_type\":\"2\",\"check_status\":\"2\",\"user_name\":\"陈晓蓉\",\"user_type\":\"0\"}]";
			/*
			 * [{"cert_no":"37040619970506451x", "cert_type":"2",
			 * "check_status":"1", 0、通过 1、待审核 2、未通过 "user_name":"于舜",
			 * "user_type":"0"}]
			 */

			result = queryVerifyInteface(userlist, "tongcheng");
			JSONArray jsonRs = JSONArray.fromObject(result);
			JSONObject jsonPassenger;

			String check_status;

			boolean verify_flag = true;
			StringBuffer message = new StringBuffer();
			/** 同程核验状态:1已经通过 0待核验 -1未通过 */
			for (int i = 0; i < jsonRs.size(); i++) {
				passenger = new JSONObject();
				// [{"cert_no":"410521199004010692","cert_type":"2","check_status":"0","user_name":"李海潮","user_type":"0"}]
				jsonPassenger = jsonRs.getJSONObject(i);

				check_status = jsonPassenger.getString("check_status");
				if (StringUtils.isEmpty(check_status)) {
					passenger.put("verification_status", 1);
					passenger.put("verification_status_name", "通过");
				} else if ("0".equals(check_status)) {// 已通过
					passenger.put("verification_status", 1);
					passenger.put("verification_status_name", "通过");
				} else if ("1".equals(check_status)) {// 审核中
					passenger.put("verification_status", 0);
					passenger.put("verification_status_name", "待核验");
					verify_flag = false;
					message.append(jsonPassenger.getString("cert_no") + "待核验");
				} else if ("2".equals(check_status)) {// 未通过
					// 未通过里包括身份冒用
					if (jsonPassenger.getString("message").contains("冒用")) {
						passenger.put("verification_status", 2);
						passenger.put("verification_status_name", "身份涉嫌冒用");
						verify_flag = false;
						message.append(jsonPassenger.getString("cert_no") + "身份涉嫌冒用");
					} else {
						passenger.put("verification_status", -1);
						passenger.put("verification_status_name", "未通过");
						verify_flag = false;
						message.append(jsonPassenger.getString("cert_no") + "未通过");
					}

				} else {
					passenger.put("verification_status", 1);
					passenger.put("verification_status_name", "通过");
				}
				passenger.put("passenger_type_name", TongChengConsts.getPiaotypename(TongChengConsts.getTcTicketType(jsonPassenger.getString("user_type"))));
				passenger.put("passenger_id_type_code", TongChengConsts.getTcIdsType(jsonPassenger.getString("cert_type")));
				passenger.put("passenger_id_no", jsonPassenger.getString("cert_no"));
				passenger.put("passenger_type", TongChengConsts.getTcTicketType(jsonPassenger.getString("user_type")));
				passenger.put("passenger_id_type_name",
						TongChengConsts.getPassporttypeseidname(TongChengConsts.getTcIdsType(jsonPassenger.getString("cert_type"))));
				passenger.put("passenger_name", jsonPassenger.getString("user_name"));
				passengers.add(passenger);

			}

			logger.info("tc verity passengers total take " + (System.currentTimeMillis() - start) + "ms");

			if (verify_flag) {
				countMap.put("code", "01");
				countMap.put("message", System.currentTimeMillis() - start);
			} else {
				countMap.put("code", "02");
				countMap.put("message", message);
			}
			requestCountServer(countMap);

			return_data.put("status", true);
			return_data.put("passenger_total", passengers.size());
			return_data.put("passengers", passengers.toString());

			json.put("code", 1100);
			json.put("message", "操作成功");
			json.put("data", return_data.toString());

			logger.info("tc printJson" + json.toString());
			printJson(response, json.toString());

		} catch (Exception e) {
			logger.error("tc_核验用户数据发生异常" + e);
			json.put("code", 1200);
			json.put("message", "核验接口异常");
			json.put("data", null);
			printJson(response, json.toString());
		}

	}

	private String getJsonValue(JSONObject json, String name) {
		return json.get(name) == null ? "" : json.getString(name);

	}

	public static void main(String[] args) {
		String str = "345921V01011435903173{\"passengers\":[{\"passenger_id_no\":\"140502197201171029\",\"passenger_id_type_code\":\"1\",\"passenger_id_type_name\":\"二代身份证\",\"passenger_name\":\"陈晋芳\"}]}AAD258457F9378822AFDFEF6CE43F7C3";
		System.out.println(ElongMd5Util.md5_32(str, "UTF-8"));

	}
}
