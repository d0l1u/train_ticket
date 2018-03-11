package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.TuniuRefund;
import com.l9e.transaction.vo.TuniuUrgeRefund;

/**
 * 途牛退票持久接口
 * @author licheng
 *
 */
public interface TuniuRefundDao {

	/**
	 * 添加退款记录
	 * @param refund
	 */
	void insertRefund(TuniuRefund refund);
	
	/**
	 * 查询退款记录
	 * @param queryParam
	 * @return
	 */
	TuniuRefund selectOneRefund(Map<String, Object> queryParam);
	
	/**
	 * 更新退款记录
	 * @param refund
	 */
	void updateRefund(TuniuRefund refund);
	/**
	 * 查询账号
	 * @param queryParam
	 * @return
	 */
	Account selectOneAccount(Map<String, Object> queryParam);

	void deleteRefund(TuniuRefund refund);
	/**
	 * 插入催退款信息
	 * @param urgeRefundInfo
	 */
	void insertUrgeRefund(TuniuUrgeRefund urgeRefundInfo);
	/**
	 * 查询催退款信息
	 * @param param
	 * @return
	 */
	TuniuUrgeRefund getUrgeRefundInfo(Map<String,Object> param);
	/**
	 * 更新催退款信息
	 * @param urgeRefundInfo
	 * @return
	 */
	int updateUrgeRefund(TuniuUrgeRefund urgeRefundInfo);
	/**
	 * 查询退款记录
	 * @param queryParam
	 * @return
	 */
	List<Integer> checkOfflineNotify(Map<String, Object> queryParam);
	
}
