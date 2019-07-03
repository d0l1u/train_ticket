package com.l9e.transaction.job;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.EmappSignService;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.PayUtil;

/**
 * 19pay出票结果通知job
 * @author yangchao
 *
 */
@Component("OrderReNotifyJob")
public class OrderReNotifyJob {
	
	private static final Logger logger = Logger.getLogger(OrderReNotifyJob.class);
	
	@Resource
	private OrderService orderService;
	
	
	@Value("#{propertiesReader[appInitKey]}")
	private String appInitKey;//静态验签key
	
	@Value("#{propertiesReader[characterSet]}")
	private String characterSet;//编码
	
	public void resultNotify(){
		List<Map<String, String>> resultList = orderService.queryTimedOrderReList();
		if(resultList.size()>0 && resultList!=null){
			for (Map<String, String> resultMap : resultList) {
				this.resultNotifyRequest(resultMap);
			}
		}
	}
	
	private void resultNotifyRequest(Map<String, String> resultMap){
		logger.info("出票结果通知");
		String orderId = resultMap.get("order_id");
		
		//修改状态为开始通知,同时通知次数加一
		Map<String,String> map=new HashMap<String,String>();
		map.put("notify_status", "11");//开始通知
		map.put("order_id", orderId);
		map.put("plat_order_id", resultMap.get("plat_order_id"));
		orderService.updateReNotifyStatus(map);
		
		try {
			//根据商家订单号查询出票结果通知地址
			OrderInfo orderInfo = orderService.queryOrderInfo(orderId);
			String resultNotifyUrl=orderInfo.getTicket_url();
			//19pay订单号
			String plat_order_id=resultMap.get("plat_order_id");
			//订单状态
			String status=resultMap.get("order_status");
			if("44".equals(status)){
				status="0";
				logger.info("【出票结果通知】出票成功通知！订单号："+orderId);
			}else if("45".equals(status)){
				status="1";
				logger.info("【出票结果通知】出票失败通知！订单号："+orderId);
			}
			//商家时间戳
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String mxdate = sdf.format(new Date());
			String extend1="";
			String extend2="";
			String hmac="";
			
			String req_param="";
			String[][] resParam=new String[6][2];
			resParam[0][0]="mxorderid";
			resParam[0][1]=orderId;
			resParam[1][0]="mxdate";
			resParam[1][1]=mxdate;
			resParam[2][0]="status";
			resParam[2][1]=status;
			resParam[3][0]="extend1";
			resParam[3][1]=extend1;
			resParam[4][0]="extend2";
			resParam[4][1]=extend2;
			resParam[5][0]="orderid";
			resParam[5][1]=plat_order_id;
			req_param=EmappSignService.creatResSign(resParam, appInitKey);
			
			String [] ticketurl=resultNotifyUrl.split("\\?");
			String []  para=ticketurl[1].split("\\=");
			StringBuffer sbPara=new StringBuffer();
			sbPara.append("extend1=").append(URLEncoder.encode(extend1, characterSet))
				  .append("&extend2=").append(URLEncoder.encode(extend2, characterSet))
				  .append("&mxdate=").append(URLEncoder.encode(mxdate, characterSet))
				  .append("&mxorderid=").append(URLEncoder.encode(orderId, characterSet))
				  .append("&orderid=").append(URLEncoder.encode(plat_order_id, characterSet))
				  .append("&status=").append(URLEncoder.encode(status, characterSet))
				  .append("&"+para[0]+"=").append(URLEncoder.encode(para[1], characterSet));
			
			hmac=PayUtil.getValue(req_param, "hmac");
			logger.info("【出票结果通知】出票结果通知请求地址："+ticketurl[0]+" 请求参数："+sbPara.toString().trim()+"签名报文："+hmac);
			
			//发起http请求，并获取响应
			String res = "";
			try{
				res=HttpPostUtil.sendAndRecive(ticketurl[0],sbPara.toString().trim()+"&hmac="+hmac);
			}catch(Exception e){
				logger.error("【出票结果通知】订单号："+orderId+"---发起出票结果通知失败！", e);
				return;
			}
			if("Y".equals(res)){
				//修改通知状态为通知成功,通知完成时间为当前时间
				orderService.updateReNoStatusTo22(resultMap);
				logger.info("【出票结果通知】订单号："+orderId+"---出票结果通知成功,返回信息:"+res+",更新通知状态为通知完成!");
			}else{
				logger.info("【出票结果通知】订单号："+orderId+"---出票结果通知失败,返回信息:"+res);
			}
		} catch (Exception e) {
			logger.error("【出票结果通知】订单号："+orderId+"---出票结果通知失败！");
		}
	}

}
