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
import net.sf.json.JSONObject;

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
import com.l9e.transaction.service.BookService;
import com.l9e.transaction.service.RefundTicketService;
import com.l9e.transaction.service.SystemSettingService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.QunarBookVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatUtil;
/**
 * 退票管理
 * @author liht
 *
 */
@Controller
@RequestMapping("/refundTicket")
public class RefundTicketController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(RefundTicketController.class);
	
	@Resource
	private RefundTicketService refundTicketService;
	@Resource
	private BookService BookService;
	@Resource
	private SystemSettingService systemSettingService;
	@Resource
	private Tj_OpterService tj_OpterService;
	
	private String find_accountinfo;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRefundPage.do")
	public String queryRefundPage(HttpServletRequest request,
			HttpServletResponse response){
		String selectAllrefund_type = this.getParam(request, "selectAllrefund_type");//全选
		String selectAllrefund_statuses = this.getParam(request, "selectAllrefund_statuses");//全选
		request.setAttribute("orderSource", BookVo.getOrderSource());
		request.setAttribute("refund_types", RefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", RefundTicketVo.getRefund_Status());
		request.setAttribute("selectAllrefund_type", selectAllrefund_type);
		request.setAttribute("selectAllrefund_statuses", selectAllrefund_statuses);
		request.setAttribute("refund_statuses", RefundTicketVo.getRefundStatus());
		return "redirect:/refundTicket/queryRefundTicketList.do?refund_status=00&refund_status=03&refund_status=07&refund_status=09";
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRefundTicketList.do")
	public String queryRefundTicket(HttpServletRequest request,
			HttpServletResponse response){
		
		//获得系统当前时间
		//String now = DateUtil.nowDate();
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String eop_order_id = this.getParam(request, "eop_order_id");//eop订单id
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
		String begin_create_time = this.getParam(request, "begin_create_time");//开始时间
		String end_create_time = this.getParam(request, "end_create_time");//结束时间
		List<String>refund_TypeList = this.getParamToList(request, "refund_type");//退款类型
		List<String> order_sourceList = this.getParamToList(request, "order_source");
		String opt_person = this.getParam(request, "opt_person");
		String selectAllrefund_type = this.getParam(request, "selectAllrefund_type");//全选
		String selectAllrefund_statuses = this.getParam(request, "selectAllrefund_statuses");//全选
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> refund_status = new ArrayList<String>(refund_StatusList);
		if(order_id.trim().length()<=0){
			request.setAttribute("refund_statusStr", refund_StatusList.toString());
		}
		if(refund_status.contains("012")){
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
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("eop_order_id", eop_order_id);
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("begin_create_time", begin_create_time);//开始时间
			paramMap.put("end_create_time", end_create_time);//结束时间
			paramMap.put("opt_person", opt_person);
			paramMap.put("refund_type", refund_TypeList);
			paramMap.put("refund_status", refund_status);
			paramMap.put("order_source", order_sourceList);
		}
		/******************分页条件开始********************/
		int totalCount = refundTicketService.queryRefundTicketCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> refundTicketList = 
			refundTicketService.queryRefundTicketList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("refundTicketList", refundTicketList);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("eop_order_id", eop_order_id);
			request.setAttribute("refund_seq", refund_seq);
			request.setAttribute("refund_12306_seq", refund_12306_seq);
			request.setAttribute("begin_create_time", begin_create_time);
			request.setAttribute("end_create_time", end_create_time);
			request.setAttribute("opt_person", opt_person);
			request.setAttribute("refund_typeStr", refund_TypeList.toString());
			request.setAttribute("orderSourceStr", order_sourceList.toString());
			request.setAttribute("selectAllrefund_type", selectAllrefund_type);
			request.setAttribute("selectAllrefund_statuses", selectAllrefund_statuses);
		}
		request.setAttribute("refund_types", RefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", RefundTicketVo.getRefundStatus());
		request.setAttribute("refundStatuses", RefundTicketVo.getRefund_Status());
		request.setAttribute("orderSource", BookVo.getOrderSource());
		request.setAttribute("returnOptlog", RefundTicketVo.getReturnOptlog());
		request.setAttribute("isShowList", 1);
		request.setAttribute("refund_and_alert", systemSettingService.querySystemRefundAndAlert("refund_and_alert"));
		return "refundTicket/refundTicketList";
	}
	/***********************获取账号*************************/
	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}
	
	@RequestMapping("/queryNoReply.do")
	public void queryNoReply(HttpServletRequest request,HttpServletResponse response){
		String no_reply = null;
		try {
			no_reply = Integer.toString(refundTicketService.queryNoReplyCount());
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("no_reply", no_reply);
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(no_reply);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRefundTicketInfo.do")
	public String queryRefundTicketInfo(HttpServletRequest request,
			HttpServletResponse response){
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String typeList = this.getParam(request, "typeList");//得到选中的退款方式
		String pageIndex =this.getParam(request, "pageIndex");
		/******************查询条件********************/
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String refund_type = this.getParam(request, "refund_type");
		/*********************创建容器*********************/
		Map<String,String> id_Map = new HashMap<String,String>();
		id_Map.put("stream_id", stream_id);
		id_Map.put("order_id", order_id);
		id_Map.put("cp_id", cp_id);
		/******************查询开始********************/
		List<Map<String,String>>ticketInfo = refundTicketService.queryRefundTicketInfo(id_Map); //查询明细
		Map<String,String> refundTicketInfo = ticketInfo.get(0);
		Map<String, String> orderInfo = BookService.queryBookOrderInfo(order_id);//查询预订订单信息
		List<Map<String, String>> bxList = BookService.queryBookOrderInfoBx(order_id);//查询预订订单保险
		List<Map<String, String>> cpList = BookService.queryBookOrderInfoCp(order_id);//查询预订订单车票
		List<Map<String, Object>> history ;
		if("2".equals(refund_type)||"3".equals(refund_type)||"6".equals(refund_type)||"7".equals(refund_type)||"8".equals(refund_type)){ //差额/出票失败退款的日志
			history = refundTicketService.queryHistroyByOrderId(order_id);  //查询差额退款日志
		}else{
			history = refundTicketService.queryHistroyByCpId(cp_id); //查询日志信息
		}
		String account = null;//refund_status为准备退款00，人工改签03，人工退款07，审核退款09，搁置订单99
		if(StringUtils.equals(refundTicketInfo.get("refund_status"), "00") || StringUtils.equals(refundTicketInfo.get("refund_status"), "03")
				|| StringUtils.equals(refundTicketInfo.get("refund_status"), "07") || StringUtils.equals(refundTicketInfo.get("refund_status"), "09")
				|| StringUtils.equals(refundTicketInfo.get("refund_status"), "99")
				){
			Map<String, String> paramMap  = new HashMap<String, String>();
			paramMap.put("orderid", order_id);
			String params=null;
			try {
				params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				account = HttpUtil.sendByPost(find_accountinfo, params, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/******************request绑定********************/
		request.setAttribute("refundTicketInfo", refundTicketInfo);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("bxList", bxList);
		request.setAttribute("cpList", cpList);
		request.setAttribute("history", history);
		request.setAttribute("stream_id", stream_id);
		
		request.setAttribute("cp_id", cp_id);
		request.setAttribute("order_id", order_id);
		request.setAttribute("account", account);
		request.setAttribute("refund_statuses", RefundTicketVo.getRefund_Status());
		request.setAttribute("refund_types", RefundTicketVo.getRefund_Types());
		request.setAttribute("order_statuses", BookVo.getBookStatus());
		request.setAttribute("ticket_types", RefundTicketVo.getTicket_Types());
		request.setAttribute("ids_types", RefundTicketVo.getCertificate_type());
		request.setAttribute("seat_types", RefundTicketVo.getSeat_type());
		request.setAttribute("seatType", QunarBookVo.getSeatType());
		request.setAttribute("query_type", this.getParam(request, "query_type"));
		request.setAttribute("our_remark", RefundTicketVo.getOur_remark());
		request.setAttribute("refund_type", refund_type);
		request.setAttribute("statusList", statusList);
		request.setAttribute("typeList", typeList);
		request.setAttribute("pageIndex", pageIndex);
		return "refundTicket/refundTicketInfo";
	}
	
	/**
	 * 退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateRefundTicket.do")
	public String updateRefundTicket(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		
		/******************更改条件********************/
		String opt_person = loginUserVo.getReal_name();//当前登录人
		String order_time = this.getParam(request, "create_time");
		String stream_id=this.getParam(request, "stream_id");//主键id
		String cp_id = this.getParam(request, "cp_id");//车票id
		String order_id = this.getParam(request, "order_id");//订单id
		String refund_money = this.getParam(request, "refund_money");//退款金额
		String actual_refund_money = this.getParam(request, "actual_refund_money");//12306退款金额
		String alter_tickets_money = this.getParam(request, "alter_tickets_money");//改签差价
		String refund_limit = this.getParam(request, "refund_limit");//计划退款时间
		String our_remark = this.getParam(request, "our_remark");//出票方备注
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//退款流水号
		String change_ticket_info = this.getParam(request, "change_ticket_info");
		String statusList = this.getParam(request, "statusList");
		String typeList = this.getParam(request, "typeList");
		String pageIndex =this.getParam(request, "pageIndex");
		//修改改签信息
		//List<String> orderIdCpList = refundTicketService.queryOrderCpId(order_id);//查看普通订单的cp_id
		String cp_id01 = this.getParam(request, "cp_id_"+cp_id);//改签车票
		if(cp_id.equals(cp_id01)){
			String alter_train_no = this.getParam(request, "alter_train_no_"+cp_id01);//改签车次
			String alter_train_box = this.getParam(request, "alter_train_box_"+cp_id01);//改签车厢
			String alter_seat_no = this.getParam(request, "alter_seat_no_"+cp_id01);//改签座位
			String alter_seat_type = this.getParam(request, "alter_seat_type_"+cp_id01);//改签坐席
			String alter_money = this.getParam(request, "alter_money_"+cp_id01);//改签差额
			String refund_12306_money = this.getParam(request, "refund_12306_money_"+cp_id01);//12306退款
			
			if(alter_train_no.trim().length()>0 || alter_train_box.trim().length()>0 || alter_seat_no.trim().length()>0 
					|| alter_seat_type.trim().length()>0 || alter_money.trim().length()>0 || refund_12306_money.trim().length()>0){
				HashMap<String, Object> param_Map = new HashMap<String, Object>();
				param_Map.put("cp_id", cp_id01);
				param_Map.put("alter_train_no", alter_train_no);
				param_Map.put("alter_train_box", alter_train_box);
				param_Map.put("alter_seat_no", alter_seat_no);
				param_Map.put("alter_money", alter_money);
				param_Map.put("refund_12306_money", refund_12306_money);
				Set<String>kset=QunarBookVo.getSeatType().keySet(); 
				for(String ks : kset){     
					if(alter_seat_type.equals(QunarBookVo.getSeatType().get(ks))){          
						param_Map.put("alter_seat_type", ks);  
						System.out.println("改签的坐席是："+ks);
						break;
					}else{
						param_Map.put("alter_seat_type", ""); 
					} 
				}
				refundTicketService.updateAlertRefund(param_Map);
			}
		}
		
		String opt_person_log = opt_person+"点击了【退款】!车票ID："+cp_id+"实际退款金额为："+actual_refund_money+"计划退款时间为："+refund_limit+"天";
		logger.info(opt_person_log);
		if(StringUtils.isEmpty(refund_money)||StringUtils.isEmpty(actual_refund_money)||StringUtils.isEmpty(refund_limit)){
			logger.error("参数为空，退款失败");
			Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
			log_Map.put("cp_id", cp_id);
			log_Map.put("opt_person", "_"+opt_person);
			log_Map.put("opt_person_log", opt_person_log);
			log_Map.put("order_time", order_time);
			refundTicketService.addErrorLogInfo(log_Map);
			PrintWriter out;
			try {
				out = response.getWriter();
			
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
			builder.append("alert(\"退款参数为空，退款操作失败，请重试！\");");
			builder.append("window.location.href=\"");
			builder.append("/refundTicket/queryRefundPage.do\";</script>");
			out.print(builder.toString());
			out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//return "redirect:/refundTicket/queryRefundTicketList.do?refund_status=00";
			return null;
		}
		
		/******************创建容器********************/
		Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
		Map<String,Object>refund_Map = new HashMap<String,Object>();//执行退款容器
		/****************将条件添加到容器******************/
		log_Map.put("cp_id", cp_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		log_Map.put("order_time", order_time);
		refund_Map.put("stream_id", stream_id);
		refund_Map.put("order_id", order_id);
		refund_Map.put("cp_id", cp_id);
		refund_Map.put("refund_money", refund_money);
		refund_Map.put("actual_refund_money", actual_refund_money);
		refund_Map.put("alter_tickets_money", alter_tickets_money);
		refund_Map.put("refund_limit", refund_limit);
		refund_Map.put("our_remark", our_remark);
		refund_Map.put("refund_12306_seq", refund_12306_seq);
		refund_Map.put("opt_person", opt_person);
		refund_Map.put("change_ticket_info", change_ticket_info);

		/****************执行Service进行修改******************/
		if(StringUtils.isEmpty(stream_id)||!SwitchUtils.isNum(refund_money)
				||!SwitchUtils.isNum(actual_refund_money)){
			logger.info("stream_id为空，opt_person_log="+opt_person_log+
					"refund_money="+refund_money+"actual_refund_money="+actual_refund_money);
		}else{
			refundTicketService.updateRefundTicket(log_Map,refund_Map);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", opt_person);
			optMap.put("channel", "19e");
			optMap.put("all", "refund");
			tj_OpterService.operate(optMap);
		}
		String[] arr1 = statusList.split(",");
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
		for(int i=0;i<arr1.length;i++){
			str1 += "&refund_status="+arr1[i];
		}
		}
		String[] arr2 = typeList.split(",");
		String str2 = "";
		if("".equals(typeList)||typeList==null){
			str2 = "";
		}else{
		for(int i=0;i<arr2.length;i++){
			str2 += "&refund_type="+arr2[i];
		}
		}
		return "redirect:/refundTicket/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}
	
	/**
	 * 拒绝退款流程
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateRefuseRefund.do")
	public String updateRefuseRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		/******************更改条件********************/
		String order_time = this.getParam(request, "create_time");
		String stream_id = this.getParam(request, "stream_id");
		String cp_id = this.getParam(request, "cp_id");
		String order_id = this.getParam(request, "order_id");
		String our_remark = this.getParam(request, "our_remark");
		String statusList = this.getParam(request, "statusList");
		String typeList = this.getParam(request, "typeList");
		String pageIndex =this.getParam(request, "pageIndex");
		//List<String>our_remark = this.getParamToList(request, "our_remark");
		String refund_total = null ;
		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了【拒绝退款】!车票ID:"+cp_id+", 退款原因：【"+our_remark+"】";
		/******************逻辑处理********************/
		if(!StringUtils.isEmpty(this.getParam(request, "refund_total"))){
			Double refund_money = Double.parseDouble(this.getParam(request, "refund_money"));
			Double refund_total_Double = Double.parseDouble(this.getParam(request, "refund_total"));
			refund_total = (refund_total_Double-refund_money)+"";
		}
		//修改改签信息
		//List<String> orderIdCpList = refundTicketService.queryOrderCpId(order_id);//查看普通订单的cp_id
		String cp_id01 = this.getParam(request, "cp_id_"+cp_id);//改签车票
		if(cp_id.equals(cp_id01)){
			String alter_train_no = this.getParam(request, "alter_train_no_"+cp_id01);//改签车次
			String alter_train_box = this.getParam(request, "alter_train_box_"+cp_id01);//改签车厢
			String alter_seat_no = this.getParam(request, "alter_seat_no_"+cp_id01);//改签座位
			String alter_seat_type = this.getParam(request, "alter_seat_type_"+cp_id01);//改签坐席
			String alter_money = this.getParam(request, "alter_money_"+cp_id01);//改签差额
			String refund_12306_money = this.getParam(request, "refund_12306_money_"+cp_id01);//12306退款
			
			if(alter_train_no.trim().length()>0 || alter_train_box.trim().length()>0 || alter_seat_no.trim().length()>0 
					|| alter_seat_type.trim().length()>0 || alter_money.trim().length()>0 || refund_12306_money.trim().length()>0){
				HashMap<String, Object> param_Map = new HashMap<String, Object>();
				param_Map.put("cp_id", cp_id01);
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
				refundTicketService.updateAlertRefund(param_Map);
			}
		}
		/******************创建容器********************/
		Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
		Map<String,Object>refuse_Map = new HashMap<String,Object>();//执行拒绝退款容器
		Map<String,String>order_Map = new HashMap<String,String>();//执行主表的更改操作
		/****************将条件添加到容器******************/
		log_Map.put("order_id", order_id);
		log_Map.put("cp_id", cp_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		log_Map.put("order_time", order_time);
		refuse_Map.put("cp_id", cp_id);
		refuse_Map.put("stream_id", stream_id);
		refuse_Map.put("opt_person", opt_person);
		refuse_Map.put("order_id", order_id);
		refuse_Map.put("our_remark", our_remark);
		order_Map.put("order_id", order_id);
		order_Map.put("refund_total", refund_total);
		if(StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"未通过，stream_id："+stream_id);
		}else{
		/****************执行Service进行修改******************/
			refundTicketService.updateRefuseRefund(log_Map,refuse_Map,order_Map);
		}
		String[] arr1 = statusList.split(",");
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
		for(int i=0;i<arr1.length;i++){
			str1 += "&refund_status="+arr1[i];
		}
		}
		String[] arr2 = typeList.split(",");
		String str2 = "";
		if("".equals(typeList)||typeList==null){
			str2 = "";
		}else{
		for(int i=0;i<arr2.length;i++){
			str2 += "&refund_type="+arr2[i];
		}
		}
		return "redirect:/refundTicket/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}
	
	/**
	 * 差额退款流程
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateDifferRefund.do")
	public String updateDifferRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		/******************更改条件********************/
		String order_time = this.getParam(request, "create_time");
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String refund_money = this.getParam(request, "refund_money");
		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了【差额退款】!";
		String statusList = this.getParam(request, "statusList");
		String typeList = this.getParam(request, "typeList");
		String pageIndex =this.getParam(request, "pageIndex");
		/******************创建容器********************/
		Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
		Map<String,Object>differ_Map = new HashMap<String,Object>();//执行差额退款容器
		/****************将条件添加到容器******************/
		log_Map.put("order_id", order_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		log_Map.put("order_time", order_time);
		differ_Map.put("stream_id", stream_id);
		differ_Map.put("order_id", order_id);
		differ_Map.put("refund_money", refund_money);
		differ_Map.put("opt_person", opt_person);
		if(!SwitchUtils.isNum(refund_money)||StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id="+stream_id);
		}else{
			/****************执行Service进行修改******************/
			refundTicketService.updateDifferRefund(log_Map,differ_Map);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", opt_person);
			optMap.put("channel", "19e");
			optMap.put("type", "differ");
			optMap.put("all", "refund");
			tj_OpterService.operate(optMap);
		}
		String[] arr1 = statusList.split(",");
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
		for(int i=0;i<arr1.length;i++){
			str1 += "&refund_status="+arr1[i];
		}
		}
		String[] arr2 = typeList.split(",");
		String str2 = "";
		if("".equals(typeList)||typeList==null){
			str2 = "";
		}else{
		for(int i=0;i<arr2.length;i++){
			str2 += "&refund_type="+arr2[i];
		}
		}
		return "redirect:/refundTicket/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}
	
	/**
	 * 线下退款流程
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateXianXiaRefund.do")
	public String updateXianXiaRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		/******************更改条件********************/
		String order_time = this.getParam(request, "create_time");
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String refund_money = this.getParam(request, "refund_money");
		String actual_refund_money = this.getParam(request, "actual_refund_money");
		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了【线下退款】!";
		String statusList = this.getParam(request, "statusList");
		String typeList = this.getParam(request, "typeList");
		String pageIndex =this.getParam(request, "pageIndex");
		/******************创建容器********************/
		Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
		Map<String,Object>differ_Map = new HashMap<String,Object>();//执行差额退款容器
		/****************将条件添加到容器******************/
		log_Map.put("order_id", order_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		log_Map.put("order_time", order_time);
		differ_Map.put("stream_id", stream_id);
		differ_Map.put("order_id", order_id);
		differ_Map.put("refund_money", refund_money);
		differ_Map.put("actual_refund_money", actual_refund_money);
		differ_Map.put("opt_person", opt_person);
		if(!SwitchUtils.isNum(refund_money)||StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id="+stream_id);
		}else{
			/****************执行Service进行修改******************/
			refundTicketService.updateDifferRefund(log_Map,differ_Map);
		}
		String[] arr1 = statusList.split(",");
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
		for(int i=0;i<arr1.length;i++){
			str1 += "&refund_status="+arr1[i];
		}
		}
		String[] arr2 = typeList.split(",");
		String str2 = "";
		if("".equals(typeList)||typeList==null){
			str2 = "";
		}else{
		for(int i=0;i<arr2.length;i++){
			str2 += "&refund_type="+arr2[i];
		}
		}
		return "redirect:/refundTicket/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}
	/**
	 * 跳转增加差额/线下退款页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("AddrefundTicket.do")
	public String AddrefundTicket(HttpServletRequest request,HttpServletResponse response){
		String type = this.getParam(request, "type");		
		request.setAttribute("type", type);
		return "refundTicket/refundTicketAdd";
	}
	
	/**
	 * 增加差额退款流程/线下退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("refundTicketAdd.do")
	public String refundTicketAdd(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得操作人姓名opt_person
		/******************更改条件********************/
		String type = this.getParam(request, "type");	
		if("2".equals(type)){
		String order_id = this.getParam(request, "order_id");			//订单号
		String refund_money = this.getParam(request, "refund_money");	//差额退款金额
		String user_remark = this.getParam(request, "user_remark");		//退款原因
		String opt_person_log = opt_person+"点击了【生成差额退款】!orderId为："+order_id+",金额为："+refund_money+",退款原因为："+user_remark;
		/******************创建容器********************/
		Map<String,String>log_Map = new HashMap<String,String>();       //日志容器
		Map<String, Object> map_add = new HashMap<String, Object>();
		map_add.put("order_id", order_id);
		map_add.put("refund_money", refund_money);
		map_add.put("refund_type", "2");
		map_add.put("refund_seq", CreateIDUtil.createID("CE"));
		map_add.put("create_time", "now()");
		map_add.put("refund_status", "00");
		map_add.put("notify_num", "0");
		map_add.put("opt_person", opt_person);
		map_add.put("user_remark", user_remark);
		
		/****************将条件添加到容器******************/
		log_Map.put("order_id", order_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("refund_money", refund_money);
		log_Map.put("refund_type", "2");
		log_Map.put("refund_seq", CreateIDUtil.createID("CE"));
		log_Map.put("refund_status", "00");
		log_Map.put("notify_num", "0");
		log_Map.put("user_remark", user_remark);
		log_Map.put("opt_person_log", opt_person_log);
		/****************执行操作******************/
		logger.info(opt_person_log+"验证未通过"+"order_id="+order_id);
		refundTicketService.queryrefundTicketAdd(log_Map,map_add);	//执行差额退款容器
		}else{
			String order_id = this.getParam(request, "order_id");			//订单号
			String cp_id = this.getParam(request, "cp_id");					//车票号
			String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水号
			String refund_money = this.getParam(request, "refund_money");	//线下退款金额
			String actual_refund_money = this.getParam(request, "actual_refund_money");	//12306退款金额
			String user_remark = this.getParam(request, "user_remark");		//退款原因
			String opt_person_log = opt_person+"点击了【生成线下退款】!orderId为："+order_id+",金额为："+refund_money+",退款原因为："+user_remark;
			/******************创建容器********************/
			Map<String,String>log_Map = new HashMap<String,String>();       //日志容器
			Map<String, Object> map_add = new HashMap<String, Object>();
			map_add.put("order_id", order_id);
			map_add.put("cp_id", cp_id);
			map_add.put("refund_12306_seq", refund_12306_seq);
			map_add.put("refund_money", refund_money);
			map_add.put("actual_refund_money", actual_refund_money);
			map_add.put("refund_type", "8");
			map_add.put("refund_seq", CreateIDUtil.createID("CE"));
			map_add.put("create_time", "now()");
			map_add.put("refund_status", "00");
			map_add.put("notify_num", "0");
			map_add.put("opt_person", opt_person);
			map_add.put("user_remark", user_remark);
			
			/****************将条件添加到容器******************/
			log_Map.put("order_id", order_id);
			log_Map.put("opt_person", opt_person);
			log_Map.put("refund_money", refund_money);
			log_Map.put("refund_type", "2");
			log_Map.put("refund_seq", CreateIDUtil.createID("CE"));
			log_Map.put("refund_status", "00");
			log_Map.put("notify_num", "0");
			log_Map.put("user_remark", user_remark);
			log_Map.put("opt_person_log", opt_person_log);
			/****************执行操作******************/
			logger.info(opt_person_log+"修改hc_orderinfo不可申请退款");
			refundTicketService.queryrefundTicketAdd(log_Map,map_add);	//执行线下退款容器
			refundTicketService.updateOrderInfo_can_refundTo0(map_add);
		}
		return "redirect:/refundTicket/queryRefundTicketList.do";
	}
	//查看order_id是否存在
	@RequestMapping("/queryRefundTicketAdd.do")
	@ResponseBody
	public String queryRefundTicketOrderId(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");						
		String result = null;
		if(StringUtils.isEmpty(refundTicketService.queryRefundTicketOrderId(order_id))){
			result = "no";
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
	
	//查看cp_id是否存在
	@RequestMapping("/queryRefundTicketAddCp.do")
	@ResponseBody
	public String queryRefundTicketCpId(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");	
		String cp_id = this.getParam(request, "cp_id");	
		List<Map<String, String>> cpList = BookService.queryBookOrderInfoCp(order_id);//查询预订订单车票
		String str = "no";
		for(int i=0;i<cpList.size();i++){
			if((cp_id).equals(cpList.get(i).get("cp_id")))
				str = "yes";
		}
		
		String result = null;
		if(StringUtils.isEmpty(cpList.toString())){
			result = "no";
		}else{
			result = str;
			
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
	//查询buy_money和ticket_pay_money,验证验证refund_money + buy_money <= ticket_pay_money
	@RequestMapping("/queryBuymoneyAndTicketpaymoney.do")
	@ResponseBody
	public String queryBuymoneyAndTicketpaymoney(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");	
		String refund_money = this.getParam(request, "refund_money");
		
		Map<String, String> map_buy = refundTicketService.queryBuymoneyAndTicketpaymoney(order_id);
		String flag = null;
		if(map_buy!=null && (map_buy.get("pay_money")!=null) && (map_buy.get("sumRefundMoney")!=null)){
			Double pay_money = Double.parseDouble(map_buy.get("pay_money"));
			Double sumRefundMoney = Double.parseDouble(map_buy.get("sumRefundMoney"));
			if( sumRefundMoney+Double.parseDouble(refund_money) <= pay_money ){
				flag = "yes";
			}else{
				flag = "no";
			}
		}else{
			flag = "exception";
		}
			
		
		try {
//			response.getWriter().write(order_id);
//			response.getWriter().write(refund_money);
			response.getWriter().write(flag);
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
		Map<String, String> map_buy = refundTicketService.queryRefundMoney(orderId,streamId);
		String flag = null;
		if(map_buy!=null && (map_buy.get("pay_money")!=null) && (map_buy.get("sumRefundMoney")!=null)){
			Double pay_money = Double.parseDouble(map_buy.get("pay_money"));
			Double sumRefundMoney = Double.parseDouble(map_buy.get("sumRefundMoney"));
			Double refund_money = Double.parseDouble(map_buy.get("refund_money"));
		//	System.out.println("***************sumRefundMoney="+sumRefundMoney+"~~~~~~~~~~~~~~~~pay_money="+pay_money);
		//	System.out.println("############"+(pay_money-sumRefundMoney)+"~~~~~~~~~~refund_money="+refund_money);
			if( (sumRefundMoney-refund_money)+Double.parseDouble(refundMoney) <= pay_money ){
				flag = "yes";
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
	
	
	/**
	 * 出票失败退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateOut_Ticket_Refund.do")
	public String updateOut_Ticket_Refund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		/******************更改条件********************/
		String order_time = this.getParam(request, "create_time");
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String refund_money = this.getParam(request, "refund_money");
		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了【全额退款】!";
		String statusList = this.getParam(request, "statusList");
		String typeList = this.getParam(request, "typeList");
		String pageIndex =this.getParam(request, "pageIndex");
		/******************创建容器********************/
		Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
		Map<String,Object>outTicket_Defeated_Map = new HashMap<String,Object>();//执行差额退款容器
		/****************将条件添加到容器******************/
		log_Map.put("order_id", order_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		log_Map.put("order_time", order_time);
		outTicket_Defeated_Map.put("order_id", order_id);
		outTicket_Defeated_Map.put("stream_id", stream_id);
		outTicket_Defeated_Map.put("refund_money", refund_money);
		outTicket_Defeated_Map.put("opt_person", opt_person);
		if(!SwitchUtils.isNum(refund_money)|| StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id为："+stream_id);
		}else{
			/****************执行Service进行修改******************/
			refundTicketService.updateOut_Ticket_Refund(log_Map,outTicket_Defeated_Map);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", opt_person);
			optMap.put("channel", "19e");
			optMap.put("type", "failure");
			optMap.put("all", "refund");
			tj_OpterService.operate(optMap);
		}
		String[] arr1 = statusList.split(",");
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
		for(int i=0;i<arr1.length;i++){
			str1 += "&refund_status="+arr1[i];
		}
		}
		String[] arr2 = typeList.split(",");
		String str2 = "";
		if("".equals(typeList)||typeList==null){
			str2 = "";
		}else{
		for(int i=0;i<arr2.length;i++){
			str2 += "&refund_type="+arr2[i];
		}
		}
		return "redirect:/refundTicket/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}
	
	
		/**
		 * 取消预约退款
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/updateCancel_Book_Refund.do")
		public String updateCancel_Book_Refund(HttpServletRequest request,
				HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

			/******************更改条件********************/
			String order_time = this.getParam(request, "create_time");
			String stream_id = this.getParam(request, "stream_id");
			String order_id = this.getParam(request, "order_id");
			String refund_money = this.getParam(request, "refund_money");
			String opt_person = loginUserVo.getReal_name();
			String opt_person_log = opt_person+"在退款管理点击了【取消预约退款】!";
			String statusList = this.getParam(request, "statusList");
			String typeList = this.getParam(request, "typeList");
			String pageIndex =this.getParam(request, "pageIndex");
			/******************创建容器********************/
			Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
			Map<String,Object>outTicket_Defeated_Map = new HashMap<String,Object>();
			/****************将条件添加到容器******************/
			log_Map.put("order_id", order_id);
			log_Map.put("opt_person", opt_person);
			log_Map.put("opt_person_log", opt_person_log);
			log_Map.put("order_time", order_time);
			outTicket_Defeated_Map.put("order_id", order_id);
			outTicket_Defeated_Map.put("stream_id", stream_id);
			outTicket_Defeated_Map.put("refund_money", refund_money);
			outTicket_Defeated_Map.put("opt_person", opt_person);
			if(!SwitchUtils.isNum(refund_money)|| StringUtils.isEmpty(stream_id)){
				logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id为："+stream_id);
			}else{
				/****************执行Service进行修改******************/
				refundTicketService.updateOut_Ticket_Refund(log_Map,outTicket_Defeated_Map);
				//客服操作记录
				Map<String, Object> optMap = new HashMap<String, Object>();
				optMap.put("userName", opt_person);
				optMap.put("channel", "19e");
				optMap.put("all", "refund");
				tj_OpterService.operate(optMap);
			}
			String[] arr1 = statusList.split(",");
			String str1 = "";
			if("".equals(statusList)||statusList==null){
				str1 = "";
			}else{
			for(int i=0;i<arr1.length;i++){
				str1 += "&refund_status="+arr1[i];
			}
			}
			String[] arr2 = typeList.split(",");
			String str2 = "";
			if("".equals(typeList)||typeList==null){
				str2 = "";
			}else{
			for(int i=0;i<arr2.length;i++){
				str2 += "&refund_type="+arr2[i];
			}
			}
			return "redirect:/refundTicket/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
		}
	/**
	 * 刷新通知次数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateRefreshNotice.do")
	public String updateRefreshNotice(HttpServletRequest request,
			HttpServletResponse response){
		String refund_status = RefundTicketVo.REFUND_STATUS_START;
		int notify_num = 0;
		Map<String,Object>update_RefreshNoticeMap = new HashMap<String,Object>();
		update_RefreshNoticeMap.put("refund_status", refund_status);
		update_RefreshNoticeMap.put("notify_num", notify_num);
		refundTicketService.updateRefreshNotice(update_RefreshNoticeMap);
		return "redirect:/refundTicket/queryRefundTicketList.do?refund_status=00";
	}
	/**
	 * 重新通知
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("gotoNotifyAgain.do")
	public void updateInsuranceStatusSendAgain(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		/*********************查询条件************************/
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String stream_id = this.getParam(request, "stream_id");
		String refund_status = RefundTicketVo.REFUND_STATUS_START;
		int notify_num = 0;
		Map<String,Object> update_Map = new HashMap<String,Object>();
		update_Map.put("refund_status", refund_status);
		update_Map.put("notify_num", notify_num);
		update_Map.put("order_id", order_id);
		update_Map.put("cp_id", cp_id);
		update_Map.put("stream_id", stream_id);
		String opter =loginUserVo.getReal_name();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("order_id", order_id);
		map.put("cp_id", cp_id);
		map.put("opt_person_log", opter+"点击了重新通知!订单号："+order_id+"车票号："+cp_id);
		map.put("opt_person", opter);
		/*********************执行service************************/
		refundTicketService.addErrorLogInfo(map);
		HashMap<String, Object> ordermap = new HashMap<String, Object>();
		ordermap.put("user", opter);
		ordermap.put("order_id", order_id);
		ordermap.put("stream_id", stream_id);
		refundTicketService.updateRefundOpt(ordermap);
		refundTicketService.updateRefreshNoticeById(request,response, update_Map);

	}
	
	
	/**
	 * 退款锁
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRfundTicketIsLock.do")
	@ResponseBody
	public String queryRfundTicketIsLock(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String stream_id = this.getParam(request, "stream_id");
		String opt_person = loginUserVo.getReal_name();
		
		List<Map<String, String>> map2=refundTicketService.queryRefundTicketByStreamId(stream_id);
		if(order_id.isEmpty() || "".equals(order_id))order_id=map2.get(0).get("order_id");
		
		String key = "Lock_Refund_" + order_id;
		String value = "Lock_Refund_"+order_id+"&"+opt_person;
//		System.out.println("!!!!!!!!!!!!!!!!!!!");
		String isLock;
		isLock = (String) MemcachedUtil.getInstance().getAttribute(key); //读值
		if(StringUtils.isEmpty(isLock)){
			MemcachedUtil.getInstance().setAttribute(key, value, 5*60*1000); //写值
			isLock="";
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("order_id", order_id);
			map.put("cp_id", cp_id);
			map.put("opt_person_log", opt_person + "锁定了订单：" + order_id);
			map.put("opt_person", opt_person);
			refundTicketService.addErrorLogInfo(map);
			HashMap<String, Object> ordermap = new HashMap<String, Object>();
			ordermap.put("user", opt_person);
			ordermap.put("order_id", order_id);
			ordermap.put("stream_id", stream_id);
			refundTicketService.updateOrder(ordermap);// 更新订单表中操作人信息
			refundTicketService.updateRefundOpt(ordermap);// 更新退款表中操作人信息
		}else if(isLock.indexOf(opt_person) != -1){
			isLock = "";
		}
		HashMap<String, Object> ordermap = new HashMap<String, Object>();
		ordermap.put("user", opt_person);
		ordermap.put("order_id", order_id);
		refundTicketService.updateOrder(ordermap);// 更新订单表中操作人信息
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(isLock);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//搁置订单
	@RequestMapping("/updateGezhiRefund.do")
	public String updateGezhiRefund(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		/******************更改条件********************/
		String order_time = this.getParam(request, "create_time");
		String stream_id = this.getParam(request, "stream_id");
		String cp_id = this.getParam(request, "cp_id");
		String order_id = this.getParam(request, "order_id");
		String refund_status = this.getParam(request, "refund_status");
		String statusList = this.getParam(request, "statusList");
		String typeList = this.getParam(request, "typeList");
		String pageIndex =this.getParam(request, "pageIndex");
		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了【搁置订单】!车票ID:"+cp_id;
		/******************创建容器********************/
		Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
		Map<String,Object>refuse_Map = new HashMap<String,Object>();//执行搁置订单容器
		/****************将条件添加到容器******************/
		log_Map.put("order_id", order_id);
		log_Map.put("cp_id", cp_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		log_Map.put("order_time", order_time);
		refuse_Map.put("cp_id", cp_id);
		refuse_Map.put("stream_id", stream_id);
		refuse_Map.put("opt_person", opt_person);
		refuse_Map.put("order_id", order_id);
		refuse_Map.put("refund_status", refund_status);//退款状态99：搁置订单
		if(StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"未通过，stream_id："+stream_id);
		}else{
		/****************执行Service进行修改******************/
			refundTicketService.updateGezhiRefund(log_Map,refuse_Map);
		}
		String[] arr1 = statusList.split(",");
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
		for(int i=0;i<arr1.length;i++){
			str1 += "&refund_status="+arr1[i];
		}
		}
		String[] arr2 = typeList.split(",");
		String str2 = "";
		if("".equals(typeList)||typeList==null){
			str2 = "";
		}else{
		for(int i=0;i<arr2.length;i++){
			str2 += "&refund_type="+arr2[i];
		}
		}
		return "redirect:/refundTicket/queryRefundTicketList.do?pageIndex="+pageIndex+str1+str2;
	}
	
	//机器退票，机器改签
	@RequestMapping("/updateOrderstatusToRobotGai.do")
	@ResponseBody
	public void updateOrderstatusToRobotGai(HttpServletResponse response,HttpServletRequest request, 
			String order_id, String refund_status, String refund_seq, String create_time) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String result = "";
		if (StringUtil.isNotEmpty(order_id)) {
			String order_time = this.getParam(request, "create_time");
			String stream_id = this.getParam(request, "stream_id");
			String cp_id = this.getParam(request, "cp_id");
			String opt_person = loginUserVo.getReal_name();
			String opt_person_log = "";
			if(refund_status.endsWith("01")){
				opt_person_log = opt_person+"点击了【机器改签】!车票ID:"+cp_id;
			}else if(refund_status.endsWith("05")){
				opt_person_log = opt_person+"点击了【机器退票】!车票ID:"+cp_id;
			}
			/******************创建容器********************/
			Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
			Map<String,Object>refuse_Map = new HashMap<String,Object>();//执行搁置订单容器
			/****************将条件添加到容器******************/
			log_Map.put("order_id", order_id);
			log_Map.put("cp_id", cp_id);
			log_Map.put("opt_person", opt_person);
			log_Map.put("opt_person_log", opt_person_log);
			log_Map.put("order_time", order_time);
			refuse_Map.put("cp_id", cp_id);
			refuse_Map.put("stream_id", stream_id);
			refuse_Map.put("opt_person", opt_person);
			refuse_Map.put("order_id", order_id);
			refuse_Map.put("refund_status", refund_status);//退款状态99：搁置订单
			if(StringUtils.isEmpty(stream_id)){
				logger.info(opt_person_log+"未通过，stream_id："+stream_id);
			}else{
			/****************执行Service进行修改******************/
				refundTicketService.updateGezhiRefund(log_Map,refuse_Map);
				result = "yes";
			}
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
	
	//查询日志信息
	@RequestMapping("/queryRefundHistoryInfo.do")
	public void queryRefundHistoryInfo(HttpServletRequest request,
			HttpServletResponse response){
		
		/******************查询条件********************/
		String refund_type = this.getParam(request, "refund_type");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		/******************查询开始********************/
		List<Map<String, Object>> history ;
		if("2".equals(refund_type)||"3".equals(refund_type)||"6".equals(refund_type)||"7".equals(refund_type)){ //差额/出票失败退款的日志
			history = refundTicketService.queryHistroyByOrderId(order_id);  //查询差额退款日志
		}else{
			history = refundTicketService.queryHistroyByCpId(cp_id); //查询日志信息
		}
		//悬浮明细
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
	
	//批量出票失败退款处理
	@RequestMapping("/updateBatchToRobot.do")
	public String updateBatchToRobot(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得当前登录人姓名
		//获取url查询条件
		String pageIndex = this.getParam(request, "pageIndex");
		String jsonArr = "["+this.getParam(request, "jsonArr")+"]";
		JSONArray array = JSONArray.fromObject(jsonArr); //转换为json数组
		for(int i = 0; i < array.size(); i++){ 
			JSONObject jsonObject = array.getJSONObject(i); 
			String stream_id = (String) jsonObject.get("stream_id");
			String create_time = (String) jsonObject.get("create_time");
			String opt_person_log=opt_person+"点击了【批量出票失败退款】";
			List<Map<String, String>> map=refundTicketService.queryRefundTicketByStreamId(stream_id);
			String order_id=map.get(0).get("order_id");
			String refund_money=String.valueOf(map.get(0).get("refund_money"));
			if(StringUtil.isNotEmpty(stream_id)){
				
				/******************创建容器********************/
				Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
				Map<String,Object>outTicket_Defeated_Map = new HashMap<String,Object>();//执行差额退款容器
				/****************将条件添加到容器******************/
				log_Map.put("order_id", order_id);
				log_Map.put("opt_person", opt_person);
				log_Map.put("opt_person_log", opt_person_log);
				log_Map.put("order_time", create_time);
				outTicket_Defeated_Map.put("order_id", order_id);
				outTicket_Defeated_Map.put("stream_id", stream_id);
				outTicket_Defeated_Map.put("refund_money", refund_money);
				outTicket_Defeated_Map.put("opt_person", opt_person);
				if(!SwitchUtils.isNum(refund_money)|| StringUtils.isEmpty(stream_id)){
					logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id为："+stream_id);
				}else{
					/****************执行Service进行修改******************/
					refundTicketService.updateOut_Ticket_Refund(log_Map,outTicket_Defeated_Map);
				}
				
				logger.info("【批量出票失败退款】添加操作日志成功订单id为："+order_id+"，操作人为【"+opt_person+"】");
			}
		} 
		return "redirect:/refundTicket/queryRefundTicketList.do?refund_status=00&refund_status=03&refund_status=07&refund_status=09";
	}
	
	//批量差额退款处理
	@RequestMapping("/updateBatchToRobot2.do")
	public String updateBatchToRobot2(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得当前登录人姓名
		//获取url查询条件
		String pageIndex = this.getParam(request, "pageIndex");
		String jsonArr = "["+this.getParam(request, "jsonArr")+"]";
		JSONArray array = JSONArray.fromObject(jsonArr); //转换为json数组
		for(int i = 0; i < array.size(); i++){ 
			JSONObject jsonObject = array.getJSONObject(i); 
			String stream_id = (String) jsonObject.get("stream_id");
			String create_time = (String) jsonObject.get("create_time");
			String opt_person_log=opt_person+"点击了【批量差额退款】";
			List<Map<String, String>> map=refundTicketService.queryRefundTicketByStreamId(stream_id);
			String order_id=map.get(0).get("order_id");
			String refund_money=String.valueOf(map.get(0).get("refund_money"));
			if(StringUtil.isNotEmpty(stream_id)){
				
				/******************创建容器********************/
				Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
				Map<String,Object>differ_Map = new HashMap<String,Object>();//执行差额退款容器
				/****************将条件添加到容器******************/
				log_Map.put("order_id", order_id);
				log_Map.put("opt_person", opt_person);
				log_Map.put("opt_person_log", opt_person_log);
				log_Map.put("order_time", create_time);
				differ_Map.put("stream_id", stream_id);
				differ_Map.put("order_id", order_id);
				differ_Map.put("refund_money", refund_money);
				differ_Map.put("opt_person", opt_person);
				if(!SwitchUtils.isNum(refund_money)||StringUtils.isEmpty(stream_id)){
					logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id="+stream_id);
				}else{
					/****************执行Service进行修改******************/
					refundTicketService.updateDifferRefund(log_Map,differ_Map);
				}
				
				logger.info("【批量差额退款】添加操作日志成功订单id为："+order_id+"，操作人为【"+opt_person+"】");
			}
		} 
		return "redirect:/refundTicket/queryRefundTicketList.do?refund_status=00&refund_status=03&refund_status=07&refund_status=09";
	}

	//上传批量车站退票
	@RequestMapping("/uploadAddRefund.do")
	public String uploadAddRefund(HttpServletRequest request,
		HttpServletResponse response){
		return "refundTicket/refundTicketAdds";
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
    	Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String verify_time = df.format(theCa.getTime());
//		logger.info("verify_time="+verify_time);
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
                  		int isExistNum =refundTicketService.queryCpidIsRefundNum(pMap);//从数据库中查询该车票是否已经退过票
                  		String orderStatus =refundTicketService.queryStatusByOrderId(pMap);//从数据库中查询该订单号是否是出票成功订单
                  		double buy_money=0.00;
                  		Map<String,String> money = refundTicketService.queryMoneyByCpId(pMap);
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
                         	String refund_seq=CreateIDUtil.createID("TK");
                  			
                         	if(StringUtils.isNotEmpty(order_id) 
                         			&&StringUtils.isNotEmpty(cp_id)
                         			&&StringUtils.isNotEmpty(refund_money)
                         			&&StringUtils.isNotEmpty(channel)){
                       		Map<String,Object> paramMap = new HashMap<String,Object>();
                          	paramMap.put("order_id", order_id);
                          	paramMap.put("cp_id", cp_id);
                          	paramMap.put("refund_money", refund_money);
                       		paramMap.put("refund_type", "33");//退款类型为车站退票
                       		if(isExistNum==0 && "44".equals(orderStatus) && buy_money>= Double.parseDouble(refund_money)){
                       			paramMap.put("refund_status", "11");// 退款状态为车站退票--等待退票
                       			content=opt_person+" 批量车站退票,车票号："+cp_id+"【开始退票】";
                       		}else{
                       			paramMap.put("refund_status", "55"); //退款状态为拒绝退票
                       			if(isExistNum>0){
                           			content=opt_person+" 批量车站退票,车票号："+cp_id+" 异常退款【已拒绝】:已退款";
                           		}else if (!"44".equals(orderStatus) ){
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
                       		paramMap.put("verify_time",verify_time);
                       		paramMap.put("our_remark", "用户车站退票");
                       		paramMap.put("opt_person", opt_person);
                       		paramMap.put("refund_plan_time","now()");
                       		paramMap.put("notify_time","now()");
                       		paramMap.put("notify_num",0);
//                       		paramMap.put("channel", "19e");
//                       		paramMap.put("user_time", user_time); 
                    		
                       		HashMap<String, String> log_Map = new HashMap<String, String>();
                   			log_Map.put("order_id", order_id);
                   			log_Map.put("opt_person", opt_person);
                   			log_Map.put("opt_person_log", content);
                       		try { 
                       			/****************执行操作******************/ 
                       			refundTicketService.queryRefundStationTicketAdd(log_Map,paramMap);
        					} catch (Exception e) {
        						logger.error("插入车站退票【失败】订单号："+order_id+"车票号："+cp_id+"||e:"+e);
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
       	return "redirect:/refundTicket/queryRefundPage.do";
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
	
	
	
	
}
