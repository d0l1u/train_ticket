package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.SystemSettingVo;

public interface TrainSystemSettingDao {

	/*
	 * 获取系统设置信息
	 */
	public List<SystemSettingVo> getSystemSetting();
	
	public void updateInterfaceChannel(SystemSettingVo systemSettingVo);
	
	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo);
	
	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo);
	
	public void updateInterface12306URL(SystemSettingVo systemSettingVo);
	
	public void insertInterface12306URL(SystemSettingVo systemSettingVo);
	
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo);
	
	public void changeURLStatus(SystemSettingVo systemSettingVo);
	
	public SystemSettingVo getSystemSettingById(String setting_id);

	public void updatePayCtrl(SystemSettingVo systemSettingVo);

	public void addSystemLog(Map<String, String> log);
	
	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo);
	
	public void updateChannelRh(SystemSettingVo systemSettingVo);

	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap);

	public int querySystemSetListCount();

	public Object querySystemRefundAndAlert(String string);

	//查询返回日志
	public List<Map<String,Object>> querytrain_return_optlog();
	public List<Map<String,Object>> querytrain_return_optlogList(Map<String, Object> paramMap);
	public int querytrain_return_optlogCount(Map<String, Object> paramMap);
	//修改返回日志
	public void updatetrain_return_optlog(Map<String, Object> paramMap);
	//增加返回日志
	public void addtrain_return_optlog(Map<String, Object> paramMap) ;
	//验证编号是否存在
	String queryreturn_optlogById(String return_id);
	//删除返回日志
	public void deletetrain_return_optlog(Map<String, Object> paramMap) ;
	
	public void addSetting(SystemSettingVo systemSettingVo);

	public Map<String, String> queryCodeInfo();

	public void updateCodeInfo(Map<String, Object> paramMap);

	
}
