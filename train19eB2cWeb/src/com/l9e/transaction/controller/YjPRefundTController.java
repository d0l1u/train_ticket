package com.l9e.transaction.controller;

import java.io.IOException;
import java.net.URLEncoder;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.EmappSignUtil;
import com.l9e.util.KeyedDigestMD5;

/**
 * 车票退订
 * @author yangchao
 *
 */
@Controller
@RequestMapping("/refund")
public class YjPRefundTController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(YjPRefundTController.class);
	
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[appInitKey]}")
	private String appInitKey;//验签静态key
	
	@Value("#{propertiesReader[merchantId]}")
	private String merchantId;//合作商户ID
	
	@RequestMapping("/refunding.jhtml")
	public String refunding(RefundVo refund, MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException{
		logger.info("[代理商主动发起退款]order_id="+refund.getOrder_id());
		//9点-21点可以
		Date currentDate = new Date();
		String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "090000", "yyyyMMddHHmmss");//9:00
		Date end = DateUtil.stringToDate(datePre + "210000", "yyyyMMddHHmmss");//21:00
		if(currentDate.before(begin) || currentDate.after(end)){
			logger.info("不受理该退款，发起时间为："+DateUtil.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss"));
			String errMsg = "<span style='line-height:40px;'>由于火车票业务新上线，每日临时受理退款时间为早9:00～晚21:00，请见谅。<br />如有特殊情况请先到车站退票，第二日持车站退款小票到代理商处退款。</span>";
			errMsg = URLEncoder.encode(errMsg, "UTF-8");
			return "redirect:/common/goToErrPage.jhtml?errMsg="+errMsg;
		}
		String old_refund_status = this.getParam(request, "old_refund_status");//原来的退款状态
		refund.setRefund_status(TrainConsts.REFUND_PRE_REFUND);//准备退款
		orderService.refunding(refund, old_refund_status);
		
		return "redirect:/query/queryOrderList.jhtml";
	}
	
	/**
	 * 退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/refund.jhtml")
	public String refund(MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException {		
		/**
		 * token防止重复提交
		 */
		String token = this.getParam(request, "token");
		if(null==request.getSession().getAttribute("sessionToken") 
				|| !token.equals((String)request.getSession().getAttribute("sessionToken"))){
			logger.info("[token校验]退款发生重复提交，请求被拒绝，重定向到订单查询页面");
			return "redirect:/query/queryOrderList.jhtml";
		}else{
			request.getSession().removeAttribute("sessionToken");
		}
		String order_id = this.getParam(request, "order_id");//按照订单号退款
		logger.info("[代理商主动发起退款]order_id="+order_id);
		//9点-21点可以
		Date currentDate = new Date();
		String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "070000", "yyyyMMddHHmmss");//7:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
		if(currentDate.before(begin) || currentDate.after(end)){
			logger.info("不受理该退款，发起时间为："+DateUtil.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss"));
			String errMsg = "<span style='line-height:40px;'>每日受理退票时间为早7:00～晚23:00，请见谅。<br />如有特殊情况请先到车站退票，第二日持车站退款小票到代理商处退款。</span>";
			errMsg = URLEncoder.encode(errMsg, "UTF-8");
			return "redirect:/common/goToErrPage.jhtml?errMsg="+errMsg;
		}
		
//		String cp_id_str = this.getParam(request, "cp_id_str");
		List<Map<String, String>> cp_id_str = orderService.queryCpInfoList(order_id);
		List<Map<String, String>> refundList = new ArrayList<Map<String, String>>();
		Map<String, String> refundMap = null;
		for(Map<String, String> cp_id_map : cp_id_str){
			String cp_id = cp_id_map.get("cp_id");
			logger.info("[代理商主动发起退款]cp_id="+cp_id);
			refundMap = new HashMap<String, String>();
			
			refundMap.put("refund_type", TrainConsts.REFUND_TYPE_1);//用户退款
			refundMap.put("order_id", order_id);
			refundMap.put("cp_id", cp_id);
			refundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
			refundMap.put("refund_money", this.getParam(request, "refund_money_" + cp_id));//退款金额
			refundMap.put("user_remark", this.getParam(request, "user_remark_" + cp_id));
			refundMap.put("refund_status", TrainConsts.REFUND_STREAM_WAIT_REFUND);//准备退款
			refundMap.put("old_refund_status", this.getParam(request, "refund_status_" + cp_id));//旧的退款状态
			refundMap.put("refund_percent", this.getParam(request, "refund_percent_" + cp_id));
			refundMap.put("refund_no", CreateIDUtil.createID("WB"));//app退款流水号
			refundMap.put("notify_status", "00");
			refundMap.put("notify_time", "now()");
			refundMap.put("notify_num", "0");
			refundMap.put("pay_type", "44");//19pay
			//refundMap.put("order_money", "0");//订单金额
			refundMap.put("order_create_time", cp_id_map.get("create_time"));//生成订单时间
			
			refundList.add(refundMap);
		}
		orderService.addRefundStream(refundList, order_id);
		
		return "redirect:/query/queryOrderList.jhtml";
	}
	
	
	
	
	/**
	 * 接收19pay发送的退款结果通知请求
	* @param request
	 * @param response
	 */
	@RequestMapping("/refundToTrain_no.jhtml")
	public void refundToTrain(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.info("【退款流水接口】订单号："+this.getParam(request, "oriPayOrderId")+"调用异步退票结果反馈");
		String version = this.getParam(request, "version");
		String mxresq = this.getParam(request, "mxResq");
		String status = this.getParam(request, "status");
		String code = this.getParam(request, "retCode");
		String verify = this.getParam(request, "verifyString");
		String orderid = this.getParam(request, "oriPayOrderId");
		String sysseq = this.getParam(request, "refOrderId");
		String sutime = this.getParam(request, "finishTime");
		String amount = this.getParam(request, "amount");
		
		StringBuffer buffer = new StringBuffer();
	    buffer.append("version=").append(version);
	    buffer.append("&mxResq=").append(mxresq);
        buffer.append("&status=").append(status);
        buffer.append("&retCode=").append(code);
	    buffer.append("&oriPayOrderId=").append(orderid);
	    buffer.append("&finishTime=").append(sutime);
	    buffer.append("&amount=").append(amount);
	    buffer.append("&refOrderId=").append(sysseq);// 退款流水
	    buffer.append("&fee=").append("0.00");
	    String verifyString = KeyedDigestMD5.getKeyedDigestUTF8(buffer.toString(), appInitKey);
		logger.info("【退款流水接口】加密串："+buffer.toString());
		
		Map<String,String> refundReMap=new HashMap<String,String>();
		refundReMap.put("order_id", orderid);
		refundReMap.put("refund_seq", mxresq);
		refundReMap.put("plat_refund_seq", sysseq);
		refundReMap.put("actual_refund_money", amount);
		refundReMap.put("refund_time", sutime);
		refundReMap.put("refund_status", TrainConsts.REFUND_STREAM_REFUND_FINISH);

		//添加日志
		Map<String,String> logMap = new HashMap<String,String>();
		logMap.put("order_id", orderid);
		logMap.put("opter", "19trip");
		logMap.put("order_time", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
		
		//开始验证签名
		boolean flag=verifyString.equals(verify);
		int update=0;
		if(flag){
			if("0".equals(status)){
				logger.info("【退款流水接口】19pay收单成功，退款状态更新成功，订单号："+ orderid);
				if(StringUtils.isEmpty(sysseq)){
					logger.info("【退款流水接口】19pay退款流水号为空！ 订单号："+orderid);
					write2Response(response,"N");
				}else{
					try {
						update=orderService.updateRefundStreamInfo(refundReMap);
						if(update==1){
							write2Response(response,"Y");
							logger.info("【退款流水接口】已更新19pay退款交易流水号、实际退款金额及退款状态！");
						}else{
							write2Response(response,"N");
							logger.info("【退款流水接口】更新19pay退款交易流水号、实际退款金额及退款状态失败！");
						}
					} catch (Exception e) {
						write2Response(response,"N");
						logger.error("【退款流水接口】订单号："+orderid+"--异步反馈，更新19pay退款交易流水号失败！", e);
					}
				}
				logMap.put("order_optlog", "【退款流水接口】退款成功,退款状态修改成功");
			}else{
				write2Response(response,"N");
				logMap.put("order_optlog", "【退款流水接口】接口返回退款状态为退款失败");
				logger.info("【退款流水接口】订单号："+orderid+"--接收19pay退款通知请求状态失败");
			}
		}else{
			try {
				logger.info("【退款流水接口】订单号："+orderid+" 退款结果通知请求验签失败！");
				write2Response(response,"N");
			} catch (Exception e) {
				StringBuffer err_log = new StringBuffer();
				err_log.append("【退款流水接口】对退款返回数据验签失败，orderId=").append(orderid)
						.append("本地加密：").append(verifyString).append("；19pay加密：").append(verify);
				logger.info(err_log.toString());
				logMap.put("order_optlog", err_log.toString());
				return;
			}
		}
		orderService.addOrderOptLog(logMap);
	}
	
}
