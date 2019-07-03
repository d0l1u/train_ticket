package com.l9e.transaction.job;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alipay.util.UtilDate;
import com.hispeed.encryption.KeyedDigestMD5;
import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.TradeService;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.transaction.vo.TradeVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;

/**
 * 扫描退款流水发起退款请求job
 * @author zhangjun
 *
 */
@Component("refundStreamJob")
public class RefundStreamJob extends BaseController{
	
	private static final Logger logger = Logger.getLogger(RefundStreamJob.class);
	
	@Resource
	private OrderService orderService;
	@Resource
	private TradeService tradeService;
	
	public void refund(){
		List<Map<String, String>> refundList = orderService.queryTimedRefundStreamList();
		for (Map<String, String> refundMap : refundList) {
		
			if("alipay".equalsIgnoreCase(refundMap.get("pay_channel"))) {
				this.insertTrade(refundMap);
			}else{
				this.sendRefundStreamRequest(refundMap);
			}
		}
	}
	
	private void insertTrade(Map<String, String> map) {
		logger.info("【支付宝定时向交易表里插入退款交易流水任务】退款流水为：" + map);
		String batch_no = CreateIDUtil.createID("");
		TradeVo trade = new TradeVo();
		trade.setTrade_type(TradeVo.EXPENSES);
		trade.setOrder_id(map.get("order_id"));
		trade.setTrade_seq(map.get("refund_seq"));
		trade.setRemark(RefundVo.REFUND_STATUS_MAP.get(map.get("refund_type")));
		trade.setBatch_no(batch_no);
		Map<String, Object> tradeMap = new HashMap<String, Object>();
		tradeMap.put("order_id", map.get("order_id"));
		tradeMap.put("trade_seq", map.get("refund_seq"));
		List<TradeVo> refundTradeList = tradeService.queryTrade(tradeMap);
		if (refundTradeList.size() > 0) {
			logger.info("【支付宝定时退款任务】数据库中已存在该笔退款流水号，不予处理！");
			return;
		}
		tradeMap.remove("trade_seq");
		List<TradeVo> tradeList = tradeService.queryTrade(tradeMap);
		double fee = 0.0;
		for(TradeVo tradeVo: tradeList) {
			if(TradeVo.INCOME.equalsIgnoreCase(tradeVo.getTrade_type())) {
				fee = fee + Double.parseDouble(tradeVo.getTrade_fee());
				trade.setTrade_no(tradeVo.getTrade_no());
				trade.setBuyer_id(tradeVo.getSeller_id());
				trade.setSeller_id(tradeVo.getBuyer_id());
				trade.setOperate_person("RefundStreamJob");
				trade.setTrade_status(TradeVo.WAIT_REFUND);
				trade.setTrade_id(CreateIDUtil.createID("TKCQT"));
				trade.setTrade_channel(map.get("pay_channel"));
			}
			if(TradeVo.EXPENSES.equalsIgnoreCase(tradeVo.getTrade_type())) {
				fee = fee - Double.parseDouble(tradeVo.getTrade_fee());
			}
		}
		double refund_money = Double.parseDouble(map.get("refund_money"));
		logger.info("【支付宝定时退款任务】支付宝退款金额为：" + refund_money);
		logger.info("【支付宝定时退款任务】支付宝剩余退款额为：" + fee);
		if (refund_money > fee) {
			
			logger.info("【支付宝定时退款任务】退款金额不能大于支付余额！");
			return;
		}else {
			trade.setTrade_fee(String.format("%.2f",refund_money));
		}
		tradeService.insertIntoTrade(trade);
		logger.info("【支付宝定时向交易表里插入退款交易流水任务】结束");
		
		//更新inner_orderinfo_refundstream为正在退款
		String requestId = map.get("refund_seq");//ASP退款请求流水号
		logger.info("请求退款！订单号："+map.get("order_id")+"; 退款流水号："+requestId);
		if(StringUtils.isEmpty(requestId)){
			logger.info("【退款流水接口】退款流水号为空，订单号：" + map.get("order_id"));
			return;
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stream_id", map.get("stream_id"));
		paramMap.put("order_id", map.get("order_id"));
		paramMap.put("refund_seq", requestId);
		paramMap.put("refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
		
		int count = orderService.updateRefundStreamBegin(paramMap);//更新退款请求开始
	}
	
	private void sendRefundStreamRequest(Map<String, String> refundMap){
		String requestId = refundMap.get("refund_seq");//ASP退款请求流水号
		logger.info("请求退款！订单号："+refundMap.get("order_id")+"; 退款流水号："+requestId);
		if(StringUtils.isEmpty(requestId)){
			logger.info("【退款流水接口】退款流水号为空，订单号：" + refundMap.get("order_id"));
			return;
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stream_id", refundMap.get("stream_id"));
		paramMap.put("order_id", refundMap.get("order_id"));
		paramMap.put("refund_seq", requestId);
		paramMap.put("refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
		
		int count = orderService.updateRefundStreamBegin(paramMap);//更新退款请求开始
		
		if(count == 1){
			if(TrainConsts.REFUND_TYPE_1.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】用户退款，订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_2.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】差额退款，订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_3.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】出票失败退款，订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_4.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】改签差额退款，订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_5.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】改签单退款，订单号：" + refundMap.get("order_id"));
			}
		}else{
			logger.info("【退款流水接口】更新退款开始异常，退款请求取消！" + refundMap.get("order_id"));
			return;
		}
		
		String version = "2.00";
		String orderId = refundMap.get("order_id");
		try {
			BigDecimal bg = new BigDecimal(Double.parseDouble(refundMap.get("refund_money")));
			double amount = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			String ts = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2);
			DecimalFormat df = new DecimalFormat("0.00"); 
			
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("version", version);
			hashMap.put("merchantId", merchant_id);
			hashMap.put("mxResq", requestId);
			hashMap.put("oriPayOrderId", orderId);
			hashMap.put("amount", df.format(amount));
	       	hashMap.put("notifyUrl", refund_notifyUrl);
	       	hashMap.put("oriPayDate", ts);
	       	hashMap.put("tradeDesc", "train_refund");
	       	hashMap.put("tradeType", "E_BANK");
	       	hashMap.put("currencyType", "RMB");
	       	hashMap.put("remark", "");
	       	hashMap.put("reserved", "");
	       	String hmac_src = getValuesBySort(hashMap, null);
	       	String verify = KeyedDigestMD5.getKeyedDigestUTF8(hmac_src, merchant_key);
			//-- 请求报文
			StringBuffer buf = new StringBuffer();
			buf.append("version=").append(version);
			buf.append("&merchantId=").append(merchant_id);
			buf.append("&mxResq=").append(requestId);
			buf.append("&oriPayOrderId=").append(orderId);
			buf.append("&amount=").append(df.format(amount));
			buf.append("&notifyUrl=").append(refund_notifyUrl);
			buf.append("&oriPayDate=").append(ts);
			buf.append("&tradeDesc=train_refund");
			buf.append("&tradeType=E_BANK");
			buf.append("&currencyType=RMB");
			buf.append("&remark=").append("&reserved=");
			buf.append("&verifyString=").append(verify);
			
			StringBuffer param_log = new StringBuffer();
			param_log.append("排序后带加密参数：").append(hmac_src).append(";加密后结果：").append(verify)
					.append("请求报文：").append(buf.toString());
			logger.info(param_log.toString());
        	String res = HttpUtil.sendAndRecive(refund_url, buf.toString(),characterSet_GBK);
			//获取返回码
			String status = HttpUtil.getValue(res, "status");
			logger.info("【退款流水接口】status="+status+"，orderId="+ orderId);
			
			//手机支付返回报文的消息摘要，用于商户验签
			HashMap<String, String> reHashMap = new HashMap<String, String>();
			reHashMap.put("version", HttpUtil.getValue(res, "version"));
			reHashMap.put("mxResq", HttpUtil.getValue(res, "mxResq"));
			reHashMap.put("status", HttpUtil.getValue(res, "status"));
			reHashMap.put("retCode", HttpUtil.getValue(res, "retCode"));
			reHashMap.put("oriPayOrderId", HttpUtil.getValue(res, "oriPayOrderId"));
			reHashMap.put("amount", HttpUtil.getValue(res, "amount"));
			reHashMap.put("fee", HttpUtil.getValue(res, "fee"));
	       	String re_hmac_src = getValuesBySort(reHashMap, null);
	     // -- 验证签名
	       	String re_verify = KeyedDigestMD5.getKeyedDigestUTF8(re_hmac_src, merchant_key);
			logger.info("【退款流水接口】退款返回数据:"+re_hmac_src);
			if(!re_verify.equals(HttpUtil.getValue(res, "verifyString"))){
				StringBuffer err_log = new StringBuffer();
				err_log.append("【退款流水接口】对退款返回数据验签失败，orderId=").append(orderId)
						.append("本地加密：").append(re_verify).append("；19pay加密：").append(HttpUtil.getValue(res, "verifyString"));
				logger.info(err_log.toString());
				return;
			}else{
				String now_time = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
				//添加日志
				Map<String,String> logMap = new HashMap<String,String>();
				logMap.put("order_id", orderId);
				logMap.put("opter", "chunqiu");
				logMap.put("order_time", now_time);
				if("SUCCESS".equals(status)){
					logger.info("【退款流水接口】退款申请成功，orderId="+ orderId);
					Map<String, String> param = new HashMap<String, String>();
					param.put("order_id", orderId);
					param.put("refund_seq", requestId);//ASP退款请求流水号
					param.put("refund_status", TrainConsts.REFUND_STREAM_EOP_REFUNDING);//19pay退款中
					param.put("old_refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
					orderService.updateOrderStreamStatus(param);//更新退款状态
					logMap.put("order_optlog", "退款申请成功");
				}else if("FAIL".equals(status)){
					logger.info("【退款流水接口】退款申请失败,失败返回码:"+HttpUtil.getValue(res, "code")+"，orderId="+ orderId);
					Map<String, String> param = new HashMap<String, String>();
					param.put("order_id", orderId);
					param.put("refund_seq", requestId);//ASP退款请求流水号
					param.put("refund_status", TrainConsts.REFUND_STREAM_EOP_FAIL);//19pay退款上申请失败
					param.put("old_refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
					orderService.updateOrderStreamStatus(param);//更新退款状态
					logMap.put("order_optlog", "退款申请失败,失败返回码:"+HttpUtil.getValue(res, "code"));
				}else{
					logger.info("【退款流水接口】退款申请失败，orderId="+ orderId);
					logMap.put("order_optlog", "退款申请失败");
				}
				orderService.addOrderinfoHistory(logMap);
			}
		} catch (Exception e) {
			logger.error("【退款流水接口】发起退款请求Exception，orderId="+orderId, e);
		}

	}

}
