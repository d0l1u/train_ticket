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
import com.l9e.transaction.service.GtBookService;
import com.l9e.transaction.vo.DifferVo;
import com.l9e.transaction.vo.ExtBookVo;
import com.l9e.transaction.vo.ExtRefundTicketVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.util.HttpUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatUtil;

@Controller
@RequestMapping("/gtBooking")
public class GtBookController extends BaseController {
	private static final Logger logger = Logger.getLogger(GtBookController.class);

	@Resource
	private GtBookService gtBookService;
	private String find_accountinfo;

	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryGtBookPage.do")
	public String queryGtBookPage(HttpServletRequest request,HttpServletResponse response) {
		/** ***************************省级代理省份方法**************************** */
		Calendar theCa = Calendar.getInstance();
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, -3);
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate = df.format(date);
		return "redirect:/gtBooking/queryGtBookList.do?order_status=11&order_status=22&order_status=33&queryTime=0&begin_info_time="
					+ querydate;
	}

	@RequestMapping("/queryGtBookList.do")
	public String queryCmBookList(HttpServletRequest request, HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id"); // 订单号
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		List<String> statusList = this.getParamToList(request, "order_status"); // 订单状态
		List<String> notify_status = this.getParamToList(request,"notify_status");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno"); // 12306订单号
		String begin_info_time = this.getParam(request, "begin_info_time"); // 查询开始时间
		String end_info_time = this.getParam(request, "end_info_time"); // 查询结束时间
		String queryTime = this.getParam(request, "queryTime"); // 查询的当前时间
		//String plat_order_id=this.getParam(request, "plat_order_id");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
		paramMap.put("order_id", order_id);
		}else if(merchant_order_id.trim().length()>0){
			paramMap.put("merchant_order_id", merchant_order_id);
		}else{
		paramMap.put("out_ticket_billno", out_ticket_billno);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("order_status", statusList);
		paramMap.put("notify_status", notify_status);
		paramMap.put("queryTime", queryTime);
		//paramMap.put("plat_order_id", plat_order_id);
		}
		int totalCount = gtBookService.queryGtBookListCount(paramMap);// 总条数
		// 分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数

		List<Map<String, String>> bookList = gtBookService.queryGtBookList(paramMap);
		if(order_id.trim().length()>0){
		request.setAttribute("order_id", order_id);
		request.setAttribute("merchant_order_id", merchant_order_id);
		}else if(merchant_order_id.trim().length()>0){
			request.setAttribute("merchant_order_id", merchant_order_id);
		}else{
			request.setAttribute("statusStr", statusList.toString());
			request.setAttribute("notify_status", notify_status.toString());
			request.setAttribute("out_ticket_billno", out_ticket_billno);
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			//request.setAttribute("plat_order_id", plat_order_id);
			request.setAttribute("queryTime", queryTime);
		}
		request.setAttribute("bookStatus", ExtBookVo.getBookStatus());
		request.setAttribute("refundStatus", RefundVo.getStatus());
		request.setAttribute("differStatus", DifferVo.getStatus());
		request.setAttribute("notifyStatus", ExtBookVo.getNotify_Status());
		request.setAttribute("bookList", bookList);
		request.setAttribute("isShowList", 1);

		return "gtBook/gtBookList";
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
	@RequestMapping("/gtQueryBookInfo.do")
	public String queryBookInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		Map<String, String> orderInfo = gtBookService.queryGtBookOrderInfo(order_id);
		List<Map<String, String>> cpList = gtBookService.queryGtBookOrderInfoCp(order_id);
		List<Map<String, Object>> history = gtBookService.queryGtHistroyByOrderId(order_id);
		List<Map<String, Object>> outTicket_info = gtBookService.queryGtOutTicketInfo(order_id);
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

		request.setAttribute("ignoreStatus", ExtBookVo.getIgnore());
		request.setAttribute("strSwitch", strSwitch);
		request.setAttribute("history", history);
		request.setAttribute("outTicket_info", outTicket_info);
		request.setAttribute("account", account);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("refund_types", RefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", ExtRefundTicketVo.getRefund_Status());
		request.setAttribute("bookStatus", ExtBookVo.getBookStatus());
		request.setAttribute("refundStatus", RefundVo.getStatus());
		request.setAttribute("idstype", ExtBookVo.getIdstype());
		request.setAttribute("tickettype", ExtBookVo.getTicketType());
		request.setAttribute("seattype", ExtBookVo.getSeattype());
		request.setAttribute("cpList", cpList);
		return "gtBook/gtBookInfo";
	}
	

	/**
	 * 出票系统重新通知订单系统
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cpNoticeAgain.do")
	@ResponseBody
	public String cpNoticeAgain(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		//更新cp_orderinfo_notify
		try{
			gtBookService.cpNoticeAgain(order_id);
			return "success";
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
		
	}
	/**
	 * 切换无视截止时间
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateGtSwitch_ignore.do")
	public String updateSwitch_ignore(HttpServletResponse response,HttpServletRequest request) {
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
		gtBookService.addGtUserAccount(log_Map);
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", order_id);
		map.put("opt_ren", opt_person);
		map.put("refund_deadline_ignore", refund_deadline_ignore);

		gtBookService.updateGtSwitch_ignore(map);
		return "redirect:/extBooking/extQueryBookInfo.do?order_id=" + order_id;
	}

	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		List<Map<String, Object>> history = gtBookService.queryGtHistroyByOrderId(order_id);
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
	
	@RequestMapping("/queryGTNOCallBackOrder.do")
	public String queryGTNOCallBackOrder(HttpServletRequest request,
			HttpServletResponse response) {
		
		  logger.info("查询未回调订单操作,页面跳转");
		
		
	     return "gtBook/gtNoCallBackOrder";
	
	}
	
	
	
	
	
	
	
	
}
