package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.controller.ExtRefundController;
import com.l9e.transaction.dao.ExtRefundDao;
import com.l9e.transaction.service.ExtRefundService;
@Service("extRefundService")
public class ExtRefundServiceImpl implements ExtRefundService {
	
	private static final Logger logger = Logger.getLogger(ExtRefundServiceImpl.class);
	@Resource
	private ExtRefundDao extRefundDao ;

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return extRefundDao.queryRefundTicketCount(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return extRefundDao.queryRefundTicketList(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return extRefundDao.queryRefundTicketInfo(id_Map);
	}

	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return extRefundDao.queryRefundTicketOrderInfoCp(cp_id);
	}
	
	public void queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> mapAdd) {
		extRefundDao.addOrderRefundTicket_log(log_Map);//增加操作日志
		extRefundDao.queryrefundTicketAdd(mapAdd);//增加差额退款，向ext_orderinfo_refundstream表添加数据
		//根据order_id查询eop_order_id和eop_refund_url
		List<Map<String, String>> orderList = extRefundDao.queryOrderInfo(mapAdd.get("order_id"));
		Map<String, String> orderMap = orderList.get(0);
		orderMap.put("refund_reason", mapAdd.get("user_remark").toString());
		extRefundDao.addOrderRefundNotify(orderMap);//向ext_orderinfo_refundeopnotify表添加数据（平台通知表）
	}
	
	public void updateRefundTicket(Map<String, String> log_Map,
			Map<String, Object> refund_Map) {
		extRefundDao.addCpRefundTicket_log(log_Map);
		extRefundDao.updateRefundTicketInfo(refund_Map);
	}

	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return extRefundDao.queryHistroyByCpId(cp_id);
	}

	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return extRefundDao.queryHistroyByOrderId(order_id);
	}
	
	public Map<String, String> queryBuymoneyAndTicketpaymoney(String order_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = extRefundDao.querySumRefundMoney(order_id);
		String pay_money = extRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		return money_Map;
	}
	
	public Map<String, String> queryRefundMoney(String order_id,String stream_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = extRefundDao.querySumRefundMoney(order_id);
		String pay_money = extRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		String refund_money =extRefundDao.queryRefundMoney(stream_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		money_Map.put("refund_money", refund_money);
		return money_Map;
	}
	
	public void updateDifferRefund(Map<String, String> log_Map,
			Map<String, Object> differ_Map) {
		extRefundDao.addOrderRefundTicket_log(log_Map);
		extRefundDao.updateDifferRefund(differ_Map);
	}


	public void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map) {
		extRefundDao.addCpRefundTicket_log(log_Map);
		extRefundDao.updateOut_Ticket_Refund(outTicket_Defeated_Map);
//		extRefundDao.updateOutTicketRefundEOP(outTicket_Defeated_Map);//更新ext_orderinfo_refundeopnotify表数据
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		extRefundDao.updateRefreshNotice(update_RefreshNoticeMap);
	}

	public void addErrorLogInfo(Map<String, String> log_Map) {
		extRefundDao.addCpRefundTicket_log(log_Map);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return extRefundDao.queryRefundTicketOrderId(order_id);
	}

	public String queryRefundTicketOrderIdExists(String orderId) {
		return extRefundDao.queryRefundTicketOrderIdExists(orderId);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return extRefundDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return extRefundDao.queryBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return extRefundDao.queryBookOrderInfoCp(orderId);
	}
	
	//同意退款
	@Override
	public void updateRefundNotify(Map<String,Object>map,Map<String,String>log_Map) {
		extRefundDao.updateRefundNotify(map);				 //更新ext_orderinfo_refundstream表的信息（同意退款）
		extRefundDao.updateCpRefundStatusSuccess(map.get("cp_id").toString());//更新ext_orderinfo_cp表的信息
		int count = extRefundDao.queryRefundCount(map.get("refund_seq").toString());//根据refund_seq查询ext_orderinfo_refundstream有几条退款记录
		if(count > 0){
			extRefundDao.addCpRefundTicket_log(log_Map);		 //添加操作日志
		}else{
			extRefundDao.updateExtRefundNotifyStatus(map);		 //更新ext_orderinfo_refundnotify表的信息（同意退款）
			extRefundDao.addCpRefundTicket_log(log_Map);		 //添加操作日志
		}
	}

	//拒绝退款
	public void updateRefuseRefund(Map<String, String> log_Map, Map<String, Object> refuse_Map, Map<String,String> order_Map) {
		extRefundDao.updateRefund_StatusTo55(refuse_Map);	//修改退款状态为拒绝退款同时备注,操作人和审核时间也会修改 ext_orderinfo_refundstream
		extRefundDao.updateCpRefundStatusFail(refuse_Map);  //更新ext_orderinfo_cp表的信息
		int count = extRefundDao.queryRefundCount(refuse_Map.get("refund_seq").toString());//根据refund_seq查询ext_orderinfo_refundstream有几条退款记录
		if(count > 0){
			extRefundDao.addCpRefundTicket_log(log_Map);		//添加操作日志
		}else{
			extRefundDao.addCpRefundTicket_log(log_Map);		//添加操作日志
			//extRefundDao.deleteRefundNotify(refuse_Map);		//删除退款信息  ext_orderinfo_refundnotify
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("order_id", refuse_Map.get("order_id"));
			map.put("refund_seq", refuse_Map.get("refund_seq"));
			extRefundDao.updateExtRefundNotifyStatus(map);		 //更新ext_orderinfo_refundnotify表的信息（同意退款）
			extRefundDao.updateOrderInfo_can_refundTo1_And_refund_total(order_Map);//修改主表中can_refund为1与更新refund_total(ext_orderinfo)
		}
		
	}
	
	@Override
	public void updateGezhiRefund(Map<String, String> logMap,
			Map<String, Object> refuseMap) {
		extRefundDao.addCpRefundTicket_log(logMap);//添加操作日志
		extRefundDao.updateGezhiRefund(refuseMap);//搁置订单
	}
	
	@Override
	public List<Map<String, String>> queryExtMerchantinfo() {
		return extRefundDao.queryExtMerchantinfo();
	}

	@Override
	public void updateExtRefundNotifyNum(Map<String, String> paramMap) {
		extRefundDao.updateExtRefundNotifyNum(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(Map<String, String> logMap,
			Map<String, Object> mapAdd,Map<String,String> notifyMap) {
			
		extRefundDao.addOrderRefundTicket_log(logMap);
		//添加EOP请求,自动退款
		if(extRefundDao.queryRefundNotifyNum(notifyMap) > 0){
			logger.info("已经有该退款通知");
		}else{
			extRefundDao.addRefundNotify(notifyMap);
		}
		
		return extRefundDao.queryRefundStationTicketAdd(mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return extRefundDao.queryRefundStationTicketCpId(paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return extRefundDao.queryCpidIsRefund(paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return extRefundDao.queryMerchantOrderId(orderId);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		extRefundDao.insertLog(map);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		extRefundDao.updateOrder(map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		extRefundDao.updateOrderstatusToRobotGai(updateMap);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		extRefundDao.updateRefundOpt(map);
	}

	@Override
	public List<Map<Object, Object>> queryExtOrderInfo(String order_id) {
		// TODO Auto-generated method stub
		return extRefundDao.queryExtOrderInfo(order_id);
	}

	@Override
	public String findExtRefundNotifyUrl(Map<String, String> queryMap) {
		// TODO Auto-generated method stub
		return extRefundDao.findExtRefundNotifyUrl(queryMap);
	}



	
}
