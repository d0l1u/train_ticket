package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.InterAccount;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.QunarOrder;
import com.l9e.transaction.vo.QunarOrderPack;
import com.l9e.transaction.vo.SysConfig;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;

/**
 * 获取退票订单job
 * @author zhangjun
 *
 */
@Component("queryRefundJob")
public class QueryRefundJob {
	
	private static final Logger logger = Logger.getLogger(QueryRefundJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[qunarReqUrl]}")
	private String qunarReqUrl;//Qunar请求地址
	
	public void queryRefund() throws Exception{
		
		/**获取各账号下的数据**/
		for(InterAccount account : SysConfig.accountContainer){
			queryFromPerMerchant(account);
		}
	}

	private void queryFromPerMerchant(InterAccount account) throws Exception{

		String md5Key = account.getMd5Key();
		String merchantCode = account.getMerchantCode();
		String order_source = account.getName();
		
		String logPre = "【获取退票<"+order_source+">】";
		
		String type = "APPLY_REFUND";
		String hMac = DigestUtils.md5Hex(md5Key+merchantCode+type).toUpperCase();
		
		StringBuffer reqUrl = new StringBuffer();
		reqUrl.append(qunarReqUrl).append("QueryOrders.do?merchantCode=")
			.append(merchantCode).append("&type=").append(type).append("&HMAC=").append(hMac);
		
		String jsonStr = HttpUtil.sendByGet(reqUrl.toString(), "UTF-8", "10000", "30000");
		//System.out.println(jsonStr);
		
		ObjectMapper mapper = new ObjectMapper();
		QunarOrderPack orderPack = mapper.readValue(jsonStr, QunarOrderPack.class);
		
		if(orderPack != null && !orderPack.isRet()){
			logger.info(logPre+"获取退票订单失败，errCode="+orderPack.getErrCode()+"&errMsg="+orderPack.getErrMsg());
			return;
		}
		
		logger.info(logPre+"本次获取退票订单的数为" + orderPack.getTotal());
		
		if(orderPack != null && orderPack.getData() != null 
				&& orderPack.getData().size()>0){
			/*
			 * 1、开车前15天，退票时不收取手续费
			 * 2、开车前48小时以上，不足15天，退票时收取票价5%的退票费；
			 * 3、开车前24小时以上、不足48小时的，退票时收取票价10%的退票费；
			 * 4、开车前不足24小时的，退票时收取票价20%退票费。
			 */
			for(QunarOrder qunarOrder : orderPack.getData()){
				if(StringUtils.isEmpty(qunarOrder.getOrderNo())){
					continue;
				}
				
				//int count = orderService.queryRefundCountByNo(qunarOrder.getOrderNo());
				Map<String, String> map_sta=orderService.queryRefundStatusByNo(qunarOrder.getOrderNo());
				if(null == map_sta || null == map_sta.get("refund_status")){
					OrderInfo orderInfo = orderService.queryOrderInfoById(qunarOrder.getOrderNo());
					if(null==orderInfo){
				        orderService.addOrderInfoByBackup(qunarOrder.getOrderNo());
				        orderService.addOrderCpInfoByBackup(qunarOrder.getOrderNo());
					}
					String refund_money = "0";
					try{
						List<Map<String, String>> list = orderService.queryPayMoneyByOrderId(qunarOrder.getOrderNo());
						String from_15d = DateUtil.dateAddDaysFmt3(qunarOrder.getTrainStartTime()+":00","-15");
						String from_24 = DateUtil.dateAddDaysFmt3(qunarOrder.getTrainStartTime()+":00","-1");
						String from_48 = DateUtil.dateAddDaysFmt3(qunarOrder.getTrainStartTime()+":00","-2");
						double feePercent = 0;
						if(new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd"))){
							feePercent = 0;
						}else if(new Date().before(DateUtil.stringToDate(from_48, "yyyy-MM-dd HH:mm:ss"))){
							feePercent = 0.05;
						}else if(new Date().before(DateUtil.stringToDate(from_24, "yyyy-MM-dd HH:mm:ss"))){
							feePercent = 0.1;
						}else{
							feePercent = 0.2;
						}
						double total_refund_money = 0;
						for(Map<String,String> map:list){
							double buy_money = Double.parseDouble(map.get("buy_money"));
							double sxf = 0.0;
							if(feePercent!=0.0){
								sxf = AmountUtil.quarterConvert(AmountUtil.mul(buy_money, feePercent));//手续费
								sxf = sxf > 2 ? sxf : 2;
							}
							double cp_refund_money = AmountUtil.ceil(AmountUtil.sub(buy_money, sxf));
							Map<String, Object> map_cp = new HashMap<String, Object>();
							map_cp.put("cp_id", map.get("cp_id"));
							map_cp.put("order_id", qunarOrder.getOrderNo());
							map_cp.put("refund_money", cp_refund_money);
							//更新单张车票退款金额
							try{
								logger.info(map.get("cp_id")+"]车票号;需退款金额："+cp_refund_money);
								orderService.updateQunarCPRefundMoney(map_cp);
							}catch(Exception e){
								e.printStackTrace();
							}
							total_refund_money += cp_refund_money;
						}
						refund_money = String.valueOf(total_refund_money);
						Map<String, String> map = new HashMap<String, String>();
						map.put("refund_seq", CreateIDUtil.createID("TK"));
						map.put("order_id", qunarOrder.getOrderNo());
						map.put("order_source", order_source);
						map.put("order_type", qunarOrder.getOrderType());//普通订单，联程订单
						map.put("refund_money", refund_money);
						//保存退票信息
						orderService.addQunarRefund(map);
					}catch(Exception e){
						logger.error("计算去哪儿退款金额失败！",e);
						Map<String, String> map = new HashMap<String, String>();
						map.put("refund_seq", CreateIDUtil.createID("TK"));
						map.put("order_id", qunarOrder.getOrderNo());
						map.put("order_source", order_source);
						map.put("order_type", qunarOrder.getOrderType());//普通订单，联程订单
						map.put("refund_money", refund_money);
						//保存退票信息
						orderService.addQunarRefund(map);
					}
				}else{
//					long diff_min = DateUtil.minuteDiff(new Date(),DateUtil.stringToDate(map_sta.get("notify_time"),DateUtil.DATE_FMT3));
					/** 此处开始对去哪重复退款做处理-----只针对拒绝退款单子做处理*/
					if("22".equals(map_sta.get("refund_status"))){
						logger.info("已拒绝订单，重复申请那个退款："+qunarOrder.getOrderNo());
						orderService.updateQunarRefund(qunarOrder.getOrderNo());
					}
					/**其他状态不受理*/
				}
				
			}
			
		}
		
	}
	
}
