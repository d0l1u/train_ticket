package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.dao.RefundDao;
import com.l9e.transaction.service.RefundService;
import com.l9e.util.AmountUtil;

@Service("refundService")
public class RefundServiceImpl implements RefundService {
	
	private static final Logger logger = Logger.getLogger(RefundServiceImpl.class);
	
	@Resource
	private OrderDao orderDao;
	
	@Resource
	private RefundDao refundDao;
	
	@Override
	public boolean addUserRefundStream(List<Map<String, String>> refundList, JSONObject jsonRes) {
		String order_id = refundList.get(0).get("order_id");
		String cp_id = refundList.get(0).get("cp_id");
		String refund_seq = refundList.get(0).get("refund_seq");
		logger.info("退票流水号："+refund_seq);
		String notify_url = refundList.get(0).get("notify_url");
		
		Map<String, String> map = refundDao.queryRefundTotalInOrder(order_id);
		double refund_total = Double.parseDouble(map.get("refund_total")); 
		double refund_money = 0;
		int count = 0;
		//验证退款流水中是否包含该车票的退款信息（除拒绝退款数据）,已经有该车票退款数据，则拒绝
		for(Map<String, String> refundMap : refundList){
			count = refundDao.queryRefundStreamContainCp(refundMap);
			if(count > 0){
				//查询已经退票的用户信息
				Map<String, String> passenger = refundDao.queryPassengerInfoByCpId(refundMap.get("cp_id"));
				String errInfo = passenger.get("user_name")+"("+passenger.get("user_ids")+")已发生过退票申请，拒绝本次退票申请";
				
				logger.info(errInfo+"，order_id="+refundMap.get("order_id")
						+"&request_id="+refundMap.get("merchant_refund_seq"));
				
				jsonRes.put("merchant_order_id", refundMap.get("merchant_order_id"));
				jsonRes.put("message", errInfo);
				jsonRes.put("order_id", refundMap.get("order_id"));
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no","");
				jsonRes.put("status", "FAILURE");//申请失败
				jsonRes.put("fail_reason", errInfo);
				return false;
			}
		}
		
		//保存退票数据
		for(Map<String, String> refundMap : refundList){
			//退款列表中含有拒绝退款的车票，则删除老的退款流水
			if(!StringUtils.isEmpty(refundMap.get("old_refund_status"))
					&& TrainConsts.REFUND_STATUS_REFUSE.equals(refundMap.get("old_refund_status"))){
				refundDao.deleteRefundStreamOnRefuse(refundMap);
				logger.info("退款列表中含有拒绝退款的车票，则删除老的退款流水:order_id="+refundMap.get("order_id")+"  cp_id="+refundMap.get("cp_id"));
			}
			refund_money = Double.parseDouble(refundMap.get("refund_money"));
			refund_total = AmountUtil.add(refund_total, refund_money);
			refundDao.addRefundStream(refundMap);
			logger.info("生成一条退款:order_id="+refundMap.get("order_id")+"  cp_id="+refundMap.get("cp_id"));
		}
		
		//生成多条退票通知数据new
		for(Map<String, String> refundMap1 : refundList){
			Map<String, String> notifyMap = new HashMap<String, String>();
			notifyMap.put("order_id", refundMap1.get("order_id"));
			notifyMap.put("refund_seq", refundMap1.get("refund_seq"));
			notifyMap.put("cp_id", refundMap1.get("cp_id"));
			notifyMap.put("notify_url", refundMap1.get("notify_url"));
			notifyMap.put("notify_status", TrainConsts.REFUND_NOTIFY_NOT);//初始化（未通知）
			if(refundDao.queryRefundNotifyNum(notifyMap) > 0){
				logger.info("已经有该退款通知:"+refundMap1.get("order_id")+" ,cp_id:"+refundMap1.get("cp_id"));
//				refundDao.updateRefundNotifyRestart(notifyMap);
			}else{
				logger.info("生成一条退款通知：order_id="+refundMap1.get("order_id")+"  cp_id="+refundMap1.get("cp_id"));
				refundDao.addRefundNotify(notifyMap);
			}
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("refund_total", String.valueOf(refund_total));
		paramMap.put("order_id", order_id);
		//查询未发生退款的票数
		int left_count = refundDao.queryRefundLeftCount(order_id);
		if(left_count==0){
			paramMap.put("can_refund", "0");//不能退款
		}else{
			paramMap.put("can_refund", "1");//能退款
		}
		orderDao.updateOrderRefundTotal(paramMap);
		return true;
	}

	@Override
	public Map<String, String> querySpecTimeBeforeFrom(String order_id) {
		return refundDao.querySpecTimeBeforeFrom(order_id);
	}

	@Override
	public List<Map<String, String>> queryRefundResultWaitList() {
		return refundDao.queryRefundResultWaitList();
	}

	@Override
	public List<Map<String, String>> queryRefundStreamListBySeq(
			Map<String, String> paramMap) {
		return refundDao.queryRefundStreamListBySeq(paramMap);
	}

	@Override
	public List<Map<String, String>> queryCpListByOrderId(String order_id) {
		return refundDao.queryCpListByOrderId(order_id);
	}

	@Override
	public void updateRefundNotfiyFinish(Map<String, String> map) {
		refundDao.updateRefundNotfiyFinish(map);
	}

	@Override
	public int updateRefundNotfiyBegin(Map<String, String> beginMap) {
		return refundDao.updateRefundNotfiyBegin(beginMap);
	}

	@Override
	public int queryRefundCountByMerchantSeq(Map<String, String> merchantMap) {
		return refundDao.queryRefundCountByMerchantSeq(merchantMap);
	}

	@Override
	public String queryOrderStatusById(Map<String, String> orderMap) {
		return refundDao.queryOrderStatusById(orderMap);
	}

	@Override
	public void updateOrderRefundStatus(Map<String, String> param) {
		refundDao.updateOrderRefundStatus(param);
	}

	@Override
	public List<Map<String, String>> queryEopRefundResultWaitList() {
		return refundDao.queryEopRefundResultWaitList();
	}

	@Override
	public int updateEopRefundNotfiyBegin(Map<String, String> beginMap) {
		return refundDao.updateEopRefundNotfiyBegin(beginMap);
	}

	@Override
	public void updateEopRefundNotfiyFinish(Map<String, String> map) {
		refundDao.updateEopRefundNotfiyFinish(map);
		refundDao.updateRefundStreamEopRefundSeq(map);
	}

	@Override
	public void updateEopRefundStreamInfo(Map<String, String> map) {
		refundDao.updateEopRefundStreamInfo(map);
	}

	@Override
	public void updateRefundNotifyRestart(Map<String, String> map) {
		refundDao.updateRefundNotifyRestart(map);
	}

	@Override
	public String queryRefundStatusByCpId(Map<String, String> map) {
		return refundDao.queryRefundStatusByCpId(map);
	}

	@Override
	public void updateRefundStreamTo33(Map<String, String> updateMap) {
		refundDao.updateRefundStreamTo33(updateMap);
	}
}
