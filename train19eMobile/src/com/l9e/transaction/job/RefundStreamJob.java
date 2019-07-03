package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jiexun.iface.bean.RefundBean;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;

/**
 * 扫描退款流水发起退款请求job
 * @author zhangjun
 *
 */
@Component("refundStreamJob")
public class RefundStreamJob {
	
	private static final Logger logger = Logger.getLogger(RefundStreamJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[ASP_ID]}")
	private String asp_id;
	
	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;//验签key
	
	public void refund(){
		List<Map<String, String>> refundList = orderService.queryTimedRefundStreamList();
		for (Map<String, String> refundMap : refundList) {
			this.sendRefundStreamRequest(refundMap);
		}
	}
	
	private void sendRefundStreamRequest(Map<String, String> refundMap){
		
		String aspRefundNo = refundMap.get("refund_seq");//ASP退款请求流水号
		if(StringUtils.isEmpty(aspRefundNo)){
			logger.info("【退款流水接口】ASP退款流水号为空，ASP订单号：" + refundMap.get("order_id"));
			return;
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stream_id", refundMap.get("stream_id"));
		paramMap.put("order_id", refundMap.get("order_id"));
		paramMap.put("refund_seq", aspRefundNo);
		paramMap.put("eop_order_id", refundMap.get("eop_order_id"));
		paramMap.put("refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
		
		int count = orderService.updateRefundStreamBegin(paramMap);//更新退款请求开始
		
		if(count == 1){
			RefundBean bean = new RefundBean();
			bean.setAsp_verify_key(asp_verify_key);
			bean.setPartner_id(asp_id);
			bean.setAsp_refund_number(aspRefundNo);
			bean.setEop_order_id(refundMap.get("eop_order_id"));
			bean.setAsp_order_id(refundMap.get("order_id"));//ASP订单号
			bean.setAsp_refund_money(refundMap.get("refund_money"));
			
			if(TrainConsts.REFUND_TYPE_1.equals(refundMap.get("refund_type"))){
				bean.setRefund_reason("用户退款，cp_id="+refundMap.get("cp_id"));
				bean.setRemark("用户主动发起退款");
				logger.info("【退款流水接口】用户退款，ASP订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_2.equals(refundMap.get("refund_type"))){
				bean.setRefund_reason("差额退款");
				bean.setRemark("差额退款");
				logger.info("【退款流水接口】差额退款，ASP订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_3.equals(refundMap.get("refund_type"))){
				bean.setRefund_reason("出票失败退款");
				bean.setRemark("出票失败系统发起退款");
				logger.info("【退款流水接口】出票失败退款，ASP订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_4.equals(refundMap.get("refund_type"))){
				bean.setRefund_reason("改签差额退款");
				bean.setRemark("改签差额退款");
				logger.info("【退款流水接口】改签差额退款，ASP订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_5.equals(refundMap.get("refund_type"))){
				bean.setRefund_reason("改签单退款");
				bean.setRemark("改签单退款");
				logger.info("【退款流水接口】改签单退款，ASP订单号：" + refundMap.get("order_id"));
			}
			
			StringBuffer eopRefundUrl = new StringBuffer();
			if(StringUtils.isEmpty(refundMap.get("eop_refund_url"))){
				logger.info("【退款流水接口】申请退款地址eop_refund_url为空，跳过本条数据");
				return;				
			}
			eopRefundUrl.append(refundMap.get("eop_refund_url"))
					   .append((refundMap.get("eop_refund_url").indexOf("?")!=-1) ? "?data_type=JSON" : "&data_type=JSON");
			
			ASPUtil.refund(bean, eopRefundUrl.toString());
	
			if("SUCCESS".equalsIgnoreCase(bean.getResult_code())){//申请成功，接受退款申请
			    /*
			     * 申请成功更新退款状态信息
			     */
				Map<String, String> map = new HashMap<String, String>();
				map.put("stream_id", refundMap.get("stream_id"));
				map.put("order_id", refundMap.get("order_id"));
				map.put("refund_seq", bean.getAsp_refund_number());//ASP退款请求流水号
			    map.put("eop_refund_seq", bean.getEop_refund_seq());//EOP退款流水号
			    map.put("refund_status", TrainConsts.REFUND_STREAM_EOP_REFUNDING);//EOP退款中
			    map.put("old_refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
			    orderService.updateOrderStreamStatus(map);
				
				logger.info("【退款流水接口】申请成功，接受退款申请" + "ASP订单号：" + bean.getAsp_order_id()
						+ "，ASP退款流水号：" + bean.getAsp_refund_number());
				
			}else if("REFUNDING".equalsIgnoreCase(bean.getResult_code())){//重复退款申请-退款中
			    
				logger.info("【退款流水接口】重复退款申请-退款中" + "ASP订单号：" + bean.getAsp_order_id()
						+ "，ASP退款流水号：" + bean.getAsp_refund_number());
				
			}else if("REFUND".equalsIgnoreCase(bean.getResult_code())){//重复退款申请-已完成
			    
				logger.info("【退款流水接口】重复退款申请-已完成" + "ASP订单号：" + bean.getAsp_order_id()
						+ "，ASP退款流水号：" + bean.getAsp_refund_number());
			}else{//申请失败
				logger.info("【退款流水接口】result_code="+bean.getResult_code());
				logger.info("【退款流水接口】申请失败" + "ASP订单号：" + bean.getAsp_order_id()
						+ "，失败原因：" + bean.getResult_desc());
			}
		}

	}

}
