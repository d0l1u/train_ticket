package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.l9e.transaction.service.AppRefundService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.vo.AppRefundTicketVo;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatUtil;
@Controller
@RequestMapping("/appRefund")
public class AppRefundController extends BaseController {

	private static final Logger logger = Logger.getLogger(AppRefundController.class);
	
	@Resource
	private AppRefundService appRefundService;
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
		request.setAttribute("refund_types", AppRefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", AppRefundTicketVo.getRefund_Status());
		request.setAttribute("channelTypes", BookVo.getchannel_type());
		return "redirect:/appRefund/queryRefundTicketList.do?refund_status=00";
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
		String user_phone = this.getParam(request, "user_phone");//user_phone用户电话
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
		String begin_create_time = this.getParam(request, "begin_create_time");//开始时间
		String end_create_time = this.getParam(request, "end_create_time");//结束时间
		List<String>refund_TypeList = this.getParamToList(request, "refund_type");//退款类型
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> channelList = this.getParamToList(request, "channel");//渠道
		String opt_person = this.getParam(request, "opt_person");
		List<String> merchant_idList = this.getParamToList(request, "merchant_id");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		if(order_id.trim().length()>0){
		paramMap.put("order_id", order_id);
		}else{
			paramMap.put("user_phone", user_phone);
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("begin_create_time", begin_create_time);//开始时间
			paramMap.put("end_create_time", end_create_time);//结束时间
			paramMap.put("opt_person", opt_person);
			paramMap.put("refund_type", refund_TypeList);
			paramMap.put("channel", channelList);
			paramMap.put("refund_status", refund_StatusList);
			paramMap.put("merchant_id", merchant_idList);
		}
		/******************分页条件开始********************/
		int totalCount = appRefundService.queryRefundTicketCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> refundTicketList = 
			appRefundService.queryRefundTicketList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("refundTicketList", refundTicketList);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("user_phone", user_phone);
			request.setAttribute("refund_seq", refund_seq);
			request.setAttribute("refund_12306_seq", refund_12306_seq);
			request.setAttribute("begin_create_time", begin_create_time);
			request.setAttribute("end_create_time", end_create_time);
			request.setAttribute("opt_person", opt_person);
			request.setAttribute("refund_typeStr", refund_TypeList.toString());
			request.setAttribute("refund_statusStr", refund_StatusList.toString());
			request.setAttribute("channelStr", channelList.toString());
			request.setAttribute("merchant_idList", merchant_idList);
		}
		request.setAttribute("refund_types", AppRefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", AppRefundTicketVo.getRefund_Status());
		request.setAttribute("channelTypes", BookVo.getchannel_type());
		request.setAttribute("isShowList", 1);
		request.setAttribute("returnOptlog", RefundTicketVo.getReturnOptlog());
		return "appRefund/appRefundList";
	}
	
	/***********************获取账号*************************/
	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}
	
	@RequestMapping("/updateAppRefundNotifyNum.do")
	public String updateAppRefundNotifyNum(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		/******************更改条件********************/
		String notify_id = this.getParam(request, "notify_id");
		String order_id = this.getParam(request, "order_id");
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
		appRefundService.updateExtRefundNotifyNum(paramMap);
		return "redirect:/appRefund/queryRefundTicketList.do?refund_status=00";
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
		
		/******************查询条件********************/
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
			appRefundService.queryRefundTicketInfo(id_Map);
		Map<String,String> refundTicketInfo   = ticketInfo.get(0);
		
		Map<String, String> orderInfo = appRefundService.queryBookOrderInfo(order_id);
		List<Map<String, String>> bxList = appRefundService.queryBookOrderInfoBx(order_id);
		List<Map<String, String>> cpList = appRefundService.queryBookOrderInfoCp(order_id);
		
		
		List<Map<String, Object>> history ;
		if("2".equals(refundTicketInfo.get("refund_type"))||"4".equals(refundTicketInfo.get("refund_type"))||"5".equals(refundTicketInfo.get("refund_type"))){ //差额退款的日志
			history = appRefundService.queryHistroyByOrderId(order_id);
		}else{
			history = appRefundService.queryHistroyByCpId(cp_id);
		}
		String account = null;
		if(StringUtils.equals(refundTicketInfo.get("refund_status"), AppRefundTicketVo.REFUND_STATUS_PREPARE)){
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
		request.setAttribute("refund_statuses", AppRefundTicketVo.getRefund_Status());
		request.setAttribute("refund_types", AppRefundTicketVo.getRefund_Types());
		request.setAttribute("order_statuses", BookVo.getBookStatus());
		request.setAttribute("ticket_types", AppRefundTicketVo.getTicket_Types());
		request.setAttribute("ids_types", AppRefundTicketVo.getCertificate_type());
		request.setAttribute("seat_types", AppRefundTicketVo.getSeat_type());
		request.setAttribute("query_type", this.getParam(request, "query_type"));
		request.setAttribute("channelTypes", BookVo.getchannel_type());
		return "appRefund/appRefundInfo";
	}
	
	/**
	 * 同意退款流程
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
		
		String opt_person_log = opt_person+"点击了退款!车票ID："+cp_id+"实际退款金额为："+actual_refund_money+"计划退款时间为："+refund_limit+"天";
		logger.info(opt_person_log);
		if(StringUtils.isEmpty(refund_money)||StringUtils.isEmpty(actual_refund_money)||StringUtils.isEmpty(refund_limit)){
			logger.error("参数为空，退款失败");
			Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
			log_Map.put("cp_id", cp_id);
			log_Map.put("opt_person", "_"+opt_person);
			log_Map.put("opt_person_log", opt_person_log);
			log_Map.put("order_time", order_time);
			//appRefundService.addErrorLogInfo(log_Map);
			logger.info("车票："+cp_id+"，操作人："+opt_person+"，内容："+opt_person_log+"，订单时间："+order_time);
			PrintWriter out;
			try {
				out = response.getWriter();
			
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
			builder.append("alert(\"退款参数为空，退款操作失败，请重试！\");");
			builder.append("window.location.href=\"");
			builder.append("/appRefund/queryRefundPage.do\";</script>");
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
		log_Map.put("order_id", order_id);
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
		refund_Map.put("refund_status", "11");
		/****************执行Service进行修改******************/
		if(StringUtils.isEmpty(stream_id)||!SwitchUtils.isNum(refund_money)
				||!SwitchUtils.isNum(actual_refund_money)){
			logger.info("stream_id为空，opt_person_log="+opt_person_log+
					"refund_money="+refund_money+"actual_refund_money="+actual_refund_money);
		}else{
			//appRefundService.updateRefundTicket(log_Map,refund_Map);
		}
		appRefundService.updateRefundTicket(log_Map,refund_Map);
		//客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", opt_person);
		optMap.put("channel", "app");
		optMap.put("all", "refund");
		tj_OpterService.operate(optMap);
		return "redirect:/appRefund/queryRefundTicketList.do?refund_status=00";
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
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
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
		refuse_Map.put("refund_status", "33");
		refuse_Map.put("refund_fail_reason", our_remark);
		order_Map.put("order_id", order_id);
		order_Map.put("refund_total", refund_total);
		if(StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"未通过，stream_id："+stream_id);
		}else{
		/****************执行Service进行修改******************/
			appRefundService.updateRefuseRefund(log_Map,refuse_Map,order_Map);
		}
		return "redirect:/appRefund/queryRefundTicketList.do?refund_status=00" ;
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
			appRefundService.updateDifferRefund(log_Map,differ_Map);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", opt_person);
			optMap.put("channel", "app");
			optMap.put("type", "differ");
			optMap.put("all", "refund");
			tj_OpterService.operate(optMap);
		}
		return "redirect:/appRefund/queryRefundTicketList.do?refund_status=00";
	}
	
	@RequestMapping("AddrefundTicket.do")
	public String AddrefundTicket(HttpServletRequest request,HttpServletResponse response){
		return "appRefund/appRefundAdd";
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
		/******************更改条件********************/
		String order_id = this.getParam(request, "order_id");			//订单号
		String refund_money = this.getParam(request, "refund_money");	//差额退款金额
		String user_remark = this.getParam(request, "user_remark");		//退款原因
		String opt_person_log = opt_person+"点击了生成差额退款!orderId为："+order_id+",金额为："+refund_money+",退款原因为："+user_remark;
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
		List<Map<String,Object>> opt_map = appRefundService.queryrefundTicketAdd(log_Map,map_add);	//执行差额退款容器
		return "redirect:/appRefund/queryRefundTicketList.do";
	}
	
	@RequestMapping("toAddStationRefundTicket.do")
	public String toAddStationRefundTicket(HttpServletRequest request,HttpServletResponse response){
		return "appRefund/appRefundAddStation";
	}
	
	/**
	 * 增加车站App退款流程
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
		//String merchant_order_id = this.getParam(request, "merchant_order_id");		//商户订单号
		//String merchant_order_id = appRefundService.queryMerchantOrderId(order_id);
		String refund_percent = this.getParam(request, "refund_percent");			//退款手续费
		//String channel = this.getParam(request, "channel");							//合作商户ID
		String actual_refund_money = this.getParam(request, "actual_refund_money");	//实际退款金额
		String refund_money = this.getParam(request, "refund_money");				//商户退款金额
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");		//12306退款流水号
		String user_remark = this.getParam(request, "user_remark");					//退款原因
		
		String opt_person_log = opt_person+"点击了生成车站退票!orderId为："+order_id+",cpId为:"+cp_id+",金额为："+refund_money+",退款原因为："+user_remark;
		/******************创建容器********************/
		Map<String, Object> map_add = new HashMap<String, Object>();
		map_add.put("order_id", order_id);
		map_add.put("cp_id", cp_id);
		//map_add.put("merchant_order_id", merchant_order_id);
		map_add.put("refund_percent", refund_percent);
		//map_add.put("channel", channel);
		map_add.put("actual_refund_money", actual_refund_money);
		map_add.put("refund_money", refund_money);
		map_add.put("refund_12306_seq", refund_12306_seq);
		map_add.put("user_remark", user_remark);
		map_add.put("refund_type", "2");
		map_add.put("refund_seq", CreateIDUtil.createID("APTK"));//自动生成以TK开头的退款流水号
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
		List<Map<String,Object>> opt_map = appRefundService.queryRefundStationTicketAdd(log_Map,map_add);	//执行车站退票
		return "redirect:/appRefund/queryRefundTicketList.do";
	}
	
	
	//查看order_id是否存在
	@RequestMapping("/queryRefundTicketAdd.do")
	@ResponseBody
	public String queryRefundTicketOrderId(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");						
		String orderId = appRefundService.queryRefundTicketOrderId(order_id);
		//String orderIdExists = appRefundService.queryRefundTicketOrderIdExists(order_id);//查看该订单号是否已经生成差额退款
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
		String cpId = appRefundService.queryRefundStationTicketCpId(paramMap);//查看该订单号里面是否存在该车票id
		String cpIdIsRefund = appRefundService.queryCpidIsRefund(paramMap);//产看该车票号是否已经生成退款
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
		
		Map<String, String> map_buy = appRefundService.queryBuymoneyAndTicketpaymoney(order_id);
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
		Map<String, String> map_buy = appRefundService.queryRefundMoney(orderId,streamId);
		String flag = null;
		if(map_buy!=null && (map_buy.get("pay_money")!=null) && (map_buy.get("sumRefundMoney")!=null)){
			Double pay_money = Double.parseDouble(map_buy.get("pay_money"));
			Double sumRefundMoney = Double.parseDouble(map_buy.get("sumRefundMoney"));
			Double refund_money = Double.parseDouble(map_buy.get("refund_money"));
			//System.out.println("***************sumRefundMoney="+sumRefundMoney+"~~~~~~~~~~~~~~~~pay_money="+pay_money);
			//System.out.println("############"+(pay_money-sumRefundMoney)+"~~~~~~~~~~refund_money="+refund_money);
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
		String refund_seq = this.getParam(request, "refund_seq");
		String opt_person = loginUserVo.getReal_name();
		String opt_person_log = opt_person+"点击了全额退款!";
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
		outTicket_Defeated_Map.put("refund_seq", refund_seq);
		outTicket_Defeated_Map.put("opt_person", opt_person);
		if(!SwitchUtils.isNum(refund_money)|| StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id为："+stream_id);
		}else{
			/****************执行Service进行修改******************/
			appRefundService.updateOut_Ticket_Refund(log_Map,outTicket_Defeated_Map);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", opt_person);
			optMap.put("channel", "app");
			optMap.put("type", "failure");
			optMap.put("all", "refund");
			tj_OpterService.operate(optMap);
		}
		return "redirect:/appRefund/queryRefundTicketList.do?refund_status=00";
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
		String refund_status = AppRefundTicketVo.REFUND_STATUS_START;
		int notify_num = 0;
		Map<String,Object>update_RefreshNoticeMap = new HashMap<String,Object>();
		update_RefreshNoticeMap.put("refund_status", refund_status);
		update_RefreshNoticeMap.put("notify_num", notify_num);
		appRefundService.updateRefreshNotice(update_RefreshNoticeMap);
		return "redirect:/appRefund/queryRefundTicketList.do?refund_status=00";
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
	
	//跳转到生成电话退票页面
	@RequestMapping("toAddPhoneRefundTicket.do")
	public String toAddPhoneRefundTicket(HttpServletRequest request,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");						//订单号
		String refund_type = this.getParam(request, "refund_type");	
		String refund = this.getParam(request, "refund");
		request.setAttribute("order_id", order_id);
		request.setAttribute("refund_type", refund_type);
		request.setAttribute("refund", refund);
		request.setAttribute("refund_types", AppRefundTicketVo.getRefund_Types());
		return "appRefund/appRefundTicketAddPhone";
	}
	
	/**
	 * 增加车站App退款流程
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addPhoneRefundTicket.do")
	public String addPhoneRefundTicket(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得操作人姓名opt_person
		/******************更改条件********************/
		String type = this.getParam(request, "type");
		String refundAgain = this.getParam(request, "refundAgain");
		if(type.equals("agree")){
			String order_id = this.getParam(request, "order_id");						//订单号
			String refund_type = this.getParam(request, "refund_type");					//退款类型
			String refund_12306_seq = this.getParam(request, "refund_12306_seq");		//12306退款流水号
			String actual_refund_money = this.getParam(request, "actual_refund_money");	//实际退款金额
			String alter_tickets_money = this.getParam(request, "alter_tickets_money");	//改签差价
			String refund_money = this.getParam(request, "refund_money");				//商户退款金额
			String bank_username = this.getParam(request, "bank_username");				//银行账户姓名
			String bank_type = this.getParam(request, "bank_type");						//乘客银行类型
			String bank_type1 = this.getParam(request, "bank_type1");					//乘客银行类型为“其他”时，选择文本框输入值
			String bank_account = this.getParam(request, "bank_account");				//乘客银行账号
			String bank_openName = this.getParam(request, "bank_openName");				//开户行名称
			String opt_person_log = "生成电话退票【同意退票】";
			/******************创建容器********************/
			Map<String, Object> map_add = new HashMap<String, Object>();
			map_add.put("order_id", order_id);
			map_add.put("refund_type", refund_type);
			map_add.put("refund_12306_seq", refund_12306_seq);
			map_add.put("actual_refund_money", actual_refund_money);
			map_add.put("alter_tickets_money", alter_tickets_money);
			map_add.put("refund_money", refund_money);
			map_add.put("bank_username", bank_username);
			if(bank_type.equals("其他")){
				map_add.put("bank_type", bank_type1);
			}else{
				map_add.put("bank_type", bank_type);
			}
			map_add.put("bank_account", bank_account);
			map_add.put("bank_openName", bank_openName);
			map_add.put("refund_seq", CreateIDUtil.createID("APTK"));//自动生成以TK开头的退款流水号
			map_add.put("create_time", "now()");
			map_add.put("refund_status", "11");
			map_add.put("opt_person", opt_person);
			/****************将条件添加到容器******************/
			Map<String,String>log_Map = new HashMap<String,String>();       //日志容器
			log_Map.put("order_id", order_id);
			log_Map.put("opt_person", opt_person);
			log_Map.put("opt_person_log", opt_person_log);
			/****************执行操作******************/
			if(refundAgain.equals("1")){
				appRefundService.updateRefundStationTicketAdd(log_Map,map_add);	//执行车站退票
			}else{
				appRefundService.queryRefundStationTicketAdd(log_Map,map_add);	//执行车站退票
			}
		}else if(type.equals("refuse")){
			String order_id = this.getParam(request, "order_id1");			//订单号
			String refund_type = this.getParam(request, "refund_type1");	//退款类型
			String user_remark = this.getParam(request, "user_remark");		//拒绝退款原因
			String opt_person_log = "生成电话退票【拒绝退票】，退款原因为："+user_remark;
			/******************创建容器********************/
			Map<String, Object> map_add = new HashMap<String, Object>();
			map_add.put("order_id", order_id);
			map_add.put("refund_type", refund_type);
			map_add.put("our_remark", user_remark);
			map_add.put("refund_seq", CreateIDUtil.createID("APTK"));//自动生成以TK开头的退款流水号
			map_add.put("create_time", "now()");
			map_add.put("refund_status", "22");
			map_add.put("opt_person", opt_person);
			/****************将条件添加到容器******************/
			Map<String,String>log_Map = new HashMap<String,String>();       //日志容器
			log_Map.put("order_id", order_id);
			log_Map.put("opt_person", opt_person);
			log_Map.put("opt_person_log", opt_person_log);
			/****************执行操作******************/
			appRefundService.queryRefundStationTicketAdd(log_Map,map_add);	//执行车站退票
		}
		return "redirect:/appRefund/queryRefundTicketList.do";
	}
	
	/**
	 * 查询电话退票明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRefundPhoneInfo.do")
	public String queryRefundPhoneInfo(HttpServletRequest request,
			HttpServletResponse response){
		/******************查询条件********************/
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		/*********************创建容器*********************/
		Map<String,String> id_Map = new HashMap<String,String>();
		id_Map.put("stream_id", stream_id);
		id_Map.put("order_id", order_id);
		/******************查询开始********************/
		List<Map<String,String>>ticketInfo=
			appRefundService.queryRefundTicketPhoneInfo(id_Map);
		List<Map<String, Object>> history = appRefundService.queryHistroyByOrderId(order_id);
		/******************request绑定********************/
		request.setAttribute("history", history);
		request.setAttribute("stream_id", stream_id);
		request.setAttribute("order_id", order_id);
		request.setAttribute("orderInfo", ticketInfo.get(0));
		request.setAttribute("refund_statuses", AppRefundTicketVo.getRefund_Status());
		request.setAttribute("refund_types", AppRefundTicketVo.getRefund_Types());
		request.setAttribute("order_statuses", BookVo.getBookStatus());
		request.setAttribute("ticket_types", AppRefundTicketVo.getTicket_Types());
		request.setAttribute("ids_types", AppRefundTicketVo.getCertificate_type());
		request.setAttribute("seat_types", AppRefundTicketVo.getSeat_type());
		request.setAttribute("query_type", this.getParam(request, "query_type"));
		return "appRefund/appRefundPhoneInfo";
	}
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String type = this.getParam(request, "type");
		/*********************创建容器*********************/
		Map<String,String> id_Map = new HashMap<String,String>();
		id_Map.put("stream_id", stream_id);
		id_Map.put("order_id", order_id);
		id_Map.put("cp_id", cp_id);
		/******************查询开始********************/
		List<Map<String,String>> ticketInfo;
		List<Map<String, Object>> history ;
		if(type.equals("1")){
				ticketInfo = appRefundService.queryRefundTicketPhoneInfo(id_Map);
				history = appRefundService.queryHistroyByOrderId(order_id);
		}else{
				ticketInfo = appRefundService.queryRefundTicketInfo(id_Map);
			Map<String,String> refundTicketInfo   = ticketInfo.get(0);
			if("2".equals(refundTicketInfo.get("refund_type"))||"4".equals(refundTicketInfo.get("refund_type"))||"5".equals(refundTicketInfo.get("refund_type"))){ //差额退款的日志
				history = appRefundService.queryHistroyByOrderId(order_id);
			}else{
				history = appRefundService.queryHistroyByCpId(cp_id);
			}
		}
		
		
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
