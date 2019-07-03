package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuQueueOrder;

/**
 * 途牛通知持久接口
 * @author licheng
 *
 */
public interface TuniuNoticeDao {

	/**
	 * 预订通知记录入库
	 * @param notice
	 */
	void insertBookNotice(Notice notice);
	
	/**
	 * 预订通知记录更新
	 * @param notice
	 */
	void updateBookNotice(Notice notice);
	
	/**
	 * 查询预订通知(出票系统待通知)
	 * @param queryParam
	 * @return
	 */
	List<Notice> selectBookPreparedNotices(Map<String, Object> queryParam);
	
	/**
	 * 查询待回调预订结果的通知
	 * @param queryParam
	 * @return
	 */
	List<Notice> selectBookCallbackNotices(Map<String, Object> queryParam);
	
	/**
	 * 查询一个预定通知
	 * @param queryParam
	 * @return
	 */
	Notice selectBookOneNotice(Map<String, Object> queryParam);
	
	/**
	 * 出票通知记录入库
	 * @param notice
	 */
	void insertOutNotice(Notice notice);
	
	/**
	 * 出票通知记录更新
	 * @param notice
	 */
	void updateOutNotice(Notice notice);
	
	/**
	 * 查询出票通知(出票系统待通知)
	 * @param queryParam
	 * @return
	 */
	List<Notice> selectOutPreparedNotices(Map<String, Object> queryParam);
	
	/**
	 * 查询待回调出票或取消结果的通知
	 * @param queryParam
	 * @return
	 */
	List<Notice> selectOutCallbackNotices(Map<String, Object> queryParam);
	
	/**
	 * 查询一个出票通知
	 * @param queryParam
	 * @return
	 */
	Notice selectOutOneNotice(Map<String, Object> queryParam);
	
	/**
	 * 退票通知记录入库
	 * @param notice
	 */
	void insertRefundNotice(Notice notice);
	
	/**
	 * 退票通知记录更新
	 * @param notice
	 */
	void updateRefundNotice(Notice notice);
	
	/**
	 * 查询退款通知(出票系统待通知)
	 * @param queryParam
	 * @return
	 */
	List<Notice> selectRefundPreparedNotices(Map<String, Object> queryParam);
	
	/**
	 * 查询待回调退款结果的通知
	 * @param queryParam
	 * @return
	 */
	List<Notice> selectRefundCallbackNotices(Map<String, Object> queryParam);
	
	/**
	 * 查询一个退款通知
	 * @param queryParam
	 * @return
	 */
	Notice selectRefundOneNotice(Map<String, Object> queryParam);

	void deleteNotice(Notice notice);
	
	List <TuniuQueueOrder> getQueueOrder();
	void updateQueueNotice(TuniuQueueOrder queueOrder);
}
