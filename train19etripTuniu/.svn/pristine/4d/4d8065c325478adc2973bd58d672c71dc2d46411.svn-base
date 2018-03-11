package com.l9e.transaction.service;

import java.util.List;

import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuQueueOrder;

/**
 * 途牛通知业务接口
 * @author licheng
 *
 */
public interface TuniuNoticeService {

	/**
	 * 查询指定数量的预订通知记录，(出票系统)
	 * @param service
	 * @return
	 */
	List<Notice> findBookWaitOutTicketNotifies(int amount);
	/**
	 * 查询指定数量的预订结果回调通知记录
	 * @param amount
	 * @return
	 */
	List<Notice> findBookWaitCallbackNotifies(int amount);
	/**
	 * 查询指定数量的出票通知记录，(出票系统)
	 * @param service
	 * @return
	 */
	List<Notice> findOutWaitOutTicketNotifies(int amount);
	/**
	 * 查询指定数量的预订结果回调通知记录
	 * @param amount
	 * @return
	 */
	List<Notice> findOutWaitCallbackNotifies(int amount);
	/**
	 * 查询指定数量的退款通知记录，(出票系统)
	 * @param service
	 * @return
	 */
	List<Notice> findRefundWaitOutTicketNotifies(int amount);
	/**
	 * 查询指定数量的预订结果回调通知记录
	 * @param amount
	 * @return
	 */
	List<Notice> findRefundWaitCallbackNotifies(int amount);
	/**
	 * 更新预订通知信息
	 * @param notice
	 */
	void updateBookNotice(Notice notice);
	/**
	 * 更新预出票知信息
	 * @param notice
	 */
	void updateOutNotice(Notice notice);
	/**
	 * 更新退款通知信息
	 * @param notice
	 */
	void updateRefundNotice(Notice notice);
	/**
	 * 通过订单号查询预订通知
	 * @param orderId
	 * @return
	 */
	Notice getBookNoticeByOrderId(String orderId);
	/**
	 * 通过订单号查询出票通知
	 * @param orderId
	 * @return
	 */
	Notice getOutNoticeByOrderId(String orderId);
	/**
	 * 通过订单号和车票号查询退票通知
	 * @param orderId
	 * @param cpId
	 * @return
	 */
	Notice getRefundNoticeByOrderIdAndCpId(String orderId, String cpId);
	List <TuniuQueueOrder> getQueueOrder();
	void updateQueueNotice(TuniuQueueOrder queueOrder);
	
}
