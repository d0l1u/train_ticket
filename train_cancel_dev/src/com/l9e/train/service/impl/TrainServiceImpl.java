package com.l9e.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.train.po.Account;
import com.l9e.train.po.AccountLog;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.DBBeanUtil;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * @author
 * 
 * 
 */
public class TrainServiceImpl {

	private Logger logger = Logger.getLogger(this.getClass());
	private DBBean dbbean = null;

	public List<Order> list = null;
	public Account account = null;
	public Worker worker = null;

	public Worker getWorker() {
		return worker;
	}

	public TrainServiceImpl() {
		// TODO Auto-generated constructor stub

		dbbean = new DBBean();
	}

	public Account getAccount() {
		return account;
	}

	/**
	 * 获取所有需要通知的列表
	 * 
	 * @return
	 */
	public List<Order> getOrderbill() {
		// TODO Auto-generated method stub

		return this.list;
	}

	/**
	 * 查询需要通知的数据，并且进行更新
	 * 
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderbillByList(final int getNum) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// TODO Auto-generated method stub

				// 获取订单信息
				StringBuffer sql = new StringBuffer();
				sql.append("select c.order_id, c.order_status, c.out_ticket_billno, c.account_id from cp_orderinfo c where c.order_status=? AND (c.supplier_type ='00' OR c.supplier_type is null) ");
				// sql.append("AND c.create_time > DATE_SUB(NOW(), INTERVAL 46
				// MINUTE)");
				// sql.append(" AND c.option_time < DATE_SUB(NOW(), INTERVAL 3
				// MINUTE)");
				sql.append(" ORDER BY c.create_time ASC limit ?");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Order.STATUS_CANCEL_START);
				cs.setInt(2, getNum);

				ResultSet rs = cs.executeQuery();

				list = new ArrayList<Order>();
				while (rs.next()) {
					Order order = new Order();
					order.setOrderId(rs.getString(1));
					order.setOrderStatus(rs.getString(2));
					order.setOutTicketBillNo(rs.getString(3));
					order.setAccountId(rs.getInt(4));
					list.add(order);
				}

				logger.info("get orderbill size:" + list.size());

				// 获取车票信息
				for (Order orderbill : list) {
					sql = new StringBuffer();
					sql.append(
							"select cp_id, CONCAT(user_name, '|',ticket_type, '|',cert_type, '|',cert_no, '|',seat_type) from cp_orderinfo_cp where order_id=?");

					PreparedStatement cpCs = cn.prepareStatement(sql.toString());
					cpCs.setString(1, orderbill.getOrderId());

					ResultSet cpRs = cpCs.executeQuery();

					while (cpRs.next()) {
						orderbill.addOrderCp(cpRs.getString(2));
					}

				}

				// 更新订单为正在取消
				sql = new StringBuffer();

				sql.append("update cp_orderinfo set order_status=?, option_time=now() where order_id=?");

				cs = cn.prepareStatement(sql.toString());
				for (Order orderbill : list) {
					// 更新订单状态为正在取消
					cs.setString(1, Order.STATUS_CANCELING);
					cs.setString(2, orderbill.getOrderId());

					cs.addBatch();
				}

				cs.executeBatch();

				return 0;
			}
		});
	}

	/**
	 * 需要重新发送订单的处理
	 * 
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsResend(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// TODO Auto-generated method stub

				// 更新系统订单
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?, option_time=now() where order_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, Order.STATUS_CANCEL_START);// 开始取消

				cs.setString(2, order.getOrderId());

				cs.executeUpdate();

				return 0;
			}
		});
	}

	/**
	 * 超时订单人工处理
	 * 
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsManual(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新订单状态
				StringBuffer sql = new StringBuffer();

				sql.append("select worker_id, worker_name, " //
						+ "worker_type,worker_ext, public_ip, worker_language_type " //
						+ "from cp_workerinfo where worker_type = ? "//
						+ "and worker_status<>?  order by rand() limit 1");

				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Worker.ORDER_Manual);
				cs.setString(2, Worker.STATUS_STOP);
				ResultSet rs = cs.executeQuery();

				if (rs.next()) {
					worker = new Worker();
					worker.setWorkerId(String.valueOf(rs.getInt(1)));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(rs.getInt(3));
					worker.setWorkerExt(rs.getString(4));
					worker.setPublicIp(rs.getString(5));
					worker.setScriptType(rs.getString(6));
				}

				// 更新订单状态为正在预定
				sql = new StringBuffer();

				sql.append("update cp_orderinfo set worker_id=?, order_status=?, option_time=now() where order_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, worker.getWorkerId());
				cs.setString(2, Order.STATUS_BILL_FAILURE);
				cs.setString(3, order.getOrderId());

				cs.executeUpdate();

				// start 更新员工处理量
				sql = new StringBuffer();

				sql.append("update cp_workerinfo set order_num=order_num+1 where worker_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, worker.getWorkerId());
				cs.executeUpdate();
				// end

				// 暂停账号
				sql = new StringBuffer();

				sql.append("update cp_accountinfo set acc_status=?, option_time=now() where acc_id=? and order_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, Account.STOP);
				cs.setInt(2, order.getAccountId());
				cs.setString(3, order.getOrderId());

				cs.executeUpdate();

				return 0;
			}
		});
	}

	/**
	 * 取消成功后订单的处理
	 * 
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsSuccess(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				logger.info("[orderIsSuccess]订单号为：" + order.getOrderId() + "的订单取消成功，开始更新数据库！");
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?,  option_time=now() ,opt_ren=? where order_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, Order.STATUS_BILL_FAILURE);
				cs.setString(2, "robot");
				cs.setString(3, order.getOrderId());

				logger.info("[orderIsSuccess]将订单号为：" + order.getOrderId() + "的订单状态更新为失败：" + Order.STATUS_BILL_FAILURE);
				cs.executeUpdate();

				// 开始发送通知给前台
				sql = new StringBuffer();
				sql.append(
						"update cp_orderinfo_notify set notify_status = ?, notify_num=0, notify_next_time=DATE_ADD(now(), interval 20 second), modify_time=now() where order_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, "1");
				cs.setString(2, order.getOrderId());

				cs.executeUpdate();
				// 开始发送通知 end

				String account_user = "";
				int account_num = 0;
				// 查询账号信息
				sql = new StringBuffer();

				sql.append("SELECT acc_username,contact_num FROM cp_accountinfo where acc_id=? for update");

				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, order.getAccountId());
				ResultSet rs = cs.executeQuery();

				if (rs.next()) {
					account_num = rs.getInt("contact_num");
					account_user = rs.getString("acc_username");
				}
				logger.info("acc_id:" + order.getAccountId() + ";account_num:" + account_num + ";account_user:" + account_user);
				if (account_num == 100) {
					// 账号停用
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status=?,order_id='',opt_person='cancle app',stop_reason=? where acc_id=?");

					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Account.STOP);
					cs.setString(2, "3");
					cs.setInt(3, order.getAccountId());

					cs.executeUpdate();
				} else {
					// 重发订单释放账号
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status=?, order_id='' where acc_id=?");

					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Account.FREE);
					cs.setInt(2, order.getAccountId());

					cs.executeUpdate();
				}

				// 添加操作账号日志
				logger.info("release 12306 account:" + account_user);
				String opt_logs = "cancel order释放账号";
				addAccLog(cn, cs, account_user, order.getOrderId(), opt_logs);

				return 0;

			}
		});

	}

	/**
	 * 获取账号
	 * 
	 * @param accountId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int findAccountAndWorker(final Order order) throws RepeatException, DatabaseException {
		// TODO:获取出票账号
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// TODO Auto-generated method stub

				// 获取出票人员
				StringBuffer sql = new StringBuffer();

				sql.append(
						"select worker_id, worker_name, worker_type,worker_ext, public_ip, worker_language_type  from cp_workerinfo where worker_type=? and worker_status<>? order by rand() limit 1  for update");

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(1, Worker.CANCEL_ROBOT);
				cs.setString(2, Worker.STATUS_STOP);

				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					worker = new Worker();
					worker.setWorkerId(String.valueOf(rs.getInt(1)));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(rs.getInt(3));
					worker.setWorkerExt(rs.getString(4));
					worker.setPublicIp(rs.getString(5));
					worker.setScriptType(rs.getString(6));
				} else {// 未获取到的出票人员
					return 2;
				}

				// 获取出票账号
				sql = new StringBuffer();

				sql.append(
						"select a.acc_id, a.acc_username, a.acc_password from cp_accountinfo a, cp_orderinfo o where a.acc_id=o.account_id and o.order_id=? for update");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, order.getOrderId());

				rs = cs.executeQuery();

				DBBeanUtil util = new DBBeanUtil();

				List<Account> alist = util.rs2List(rs, "com.l9e.train.po.Account");
				logger.info("alist size:" + alist.size());
				if (alist.size() > 0) {// 获取到账号
					account = alist.get(0);
					logger.info("orderByAccount account:" + account.getAccUsername() + " password:" + account.getAccPassword());
				} else {
					return 1;
				}

				return 0;
			}
		});

	}

	/**
	 * 插入历史记录
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int insertHistory(final String orderId, final String optlog) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				// 更新订单状态为正在预定
				StringBuffer sql = new StringBuffer();

				sql.append("insert into cp_orderinfo_history (order_id, order_optlog, create_time, opter) values(?, ?, now(), ?)");

				ps = cn.prepareStatement(sql.toString());

				ps.setString(1, orderId);
				ps.setString(2, optlog);
				ps.setString(3, "cancel robot");

				ps.executeUpdate();

				return 0;
			}
		});
	}

	/**
	 * 添加账号日志
	 * 
	 * @param cn
	 * @param cs
	 * @param acc_name
	 * @param orderId
	 * @param opt_logs
	 */
	public void addAccLog(Connection cn, PreparedStatement cs, final String acc_name, final String orderId, final String opt_logs) {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO account_logs(acc_username,order_id,opt_logs,create_time,opt_person )VALUES(?,?,?,NOW(),'pay app')");

		try {
			cs = cn.prepareStatement(sql.toString());
			cs.setString(1, acc_name);
			cs.setString(2, orderId);
			cs.setString(3, opt_logs);
			cs.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("add account log error", e);
		}

	}

	/**
	 * 账号相关取消 退票 改签 动作记录
	 * 
	 * @param log
	 */
	public int insertAccountHistory(final AccountLog log) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select acc_name,order_id,cz_type,create_time,opter from cp_account_refund_cancel_log where acc_name=? and order_id=? and cz_type=?");
				ps = cn.prepareStatement(sql.toString());
				ps.setString(1, log.getAcc_name());
				ps.setString(2, log.getOrder_id());
				ps.setString(3, log.getCz_type());
				ResultSet rs = ps.executeQuery();
				if (!rs.first()) {
					StringBuffer sql2 = new StringBuffer();
					sql2.append("insert into cp_account_refund_cancel_log (acc_name,order_id,cz_type,create_time,opter ) values (?,?,?,now(),?)");
					ps = cn.prepareStatement(sql2.toString());
					ps.setString(1, log.getAcc_name());
					ps.setString(2, log.getOrder_id());
					ps.setString(3, log.getCz_type());
					ps.setString(4, log.getOpter());
					ps.executeUpdate();
					logger.info("add account history success");
				} else {
					logger.info("add account history fail");
				}
				return 0;
			}
		});
	}

	/**
	 * 取消失败
	 * 
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsFail(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新系统订单
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?, option_time=now() where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Order.STATUS_CANCEL_FAILURE);// 取消失败
				cs.setString(2, order.getOrderId());
				cs.executeUpdate();
				return 0;
			}
		});
	}

}
