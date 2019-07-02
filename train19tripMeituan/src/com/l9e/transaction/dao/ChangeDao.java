package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLog;
import com.l9e.transaction.vo.ChangePassengerInfo;

public interface ChangeDao {
	/**
	 * 插入改签信息
	 * @param change
	 * @return
	 */
	public int insertChangeInfo(ChangeInfo change);
	/**
	 * 插入改签乘客信息
	 * @param passenger
	 * @return
	 */
	public void insertChangeCp(ChangePassengerInfo passenger);
	/**
	 * 增加改签日志
	 * @return
	 */
	public void addChangeLog(ChangeLog log);
	/**
	 * 修改改签信息
	 * @param change
	 * @return
	 */
	public int updateChangeInfo(ChangeInfo change);
	/**
	 * 修改改签车票信息
	 * @param passenger
	 * @return
	 */
	public int updateChangeCp(ChangePassengerInfo passenger);
	/**
	 * 查询改签信息
	 * @param param
	 * @return
	 */
	public ChangeInfo selectChangeInfo(Map<String, Object> param);
	/**
	 * 获取待通知的改签信息
	 * @param merchantId
	 * @return
	 */
	List<ChangeInfo> selectNoticeChangeInfo(String merchantId);
	/**
	 * 查询改签乘客信息
	 * @param param
	 * @return
	 */
	public List<ChangePassengerInfo> selectChangeCp(Map<String,Object> param);
	/**
	 * 查询改签车票信息
	 * @param param
	 * @return
	 */
	Map<String,Object> queryChangeCpInfo(Map<String,Object> param);
}
