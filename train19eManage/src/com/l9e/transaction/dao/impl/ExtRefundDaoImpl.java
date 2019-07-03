package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ExtRefundDao;
@Repository("extRefundDao")
public class ExtRefundDaoImpl extends BaseDao implements ExtRefundDao {

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("extRefund.queryRefundTicketCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryRefundTicketList",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryRefundTicketInfo",id_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryRefundTicketInfo",cp_id);
	}

	public void addCpRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().insert("extRefund.addCpRefundTicket_log",log_Map);
	}
	
	public void addOrderRefundTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().update("extRefund.addOrderRefundTicket_log", log_Map);
	}

	public void updateRefundTicketInfo(Map<String, Object> refund_Map) {
		this.getSqlMapClientTemplate().update("extRefund.updateRefundTicketInfo",refund_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryHistroyByCpId",cp_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryrefundTicketAdd(Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("extRefund.refundTicketAdd",mapAdd);
	}
	
	@SuppressWarnings("unchecked")
	public String queryBuymoneyAndTicketpaymoney(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("extRefund.queryBuyMoney_TicketPayMoney",order_id);
	}
	
	@SuppressWarnings("unchecked")
	public String queryRefundMoney(String stream_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("extRefund.queryRefundMoney",stream_id);
	}
	
	public void updateRefund_StatusTo11(Map<String,Object> id_Map) {
		this.getSqlMapClientTemplate().update("extRefund.updateRefund_StatusTo11",id_Map);
	}

	public void updateOrderInfo_can_refundTo1_And_refund_total(Map<String,String> order_Map) {
		this.getSqlMapClientTemplate().update("extRefund.updateOrderInfo_can_refundTo1_And_refund_total",order_Map);
	}

	public void updateRefund_StatusTo55(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("extRefund.updateRefund_StatusTo55",refuse_Map);
	}

	public void updateDifferRefund(Map<String, Object> differ_Map) {
		this.getSqlMapClientTemplate().update("extRefund.updateDifferRefund", differ_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryHistroyByOrderId",order_id);
	}

	public void updateOut_Ticket_Refund(
			Map<String, Object> outTicket_Defeated_Map) {
		this.getSqlMapClientTemplate().update("extRefund.updateOut_Ticket_Refund",outTicket_Defeated_Map);
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		this.getSqlMapClientTemplate().update("extRefund.updateRefreshNotice",update_RefreshNoticeMap);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("extRefund.queryRefundTicketOrderId",order_id);
	}

	@Override
	public String querySumRefundMoney(String orderId) {
		return (String)this.getSqlMapClientTemplate().queryForObject("extRefund.querySumRefundMoney",orderId);
	}

	@Override
	public String queryRefundTicketOrderIdExists(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("extRefund.queryRefundTicketOrderIdExists", orderId);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("extRefund.queryBookOrderInfo", orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryBookOrderInfoBx", orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryBookOrderInfoCp", orderId);
	}

	@Override
	public void updateRefundNotify(Map<String,Object>refund_Map) {
		this.getSqlMapClientTemplate().update("extRefund.updateRefundNotify", refund_Map);
	}

	@Override
	public List<Map<String, String>> queryExtMerchantinfo() {
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryExtMerchantinfo");
	}

	@Override
	public void updateExtRefundNotifyNum(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("extRefund.updateExtRefundNotifyNum", paramMap);
	}

	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("extRefund.queryRefundStationTicketAdd",mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("extRefund.queryRefundStationTicketCpId", paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("extRefund.queryCpidIsRefund", paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("extRefund.queryMerchantOrderId", orderId);
	}

	@Override
	public void updateExtRefundNotifyStatus(Map<String, Object> refundMap) {
		this.getSqlMapClientTemplate().update("extRefund.updateExtRefundNotifyStatus", refundMap);
	}

	@Override
	public void deleteRefundNotify(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().delete("extRefund.deleteRefundNotify", refuseMap);
	}

	@Override
	public int queryRefundCount(String string) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("extRefund.queryRefundCount", string);
	}

	public void updateCpRefundStatusFail(Map<String, Object> refuse_Map) {
		this.getSqlMapClientTemplate().update("extRefund.updateCpRefundStatusFail", refuse_Map);
	}

	@Override
	public void updateCpRefundStatusSuccess(String string) {
		this.getSqlMapClientTemplate().update("extRefund.updateCpRefundStatusSuccess", string);
	}

	@Override
	public void updateOutTicketRefundEOP(
			Map<String, Object> outTicketDefeatedMap) {
		this.getSqlMapClientTemplate().update("extRefund.updateOutTicketRefundEOP", outTicketDefeatedMap);
	}

	@Override
	public void addOrderRefundNotify(Map<String, String> mapAdd) {
		this.getSqlMapClientTemplate().insert("extRefund.addOrderRefundNotify", mapAdd);
	}

	@Override
	public List<Map<String, String>> queryOrderInfo(Object object) {
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryOrderInfo", object);
	}

	@Override
	public void updateGezhiRefund(Map<String, Object> refuseMap) {
		this.getSqlMapClientTemplate().update("extRefund.updateGezhiRefund", refuseMap);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().insert("extRefund.insertLog", map);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("extRefund.updateOrder", map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("extRefund.updateOrderstatusToRobotGai", updateMap);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("extRefund.updateRefundOpt", map);
	}

	@Override
	public List<Map<Object, Object>> queryExtOrderInfo(String order_id) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("extRefund.queryExtOrderInfo", order_id);
	}

	@Override
	public int queryRefundNotifyNum(Map<String, String> param_eop) {
		// TODO Auto-generated method stub
		return (Integer) this.getSqlMapClientTemplate().queryForObject("extRefund.queryRefundNotifyNum", param_eop);
	}

	@Override
	public void addRefundNotify(Map<String, String> param_eop) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("extRefund.addRefundNotify", param_eop);
	}

	@Override
	public String findExtRefundNotifyUrl(Map<String, String> queryMap) {
		// TODO Auto-generated method stub
		return  (String) this.getSqlMapClientTemplate().queryForObject("extRefund.findExtRefundNotifyUrl", queryMap);
	}


}
