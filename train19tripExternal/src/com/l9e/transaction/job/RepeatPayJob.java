package com.l9e.transaction.job;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jiexun.iface.bean.NoPwdPayBean;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.util.DateUtil;


/**
 * 向平台发送订单重新支付
 * @author zuoyuxing
 *
 */
@Component("reatPayJob")
@Deprecated
public class RepeatPayJob {
	private static final Logger logger = Logger.getLogger(RepeatPayJob.class);
	@Value("#{propertiesReader[terminal]}")
	protected String terminal;		//terminal
	
	@Value("#{propertiesReader[pay_sign_key]}")
	protected String pay_sign_key;		//平台密钥
	
	@Value("#{propertiesReader[biz_id]}")
	protected String biz_id;		//平台分配火车票业务ID
	
	@Value("#{propertiesReader[partner_id]}")
	protected String partner_id;	//平台EOP分配的ID
	
	@Resource
	ReceiveNotifyService receiveService;
	
	@Resource
	private OrderService orderService;
	
	public void sendPayResultData(){
		List<Map<String,String>> listNotify = receiveService.findPayResultNotify();
		if(listNotify !=null && listNotify.size()>0 ){
			for(Map<String,String> map : listNotify){
				String order_id = map.get("order_id");
				logger.info("订单号："+order_id+"，发起订单平台支付！！");
				//向平台支付
				try {
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("order_id", map.get("order_id"));
					paramMap.put("merchant_order_id", map.get("merchant_order_id"));
					Map<String, String> orderMap = orderService.queryOrderInfoByMap(paramMap);
//					orderMap.put("ip_address", user_ip);
					NoPwdPayBean bean = new NoPwdPayBean();
					bean.setService("nopwd_pay");
					bean.setSign_type("MD5");
					bean.setTimestamp(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2));
					bean.setPartner_id(partner_id);
					bean.setInput_charset("UTF-8");
					bean.setTerminal(terminal);
					bean.setAgent_id(map.get("agent_id"));
					bean.setBiz_id(biz_id);
					bean.setEop_order_id(orderMap.get("eop_order_id"));
					bean.setAsp_order_id(orderMap.get("order_id"));
					Object pay_money = orderMap.get("pay_money");
					NumberFormat objFormat =new DecimalFormat("#.00");
					bean.setPay_money(objFormat.format(Double.valueOf(pay_money.toString())).toString());
					bean.setPay_point("0");
					bean.setIp_address(orderMap.get("ip_address"));
					bean.setAsp_verify_key(pay_sign_key);
					NoPwdPayBean noBean = ASPUtil.noPwdPay(bean, orderMap.get("nopwd_pay_url")+"?data_type=JSON");
					
					ExternalLogsVo logs=new ExternalLogsVo();
					logs.setOrder_id(order_id);
					logs.setOpter("ext_app");
					if("SUCCESS".equals(noBean.getResult_code())){
						logger.error("调用重新通知19pay支付接口成功！"+bean.getAsp_order_id());
						receiveService.updatePayResultNotify(order_id);
						
						logs.setOrder_optlog("重新通知eop平台支付接口成功");
						orderService.insertOrderLogs(logs);
					}else if("MONEYLESS".equals(noBean.getResult_code())){
						logger.error("调用重新通知19pay支付接口失败！"+bean.getAsp_order_id()+"失败原因：余额不足！！");
						Map<String,String> param = new HashMap<String,String>();
						param.put("order_id", order_id);
						param.put("fail_reason", noBean.getResult_desc());
						receiveService.updatePayResultNotifyStatus(param);
						
						logs.setOrder_optlog("重新通知eop平台支付失败");
						orderService.insertOrderLogs(logs);
					}else{
						logger.error("调用通知19pay支付接口失败！"+bean.getAsp_order_id()+"失败原因："+noBean.getResult_desc());
						//更新通知表通知次数
						Map<String,String> param = new HashMap<String,String>();
						param.put("order_id", order_id);
						param.put("fail_reason", noBean.getResult_desc());
						receiveService.updatePayResultNotifyStatus(param);
						
						logs.setOrder_optlog("重新通知eop平台支付接口成功");
						orderService.insertOrderLogs(logs);
					}
				}catch(Exception e){ 
					logger.error("调用通知19pay支付接口失败！", e);
				}
			}
		}
	}
}
