package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.DBChangeInfo;
import com.l9e.transaction.vo.DBPassengerChangeInfo;
import com.l9e.transaction.vo.TongchengChangeLogVO;

/**
 * 同城同步、异步改签service
 * @author licheng
 *
 */
public interface TongChengChangeService {
	
	/**
	 * 准备异步回调
	 */
	String CHANGE_NOTIFY_PRE = "000";
	/**
	 * 开始异步回调
	 */
	String CHANGE_NOTIFY_START = "111";
	/**
	 * 回调完成
	 */
	String CHANGE_NOTIFY_OVER = "222";
	/**
	 * 回调失败
	 */
	String CHANGE_NOTIFY_FAIL = "333";
	/*改签状态*/
	/**
	 * 改签预订
	 */
	String TRAIN_REQUEST_CHANGE = "11";
	/**
	 * 正在改签预订
	 */
	String TRAIN_REQUEST_CHANGE_ING = "12";
	/**
	 * 人工改签预订
	 */
	String TRAIN_REQUEST_CHANGE_ARTIFICIAL = "13";
	/**
	 * 改签成功等待确认
	 */
	String TRAIN_REQUEST_CHANGE_SUCCESS = "14";
	/**
	 * 改签预订失败
	 */
	String TRAIN_REQUEST_CHANGE_FAIL = "15";
	/**
	 * 改签取消
	 */
	String TRAIN_CANCEL_CHANGE = "21";
	/**
	 * 正在取消
	 */
	String TRAIN_CANCEL_CHANGE_ING = "22";
	/**
	 * 取消成功
	 */
	String TRAIN_CANCEL_CHANGE_SUCCESS = "23";
	/**
	 * 取消失败
	 */
	String TRAIN_CANCEL_CHANGE_FAIL = "24";
	/**
	 * 开始支付
	 */
	String TRAIN_CONFIRM_CHANGE = "31";
	/**
	 * 正在支付
	 */
	String TRAIN_CONFIRM_CHANGE_PAY = "32";
	/**
	 * 人工支付
	 */
	String TRAIN_CONFIRM_CHANGE_PAY_ARTIFICIAL = "33";
	/**
	 * 支付成功
	 */
	String TRAIN_CONFIRM_CHANGE_SUCCESS = "34";
	/**
	 * 支付失败
	 */
	String TRAIN_CONFIRM_CHANGE_FAIL = "35";
	/**
	 * 补价支付
	 */
	String TRAIN_CONFIRM_CHANGE_START_PAY = "36";
	
	/*异步改签通知*/
	
	/**
	 * 获取需要通知的改签操作
	 */
	List<DBChangeInfo> getNotifyList();
	/**
	 * 回调前更新状态为开始状态
	 * @param notifyList
	 */
	int updateNotifyBegin(DBChangeInfo changeInfo);
	/**
	 * 回调通知异步改签信息
	 * @param changeInfo
	 */
	void notifyChange(Integer change_id);
	
	/*改签业务*/
	/**
	 * 添加改签信息
	 */
	void addChangeInfo(DBChangeInfo changeInfo, boolean cascade);
	void updateChangeInfo(DBChangeInfo changeInfo, boolean cascade);
	DBChangeInfo getChangeInfo(Map<String, Object> param, boolean cascade);
	
	List<DBPassengerChangeInfo> findRequestChangeCp(Map<String, Object> param);
	void updateChangeCp(List<DBPassengerChangeInfo> cPassengers);
	DBPassengerChangeInfo getChangeCp(String cp_id);
	String getAccountId(String order_id);
	List<String> getChangeReqtokens(String orderId);
	
	void changeManual4Timeout();
//	/**
//	 * 拉取改签操作结果
//	 * @param param 查询参数(反复查询,直到得到结果或者超时)
//	 * @param timeout 超时毫秒值，null表示无超时限制
//	 * @return
//	 */
//	List<DBPassengerChangeInfo> getChangeResult(Map<String, Object> param, Long timeout);
	/*日志*/
	void addTongChengChangeLog(TongchengChangeLogVO log);
}
