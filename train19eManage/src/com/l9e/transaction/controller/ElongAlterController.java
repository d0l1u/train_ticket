package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.AcquireService;
import com.l9e.transaction.service.ElongAlterService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.service.SystemSettingService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.vo.AlterVo;
import com.l9e.transaction.vo.ElongVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;

@Controller
@RequestMapping("/elongAlter")
public class ElongAlterController extends BaseController {
	private static final Logger logger = Logger.getLogger(ElongAlterController.class);
	@Resource
	private ElongAlterService elongAlterService;
	@Resource
	private AccountService accountService;
	@Resource
	private Tj_OpterService tj_OpterService;
	@Resource
	private SystemSettingService systemSettingService;
	@Resource
	private AcquireService acquireService;
	@Resource
	private ExtRefundService extRefundService;

	@RequestMapping("/queryAlterTicketPage.do")
	public String getAlterTicketPage(HttpServletRequest request, HttpServletResponse response) {
		logger.info("进入改签页面");
		return "redirect:/elongAlter/queryAlterTicketList.do?" + "change_status=13&change_status=33";
	}

	// 查询列表
	@RequestMapping("/queryAlterTicketList.do")
	public String getAlterTicketList(HttpServletRequest request, HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> change_statusList = this.getParamToList(request, "change_status");
		String opt_person = this.getParam(request, "opt_person");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		List<String> change_channel = this.getParamToList(request, "change_channel");
		List<String> change_status = new ArrayList<String>(change_statusList);
		if (order_id.trim().length() <= 0) {
			request.setAttribute("change_status", change_status.toString());
		}
		List<String> change_notify_status = this.getParamToList(request, "change_notify_status");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if (order_id.trim().length() > 0) {
			paramMap.put("order_id", order_id);
		} else {
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("change_status", change_status);
			paramMap.put("change_notify_status", change_notify_status);
			paramMap.put("out_ticket_billno", out_ticket_billno);
			paramMap.put("opt_person", opt_person);
			paramMap.put("change_channel", change_channel);
		}

		int totalCount = elongAlterService.queryAlterTicketListCounts(paramMap);// 获得所有退款订单条数
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());
		paramMap.put("pageSize", page.getEveryPageRecordCount());
		List<Map<String, String>> alterTicketList = elongAlterService.queryAlterTicketList(paramMap);
		request.setAttribute("refund_and_alert", systemSettingService.querySystemRefundAndAlert("refund_and_alert"));
		request.setAttribute("isShowList", 1);
		request.setAttribute("alterTicketList", alterTicketList);
		request.setAttribute("alterStatus", ElongVo.getAlterStatus());
		request.setAttribute("notifyStatus", ElongVo.getChangeNotifyStatus());
		request.setAttribute("failReason", ElongVo.getAllAlertFailReason());
		List<Map<String, String>> merchantList = extRefundService.queryExtMerchantinfo();// 查询合作商户的id及名称
		Map<String, String> merchantMap = AlterVo.getAlterChannel();
		for (int i = 0; i < merchantList.size(); i++) {
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("alterChannel", merchantMap);
		if (order_id.trim().length() > 0) {
			request.setAttribute("order_id", order_id);
		} else {
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("change_notify_status", change_notify_status.toString());
			request.setAttribute("out_ticket_billno", out_ticket_billno);
			request.setAttribute("opt_person", opt_person);
			request.setAttribute("change_channel", change_channel);
		}
		return "elongAlter/alterList";
	}

	@RequestMapping("/ticketInfo.do")
	public String getTicketInfo(HttpServletRequest request, HttpServletResponse response) {
		String statusList = this.getParam(request, "statusList");// 得到选中的改签状态
		String notifyList = this.getParam(request, "notifyList");// 得到选中的通知状态
		String pageIndex = this.getParam(request, "pageIndex");
		String change_id = this.getParam(request, "change_id");
		String order_id = this.getParam(request, "order_id");
		String opt_type = this.getParam(request, "opt_type");// 操作方式：0、明细，1、人工改签，2，人工支付
		if (change_id != null && !"".equals(change_id)) {
			HashMap<String, String> alterInfo = elongAlterService.queryAlterInfo(change_id);
			Map<String, String> originOrderInfo = acquireService.queryAcquireOrderInfo(alterInfo.get("order_id"));
			List<HashMap<String, String>> cpInfo = elongAlterService.queryCpInfo(change_id);
			List<HashMap<String, String>> history = elongAlterService.queryLogById(order_id);
			request.setAttribute("alterInfo", alterInfo);
			request.setAttribute("originOrderInfo", originOrderInfo);
			request.setAttribute("cpInfo", cpInfo);
			request.setAttribute("seattype", ElongVo.getSeatType());
			request.setAttribute("ticketType", ElongVo.getTicket_Types());
			request.setAttribute("idstype", ElongVo.getIdstype());
			request.setAttribute("history", history);
			request.setAttribute("opt_type", opt_type);
			request.setAttribute("alterStatus", ElongVo.getAlterStatus());
			request.setAttribute("notifyStatus", ElongVo.getChangeNotifyStatus());
			if (alterInfo.get("merchant_id") != null && !alterInfo.get("merchant_id").equals("tongcheng")) {

				if (alterInfo.get("merchant_id").equals("301030")) {// 去除高铁没有的错误码，不显示
					Map<String, String> gt_failReason = new HashMap<String, String>();
					gt_failReason.putAll(ElongVo.getTuniuAlertFailReason());
					gt_failReason.remove(ElongVo.FAIL_REASON_325);
					gt_failReason.remove(ElongVo.FAIL_REASON_326);
					request.setAttribute("failReason", gt_failReason);
				} else {
					request.setAttribute("failReason", ElongVo.getTuniuAlertFailReason());
				}

			} else {
				request.setAttribute("failReason", ElongVo.getAlertFailReason());
			}
			Map<String, String> account = accountService.queryAccount(alterInfo.get("account_id"));
			request.setAttribute("account", account);
		}
		request.setAttribute("statusList", statusList);
		request.setAttribute("notifyList", notifyList);
		request.setAttribute("pageIndex", pageIndex);
		return "elongAlter/alterInfo";
	}

	// 查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response, HttpServletRequest request) {
		String order_id = this.getParam(request, "order_id");
		List<HashMap<String, String>> history = elongAlterService.queryLogById(order_id);
		JSONArray jsonArray = JSONArray.fromObject(history);
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(jsonArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 支付锁
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryPayIsLock.do")
	@ResponseBody
	public void queryPayIsLock(HttpServletResponse response, HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String order_id = this.getParam(request, "order_id");
		String change_id = this.getParam(request, "change_id");
		String opt_person = loginUserVo.getReal_name();
		String key = "AlterLock_" + order_id;
		String value = "AlterLock_" + order_id + "&" + opt_person;
		String isLock;
		isLock = (String) MemcachedUtil.getInstance().getAttribute(key); // 读值
		if (StringUtils.isEmpty(isLock)) {
			MemcachedUtil.getInstance().setAttribute(key, value, 5 * 60 * 1000); // 写值
			isLock = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", order_id);
			map.put("change_id", change_id);
			map.put("content", opt_person + "锁定了订单：" + order_id);
			map.put("opt_person", opt_person);
			elongAlterService.insertLog(map);
			HashMap<String, Object> ordermap = new HashMap<String, Object>();
			ordermap.put("opt_ren", opt_person);
			ordermap.put("change_id", change_id);
			ordermap.put("order_id", order_id);
			elongAlterService.updateAlterOrder(ordermap);// 更新订单表中操作人信息
		} else if (isLock.indexOf(opt_person) != -1) {
			isLock = "";
		}
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(isLock);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("退款处理锁时response.getWriter()异常");
			e.printStackTrace();
		}
	}

	@RequestMapping("/alter.do")
	public String refund(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();
		String statusList = this.getParam(request, "statusList");// 得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");// 得到选中的通知状态
		String pageIndex = this.getParam(request, "pageIndex");
		String order_id = this.getParam(request, "order_id");
		String change_id = this.getParam(request, "change_id");
		String alter_status = this.getParam(request, "alter_status");
		String change_train_no = this.getParam(request, "alter_train_no");
		// String change_diff_money = this.getParam(request,
		// "change_diff_money");
		// String change_refund_money = this.getParam(request,
		// "change_refund_money");
		// String change_receive_money = this.getParam(request,
		// "change_receive_money");
		String fail_reason = this.getParam(request, "fail_reason");
		String pay_limit_time = this.getParam(request, "pay_limit_time");// 支付截止时间
		if ("14".equals(alter_status)) {
			List<HashMap<String, String>> cpInfo = elongAlterService.queryCpInfo(change_id);
			for (int i = 0; i < cpInfo.size(); i++) {
				String cp_id = cpInfo.get(i).get("cp_id");
				String change_train_box = this.getParam(request, "alter_train_box_" + cp_id);// 改签车厢
				String change_seat_no = this.getParam(request, "alter_seat_no_" + cp_id);// 改签座位
				String change_seat_type = this.getParam(request, "alter_seat_type_" + cp_id);// 改签坐席
				String change_buy_money = this.getParam(request, "alter_buy_money_" + cp_id);// 改签后票价

				if (change_train_box.trim().length() > 0 || change_seat_no.trim().length() > 0 || change_seat_type.trim().length() > 0
						|| change_buy_money.trim().length() > 0) {
					HashMap<String, Object> param_Map = new HashMap<String, Object>();
					param_Map.put("cp_id", cp_id);
					param_Map.put("change_id", change_id);
					param_Map.put("order_id", order_id);
					param_Map.put("change_train_box", change_train_box.trim());
					param_Map.put("change_seat_no", change_seat_no.trim());
					param_Map.put("change_buy_money", change_buy_money.trim());
					// Set<String>kset=ElongVo.getSeatType().keySet();
					// for(String ks : kset){
					// if(change_seat_type.equals(ElongVo.getSeatType().get(ks))){
					// param_Map.put("change_seat_type", ks);
					// System.out.println(ks);
					// break;
					// }else{
					// param_Map.put("change_seat_type", "");
					// }
					// }
					elongAlterService.updateAlertCpInfo(param_Map);
				}
			}
		}
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		HashMap<String, Object> pMap = new HashMap<String, Object>();
		paramMap.put("opt_ren", user);
		paramMap.put("order_id", order_id);
		paramMap.put("change_id", change_id);
		paramMap.put("alter_status", alter_status);
		paramMap.put("change_train_no", change_train_no);
		// paramMap.put("change_diff_money", change_diff_money);
		// paramMap.put("change_refund_money", change_refund_money);
		// paramMap.put("change_receive_money", change_receive_money);
		paramMap.put("fail_reason", fail_reason);
		paramMap.put("pay_limit_time", pay_limit_time);
		elongAlterService.updateAlter(paramMap);
		String content = "";
		if ("14".equals(alter_status)) {
			content = user + "点击了【改签成功】";
		} else if ("15".equals(alter_status)) {
			content = user + "点击了【改签失败】";
		} else if ("34".equals(alter_status)) {
			content = user + "点击了【改签支付成功】";
		} else if ("35".equals(alter_status)) {
			content = user + "点击了【改签支付失败】";
		}
		pMap.put("change_id", change_id);
		pMap.put("order_id", order_id);
		pMap.put("order_time", "now()");
		pMap.put("content", content);
		pMap.put("opt_person", user);
		elongAlterService.insertLog(pMap);
		// 客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", user);
		optMap.put("channel", "tongcheng");
		optMap.put("all", "refund");
		tj_OpterService.operate(optMap);
		String str1 = "";
		if ("".equals(statusList) || statusList == null) {
			str1 = "";
		} else {
			String[] arr1 = statusList.split(",");
			for (int i = 0; i < arr1.length; i++) {
				str1 += "&change_status=" + arr1[i];
			}
		}
		String str2 = "";
		if ("".equals(notifyList) || notifyList == null) {
			str2 = "";
		} else {
			String[] arr2 = notifyList.split(",");
			for (int i = 0; i < arr2.length; i++) {
				str2 += "&change_notify_status=" + arr2[i];
			}
		}
		return "redirect:/elongAlter/queryAlterTicketList.do?pageIndex=" + pageIndex + str1 + str2;
	}

	// 改签机器处理
	@RequestMapping("/updateRobotAlter.do")
	public void updateRobotAlter(HttpServletResponse response, HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();// 获取当前登录的人
		String order_id = this.getParam(request, "order_id");
		String change_id = this.getParam(request, "change_id");
		String change_status = this.getParam(request, "change_status");
		String result = null;
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		HashMap<String, Object> pMap = new HashMap<String, Object>();
		String content = "", alter_status = "", moneyType = "11";
		if (StringUtil.isNotEmpty(order_id) || StringUtil.isNotEmpty(change_id) || StringUtil.isNotEmpty(change_status)) {
			List<HashMap<String, String>> cpInfo = elongAlterService.queryCpInfo(change_id);

			if (Double.parseDouble(String.valueOf(cpInfo.get(0).get("buy_money"))) < Double
					.parseDouble(String.valueOf(cpInfo.get(0).get("change_buy_money")))) {
				// moneyType="22";
				// 流程已改，机器支付：高改低/票价相等/低改高 三种情况下改签状态都改为31 2016-11-16 wangsf
				moneyType = "11";
			}

			if ("13".equals(change_status)) {
				content = user + "点击了【机器改签】13-->11 ";
				alter_status = "11";
			} else if ("33".equals(change_status) && "11".equals(moneyType)) {
				content = user + "点击了【机器支付】 33-->31 高改低/票价相等/低改高";
				alter_status = "31";
			}
			// else if("33".equals(change_status) && "22".equals(moneyType)){
			// content = user + "点击了【机器支付】 33-->36 低改高";
			// alter_status = "36";
			// }
			updateMap.put("alter_status", alter_status);
			updateMap.put("order_id", order_id);
			updateMap.put("change_id", change_id);
			updateMap.put("opt_ren", user);
			elongAlterService.updateAlter(updateMap);
			result = "yes";
			pMap.put("order_id", order_id);
			pMap.put("order_time", "now()");
			pMap.put("content", content);
			pMap.put("opt_person", user);
			pMap.put("change_id", change_id);
			elongAlterService.insertLog(pMap);
			logger.info(content);
		} else {
			pMap.put("change_id", change_id);
			pMap.put("order_id", order_id);
			pMap.put("order_time", "now()");
			pMap.put("content", "此次点击无效，状态异常，联系技术处理。");
			pMap.put("opt_person", user);
			elongAlterService.insertLog(pMap);
		}
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.error("机器处理response返回参数时异常");
			e.printStackTrace();
		}
	}
}
