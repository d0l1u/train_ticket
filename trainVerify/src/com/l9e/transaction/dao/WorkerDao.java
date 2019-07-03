package com.l9e.transaction.dao;

import java.util.Map;

import com.l9e.transaction.vo.CardBankVo;
import com.l9e.transaction.vo.CardInfoVo;

public interface WorkerDao {

	public void updateWorker(Map<String, Object> map);

	public String queryVerifyCode(String account);
	
	public CardInfoVo queryCardInfoVoByComNo(String comNo);
	
	public CardBankVo queryCardBank(Map<String,String> map); 
	
	public void insertWorkerCodeInfo(Map<String, String> insertMap);
	
	//支付宝余额验证码
	public void insertAlipayCodeInfo(Map<String,String> insertMap); 
}
