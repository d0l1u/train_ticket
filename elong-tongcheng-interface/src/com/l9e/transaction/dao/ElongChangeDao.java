package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ElongChangeInfo;
import com.l9e.transaction.vo.ElongChangeLogVO;
import com.l9e.transaction.vo.ElongChangePassengerInfo;

public interface ElongChangeDao {


	/**
	 * 根据改签特征值查询改签信息
	 * @param reqtoken
	 * @return
	 */
	public ElongChangeInfo selectChangeInfoByReqtoken(String reqtoken);
	/**
	 * 改签车票信息
	 * @param 
	 * @return
	 */
	public List<ElongChangePassengerInfo> selectChangeCp(Map <String,Object>map);
	/**
	 * 增加改签信息
	 * @param changeInfo
	 * @return
	 */
	public int insertChangeInfo(ElongChangeInfo changeInfo);
	/**
	 * 增加改签用户信息
	 * @param passenger
	 * @return
	 */
	public void insertChangeCp(ElongChangePassengerInfo passenger); 
	/**
	 * 增加改签日志
	 * @param log
	 * @return
	 */
	public void insertChangeLog(ElongChangeLogVO log);
	/**
	 * 更新改签信息
	 * @param changeInfo
	 * @return
	 */
	public int updateChangeInfo(ElongChangeInfo changeInfo);
	/**
	 * 更新改签车票信息
	 * @param changePassengerInfo
	 * @return
	 */
	public int updateChangeCp(ElongChangePassengerInfo changePassengerInfo);
	
	
	/*********回调********/
	/**
	 * 查询待通知的改签信息
	 */
	public List<ElongChangeInfo> selectNoticeChangeInfo(String merchantId);
	/**
	 * 根据订单号查询车票id集合
	 * @param orderId
	 * @return
	 */
	public List<String> getCpListByOrderId(String orderId);
	/**
	 * 查询新票的改签车票信息
	 * @param param
	 * @return
	 */
	public Map<String,Object> queryChangeCpInfo(Map<String,Object> param);
	
	/**
	 * 根据改签的车次，查询改签到达站的时间
	 * @param map
	 * @return
	 */
	public Map<String, String> querySinfo(Map<String, String> map);



}
