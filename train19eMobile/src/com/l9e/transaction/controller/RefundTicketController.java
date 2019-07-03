package com.l9e.transaction.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;

/**
 * 车票退订
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/refund")
public class RefundTicketController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(RefundTicketController.class);
	
	@Resource
	private OrderService orderService;
	
	/**
	 * 退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/refund_no.jhtml")
	public void refundNoSession(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//验证参数
		String order_id = this.getParam(request, "order_id");
		String agentId = this.getParam(request, "agentId");
		logger.info("[代理商主动发起退款]order_id="+order_id);
		
		String token = this.getParam(request, "refund_token");
		//refunds:[{"cp_id":"","refund_money":"","user_remark":"","refund_status":"","refund_percent":""}]
		String refunds = this.getParam(request, "refunds");
		if(StringUtils.isEmpty(order_id) || StringUtils.isEmpty(agentId)
				|| StringUtils.isEmpty(token) || StringUtils.isEmpty(refunds)){
			logger.info("退票申请参数为空！");
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code", "PARAM_EMPTY");
			returnJson.put("message", "请求的必填参数不能为空！。");
			printJson(response, returnJson.toString());
			return;
		}
		
		/**
		 * token防止重复提交
		 

		if(null==request.getSession().getAttribute("sessionToken") 
				|| !token.equals((String)request.getSession().getAttribute("sessionToken"))){
			logger.info("[token校验]退款发生重复提交，请求被拒绝，重定向到订单查询页面");
			
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","REPEAT_SUBMIT");
			returnJson.put("message","token验证失败，退票申请重复提交！");
			printJson(response,returnJson.toString());
			return;
		}else{
			request.getSession().removeAttribute("sessionToken");
		}
		*/
		//9点-23点可以
		Date currentDate = new Date();
		String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "070000", "yyyyMMddHHmmss");//7:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
		if(currentDate.before(begin) || currentDate.after(end)){
			logger.info("不受理该退款，发起时间为："+DateUtil.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss"));
			
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","ILLEGAL_TIME");
			returnJson.put("message","受理退票时间为早7:00至晚23:00！");
			printJson(response,returnJson.toString());
			return;
		}
		
		//校验订单号和代理商是否匹配
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put("order_id", order_id);
		paramMap.put("agentId", agentId);
		OrderInfo orderInfo = orderService.queryOrderInfoWithAgentId(paramMap);
		if(orderInfo == null){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","NO_MATCH_ORDER");
			returnJson.put("message","请订单和代理商ID不匹配！");
			printJson(response,returnJson.toString());
			return;
		}
		
		//校验是否可以进行退票操作
		//可以退票的条件：1、出票成功 2、可以退票 3、没有达到截止日期或者人为移除截止限制 4、没有发生过退票或者拒绝退票
		if(!TrainConsts.OUT_SUCCESS.equals(orderInfo.getOrder_status())){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","REFUSE_REFUND");
			returnJson.put("message","订单不允许发生退票，拒绝原因:订单状态不是出票成功！");
			printJson(response,returnJson.toString());
			return;
		}
		if(StringUtils.isNotEmpty(orderInfo.getCan_refund()) && !"1".equals(orderInfo.getCan_refund())){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","REFUSE_REFUND");
			returnJson.put("message","订单不允许发生退票，拒绝原因:can_refund=0！");
			printJson(response,returnJson.toString());
			return;
		}
		if("1".equals(orderInfo.getIs_deadline()) && !"1".equals(orderInfo.getDeadline_ignore())){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","REFUSE_REFUND");
			returnJson.put("message","订单不允许发生退票，拒绝原因：已过退票截止日期！");
			printJson(response,returnJson.toString());
			return;
		}
		
		List<Map<String, String>> refundList = new ArrayList<Map<String, String>>();
		Map<String, String> refundMap = null;
		JSONArray refundJs = null;
		try{
			refundJs = JSONArray.fromObject(refunds);
		}catch(Exception e){
			logger.error("退票明细refunds解析JSON失败", e);
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code", "FAIL");
			returnJson.put("message", "退票明细refunds解析JSON失败！");
			printJson(response, returnJson.toString());
			return;
		}
		JSONObject refundJson = null; 
		String cp_id = null;
		String refund_status = null;
		String refund_percent = null;
		
        //	 * ①开车前48小时以上的，退票时收取票价5%的退票费；
        //	 * ②开车前24小时以上、不足48小时的，退票时收取票价10%的退票费；
        //	 * ③开车前不足24小时的，退票时收取票价20%退票费。
		 
		double feePercent = 0;
		Map<String, String> timeMap = orderService.querySpecTimeBeforeFrom(order_id);
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
		
		for(int i=0; i<refundJs.size(); i++){
			refundMap = new HashMap<String, String>();
			refundJson = refundJs.getJSONObject(i);
			cp_id = refundJson.getString("cp_id");
			//refund_money = refundJson.getString("refund_money");
			//refund_status = refundJson.getString("old_refund_status");
			
			if(StringUtils.isEmpty(cp_id)){
				logger.error("退票明细refunds参数为空！");
				JSONObject returnJson = new JSONObject();
				returnJson.put("return_code", "PARAM_EMPTY");
				returnJson.put("message", "退票明细refunds参数为空！");
				printJson(response, returnJson.toString());
				return;
				
			}else if(StringUtils.isNotEmpty(refund_status) && !TrainConsts.REFUND_STREAM_REFUSE.equals(refund_status)){
				logger.error("退票明细refunds中status状态非法！");
				JSONObject returnJson = new JSONObject();
				returnJson.put("return_code", "FAIL");
				returnJson.put("message", "退票明细refunds中退票状态只能为空或者拒绝退票！");
				printJson(response, returnJson.toString());
				return;
			}
			//计算退票金额
			Map<String, String> cpMap = orderService.queryOrderCpInfoByCpId(cp_id);
			Map<String, String> param = new HashMap<String, String>(2);
			param.put("order_id", order_id);
			param.put("cp_id", cp_id);
			Map<String, String> cpRefundMap = orderService.queryRefundInfoByCpId(param);
			String old_status = null;
			if(cpRefundMap != null){
				old_status = cpRefundMap.get("refund_status");
			}
			
			if(StringUtils.isNotEmpty(old_status) && !TrainConsts.REFUND_STREAM_REFUSE.equals(old_status)){
				JSONObject returnJson = new JSONObject();
				returnJson.put("return_code","REFUSE_REFUND");
				returnJson.put("message","订单不允许发生退票，拒绝原因:已发生过退票！");
				printJson(response,returnJson.toString());
				return;
			}
			
			//计算退票金额
			double refund_money = 0;
			double buy_money = Double.parseDouble(cpMap.get("cp_buy_money"));
			double sxf = AmountUtil.quarterConvert(AmountUtil.mul(buy_money, feePercent));//手续费
			sxf = sxf > 2 ? sxf : 2;
			refund_money = AmountUtil.ceil(AmountUtil.sub(buy_money, sxf));
			logger.info("退票金额计算：order_id="+order_id+"&cp_id="+cp_id+"&refund_money="+refund_money);
			
			//refunds:[{"cp_id":"","user_remark":""}]
			refundMap.put("refund_type", TrainConsts.REFUND_TYPE_1);//用户退款
			refundMap.put("order_id", order_id);
			refundMap.put("cp_id", cp_id);
			refundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
			refundMap.put("refund_money", String.valueOf(refund_money));//退款金额
			refundMap.put("refund_purl", null);
			refundMap.put("user_remark", refundJson.getString("user_remark"));
			refundMap.put("refund_status", TrainConsts.REFUND_STREAM_PRE_REFUND);//准备退款
			refundMap.put("old_refund_status", old_status);//旧的退款状态
			refundMap.put("refund_percent", refund_percent);
			refundList.add(refundMap);
		}
		int ret = orderService.addRefundStream(refundList, order_id);
		if(ret == -1){
			//[数据库校验]退款发生重复提交，请求被拒绝
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","REPEAT_SUBMIT");
			returnJson.put("message","DB验证失败，已包含相关退票数据！");
			printJson(response,returnJson.toString());
			return;
		}
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("return_code", "SUCCESS");
		returnJson.put("message", "申请退票成功！");
		returnJson.put("order_id", order_id);
		printJson(response,returnJson.toString());
		return;
	}
	
	/**
	 * 退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/refund.jhtml")
	public void refund(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//验证参数
		String order_id = this.getParam(request, "order_id");
		logger.info("[代理商主动发起退款]order_id="+order_id);
		
		String token = this.getParam(request, "refund_token");
		//refunds:[{"cp_id":"","refund_money":"","user_remark":"","refund_status":"","refund_percent":""}]
		String refunds = this.getParam(request, "refunds");
		if(StringUtils.isEmpty(order_id) || StringUtils.isEmpty(token) || StringUtils.isEmpty(refunds)){
			logger.info("退票申请参数为空！");
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code", "PARAM_EMPTY");
			returnJson.put("message", "请求的必填参数不能为空！。");
			printJson(response, returnJson.toString());
			return;
		}
		
		//9点-23点可以
		Date currentDate = new Date();
		String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "070000", "yyyyMMddHHmmss");//7:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
		if(currentDate.before(begin) || currentDate.after(end)){
			logger.info("不受理该退款，发起时间为："+DateUtil.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss"));
			
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","ILLEGAL_TIME");
			returnJson.put("message","受理退票时间为早7:00至晚23:00！");
			printJson(response,returnJson.toString());
			return;
		}
		
		//校验是否可以进行退票操作
		//可以退票的条件：1、出票成功 2、可以退票 3、没有达到截止日期或者人为移除截止限制 4、没有发生过退票或者拒绝退票
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		if(!TrainConsts.OUT_SUCCESS.equals(orderInfo.getOrder_status())){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","REFUSE_REFUND");
			returnJson.put("message","订单不允许发生退票，拒绝原因:订单状态不是出票成功！");
			printJson(response,returnJson.toString());
			return;
		}
		if(StringUtils.isNotEmpty(orderInfo.getCan_refund()) && !"1".equals(orderInfo.getCan_refund())){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","REFUSE_REFUND");
			returnJson.put("message","订单不允许发生退票，拒绝原因:can_refund=0！");
			printJson(response,returnJson.toString());
			return;
		}
		if("0".equals(orderInfo.getIs_deadline()) 
				|| ("1".equals(orderInfo.getIs_deadline()) && !"1".equals(orderInfo.getDeadline_ignore()))){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","REFUSE_REFUND");
			returnJson.put("message","订单不允许发生退票，拒绝原因：已过退票截止日期！");
			printJson(response,returnJson.toString());
			return;
		}
		
		List<Map<String, String>> refundList = new ArrayList<Map<String, String>>();
		Map<String, String> refundMap = null;
		JSONArray refundJs = null;
		try{
			refundJs = JSONArray.fromObject(refunds);
		}catch(Exception e){
			logger.error("退票明细refunds解析JSON失败", e);
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code", "FAIL");
			returnJson.put("message", "退票明细refunds解析JSON失败！");
			printJson(response, returnJson.toString());
			return;
		}
		JSONObject refundJson = null; 
		String cp_id = null;
		String refund_money = null;
		String refund_status = null;
		String refund_percent = null;
		
		for(int i=0; i<refundJs.size(); i++){
			refundMap = new HashMap<String, String>();
			refundJson = refundJs.getJSONObject(i);
			cp_id = refundJson.getString("cp_id");
			refund_money = refundJson.getString("refund_money");
			refund_status = refundJson.getString("old_refund_status");
			refund_percent = refundJson.getString("refund_percent");
			
			if(StringUtils.isEmpty(cp_id) || StringUtils.isEmpty(refund_money)
					|| StringUtils.isEmpty(refund_percent)){
				logger.error("退票明细refunds参数为空！");
				JSONObject returnJson = new JSONObject();
				returnJson.put("return_code", "PARAM_EMPTY");
				returnJson.put("message", "退票明细refunds参数为空！");
				printJson(response, returnJson.toString());
				return;
				
			}else if(StringUtils.isNotEmpty(refund_status) && !TrainConsts.REFUND_STREAM_REFUSE.equals(refund_status)){
				logger.error("退票明细refunds中status状态非法！");
				JSONObject returnJson = new JSONObject();
				returnJson.put("return_code", "FAIL");
				returnJson.put("message", "退票明细refunds中退票状态只能为空或者拒绝退票！");
				printJson(response, returnJson.toString());
				return;
			}
			
			//refunds:[{"cp_id":"","refund_money":"","user_remark":"","refund_status":"","refund_percent":""}]
			refundMap.put("refund_type", TrainConsts.REFUND_TYPE_1);//用户退款
			refundMap.put("order_id", order_id);
			refundMap.put("cp_id", cp_id);
			refundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
			refundMap.put("refund_money", refund_money);//退款金额
			refundMap.put("refund_purl", null);
			refundMap.put("user_remark", refundJson.getString("user_remark"));
			refundMap.put("refund_status", TrainConsts.REFUND_STREAM_PRE_REFUND);//准备退款
			refundMap.put("old_refund_status", refund_status);//旧的退款状态
			refundMap.put("refund_percent", refund_percent);
			refundList.add(refundMap);
		}
		int ret = orderService.addRefundStream(refundList, order_id);
		if(ret == -1){
			//[数据库校验]退款发生重复提交，请求被拒绝
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","REPEAT_SUBMIT");
			returnJson.put("message","DB验证失败，已包含相关退票数据！");
			printJson(response,returnJson.toString());
			return;
		}
		request.getSession().setAttribute("sessionToken", token);
		JSONObject returnJson = new JSONObject();
		returnJson.put("return_code", "SUCCESS");
		returnJson.put("message", "申请退票成功！");
		returnJson.put("order_id", order_id);
		printJson(response,returnJson.toString());
		return;
	}
	
}
