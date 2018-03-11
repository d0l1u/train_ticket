package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AppRefundDao;
@Repository("appRefundDao")
public class AppRefundDaoImpl extends BaseDao implements AppRefundDao {

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("appRefund.queryRefundTicketCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryRefundTicketList",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryRefundTicketInfo",id_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryRefundTicketInfo",cp_id);
	}

	public void addCpRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().insert("appRefund.addCpRefundTicket_log",log_Map);
	}
	
	public void addOrderRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().update("appRefund.addOrderRefundTicket_log", log_Map);
	}

	public void updateRefundTicketInfo(Map<String, Object> refund_Map) {
		this.getSqlMapClientTemplate().update("appRefund.updateRefundTicketInfo",refund_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryHistroyByCpId",cp_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryrefundTicketAdd(Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("appRefund.refundTicketAdd",mapAdd);
	}
	
	@SuppressWarnings("unchecked")
	public String queryBuymoneyAndTicketpaymoney(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("appRefund.queryBuyMoney_TicketPayMoney",order_id);
	}
	
	@SuppressWarnings("unchecked")
	public String queryRefundMoney(String stream_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("appRefund.queryRefundMoney",stream_id);
	}
	
	public void updateRefund_StatusTo11(Map<String,Object> id_Map) {
		this.getSqlMapClientTemplate().update("appRefund.updateRefund_StatusTo11",id_Map);
	}

	public void updateOrderInfo_can_refundTo1_And_refund_total(Map<String,String> order_Map) {
		this.getSqlMapClientTemplate().update("appRefund.updateOrderInfo_can_refundTo1_And_refund_total",order_Map);
	}

	public void updateRefund_StatusTo55(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("appRefund.updateRefund_StatusTo55",refuse_Map);
	}

	public void updateDifferRefund(Map<String, Object> differ_Map) {
		this.getSqlMapClientTemplate().update("appRefund.updateDifferRefund", differ_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryHistroyByOrderId",order_id);
	}

	public void updateOut_Ticket_Refund(
			Map<String, Object> outTicket_Defeated_Map) {
		this.getSqlMapClientTemplate().update("appRefund.updateOut_Ticket_Refund",outTicket_Defeated_Map);
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		this.getSqlMapClientTemplate().update("appRefund.updateRefreshNotice",update_RefreshNoticeMap);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("appRefund.queryRefundTicketOrderId",order_id);
	}

	@Override
	public String querySumRefundMoney(String orderId) {
		return (String)this.getSqlMapClientTemplate().queryForObject("appRefund.querySumRefundMoney",orderId);
	}

	@Override
	public String queryRefundTicketOrderIdExists(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("appRefund.queryRefundTicketOrderIdExists", orderId);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("appRefund.queryBookOrderInfo", orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryBookOrderInfoBx", orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryBookOrderInfoCp", orderId);
	}

	@Override
	public void updateRefundNotify(Map<String,Object>refund_Map) {
		this.getSqlMapClientTemplate().update("appRefund.updateRefundNotify", refund_Map);
	}

	@Override
	public List<Map<String, String>> queryExtMerchantinfo() {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryExtMerchantinfo");
	}

	@Override
	public void updateExtRefundNotifyNum(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("appRefund.updateExtRefundNotifyNum", paramMap);
	}

	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("appRefund.queryRefundStationTicketAdd",mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("appRefund.queryRefundStationTicketCpId", paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("appRefund.queryCpidIsRefund", paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("appRefund.queryMerchantOrderId", orderId);
	}

	@Override
	public void updateRefundCpOrderInfo(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("appRefund.updateRefundCpOrderInfo", map);
	}

	@Override
	public void updateRefundCpOrderInfoRefuse(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().update("appRefund.updateRefundCpOrderInfoRefuse", refuseMap);
	}

	@Override
	public List<Map<String, String>> queryRefundTicketPhoneInfo(
			Map<String, String> idMap) {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryRefundTicketPhoneInfo", idMap);
	}

	@Override
	public void updateRefundStationTicketPhone(Map<String, Object> mapAdd) {
		this.getSqlMapClientTemplate().update("appRefund.updateRefundStationTicketPhone", mapAdd);
	}

	@Override
	public List<String> queryRefundStatus(String string) {
		return this.getSqlMapClientTemplate().queryForList("appRefund.queryRefundStatus", string);
	}

	@Override
	public void updateNotifyStatus(Map<String, Object> refundMap) {
		this.getSqlMapClientTemplate().update("appRefund.updateNotifyStatus", refundMap);
	}


}
