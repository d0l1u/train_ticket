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
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;

/**
 * 预订查询
 *
 */
@Controller
@RequestMapping("/query")
public class QueryOrderController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(QueryOrderController.class);
	
	private static final int PAGE_SIZE = 10;//每页显示的条数
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private UserInfoService userInfoService;
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/myTravel.jhtml")
	public String myTravel(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			return "redirect:queryOrderList.jhtml";
		}
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
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			String user_id = loginUser.getUser_id();
			String user_name = this.getParam(request, "user_name");//乘车人姓名
			String type = this.getParam(request, "type");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("user_id", user_id);
			paramMap.put("user_name", user_name);
			//开车后的停止退票时间(单位为小时)
			paramMap.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
			
			int count = orderService.queryOrderListCount(paramMap);//订单总数
			List<OrderInfo> orderList = orderService.queryOrderList(paramMap);
			logger.info("查询出订单" + count + "条");
			for(int i=0; i< orderList.size(); i++){
				OrderInfo order = orderList.get(i);
				if(order.getOrder_status().equals("44") || order.getOrder_status().equals("45")){
					String orderId = order.getOrder_id();
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
			}
			//分页
//			PageVo page = PageUtil.getInstance().paging(request, PAGE_SIZE, count);
//			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
//			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			Map<String, String> loginMap = userInfoService.queryUserAllInfo(user_id);
			
			request.setAttribute("user_name", user_name);
			request.setAttribute("user_password", loginMap.get("user_password"));//账号密码
			request.setAttribute("user_phone", loginUser.getUser_phone().substring(0, 3)+"****"+loginUser.getUser_phone().substring(7));
			request.setAttribute("orderStatusMap", TrainConsts.getBookStatus());
			request.setAttribute("rsStatusMap", TrainConsts.getRefundStreamStatus());
			request.setAttribute("seatType", TrainConsts.getSeatType());
			request.setAttribute("orderList", orderList);
			request.setAttribute("count", count);
			request.setAttribute("isShowList", 1);
			
			if(type.equals("hcp")){
				return "query/orderQueryList";
			}else{
				return "query/orderQueryNew";
			}
		}
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
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		Map<String, String> fpMap = orderService.queryOrderInfoBxfp(order_id);
		
		this.queryConcatCpList(request, order_id, false);//查询经过数据处理的车票列表
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		if(Float.parseFloat(orderInfo.getBx_pay_money())>0.0){
			request.setAttribute("bx", "yes");
		}else{
			request.setAttribute("bx", "no");
		}
		System.out.println("------------"+orderInfo.getStop_pay_time());
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("fpMap", fpMap);//保险发票
		request.setAttribute("orderStatusMap", TrainConsts.getBookStatus());
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("psStatusMap", TrainConsts.getPsStatus());
		request.setAttribute("refundStatusMap", TrainConsts.getRefundStatus());
//		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		request.setAttribute("fromFunc", this.getParam(request, "fromFunc"));//访问来源
		
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
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		Map<String, String> fpMap = orderService.queryOrderInfoBxfp(order_id);
		
		this.queryConcatCpList(request, order_id, true);//查询经过数据处理的车票列表
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("fpMap", fpMap);//保险发票
		request.setAttribute("orderStatusMap", TrainConsts.getBookStatus());
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("psStatusMap", TrainConsts.getPsStatus());
		request.setAttribute("refundStatusMap", TrainConsts.getRefundStatus());
		//request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
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
				}else if(TrainConsts.REFUND_TYPE_3.equals(rsMap.get("refund_type"))){ //出票失败退款
					for(Map<String, String> detailMap : detailList){
						detailMap.put("refund_status", rsMap.get("refund_status"));
					}
					break;
				}
			}
		}
		if(addRefund){
			//查询退款需要上传小票的时间点 列车发车前3小时
			Map<String, String> timeMap = orderService.querySpecTimeBeforeFrom(order_id);
			/*
			 * ①开车前48小时以上的，退票时收取票价5%的退票费；
			 * ②开车前24小时以上、不足48小时的，退票时收取票价10%的退票费；
			 * ③开车前不足24小时的，退票时收取票价20%退票费。
			 */
			String refund_percent = null;
			double feePercent = 0;
			if(new Date().before(DateUtil.stringToDate(timeMap.get("from_time_48"), "yyyy-MM-dd HH:mm:ss"))){
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
				double sxf = AmountUtil.quarterConvert(AmountUtil.mul(buy_money, feePercent));//手续费
				sxf = sxf > 2 ? sxf : 2;
				refund_money = AmountUtil.ceil(AmountUtil.sub(buy_money, sxf));
				detailMap.put("cp_refund_money", String.valueOf(refund_money));
				detailMap.put("refund_percent", refund_percent);
			}
		}
		for(Map<String, String> rsMap : rsList){
			String user_name = orderService.selectRefundPassengers(rsMap.get("cp_id"));
			rsMap.put("user_name", user_name);
		}
		
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
	
	
	
}
