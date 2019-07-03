package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.VerifySuccPassenger;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.TrainPropUtil;
import com.l9e.util.UrlFormatUtil;

@Controller
@RequestMapping("/passenger")
public class PassengerController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(PassengerController.class);

	@Value("#{propertiesReader[real_name_verify_url]}")
	private String real_name_verify_url;

	@Value("#{propertiesReader[COUNT_URL]}")
	private String COUNT_URL;
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
		countMap.put("channel", "qunar");
		countMap.put("source", "trainQunarWeb"); // 请求来源，项目名
		countMap.put("type", "02"); // 核验乘客信息
		countMap.put("code", "00"); // 一次请求
		countMap.put("message", "去哪发送了一次核验乘客请求");
		requestCountServer(countMap);

		long start = System.currentTimeMillis();
		JSONObject returnJson = new JSONObject();// 接口返回信息

		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "070000", "yyyyMMddHHmmss");// 7:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");// 23:00
		if (date.before(begin) || date.after(end)) {
			countMap.remove("code");
			countMap.remove("message");
			countMap.put("code", "03"); // 发送数据不符合要求
			countMap.put("message", "23:00~07:00之间不允许核验乘客信息");
			requestCountServer(countMap);

			logger.info("网络购票时间为7：00～23：00 ，其余时间不提供查询");
			returnJson.put("ret", false);
			returnJson.put("errMsg", "网络购票时间为7：00～23：00 ，其余时间不提供查询");
			returnJson.put("errCode", "101");
			printJson(response, returnJson.toString());
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
				countMap.remove("code");
				countMap.remove("message");
				countMap.put("code", "03"); // 发送数据不符合要求
				countMap.put("message", "实际出票坐席数量与证件数不符");
				requestCountServer(countMap);

				returnJson.put("ret", false);
				returnJson.put("errMsg", "实际出票坐席数量与证件数不符");
				returnJson.put("errCode", "104");
				printJson(response, returnJson.toString());
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
			String verify_value = commonService
					.queryQunarSysValue("qunar_check");
			List<VerifySuccPassenger> vsps = new ArrayList<VerifySuccPassenger>();
			for (int i = 0; i < passengers.size(); i++) {
				// "passenger_id_no": "130105199210291211",
				// "passenger_name": "闫益恒",
				// "passenger_id_type_code": "1"
				map = new HashMap<String, String>();

				jsonPassenger = passengers.getJSONObject(i);
				cert_no = jsonPassenger.getString("passenger_id_no");
				cert_type = TrainPropUtil.get19eIdsType(jsonPassenger
						.getString("passenger_id_type_code"), cert_no);
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
			if(vsps.size() > 0) {
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
			countMap.remove("code");
			countMap.remove("message");
			countMap.put("code", "03"); // 发送数据不符合要求
			countMap.put("message", "Datas不是有效的JSON数据");
			requestCountServer(countMap);

			logger.error("JSON解析异常", e);
			returnJson.put("ret", false);
			returnJson.put("errMsg", "Datas不是有效的JSON数据");
			returnJson.put("errCode", "105");
			printJson(response, returnJson.toString());
			return;
		}
		// 发送核验请求，解析核验结果
		String result = null;
		try {

			result = queryVerifyInteface(userlist);
			List<VerifySuccPassenger> vsps = this
					.getVerifySuccPassengerList(result);
			logger.info("verity passengers total take "
					+ (System.currentTimeMillis() - start) + "ms");

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

			returnJson.put("status", true);
			returnJson.put("passenger_total", vsps.size());
			returnJson.put("passengers", JSONArray.fromObject(vsps));
			printJson(response, returnJson.toString());

		} catch (Exception e) {
			countMap.remove("code");
			countMap.remove("message");
			countMap.put("code", "04"); // 系统错误导致失败
			countMap.put("message", "系统错误，未知服务异常");
			requestCountServer(countMap);

			logger.error("核验用户数据发生异常", e);
			returnJson.put("ret", false);
			returnJson.put("errMsg", "系统错误，未知服务异常");
			returnJson.put("errCode", "102");
			printJson(response, result);
		}
	}

	/**
	 * 向验证接口发送验证
	 * 
	 * @param userlist
	 * @throws Exception
	 */
	private String queryVerifyInteface(List<Map<String, String>> userlist)
			throws Exception {

		JSONArray users = JSONArray.fromObject(userlist);
		logger.info("our passengers=" + users.toString());

		Map<String, String> maps = new HashMap<String, String>();
		maps.put("command", "verify");// 请求核验用户信息接口
		maps.put("passengers", users.toString());
		maps.put("channel", "qunar");

		String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");

		String result = HttpUtil.sendByPost(real_name_verify_url, reqParams,
				"UTF-8");
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
				//未通过里包括身份冒用
				if(jsonPassenger.getString("message").contains("冒用")){
					vsp.setVerification_status("-2");
					vsp.setVerification_status_name("身份冒用");
				}else{
					vsp.setVerification_status("-1");
					vsp.setVerification_status_name("未通过");
				}
			} else {
				vsp.setVerification_status("1");
				vsp.setVerification_status_name("已通过");
			}
			vsp.setPassenger_type("1");
			vsp.setPassenger_type_name("成人");
			vsp.setPassenger_id_no(jsonPassenger.getString("cert_no"));
			vsp.setPassenger_id_type_code(TrainPropUtil
					.getQunarIdsType(jsonPassenger.getString("cert_type")));
			vsp.setPassenger_id_type_name(TrainPropUtil
					.getQunarIdsTypeName(jsonPassenger.getString("cert_type")));
			vsp.setPassenger_name(jsonPassenger.getString("user_name"));
			vsps.add(vsp);
		}
		return vsps;
	}

	private void requestCountServer(Map<String, Object> map) {
		JSONObject countJson = new JSONObject();
		countJson.putAll(map);
		String param = "command=count&result=" + countJson.toString();
		HttpUtil.sendByPost(COUNT_URL, param, "UTF-8");
	}

	private void printJson(HttpServletResponse response, String StatusStr) {
		logger.info("返回qunar验证结果：" + StatusStr);
		write2Response(response, StatusStr);
	}
}
