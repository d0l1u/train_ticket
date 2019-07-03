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
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.OrderService;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;

/**
 * 向EOP请求退款job
 * @author zhangjun
 *
 */
@Component("refundJob")
public class RefundJob {
	
	private static final Logger logger = Logger.getLogger(RefundJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private JoinUsService joinUsService;
	
	@Value("#{propertiesReader[ASP_ID]}")
	private String asp_id;
	
	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;//验签key
	
	public void refund(){
		//订购退款
		List<Map<String, String>> refundList = orderService.queryTimedRefundList();
		for (Map<String, String> refundMap : refundList) {
			this.sendRefundRequest(refundMap);
		}
		
		//加盟退款（每天1点-2点）
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "010000", "yyyyMMddHHmmss");//1:00
		Date end = DateUtil.stringToDate(datePre + "020000", "yyyyMMddHHmmss");//2:00
		if(begin.before(date) && end.after(date)){
			List<Map<String, String>> jmRefundList = joinUsService.queryTimedJmRefundList();
			for (Map<String, String> refundMap : jmRefundList) {
				this.sendJmRefundRequest(refundMap);
			}
		}
	}
	
	private void sendRefundRequest(Map<String, String> refundMap){
		
		String aspRefundNo = CreateIDUtil.createID("TK");//ASP退款请求流水号
		RefundBean bean = new RefundBean();
		bean.setAsp_verify_key(asp_verify_key);
		bean.setPartner_id(asp_id);
		bean.setAsp_refund_number(aspRefundNo);
		bean.setEop_order_id(refundMap.get("eop_order_id"));
		bean.setAsp_order_id(refundMap.get("order_id"));//ASP订单号
		bean.setAsp_refund_money(refundMap.get("refund_money"));
		bean.setRefund_reason("主动退款");
		bean.setRemark("主动退款");
		
		StringBuffer eopRefundUrl = new StringBuffer();
		eopRefundUrl.append(refundMap.get("eop_refund_url"))
				   .append((refundMap.get("eop_refund_url").indexOf("?")!=-1) ? "&data_type=JSON" : "?data_type=JSON");
		
		ASPUtil.refund(bean, eopRefundUrl.toString());

		if("SUCCESS".equalsIgnoreCase(bean.getResult_code())){//申请成功，接受退款申请
		    /*
		     * 更新订单信息
		     */
			Map<String, String> map = new HashMap<String, String>();
			map.put("asp_order_id", refundMap.get("order_id"));
			map.put("asp_refund_seq", aspRefundNo);//ASP退款请求流水号
		    map.put("eop_refund_seq", bean.getEop_refund_seq());//EOP退款流水号
		    map.put("refund_status", TrainConsts.REFUND_EOP_REFUNDING);//EOP正在退款
		    map.put("old_refund_status", TrainConsts.REFUND_REFUNDING);//正在退款
		    orderService.updateOrderRefund(map);
			
			logger.info("【退款接口】申请成功，接受退款申请" + "EOP订单号：" + bean.getEop_order_id()
					+ "，EOP退款流水号：" + bean.getEop_refund_seq());
			
		}else if("REFUNDING".equalsIgnoreCase(bean.getResult_code())){//重复退款申请-退款中
		    
			logger.info("【退款接口】重复退款申请-退款中" + "EOP订单号：" + bean.getEop_order_id()
					+ "，EOP退款流水号：" + bean.getEop_refund_seq());
			
		}else if("REFUND ".equalsIgnoreCase(bean.getResult_code())){//重复退款申请-已完成
		    
			logger.info("【退款接口】重复退款申请-已完成" + "EOP订单号：" + bean.getEop_order_id()
					+ "，EOP退款流水号：" + bean.getEop_refund_seq());
		}else{//申请失败
			
			logger.info("【退款接口】申请失败" + "EOP订单号：" + bean.getEop_order_id()
					+ "，失败原因：" + bean.getResult_desc());
		}

	}
	
	private void sendJmRefundRequest(Map<String, String> refundMap){
		
		String aspRefundNo = CreateIDUtil.createID("TK");//ASP退款请求流水号
		RefundBean bean = new RefundBean();
		bean.setAsp_verify_key(asp_verify_key);
		bean.setPartner_id(asp_id);
		bean.setAsp_refund_number(aspRefundNo);
		bean.setEop_order_id(refundMap.get("eop_order_id"));
		bean.setAsp_order_id(refundMap.get("order_id"));//ASP订单号
		bean.setAsp_refund_money(refundMap.get("refund_money"));
		bean.setRefund_reason("主动退款");
		bean.setRemark("加盟退款");
		
		StringBuffer eopRefundUrl = new StringBuffer();
		eopRefundUrl.append(refundMap.get("eop_refund_url"))
				   .append((refundMap.get("eop_refund_url").indexOf("?")!=-1) ? "?data_type=JSON" : "&data_type=JSON");
		
		ASPUtil.refund(bean, eopRefundUrl.toString());

		if("SUCCESS".equalsIgnoreCase(bean.getResult_code())){//申请成功，接受退款申请
		    /*
		     * 更新订单信息
		     */
			Map<String, String> map = new HashMap<String, String>();
			map.put("asp_order_id", refundMap.get("order_id"));
			map.put("asp_refund_seq", aspRefundNo);//ASP退款请求流水号
		    map.put("eop_refund_seq", bean.getEop_refund_seq());//EOP退款流水号
		    map.put("order_status", "34");//EOP正在退款
		    map.put("old_status", "33");//等待退款
		    joinUsService.updateJmOrderEopInfo(map);
			
			logger.info("【退款接口】申请成功，接受退款申请" + "EOP订单号：" + bean.getEop_order_id()
					+ "，EOP退款流水号：" + bean.getEop_refund_seq());
			
		}else if("REFUNDING".equalsIgnoreCase(bean.getResult_code())){//重复退款申请-退款中
		    
			logger.info("【退款接口】重复退款申请-退款中" + "EOP订单号：" + bean.getEop_order_id()
					+ "，EOP退款流水号：" + bean.getEop_refund_seq());
			
		}else if("REFUND ".equalsIgnoreCase(bean.getResult_code())){//重复退款申请-已完成
		    
			logger.info("【退款接口】重复退款申请-已完成" + "EOP订单号：" + bean.getEop_order_id()
					+ "，EOP退款流水号：" + bean.getEop_refund_seq());
		}else{//申请失败
			
			logger.info("【退款接口】申请失败" + "EOP订单号：" + bean.getEop_order_id()
					+ "，失败原因：" + bean.getResult_desc());
		}

	}

}
