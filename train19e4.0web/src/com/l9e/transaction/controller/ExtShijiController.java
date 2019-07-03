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
import org.springframework.web.servlet.ModelAndView;

import com.l9e.common.BaseController;
import com.l9e.common.ExcelViewExtAccount;
import com.l9e.common.ExtShijiConsts;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ExtShijiService;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.vo.AgentVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;
/**
 * 对外商户--世纪
 * @author guona
 *
 */
@Controller
@RequestMapping("/extShiji")
public class ExtShijiController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(ExtShijiController.class);
	
	private static final int PAGE_SIZE = 10;//每页显示的条数
	
	@Resource
	private ExtShijiService extShijiService;
	@Resource
	private JoinUsService joinUsService;
	
	/**
	 * 查询订单列表  
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryExtShijiOrderList.jhtml")
	public String queryOrderList(HttpServletRequest request, 
			HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");//订单号
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");//取票单号
		String oneMonthOrder = "0";
		
		//用来标识用户点击的是哪个状态 ？全部订单、未支付、出票中、出票成功、出票失败、退款结果
		String selectType=this.getParam(request, "selectType");
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
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
		if(order_id.trim().length()>0){//根据订单号查询时可以查询所有时间的订单
			numMap.put("order_id", order_id);//若订单号不为空，则根据订单号查询
		}else{
			numMap.put("out_ticket_billno", out_ticket_billno);
			if("1".equals(oneMonthOrder)){
				//numMap.put("monthOrder", "0");//一个月之外
			}else{
				numMap.put("oneMonthOrder", "0");//一个月之内
			}
		}
		
		Map<String,Object> map=extShijiService.queryAgentOrderNum(numMap);
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
		logger.info("得到的代理商【世纪】订单数量为："+noPayNum+" 未支付："+waitingNum+" 成功： "+successNum+" 失败： "+failNum+" 退款："+refundNum);
		/*******查询未支付，出票中，出票成功，出票失败，退款结果数量结束。********/
		
		
		/***********开始根据用户的选择条件查询**********/
		List<String> statusList=new ArrayList<String>();
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("agent_id", agentId);//代理商id  不能为空
		int count=0;
		if(order_id.trim().length()>0){//根据订单号查询时可以查询所有时间的订单
			paramMap.put("order_id", order_id);//若订单号不为空，则根据订单号查询
		}else{//若定单号为空，则根据其他查询条件查询
			if("1".equals(oneMonthOrder)){
				//paramMap.put("monthOrder", "0");//一个月之外
			}else{
				paramMap.put("oneMonthOrder", "0");//一个月之内
			}
			paramMap.put("out_ticket_billno", out_ticket_billno);//12306订单号
			if("1".equals(selectType)){
				//出票中
				statusList.add(ExtShijiConsts.PAY_SUCCESS);//11支付成功
				statusList.add(ExtShijiConsts.EOP_SEND);//12EOP发货
				statusList.add(ExtShijiConsts.BOOKING_TICKET);//22正在预订
				statusList.add(ExtShijiConsts.BOOK_SUCCESS);//33预订成功
				paramMap.put("order_status", statusList);
			}else if("2".equals(selectType)){
				//出票成功
				statusList.add(ExtShijiConsts.OUT_SUCCESS);//44出票成功	
				paramMap.put("order_status", statusList);
			}else if("3".equals(selectType)){
				//出票失败
				statusList.add(ExtShijiConsts.OUT_FAIL);//45出票失败
				paramMap.put("order_status", statusList);
			}else if("4".equals(selectType)){
				//退款状态：00、等待退票 11、同意退票 22、拒绝退票 33、退票完成 44、退票失败
				statusList.add(ExtShijiConsts.REFUND_STREAM_PRE_REFUND);//00 
				statusList.add(ExtShijiConsts.REFUND_STREAM_GEZHI);//99
				statusList.add(ExtShijiConsts.REFUND_STREAM_WAIT_REFUND);//11 
				statusList.add(ExtShijiConsts.REFUND_STREAM_BEGIN_REFUND);//22 
				statusList.add(ExtShijiConsts.REFUND_STREAM_EOP_REFUNDING);//33 
				statusList.add(ExtShijiConsts.REFUND_STREAM_REFUND_FINISH);//44 
				paramMap.put("refund_status", statusList);
			}else if("5".equals(selectType)){
				//未支付
				//statusList.add(ExtShijiConsts.PRE_ORDER);//00未支付
				//statusList.add(ExtShijiConsts.PAY_FAIL);//99支付失败
				//paramMap.put("order_status", statusList);
				//paramMap.put("no_pay", "true");
			}
		}
		if("1".equals(selectType)){
			count=waitingNum;//出票中
		}else if("2".equals(selectType)){
			count=successNum;//出票成功
		}else if("3".equals(selectType)){
			count=failNum;//出票失败
		}else if("4".equals(selectType)){
			count=refundNum;//退款状态：00、等待退票 11、同意退票 22、拒绝退票 33、退票完成 44、退票失败
		}else if("5".equals(selectType)){
			count=noPayNum;
		}else{
			//如果用户从菜单栏上点击我的订单 则赋值0
			selectType="0";
			count = extShijiService.queryOrderListCount(paramMap);
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
			orderList = extShijiService.queryOrderRefundList(paramMap);
		}else{
			orderList = extShijiService.queryOrderList(paramMap);
		}
		for(int i=0; i< orderList.size(); i++){
			OrderInfo order = orderList.get(i);
			String orderId = order.getOrder_id();
			List<Map<String, String>> rsList = extShijiService.queryRefundStreamList(orderId);
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
		if("1".equals(oneMonthOrder)){
			//request.setAttribute("oneMonthOrder", oneMonthOrder);
		}else{
			request.setAttribute("oneMonthOrder", "0");//一个月之内
		}
		request.setAttribute("orderStatusMap", ExtShijiConsts.getBookStatus());
		request.setAttribute("rsStatusMap", ExtShijiConsts.getRefundStreamStatus());
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
		return "extShiji/extShijiQueryNew";

	}
	
	/**
	 * 查看订单明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryExtShijiOrderDetail.jhtml")
	public String queryExtShijiOrderDetail(HttpServletRequest request, 
			HttpServletResponse response){
		String order_id= this.getParam(request, "order_id");
		OrderInfo orderInfo = extShijiService.queryOrderInfo(order_id);
		OrderInfoPs orderInfoPs = extShijiService.queryOrderInfoPs(order_id);
		this.queryConcatCpList(request, order_id, false);//查询经过数据处理的车票列表
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("orderStatusMap", ExtShijiConsts.getBookStatus());
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("psStatusMap", TrainConsts.getPsStatus());
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		request.setAttribute("fromFunc", this.getParam(request, "fromFunc"));//访问来源
		
		return "extShiji/extShijiDetailNew";
	}
	
	/**
	 * 查询经过数据处理的车票列表
	 * @param request
	 * @param order_id
	 * @param addRefund 是否附加退款信息计算
	 */
	private void queryConcatCpList(HttpServletRequest request,
			String order_id, boolean addRefund){
		List<Map<String, String>> detailList = extShijiService.queryOrderDetailList(order_id);
		List<Map<String, String>> rsList = extShijiService.queryRefundStreamList(order_id);
		if(rsList != null && rsList.size()>0){
			for(Map<String, String> rsMap : rsList){
				if(StringUtils.isEmpty(rsMap.get("refund_type"))){
					continue;
				}else if(ExtShijiConsts.REFUND_TYPE_1.equals(rsMap.get("refund_type"))){//用户退款
					for(Map<String, String> detailMap : detailList){
						if(detailMap.get("order_id").equals(rsMap.get("order_id"))
							&& detailMap.get("cp_id").equals(rsMap.get("cp_id"))){
							detailMap.put("refund_status", rsMap.get("refund_status"));
							//拒绝退款则写入退款原因
							if(!StringUtils.isEmpty(rsMap.get("refund_status"))
									&& ExtShijiConsts.REFUND_STREAM_BEGIN_REFUND.equals(rsMap.get("refund_status"))){
								detailMap.put("our_remark", rsMap.get("our_remark"));
							}
							break;
						}
					}
				}else if(ExtShijiConsts.REFUND_TYPE_5.equals(rsMap.get("refund_type"))){//出票失败退款
					for(Map<String, String> detailMap : detailList){
						detailMap.put("refund_status", rsMap.get("refund_status"));
					}
					break;
				}
			}
		}
		if(addRefund){
			String isNeedTip = "0";//是否需要上传小票
			Map<String, String> timeMap = extShijiService.querySpecTimeBeforeFrom(order_id);
			String upload_time = timeMap.get("from_time_3");
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
			String user_name = extShijiService.selectRefundPassengers(rsMap.get("cp_id"));
			rsMap.put("user_name", user_name);
		}
		request.setAttribute("detailList", detailList);
		request.setAttribute("rsList", rsList);
		request.setAttribute("perCpMoney", detailList.get(0).get("cp_pay_money"));//每张车票的面值
		request.setAttribute("rsStatusMap", ExtShijiConsts.getRefundStreamStatus());
		request.setAttribute("refundTypeMap", ExtShijiConsts.getRefundType());
		request.setAttribute("outFailReasonMap", ExtShijiConsts.getOutFailReason());//出票失败原因
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
		return queryExtShijiOrderDetail(request, response);
	}
	
	/**
	 * 	跳转到个人信息页面
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping("/agentExtShijiInfoIndex.jhtml")
	public String gotoAgentInfoPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("跳转到商户信息页面");
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		
		if(agentVo==null){
			//无数据未开通
			request.setAttribute("realPass", "0");
			return "join/joinInput";
		}else{
			//有数据
			request.setAttribute("realPass", "1");
			/**处理代理商的金牌银牌铜牌数据，得到代理商等级*/
			List<Map<String,String>> userInfoList=joinUsService.queryAgentRegisterInfo(agentId);
			request.setAttribute("registList", userInfoList);
			String agentLevel=this.getAgentLevel(userInfoList);
			
			request.setAttribute("agentEstateMap", TrainConsts.getAgentEstate());
			request.setAttribute("agentLevel", agentLevel);
			request.setAttribute("agentVo", agentVo);
			return "extShiji/extShijiAgentInfo";
		}
	}
	/**
	 * 根据代理商的联系人情况，返回他的用户等级，0 铜牌 1 银牌 2 金牌 3未通过 4 审核中 5 未认证
	 * @param request
	 * @param userInfoList
	 * @return
	 */
	private String getAgentLevel(List<Map<String,String>> userInfoList){
		int passNum =0;//已通过数量
		int waitNum=0;//审核中数量
		int noNum=0;//未通过数量
		if(userInfoList!=null && userInfoList.size()>0){
			//弹窗实名标记
			for(int i=0;i<userInfoList.size();i++){
				String registStatus=userInfoList.get(i).get("regist_status").trim();
				if("22".equals(registStatus)){
					passNum=passNum+1;
				}else if("00".equals(registStatus)|| "11".equals(registStatus)||"55".equals(registStatus)||"44".equals(registStatus)){
					waitNum=waitNum+1;
				}else if("33".equals(registStatus)){
					noNum=noNum+1;
				}
			}
		}else{
			logger.info("该代理商还未添加联系人！");
			return "5";
		}
		if(passNum>0){
			if(passNum==1){
				return "0"; //铜牌
			}else if(passNum>=2 && passNum<=4){
				return "1"; //银牌
			}else {
				return "2"; //金牌
			}
		}else if(noNum>0){
			return "3"; //未通过
		}else if(waitNum>0){
			return "4"; //审核中
		}else{
			return "";
		}
	}
	
	//进入查询页面
	@RequestMapping("/queryExtAccountOrder.jhtml")
	public String queryExtAccountOrder(HttpServletRequest request, HttpServletResponse response){
		logger.info("【进入对账管理查询页面】queryExtAccountOrder.jhtml");
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		Date now = theCa.getTime();
		theCa.add(theCa.MONTH, -1); 
		//theCa.add(theCa.DATE, +1); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String begin_time = df.format(date);
		String end_time = df.format(now);
		return "redirect:/extShiji/queryExtAccountOrderList.jhtml?begin_time="+begin_time+"&end_time="+end_time;
	}
	//对账管理
	@RequestMapping("/queryExtAccountOrderList.jhtml")
	public String queryExtAccountOrderList(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();//代理商id
		
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String eop_order_id = this.getParam(request, "eop_order_id");//EOP订单号
		String order_status = this.getParam(request, "order_status");//订单状态
		String refund_status = this.getParam(request, "refund_status");//退款状态
		String begin_time = this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
		String pay_type = this.getParam(request, "pay_type");//支付类型
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("agent_id", agentId);//代理商id
		paramMap.put("order_id", order_id);
		paramMap.put("eop_order_id", eop_order_id);
		//订单状态：44出票成功  45出票失败  00全部
		List<String> order_statusList = new ArrayList<String>();
		//退款类型：1、用户线上退票 2、用户车站退票 3、改签单退款 4、差额退款 5、出票失败退款
		if(order_status.equals("00") || order_status.length()==0){
			order_statusList.add("44");
			order_statusList.add("45");
		}else if(order_status.equals("44")){
			order_statusList.add("44");
		}else if(order_status.equals("45")){
			order_statusList.add("45");
		}
		paramMap.put("order_status", order_statusList);
		//退款状态：00已差额退款(refund_type='4' refund_status='33')  11退款完成(refund_type!='4' refund_status='33')
		List<String> refund_TypeList = new ArrayList<String>();
		//退款类型：1、用户线上退票 2、用户车站退票 3、改签单退款 4、差额退款 5、出票失败退款
		if(refund_status.equals("00")){
			refund_TypeList.add("4");
		}else if(refund_status.equals("11")){
			refund_TypeList.add("1");
			refund_TypeList.add("2");
			refund_TypeList.add("3");
			refund_TypeList.add("5");
		}
		paramMap.put("refund_type", refund_TypeList);
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("pay_type", pay_type);//支付类型：0 钱包 1 支付宝

		/******************分页条件开始********************/
		int totalCount = extShijiService.queryExtAccountOrderListCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<OrderInfo> orderList = extShijiService.queryExtAccountOrderList(paramMap);
		for(int i=0; i< orderList.size(); i++){
			OrderInfo order = orderList.get(i);
			String orderId = order.getOrder_id();
			List<Map<String, String>> rsList = extShijiService.queryRefundList(orderId);//退款完成的退款订单信息
			if(rsList.size()>0){
				Map<String, String> rsMap = rsList.get(0);
				order.setRefund_status(rsMap.get("refund_status"));//退款状态
				order.setRefund_type(rsMap.get("refund_type"));//退款类型
				order.setRefund_time_start(rsMap.get("refund_time_start"));//退款时间
				order.setRefund_time_end(rsMap.get("refund_time_end"));
				Double refund_money = Double.parseDouble(rsMap.get("refund_money"));
				if(rsList.size()>1){//多张车票，累加得到总的退款金额
					for(int j=1;j<rsList.size();j++){
						refund_money += Double.parseDouble(rsList.get(j).get("refund_money"));
					}
				}
				order.setRefund_money(refund_money.toString());//退款金额
			}
		}
		/******************Request绑定开始********************/
		request.setAttribute("orderList", orderList);
		request.setAttribute("orderStatusMap", ExtShijiConsts.getBookStatus());//订单状态
		request.setAttribute("payTypeMap", ExtShijiConsts.getPayType());//支付类型
		request.setAttribute("isShowList", 1);
		request.setAttribute("order_id", order_id);
		request.setAttribute("eop_order_id", eop_order_id);
		request.setAttribute("order_status", order_status);
		request.setAttribute("refund_status", refund_status);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("pay_type", pay_type);
		return "extShiji/extAccountOrderList";
	}
	
	//对外商户对账管理中导出Excel报表
	@RequestMapping("excelexportForExtAccount.jhtml")
	public ModelAndView excelexportForExtAccount(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();//代理商id
		String agentName = loginUser.getAgentName();//代理商名称
		logger.info("代理商："+agentName+",ID为："+agentId+"点击了对账管理中的【报表】");
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String eop_order_id = this.getParam(request, "eop_order_id");//EOP订单号
		String order_status = this.getParam(request, "order_status");//订单状态
		String refund_status = this.getParam(request, "refund_status");//退款状态
		String begin_time = this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
		String pay_type = this.getParam(request, "pay_type");//支付类型
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("agent_id", agentId);//代理商id
		paramMap.put("order_id", order_id);
		paramMap.put("eop_order_id", eop_order_id);
		//订单状态：44出票成功  45出票失败  00全部
		List<String> order_statusList = new ArrayList<String>();
		//退款类型：1、用户线上退票 2、用户车站退票 3、改签单退款 4、差额退款 5、出票失败退款
		if(order_status.equals("00") || order_status.length()==0){
			order_statusList.add("44");
			order_statusList.add("45");
		}else if(order_status.equals("44")){
			order_statusList.add("44");
		}else if(order_status.equals("45")){
			order_statusList.add("45");
		}
		paramMap.put("order_status", order_statusList);
		//退款状态：00已差额退款(refund_type='4' refund_status='33')  11退款完成(refund_type!='4' refund_status='33')
		List<String> refund_TypeList = new ArrayList<String>();
		//退款类型：1、用户线上退票 2、用户车站退票 3、改签单退款 4、差额退款 5、出票失败退款
		if(refund_status.equals("00")){
			refund_TypeList.add("4");
		}else if(refund_status.equals("11")){
			refund_TypeList.add("1");
			refund_TypeList.add("2");
			refund_TypeList.add("3");
			refund_TypeList.add("5");
		}
		paramMap.put("refund_type", refund_TypeList);
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("pay_type", pay_type);//支付类型：0 钱包 1 支付宝

		/******************分页条件开始********************/
		int totalCount = extShijiService.queryExtAccountOrderListCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<OrderInfo> orderList = extShijiService.queryExtAccountOrderExcelList(paramMap);
		for(int i=0; i< orderList.size(); i++){
			OrderInfo order = orderList.get(i);
			String orderId = order.getOrder_id();
			List<Map<String, String>> rsList = extShijiService.queryRefundList(orderId);//退款完成的退款订单信息
			if(rsList.size()>0){
				Map<String, String> rsMap = rsList.get(0);
				order.setRefund_status(rsMap.get("refund_status"));//退款状态
				order.setRefund_type(rsMap.get("refund_type"));//退款类型
				order.setRefund_time(rsMap.get("refund_time"));//退款时间
				Double refund_money = Double.parseDouble(rsMap.get("refund_money"));
				if(rsList.size()>1){//多张车票，累加得到总的退款金额
					for(int j=1;j<rsList.size();j++){
						refund_money += Double.parseDouble(rsList.get(j).get("refund_money"));
					}
				}
				order.setRefund_money(refund_money.toString());//退款金额
			}
		}
		Map map = new HashMap();
		map.put("list",orderList);
		map.put("begin_info_time", begin_time);
		map.put("end_info_time", end_time);
		ExcelViewExtAccount excelViewRefund = new ExcelViewExtAccount();
		return new ModelAndView(excelViewRefund, map);
	}
	
	
	/**
	 * 查询公告列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticeList.jhtml")
	public String queryNoticeList(HttpServletRequest request, 
			HttpServletResponse response){
		try{
			LoginUserInfo loginUser = this.getLoginUser(request);
			String agentId = loginUser.getAgentId();
			AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
			String ext_channel = agentVo.getExt_channel();
			
			List<Map<String, String>> noticeList = commonService.queryExtNoticeList(ext_channel);
			request.setAttribute("noticeList", noticeList);
			return "common/extNoticeList";
		}catch(Exception e){
			logger.info("对外商户查询公告列表异常！",e);
			request.setAttribute("noticeList", null);
			return "common/extNoticeList";
		}
	}
	
	/**
	 * 查询全部公告
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticeAllList.jhtml")
	public String queryNoticeAllList(HttpServletRequest request, 
			HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		String ext_channel = agentVo.getExt_channel();
		
		List<Map<String, String>> noticeList = commonService.queryExtNoticeAllList(ext_channel);
		request.setAttribute("noticeList", noticeList);
		return "common/extNoticeQuery";
	}
	
	/**
	 * 查询公告内容
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryNoticeInfo_no.jhtml")
	public String queryNoticeInfo(HttpServletRequest request, 
			HttpServletResponse response){
		String noticeId = this.getParam(request, "noticeId");
		Map<String, String> noticeInfo = commonService.queryExtNoticeInfo(noticeId);
		request.setAttribute("noticeInfo", noticeInfo);
		return "common/extNoticeInfo";
	}
	
}
