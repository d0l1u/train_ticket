package com.l9e.train.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public interface TrainService {

	PayCard getPayCard();

	Worker getWorker();

	Account getAccount();

	/**
	 * 获取所有需要通知的列表
	 */
	List<Order> getOrderbill();

	/**
	 * 查询需要通知的数据，并且进行更新
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int orderbillByList(int getNum, int time) throws RepeatException,
			DatabaseException;

	/**
	 * 需要重新发送订单的处理
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int payIsResend(Order order) throws RepeatException, DatabaseException;

	/**
	 * 超时订单人工处理
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int payIsManual(Order order) throws RepeatException, DatabaseException;

	/**
	 * 发送成功后订单的处理
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int payIsSuccess(Order order, Result result) throws RepeatException,
			DatabaseException;

	/**
	 * 给状态为“支付开始”的订单匹配处理人和账号信息
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int selectPayAccountAndWorkerBy(Order order) throws RepeatException,
			DatabaseException;

	/**
	 * 插入历史记录
	 * @param orderId
	 * @param optlog
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int insertHistory(String orderId, String optlog) throws RepeatException,
			DatabaseException;

	int freeCard(String cardId) throws RepeatException, DatabaseException;

	/**
	 * 添加账号日志
	 * @param cn
	 * @param cs
	 * @param acc_name
	 * @param orderId
	 * @param opt_logs
	 */
	void addAccLog(Connection cn, PreparedStatement cs, String accName,
			String orderId, String optLogs);

	/**
	 * 获得支付订单数量
	 * 
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int queryProductNum() throws RepeatException, DatabaseException;

	/**
	 * 获得帐号下单次数
	 * 
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int queryProductBookNum() throws RepeatException, DatabaseException;

	/**
	 * 获得支付订单数量
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int queryProductTime() throws RepeatException, DatabaseException;

	/**
	 * 获得新支付等侯时间
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int queryProductNewTime() throws RepeatException, DatabaseException;

}
