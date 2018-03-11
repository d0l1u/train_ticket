package com.l9e.transaction.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.AppBookService;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.DifferVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.util.HttpUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatUtil;

@Controller
@RequestMapping("/appbooking")
public class AppBookController extends BaseController {
	private static final Logger logger = Logger.getLogger(AppBookController.class);

	@Resource
	private AppBookService appBookService;

	private String find_accountinfo;

	/**
	 * 进入查询页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAppBookPage.do")
	public String queryAppBookPage(HttpServletRequest request,
			HttpServletResponse response) {
		/** ***************************省级代理省份方法**************************** */
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		Calendar theCa = Calendar.getInstance();
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, -3);
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate = df.format(date);
		if (loginUserVo.getUser_level().equals("1.1")
				&& loginUserVo.getSupervise_name() != null) {
			String str = loginUserVo.getSupervise_name();
			
			request.setAttribute("bookStatus", BookVo.getBookStatus());
			request.setAttribute("payTypes", BookVo.getPay_type());
			request.setAttribute("channelTypes", BookVo.getchannel_type());
			return "book/bookList";
		} else {
			return "redirect:/appbooking/queryAppBookList.do?order_status=11&order_status=22&order_status=33&queryTime=0&begin_info_time="
					+ querydate;
		}
	}

	@RequestMapping("/queryAppBookList.do")
	public String queryAppBookList(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id"); // 订单号
		String user_phone = this.getParam(request, "user_phone"); // apppay付款手机号
		List<String> statusList = this.getParamToList(request, "order_status"); // 订单状态
		List<String> payTypeList = this.getParamToList(request, "pay_type");//支付方式
		List<String> channelList = this.getParamToList(request, "channel");//渠道
		String out_ticket_billno = this.getParam(request, "out_ticket_billno"); // 12306订单号
		String begin_info_time = this.getParam(request, "begin_info_time"); // 查询开始时间
		String end_info_time = this.getParam(request, "end_info_time"); // 查询结束时间
		String queryTime = this.getParam(request, "queryTime"); // 查询的当前时间

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("user_phone", user_phone);
			paramMap.put("out_ticket_billno", out_ticket_billno);
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("order_status", statusList);
			paramMap.put("pay_type", payTypeList);
			paramMap.put("channel", channelList);
			paramMap.put("queryTime", queryTime);
		}

		int totalCount = appBookService.queryAppBookListCount(paramMap);// 总条数
		// 分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数

		List<Map<String, String>> bookList = appBookService
				.queryAppBookList(paramMap);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("user_phone", user_phone);
			request.setAttribute("statusStr", statusList.toString());
			request.setAttribute("payTypeStr", payTypeList.toString());
			request.setAttribute("channelStr", channelList.toString());
			request.setAttribute("out_ticket_billno", out_ticket_billno);
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("queryTime", queryTime);
		}
		request.setAttribute("bookStatus", BookVo.getBookStatus());
		request.setAttribute("payTypes", BookVo.getPay_type());
		request.setAttribute("channelTypes", BookVo.getchannel_type());
		request.setAttribute("refundStatus", RefundVo.getStatus());
		request.setAttribute("differStatus", DifferVo.getStatus());
		request.setAttribute("bookList", bookList);
		request.setAttribute("isShowList", 1);


		return "appBook/appBookList";
	}

	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}

	/**
	 * 查询明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/appQueryBookInfo.do")
	public String queryBookInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		String order_type = this.getParam(request, "order_type");
		Map<String, String> orderInfo = appBookService
				.queryAppBookOrderInfo(order_id);
		List<Map<String, String>> bxList = appBookService
				.queryAppBookOrderInfoBx(order_id);
		List<Map<String, String>> cpList = appBookService
				.queryAppBookOrderInfoCp(order_id);
		List<Map<String, Object>> history = appBookService
				.queryAppHistroyByOrderId(order_id);
		List<Map<String, Object>> outTicket_info = appBookService
				.queryAppOutTicketInfo(order_id);
		String account = null;
		// if(StringUtils.equals(orderInfo.get("refund_status"),
		// RefundVo.PRE_REFUND)){
		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("orderid", order_id);

		String params = null;
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");

			account = HttpUtil.sendByPost(find_accountinfo, params, "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
		String str = orderInfo.get("refund_deadline_ignore");
		logger.info("是否截止：" + str);
		String strSwitch = SwitchUtils.switch1Or0(str);

		request.setAttribute("ignoreStatus", BookVo.getIgnore());
		request.setAttribute("strSwitch", strSwitch);
		request.setAttribute("history", history);
		request.setAttribute("outTicket_info", outTicket_info);
		request.setAttribute("account", account);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("refund_types", RefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", RefundTicketVo
				.getRefund_Status());
		request.setAttribute("bookStatus", BookVo.getBookStatus());
		request.setAttribute("refundStatus", RefundVo.getStatus());
		request.setAttribute("idstype", BookVo.getIdstype());
		request.setAttribute("tickettype", BookVo.getTicketType());
		request.setAttribute("seattype", BookVo.getSeattype());
		request.setAttribute("channelTypes", BookVo.getchannel_type());
		request.setAttribute("cpList", cpList);
		return "appBook/appBookInfo";
	}

	/**
	 * 切换无视截止时间
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateAppSwitch_ignore.do")
	public String updateSwitch_ignore(HttpServletResponse response,
			HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");

		String opt_person = loginUserVo.getReal_name();
		String order_id = this.getParam(request, "order_id");
		String refund_deadline_ignore = this.getParam(request,
				"refund_deadline_ignore");
		String userAccount;
		if (refund_deadline_ignore.equals("1")) {
			userAccount = opt_person + "点击了关闭无视退款时间";
		} else {
			userAccount = opt_person + "点击了开启无视退款时间";
		}
		Map<String, String> log_Map = new HashMap<String, String>();
		log_Map.put("order_id", order_id);
		log_Map.put("user", opt_person);
		log_Map.put("userAccount", userAccount);
		appBookService.addAppUserAccount(log_Map);
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", order_id);
		map.put("opt_ren", opt_person);
		map.put("refund_deadline_ignore", refund_deadline_ignore);

		appBookService.updateAppSwitch_ignore(map);
		return "redirect:/appbooking/appQueryBookInfo.do?order_id=" + order_id;
	}
	
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		List<Map<String, Object>> history = appBookService.queryAppHistroyByOrderId(order_id);
		JSONArray jsonArray = JSONArray.fromObject(history);  
		response.setCharacterEncoding("utf-8");
		//System.out.println(jsonArray.toString());
		try {
			response.getWriter().write(jsonArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
