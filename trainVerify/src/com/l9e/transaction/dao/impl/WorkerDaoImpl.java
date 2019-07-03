package com.l9e.transaction.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.WorkerDao;
import com.l9e.transaction.vo.CardBankVo;
import com.l9e.transaction.vo.CardInfoVo;

@Repository("workerDao")
public class WorkerDaoImpl extends BaseDao implements WorkerDao {

	
	public void updateWorker(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("worker.updateWorker", map);
	}

	
	public String queryVerifyCode(String account) {
		return (String) this.getSqlMapClientTemplate().queryForObject("worker.queryVerifyCode", account);
	}

	
	public CardInfoVo queryCardInfoVoByComNo(String comNo) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("worker.queryCardInfoVoByComNo",comNo);
		if(obj instanceof CardInfoVo){
			return (CardInfoVo)obj;
		}
		return null;
	}

	
	public void insertWorkerCodeInfo(Map<String, String> insertMap) {
		this.getSqlMapClientTemplate().insert("worker.insertWorkerCodeInfo",insertMap);
		
	}


	public void insertAlipayCodeInfo(Map<String, String> insertMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("worker.insertAlipayCodeInfo", insertMap);
	}


	@Override
	public CardBankVo queryCardBank(Map<String, String> map) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("worker.queryCardBank",map);
		if(obj instanceof CardBankVo){
			return (CardBankVo)obj;
		}
		return null;
	}

}
