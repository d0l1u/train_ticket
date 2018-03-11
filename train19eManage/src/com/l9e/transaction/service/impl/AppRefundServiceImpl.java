package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AppRefundDao;
import com.l9e.transaction.service.AppRefundService;
@Service("appRefundService")
public class AppRefundServiceImpl implements AppRefundService {
	@Resource
	private AppRefundDao appRefundDao ;

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return appRefundDao.queryRefundTicketCount(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return appRefundDao.queryRefundTicketList(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return appRefundDao.queryRefundTicketInfo(id_Map);
	}

	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return appRefundDao.queryRefundTicketOrderInfoCp(cp_id);
	}
	
	public List<Map<String, Object>> queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> mapAdd) {
		appRefundDao.addOrderRefundTicket_log(log_Map);
		return appRefundDao.queryrefundTicketAdd(mapAdd);
	}
	//同意退款流程
	public void updateRefundTicket(Map<String, String> log_Map,
			Map<String, Object> refund_Map) {
		appRefundDao.updateRefundTicketInfo(refund_Map);//更新app_orderinfo_refundstream表中状态为同意退款（11、开始退票）
		appRefundDao.updateRefundCpOrderInfoRefuse(refund_Map);//更新app_orderinfo_cp表的信息（同意退款refund_status=11）
		//根据order_id查询app_orderinfo_refundstream表中该订单的退款状态
		//List<String> refundStatusList = appRefundDao.queryRefundStatus(refund_Map.get("order_id").toString());
		//所有退款状态为“拒绝退款22”或者某个退款状态为“等待退款00”时，不做操作(不要)
		//if(refundStatusList.equals("22") || refundStatusList.contains("00")){//22、拒绝退票  00、等待退票
		//	appRefundDao.addCpRefundTicket_log(log_Map);//添加操作日志
		//}else{
			//根据order_id和refund_seq修改app_orderinfo_refundnotify表，通知状态改为“开始通知22”
			refund_Map.put("notify_status", "22");
			appRefundDao.updateNotifyStatus(refund_Map);
			appRefundDao.addCpRefundTicket_log(log_Map);//添加操作日志
		//}
		
	}

	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return appRefundDao.queryHistroyByCpId(cp_id);
	}

	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return appRefundDao.queryHistroyByOrderId(order_id);
	}
	
	public Map<String, String> queryBuymoneyAndTicketpaymoney(String order_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = appRefundDao.querySumRefundMoney(order_id);
		String pay_money = appRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		return money_Map;
	}
	
	public Map<String, String> queryRefundMoney(String order_id,String stream_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = appRefundDao.querySumRefundMoney(order_id);
		String pay_money = appRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		String refund_money =appRefundDao.queryRefundMoney(stream_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		money_Map.put("refund_money", refund_money);
		return money_Map;
	}
	
	//拒绝退款流程
	public void updateRefuseRefund(Map<String, String> log_Map,
			Map<String, Object> refuse_Map,Map<String,String>order_Map) {
		appRefundDao.updateRefund_StatusTo55(refuse_Map);//修改app_orderinfo_refundstream退款状态为“22拒绝退款”
		appRefundDao.updateOrderInfo_can_refundTo1_And_refund_total(order_Map);//修改主表中can_refund为1与更新refund_total
		appRefundDao.updateRefundCpOrderInfoRefuse(refuse_Map);//更新app_orderinfo_cp表的信息（拒绝退款及其原因）（refund_status=33）
		
		//List<String> refundStatusList = appRefundDao.queryRefundStatus(refuse_Map.get("order_id").toString());
		//所有退款状态为“拒绝退款22”或者某个退款状态为“等待退款00”时，不做操作
		//if(refundStatusList.equals("22") || refundStatusList.contains("00")){//22、拒绝退票  00、等待退票
			//appRefundDao.addCpRefundTicket_log(log_Map);//添加操作日志
		//}else{
			//根据order_id和refund_seq修改app_orderinfo_refundnotify表，通知状态改为“开始通知22”
			appRefundDao.updateNotifyStatus(refuse_Map);
			appRefundDao.addCpRefundTicket_log(log_Map);//添加操作日志
		//}
		
	}

	public void updateDifferRefund(Map<String, String> log_Map,
			Map<String, Object> differ_Map) {
		appRefundDao.addOrderRefundTicket_log(log_Map);
		appRefundDao.updateDifferRefund(differ_Map);
	}


	public void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map) {
		appRefundDao.addOrderRefundTicket_log(log_Map);
		appRefundDao.updateOut_Ticket_Refund(outTicket_Defeated_Map);
		
		outTicket_Defeated_Map.put("notify_status", "22");
		appRefundDao.updateNotifyStatus(outTicket_Defeated_Map);
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		appRefundDao.updateRefreshNotice(update_RefreshNoticeMap);
	}

	public void addErrorLogInfo(Map<String, String> log_Map) {
		appRefundDao.addCpRefundTicket_log(log_Map);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return appRefundDao.queryRefundTicketOrderId(order_id);
	}

	public String queryRefundTicketOrderIdExists(String orderId) {
		return appRefundDao.queryRefundTicketOrderIdExists(orderId);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return appRefundDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return appRefundDao.queryBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return appRefundDao.queryBookOrderInfoCp(orderId);
	}

	@Override
	public void updateRefundNotify(Map<String,Object>map) {
		appRefundDao.updateRefundNotify(map);
		appRefundDao.updateRefundCpOrderInfo(map);
	}

	@Override
	public List<Map<String, String>> queryExtMerchantinfo() {
		return appRefundDao.queryExtMerchantinfo();
	}

	@Override
	public void updateExtRefundNotifyNum(Map<String, String> paramMap) {
		appRefundDao.updateExtRefundNotifyNum(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd) {
		appRefundDao.addOrderRefundTicket_log(logMap);
		return appRefundDao.queryRefundStationTicketAdd(mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return appRefundDao.queryRefundStationTicketCpId(paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return appRefundDao.queryCpidIsRefund(paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return appRefundDao.queryMerchantOrderId(orderId);
	}

	@Override
	public List<Map<String, String>> queryRefundTicketPhoneInfo(
			Map<String, String> idMap) {
		return appRefundDao.queryRefundTicketPhoneInfo(idMap);
	}

	@Override
	public void updateRefundStationTicketAdd(Map<String, String> logMap,
			Map<String, Object> mapAdd) {
		appRefundDao.addOrderRefundTicket_log(logMap);
		appRefundDao.updateRefundStationTicketPhone(mapAdd);
	}


	
}
