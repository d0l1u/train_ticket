package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.controller.PsOrderController;
import com.l9e.transaction.dao.BookDao;
import com.l9e.transaction.dao.PsOrderDao;
import com.l9e.transaction.service.PsOrderService;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.PsNotify.AmountUtil;

@Service("psOrderService")
public class PsOrderServiceImpl implements PsOrderService{
	private static final Logger logger = Logger.getLogger(PsOrderController.class);
	
	@Resource
	private PsOrderDao psOrderDao;
	
	@Resource
	private BookDao bookDao;
	@Override
	public String queryDbOrderStatus(String orderId) {
		return psOrderDao.queryDbOrderStatus(orderId);
	}

	@Override
	public List<Map<String, Object>> queryHistroyByOrderId(String orderId) {
		return psOrderDao.queryHistroyByOrderId(orderId);
	}

	@Override
	public int queryPsOrderCount(Map<String, Object> paramMap) {
		return psOrderDao.queryPsOrderCount(paramMap);
	}

	@Override
	public Map<String, String> queryPsOrderInfo(String orderId) {
		return psOrderDao.queryPsOrderInfo(orderId);
	}

	@Override
	public List<Map<String, Object>> queryPsOrderInfoCp(String orderId) {
		return psOrderDao.queryPsOrderInfoCp(orderId);
	}

	@Override
	public List<Map<String, String>> queryPsOrderList(
			Map<String, Object> paramMap) {
		return psOrderDao.queryPsOrderList(paramMap);
	}

	@Override
	public void updatePsOrderCpList(List<Map<String, String>> cpList) {
		for(Map<String,String> cpInfo : cpList){
			psOrderDao.updatePsOrderCpInfo(cpInfo);
		}
	}

	@Override
	public void updatePsOrderStatus(AcquireVo acquire) {
		psOrderDao.updatePsOrderStatus(acquire);
	}

	@Override
	public int updateOrderWithCpNotify(Map<String, String> hc,
			List<Map<String, String>> cpList, Map<String, String> failRefundMap) {
		try {
			psOrderDao.updateOrderWithCpNotify(hc);
			if (cpList != null) {
				for (Map<String, String> cpMap : cpList) {
					psOrderDao.updateCpOrderWithCpNotify(cpMap);
				}
				//出票成功则更新保险状态
				if(!StringUtils.isEmpty(hc.get("order_status"))
						&& "44".equals(hc.get("order_status"))){
					psOrderDao.updateBxStatusNotSend(hc.get("order_id"));
				}
				
				//生成差额退款数据
				Map<String, String> differMap = psOrderDao.queryOrderDiffer(hc.get("order_id"));
				if(differMap != null && !StringUtils.isEmpty(differMap.get("refund_money"))
					&& Double.parseDouble(differMap.get("refund_money")) > 0){
					String remark = "系统自动发起差额退款，退款金额：" + differMap.get("refund_money");
					Map<String, String> refundMap = new HashMap<String, String>();
					refundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
					refundMap.put("order_id", hc.get("order_id"));
					refundMap.put("refund_type", "2");//差额退款
					refundMap.put("refund_money", differMap.get("refund_money"));
					refundMap.put("user_remark", remark);
					refundMap.put("refund_status", "00");//准备退款
					psOrderDao.addRefundStream(refundMap);
					
					//更新退款总金额
					Map<String, String> tkMoneyMap = new HashMap<String, String>();
					double refund_total = Double.parseDouble(differMap.get("refund_total"));
					double refund_money = Double.parseDouble(differMap.get("refund_money"));
					refund_total = AmountUtil.add(refund_total, refund_money);
					tkMoneyMap.put("refund_total", String.valueOf(refund_total));
					tkMoneyMap.put("order_id", hc.get("order_id"));
					psOrderDao.updateOrderRefundTotal(tkMoneyMap);
					
					//添加操作日志
					Map<String, String> logMap = new HashMap<String, String>();
					logMap.put("order_id", hc.get("order_id"));
					logMap.put("userAccount", remark);
					logMap.put("user", "19e_buy");
					bookDao.addUserAccount(logMap);
				}
			}
			//生成出票失败退款数据
			if (failRefundMap != null) {
				psOrderDao.addRefundStream(failRefundMap);
				
				//更新退款总金额，是否能退款
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("refund_total", failRefundMap.get("refund_money"));
				paramMap.put("order_id", failRefundMap.get("order_id"));
				paramMap.put("can_refund", "0");//不能退款
				psOrderDao.updateOrderRefundTotal(paramMap);
				
				//添加操作日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id", failRefundMap.get("order_id"));
				if(failRefundMap.get("refund_type").equals("3")){
					logMap.put("userAccount", "系统自动发起出票失败退款，退款金额：" + failRefundMap.get("refund_money"));
				}else{
					logMap.put("userAccount", "系统自动发起取消预约退款，退款金额：" + failRefundMap.get("refund_money"));
				}
				logMap.put("user", "19e_buy");
				bookDao.addUserAccount(logMap);
			}
		} catch (Exception e) {
			logger.error("Exception"+hc.get("order_id")+":"+e);
			e.printStackTrace();
			return 0;
		}
		return 1;
		
	}

	@Override
	public Map<String, String> queryPsOrderInfoPssm(String orderId) {
		return psOrderDao.queryPsOrderInfoPssm(orderId);
	}

}
