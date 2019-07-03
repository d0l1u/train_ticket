package com.l9e.transaction.controller;

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
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.WeekDay;
import com.l9e.util.AmountUtil;
import com.l9e.util.DateUtil;

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
		return "query/orderQuery";
	}
	
	/**
	 * 查询订单列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderList.jhtml")
	public String queryOrderList(HttpServletRequest request, 
			HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String cm_phone = loginUser.getCm_phone();//loginUser.getCm_phone();
		
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("order_id", order_id);//订单号
		paramMap.put("cm_phone", cm_phone);//代理商id
		paramMap.put("out_ticket_billno", out_ticket_billno);//12306订单号
		//int count = orderService.queryOrderListCount(paramMap);
		
		//logger.info("查询出订单" + count + "条");
		//分页
		//PageVo page = PageUtil.getInstance().paging(request, PAGE_SIZE, count);
		
		//paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		//paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<OrderInfo> orderList = orderService.queryOrderList(paramMap);
		for(int i=0;i<orderList.size();i++){
			OrderInfo order = orderList.get(i);
			String orderId = order.getOrder_id();
			List<String> passengerList = orderService.queryPassengerList(orderId);
			order.setPassengerList(passengerList);
		}
		request.setAttribute("order_id", order_id);
		request.setAttribute("out_ticket_billno", out_ticket_billno);
		request.setAttribute("orderStatusMap", TrainConsts.getBookStatus());
		request.setAttribute("rsStatusMap", TrainConsts.getRefundStreamStatus());
		request.setAttribute("orderList", orderList);
		request.setAttribute("isShowList", 1);
		return "query/orderQuery";
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
		String order_id= this.getParam(request, "order_id");
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		System.out.println(orderInfo.getRefund_status()+"+++"+orderInfo.getOrder_status());
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		
		this.queryConcatCpList(request, order_id, false);//查询经过数据处理的车票列表
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		String travel_time = orderInfo.getTravel_time();
		int day = DateUtil.getDay(travel_time, "yyyy-MM-dd");
		String dayOfWeek = WeekDay.getDay(day);
		request.setAttribute("day", dayOfWeek);
		request.setAttribute("orderInfo", orderInfo);
		logger.info(request.getAttribute("detailList"));
		request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("orderStatusMap", TrainConsts.getBookStatus());
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("psStatusMap", TrainConsts.getPsStatus());
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		request.setAttribute("fromFunc", this.getParam(request, "fromFunc"));//访问来源
		
		return "query/orderDetail";
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
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		
		this.queryConcatCpList(request, order_id, true);//查询经过数据处理的车票列表
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		String travel_time = orderInfo.getTravel_time();
		int day = DateUtil.getDay(travel_time, "yyyy-MM-dd");
		String dayOfWeek = WeekDay.getDay(day);
		request.setAttribute("day", dayOfWeek);
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
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
		List<Map<String, String>> rsList = orderService.queryRefundStreamList(order_id);
		if(rsList != null && rsList.size()>0){
			for(Map<String, String> rsMap : rsList){
				if(StringUtils.isEmpty(rsMap.get("refund_type"))){
					continue;
				}else if(TrainConsts.REFUND_TYPE_1.equals(rsMap.get("refund_type"))){//用户退款
					for(Map<String, String> detailMap : detailList){
						if(detailMap.get("order_id").equals(rsMap.get("order_id"))
							&& detailMap.get("cp_id").equals(rsMap.get("cp_id"))){
							detailMap.put("refund_status", rsMap.get("refund_status"));
							//拒绝退款则写入退款原因
							if(!StringUtils.isEmpty(rsMap.get("refund_status"))
									&& TrainConsts.REFUND_STREAM_REFUSE.equals(rsMap.get("refund_status"))){
								detailMap.put("our_remark", rsMap.get("our_remark"));
							}
							break;
						}
					}
				}else if(TrainConsts.REFUND_TYPE_3.equals(rsMap.get("refund_type"))){//出票失败退款
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
			if(!StringUtils.isEmpty(upload_time)){
				if(DateUtil.stringToDate(upload_time, "yyyy-MM-dd HH:mm:ss").before(new Date())){
					isNeedTip = "1";
				}
			}
			request.setAttribute("isNeedTip", isNeedTip);
			
			
			/*
			 * 1、开车前15天，退票时不收取手续费
			 * 2、开车前48小时以上，不足15天，退票时收取票价5%的退票费；
			 * 3、开车前24小时以上、不足48小时的，退票时收取票价10%的退票费；
			 * 4、开车前不足24小时的，退票时收取票价20%退票费。
			 */
			String refund_percent = null;
			double feePercent = 0;
			if("2015-02-04".compareTo(timeMap.get("from_time_15d"))<0 
					&& "2015-03-15".compareTo(timeMap.get("from_time_15d"))>0){
				feePercent = 0.2;
				refund_percent = "20%";
			}else{
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
			}
			
			//计算退票金额
			double refund_money = 0;
			for(Map<String, String> detailMap : detailList){
				System.out.println(detailMap.get("cp_buy_money"));
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
		
		request.setAttribute("detailList", detailList);
		
		if(rsList!=null && rsList.size()>0){
			request.setAttribute("rsList", rsList.get(0));
		}else{
			request.setAttribute("rsList", rsList);
		}
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
		if(loginUser != null && !StringUtils.isEmpty(loginUser.getCm_phone())){
			String cm_phone = loginUser.getCm_phone();
		
			Map<String, Object> paramMap = new HashMap<String, Object>(3);
			paramMap.put("cm_phone", cm_phone);//代理商id
			
			lastestOrderList = orderService.queryLastestOrderList(paramMap);
		}
		request.setAttribute("lastestOrderList", lastestOrderList);
		return "common/lastestOrder";
	}
	

}
