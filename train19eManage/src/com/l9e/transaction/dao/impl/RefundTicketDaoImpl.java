package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RefundTicketDao;
@Repository("refundTicketDao")
public class RefundTicketDaoImpl extends BaseDao implements RefundTicketDao{

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("refundTicket.queryRefundTicketCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("refundTicket.queryRefundTicket",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return this.getSqlMapClientTemplate().queryForList("refundTicket.queryRefundTicketInfo",id_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("refundTicket.queryRefundTicketInfo",cp_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketByStreamId(String stream_id) {
		return this.getSqlMapClientTemplate().queryForList("refundTicket.queryRefundTicketByStreamId",stream_id);
	}

	public void addCpRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().insert("refundTicket.addCpRefundTicket_log",log_Map);
	}
	
	public void addOrderRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().update("refundTicket.addOrderRefundTicket_log", log_Map);
	}

	public void updateRefundTicketInfo(Map<String, Object> refund_Map) {
		this.getSqlMapClientTemplate().update("refundTicket.updateRefundTicketInfo",refund_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("refundTicket.queryHistroyByCpId",cp_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryrefundTicketAdd(Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("refundTicket.refundTicketAdd",mapAdd);
	}

	public void updateOrderInfo_can_refundTo0(Map<String,Object> mapAdd) {
		this.getSqlMapClientTemplate().update("refundTicket.updateOrderInfo_can_refundTo0",mapAdd);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryrefundTicketTelAdd(Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("refundTicket.refundTicketTelAdd",mapAdd);
	}
	
	@SuppressWarnings("unchecked")
	public String queryBuymoneyAndTicketpaymoney(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("refundTicket.queryBuyMoney_TicketPayMoney",order_id);
	}
	
	@SuppressWarnings("unchecked")
	public String queryRefundMoney(String stream_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("refundTicket.queryRefundMoney",stream_id);
	}
	
	public void updateRefund_StatusTo11(Map<String,Object> id_Map) {
		this.getSqlMapClientTemplate().update("refundTicket.updateRefund_StatusTo11",id_Map);
	}

	public void updateOrderInfo_can_refundTo1_And_refund_total(Map<String,String> order_Map) {
		this.getSqlMapClientTemplate().update("refundTicket.updateOrderInfo_can_refundTo1_And_refund_total",order_Map);
	}

	public void updateRefund_StatusTo55(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("refundTicket.updateRefund_StatusTo55",refuse_Map);
	}

	public void updateDifferRefund(Map<String, Object> differ_Map) {
		this.getSqlMapClientTemplate().update("refundTicket.updateDifferRefund", differ_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("refundTicket.queryHistroyByOrderId",order_id);
	}

	public void updateOut_Ticket_Refund(
			Map<String, Object> outTicket_Defeated_Map) {
		this.getSqlMapClientTemplate().update("refundTicket.updateOut_Ticket_Refund",outTicket_Defeated_Map);
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		this.getSqlMapClientTemplate().update("refundTicket.updateRefreshNotice",update_RefreshNoticeMap);
	}
	public int updateRefreshNoticeById(Map<String, Object> update_Map) {
		return (Integer)this.getSqlMapClientTemplate().update("refundTicket.updateRefreshNoticeById",update_Map);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("refundTicket.queryRefundTicketOrderId",order_id);
	}

	@Override
	public String querySumRefundMoney(String orderId) {
		return (String)this.getSqlMapClientTemplate().queryForObject("refundTicket.querySumRefundMoney",orderId);
	}

	@Override
	public String queryRefundTicketOrderIdExists(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("refundTicket.queryRefundTicketOrderIdExists", orderId);
	}

	@Override
	public int queryNoReplyCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("complain.queryNoReplyCount");
	}

	@Override
	public void updateGezhiRefund(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().update("refundTicket.updateGezhiRefund", refuseMap);
	}

	@Override
	public List<String> queryOrderCpId(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("refundTicket.queryOrderCpId", orderId);
	}

	@Override
	public void updateAlertRefund(HashMap<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("refundTicket.updateAlertRefund", paramMap);
	}

	@Override
	public List<Map<String, String>> queryRefundTicket(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("refundTicket.queryRefundTicket2",paramMap);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("refundTicket.updateOrder", map);
		
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("refundTicket.updateRefundOpt", map);
		
	} 
	
	@Override
	public int queryCpidIsRefundNum(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("refundTicket.queryCpidIsRefundNum", paramMap);
	}
	
	@Override
	public String queryStatusByOrderId(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("refundTicket.queryStatusByOrderId", paramMap);
	}
 
	@SuppressWarnings("unchecked")
	public Map<String,String> queryMoneyByCpId(Map<String, Object> paramMap) {
		return (Map<String,String>) this.getSqlMapClientTemplate().queryForObject("refundTicket.queryMoneyByCpId", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("refundTicket.queryRefundStationTicketAdd",mapAdd);
	}



}
