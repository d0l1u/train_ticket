package com.l9e.transaction.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.AgentVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;

/**
 * 预订查询
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/query")
public class QueryOrderController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(QueryOrderController.class);
	
	private static final int PAGE_SIZE = 10;//每页显示的条数
	
	@Resource
	private OrderService orderService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryIndex.jhtml")
	public String queryIndex(HttpServletRequest request, 
			HttpServletResponse response){
		return "redirect:queryOrderList.jhtml";
	}
	
	/**
	 * 查询订单列表 yc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderList.jhtml")
	public String queryOrderList(HttpServletRequest request, 
			HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		String create_time=this.getParam(request, "create_time");
		String oneMonthOrder=this.getParam(request, "oneMonthOrder");
		//用来标识用户点击的是哪个状态 ？全部订单、未支付、出票中、出票成功、出票失败、退款结果
		String selectType=this.getParam(request, "selectType");
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		/**
		 * 判断用户选择的订票日期和选择的一个月之外/一个月之内是否冲突,如果冲突，以用户选择的订票日期为准
		 * 比如：今天是4月17号 用户选择了查询一个月之内的订单(oneMonthOrder=0)，选择的订票日期是2月15号(create_time=2014-02-15)
		 * 这时候以订票日期create_time为准,变oneMonthOrder的值为1
		 */
		if(StringUtils.isNotEmpty(create_time)){
			Date date=new Date();
			Calendar calendar = Calendar.getInstance();	
			calendar.setTime(date);		
			calendar.add(Calendar.MONTH, -1);
			date = calendar.getTime();		
			if(date.before(DateUtil.stringToDate(create_time, "yyyy-MM-dd"))){
				//实际选择的查询日期为一个月之内, 选择的下拉框信息为一个月之外，则改变下拉框的值
				if("1".equals(oneMonthOrder)){
					oneMonthOrder="0";
				}
			}else{
				if("0".equals(oneMonthOrder) || "".equals(oneMonthOrder)){
					oneMonthOrder="1";
				}
			}
		}
		
		/**
		 * 分别查询未支付、出票中、出票成功、出票失败、退款结果的订单数量
		 */
		//方法一
		//方法二
		int noPayNum=0;
		int waitingNum=0;
		int successNum=0;
		int failNum=0;
		int refundNum=0;
		Map<String, Object> numMap = new HashMap<String, Object>(3);
		numMap.put("agent_id", agentId);//代理商id
		numMap.put("order_id", order_id);//订单号
		numMap.put("create_time", create_time);//订票日期
		numMap.put("out_ticket_billno", out_ticket_billno);
		if("1".equals(oneMonthOrder)){
			numMap.put("monthOrder", "0");//一个月之外
		}else{
			numMap.put("oneMonthOrder", "0");//一个月之内
		}
		Map<String,Object> map=orderService.queryAgentOrderNum(numMap);
		if(map.get("noPayNum")==null ){
			//避免空指针异常
		}else{
			noPayNum=Integer.valueOf(map.get("noPayNum").toString().trim());
		}
		if(map.get("waitingNum")==null ){
			//避免空指针异常
		}else{
			waitingNum=Integer.valueOf(map.get("waitingNum").toString().trim());
		}
		if(map.get("successNum")==null){
			//避免空指针异常
		}else{
			successNum=Integer.valueOf(map.get("successNum").toString().trim());
		}
		if(map.get("failNum")==null){
			//避免空指针异常
		}else{
			failNum=Integer.valueOf(map.get("failNum").toString().trim());
		}
		if(map.get("refundNum")==null){
			//避免空指针异常
		}else{
			refundNum=Integer.valueOf(map.get("refundNum").toString().trim());
		}
		
		logger.info("得到的代理商订单数量为："+noPayNum+" 未支付："+waitingNum+" 成功： "+successNum+" 失败： "+failNum+" 退款："+refundNum);
		/*******查询未支付，出票中，出票成功，出票失败，退款结果数量结束。********/
		
		
		/***********开始根据用户的选择条件查询**********/
		List<String> statusList=new ArrayList<String>();
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		if("1".equals(oneMonthOrder)){
			paramMap.put("monthOrder", "0");//一个月之外
		}else{
			paramMap.put("oneMonthOrder", "0");//一个月之内
		}
		paramMap.put("order_id", order_id);//订单号
		paramMap.put("agent_id", agentId);//代理商id
		paramMap.put("out_ticket_billno", out_ticket_billno);//12306订单号
		paramMap.put("create_time", create_time);//订票日期
		int count=0;
		if("1".equals(selectType)){
			count=waitingNum;
			//出票中
			statusList.add(TrainConsts.PAY_SUCCESS);
			statusList.add(TrainConsts.EOP_SEND);
			statusList.add(TrainConsts.BOOKING_TICKET);
			statusList.add(TrainConsts.BOOK_SUCCESS);
			statusList.add(TrainConsts.PRE_BOOK);//预约购票
			paramMap.put("order_status", statusList);
		}else if("2".equals(selectType)){
			count=successNum;
			//出票成功
			statusList.add(TrainConsts.OUT_SUCCESS);
			statusList.add(TrainConsts.PS_ING);
			statusList.add(TrainConsts.PS_SUCCESS);
			statusList.add(TrainConsts.PS_FAILURE);
			paramMap.put("order_status", statusList);
		}else if("3".equals(selectType)){
			count=failNum;
			//出票失败
			statusList.add(TrainConsts.OUT_FAIL);
			statusList.add(TrainConsts.CANCLE_BOOK);
			paramMap.put("order_status", statusList);
		}else if("4".equals(selectType)){
			count=refundNum;
			statusList.add(TrainConsts.REFUND_STREAM_PRE_REFUND);
			statusList.add(TrainConsts.REFUND_STREAM_GEZHI);
			statusList.add(TrainConsts.REFUND_STREAM_WAIT_REFUND);
			statusList.add(TrainConsts.REFUND_STREAM_BEGIN_REFUND);
			statusList.add(TrainConsts.REFUND_STREAM_EOP_REFUNDING);
			statusList.add(TrainConsts.REFUND_STREAM_REFUND_FINISH);
			statusList.add(TrainConsts.REFUND_STREAM_REFUSE);
			paramMap.put("refund_status", statusList);
		}else if("5".equals(selectType)){
			count=noPayNum;
			//未支付
			statusList.add(TrainConsts.PRE_ORDER);
			statusList.add(TrainConsts.PAY_FAIL);
			paramMap.put("order_status", statusList);
			paramMap.put("no_pay", "true");
		}else{
			//如果用户从菜单栏上点击我的订单 则赋值0
			selectType="0";
			count = orderService.queryOrderListCount(paramMap);
		}
		
		logger.info("查询出订单" + count + "条");
		//分页
		PageVo page = PageUtil.getInstance().paging(request, PAGE_SIZE, count);
		
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		paramMap.put("refund_before_time",  this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time"));
		paramMap.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));//每页显示的条数
		List<OrderInfo> orderList = null ;
		if("4".equals(selectType)){
			orderList = orderService.queryOrderRefundList(paramMap);
		}else{
			orderList = orderService.queryOrderList(paramMap);
		}
		for(int i=0; i< orderList.size(); i++){
			OrderInfo order = orderList.get(i);
			String orderId = order.getOrder_id();
//			String cpCount = orderService.queryCpCount(orderId).toString();
//			order.setCpCount(cpCount);
			List<Map<String, String>> rsList = orderService.queryRefundStreamList(orderId);
			if(rsList.size()>0){
				Map<String, String> rsMap = rsList.get(0);
				String refund_type = rsMap.get("refund_type");
				String refund_status = rsMap.get("refund_status");
				String refund_time_start = rsMap.get("refund_time_start");
				String refund_time_end = rsMap.get("refund_time_end");
				order.setRefund_status(refund_status);
				order.setRefund_type(refund_type);
				order.setRefund_time_start(refund_time_start);
				order.setRefund_time_end(refund_time_end);
			}
		}
		request.setAttribute("order_id", order_id);
		request.setAttribute("out_ticket_billno", out_ticket_billno);
		request.setAttribute("create_time", create_time);
		if("1".equals(oneMonthOrder)){
			request.setAttribute("oneMonthOrder", oneMonthOrder);
		}else{
			request.setAttribute("oneMonthOrder", "0");
		}
		request.setAttribute("orderStatusMap", TrainConsts.getBookStatus());
		request.setAttribute("rsStatusMap", TrainConsts.getRefundStreamStatus());
		request.setAttribute("orderList", orderList);
		request.setAttribute("isShowList", 1);
		//用来标识显示哪个页面
		request.setAttribute("selectType", selectType);
		//出票中，出票成功，出票失败，退款结果的数量
		request.setAttribute("noPayNum", noPayNum);
		request.setAttribute("waitingNum", waitingNum);
		request.setAttribute("successNum", successNum);
		request.setAttribute("failNum", failNum);
		request.setAttribute("refundNum", refundNum);
//		return "query/orderQuery";
		return "query/orderQueryNew";

	}
	
	/**
	 * 查看订单明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderDetail.jhtml")
	public String queryOrderDetail(HttpServletRequest request, 
			HttpServletResponse response){
		//LoginUserInfo loginUser = this.getLoginUser(request);
		//String agentId = loginUser.getAgentId();
		//AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		//String provinceId = agentVo.getProvince_id();//代理商所属省份的ID
		
		String order_id= this.getParam(request, "order_id");
		Map<String, String> paramMap1 = new HashMap<String, String>();
		paramMap1.put("order_id", order_id);
		String refund_before_time=this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time");
		paramMap1.put("refund_before_time",  refund_before_time);
		paramMap1.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
		OrderInfo orderInfo = orderService.queryOrderInfo2(paramMap1);
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		
		this.queryConcatCpList(request, order_id, false);//查询经过数据处理的车票列表
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		
		if(Double.parseDouble(orderInfo.getServer_pay_money())>0){//SVIP服务费
			request.setAttribute("provinceJishufei", "1");
		}else{
			request.setAttribute("provinceJishufei", "0");
		}
		Map<String, String> orderInfoPssm = orderService.queryOrderInfoPssm(order_id);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("orderInfoPssm", orderInfoPssm);
		request.setAttribute("orderStatusMap", TrainConsts.getBookStatus());
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("psStatusMap", TrainConsts.getPsStatus());
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		request.setAttribute("fromFunc", this.getParam(request, "fromFunc"));//访问来源
		request.setAttribute("refund_before_time", refund_before_time);
		
//		return "query/orderDetail";
		return "query/orderDetailNew";
	}
	
	/**
	 * 查看订单退款信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderRefund.jhtml")
	public String queryOrderRefund(HttpServletRequest request, 
			HttpServletResponse response){
		String order_id= this.getParam(request, "order_id");
		Map<String, String> paramMap1 = new HashMap<String, String>();
		paramMap1.put("order_id", order_id);
		paramMap1.put("refund_before_time",  this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time"));
		paramMap1.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
		OrderInfo orderInfo = orderService.queryOrderInfo2(paramMap1);
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		
		this.queryConcatCpList(request, order_id, true);//查询经过数据处理的车票列表
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("orderInfoPs", orderInfoPs);

		request.setAttribute("orderStatusMap", TrainConsts.getBookStatus());
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("psStatusMap", TrainConsts.getPsStatus());
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		//防止表单重复提交
		String token = Math.random() + order_id + System.currentTimeMillis();
		request.setAttribute("token", token);
		request.getSession().setAttribute("sessionToken", token);
		
		return "query/orderRefund";
	}
	
	/**
	 * 查询经过数据处理的车票列表
	 * @param request
	 * @param order_id
	 * @param addRefund 是否附加退款信息计算
	 */
	private void queryConcatCpList(HttpServletRequest request,
			String order_id, boolean addRefund){
//		System.out.println("############haha########");
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
		List<Map<String, String>> rsList = orderService.queryRefundStreamList(order_id);
		List<String> needCheck = null;//待核验的乘客身份证号
		List<String> optlogList = orderService.queryNeedCheckOptlogList(order_id);//查询有待核验信息的日志
		if(rsList != null && rsList.size()>0){
			for(Map<String, String> rsMap : rsList){
				if(StringUtils.isEmpty(rsMap.get("refund_type"))){
					continue;
				}else if(TrainConsts.REFUND_TYPE_1.equals(rsMap.get("refund_type"))){//用户退款
					for(Map<String, String> detailMap : detailList){
						if(detailMap.get("order_id").equals(rsMap.get("order_id"))
							&& detailMap.get("cp_id").equals(rsMap.get("cp_id"))){
							detailMap.put("refund_status", rsMap.get("refund_status"));
//							System.out.println("############start########");
//							System.out.println(optlogList.size());
							for(int m=0;m<optlogList.size();m++){
//								System.out.println("############start########");
//								System.out.println("optlogList.get(m)="+optlogList.get(m));
//								System.out.println("detailMap.get(user_ids)="+detailMap.get("user_ids"));
								if(optlogList.get(m).indexOf(detailMap.get("user_ids"))!=-1){
									needCheck.add(detailMap.get("user_ids"));
								}
							}
							//拒绝退款则写入退款原因
							if(!StringUtils.isEmpty(rsMap.get("refund_status"))
									&& TrainConsts.REFUND_STREAM_REFUSE.equals(rsMap.get("refund_status"))){
								detailMap.put("our_remark", rsMap.get("our_remark"));
							}
							break;
						}
					}
				}else if(TrainConsts.REFUND_TYPE_3.equals(rsMap.get("refund_type")) //出票失败退款
						|| TrainConsts.REFUND_TYPE_7.equals(rsMap.get("refund_type"))){//取消预约退款
					for(Map<String, String> detailMap : detailList){
						detailMap.put("refund_status", rsMap.get("refund_status"));
					}
					break;
				}
			}
		}
		if(addRefund){
			String isNeedTip = "0";//是否需要上传小票
			//查询退款需要上传小票的时间点 列车发车前3小时
			Map<String, String> timeMap = orderService.querySpecTimeBeforeFrom(order_id);
			String upload_time = timeMap.get("from_time_3");
			//String upload_time = orderService.queryUploadTipTime(order_id);
//			if(!StringUtils.isEmpty(upload_time)){
//				if(DateUtil.stringToDate(upload_time, "yyyy-MM-dd HH:mm:ss").before(new Date())){
//					isNeedTip = "1";
//				}
//			}
			request.setAttribute("isNeedTip", isNeedTip);
			/*
			 * 1、开车前15天，退票时不收取手续费
			 * 2、开车前48小时以上，不足15天，退票时收取票价5%的退票费；
			 * 3、开车前24小时以上、不足48小时的，退票时收取票价10%的退票费；
			 * 4、开车前不足24小时的，退票时收取票价20%退票费。
			 */
			String refund_percent = null;
			double feePercent = 0;
			if(new Date().before(DateUtil.stringToDate(timeMap.get("from_time_15d"), "yyyy-MM-dd"))){
				refund_percent = "0%";
			}else if(new Date().before(DateUtil.stringToDate(timeMap.get("from_time_48"), "yyyy-MM-dd HH:mm:ss"))){
				feePercent = 0.05;
				refund_percent = "5%";
			}else if(new Date().before(DateUtil.stringToDate(timeMap.get("from_time_24"), "yyyy-MM-dd HH:mm:ss"))){
				feePercent = 0.1;
				refund_percent = "10%";
			}else{
				feePercent = 0.2;
				refund_percent = "20%";
			}
	/*			feePercent = 0.2;
			refund_percent = "20%";*/
			
			//计算退票金额
			double refund_money = 0;
			for(Map<String, String> detailMap : detailList){
				double buy_money = Double.parseDouble(detailMap.get("cp_buy_money"));
				double sxf = 0;
				if(feePercent!=0){
					sxf = AmountUtil.quarterConvert(AmountUtil.mul(buy_money, feePercent));//手续费
					sxf = sxf > 2 ? sxf : 2;
				}
				refund_money = AmountUtil.ceil(AmountUtil.sub(buy_money, sxf));
				detailMap.put("cp_refund_money", String.valueOf(refund_money));
				detailMap.put("refund_percent", refund_percent);
			}
		}

		for(Map<String, String> rsMap : rsList){
			String user_name = orderService.selectRefundPassengers(rsMap.get("cp_id"));
			rsMap.put("user_name", user_name);
		}
		request.setAttribute("needCheck", needCheck);
		request.setAttribute("detailList", detailList);
		request.setAttribute("rsList", rsList);
		request.setAttribute("perCpMoney", detailList.get(0).get("cp_pay_money"));//每张车票的面值
		request.setAttribute("rsStatusMap", TrainConsts.getRefundStreamStatus());
		request.setAttribute("refundTypeMap", TrainConsts.getRefundType());
		request.setAttribute("outFailReasonMap", TrainConsts.getOutFailReason());//出票失败原因

	}
	
	/**
	 * 查看订单明细（EOP）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderDetail_no.jhtml")
	public String queryOrderDetail_no(HttpServletRequest request, 
			HttpServletResponse response){
		return queryOrderDetail(request, response);
	}
	
	/**
	 * 查询代理商最近订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryLastestOrderList.jhtml")
	public String queryLastestOrderList(HttpServletRequest request, 
			HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		List<OrderInfo> lastestOrderList = null;
		if(loginUser != null && !StringUtils.isEmpty(loginUser.getAgentId())){
			String agentId = loginUser.getAgentId();
		
			Map<String, Object> paramMap = new HashMap<String, Object>(3);
			paramMap.put("agent_id", agentId);//代理商id
			
			lastestOrderList = orderService.queryLastestOrderList(paramMap);
		}
		request.setAttribute("lastestOrderList", lastestOrderList);
		return "common/lastestOrder";
	}
	
	//进入销售报表查询页面
	@RequestMapping("/querySaleReport.jhtml")
	public String querySaleReport(HttpServletRequest request, HttpServletResponse response){
		logger.info("【进入销售报表查询页面】querySaleReport.jhtml");
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		Date now = theCa.getTime();
		theCa.add(theCa.MONTH, -1); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String begin_time = df.format(date);
		String end_time = df.format(now);
		return "redirect:/query/querySaleReportList.jhtml?begin_time="+begin_time+"&end_time="+end_time;
	}
	
	//查询销售报表
	@RequestMapping("/querySaleReportList.jhtml")
	public String querySaleReportList(HttpServletRequest request, 
			HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();//代理商ID
		String begin_time = this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
		
		/***********开始根据用户的选择条件查询**********/
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("agent_id", agentId);//代理商id
		paramMap.put("begin_time", begin_time);//开始时间
		paramMap.put("end_time", end_time);//结束时间
		
		int count = orderService.querySaleReportListCount(paramMap);
		logger.info("查询出订单" + count + "条");
		//分页
		PageVo page = PageUtil.getInstance().paging(request, PAGE_SIZE, count);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String,Object>> orderList = orderService.querySaleReportList(paramMap);
		
		request.setAttribute("orderList", orderList);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("isShowList", 1);
		return "query/orderSaleReport";
	}
}
