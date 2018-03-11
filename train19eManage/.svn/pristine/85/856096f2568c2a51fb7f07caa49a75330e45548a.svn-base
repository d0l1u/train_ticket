package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.ElongBookService;
import com.l9e.transaction.service.ElongRefundService;
import com.l9e.transaction.vo.ElongVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.DateUtil;
import com.l9e.util.ExcelUtil;
import com.l9e.util.ExcelUtilNew;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.UrlFormatUtil;

@Controller
@RequestMapping("/elongBook")
public class ElongBookController extends BaseController {
	private static final Logger logger = Logger.getLogger(ElongBookController.class);
	@Resource
	private ElongBookService elongBookService;
	@Resource
	private ElongRefundService elongRefundService;

	private String find_accountinfo;

	/** *********************��ȡ�˺�************************ */
	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}

	@RequestMapping("/queryAllBookList.do")
	public String getAllBookList(HttpServletRequest request,
			HttpServletResponse response) {
		String chexiao = this.getParam(request, "chexiao");
		String abnormal = this.getParam(request, "abnormal");
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date backDate = DateUtil.dateAddDays(currentTime, -3);
		String dateString = formatter.format(backDate);
		if("1".equals(chexiao)){
			return "redirect:/elongBook/queryBookList.do?chexiao=" + chexiao + "&begin_info_time=" + dateString;
		}else if("1".equals(abnormal)){
			return "redirect:/elongBook/queryBookList.do?abnormal=" + abnormal + "&begin_info_time=" + dateString;
		}else{
			return "redirect:/elongBook/queryBookList.do?begin_info_time=" + dateString +  "&order_status=11&order_status=22";
		}
	}
	
	@RequestMapping("/queryNotifyList.do")
	public String queryNotifyList(HttpServletRequest request,
			HttpServletResponse response) {
		String notify_status = this.getParam(request, "notify_status");
		return "redirect:/elongBook/queryBookList.do?notify_status=" + notify_status ;
	}

	@RequestMapping("/queryBookList.do")
	public String queryBookList(HttpServletRequest request,
			HttpServletResponse response) {
		String chexiao = this.getParam(request, "chexiao");
		String abnormal = this.getParam(request, "abnormal");
		String order_id = this.getParam(request, "order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		String begin_info_time = this.getParam(request, "begin_info_time");
		if(StringUtils.isEmpty(begin_info_time)){
			Calendar theCa = Calendar.getInstance(); 
			theCa.setTime(new Date());  
			theCa.add(theCa.DATE, -1); 
			Date date = theCa.getTime();
			DateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
			String querydate=dff.format(date);
			begin_info_time = querydate;
		}
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> order_status = this.getParamToList(request, "order_status");
		List<String> notify_status = this.getParamToList(request, "notify_status");
		List<String> order_source = this.getParamToList(request, "order_source");
		List<String> channelList = this.getParamToList(request,"channel");
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
		paramMap.put("channel", channelList);
		}
		List<ElongVo> bookList =new ArrayList<ElongVo>();
		if("1".equals(chexiao)){
			int totalCount = elongBookService.getBookListCountCx(paramMap);
			PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());
			paramMap.put("pageSize", page.getEveryPageRecordCount());
			bookList = elongBookService.getBookListCx(paramMap);
		}else{
		int totalCount = elongBookService.getBookListCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());
		paramMap.put("pageSize", page.getEveryPageRecordCount());
			bookList = elongBookService.getBookList(paramMap);
		}
		request.setAttribute("bookStatus", ElongVo.getBookStatus());
		request.setAttribute("notifyStatus", ElongVo.getNotifyStatus());
		request.setAttribute("isShowList", 1);
		for (ElongVo qunarBook : bookList) {
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
			request.setAttribute("order_status", order_status);
			request.setAttribute("notify_status", notify_status);
			request.setAttribute("order_source", order_source);
			request.setAttribute("channelStr", channelList.toString());
		}
		request.setAttribute("Channel", ElongVo.getQunarChannel());
		if("1".equals(chexiao))
			return "elongCheXiao/elongCheXiao";
		else if("1".equals(abnormal))
			return "elongAbnormal/elongAbnormal";
		else
			return "elongBook/bookList";
	}

	//批量撤销订单
	@RequestMapping("/updateCheXiao.do")
	public String updateCheXiao(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获得当前登录人姓名
		//获取url查询条件
//		String pageIndex = this.getParam(request, "pageIndex");
		String jsonArr = "["+this.getParam(request, "jsonArr")+"]";
		String statusList = this.getParam(request, "statusList");//得到选中的状态
		JSONArray array = JSONArray.fromObject(jsonArr); //转换为json数组
		for(int i = 0; i < array.size(); i++){ 
			JSONObject jsonObject = array.getJSONObject(i); 
			String order_id = (String) jsonObject.get("order_id");
			String db_order_status = elongBookService.queryDbOrder_status(order_id);
			String db_notify_status = elongBookService.queryDbNotify_status(order_id);
			if((!"88".equals(db_order_status))&& (!"10".equals(db_order_status))&& (!"99".equals(db_order_status))&& (!"22".equals(db_notify_status))){
			String create_time = (String) jsonObject.get("create_time");
			String userAccount=user+"点击了【批量撤销】";
			if(StringUtil.isNotEmpty(order_id)){
				Map<String,String>updateMap = new HashMap<String,String>();
				updateMap.put("order_status", "51");
				updateMap.put("order_id", order_id);
				updateMap.put("opt_ren", user);
				elongBookService.updateCheXiao(updateMap);
				elongBookService.updateCheXiaoCp(updateMap);
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("opt_person", user);
				paramMap.put("content", userAccount);
				//添加操作日志
				elongBookService.insertLog(paramMap);
				logger.info("【批量撤销】撤销并添加通知成功，订单id为："+order_id+"，操作人为【"+user+"】");
			}
		}
		}
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++)
				str += "&order_status="+arr[i];
		
		if(null==statusList || "".equals(statusList))
			return "redirect:/elongBook/queryBookList.do?chexiao=1";
		else
			return "redirect:/elongBook/queryBookList.do?chexiao=1"+str;
	}
	
	
	@RequestMapping("/updateGotoCheXiao.do")
	@ResponseBody
	public void updateGotoCheXiao(HttpServletResponse response,HttpServletRequest request,String order_id,String create_time){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		String userAccount=user+"点击了撤销订单";
		String result=null;
		if(StringUtil.isNotEmpty(order_id)){
			String db_order_status = elongBookService.queryDbOrder_status(order_id);
			String db_notify_status = elongBookService.queryDbNotify_status(order_id);
			if( (!"88".equals(db_order_status))&& (!"10".equals(db_order_status))&& (!"99".equals(db_order_status))&& (!"22".equals(db_notify_status))){
				Map<String,String>updateMap = new HashMap<String,String>();
				updateMap.put("order_status", "51");
				updateMap.put("order_id", order_id);
				updateMap.put("opt_ren", user);
				result=elongBookService.updateCheXiao(updateMap);
				String resultCp= elongBookService.updateCheXiaoCp(updateMap);
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("opt_person", user);
				paramMap.put("content", userAccount);
				//添加操作日志
				elongBookService.insertLog(paramMap);
				if("yes".equals(resultCp))
				logger.info("【撤销订单】撤销(cp_orderinfo表和elong_orderinfo表)并添加通知成功，订单id为："+order_id+"操作人为【"+user+"】");
				else
				logger.info("【撤销订单】撤销(elong_orderinfo表)并添加通知成功，订单id为："+order_id+"操作人为【"+user+"】");	
			}
			else
				result="no";
		}
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.error("撤销订单response返回参数时异常");
			e.printStackTrace();
		}
	}
		
	@RequestMapping("/notifyAgain.do")
	public void notifyAgain(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获取当前登录人
		String order_id = this.getParam(request, "order_id");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("opt_ren", user);
		elongBookService.updateNotify_Again(request,response,paramMap);
			
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", order_id);
		String content = user + "点击了重新通知按钮";
		logMap.put("content" , content);
		logMap.put("opt_person", user);
		elongBookService.insertLog(logMap);
	}
		
	@RequestMapping("/notify.do")
	public void updateNotify_status(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获取当前登录人
		
		String order_id = this.getParam(request, "order_id");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("opt_ren", user);
		elongBookService.updateNotify_status(request,response,paramMap);
		
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", order_id);
		String content = user + "点击了通知成功按钮";
		logMap.put("content" , content);
		logMap.put("opt_person", user);
		elongBookService.insertLog(logMap);
	}

	@RequestMapping("/bookInfo.do")
	public String bookInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		HashMap<String, String> bookVo = elongBookService
				.getBookOrderInfo(order_id);
		List<HashMap<String, String>> cpListInfo = elongBookService
				.getBookCpInfo(order_id);
		List<HashMap<String, String>> logInfo = elongBookService
				.getBookLog(order_id);
		HashMap<String, String> notify_status = elongBookService
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
		request.setAttribute("bookStatus", ElongVo.getBookStatus());
		request.setAttribute("seatType", ElongVo.getSeatType());
		request.setAttribute("Channel", ElongVo.getQunarChannel());
		request.setAttribute("bookVo", bookVo);
		request.setAttribute("cpListInfo", cpListInfo);
		request.setAttribute("notify_status", notify_status);
		request.setAttribute("ticketType", ElongVo.getTicket_Types());
		request.setAttribute("idstype", ElongVo.getIdstype());
		request.setAttribute("history", logInfo);
		String IsLock = this.getParam(request, "IsLock");
		request.setAttribute("IsLock", IsLock);
		return "elongBook/bookInfo";
	}

	@RequestMapping("/queryLianChengOrderInfo.do")
	public String queryLianChengOrderInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		HashMap<String, String> bookVo = elongBookService
				.queryLianChengTotalOrderInfo(order_id);
		List<HashMap<String, String>> lianchengOrderInfoList = elongBookService
				.queryLianChengOrderInfo(order_id);
		List<List<HashMap<String, String>>> lianchengDetailCpInfoList = new ArrayList<List<HashMap<String, String>>>();
		Map<String, String> accountMap = new HashMap<String, String>();
		List<Map<String, String>> lianchengPayInfoList = new ArrayList<Map<String, String>>();
		List<HashMap<String, String>> logInfo = elongBookService
				.getBookLog(order_id);
		int index = 0;
		for (HashMap<String, String> map : lianchengOrderInfoList) {
			List<HashMap<String, String>> lianchengCpListInfo = elongBookService
					.getBookCpInfo(map.get("trip_id"));
			
			HashMap<String, String> lianchengPayInfo = elongBookService
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
		request.setAttribute("bookStatus", ElongVo.getBookStatus());
		request.setAttribute("seatType", ElongVo.getSeatType());
		request.setAttribute("ticketType", ElongVo.getTicket_Types());
		request.setAttribute("idstype", ElongVo.getIdstype());
		request.setAttribute("history", logInfo);
		return "elongBook/lianchengBookInfo";
	}

	//艺龙预定管理--导出excel
	@RequestMapping("/exportexcel.do")
	public String exportqunarExcel(HttpServletRequest request,
			HttpServletResponse response) {

		String order_id = this.getParam(request, "order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> order_status = this.getParamToList(request, "order_status");
		List<String> notify_status = this.getParamToList(request, "notify_status");
		List<String> channel = this.getParamToList(request, "channel");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("out_ticket_billno", out_ticket_billno);
		map.put("begin_time", begin_info_time);
		map.put("end_time", end_info_time);
		map.put("order_status", order_status);
		map.put("notify_status", notify_status);
		map.put("channel", channel);
		List<Map<String, String>> reslist = elongBookService.queryQunarBookCp(map);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		String bfOrderId="";
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			if(bfOrderId.equals(m.get("order_id"))){
				linkedList.add("");
			}else{
				bfOrderId=m.get("order_id");
				linkedList.add(m.get("order_id"));
			}
			linkedList.add(m.get("user_name"));
			linkedList.add(m.get("cp_id"));
			linkedList.add(m.get("order_name"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(m.get("order_time"));
			linkedList.add(m.get("out_ticket_time"));
			String buy_money = m.get("buy_money");
			String pay_money = m.get("pay_money");
			String differ = m.get("differ");
			String all_buy_money = m.get("all_buy_money");
			String all_pay_money = m.get("all_pay_money");
			String all_differ = m.get("all_differ");

			linkedList.add(pay_money);
			linkedList.add(buy_money);
			linkedList.add(differ);
			
			linkedList.add(m.get("out_ticket_account"));
			linkedList.add(m.get("bank_pay_seq"));
			String from_time = m.get("from_time");
			linkedList.add(from_time);
			
			linkedList.add(all_pay_money);
			linkedList.add(all_buy_money);
			linkedList.add(all_differ);
			linkedList.add(m.get("ticket_num"));
			if("tongcheng".equals(m.get("channel")))linkedList.add("同程");
			else if("elong".equals(m.get("channel")))linkedList.add("艺龙");
			list.add(linkedList);
		}
		
		String title = "代理管理预定明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "代理管理预订.xls";
//		String[] secondTitles = { "序号", "订单号", "12306订单号", "订单时间", "出票时间",
//				"订票价", "实际订票价", "差价", "付款账号", "付款流水号", "出发日期", "票数"};
		String[] secondTitles = { "序号", "订单号", "乘客姓名" ,"车票号","出发/到达","12306订单号", "订单时间", "出票时间",
				"订票价", "实际订票价", "差价", "付款账号", "付款流水号", "出发日期","总订票价", "总实际订票价", "总差价","票数","渠道"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
		
//		XSSFWorkbook book2 = ExcelUtilNew.createExcel(filename, title, date,
//				secondTitles, list, request, response);

		return null;
	}
	
	//艺龙退票管理--导出excel
	@RequestMapping("/exportrefundexcel.do")
	public String exportRefundExcel(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String refund_seq = this.getParam(request, "refund_seq");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> refund_status = this.getParamToList(request, "refund_status");
		List<String> refund_type = this.getParamToList(request,"refund_type");
		List<String> channel = this.getParamToList(request,"channel");
		String opt_person = this.getParam(request, "opt_person");
		String user_time = this.getParam(request, "user_time");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("cp_id", cp_id);
		map.put("refund_seq", refund_seq);
		map.put("begin_time", begin_info_time);
		map.put("end_time", end_info_time);
		map.put("refund_status", refund_status);
		map.put("refund_type", refund_type);
		map.put("channel", channel);
		map.put("opt_person", opt_person);
		map.put("user_time", user_time);
		List<Map<String, String>> reslist = elongBookService.queryRefundTicket(map);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("refund_seq"));
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("cp_id"));
			String refund_money = m.get("refund_money");
			linkedList.add(m.get("create_time"));
			if((m.get("verify_time")==null)){
				linkedList.add(m.get("create_time"));
			}else{
				linkedList.add(m.get("verify_time"));
			}
			
			String actual_refund_money = m.get("actual_refund_money");
			String alter_tickets_money = m.get("alter_tickets_money");
			if (refund_money != null)
				linkedList.add("￥" + refund_money);
			else
				linkedList.add(refund_money);
			if (actual_refund_money != null)
				linkedList.add("￥" + actual_refund_money);
			else
				linkedList.add(m.get("detail_refund"));
			if (alter_tickets_money != null)
				linkedList.add("￥" + alter_tickets_money);
			else
				linkedList.add(m.get("detail_alter_tickets"));
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(m.get("detail_refund"));
			linkedList.add(m.get("detail_alter_tickets"));
			linkedList.add(m.get("change_ticket_info"));
			linkedList.add(m.get("ticket_num"));
			String user_name =elongBookService.queryRefundTicketName(m.get("cp_id")); 
//			System.out.println(m.get("cp_id") +"@@@@@@@@@@@@@"+user_name );
			linkedList.add(user_name);
			linkedList.add(m.get("order_name"));
			if("tongcheng".equals(m.get("channel")))linkedList.add("同程");
			else if("elong".equals(m.get("channel")))linkedList.add("艺龙");
			if("11".equals(m.get("refund_type")))linkedList.add("用户退款");
			else if("22".equals(m.get("refund_type")))linkedList.add("线下退款");
			else if("33".equals(m.get("refund_type")))linkedList.add("车站退票");
			linkedList.add(String.valueOf(m.get("user_time")));
			list.add(linkedList);
		}

		String title = "代理管理退票明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "代理管理退款.xls";
		String[] secondTitles = { "序号", "退款流水号", "订单号", "车票号","创建时间", "审核时间",
				"退款金额", "12306退款金额", "改签差价", "12306退款流水号", "12306单号","详细退款金额", "详细改签价格", "改签明细", "票数" , "乘客姓名" ,"出发/到达","渠道","退款类型","用户退款时间"};
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
			Map<String, String> logMap = new HashMap<String, String>();
			logMap.put("order_id", order_id);
			String content =  opt_person + "锁定了订单：" + order_id;
			logMap.put("content" , content);
			logMap.put("opt_person", opt_person);
			elongBookService.insertLog(logMap);
			HashMap<String, Object> ordermap = new HashMap<String, Object>();
			ordermap.put("user", opt_person);
			ordermap.put("order_id", order_id);
			elongRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
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
		List<HashMap<String, String>> history = elongBookService
		.getBookLog(order_id);
		JSONArray jsonArray = JSONArray.fromObject(history);  
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(jsonArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	//异常管理重新通知
	@RequestMapping("/updateGotoNormal.do")
	@ResponseBody
	public void updateGotoNormal(HttpServletResponse response,HttpServletRequest request,String order_id,String order_status){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		String userAccount = "";
		String result=null;
		if(StringUtil.isNotEmpty(order_id)){
				Map<String,String>updateMap = new HashMap<String,String>();
				if("22".equals(order_status)){
					updateMap.put("book_notify_status", "00");
					updateMap.put("book_notify_num", "0");
					userAccount = user+"点击了异常管理【重新通知】book";
				}else if("33".equals(order_status) ||"44".equals(order_status)){
					updateMap.put("out_notify_status", "00");
					updateMap.put("out_notify_num", "0");
					userAccount = user+"点击了异常管理【重新通知】out";
				}
				updateMap.put("order_id", order_id);
				result=elongBookService.updateGotoNormal(updateMap);
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("opt_person", user);
				paramMap.put("content", userAccount);
				//添加操作日志
				elongBookService.insertLog(paramMap);
		}
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.error("异常管理【重新通知】response返回参数时异常"+e);
			e.printStackTrace();
		}
	}
	
	//异常管理预订失败
	@RequestMapping("/updateGotoFailure.do")
	public void updateGotoFailure(HttpServletResponse response,HttpServletRequest request,String order_id,String order_status){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		String userAccount=user+"点击了异常管理【预订失败】book_fail";
		String result=null;
		if(StringUtil.isNotEmpty(order_id)){
			
				Map<String,String>updateMap = new HashMap<String,String>();
				updateMap.put("order_status", "44");
				updateMap.put("out_fail_reason", "1");
				updateMap.put("order_id", order_id);
				
				updateMap.put("book_notify_status", "00");
				updateMap.put("book_notify_num", "0");
				result = elongBookService.updateGotoFailure(updateMap);
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("opt_person", user);
				paramMap.put("content", userAccount);
				//添加操作日志
				elongBookService.insertLog(paramMap);
		}
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.error("异常管理【预订失败】response返回参数时异常"+e);
			e.printStackTrace();
		}
	}
	
	
}
