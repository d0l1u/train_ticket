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
	public ChangePassengerInfo getChangeCpById(String cp_id); 
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
	
	/*********回调********/
	/**
	 * 查询待通知的改签信息
	 */
	public List<ChangeInfo> getNoticeChangeInfo();
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
	public List<String> getNewCpListByParam(Map<String, Object> param);
	/**
	 * 查询新票的改签车票信息
	 * @param param
	 * @return
	 */
	public Map<String,Object> queryChangeCpInfo(Map<String,Object> param);
	
	/**
	 * 查询当前最新改签订单的是否回调成功.
	 * @param order_id
	 * @return
	 */
	public int selectNoCompleteChangeOrderLastOneCount(String order_id);
}
