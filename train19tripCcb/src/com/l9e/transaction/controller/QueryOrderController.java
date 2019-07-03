package com.l9e.transaction.controller;

import java.util.ArrayList;
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

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.MobileMsgUtil;
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
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
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
	@RequestMapping("/getVerifyCode.jhtml")
	public void getVerifyCode(HttpServletRequest request, HttpServletResponse response){
		String verify_code = CreateIDUtil.createSixNum();
		String user_phone = request.getParameter("user_phone");
		//短信通知用户注册验证码
		String content = "尊敬的用户，您的临时验证码为"+verify_code+"。临时验证码有效时间为90秒，使用后即作废。";
		mobileMsgUtil.send(user_phone, content);
		logger.info("验证码："+verify_code);
		write2Response(response, verify_code);
		return;
	}
	
	/**
	 * 查询订单列表 yc
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/queryOrderList.jhtml")
	public String queryOrderList(HttpServletRequest request, 
			HttpServletResponse response){
		String user_id=this.getParam(request, "user_id");
		String cm_phone=this.getParam(request, "user_id");//游客用手机号登陆，传入的手机号
		String order_id =this.getParam(request, "order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		String start_time=this.getParam(request, "start_time");
		String end_time=this.getParam(request, "end_time");
		//用来标识用户点击的是哪个状态 ？全部订单、出票中、出票成功、出票失败、退款结果
		String selectType=this.getParam(request, "selectType");
		LoginUserInfo loginUser = this.getLoginUser(request);
		logger.info("查询订单,Sid:"+loginUser.getSid()+",UserId:"+loginUser.getUserId()+",CellPhone:"+loginUser.getCellPhone()+",CityName"+loginUser.getCityName()+",ItemNo:"+loginUser.getItemNo());
		if(!StringUtil.isEmpty(user_id)){
			loginUser.setUserId(user_id);
			loginUser.setCellPhone(user_id);
		}
	    
		String userId = loginUser.getUserId();
		cm_phone=loginUser.getCellPhone();
		if("D63D1A292A70CA8E".equals(userId)){
			request.setAttribute("weatherLogin", "false");
			//用来标识显示哪个页面
			request.setAttribute("selectType", "0");
			return "query/orderQueryNew";
		}
		/**
		 * 分别查询出票中、出票成功、出票失败、退款结果的订单数量
		 */
		//方法一
		//方法二
		int waitingNum=0;
		int successNum=0;
		int failNum=0;
		int refundNum=0;
		Map<String, Object> numMap = new HashMap<String, Object>(3);
		numMap.put("user_id", userId);//用户id
		numMap.put("cm_phone", cm_phone);//用户手机号	
		numMap.put("start_time", start_time);//订票开始日期
		numMap.put("end_time", end_time);//订票结束日期
		numMap.put("out_ticket_billno", out_ticket_billno);
		numMap.put("order_id", order_id);
		Map<String,Object> map = orderService.queryAgentOrderNum(numMap);
		Map<String,Object> refundMap = orderService.queryAgentRefundNum(numMap);
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
		if(refundMap.get("refundNum")==null){
			//避免空指针异常
		}else{
			refundNum=Integer.valueOf(refundMap.get("refundNum").toString().trim());
		}
		StringBuffer log = new StringBuffer();
		log.append("用户：").append(userId).append("的订单数量为：").append(waitingNum).append(" 成功： ")
			.append(successNum).append(" 失败： ").append(failNum).append(" 退款：").append(refundNum);
		logger.info(log);
		/*******查询出票中，出票成功，出票失败，退款结果数量结束。********/
		
		
		/***********开始根据用户的选择条件查询**********/
		List<String> statusList=new ArrayList<String>();
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("user_id", userId);//代理商id
		paramMap.put("cm_phone", cm_phone);//登陆传过来的手机号
		paramMap.put("out_ticket_billno", out_ticket_billno);//12306订单号
		paramMap.put("start_time", start_time);
		paramMap.put("end_time", end_time);
		paramMap.put("order_id", order_id);
		int count=0;
		if("1".equals(selectType)){
			count=waitingNum;
			//出票中
			statusList.add(TrainConsts.PAY_SUCCESS);
			statusList.add(TrainConsts.EOP_SEND);
			statusList.add(TrainConsts.BOOKING_TICKET);
			statusList.add(TrainConsts.BOOK_SUCCESS);
			paramMap.put("order_status", statusList);
		}else if("2".equals(selectType)){
			count=successNum;
			//出票成功
			statusList.add(TrainConsts.OUT_SUCCESS);
			paramMap.put("order_status", statusList);
		}else if("3".equals(selectType)){
			count=failNum;
			//出票失败
			statusList.add(TrainConsts.OUT_FAIL);
			paramMap.put("order_status", statusList);
		}else if("4".equals(selectType)){
			count=refundNum;
			statusList.add(TrainConsts.REFUND_STREAM_PRE_REFUND);
			statusList.add(TrainConsts.REFUND_STREAM_WAIT_REFUND);
			statusList.add(TrainConsts.REFUND_STREAM_BEGIN_REFUND);
			statusList.add(TrainConsts.REFUND_STREAM_EOP_REFUNDING);
			statusList.add(TrainConsts.REFUND_STREAM_REFUND_FINISH);
			statusList.add(TrainConsts.REFUND_STREAM_REFUSE);
			paramMap.put("refund_status", statusList);
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
		
		List<OrderInfo> orderList = orderService.queryOrderList(paramMap);
		
		request.setAttribute("out_ticket_billno", out_ticket_billno);
		request.setAttribute("start_time", start_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("orderStatusMap", TrainConsts.getBookStatus());
		request.setAttribute("rsStatusMap", TrainConsts.getRefundStreamStatus());
		request.setAttribute("orderList", orderList);
		request.setAttribute("isShowList", 1);
		//用来标识显示哪个页面
		request.setAttribute("selectType", selectType);
		//出票中，出票成功，出票失败，退款结果的数量
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
		String order_id= this.getParam(request, "order_id");
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		
		this.queryConcatCpList(request, order_id, false);//查询经过数据处理的车票列表
		
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
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
		List<Map<String, String>> rsList = orderService.queryRefundStreamList(order_id);
		double feePercent = 0;
		if(addRefund){
			String isNeedTip = "0";//是否需要上传小票
//			//查询退款需要上传小票的时间点 列车发车前3小时
			Map<String, String> timeMap = orderService.querySpecTimeBeforeFrom(order_id);
//			String upload_time = timeMap.get("from_time_3");
//			//String upload_time = orderService.queryUploadTipTime(order_id);
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
//			feePercent = 0.2;
//			refund_percent = "20%";
			
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
		
		if(rsList != null && rsList.size()>0){
			for(Map<String, String> rsMap : rsList){
				if(StringUtils.isEmpty(rsMap.get("refund_type"))){
					continue;
				}else if(TrainConsts.REFUND_TYPE_1.equals(rsMap.get("refund_type"))){//用户退款
					for(Map<String, String> detailMap : detailList){
						if(detailMap.get("order_id").equals(rsMap.get("order_id"))
							&& detailMap.get("cp_id").equals(rsMap.get("cp_id"))){
							//写入手续费
							double buy_money = Double.parseDouble(detailMap.get("cp_buy_money"));
							double sxf = AmountUtil.quarterConvert(AmountUtil.mul(buy_money, feePercent));//手续费
							sxf = sxf > 2 ? sxf : 2;
							rsMap.put("sxf", String.valueOf(sxf));
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
		if(loginUser != null && !StringUtils.isEmpty(loginUser.getUserId())){
			String user_id = loginUser.getUserId();
		
			Map<String, Object> paramMap = new HashMap<String, Object>(3);
			paramMap.put("user_id", user_id);//代理商id
			
			lastestOrderList = orderService.queryLastestOrderList(paramMap);
		}
		request.setAttribute("lastestOrderList", lastestOrderList);
		return "common/lastestOrder";
	}
	

}
