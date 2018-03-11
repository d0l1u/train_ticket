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
import com.l9e.transaction.service.InnerRefundService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.InnerBookVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatUtil;
/**
 * 退票管理
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/innerRefund")
public class InnerRefundController extends BaseController {
	private static final Logger logger = Logger.getLogger(InnerRefundController.class);
	
	@Resource
	private InnerRefundService innerRefundService;
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
		String selectAllrefund_status = this.getParam(request, "selectAllrefund_status");//全选
		request.setAttribute("selectAllrefund_type", selectAllrefund_type);
		request.setAttribute("selectAllrefund_status", selectAllrefund_status);

		request.setAttribute("refund_types", InnerBookVo.getRefund_Types());
		request.setAttribute("refund_statuses", RefundTicketVo.getInnerRefund_Status());
		request.setAttribute("innerChannels",InnerBookVo.getChannel());
		return "redirect:/innerRefund/queryRefundTicketList.do?refund_status=00&refund_status=34";
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
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
		String begin_create_time = this.getParam(request, "begin_create_time");//开始时间
		String end_create_time = this.getParam(request, "end_create_time");//结束时间
		List<String>refund_TypeList = this.getParamToList(request, "refund_type");//退款类型
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> channelList = this.getParamToList(request, "inner_channel"); //  查询购买渠道
		String opt_person = this.getParam(request, "opt_person");
		String selectAllrefund_type = this.getParam(request, "selectAllrefund_type");//全选
		String selectAllrefund_status = this.getParam(request, "selectAllrefund_status");//全选
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("begin_create_time", begin_create_time);//开始时间
			paramMap.put("end_create_time", end_create_time);//结束时间
			paramMap.put("opt_person", opt_person);
			paramMap.put("refund_type", refund_TypeList);
			paramMap.put("refund_status", refund_StatusList);
			paramMap.put("inner_channel", channelList);
		}
		
		/******************分页条件开始********************/
		int totalCount = innerRefundService.queryRefundTicketCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> refundTicketList = 
			innerRefundService.queryRefundTicketList(paramMap);
		
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
			request.setAttribute("refund_typeStr", refund_TypeList.toString());
			request.setAttribute("refund_statusStr", refund_StatusList.toString());
			request.setAttribute("inner_channelStr", channelList.toString());
			request.setAttribute("selectAllrefund_type", selectAllrefund_type);
			request.setAttribute("selectAllrefund_status", selectAllrefund_status);
		}
		request.setAttribute("refund_types", InnerBookVo.getRefund_Types());
		request.setAttribute("refund_statuses", RefundTicketVo.getInnerRefund_Status());
		request.setAttribute("innerChannels", InnerBookVo.getChannel());
		request.setAttribute("isShowList", 1);
		request.setAttribute("returnOptlog", RefundTicketVo.getReturnOptlog());
		return "innerRefund/innerRefundList";
	}
	/***********************获取账号*************************/
	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
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
//		String inner_channel = this.getParam(request, "inner_channel");
		/*********************创建容器*********************/
		Map<String,String> id_Map = new HashMap<String,String>();
		id_Map.put("stream_id", stream_id);
		id_Map.put("order_id", order_id);
		id_Map.put("cp_id", cp_id);
//		id_Map.put("inner_channel", inner_channel);
		/******************查询开始********************/
		List<Map<String,String>>ticketInfo=
			innerRefundService.queryRefundTicketInfo(id_Map);
		Map<String,String> refundTicketInfo   = ticketInfo.get(0);
		
		Map<String, String> orderInfo = innerRefundService.queryBookOrderInfo(order_id);
		List<Map<String, String>> bxList = innerRefundService.queryBookOrderInfoBx(order_id);
		List<Map<String, String>> cpList = innerRefundService.queryBookOrderInfoCp(order_id);
		
		
		List<Map<String, Object>> history ;
		if("2".equals(refundTicketInfo.get("refund_type"))||"3".equals(refundTicketInfo.get("refund_type"))){ //差额退款的日志
			history = innerRefundService.queryHistroyByOrderId(order_id);
		}else{
			history = innerRefundService.queryHistroyByCpId(cp_id);
		}
		String account = null;
		if(StringUtils.equals(refundTicketInfo.get("refund_status"), RefundTicketVo.REFUND_STATUS_PREPARE)){
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
		request.setAttribute("refund_statuses", RefundTicketVo.getInnerRefund_Status());
		request.setAttribute("innerChannels", InnerBookVo.getChannel());
		request.setAttribute("refund_types", InnerBookVo.getRefund_Types());
		request.setAttribute("order_statuses", BookVo.getBookStatus());
		request.setAttribute("ticket_types", RefundTicketVo.getTicket_Types());
		request.setAttribute("ids_types", RefundTicketVo.getCertificate_type());
		request.setAttribute("seat_types", RefundTicketVo.getSeat_type());
		request.setAttribute("query_type", this.getParam(request, "query_type"));
		request.setAttribute("statusList", statusList);
		request.setAttribute("pageIndex", pageIndex);
		return "innerRefund/innerRefundInfo";
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
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		/******************更改条件********************/
		String opt_person = loginUserVo.getReal_name();//当前登录人\
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
		String change_ticket_info = this.getParam(request, "change_ticket_info"); //改签明细

		String opt_person_log = opt_person+"点击了退款!车票ID："+cp_id+"实际退款金额为："+actual_refund_money+"计划退款时间为："+refund_limit+"天";
		logger.info(opt_person_log);
		if(StringUtils.isEmpty(refund_money)||StringUtils.isEmpty(actual_refund_money)||StringUtils.isEmpty(refund_limit)){
			logger.error("参数为空，退款失败");
			Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
			log_Map.put("cp_id", cp_id);
			log_Map.put("opt_person", "_"+opt_person);
			log_Map.put("opt_person_log", opt_person_log);
			log_Map.put("order_time", order_time);
			//innerRefundService.addErrorLogInfo(log_Map);
			logger.info("车票："+cp_id+"，操作人："+opt_person+"，内容："+opt_person_log+"，订单时间："+order_time);
			PrintWriter out;
			try {
				out = response.getWriter();
			
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
			builder.append("alert(\"退款参数为空，退款操作失败，请重试！\");");
			builder.append("window.location.href=\"");
			builder.append("/innerRefund/queryRefundPage.do\";</script>");
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
			innerRefundService.updateRefundTicket(log_Map,refund_Map);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", opt_person);
			optMap.put("channel", "inner");
			optMap.put("all", "refund");
			tj_OpterService.operate(optMap);
		}
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
			str += "&refund_status="+arr[i];
		}
		return "redirect:/innerRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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

		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		/******************更改条件********************/
		String order_time = this.getParam(request, "create_time");
		String stream_id = this.getParam(request, "stream_id");
		String cp_id = this.getParam(request, "cp_id");
		String order_id = this.getParam(request, "order_id");
		String our_remark = this.getParam(request, "our_remark");
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
		order_Map.put("order_id", order_id);
		order_Map.put("refund_total", refund_total);
		if(StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"未通过，stream_id："+stream_id);
		}else{
		/****************执行Service进行修改******************/
			innerRefundService.updateRefuseRefund(log_Map,refuse_Map,order_Map);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", opt_person);
			optMap.put("channel", "inner");
			optMap.put("type", "differ");
			optMap.put("all", "refund");
			tj_OpterService.operate(optMap);
		}
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
			str += "&refund_status="+arr[i];
		}
		return "redirect:/innerRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
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
			innerRefundService.updateDifferRefund(log_Map,differ_Map);
			
		}
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
			str += "&refund_status="+arr[i];
		}
		return "redirect:/innerRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
	}
	@RequestMapping("addrefundTicket.do")
	public String AddrefundTicket(HttpServletRequest request,HttpServletResponse response){
		return "innerRefund/innerRefundAdd";
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
//		InnerBookVo innerBookVo=(InnerBookVo) innerRefundService.queryBookOrderInfo(order_id);
		String inner_channel = this.getParam(request, "innerChannels");	
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
		map_add.put("inner_channel", inner_channel);
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
		log_Map.put("inner_channel", inner_channel);
		log_Map.put("notify_num", "0");
		log_Map.put("user_remark", user_remark);
		log_Map.put("opt_person_log", opt_person_log);
		/****************执行操作******************/
		logger.info(opt_person_log+"验证未通过"+"order_id="+order_id);
		List<Map<String,Object>> opt_map = innerRefundService.queryrefundTicketAdd(log_Map,map_add);	//执行差额退款容器
		return "redirect:/innerRefund/queryRefundTicketList.do";
	}
	//查看order_id是否存在
	@RequestMapping("/queryRefundTicketAdd.do")
	@ResponseBody
	public String queryRefundTicketOrderId(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");						
		String orderId = innerRefundService.queryRefundTicketOrderId(order_id);
		//String orderIdExists = innerRefundService.queryRefundTicketOrderIdExists(order_id);//查看该订单号是否已经生成差额退款
		//System.out.println("orderId="+orderId);
		String result = null;
		if(StringUtils.isEmpty(innerRefundService.queryRefundTicketOrderId(order_id))){
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
	//查询buy_money和ticket_pay_money,验证验证refund_money + buy_money <= ticket_pay_money
	@RequestMapping("/queryBuymoneyAndTicketpaymoney.do")
	@ResponseBody
	public String queryBuymoneyAndTicketpaymoney(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");	
		String refund_money = this.getParam(request, "refund_money");
		
		Map<String, String> map_buy = innerRefundService.queryBuymoneyAndTicketpaymoney(order_id);
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
		Map<String, String> map_buy = innerRefundService.queryRefundMoney(orderId,streamId);
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

		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		/******************更改条件********************/
		String order_time = this.getParam(request, "create_time");
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");
		String refund_money = this.getParam(request, "refund_money");
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
		outTicket_Defeated_Map.put("opt_person", opt_person);
		if(!SwitchUtils.isNum(refund_money)|| StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id为："+stream_id);
		}else{
			/****************执行Service进行修改******************/
			innerRefundService.updateOut_Ticket_Refund(log_Map,outTicket_Defeated_Map);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", opt_person);
			optMap.put("channel", "inner");
			optMap.put("type", "failure");
			optMap.put("all", "refund");
			tj_OpterService.operate(optMap);
		}
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
			str += "&refund_status="+arr[i];
		}
		return "redirect:/innerRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
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
		innerRefundService.updateRefreshNotice(update_RefreshNoticeMap);
		return "redirect:/innerRefund/queryRefundTicketList.do?refund_status=00";
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
	
	@RequestMapping("/queryNoReply.do")
	public void queryNoReply(HttpServletRequest request,HttpServletResponse response){
		String no_reply = null;
		try {
			no_reply = Integer.toString(innerRefundService.queryNoReplyCount());
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
	
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		List<Map<String, Object>> history = innerRefundService.queryHistroyByOrderId(order_id);
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
	 * 进入线下退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAddRefundPage.do")
	public String toAddRefundPage(HttpServletRequest request,HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");	
		String cpid= this.getParam(request, "cpid");
		if(!order_id.isEmpty()){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("order_id", order_id);
			List<Map<String, String>> cpidList=innerRefundService.queryRefundTicket(paramMap);
			request.setAttribute("cpidList", cpidList);
		}
		request.setAttribute("cpid", cpid);
		request.setAttribute("order_id", order_id);
		return "innerRefund/refundAdd";
	}
	
	/**
	 * 增加线下退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("offlineRefundTicketAdd.do")
	public String offlineRefundTicketAdd(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得操作人姓名opt_person
		/******************更改条件********************/
		String order_id = this.getParam(request, "order_id");						//订单号
		String cp_id = this.getParam(request, "cpid");								//车票ID
		String refund_money = this.getParam(request, "refund_money");	//退款金额
		String actual_refund_money = this.getParam(request, "actual_refund_money");	//12306退款金额
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");		//12306退款流水号
		String our_remark = this.getParam(request, "our_remark");					//退款原因
		String inner_channel = this.getParam(request, "inner_channel");	
		String content = opt_person+"点击了生成线下退票!orderId为："+order_id+",cpId为:"+cp_id+",金额为："+refund_money+",退款原因为："+our_remark;
		/******************创建容器********************/
		Map<String, Object> map_add = new HashMap<String, Object>();
		map_add.put("order_id", order_id);
		map_add.put("cp_id", cp_id);
		map_add.put("refund_money", refund_money);
		map_add.put("actual_refund_money", actual_refund_money);
		map_add.put("refund_12306_seq", refund_12306_seq);
		map_add.put("our_remark", our_remark);
		map_add.put("refund_type", "6"); //退款类型：线下退款
		map_add.put("refund_seq", CreateIDUtil.createID("TK"));//自动生成以TK开头的退款流水号
		map_add.put("create_time", "now()");
		map_add.put("refund_status", "66");
		map_add.put("opt_person", opt_person);
		map_add.put("inner_channel", inner_channel);
		/****************将条件添加到容器******************/
		Map<String,String>log_Map = new HashMap<String,String>();       //日志容器
		log_Map.put("order_id", order_id);
		log_Map.put("cp_id", cp_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", content);
		/****************执行操作******************/
		innerRefundService.queryRefundTicketAdd(log_Map,map_add);	//执行线下退票
		return "redirect:/innerRefund/queryRefundTicketList.do?refund_status=66&refund_status=77";
	}
	
	
	/**
	 * 线下退款流程
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateOfflineRefund.do")
	public String updateOfflineRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		/******************更改条件********************/
		String order_time = this.getParam(request, "create_time");//订单时间
		String stream_id = this.getParam(request, "stream_id");
		String order_id = this.getParam(request, "order_id");//订单id
		String cp_id = this.getParam(request, "cp_id");//车票id
		String actual_refund_money = this.getParam(request, "actual_refund_money");//12306实际退款金额
		String refund_money = this.getParam(request, "refund_money");//退款金额
		String our_remark = this.getParam(request, "our_remark");//备注
		String opt_person = loginUserVo.getReal_name();
		
		String opt_person_log = opt_person+"点击了退款!订单ID：" + order_id + "车票ID："+cp_id+"实际退款金额为："+actual_refund_money + "备注：" + our_remark;
		/******************创建容器********************/
		Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
		Map<String,Object> offline_Map = new HashMap<String,Object>();//执行线下退款容器
		/****************将条件添加到容器******************/
		//#order_id#,#cp_id#,#opt_person_log#,NOW(),#order_time#,#opt_person#
		log_Map.put("order_id", order_id);
		log_Map.put("cp_id", cp_id);
		log_Map.put("opt_person", opt_person);
		log_Map.put("opt_person_log", opt_person_log);
		log_Map.put("order_time", order_time);
		offline_Map.put("stream_id", stream_id);
		offline_Map.put("order_id", order_id);
		offline_Map.put("refund_money", refund_money);
		offline_Map.put("opt_person", opt_person);
		if(!SwitchUtils.isNum(refund_money)||StringUtils.isEmpty(stream_id)){
			logger.info(opt_person_log+"验证未通过，金额为："+refund_money+"stream_id="+stream_id);
		}else{
			/****************执行Service进行修改******************/
			innerRefundService.updateOfflineRefund(log_Map,offline_Map);
		}
		String[] arr = statusList.split(",");
		String str = "";
		for(int i=0;i<arr.length;i++){
			str += "&refund_status="+arr[i];
		}
		return "redirect:/innerRefund/queryRefundTicketList.do?pageIndex="+pageIndex+str;
	}
	
	/**
	 *退款失败再次发起
	 */
	@RequestMapping("/RefundTicketAgain.do")
	@ResponseBody
	public void RefundTicketAgain(HttpServletResponse response,HttpServletRequest request){
		logger.info("退款start...");
		String result = "yes";
		try{
			String order_time = this.getParam(request, "create_time");//订单时间
			String stream_id = this.getParam(request, "stream_id");
			String order_id = this.getParam(request, "order_id");//订单id
			String cp_id = this.getParam(request, "cp_id");//车票id
			//找到当前登录人
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String opt_person = loginUserVo.getReal_name();
			/******************创建容器********************/
			Map<String,String>log_Map = new HashMap<String,String>(); //日志容器
			Map<String,Object> refundMap = new HashMap<String,Object>();//执行线下退款容器
			/****************将条件添加到容器******************/
			String opt_person_log = opt_person+"点击了重新通知退款!订单ID：" + order_id +"stream_id:"+stream_id;
			log_Map.put("order_id", order_id);
			log_Map.put("cp_id", cp_id);
			log_Map.put("order_time", order_time);
			log_Map.put("opt_person", opt_person);
			log_Map.put("opt_person_log", opt_person_log);
			refundMap.put("stream_id", stream_id);
			refundMap.put("order_id", order_id);
			refundMap.put("cp_id", cp_id);
			refundMap.put("opt_person", opt_person);
			innerRefundService.refundTicketAgain(log_Map,refundMap);
		}catch(Exception e){
			e.printStackTrace();
			result="no";
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
