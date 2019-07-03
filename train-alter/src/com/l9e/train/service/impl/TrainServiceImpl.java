
package com.l9e.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.l9e.train.common.TrainConsts;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.l9e.train.po.WorkerVo;
import com.l9e.train.util.ConfigUtil;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;
import com.l9e.train.util.WorkIdNum;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.DBBeanUtil;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * @author wangdong
 * 
 * 
 */
public class TrainServiceImpl {

	private Logger logger = Logger.getLogger(this.getClass());
	private DBBean dbbean = null;

	public List<Order> list = null;
	public Worker worker = null;

	public Worker getWorker() {
		return worker;
	}

	public PayCard payCard = null;

	public PayCard getPayCard() {
		return payCard;
	}

	public TrainServiceImpl() {
		// TODO Auto-generated constructor stub
		dbbean = new DBBean();
	}

	public Order order3c = null; // 新增

	public Order getOrder3c() {
		return order3c;
	}

	/**
	 * 获取所有需要通知的列表
	 * 
	 * @return
	 */
	public List<Order> getOrderbill() {
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
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				list = new ArrayList<Order>();

				// 获取退款订单主键
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT change_id, order_id from elong_orderinfo_change where change_status in (?,?) AND (supplier_type ='00' OR supplier_type is null) ");
				sql.append("group by change_id order by from_time asc limit ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Order.WAITING_RESIGN);// 00等待机器改签
				cs.setString(2, Order.WAITING_PAY);
				cs.setInt(3, getNum);
				ResultSet rs = cs.executeQuery();
				List<String> change_order_list = new ArrayList<String>();
				while (rs.next()) {
					logger.info("改签chang_id为:" + rs.getString(1));
					logger.info("改签order_id为:" + rs.getString(2));
					change_order_list.add(rs.getString(1));
				}

				for (String change_id : change_order_list) {
					logger.info("改签chang_id为:" + change_id);
					// 获取订单加锁 start
					/*
					 * sql = new StringBuffer(); sql.append(
					 * "select change_id from elong_orderinfo_change  where change_id=?"
					 * );// FOR UPDATE"); cs =
					 * cn.prepareStatement(sql.toString()); cs.setString(1,
					 * change_id); //cs.setString(2,
					 * Order.WAITING_RESIGN);//11等待机器改签 rs = cs.executeQuery();
					 * if(!rs.next()){ logger.info("没有查询到需要改签的订单，跳出循环continue");
					 * continue; }
					 */
					// 获取订单加锁 end

					// 获取订单信息 start
					sql = new StringBuffer();
					sql.append("select change_id, order_id, create_time, out_ticket_billno, train_no, change_train_no, ")
							.append("from_city, to_city, from_time, travel_time, change_travel_time, ")
							.append("account_id,DATE_FORMAT(DATE_SUB(from_time, INTERVAL 15 DAY),'%Y-%m-%d') AS from_time_fifd,")
							.append("DATE_FORMAT(DATE_SUB(from_time, INTERVAL 24 HOUR),'%Y-%m-%d %H:%i:%s') AS from_time_tfh,")
							.append("DATE_FORMAT(DATE_SUB(from_time, INTERVAL 48 HOUR),'%Y-%m-%d %H:%i:%s') AS from_time_feh ")
							.append(", change_status,ischangeto,hasSeat,alter_pay_type,change_receive_money, chooseSeats from elong_orderinfo_change where change_id=? AND change_status in(?, ?) ");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, change_id);
					cs.setString(2, Order.WAITING_RESIGN);// 11等待机器改签
					cs.setString(3, Order.WAITING_PAY);// 31等待机器支付
					rs = cs.executeQuery();
					int index = 1;
					if (rs.next()) {
						Order order = new Order();
						order.setChangeId(rs.getString(index++));
						order.setOrderId(rs.getString(index++));
						order.setCreateTime(rs.getString(index++));
						order.setOutTicketBillno(rs.getString(index++));
						order.setTrainNo(rs.getString(index++));
						order.setChangeTrainNo(rs.getString(index++));
						order.setFromCity(rs.getString(index++));
						order.setToCity(rs.getString(index++));
						order.setFromTime(rs.getString(index++));
						order.setTravelTime(DateUtil.stringToString(rs.getString("from_time"), DateUtil.DATE_FMT1));
						index++;
						order.setChangeTravelTime(DateUtil.stringToString(rs.getString(index++), DateUtil.DATE_FMT1));
						// order.setSeatType(rs.getString(index++));
						// order.setAlterSeatType(rs.getString(index++));
						// order.setChannel(rs.getString(index++));
						// order.setAccountName(rs.getString(index++));
						// order.setAccountPwd(rs.getString(index++));
						order.setAccountId(rs.getInt(index++));
						order.setFrom_time_fifd(rs.getString(index++));
						order.setFrom_time_tfh(rs.getString(index++));
						order.setFrom_time_feh(rs.getString(index++));
						order.setChangeStatus(rs.getString(index++));
						order.setBuy_money("0");
						order.setIsChangeTo(rs.getInt(index++));
						order.setHasSeat(rs.getInt(index++));
						order.setAlterPayType(rs.getInt(index++));
						order.setChangeReceiveMoney(rs.getDouble(index++));
						order.setChooseSeat(rs.getString(index++));
						sql = new StringBuffer();
						sql.append("select acc_username, acc_password from cp_accountinfo where acc_id=?");
						cs = cn.prepareStatement(sql.toString());
						cs.setInt(1, order.getAccountId());
						rs = cs.executeQuery();
						if (rs.next()) {
							order.setAccountName(rs.getString(1));
							order.setAccountPwd(rs.getString(2));
						} else {
							logger.info("there is no account whose id = " + order.getAccountId());
						}

						String orderId = order.getOrderId();
						String changeId = order.getChangeId();
						sql = new StringBuffer();
						sql.append("select cp_id, buy_money, change_buy_money, ").append(" ticket_type, train_box, change_train_box, seat_no, change_seat_no, ")
								.append("ids_type, user_name, user_ids, seat_type, change_seat_type ").append("from elong_change_cp cp ")
								.append("where cp.order_id=? AND cp.change_id=?");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, orderId);// 订单号
						cs.setString(2, changeId);

						rs = cs.executeQuery();
						List<OrderCP> cps = new ArrayList<OrderCP>();
						while (rs.next()) {
							OrderCP cp = new OrderCP();
							cp.setCpId(rs.getString(1));
							cp.setBuyMoney(rs.getString(2));
							cp.setChangeBuyMoney(rs.getString(3));
							// cp.setOrderStatus(rs.getString(4));
							// cp.setOptionTime(rs.getString(5));
							// cp.setOptRen(rs.getString(6));
							cp.setTicketType(rs.getString(4));
							cp.setTrainBox(rs.getString(5));
							cp.setChangeTrainBox(rs.getString(6));
							cp.setSeatNo(rs.getString(7));
							cp.setChangeSeatNo(rs.getString(8));
							cp.setIdsType(rs.getString(9));
							cp.setUserName(rs.getString(10));
							cp.setUserIds(rs.getString(11));
							cp.setSeatType(rs.getString(12));
							cp.setChangeSeatType(rs.getString(13));
							order.setSeatType(rs.getString(12));
							order.setChangeSeatType(rs.getString(13));
							if (Double.valueOf(cp.getBuyMoney()) > Double.valueOf(order.getBuy_money())) {
								order.setBuy_money(cp.getBuyMoney());
							}
							cps.add(cp);

							sql = new StringBuffer();
							sql.append("update elong_orderinfo_change set change_status=? , option_time=now() where change_id=? ")
									.append(" AND change_status=?");
							cs = cn.prepareStatement(sql.toString());
							if (Order.WAITING_PAY.equals(order.getChangeStatus())) {
								cs.setString(1, Order.PAYING);
							} else {
								cs.setString(1, Order.RESIGNING);// 12开始机器改签
							}
							cs.setString(2, order.getChangeId());// 订单号
							// cs.setString(3, cp.getCpId());//车票号
							if (Order.WAITING_PAY.equals(order.getChangeStatus())) {
								cs.setString(3, Order.WAITING_PAY);// 等待支付
							} else {
								cs.setString(3, Order.WAITING_RESIGN);// 00等待机器改签
							}
							cs.executeUpdate();

							if (Order.WAITING_PAY.equals(order.getChangeStatus())) {
								logger.info("订单号为：" + order.getOrderId() + ";改签表主键为:" + order.getChangeId() + "更新表状态为正在支付32成功！");
							} else {
								logger.info("订单号为：" + order.getOrderId() + ";改签表主键为:" + order.getChangeId() + "更新表状态为正在改签12成功！");
							}

						}
						order.setCps(cps);

						list.add(order);
					} else {
						continue;
					}
					logger.info("get the alert_orderinfo:" + change_id);
				}
				logger.info("get orderbill size:" + list.size());
				return 0;

			}
		});
	}

	/**
	 * TODO
	 * 
	 * @author: taoka
	 * @date: 2018年3月6日 下午3:01:54
	 * @param order
	 * @param result
	 * @return int
	 * @throws Exception
	 */
	public int payIsManual(final Order order, final Result result) throws Exception {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 获取随机出票人员ID
				Integer count = 1;
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append("select count(1) num from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? ");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.Robot_pay);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_SPARE);

				rs = cs.executeQuery();
				while (rs.next()) {
					count = rs.getInt("num");
				}
				int num = WorkIdNum.getNextNum(count);
				sql_rand = new StringBuffer();
				sql_rand.append("select worker_id,worker_name, worker_type,worker_ext, public_ip , worker_language_type")
						.append("from cp_workerinfo where worker_type=? and worker_status <>? ").append("and worker_status <>? order by worker_id limit ?,1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.Robot_pay);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_SPARE);
				cs.setInt(4, num - 1);
				rs = cs.executeQuery();
				while (rs.next()) {
					worker = new Worker();
					// 正常分配到的机器人参数
					worker.setWorkerId(rs.getString(1));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(rs.getInt(3));
					worker.setWorkerExt(rs.getString(4));
					worker.setPublicIp(rs.getString(5));
					worker.setScript(rs.getString(6));

				}
				StringBuffer log_work = new StringBuffer();
				log_work.append("log_work: num:").append(num).append(" worker.getWorkerName()").append(worker.getWorkerName()).append(" work_url")
						.append(worker.getWorkerExt());
				logger.info(log_work.toString());
				// start 更新员工处理量
				StringBuffer sql = new StringBuffer();

				sql.append("update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, worker.getWorkerId());
				cs.executeUpdate();
				// end

				return 0;
			}
		});
	}

	/**
	 * 查询可用的改签机器人 </br>
	 * 0:查询成功， 1:没有可用机器人
	 * 
	 * @author: taoka
	 * @date: 2018年3月6日 下午2:21:08
	 * @param orderId
	 * @param logid
	 * @return int
	 * @throws Exception
	 */
	public int selectChangeWorker(final String orderId, final String logid) throws Exception {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				ResultSet rs = null;
				StringBuffer sql = new StringBuffer();

				logger.info(logid + "获取改签机器人...");
				// 根据操作时间排序，获取一个改签机器人
				sql = new StringBuffer();
				sql.append("SELECT ")//
						.append("worker_id, ")//
						.append("worker_name, ")//
						.append("worker_type, ")//
						.append("worker_ext, ")//
						.append("robot_id, ")//
						.append("public_ip , ")//
						.append("worker_language_type ")//
						.append("FROM cp_workerinfo WHERE ")//
						.append("worker_type = ? ")//
						.append("AND worker_status <> ? ")//
						.append("AND worker_status <> ? ")//
						.append("ORDER BY option_time  ASC ")//
						.append("LIMIT 1");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Worker.Robot_alert);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_SPARE);
				rs = cs.executeQuery();
				Worker wk = null;
				while (rs.next()) {
					wk = new Worker();
					// 正常分配到的机器人参数
					wk.setWorkerId(rs.getString(1));
					wk.setWorkerName(rs.getString(2));
					wk.setWorkerType(rs.getInt(3));
					wk.setWorkerExt(rs.getString(4));
					wk.setRobotId(rs.getString(5));
					wk.setPublicIp(rs.getString(6));
					wk.setScript(rs.getString(7));
				}

				if (wk == null) {
					return 1;
				}
				worker = wk;

				logger.info(logid + "Update Table ‘elong_orderinfo_change’ ...");
				String workerId = worker.getWorkerId();
				sql = new StringBuffer();
				sql.append("UPDATE elong_orderinfo_change SET worker_id = ? WHERE order_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, workerId);
				cs.setString(2, orderId);
				cs.executeUpdate();

				logger.info(logid + "Update Table ‘cp_workerinfo’ Value ‘spare_thread’ ...");
				sql = new StringBuffer();
				sql.append("UPDATE cp_workerinfo SET order_num = order_num + 1, spare_thread = spare_thread - 1, option_time = now() WHERE worker_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, workerId);
				cs.executeUpdate();
				return 0;
			}
		});
	}

	/**
	 * 更新支付账号余额
	 * 
	 * @param payCard
	 * @param result
	 * @return
	 * @throws DatabaseException
	 * @throws RepeatException
	 */
	public int updatePayCardBalance(final PayCard payCard, final String balance) throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection conn, PreparedStatement ps) throws SQLException {
				logger.info(" start update balance for payCard!~开始更新支付账户的余额！");
				if (StringUtils.isEmpty(balance)) {
					return 1;
				}
				if (payCard == null) {
					return 2;
				}

				String cardNo = payCard.getCardNo();
				Double cardRemain = null;

				try {
					cardRemain = Double.valueOf(balance);
				} catch (NumberFormatException e) {
					logger.info("balance : " + cardRemain + " exception: " + e.getMessage());
					return 1;
				}
				logger.info("update balance for payCard ,card_no : " + cardNo + ", balance : " + cardRemain);

				/* 支付宝余额记录 */
				StringBuilder sql = new StringBuilder();
				sql.append("update cp_cardinfo set card_remain = ? where card_no = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setDouble(1, cardRemain);
				ps.setString(2, cardNo);

				ps.executeUpdate();
				logger.info("更新支付账户余额！");
				return 0;
			}
		});
	}

	/**
	 * 从服务器获取支付机器人
	 * 
	 * @author: taoka
	 * @date: 2018年3月6日 下午3:07:06
	 * @param logid
	 */
	private void getPayWorkerFromService(String logid) {
		try {
			int count = 0;
			do {
				WorkerVo workerVo = null;
				String resultJson = HttpUtil.sendByPost(ConfigUtil.getValue("getWorker"), "type=3", "utf-8");
				logger.info(logid + "Server Response Result: " + resultJson);
				JSONObject json = JSONObject.parseObject(resultJson);
				Boolean success = json.getBoolean("success");
				if (success) {
					workerVo = JSONObject.parseObject(json.getJSONObject("data").toJSONString(), WorkerVo.class);
				}
				if (workerVo != null) {
					worker = new Worker();
					worker.setWorkerExt(workerVo.getWorkerExt());
					worker.setWorkerId(workerVo.getWorkerId().toString());
					worker.setWorkerName(workerVo.getWorkerName());
					worker.setWorkerType(Worker.Robot_pay);
					worker.setPay_device_type(Integer.valueOf(workerVo.getPay_device_type()));
					break;
				}
				count++;
			} while (count < 5);
		} catch (Exception e) {
			logger.info(logid + "【getPayWorkerFromService() Exception】", e);
		}
	}

	/***
	 * 获取改签支付时的机器人</br>
	 * 0:成功; 1:没有机器人; 2:没有支付账号;
	 * 
	 * @author: taoka
	 * @date: 2018年3月6日 下午2:31:59
	 * @param orderId
	 * @param logid
	 * @return int
	 * @throws Exception
	 */
	public int selectChangePayWorker(final String orderId, final String logid) throws Exception {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			DBBeanUtil util = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();

				logger.info(logid + "获取队列中的支付机器人...");
				getPayWorkerFromService(logid);
				if (worker == null) {
					return 1;
				}
				String workerId = worker.getWorkerId();
				String workerName = worker.getWorkerName();
				logger.info(logid + "支付机器人:" + workerName + "<-->" + workerId);

				sql = new StringBuffer();
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
					worker.setScript(rs.getString(6));
				} else {
					return 1;
				}

				// 更新订单改签机器人
				logger.info(logid + "Update Table ‘elong_orderinfo_change’ ...");
				sql = new StringBuffer();
				sql.append("update elong_orderinfo_change set worker_id=? where order_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, workerId);
				cs.setString(2, orderId);
				cs.executeUpdate();

				logger.info(logid + "Update Table ‘cp_workerinfo’ Value ‘spare_thread’ ...");
				sql = new StringBuffer();
				sql.append("update cp_workerinfo set order_num = order_num + 1, spare_thread = spare_thread - 1, option_time = now() WHERE worker_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, workerId);
				cs.executeUpdate();

				// 获取要支付的支付卡信息
				logger.info(logid + "获取支付信息...");
				sql = new StringBuffer();
				sql.append("SELECT ")//
						.append("card_id, ")//
						.append("card_no, ")//
						.append("card_pwd, ")//
						.append("card_phone, ")//
						.append("bank_type, ")//
						.append("pay_type, ")//
						.append("card_remain, ")//
						.append("card_ext ")//
						.append("FROM cp_cardinfo WHERE  ")//
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
					payCard = plist.get(0);
				} else {
					return 2;
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
	public int insertHistory(final String order_id, final String cp_id, final String optlog) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				logger.info(order_id + "~~~insert into elong_change_logs 开始往elong_change_logs表插入日志信息！");
				StringBuffer sql = new StringBuffer();

				sql.append("insert into elong_change_logs (order_id, change_id, cp_id, content, opt_person, create_time) values(?,?, ?, ?, ?, now())");

				ps = cn.prepareStatement(sql.toString());

				ps.setString(1, order_id);
				ps.setString(2, null);
				ps.setString(3, cp_id);
				ps.setString(4, optlog);
				ps.setString(5, "train_alter");

				ps.execute();
				logger.info(order_id + "~~~~insert into elong_change_logs 往elong_change_logs表插入日志信息成功！");
				return 0;
			}
		});
	}

	/**
	 * 获得退款订单数量
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryProductNum() throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				// 获取一次预订数量
				StringBuffer sql = new StringBuffer();
				int count = 6;
				sql.append("select setting_value from hc_system_setting where setting_name='robot_alert_product_num' and setting_status='1'");

				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				while (rs.next()) {
					String setting_value = rs.getString("setting_value");
					count = Integer.valueOf(setting_value);
				}
				return count;
			}
		});
	}

	// 更改退款表的订单状态
	public int updateOrderStatus(final Map<String, String> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				String logid = map.get("logid");
				String type = map.get("type");
				String changeFromTime = map.get("change_from_time");
				String bookFlag = map.get("bookFlag");
				String changeToTime = map.get("change_to_time");
				StringBuffer paramLog = new StringBuffer();

				StringBuffer sql = new StringBuffer();
				int index = 1;
				if ("ALL".equals(type)) {
					sql.append("UPDATE elong_orderinfo_change SET change_status = ?");
					if (StringUtils.isNotBlank(changeFromTime)) {
						sql.append(", change_from_time = ?");
					}
					if (StringUtils.isNotBlank(bookFlag) && bookFlag.equals("1")) {
						sql.append(", book_ticket_time = now()");
					}
					if (StringUtils.isNotBlank(changeToTime)) {
						sql.append(", change_to_time = ?");
					}
					if (null != map.get("fail_reason")) {
						sql.append(", fail_reason=?");
					}
					if (null != map.get("alter_pay_type")) {
						sql.append(", alter_pay_type=?");
					}
					if (null != map.get("change_receive_money")) {
						sql.append(", change_receive_money=?");
					}
					if (null != map.get("pay_limit_time") && !("".equals(map.get("pay_limit_time")))) {
						sql.append(", pay_limit_time=?");
					}
					// 支付流水号
					if (null != map.get("bankPaySeq")) {
						sql.append(", bank_pay_seq=?");
					}
					sql.append(", option_time = NOW(), opt_ren = 'train_alter' WHERE order_id = ? AND change_id = ? AND change_status = ?");

					logger.info("sql of update:" + sql.toString());
					cs = cn.prepareStatement(sql.toString());
					logger.info("update orderid:" + map.get("order_id") + " status:" + map.get("new_order_status"));
					cs.setString(index++, map.get("new_order_status"));
					if (StringUtils.isNotBlank(changeFromTime)) {
						cs.setString(index++, changeFromTime);
					}
					if (StringUtils.isNotBlank(changeToTime)) {
						cs.setString(index++, changeToTime);
					}
					if (null != map.get("fail_reason")) {
						cs.setString(index++, map.get("fail_reason"));
					}
					if (null != map.get("alter_pay_type")) {
						cs.setInt(index++, Integer.valueOf(map.get("alter_pay_type")));
					}
					if (null != map.get("change_receive_money")) {
						cs.setDouble(index++, Double.valueOf(map.get("change_receive_money")));
					}
					if (null != map.get("pay_limit_time") && !("".equals(map.get("pay_limit_time")))) {
						cs.setString(index++, map.get("pay_limit_time"));
					}
					// 支付流水号
					if (null != map.get("bankPaySeq")) {
						cs.setString(index++, map.get("bankPaySeq"));
					}

					cs.setString(index++, map.get("order_id"));
					cs.setString(index++, map.get("change_id"));
					cs.setString(index, map.get("order_status"));
					int row = cs.executeUpdate();
					logger.info("更新状态受影响的条数为：" + row);
					// 更新改签结果通知表
					/*
					 * sql = new StringBuffer(); sql.append(
					 * "UPDATE cp_orderinfo_refund_notify SET notify_type=0, notify_status=1,notify_time=now(),notify_next_time=now() WHERE order_id=?"
					 * ); cs = cn.prepareStatement(sql.toString());
					 * cs.setString(1, map.get("order_id")); cs.executeUpdate();
					 * 
					 * if("refund".equals(map.get("stract"))){ //更新改签结果通知表 sql =
					 * new StringBuffer(); sql.append(
					 * "UPDATE cp_orderinfo_refund SET alter_seat_type=seat_type, alter_train_box=train_box,alter_seat_no=seat_no WHERE order_id=?"
					 * ); cs = cn.prepareStatement(sql.toString());
					 * cs.setString(1, map.get("order_id")); cs.executeUpdate();
					 * }
					 */
				} else {
					sql.append("UPDATE elong_orderinfo_change SET change_status=?");
					if (null != changeFromTime && !("".equalsIgnoreCase(changeFromTime))) {
						sql.append(", change_from_time=?");
					}
					if (StringUtils.isNotBlank(bookFlag) && bookFlag.equals("1")) {
						sql.append(", book_ticket_time=now()");
					}
					if (StringUtils.isNotBlank(changeToTime)) {
						sql.append(", change_to_time=?");
					}
					if (null != map.get("fail_reason")) {
						sql.append(", fail_reason=?");
					}
					if (null != map.get("alter_pay_type")) {
						sql.append(", alter_pay_type=?");
					}
					if (null != map.get("change_receive_money")) {
						sql.append(", change_receive_money=?");
					}
					if (null != map.get("pay_limit_time") && !("".equals(map.get("pay_limit_time")))) {
						sql.append(", pay_limit_time=?");
					}
					// 支付流水号
					if (null != map.get("bankPaySeq")) {
						sql.append(", bank_pay_seq=?");
					}

					sql.append(", option_time=NOW(), opt_ren='train_alter'  WHERE order_id=? AND change_id=? AND change_status=? ");

					logger.info("else sql of update:" + sql.toString());
					index = 1;
					cs = cn.prepareStatement(sql.toString());
					logger.info("update orderid:" + map.get("order_id") + " status:" + map.get("new_order_status"));
					cs.setString(index++, map.get("new_order_status"));
					if (null != changeFromTime && !("".equalsIgnoreCase(changeFromTime))) {
						cs.setString(index++, changeFromTime);
					}
					if (StringUtils.isNotBlank(changeToTime)) {
						cs.setString(index++, changeToTime);
					}
					if (null != map.get("fail_reason")) {
						cs.setString(index++, map.get("fail_reason"));
					}
					if (null != map.get("alter_pay_type")) {
						cs.setInt(index++, Integer.valueOf(map.get("alter_pay_type")));
					}
					if (null != map.get("change_receive_money")) {
						cs.setDouble(index++, Double.valueOf(map.get("change_receive_money")));
					}
					if (null != map.get("pay_limit_time") && !("".equals(map.get("pay_limit_time")))) {
						cs.setString(index++, map.get("pay_limit_time"));
					}
					// 支付流水号
					if (null != map.get("bankPaySeq")) {
						cs.setString(index++, map.get("bankPaySeq"));
					}

					cs.setString(index++, map.get("order_id"));
					cs.setString(index++, map.get("change_id"));
					cs.setString(index, map.get("order_status"));
					int row = cs.executeUpdate();
					logger.info("else update order_id" + map.get("order_id") + " the row is " + row);
					/*
					 * sql = new StringBuffer(); sql.append(
					 * "UPDATE cp_orderinfo_refund_notify SET notify_type=0, notify_status=1,notify_time=now(),notify_next_time=now() WHERE order_id=? and cp_id=? "
					 * ); cs = cn.prepareStatement(sql.toString());
					 * cs.setString(1, map.get("order_id")); cs.setString(2,
					 * map.get("cp_id")); cs.executeUpdate();
					 * 
					 * if("refund".equals(map.get("stract"))){ //更新改签结果通知表 sql =
					 * new StringBuffer(); sql.append(
					 * "UPDATE cp_orderinfo_refund SET alter_seat_type=seat_type, alter_train_box=train_box,alter_seat_no=seat_no WHERE order_id=? and cp_id=?"
					 * ); cs = cn.prepareStatement(sql.toString());
					 * cs.setString(1, map.get("order_id")); cs.setString(2,
					 * map.get("cp_id")); cs.executeUpdate(); }
					 */
				}

				// 用于统计数据
				if ("true".equals(map.get("statistical"))) {
					// 插入日志
					sql = new StringBuffer();
					sql.append("insert into cp_count(source,channel,type,create_time,message,code) values(?,?,?,NOW(),?,?)");
					cs = cn.prepareStatement(sql.toString());
					index = 1;
					cs.setString(index++, "train_alter_dev");
					cs.setString(index++, map.get("channel"));
					cs.setString(index++, "03");
					if (TrainConsts.ROBOT_REFUND_STATUS03.equals(map.get("new_order_status"))) {
						cs.setString(index++, "改签失败！");
						cs.setString(index, "02");
					} else if (TrainConsts.ROBOT_REFUND_STATUS04.equals(map.get("new_order_status"))) {
						cs.setString(index++, "改签成功！");
						cs.setString(index, "01");
					} else {
						cs.setString(index++, "改签一次！");
						cs.setString(index, "00");
					}
					cs.executeUpdate();
				}
				return 0;
			}
		});
	}

	// 更新改签信息
	public int updateOrderCp(final Map<String, String> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("UPDATE elong_change_cp SET ")//
						.append("change_buy_money = ?, ")//
						.append("change_seat_type = ?, ")//
						.append("change_train_box = ?, ")//
						.append("change_seat_no = ? ")//
						.append("WHERE ")//
						.append("order_id = ? ")//
						.append("AND cp_id = ?");
				cs = cn.prepareStatement(sql.toString());
				logger.info("update orderid:" + map.get("order_id"));

				cs.setString(1, map.get("alter_pay_money"));
				// cs.setString(2, map.get("alter_from_time"));
				// cs.setString(3, map.get("alter_travel_time"));
				cs.setString(2, map.get("alter_seat_type"));
				cs.setString(3, map.get("alter_train_box"));
				cs.setString(4, map.get("alter_seat_no"));
				cs.setString(5, map.get("order_id"));
				cs.setString(6, map.get("cp_id"));

				cs.executeUpdate();
				return 0;
			}
		});
	}

	// 更新改签信息
	public int updateOrderAlterMoney(final Map<String, String> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				logger.info("update orderid alter_money:" + map.get("order_id"));
				sql.append("UPDATE elong SET alter_diff_money=buy_money-alter_buy_money ").append("WHERE order_id=? AND cp_id=? ");
				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, map.get("order_id"));
				cs.setString(2, map.get("cp_id"));

				cs.executeUpdate();
				return 0;
			}
		});
	}

	/**
	 * 查询订单表，获得包含到达城市与出发城市三字码的实体 add by wangsf
	 * 
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public void queryOrderCity3c(final String fromCity, final String toCity) throws RepeatException, DatabaseException {

		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				// 获取跟订单中的出发和到达城市相对应的三字码
				StringBuffer sql = new StringBuffer();
				sql.append("select c.zmhz,c.lh ");
				sql.append(" from t_zm AS c where c.zmhz = ? or c.zmhz = ? ");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, fromCity);
				cs.setString(2, toCity);
				ResultSet rs = cs.executeQuery();
				rs = cs.executeQuery();

				String zmhz = ""; // t_zm表中的火车站点名称
				String lh = ""; // t_zm表中的站点三字码
				order3c = new Order();
				while (rs.next()) {
					zmhz = rs.getString(1);
					lh = rs.getString(2);
					if (fromCity.equals(zmhz)) {
						order3c.setFromCity_3c(lh);
					} else if (toCity.equals(zmhz)) {
						order3c.setToCity_3c(lh);
					}
				}
				return 0;
			}

		});

	}

	/**
	 * 查询系统设置值
	 * 
	 * @param settingName
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public String getSysSettingValue(final String settingName) throws RepeatException, DatabaseException {

		final StringBuilder builder = new StringBuilder();
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				if (settingName == null || "".equals(settingName))
					return 1;

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
	 * 检票口
	 * 
	 * @author: taoka
	 * @date: 2018年4月12日 上午11:11:39
	 * @param order
	 * @param entranceInfo
	 * @param logid
	 * @throws DatabaseException
	 * @throws RepeatException
	 */
	public int insertEntrance(final Order order, final JSONObject entranceInfo, final String logid) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				String orderId = order.getOrderId();
				String changeId = order.getChangeId();
				String trainCode = entranceInfo.getString("trainCode");
				String stationName = entranceInfo.getString("stationName");
				String entrance = entranceInfo.getString("entrance");

				logger.info(logid + "ORDER_ID:" + orderId + ", CHANGE_ID:" + changeId + ", trainCode:" + trainCode + ", stationName:" + stationName
						+ ", entrance:" + entrance);

				StringBuffer sql = new StringBuffer();
				sql.append("INSERT INTO ")//
						.append("cp_orderinfo_ct_change (")//
						.append("order_id, ")//
						.append("change_id, ")//
						.append("train_num, ")//
						.append("station_name, ")//
						.append("entrance) ")//
						.append("VALUES (?, ?, ?, ?, ?)");
				ps = cn.prepareStatement(sql.toString());
				ps.setString(1, orderId);
				ps.setString(2, changeId);
				ps.setString(3, trainCode);
				ps.setString(4, stationName);
				ps.setString(5, entrance);
				boolean execute = ps.execute();
				if (execute) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

}
