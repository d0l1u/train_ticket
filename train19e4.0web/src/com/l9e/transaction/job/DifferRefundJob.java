package com.l9e.transaction.job;

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
import com.l9e.util.CreateIDUtil;

/**
 * 向EOP请求差额退款job
 * @author zhangjun
 *
 */
@Component("differRefundJob")
public class DifferRefundJob {
	
	private static final Logger logger = Logger.getLogger(DifferRefundJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[ASP_ID]}")
	private String asp_id;
	
	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;//验签key
	
	public void differRefund(){
		//差额退款
		List<Map<String, String>> differList = orderService.queryTimedDifferRefundList();
		for (Map<String, String> differMap : differList) {
			this.sendDifferRefundRequest(differMap);
		}
	}
	
	private void sendDifferRefundRequest(Map<String, String> differMap){
		
		String aspRefundNo = CreateIDUtil.createID("CE");//ASP退款请求流水号
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", differMap.get("order_id"));
		paramMap.put("refund_seq", aspRefundNo);
		paramMap.put("eop_order_id", differMap.get("eop_order_id"));
		paramMap.put("differ_status", TrainConsts.DIFFER_BEGIN_REFUND);//开始退款
		
		int count = orderService.updateDifferBegin(paramMap);//更新差额退款请求开始
		
		if(count == 1){
			RefundBean bean = new RefundBean();
			bean.setAsp_verify_key(asp_verify_key);
			bean.setPartner_id(asp_id);
			bean.setAsp_refund_number(aspRefundNo);
			bean.setEop_order_id(differMap.get("eop_order_id"));
			bean.setAsp_order_id(differMap.get("order_id"));//ASP订单号
			bean.setAsp_refund_money(differMap.get("refund_money"));
			bean.setRefund_reason("差额退款");
			bean.setRemark("差额退款");
			
			StringBuffer eopRefundUrl = new StringBuffer();
			eopRefundUrl.append(differMap.get("eop_refund_url"))
					   .append((differMap.get("eop_refund_url").indexOf("?")!=-1) ? "?data_type=JSON" : "&data_type=JSON");
			
			ASPUtil.refund(bean, eopRefundUrl.toString());
	
			if("SUCCESS".equalsIgnoreCase(bean.getResult_code())){//申请成功，接受退款申请
			    /*
			     * 更新差额退款信息
			     */
				Map<String, String> map = new HashMap<String, String>();
				map.put("order_id", differMap.get("order_id"));
				map.put("refund_seq", bean.getAsp_refund_number());//ASP退款请求流水号
			    map.put("eop_refund_seq", bean.getEop_refund_seq());//EOP退款流水号
			    map.put("differ_status", TrainConsts.DIFFER_EOP_REFUNDING);//EOP退款中
			    map.put("old_differ_status", TrainConsts.DIFFER_BEGIN_REFUND);//开始退款
			    orderService.updateOrderDiffer(map);
				
				logger.info("【差额退款接口】申请成功，接受差额退款申请" + "ASP订单号：" + bean.getAsp_order_id()
						+ "，ASP退款流水号：" + bean.getAsp_refund_number());
				
			}else if("REFUNDING".equalsIgnoreCase(bean.getResult_code())){//重复退款申请-退款中
			    
				logger.info("【差额退款接口】重复退款申请-退款中" + "ASP订单号：" + bean.getAsp_order_id()
						+ "，ASP退款流水号：" + bean.getAsp_refund_number());
				
			}else if("REFUND ".equalsIgnoreCase(bean.getResult_code())){//重复退款申请-已完成
			    
				logger.info("【差额退款接口】重复退款申请-已完成" + "ASP订单号：" + bean.getAsp_order_id()
						+ "，ASP退款流水号：" + bean.getAsp_refund_number());
			}else{//申请失败
				
				logger.info("【差额退款接口】申请失败" + "ASP订单号：" + bean.getAsp_order_id()
						+ "，失败原因：" + bean.getResult_desc());
			}
		}

	}

}
