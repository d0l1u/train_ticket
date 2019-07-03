package com.l9e.transaction.pubsub;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jiexun.iface.bean.DeliverBean;
import com.jiexun.iface.bean.RefundBean;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.transaction.vo.RobTicket_Refund;
import com.l9e.util.CreateIDUtil;

@Component("robRefund")
@SuppressWarnings("all")
public class RobRefund {
	private static Logger logger = Logger.getLogger(RobRefund.class);

	@Value("#{propertiesReader[ASP_ID]}")
	private String asp_id;

	@Value("#{propertiesReader[ASP_BIZ_ID]}")
	private String asp_biz_id;// 业务id

	@Value("#{propertiesReader[ASP_PRODUCT_ID]}")
	private String asp_product_id;// 商品ID

	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;// 验签key

	@Value("#{propertiesReader[PAY_RESULT_BACK_NOTIFY_URL]}")
	private String pay_result_back_notify_url;// 支付结果后台通知地址

	@Value("#{propertiesReader[PAY_RESULT_FRONT_NOTIFY_URL]}")
	private String pay_result_front_notify_url;// 支付结果前台页面地址

	@Value("#{propertiesReader[ORDER_DETAIL_URL]}")
	private String order_detail_url;// 订单详情连接地址

	@Value("#{propertiesReader[REFUND_RESULT_NOTIFY_URL]}")
	private String refund_result_notify_url;// 退款完成通知地址

	@Value("#{propertiesReader[query_left_ticket_url]}")
	protected String query_left_ticket_url;// 12306新接口
	
	@Resource
	private RobTicketService robTicketService;
	@Resource
	private CommonService commonService;
	
	public void refundFromEOP(String orderId,String refundMoney,String bizType){
		logger.info("抢票系统进入EOP退款--单号--"+orderId);
		String aspRefundNo = CreateIDUtil.createID("TK_ROB");
		RobTicket_OI oi = new RobTicket_OI();
		oi.setOrderId(orderId);
		RobTicket_OI selectOrderInfo = null;
		try {
			selectOrderInfo = robTicketService.selectOrderInfo(oi);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logger.info("抢票系统进入EOP退款--数据库对象--"+JSON.toJSONString(selectOrderInfo));
		if (StringUtils.isEmpty(refundMoney)) {
			refundMoney = selectOrderInfo.getPayMoney().toString();
		}
		Map<String, String> refundMap = commonService.queryEopInfo(orderId);
		DeliverBean dbean = new DeliverBean();
		dbean.setAsp_verify_key(asp_verify_key);	
		dbean.setPartner_id(asp_id);	
		dbean.setAsp_order_id(orderId);	
		dbean.setEop_order_id(refundMap.get("eop_order_id"));
		dbean.setSend_result("FAIL");//全部失败	
		dbean.setAsp_refund_number(aspRefundNo);
		dbean.setRefund_pay(refundMoney);
		StringBuffer sendNotifyUrl = new StringBuffer();
		sendNotifyUrl.append(refundMap.get("query_result_url"))
				   .append((refundMap.get("query_result_url").indexOf("?")!=-1) ? "?data_type=JSON" : "&data_type=JSON");
		ASPUtil.sendResultInform(dbean, sendNotifyUrl.toString());
		logger.info("抢票系统发货通知EOP操作成功--单号"+orderId+"--EOP系统接收返回结果"+JSON.toJSONString(dbean));
		try {
			RefundBean bean = new RefundBean();
			bean.setAsp_verify_key(asp_verify_key);
			bean.setPartner_id(asp_id);
			bean.setAsp_refund_number(aspRefundNo);
			bean.setEop_order_id(refundMap.get("eop_order_id"));
			bean.setAsp_order_id(orderId);//ASP订单号
			bean.setAsp_refund_money(refundMoney);
			bean.setRefund_reason("用户取消抢票,直接退款");
			StringBuffer eopRefundUrl = new StringBuffer();
			eopRefundUrl.append(refundMap.get("query_result_url"))
					   .append((refundMap.get("query_result_url").indexOf("?")!=-1) ? "?data_type=JSON" : "&data_type=JSON");
			
			ASPUtil.refund(bean, eopRefundUrl.toString());
			RobTicket_Refund refund = new RobTicket_Refund();
			String id = CreateIDUtil.createID(refund.DB_PREFIX);
			refund.setRe_id(id);
			refund.setEop_order_id(bean.getEop_order_id());
			refund.setOpt_log(bizType);
			refund.setOpt_time(new Date());
			refund.setRefund_money(refundMoney);
			refund.setRefund_status(refund.ING);
			refund.setRob_order_id(orderId);
			refund.setEop_refund_seq(bean.getEop_refund_seq());// 当前退款 唯一 流水号
			logger.info("RobTicket_Refund===>"+JSON.toJSONString(refund));
			robTicketService.insertRefund(refund);
			logger.info("抢票系统退款EOP操作--单号"+orderId+"--EOP系统接收返回结果"+JSON.toJSONString(bean));
		} catch (Exception e) {
			logger.error("EOP主动退款 异常 当前orderId===>"+orderId+"===>"+e.toString());
		}
		
		
	}

}
