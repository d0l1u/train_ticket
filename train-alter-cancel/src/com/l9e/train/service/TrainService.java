package com.l9e.train.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.l9e.train.po.Account;
import com.l9e.train.po.AccountLog;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public interface TrainService {

	Worker getWorker();

	Account getAccount();

	/**
	 * 获取所有需要通知的列表
	 * @return
	 */
	List<Order> getOrderbill();

	/**
	 * 查询需要通知的数据，并且进行更新
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int orderbillByList(int getNum) throws RepeatException, DatabaseException;

	/**
	 * 需要重新发送订单的处理
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int orderIsResend(Order order, Result result) throws RepeatException,
			DatabaseException;

	/**
	 * 超时订单人工处理
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int orderIsManual(Order order, Result result) throws RepeatException,
			DatabaseException;

	/**
	 * 取消成功后订单的处理
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int orderIsSuccess(Order order, Result result) throws RepeatException,
			DatabaseException;

	/**
	 * 获取账号
	 * @param accountId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int findAccountAndWorker(Order order) throws RepeatException,
			DatabaseException;

	/**
	 * 插入历史记录
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int insertHistory(String orderId, Integer changeId, String optlog) throws RepeatException,
			DatabaseException;

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
	 * 账号相关取消 退票 改签 动作记录
	 * @param log
	 */
	int insertAccountHistory(AccountLog log) throws RepeatException,
			DatabaseException;

	/**
	 * 取消失败
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	int orderIsFail(Order order, Result result) throws RepeatException,
			DatabaseException;

}
