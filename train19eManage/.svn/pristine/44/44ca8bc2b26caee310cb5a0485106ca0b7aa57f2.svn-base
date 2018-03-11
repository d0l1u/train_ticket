package com.l9e.transaction.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.MeituanRefundService;
import com.l9e.transaction.service.MeituanBookService;
import com.l9e.transaction.service.SystemSettingService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.vo.AllRefundVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.MeituanVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.UrlFormatUtil;

@Controller
@RequestMapping("/meituanRefund")
public class MeituanRefundController extends BaseController {

	private static final Logger logger = Logger.getLogger(MeituanRefundController.class);
	private String find_accountinfo;
	@Resource
	private MeituanRefundService meituanRefundService;
	@Resource
	private SystemSettingService systemSettingService;
	@Resource
	private Tj_OpterService tj_OpterService;

	/** *********************��ȡ�˺�************************ */
	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}

	@RequestMapping("/queryRefundTicketPage.do")
	public String getRefundTicketPage(HttpServletRequest request,
			HttpServletResponse response) {
		return "redirect:/meituanRefund/queryRefundTicketList.do?" +
				"refund_status=03&refund_status=07&refund_status=33&refund_status=72&refund_status=73&refund_status=012&refund_status=456";
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
	public void queryPayIsLock(HttpServletResponse response,
			HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String order_id = this.getParam(request, "order_id");
//		String refund_seq = this.getParam(request, "refund_seq");
		if (order_id.contains("_")) {// “联程”车票订单锁
			order_id = order_id.substring(0, order_id.length() - 2);
		}
		String opt_person = loginUserVo.getReal_name();
		String key = "Lock_" + order_id;
		String value = "Lock_" + order_id + "&" + opt_person;
		String isLock;
		isLock = (String) MemcachedUtil.getInstance().getAttribute(key); // 读值
		if (StringUtils.isEmpty(isLock)) {
			MemcachedUtil.getInstance().setAttribute(key, value, 5 * 60 * 1000); // 写值
			isLock = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", order_id);
			map.put("content", opt_person + "锁定了订单：" + order_id);
			map.put("user", opt_person);
			meituanRefundService.insertLog(map);
			HashMap<String, Object> ordermap = new HashMap<String, Object>();
			ordermap.put("user", opt_person);
			ordermap.put("order_id", order_id);
			meituanRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
			meituanRefundService.updateRefundOpt(ordermap);// 更新退款表中操作人信息
		} else if (isLock.indexOf(opt_person) != -1) {
			isLock = "";
		}
		HashMap<String, Object> ordermap = new HashMap<String, Object>();
		ordermap.put("user", opt_person);
		ordermap.put("order_id", order_id);
		meituanRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
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
	
	//查询列表
	@RequestMapping("/queryRefundTicketList.do")
	public String getRefundTicketList(HttpServletRequest request, HttpServletResponse response) {
		
		String order_id = this.getParam(request, "order_id");
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
		String refund_seq = this.getParam(request, "refund_seq");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");
		List<String> refund_statusList = this.getParamToList(request,"refund_status");
		String opt_person = this.getParam(request, "opt_person");
		List<String> refund_status = new ArrayList<String>(refund_statusList);
		if(order_id.trim().length()<=0){
			request.setAttribute("refund_status", refund_status.toString());
		}
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
		List<String> notify_status = this.getParamToList(request,"notify_status");
		List<String> refund_type = this.getParamToList(request,"refund_type");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("refund_status", refund_status);
			paramMap.put("notify_status", notify_status);
			paramMap.put("refund_type", refund_type);
			paramMap.put("opt_person", opt_person);
		}
		
		int totalCount = meituanRefundService.getRefundTicketListCounts(paramMap);// 获得所有退款订单条数

		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());
		paramMap.put("pageSize", page.getEveryPageRecordCount());

		List<Map<String, String>> refundTicketList = meituanRefundService
				.getRefundTicketList(paramMap);

		request.setAttribute("isShowList", 1);
		request.setAttribute("refundTicketList", refundTicketList);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("refund_seq", refund_seq);
			request.setAttribute("refund_12306_seq", refund_12306_seq);
			request.setAttribute("notify_status", notify_status.toString());
			request.setAttribute("refund_type", refund_type.toString());
			request.setAttribute("opt_person", opt_person);
		}
		request.setAttribute("refundStatus", MeituanVo.getRefundStatus());
		request.setAttribute("notifyStatus", MeituanVo.getNotify_Status());
		request.setAttribute("refundTypes", MeituanVo.getRefund_types());
		request.setAttribute("returnOptlog", RefundTicketVo.getReturnOptlog());
		request.setAttribute("refund_and_alert", systemSettingService.querySystemRefundAndAlert("refund_and_alert"));
		return "meituanRefund/refundList";
	}
	
	@RequestMapping("/ticketInfo.do")
	public String getTicketInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String pageIndex =this.getParam(request, "pageIndex");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		if (order_id != null && !"".equals(order_id)) {
			String refund_seq = this.getParam(request, "refund_seq");
			String isActive = this.getParam(request, "isActive");
			Map<String, String> oMap = new HashMap<String, String>();
			oMap.put("order_id", order_id);
			oMap.put("cp_id", cp_id);
			oMap.put("refund_seq", refund_seq);
			HashMap<String, String> orderInfo = meituanRefundService.getRefundTicketInfo(order_id);
			List<HashMap<String, String>> cpInfo = meituanRefundService.getRefundTicketcpInfo(order_id);
			HashMap<String, String> refundInfo = meituanRefundService.getRefundInfo(oMap);
			List<HashMap<String, String>> history = meituanRefundService.queryLog(order_id);
			request.setAttribute("orderInfo", orderInfo);
			request.setAttribute("cpInfo", cpInfo);
			request.setAttribute("refundInfo", refundInfo);
			request.setAttribute("seattype", MeituanVo.getSeatType());
			request.setAttribute("ticketType", MeituanVo.getTicket_Types());
			request.setAttribute("idstype", MeituanVo.getIdstype());
			request.setAttribute("orderstatus", MeituanVo.getBookStatus());
			request.setAttribute("refuse_Reason", AllRefundVo.getRefuseReason().get("meituan"));
			request.setAttribute("history", history);
			request.setAttribute("refund_status", MeituanVo.getRefundStatus());
			Map<String, String> id_Map = new HashMap<String, String>();
			id_Map.put("order_id", order_id);
			List<Map<String, String>> ticketInfo = meituanRefundService.queryRefundTicketInfo(id_Map);
			Map<String, String> refundTicketInfo = ticketInfo.get(0);
			String account = null;
			if(!StringUtils.equals(refundTicketInfo.get("refund_status"),MeituanVo.REFUSEREFUND)) {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("orderid", order_id);
				String params = null;
				try {
					params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
					account = HttpUtil.sendByPost(find_accountinfo, params,
							"UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			request.setAttribute("isActive", isActive);
			request.setAttribute("account", account);
		}
		request.setAttribute("cp_id", cp_id);
		
		Map<String, Object> paramMapMoney = new HashMap<String, Object>();
		paramMapMoney.put("order_id", order_id);
		paramMapMoney.put("cp_id", cp_id);
		Map<String, String> map_buy = meituanRefundService.queryRefundMoney(paramMapMoney,"");
		Double sumRefundMoney = Double.parseDouble(map_buy.get("sumRefundMoney"));
		Double sumYhRefundMoney = Double.parseDouble(map_buy.get("sumYhRefundMoney"));
		Double sumXxRefundMoney = Double.parseDouble(map_buy.get("sumXxRefundMoney"));
		request.setAttribute("sumRefundMoney", sumRefundMoney);
		request.setAttribute("sumYhRefundMoney", sumYhRefundMoney);
		request.setAttribute("sumXxRefundMoney", sumXxRefundMoney);
		request.setAttribute("statusList", statusList);
		request.setAttribute("notifyList", notifyList);
		request.setAttribute("pageIndex", pageIndex);
		return "meituanRefund/refundInfo";
	}

	@RequestMapping("/refund.do")
	public String refund(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String pageIndex =this.getParam(request, "pageIndex");
		String orderid = this.getParam(request, "orderid");
		String refundseq = this.getParam(request, "refundseq");
		String refund_status = this.getParam(request, "refund_status");
		//System.out.println("!!!!!!!!!"+refund_status);
		String refundmoney = this.getParam(request, "refund_money");
		String actualRefundmoney = this.getParam(request, "actual_refund_money");
		String our_remark = this.getParam(request, "our_remark");
		double alter_tickets_money = 0;
		double actual_refund_money = 0;
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");
		String detail_refund = this.getParam(request, "detail_refund");
		String detail_alter_tickets = this.getParam(request,"detail_alter_tickets");
		String change_ticket_info = this.getParam(request, "change_ticket_info");
		
		List<String> orderIdList2 = meituanRefundService.queryOrderCpId(orderid);//查看普通订单的cp_id
			for(int i=0; i<orderIdList2.size(); i++){//不是联程票
				String cpId = orderIdList2.get(i);
				String cp_id = this.getParam(request, "cp_id_"+cpId);//改签车票
				if(cpId.equals(cp_id)){
					String alter_train_no = this.getParam(request, "alter_train_no_"+cp_id);//改签车次
					String alter_train_box = this.getParam(request, "alter_train_box_"+cp_id);//改签车厢
					String alter_seat_no = this.getParam(request, "alter_seat_no_"+cp_id);//改签座位
					String alter_seat_type = this.getParam(request, "alter_seat_type_"+cp_id);//改签坐席
					String alter_money = this.getParam(request, "alter_money_"+cp_id);//改签差额
					String refund_12306_money = this.getParam(request, "refund_12306_money_"+cp_id);//12306退款
					if(alter_train_no.trim().length()>0 || alter_train_box.trim().length()>0 || alter_seat_no.trim().length()>0 
							|| alter_seat_type.trim().length()>0 || alter_money.trim().length()>0 || refund_12306_money.trim().length()>0){
						HashMap<String, Object> param_Map = new HashMap<String, Object>();
						param_Map.put("cp_id", cp_id);
						param_Map.put("alter_train_no", alter_train_no);
						param_Map.put("alter_train_box", alter_train_box);
						param_Map.put("alter_seat_no", alter_seat_no);
						param_Map.put("alter_money", alter_money);
						param_Map.put("refund_12306_money", refund_12306_money);
						Set<String>kset=MeituanVo.getSeatType().keySet(); 
						for(String ks : kset){     
							if(alter_seat_type.equals(MeituanVo.getSeatType().get(ks))){          
								param_Map.put("alter_seat_type", ks);  
								System.out.println(ks);
								break;
							}else{
								param_Map.put("alter_seat_type", ""); 
							} 
						}
						
						meituanRefundService.updateAlertRefund(param_Map);
					}
				}
			}
		
		/* 处理退款数据 */
		if(!detail_refund.isEmpty()){
			String[] detail_refunds = detail_refund.split(",");
			for (int i = 0; i < detail_refunds.length; i++) {
				actual_refund_money += Double.parseDouble(detail_refunds[i].trim());
			}
		}
		
		if (!("".equals(detail_alter_tickets))) {
			String[] detail_alter_ticketss = detail_alter_tickets.split(",");
			for (int i = 0; i < detail_alter_ticketss.length; i++) {
				alter_tickets_money += Double.parseDouble(detail_alter_ticketss[i].trim());
			}
		}
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		HashMap<String, Object> pMap = new HashMap<String, Object>();
		paramMap.put("user", user);
		paramMap.put("refund_seq", refundseq);
		paramMap.put("order_id", orderid);
		if(refund_status.equals("07")){
			paramMap.put("refund_status", "11");
		}else if(refund_status.equals("03")){
			paramMap.put("refund_status", "11");
		}else if(refund_status.equals("72") || refund_status.equals("73")){
			paramMap.put("refund_status", "71");
		}else if(refund_status.equals("33")){
			paramMap.put("refund_status", "11");
		}else if(refund_status.equals("44")){
			paramMap.put("refund_status", "71");//搁置订单线下退款
//			System.out.println("refund_status="+refund_status);
		}
		paramMap.put("alter_tickets_money", alter_tickets_money);      
		paramMap.put("refund_money", refundmoney);
		paramMap.put("our_remark", our_remark);
		paramMap.put("actual_refund_money", actual_refund_money);
		if(!actualRefundmoney.isEmpty()){
			paramMap.put("actual_refund_money", actualRefundmoney);
		}
		paramMap.put("refund_12306_seq", refund_12306_seq);
		paramMap.put("detail_refund", detail_refund);
		paramMap.put("detail_alter_tickets", detail_alter_tickets);
		paramMap.put("change_ticket_info", change_ticket_info);
		
		meituanRefundService.updateRefund(paramMap);
		HashMap<String, String> orderInfo = meituanRefundService.getRefundTicketInfo(orderid);// 查询订单信息
		String content = "";
		if(refund_status.equals("07")){
			content = user + "点击了人工退票【同意退款】";
		}else if(refund_status.equals("03")){
			content = user + "点击了人工改签【同意退款】";
		}else if(refund_status.equals("72") ||refund_status.equals("73")){
			content = user + "点击了线下退款【线下退款】";
		}else if(refund_status.equals("33")){
			content = user + "点击了审核退款【退款成功】";
		}else if(refund_status.equals("44")){
			content = user + "点击了搁置订单【线下退款】";
		}
		pMap.put("order_id", orderid);
		pMap.put("order_time", orderInfo.get("create_time"));
		pMap.put("content", content);
		pMap.put("user", user);
		meituanRefundService.insertLog(pMap);
		//客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", user);
		optMap.put("channel", "meituan");
		optMap.put("all", "refund");
		tj_OpterService.operate(optMap);
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
			String[] arr1 = statusList.split(",");
			for(int i=0;i<arr1.length;i++){
				str1 += "&refund_status="+arr1[i];
			}
		}
		String str2 = "";
		if("".equals(notifyList)||notifyList==null){
			str2 = "";
		}else{
			String[] arr2 = notifyList.split(",");
			for(int i=0;i<arr2.length;i++){
				str2 += "&notify_status="+arr2[i];
			}
		}
		return "redirect:/meituanRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}

	@RequestMapping("/refuse.do")
	public String refuse(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String pageIndex =this.getParam(request, "pageIndex");
		String order_id = this.getParam(request, "orderid");
		String refundseq = this.getParam(request, "refundseq");
		String our_remark = this.getParam(request, "our_remark");
		String refuse_reason = this.getParam(request, "refuse_reason");

		List<String> orderIdList2 = meituanRefundService.queryOrderCpId(order_id);//查看普通订单的cp_id
			for(int i=0; i<orderIdList2.size(); i++){//不是联程票
				String cpId = orderIdList2.get(i);
				String cp_id = this.getParam(request, "cp_id_"+cpId);//改签车票
				if(cpId.equals(cp_id)){
					String alter_train_no = this.getParam(request, "alter_train_no_"+cp_id);//改签车次
					String alter_train_box = this.getParam(request, "alter_train_box_"+cp_id);//改签车厢
					String alter_seat_no = this.getParam(request, "alter_seat_no_"+cp_id);//改签座位
					String alter_seat_type = this.getParam(request, "alter_seat_type_"+cp_id);//改签坐席
					String alter_money = this.getParam(request, "alter_money_"+cp_id);//改签差额
					String refund_12306_money = this.getParam(request, "refund_12306_money_"+cp_id);//12306退款
					
					if(alter_train_no.trim().length()>0 || alter_train_box.trim().length()>0 || alter_seat_no.trim().length()>0 
							|| alter_seat_type.trim().length()>0 || alter_money.trim().length()>0 || refund_12306_money.trim().length()>0){
						HashMap<String, Object> param_Map = new HashMap<String, Object>();
						param_Map.put("cp_id", cp_id);
						param_Map.put("alter_train_no", alter_train_no);
						param_Map.put("alter_train_box", alter_train_box);
						param_Map.put("alter_seat_no", alter_seat_no);
						param_Map.put("alter_money", alter_money);
						param_Map.put("refund_12306_money", refund_12306_money);
						Set<String>kset=MeituanVo.getSeatType().keySet(); 
						for(String ks : kset){     
							if(alter_seat_type.equals(MeituanVo.getSeatType().get(ks))){          
								param_Map.put("alter_seat_type", ks);  
								System.out.println(ks);
								break;
							}else{
								param_Map.put("alter_seat_type", ""); 
							} 
						}
						
						meituanRefundService.updateAlertRefund(param_Map);
					}
				}
			}
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		HashMap<String, Object> pMap = new HashMap<String, Object>();
		paramMap.put("user", user);
		paramMap.put("order_id", order_id);
		paramMap.put("our_remark", our_remark);
		paramMap.put("refund_seq", refundseq);
		paramMap.put("refuse_reason", refuse_reason);
		
		meituanRefundService.updateRefuse(paramMap);
		HashMap<String, String> orderInfo = meituanRefundService
				.getRefundTicketInfo(order_id);
		String content = null;
		content = user + "点击了【拒绝退款】" + "，退款原因为：【"
				+ MeituanVo.getRefuseReason().get(refuse_reason) + "】";
		if(refuse_reason.isEmpty()){
			content=user + "点击了线下退款【拒绝退款】" ;
		}
		pMap.put("order_id", order_id);
		pMap.put("content", content);
		pMap.put("order_time", orderInfo.get("create_time"));
		pMap.put("user", user);
		meituanRefundService.insertLog(pMap);
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
			String[] arr1 = statusList.split(",");
			for(int i=0;i<arr1.length;i++){
				str1 += "&refund_status="+arr1[i];
			}
		}
		String str2 = "";
		if("".equals(notifyList)||notifyList==null){
			str2 = "";
		}else{
			String[] arr2 = notifyList.split(",");
			for(int i=0;i<arr2.length;i++){
				str2 += "&notify_status="+arr2[i];
			}
		}
		return "redirect:/meituanRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}

	// 查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,
			HttpServletRequest request) {
		String order_id = this.getParam(request, "order_id");
		List<HashMap<String, String>> history = meituanRefundService
				.queryLog(order_id);
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

	@RequestMapping("/updateOrderstatusToRobotGai.do")
	@ResponseBody
	public void updateOrderstatusToRobotGai(HttpServletResponse response,HttpServletRequest request, 
			String order_id, String cp_id, String refund_status, String refund_seq, String create_time) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();// 获取当前登录的人
		String result = null;
		if (StringUtil.isNotEmpty(order_id)) {
			Map<String, String> updateMap = new HashMap<String, String>();
			updateMap.put("refund_status", refund_status);
			updateMap.put("order_id", order_id);
			updateMap.put("cp_id", cp_id);
			updateMap.put("refund_seq", refund_seq);
			updateMap.put("user", user);
			meituanRefundService.updateOrderstatusToRobotGai(updateMap);
			result = "yes";
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", order_id);
			if("01".equals(refund_status)){
				map.put("content", user+"点击了【机器改签】");
			}else if("05".equals(refund_status)){
				map.put("content", user+"点击了【机器退票】");
			}else if("44".equals(refund_status)){
				map.put("content", user+"点击了【搁置订单】");
			}
			map.put("user", user);
			meituanRefundService.insertLog(map);
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
	
	@RequestMapping("toAddRefundPage.do")
	public String toAddRefundPage(HttpServletRequest request,HttpServletResponse response){
		
		String refund_type = this.getParam(request, "refund_type");	
		String order_id = this.getParam(request, "order_id");	
		String cpid= this.getParam(request, "cpid");
		
		
		if(!StringUtils.isEmpty(order_id) && !"55".equals(refund_type)){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("order_id", order_id);
			List<Map<String, String>> cpidList=meituanRefundService.queryRefundTicket(paramMap);
			request.setAttribute("cpidList", cpidList);
		}
		if(!StringUtils.isEmpty(order_id) && "55".equals(refund_type)){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("order_id", order_id);
			List<Map<String, String>> cpidList=meituanRefundService.queryChangeRefundTicket(paramMap);
			request.setAttribute("cpidList", cpidList);
		}
		request.setAttribute("cpid", cpid);
		request.setAttribute("order_id", order_id);
		request.setAttribute("refund_type", refund_type);
		return "meituanRefund/refundAdd";
	}
	
	/**
	 * 增加线下退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("refundTicketAdd.do")
	public String refundTicketAdd(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得操作人姓名opt_person
		/******************更改条件********************/
		String order_id = this.getParam(request, "order_id");						//订单号
		String cp_id = this.getParam(request, "cpid");								//车票ID
		String refund_money = this.getParam(request, "refund_money");	//退款金额
		String actual_refund_money = this.getParam(request, "actual_refund_money");	//12306退款金额
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");		//12306退款流水号
		String our_remark = this.getParam(request, "our_remark");					//退款原因
		String refund_type = this.getParam(request, "refund_type");
		if(refund_type!=null && refund_type.equals("55")){
			refund_type = "22";
		}
		if("".equals(our_remark) || StringUtils.isEmpty(our_remark)){
			our_remark = "线下退款";
		}
		String channel = this.getParam(request, "channel");	
		String content = opt_person+"点击了生成线下退票!orderId为："+order_id+",cpId为:"+cp_id+",金额为："+refund_money+",退款原因为："+our_remark;
		/******************创建容器********************/
		Map<String, Object> map_add = new HashMap<String, Object>();
		map_add.put("order_id", order_id);
		map_add.put("cp_id", cp_id);
		map_add.put("refund_money", refund_money);
		map_add.put("actual_refund_money", actual_refund_money);
		map_add.put("refund_12306_seq", refund_12306_seq);
		map_add.put("our_remark", our_remark);
		map_add.put("refund_type", refund_type);
		map_add.put("refund_seq", CreateIDUtil.createID("TK"));//自动生成以TK开头的退款流水号
		map_add.put("create_time", "now()");
		map_add.put("refund_status", "72");
		map_add.put("opt_person", opt_person);
		map_add.put("channel", channel);
		/****************将条件添加到容器******************/
		Map<String,String>log_Map = new HashMap<String,String>();       //日志容器
		log_Map.put("order_id", order_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("content", content);
		/****************执行操作******************/
		meituanRefundService.queryRefundTicketAdd(log_Map,map_add);	//执行车站退票
		return "redirect:/meituanRefund/queryRefundTicketList.do?refund_status=03&refund_status=07&refund_status=33&refund_status=72&refund_status=73";
	}
	
	//查看order_id是否存在
	@RequestMapping("/queryRefundTicketAdd.do")
	@ResponseBody
	public String queryRefundTicketOrderId(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");						
		String orderId = meituanRefundService.queryRefundTicketOrderId(order_id);
		//String orderIdExists = extRefundService.queryRefundTicketOrderIdExists(order_id);//查看该订单号是否已经生成差额退款
		//System.out.println("orderId="+orderId);
		String result = null;
		if(StringUtils.isEmpty(orderId)){
			result = "no";
//		}else if(!StringUtils.isEmpty(orderIdExists)){
//			result = "exists";
		}else{
			result = "yes";
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//限制金额不大于票面金额减去已退款金额
	@RequestMapping("/queryRefundMoney.do")
	@ResponseBody
	public String queryRefundMoney(HttpServletRequest request ,HttpServletResponse response){
		String orderId = this.getParam(request, "orderId");	
		String refundMoney = this.getParam(request, "refundMoney");
		String streamId = this.getParam(request, "streamId");
		String cpId=this.getParam(request, "cpId");	
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", orderId);
		paramMap.put("cp_id", cpId);
		Map<String, String> map_buy = meituanRefundService.queryRefundMoney(paramMap,streamId);
		String flag = null;
	//	System.out.println(map_buy.get("pay_money")+"~~~~~~~~~~~"+map_buy.get("sumRefundMoney"));
		String pay_money_sj=map_buy.get("pay_money");
		if(pay_money_sj==null ||"0".equals(pay_money_sj) ||"0.00".equals(pay_money_sj) ||"0.0".equals(pay_money_sj) )pay_money_sj=map_buy.get("buy_money");
		//System.out.println(map_buy.get("pay_money")+"^%%%%%%%%"+map_buy.get("buy_money")+pay_money_sj);
		if(map_buy!=null && (!pay_money_sj.isEmpty()) && (map_buy.get("sumRefundMoney")!=null)){
			Double pay_money = Double.parseDouble(pay_money_sj);
			Double sumRefundMoney = Double.parseDouble(map_buy.get("sumRefundMoney"));
			Double refund_money = 0.0;
			if(!streamId.isEmpty()){
			refund_money = Double.parseDouble(map_buy.get("refund_money"));
			}
			if( (sumRefundMoney-refund_money)+Double.parseDouble(refundMoney) <= pay_money ){
				flag = map_buy.get("sumRefundMoney");
			}else{
				flag = "no";
			}
		}else{
			flag = "exception";
		}
		try {
			response.getWriter().write(flag);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping("/notifyAgain.do")
	public String notifyAgain(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获取当前登录人
		
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String refundMoney = this.getParam(request, "refundMoney");
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String pageIndex =this.getParam(request, "pageIndex");
		String content = user + "点击了重新通知按钮,车票号："+cp_id+"退款金额更改为"+refundMoney;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stream_id", stream_id);
		paramMap.put("opt_person", user);
		paramMap.put("refundMoney", refundMoney);
		meituanRefundService.updateNotify_Again(paramMap);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("content" , content);
		map.put("user", user);
		meituanRefundService.insertLog(map);
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
			String[] arr1 = statusList.split(",");
			for(int i=0;i<arr1.length;i++){
				str1 += "&refund_status="+arr1[i];
			}
		}
		String str2 = "";
		if("".equals(notifyList)||notifyList==null){
			str2 = "";
		}else{
			String[] arr2 = notifyList.split(",");
			for(int i=0;i<arr2.length;i++){
				str2 += "&notify_status="+arr2[i];
			}
		}
		return "redirect:/meituanRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}
		
	//上传批量车站退票
	@RequestMapping("/uploadAddRefund.do")
	public String uploadAddRefund(HttpServletRequest request,
		HttpServletResponse response){
		return "meituanRefund/refundAdds";
	}
	
	//Excel批量导入
	@RequestMapping("/addAllRefundStation.do")
	public String addAllRefundStation(HttpServletRequest request,HttpServletResponse response) throws IOException{
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();
		String path = "";
		path = this.getParam(request, "file");
        String order_id = null;
        String cp_id = null;
        String refund_money = null;
        String detail_refund = null;
        String user_time= null;
        String channel= null;
    	String content ="";
		 // 检查
		String newPath = null;
		//上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile upfile = multipartRequest.getFile("excelFile");
		if(!upfile.isEmpty()){
			String fileSuffix = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));

			if(".xls".equalsIgnoreCase(fileSuffix)  || ".xlsx".equalsIgnoreCase(fileSuffix)){
				String fileName = CreateIDUtil.createID("XLS") + fileSuffix;
				String prePath = multipartRequest.getSession().getServletContext().getRealPath("/upload");
				String date = DateUtil.dateToString(new Date(), "yyyyMMdd");
				prePath = prePath + "/" + date;
		        File targetFile = new File(prePath, fileName);  
		        if(!targetFile.exists()){
		        	targetFile.mkdirs();
		        }
		        newPath = prePath + "/" + fileName; 
				upfile.transferTo(targetFile);
			}
		}
		logger.info("path是："+path); 
		File file=new File(newPath); 
		logger.info("file===="+file+"=======file.exists==="+file.exists()); 
        if (!file.exists()) {
            throw new FileNotFoundException("传入的文件不存在：" + path);
        }
        if (!(path.endsWith("xls") || path.endsWith("xlsx"))) {
            throw new FileFormatException("传入的文件不是excel");
        }
        // 获取workbook对象
        Workbook workbook = null;
        InputStream is =null;
        try {
            is= new FileInputStream(newPath);
            if (newPath.endsWith("xls")) {
                workbook = new HSSFWorkbook(is);
            } else if (newPath.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(is);
            }
            // 读文件 一个sheet一个sheet地读取
                Sheet sheet = workbook.getSheetAt(0);
                int firstRowIndex = sheet.getFirstRowNum();
                int lastRowIndex = sheet.getLastRowNum();
                // 读取数据行
                for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
                    Row currentRow = sheet.getRow(rowIndex);// 当前行
                        Cell cell1 = currentRow.getCell(0);
                        String content1 = this.getCellValue(cell1, true);
                        Cell cell2 = currentRow.getCell(1);
                        String content2 = this.getCellValue(cell2, true);
                    	if(StringUtils.isNotEmpty(content1) 
                     			&&StringUtils.isNotEmpty(content2)){
                        Map<String,Object> pMap = new HashMap<String,Object>();
                  		pMap.put("order_id", content1);
                  		pMap.put("cp_id", content2);
                  		pMap.put("refund_status", "22");
                  		int isExistNum =meituanRefundService.queryCpidIsRefundNum(pMap);//从数据库中查询该车票是否已经退过票
                  		String orderStatus =meituanRefundService.queryStatusByOrderId(pMap);//从数据库中查询该订单号是否是出票成功订单
                  		double buy_money=0.00;
                  		Map<String,String> money = meituanRefundService.queryMoneyByCpId(pMap);
                  		if(MapUtils.isNotEmpty(money)){
                  			if(money.get("buy_money")!=null){
                  			buy_money = Double.parseDouble(String.valueOf(money.get("buy_money")));
//                  			logger.info("buy_money=money.get(buy_money)");
                  			}else if(money.get("pay_money")!=null){
                  			buy_money = Double.parseDouble(String.valueOf(money.get("pay_money")));
//                  			logger.info("buy_money=money.get(pay_money)");
                  			}
                  		}
                  		if((!content1.equals(""))&&content1!=null){
                  			order_id =content1 ;//参数顺序为（列，行）
                  			cp_id = content2;//getContents().trim()是获取单元格内的值并去空格
                  			refund_money =this.getCellValue(currentRow.getCell(2), true);
                  			detail_refund = this.getCellValue(currentRow.getCell(3), true);
                  			user_time = this.getCellValue(currentRow.getCell(4), true);
                  			channel = this.getCellValue(currentRow.getCell(5), true);
                         	String refund_seq=CreateIDUtil.createID("CZ");
                  			
                         	if(StringUtils.isNotEmpty(order_id) 
                         			&&StringUtils.isNotEmpty(cp_id)
                         			&&StringUtils.isNotEmpty(refund_money)
                         			&&StringUtils.isNotEmpty(channel)){
                       		Map<String,Object> paramMap = new HashMap<String,Object>();
                          	paramMap.put("order_id", order_id);
                          	paramMap.put("cp_id", cp_id);
                          	paramMap.put("refund_money", refund_money);
                       		paramMap.put("refund_type", "33");//退款类型为车站退票
                       		if(isExistNum==0 && "33".equals(orderStatus) && buy_money>= Double.parseDouble(refund_money)){
                       			paramMap.put("refund_status", "81");
                       			paramMap.put("notify_status", "00");
                       			content=opt_person+" 批量车站退票,车票号："+cp_id+"【开始退票】";
                       		}else{
                       			paramMap.put("refund_status", "22");
                       			paramMap.put("notify_status", "22");
                       			if(isExistNum>0){
                           			content=opt_person+" 批量车站退票,车票号："+cp_id+" 异常退款【已拒绝】:已退款";
                           		}else if (! "33".equals(orderStatus) ){
                           			content=opt_person+" 批量车站退票,车票号："+cp_id+" 异常退款【已拒绝】:不是出票成功订单";
                           		}else if (buy_money< Double.parseDouble(refund_money)){
                           			content=opt_person+" 批量车站退票,车票号："+cp_id+" 异常退款【已拒绝】:退款金额"+refund_money+"大于票价"+buy_money;
                           		}else{
                           			content=opt_person+" 批量车站退票,车票号："+cp_id+" 异常退款【已拒绝】";
                           		}
                       		}
                       		paramMap.put("refund_seq", refund_seq);
                       		paramMap.put("detail_refund", detail_refund);
                       		paramMap.put("create_time","now()");
                       		paramMap.put("our_remark", "用户车站退票");
                       		paramMap.put("opt_person", opt_person);
                       		if("美团".equals(channel))channel="meituan";
                       		paramMap.put("channel", channel);
                       		paramMap.put("user_time", user_time);
                       		
                       		Map<String,String> logMap = new HashMap<String,String>();
                       		logMap.put("order_id", order_id);
                       		logMap.put("content", content);
                       		logMap.put("opt_person", opt_person);
                       		try {
                       			meituanRefundService.addMeituanStation(paramMap,logMap);
        					} catch (Exception e) {
        						logger.info("插入车站退票【失败】订单号："+order_id+"车票号："+cp_id);
        					}
        					
                         	}else{
                         		logger.info("格式错误，缺少上传必要元素");
                         	}
                      	}
                }else{
                	logger.info("格式错误，缺少上传必要元素");
                }
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(file.exists()){
       		if(file.getParentFile().isDirectory()){
          		file.delete();
          		logger.info("删除文件："+file);
          		file.getParentFile().delete();
          		logger.info("删除文件目录："+file.getParentFile());
          	}
        }
       	return "redirect:/meituanRefund/queryRefundTicketPage.do";
	}
	
    /**
     * 取单元格的值
     * @param cell 单元格对象
     * @param treatAsStr 为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
     * @return
     */
    private String getCellValue(Cell cell, boolean treatAsStr) {
        if (cell == null) {
            return "";
        }

        if (treatAsStr) {
            // 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
            // 加上下面这句，临时把它当做文本来读取
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue()).trim();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue()).trim();
        } else {
            return String.valueOf(cell.getStringCellValue()).trim();
        }
    }
    
    
	@RequestMapping("/deleteOrder.do")
	public String deleteOrder(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获取当前登录人
		
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String refund_seq = this.getParam(request, "refund_seq");
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String pageIndex =this.getParam(request, "pageIndex");
		String content = user + "点击了删除订单按钮,车票号："+cp_id;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stream_id", stream_id);
		paramMap.put("opt_person", user);
		paramMap.put("order_id", order_id);
		paramMap.put("cp_id", cp_id);
		paramMap.put("refund_seq", refund_seq);
		meituanRefundService.deleteOrder(paramMap);//假删除（订单改为拒绝退款，通知完成）
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("content" , content);
		map.put("user", user);
		meituanRefundService.insertLog(map);
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
			String[] arr1 = statusList.split(",");
			for(int i=0;i<arr1.length;i++){
				str1 += "&refund_status="+arr1[i];
			}
		}
		String str2 = "";
		if("".equals(notifyList)||notifyList==null){
			str2 = "";
		}else{
			String[] arr2 = notifyList.split(",");
			for(int i=0;i<arr2.length;i++){
				str2 += "&notify_status="+arr2[i];
			}
		}
		return "redirect:/meituanRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}
}
