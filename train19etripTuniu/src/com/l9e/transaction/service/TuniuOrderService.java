package com.l9e.transaction.service;

import com.l9e.transaction.exception.TuniuCommonException;
import com.l9e.transaction.exception.TuniuOrderException;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.SynchronousOutput;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.transaction.vo.TuniuPassenger;
import com.l9e.transaction.vo.TuniuQueueOrder;
import com.l9e.transaction.vo.model.Parameter;
import com.l9e.transaction.vo.model.Result;

/**
 * 途牛出票业务接口
 * @author licheng
 *
 */
public interface TuniuOrderService {
	
	/*订单类型*/
	/**
	 * 先预定后支付
	 */
	String TYPE_BOOK_OUT = "1";
	/**
	 * 先支付后预订
	 */
	String TYPE_OUT_BOOK = "2";
	
	/*订单状态*/
	/**
	 * 下单成功
	 */
	String STATUS_PLACE_ORDER_SUCCESS = "00";
	
	/**
	 * 通知出票成功
	 */
	String STATUS_NOTIFY_SUCCESS = "11";
	
	/**
	 * 预订成功
	 */
	String STATUS_BOOK_SUCCESS = "22";
	
	/**
	 * 支付中
	 */
	String STATUS_WAIT_PAY = "32";
	
	/**
	 * 出票成功
	 */
	String STATUS_OUT_SUCCESS = "33";
	
	/**
	 * 出票失败
	 */
	String STATUS_OUT_FAILURE = "44";
	
	/**
	 * 超时订单
	 */
	String STATUS_TIME_OUT = "88";
	
	/**
	 * 正在取消
	 */
	String STATUS_CANCEL_ING = "23";
	
	/**
	 * 取消成功
	 */
	String STATUS_CANCEL_SUCCESS = "24";
	/**
	 * 取消失败
	 */
	String STATUS_CANCEL_FAILURE = "25";
	
	/*途牛车票类型*/
	/**
	 * 途牛学生票
	 */
	String PIAO_TYPE_STUDENT = "3";
	
	/*异常码*/
	/**
	 * 没有余票
	 */
	String RETURN_CODE_NO_REMAINDER_TICKETS = "110000";
	/**
	 * 乘客信息异常
	 */
	String RETURN_CODE_PASSENGER_INFO_ERROR = "110001";
	/**
	 * 乘客已经办理其他订单
	 */
	String RETURN_CODE_PASSENGER_ORDER_OTHER = "110002";
	/**
	 * 乘客身份信息未通过
	 */
	String RETURN_CODE_PASSENGER_NOT_PASS_VERIFY = "110003";
	/**
	 * 重复订单
	 */
	String RETURN_CODE_REPEAT_ORDER = "110016";
	/**
	 * 系统错误
	 */
	String RETURN_CODE_SYSTEM_ERROR = "110024";
	/**
	 * 订单超时未支付
	 */
	String RETURN_CODE_ORDER_TIME_OUT = "120002";
	/**
	 * 订单已取消不可确认出票
	 */
	String RETURN_CODE_ORDER_CANCELD="120005";
	
	/**
	 * 出票失败
	 */
	String RETURN_CODE_CONFIRM_FAIL = "120000";

	/**
	 * 出票成功
	 */
	String RETURN_CODE_CONFIRM_ORDER_SUCCESS = "120001";
	/**
	 * 出票异常
	 */
	String RETURN_CODE_CONFIRE_SYS_ERROR="120008";
	
	/**
	 * 已出票，不能取消
	 */
	String RETURN_CODE_TICKET_OUT = "130000";
	/**
	 * 取消失败
	 */
	String RETURN_CODE_CANCEL_FAIL = "130001";
	/**
	 * 取消成功
	 */
	String RETURN_CODE_CANCEL_SUCCESS = "130002";
	/**
	 * 此状态不可取消
	 */
	String RETURN_CODE_CANCEL_ILLEGAL_STATUS="130005";
	/**
	 * 正在取消中
	 */
	String RETURN_CODE_CANCEL_ORDER_ING = "130004";
	/**
	 *已取消不能取消
	 */
	String RETURN_CODE_CANCEL_ORDER_ALREADY_CANCEL = "130003";
	/**
	 * 取消系统异常
	 */
	String 	RETURN_CODE_CANCEL_SYS_ERROR="130010";
	
	
	/**
	 * 查询订单状态失败
	 */
	String RETURN_CODE_SEARCHSTATUS_ERROR = "200001";   	
	/**
	 * 查询订单状态，订单不存在
	 */
	String RETURN_CODE_SEARCHSTATUS_ORDERNOTEXIST = "200002";

	/**
	 * 预订下单
	 * @param parameter
	 * @param synOutput
	 */
	Result trainBook(Parameter parameter);
	/**
	 * 确认出票
	 * @param parameter
	 * @return
	 * @throws TuniuOrderException
	 */
	Result trainConfirm(Parameter parameter);
	/**
	 * 取消订单
	 * @param parameter
	 * @return
	 * @throws TuniuOrderException
	 */
	Result trainCancel(Parameter parameter);
	/**
	 * 订单入库
	 * @param order
	 */
	void addOrder(TuniuOrder order);
	/**
	 * 通知记录入库
	 * @param notice
	 */
	void addNotice(Notice notice, String service);
	/**
	 * 向出票系统异步发送占座通知
	 * @param order
	 */
	void sendBookOrder(TuniuOrder order, Notice notice);
	/**
	 * 向途牛异步回调占座结果
	 * @param order
	 * @param notice
	 */
	void callbackBookOrder(TuniuOrder order, Notice notice);
	/**
	 * 向出票系统异步发送出票通知(支付)
	 * @param order
	 * @param notice
	 */
	void sendOutOrder(TuniuOrder order, Notice notice);
	/**
	 * 向途牛异步回调出票结果
	 * @param order
	 * @param notice
	 */
	void callbackOutOrder(TuniuOrder order, Notice notice);
	/**
	 * 向出票系统异步发送取消订单通知
	 * @param order
	 * @param notice
	 */
	void sendCancelOrder(TuniuOrder order, Notice notice);
	/**
	 * 查询订单
	 * @param orderId
	 * @return
	 */
	TuniuOrder getOrderById(String orderId, boolean cascade);
	/**
	 * 根据车票号获取乘客车票信息
	 * @param cpId
	 * @return
	 */
	TuniuPassenger getPassengerById(String cpId);
	/**
	 * 更新途牛订单
	 * @param order
	 * @param cascade
	 */
	void updateOrder(TuniuOrder order, boolean cascade);
	/**
	 * 获取订单信息中的车票状态信息
	 * @param parameter
	 * @param synOutput
	 */
	Result searchStatusFrom12306(Parameter parameter);
	/**
	 * 回调排队订单信息
	 * @param notice
	 */
	void  callbackQueueOrder(TuniuQueueOrder notice);
	
	
	/*---------------------------dqh2015-11-23---------------------------------------------------*/
	/**
	 * 途牛获取验证码时的下单
	 * @param parameter 
	 */
	Result captchaTrainBook(Parameter parameter,String accountUrl,String captchaResultUrl,String robotUrl,int num);
	
	
	/**
	 * 途牛推送超时订单
	 * @param parameter
	 * @param synOutput
	 */
	void tuniuPushOrderStatus(Parameter parameter, SynchronousOutput synOutput) throws TuniuCommonException;
	
}
