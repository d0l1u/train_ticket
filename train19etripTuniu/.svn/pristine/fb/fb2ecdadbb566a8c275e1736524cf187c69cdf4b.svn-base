package com.l9e.transaction.service;

import java.util.List;

import com.l9e.transaction.exception.TuniuCommonException;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuChangeInfo;
import com.l9e.transaction.vo.model.Parameter;
import com.l9e.transaction.vo.model.Result;


/**
 * 途牛改签接口
 * @author caona
 *
 */
public interface TuniuChangeService {
	String APPLY_CHAGNE_ACCOUNT_ERROR="1600000";//传参账号有误
	String APPLY_CHANGE_ERROR="1600101";//请求改签失败
	String APPLY_CHANGE_CANCEL_TOMUCH_ERROR = "1600102";//取消改签次数超过上限，无法继续操作
	String APPLY_CHANGE_TIME_ERROR = "1600103";//距离开车时间太近无法改签
	String CHANGE_NOT_EXIST_ERROR = "1600105";//未找到要改签的车票
	String CHANGE_LOGIN_ERROPR="1600106";//登陆失败
	String APPLY_CHANGE_CHANGED_ERROR = "1600108";//已改签，不能再次改签
	String CHANGE_STATUS_ERROR = "1600111";//状态不正确无法改签
	String APPLY_CHANGE_REPEAT_ERROR = "1600112";//重复提交的改签信息
	String APPLY_CHANGE_SEAT_TYPE_ERROR = "1600113";//批量改签新票和原车票座位都不能为卧铺
	String CANCEL_CHANGE_ERROR="1600301"; //取消改签失败
	String CANCEL_CHANGE_ALREADYCONFIRM_ERROR="1600302"; //订单已确认改签，不能取消改签
	String CANCEL_CHAGNE_NOT_EXIST_ERROR="1600303";//找不到要取消改签的订单
	String CANCEL_CHANGE_TIMEOUT_ERROR="1600304"; //取消改签超时
	String CONFIRM_CHANGE_ERROR = "1600201";//确认改签失败
	String CONFIRM_CHANGE_TIMEOUT_ERROR = "1600202";//确认改签时间已超时，确认改签失败
	String CONFIRM_CHANGE_NOTEXIST_ERROR = "1600203";//确认改签未找到订单
	String CANCEL_CHANGE_NOTEXIST_ERROR = "1600303";//取消改签未找到订单
	
	/*******************改签状态***********************************/
	public static final String TRAIN_REQUEST_CHANGE = "11";//改签预订
	public static final String TRAIN_REQUEST_CHANGE_ING = "12";//正在改签预订
	public static final String TRAIN_REQUEST_CHANGE_ARTIFICIAL = "13";// 人工改签预订
	public static final String TRAIN_REQUEST_CHANGE_SUCCESS = "14";//改签成功等待确认
	public static final String TRAIN_REQUEST_CHANGE_FAIL = "15";//改签预订失败
	public static final String TRAIN_CANCEL_CHANGE = "21";//改签取消
	public static final String TRAIN_CANCEL_CHANGE_ING = "22";// 正在取消
	public static final String TRAIN_CANCEL_CHANGE_SUCCESS = "23";//取消成功
	public static final String TRAIN_CANCEL_CHANGE_FAIL = "24";//取消失败
	public static final String TRAIN_CONFIRM_CHANGE = "31";//开始支付
	public static final String TRAIN_CONFIRM_CHANGE_PAY = "32";//正在支付
	public static final String TRAIN_CONFIRM_CHANGE_PAY_ARTIFICIAL = "33";//人工支付
	public static final String TRAIN_CONFIRM_CHANGE_SUCCESS = "34";//支付成功
	public static final String TRAIN_CONFIRM_CHANGE_FAIL = "35";//支付失败
	public static final String TRAIN_CONFIRM_CHANGE_START_PAY = "36";//补价支付
	
	/***************************改签回调状态*******************************/
	public static final String CHANGE_NOTIFY_PRE = "000";//准备异步回调
	public static final String CHANGE_NOTIFY_START = "111";//开始异步回调
	public static final String CHANGE_NOTIFY_OVER = "222";//回调完成
	public static final String CHANGE_NOTIFY_FAIL = "333";//回调失败
	/**
	 * 请求改签
	 * @param parameter
	 * @return
	 */
	Result addChange(Parameter parameter) throws TuniuCommonException;
	/**
	 * 取消改签
	 * @param parameter
	 * @return
	 */
	Result cancelChange(Parameter parameter) throws TuniuCommonException;
	/**
	 * 确认改签
	 * @param parameter
	 * @return
	 */
	Result confirmChange(Parameter parameter) throws TuniuCommonException;
	/**
	 * 获取待通知的改签信息
	 * @param merchantId
	 * @return
	 */
	List<TuniuChangeInfo> getNoticeChangeInfo(String merchantId);
	/**
	 * 通知改签
	 * @param changeInfos
	 */
	void callbackChangeNotice(List<TuniuChangeInfo> notifyList);
	/**
	 * 更新通知信息
	 * @param notice
	 */
	void updateNotice(Notice notice);
	
	/*---------------------------dqh2015-11-23---------------------------------------------------*/
	/**
	 * 请求改签验证码
	 * @param parameter
	 * @return
	 */
	Result addChangeCaptcha(Parameter parameter,String captchaResultUrl,String robotUrl) throws TuniuCommonException;
	
}
