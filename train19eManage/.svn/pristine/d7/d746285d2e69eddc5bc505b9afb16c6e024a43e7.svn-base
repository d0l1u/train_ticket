package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AppSystemSettingDao;
import com.l9e.transaction.vo.SystemSettingVo;
@Repository("appSystemSettingDao")
public class AppSystemSettingDaoImpl extends BaseDao implements AppSystemSettingDao {

	@Override
	public void addSystemLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("appSystemsetting.addSystemLog", log);
	}

	@Override
	public List<SystemSettingVo> getSystemSetting() {
		return this.getSqlMapClientTemplate().queryForList("appSystemsetting.querySystemSetting");
	}

	@Override
	public SystemSettingVo getSystemSettingById(String settingId) {
		try {
			return (SystemSettingVo)(this.getSqlMapClient().queryForObject("appSystemsetting.getSystemSettingById", settingId));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("appSystemsetting.querySystemSetList",paramMap);
	}

	@Override
	public int querySystemSetListCount() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("appSystemsetting.querySystemSetListCount");
	}

	@Override
	public void updateChannel(SystemSettingVo bxChannel) {
		this.getSqlMapClientTemplate().update("appSystemsetting.updateChannels", bxChannel);
	}

	@Override
	public void insertInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().insert("appSystemsetting.insertURL",
				systemSettingVo);
	}

	@Override
	public void changeURLStatus(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"appSystemsetting.changeURLStatus", systemSettingVo);
		
	}

	@Override
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().delete("appSystemsetting.deleteURL",
				systemSettingVo);
	}

	@Override
	public void updateInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("appSystemsetting.updateURL",
				systemSettingVo);
	}

}
