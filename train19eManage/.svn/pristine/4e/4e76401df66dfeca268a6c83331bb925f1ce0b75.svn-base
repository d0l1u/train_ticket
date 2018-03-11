package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.QunarBookService;
import com.l9e.transaction.service.QunarExcelService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.QunarBookVo;
import com.l9e.util.DateUtil;
import com.l9e.util.ExcelUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.UrlFormatUtil;

@Controller
@RequestMapping("/qunarbook")
public class QunarBookController extends BaseController {
	private static final Logger logger = Logger.getLogger(QunarBookController.class);
	@Resource
	private QunarBookService qunarBookService;

	@Resource
	private QunarExcelService qunarExcelService;
	private String find_accountinfo;

	/** *********************��ȡ�˺�************************ */
	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}

	@RequestMapping("/queryAllBookList.do")
	public String getAllBookList(HttpServletRequest request,
			HttpServletResponse response) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date backDate = DateUtil.dateAddDays(currentTime, -3);
		String dateString = formatter.format(backDate);
		return "redirect:/qunarbook/queryBookList.do?begin_info_time=" + dateString +  "&order_status=11&order_status=33";
	}

	@RequestMapping("/queryBookList.do")
	public String queryBookList(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		String begin_time = this.getParam(request, "begin_time");//出发时间
		String end_time = this.getParam(request, "end_time");//出发时间
		List<String> order_status = this.getParamToList(request, "order_status");
		List<String> notify_status = this.getParamToList(request, "notify_status");
		List<String> order_source = this.getParamToList(request, "order_source");
		String order_type = this.getParam(request, "order_type");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("out_ticket_billno", out_ticket_billno);
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("order_status", order_status);
			paramMap.put("notify_status", notify_status);
			paramMap.put("order_source", order_source);
			paramMap.put("begin_time", begin_time);//出发时间
			paramMap.put("end_time", end_time);//出发时间
			paramMap.put("order_type", order_type);
		}
		int totalCount = qunarBookService.getBookListCount(paramMap);

		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());
		paramMap.put("pageSize", page.getEveryPageRecordCount());

		List<QunarBookVo> bookList = qunarBookService.getBookList(paramMap);
		request.setAttribute("bookStatus", QunarBookVo.getBookStatus());
		request.setAttribute("notifyStatus", QunarBookVo.getNotifyStatus());
		request.setAttribute("qunarChannel", QunarBookVo.getQunarChannel());
		request.setAttribute("isShowList", 1);
		for (QunarBookVo qunarBook : bookList) {
			if (qunarBook.getOrder_type() == null
					|| "".equals(qunarBook.getOrder_type())) {
				qunarBook.setOrder_type("0");
			}
		}
		request.setAttribute("bookList", bookList);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("out_ticket_billno", out_ticket_billno);
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("begin_time", begin_time);
			request.setAttribute("end_time", end_time);
			request.setAttribute("order_status", order_status);
			request.setAttribute("notify_status", notify_status);
			request.setAttribute("order_source", order_source);
			request.setAttribute("order_type", order_type);
		}
		return "qunarbook/bookList";
	}
	
	@RequestMapping("/queryNotifyList.do")
	public String queryNotifyList(HttpServletRequest request,
			HttpServletResponse response) {
		String notify_status = this.getParam(request, "notify_status");
		return "redirect:/qunarbook/queryBookList.do?notify_status=" + notify_status ;
	}

	@RequestMapping("/notify.do")
	public void updateNotify_status(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获取当前登录人
		
		String order_id = this.getParam(request, "order_id");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("opt_ren", user);
		qunarBookService.updateNotify_status(request,response,paramMap);
		
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", order_id);
		String content = user + "点击了通知成功按钮";
		logMap.put("content" , content);
		logMap.put("opt_person", user);
		qunarBookService.insertLog(logMap);
	}

	@RequestMapping("/bookInfo.do")
	public String bookInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		HashMap<String, String> bookVo = qunarBookService
				.getBookOrderInfo(order_id);
		List<HashMap<String, String>> cpListInfo = qunarBookService
				.getBookCpInfo(order_id);
		List<HashMap<String, String>> logInfo = qunarBookService
				.getBookLog(order_id);
		HashMap<String, String> notify_status = qunarBookService
				.getBookNotify(order_id);
		String account = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("orderid", order_id);
		String params = null;
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			account = HttpUtil.sendByPost(find_accountinfo, params, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String ext_seat = bookVo.get("ext_seat");

		List<String> ext_seatList = new ArrayList<String>();
		if (ext_seat != null) {
			String[] ext_seats = ext_seat.split("\\|");

			for (int i = 0; i < ext_seats.length; i++) {
				// ext_seats[i] = ext_seats[i].substring(0,
				// ext_seats[i].indexOf(","));
				ext_seatList.add(ext_seats[i]);
			}
		}
		request.setAttribute("account", account);
		request.setAttribute("ext_seat", ext_seatList);
		request.setAttribute("bookStatus", QunarBookVo.getBookStatus());
		request.setAttribute("seatType", QunarBookVo.getSeatType());
		request.setAttribute("bookVo", bookVo);
		request.setAttribute("cpListInfo", cpListInfo);
		request.setAttribute("notify_status", notify_status);
		request.setAttribute("ticketType", QunarBookVo.getTicket_Types());
		request.setAttribute("idstype", QunarBookVo.getIdstype());
		request.setAttribute("qunarChannel", QunarBookVo.getQunarChannel());
		request.setAttribute("history", logInfo);
		String IsLock = this.getParam(request, "IsLock");
		request.setAttribute("IsLock", IsLock);
		return "qunarbook/bookInfo";
	}

	@RequestMapping("/queryLianChengOrderInfo.do")
	public String queryLianChengOrderInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		HashMap<String, String> bookVo = qunarBookService
				.queryLianChengTotalOrderInfo(order_id);
		List<HashMap<String, String>> lianchengOrderInfoList = qunarBookService
				.queryLianChengOrderInfo(order_id);
		List<List<HashMap<String, String>>> lianchengDetailCpInfoList = new ArrayList<List<HashMap<String, String>>>();
		Map<String, String> accountMap = new HashMap<String, String>();
		List<Map<String, String>> lianchengPayInfoList = new ArrayList<Map<String, String>>();
		List<HashMap<String, String>> logInfo = qunarBookService
				.getBookLog(order_id);
		int index = 0;
		for (HashMap<String, String> map : lianchengOrderInfoList) {
			List<HashMap<String, String>> lianchengCpListInfo = qunarBookService
					.getBookCpInfo(map.get("trip_id"));
			
			HashMap<String, String> lianchengPayInfo = qunarBookService
					.queryLianChengPayInfo(map.get("trip_id"));
			String account = null;

			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("orderid", map.get("trip_id"));
			String params = null;
			try {
				params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				account = HttpUtil
						.sendByPost(find_accountinfo, params, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}

			accountMap.put("account" + map.get("trip_id"), account);
			// request.setAttribute(map.get("trip_id"), account);
			request.setAttribute("pay" + map.get("trip_id"), lianchengPayInfo);
			lianchengPayInfoList.add(lianchengPayInfo);
			request.setAttribute("lianchengDetailCpInfoList" + "_" + index++,
					lianchengCpListInfo);
			lianchengDetailCpInfoList.add(lianchengCpListInfo);
		}
		request.setAttribute("index", index);
		request.setAttribute("lianchengPayInfoList", lianchengPayInfoList);
		request.setAttribute("accountMap", accountMap);
		request.setAttribute("lianchengDetailCpInfoList",
				lianchengDetailCpInfoList);
		request.setAttribute("bookVo", bookVo);
		request.setAttribute("lianchengOrderInfoList", lianchengOrderInfoList);
		request.setAttribute("bookStatus", QunarBookVo.getBookStatus());
		request.setAttribute("seatType", QunarBookVo.getSeatType());
		request.setAttribute("ticketType", QunarBookVo.getTicket_Types());
		request.setAttribute("idstype", QunarBookVo.getIdstype());
		request.setAttribute("qunarChannel", QunarBookVo.getQunarChannel());
		request.setAttribute("history", logInfo);
		String IsLock = this.getParam(request, "IsLock");
		request.setAttribute("IsLock", IsLock);
		return "qunarbook/lianchengBookInfo";
	}

	@RequestMapping("/exportexcel.do")
	public String exportqunarExcel(HttpServletRequest request,
			HttpServletResponse response) {

		String order_id = this.getParam(request, "order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> order_status = this.getParamToList(request, "order_status");
		String begin_time = this.getParam(request, "begin_time");//出发时间
		String end_time = this.getParam(request, "end_time");//出发时间
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("out_ticket_billno", out_ticket_billno);
		map.put("begin_info_time", begin_info_time);
		map.put("end_info_time", end_info_time);
		map.put("order_status", order_status);
		map.put("begin_time", begin_time);
		map.put("end_time", end_time);
		List<Map<String, String>> reslist = qunarExcelService
				.queryQunarBook(map);
		List<Map<String, String>> lianchengList = qunarExcelService
				.queryLianChengQunarBook(map);
		if (reslist.addAll(lianchengList)) {
			System.out.println("联程list添加到总list中");
		} else {
			System.out.println("联程list没有添加到总list中");
		}
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {

			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(m.get("order_time"));
			linkedList.add(m.get("out_ticket_time"));
			String buy_money = m.get("buy_money");
			String pay_money = m.get("pay_money");
			String differ = m.get("differ");

			linkedList.add(pay_money);
			linkedList.add(buy_money);
			linkedList.add(differ);
			linkedList.add(m.get("out_ticket_account"));
			linkedList.add(m.get("bank_pay_seq"));
			if (!("1".equals(m.get("order_type")))) {
				linkedList.add("普通");
			} else {
				linkedList.add("联程");
			}

			list.add(linkedList);
			String from_time = m.get("from_time");
			linkedList.add(from_time);
		}
		String title = "去哪儿火车票预定明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "去哪儿预订.xls";
		String[] secondTitles = { "序号", "订单号", "12306订单号", "订单时间", "出票时间",
				"订票价", "实际订票价", "差价", "付款账号", "付款流水号", "订单类型" ,"出发日期"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);

		return null;
	}

	@RequestMapping("/exportrefundexcel.do")
	public String exportRefundExcel(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		String refund_seq = this.getParam(request, "refund_seq");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> refund_statusList = this.getParamToList(request, "refund_status");
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<String> refund_status = new ArrayList<String>(refund_statusList);
		if(refund_status.contains("012")){
			refund_status.add("00");
			refund_status.add("01");
			refund_status.add("02");
			refund_status.remove("012");
		}
		if(refund_status.contains("456")){
			refund_status.add("04");
			refund_status.add("05");
			refund_status.add("06");
			refund_status.remove("456");
		}
		map.put("order_id", order_id);
		map.put("refund_seq", refund_seq);
		map.put("begin_time", begin_info_time);
		map.put("end_time", end_info_time);
		map.put("refund_status", refund_status);
		List<Map<String, String>> reslist = qunarExcelService.queryRefundTicket(map);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("refund_seq"));
			linkedList.add(m.get("order_id"));
			String refund_money = m.get("refund_money");
			linkedList.add(m.get("create_time"));
			linkedList.add(m.get("verify_time"));
			String detail_refund = m.get("detail_refund");
			String detail_alter_tickets = m.get("detail_alter_tickets");
			if (refund_money != null)
				linkedList.add("￥" + refund_money);
			else
				linkedList.add(refund_money);
			if (detail_refund != null)
				linkedList.add("￥" + detail_refund);
			else
				linkedList.add(detail_refund);
			if (detail_alter_tickets != null)
				linkedList.add("￥" + detail_alter_tickets);
			else
				linkedList.add(detail_alter_tickets);
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("detail_refund"));
			linkedList.add(m.get("detail_alter_tickets"));
			linkedList.add(m.get("change_ticket_info"));
			list.add(linkedList);
		}

		String title = "去哪儿火车票退票明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "去哪儿退款.xls";
		String[] secondTitles = { "序号", "退款流水号", "订单号", "创建时间时间", "审核时间",
				"退款额", "实际退款金额", "改签差价", "12306退款流水号", "详细退款金额", "详细改签价格","改签明细" };
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);

		return null;
	}

	private String createDate(String begin_info_time, String end_info_time) {
		String date = "日期：";
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		if (begin_info_time.equals(end_info_time)
				|| begin_info_time == end_info_time) {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				date += ss.format(new Date());
			} else {
				date += begin_info_time;
			}
		} else {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += ss.format(new Date()) + "之前";
				} else {
					date += end_info_time + "之前";
				}
			} else {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += begin_info_time + "-------" + ss.format(new Date());
				} else {
					date += begin_info_time + "-------" + end_info_time;
				}
			}
		}
		return date;
	}
	
	/**
	 * 订单锁
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryOrderIsLock.do")
	@ResponseBody
	public void queryOrderIsLock(HttpServletResponse response ,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String order_id = this.getParam(request, "order_id");
		String opt_person = loginUserVo.getReal_name();
		String key = "Lock_" + order_id;
		String value = "Lock_"+order_id+"&"+opt_person;
		String isLock;
		isLock = (String) MemcachedUtil.getInstance().getAttribute(key); //读值
		if(StringUtils.isEmpty(isLock)){
			MemcachedUtil.getInstance().setAttribute(key, value, 5*60*1000); //写值
			isLock="";
		}else if(isLock.indexOf(opt_person) != -1){
			isLock = "";
		}
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(isLock);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("订单锁时response.getWriter()异常");
			e.printStackTrace();
		}
	}
	
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		List<HashMap<String, String>> history = qunarBookService
		.getBookLog(order_id);
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
