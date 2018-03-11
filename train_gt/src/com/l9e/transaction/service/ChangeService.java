package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLogVO;
import com.l9e.transaction.vo.ChangePassengerInfo;

public interface ChangeService {
	/**
	 * 根据改签特征值查询改签信息
	 * @param reqtoken
	 * @return
	 */
	public ChangeInfo getChangeInfoByReqtoken(String reqtoken);
	/**
	 * 根据车票id 查询改签车票信息
	 * @param cp_id
	 * @return
	 */
	public List<ChangePassengerInfo> getChangeCpById(String cp_id); 
	/**
	 * 增加改签信息
	 * @param changeInfo
	 * @return
	 */
	public int addChangeInfo(ChangeInfo changeInfo);
	/**
	 * 增加改签日志
	 * @param log
	 * @return
	 */
	public void addChangeLog(ChangeLogVO log);
	/**
	 * 查询改签乘客信息
	 * @param param
	 * @return
	 */
	public List<ChangePassengerInfo> getChangePassenger(Map<String,Object> param);
	
	/*********回调********/
	/**
	 * 查询高铁管家待通知的改签信息
	 */
	public List<ChangeInfo> getNoticeChangeInfo(String merchantId);
	/**
	 * 回调改签信息
	 * @param notifyList
	 */
	public void changeNotice(List<ChangeInfo> notifyList);
	/**
	 * 更新改签信息
	 * @param changeInfo
	 * @return
	 */
	public int updateChangeInfo(ChangeInfo changeInfo);
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
	 * 通过途径站，查询时间
	 * @param changeInfo
	 */
	public String queryTimeNew(ChangeInfo changeInfo);
	
}
