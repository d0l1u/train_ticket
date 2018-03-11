package com.l9e.transaction.service;

import com.l9e.transaction.exception.TuniuRefundException;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.SynchronousOutput;
import com.l9e.transaction.vo.TuniuRefund;
import com.l9e.transaction.vo.model.Parameter;
import com.l9e.transaction.vo.model.Result;

/**
 * 途牛退票业务接口
 * @author licheng
 *
 */
public interface TuniuRefundService {
	
	/*退款类型*/
	/**
	 * 线上退款
	 */
	String REFUND_TYPE_ONLINE = "11";
	/**
	 * 线下退款
	 */
	String REFUND_TYPE_OFFLINE = "22";
	/**
	 * 车站退款(线下退款)
	 */
	String REFUND_TYPE_STATION = "33";
	/**
	 * 改签退款
	 */
	String REFUND_TYPE_CHANGE = "55";
	
	/*退款状态*/
	/**
	 * 准备机器改签
	 */
	String STATUS_PRE_ROBOT_ALTER = "00";
	/**
	 * 开始机器改签
	 */
	String STATUS_START_ROBOT_ALTER = "02";
	/**
	 * 退款完成
	 */
	String STATUS_REFUND_SUCCESS = "11";
	/**
	 * 退款失败
	 */
	String STATUS_REFUND_FAILURE = "22";
	
	//催退款状态
	/**
	 * 催退款 初始状态-退款处理中
	 */
	String STATUS_URGE_ING="11";
	/**
	 * 催退款 初始状态-退款成功
	 */
	String STATUS_URGE_SUCCESS="22";
	/**
	 * 催退款 初始状态-退款失败
	 */
	String STATUS_URGE_FAIL="33";
	/**
	 * 催退款 初始状态-其他
	 */
	String STATUS_URGE_OTHER="44";
	
	/*返回码*/
	/**
	 * 车票不存在或车票状态异常
	 */
	//String RETURN_CODE_TICKET_NOT_EXISTS_OR_STATUS_ERROR = "606";
	 
	/**
	 * 退票传参账号有误
	 */
	//String RETURN_CODE_ACCOUNT_ERROR="609";
	/**
	 * 退款失败
	 */
	String RETURN_CODE_REFUND_FAILURE = "140002";
	/**
	 * 已取纸质票，不能退票
	 */
	String RETURN_CODE_REFUND_QUPIAO_FAILURE = "140003";
	/**
	 * 退款成功
	 */
	String RETURN_CODE_REFUND_SUCCESS = "140001";
	/**
	 * 已退款成功
	 */
	String RETURN_CODE_REFUND_ALREADY_REFUND ="140004";
	/**
	 * 退款记录已存在
	 */
	String RETURN_CODE_REFUND_REFUNDING ="140005";
	/**
	 * 催退款失败
	 */
	String RETURN_CODE_REFUND_NOTICE_FAILURE="2001";

	/**
	 * 途牛线上退票
	 * @param parameter
	 * @return
	 * @throws TuniuRefundException
	 */
	Result trainRefund(Parameter parameter);
	/**
	 * 退款记录入库
	 * @param refund
	 */
	void addRefund(TuniuRefund refund);
	/**
	 * 通知记录入库
	 * @param notice
	 */
	void addNotice(Notice notice);
	/**
	 * 向出票系统发送退款通知
	 * @param order
	 * @param notice
	 */
	void sendRefundOrder(TuniuRefund refund, Notice notice);
	/**
	 * 向途牛异步回调退款结果
	 * @param order
	 * @param notice
	 */
	void callbackRefundOrder(TuniuRefund refund, Notice notice);
	/**
	 * 根据退款记录主键获取退款信息
	 * @param refundId
	 * @return
	 */
	TuniuRefund getRefundById(Integer refundId);
	/**
	 * 根据订单号和车票号获取退票记录
	 * @param orderId
	 * @param cpId
	 * @return
	 */
	TuniuRefund getRefundByOrderIdAndCpId(String orderId, String cpId);
	/**
	 * 更新退款信息
	 * @param refund
	 */
	void updateRefund(TuniuRefund refund);
	/**
	 * 催退款通知
	 * @param parameter
	 * @return
	 */
	void queryRefundNotice(Parameter parameter,SynchronousOutput synOutput);
}
