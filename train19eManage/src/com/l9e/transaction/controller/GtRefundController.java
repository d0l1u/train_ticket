package com.l9e.transaction.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.l9e.transaction.service.GtRefundService;
import com.l9e.transaction.service.SystemSettingService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.ElongVo;
import com.l9e.transaction.vo.ExtRefundTicketVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatUtil;
@Controller
@RequestMapping("/gtRefund")
public class GtRefundController extends BaseController {

	private static final Logger logger = Logger.getLogger(GtRefundController.class);
	
	@Resource
	private GtRefundService gtRefundService;
	
	private String find_accountinfo;
	@Resource
	private SystemSettingService systemSettingService;
	@Resource
	private Tj_OpterService tj_OpterService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRefundPage.do")
	public String queryRefundPage(HttpServletRequest request,
			HttpServletResponse response){
		
		List<Map<String,String>> merchantList = gtRefundService.queryGtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("refund_types", ExtRefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", ExtRefundTicketVo.getRefund_Status());
		request.setAttribute("notifyStatus", ExtRefundTicketVo.getNotify_Status());
		return "redirect:/gtRefund/queryRefundTicketList.do?refund_status=00&refund_status=03&refund_status=07&refund_status=55";
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
	
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
		String begin_create_time = this.getParam(request, "begin_create_time");//开始时间
		String end_create_time = this.getParam(request, "end_create_time");//结束时间
		List<String>refund_TypeList = this.getParamToList(request, "refund_type");//退款类型
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> refund_status = new ArrayList<String>(refund_StatusList);
		List<String> notify_status = this.getParamToList(request,"notify_status");
		String opt_person = this.getParam(request, "opt_person");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		if(order_id.trim().length()<=0 && merchant_order_id.trim().length()<=0){
			request.setAttribute("refund_status", refund_status.toString());
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
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else if(merchant_order_id.trim().length()>0){
			paramMap.put("merchant_order_id", merchant_order_id);
		}else{
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("begin_create_time", begin_create_time);//开始时间
			paramMap.put("end_create_time", end_create_time);//结束时间
			paramMap.put("opt_person", opt_person);
			paramMap.put("refund_type", refund_TypeList);
			paramMap.put("refund_status", refund_status);
			paramMap.put("notify_status", notify_status);
		}
		/******************分页条件开始********************/
		int totalCount = gtRefundService.queryRefundTicketCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> refundTicketList = gtRefundService.queryRefundTicketList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("refundTicketList", refundTicketList);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else if(merchant_order_id.trim().length()>0){
			request.setAttribute("merchant_order_id", merchant_order_id);
		}else{
			request.setAttribute("refund_seq", refund_seq);
			request.setAttribute("refund_12306_seq", refund_12306_seq);
			request.setAttribute("begin_create_time", begin_create_time);
			request.setAttribute("end_create_time", end_create_time);
			request.setAttribute("opt_person", opt_person);
			request.setAttribute("notify_status", notify_status.toString());
			request.setAttribute("refund_typeStr", refund_TypeList.toString());
		}
		request.setAttribute("notifyStatus", ExtRefundTicketVo.getNotify_Status());
		request.setAttribute("refund_types", ExtRefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", ExtRefundTicketVo.getRefund_Status());
		request.setAttribute("refund_and_alert", systemSettingService.querySystemRefundAndAlert("refund_and_alert"));
		request.setAttribute("isShowList", 1);
		request.setAttribute("returnOptlog", RefundTicketVo.getReturnOptlog());
		return "gtRefund/gtRefundList";
	}
	
	/***********************获取账号*************************/
	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}
	
	@RequestMapping("/updateGtRefundNotifyNum.do")
	public String updateGtRefundNotifyNum(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		/******************更改条件********************/
		String notify_id = this.getParam(request, "notify_id");
		String order_id = this.getParam(request, "order_id");
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!"+statusList);
		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了重新通知，订单号为："+order_id+"!";
		logger.info(opt_person_log);
		/******************创建容器********************/
		Map<String,String>paramMap = new HashMap<String,String>(); 
		/****************将条件添加到容器******************/
		paramMap.put("notify_id", notify_id);
		paramMap.put("order_id", order_id);
		paramMap.put("opt_person", opt_person);
		/****************执行Service进行修改******************/
		gtRefundService.updateGtRefundNotifyNum(paramMap);
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/gtRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
		String pageIndex =this.getParam(request, "pageIndex");
		/******************查询条件********************/
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
	//	String refund_seq=this.getParam(request, "refund_seq");
		/*********************创建容器*********************/
		Map<String,String> id_Map = new HashMap<String,String>();
		id_Map.put("stream_id", stream_id);
		id_Map.put("order_id", order_id);
		id_Map.put("cp_id", cp_id);
	//	id_Map.put("refund_seq", refund_seq);
		/******************查询开始********************/
		List<Map<String,String>>ticketInfo=
			gtRefundService.queryRefundTicketInfo(id_Map);
		Map<String,String> refundTicketInfo   = ticketInfo.get(0);
		
		Map<String, String> orderInfo = gtRefundService.queryBookOrderInfo(order_id);
		List<Map<String, String>> bxList = gtRefundService.queryBookOrderInfoBx(order_id);
		List<Map<String, String>> cpList = gtRefundService.queryBookOrderInfoCp(order_id);
		
		
		List<Map<String, Object>> history ;
		if("2".equals(refundTicketInfo.get("refund_type"))||"3".equals(refundTicketInfo.get("refund_type"))
				||"4".equals(refundTicketInfo.get("refund_type"))||"5".equals(refundTicketInfo.get("refund_type"))){ //差额退款的日志
			history = gtRefundService.queryHistroyByOrderId(order_id);
		}else{
			history = gtRefundService.queryHistroyByCpId(cp_id);
		}
		String account = null;
		if(StringUtils.equals(refundTicketInfo.get("refund_status"), ExtRefundTicketVo.REFUND_STATUS_PREPARE)){
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
		request.setAttribute("pageIndex", pageIndex);
		request.setAttribute("bxList", bxList);
		request.setAttribute("cpList", cpList);
		request.setAttribute("history", history);
		request.setAttribute("stream_id", stream_id);
		request.setAttribute("cp_id", cp_id);
		request.setAttribute("order_id", order_id);
	//	request.setAttribute("refund_seq", refund_seq);
		request.setAttribute("account", account);
		request.setAttribute("refund_statuses", ExtRefundTicketVo.getRefund_Status());
		request.setAttribute("refund_types", ExtRefundTicketVo.getRefund_Types());
		request.setAttribute("order_statuses", BookVo.getBookStatus());
		request.setAttribute("ticket_types", ExtRefundTicketVo.getTicket_Types());
		request.setAttribute("ids_types", ExtRefundTicketVo.getCertificate_type());
		request.setAttribute("seat_types", ExtRefundTicketVo.getSeat_type());
		request.setAttribute("query_type", this.getParam(request, "query_type"));
		request.setAttribute("statusList", statusList);
		return "gtRefund/gtRefundInfo";
	}
	
	/**
	 * 退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateRefundTicket.do")
	public String updateRefundTicket(HttpServletRequest request,
			HttpServletResponse response){
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
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String change_ticket_info = this.getParam(request, "change_ticket_info");//改签明细
		String statusList = this.getParam(request, "statusList");
		String pageIndex =this.getParam(request, "pageIndex");
		String opt_person_log = opt_person+"点击了退款!车票ID："+cp_id+"实际退款金额为："+actual_refund_money+"计划退款时间为："+refund_limit+"天";
		logger.info(opt_person_log);
		
		if(StringUtils.isEmpty(refund_money)||StringUtils.isEmpty(actual_refund_money)||StringUtils.isEmpty(refund_limit)){
			logger.error("参数为空，退款失败");
			Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
			log_Map.put("cp_id", cp_id);
			log_Map.put("opt_person", "_"+opt_person);
			log_Map.put("opt_person_log", opt_person_log);
			log_Map.put("order_time", order_time);
			//gtRefundService.addErrorLogInfo(log_Map);
			logger.info("车票："+cp_id+"，操作人："+opt_person+"，内容："+opt_person_log+"，订单时间："+order_time);
			PrintWriter out;
			try {
				out = response.getWriter();
			
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
			builder.append("alert(\"退款参数为空，退款操作失败，请重试！\");");
			builder.append("window.location.href=\"");
			builder.append("/gtRefund/queryRefundPage.do\";</script>");
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
		refund_Map.put("refund_seq", refund_seq);
		refund_Map.put("opt_person", opt_person);
		refund_Map.put("refund_12306_seq", refund_12306_seq);
		refund_Map.put("change_ticket_info", change_ticket_info);
		/****************执行Service进行修改******************/
		if(StringUtils.isEmpty(stream_id)||!SwitchUtils.isNum(refund_money)
				||!SwitchUtils.isNum(actual_refund_money)){
			logger.info("stream_id为空，opt_person_log="+opt_person_log+
					"refund_money="+refund_money+"actual_refund_money="+actual_refund_money);
		}else{
			//gtRefundService.updateRefundTicket(log_Map,refund_Map);
			
		}
		gtRefundService.updateRefundNotify(refund_Map,log_Map);
		//客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", opt_person);
		optMap.put("channel", "gtgj");//ext--商户退款改为高铁退款
		optMap.put("all", "refund");
		tj_OpterService.operate(optMap);
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/gtRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
			gtRefundService.updateGezhiRefund(log_Map,refuse_Map);
		}
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/gtRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
		String refund_seq = this.getParam(request, "refund_seq");
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		String refund_total = null ;
		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了拒绝退款!车票ID:"+cp_id+"，退款原因为：【"+our_remark+"】";
		/******************逻辑处理********************/
		if(!StringUtils.isEmpty(this.getParam(request, "refund_total"))){
			Double refund_money = Double.parseDouble(this.getParam(request, "refund_money"));
			Double refund_total_Double = Double.parseDouble(this.getParam(request, "refund_total"));
			refund_total = (refund_total_Double-refund_money)+"";
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
		refuse_Map.put("refund_seq", refund_seq);
		order_Map.put("order_id", order_id);
		order_Map.put("refund_total", refund_total);
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}

		if(StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"未通过，stream_id："+stream_id);
		}else{
		/****************执行Service进行修改******************/
			gtRefundService.updateRefuseRefund(log_Map,refuse_Map,order_Map);
		}
	return "redirect:/gtRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了差额退款!";
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
			gtRefundService.updateDifferRefund(log_Map,differ_Map);
		}
		request.setAttribute("statusList", statusList);

		
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/gtRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
	}
	
	
	
	@RequestMapping("ToAddStationRefundTicket.do")
	public String toAddStationRefundTicket(HttpServletRequest request,HttpServletResponse response){
		List<Map<String,String>> merchantList = gtRefundService.queryGtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
		return "gtRefund/gtRefundAddStation";
	}
	
	/**
	 * 增加车站退票流程
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("refundStationTicketAdd.do")
	public String refundStationTicketAdd(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得操作人姓名opt_person
		/******************更改条件********************/
		String order_id = this.getParam(request, "order_id");						//订单号
		String cp_id = this.getParam(request, "cp_id");								//车票ID
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		//String merchant_order_id = this.getParam(request, "merchant_order_id");		//商户订单号
		String merchant_order_id = gtRefundService.queryMerchantOrderId(order_id);
		String refund_percent = this.getParam(request, "refund_percent");			//退款手续费
		String channel = this.getParam(request, "channel");							//合作商户ID
		String actual_refund_money = this.getParam(request, "actual_refund_money");	//实际退款金额
		String refund_money = this.getParam(request, "refund_money");				//商户退款金额
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");		//12306退款流水号
		String user_remark = this.getParam(request, "user_remark");					//退款原因
		
		String opt_person_log = opt_person+"点击了生成车站退票!orderId为："+order_id+",cpId为:"+cp_id+",金额为："+refund_money+",退款原因为："+user_remark;
		/******************创建容器********************/
		Map<String, Object> map_add = new HashMap<String, Object>();
		map_add.put("order_id", order_id);
		map_add.put("cp_id", cp_id);
		map_add.put("merchant_order_id", merchant_order_id);
		map_add.put("refund_percent", refund_percent);
		map_add.put("channel", channel);
		map_add.put("actual_refund_money", actual_refund_money);
		map_add.put("refund_money", refund_money);
		map_add.put("refund_12306_seq", refund_12306_seq);
		map_add.put("user_remark", user_remark);
		map_add.put("refund_type", "2");
		map_add.put("refund_seq", CreateIDUtil.createID("TK"));//自动生成以TK开头的退款流水号
		map_add.put("refund_money", refund_money);
		map_add.put("create_time", "now()");
		map_add.put("refund_status", "00");
		map_add.put("opt_person", opt_person);
		/****************将条件添加到容器******************/
		Map<String,String>log_Map = new HashMap<String,String>();       //日志容器
		log_Map.put("order_id", order_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		/****************执行操作******************/
		gtRefundService.queryRefundStationTicketAdd(log_Map,map_add);	//执行车站退票
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
			String str = "";
			for(int i=0;i<arr.length;i++){
					str += "&refund_status="+arr[i];
			}
		return "redirect:/gtRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;

	}
	
	
	//查看order_id是否存在
	@RequestMapping("/queryRefundTicketAdd.do")
	@ResponseBody
	public String queryRefundTicketOrderId(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");						
		String orderId = gtRefundService.queryRefundTicketOrderId(order_id);
		//String orderIdExists = gtRefundService.queryRefundTicketOrderIdExists(order_id);//查看该订单号是否已经生成差额退款
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
	
	//查看cp_id是否存在
	@RequestMapping("/queryRefundStationTicketCpId.do")
	@ResponseBody
	public String queryRefundStationTicketCpId(HttpServletRequest request ,HttpServletResponse response){
		String cp_id = this.getParam(request, "cp_id");			
		String order_id = this.getParam(request, "order_id");	
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("cp_id", cp_id);
		String cpId = gtRefundService.queryRefundStationTicketCpId(paramMap);//查看该订单号里面是否存在该车票id
		String cpIdIsRefund = gtRefundService.queryCpidIsRefund(paramMap);//产看该车票号是否已经生成退款
		String result = null;
		if(StringUtils.isEmpty(cpId)){
			result = "no";
		}else if(StringUtils.isNotEmpty(cpIdIsRefund)){
			result = "exist";
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
	
	
	//查询buy_money和ticket_pay_money,验证验证refund_money + buy_money <= ticket_pay_money
	@RequestMapping("/queryBuymoneyAndTicketpaymoney.do")
	@ResponseBody
	public String queryBuymoneyAndTicketpaymoney(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");	
		String refund_money = this.getParam(request, "refund_money");
		
		Map<String, String> map_buy = gtRefundService.queryBuymoneyAndTicketpaymoney(order_id);
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
		Map<String, String> map_buy = gtRefundService.queryRefundMoney(orderId,streamId);
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
		String cp_id = this.getParam(request, "cp_id");
		String refund_money = this.getParam(request, "refund_money");
		String refund_seq = this.getParam(request, "refund_seq");
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");

		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了全额退款!";
		/******************创建容器********************/
		Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
		Map<String,Object>outTicket_Defeated_Map = new HashMap<String,Object>();//执行差额退款容器
		/****************将条件添加到容器******************/
		log_Map.put("order_id", order_id);
		log_Map.put("cp_id", cp_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		log_Map.put("order_time", order_time);
		outTicket_Defeated_Map.put("order_id", order_id);
		outTicket_Defeated_Map.put("stream_id", stream_id);
		outTicket_Defeated_Map.put("refund_money", refund_money);
		outTicket_Defeated_Map.put("opt_person", opt_person);
		outTicket_Defeated_Map.put("refund_seq", refund_seq);
		if(!SwitchUtils.isNum(refund_money)|| StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id为："+stream_id);
		}else{
			/****************执行Service进行修改******************/
			gtRefundService.updateOut_Ticket_Refund(log_Map,outTicket_Defeated_Map);
		}
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/gtRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");

		String refund_status = ExtRefundTicketVo.REFUND_STATUS_START;
		int notify_num = 0;
		Map<String,Object>update_RefreshNoticeMap = new HashMap<String,Object>();
		update_RefreshNoticeMap.put("refund_status", refund_status);
		update_RefreshNoticeMap.put("notify_num", notify_num);
		gtRefundService.updateRefreshNotice(update_RefreshNoticeMap);
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/gtRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
		String stream_id = this.getParam(request, "stream_id");
		String opt_person = loginUserVo.getReal_name();
		
		String key = "Lock_Refund_" + stream_id;
		String value = "Lock_Refund_"+stream_id+"&"+opt_person;
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
			e.printStackTrace();
		}
		
		return null;
	}
	
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		/*********************创建容器*********************/
		Map<String,String> id_Map = new HashMap<String,String>();
		id_Map.put("stream_id", stream_id);
		id_Map.put("order_id", order_id);
		id_Map.put("cp_id", cp_id);
		/******************查询开始********************/
		List<Map<String,String>>ticketInfo=
			gtRefundService.queryRefundTicketInfo(id_Map);
		Map<String,String> refundTicketInfo   = ticketInfo.get(0);
		List<Map<String, Object>> history ;
		if("2".equals(refundTicketInfo.get("refund_type"))||"3".equals(refundTicketInfo.get("refund_type"))
				||"4".equals(refundTicketInfo.get("refund_type"))||"5".equals(refundTicketInfo.get("refund_type"))){ //差额退款的日志
			history = gtRefundService.queryHistroyByOrderId(order_id);
		}else{
			history = gtRefundService.queryHistroyByCpId(cp_id);
		}
		//List<Map<String, Object>> history = gtRefundService.queryHistroyByOrderId(order_id);
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
			gtRefundService.insertLog(map);
			HashMap<String, Object> ordermap = new HashMap<String, Object>();
			ordermap.put("user", opt_person);
			ordermap.put("order_id", order_id);
			ordermap.put("refund_seq", refund_seq);
			gtRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
			gtRefundService.updateRefundOpt(ordermap);// 更新退款表中操作人信息
		} else if (isLock.indexOf(opt_person) != -1) {
			isLock = "";
		}
		HashMap<String, Object> ordermap = new HashMap<String, Object>();
		ordermap.put("user", opt_person);
		ordermap.put("order_id", order_id);
		gtRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
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
			gtRefundService.updateOrderstatusToRobotGai(updateMap);
			result = "yes";
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", order_id);
//			System.out.println("!!!!!!!!!!!!!!!"+refund_status);
			if(refund_status.equals("01")){
				map.put("content", user+"点击了【机器改签】");
			}else if(refund_status.equals("05")){
				map.put("content", user+"点击了【机器退票】");
			}
			map.put("user", user);
			gtRefundService.insertLog(map);
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
	

	//上传批量车站退票
	@RequestMapping("/uploadAddRefund.do")
	public String uploadAddRefund(HttpServletRequest request,
		HttpServletResponse response){
		return "gtRefund/gtRefundAdds";
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
//        String user_time= null;
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
                  		pMap.put("refund_status", "33");//33为退款完成
                  		int isExistNum = gtRefundService.queryCpidIsRefundNum(pMap);//从数据库中查询该车票是否已经退过票
                  		String orderStatus = gtRefundService.queryStatusByOrderId(pMap);//从数据库中查询该订单号是否是出票成功订单
                  		double buy_money=0.00;
                  		Map<String,String> money = gtRefundService.queryMoneyByCpId(pMap);
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
//                  			user_time = this.getCellValue(currentRow.getCell(4), true);
                  			channel = this.getCellValue(currentRow.getCell(5), true);
                         	String refund_seq=CreateIDUtil.createID("CZ");
                  			
                         	if(StringUtils.isNotEmpty(order_id) 
                         			&&StringUtils.isNotEmpty(cp_id)
                         			&&StringUtils.isNotEmpty(refund_money)
                         			&&StringUtils.isNotEmpty(channel)){
                       		Map<String,Object> paramMap = new HashMap<String,Object>();
                          	paramMap.put("order_id", order_id);
                          	String merchant_order_id = gtRefundService.queryMerchantIdByOrderId(pMap);//从数据库中查询该订单号的商户订单号
                        	paramMap.put("merchant_order_id", merchant_order_id);
                          	paramMap.put("cp_id", cp_id);
                          	paramMap.put("refund_money", refund_money);
                       		paramMap.put("refund_type", "2");//退款类型为车站退票
                       		if(isExistNum==0 && "44".equals(orderStatus) && buy_money>= Double.parseDouble(refund_money)){
                       			paramMap.put("refund_status", "11");
                       			paramMap.put("notify_status", "11");
                       			content=opt_person+" 批量车站退票,车票号："+cp_id+"【开始退票】";
                       		}else{
                       			paramMap.put("refund_status", "22");
                       			paramMap.put("notify_status", "33");
                       			if(isExistNum>0){
                           			content=opt_person+" 批量车站退票,车票号："+cp_id+" 异常退款【已拒绝】:已退款";
                           		}else if (! "44".equals(orderStatus) ){
                           			content=opt_person+" 批量车站退票,车票号："+cp_id+" 异常退款【已拒绝】:不是出票成功订单";
                           		}else if (buy_money< Double.parseDouble(refund_money)){
                           			content=opt_person+" 批量车站退票,车票号："+cp_id+" 异常退款【已拒绝】:退款金额"+refund_money+"大于票价"+buy_money;
                           		}else{
                           			content=opt_person+" 批量车站退票,车票号："+cp_id+" 异常退款【已拒绝】";
                           		}
                       		}
                       		paramMap.put("refund_seq", refund_seq);
                       		paramMap.put("actual_refund_money", detail_refund);
                       		paramMap.put("create_time","now()");
                       		paramMap.put("our_remark", "用户车站退票");
                       		paramMap.put("opt_person", opt_person);
                       		paramMap.put("channel", "301030");
                       		paramMap.put("notify_url", "https://jt.rsscc.com/trainwap/platform/tasks/refundCallblack419e.action");
//                       	paramMap.put("user_time", user_time);
                       		
                       		Map<String,String> logMap = new HashMap<String,String>();
                       		logMap.put("order_id", order_id);
                       		logMap.put("opt_person_log", content);
                       		logMap.put("opt_person", opt_person);
                       		try {
                       			gtRefundService.queryRefundStationTicketAdd(logMap,paramMap);
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
       	return "redirect:/gtRefund/queryRefundPage.do";
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
    
    
	@RequestMapping("toAddRefundPage.do")
	public String toAddRefundPage(HttpServletRequest request,HttpServletResponse response){
		
		String order_id = this.getParam(request, "order_id");	
		String cpid= this.getParam(request, "cpid");
		String refund_type = this.getParam(request, "refund_type");
		if(!order_id.isEmpty() && !"3".equals(refund_type)){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("order_id", order_id);
			List<Map<String, String>> cpidList=gtRefundService.queryTicketCpId(paramMap);
			request.setAttribute("cpidList", cpidList);
		}else if(!order_id.isEmpty() && "3".equals(refund_type)){
			//生成 线下改签退票
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("order_id", order_id);
			List<Map<String, String>> cpidList=gtRefundService.queryChangeRefundTicket(paramMap);
			request.setAttribute("cpidList", cpidList);
		}
		request.setAttribute("refund_type", refund_type);
		logger.info("高铁改签,refund_type:"+refund_type);
		request.setAttribute("cpid", cpid);
		request.setAttribute("order_id", order_id);
		return "gtRefund/gtRefundAdd";
	}

	@RequestMapping("refundTicketAdd.do")
	public String refundTicketAdd(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得操作人姓名opt_person
		/******************更改条件********************/
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		String order_id = this.getParam(request, "order_id");						//订单号
		String cp_id = this.getParam(request, "cpid");								//车票ID
		String refund_money = this.getParam(request, "refund_money");	//退款金额
		String actual_refund_money = this.getParam(request, "actual_refund_money");	//12306退款金额
		String user_remark = this.getParam(request, "our_remark");					//退款原因
		String refund_type= this.getParam(request, "refund_type");
		if("".equals(user_remark) || StringUtils.isEmpty(user_remark)){
			user_remark = "线下退款";
		}
		
		Map<String,Object> pMap = new HashMap<String,Object>();
		pMap.put("order_id", order_id);
		pMap.put("cp_id", cp_id);
		pMap.put("refund_status", "33");//33为退款完成
		String orderStatus = gtRefundService.queryStatusByOrderId(pMap);//从数据库中查询该订单号是否是出票成功订单
		double buy_money=0.00;
		if(refund_type!=null && refund_type.equals("2")){
			Map<String,String> money = gtRefundService.queryMoneyByCpId(pMap);
	  		if(MapUtils.isNotEmpty(money)){
	  			if(money.get("buy_money")!=null){
	  			buy_money = Double.parseDouble(String.valueOf(money.get("buy_money")));
	  			}else if(money.get("pay_money")!=null){
	  			buy_money = Double.parseDouble(String.valueOf(money.get("pay_money")));
	  			}
	  		}
		}else if(refund_type!=null && refund_type.equals("3")){
			String money = gtRefundService.queryChangeCpPayMoney(pMap);
  			buy_money = Double.parseDouble(money);

		}
  		
  		String content ="";
		/******************创建容器********************/
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if( "44".equals(orderStatus) && buy_money>= Double.parseDouble(refund_money)){
			paramMap.put("refund_status", "11");
			paramMap.put("notify_status", "11");
			content=opt_person+" 线下退票,车票号："+cp_id+"【开始退票】";
		}else{
			paramMap.put("refund_status", "22");
			paramMap.put("notify_status", "33");
			if (! "44".equals(orderStatus) ){
				content=opt_person+" 线下退票,车票号："+cp_id+" 异常退款【已拒绝】:不是出票成功订单";
			}else if (buy_money< Double.parseDouble(refund_money)){
				content=opt_person+" 线下退票,车票号："+cp_id+" 异常退款【已拒绝】:退款金额"+refund_money+"大于票价"+buy_money;
			}else{
				content=opt_person+" 线下退票,车票号："+cp_id+" 异常退款【已拒绝】";
			}
		}
      	paramMap.put("order_id", order_id);
      	String merchant_order_id = gtRefundService.queryMerchantIdByOrderId(paramMap);//从数据库中查询该订单号的商户订单号
    	paramMap.put("merchant_order_id", merchant_order_id);
      	paramMap.put("cp_id", cp_id);
      	paramMap.put("refund_money", refund_money);
   		paramMap.put("refund_type", "2");//退款类型为车站退票
   		paramMap.put("refund_seq", CreateIDUtil.createID("XX"));
   		paramMap.put("actual_refund_money", actual_refund_money);
   		paramMap.put("create_time","now()");
   		paramMap.put("our_remark", user_remark);
   		paramMap.put("opt_person", opt_person);
   		paramMap.put("channel", "301030");
   		paramMap.put("notify_url", "https://jt.rsscc.com/trainwap/platform/tasks/refundCallblack419e.action");
   		
   		Map<String,String> logMap = new HashMap<String,String>();
   		logMap.put("order_id", order_id);
   		logMap.put("opt_person_log", content);
   		logMap.put("opt_person", opt_person);
   		gtRefundService.queryRefundStationTicketAdd(logMap,paramMap);
		
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/gtRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
	}
	
	//限制金额不大于票面金额减去已退款金额
	@RequestMapping("/checkRefundMoney.do")
	@ResponseBody
	public String checkRefundMoney(HttpServletRequest request ,HttpServletResponse response){
		String orderId = this.getParam(request, "orderId");	
		String refundMoney = this.getParam(request, "refundMoney");
//		String streamId = this.getParam(request, "streamId");
		String cpId=this.getParam(request, "cpId");	
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", orderId);
		paramMap.put("cp_id", cpId);
		Map<String, String> map_buy = gtRefundService.checkRefundMoney(paramMap);
		String flag = null;
//		System.out.println(map_buy.get("pay_money")+"~~~~~~~~~~~"+map_buy.get("sumRefundMoney"));
		String pay_money_sj=map_buy.get("pay_money");
		if(pay_money_sj==null ||"0".equals(pay_money_sj) ||"0.00".equals(pay_money_sj) ||"0.0".equals(pay_money_sj) )pay_money_sj=map_buy.get("buy_money");
//		System.out.println(map_buy.get("pay_money")+"^%%%%%%%%"+map_buy.get("buy_money")+pay_money_sj);
		if(map_buy!=null && (!pay_money_sj.isEmpty()) && (map_buy.get("sumRefundMoney")!=null)){
			Double pay_money = Double.parseDouble(pay_money_sj);
			Double sumRefundMoney = Double.parseDouble(map_buy.get("sumRefundMoney"));
			Double refund_money = 0.0;
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
}
