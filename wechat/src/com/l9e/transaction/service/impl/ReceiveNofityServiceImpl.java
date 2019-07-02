package com.l9e.transaction.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.dao.ReceiveNotifyDao;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;

@Service("receiveNotifyService")
public class ReceiveNofityServiceImpl implements ReceiveNotifyService {
	private static final Logger logger = Logger.getLogger(ReceiveNofityServiceImpl.class);
	
	@Resource
	private ReceiveNotifyDao receiveNotifyDao;
	
	@Resource
	private OrderDao orderDao;

	public int updateOrderWithCpNotify(Map<String, String> paraMap, 
			List<Map<String, String>> cpMapList, Map<String, String> failRefundMap) {
		try {
			receiveNotifyDao.updateOrderWithCpNotify(paraMap);
			if (cpMapList != null) {
				for (Map<String, String> cpMap : cpMapList) {
					receiveNotifyDao.updateCpOrderWithCpNotify(cpMap);
				}
				//出票成功则更新保险状态
				if(!StringUtils.isEmpty(paraMap.get("order_status"))
						&& TrainConsts.OUT_SUCCESS.equals(paraMap.get("order_status"))){
					orderDao.updateBxStatusNotSend(paraMap.get("order_id"));
				}
				
				//生成差额退款数据
				Map<String, String> differMap = orderDao.queryOrderDiffer(paraMap.get("order_id"));
				if(differMap != null && !StringUtils.isEmpty(differMap.get("refund_money"))
					&& Double.parseDouble(differMap.get("refund_money")) > 0){
					String remark = "系统自动发起差额退款，退款金额：" + differMap.get("refund_money");
					Map<String, String> refundMap = new HashMap<String, String>();
					String finish_time = differMap.get("finish_time");
					DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = null;
					String refund_plan_time = null;
					try {
						date = fmt.parse(finish_time);
						long ms = date.getTime();
						ms = ms + 24 * 60 * 60 * 1000;
						date = new Date(ms);
						refund_plan_time = fmt.format(date);
						logger.info("[代理商主动发起退款]计划退款时间为" + refund_plan_time);
					} catch (ParseException e) {
						logger.error("[代理商主动发起退款]日期、字符串转化发生错误！" + e);
					}
					refundMap.put("refund_plan_time", refund_plan_time);
					refundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
					refundMap.put("order_id", paraMap.get("order_id"));
					refundMap.put("refund_type", TrainConsts.REFUND_TYPE_2);//差额退款
					refundMap.put("refund_money", differMap.get("refund_money"));
					refundMap.put("user_remark", remark);
					refundMap.put("refund_no", CreateIDUtil.createID(""));
					refundMap.put("refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
					orderDao.addRefundStream(refundMap);
					
					//更新退款总金额
					Map<String, String> tkMoneyMap = new HashMap<String, String>();
					double refund_total = Double.parseDouble(differMap.get("refund_total"));
					double refund_money = Double.parseDouble(differMap.get("refund_money"));
					refund_total = AmountUtil.add(refund_total, refund_money);
					tkMoneyMap.put("refund_total", String.valueOf(refund_total));
					tkMoneyMap.put("order_id", paraMap.get("order_id"));
					orderDao.updateOrderRefundTotal(tkMoneyMap);
					
					//添加操作日志
					Map<String, String> logMap = new HashMap<String, String>();
					logMap.put("order_id", paraMap.get("order_id"));
					logMap.put("order_optlog", remark);
					logMap.put("opter", "19e_buy");
					orderDao.addOrderOptLog(logMap);
				}
			}
			//生成出票失败退款数据
			if (failRefundMap != null) {
				orderDao.addRefundStream(failRefundMap);
				
				//更新退款总金额，是否能退款
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("refund_total", failRefundMap.get("refund_money"));
				paramMap.put("order_id", failRefundMap.get("order_id"));
				paramMap.put("can_refund", "0");//不能退款
				orderDao.updateOrderRefundTotal(paramMap);
				
				//添加操作日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id", failRefundMap.get("order_id"));
				logMap.put("order_optlog", "系统自动发起出票失败退款，退款金额：" + failRefundMap.get("refund_money"));
				logMap.put("opter", "19e_buy");
				orderDao.addOrderOptLog(logMap);
			}
		} catch (Exception e) {
			//logger.info("Exception"+paraMap.get("order_id")+":"+e);
			logger.error("Exception"+paraMap.get("order_id"), e);
			e.printStackTrace();
			return 0;
		}
		return 1;
		
	}
}
