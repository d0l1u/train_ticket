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
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.BookService;
import com.l9e.transaction.service.CtripService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.service.JDRefundService;
import com.l9e.transaction.service.SystemSettingService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.service.TrainSystemSettingService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AllRefundVo;
import com.l9e.transaction.vo.ElongVo;
import com.l9e.transaction.vo.JDRefundVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
/**
 * 京东退票管理
 * @author wangsf01
 *
 */
@Controller
@RequestMapping("/jdRefund")
public class JDRefundController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(JDRefundController.class);
	
	@Resource
	private JDRefundService jdRefundService;
	@Resource
	private BookService bookService;
	@Resource
	private SystemSettingService systemSettingService;
	@Resource
	private ExtRefundService extRefundService;
	@Resource
	private TrainSystemSettingService trainSystemSettingService;
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
		String urgent_order = this.getParam(request, "urgent_order");
		return "redirect:/jdRefund/queryJDRefundList.do?refund_status=02&refund_status=06&urgent_order="+urgent_order;
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryJDRefundList.do")
	public String queryallRefund(HttpServletRequest request,
			HttpServletResponse response){
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String jd_order_id = this.getParam(request, "jd_order_id");//京东流水号
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		if(StringUtils.isEmpty(begin_info_time)){
			Calendar theCa = Calendar.getInstance(); 
			theCa.setTime(new Date());  
			theCa.add(theCa.DATE, -7); 
			Date date = theCa.getTime();
			DateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
			String querydate=dff.format(date);
			begin_info_time = querydate;
		}
		
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String urgent_order = this.getParam(request, "urgent_order");
		List<String> refund_StatusList = this.getParamToList(request, "refund_status");//退款状态

		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		List<String> refund_status = new ArrayList<String>(refund_StatusList);
		if(order_id.trim().length()<=0){
			request.setAttribute("refund_status", refund_status.toString());
		}
	
		
		/******************查询Map********************/
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(order_id.trim().length()>0){
		paramMap.put("order_id", order_id);
		}else{
		paramMap.put("jd_order_id", jd_order_id);//京东流水号
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("refund_status", refund_status);
		paramMap.put("urgent_order", urgent_order);
		}
		/******************分页条件开始********************/
		int totalCount = jdRefundService.queryJDRefundCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> jdRefundList = jdRefundService.queryJDRefundList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("jdRefundList", jdRefundList);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("jd_order_id", jd_order_id);
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("urgent_order", urgent_order);
		}
		request.setAttribute("refundStatus", JDRefundVo.getRefundStatus());
	


		List<Map<String,Object>> returnlogList = trainSystemSettingService.querytrain_return_optlog();//查询返回日志的id及简称
		Map<String, Object> returnlogMap = new HashMap<String, Object>();
		for(int i = 0 ; i < returnlogList.size() ; i++){
			returnlogMap.put((String) returnlogList.get(i).get("return_id"), returnlogList.get(i).get("return_value"));
		}
		request.setAttribute("returnlogMap", returnlogMap);
		request.setAttribute("isShowList", 1);
		request.setAttribute("refund_and_alert", systemSettingService.querySystemRefundAndAlert("refund_and_alert"));
		Calendar theCa2 = Calendar.getInstance(); 
//		theCa2.add(java.util.Calendar.HOUR_OF_DAY, +2); //把时间设置为当前时间+2小时
		theCa2.add(java.util.Calendar.MINUTE, +30); //把时间设置为当前时间+30分钟
		Date date2 = theCa2.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String querydate2=df.format(date2);
		Calendar theCa4 = Calendar.getInstance(); 
//		theCa4.add(java.util.Calendar.HOUR_OF_DAY, +4);  //把时间设置为当前时间+4小时
		theCa4.add(java.util.Calendar.MINUTE, +90); //把时间设置为当前时间+90分钟
		Date date4 = theCa4.getTime();
		String querydate4=df.format(date4);
		request.setAttribute("now2", querydate2);
		request.setAttribute("now4", querydate4);

		return "jdRefund/jdRefundList";
	}

	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryJDRefundInfo.do")
	public String queryJDRefundInfo(HttpServletRequest request,
			HttpServletResponse response){
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
	
		String pageIndex =this.getParam(request, "pageIndex");
		/******************查询条件********************/
		String refund_seq = this.getParam(request, "refund_seq");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String isActive = this.getParam(request, "isActive");
		/*********************创建容器*********************/
		Map<String,Object> id_Map = new HashMap<String,Object>();
		id_Map.put("order_id", order_id);
		List<Map<String, String>> cpList = jdRefundService.queryJDRefundInfo(id_Map);//查询预订订单车票
		id_Map.put("refund_seq", refund_seq);
		id_Map.put("cp_id", cp_id);
		/******************查询开始********************/
		List<Map<String,String>> ticketInfo = jdRefundService.queryJDRefundInfo(id_Map); //查询明细
		Map<String,String> jdRefundInfo = ticketInfo.get(0);
		List<Map<String, Object>> history = jdRefundService.queryHistroy(id_Map); //查询日志信息
		/******************request绑定********************/
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		Map<String, String> orderInfo = bookService.queryBookOrderInfo(order_id);//查询预订订单信息
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		request.setAttribute("jdRefundInfo", jdRefundInfo);
		request.setAttribute("ticketInfo", ticketInfo);
		request.setAttribute("cpList", cpList);
		request.setAttribute("isActive", isActive);
		request.setAttribute("history", history);
		request.setAttribute("refund_seq", refund_seq);
		request.setAttribute("cp_id", cp_id);
		request.setAttribute("order_id", order_id);
		request.setAttribute("refundStatus", JDRefundVo.getRefundStatus());
		request.setAttribute("ticket_types", JDRefundVo.getTicket_Types());
		request.setAttribute("ids_types", JDRefundVo.getCertificate_type());
		request.setAttribute("seat_types", JDRefundVo.getSeat_type());
		request.setAttribute("query_type", this.getParam(request, "query_type"));
		request.setAttribute("refuse_Reason", JDRefundVo.getRefuseReason());
		request.setAttribute("statusList", statusList);
		request.setAttribute("pageIndex", pageIndex);
		request.setAttribute("refund_and_alert", systemSettingService.querySystemRefundAndAlert("refund_and_alert"));
		
		
		return "jdRefund/jdRefundInfo";
	}
	
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		String cp_id = this.getParam(request,"cp_id");
		Map<String,Object> id_Map = new HashMap<String,Object>();
		id_Map.put("order_id", order_id);
		id_Map.put("cp_id", cp_id);
		List<Map<String, Object>> history = jdRefundService.queryHistroy(id_Map); //查询日志信息
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

//		if (order_id.contains("_")) {// “联程”车票订单锁
//			order_id = order_id.substring(0, order_id.length() - 2);
//		}
		String opt_ren = loginUserVo.getReal_name();
		String key = "Lock_" + order_id;
		String value = "Lock_" + order_id + "&" + opt_ren;
		String isLock;
		isLock = (String) MemcachedUtil.getInstance().getAttribute(key); // 读值
		if (StringUtils.isEmpty(isLock)) {
			synchronized (TrainConsts.REFUND_LOCK) {
				MemcachedUtil.getInstance().setAttribute(key, value, 5 * 60 * 1000); // 写值
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("order_id", order_id);
				map.put("order_optlog", opt_ren + "锁定了订单：" + order_id);
				map.put("opter", opt_ren);
				jdRefundService.addJDRefundlog(map);	//添加日志
				HashMap<String, Object> ordermap = new HashMap<String, Object>();
				ordermap.put("opt_ren", opt_ren);
				ordermap.put("order_id", order_id);
				jdRefundService.updateRefundOpt(ordermap);// 更新退款表中操作人信息
			}
			isLock = "";
		} else if (isLock.indexOf(opt_ren) != -1) {
			isLock = "";
		}
//		HashMap<String, Object> ordermap = new HashMap<String, Object>();
//		ordermap.put("opter", opt_ren);
//		ordermap.put("order_id", order_id);
	//	jdRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
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
	
	
	
	//退款
	@RequestMapping("/refund.do")
	public String refund(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		String orderid = this.getParam(request, "orderId");
		String cp_id = this.getParam(request, "cpId");
		String refundseq = this.getParam(request, "refundseq");
		String refund_status = this.getParam(request, "refund_status");
		String refundmoney = this.getParam(request, "refund_money");
		String our_remark = this.getParam(request, "our_remark");
		String refund_12306_money = this.getParam(request, "refund_12306_money");//12306退款
	
		//查询本订单的eopid
		HashMap<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("order_id", orderid);
		String eopID = jdRefundService.selectEopID(queryMap);
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", orderid);
		paramMap.put("cp_id", cp_id);
		paramMap.put("refund_12306_money", refund_12306_money);
		paramMap.put("refund_status", refund_status);
		paramMap.put("our_remark", our_remark);
		
		//更新京东退款表状态为退款成功
		jdRefundService.updateJDRefundStatus(paramMap);
		
		paramMap.put("eop_order_id", eopID);
		paramMap.put("opt_person", user);
		paramMap.put("refund_type", "1");//1:用户退款
		paramMap.put("refundstream_status", "11");//00：准备退款  11:等待退款
		
		//开始eop退款
		int count = jdRefundService.updateRefundstreamStatus(paramMap);
		
		HashMap<String, String> pMap = new HashMap<String, String>();
		if(count>0){
		
		String content = "";
		if(refund_status.equals("11")){
			content = user + "点击了人工退票【立即退款】订单号："+orderid+" 车票号："+cp_id;
		}
		pMap.put("order_id", orderid);
		pMap.put("cp_id", cp_id);
		pMap.put("order_optlog", content);
		pMap.put("order_time", "1");
		pMap.put("opter", user);
		jdRefundService.addJDRefundlog(pMap);
		logger.info(content);

		//客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", user);
		optMap.put("channel", "19e");
		optMap.put("all", "refund");
		optMap.put("type", "verify");
		
		tj_OpterService.operate(optMap);
		}else{
			pMap.put("order_id", orderid);
			pMap.put("cp_id", cp_id);
			pMap.put("order_optlog", "退款异常，此次点击无效！orderid="+orderid +"cp_id="+cp_id);
			pMap.put("opter", user);
			jdRefundService.addJDRefundlog(pMap);
			logger.info("退款异常，此次点击无效！orderid="+orderid +"cp_id="+cp_id);
		}
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
			String[] arr1 = statusList.split(",");
			for(int i=0;i<arr1.length;i++){
				str1 += "&refund_status="+arr1[i];
			}
		}
		return "redirect:/jdRefund/queryJDRefundList.do?pageIndex="+pageIndex+str1;
	}
	
	//拒绝退款处理
	@RequestMapping("/refuse.do")
	public String refuse(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String pageIndex =this.getParam(request, "pageIndex");
		String order_id = this.getParam(request, "orderId");
		String cp_id = this.getParam(request, "cpId");
		String refundseq = this.getParam(request, "refundseq");
		String our_remark = this.getParam(request, "our_remark");
		String refuse_reason = this.getParam(request, "refuse_reason");
		String refund_12306_money = this.getParam(request, "refund_12306_money");//12306退款
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		paramMap.put("order_id", order_id);
		paramMap.put("cp_id", cp_id);
		paramMap.put("refund_12306_money", refund_12306_money);
		paramMap.put("refund_status", "10");//10：退款失败
		paramMap.put("our_remark", our_remark);
		paramMap.put("refuse_reason", refuse_reason);
	
		//更新京东退款表状态为失败
		jdRefundService.updateJDRefundStatus(paramMap);
		
		
		//查询本订单的eopid
		HashMap<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("order_id", order_id);
		String eopID = jdRefundService.selectEopID(queryMap);
		
		//更新EOP退款表为人工处理
		paramMap.put("eop_order_id", eopID);
		paramMap.put("opt_person", user);
		paramMap.put("refund_type", "1");//1:用户退款
		paramMap.put("refundstream_status", "07");//00：准备退款  11:等待退款  07：人工退票
		
		//开始eop退款
		int count = jdRefundService.updateRefundstreamStatus(paramMap);

		HashMap<String, String> pMap = new HashMap<String, String>();
		if(count > 0){
		String content = null;
	
		content = user + "点击了【拒绝退款】" + "，退款原因为：【"
		+ AllRefundVo.getRefuseReason().get("OTHER").get(refuse_reason) + "】";
		logger.info(user + "点击了【拒绝退款】" + "，退款原因为：【"
				+ AllRefundVo.getRefuseReason().get("OTHER").get(refuse_reason) + "】");
		
		pMap.put("order_id", order_id);
		pMap.put("cp_id", cp_id);
		pMap.put("order_optlog", content);
		pMap.put("opter", user);
		pMap.put("order_time", "1");
		jdRefundService.addJDRefundlog(pMap);
		logger.info(content);
		
		//客服操作记录---拒绝退票
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", user);
		optMap.put("type", "refuse");
		tj_OpterService.operate(optMap);
		} else {
			pMap.put("order_id", order_id);
			pMap.put("cp_id", cp_id);
			pMap.put("order_optlog", "拒绝退款异常，此次点击无效！orderid=" + order_id+ " cp_id=" + cp_id);
			pMap.put("opter", user);
			jdRefundService.addJDRefundlog(pMap);
			logger.info("拒绝退款异常，此次点击无效！orderid=" + order_id + "cp_id=" + cp_id);
		}
		
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
			String[] arr1 = statusList.split(",");
			for(int i=0;i<arr1.length;i++){
				str1 += "&refund_status="+arr1[i];
			}
		}
		
		return "redirect:/jdRefund/queryJDRefundList.do?pageIndex="+pageIndex+str1;
	}
	
	@RequestMapping("/updateOrderstatusToRobot.do")
	@ResponseBody
	public void updateOrderstatusToRobot(HttpServletResponse response,HttpServletRequest request, 
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
			updateMap.put("opt_ren", user);
			jdRefundService.updateOrderstatusToRobot(updateMap);
			result = "yes";
			HashMap<String, String> pMap = new HashMap<String, String>();
			pMap.put("order_id", order_id);
			pMap.put("cp_id", cp_id);
			if("01".equals(refund_status)){
				pMap.put("order_optlog", user+"点击了【机器退票】车票号："+cp_id);
				logger.info(user+"点击了【机器退票】订单号："+order_id);
			}
			pMap.put("opter", user);
			jdRefundService.addJDRefundlog(pMap);
			
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
	
	//导出excel
	@RequestMapping("/exportrefundexcel.do")
	public String exportRefundExcel(HttpServletRequest request,
			HttpServletResponse response) {
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String jd_order_id = this.getParam(request, "jd_order_id");//京东流水号

		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态

		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		List<String> refund_status = new ArrayList<String>(refund_StatusList);
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("jd_order_id", jd_order_id);
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("refund_status", refund_status);

		
		List<Map<String, String>> reslist = jdRefundService.queryJDRefundExcel(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("refund_seq"));
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("cp_id"));
			String refund_money = String.valueOf(m.get("refund_money"));
			linkedList.add(m.get("create_time"));
			
			String refund_12306_money = String.valueOf(m.get("refund_12306_money"));

			if (refund_money != null)
				linkedList.add("￥" + refund_money);
			else
				linkedList.add(refund_money);
			if (refund_12306_money != null)
				linkedList.add("￥" + refund_12306_money);
			else
				linkedList.add(refund_12306_money);

			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("user_name"));
			String station =m.get("from_station")+"/"+m.get("arrive_station");
			linkedList.add(station);
			list.add(linkedList);
		}

		String title = "火车票退票管理明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票退款管理.xls";
		String[] secondTitles = { "序号", "退款流水号", "订单号", "车票号","创建时间",
				"退款金额", "12306退款金额","12306退款流水号",  "乘客姓名" ,"出发/到达"};
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
	
	
	
	 
}
