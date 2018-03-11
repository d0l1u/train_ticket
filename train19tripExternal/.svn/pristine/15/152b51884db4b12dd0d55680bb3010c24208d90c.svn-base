package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jiexun.iface.bean.RefundBean;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.RefundService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.util.DateUtil;

/**
 * 退款结果通知job
 * @author zhangjun
 *
 */
@Component("eopRefundStreamJob")
public class RefundEopJob {
	
	private static final Logger logger = Logger.getLogger(RefundEopJob.class);
	@Value("#{propertiesReader[pay_sign_key]}")
	protected String pay_sign_key;		//平台密钥
	
	@Value("#{propertiesReader[biz_id]}")
	protected String biz_id;		//平台分配火车票业务ID
	
	@Value("#{propertiesReader[good_ids]}")
	protected String good_ids;		//平台分配火车票商品ID
	
	@Value("#{propertiesReader[partner_id]}")
	protected String partner_id;	//平台EOP分配的ID
	
	@Resource
	private RefundService refundService;
	
	@Resource
	private OrderService orderService;
	
	public void refundEop(){
		//定时查询待通知退票结果的列表
		List<Map<String, String>> notifyList = refundService.queryEopRefundResultWaitList();
		for (Map<String, String> notifyMap : notifyList) {
			String order_id = notifyMap.get("order_id");
			String refund_seq = notifyMap.get("refund_seq");
			try {
				this.sendEopRefundNotfiyRequest(notifyMap);
			} catch (Exception e) {
				logger.error("发起退票结果通知异常，order_id="
						+order_id+"&refund_seq="+refund_seq, e);
			}
		}
	}
	
	private void sendEopRefundNotfiyRequest(Map<String, String> notifyMap) throws Exception{
		String order_id = notifyMap.get("order_id");
		logger.info("发起eop退款通知,订单号："+order_id);
		String refund_seq = notifyMap.get("refund_seq");
		String notify_id = notifyMap.get("notify_id");
		
		//更新通知开始
		Map<String, String> beginMap = new HashMap<String, String>();
		beginMap.put("notify_id", notify_id);
		beginMap.put("order_id", order_id);
		beginMap.put("notify_status", TrainConsts.REFUND_NOTIFY_BEGIN);//开始通知
		beginMap.put("old_notify_status", notifyMap.get("notify_status"));//旧状态
		int count = refundService.updateEopRefundNotfiyBegin(beginMap);
		if(count == 0){
			logger.info("发起退票结果通知状态已经变化！order_id="+order_id);
			return;
		}
		RefundBean refundBean = new RefundBean();
		refundBean.setService("refund");
		refundBean.setSign_type("MD5");
		refundBean.setTimestamp(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2));
		refundBean.setPartner_id(partner_id);
		refundBean.setInput_charset("UTF-8");
		refundBean.setAsp_refund_number(refund_seq);
		String refund_money = notifyMap.get("refund_money");
		refundBean.setAsp_refund_money(String.format("%.2f", Double.valueOf(refund_money)));
		refundBean.setRefund_reason(notifyMap.get("refund_reason"));
		refundBean.setRemark("电商代购退款");
		refundBean.setEop_order_id(notifyMap.get("eop_order_id"));
		refundBean.setAsp_order_id(order_id);
		refundBean.setAsp_verify_key(pay_sign_key);
		RefundBean reBean = ASPUtil.refund(refundBean, notifyMap.get("notify_url")+"?data_type=JSON");
		ExternalLogsVo logs=new ExternalLogsVo();
		logs.setOrder_id(order_id);
		logs.setOpter("ext_app");
		
		if("0.0".equals(refund_money)){
			//更新通知完成
			Map<String, String> map = new HashMap<String, String>();
			map.put("notify_id", notify_id);
			map.put("order_id", order_id);
			map.put("refund_seq", refund_seq);
			map.put("eop_refund_seq", reBean.getEop_refund_seq());
			map.put("notify_status", TrainConsts.REFUND_NOTIFY_FINISH);
			refundService.updateEopRefundNotfiyFinish(map);
			logger.info("退票结果通知平台成功" + "订单号：" + order_id
					+ "，退款流水号：" + refund_seq);
			logs.setOrder_optlog("退票结果通知eop平台成功");
			orderService.insertOrderLogs(logs);
			return;
		}
		if("SUCCESS".equals(reBean.getResult_code())){
			//更新通知完成
			Map<String, String> map = new HashMap<String, String>();
			map.put("notify_id", notify_id);
			map.put("order_id", order_id);
			map.put("refund_seq", refund_seq);
			map.put("eop_refund_seq", reBean.getEop_refund_seq());
			map.put("notify_status", TrainConsts.REFUND_NOTIFY_FINISH);
			refundService.updateEopRefundNotfiyFinish(map);
			logger.info("退票结果通知平台成功" + "订单号：" + order_id
					+ "，退款流水号：" + refund_seq);
			logs.setOrder_optlog("退票结果通知eop平台成功");
		}else if("REFUNDING".equals(reBean.getResult_code())){
			logger.info("退票结果通知平台重复" + "订单号：" + order_id
					+ "，退款流水号：" + refund_seq);
			//更新通知完成
			Map<String, String> map = new HashMap<String, String>();
			map.put("notify_id", notify_id);
			map.put("order_id", order_id);
			map.put("eop_refund_seq", reBean.getEop_refund_seq());
			map.put("notify_status", TrainConsts.REFUND_NOTIFY_FINISH);
			refundService.updateEopRefundNotfiyFinish(map);
			logs.setOrder_optlog("退票结果通知eop平台重复");
		}else{
			logger.info("退票结果通知平台失败" + "订单号：" + order_id
					+ "，退款流水号：" + refund_seq+";失败原因："+reBean.getResult_desc());
			logs.setOrder_optlog("退票结果通知eop平台失败，失败原因："+reBean.getResult_desc());
		}
		orderService.insertOrderLogs(logs);
	}

}
