package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TrainSystemSettingDao;
import com.l9e.transaction.vo.SystemSettingVo;

@Repository("trainSystemSettingDao")
public class TrainSystemSettingDaoImpl extends BaseDao implements TrainSystemSettingDao{

	@Override
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().delete("trainSystemsetting.deleteURL",systemSettingVo);
	}

	@SuppressWarnings("unchecked")
	public List<SystemSettingVo> getSystemSetting() {
		return this.getSqlMapClientTemplate().queryForList(
				"trainSystemsetting.querySystemSetting");
	}

	@Override
	public void insertInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().insert("trainSystemsetting.insertURL",
				systemSettingVo);
	}

	@Override
	public void updateInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("trainSystemsetting.updateURL",
				systemSettingVo);
	}

	@Override
	public void updateInterfaceChannel(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("trainSystemsetting.updateChannel",
				systemSettingVo);

	}

	@Override
	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("trainSystemsetting.updateConTimeout",
				systemSettingVo);

	}

	@Override
	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"trainSystemsetting.updateReadTimeout", systemSettingVo);

	}
	
	public void changeURLStatus(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"trainSystemsetting.changeURLStatus", systemSettingVo);
	}
	
	public SystemSettingVo getSystemSettingById(String setting_id) {
		try {
			return (SystemSettingVo)(this.getSqlMapClient().queryForObject("trainSystemsetting.getSystemSettingById", setting_id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updatePayCtrl(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"trainSystemsetting.updatePayCtrl", systemSettingVo);
	}

	public void addSystemLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("trainSystemsetting.addSystemLog", log);
	}
	
	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo){
		this.getSqlMapClientTemplate().update("trainSystemsetting.updateChannels", systemSettingVo);
	}
	
	//切换渠道
	public void updateChannelRh(SystemSettingVo systemSettingVo){
		this.getSqlMapClientTemplate().update("trainSystemsetting.updateChannelsRh", systemSettingVo);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("trainSystemsetting.querySystemSetList",paramMap);
	}

	public int querySystemSetListCount() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("trainSystemsetting.querySystemSetListCount");
	}

	@Override
	public Object querySystemRefundAndAlert(String string) {
		return this.getSqlMapClientTemplate().queryForObject("trainSystemsetting.querySystemRefundAndAlert", string);
	}
	
	//查询返回日志
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> querytrain_return_optlogList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("trainSystemsetting.querytrain_return_optlogList",paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> querytrain_return_optlog() {
		return this.getSqlMapClientTemplate().queryForList("trainSystemsetting.querytrain_return_optlog");
	}
	public int querytrain_return_optlogCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("trainSystemsetting.querytrain_return_optlogCount",paramMap);
	}

	//修改返回日志
	public void updatetrain_return_optlog(Map<String, Object> paramMap){
		this.getSqlMapClientTemplate().update("trainSystemsetting.updatetrain_return_optlog", paramMap);
	}
	//增加返回日志
	public void addtrain_return_optlog(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("trainSystemsetting.addtrain_return_optlog", paramMap);
	}
	//验证编号是否存在
	public String queryreturn_optlogById(String return_id){
		return (String) this.getSqlMapClientTemplate().queryForObject("trainSystemsetting.queryreturn_optlogById",return_id);
	}
	//删除返回日志
	public void deletetrain_return_optlog(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().delete("trainSystemsetting.deletetrain_return_optlog",paramMap);

	}

	@Override
	public void addSetting(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().insert("trainSystemsetting.addSetting",systemSettingVo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryCodeInfo() {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("trainSystemsetting.queryCodeInfo");
	}

	@Override
	public void updateCodeInfo(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("trainSystemsetting.updateCodeInfo", paramMap);
	}
	
}
