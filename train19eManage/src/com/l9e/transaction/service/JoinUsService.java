package com.l9e.transaction.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.JoinDetailVo;
import com.l9e.transaction.vo.JoinUsVo;

public interface JoinUsService {
	
	public int queryJoinUsListCount(Map<String, Object> paramMap);
	
	public List<Map<String,String>>queryJoinUsList(
			Map<String,Object>paramMap) ;
	
//	public List<Map<String, String>> queryJoinUsListSwitch(
//			Map<String, Object> paramMap);
	
	public Map<String, String> queryUpdateJoinUsInfo(String user_id);
	
	public void updateJoinUs(JoinUsVo join);
	
	public void deleteJoinUs(JoinUsVo join);
	
	public List<Map<String, String>> queryJoinDetail(Map<String,Object>paramMap);
	
	public ArrayList<JoinDetailVo> queryJoinDetailNowMouth(String user_id);
	
	public ArrayList<JoinDetailVo> queryJoinDetailPreMouth(String user_id);
	
	
	public Map<String, String> querySumNow(String user_id);//查询单个user的本月总计
	
	
	public Map<String,String> querySumPre(String user_id) ;
	
	
	public Map<String, String> queryLastCreate(String user_id);
	
	public int queryUserOrderCount(Map<String, Object> paramMap);
	
	public ArrayList<JoinDetailVo> queryUserOrder(String user_id);
	
	public String queryGetEstate(String user_id);
	

	
	
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

	public void updateEstatePass(String user_id);

	public void updateEstateNot(String user_id);

	public List<String> queryJoinUsEstateCount();
	//操作日志和操作人
	public void updateOpt_ren(Map<String, String> map);

	//省级负责人
	Map<String, String> querySupervise_nameToArea_no(String area_name);

	public List<Map<String, String>> queryUserRegistInfo(
			Map<String, Object> paramMap);

	public int ueryUserRegistInfoCount(Map<String, Object> paramMap);
	




}

