package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuRefundDao;
@Repository("tuniuRefundDao")
public class TuniuRefundDaoImpl extends BaseDao implements TuniuRefundDao {

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryRefundTicketCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryRefundTicketList",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryRefundTicketInfo",id_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryRefundTicketInfo",cp_id);
	}

	public void addCpRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().insert("tuniuRefund.addCpRefundTicket_log",log_Map);
	}
	
	public void addOrderRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.addOrderRefundTicket_log", log_Map);
	}

	public void updateRefundTicketInfo(Map<String, Object> refund_Map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateRefundTicketInfo",refund_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryHistroyByCpId",cp_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryrefundTicketAdd(Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("tuniuRefund.refundTicketAdd",mapAdd);
	}
	
	public String queryBuymoneyAndTicketpaymoney(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryBuyMoney_TicketPayMoney",order_id);
	}
	
	public String queryRefundMoney(String stream_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryRefundMoney",stream_id);
	}
	
	public void updateRefund_StatusTo11(Map<String,Object> id_Map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateRefund_StatusTo11",id_Map);
	}

	public void updateOrderInfo_can_refundTo1_And_refund_total(Map<String,String> order_Map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateOrderInfo_can_refundTo1_And_refund_total",order_Map);
	}

	public void updateRefund_StatusTo55(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateRefund_StatusTo55",refuse_Map);
	}

	public void updateDifferRefund(Map<String, Object> differ_Map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateDifferRefund", differ_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryHistroyByOrderId",order_id);
	}

	public void updateOut_Ticket_Refund(
			Map<String, Object> outTicket_Defeated_Map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateOut_Ticket_Refund",outTicket_Defeated_Map);
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateRefreshNotice",update_RefreshNoticeMap);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryRefundTicketOrderId",order_id);
	}

	@Override
	public String querySumRefundMoney(String orderId) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tuniuRefund.querySumRefundMoney",orderId);
	}

	@Override
	public String queryRefundTicketOrderIdExists(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryRefundTicketOrderIdExists", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryBookOrderInfo", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryBookOrderInfoBx", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryBookOrderInfoCp", orderId);
	}

	@Override
	public void updateRefundNotify(Map<String,Object>refund_Map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateRefundNotify", refund_Map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTuniuMerchantinfo() {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryTuniuMerchantinfo");
	}

	@Override
	public void updateTuniuRefundNotifyNum(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateTuniuRefundNotifyNum", paramMap);
	}

	@Override
	public String queryRefundStationTicketAdd(
			Map<String, Object> mapAdd) {
		return (String) this.getSqlMapClientTemplate().insert("tuniuRefund.queryRefundStationTicketAdd",mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryRefundStationTicketCpId", paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryCpidIsRefund", paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryMerchantOrderId", orderId);
	}

	@Override
	public void updateTuniuRefundNotifyStatus(Map<String, Object> refundMap) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateTuniuRefundNotifyStatus", refundMap);
	}

	@Override
	public void deleteRefundNotify(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().delete("tuniuRefund.deleteRefundNotify", refuseMap);
	}

	@Override
	public int queryRefundCount(String string) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryRefundCount", string);
	}

	public void updateCpRefundStatusFail(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateCpRefundStatusFail", refuse_Map);
	}

	@Override
	public void updateCpRefundStatusSuccess(String string) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateCpRefundStatusSuccess", string);
	}

	@Override
	public void updateOutTicketRefundEOP(
			Map<String, Object> outTicketDefeatedMap) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateOutTicketRefundEOP", outTicketDefeatedMap);
	}

	@Override
	public void addOrderRefundNotify(Map<String, String> mapAdd) {
		this.getSqlMapClientTemplate().insert("tuniuRefund.addOrderRefundNotify", mapAdd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryOrderInfo(Object object) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryOrderInfo", object);
	}

	@Override
	public void updateGezhiRefund(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateGezhiRefund", refuseMap);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tuniuRefund.insertLog", map);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateOrder", map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateOrderstatusToRobotGai", updateMap);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("tuniuRefund.updateRefundOpt", map);
	}

	@Override
	public int queryCpidIsRefundNum(Map<String, Object> pMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryCpidIsRefundNum", pMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryMoneyByCpId(Map<String, Object> pMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryMoneyByCpId", pMap);
	}

	@Override
	public String queryStatusByOrderId(Map<String, Object> pMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryStatusByOrderId", pMap);
	}

	@Override
	public void addRefundStationTicketNotify(Map<String, Object> mapAdd) {
		this.getSqlMapClientTemplate().insert("tuniuRefund.addRefundStationTicketNotify", mapAdd);
	}

	@Override
	public String queryMerchantIdByOrderId(Map<String, Object> pMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryMerchantIdByOrderId", pMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTicketCpId(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryTicketCpId", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCpBuyAndPayMoney(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryCpBuyAndPayMoney", paramMap);
	}

	@Override
	public String queryCpSumRefundMoney(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryCpSumRefundMoney", paramMap);
	}

	@Override
	public String queryRefundTime(Map<String, Object> param) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tuniuRefund.queryRefundTime", param);
	}

	@Override
	public List<Map<String, String>> queryChangeRefundTicket(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("tuniuRefund.queryChangeRefundTicket", paramMap);
	}

}
