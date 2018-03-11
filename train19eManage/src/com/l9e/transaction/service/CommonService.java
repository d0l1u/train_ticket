package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AreaVo;

public interface CommonService {
	
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
