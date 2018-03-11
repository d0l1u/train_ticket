package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.AreaVo;

public interface AccountDao {
	List<Map<String, String>> queryAccountList(Map<String, Object> paramMap);

	List<Map<String, String>> queryAccountExcel(Map<String, Object> paramMap);
	
	int queryAccountListCount(Map<String, Object> paramMap);

	Map<String, String> queryAccount(String acc_id);

	void updateAccount(AccountVo  account);
	
	void insertAccount(AccountVo  account);
	
	void deleteAccount(AccountVo  account);
	
	/**
	 * 获取省份
	 * @return
	 */
	public List<AreaVo> getProvince();
	
	
	/**
	 * 获取城市
	 * @return
	 */
	public List<AreaVo>  getCity(String provinceid);
	
	
	/**
	 * 获取区县
	 * @return
	 */
	public List<AreaVo>  getArea(String cityid);

	String queryAcc_username(String acc_username);

	void insertAcc_logs(AccountVo account);

	List<Map<String, Object>> queryRegisters(Map<String, Object> paramMap);

	void addAccounts(List<Map<String, Object>> queryRegisters);
	
	int startAccounts(Map<String, Object> paramMap);

	void updateRegisters(Map<String, Object> map);

	void updateStopStatus();

	void updateAbatchStop(AccountVo accountVo);

}
