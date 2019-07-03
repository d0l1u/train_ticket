package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.QunarBookService;
import com.l9e.transaction.service.QunarRefundService;
import com.l9e.transaction.service.SystemSettingService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.QunarBookVo;
import com.l9e.transaction.vo.QunarRefundVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.UrlFormatUtil;

@Controller
@RequestMapping("/qunarrefund")
public class QunarRefundController extends BaseController {

	private static final Logger logger = Logger.getLogger(QunarRefundController.class);
	private String find_accountinfo;
	@Resource
	private QunarRefundService qunarRefundService;
	@Resource
	private QunarBookService qunarBookService;
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
		return "redirect:/qunarrefund/queryRefundTicketList.do?refund_status=03&refund_status=07&refund_status=33";
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
		String refund_seq = this.getParam(request, "refund_seq");
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
			qunarRefundService.insertLog(map);
			HashMap<String, Object> ordermap = new HashMap<String, Object>();
			ordermap.put("user", opt_person);
			ordermap.put("order_id", order_id);
			ordermap.put("refund_seq", refund_seq);
			qunarRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
			qunarRefundService.updateRefundOpt(ordermap);// 更新退款表中操作人信息
		} else if (isLock.indexOf(opt_person) != -1) {
			isLock = "";
		}
		HashMap<String, Object> ordermap = new HashMap<String, Object>();
		ordermap.put("user", opt_person);
		ordermap.put("order_id", order_id);
		qunarRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
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
		String end_info_time = this.getParam(request, "end_info_time");
		String refund_seq = this.getParam(request, "refund_seq");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");
		List<String> refund_statusList = this.getParamToList(request,"refund_status");
		String opt_person = this.getParam(request, "opt_person");
		String order_type = this.getParam(request, "order_type");
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
		String order_source = this.getParam(request, "order_source");// 订单来源
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		String liancheng="";
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("refund_status", refund_status);
			paramMap.put("notify_status", notify_status);
			paramMap.put("opt_person", opt_person);
//			System.out.println(order_source.toString().trim().length());
			paramMap.put("order_source", order_source);
			paramMap.put("order_type", order_type);
			liancheng="1";
		}
		
		int totalCount = qunarRefundService.getRefundTicketListCounts(paramMap);// 获得所有退款订单条数
		int totalCount2 =0;
		if("1".equals(liancheng)){
		HashMap<String, Object> paramMap2 = new HashMap<String, Object>();
		paramMap2.put("liancheng", "1");
			totalCount2 = qunarRefundService.getRefundTicketListCounts(paramMap);// 获得所有退款订单条数
		}
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount+totalCount2);
		
		paramMap.put("everyPagefrom", page.getFirstResultIndex());
		paramMap.put("pageSize", page.getEveryPageRecordCount());
		if("1".equals(liancheng)){
		paramMap.put("liancheng", "1");
		}
		List<Map<String, String>> refundTicketList = qunarRefundService.getRefundTicketList(paramMap);

		request.setAttribute("refundTicketList", refundTicketList);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("refund_seq", refund_seq);
			request.setAttribute("refund_12306_seq", refund_12306_seq);
			request.setAttribute("notify_status", notify_status.toString());
			request.setAttribute("order_source", order_source);
			request.setAttribute("opt_person", opt_person);
			request.setAttribute("order_type", order_type);
		}
		request.setAttribute("isShowList", 1);
		request.setAttribute("refundStatus", QunarRefundVo.getRefundStatus());
		request.setAttribute("notifyStatus", QunarRefundVo.getNotifyStatus());
		request.setAttribute("qunarChannel", QunarBookVo.getQunarChannel());
		request.setAttribute("refund_and_alert", systemSettingService.querySystemRefundAndAlert("refund_and_alert"));
		request.setAttribute("returnOptlog", RefundTicketVo.getReturnOptlog());
		
		return "qunarRefund/refundList";
	}
	
	@RequestMapping("/ticketInfo.do")
	public String getTicketInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		if (order_id != null && !"".equals(order_id)) {
			String refund_seq = this.getParam(request, "refund_seq");
			String isActive = this.getParam(request, "isActive");
			String statusList = this.getParam(request, "statusList");//得到选中的退款状态pageIndex
			String notifyList = this.getParam(request, "notifyList");
			String  order_source = this.getParam(request, "order_source");
			String pageIndex = this.getParam(request, "pageIndex");
			Map<String, String> oMap = new HashMap<String, String>();
			oMap.put("order_id", order_id);
			oMap.put("refund_seq", refund_seq);
			HashMap<String, String> orderInfo = qunarRefundService
					.getRefundTicketInfo(order_id);
			List<HashMap<String, String>> cpInfo = qunarRefundService
					.getRefundTicketcpInfo(order_id);
			HashMap<String, String> refundInfo = qunarRefundService
					.getRefundInfo(oMap);
			List<HashMap<String, String>> history = qunarRefundService
					.queryLog(order_id);
			request.setAttribute("orderInfo", orderInfo);
			request.setAttribute("cpInfo", cpInfo);
			request.setAttribute("refundInfo", refundInfo);
			request.setAttribute("seattype", QunarBookVo.getSeatType());
			request.setAttribute("ticketType", QunarBookVo.getTicket_Types());
			request.setAttribute("idstype", QunarBookVo.getIdstype());
			request.setAttribute("orderstatus", QunarBookVo.getBookStatus());
			request.setAttribute("refuse_Reason", QunarRefundVo
					.getRefuseReason());
			request.setAttribute("history", history);
			request.setAttribute("refund_status", QunarRefundVo
					.getRefundStatus());
			Map<String, String> id_Map = new HashMap<String, String>();
			id_Map.put("order_id", order_id);
			List<Map<String, String>> ticketInfo = qunarRefundService
					.queryRefundTicketInfo(id_Map);
			Map<String, String> refundTicketInfo = ticketInfo.get(0);
			String account = null;
			if(!StringUtils.equals(refundTicketInfo.get("refund_status"),QunarRefundVo.REFUSEREFUND)) {
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
			request.setAttribute("statusList", statusList);
			request.setAttribute("notifyList", notifyList);
			request.setAttribute("pageIndex", pageIndex);
			request.setAttribute("order_source", order_source);
			
		}
		request.setAttribute("qunarChannel", QunarBookVo.getQunarChannel());
		return "qunarRefund/refundInfo";
	}

	@RequestMapping("/lianchengTicketInfo.do")
	public String queryLianchengTicketInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		String refund_seq = this.getParam(request, "refund_seq");
		String isActive = this.getParam(request, "isActive");
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");
		String pageIndex = this.getParam(request, "pageIndex");
		Map<String, String> oMap = new HashMap<String, String>();
		oMap.put("order_id", order_id);
		oMap.put("refund_seq", refund_seq);
		HashMap<String, String> refundInfo = qunarRefundService
				.getRefundInfo(oMap);
		HashMap<String, String> bookVo = qunarRefundService
				.getRefundTicketInfo(order_id);
		List<HashMap<String, String>> lianchengOrderInfoList = qunarBookService
				.queryLianChengOrderInfo(order_id);
		List<HashMap<String, String>> history = qunarRefundService
				.queryLog(order_id);
		for (HashMap<String, String> map : history) {
			//System.out.println("日志信息" + map);
		}
		List<List<HashMap<String, String>>> lianchengDetailCpInfoList = new ArrayList<List<HashMap<String, String>>>();
		Map<String, String> accountMap = new HashMap<String, String>();
		List<Map<String, String>> lianchengPayInfoList = new ArrayList<Map<String, String>>();
		int index = 0;
		String out_ticket_billno = "";//12306取票单号
		for (HashMap<String, String> map : lianchengOrderInfoList) {
			out_ticket_billno += map.get("out_ticket_billno")+"/";
			List<HashMap<String, String>> lianchengCpListInfo = qunarBookService
					.getBookCpInfo(map.get("trip_id"));
			System.out.println(map.get("trip_id"));
			HashMap<String, String> lianchengPayInfo = qunarBookService
					.queryLianChengPayInfo(map.get("trip_id"));
			String account = null;

			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("orderid", map.get("trip_id"));
			String params = null;
			try {
				params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				account = HttpUtil.sendByPost(find_accountinfo, params, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}

			accountMap.put("account" + map.get("trip_id"), account);
			// request.setAttribute(map.get("trip_id"), account);
			request.setAttribute("pay" + map.get("trip_id"), lianchengPayInfo);
			lianchengPayInfoList.add(lianchengPayInfo);
			request.setAttribute("lianchengDetailCpInfoList" + "_" + index++, lianchengCpListInfo);
			lianchengDetailCpInfoList.add(lianchengCpListInfo);
		}
		out_ticket_billno = out_ticket_billno.substring(0, out_ticket_billno.length()-1);
		System.out.println("out_ticket_billno 12306取票单号:"+out_ticket_billno);
		request.setAttribute("out_ticket_billno", out_ticket_billno);
		request.setAttribute("isActive", isActive);
		request.setAttribute("index", index);
		request.setAttribute("lianchengPayInfoList", lianchengPayInfoList);
		request.setAttribute("accountMap", accountMap);
		request.setAttribute("lianchengDetailCpInfoList", lianchengDetailCpInfoList);
		request.setAttribute("bookVo", bookVo);
		request.setAttribute("history", history);
		request.setAttribute("orderstatus", QunarBookVo.getBookStatus());
		request.setAttribute("refuse_Reason", QunarRefundVo.getRefuseReason());
		request.setAttribute("lianchengOrderInfoList", lianchengOrderInfoList);
		request.setAttribute("bookStatus", QunarBookVo.getBookStatus());
		request.setAttribute("seatType", QunarBookVo.getSeatType());
		request.setAttribute("ticketType", QunarBookVo.getTicket_Types());
		request.setAttribute("idstype", QunarBookVo.getIdstype());
		request.setAttribute("refund_status", QunarRefundVo.getRefundStatus());
		request.setAttribute("refundInfo", refundInfo);
		request.setAttribute("qunarChannel", QunarBookVo.getQunarChannel());
		request.setAttribute("statusList", statusList);
		request.setAttribute("notifyList",notifyList);
		request.setAttribute("pageIndex", pageIndex);
		return "qunarRefund/lianchengRefundInfo";
	}

	@RequestMapping("/refund.do")
	public String refund(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();
		String orderid = this.getParam(request, "orderid");
		String refundseq = this.getParam(request, "refundseq");
		String refund_status = this.getParam(request, "refund_status");
		String refundmoney = this.getParam(request, "refund_money");
		String our_remark = this.getParam(request, "our_remark");
		String statusList = this.getParam(request, "statusList");
		String notifyList = this.getParam(request, "notifyList");
		String pageIndex = this.getParam(request, "pageIndex");
		String  order_source = this.getParam(request, "order_source");
		double alter_tickets_money = 0;
		double actual_refund_money = 0;
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");
		String detail_refund = this.getParam(request, "detail_refund");
		String detail_alter_tickets = this.getParam(request,"detail_alter_tickets");
		String change_ticket_info = this.getParam(request, "change_ticket_info");
		
		List<String> orderIdList = qunarRefundService.queryLianchengOrder_id(orderid);//查看是否是联程票
		List<String> orderIdList2 = qunarRefundService.queryOrderCpId(orderid);//查看普通订单的cp_id
		if(orderIdList.size()>0){//为联程票
			for(int i=0;i<orderIdList.size();i++){
				String order_id= orderIdList.get(i);
				//改签后的信息
				List<HashMap<String, String>> cpInfo = qunarRefundService.getRefundTicketcpInfo(order_id);
				for(int j=0;j<cpInfo.size();j++){
					String cpId = cpInfo.get(j).get("cp_id");
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
							
							param_Map.put("alter_train_no", alter_train_no);
							param_Map.put("alter_train_box", alter_train_box);
							param_Map.put("alter_seat_no", alter_seat_no);
							param_Map.put("alter_money", alter_money);
							param_Map.put("refund_12306_money", refund_12306_money);
							
							param_Map.put("cp_id", cp_id);
							Set<String>kset=QunarBookVo.getSeatType().keySet(); 
							for(String ks : kset){     
								if(alter_seat_type.equals(QunarBookVo.getSeatType().get(ks))){          
									param_Map.put("alter_seat_type", ks);  
									System.out.println(ks);
									break;
								}else{
									param_Map.put("alter_seat_type", ""); 
								} 
							}
							qunarRefundService.updateAlertRefund(param_Map);
						}
						
					}
				}
			}
		}else{
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
						Set<String>kset=QunarBookVo.getSeatType().keySet(); 
						for(String ks : kset){     
							if(alter_seat_type.equals(QunarBookVo.getSeatType().get(ks))){          
								param_Map.put("alter_seat_type", ks);  
								System.out.println(ks);
								break;
							}else{
								param_Map.put("alter_seat_type", ""); 
							} 
						}
						
						qunarRefundService.updateAlertRefund(param_Map);
					}
				}
			}
		}
		
		/* 处理退款数据 */
		String[] detail_refunds = detail_refund.split(",");

		for (int i = 0; i < detail_refunds.length; i++) {
			actual_refund_money += Double.parseDouble(detail_refunds[i].trim());
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
		paramMap.put("alter_tickets_money", alter_tickets_money);
		paramMap.put("refund_money", refundmoney);
		paramMap.put("our_remark", our_remark);
		paramMap.put("actual_refund_money", actual_refund_money);
		paramMap.put("refund_12306_seq", refund_12306_seq);
		paramMap.put("detail_refund", detail_refund);
		paramMap.put("detail_alter_tickets", detail_alter_tickets);
		paramMap.put("change_ticket_info", change_ticket_info);
		
		qunarRefundService.updateRefund(paramMap);
		HashMap<String, String> orderInfo = qunarRefundService
				.getRefundTicketInfo(orderid);// 查询订单信息
		System.out.println(orderInfo);
		String content = "";
		if(refund_status.equals("07")){
			content = user + "点击了人工退票【同意退款】";
		}else if(refund_status.equals("03")){
			content = user + "点击了人工改签【同意退款】";
		}else if(refund_status.equals("33")){
			content = user + "点击了审核退款【退款成功】";
		}else if(refund_status.equals("44")){
			content = user + "点击了搁置订单【退款成功】";
		}
		pMap.put("order_id", orderid);
		pMap.put("order_time", orderInfo.get("create_time"));
		pMap.put("content", content);
		pMap.put("user", user);
		qunarRefundService.insertLog(pMap);
		//客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", user);
		optMap.put("channel", "qunar");
		optMap.put("all", "refund");
		tj_OpterService.operate(optMap);
		String[] arr1 = statusList.split(",");
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
		for(int i=0;i<arr1.length;i++){
			str1 += "&refund_status="+arr1[i];
		}
		}
		String[] arr2 = notifyList.split(",");
		String str2 = "";
		if("".equals(notifyList)||notifyList==null){
			str2 = "";
		}else{
		for(int i=0;i<arr2.length;i++){
			str2 += "&notify_status="+arr2[i];
		}
		}
		return "redirect:/qunarrefund/queryRefundTicketList.do?pageIndex="+pageIndex+"&order_source="+order_source+str1+str2;
	}

	@RequestMapping("/refuse.do")
	public String refuse(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();
		String order_id = this.getParam(request, "orderid");
		String refundseq = this.getParam(request, "refundseq");
		String our_remark = this.getParam(request, "our_remark");
		String refuse_reason = this.getParam(request, "refuse_reason");
		String statusList = this.getParam(request, "statusList");
		String notifyList = this.getParam(request, "notifyList");
		String pageIndex = this.getParam(request, "pageIndex");
		String  order_source = this.getParam(request, "order_source");
		List<String> orderIdList = qunarRefundService.queryLianchengOrder_id(order_id);//查看是否是联程票
		List<String> orderIdList2 = qunarRefundService.queryOrderCpId(order_id);//查看普通订单的cp_id
		if(orderIdList.size()>0){//为联程票
			for(int i=0;i<orderIdList.size();i++){
				String orderId= orderIdList.get(i);
				//改签后的信息
				List<HashMap<String, String>> cpInfo = qunarRefundService.getRefundTicketcpInfo(orderId);
				for(int j=0;j<cpInfo.size();j++){
					String cpId = cpInfo.get(j).get("cp_id");
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
							Set<String>kset=QunarBookVo.getSeatType().keySet(); 
							for(String ks : kset){     
								if(alter_seat_type.equals(QunarBookVo.getSeatType().get(ks))){          
									param_Map.put("alter_seat_type", ks);  
									System.out.println(ks);
									break;
								}else{
									param_Map.put("alter_seat_type", ""); 
								} 
							}
							qunarRefundService.updateAlertRefund(param_Map);
						}
					}
				}
			}
		}else{
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
						Set<String>kset=QunarBookVo.getSeatType().keySet(); 
						for(String ks : kset){     
							if(alter_seat_type.equals(QunarBookVo.getSeatType().get(ks))){          
								param_Map.put("alter_seat_type", ks);  
								System.out.println(ks);
								break;
							}else{
								param_Map.put("alter_seat_type", ""); 
							} 
						}
						
						qunarRefundService.updateAlertRefund(param_Map);
					}
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
		
		qunarRefundService.updateRefuse(paramMap);
		HashMap<String, String> orderInfo = qunarRefundService
				.getRefundTicketInfo(order_id);
		System.out.println(orderInfo);
		String content = user + "点击了拒绝退款" + "，退款原因为：【"
				+ QunarRefundVo.getRefuseReason().get(refuse_reason) + "】";
		pMap.put("order_id", order_id);
		pMap.put("content", content);
		pMap.put("order_time", orderInfo.get("create_time"));
		pMap.put("user", user);
		qunarRefundService.insertLog(pMap);
		String[] arr1 = statusList.split(",");
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
		for(int i=0;i<arr1.length;i++){
			str1 += "&refund_status="+arr1[i];
		}
		}
		String[] arr2 = notifyList.split(",");
		String str2 = "";
		if("".equals(notifyList)||notifyList==null){
			str2 = "";
		}else{
		for(int i=0;i<arr2.length;i++){
			str2 += "&notify_status="+arr2[i];
		}
		}
		return "redirect:/qunarrefund/queryRefundTicketList.do?pageIndex="+pageIndex+"&order_source="+order_source+str1+str2;
	}

	// 查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,
			HttpServletRequest request) {
		String order_id = this.getParam(request, "order_id");
		List<HashMap<String, String>> history = qunarRefundService
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
			String order_id, String refund_status, String refund_seq, String create_time) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();// 获取当前登录的人
		String result = null;
		if (StringUtil.isNotEmpty(order_id)) {
			Map<String, String> updateMap = new HashMap<String, String>();
			updateMap.put("refund_status", refund_status);
			updateMap.put("order_id", order_id);
			updateMap.put("refund_seq", refund_seq);
			updateMap.put("user", user);
			qunarRefundService.updateOrderstatusToRobotGai(updateMap);
			result = "yes";
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", order_id);
			if(refund_status.equals("01")){
				map.put("content", user+"点击了【机器改签】");
			}else if(refund_status.equals("05")){
				map.put("content", user+"点击了【机器退票】");
			}else if(refund_status.equals("44")){
				map.put("content", user+"点击了【搁置订单】");
			}
			map.put("user", user);
			qunarRefundService.insertLog(map);
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
	
	@RequestMapping("/notify.do")
	public String updateNotify_status(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获取当前登录人
		String statusList = this.getParam(request, "statusList");
		String notifyList = this.getParam(request, "notifyList");
		String pageIndex = this.getParam(request, "pageIndex");
		String  order_source = this.getParam(request, "order_source");
		String order_id = this.getParam(request, "order_id");
		String refund_seq = this.getParam(request, "refund_seq");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("refund_seq", refund_seq);
		paramMap.put("opt_ren", user);
		qunarRefundService.updateNotify_status(paramMap);
//		System.out.println("order_id="+order_id+"   refund_seq="+refund_seq);
		HashMap<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("order_id", order_id);
		String content = user + "点击了通知成功按钮";
		logMap.put("content" , content);
		logMap.put("user", user);
		qunarRefundService.insertLog(logMap);
		String[] arr1 = statusList.split(",");
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
		for(int i=0;i<arr1.length;i++){
			str1 += "&refund_status="+arr1[i];
		}
		}
		String[] arr2 = notifyList.split(",");
		String str2 = "";
		if("".equals(notifyList)||notifyList==null){
			str2 = "";
		}else{
		for(int i=0;i<arr2.length;i++){
			str2 += "&notify_status="+arr2[i];
		}
		}
		return "redirect:/qunarrefund/queryRefundTicketList.do?pageIndex="+pageIndex+"&order_source="+order_source+str1+str2;
	}
}
