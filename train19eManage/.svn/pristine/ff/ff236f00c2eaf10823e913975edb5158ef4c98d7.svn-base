package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AcquireDao;
import com.l9e.transaction.vo.AcquireVo;

@Repository("acquireDao")
public class AcquireDaoImpl extends BaseDao implements AcquireDao {
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAcquireList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("acquire.queryAcquireList", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAcquireListCp(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("acquire.queryAcquireListCp", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAcquireExcel(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("acquire.queryAcquireExcel", paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAcquireFailExcel(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("acquire.queryAcquireFailExcel", paramMap);
	}
	
	
	@Override
	public int queryAcquireListCount(Map<String, Object> paramMap) {
		return getTotalRows("acquire.queryAcquireListCount", paramMap);
	}
	@Override
	public int queryAcquireListCountCp(Map<String, Object> paramMap) {
		return getTotalRows("acquire.queryAcquireListCountCp", paramMap);
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> queryAcquireOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("acquire.queryAcquireOrderInfo", order_id);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryAcquireOrderInfoCp(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("acquire.queryAcquireOrderInfoCp", order_id);
	}
	@Override
	public void updateAcquire(AcquireVo acquire) {
		this.getSqlMapClientTemplate().update("acquire.updateAcquire", acquire);
	}
	@Override
	public void updateEndOpt_Ren(Map<String, String> userMap) {
		this.getSqlMapClientTemplate().update("acquire.updateEndOpt_Ren",userMap);
	}
	@Override
	public void updateNotify(AcquireVo acquire) {
		//给前台发送通知
		this.getSqlMapClientTemplate().update("acquire.updateNotify", acquire.getOrder_id());
	}
	@Override
	public void freeAccount(String accountId) {
		//如果状态为成功或者失败，释放12306账号的
		
		this.getSqlMapClientTemplate().update("acquire.freeAccount", accountId);
		
	}
//	@SuppressWarnings("unchecked")
//	public Map<String, String> queryPayOrderInfo(String order_id) {
//		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("acquire.queryPayOrderInfo", order_id);
//	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>>  queryHistroyByOrderId(String order_id) {
		String sql = (String) this.getSqlMapClientTemplate().queryForObject("acquire2.querysqlSetting");
		List<Map<String, Object>> list=null;
		if("2".equals(sql)){
			list = this.getSqlMapClient2().queryForList("acquire2.queryOrderInfoHistroy", order_id);
		}else if("1".equals(sql)){
			list = this.getSqlMapClientTemplate().queryForList("acquire2.queryOrderInfoHistroy", order_id);
		}
		return list;
	}
	@Override
	public void updateAccount(Map<String,String>map) {
		this.getSqlMapClientTemplate().update("acquire.updateAccount",map);
	}
	
	@Override
	public void updateAccountToNull(Map<String,String>map) {
		this.getSqlMapClientTemplate().update("acquire.updateAccountToNull",map);
	}
	
	@Override
	public String queryAccount(String channel) {
		return (String)this.getSqlMapClientTemplate().queryForObject("acquire.queryAccount",channel);
	}
	@Override
	public void updateAccountAssoil(Map<String,String>map) {
		this.getSqlMapClientTemplate().update("acquire.updateAccountAssoil",map);
	}
	@Override
	public void updateRegistTo33(Map<String,String>map) {
		this.getSqlMapClientTemplate().update("acquire.updateRegistTo33",map);
	}
	@Override
	public void updateAccountStart(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("acquire.updateAccountStart",map);
	}
	@Override
	public void updateCp_Orderinfo_Buy_money(Map<String,String> map) {
		this.getSqlMapClientTemplate().update("acquire.updateCp_Orderinfo_Buy_money",map);
	}
	@Override
	public void updateCp_Orderinfo_Cp(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("acquire.updateCp_Orderinfo_Cp",map);
	}
	@Override
	public void updateCp_Orderinfo_Notify(String order_id) {
		this.getSqlMapClientTemplate().update("acquire.updateCp_Orderinfo_Notify",order_id);
	}
	@Override
	public String queryDbOrder_status(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("acquire.queryDbOrder_status",order_id);
	}
	
	@Override
	public void addUserAccount(Map<String,String>paramMap) {
		this.getSqlMapClientTemplate().insert("acquire.addUserAccount",paramMap);
	}
	@Override
	public void updateInfoOrderStatus(Map<String,String>paramMap) {
		this.getSqlMapClientTemplate().update("acquire.updateInfoOrderStatus",paramMap);
	}
	public void updateChangeSeatType(Map<String, String> modify_Map) {
		this.getSqlMapClientTemplate().update("acquire.updateChangeSeatType",modify_Map);
	}
	public void updateOrderInfo(Map<String, String> modify_CpMap) {
		this.getSqlMapClientTemplate().update("acquire.updateOrderInfo",modify_CpMap);
	}
	public void updateOrderInfo_cp(Map<String, String> modify_CpMap) {
		this.getSqlMapClientTemplate().update("acquire.updateOrderInfo_cp",modify_CpMap);
	}
	public void updateOrderInfoFor61(Map<String, String> update_Map) {
		this.getSqlMapClientTemplate().update("acquire.updateOrderInfoFor61",update_Map);
	}
	public void updateOrderInfoFor45(Map<String, String> update_Map) {
		this.getSqlMapClientTemplate().update("acquire.updateOrderInfoFor45",update_Map);
	}
	@Override
	public void updateReceiveCpInfo(Map<String, String> cp_Map) {
		this.getSqlMapClientTemplate().update("acquire.updateReceiveCpInfo",cp_Map);
	}
	@Override
	public void updateReceiveOrderInfo(Map<String, String> order_Map) {
		this.getSqlMapClientTemplate().update("acquire.updateReceiveOrderInfo",order_Map);
	}
	@Override
	public String queryOrder_time(String order_time_) {
		return (String)this.getSqlMapClientTemplate().queryForObject("acquire.queryOrder_time");
	}
	@Override
	public void updateNotifyToBookSuccess(String order_id) {
		this.getSqlMapClientTemplate().update("acquire.updateNotify",order_id);
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryCpInfo(String cpId) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("acquire.queryCpInfo",cpId);
	}
	@Override
	public void updateOrderStatus(String order_status, String order_id) {
		Map<String,String>map = new HashMap<String,String>();
		map.put("order_id", order_id);
		map.put("order_status", order_status);
		this.getSqlMapClientTemplate().update("acquire.updateOrderStatus",map);
	}
	@Override
	public void updateLockAccount(String lockAccount) {
		this.getSqlMapClientTemplate().update("acquire.updateLockAccount",lockAccount);
	}
	@Override
	public void updateAcquireToRobot(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("acquire.updateAcquireToRobot",updateMap);
	}
	@Override
	public String queryCMpayOrderStatus(Map<String, String> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("acquire.queryCMpayOrderStatus", paramMap);
	}
	@Override
	public String queryCodeType() {
		return (String) this.getSqlMapClientTemplate().queryForObject("acquire.queryCodeType");
	}
	@Override
	public String queryRobotRandom() {
		return (String) this.getSqlMapClientTemplate().queryForObject("acquire.queryRobotRandom");
	}
	@SuppressWarnings("unchecked")
	public Map<String,String> queryMoney(Map<String, String> updateMap) {
		return (Map<String,String>) this.getSqlMapClientTemplate().queryForObject("acquire.queryMoney", updateMap);
	}
	@Override
	public void updateCpPrice(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("acquire.updateCpPrice",updateMap);
	}
	@Override
	public String queryOrderMoney(Map<String, String> updateMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("acquire.queryOrderMoney", updateMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAcquireOvertimeList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("acquire.queryAcquireOvertimeList", paramMap);
	}
	@Override
	public int queryOvertimeListCount(Map<String, Object> paramMap) {
		return getTotalRows("acquire.queryOvertimeListCount", paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAcquireFailList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("acquire.queryAcquireFailList", paramMap);
	}
	@Override
	public int queryAcquireFailListCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("acquire.queryAcquireFailListCount", paramMap);
	}
	@Override
	public String queryCurrentCodeType() {
		return (String)this.getSqlMapClientTemplate().queryForObject("acquire.queryCurrentCodeType");
	}
	@Override
	public void updateInfoOrderStatusTo55(Map<String,String> map) {
		this.getSqlMapClientTemplate().update("acquire.updateInfoOrderStatusTo55", map);
	}
	@Override
	public String queryOrderIsPay(String order_id){
		return (String)this.getSqlMapClientTemplate().queryForObject("acquire.queryOrderIsPay",order_id);
	}

	@Override
	public int queryAccountContactNum(String accountId) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("acquire.queryAccountContactNum", accountId);
	}
	@Override
	public void updateAccountStop(String accountId) {
		this.getSqlMapClientTemplate().update("acquire.updateAccountStop", accountId);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAcquireExcelXl(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("acquire.queryAcquireExcelXl", paramMap);
	}

	@Override
	public int queryAcquireListCountXl(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("acquire.queryAcquireListCountXl", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAcquireListXl(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("acquire.queryAcquireListXl", paramMap);
	}

	@Override
	public void updateStatus00To11(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("acquire.updateStatus00To11",paramMap);
	}

	@Override
	public void updateManualToRobot(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("acquire.updateManualToRobot",updateMap);
	}

	@Override
	public void updateCtripToRobot(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("acquire.updateCtripToRobot",updateMap);
	}

	@Override
	public void ctripSearchAgain(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("acquire.ctripSearchAgain",map);
	}

	@Override
	public void updateCtripGoStatusTo44(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("acquire.updateCtripGoStatusTo44",updateMap);
	}

	@Override
	public int updateInfoOrderStatusToManual(Map<String, String> param_map) {
		return this.getSqlMapClientTemplate().update("acquire.updateInfoOrderStatusToManual",param_map);
	}

	

}
