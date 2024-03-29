package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.sign.PayResultSign;
import com.jiexun.iface.sign.RefundResultSign;
import com.jiexun.iface.sign.SignService;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.AgentVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.RobTicketVo;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.MobileMsgUtil;
import com.l9e.util.RobTicketUtils;

@Controller
@RequestMapping("/receiveNotify")
public class ReceiveNotifyController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(ReceiveNotifyController.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private JoinUsService joinUsService;
	
	@Resource
	private ReceiveNotifyService receiveNotifyService;
	
	@Resource
	private UserInfoService userInfoService;
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
	@Resource
	private RobTicketController robTicketController;
	
	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;//验签key
	
	@Resource
	private RobTicketService robTicketService;
	
	public static final Object LOCK = new Object();
	
	/**
	 * 接收支付结果后台通知
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/payResultBackNotify_no.jhtml")
	public void payResultBackNotify(HttpServletRequest request,
			HttpServletResponse response){
		Map<String, String> resMap = new HashMap<String, String>();
		String asp_order_type = this.getParam(request, "asp_order_type");//订单类别
		
		SignService ss = new PayResultSign();
		ASPUtil.sign(request, asp_verify_key, ss);
		
		//验签成功
		if(!StringUtils.isEmpty(ss.getSignResult())
				&& "SUCCESS".equalsIgnoreCase(ss.getSignResult())){
			PayResultSign ps = (PayResultSign)ss;
			Map<String, String> eopInfo = new HashMap<String, String>();
			eopInfo.put("send_notify_url", ps.getSend_notify_url());//ASP发货完成后通知EOP的地址
			eopInfo.put("eop_refund_url", ps.getEop_refund_url());//ASP向EOP请求退款的地址
			eopInfo.put("pay_time", ps.getPay_time());//支付时间

			if(StringUtils.isEmpty(ps.getPay_status())){//异常
				logger.info("【接收支付结果后台通知】通知异常支付结果为空，EOP订单号" + ps.getEop_order_id());
				resMap.put("result_code", "FAIL");
				resMap.put("result_desc", "支付结果为空");
				JSONObject json = JSONObject.fromObject(resMap); 
				write2Response(response, json.toString());
				return;
				
			}else if("SUCCESS".equalsIgnoreCase(ps.getPay_status())){
				logger.info("【接收支付结果后台通知】EOP订单号" + ps.getEop_order_id() + "支付成功");
				if("hc".equals(asp_order_type)){//火车票订单
					//eopInfo.put("old_status", TrainConsts.PRE_ORDER);//订单原来的状态
					eopInfo.put("old_status", null);
					eopInfo.put("order_status", TrainConsts.PAY_SUCCESS);//支付成功
					eopInfo.put("asp_order_id", ps.getAsp_order_id());
					
					// 抢票 订单 支付通知逻辑
					if(ps.getAsp_order_id().indexOf("HC_ROB")!=-1){
						RobTicket_OI oi = new RobTicket_OI();
						oi.setOrderId(ps.getAsp_order_id());
						try {
							robTicketService.updateAfterPay(oi,ps.getPay_status(),eopInfo);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						resMap.put("result_code", "SUCCESS");
						resMap.put("result_desc", "支付通知接收成功");
						JSONObject json = JSONObject.fromObject(resMap); 
						write2Response(response, json.toString());
						return ;
					}
					
					
					orderService.updateOrderEopInfo(eopInfo);//更新订单信息
					
					//回复支付接口
					resMap.put("result_code", "SUCCESS");
					resMap.put("result_desc", "支付通知接收成功");
					JSONObject json = JSONObject.fromObject(resMap); 
					write2Response(response, json.toString());
					
					//this.sendVipPaySuccMsn(ps.getAsp_order_id());//发送vip支付成功短信
					
				}else{//加盟订单
					logger.info("【接收支付结果后台通知-加盟】EOP订单号" + ps.getEop_order_id() + "支付成功");
					String agentId = this.getParam(request, "asp_agent_id");//代理商id
					String productId = this.getParam(request, "asp_product_id");
					eopInfo.put("user_id", agentId);//代理商id
					eopInfo.put("product_id", productId);
					eopInfo.put("old_status", "00");//订单原来的状态
					eopInfo.put("order_status", "11");//支付成功
					eopInfo.put("asp_order_id", ps.getAsp_order_id());
					eopInfo.put("asp_order_type", asp_order_type);//（jm:加盟订单 jmxf_sys:主动续费 jmxf_hum:被动续费）
					joinUsService.updateJmOrderEopInfo(eopInfo);//更新订单信息
					
					resMap.put("result_code", "SUCCESS");
					resMap.put("result_desc", "支付通知接收成功");
					JSONObject json = JSONObject.fromObject(resMap); 
					write2Response(response, json.toString());
				}
				//eopInfo.put("pay_amount", ps.getPay_amount());//支付总金额
			}else if("FAIL".equalsIgnoreCase(ps.getPay_status()) || "TRADE_MONEY_LACK".equalsIgnoreCase(ps.getPay_status())){
				logger.info("【接收支付结果后台通知】EOP订单号" + ps.getEop_order_id() + "支付失败，失败原因：" + ps.getReason());
				if("TRADE_MONEY_LACK".equalsIgnoreCase(ps.getPay_status())){
					logger.info("【接收支付结果后台通知】EOP订单号" + ps.getEop_order_id() + "交易类代理商钱包余额不足"+ps.getPay_status());
				}
				if("hc".equals(asp_order_type)){//火车票订单
					eopInfo.put("old_status", TrainConsts.PRE_ORDER);//订单原来的状态
					eopInfo.put("order_status", TrainConsts.PAY_FAIL);//支付失败
					eopInfo.put("asp_order_id", ps.getAsp_order_id());
					orderService.updateOrderEopInfo(eopInfo);//更新订单信息
					
					resMap.put("result_code", "SUCCESS");
					resMap.put("result_desc", "支付通知接收成功");
					JSONObject json = JSONObject.fromObject(resMap); 
					write2Response(response, json.toString());
				}else{//加盟订单
					logger.info("【接收支付结果后台通知-加盟】EOP订单号" + ps.getEop_order_id() + "支付失败，失败原因：" + ps.getReason());
					eopInfo.put("old_status", "00");//订单原来的状态
					eopInfo.put("order_status", "22");//支付失败
					eopInfo.put("asp_order_id", ps.getAsp_order_id());
					joinUsService.updateJmOrderEopInfo(eopInfo);//更新订单信息
					
					resMap.put("result_code", "SUCCESS");
					resMap.put("result_desc", "支付通知接收成功");
					JSONObject json = JSONObject.fromObject(resMap); 
					write2Response(response, json.toString());
				}
			}else{
				logger.error("【接收支付结果后台通知】EOP订单号" + ps.getEop_order_id()+"未知支付状态，请核实！");
				resMap.put("result_code", "FAIL");
				resMap.put("result_desc", "未知支付状态");
				JSONObject json = JSONObject.fromObject(resMap); 
				write2Response(response, json.toString());
			}

		}else{
			resMap.put("result_code", "FAIL");
			resMap.put("result_desc", "验签失败");
			JSONObject json = JSONObject.fromObject(resMap); 
			write2Response(response, json.toString());
		}
	}
	
	
	/**
	 * 接收支付结果前台通知
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/payResultFrontNotify_no.jhtml")
	public String payResultFrontNotify(HttpServletRequest request,
			HttpServletResponse response){
		String pay_status = this.getParam(request, "pay_status");//支付结果
		String asp_order_id = this.getParam(request, "asp_order_id");//asp订单号
		if(asp_order_id.indexOf("HC_ROB")!=-1){
			return robTicketController.payResultAction(request, response);
		}
		Map<String, String> eopInfo = commonService.queryEopInfo(asp_order_id);
		
		request.setAttribute("eop_order_id", eopInfo.get("eop_order_id"));//eop订单号
		request.setAttribute("pay_status", pay_status);
		
		String asp_order_type = this.getParam(request, "asp_order_type");//订单类别
		
		if("hc".equals(asp_order_type)){//火车票订单
			logger.info("【接收支付结果前台通知】接收前台支付通知成功，ASP订单号" + asp_order_id);
			//return "redirect:/query/queryOrderDetail.jhtml?fromFunc=payNotify&order_id=" + asp_order_id;//跳转至本订单详情页面
			return "redirect:/query/queryOrderList.jhtml";//跳转至我的订单页面
		}else{//加盟订单
			logger.info("【接收支付结果前台通知-加盟】通知异常支付结果为空，ASP订单号" + asp_order_id);
			String agentId = this.getParam(request, "asp_agent_id");//代理商id
			String productId = this.getParam(request, "asp_product_id");//加盟产品id
			ProductVo productVo = new ProductVo();
			productVo.setProduct_id(productId);
			List<ProductVo> productList = commonService.queryProductInfoList(productVo);
			
			AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
			
			request.setAttribute("productVo", productList.get(0));
			request.setAttribute("saleTypeMap", TrainConsts.getSaleType());//计费方式
			request.setAttribute("agentEstateMap", TrainConsts.getAgentEstate());//审核状态
			request.setAttribute("agentVo", agentVo);//代理商信息
			request.setAttribute("pay_status", pay_status);
			return "join/verifying";
		}
	}
	
	/**
	 * 接收退款结果通知
	 * @return
	 */
	@RequestMapping("/refundResultNotify_no.jhtml")
	public void refundResultNotify(HttpServletRequest request,
			HttpServletResponse response){
		
		JSONObject json= new JSONObject();
		Map<String, String> eopInfo = new HashMap<String, String>();
		
		SignService ss = new RefundResultSign();
		ASPUtil.sign(request, asp_verify_key, ss);
		//添加操作日志
		Map<String, String> logMap = new HashMap<String, String>();
		
		if("SUCCESS".equals(ss.getSignResult())){
			RefundResultSign rs = (RefundResultSign)ss;
			Map<String, String> eop = null;
			try {
				eop = robTicketService.queryEOPByEopId(rs.getEop_order_id());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String string = eop.get("order_id");
			
			if(string.indexOf("HC_ROB")!=-1){
				// 抢票 订单 退款 返回消息;
				RobTicketUtils.refundNotifyBiz(rs,request,response,string,robTicketService);
				return ;
			}
			Map<String,String> eop_map = new HashMap<String,String>();
			eop_map.put("eop_order_id", rs.getEop_order_id());
			eop_map.put("refund_seq", rs.getAsp_refund_number());
			Map<String,String> order_map = commonService.queryOrderIdByEopId(eop_map);//根据EOP订单号查询ASP订单号
			String order_id = order_map.get("order_id");
			logMap.put("order_id", order_id);
			logMap.put("cp_id", order_map.get("cp_id"));
			logMap.put("opter", "19e");
			if(StringUtils.isEmpty(rs.getRefund_status())){//异常
				logger.info("【接收退款结果通知接口】通知异常退款结果为空，EOP订单号" + rs.getEop_order_id());
				logMap.put("order_optlog", "接收eop退款结果异常为空");
				
				json.put("result_code", "FAIL");
				json.put("result_desc", "退款结果为空");
				orderService.addOrderOptLog(logMap);
				write2Response(response, json.toString());
			}else if("SUCCESS".equalsIgnoreCase(rs.getRefund_status())){//退款成功
				String asp_order_type = this.getParam(request, "asp_order_type");//订单类别
				
				if("hc".equals(asp_order_type)){//火车票订单
					logger.info("【接收退款结果通知接口】ASP订单号" + order_id + "退款成功");
					logMap.put("order_optlog", "接收eop退款结果：退款成功");
					eopInfo.put("order_id", order_id);
					eopInfo.put("eop_refund_time", rs.getEop_refund_time());//退款完成时间
					eopInfo.put("refund_seq", rs.getAsp_refund_number());//ASP退款请求流水号
					eopInfo.put("eop_refund_seq", rs.getEop_refund_seq());//EOP退款流水号
					eopInfo.put("refund_status", TrainConsts.REFUND_STREAM_REFUND_FINISH);//退款成功
//					eopInfo.put("old_refund_status", TrainConsts.REFUND_STREAM_EOP_REFUNDING);//EOP退款中
					orderService.updateOrderStreamStatus(eopInfo);
					logger.info("添加退款结果日志！"+order_id+";cp_id:"+order_map.get("cp_id")); 
					orderService.addOrderOptLog(logMap);
				}else{//加盟订单
					logMap.put("order_optlog", "接收eop退款结果：退款成功");
					eopInfo.put("order_status", "44");//退款成功
					eopInfo.put("eop_refund_time", rs.getEop_refund_time());//退款完成时间
					eopInfo.put("old_status", "34");//EOP正在退款
					eopInfo.put("asp_order_id", order_id);
					joinUsService.updateJmOrderEopInfo(eopInfo);//更新加盟订单信息
					orderService.addOrderOptLog(logMap);
				}
				json.put("result_code", "SUCCESS");
				json.put("result_desc", "退款通知接收成功");
				
				logger.info("反馈eop退款结果通知！"+order_id); 
				write2Response(response, json.toString());
			}else{
				logMap.put("order_optlog", "接收eop退款结果错误");
				json.put("result_code", "FAIL");
				json.put("result_desc", "退款结果错误");
				orderService.addOrderOptLog(logMap);
				logger.info("【接收退款结果通知接口】EOP订单号" + rs.getEop_order_id() + "退款失败");
				write2Response(response, json.toString());
			}

		}else{
			logMap.put("order_optlog", "接收eop退款结果错误：验签失败");
			json.put("result_code", "FAIL");
			json.put("result_desc", "验签失败");
			orderService.addOrderOptLog(logMap);
			write2Response(response, json.toString());
		}
	}
	
	/**
	 * 接收出票结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/cpNotify_no.jhtml")
	public void cpNotify(HttpServletRequest request,
			HttpServletResponse response){
		String result = this.getParam(request, "result");
		String orderId = this.getParam(request, "orderid");
		String billNo = this.getParam(request, "billno");
		String buyMoney = this.getParam(request, "buymoney");
		String seatTrains = this.getParam(request, "seattrains");
		String status = this.getParam(request, "status");//00：出票成功 11预定成功
		String level = this.getParam(request, "level");//vip用户不为空
		String passengers = this.getParam(request, "passengers");//乘客审核信息
		//CP1403021140261036|西红柿|110101198406079315|0#CP1403021140261038|海龟派|110101198406079235|1#CP1403021140261040|想不通|110101198810014951|0
		
		
		logger.info("[接收出票结果通知接口]参数orderId=" + orderId 
				+ "，result=" + result + "，billNo=" + billNo
				+ "，buyMoney=" + buyMoney + "，seatTrains=" + seatTrains + "，status=" + status+",level="+level);
		
		logger.info("order_id="+orderId+"&passengers="+passengers);
		
		Map<String, String> paramMap = new HashMap<String, String>(3);//主订单参数
		List<Map<String, String>> cpMapList = new ArrayList<Map<String, String>>();//车票订单参数
		
		if(StringUtils.isEmpty(result) || StringUtils.isEmpty(orderId)){
			//参数为空
			logger.info("exception：参数为空！");
			write2Response(response, "failed");
			
		}else if(TrainConsts.SUCCESS.equalsIgnoreCase(result)){//成功
			if(StringUtils.isEmpty(billNo) || StringUtils.isEmpty(buyMoney) 
					|| StringUtils.isEmpty(status) || StringUtils.isEmpty(seatTrains)){
				//参数为空
				logger.info("exception：明细参数为空！");
				write2Response(response, "failed");
				return;
			}else if(!"11".equals(status) && !"00".equals(status)){
				logger.info("exception：状态有误！status="+status);
				write2Response(response, "failed");
				return;
			}
			
			//查询订单信息
			Map<String, String> paramMap1 = new HashMap<String, String>();
			paramMap1.put("order_id", orderId);
			paramMap1.put("refund_before_time",  this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time"));
			paramMap1.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
			OrderInfo orderInfo = orderService.queryOrderInfo2(paramMap1);
			if(StringUtils.isEmpty(orderInfo.getOrder_status())){
				logger.info("exception：订单查询异常，订单状态为空！");
				write2Response(response, "failed");
				return;
			}
			
	
			
			if("11".equals(status)){//预订成功
				//预订成功通知重复通知
				if(!TrainConsts.BOOKING_TICKET.equals(orderInfo.getOrder_status())){
					logger.info("[接收出票结果通知接口]本次预订成功通知为重复通知，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				paramMap.put("order_id", orderId);
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.BOOK_SUCCESS);//预订成功
				
				//明细数据处理
				this.detailDataPacking(seatTrains, cpMapList, response);

				int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList, null);
				if (count == 1){
					write2Response(response, "success");
					if(StringUtils.isEmpty(level) || "0".equals(level)){
						//发送预订成功短信
						//this.sendYdSuccMsn(orderId, billNo);
					}else{
						//发送vip订购短信
						//this.sendVipSuccMsn(orderId, billNo);
					}
					
				}else{
					logger.info("failed：修改订单" + orderId + "失败！");
					write2Response(response, "failed");
				}
				
			}else if("00".equals(status)){//出票成功
				
				if(!TrainConsts.BOOKING_TICKET.equals(orderInfo.getOrder_status())
						&& !TrainConsts.BOOK_SUCCESS.equals(orderInfo.getOrder_status())){
					logger.info("[接收出票结果通知接口]本次出票成功通知为重复通知，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				paramMap.put("order_id", orderId);
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.OUT_SUCCESS);//出票成功
				
				//明细数据处理
				this.detailDataPacking(seatTrains, cpMapList, response);

				int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList, null);
				if (count == 1){
					write2Response(response, "success");
					
					if(StringUtils.isEmpty(level) || "0".equals(level)){
						//发送出票成功短信
						this.sendCpSuccMsn(orderId, billNo);
					}else{
						//发送vip订购短信
						this.sendVipSuccMsn(orderId, billNo);
					}
					
				}else{
					logger.info("failed：修改订单" + orderId + "失败！");
					write2Response(response, "failed");
				}
			}
			
		}else if(TrainConsts.FAILURE.equalsIgnoreCase(result)){//失败
			
			//阻止重复出票系统通知请求
			Map<String, String> paramMap1 = new HashMap<String, String>();
			paramMap1.put("order_id", orderId);
			paramMap1.put("refund_before_time",  this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time"));
			paramMap1.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
			OrderInfo orderInfo = orderService.queryOrderInfo2(paramMap1);
			if(!StringUtils.isEmpty(orderInfo.getOrder_status())
					&& TrainConsts.OUT_FAIL.equals(orderInfo.getOrder_status())){
				logger.info("[接收出票结果通知接口]本次出票失败通知为重复请求，orderId=" + orderId);
				write2Response(response, "success");
				return;
			}
			
			paramMap.put("order_id", orderId);
			paramMap.put("buy_money", buyMoney);
			paramMap.put("out_ticket_billno", billNo);
			paramMap.put("order_status", TrainConsts.OUT_FAIL);//出票失败
			
			String errorinfo = this.getParam(request, "errorinfo");//错误信息
			request.getSession().setAttribute(orderId+"_error", errorinfo);
			Map<String, String> failRefundMap = new HashMap<String, String>();//出票失败退款map
			failRefundMap.put("order_id", orderId);
			failRefundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
			failRefundMap.put("refund_type", TrainConsts.REFUND_TYPE_3);//出票失败退款
			failRefundMap.put("refund_money", orderInfo.getPay_money());
			failRefundMap.put("user_remark", errorinfo);
			failRefundMap.put("refund_status", TrainConsts.REFUND_STREAM_PRE_REFUND);//准备退款
			int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, null, failRefundMap);
			if (count == 1){
				//比支付时间超过5min
				String now = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
				String min = DateUtil.getDistanceTime(orderInfo.getPay_time(), now);
				if( Integer.parseInt(min) >= 5){
					//发送出票失败短信
					if(StringUtils.isEmpty(level) || "0".equals(level)){
						//普通用户不发送出票失败
					}else{
					this.sendCpFailMsn(orderId);
					logger.info("订单号为："+orderId+"，支付时间为："+orderInfo.getPay_time()+"，距现在超过5分钟，发送出票失败短信!");
					}
				}
				
				write2Response(response, "success");

			}else{
				logger.info("failed：修改订单" + orderId + "失败！");
				write2Response(response, "failed");
			}
			
		}else{//异常
			logger.info("exception：订单" + orderId + "，接口返回未知状态码！");
			write2Response(response, "failed");
		}
		
		/**
		 * 开始添加用户身份证信息
		 */
		if(!StringUtils.isEmpty(passengers)){
			userInfoService.ProcessingUserCheckData(passengers);
		}
	}
	
	/**
	 * 用户电话取消预约退款接口
	 * @param request
	 * @param response
	 */
	@RequestMapping("/cancleOrder_no.jhtml")
	public void cancleOrder(HttpServletRequest request, HttpServletResponse response){
		String orderId = this.getParam(request, "orderId");//后台调用接口，传参orderId
		//阻止重复出票系统通知请求
		Map<String, String> paramMap1 = new HashMap<String, String>();
		paramMap1.put("order_id", orderId);
		paramMap1.put("refund_before_time",  this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time"));
		paramMap1.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
		OrderInfo orderInfo = orderService.queryOrderInfo2(paramMap1);
		if(!StringUtils.isEmpty(orderInfo.getOrder_status())
				&& TrainConsts.CANCLE_BOOK.equals(orderInfo.getOrder_status())){
			logger.info("[接收后台通知]本次取消预约通知为重复请求，orderId=" + orderId);
			write2Response(response, "success");
			return;
		}
		Map<String, String> paramMap = new HashMap<String, String>();//主订单参数
		paramMap.put("order_id", orderId);
		paramMap.put("buy_money", orderInfo.getTicket_pay_money());//票价总额
		paramMap.put("out_ticket_billno", orderInfo.getOut_ticket_billno());//12306单号  null
		paramMap.put("order_status", TrainConsts.CANCLE_BOOK);//取消预约
		
		String errorinfo = "用户电话取消预约";//错误信息
		Map<String, String> failRefundMap = new HashMap<String, String>();//出票失败退款map
		failRefundMap.put("order_id", orderId);
		failRefundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
		failRefundMap.put("refund_type", TrainConsts.REFUND_TYPE_7);//取消预约退款
		failRefundMap.put("refund_money", orderInfo.getTicket_pay_money());//仅退还用户的票价总额，用户的SVIP服务费不予退还
		failRefundMap.put("user_remark", errorinfo);
		failRefundMap.put("refund_status", TrainConsts.REFUND_STREAM_PRE_REFUND);//准备退款
		int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, null, failRefundMap);
		if (count == 1){
			//比支付时间超过5min
			//String now = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
			//String min = DateUtil.getDistanceTime(orderInfo.getPay_time(), now);
			//if( Integer.parseInt(min) >= 5){
				//发送出票失败短信
			//	this.sendCpFailMsn(orderId);
			//	logger.info("订单号为："+orderId+"，支付时间为："+orderInfo.getPay_time()+"，距现在超过5分钟，发送出票失败短信!");
			//}
			logger.info("success：修改【取消预约】订单" + orderId + "成功！");
			write2Response(response, "success");
		}else{
			logger.info("failed：修改【取消预约】订单" + orderId + "失败！");
			write2Response(response, "failed");
		}
	}
	
	/**
	 * 明细数据组合
	 */
	private void detailDataPacking(String seatTrains, List<Map<String, String>> cpMapList,
			HttpServletResponse response){
		//CP0120212|133|12|058号#CP0120213|133|12|059号
		String[] seatMsgs = seatTrains.split("#");
		Map<String, String> cpMap = null;
		for (String seatMsg : seatMsgs) {//CP0120212|133|12|058号
			String[] str = seatMsg.split("\\|");
			if(str == null){
				//参数为空
				logger.info("exception：参数拆分失败！");
				write2Response(response, "failed");
				return;
			} 
			for (int i = 0; i < str.length; i++) {
				cpMap = new HashMap<String, String>();
				cpMap.put("cp_id", str[0]);
				cpMap.put("buy_money", str[1]);//成本价格
				cpMap.put("train_box", str[2]);//车厢
				cpMap.put("seat_no", str[3]);//座位号
				if(str.length==5){
					cpMap.put("seat_type", str[4]);//座位号
				}
				cpMapList.add(cpMap);
			}
		}
	}
	


	//【步步高超市】尊贵的VIP用户，欢迎您使用19e提供的火车票订购业务，我们会竭诚为您服务！如果您在订购中出现问题，请拨打专线400-698-6666转5进行咨询。
	
	/**
	 * 发送VIP出票短信（预订成功则发短信）
	 */
	private void sendVipSuccMsn(String orderId, String billNo){
		//发送出票成功短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		List<Map<String, String>> conList = orderService.queryCpContactInfo(orderId);
		if(contact != null && conList != null && conList.size() > 0){
			//【步步高超市】E888888888，尊贵的VIP用户您成功订购07月26日13:50（福州—福州南）D6237次2车14D号席位，张XX、李XX请持二代身份证换票乘车，祝您旅途愉快！
			StringBuffer content = new StringBuffer();
			
			//尊贵的VIP用户，19e加盟站提醒您，我们非常荣幸的帮助您预定成功编号为E297492020的11月12日20:55（北京西-武昌）Z37次张睿的12车17号下铺、
			//陈迪12车18号上铺、严志有12车20号上铺席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝你旅途愉快！
			if(Double.parseDouble(contact.get("server_pay_money"))>0){
				content.append("尊贵的SVIP用户，");
			}else{
				content.append("尊贵的VIP用户，");
			}
			if(StringUtils.isEmpty(contact.get("shop_short_name"))){
				content.append("19e加盟站");
			}else{
				content.append(contact.get("shop_short_name"));
			}
			content.append("提醒您，我们非常荣幸的帮助您成功订购编号为")
				   .append(billNo)
				   .append("的")
				   .append(contact.get("from_time"))
				   .append("（").append(contact.get("from_city")).append("-").append(contact.get("to_city")).append("）")
				   .append(contact.get("train_no")).append("次");
			for(int i=0; i<conList.size(); i++){
				Map<String, String> conDetail = conList.get(i);
				if(i > 0){
					content.append("、");
				}
				content.append(conDetail.get("user_name"))
				       .append(conDetail.get("train_box")).append("车").append(conDetail.get("seat_no"));
			}
			content.append("席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝您旅途愉快！");
			
			//五周年系列活动
			//活动时间：2013年11月18日至11月22日
			Date date = new Date();
			Date begin = DateUtil.stringToDate("20131118000000", "yyyyMMddHHmmss");
			Date end = DateUtil.stringToDate("20131122235959", "yyyyMMddHHmmss");
			if(date.after(begin) && date.before(end)){
				content.append("19e五周年活动码")
				   .append(getFiveYearCode())
				   .append("。");
	
				Map<String, String> msn = new HashMap<String, String>();
				msn.put("user_name", contact.get("link_name"));
				msn.put("user_phone", contact.get("link_phone"));
				msn.put("content", content.toString());
				orderService.addMsn(msn);
				
			}
			/*
			 * 发送短信通知车票预订成功
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString());
			
			
/*			//五周年系列活动
			//活动时间：2013年11月18日至11月22日
			Date date = new Date();
			Date begin = DateUtil.stringToDate("20131118000000", "yyyyMMddHHmmss");
			Date end = DateUtil.stringToDate("20131122235959", "yyyyMMddHHmmss");
			if(date.after(begin) && date.before(end)){
				StringBuffer content1 = new StringBuffer();
				content1.append("11.18—11.22订火车票+保险，ipad mini抱回家！活动期间，活动码后三位数与当天福彩3D号码一致，即为中奖！兑奖请致电010-57386415！19e五周年活动码")
				
						.append(getFiveYearCode());
				
				Map<String, String> msn = new HashMap<String, String>();
				msn.put("user_name", contact.get("link_name"));
				msn.put("user_phone", contact.get("link_phone"));
				msn.put("content", content1.toString());
				orderService.addMsn(msn);
				
				logger.info("【五周年活动】"+content1.toString());
				mobileMsgUtil.send(contact.get("link_phone"), content1.toString());
			}*/
		}
	}
	
	/**
	 * 五周年短信码
	 * @return
	 */
	public static synchronized String getFiveYearCode(){
		String key = "FIVE_YEAR_CODE_"+DateUtil.dateToString(new Date(),"yyyyMMdd");
		
		Integer suffixCode = new Integer(1001);
		synchronized (LOCK) {
			if(null == MemcachedUtil.getInstance().getAttribute(key)){
				MemcachedUtil.getInstance().setAttribute(key, suffixCode, 7*24*60*60*1000);
			}else{
				suffixCode = (Integer) MemcachedUtil.getInstance().getAttribute(key);
				suffixCode++;
				MemcachedUtil.getInstance().setAttribute(key, suffixCode, 7*24*60*60*1000);
			}
		}
		String code = DateUtil.dateToString(new Date(),"yyMM")+String.valueOf(suffixCode);
		return code;
	}
	
	/**
	 * 发送预订成功短信
	 */
	private void sendYdSuccMsn(String orderId, String billNo){
		//发送预订成功短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		List<Map<String, String>> conList = orderService.queryCpContactInfo(orderId);
		if(contact != null && conList != null && conList.size() > 0){
			//【步步高超市】E888888888，提醒您成功订购07月26日13:50（福州—福州南）D6237次2车14D号席位，张XX、李XX请持二代身份证乘车或换票乘车。
			StringBuffer content = new StringBuffer();
			String msg_head = "【19e加盟站】";
			if(!StringUtils.isEmpty(contact.get("shop_short_name"))){
				msg_head = "【" + contact.get("shop_short_name") + "】";
			}
			content.append(msg_head)
				   .append(billNo)//12306订单号
				   .append("，")
				   .append("提醒您成功预订")
				   .append(contact.get("from_time"))
				   .append("（").append(contact.get("from_city")).append("-").append(contact.get("to_city")).append("）")
				   .append(contact.get("train_no")).append("次");
			for(int i=0; i<conList.size(); i++){
				Map<String, String> conDetail = conList.get(i);
				if(i > 0){
					content.append("、");
				}
				content.append(conDetail.get("train_box")).append("车").append(conDetail.get("seat_no"));
			}
			content.append("席位。");
			content.append("请您等待最终出票短信通知。");	
			/*
			 * 发送短信通知车票预订成功
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString());
		}
	}
	
	/**
	 * 发送出票成功短信
	 */
	private void sendCpSuccMsn(String orderId, String billNo){
		//发送出票成功短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		List<Map<String, String>> conList = orderService.queryCpContactInfo(orderId);
		if(contact != null && conList != null && conList.size() > 0){
			//【步步高超市】E888888888，提醒您成功订购07月26日13:50（福州—福州南）D6237次2车14D号席位，张XX、李XX请持二代身份证乘车或换票乘车。
			StringBuffer content = new StringBuffer();
			
			//19e加盟站提醒您，我们非常荣幸的帮助您预定成功编号为E297492020的11月12日20:55（北京西-武昌）Z37次张睿的12车17号下铺、
			//陈迪12车18号上铺、严志有12车20号上铺席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝你旅途愉快！
			if(StringUtils.isEmpty(contact.get("shop_short_name"))){
				content.append("19e加盟站");
			}else{
				content.append(contact.get("shop_short_name"));
			}
			content.append("提醒您，我们非常荣幸的帮助您成功订购编号为")
				   .append(billNo)
				   .append("的")
				   .append(contact.get("from_time"))
				   .append("（").append(contact.get("from_city")).append("-").append(contact.get("to_city")).append("）")
				   .append(contact.get("train_no")).append("次");
			for(int i=0; i<conList.size(); i++){
				Map<String, String> conDetail = conList.get(i);
				if(i > 0){
					content.append("、");
				}
				content.append(conDetail.get("user_name"))
				       .append(conDetail.get("train_box")).append("车").append(conDetail.get("seat_no"));
			}
			content.append("席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝您旅途愉快！");
			
			/*
			 * 发送短信通知车票预订成功
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString());
		}
	}
	
	
	/**
	 * 发送出票失败短信
	 */
	private void sendCpFailMsn(String orderId){
		//发送出票失败短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		//【19e火车票】您从步步高超市/小店订购的07月26日13:50（福州—福州南）T101次的火车票出票失败，请及时联系步步高超市/小店收回退款，谢谢！
		//【19e火车票】您订购的07月26日13:50（福州—福州南）T101次的火车票出票失败，谢谢！
		//您订购的07月26日13:50（福州—福州南）T101次的火车票出票失败，谢谢！【19易】
		StringBuffer content = new StringBuffer();
		if(contact != null){
			content.append("您");
//			if(!StringUtils.isEmpty(contact.get("shop_short_name"))){
//				content.append(contact.get("shop_short_name"));
//			}else{
//				content.append("小店");
//			}
			content.append("订购的")
				   .append(contact.get("from_time"))
				   .append("（").append(contact.get("from_city")).append("-").append(contact.get("to_city")).append("）")
				   .append(contact.get("train_no")).append("次").append("的火车票出票失败，谢谢！【19易】");
//				   .append("的火车票出票失败，请及时联系");
			
//			if(!StringUtils.isEmpty(contact.get("shop_short_name"))){
//				content.append(contact.get("shop_short_name"));
//			}else{
//				content.append("小店");
//			}
//			content.append("收回退款，谢谢！");
			/*
			 * 发送短信通知出票失败
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString());
		}
	}
	
	/**
	 * Vip发送支付成功短信
	 * @param orderId
	 */
	private void sendVipPaySuccMsn(String orderId){
		
		try {
			//Vip发送支付成功短信
			String bx_pay_money = orderService.queryBxPayMoneyAtPaySucc(orderId);
			//购买了保险
			if(!StringUtils.isEmpty(bx_pay_money) 
					&& Double.parseDouble(bx_pay_money)>0){
			
				Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
				
				if(contact != null){
					//【步步高超市】尊贵的VIP用户，欢迎您使用19e提供的火车票订购业务，我们会竭诚为您服务！如果您在订购中出现问题，请拨打专线400-698-6666转5进行咨询。
					StringBuffer content = new StringBuffer();
					String msg_head = "【19e加盟站】";
					if(!StringUtils.isEmpty(contact.get("shop_short_name"))){
						msg_head = "【" + contact.get("shop_short_name") + "】";
					}
					content.append(msg_head)
						   .append("尊贵的VIP用户，欢迎您使用19e提供的火车票订购业务，我们会竭诚为您服务！如果您在订购过程中出现问题，请拨打专线400-698-6666转5进行咨询。");
					/*
					 * 发送短信通知车票预订成功
					 */
					mobileMsgUtil.send(contact.get("link_phone"), content.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 接收退票结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/refundNotify_no.jhtml")
	public void refundNotify_no(HttpServletRequest request,
			HttpServletResponse response){
		String result = this.getParam(request, "result");//成功
		String orderId = this.getParam(request, "orderid");
		String cpid = this.getParam(request, "cpid");
		String alterdiffmoney = this.getParam(request, "alterdiffmoney");
		String refundmoney = this.getParam(request, "refundmoney");
		String refund12306money = this.getParam(request, "refund12306money");
		String refund12306seq = this.getParam(request, "refund12306seq");
		String status = this.getParam(request, "status");//0、改签通知 1、退票通知
		String our_remark = this.getParam(request, "our_remark");//备注
		String refuse_reason = this.getParam(request, "refuse_reason");//拒绝退票原因
		
		logger.info("【接收退票系统通知】参数orderId=" + orderId + "，cpid=" + cpid + 
				"，alterdiffmoney=" + alterdiffmoney + "，refund12306money=" + refund12306money + 
				"，refund12306seq=" + refund12306seq + "，status=" + status + "，result=" + result + 
				"，our_remark=" + our_remark + "，refuse_reason=" + refuse_reason);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("order_id", orderId);
		map.put("cp_id", cpid);
		map.put("our_remark", our_remark);//出票方备注
		map.put("refuse_reason", refuse_reason);
		if(StringUtils.isEmpty(alterdiffmoney)){
			map.put("alter_tickets_money", "0.0");//改签差价
		}else{
			map.put("alter_tickets_money", Double.valueOf(alterdiffmoney));//改签差价
		}
		if(StringUtils.isEmpty(refund12306money)){
			map.put("actual_refund_money", "0.0");//12306实际退款金额
		}else{
			map.put("actual_refund_money", Double.valueOf(refund12306money));//12306实际退款金额
		}
		
		map.put("refund_12306_seq", refund12306seq); //12306退款流水单号
		try{
			if("0".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)){
				orderService.updateCPAlterInfo(map);
				logger.info("更改hc_orderinfo_cp表的alter_money="+alterdiffmoney);
				write2Response(response, "success");
			}else if("1".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)){
				map.put("refund_status", "11");
				map.put("refund_money", Double.valueOf(refundmoney));
				orderService.updateRefundInfo(map);
				logger.info("更改hc_orderinfo_refundstream表的refund_status=11");
				write2Response(response, "success");
			}else if("1".equals(status) && TrainConsts.FAILURE.equalsIgnoreCase(result)){
				map.put("refund_status", "55");//55、拒绝退款
				orderService.updateRefundInfo(map);
				logger.info("更改hc_orderinfo_refundstream表的refund_status=55");
				write2Response(response, "success");
			}else if("2".equals(status)){
				map.put("refund_status", "99");//99、搁置订单
				orderService.updateRefundInfo(map);
				logger.info("更改hc_orderinfo_refundstream表的refund_status=99");
				write2Response(response, "success");
			}
		}catch(Exception e){
			logger.error("exception",e);
			write2Response(response, "failed");
		}
		
	}
	
}
