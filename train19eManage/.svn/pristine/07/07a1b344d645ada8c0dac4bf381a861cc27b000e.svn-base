package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.TuniuUrgeRefund;

public interface TuniuUrgeRefundDao {
	/**
	 * 查询催退款数量
	 * @param param
	 * @return
	 */
	public int queryUrgeRefundCount(Map<String,Object> param);
	/**
	 * 查询催退款信息
	 * @param param
	 * @return
	 */
	public List<TuniuUrgeRefund> queryUrgeRefundList(Map<String,Object> param);
	/**
	 * 查询催退款详情
	 * @param param
	 * @return
	 */
	public TuniuUrgeRefund queryUrgeRefundInfo(Map<String,Object> param);
	/**
	 * 更新催退款信息
	 * @param param
	 * @return
	 */
	public int updateUrgeRefundInfo(Map<String,Object> param);
	
	public int updateUrgeRefundByOrderId(Map<String, Object> param);
	
	public List<TuniuUrgeRefund> queryUrgeRefundListForExcel(Map<String,Object> param);
	
	public int updateUrgeRefund(TuniuUrgeRefund tuniuUrgeRefund);
}
