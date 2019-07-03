package com.l9e.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.TicketEntrance;
import com.l9e.train.po.Worker;
import com.l9e.train.po.WorkerVo;
import com.l9e.train.util.ConfigUtil;
import com.l9e.train.util.HttpUtil;
import com.l9e.train.util.WorkIdNum;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.DBBeanUtil;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

import net.sf.json.JSONObject;

public class TrainServiceImpl {

	private Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);

	private DBBean dbbean = null;
	public List<Order> list = null;
	public Account account = null;
	public Worker worker = null;
	public PayCard payCard = null;

	public int orderbillByList(final int getNum, final int time) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				list = new ArrayList<Order>();

				logger.info("#############################");
				logger.info("###");
				StringBuffer sql = new StringBuffer();
				sql.append("select c.order_id from cp_orderinfo c where c.order_status=? AND (c.supplier_type ='00' OR c.supplier_type is null) ");
				// sql.append("AND c.out_ticket_time< DATE_SUB(NOW(), INTERVAL
				// 30 SECOND) ");
				sql.append("order by c.out_ticket_time asc limit ?");
				logger.info("###  SQL:" + sql.toString());
				logger.info("###");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Order.STATUS_PAY_START);
				cs.setInt(2, getNum);
				ResultSet rs = cs.executeQuery();
				List<String> order_list = new ArrayList<String>();
				while (rs.next()) {
					order_list.add(rs.getString(1));
				}
				logger.info("### WAIT LIST:" + order_list);
				logger.info("###");
				logger.info("#############################");
				for (String order_id : order_list) {
					sql = new StringBuffer();
					sql.append(
							"select c.order_id from cp_orderinfo c where c.order_status=? AND c.order_id=? FOR UPDATE");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Order.STATUS_PAY_START);
					cs.setString(2, order_id);
					rs = cs.executeQuery();
					if (rs.next()) {
						sql = new StringBuffer();

						sql.append(
								"select c.order_id,c.order_status,c.account_id, c.buy_money, c.out_ticket_billno,c.out_ticket_time,c.from_time,c.level");
						sql.append(" from cp_orderinfo c where c.order_id=? AND c.order_status=? ");

						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, order_id);
						cs.setString(2, Order.STATUS_PAY_START);

						rs = cs.executeQuery();
						if (rs.next()) {
							Order order = new Order();
							order.setOrderId(rs.getString(1));
							order.setOrderStatus(rs.getString(2));
							order.setAccountId(Integer.valueOf(rs.getInt(3)));
							order.setBuymoney(rs.getString(4));
							order.setOutTicketBillNo(rs.getString(5));
							order.setOutTicketTime(rs.getTimestamp(6));
							order.setFromTime(rs.getTimestamp(7));
							order.setLevel(rs.getString(8));

							sql = new StringBuffer();
							sql.append(
									"select cp.user_name,cp.cert_no from cp_orderinfo_cp cp where cp.order_id=? limit 1");
							cs = cn.prepareStatement(sql.toString());
							cs.setString(1, order.getOrderId());

							rs = cs.executeQuery();
							while (rs.next()) {
								order.setUserName(rs.getString(1));
							}
							list.add(order);

							sql = new StringBuffer();
							sql.append(
									"update cp_orderinfo c set c.order_status=? where c.order_id=? and c.order_status=?");
							cs = cn.prepareStatement(sql.toString());
							logger.info("update orderid:" + order_id + " status:" + Order.STATUS_PAY_ING);
							cs.setString(1, Order.STATUS_PAY_ING);
							cs.setString(2, order_id);
							cs.setString(3, Order.STATUS_PAY_START);
							if (cs.executeUpdate() == 1) {
								logger.info("get the pay_orderinfo:" + order_id);
							}
						}
					}
				}
				logger.info("get orderbill size:" + list.size());
				return 0;
			}
		});
	}

	public int payIsResend(final Order order, final String logid) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = null;
				sql = new StringBuffer();

				sql.append(
						"SELECT COUNT(1) FROM cp_orderinfo c1 LEFT JOIN cp_orderinfo c2 ON c1.out_ticket_billno = c2.out_ticket_billno AND c1.order_id <> c2.order_id WHERE timediff(now() , c1.out_ticket_time)<'01:00:00' and c1.out_ticket_billno = ? AND ((c1.channel = 'tuniu' AND c2.channel <> '19e') OR (c1.channel = '19e' AND c2.channel <> 'tuniu'))");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, order.getOrderId());

				ResultSet rs = cs.executeQuery();
				rs.next();
				int size = rs.getInt(1);
				if (size == 0) {
					try {
						insertHistory(order.getOrderId(), "订单超过10分钟进入人工支付。");

						return payIsManual(order, null, logid);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?,");
				if ((order.getPaybillno() != null) && (!"".equals(order.getPaybillno()))) {
					sql.append("bank_pay_seq=?,");
				}
				sql.append("option_time=now() where order_id=?");

				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, Order.STATUS_PAY_MANUAL);
				if ((order.getPaybillno() != null) && (!"".equals(order.getPaybillno()))) {
					cs.setString(index++, order.getPaybillno());
				}
				cs.setString(index++, order.getOrderId());

				cs.executeUpdate();

				return 0;
			}
		});
	}

	public int payIsManual(final Order order, final List<TicketEntrance> ticketEntrances, final String logid)
			throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				Integer count = Integer.valueOf(1);

				// 查询机器人
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT ")//
						.append("count(1) num ")//
						.append("FROM cp_workerinfo WHERE ")//
						.append("worker_type = ? ")//
						.append("AND worker_status <> ? ")//
						.append("AND worker_status <> ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Worker.Robot_pay.intValue());
				cs.setString(2, "22");
				cs.setString(3, "33");
				this.rs = cs.executeQuery();
				while (this.rs.next()) {
					count = Integer.valueOf(this.rs.getInt("num"));
				}
				int num = WorkIdNum.getNextNum(count.intValue());

				sql = new StringBuffer();
				sql.append("SELECT ")//
						.append("worker_id, ")//
						.append("worker_name, ")//
						.append("worker_type, ")//
						.append("worker_ext, ")//
						.append("public_ip, ")//
						.append("worker_language_type ")//
						.append("FROM cp_workerinfo WHERE ")//
						.append("worker_type = ? ")//
						.append("and worker_status <> ? ")//
						.append("and worker_status <> ? ")//
						.append("ORDER BY worker_id ")//
						.append("limit ?,1");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Worker.Robot_pay.intValue());
				cs.setString(2, "22");
				cs.setString(3, "33");
				cs.setInt(4, num - 1);
				this.rs = cs.executeQuery();
				while (this.rs.next()) {
					worker = new Worker();
					worker.setWorkerId(this.rs.getString(1));
					worker.setWorkerName(this.rs.getString(2));
					worker.setWorkerType(Integer.valueOf(this.rs.getInt(3)));
					worker.setWorkerExt(this.rs.getString(4));
					worker.setPublicIp(this.rs.getString(5));
					worker.setLanguage(this.rs.getString(6));
				}

				String paybillno = order.getPaybillno();
				String orderId = order.getOrderId();
				sql = new StringBuffer();
				sql.append("UPDATE cp_orderinfo SET worker_id = ?, order_status = ?, ");
				if (StringUtils.isNotBlank(paybillno)) {
					sql.append("bank_pay_seq = ?, ");
				}
				sql.append("option_time = now() WHERE order_id = ?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				cs.setString(index++, worker.getWorkerId());
				cs.setString(index++, Order.STATUS_PAY_MANUAL);
				if (StringUtils.isNotBlank(paybillno)) {
					cs.setString(index++, paybillno);
				}
				cs.setString(index++, orderId);
				cs.executeUpdate();

				sql = new StringBuffer();
				sql.append(
						"UPDATE cp_workerinfo SET order_num = order_num + 1 ,spare_thread = spare_thread - 1 WHERE worker_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, worker.getWorkerId());
				cs.executeUpdate();

				// 插入检票口
				logger.info(logid + "更新检票口数据...");
				if (ticketEntrances != null && ticketEntrances.size() > 0) {
					sql = new StringBuffer();
					sql.append(
							"INSERT INTO cp_orderinfo_ct(order_id, train_num, station_name, entrance) values(?,?,?,?)");
					cs = cn.prepareStatement(sql.toString());
					TicketEntrance ticketEntrance = null;
					for (int i = 0; i < ticketEntrances.size(); i++) {
						ticketEntrance = ticketEntrances.get(i);
						cs.setString(1, order.getOrderId());
						cs.setString(2, ticketEntrance.getTrainNum());
						cs.setString(3, ticketEntrance.getStationName());
						cs.setString(4, ticketEntrance.getEntrance());
						cs.addBatch();
					}
					cs.executeBatch();
				}
				return 0;
			}
		});
	}

	/**
	 * 支付成功
	 * 
	 * @author: taoka
	 * @date: 2018年2月28日 下午2:12:11
	 * @param order
	 * @param result
	 * @param logid
	 * @return int
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int payIsSuccess(final Order order, final List<TicketEntrance> ticketEntrances, final PayCard payCard,
			final String logid) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			@SuppressWarnings("resource")
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				String paybillno = order.getPaybillno();
				String cardNo = payCard.getCardNo();
				String orderId = order.getOrderId();

				logger.info(logid + "更新订单数据...");
				StringBuilder sql = new StringBuilder();
				sql.append("UPDATE cp_orderinfo SET ")//
						.append("order_status = ?, ")//
						.append("option_time = now(), ")//
						.append("pay_time = now(), ");
				if (StringUtils.isNotBlank(paybillno)) {
					sql.append("bank_pay_seq = ?,");
				}
				sql.append("out_ticket_account=?, ")//
						.append("opt_ren = ? ")//
						.append("WHERE order_id = ?");

				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, Order.STATUS_BILL_SUCCESS);
				if (StringUtils.isNotBlank(paybillno)) {
					cs.setString(index++, paybillno);
				}
				cs.setString(index++, cardNo);
				cs.setString(index++, "robot");
				cs.setString(index++, orderId);
				cs.executeUpdate();

				// 检票口
				if (ticketEntrances != null && ticketEntrances.size() > 0) {
					sql = new StringBuilder();
					logger.info(logid + "更新检票口数据...");
					sql.append("INSERT INTO cp_orderinfo_ct(order_id,train_num,station_name,entrance) values(?,?,?,?)");
					cs = cn.prepareStatement(sql.toString());
					for (int i = 0; i < ticketEntrances.size(); i++) {
						TicketEntrance ticketEntrance = ticketEntrances.get(i);
						cs.setString(1, orderId);
						cs.setString(2, ticketEntrance.getTrainNum());
						cs.setString(3, ticketEntrance.getStationName());
						cs.setString(4, ticketEntrance.getEntrance());
						cs.addBatch();
					}
					cs.executeBatch();
				}

				// 通知
				logger.info(logid + "更新通知数据...");
				sql = new StringBuilder();
				sql.append(
						"UPDATE cp_orderinfo_notify set notify_status = ?, notify_num=0, notify_next_time=now(), modify_time=now() where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, "1");
				cs.setString(2, orderId);
				cs.executeUpdate();

				Integer accountId = order.getAccountId();
				logger.info(logid + "处理12306账号 " + accountId + " ...");
				String username = "";
				int contactNum = 0;
				int bookNum = 0;
				sql.setLength(0);
				sql.append("SELECT acc_username,contact_num,book_num FROM cp_accountinfo where acc_id = ? limit 0,1");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, accountId);
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					username = rs.getString("acc_username");
					contactNum = rs.getInt("contact_num");
					bookNum = rs.getInt("book_num");
				}

				logger.info(logid + "账号:" + username + " 常用联系人个数:" + contactNum + ", 下单次数:" + bookNum);
				// 停用原因:1:账号被封; 2:取消订单过多; 3:联系人达上限; 4:账号冒用; 5:临时封停; 6:账号不存在;
				// 7:账号密码错误; 8:手机未核验; 9:手机双向核验; 10:用户信息不完整; 11:身份信息待核验
				if (contactNum >= 15) {
					logger.info(logid + "联系人达上限，停用账号:" + accountId);
					sql.setLength(0);
					sql.append(
							"update cp_accountinfo set acc_status = ?,order_id='',opt_person='pay app',stop_reason=? where acc_id=? ");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Account.STOP);
					cs.setString(2, "3");
					cs.setInt(3, accountId);
					cs.executeUpdate();
				} else {
					sql.setLength(0);
					sql.append("update cp_accountinfo set acc_status=?, opt_person='pay app' ");
					if (bookNum >= WorkIdNum.book_num.intValue()) {
						sql.append(",stop_reason = '5' ");

						sql.append(",book_num = 4 ");
					}
					sql.append(" where acc_id = ?");

					cs = cn.prepareStatement(sql.toString());
					index = 1;
					if (bookNum >= WorkIdNum.book_num.intValue()) {
						logger.info(logid + "临时停用账号:" + accountId);
						cs.setString(index++, Account.TEMP_STOP);
					} else {
						cs.setString(index++, Account.FREE);
					}
					cs.setInt(index++, accountId);
					cs.executeUpdate();
				}
				String optLogs = "pay order释放账号";
				addAccLog(cn, cs, username, orderId, optLogs);
				return 0;
			}
		});
	}

	/**
	 * 1:没有12306账号; 2:没有机器人; 3:没有支付账号;
	 * 
	 * @author: taoka
	 * @date: 2018年2月27日 下午4:03:42
	 * @param order
	 * @param logid
	 * @return int
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int selectPayAccountAndWorkerBy(final Order order, final String logid)
			throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				DBBeanUtil util = null;
				ResultSet rs = null;

				// 从机器人服务器获取机器人
				getPayWorkerFromService(logid);
				if (worker == null) {
					return 2;
				}

				String workerId = worker.getWorkerId();
				logger.info(logid + "机器人ID为:" + workerId);

				StringBuffer sql = new StringBuffer();
				sql.append("SELECT ")//
						.append("worker_id, ")//
						.append("worker_name, ")//
						.append("worker_type, ")//
						.append("worker_ext, ")//
						.append("public_ip, ")//
						.append("worker_language_type ")//
						.append("FROM cp_workerinfo WHERE ")//
						.append("worker_id = ? ");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, workerId);
				rs = cs.executeQuery();
				if (rs.next()) {
					worker = new Worker();
					worker.setWorkerId(rs.getString(1));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(Integer.valueOf(rs.getInt(3)));
					worker.setWorkerExt(rs.getString(4));
					worker.setPublicIp(rs.getString(5));
					worker.setLanguage(rs.getString(6));
				} else {
					return 2;
				}

				// 支付信息
				sql.setLength(0);
				sql.append("SELECT ")//
						.append("card_id, ")//
						.append("card_no, ")//
						.append("card_pwd, ")//
						.append("card_phone, ")//
						.append("bank_type, ")//
						.append("pay_type, ")//
						.append("card_remain, ")//
						.append("card_ext ")//
						.append("FROM cp_cardinfo WHERE ")//
						.append("card_status <> ? ")//
						.append("AND worker_id = ?");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, PayCard.STATUS_STOP);
				cs.setString(2, workerId);
				rs = cs.executeQuery();
				util = new DBBeanUtil();
				@SuppressWarnings("unchecked")
				List<PayCard> plist = util.rs2List(rs, "com.l9e.train.po.PayCard");
				if (plist.size() > 0) {
					payCard = ((PayCard) plist.get(0));
				} else {
					// 没有支付账号
					return 3;
				}

				// 12306账号信息
				Integer accountId = order.getAccountId();
				logger.info(logid + "accountId:" + accountId);
				sql.setLength(0);
				sql.append("SELECT ")//
						.append("acc_id, ")//
						.append("acc_username, ")//
						.append("acc_password ")//
						.append("FROM cp_accountinfo WHERE ")//
						.append("acc_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, String.valueOf(order.getAccountId()));
				rs = cs.executeQuery();
				if (rs.next()) {
					account = new Account();
					account.setAccId(rs.getString(1));
					account.setAccUsername(rs.getString(2));
					account.setAccPassword(rs.getString(3));
				} else {
					return 1;
				}

				sql.setLength(0);
				sql.append("update cp_orderinfo set worker_id=?, option_time=now() where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, worker.getWorkerId());
				cs.setString(2, order.getOrderId());
				cs.executeUpdate();
				return 0;
			}
		});
	}

	public int insertHistory(final String orderId, final String optlog) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				StringBuffer sql = new StringBuffer();

				sql.append(
						"insert into cp_orderinfo_history (order_id, order_optlog, create_time, opter) values(?, ?, now(), ?)");

				ps = cn.prepareStatement(sql.toString());

				ps.setString(1, orderId);
				ps.setString(2, optlog);
				ps.setString(3, "pay app");

				ps.executeUpdate();

				return 0;
			}
		});
	}

	public int freeCard(final String cardId) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();

				sql.append("update cp_cardinfo set card_status=? where card_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, PayCard.STATUS_WAITUSER);
				cs.setString(2, cardId);

				cs.executeUpdate();

				return 0;
			}
		});
	}

	public void addAccLog(Connection cn, PreparedStatement cs, String acc_name, String orderId, String opt_logs) {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"INSERT INTO account_logs(acc_username,order_id,opt_logs,create_time,opt_person )VALUES(?,?,?,NOW(),'pay app')");
		try {
			cs = cn.prepareStatement(sql.toString());
			cs.setString(1, acc_name);
			cs.setString(2, orderId);
			cs.setString(3, opt_logs);
			cs.executeUpdate();
		} catch (SQLException e) {
			logger.info("add account log error", e);
		}
	}

	public int queryProductNum() throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				int count = 6;
				sql.append(
						"select setting_value from train_system_setting where setting_name='robot_pay_product_num' and setting_status='1'");

				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				while (rs.next()) {
					String setting_value = rs.getString("setting_value");
					count = Integer.valueOf(setting_value).intValue();
				}
				return count;
			}
		});
	}

	public int queryProductBookNum() throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				int count = 0;
				sql.append(
						"select setting_value from train_system_setting where setting_name='robot_app_book_num' and setting_status='1'");

				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				while (rs.next()) {
					String setting_value = rs.getString("setting_value");
					count = Integer.valueOf(setting_value).intValue();
				}
				return count;
			}
		});
	}

	public int queryProductTime() throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				int count = 3;
				sql.append(
						"select setting_value from train_system_setting where setting_name='robot_pay_time' and setting_status='1'");

				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				while (rs.next()) {
					String setting_value = rs.getString("setting_value");
					count = Integer.valueOf(setting_value).intValue();
				}
				return count;
			}
		});
	}

	public int queryProductNewTime() throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				int count = 5;
				sql.append(
						"select setting_value from train_system_setting where setting_name='robot_pay_new_time' and setting_status='1'");

				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				while (rs.next()) {
					String setting_value = rs.getString("setting_value");
					count = Integer.valueOf(setting_value).intValue();
				}
				return count;
			}
		});
	}

	public int isPaySuccessNoOrder() throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuilder sql = new StringBuilder();

				sql.append(
						"select setting_value from train_system_setting where setting_name = ? and setting_status = ?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, "pay_to_success");
				cs.setString(2, "1");

				ResultSet rs = cs.executeQuery();
				int retVal = 0;
				while (rs.next()) {
					String setting_value = rs.getString("setting_value");
					if ("00".equals(setting_value)) {
						retVal = 0;
					} else if ("11".equals(setting_value)) {
						retVal = 1;
					}
				}
				return retVal;
			}
		});
	}

	public int order2find(final String order_id) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();

				sql.append(
						"insert into cp_orderinfo_find(order_id,create_time,find_status) values(?,NOW(),?) on duplicate key update create_time = NOW(),find_status=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, order_id);
				cs.setString(2, "00");
				cs.setString(3, "00");

				cs.executeUpdate();
				return 0;
			}
		});
	}

	public Integer startWorkerReport(final Worker worker, final Order order, final String optType)
			throws RepeatException, DatabaseException {
		final StringBuilder builder = new StringBuilder();

		int ret = this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				ResultSet rs = null;
				if ((worker == null) || (order == null)) {
					return 1;
				}
				StringBuilder sql = new StringBuilder();
				Integer workerId = Integer.valueOf(worker.getWorkerId());
				String orderId = order.getOrderId();

				sql.append(
						"insert into cp_workerinfo_report(worker_id,worker_name,order_id,request_time,create_time,opt_type) values")
						.append(" (?,?,?,NOW(),NOW(),?)");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, workerId.intValue());
				cs.setString(2, worker.getWorkerName());
				cs.setString(3, orderId);
				cs.setString(4, optType);

				cs.executeUpdate();

				sql.setLength(0);
				sql.append("select report_id from cp_workerinfo_report where worker_id = ? and order_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, workerId.intValue());
				cs.setString(2, orderId);

				rs = cs.executeQuery();
				if (rs.next()) {
					builder.append(rs.getInt(1));
				} else {
					return 1;
				}
				return 0;
			}
		});
		if (ret == 1) {
			return null;
		}
		return Integer.valueOf(builder.toString());
	}

	public int endWorkerReport(final Integer reportId) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				if (reportId == null) {
					return 1;
				}
				StringBuilder sql = new StringBuilder();

				sql.append("update cp_workerinfo_report set release_time = NOW() where report_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, reportId.intValue());

				cs.executeUpdate();
				return 0;
			}
		});
	}

	public int stopWorker(Worker worker) throws RepeatException, DatabaseException {
		String url = ConfigUtil.getValue("stopAndStartPreparedWorker");
		String resultJson = HttpUtil.httpPost(url, "reason=22&workerId=" + worker.getWorkerId(), "utf-8", 3000, false);
		logger.info("停用支付机器人返回结果 result : " + resultJson);

		return 0;
	}

	public int balance4PayCard(final PayCard payCard, final Result result) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection conn, PreparedStatement ps) throws SQLException {
				logger.info(" start update balance for payCard!");
				if (StringUtils.isEmpty(result.getBalance())) {
					return 1;
				}
				if (payCard == null) {
					return 2;
				}
				String cardNo = payCard.getCardNo();
				Double balance = null;
				try {
					balance = Double.valueOf(result.getBalance());
				} catch (NumberFormatException e) {
					logger.info("balance : " + result.getBalance() + " exception: " + e.getMessage());
					return 1;
				}
				logger.info("update balance for payCard ,card_no : " + cardNo + ", balance : " + balance);

				StringBuilder sql = new StringBuilder();
				sql.append("update cp_cardinfo set card_remain = ? where card_no = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setDouble(1, balance.doubleValue());
				ps.setString(2, cardNo);

				ps.executeUpdate();

				return 0;
			}
		});
	}

	private void getPayWorkerFromService(String logid) {
		try {
			int count = 0;
			do {
				String resultJson = HttpUtil.httpPost(ConfigUtil.getValue("getWorker"), "type=3", "utf-8", 3000, false);
				logger.info(logid + "Server Response Result: " + resultJson);
				WorkerVo workerVo = null;
				if (StringUtils.isNotBlank(resultJson) && !HttpUtil.CONNECT_ERROR.equals(resultJson)
						&& !HttpUtil.URL_ERROR.equals(resultJson) && !HttpUtil.TIME_OUT.equals(resultJson)) {
					JSONObject resultJsonObject = JSONObject.fromObject(resultJson);
					if ((resultJsonObject.has("success")) && (resultJsonObject.getBoolean("success"))) {
						workerVo = (WorkerVo) JSONObject.toBean(resultJsonObject.getJSONObject("data"), WorkerVo.class);
					}
				}
				if (workerVo != null) {
					this.worker = new Worker();
					this.worker.setWorkerExt(workerVo.getWorkerExt());
					this.worker.setWorkerId(workerVo.getWorkerId().toString());
					this.worker.setWorkerName(workerVo.getWorkerName());
					this.worker.setWorkerType(Worker.Robot_pay);
					this.worker.setPay_device_type(workerVo.getPay_device_type());
					break;
				}
				count++;
			} while (count < 5);
		} catch (Exception e) {
			logger.info(logid + "【getPayWorkerFromService() Exception】", e);
		}
	}

	public String getSysSettingValue(final String settingName) throws RepeatException, DatabaseException {
		final StringBuilder builder = new StringBuilder();
		this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				if ((settingName == null) || ("".equals(settingName))) {
					return 1;
				}
				StringBuilder sql = new StringBuilder();

				ResultSet rs = null;
				sql.append("select setting_value from train_system_setting where setting_name = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, settingName);

				rs = cs.executeQuery();
				if (rs.next()) {
					builder.append(rs.getString(1));
				}
				return 0;
			}
		});
		return builder.length() == 0 ? null : builder.toString();
	}

	/**
	 * 查询同样12306的订单
	 * 
	 * @author: taoka
	 * @date: 2018年2月28日 下午1:51:38
	 * @param sequence
	 * @return int
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int querySameOutTicketBillno(final String sequence) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("SELECT ")//
						.append("count(ho.order_id) ")//
						.append("FROM cp_orderinfo ho WHERE ")//
						.append("TIMEDIFF(now() , ho.out_ticket_time)<'01:00:00' ")
						.append("AND ho.out_ticket_billno=? ");

				cs = cn.prepareCall(sql.toString());
				cs.setString(1, sequence);

				ResultSet rs = cs.executeQuery();
				rs.next();
				int size = rs.getInt(1);
				return size;
			}
		});
	}

	public Order queryOrder(final String orderId) {
		final Order order = new Order();
		try {
			this.dbbean.executeMethod(new ICallBack() {
				public int execute(Connection cn, PreparedStatement cs) throws SQLException {
					StringBuffer sql = new StringBuffer();
					sql.append("SELECT ")//
							.append("buy_money, ")//
							.append("out_ticket_billno, ")//
							.append("train_no, ")//
							.append("from_city, ")//
							.append("from_3c, ")//
							.append("to_city, ")//
							.append("to_3c, ")//
							.append("travel_time ")//
							.append("FROM cp_orderinfo ")//
							.append("WHERE order_id = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, orderId);

					ResultSet rs = cs.executeQuery();
					while (rs.next()) {
						order.setBuymoney(rs.getString(1));
						order.setOutTicketBillNo(rs.getString(2));
						order.setTrainno(rs.getString(3));
						order.setFrom(rs.getString(4));
						order.setFrom3c(rs.getString(5));
						order.setTo(rs.getString(6));
						order.setTo3c(rs.getString(7));
						order.setTranDate(rs.getString(8));
					}

					// 乘客
					sql = new StringBuffer();
					sql.append("SELECT ")//
							.append("user_name, ")//
							.append("cert_type, ")//
							.append("cert_no, ")//
							.append("ticket_type, ")//
							.append("seat_type, ")//
							.append("train_box, ")//
							.append("seat_no, ")//
							.append("sub_outTicket_billno ")//
							.append("FROM cp_orderinfo_cp ")//
							.append("WHERE order_id = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, orderId);
					rs = cs.executeQuery();
					List<OrderCP> cps = new ArrayList<OrderCP>();
					while (rs.next()) {
						OrderCP cp = new OrderCP();
						cp.setUsername(rs.getString(1));
						cp.setCertType(rs.getInt(2));
						cp.setCertNo(rs.getString(3));
						cp.setTrainType(rs.getInt(4));
						cp.setSeatType(rs.getInt(5));
						cp.setTrainbox(rs.getString(6));
						cp.setSeatNo(rs.getString(7));
						cp.setSubSequence(rs.getString(8));
						cps.add(cp);
					}
					order.setCps(cps);
					return 0;
				}
			});
		} catch (RepeatException | DatabaseException e) {
			e.printStackTrace();
			return null;
		}
		return order;
	}

	public PayCard getPayCard() {
		return this.payCard;
	}

	public Worker getWorker() {
		return this.worker;
	}

	public TrainServiceImpl() {
		this.dbbean = new DBBean();
	}

	public Account getAccount() {
		return this.account;
	}

	public List<Order> getOrderbill() {
		return this.list;
	}
}
