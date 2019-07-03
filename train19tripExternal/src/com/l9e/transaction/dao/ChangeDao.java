package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLogVO;
import com.l9e.transaction.vo.ChangePassengerInfo;

public interface ChangeDao {
	/**
	 * 根据改签特征值查询改签信息
	 * @param reqtoken
	 * @return
	 */
	public ChangeInfo selectChangeInfoByReqtoken(String reqtoken);
	/**
	 * 改签车票信息
	 * @param 
	 * @return
	 */
	public List<ChangePassengerInfo> selectChangeCp(Map <String,Object>map);
	/**
	 * 增加改签信息
	 * @param changeInfo
	 * @return
	 */
	public int insertChangeInfo(ChangeInfo changeInfo);
	/**
	 * 增加改签用户信息
	 * @param passenger
	 * @return
	 */
	public void insertChangeCp(ChangePassengerInfo passenger); 
	/**
	 * 增加改签日志
	 * @param log
	 * @return
	 */
	public void insertChangeLog(ChangeLogVO log);
	/**
	 * 更新改签信息
	 * @param changeInfo
	 * @return
	 */
	public int updateChangeInfo(ChangeInfo changeInfo);
	/**
	 * 更新改签车票信息
	 * @param changePassengerInfo
	 * @return
	 */
	public int updateChangeCp(ChangePassengerInfo changePassengerInfo);
	
	
	/*********回调********/
	/**
	 * 查询待通知的改签信息
	 */
	public List<ChangeInfo> selectNoticeChangeInfo();
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
	 * 查询该改签订单，的回调状态是否成功，以记录数来判别
	 * @param order_id
	 * @return
	 */
	public int selectNoCompleteChangeOrderLastOneCount(String order_id);


}
