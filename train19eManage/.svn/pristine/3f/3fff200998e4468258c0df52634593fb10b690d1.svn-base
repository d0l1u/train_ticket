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
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.AllRefundService;
import com.l9e.transaction.service.BookService;
import com.l9e.transaction.service.CtripService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.service.SystemSettingService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.service.TrainSystemSettingService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AllRefundVo;
import com.l9e.transaction.vo.ElongVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
/**
 * 退票管理
 * @author liht
 *
 */
@Controller
@RequestMapping("/allRefund")
public class AllRefundController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(AllRefundController.class);
	
	@Resource
	private AllRefundService allRefundService;
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
	@Resource
	private CtripService ctripService;
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
		return "redirect:/allRefund/queryAllRefundList.do?refund_status=03&refund_status=07&refund_status=33&urgent_order="+urgent_order;
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAllRefundList.do")
	public String queryallRefund(HttpServletRequest request,
			HttpServletResponse response){
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
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
		String opt_ren = this.getParam(request, "opt_ren");
		String urgent_order = this.getParam(request, "urgent_order");
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> notify_status = this.getParamToList(request,"notify_status");//通知状态
		List<String> channelList = this.getParamToList(request, "channel");//渠道
		List<String> channel = new ArrayList<String>(channelList);
		if(channel.contains("30101612")){
			channel.add("301016");
			channel.add("30101601");
			channel.add("30101602");
		}
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
		if(refund_status.contains("089")){
			refund_status.add("08");
			refund_status.add("09");
			refund_status.remove("089");
		}
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		if(order_id.trim().length()>0){
		paramMap.put("order_id", order_id);
		}else{
		paramMap.put("refund_seq", refund_seq);
		paramMap.put("refund_12306_seq", refund_12306_seq);
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("opt_ren", opt_ren);
		paramMap.put("notify_status", notify_status);
		paramMap.put("refund_status", refund_status);
		paramMap.put("channel", channel);
		paramMap.put("urgent_order", urgent_order);
		}
		/******************分页条件开始********************/
		int totalCount = allRefundService.queryAllRefundCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> allRefundList = allRefundService.queryAllRefundList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("allRefundList", allRefundList);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("refund_seq", refund_seq);
			request.setAttribute("refund_12306_seq", refund_12306_seq);
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("opt_ren", opt_ren);
			request.setAttribute("notify_status", notify_status.toString());
			request.setAttribute("channelStr", channel.toString());
			request.setAttribute("urgent_order", urgent_order);
		}
		request.setAttribute("refundStatus", AllRefundVo.getRefundStatus());
		request.setAttribute("notifyStatus", AllRefundVo.getNotify_Status());
	


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
//		List<String> manualOrderList = allRefundService.queryManualOrderList();
//		request.setAttribute("manualOrderList", manualOrderList);
		return "allRefund/allRefundList";
	}

	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAllRefundInfo.do")
	public String queryAllRefundInfo(HttpServletRequest request,
			HttpServletResponse response){
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String channelList = this.getParam(request, "channelList");//得到选中渠道
		String pageIndex =this.getParam(request, "pageIndex");
		/******************查询条件********************/
		String refund_seq = this.getParam(request, "refund_seq");
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String isActive = this.getParam(request, "isActive");
		/*********************创建容器*********************/
		Map<String,Object> id_Map = new HashMap<String,Object>();
		id_Map.put("order_id", order_id);
		List<Map<String, String>> cpList = allRefundService.queryAllRefundInfo(id_Map);//查询预订订单车票
		id_Map.put("refund_seq", refund_seq);
		id_Map.put("cp_id", cp_id);
		/******************查询开始********************/
		List<Map<String,String>>ticketInfo = allRefundService.queryAllRefundInfo(id_Map); //查询明细
		Map<String,String> allRefundInfo = ticketInfo.get(0);
		List<Map<String, Object>> history = allRefundService.queryHistroy(id_Map); //查询日志信息
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
		request.setAttribute("allRefundInfo", allRefundInfo);
		request.setAttribute("ticketInfo", ticketInfo);
		request.setAttribute("cpList", cpList);
		request.setAttribute("isActive", isActive);
		request.setAttribute("history", history);
		request.setAttribute("refund_seq", refund_seq);
		request.setAttribute("cp_id", cp_id);
		request.setAttribute("order_id", order_id);
		request.setAttribute("refundStatus", AllRefundVo.getRefundStatus());
		request.setAttribute("ticket_types", AllRefundVo.getTicket_Types());
		request.setAttribute("ids_types", AllRefundVo.getCertificate_type());
		request.setAttribute("seat_types", AllRefundVo.getSeat_type());
		request.setAttribute("query_type", this.getParam(request, "query_type"));
		request.setAttribute("refuse_Reason", AllRefundVo.getRefuseReason());
		request.setAttribute("statusList", statusList);
		request.setAttribute("notifyList", notifyList);
		request.setAttribute("channelList", channelList);
		request.setAttribute("pageIndex", pageIndex);
		request.setAttribute("refund_and_alert", systemSettingService.querySystemRefundAndAlert("refund_and_alert"));
		
		Map<String,String>map = new HashMap<String,String>();
		map.put("order_id", order_id);
		HashMap<String, String> ctripInfo = ctripService.queryCtripAccount(map);
		request.setAttribute("ctripInfo", ctripInfo);
		
		if("55".equals(allRefundInfo.get("refund_type"))){
			map.put("new_cp_id", cp_id);
			Map<String, String> alterInfo = allRefundService.queryAlterInfo(map);
			request.setAttribute("alterInfo", alterInfo);
		}
		return "allRefund/allRefundInfo";
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
		List<Map<String, Object>> history = allRefundService.queryHistroy(id_Map); //查询日志信息
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
		String channel = this.getParam(request, "channel");
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
				allRefundService.addAllRefundlog(map);	//添加日志
				HashMap<String, Object> ordermap = new HashMap<String, Object>();
				ordermap.put("opt_ren", opt_ren);
				ordermap.put("order_id", order_id);
				allRefundService.updateRefundOpt(ordermap);// 更新退款表中操作人信息
			}
			isLock = "";
		} else if (isLock.indexOf(opt_ren) != -1) {
			isLock = "";
		}
//		HashMap<String, Object> ordermap = new HashMap<String, Object>();
//		ordermap.put("opter", opt_ren);
//		ordermap.put("order_id", order_id);
	//	allRefundService.updateOrder(ordermap);// 更新订单表中操作人信息
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
	
	//拒绝退款
	@RequestMapping("/refuse.do")
	public String refuse(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String channelList = this.getParam(request, "channelList");//得到选中渠道
		String pageIndex =this.getParam(request, "pageIndex");
		String order_id = this.getParam(request, "orderId");
		String cp_id = this.getParam(request, "cpId");
		String refundseq = this.getParam(request, "refundseq");
		String our_remark = this.getParam(request, "our_remark");
		String refuse_reason = this.getParam(request, "refuse_reason");
		String alter_diff_money = this.getParam(request,"alter_diff_money");
		String refund_12306_money = this.getParam(request, "refund_12306_money");//12306退款
		
		
		String alter_train_no = this.getParam(request, "alter_train_no_"+cp_id);//改签车次
		String alter_train_box = this.getParam(request, "alter_train_box_"+cp_id);//改签车厢
		String alter_seat_no = this.getParam(request, "alter_seat_no_"+cp_id);//改签座位
		String alter_seat_type = this.getParam(request, "alter_seat_type_"+cp_id);//改签坐席
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(alter_train_no.trim().length()>0 || alter_train_box.trim().length()>0 || alter_seat_no.trim().length()>0 
				|| alter_seat_type.trim().length()>0 ){
			paramMap.put("alter_train_no", alter_train_no);
			paramMap.put("alter_train_box", alter_train_box);
			paramMap.put("alter_seat_no", alter_seat_no);
			Set<String>kset=ElongVo.getSeatType().keySet(); 
			for(String ks : kset){     
				if(alter_seat_type.equals(ElongVo.getSeatType().get(ks))){          
					paramMap.put("alter_seat_type", ks);  
					System.out.println(ks);
					break;
				}else{
					paramMap.put("alter_seat_type", ""); 
				} 
			} 
		}
		String channel = this.getParam(request, "channel");
		if("19e".equals(channel)){
			String refund_total = null ;
			if(!StringUtils.isEmpty(this.getParam(request, "refund_total"))){
				Double refund_money = Double.parseDouble(this.getParam(request, "refund_money"));
				Double refund_total_Double = Double.parseDouble(this.getParam(request, "refund_total"));
				refund_total = (refund_total_Double-refund_money)+"";
				System.out.println("refund_money="+refund_money +"refund_total_Double="+refund_total_Double);
				paramMap.put("refund_total", refund_total);
			}
		}
		
		HashMap<String, String> pMap = new HashMap<String, String>();
		paramMap.put("alter_diff_money", alter_diff_money);
		paramMap.put("refund_12306_money", refund_12306_money);
		paramMap.put("opt_ren", user);
		paramMap.put("order_id", order_id);
		paramMap.put("cp_id", cp_id);
		paramMap.put("our_remark", our_remark);
		paramMap.put("refund_seq", refundseq);
		paramMap.put("refuse_reason", refuse_reason);
		//拒绝退票时，退票金额更新为0
		paramMap.put("refund_money", 0);
		allRefundService.updateRefuse(paramMap);
		Map<String, String> nMap = new HashMap<String, String>();
		nMap.put("cp_id", cp_id);
		nMap.put("opt_ren", user);
		nMap.put("order_id", order_id);
		nMap.put("notify_status", "1");//修改为准备通知
		nMap.put("notify_type", "1");//修改退票结果通知
		try {
			int updateNum = allRefundService.updateNotify_Again(nMap);
			if(updateNum > 0){
				logger.info("修改退款管理退款 通知表 通知结果为 退款结果通知order_id："+order_id);
			}else{
				logger.error("修改退款管理中通知表失败！order_id："+order_id);
			}
		} catch (Exception e) {
			logger.error("修改退款管理中通知表失败！order_id："+order_id+e);
		}
		
		String content = null;
		if(!"301030".equals(channel) && !"meituan".equals(channel) && !"tongcheng".equals(channel)){
			content = user + "点击了【拒绝退款】" + "，退款原因为：【"
			+ AllRefundVo.getRefuseReason().get("OTHER").get(refuse_reason) + "】";
			logger.info(user + "点击了【拒绝退款】" + "，退款原因为：【"
					+ AllRefundVo.getRefuseReason().get("OTHER").get(refuse_reason) + "】");
		}else{
			content = user + "点击了【拒绝退款】" + "，退款原因为：【"
			+ AllRefundVo.getRefuseReason().get(channel).get(refuse_reason) + "】";
			logger.info(user + "点击了【拒绝退款】" + "，退款原因为：【"
					+ AllRefundVo.getRefuseReason().get(channel).get(refuse_reason) + "】");
		}
		pMap.put("order_id", order_id);
		pMap.put("cp_id", cp_id);
		pMap.put("order_optlog", content);
		pMap.put("opter", user);
		pMap.put("order_time", "1");
		allRefundService.addAllRefundlog(pMap);
		logger.info(content);
		
		
		//客服操作记录---拒绝退票
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", user);
		optMap.put("type", "refuse"); 
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
		String str3 = "";
		if("".equals(channelList)||channelList==null){
			str3 = "";
		}else{
			String[] arr3 = channelList.split(",");
			for(int i=0;i<arr3.length;i++){
				str3 += "&channel="+arr3[i];
			}
		}
		return "redirect:/allRefund/queryAllRefundList.do?pageIndex="+pageIndex+str1+str2+str3;
	}
	
	//退款
	@RequestMapping("/refund.do")
	public String refund(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String channelList = this.getParam(request, "channelList");//得到选中渠道
		String pageIndex =this.getParam(request, "pageIndex");
		String orderid = this.getParam(request, "orderId");
		String cp_id = this.getParam(request, "cpId");
		String channel = this.getParam(request, "channel");
		String refundseq = this.getParam(request, "refundseq");
		String refund_status = this.getParam(request, "refund_status");
		String refundmoney = this.getParam(request, "refund_money");
		String our_remark = this.getParam(request, "our_remark");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");
		String alter_diff_money = this.getParam(request,"alter_diff_money");
		String refund_12306_money = this.getParam(request, "refund_12306_money");//12306退款
		
		String alter_train_no = this.getParam(request, "alter_train_no_"+cp_id);//改签车次
		String alter_train_box = this.getParam(request, "alter_train_box_"+cp_id);//改签车厢
		String alter_seat_no = this.getParam(request, "alter_seat_no_"+cp_id);//改签座位
		String alter_seat_type = this.getParam(request, "alter_seat_type_"+cp_id);//改签坐席
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(alter_train_no.trim().length()>0 || alter_train_box.trim().length()>0 || alter_seat_no.trim().length()>0 
				|| alter_seat_type.trim().length()>0 ){
			paramMap.put("alter_train_no", alter_train_no);
			paramMap.put("alter_train_box", alter_train_box);
			paramMap.put("alter_seat_no", alter_seat_no);
			Set<String>kset=ElongVo.getSeatType().keySet(); 
			for(String ks : kset){     
				if(alter_seat_type.equals(ElongVo.getSeatType().get(ks))){          
					paramMap.put("alter_seat_type", ks);  
					System.out.println(ks);
					break;
				}else{
					paramMap.put("alter_seat_type", ""); 
				} 
			} 
		}
		
		paramMap.put("cp_id", cp_id);
		paramMap.put("opt_ren", user);
		paramMap.put("refund_seq", refundseq);
		paramMap.put("order_id", orderid);
		paramMap.put("refund_status", "11");
		paramMap.put("refund_money", refundmoney);
		paramMap.put("our_remark", our_remark);
		paramMap.put("refund_12306_seq", refund_12306_seq);
		paramMap.put("alter_diff_money", alter_diff_money);
		paramMap.put("refund_12306_money", refund_12306_money);
		int count = allRefundService.updateAllRefundInfo(paramMap);//更新为退款完成
		HashMap<String, String> pMap = new HashMap<String, String>();
		if(count>0){
		Map<String, String> nMap = new HashMap<String, String>();
		nMap.put("cp_id", cp_id);
		nMap.put("opt_ren", user);
		nMap.put("order_id", orderid);
		nMap.put("notify_status", "1");//修改为准备通知
		nMap.put("notify_type", "1");//修改退票结果通知
		try {
			int updateNum = allRefundService.updateNotify_Again(nMap);
			if(updateNum > 0){
				logger.info("修改退款管理退款 通知表 通知结果为 退款结果通知orderid："+orderid+",cp_id:"+cp_id);
			}else{
				logger.error("修改退款管理中通知表失败！orderid："+orderid);
			}
		} catch (Exception e) {
			logger.error("修改退款管理中通知表失败！orderid："+orderid+e);
		}
		
		
		String content = "";
		if(refund_status.equals("07")){
			content = user + "点击了人工退票【同意退款】订单号："+orderid+"车票号："+cp_id;
		}else if(refund_status.equals("03")){
			content = user + "点击了人工改签【同意退款】订单号："+orderid+"车票号："+cp_id;
		}else if(refund_status.equals("33")){
			content = user + "点击了审核退款【退款成功】订单号："+orderid+"车票号："+cp_id;
		}else if(refund_status.equals("99")){
			content = user + "点击了搁置订单【同意退款】订单号："+orderid+"车票号："+cp_id;
		}
		pMap.put("order_id", orderid);
		pMap.put("cp_id", cp_id);
		pMap.put("order_optlog", content);
		pMap.put("order_time", "1");
		pMap.put("opter", user);
		allRefundService.addAllRefundlog(pMap);
		logger.info(content);

		//客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", user);
		if(!channel.equals("19e") && !channel.equals("qunar") && !channel.equals("tongcheng") && !channel.equals("elong")&& !channel.equals("meituan")
				&& !channel.equals("tuniu")){
			if(channel.equals("app")|| channel.equals("19trip") ||channel.equals("weixin")){
				channel = "app";
			}else if(channel.equals("cmpay") || channel.equals("cmwap") ||channel.equals("19pay")|| channel.equals("ccb") ||channel.equals("chq") ){
				channel = "inner";
			}else if(channel.equals("301030")){
				channel = "gtgj";
			}else if(channel.equals("301031")){
				channel = "ctrip";
			}else{
				channel = "ext";
			}
		}
		optMap.put("channel", channel);
		optMap.put("all", "refund");
		
		if("33".equals(refund_status)){
			optMap.put("type", "verify");
		}
		
		tj_OpterService.operate(optMap);
		}else{
			pMap.put("order_id", orderid);
			pMap.put("cp_id", cp_id);
			pMap.put("order_optlog", "退款异常，此次点击无效！orderid="+orderid +"cp_id="+cp_id);
			pMap.put("opter", user);
			allRefundService.addAllRefundlog(pMap);
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
		String str2 = "";
		if("".equals(notifyList)||notifyList==null){
			str2 = "";
		}else{
			String[] arr2 = notifyList.split(",");
			for(int i=0;i<arr2.length;i++){
				str2 += "&notify_status="+arr2[i];
			}
		}
		String str3 = "";
		if("".equals(channelList)||channelList==null){
			str3 = "";
		}else{
			String[] arr3 = channelList.split(",");
			for(int i=0;i<arr3.length;i++){
				str3 += "&channel="+arr3[i];
			}
		}
		return "redirect:/allRefund/queryAllRefundList.do?pageIndex="+pageIndex+str1+str2+str3;
	}
	
	//限制金额不大于票面金额减去已退款金额
	@RequestMapping("/queryRefundMoney.do")
	@ResponseBody
	public String queryRefundMoney(HttpServletRequest request ,HttpServletResponse response){
		String orderId = this.getParam(request, "orderId");	
		String refundMoney = this.getParam(request, "refundMoney");
		String cpId=this.getParam(request, "cpId");	
		String channel=this.getParam(request, "channel");	
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", orderId);
		paramMap.put("cp_id", cpId);
		paramMap.put("channel", channel);
		Map<String, String> map_buy = allRefundService.queryRefundMoney(paramMap);
		String flag = null;
			Double pay_money = Double.parseDouble(map_buy.get("pay_money"));//该车票成本
			Double sumRefundMoney = Double.parseDouble(map_buy.get("sumRefundMoney"));//退票表退款总金额 + 各渠道线下退款总金额（包括待退款和已退款）
			Double refund_money = Double.parseDouble(map_buy.get("refund_money"));//本订单退款查询库里退款金额
			if((sumRefundMoney-refund_money)+Double.parseDouble(refundMoney) <= pay_money ){
				flag = map_buy.get("sumRefundMoney");
			}else{
				flag = "no";
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
	
	//重新通知
	@RequestMapping("/notifyAgain.do")
	public String notifyAgain(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获取当前登录人
		
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		//String refundMoney = this.getParam(request, "refundMoney");
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String channelList = this.getParam(request, "channelList");//得到选中渠道
		String pageIndex =this.getParam(request, "pageIndex");
		String content = user + "点击了重新通知按钮,订单号："+order_id+"车票号："+cp_id;
		logger.info(content);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("cp_id", cp_id);
		paramMap.put("opt_ren", user);
		paramMap.put("order_id", order_id);
		paramMap.put("notify_status", "5");//修改为重新通知
		paramMap.put("notify_type", "1");
		try {
			int updateNum = allRefundService.updateNotify_Again(paramMap);
			if(updateNum > 0){
				logger.info(content+"修改通知【成功】order_id:"+order_id+" cp_id:"+cp_id);
			}else{
				logger.error(content+"修改通知【失败】order_id:"+order_id+" cp_id:"+cp_id);
			}
		} catch (Exception e) {
			logger.error(content+"修改通知【失败】order_id:"+order_id+" cp_id:"+cp_id+e);
		}
		HashMap<String, Object> ordermap = new HashMap<String, Object>();
		ordermap.put("opt_ren", user);
		ordermap.put("order_id", order_id);
		ordermap.put("cp_id", cp_id);
		allRefundService.updateRefundOpt(ordermap);// 更新退款表中操作人信息
		HashMap<String, String> pMap = new HashMap<String, String>();
		pMap.put("order_id", order_id);
		pMap.put("cp_id", cp_id);
		pMap.put("order_optlog", content);
		pMap.put("opter", user);
		allRefundService.addAllRefundlog(pMap);
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
		String str3 = "";
		if("".equals(channelList)||channelList==null){
			str3 = "";
		}else{
			String[] arr3 = channelList.split(",");
			for(int i=0;i<arr3.length;i++){
				str3 += "&channel="+arr3[i];
			}
		}
		return "redirect:/allRefund/queryAllRefundList.do?pageIndex="+pageIndex+str1+str2+str3;
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
			updateMap.put("opt_ren", user);
			allRefundService.updateOrderstatusToRobotGai(updateMap);
			result = "yes";
			HashMap<String, String> pMap = new HashMap<String, String>();
			pMap.put("order_id", order_id);
			pMap.put("cp_id", cp_id);
			if("00".equals(refund_status)){
				pMap.put("order_optlog", user+"点击了【机器改签】车票号："+cp_id);
				logger.info(user+"点击了【机器改签】订单号："+order_id);
			}else if("04".equals(refund_status)){
				pMap.put("order_optlog", user+"点击了【机器退票】车票号："+cp_id);
				logger.info(user+"点击了【机器退票】订单号："+order_id);
			}else if("99".equals(refund_status)){
				pMap.put("order_optlog", user+"点击了【搁置订单】车票号："+cp_id);
				logger.info(user+"点击了【搁置订单】订单号："+order_id);
				pMap.put("order_time", "1");
				Map<String, String> nnMap = new HashMap<String, String>();
				nnMap.put("cp_id", cp_id);
				nnMap.put("opt_ren", user);
				nnMap.put("order_id", order_id);
				nnMap.put("notify_status", "1");//修改通知
				nnMap.put("notify_type", "2");//修改通知类型
				try {
					int updateNum = allRefundService.updateNotify_Again(nnMap);
					if(updateNum > 0){
						logger.info(user+"点击了【搁置订单】车票号："+cp_id+"修改通知【成功】");
						//客服操作记录--搁置订单
						Map<String, Object> optMap = new HashMap<String, Object>();
						optMap.put("userName", user); 
						optMap.put("type", "holdon"); 
						logger.info("搁置订单操作记录:"+optMap.get("userName")+","+optMap.get("type"));
						tj_OpterService.operate(optMap);
					}else{
						logger.error(user+"点击了【搁置订单】车票号："+cp_id+"修改通知【失败】");
					}
				} catch (Exception e) {
					logger.error(user+"点击了【搁置订单】车票号："+cp_id+"修改通知【失败】"+e);
				}
			}
			pMap.put("opter", user);
			allRefundService.addAllRefundlog(pMap);
			
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
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String opt_ren = this.getParam(request, "opt_ren");
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> notify_status = this.getParamToList(request,"notify_status");//通知状态
		List<String> channelList = this.getParamToList(request, "channel");//渠道
		List<String> channel = new ArrayList<String>(channelList);
		if(channel.contains("30101612")){
			channel.add("301016");
			channel.add("30101601");
			channel.add("30101602");
		}
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		List<String> refund_status = new ArrayList<String>(refund_StatusList);
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
		if(refund_status.contains("089")){
			refund_status.add("08");
			refund_status.add("09");
			refund_status.remove("089");
		}
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("refund_seq", refund_seq);
		paramMap.put("refund_12306_seq", refund_12306_seq);
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("opt_ren", opt_ren);
		paramMap.put("notify_status", notify_status);
		paramMap.put("refund_status", refund_status);
		paramMap.put("channel", channel);
		
		List<Map<String, String>> reslist = allRefundService.queryAllRefundExcel(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("refund_seq"));
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("cp_id"));
			String refund_money = String.valueOf(m.get("refund_money"));
			linkedList.add(m.get("create_time"));
			if((m.get("verify_time")==null)){
				linkedList.add(m.get("create_time"));
			}else{
				linkedList.add(m.get("verify_time"));
			}
			
			String refund_12306_money = String.valueOf(m.get("refund_12306_money"));
			String alter_diff_money = String.valueOf(m.get("alter_diff_money"));
			if (refund_money != null)
				linkedList.add("￥" + refund_money);
			else
				linkedList.add(refund_money);
			if (refund_12306_money != null)
				linkedList.add("￥" + refund_12306_money);
			else
				linkedList.add(refund_12306_money);
			if (alter_diff_money != null)
				linkedList.add("￥" + alter_diff_money);
			else
				linkedList.add(alter_diff_money);
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("user_name"));
			String station =m.get("from_station")+"/"+m.get("arrive_station");
			linkedList.add(station);
			list.add(linkedList);
		}

		String title = "火车票退票管理明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票退款管理.xls";
		String[] secondTitles = { "序号", "退款流水号", "订单号", "车票号","创建时间", "审核时间",
				"退款金额", "12306退款金额", "改签差价", "12306退款流水号",  "乘客姓名" ,"出发/到达"};
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
	
	
	//批量审核完成
	@RequestMapping("/submitToRefund.do")
	public String submitToRefund(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获得当前登录人姓名
		//获取url查询条件
		logger.info("**********批量审核退款完成开始***********");
		String pageIndex = this.getParam(request, "pageIndex");
		String statusList = this.getParam(request, "statusList");//得到选中的退款状态
		String notifyList = this.getParam(request, "notifyList");//得到选中的通知状态
		String channelList = this.getParam(request, "channelList");//得到选中渠道
		String jsonArr = "["+this.getParam(request, "jsonArr")+"]";
		JSONArray array = JSONArray.fromObject(jsonArr); //转换为json数组
		logger.info("array.size()："+array.size());
		for(int j = 0; j < array.size(); j++){ 
			JSONObject jsonObject = array.getJSONObject(j); 
			String order_id = (String) jsonObject.get("order_id");
			logger.info("orderid："+order_id+"***start");
			String cp_id = (String) jsonObject.get("cp_id");
			String refund_seq = (String) jsonObject.get("refund_seq");
			if(StringUtil.isNotEmpty(order_id)){
				HashMap<String, Object> updateMap = new HashMap<String, Object>();
				updateMap.put("order_id", order_id);
				updateMap.put("cp_id", cp_id);
				updateMap.put("refund_seq", refund_seq);
				updateMap.put("opt_ren", user);
				int count = allRefundService.updateAllRefundInfo(updateMap);//更新为退款完成
				HashMap<String, String> pMap = new HashMap<String, String>();
				if(count>0){
				Map<String, String> nMap = new HashMap<String, String>();
				nMap.put("cp_id", cp_id);
				nMap.put("opt_ren", user);
				nMap.put("order_id", order_id);
				nMap.put("notify_status", "1");//修改为准备通知
				nMap.put("notify_type", "1");//修改退票结果通知
				int updateNum = 0;
				try {
					updateNum = allRefundService.updateNotify_Again(nMap);
					if(updateNum > 0){
						logger.info("修改退款管理退款 通知表 通知结果为 退款结果通知orderid："+order_id);
					}else{
						logger.error("修改退款管理中通知表失败！orderid："+order_id);
					}
				} catch (Exception e) {
					logger.error("修改退款管理中通知表失败！orderid："+order_id+e);
				}
				logger.info("count：<"+count+">updateNum:<"+updateNum+">");
				pMap.put("order_id", order_id);
				pMap.put("cp_id", cp_id);
				pMap.put("order_optlog", user + "点击了【批量审核退款完成】订单号："+order_id+"车票号："+cp_id);
				pMap.put("order_time", "1");
				pMap.put("opter", user);
				allRefundService.addAllRefundlog(pMap);
				logger.info("【批量审核完成】添加操作日志成功订单id为："+order_id+"，操作人为【"+user+"】");
				}
			}
			logger.info("orderid："+order_id+"***end");
		}
		logger.info("############## end ##############");
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
		String str3 = "";
		if("".equals(channelList)||channelList==null){
			str3 = "";
		}else{
			String[] arr3 = channelList.split(",");
			for(int i=0;i<arr3.length;i++){
				str3 += "&channel="+arr3[i];
			}
		}
		return "redirect:/allRefund/queryAllRefundList.do?pageIndex="+pageIndex+str1+str2+str3;
	}
	
	//手机核验
	@RequestMapping("/updateToAccountCheck.do")
	@ResponseBody
	public void updateToAccountCheck(HttpServletResponse response,HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();// 获取当前登录的人
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String refund_seq  = this.getParam(request, "refund_seq");
		
		String result = "no";
		if (StringUtil.isNotEmpty(order_id)
				&&StringUtil.isNotEmpty(cp_id)
				&&StringUtil.isNotEmpty(refund_seq)){//必要参数不为空
			
				Map<String, String> updateMap = new HashMap<String, String>();
				updateMap.put("refund_status", "44");
				updateMap.put("order_id", order_id);
				updateMap.put("cp_id", cp_id);
				updateMap.put("refund_seq", refund_seq);
				updateMap.put("opt_ren", user);
				allRefundService.updateOrderstatusToRobotGai(updateMap);//改到44手机核验
				result = "yes";
			
		}		
		logger.info(user+"点击了【手机核验】订单号："+order_id+"车票号："+cp_id);
		HashMap<String, String> pMap = new HashMap<String, String>();
		pMap.put("order_id", order_id);
		pMap.put("cp_id", cp_id);
		pMap.put("order_optlog", user+"点击了【手机核验】订单号："+order_id+"车票号："+cp_id);
		pMap.put("opter", user);
		allRefundService.addAllRefundlog(pMap);
		
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.error("【手机核验】response返回参数时异常");
			e.printStackTrace();
		}
	}
	
	//人工核验完成
	@RequestMapping("/updateToManualCheck.do")
	@ResponseBody
	public void updateToManualCheck(HttpServletResponse response,HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();// 获取当前登录的人
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");
		String refund_seq  = this.getParam(request, "refund_seq");
		String account_name  = this.getParam(request, "account_name");
		
		String result = "no";
		if (StringUtil.isNotEmpty(order_id)
				&&StringUtil.isNotEmpty(cp_id)
				&&StringUtil.isNotEmpty(refund_seq)){//必要参数不为空
			
				Map<String, String> updateMap = new HashMap<String, String>();
				updateMap.put("refund_status", "04");
				updateMap.put("order_id", order_id);
				updateMap.put("cp_id", cp_id);
				updateMap.put("refund_seq", refund_seq);
				updateMap.put("opt_ren", user);
				allRefundService.updateOrderstatusToRobotGai(updateMap);//改到04
				
				allRefundService.updateAccountToManual(account_name);//改到-2
				result = "yes";
			
		}		
		logger.info(user+"点击了【人工核验完成】订单号："+order_id+"车票号："+cp_id);
		HashMap<String, String> pMap = new HashMap<String, String>();
		pMap.put("order_id", order_id);
		pMap.put("cp_id", cp_id);
		pMap.put("order_optlog", user+"点击了【人工核验完成】订单号："+order_id+"车票号："+cp_id);
		pMap.put("opter", user);
		allRefundService.addAllRefundlog(pMap);
		
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.error("【手机核验】response返回参数时异常");
			e.printStackTrace();
		}
	}
	
	 
}
