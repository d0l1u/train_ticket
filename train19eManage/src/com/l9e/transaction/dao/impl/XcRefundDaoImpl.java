package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.XcRefundDao;
@Repository("xcRefundDao")
public class XcRefundDaoImpl extends BaseDao implements XcRefundDao {

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("xcRefund.queryRefundTicketCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryRefundTicketList",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryRefundTicketInfo",id_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryRefundTicketInfo",cp_id);
	}

	public void addCpRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().insert("xcRefund.addCpRefundTicket_log",log_Map);
	}
	
	public void addOrderRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().update("xcRefund.addOrderRefundTicket_log", log_Map);
	}

	public void updateRefundTicketInfo(Map<String, Object> refund_Map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateRefundTicketInfo",refund_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryHistroyByCpId",cp_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryrefundTicketAdd(Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("xcRefund.refundTicketAdd",mapAdd);
	}
	
	public String queryBuymoneyAndTicketpaymoney(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryBuyMoney_TicketPayMoney",order_id);
	}
	
	public String queryRefundMoney(String stream_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryRefundMoney",stream_id);
	}
	
	public void updateRefund_StatusTo11(Map<String,Object> id_Map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateRefund_StatusTo11",id_Map);
	}

	public void updateOrderInfo_can_refundTo1_And_refund_total(Map<String,String> order_Map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateOrderInfo_can_refundTo1_And_refund_total",order_Map);
	}

	public void updateRefund_StatusTo55(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateRefund_StatusTo55",refuse_Map);
	}

	public void updateDifferRefund(Map<String, Object> differ_Map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateDifferRefund", differ_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryHistroyByOrderId",order_id);
	}

	public void updateOut_Ticket_Refund(
			Map<String, Object> outTicket_Defeated_Map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateOut_Ticket_Refund",outTicket_Defeated_Map);
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		this.getSqlMapClientTemplate().update("xcRefund.updateRefreshNotice",update_RefreshNoticeMap);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("xcRefund.queryRefundTicketOrderId",order_id);
	}

	@Override
	public String querySumRefundMoney(String orderId) {
		return (String)this.getSqlMapClientTemplate().queryForObject("xcRefund.querySumRefundMoney",orderId);
	}

	@Override
	public String queryRefundTicketOrderIdExists(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryRefundTicketOrderIdExists", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryBookOrderInfo", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryBookOrderInfoBx", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryBookOrderInfoCp", orderId);
	}

	@Override
	public void updateRefundNotify(Map<String,Object>refund_Map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateRefundNotify", refund_Map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryXcMerchantinfo() {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryXcMerchantinfo");
	}

	@Override
	public void updateXcRefundNotifyNum(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("xcRefund.updateXcRefundNotifyNum", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("xcRefund.queryRefundStationTicketAdd",mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryRefundStationTicketCpId", paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryCpidIsRefund", paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryMerchantOrderId", orderId);
	}

	@Override
	public void updateXcRefundNotifyStatus(Map<String, Object> refundMap) {
		this.getSqlMapClientTemplate().update("xcRefund.updateXcRefundNotifyStatus", refundMap);
	}

	@Override
	public void deleteRefundNotify(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().delete("xcRefund.deleteRefundNotify", refuseMap);
	}

	@Override
	public int queryRefundCount(String string) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryRefundCount", string);
	}

	public void updateCpRefundStatusFail(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateCpRefundStatusFail", refuse_Map);
	}

	@Override
	public void updateCpRefundStatusSuccess(String string) {
		this.getSqlMapClientTemplate().update("xcRefund.updateCpRefundStatusSuccess", string);
	}

	@Override
	public void updateOutTicketRefundEOP(
			Map<String, Object> outTicketDefeatedMap) {
		this.getSqlMapClientTemplate().update("xcRefund.updateOutTicketRefundEOP", outTicketDefeatedMap);
	}

	@Override
	public void addOrderRefundNotify(Map<String, String> mapAdd) {
		this.getSqlMapClientTemplate().insert("xcRefund.addOrderRefundNotify", mapAdd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryOrderInfo(Object object) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryOrderInfo", object);
	}

	@Override
	public void updateGezhiRefund(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().update("xcRefund.updateGezhiRefund", refuseMap);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().insert("xcRefund.insertLog", map);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateOrder", map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("xcRefund.updateOrderstatusToRobotGai", updateMap);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("xcRefund.updateRefundOpt", map);
	}

	@Override
	public int queryCpidIsRefundNum(Map<String, Object> pMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryCpidIsRefundNum", pMap);
	}

	@Override
	public String queryMerchantIdByOrderId(Map<String, Object> pMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryMerchantIdByOrderId", pMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryMoneyByCpId(Map<String, Object> pMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryMoneyByCpId", pMap);
	}

	@Override
	public String queryStatusByOrderId(Map<String, Object> pMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("xcRefund.queryStatusByOrderId", pMap);
	}

	@Override
	public void addRefundStationTicketNotify(Map<String, Object> mapAdd) {
		this.getSqlMapClientTemplate().insert("xcRefund.addRefundStationTicketNotify", mapAdd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTicketCpId(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryTicketCpId", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCpBuyAndPayMoney(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("xcRefund.queryCpBuyAndPayMoney", paramMap);
	}

	@Override
	public String queryCpSumRefundMoney(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("xcRefund.queryCpSumRefundMoney", paramMap);
	}


}
