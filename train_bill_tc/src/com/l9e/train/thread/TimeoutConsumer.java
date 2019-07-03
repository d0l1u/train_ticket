package com.l9e.train.thread;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.train.po.BillOrder;
import com.l9e.train.producerConsumer.nodistinct.NoDistinctConsumer;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.BillConst;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;
import com.l9e.train.util.MD5;

public class TimeoutConsumer extends NoDistinctConsumer<BillOrder> {

	public static final String CHANNEL = "10";

	private Logger logger=Logger.getLogger(this.getClass());
	private TrainServiceImpl service = new TrainServiceImpl();
	

	@Override
	public boolean consume(BillOrder billOrder) {
		
		logger.info("结算通知消费者消费一个记录：" + billOrder.getBillId());
		
		try {
			String optlog = "";
			
			optlog = "开始同步结算数据";
			service.insertHistory(billOrder, optlog);
			
			String requestUrl = requestUrl(billOrder);
			
			logger.info("请求地址为：" + requestUrl);
			if(requestUrl == null || "".equals(requestUrl)) {
				logger.info("通知失败，请求参数异常，bill_id" + billOrder.getBillId());
				optlog = "通知失败，失败原因[参数异常!]";
			}
			
			String result = null;
			try {
				result = HttpUtil.sendByGet(requestUrl, "UTF-8", null, null);
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.info("请求异常，" + requestUrl);
			}
			
			logger.info(billOrder.getBillId() + ",请求返回结果,result:" + result);
			if(result == null) {
				optlog = "通知失败，失败原因[请求异常或超时!]";
			} else {
				result = result.toLowerCase();
				
				if(result.equals("succeed")) {
					optlog = "对账通知成功,通知结果【" + result + "】";
					logger.info(billOrder.getBillId() + "对账通知成功!");
					service.orderIsSuccess(billOrder, result);
				} else if(result.equals("failed")) {
					optlog = "对账通知失败,通知结果【" + result + "】";
					logger.info(billOrder.getBillId() + "对账通知失败!");
					service.orderIsFailure(billOrder, result);
				} else {
					optlog = "对账通知失败,返回结果异常，通知结果【" + result + "】";
					logger.info(billOrder.getBillId() + "对账通知失败!");
					service.orderIsFailure(billOrder, result);
				}
			}
			
			/*记录日志*/
			service.insertHistory(billOrder, optlog);	
			logger.info("通知结束bill_id = " + billOrder.getBillId() + " status: " + result);
			
		} catch (Exception e) {
			logger.warn("exception!!:"+e);
		}
		return true;
	}
	
	@Override
	public String getObjectKeyId(BillOrder t) {
		return t.getOrderId();
	}

	private String requestUrl(BillOrder billOrder) {
		
		ObjectMapper mapper = new ObjectMapper();
		/*时间参数格式化*/
		
		/*生成param参数json串*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_no", billOrder.getOrderId());
		paramMap.put("ticket_no", billOrder.getOut_ticket_billno());
		paramMap.put("amount", billOrder.getAmount());
		paramMap.put("settlement_type", billOrder.getSettlementType());
		paramMap.put("quantity", billOrder.getQuantity());
		paramMap.put("trade_date", DateUtil.dateToString(billOrder.getTradeDate(), DateUtil.DATE_FMT3));
		paramMap.put("settlement_date", DateUtil.dateToString(billOrder.getSettlementDate(), DateUtil.DATE_FMT1));
		paramMap.put("channel", CHANNEL);
		paramMap.put("account_balance", billOrder.getAccountBalance());
		paramMap.put("order_type", 0);
		paramMap.put("unique", UUID.randomUUID().toString() );
		String paramJson = null;
		try {
			paramJson = mapper.writeValueAsString(paramMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(paramJson == null) 
			return null;
		
		/*请求参数排序并生成签名*/
//		Map<String, String> queryMap = new HashMap<String, String>();
//		queryMap.put("param", paramJson);
//		queryMap.put("md5key", BillConst.MD5_KEY);
//		List<String> keyList = new ArrayList<String>(queryMap.keySet());
//		Collections.sort(keyList);
		
		/*拼接querystring*/
		StringBuilder builder = new StringBuilder();
		builder.append("param=")
			.append(paramJson)
			.append("&")
			.append("md5key=")
			.append(BillConst.MD5_KEY);
		
		String sign = MD5.getMd5_UTF8(builder.toString()).toUpperCase();
		
		try {
			paramJson = URLEncoder.encode(paramJson, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		builder.setLength(0);
		builder.append(BillConst.BILL_SYNC_URL)
			.append("?")
			.append("param=")
			.append(paramJson)
			.append("&")
			.append("sign=")
			.append(sign);
		
		return builder.toString();
	}
}