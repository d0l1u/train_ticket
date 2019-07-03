package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.TuniuChangeInfo;
import com.l9e.transaction.vo.TuniuChangePassengerInfo;
import com.l9e.transaction.vo.TuniuChangeLogVO;


public interface TuniuChangeDao {
	/**
	 * 插入改签信息
	 * @param change
	 * @return
	 */
	public int insertChangeInfo(TuniuChangeInfo change);
	/**
	 * 插入改签乘客信息
	 * @param passenger
	 * @return
	 */
	public void insertChangeCp(TuniuChangePassengerInfo passenger);
	/**
	 * 增加改签日志
	 * @return
	 */
	public void addChangeLog(TuniuChangeLogVO log);
	/**
	 * 修改改签信息
	 * @param change
	 * @return
	 */
	public int updateChangeInfo(TuniuChangeInfo change);
	/**
	 * 修改改签车票信息
	 * @param passenger
	 * @return
	 */
	public int updateChangeCp(TuniuChangePassengerInfo passenger);
	/**
	 * 查询改签信息
	 * @param param
	 * @return
	 */
	public TuniuChangeInfo selectChangeInfo(Map<String, Object> param);
	/**
	 * 获取待通知的改签信息
	 * @param merchantId
	 * @return
	 */
	List<TuniuChangeInfo> selectNoticeChangeInfo(String merchantId);
	/**
	 * 查询改签乘客信息
	 * @param param
	 * @return
	 */
	public List<TuniuChangePassengerInfo> selectChangeCp(Map<String,Object> param);

}
