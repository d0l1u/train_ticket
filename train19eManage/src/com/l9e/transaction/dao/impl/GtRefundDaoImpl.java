package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.GtRefundDao;
@Repository("gtRefundDao")
public class GtRefundDaoImpl extends BaseDao implements GtRefundDao {

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("gtRefund.queryRefundTicketCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryRefundTicketList",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryRefundTicketInfo",id_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryRefundTicketInfo",cp_id);
	}

	public void addCpRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().insert("gtRefund.addCpRefundTicket_log",log_Map);
	}
	
	public void addOrderRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().update("gtRefund.addOrderRefundTicket_log", log_Map);
	}

	public void updateRefundTicketInfo(Map<String, Object> refund_Map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateRefundTicketInfo",refund_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryHistroyByCpId",cp_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryrefundTicketAdd(Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("gtRefund.refundTicketAdd",mapAdd);
	}
	
	public String queryBuymoneyAndTicketpaymoney(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryBuyMoney_TicketPayMoney",order_id);
	}
	
	public String queryRefundMoney(String stream_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryRefundMoney",stream_id);
	}
	
	public void updateRefund_StatusTo11(Map<String,Object> id_Map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateRefund_StatusTo11",id_Map);
	}

	public void updateOrderInfo_can_refundTo1_And_refund_total(Map<String,String> order_Map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateOrderInfo_can_refundTo1_And_refund_total",order_Map);
	}

	public void updateRefund_StatusTo55(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateRefund_StatusTo55",refuse_Map);
	}

	public void updateDifferRefund(Map<String, Object> differ_Map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateDifferRefund", differ_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryHistroyByOrderId",order_id);
	}

	public void updateOut_Ticket_Refund(
			Map<String, Object> outTicket_Defeated_Map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateOut_Ticket_Refund",outTicket_Defeated_Map);
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		this.getSqlMapClientTemplate().update("gtRefund.updateRefreshNotice",update_RefreshNoticeMap);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("gtRefund.queryRefundTicketOrderId",order_id);
	}

	@Override
	public String querySumRefundMoney(String orderId) {
		return (String)this.getSqlMapClientTemplate().queryForObject("gtRefund.querySumRefundMoney",orderId);
	}

	@Override
	public String queryRefundTicketOrderIdExists(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryRefundTicketOrderIdExists", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryBookOrderInfo", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryBookOrderInfoBx", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryBookOrderInfoCp", orderId);
	}

	@Override
	public void updateRefundNotify(Map<String,Object>refund_Map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateRefundNotify", refund_Map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryGtMerchantinfo() {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryGtMerchantinfo");
	}

	@Override
	public void updateGtRefundNotifyNum(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("gtRefund.updateGtRefundNotifyNum", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("gtRefund.queryRefundStationTicketAdd",mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryRefundStationTicketCpId", paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryCpidIsRefund", paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryMerchantOrderId", orderId);
	}

	@Override
	public void updateGtRefundNotifyStatus(Map<String, Object> refundMap) {
		this.getSqlMapClientTemplate().update("gtRefund.updateGtRefundNotifyStatus", refundMap);
	}

	@Override
	public void deleteRefundNotify(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().delete("gtRefund.deleteRefundNotify", refuseMap);
	}

	@Override
	public int queryRefundCount(String string) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryRefundCount", string);
	}

	public void updateCpRefundStatusFail(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateCpRefundStatusFail", refuse_Map);
	}

	@Override
	public void updateCpRefundStatusSuccess(String string) {
		this.getSqlMapClientTemplate().update("gtRefund.updateCpRefundStatusSuccess", string);
	}

	@Override
	public void updateOutTicketRefundEOP(
			Map<String, Object> outTicketDefeatedMap) {
		this.getSqlMapClientTemplate().update("gtRefund.updateOutTicketRefundEOP", outTicketDefeatedMap);
	}

	@Override
	public void addOrderRefundNotify(Map<String, String> mapAdd) {
		this.getSqlMapClientTemplate().insert("gtRefund.addOrderRefundNotify", mapAdd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryOrderInfo(Object object) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryOrderInfo", object);
	}

	@Override
	public void updateGezhiRefund(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().update("gtRefund.updateGezhiRefund", refuseMap);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().insert("gtRefund.insertLog", map);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateOrder", map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("gtRefund.updateOrderstatusToRobotGai", updateMap);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("gtRefund.updateRefundOpt", map);
	}

	@Override
	public int queryCpidIsRefundNum(Map<String, Object> pMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("gtRefund.queryCpidIsRefundNum", pMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryMoneyByCpId(Map<String, Object> pMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryMoneyByCpId", pMap);
	}

	@Override
	public String queryStatusByOrderId(Map<String, Object> pMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("gtRefund.queryStatusByOrderId", pMap);
	}

	@Override
	public void addRefundStationTicketNotify(Map<String, Object> mapAdd) {
		this.getSqlMapClientTemplate().insert("gtRefund.addRefundStationTicketNotify", mapAdd);
	}

	@Override
	public String queryMerchantIdByOrderId(Map<String, Object> pMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("gtRefund.queryMerchantIdByOrderId", pMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTicketCpId(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryTicketCpId", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCpBuyAndPayMoney(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryCpBuyAndPayMoney", paramMap);
	}

	@Override
	public String queryCpSumRefundMoney(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("gtRefund.queryCpSumRefundMoney", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryChangeRefundTicket(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("gtRefund.queryChangeRefundTicket", paramMap);
	}

	@Override
	public String queryChangeCpPayMoney(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("gtRefund.queryChangeCpPayMoney", paramMap);
	}

}
