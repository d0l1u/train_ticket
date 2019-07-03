package com.l9e.transaction.dao;

import java.util.List;

import com.l9e.transaction.vo.AreaVo;

public interface CommonDao {

	public String query();
	
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
	
}
