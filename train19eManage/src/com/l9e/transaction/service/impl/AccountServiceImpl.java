package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AreaVo;

@Service("accountService")
public class AccountServiceImpl implements AccountService{

	
	@Resource
	private AccountDao accountDao;
	
	public List<Map<String, String>> queryAccountList(
			Map<String, Object> paramMap) {
		return accountDao.queryAccountList(paramMap);
	}
	public List<Map<String, String>> queryAccountExcel(
			Map<String, Object> paramMap) {
		return accountDao.queryAccountExcel(paramMap);
	}
	
	public int queryAccountListCount(Map<String, Object> paramMap) {
		return accountDao.queryAccountListCount(paramMap);
	}

	public Map<String, String> queryAccount(String acc_id) {
		return accountDao.queryAccount(acc_id);
	}

	public void updateAccount(AccountVo  account) {
		try{
			accountDao.updateAccount(account);
			accountDao.insertAcc_logs(account);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void insertAccount(AccountVo  account) {
		accountDao.insertAccount(account);
	}

	public void deleteAccount(AccountVo account) {
		accountDao.deleteAccount(account);
	}
	
	public List<AreaVo> getProvince() {
		return accountDao.getProvince();
	}

	public List<AreaVo> getCity(String provinceid) {
		return accountDao.getCity(provinceid);
	}

	public List<AreaVo> getArea(String cityid) {
		return accountDao.getArea(cityid);
	}

	public String queryAcc_username(String acc_username) {
		return accountDao.queryAcc_username(acc_username);
	}

	@Override
	public int addRegistersBatch(Map<String, Object> paramMap) {
		//查出指定条数的信息存入list
		List<Map<String, Object>> queryRegisters = accountDao.queryRegisters(paramMap); 
		//在list中添加channel字段，并赋值
		if(queryRegisters!=null && queryRegisters.size()>0){
			
			String channel = (String) paramMap.get("channel");
			String opt_person = (String) paramMap.get("opt_person");
			String account_source = (String) paramMap.get("account_source");
			for(Map<String,Object> map: queryRegisters){
				map.put("channel",channel);
				map.put("acc_status", 33);
				map.put("opt_person", opt_person);
				map.put("at_province_id", "000000");
				map.put("account_source", account_source);
			}
			
//			System.out.println(queryRegisters.get(0).get("channel"));
			//将list中内容插入到cp_accountinfo表中
			
			accountDao.addAccounts(queryRegisters); 
			
			//修改hc_orderinfo_regist表中已转移数据状态和渠道
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("channel", channel);
			map.put("registers", queryRegisters);
			accountDao.updateRegisters(map);
			
		}
		int numbers = queryRegisters.size();
		return numbers;
	}
	
	@Override
	public int startRegistersBatch(Map<String, Object> paramMap) {
		int numbers = accountDao.startAccounts(paramMap); 
		return numbers;
	}
	
	@Override
	public void updateStopStatus() {
		accountDao.updateStopStatus();
	}

	@Override
	public void updateAbatchStop(AccountVo account) {
		accountDao.updateAbatchStop(account);
		
	}

}
