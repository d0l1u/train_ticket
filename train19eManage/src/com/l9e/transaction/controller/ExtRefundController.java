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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.service.SystemSettingService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.ExtRefundTicketVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.JedisUtil.Hash;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatUtil;
@Controller
@RequestMapping("/extRefund")
public class ExtRefundController extends BaseController {

	private static final Logger logger = Logger.getLogger(ExtRefundController.class);
	
	@Resource
	private ExtRefundService extRefundService;
	
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
		
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("refund_types", ExtRefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", ExtRefundTicketVo.getRefund_Status());
		request.setAttribute("notifyStatus", ExtRefundTicketVo.getNotify_Status());
		return "redirect:/extRefund/queryRefundTicketList.do?refund_status=00&refund_status=03&refund_status=07&refund_status=55";
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
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
		//获得系统当前时间
		//String now = DateUtil.nowDate();
	
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
		String begin_create_time = this.getParam(request, "begin_create_time");//开始时间
		String end_create_time = this.getParam(request, "end_create_time");//结束时间
		List<String>refund_TypeList = this.getParamToList(request, "refund_type");//退款类型
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> refund_status = new ArrayList<String>(refund_StatusList);
		List<String> notify_status = this.getParamToList(request,"notify_status");
		String opt_person = this.getParam(request, "opt_person");
		List<String> merchant_idList = this.getParamToList(request, "merchant_id");
		List<String> merchant_idList2 = new ArrayList<String>(merchant_idList);
		if(merchant_idList2.contains("30101612")){
			merchant_idList2.add("301016");
			merchant_idList2.add("30101601");
			merchant_idList2.add("30101602");
		}
		String selectAllrefund_type = this.getParam(request, "selectAllrefund_type");//全选
		String selectAllrefund_status = this.getParam(request, "selectAllrefund_status");//全选
		String selectAllmerchant = this.getParam(request, "selectAllmerchant");//全选
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		if(order_id.trim().length()<=0){
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
		}else{
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("begin_create_time", begin_create_time);//开始时间
			paramMap.put("end_create_time", end_create_time);//结束时间
			paramMap.put("opt_person", opt_person);
			paramMap.put("refund_type", refund_TypeList);
			//	paramMap.put("refund_status", refund_StatusList);
			paramMap.put("refund_status", refund_status);
			paramMap.put("notify_status", notify_status);
			paramMap.put("merchant_id", merchant_idList2);
		}
		/******************分页条件开始********************/
		int totalCount = extRefundService.queryRefundTicketCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> refundTicketList = 
			extRefundService.queryRefundTicketList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("refundTicketList", refundTicketList);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("refund_seq", refund_seq);
			request.setAttribute("refund_12306_seq", refund_12306_seq);
			request.setAttribute("begin_create_time", begin_create_time);
			request.setAttribute("end_create_time", end_create_time);
			request.setAttribute("opt_person", opt_person);
			request.setAttribute("merchant_idList", merchant_idList2);
			request.setAttribute("notify_status", notify_status.toString());
			request.setAttribute("refund_typeStr", refund_TypeList.toString());
			request.setAttribute("selectAllrefund_type", selectAllrefund_type);
			request.setAttribute("selectAllrefund_status", selectAllrefund_status);
			request.setAttribute("selectAllmerchant", selectAllmerchant);
		}
		request.setAttribute("notifyStatus", ExtRefundTicketVo.getNotify_Status());
		request.setAttribute("refund_types", ExtRefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", ExtRefundTicketVo.getRefund_Status());
		request.setAttribute("refund_and_alert", systemSettingService.querySystemRefundAndAlert("refund_and_alert"));
		request.setAttribute("isShowList", 1);
		request.setAttribute("returnOptlog", RefundTicketVo.getReturnOptlog());
		return "extRefund/extRefundList";
	}
	
	/***********************获取账号*************************/
	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}
	
	@RequestMapping("/updateExtRefundNotifyNum.do")
	public String updateExtRefundNotifyNum(HttpServletRequest request,
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
		extRefundService.updateExtRefundNotifyNum(paramMap);
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
			extRefundService.queryRefundTicketInfo(id_Map);
		Map<String,String> refundTicketInfo   = ticketInfo.get(0);
		
		Map<String, String> orderInfo = extRefundService.queryBookOrderInfo(order_id);
		List<Map<String, String>> bxList = extRefundService.queryBookOrderInfoBx(order_id);
		List<Map<String, String>> cpList = extRefundService.queryBookOrderInfoCp(order_id);
		
		
		List<Map<String, Object>> history ;
		if("2".equals(refundTicketInfo.get("refund_type"))||"3".equals(refundTicketInfo.get("refund_type"))
				||"4".equals(refundTicketInfo.get("refund_type"))||"5".equals(refundTicketInfo.get("refund_type"))){ //差额退款的日志
			history = extRefundService.queryHistroyByOrderId(order_id);
		}else{
			history = extRefundService.queryHistroyByCpId(cp_id);
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
		return "extRefund/extRefundInfo";
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
			//extRefundService.addErrorLogInfo(log_Map);
			logger.info("车票："+cp_id+"，操作人："+opt_person+"，内容："+opt_person_log+"，订单时间："+order_time);
			PrintWriter out;
			try {
				out = response.getWriter();
			
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
			builder.append("alert(\"退款参数为空，退款操作失败，请重试！\");");
			builder.append("window.location.href=\"");
			builder.append("/extRefund/queryRefundPage.do\";</script>");
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
			//extRefundService.updateRefundTicket(log_Map,refund_Map);
			
		}
		extRefundService.updateRefundNotify(refund_Map,log_Map);
		//客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", opt_person);
		optMap.put("channel", "ext");
		optMap.put("all", "refund");
		tj_OpterService.operate(optMap);
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
			extRefundService.updateGezhiRefund(log_Map,refuse_Map);
		}
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
			extRefundService.updateRefuseRefund(log_Map,refuse_Map,order_Map);
		}
	return "redirect:/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
			extRefundService.updateDifferRefund(log_Map,differ_Map);
		}
		request.setAttribute("statusList", statusList);

		
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
	}
	
	@RequestMapping("AddrefundTicket.do")
	public String AddrefundTicket(HttpServletRequest request,HttpServletResponse response){
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
		return "extRefund/extRefundAdd";
	}
	
	/**
	 * 增加差额退款流程
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("refundTicketAdd.do")
	public String refundTicketAdd(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得操作人姓名opt_person
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
		/******************更改条件********************/
		String order_id = this.getParam(request, "order_id");			//订单号
		String refund_money = this.getParam(request, "refund_money");	//差额退款金额
		String user_remark = this.getParam(request, "user_remark");		//退款原因
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		String channel = this.getParam(request, "channel");//合作商户编号
		String opt_person_log = opt_person+"点击了生成差额退款!orderId为："+order_id+",金额为："+refund_money+",退款原因为："+user_remark;
		/******************创建容器********************/
		String refund_seq = CreateIDUtil.createID("CE");
		Map<String,String>log_Map = new HashMap<String,String>();       //日志容器
		Map<String, Object> map_add = new HashMap<String, Object>();
		map_add.put("order_id", order_id);
		map_add.put("refund_money", refund_money);
		map_add.put("refund_type", "4");
		map_add.put("refund_seq", refund_seq);
		map_add.put("create_time", "now()");
		map_add.put("refund_status", "00");
		map_add.put("notify_num", "0");
		map_add.put("opt_person", opt_person);
		map_add.put("user_remark", user_remark);
		map_add.put("channel", channel);
		/****************将条件添加到容器******************/
		log_Map.put("order_id", order_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("refund_money", refund_money);
		log_Map.put("refund_type", "4");
		log_Map.put("refund_seq", refund_seq);
		log_Map.put("refund_status", "00");
		log_Map.put("user_remark", user_remark);
		log_Map.put("opt_person_log", opt_person_log);
		/****************执行操作******************/
		logger.info(opt_person_log+"验证未通过"+"order_id="+order_id);
		extRefundService.queryrefundTicketAdd(log_Map,map_add);	//执行差额退款容器
		
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
	}
	
	@RequestMapping("ToAddStationRefundTicket.do")
	public String toAddStationRefundTicket(HttpServletRequest request,HttpServletResponse response){
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
		return "extRefund/extRefundAddStation";
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
		String merchant_order_id = extRefundService.queryMerchantOrderId(order_id);
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
		map_add.put("refund_type", "2"); //航信通过，EOP钱包退款，用户车站退票了，这边收到回款，主动推送退款通知。
		map_add.put("refund_seq", CreateIDUtil.createID("TK")+"AA"+cp_id.substring(cp_id.length()-2, cp_id.length()));//自动生成以TK开头的退款流水号
		map_add.put("merchant_refund_seq","");
		map_add.put("refund_money", refund_money);
		map_add.put("create_time", "now()");
		map_add.put("refund_status", "11"); //收到回款，同意退票
		map_add.put("opt_person", opt_person);
		
		//查询商户的退款回调地址,notify_url
		Map<String,String> queryMap =new HashMap<String,String>();
		queryMap.put("merchant_id", channel);
		String notify_url=extRefundService.findExtRefundNotifyUrl(queryMap);
		logger.info("notify_url:"+notify_url);
		
		if(!StringUtil.isNotEmpty(notify_url)){
			
			String[] arr = statusList.split(",");
			String str = "";
			for(int i=0;i<arr.length;i++){
					str += "&refund_status="+arr[i];
			}
			
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
			builder.append("alert(\"该商户没有主动退票的配置\");");
			builder.append("window.top.location.href=\"/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str+"\"");
			builder.append("</script>");
		    try {
				response.getWriter().write(builder.toString());
				response.getWriter().flush();
				response.getWriter().close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		   return null;
		}
		
		map_add.put("opt_person", opt_person);
		
		
		/****************将条件添加到容器******************/
		Map<String,String>log_Map = new HashMap<String,String>();       //日志容器
		log_Map.put("order_id", order_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		/****************执行操作******************/
			
		Map<String, String> notifyMap = new HashMap<String, String>();
		notifyMap.put("order_id", order_id);
		notifyMap.put("refund_seq", (String) map_add.get("refund_seq"));
		notifyMap.put("cp_id", cp_id);
		notifyMap.put("notify_url", notify_url);
		notifyMap.put("notify_status", "11");//准备通知：22，5min后发起通知
	
		extRefundService.queryRefundStationTicketAdd(log_Map,map_add,notifyMap);	//执行车站退票
		
		
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
			String str = "";
			for(int i=0;i<arr.length;i++){
					str += "&refund_status="+arr[i];
			}
		return "redirect:/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;

	}
	
	
	//查看order_id是否存在
	@RequestMapping("/queryRefundTicketAdd.do")
	@ResponseBody
	public String queryRefundTicketOrderId(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");						
		String orderId = extRefundService.queryRefundTicketOrderId(order_id);
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
	
	//查看cp_id是否存在
	@RequestMapping("/queryRefundStationTicketCpId.do")
	@ResponseBody
	public String queryRefundStationTicketCpId(HttpServletRequest request ,HttpServletResponse response){
		String cp_id = this.getParam(request, "cp_id");			
		String order_id = this.getParam(request, "order_id");	
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("cp_id", cp_id);
		String cpId = extRefundService.queryRefundStationTicketCpId(paramMap);//查看该订单号里面是否存在该车票id
		String cpIdIsRefund = extRefundService.queryCpidIsRefund(paramMap);//产看该车票号是否已经生成退款
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
		
		Map<String, String> map_buy = extRefundService.queryBuymoneyAndTicketpaymoney(order_id);
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
		Map<String, String> map_buy = extRefundService.queryRefundMoney(orderId,streamId);
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
			extRefundService.updateOut_Ticket_Refund(log_Map,outTicket_Defeated_Map);
		}
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
		extRefundService.updateRefreshNotice(update_RefreshNoticeMap);
		request.setAttribute("statusList", statusList);

		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
				str += "&refund_status="+arr[i];
		}
	return "redirect:/extRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
			extRefundService.queryRefundTicketInfo(id_Map);
		Map<String,String> refundTicketInfo   = ticketInfo.get(0);
		List<Map<String, Object>> history ;
		if("2".equals(refundTicketInfo.get("refund_type"))||"3".equals(refundTicketInfo.get("refund_type"))
				||"4".equals(refundTicketInfo.get("refund_type"))||"5".equals(refundTicketInfo.get("refund_type"))){ //差额退款的日志
			history = extRefundService.queryHistroyByOrderId(order_id);
		}else{
			history = extRefundService.queryHistroyByCpId(cp_id);
		}
		//List<Map<String, Object>> history = extRefundService.queryHistroyByOrderId(order_id);
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
			extRefundService.insertLog(map);
			HashMap<String, Object> ordermap = new HashMap<String, Object>();
			ordermap.put("user", opt_person);
			ordermap.put("order_id", order_id);
			ordermap.put("refund_seq", refund_seq);
			extRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
			extRefundService.updateRefundOpt(ordermap);// 更新退款表中操作人信息
		} else if (isLock.indexOf(opt_person) != -1) {
			isLock = "";
		}
		HashMap<String, Object> ordermap = new HashMap<String, Object>();
		ordermap.put("user", opt_person);
		ordermap.put("order_id", order_id);
		extRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
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
			extRefundService.updateOrderstatusToRobotGai(updateMap);
			result = "yes";
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", order_id);
			System.out.println("!!!!!!!!!!!!!!!"+refund_status);
			if(refund_status.equals("01")){
				map.put("content", user+"点击了【机器改签】");
			}else if(refund_status.equals("05")){
				map.put("content", user+"点击了【机器退票】");
			}
			map.put("user", user);
			extRefundService.insertLog(map);
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
