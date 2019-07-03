package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.BalanceAccountDao;
@Repository("balanceAccountDao")
public class BalanceAccountDaoImpl extends BaseDao implements BalanceAccountDao{

	@Override
	public String queryOrderId(String sh_order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("balance.queryOrderId",sh_order_id);
	}

	@Override
	public void insertBalanceAccount(List<Map<String,String>>  list) {
		this.getSqlMapClientTemplate().insert("balance.insertBalanceAccount",list);
	}


	@Override
	public int queryBalanceAccountListCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("balance.queryBalanceAccountListCount",paramMap);
	}

	@Override
	public int updateOrderId(Map<String, String> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().update("balance.updateOrderId",paramMap);
	}

	@Override
	public void insertBalanceAccountOne(Map<String, String> values) {
		this.getSqlMapClientTemplate().insert("balance.insertBalanceAccountOne",values);
	}

	@Override
	public String queryOrderIdByPaySeq(String stringCellValue) {
		return (String)this.getSqlMapClientTemplate().queryForObject("balance.queryOrderIdByPaySeq",stringCellValue);
	}

	@Override
	public String queryOrderIdByOrderInfo(Map<String, String> param) {
		return (String)this.getSqlMapClientTemplate().queryForObject("balance.queryOrderIdByOrderInfo",param);
	}

	@Override
	public int queryFileCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("balance.queryFileCount", paramMap);
	}

	@Override
	public List<Map<String, Object>> queryFileList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("balance.queryFileList", paramMap);
	}
	
	@Override
	public void insertFile(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("balance.insertFile", paramMap);
	}

	@Override
	public void deleteFile(String id) {
		this.getSqlMapClientTemplate().delete("balance.deleteFile", id);
	}

	@Override
	public String queryFilepath(String id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("balance.queryFilepath", id);
	}

	@Override
	public List<Map<String, String>> queryOrderList(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("balance.queryOrderList",param);
	}

	@Override
	public int updateBalancAccount(Map<String, String> param) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().update("balance.updateBalancAccount",param);
	}

	@Override
	public int selectUpdateCount(Map<String, String> param) {
		// TODO Auto-generated method stub
		return (Integer)this.getSqlMapClientTemplate().queryForObject("balance.selectUpdateCount",param);
	}

	@Override
	public List<Map<String,Object>> queryAppRefund(Map<String,String>param) {
		return this.getSqlMapClientTemplate().queryForList("balance.queryAppRefund",param);
	}

	@Override
	public List<Map<String,Object>> queryElongRefund(Map<String,String>param) {
		return this.getSqlMapClientTemplate().queryForList("balance.queryElongRefund",param);
		
	}

	@Override
	public List<Map<String,Object>> queryExtRefund(Map<String,String>param) {
		return this.getSqlMapClientTemplate().queryForList("balance.queryExtRefund",param);
		
	}

	@Override
	public List<Map<String,Object>> queryHcRefund(Map<String,String>param) {
		return this.getSqlMapClientTemplate().queryForList("balance.queryHcRefund",param);
	}

	@Override
	public List<Map<String,Object>> queryInnerRefund(Map<String,String>param) {
		return this.getSqlMapClientTemplate().queryForList("balance.queryInnerRefund",param);
	}

	@Override
	public List<Map<String, Object>> queryQunarRefund(Map<String,String>param) {
		return this.getSqlMapClientTemplate().queryForList("balance.queryQunarRefund",param);
	}

	@Override
	public String queryRefundMoney(String orderId) {
		return (String)this.getSqlMapClientTemplate().queryForObject("balance.queryRefundMoney",orderId);
	}

	@Override
	public List<Map<String, String>> queryBalancAccountList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("balance.queryBalancAccountList",paramMap);
	}
	@Override
	public void updateRefundResult(Map<String, String> updateParam) {
		this.getSqlMapClientTemplate().update("balance.updateRefundResult",updateParam);
	}

}
