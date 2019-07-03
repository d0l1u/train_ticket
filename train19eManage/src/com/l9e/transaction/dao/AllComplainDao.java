package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AllComplainVo;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.ComplainVo;

public interface AllComplainDao {
int queryComplainListCount(Map<String, Object> paramMap);
	
	List<Map<String,String>>queryComplainList(Map<String,Object>paramMap);
	
	Map<String, String> queryComplainParticularInfo(String complain_id);
	
	void updateComplainInfo(AllComplainVo complain);

	void deleteComplain(String complain_id);

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

	List<Map<String,String>> queryComplainStatCount();

	Map<String, String> querySupervise_nameToArea_no(String string);

	List<Map<String, Object>> queryHistroyByComplainId(String complainId);

	void addComplainHistoryInfo(AllComplainVo complain);
	
	void insertLog(Map<String, String> map);

}
