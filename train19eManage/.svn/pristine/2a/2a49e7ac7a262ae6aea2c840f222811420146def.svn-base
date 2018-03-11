package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AllRefundDao;
@Repository("allRefundDao")
public class AllRefundDaoImpl extends BaseDao implements AllRefundDao{

	// 获取按条件查询的订单数 
	public int queryAllRefundCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("allRefund.queryAllRefundCounts",paramMap);
	}
	//获取按条件查询所有的订单
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllRefundList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("allRefund.queryAllRefundList",paramMap);
	}
	//导出excel
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllRefundExcel(Map<String, Object> paramMap){
		return this.getSqlMapClientTemplate().queryForList("allRefund.queryAllRefundExcel",paramMap);
	}
	//查询明细订单退款明细
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllRefundInfo(Map<String,Object>paramMap) {
		return this.getSqlMapClientTemplate().queryForList("allRefund.queryAllRefundInfo",paramMap);
	}
	//增加操作日志 
	public void addAllRefundlog(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().insert("allRefund.addAllRefundlog",log_Map);
	}
	//执行退款
	public int updateAllRefundInfo(Map<String, Object> refund_Map) {
		return (Integer)this.getSqlMapClientTemplate().update("allRefund.updateAllRefundInfo",refund_Map);
	}
	//查询退款日志
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroy(Map<String, Object> refund_Map)  {
		return this.getSqlMapClientTemplate().queryForList("allRefund.queryHistroy",refund_Map);
	}
	//增加线下/差额退票
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> addAllRefund(Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("allRefund.addAllRefund",mapAdd);
	}
	//更新refund表中操作人信息
	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("allRefund.updateRefundOpt", map);
	}
	//拒绝退款时修改状态
	public void updateRefuse(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("allRefund.updateRefuse", map);
	}
	//限制金额
	@Override
	public String querySumRefundMoney(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("allRefund.querySumRefundMoney",paramMap);
	}
	//限制金额各渠道
	@Override
	public String queryelongOrtongcheng(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("allRefund.queryelongOrtongcheng",paramMap);
	}
	@Override
	public String queryGaotie(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("allRefund.queryGaotie",paramMap);
	}
	@Override
	public String queryXiecheng(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("allRefund.queryXiecheng",paramMap);
	}
	@Override
	public String queryTuniu(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("allRefund.queryTuniu",paramMap);
	}
	@Override
	public String queryMeituan(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("allRefund.queryMeituan",paramMap);
	}
	@Override
	public String queryl9e(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("allRefund.queryl9e",paramMap);
	}
	
	@Override
	public String queryBuymoneyAndTicketpaymoney(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("allRefund.queryBuyMoney_TicketPayMoney",paramMap);
	}
	@Override
	public String queryRefundMoney(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("allRefund.queryRefundMoney",paramMap);
	}
	//重新通知
	@Override
	public int updateNotify_Again(Map<String, String> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().update("allRefund.updateNotify_Again", paramMap);
	}
	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("allRefund.updateOrderstatusToRobotGai", updateMap);
	}
	public void updateOrderInfo_can_refundTo1_And_refund_total(Map<String,Object> order_Map) {
		this.getSqlMapClientTemplate().update("allRefund.updateOrderInfo_can_refundTo1_And_refund_total",order_Map);
	}
	@SuppressWarnings("unchecked")
	public List<String> queryManualOrderList() {
		return this.getSqlMapClientTemplate().queryForList("allRefund.queryManualOrderList");
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryAlterInfo(Map<String, String> map) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("allRefund.queryAlterInfo",map);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAllRefund(Map<String, String> map) {
		return this.getSqlMapClientTemplate().queryForList("allRefund.queryAllRefund",map);
	}
	@Override
	public int queryCpIdCount(Map<String, String> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("allRefund.queryCpIdCount",map);
	}
	@Override
	public void updateAccountToManual(String accountName) {
		this.getSqlMapClientTemplate().update("allRefund.updateAccountToManual",accountName);
	}
	
}
