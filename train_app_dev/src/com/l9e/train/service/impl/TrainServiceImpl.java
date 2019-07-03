package com.l9e.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.l9e.train.main.App;
import com.l9e.train.po.Account;
import com.l9e.train.po.AccountFilter;
import com.l9e.train.po.CtripAcc;
import com.l9e.train.po.DataMaintainVo;
import com.l9e.train.po.JdAcc;
import com.l9e.train.po.JdPrePayCard;
import com.l9e.train.po.Notify;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.OrderVo;
import com.l9e.train.po.Passenger;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.l9e.train.po.WorkerWeight;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.StrUtil;
import com.l9e.train.util.WorkIdNum;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.DBBeanUtil;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * @author wangdong
 * 
 */
public class TrainServiceImpl {

	private Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);

	private DBBean dbbean = null;

	public List<Order> list = null;

	List<String> ctripPhoneNumList = null; // 携程手机号码列表

	public Account account = null;
	public Worker worker = null;
	public CtripAcc ctripAcc = null;
	// 新增京东账号实体和京东预付卡列表
	public JdAcc jdAcc = null;
	public List<JdPrePayCard> jdPrePayCardList = null;

	public List<Map<String, String>> warnList = new ArrayList<Map<String, String>>();

	public String join_acc_channel = "";

	public Order order3c = null; // 新增

	public Order getOrder3c() {
		return order3c;
	}

	public String getChannel() {
		return this.join_acc_channel;
	}

	public List<Map<String, String>> getWarnList() {
		return warnList;
	}

	public Worker getWorker() {
		return worker;
	}

	public CtripAcc getCtripAcc() {
		return ctripAcc;
	}

	public JdAcc getJdAcc() {
		return jdAcc;
	}

	public List<JdPrePayCard> getJdPrePayCardList() {
		return jdPrePayCardList;
	}

	public String param = "";

	public String getParam() {
		return this.param;
	}

	public TrainServiceImpl() {

		dbbean = new DBBean();
	}

	public Account getAccount() {
		return account;
	}

	/**
	 * 获取所有携程手机号码的列表
	 * 
	 * @return
	 */
	public List<String> getCtripPhoneNumList() {
		return ctripPhoneNumList;
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
	 * 京东出票重发次数
	 */
	public int resendNum = 0;

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
				/*
				 * // 获取订单信息 train_app_new 使用 StringBuffer sql = new
				 * StringBuffer(); int index = 1; String now_date =
				 * DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3); String
				 * sub_hour = DateUtil.dateAddHours(new Date(),-24); String
				 * sub_minute = DateUtil.dateAddMin(new Date(),-1); sql.append(
				 * "select c.order_id,c.order_status from cp_orderinfo c where c.order_status in (?,?) "
				 * ); sql.append("AND c.create_time < ?"); sql.append(
				 * "AND c.option_time < ?"); sql.append("AND c.create_time > ? "
				 * ); sql.append("order by c.create_time desc limit ?");
				 * 
				 * cs = cn.prepareStatement(sql.toString());
				 * cs.setString(index++, Order.STATUS_ORDER_START);// 开始订购
				 * cs.setString(index++, Order.STATUS_ORDER_RESEND);// 重发出票
				 * cs.setString(index++, sub_minute); // create_time_xiao
				 * cs.setString(index++, now_date); // option_time
				 * cs.setString(index++, sub_hour); // 12小时 cs.setInt(index++,
				 * getNum);
				 */

				// 获取订单信息 train_app 使用
				StringBuffer sql = new StringBuffer();
				int index = 1;
				String now_date = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
				String sub_hour = DateUtil.dateAddHours(new Date(), -24);
				sql.append("select c.order_id,c.order_status from cp_orderinfo c ");
				sql.append("where c.channel <> '301030' and c.channel <> 'tuniu' and c.order_status in (?,?) ");
				sql.append("AND c.create_time < ?");
				sql.append("AND c.option_time < ?");
				sql.append("AND c.create_time > ? ");
				sql.append("order by c.create_time asc limit ?");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(index++, Order.STATUS_ORDER_START);// 开始订购
				cs.setString(index++, Order.STATUS_ORDER_RESEND);// 重发出票
				cs.setString(index++, now_date); // create_time
				cs.setString(index++, now_date); // option_time
				cs.setString(index++, sub_hour); // 12小时
				cs.setInt(index++, getNum);

				ResultSet rs = cs.executeQuery();
				List<Map<String, String>> order_list = new ArrayList<Map<String, String>>();
				while (rs.next()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("order_id", rs.getString(1));
					map.put("order_status", rs.getString(2));
					order_list.add(map);
				}

				list = new ArrayList<Order>();
				Order order = null;
				for (Map<String, String> order_info : order_list) {
					sql = new StringBuffer();
					sql.append(
							"select order_id from cp_orderinfo where order_id=? AND order_status in(?,?) FOR UPDATE");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, order_info.get("order_id"));
					cs.setString(2, Order.STATUS_ORDER_START);// 开始订购
					cs.setString(3, Order.STATUS_ORDER_RESEND);// 重发出票

					String order_id = "";
					rs = cs.executeQuery();
					if (rs.next()) {
						order_id = rs.getString(1);
					} else {
						continue;
					}
					// logger.info("get the order_id to update :"+order_id);
					// 先订单修改状态
					sql = new StringBuffer();
					sql.append(
							"update cp_orderinfo c set c.order_status=? where c.order_id=? AND c.order_status in(?,?)");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Order.STATUS_ORDER_ING);
					cs.setString(2, order_id);
					cs.setString(3, Order.STATUS_ORDER_START);// 开始订购
					cs.setString(4, Order.STATUS_ORDER_RESEND);// 重发出票

					if (cs.executeUpdate() != 1) {
						logger.info("this order is locked:" + order_id);
						continue;
					}
					// logger.info("update order_id to downing order
					// status:"+order_id);
					// 获取订单信息
					sql = new StringBuffer();
					sql.append("select c.order_id, c.order_status,c.account_id, c.pay_money,");
					sql.append(" CONCAT(c.order_id,'|',c.from_city,'|', c.to_city, '|',");
					sql.append(" DATE_FORMAT(c.travel_time,'%Y-%m-%d'), '|', c.train_no), c.seat_type,");
					sql.append(" c.from_time, c.channel, c.level, c.ext_seattype,");
					sql.append("DATE_FORMAT(c.travel_time,'%Y-%m-%d') travel_time,account_id,manual_order,");
					sql.append("c.from_city,c.to_city,c.from_3c,c.to_3c,c.account_from_way,"); // 在此新增火车站点名称与三字码
																								// add
																								// by
																								// wangsf
					sql.append(
							"c.seat_detail_type,c.choose_seats,c.train_no,if_12306_cutover,resend_identify,jdbook_resendnum");
					sql.append(" from cp_orderinfo c where c.order_id=? ");

					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, order_id);// 开始订购
					rs = cs.executeQuery();
					order = new Order();
					if (rs.next()) {
						order.setOrderId(rs.getString(1));
						order.setOrderStatus(order_info.get("order_status"));
						order.setAccountId(rs.getInt(3));
						order.setPaymoney(rs.getString(4));
						order.setOrderstr(rs.getString(5));
						order.setSeatType(rs.getString(6));
						order.setSeattime(rs.getString(7));
						order.setChannel(rs.getString(8));
						order.setLevel(rs.getString(9));
						order.setExtSeatType(rs.getString(10));
						order.setTravel_time(rs.getString(11));
						order.setAcc_id(rs.getInt(12));
						order.setManual_order(rs.getString(13));
						// 在此新增火车站点名称与三字码 add by wangsf
						order.setFrom(rs.getString(14));
						order.setTo(rs.getString(15));
						order.setFromCity_3c(rs.getString(16));
						order.setToCity_3c(rs.getString(17));
						order.setAccountFromWay(rs.getInt(18));// 账号来源： 0：公司自有账号
																// ； 1：12306自带账号
						order.setSeatDetailType(rs.getString(19));//// 卧铺位置
						order.setChoose_seats(rs.getString(20));// 座位号
						order.setTrainno(rs.getString(21));// 车次
						order.setIf12306CutOver(rs.getInt(22));// 是否切入12306出票
																// 0：否 1：是
						order.setResendIndetify(rs.getInt(23));// 重发类型： 0：无变化重发
																// 1：切换12306账号重发
																// 2：切换京东账号重发
																// 3：切换京东预付卡重发
						order.setJdbookResendnum(rs.getInt(24));// 京东预定重发次数
					} else {
						continue;
					}
					logger.info("get the orderinfo notice robot to book :" + order_id);
					logger.info(order_id + "-订单信息:" + JSONObject.toJSONString(order));
					sql = new StringBuffer();
					if ("tongcheng".equals(order.getChannel())) {
						sql.append(
								"select cp_id, CONCAT(cp_id, '|', user_name, '|',CONVERT(ticket_type,CHAR), '|',CONVERT(cert_type,CHAR), '|',cert_no, '|',seat_type) ,ticket_type from cp_orderinfo_cp where order_id=?");
					} else {
						sql.append(
								"select cp_id, CONCAT(cp_id, '|', user_name, '|',CONVERT(ticket_type,CHAR), '|',CONVERT(cert_type,CHAR), '|',cert_no) ,ticket_type from cp_orderinfo_cp where order_id=?");
					}

					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, order_id);

					rs = cs.executeQuery();

					while (rs.next()) {
						if ("tongcheng".equals(order.getChannel())) {
							order.addOrderCp(rs.getString(2));
						} else {
							order.addOrderCp(rs.getString(2) + "|" + order.getSeatType());
						}
						if (rs.getInt(3) == 1) {
							order.setWea_price(true);
						}
					}

					sql = new StringBuffer();
					sql.append(
							"select cp_id, CONCAT(cp_id, '|', user_name, '|',CONVERT(ticket_type,CHAR), '|',CONVERT(cert_type,CHAR), '|',cert_no, '|',seat_type) ,ticket_type from cp_orderinfo_cp where order_id=?");

					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, order_id);

					rs = cs.executeQuery();
					while (rs.next()) {

					}

					// logger.info("list add orderinfo
					// :"+order_info.get("order_id"));
					if (Order.CHANNEL_QUNAR.equals(order.getChannel()) || Order.CHANNEL_TC.equals(order.getChannel())
							|| Order.CHANNEL_MEITUAN.equals(order.getChannel())
							|| Order.CHANNEL_GT.equals(order.getChannel()) || "tuniu".equals(order.getChannel())) {
						order.setChannelGroup(Order.CHANNEL_GROUP_1);
					} else {
						order.setChannelGroup(Order.CHANNEL_GROUP_2);
					}
					list.add(order);

				}
				logger.info("get orderbill size:" + list.size());
				return 0;
			}
		});
	}

	/**
	 * 需要重新发送订单的处理（排队订单）
	 * 
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsWait(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新系统订单为排队订单
				StringBuffer sql = null;

				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?,return_optlog=?, ");
				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					sql.append("to_time=?,");
				}
				sql.append("option_time=DATE_ADD(NOW(), INTERVAL 1 MINUTE) where order_id=?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());

				cs.setString(index++, Order.STATUS_ORDER_RESEND);

				cs.setString(index++, result.getReturnOptlog());

				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					cs.setString(index++, order.getArrivetime());
				}

				cs.setString(index, order.getOrderId());

				cs.executeUpdate();
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
				// 更新系统订单
				StringBuffer sql = null;

				String status = null;

				sql = new StringBuffer();
				sql.append(
						"SELECT count(ho.order_id) FROM cp_orderinfo ho where timediff(now() , ho.option_time)<'00:10:00'  and ho.order_id=?");

				cs = cn.prepareCall(sql.toString());
				cs.setString(1, order.getOrderId());

				ResultSet rs = cs.executeQuery();
				int size = 0;
				if (rs.next()) {
					size = rs.getInt(1);
				}

				if (size > 0) {// 未超规定的时间
					status = Order.STATUS_ORDER_RESEND;
				} else {// 如果时间不充裕，进入人工处理
						// status = Order.STATUS_ORDER_MANUAL;
					try {

						insertHistory(order.getOrderId(), "订单操作10分钟进入人工处理。");

						return orderIsManual(order, result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?, option_time=now(),return_optlog=? ");
				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					sql.append(",to_time=? ");
				}
				sql.append("where order_id=? AND order_status = ?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());

				cs.setString(index++, status);

				cs.setString(index++, result.getReturnOptlog());
				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					cs.setString(index++, order.getArrivetime());
				}

				cs.setString(index++, order.getOrderId());

				cs.setString(index, Order.STATUS_ORDER_ING);

				cs.executeUpdate();
				return 0;
			}
		});
	}

	/**
	 * 发送失败后订单的处理
	 * 
	 * @param logid
	 * 
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsFailure(final Order order, final Result result, final String logid)
			throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				String orderId = order.getOrderId();
				String failReason = result.getFailReason();
				logger.info("{}ORDER_ID:{} FAIL_REASON:{}", logid, orderId, failReason);

				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("UPDATE ")//
						.append("cp_orderinfo SET ")//
						.append("order_status = ?, ")//
						.append("buy_money = ?, ")//
						.append("error_info = ?, ")//
						.append("option_time = now(), ")//
						.append("return_optlog = ?, ")//
						.append("opt_ren = ?");

				String channelGroup = order.getChannelGroup();
				String arrivetime = order.getArrivetime();
				if (Order.CHANNEL_GROUP_1.equals(channelGroup) && !"".equals(arrivetime)) {
					sql.append(", to_time = ?");
				}
				sql.append(" WHERE order_id = ?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				cs.setString(index++, Order.STATUS_BILL_FAILURE);
				cs.setString(index++, result.getPayMoney());
				cs.setString(index++, failReason);
				cs.setString(index++, result.getReturnOptlog());
				cs.setString(index++, "robot");

				if (Order.CHANNEL_GROUP_1.equals(channelGroup) && !"".equals(arrivetime)) {
					cs.setString(index++, arrivetime);
				}
				cs.setString(index++, orderId);
				cs.executeUpdate();

				// 录入车票相关信息
				boolean priceModify = result.isPriceModify();
				logger.info("{}更新车票信息:{}", logid, priceModify);
				if (priceModify) {
					sql = new StringBuffer();
					sql.append(
							"update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?,seat_type=?, modify_time=now() where cp_id=? and order_id=?");
					cs = cn.prepareStatement(sql.toString());
					for (OrderCP cp : result.getOrderCps()) {
						cs.setString(1, cp.getPaymoney());
						cs.setString(2, cp.getTrainbox());
						cs.setString(3, cp.getSeatNo());
						cs.setString(4, order.getSeatType());
						cs.setString(5, cp.getCpId());
						cs.setString(6, orderId);
						cs.addBatch();
					}
					cs.executeBatch();
				}

				// 开始发送通知给前台
				logger.info("{}准备通知", logid);
				sql = new StringBuffer();
				sql.append(
						"update cp_orderinfo_notify set notify_status = ?, notify_num=0, notify_next_time=DATE_ADD(now(), interval 20 second), modify_time=now() where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Notify.WAIT);
				cs.setString(2, orderId);
				cs.executeUpdate();

				// 根据订单号查询账号id
				int accountId = queryAccountIdByOrderNo(cn, cs, orderId);
				String accountUsername = "";
				int accountNum = 0;
				// 查询账号信息
				sql = new StringBuffer();
				sql.append("SELECT acc_username,contact_num FROM cp_accountinfo where acc_id=? limit 1");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, accountId);
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					accountUsername = rs.getString("acc_username");
					accountNum = rs.getInt("contact_num");
				}

				if (accountNum >= 99) {
					logger.info("{}账号:{}常用联系人>99，停用账号", logid, accountUsername);
					sql = new StringBuffer();
					sql.append(
							"update cp_accountinfo set acc_status=?,order_id='',opt_person='pay app',stop_reason=? where acc_id=?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Account.STATUS_STOP);
					cs.setString(2, "3");
					cs.setInt(3, accountId);
					cs.executeUpdate();
				} else {
					// 重发订单释放账号
					logger.info("{}重发订单释放账号:{}", logid, accountUsername);
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status=?, order_id='' where acc_id=?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Account.STATUS_FREE);
					cs.setInt(2, accountId);
					cs.executeUpdate();
				}

				// 添加操作账号日志
				String opt_logs = "order order释放账号";
				addAccLog(cn, cs, accountUsername, orderId, opt_logs);

				if (StringUtils.equals(result.getRetValue(), Result.NOPASS)) {
					// 更新乘客12306审核状态
					sql = new StringBuffer();
					sql.append(
							"UPDATE cp_orderinfo_cp SET check_status=? WHERE order_id=? AND user_name=? AND cert_no=? ");
					cs = cn.prepareStatement(sql.toString());
					// 身份证,成人,姓名，状态 0、已通过 1、待审核 2、未通过
					String[] passengers = result.getErrorInfo().split("\\|");
					for (String passenger : passengers) {
						if (StringUtils.isNotEmpty(passenger)) {
							String[] str = passenger.split(",");
							if (str.length < 4) {
								continue;
							}
							cs.setString(1, str[3]);
							cs.setString(2, order.getOrderId());
							cs.setString(3, str[2]);
							cs.setString(4, str[0]);
							// cs.executeUpdate();
							cs.addBatch();
						}
					}
					cs.executeBatch();
				}

				try {
					if (order.getChannel().equals(Order.CHANNEL_TC) && result.getErrorInfo().contains("身份信息涉嫌被他人冒用")) {
						/* 同程常旅变更，315标识 */
						String userName = StrUtil.findRegex(result.getErrorInfo(), "(?<=【).+(?=的身份信息涉嫌被他人冒用)");
						if (!StringUtils.isEmpty(userName)) {

							List<String> infos = findPassengerSimpleInfo(orderId, cn, cs);
							if (infos != null && infos.size() > 0) {
								List<String> cpInfos = new ArrayList<String>();
								for (String infoStr : infos) {
									if (infoStr.contains(userName)) {
										infoStr += "|315";
										cpInfos.add(infoStr);
										addPassengerNotify(cn, cs, orderId, result.getAccount().getUsername(), cpInfos,
												Order.CHANNEL_TC);
										break;
									}
								}
							}
						}
					}
				} catch (Exception e) {
					logger.info("{}身份冒用常旅信息异常", logid, e);
					e.printStackTrace();
				}

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
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新订单状态

				// 获取随机出票人员ID
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append(
						"select worker_id from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? order by spare_thread desc limit 1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.TYPE_MANUAL);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_PREPARED);
				rs = cs.executeQuery();
				String rand_worker_id = "";
				while (rs.next()) {
					rand_worker_id = rs.getString("worker_id");
				}

				// 获取出票人员，并锁定该人员
				StringBuffer sql = new StringBuffer();

				sql.append(
						"select worker_id, worker_name, worker_type,worker_ext from cp_workerinfo where worker_id=? for update");

				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Integer.valueOf(rand_worker_id));

				rs = cs.executeQuery();
				DBBeanUtil util = new DBBeanUtil();

				List<Worker> list = util.rs2List(rs, "com.l9e.train.po.Worker");

				Worker worker = null;
				if (list.size() > 0) {
					worker = list.get(0);
				} else {
					logger.warn("please add a worker!");
					return 2;
				}

				// 更新订单状态为人工预定
				sql = new StringBuffer();

				sql.append("update cp_orderinfo set worker_id=?, order_status=?, return_optlog=?,");

				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					sql.append("to_time=?,");
				}
				sql.append(" option_time=now() where order_id=? AND order_status=?");
				int index = 1;

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(index++, worker.getWorkerId());

				cs.setString(index++, Order.STATUS_ORDER_MANUAL);

				if (null == result) {
					cs.setString(index++, "03");// 无账号
				} else {
					cs.setString(index++, result.getReturnOptlog());
				}

				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					cs.setString(index++, order.getArrivetime());
				}

				cs.setString(index++, order.getOrderId());

				cs.setString(index, Order.STATUS_ORDER_ING);

				cs.executeUpdate();

				// start 更新员工处理量
				sql = new StringBuffer();

				sql.append(
						"update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(1, worker.getWorkerId());
				cs.executeUpdate();
				// end

				return 0;
			}
		});
	}

	/**
	 * 取消订单过多，暂时停用
	 * 
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws Exception
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsStop(final Order order, final Result result) throws Exception {
		final String uuid = order.getUuid();
		final String orderId = order.getOrderId();
		try {
			return dbbean.executeMethod(new ICallBack() {
				@Override
				public int execute(Connection cn, PreparedStatement cs) throws SQLException {
					// 更新订单状态
					// 根据订单号查询账号id
					logger.info(uuid + "根据订单号:" + orderId + "查询账号id");
					int acc_id = queryAccountIdByOrderNo(cn, cs, orderId);
					logger.info("stop acc_id:" + acc_id + " stop order failure:" + order.getOrderId());

					StringBuilder sql = new StringBuilder();

					sql.append("update cp_orderinfo set order_status=?, ");
					if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
						sql.append("to_time=?,");
					}
					sql.append("error_info=?, account_id=?, option_time=now(),opt_ren=? where order_id=?");
					cs = cn.prepareStatement(sql.toString());
					int index = 1;
					cs.setString(index++, Order.STATUS_ORDER_RESEND);

					if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
						cs.setString(index++, order.getArrivetime());
					}

					cs.setString(index++, result.getFailReason());
					cs.setInt(index++, 0);
					cs.setString(index++, "robot");
					cs.setString(index++, order.getOrderId());
					logger.info(uuid + "change status:" + Order.STATUS_ORDER_RESEND);

					cs.executeUpdate();

					// 账号停用
					sql.setLength(0);
					sql.append(
							"update cp_accountinfo set acc_status=?,order_id='',opt_person='pay_app',stop_reason=? where acc_id=?");

					cs = cn.prepareStatement(sql.toString());
					String errorInfo = result.getErrorInfo();
					if (StrUtil.containRegex(errorInfo, "手机.*核验") || StrUtil.containRegex(errorInfo, "联系人.*上限")
							|| errorInfo.contains("账号状态为：未通过") || StrUtil.containRegex(errorInfo, "重新.*注册")
							|| errorInfo.contains("未通过核验，不能添加常用联系人") || errorInfo.contains("身份证件号码填写是否正确")) {
						cs.setString(1, Account.STATUS_STOP);
					} else {
						cs.setString(1, Account.STATUS_TEMP_STOP);
					}
					if (StrUtil.containRegex(errorInfo, "手机.*核验")) {
						cs.setString(2, "7");
					} else if (StrUtil.containRegex(errorInfo, "联系人.*上限")) {
						cs.setString(2, "3");
					} else if (StrUtil.containRegex(errorInfo, "重新.*注册") || errorInfo.contains("账号状态为：未通过")
							|| errorInfo.contains("未通过核验，不能添加常用联系人") || errorInfo.contains("通过后即可在网上购票")
							|| errorInfo.contains("身份证件号码填写是否正确")) {
						cs.setString(2, "1");
					} else {
						cs.setString(2, "2");
					}
					cs.setInt(3, acc_id);

					cs.executeUpdate();

					// 添加操作账号日志
					logger.info(uuid + "stop 12306 account:" + acc_id);
					String opt_logs = "暂时停用账号";
					addAccLog(cn, cs, String.valueOf(acc_id), order.getOrderId(), opt_logs);
					logger.info(uuid + "orderIsStop success!!!");
					return 0;
				}
			});
		} catch (RepeatException | DatabaseException e) {
			logger.info(uuid + "【orderIsStop Exception】" + e.getClass().getSimpleName(), e);
			throw e;
		}
	}

	/**
	 * 帐号封停
	 * 
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws Exception
	 */
	public int orderIsEnd(final Order order, final Result result) throws Exception {
		final String uuid = order.getUuid();
		final String orderId = order.getOrderId();
		try {
			return dbbean.executeMethod(new ICallBack() {
				@Override
				public int execute(Connection cn, PreparedStatement cs) throws SQLException {
					// 更新订单状态
					// 根据订单号查询账号id
					logger.info(uuid + "根据订单号:" + orderId + "查询账号id");
					int acc_id = queryAccountIdByOrderNo(cn, cs, orderId);
					StringBuffer sql = new StringBuffer();
					sql.append("update cp_orderinfo set order_status=?, ");
					if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
						sql.append("to_time=?,");
					}
					sql.append("error_info=?, account_id=?, option_time=now(),opt_ren=? where order_id=?");
					cs = cn.prepareStatement(sql.toString());
					int index = 1;
					cs.setString(index++, Order.STATUS_ORDER_RESEND);

					if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
						cs.setString(index++, order.getArrivetime());
					}

					cs.setString(index++, result.getFailReason());
					cs.setInt(index++, 0);
					cs.setString(index++, "robot");
					cs.setString(index++, orderId);
					logger.info(uuid + "change status:" + Order.STATUS_ORDER_RESEND);
					cs.executeUpdate();

					// 账号封停用
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status=?,order_id=?,option_time=NOW(),");
					if ("WAITCHECK".equals(result.getErrorInfo())) {
						sql.append("opt_person='check_app',");
					} else if ("STOP".equals(result.getErrorInfo())) {
						sql.append("opt_person='stop_app',");
					}
					sql.append("stop_reason=? where acc_id=?");

					cs = cn.prepareStatement(sql.toString());
					index = 1;
					cs.setString(index++, Account.STATUS_STOP);
					cs.setString(index++, order.getOrderId());
					if ("WAITCHECK".equals(result.getErrorInfo())) {
						cs.setString(index++, "5");
					} else if ("STOP".equals(result.getErrorInfo())) {
						cs.setString(index++, "1");
					} else {
						cs.setString(index++, "1");
					}
					cs.setInt(index, acc_id);

					cs.executeUpdate();

					// 添加操作账号日志
					logger.info(uuid + "end 12306 account:" + acc_id);
					String opt_logs = "封停账号" + acc_id;
					addAccLog(cn, cs, String.valueOf(acc_id), order.getOrderId(), opt_logs);
					logger.info(uuid + "orderIsEnd success!!!");
					return 0;
				}
			});
		} catch (Exception e) {
			logger.info(uuid + "【orderIsEnd Exception】:" + e.getClass().getSimpleName(), e);
			throw e;
		}
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
	public int orderIsNopass(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新订单状态

				// 获取随机出票人员ID
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append(
						"select worker_id from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? order by spare_thread desc limit 1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.TYPE_MANUAL);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_PREPARED);
				rs = cs.executeQuery();
				String rand_worker_id = "";
				while (rs.next()) {
					rand_worker_id = rs.getString("worker_id");
				}

				// 获取出票人员，并锁定该人员
				StringBuffer sql = new StringBuffer();

				sql.append(
						"select worker_id, worker_name, worker_type,worker_ext from cp_workerinfo where worker_id=? for update");

				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Integer.valueOf(rand_worker_id));

				rs = cs.executeQuery();

				DBBeanUtil util = new DBBeanUtil();

				List<Worker> list = util.rs2List(rs, "com.l9e.train.po.Worker");

				Worker worker = null;
				if (list.size() > 0) {
					worker = list.get(0);
				} else {
					logger.warn("please add a worker!");
					return 2;
				}

				// 更新订单状态为人工预定
				sql = new StringBuffer();

				sql.append("update cp_orderinfo set worker_id=?, order_status=?, option_time=now() where order_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(1, worker.getWorkerId());

				cs.setString(2, Order.STATUS_ORDER_MANUAL);

				cs.setString(3, order.getOrderId());

				cs.executeUpdate();

				/*
				 * sql = new StringBuffer(); sql.append("INSERT INTO
				 * cp_orderinfo_ext (order_id,passenger_reason,create_time)
				 * VALUES (?,?,NOW()) "); cs =
				 * cn.prepareStatement(sql.toString());
				 * 
				 * cs.setString(1, order.getOrderId()); cs.setString(2,
				 * result.getErrorInfo()); cs.executeUpdate();
				 */

				// 更新乘客12306审核状态
				sql = new StringBuffer();
				sql.append("UPDATE cp_orderinfo_cp SET check_status=? WHERE order_id=? AND user_name=? AND cert_no=? ");
				cs = cn.prepareStatement(sql.toString());
				// 身份证,成人,姓名，状态 0、已通过 1、待审核 2、未通过
				String[] passengers = result.getErrorInfo().split("\\|");
				for (String passenger : passengers) {
					if (StringUtils.isNotEmpty(passenger)) {
						String[] str = passenger.split(",");
						if (str.length < 4) {
							continue;
						}
						cs.setString(1, str[3]);
						cs.setString(2, order.getOrderId());
						cs.setString(3, str[2]);
						cs.setString(4, str[0]);
						// cs.executeUpdate();
						cs.addBatch();
					}
				}
				cs.executeBatch();

				// start 更新员工处理量
				sql = new StringBuffer();

				sql.append(
						"update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(1, worker.getWorkerId());
				cs.executeUpdate();
				// end

				return 0;
			}
		});
	}

	/**
	 * 发送成功后订单的处理
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

				logger.info("workettype:" + result.getWorker().getWorkerType());
				StringBuffer sql = null;

				int isPayManual = 1;

				if (!StringUtils.equals("0", order.getLevel())) {// 级别较高的用户，直接支付，否则看配置进行支付
					isPayManual = 1;
				} else {
					// 读取是否需要机器支付的配置
					sql = new StringBuffer();

					sql.append("SELECT setting_value FROM hc_system_setting WHERE setting_name='robot_pay_ctrl'");

					cs = cn.prepareStatement(sql.toString());

					ResultSet rs = cs.executeQuery();

					while (rs.next()) {
						isPayManual = rs.getInt(1);
						logger.info("robot_pay_ctrl:" + isPayManual);
					}
					// 读取配置结束
				}

				// 录入车票相关信息
				sql = new StringBuffer();
				// TODO TAOKAI
				if ("tongcheng".equals(order.getChannel())) {
					sql.append(
							"update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?,seat_type=?, sub_outTicket_billno=?, modify_time=now() where cp_id=? and order_id=?");
				} else {
					sql.append(
							"update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?, sub_outTicket_billno=?, modify_time=now() where cp_id=? and order_id=?");
				}

				cs = cn.prepareStatement(sql.toString());

				logger.info("ordercps:" + result.getOrderCps().size());

				/*
				 * boolean isNotify = false; boolean isNotSeat = false;
				 */
				for (OrderCP cp : result.getOrderCps()) {
					logger.info("######### ticket-no:" + cp.getSubOutTicketBillNo());
					cs.setString(1, cp.getPaymoney());
					cs.setString(2, cp.getTrainbox());
					cs.setString(3, cp.getSeatNo());
					if ("tongcheng".equals(order.getChannel())) {
						logger.info("tongcheng cp_info for update,getSeatType:" + cp.getSeatType() + ",order_id"
								+ order.getOrderId());
						/*
						 * 机器人返回的结果 ["0"]="9",--商务座 ["1"]="P",--特等座
						 * ["2"]="M",--一等座 ["3"]="O",--二等座 ["4"]="6",--高级软座
						 * ["5"]="4",--软卧 ["6"]="3",--硬卧 ["7"]="2",--软座
						 * ["8"]="1",--硬座 ["9"]="1",--无座 动车无座为二等座 ["10"]="",--其他
						 * ["11"]="H",--包厢硬卧 ["20"]="F", --动卧 7 一等软座 8 二等软座 F 动卧
						 * A 高级动卧
						 */

						/**
						 * 系统席别类型 21动卧 22高级动卧 23一等软座 24二等软座
						 */

						if ("7".equals(cp.getSeatType())) {
							cs.setString(4, "23");
						} else if ("8".equals(cp.getSeatType())) {
							cs.setString(4, "24");
						} else if ("F".equalsIgnoreCase(cp.getSeatType())) {
							cs.setString(4, "21");
						} else if ("A".equals(cp.getSeatType())) {
							cs.setString(4, "22");
						} else {
							cs.setString(4, order.getSeatType());
						}
						cs.setString(5, cp.getSubOutTicketBillNo());
						cs.setString(6, cp.getCpId());
						cs.setString(7, order.getOrderId());
					} else {
						cs.setString(4, cp.getSubOutTicketBillNo());
						cs.setString(5, cp.getCpId());
						cs.setString(6, order.getOrderId());
					}
					cs.addBatch();
					//
					if (StringUtils.isEmpty(cp.getTrainbox()) && StringUtils.isEmpty(cp.getSeatNo())
							&& StringUtils.isEmpty(cp.getPaymoney())) {
						result.setManual(true);
					}
				}

				cs.executeBatch();

				if (!result.isManual()) {
					// 开始发送通知给前台
					sql = new StringBuffer();
					sql.append(
							"update cp_orderinfo_notify set notify_status = ?, notify_next_time=now(), modify_time=now() where order_id=? and notify_status=0");

					cs = cn.prepareStatement(sql.toString());

					cs.setString(1, Notify.WAIT);
					cs.setString(2, order.getOrderId());

					cs.executeUpdate();
					// 开始发送通知 end
				}

				logger.info("orderid:" + order.getOrderId() + " update cp_orderinfo");

				sql = new StringBuffer();
				sql.append("SELECT is_pay,account_id FROM cp_orderinfo WHERE order_id=?");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, order.getOrderId());
				String is_pay = "00";
				String account_id = "0";
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					is_pay = rs.getString(1);
					account_id = rs.getString(2);
				}

				if ("11".equals(is_pay)) {
					isPayManual = 2;
					// 修改账号为占座中
					sql = new StringBuffer();
					sql.append(
							"update cp_accountinfo set acc_status='66',book_num=book_num+1,option_time=now() where acc_id=?");
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, Integer.valueOf(account_id));
					cs.executeUpdate();
					logger.info("update account save a seat :" + order.getAccountId());
				} else {
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set book_num=book_num+1 where acc_id=?");
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, Integer.valueOf(account_id));
					cs.executeUpdate();
					logger.info(order.getOrderId() + "update account book_num+1 :" + order.getAccountId());
				}

				sql = new StringBuffer();
				sql.append("UPDATE cp_orderinfo SET ").append("order_status=?, ").append("out_ticket_time=now(), ")
						.append("out_ticket_billno=?, ").append("buy_money=?, ").append("seat_type=?, ")
						.append("option_time=now(), ").append("pay_limit_time=? ");

				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup())) {
					sql.append(",from_time=? ");
					if (StringUtils.isNotBlank(order.getArrivetime())) {
						sql.append(",to_time=?");
					}
					if ("Y".equalsIgnoreCase(result.getRefundOnline())) {
						sql.append(",error_info=?");
					}
				}
				sql.append(",manual_order=?");
				sql.append(" where order_id=?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				if (!result.isManual()) {// 是否需要人工核对
					switch (isPayManual) {
					case 0:
						cs.setString(index++, Order.STATUS_PAY_MANUAL);
						break;
					case 1:
						cs.setString(index++, Order.STATUS_PAY_START);
						break;
					case 2:
						cs.setString(index++, Order.STATUS_PAY_WAIT);
						break;
					default:
						cs.setString(index++, Order.STATUS_PAY_MANUAL);
						break;
					}
				} else {
					cs.setString(index++, Order.STATUS_ORDER_MANUAL);
				}

				cs.setString(index++, result.getSheId());
				cs.setString(index++, result.getPayMoney());
				cs.setString(index++, order.getSeatType());
				cs.setString(index++, order.getLoseTime());
				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup())) {
					String seattime = order.getSeattime();
					if (StringUtils.isNotBlank(seattime)) {
						cs.setString(index++, seattime);
					} else {
						cs.setString(index++, null);
					}

					if (StringUtils.isNotBlank(order.getArrivetime())) {
						cs.setString(index++, order.getArrivetime());
					}
					if ("Y".equalsIgnoreCase(result.getRefundOnline())) {
						cs.setString(index++, "12");
					}
				}
				cs.setString(index++, "00"); // 更新出票模式 00：12306出票
				cs.setString(index, order.getOrderId());
				cs.executeUpdate();

				logger.info("orderid:" + order.getOrderId() + " update cp_orderinfo_cp");

				return 0;
			}
		});

	}

	/**
	 * 发送失败后处理订单（携程）
	 * 
	 * @param order
	 * @param result
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsManualByCtrip(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新订单状态

				// 获取随机出票人员ID
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append(
						"select worker_id from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? order by spare_thread desc limit 1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.TYPE_MANUAL);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_PREPARED);
				rs = cs.executeQuery();
				String rand_worker_id = "";
				while (rs.next()) {
					rand_worker_id = rs.getString("worker_id");
				}

				// 获取出票人员，并锁定该人员
				StringBuffer sql = new StringBuffer();

				sql.append(
						"select worker_id, worker_name, worker_type,worker_ext from cp_workerinfo where worker_id=? for update");

				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Integer.valueOf(rand_worker_id));

				rs = cs.executeQuery();
				DBBeanUtil util = new DBBeanUtil();

				List<Worker> list = util.rs2List(rs, "com.l9e.train.po.Worker");

				Worker worker = null;
				if (list.size() > 0) {
					worker = list.get(0);
				} else {
					logger.warn("please add a worker!");
					return 2;
				}

				// 更新订单状态为人工预定
				sql = new StringBuffer();

				sql.append("update cp_orderinfo set worker_id=?, order_status=?, return_optlog=?,");

				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					sql.append("to_time=?,");
				}
				sql.append(" option_time=now() where order_id=? AND order_status=?");
				int index = 1;

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(index++, worker.getWorkerId());

				cs.setString(index++, Order.STATUS_ORDER_MANUAL);

				if (null == result) {
					cs.setString(index++, "03");// 无账号
				} else {
					cs.setString(index++, result.getReturnOptlog());
				}

				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					cs.setString(index++, order.getArrivetime());
				}

				cs.setString(index++, order.getOrderId());

				cs.setString(index, Order.STATUS_ORDER_ING);

				cs.executeUpdate();

				if (result.getCtripId() != null && !"".equals(result.getCtripId())) {

					// 更新ctrip信息表
					sql.setLength(0);
					sql.append(
							"update ctrip_orderinfo set ctrip_order_id=?,option_time=now(),opt_ren='train_app',acc_id=?  where order_id=?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, result.getCtripId());
					cs.setInt(2, result.getAccount().getId());
					cs.setString(3, order.getOrderId());
					cs.executeUpdate();
				}

				// start 更新员工处理量
				sql = new StringBuffer();

				sql.append(
						"update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(1, worker.getWorkerId());
				cs.executeUpdate();
				// end

				/**
				 * 当携程订单的处理结果为人工处理时，释放本携程账号，更新账号状态位空闲， 当返回错误信息中包含 预订成功支付异常
				 * 字段时不更新
				 */
				CtripAcc ctripAcc = result.getCtripAcc();
				logger.info("当携程订单转人工处理时，释放账号前，获取的携程账号实体 CtripAcc 为:" + ctripAcc);
				if (null != ctripAcc) {
					if (!result.getErrorInfo().contains("预订成功支付异常")) {
						sql = new StringBuffer();
						sql.append("update ctrip_accountinfo set opt_status=? where ctrip_id=? ");
						cs = cn.prepareStatement(sql.toString());

						cs.setInt(1, 0); // 0-空闲 1-使用中
						cs.setInt(2, Integer.parseInt(ctripAcc.getCtrip_id()));
						cs.executeUpdate();
						logger.info("当携程订单转人工处理时，释放账号，变更账号状态成功，账号ID为 :" + ctripAcc.getCtrip_id());
					}
				}

				return 0;
			}
		});
	}

	/**
	 * 发送成功后订单的处理
	 * 
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsSuccessByCtrip(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				logger.info("workettype:" + result.getWorker().getWorkerType());
				StringBuffer sql = null;

				// 更新支付金额 E开头订单号
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set buy_money=?,account_id=?,worker_id=?,manual_order='22'");
				// if(result.getPayMoney() != null &&
				// !"".equals(result.getPayMoney())) {
				// sql.append(",pay_money = ?");
				// }
				// 携程出票的保险金额
				if (result.getCtrip_bx_money() != null && !"".equals(result.getCtrip_bx_money())) {
					sql.append(",ctrip_bx_money = ?");
				}
				sql.append(" where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, result.getBuyMoney());
				// if(result.getPayMoney() != null &&
				// !"".equals(result.getPayMoney())) {
				// cs.setString(index++,result.getPayMoney());
				// }
				if (result.getCtrip_bx_money() != null && !"".equals(result.getCtrip_bx_money())) {
					cs.setString(index++, result.getCtrip_bx_money());
				}
				cs.setInt(index++, result.getAccount().getId());
				cs.setInt(index++, result.getWorker().getWorkerId());
				cs.setString(index++, order.getOrderId());
				int count = cs.executeUpdate();
				// System.out.println("update order_idfo
				// count"+count+""+result.getBuyMoney()+",,"+result.getSheId()+".."+order.getOrderId());
				/*
				 * boolean isNotify = false; boolean isNotSeat = false;
				 */

				// 更新ctrip信息表
				sql = new StringBuffer();
				sql.append(
						"update ctrip_orderinfo set  go_status='11',ctrip_order_id=?,option_time=now(),opt_ren='train_app',acc_id=?  where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, result.getCtripId());
				cs.setInt(2, result.getAccount().getId());
				cs.setString(3, order.getOrderId());
				cs.executeUpdate();

				logger.info("orderid:" + order.getOrderId() + " update cp_orderinfo");
				sql = new StringBuffer();
				sql.append("SELECT is_pay,account_id FROM cp_orderinfo WHERE order_id=?");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, order.getOrderId());
				String is_pay = "00";
				String account_id = "0";
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					is_pay = rs.getString(1);
					account_id = rs.getString(2);
				}

				sql = new StringBuffer();
				sql.append("update cp_accountinfo set book_num=book_num+1 where acc_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Integer.valueOf(account_id));
				cs.executeUpdate();
				logger.info(order.getOrderId() + "update account book_num+1 :" + order.getAccountId());

				logger.info("orderid:" + order.getOrderId() + " update cp_orderinfo_cp");

				return 0;
			}
		});

	}

	// 订单是否支付
	public int orderIsPay(final String order_id) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT is_pay,account_id FROM cp_orderinfo WHERE order_id=?");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, order_id);

				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					if ("00".equals(rs.getString(1))) {
						return 1;
					} else {
						return 0;
					}
				} else {
					return 1;
				}
			}
		});
	}

	/**
	 * 
	 * @param order
	 * @param result
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsCancel(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				logger.info("orderIsCancel begin orderid:" + order.getOrderId() + " update cp_orderinfo_cp");
				StringBuffer sql = null;
				// 录入车票相关信息
				sql = new StringBuffer();
				sql.append(
						"update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?, modify_time=now() where cp_id=? and order_id=?");

				cs = cn.prepareStatement(sql.toString());

				logger.info("ordercps:" + result.getOrderCps().size());

				for (int i = 0; i < result.getOrderCps().size(); i++) {
					OrderCP cp = result.getOrderCps().get(i);
					logger.info("cp:" + cp);
				}

				for (OrderCP cp : result.getOrderCps()) {
					cs.setString(1, cp.getPaymoney());
					cs.setString(2, cp.getTrainbox());
					cs.setString(3, cp.getSeatNo());
					cs.setString(4, cp.getCpId());
					cs.setString(5, order.getOrderId());
					cs.addBatch();
				}

				cs.executeBatch();

				logger.info("orderIsCancel orderid:" + order.getOrderId() + " update cp_orderinfo");

				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?,");
				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					sql.append("to_time=?,");
				}
				sql.append(
						"error_info=?,out_ticket_time=now(), out_ticket_billno=?, buy_money=?, option_time=now(),opt_ren=? where order_id=?");

				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, Order.STATUS_CANCEL_START);
				if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())) {
					cs.setString(index++, order.getArrivetime());
				}
				cs.setString(index++, result.getFailReason());
				cs.setString(index++, result.getSheId());
				cs.setString(index++, result.getPayMoney());
				cs.setString(index++, "robot");
				cs.setString(index, order.getOrderId());
				cs.executeUpdate();

				logger.info("orderIsCancel end orderid:" + order.getOrderId() + " update cp_orderinfo");

				return 0;
			}

		});
	}

	/**
	 * 排队订单处理
	 * 
	 * @param order
	 * @param result
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsQueue(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection conn, PreparedStatement ps) throws SQLException {
				StringBuilder sql = new StringBuilder();

				String orderId = order.getOrderId();
				/* 获取等待时间 */
				String timeStr = StrUtil.findRegex(result.getErrorInfo(), "\\d+");
				Integer time = null;

				try {
					time = Integer.valueOf(timeStr);
				} catch (NumberFormatException e) {
					logger.info("queue order waitTime invalid : " + timeStr);
					return 1;
				}

				if (time <= 0)
					return 1;
				if (time > 300)
					time = 300;
				if (time < 10)
					time = 10;

				logger.info("order is queue, orderId : " + orderId);

				/* 更新订单为排队中状态 */
				sql.append("update cp_orderinfo set order_status = ?").append(" where order_id = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, Order.STATUS_ORDER_QUEUE);
				ps.setString(2, orderId);

				ps.executeUpdate();
				logger.info("update cp_orderinfo order_status to queue, orderId : " + orderId);

				/* 将排队订单记录到cp_orderinfo_queue表中等待重新尝试 */
				sql.setLength(0);
				sql.append("insert into cp_orderinfo_queue(order_id, resend_num, resend_time, create_time)")
						.append(" values (?, 1, date_add(now(), interval ? second), now())")
						.append(" on duplicate key update ")
						.append(" resend_num = resend_num + 1,queue_status = ?,resend_time = date_add(now(), interval ? second)");

				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, orderId);
				ps.setInt(2, time / 2);
				ps.setString(3, "00");
				ps.setInt(4, time / 2);

				ps.executeUpdate();
				logger.info("insert cp_orderinfo_queue end, orderId : " + orderId + ", time : " + time);

				return 0;
			}
		});
	}

	/**
	 * 重新获取匹配处理人和账号信息
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int reGetAccountAndWorkerBy(final Map<String, String> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				String orderId = map.get("order_id");

				StringBuffer sql = new StringBuffer();
				logger.info(map.get("acc_id"));
				sql.append("select acc_id, acc_username, acc_password from cp_accountinfo where acc_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Integer.valueOf(map.get("acc_id")));

				// cs.setString(2, Account.DOWNING);
				rs = cs.executeQuery();
				DBBeanUtil util = new DBBeanUtil();
				List<Account> alist = util.rs2List(rs, "com.l9e.train.po.Account");
				logger.info(alist.size() + "----------" + map.get("order_id"));
				if (alist.size() > 0) {// 获取到账号
					account = alist.get(0);
					logger.info("step2: orderByAccount account:" + account.getUsername() + " password:"
							+ account.getPassword());
				} else {
					return 1;
				}
				// start 更新账号状态为正在下单
				logger.info("start update account accountid:" + account.getId() + " orderid:" + orderId);
				sql = new StringBuffer();
				sql.append(
						"update cp_accountinfo set acc_status=?, order_id=? , option_time=now(),opt_person='order app'  where acc_id=?");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Account.STATUS_PLACING_ORDER);
				cs.setString(2, orderId);
				cs.setInt(3, account.getId());
				cs.executeUpdate();
				logger.info("end update account accountid:" + account.getId() + " orderid:" + orderId);
				// end

				logger.info("resend lock account");
				String opt_logs = "app order锁定账号(resend)";
				addAccLog(cn, cs, account.getUsername(), orderId, opt_logs);

				/* 重发关系匹配机器人 */
				relationWorker(account.getUsername(), cn, cs);

				if (getWorker() == null) {

					// 获取预订机器人
					worker = App.workerService.getWorkerByType(Worker.TYPE_BOOK);
				}

				if (null == worker) {
					return 0;
				}
				StringBuffer log_work = new StringBuffer();
				log_work.append(" worker.getWorkerName()").append(worker.getWorkerName()).append(" work_url")
						.append(worker.getWorkerExt());
				logger.info(log_work.toString());

				// start 更新订单出票人员和账号信息
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set account_id=?, worker_id=?, option_time=now() where order_id=?");

				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, account.getId());
				cs.setInt(2, worker.getWorkerId());
				cs.setString(3, orderId);

				cs.executeUpdate();
				logger.info("update orderinfo account:" + account.getId() + " workerid:" + worker.getWorkerId());
				// end

				// start 更新员工处理量
				// sql = new StringBuffer();
				// sql.append("update cp_workerinfo set
				// worker_status=?,order_num=order_num+1,spare_thread=spare_thread-1,work_num=work_num+1,work_time=NOW()
				// where worker_id=?");
				// cs = cn.prepareStatement(sql.toString());
				//
				// cs.setString(1, Worker.STATUS_WORKING);
				// cs.setString(2, worker.getWorkerId());
				// cs.executeUpdate();
				// // end
				// worker.setWorkerStatus(Worker.STATUS_WORKING);

				return 0;

			}
		});
	}

	/**
	 * 重新获取匹配处理人和账号信息
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int reGetAccountAndWorkerAndCtripAccBy(final Map<String, String> map)
			throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				String orderId = map.get("order_id");
				String channel = map.get("channel");

				StringBuilder sql = new StringBuilder();
				// 获取出票机器人
				int num = 0;
				try {
					num = getCtripBookWorkInfo();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}

				StringBuffer log_work = new StringBuffer();
				if (null == worker) {
					logger.info("ctrip worker is null");
					return 0;
				}

				Integer accountId = Integer.valueOf(map.get("acc_id"));
				account = App.accountService.getOrderAccount(accountId);

				if (account == null)
					return 1;

				// start 更新账号状态为正在下单
				logger.info("start update account accountid:" + account.getId() + " orderid:" + orderId);
				sql.setLength(0);
				sql.append(
						"update cp_accountinfo set acc_status=?, order_id=? , option_time=now(),opt_person='order app'  where acc_id=?");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Account.STATUS_PLACING_ORDER);
				cs.setString(2, orderId);
				cs.setInt(3, account.getId());
				cs.executeUpdate();
				logger.info("end update account accountid:" + account.getId() + " orderid:" + orderId);
				// end

				logger.info("resend lock account");
				String opt_logs = "app order锁定账号(resend)";
				addAccLog(cn, cs, account.getUsername(), orderId, opt_logs);

				// start 更新订单出票人员和账号信息
				sql.setLength(0);
				sql.append("update cp_orderinfo set account_id=?, worker_id=?, option_time=now() where order_id=?");

				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, account.getId());
				cs.setInt(2, worker.getWorkerId());
				cs.setString(3, orderId);

				cs.executeUpdate();
				logger.info("update orderinfo account:" + account.getId() + " workerid:" + worker.getWorkerId());
				// end

				// start 更新员工处理量
				sql.setLength(0);
				sql.append(
						"update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, worker.getWorkerId());
				cs.executeUpdate();
				// end

				/** 查询携程账号 */
				sql.setLength(0);
				sql.append("select ctrip_id from ctrip_orderinfo where order_id=?");
				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, orderId);
				rs = cs.executeQuery();

				if (rs.next()) {
					int ctrip_id = rs.getInt(1);
					sql.setLength(0);
					sql.append(
							"select ctrip_id,ctrip_name,ctrip_password,ctrip_phone,ctrip_username,pay_password, cookie, cid, auth, sauth,balance,acc_degree,order_succ_time,opt_status from ctrip_accountinfo where ctrip_id=?");
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, ctrip_id);
					// System.out.println(ctrip_id);
					// util = new DBBeanUtil();
					rs = cs.executeQuery();
					// List<CtripAcc> ctripAccList = util.rs2List(rs,
					// "com.l9e.train.po.CtripAcc");
					if (rs.next()) {// 获取到账号
						ctripAcc = new CtripAcc();
						ctripAcc.setCtrip_id(rs.getString(1));
						ctripAcc.setCtrip_name(rs.getString(2));
						ctripAcc.setCtrip_password(rs.getString(3));
						ctripAcc.setCtrip_phone(rs.getString(4));
						ctripAcc.setCtrip_username(rs.getString(5));
						ctripAcc.setPay_password(rs.getString(6));
						ctripAcc.setCookie(rs.getString(7));
						ctripAcc.setCid(rs.getString(8));
						ctripAcc.setAuth(rs.getString(9));
						ctripAcc.setSauth(rs.getString(10));
						ctripAcc.setBalance(rs.getDouble(11));
						ctripAcc.setAcc_degree(rs.getInt(12));
						ctripAcc.setOrder_succ_time(rs.getInt(13));
						ctripAcc.setOpt_status(rs.getInt(14));
						logger.info("step3: ctrip get ctripAcc:" + ctripAcc.getCtrip_name() + ",password:"
								+ ctripAcc.getCtrip_password() + ",balance:" + ctripAcc.getBalance() + ",acc_degree:"
								+ ctripAcc.getAcc_degree() + ",order_succ_time:" + ctripAcc.getOrder_succ_time());
					} else {
						return 1;
					}

				} else {
					return 1;
				}

				return 0;

			}
		});
	}

	/**
	 * 0:没有机器人， 1:没有账号，2:成功
	 * 
	 * @author: taoka
	 * @date: 2018年3月9日 下午4:02:57
	 * @param order
	 * @param scriptWeight
	 * @param logid
	 * @return int
	 */
	public int selectAccountAndWorker(Order order, String scriptWeight, String logid) {
		logger.info("{}开始获取账号信息########", logid);
		String orderStatus = order.getOrderStatus();
		String orderId = order.getOrderId();
		if (orderStatus.equals(Order.STATUS_ORDER_START)) {
			/**
			 * 在此先判断订单中的accountId是否为空 对订单中自带12306账号的处理，如果订单中没带12306的账号密码，
			 * 则跳过下面一步的流程，直接走 按乘客证件匹配账号的流程
			 */
			Integer accountId = order.getAccountId();
			Integer accountFromWay = order.getAccountFromWay();
			logger.info("{}accountFromWay:{}", logid, accountFromWay);// 0:公司账号,1:乘客自有
			logger.info("{}accountId:{}", logid, accountId);
			if (null != accountId && 0 != accountId && accountFromWay != null && accountFromWay == 1) {
				logger.info("{}自有账号订单，根据账号ID查询账号...", logid);
				account = App.accountService.getOrderAccount(accountId); // 查询账号
			} else {
				/* 按乘客证件匹配 */
				logger.info("{}获取乘客信息...", logid);
				List<Passenger> passengers = App.orderService.getPassengersByOrderId(orderId);
				// 多人匹配模式
				logger.info("{}根据乘客信息获取账号...", logid);
				if (passengers != null && passengers.size() > 0) {
					// 把订单下面的多个身份证号用","分隔，拼成一个全新的字符串。
					StringBuilder passportNos = new StringBuilder();
					String passportNo = null;
					for (int i = 0; i < passengers.size(); i++) {
						passportNo = passengers.get(i).getPassportNo();
						passportNos.append(passportNo);
						passportNos.append(",");
					}
					account = App.accountService.filterAccount(passportNos.toString());
					if (account != null) {
						// 如果白名单匹配成功了，则更新白名单表的匹配次数和时间
						logger.info("{}白名单匹配成功了,更新白名单表的匹配次数和时间...", logid);
						try {
							updateMatchingNumAndTime(order, account, passengers);
						} catch (Exception e) {
							logger.info("{}【系统异常】:更新白名单表的匹配次数和时间发生异常", logid, e);
						}
					}
				}

				/* 按渠道获取账号 */
				int size = passengers.size();
				if (account == null) {
					logger.info("{}白名单匹配失败,获取队列[{}]中的账号...", logid, size);
					// 取账号联系人个数+要添加的联系人个数等于15的账号
					account = App.accountService.getChannelAccount(Order.CHANNEL_19E, size);
					if (account == null) {
						logger.info("{}队列账号获取失败,获取队列[10]中的账号...", logid);
						// 取账号联系人个数小于10的账号
						account = App.accountService.getChannelAccount(Order.CHANNEL_19E, 0);
					}
				}
			}
		} else if (orderStatus.equals(Order.STATUS_ORDER_RESEND)) {
			/* 订单重发使用之前的账号，按id获取 */
			logger.info("{}重发订单,账号不变...", logid);
			Integer accountId = order.getAccountId();
			if (accountId == null || accountId == 0) {
				logger.info("{}账号ID为空,重新获取账号...", logid);

				/* 按乘客证件匹配 */
				logger.info("{}获取乘客信息...", logid);
				List<Passenger> passengers = App.orderService.getPassengersByOrderId(orderId);
				int size = passengers.size();

				/* 按渠道获取账号 */
				logger.info("{}获取队列[{}]中的账号...", logid, size);
				account = App.accountService.getChannelAccount(Order.CHANNEL_19E, size);
				if (account == null) {
					logger.info("{}队列账号获取失败,获取队列[10]中的账号...");
					// 取账号联系人个数小于10的账号
					account = App.accountService.getChannelAccount(Order.CHANNEL_19E, 0);
				}
			} else {
				logger.info("{}根据账号ID获取原账号...");
				account = App.accountService.getOrderAccount(accountId);
			}
		} else {
			return 1;
		}
		if (account == null) {
			logger.info("{}获取账号为空...", logid);
			return 1;
		}

		// 匹配机器人
		logger.info("{}开始获取机器人信息########", logid);
		String username = account.getUsername();
		Object cacheValue = null;
		try {
			cacheValue = App.redisDao.getCacheVal(username);
		} catch (Exception e) {
			logger.info("{}【系统异常】:getCacheVal发生异常", logid, e);
		}
		logger.info("{}cacheValue:{}", logid, cacheValue);

		/* 获取机器人 */
		if (cacheValue != null) {
			try {
				Integer workerId = (Integer) cacheValue;
				logger.info("根据cacheID获取机器人信息...", logid);
				worker = App.workerService.getWorkerById(workerId);
			} catch (Exception e) {
				logger.info("{}【系统异常】:getWorkerById()发生异常", logid, e);
			}
		}

		/**
		 * 在此先判断机器的权重，如果权重为lua，则原先的流程不变，机器人还是从队列里取；
		 * 如果权重为java，则直接扫库随机取一个java脚本的机器人。
		 */
		if (worker == null) {
			logger.info("{}cache机器人信息为空,查询DB获取机器人...", logid);
			if (WorkerWeight.WEIGHT_LUA.equals(scriptWeight)) {
				worker = App.workerService.getWorkerByType(Worker.TYPE_BOOK);
			} else if (WorkerWeight.WEIGHT_JAVA.equals(scriptWeight)) {
				worker = App.workerService.getOneJavaWorker();
			}
		}

		// 如果还没有获取到机器人，则释放账号
		if (worker == null) {
			logger.info("{}最终获取机器人为空,释放账号...", logid);
			App.accountService.releaseAcoount(account.getId());
			return 0;
		}
		order.setAcc_id(account.getId());
		order.setAccountId(account.getId());
		order.setWorkerId(worker.getWorkerId());

		// 订单关联关系建立
		try {
			logger.info("{}订单、账号、机器人关联关系建立...", logid);
			OrderVo orderVo = new OrderVo();
			orderVo.setId(order.getOrderId());
			orderVo.setAccountId(account.getId());
			orderVo.setWorkerId(worker.getWorkerId());
			App.orderService.updateOrder(orderVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 2;
	}

	/**
	 * 给状态为“订购开始”的订单匹配处理人和账号信息
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int selectOrderAccountAndWorkerAndCtripAccBy(final Map<String, String> map, final Order order)
			throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				String orderId = map.get("order_id");
				String channel = map.get("channel");
				String manualOrder = map.get("manual_order");// 携程出票模式：
																// 22:携程app出票
																// 33:携程PC出票
				// 获取出票机器人
				int num = 0;
				try {
					num = getCtripBookWorkInfo();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}

				StringBuffer log_work = new StringBuffer();
				if (null == worker) {
					logger.info("ctrip worker is null");
					return 0;
				}
				// 获取携程账号
				// 获取出票机器人
				int ctrip_acc_num = 0;
				try {
					if ("22".equals(manualOrder)) {
						ctripAcc = order.getCtripAcc(); // 获取携程账号
					} else if ("33".equals(manualOrder)) {
						ctrip_acc_num = getCtripAccInfo();
					}
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
				if (null == ctripAcc) {
					logger.info("ctrip account is null");
					return 0;
				}
				StringBuffer ctripWorkerAndAcclog = new StringBuffer();
				ctripWorkerAndAcclog.append("ctrip Worker and ctripAcc log,")
						.append("workerID:" + worker.getWorkerId() + ",workerEXT" + worker.getWorkerExt() + ",workerNum"
								+ num)
						.append("ctripAccPhone" + ctripAcc.getCtrip_phone() + ",ctripAccNum" + ctrip_acc_num);
				// 获取出票人员，并锁定该人员
				StringBuffer sql = new StringBuffer();

				// 获取出票账号
				/* 按乘客证件匹配 */
				List<Passenger> passengers = App.orderService.getPassengersByOrderId(orderId);

				// 多人匹配模式
				if (passengers != null && passengers.size() > 0) {

					// 把订单下面的多个身份证号用","分隔，拼成一个全新的字符串。
					StringBuilder passportNos = new StringBuilder();
					String passportNo = null;
					for (int i = 0; i < passengers.size(); i++) {
						passportNo = passengers.get(i).getPassportNo();
						passportNos.append(passportNo);
						passportNos.append(",");
					}
					account = App.accountService.filterAccount(passportNos.toString());
				}
				// 测试用
				// account =new Account();
				// account.setId(1477266);
				// account.setUsername("aiLFfOlfBi");
				// account.setPassword("aiLFfOlfBi_");

				/* 按渠道获取账号 */
				if (account == null) {
					account = App.accountService.getChannelAccount(Order.CHANNEL_19E, passengers.size());
				}
				// 未获取到账号
				if (account == null) {
					return 1;
				}

				logger.info("send lock account");
				String opt_logs = "app order锁定账号(send)";
				addAccLog(cn, cs, account.getUsername(), orderId, opt_logs);

				// start 更新订单出票人员和账号信息
				logger.info("start update orderinfo account:" + account.getId() + " workerid:" + worker.getWorkerId()
						+ " orderid:" + orderId);
				sql = new StringBuffer();

				sql.append(
						"update cp_orderinfo set account_id=?, worker_id=?, option_time=now(),manual_order='22' where order_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(1, account.getId());
				cs.setInt(2, worker.getWorkerId());
				cs.setString(3, orderId);

				cs.executeUpdate();

				logger.info("end update orderinfo account:" + account.getId() + " workerid:" + worker.getWorkerId()
						+ " orderid:" + orderId);
				// end

				// start 更新员工处理量
				logger.info("start update workerinfo workerid:" + worker.getWorkerId() + " orderid:" + orderId);
				sql = new StringBuffer();

				sql.append(
						"update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(1, worker.getWorkerId());
				cs.executeUpdate();
				logger.info("end update workerinfo workerid:" + worker.getWorkerId() + " orderid:" + orderId);
				// end

				// 插入 同城信息表 更新同城信息表
				ResultSet rs = null;
				sql = new StringBuffer();

				sql.append("select count(*) num from ctrip_orderinfo where order_id=?");
				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, orderId);
				rs = cs.executeQuery();
				int count = 0;
				while (rs.next()) {
					count = rs.getInt("num");
				}
				if (count > 0) {
					// 更新操作
					sql = new StringBuffer();

					sql.append(
							"update ctrip_orderinfo set go_status='00',option_time=now(),acc_id=?,ctrip_id=? where order_id=?");

					cs = cn.prepareStatement(sql.toString());

					cs.setInt(1, account.getId());
					cs.setString(2, ctripAcc.getCtrip_id());
					cs.setString(3, orderId);
					cs.executeUpdate();
				} else {
					// 插入操作
					sql = new StringBuffer();

					sql.append(
							"insert into ctrip_orderinfo(order_id,ctrip_id,go_status,create_time,option_time,opt_ren,acc_id) values(?, ?,'00',now(),now(),'train_app', ?)");

					cs = cn.prepareStatement(sql.toString());

					cs.setString(1, orderId);
					cs.setString(2, ctripAcc.getCtrip_id());
					cs.setInt(3, account.getId());
					cs.executeUpdate();

				}
				// end

				return 0;

			}
		});
	}

	/**
	 * 获取携程预订机器人
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int getCtripBookWorkInfo() throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 获取随机出票人员ID
				Integer count = 1;
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append(
						"select count(1) num from cp_workerinfo where worker_type=? and worker_status not in(?,?) ");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.TYPE_CTRIP);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_PREPARED);

				rs = cs.executeQuery();
				while (rs.next()) {
					count = rs.getInt("num");
				}
				int num = WorkIdNum.getctripWorkerSelectNum(count);

				sql_rand = new StringBuffer();
				sql_rand.append("select worker_id,worker_name, worker_type,worker_ext ")
						.append("from cp_workerinfo where worker_type=? and worker_status not in(?,?) ")
						.append("  order by worker_id limit ?,1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.TYPE_CTRIP);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_PREPARED);
				cs.setInt(4, num - 1);
				rs = cs.executeQuery();
				if (rs.next()) {
					worker = new Worker();
					// 正常分配到的机器人参数
					worker.setWorkerId(rs.getInt(1));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(rs.getInt(3));
					worker.setWorkerExt(rs.getString(4));
				}
				return num;
			}
		});
	}

	/**
	 * 如果携程订单中有非成人票和非一，二代身份证的情况，则出票模式改为12306出票 则执行账号解锁操作
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int updateCtripAccStatus(final String orderID) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				// 先通过订单ID查询出携程账号ID
				StringBuffer querySql = new StringBuffer();

				querySql.append("select order_id,ctrip_id,acc_id from ctrip_orderinfo where order_id=? ");
				ps = cn.prepareStatement(querySql.toString());
				ps.setString(1, orderID);
				rs = ps.executeQuery();

				Integer ctripID = null; // 携程账号ID
				while (rs.next()) {
					ctripID = rs.getInt("ctrip_id");
				}
				logger.info("如果携程订单中有儿童票，则出票模式改为12306出票，获取的[携程账号]ctripID = " + ctripID);

				if (ctripID != null && ctripID != 0) {
					// 如果携程订单中有非成人票和非一，二代身份证的情况，则出票模式改为12306出票，因为前面已经执行了携程账号锁定操作，所以在此需要执行账号解锁
					StringBuffer updateSql = new StringBuffer();
					updateSql.append("UPDATE ctrip_accountinfo SET opt_status=? WHERE ctrip_id=? ");
					ps = cn.prepareStatement(updateSql.toString());
					ps.setInt(1, CtripAcc.OPT_STATUS_0);// 0-空闲 1-使用中
					ps.setInt(2, ctripID);

					ps.executeUpdate();
					logger.info("如果携程订单中有儿童票，则出票模式改为12306出票，携程账号释放成功，订单号为: " + orderID);
				}
				return 0;
			}
		});
	}

	/**
	 * 携程app端出票获取携程账号
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int getCtripAccInfo(final Map<String, String> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				String orderId = map.get("order_id");
				String pay_money = map.get("pay_money");
				String ctrip_balance = map.get("ctrip_balance");
				String ctrip_order_num_min = map.get("ctrip_order_num_min");
				String ctrip_order_num_max = map.get("ctrip_order_num_max");

				// 获取随机出票人员ID
				Integer count = 1;
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append(
						"SELECT COUNT(1) num FROM ctrip_accountinfo WHERE ctrip_status=? AND opt_status=? AND balance<=? AND balance>=? AND order_succ_time<=? AND order_succ_time>=? ");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setString(1, CtripAcc.STATUS_WORKING);// 启用
				cs.setInt(2, CtripAcc.OPT_STATUS_0);// 0-空闲 1- 使用中
				cs.setDouble(3, (Double.parseDouble(pay_money) + Double.parseDouble(ctrip_balance)));// 余额<=(订单金额+10)
				cs.setDouble(4, Double.parseDouble(pay_money));// 余额>=订单金额
				cs.setInt(5, Integer.parseInt(ctrip_order_num_max));// 下单成功次数<=携程已出票订单个数最大值
				cs.setInt(6, Integer.parseInt(ctrip_order_num_min));// 下单成功次数>=携程已出票订单个数最小值

				rs = cs.executeQuery();
				while (rs.next()) {
					count = rs.getInt("num");
				}
				logger.info("[携程账号]count=" + count + ", 参数map=" + map);

				if (count > 0) {
					// int num = WorkIdNum.getNextCtripNum(count);

					sql_rand = new StringBuffer();
					sql_rand.append(
							"select ctrip_id,ctrip_name,ctrip_password,ctrip_phone,ctrip_username,pay_password, cookie, cid, auth, sauth,balance,acc_degree,order_succ_time,opt_status ")
							.append("from ctrip_accountinfo where ctrip_status=?  ")
							.append(" AND opt_status=? AND balance<=? AND balance>=? AND order_succ_time<=? AND order_succ_time>=? ")
							.append(" order by rand(),order_succ_time  limit ?");
					cs = cn.prepareStatement(sql_rand.toString());
					cs.setString(1, CtripAcc.STATUS_WORKING);// 启用
					cs.setInt(2, CtripAcc.OPT_STATUS_0);// 0-空闲 1- 使用中
					cs.setDouble(3, (Double.parseDouble(pay_money) + Double.parseDouble(ctrip_balance)));// 余额<=(订单金额+10)
					cs.setDouble(4, Double.parseDouble(pay_money));// 余额>=订单金额
					cs.setInt(5, Integer.parseInt(ctrip_order_num_max));// 下单成功次数<=携程已出票订单个数最大值
					cs.setInt(6, Integer.parseInt(ctrip_order_num_min));// 下单成功次数>=携程已出票订单个数最小值
					cs.setInt(7, 1);
					rs = cs.executeQuery();
					if (rs.next()) {
						ctripAcc = new CtripAcc();
						ctripAcc.setCtrip_id(rs.getString(1));
						ctripAcc.setCtrip_name(rs.getString(2));
						ctripAcc.setCtrip_password(rs.getString(3));
						ctripAcc.setCtrip_phone(rs.getString(4));
						ctripAcc.setCtrip_username(rs.getString(5));
						ctripAcc.setPay_password(rs.getString(6));
						ctripAcc.setCookie(rs.getString(7));
						ctripAcc.setCid(rs.getString(8));
						ctripAcc.setAuth(rs.getString(9));
						ctripAcc.setSauth(rs.getString(10));
						ctripAcc.setBalance(rs.getDouble(11));
						ctripAcc.setAcc_degree(rs.getInt(12));
						ctripAcc.setOrder_succ_time(rs.getInt(13));
						ctripAcc.setOpt_status(rs.getInt(14));
						logger.info("[携程获得账号]余额和订单个数----" + ctripAcc.getCtrip_name() + ",password:"
								+ ctripAcc.getCtrip_password() + ",balance:" + ctripAcc.getBalance() + ",acc_degree:"
								+ ctripAcc.getAcc_degree() + ",order_succ_time:" + ctripAcc.getOrder_succ_time());

						// 更新账号状态opt_status为“使用中1”
						sql_rand = new StringBuffer();
						sql_rand.append("UPDATE ctrip_accountinfo SET opt_status=? WHERE ctrip_id=? ");
						cs = cn.prepareStatement(sql_rand.toString());
						cs.setInt(1, CtripAcc.OPT_STATUS_1);// 0-空闲 1- 使用中
						cs.setString(2, ctripAcc.getCtrip_id());
						cs.executeUpdate();
						logger.info("根据余额获取携程账号后，账号锁定成功，订单号为: " + orderId);
					}
					return 1;
				} else {// 没有符合条件的账号
						// 查看订单金额属于什么等级礼品卡
					int acc_degree = 0;
					sql_rand = new StringBuffer();
					sql_rand.append(
							"SELECT acc_degree FROM ctrip_amountarea_conf WHERE limit_begin<=? AND limit_end>=? ");
					cs = cn.prepareStatement(sql_rand.toString());
					cs.setDouble(1, Double.parseDouble(pay_money));// limit_begin<=订单金额
					cs.setDouble(2, Double.parseDouble(pay_money));// limit_end>=订单金额

					rs = cs.executeQuery();
					while (rs.next()) {
						acc_degree = rs.getInt("acc_degree");
					}

					// 根据礼品卡等级acc_degree去选取账号
					int countNum = 0;
					sql_rand = new StringBuffer();
					sql_rand.append("select count(1) count ").append("from ctrip_accountinfo where ctrip_status=?  ")
							.append(" AND opt_status=? AND acc_degree=? ");
					cs = cn.prepareStatement(sql_rand.toString());
					cs.setString(1, CtripAcc.STATUS_WORKING);// 启用
					cs.setInt(2, CtripAcc.OPT_STATUS_0);// 0-空闲 1- 使用中
					cs.setInt(3, acc_degree);// 礼品卡等级acc_degree

					rs = cs.executeQuery();
					if (rs.next()) {
						countNum = rs.getInt("count");
					}

					logger.info("[携程账号]count=" + countNum + ", 参数账号等级acc_degree=" + acc_degree);

					if (countNum > 0) {
						sql_rand = new StringBuffer();
						sql_rand.append(
								"select ctrip_id,ctrip_name,ctrip_password,ctrip_phone,ctrip_username,pay_password, cookie, cid, auth, sauth,balance,acc_degree,order_succ_time,opt_status ")
								.append("from ctrip_accountinfo where ctrip_status=?  ")
								.append(" AND opt_status=? AND acc_degree=? AND balance > ?")
								.append(" order by rand(),order_succ_time limit ?");
						cs = cn.prepareStatement(sql_rand.toString());
						cs.setString(1, CtripAcc.STATUS_WORKING);// 启用
						cs.setInt(2, CtripAcc.OPT_STATUS_0);// 0-空闲 1- 使用中
						cs.setInt(3, acc_degree);// 礼品卡等级acc_degree
						cs.setDouble(4, Double.parseDouble(pay_money));
						// cs.setInt(4,
						// Integer.parseInt(ctrip_order_num_min));//下单成功次数>=携程已出票订单个数最小值
						cs.setInt(5, 1);
						rs = cs.executeQuery();
						if (rs.next()) {
							ctripAcc = new CtripAcc();
							ctripAcc.setCtrip_id(rs.getString(1));
							ctripAcc.setCtrip_name(rs.getString(2));
							ctripAcc.setCtrip_password(rs.getString(3));
							ctripAcc.setCtrip_phone(rs.getString(4));
							ctripAcc.setCtrip_username(rs.getString(5));
							ctripAcc.setPay_password(rs.getString(6));
							ctripAcc.setCookie(rs.getString(7));
							ctripAcc.setCid(rs.getString(8));
							ctripAcc.setAuth(rs.getString(9));
							ctripAcc.setSauth(rs.getString(10));
							ctripAcc.setBalance(rs.getDouble(11));
							ctripAcc.setAcc_degree(rs.getInt(12));
							ctripAcc.setOrder_succ_time(rs.getInt(13));
							ctripAcc.setOpt_status(rs.getInt(14));
							logger.info("[携程获得账号]acc_degree----ctrip get ctripAcc:" + ctripAcc.getCtrip_name()
									+ ",password:" + ctripAcc.getCtrip_password() + ",balance:" + ctripAcc.getBalance()
									+ ",acc_degree:" + ctripAcc.getAcc_degree() + ",order_succ_time:"
									+ ctripAcc.getOrder_succ_time());

							// 更新账号状态opt_status为“使用中1”
							sql_rand = new StringBuffer();
							sql_rand.append("UPDATE ctrip_accountinfo SET opt_status=? WHERE ctrip_id=? ");
							cs = cn.prepareStatement(sql_rand.toString());
							cs.setInt(1, CtripAcc.OPT_STATUS_1);// 0-空闲 1- 使用中
							cs.setString(2, ctripAcc.getCtrip_id());
							cs.executeUpdate();
							logger.info("根据礼品卡等级获取携程账号后，账号锁定成功，订单号为: " + orderId);
						}
						return 1;
					} else {
						logger.info("[携程账号]no ctrip account~~~");
						logger.info("[携程账号]no ctrip account~~~" + ctripAcc);
						ctripAcc = null;
						return 0;
					}

				}
			}
		});
	}

	/**
	 * 携程PC端出票获取携程账号
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int getCtripAccInfo() throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 获取随机出票人员ID
				Integer count = 1;
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append("select count(1) num from ctrip_accountinfo where ctrip_status=? ");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setString(1, CtripAcc.STATUS_WORKING);

				rs = cs.executeQuery();
				while (rs.next()) {
					count = rs.getInt("num");
				}
				int num = WorkIdNum.getNextCtripNum(count);

				sql_rand = new StringBuffer();
				sql_rand.append(
						"select ctrip_id,ctrip_name,ctrip_password,ctrip_phone,ctrip_username,pay_password, cookie, cid, auth, sauth ")
						.append("from ctrip_accountinfo where ctrip_status=?  ")
						.append("  order by ctrip_id limit ?,1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setString(1, CtripAcc.STATUS_WORKING);
				cs.setInt(2, num - 1);
				rs = cs.executeQuery();
				if (rs.next()) {
					ctripAcc = new CtripAcc();
					ctripAcc.setCtrip_id(rs.getString(1));
					ctripAcc.setCtrip_name(rs.getString(2));
					ctripAcc.setCtrip_password(rs.getString(3));
					ctripAcc.setCtrip_phone(rs.getString(4));
					ctripAcc.setCtrip_username(rs.getString(5));
					ctripAcc.setPay_password(rs.getString(6));
					ctripAcc.setCookie(rs.getString(7));
					ctripAcc.setCid(rs.getString(8));
					ctripAcc.setAuth(rs.getString(9));
					ctripAcc.setSauth(rs.getString(10));
				}
				return num;
			}
		});
	}

	/**
	 * 京东出票模式 --预定成功后订单的处理
	 * 
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsSuccessByJD(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				logger.info(order.getOrderId() + "京东出票模式预定成功后的处理~~~strat");
				StringBuffer sql = null;

				// 更新支付金额 E开头订单号
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set buy_money=?,account_id=?,worker_id=?,manual_order='44'");

				sql.append(" where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, result.getPayMoney());
				logger.info(order.getOrderId() + " 京东出票，最终支付的金额为：" + result.getPayMoney());

				cs.setInt(index++, result.getAccount().getId());
				cs.setInt(index++, result.getWorker().getWorkerId());
				cs.setString(index++, order.getOrderId());
				int count = cs.executeUpdate();
				logger.info(order.getOrderId() + " 京东出票，预定成功后更新订单表，执行次数为：" + count);

				// 更新京东信息表
				sql = new StringBuffer();
				sql.append(
						"update jd_orderinfo set go_status='11',jd_order_id=?,jd_order_no=?,option_time=now(),opt_ren='train_app',acc_id=?,pay_money=?,jd_pay_money=?,jd_coupon_money=?,klt_order_no=?  where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, result.getJdOrderId());
				cs.setString(2, result.getJdOrderNo());
				cs.setInt(3, result.getAccount().getId());
				cs.setString(4, result.getPayMoney());// 12306支付的金额
				cs.setString(5, result.getJdPayMoney());// 京东实际支付金额
				cs.setString(6, result.getJdRebateTicketMoney());// 优惠券金额
				cs.setString(7, result.getKltOrderNo());// 开联通流水号
				cs.setString(8, order.getOrderId());
				cs.executeUpdate();
				logger.info(order.getOrderId() + " 京东出票，预定成功后更新京东订单表！");

				sql = new StringBuffer();
				sql.append("update cp_accountinfo set book_num=book_num+1 where acc_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, result.getAccount().getId());
				cs.executeUpdate();
				logger.info(order.getOrderId() + "update account book_num+1 :" + order.getAccountId());

				/**
				 * 释放京东账号和优惠券
				 */
				sql.setLength(0);
				sql.append("select jd_id,coupon_no from jd_orderinfo where order_id=?");
				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, order.getOrderId());
				rs = cs.executeQuery();

				if (rs.next()) {
					int jdId = rs.getInt(1);
					String couponNo = rs.getString(2);// 优惠券编号

					// 释放京东账号
					sql.setLength(0);
					sql.append("update jd_account set account_status = ?,opt_time = now() where jd_id = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, JdAcc.STATUS_FREE); // 00：空闲 11:使用中
					cs.setInt(2, jdId);
					int row = cs.executeUpdate();

					logger.info("执行释放京东账号操作，执行的SQL语句次数为：" + row);

					// 使京东优惠券状态变为已使用
					sql.setLength(0);
					sql.append("update coupon_info set status = ? where coupon_no = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, 9); // 优惠券状态：0初始化状态可用， 1：使用中 9：已用
					cs.setString(2, result.getJdRebateTicketId());
					int updateNum = cs.executeUpdate();

					logger.info("变更京东优惠券的状态为已使用，执行的SQL语句次数为：" + updateNum);

				}

				logger.info(order.getOrderId() + "京东出票模式预定成功后的处理~~~end");
				return 0;
			}
		});

	}

	/**
	 * 京东出票模式--预定失败时的处理转人工
	 * 
	 * @param order
	 * @param result
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsManualByJD(final Order order, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新订单状态
				logger.info(order.getOrderId() + "京东出票模式预定失败后的处理~~~strat");

				StringBuffer sql = new StringBuffer();

				// 更新订单状态为人工
				sql = new StringBuffer();

				sql.append("update cp_orderinfo set order_status=?,if_12306_cutover=?,");

				sql.append("option_time=now() where order_id=? AND order_status=?");
				int index = 1;

				cs = cn.prepareStatement(sql.toString());

				cs.setString(index++, Order.STATUS_ORDER_MANUAL);

				cs.setInt(index++, 0);// 是否切入12306出票 0：否 1：是

				cs.setString(index++, order.getOrderId());

				cs.setString(index, Order.STATUS_ORDER_ING);
				cs.executeUpdate();

				logger.info(order.getOrderId() + "京东出票模式--更新订单状态为人工处理！");

				/**
				 * 释放京东账号和优惠券
				 */
				sql.setLength(0);
				sql.append("select jd_id,coupon_no from jd_orderinfo where order_id=?");
				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, order.getOrderId());
				rs = cs.executeQuery();

				if (rs.next()) {
					int jdId = rs.getInt(1);
					String couponNo = rs.getString(2);// 优惠券编号

					// 释放京东账号
					sql.setLength(0);
					sql.append("update jd_account set account_status = ?,opt_time = now() where jd_id = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, JdAcc.STATUS_FREE); // 00：空闲 11:使用中
					cs.setInt(2, jdId);
					int row = cs.executeUpdate();

					logger.info("执行释放京东账号操作，执行的SQL语句次数为：" + row);

					// 释放京东优惠券
					sql.setLength(0);
					sql.append("update coupon_info set status = ? where coupon_no = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, 0); // 优惠券状态：0初始化状态可用， 1：使用中 9：已用
					cs.setString(2, couponNo);
					int updateNum = cs.executeUpdate();

					logger.info("执行释放京东优惠券的操作，执行的SQL语句次数为：" + updateNum);

				}

				if (null != result) {
					if (result.getJdOrderId() != null && !"".equals(result.getJdOrderId())) {

						// 更新京东订单ID
						sql.setLength(0);
						sql.append(
								"update jd_orderinfo set jd_order_id=?,option_time=now(),opt_ren='train_app',acc_id=?  where order_id=?");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, result.getJdOrderId());
						cs.setInt(2, result.getAccount().getId());
						cs.setString(3, order.getOrderId());
						cs.executeUpdate();

						logger.info(order.getOrderId() + " 更新京东订单ID。");
					}
				}
				return 0;
			}
		});
	}

	/**
	 * 京东出票模式--预定重发时的处理
	 * 
	 * @param order,result
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsResendByJD(final Order order, final Result result, final int resendIndetify)
			throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新系统订单
				StringBuffer sql = null;
				ResultSet rs = null;

				String status = null;

				Integer resendNum = order.getJdbookResendnum();
				logger.info(order.getOrderId() + " 京东预定重发，重发次数为：" + resendNum);

				if (resendNum < 2) {// 未超规定的重发次数
					status = Order.STATUS_ORDER_RESEND;
				} else {// 如果超过3次重发，则进入人工处理
						// status = Order.STATUS_ORDER_MANUAL;
					try {

						insertHistory(order.getOrderId(), " 京东出票预定重发超过3次，转人工。");

						return orderIsManualByJD(order, result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 开始重发，重发次数加1
				sql = new StringBuffer();
				sql.append(
						"update cp_orderinfo set order_status=?, option_time=now(),return_optlog=?,resend_identify=?,jdbook_resendnum=?");

				sql.append(" where order_id=? AND order_status = ?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());

				cs.setString(index++, status);
				cs.setString(index++, result.getReturnOptlog());
				cs.setInt(index++, resendIndetify);// 重发类型： 0：无变化重发
													// 1：切换12306账号重发 2：切换京东账号重发
													// 3：切换京东预付卡重发
				cs.setInt(index++, ++resendNum);// 重发次数加1
				cs.setString(index++, order.getOrderId());

				cs.setString(index, Order.STATUS_ORDER_ING);

				cs.executeUpdate();
				logger.info(order.getOrderId() + " 京东出票预定重发小于3次，开始重发");

				/**
				 * 如果2：切换京东账号重发，则释放本次的京东账号和优惠券状态
				 */
				if (resendIndetify == 2) {
					sql.setLength(0);
					sql.append("select jd_id,coupon_no from jd_orderinfo where order_id=?");
					cs = cn.prepareStatement(sql.toString());

					cs.setString(1, order.getOrderId());
					rs = cs.executeQuery();

					if (rs.next()) {
						int jdId = rs.getInt(1);
						String couponNo = rs.getString(2);// 优惠券编号

						// 停用京东账号
						sql.setLength(0);
						sql.append("update jd_account set account_status = ?,opt_time = now() where jd_id = ?");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, JdAcc.STATUS_STOP); // 00：空闲 11:使用中
															// 99:停用
						cs.setInt(2, jdId);
						int row = cs.executeUpdate();

						logger.info("执行停用京东账号操作，执行的SQL语句次数为：" + row);

						// 释放京东优惠券
						sql.setLength(0);
						sql.append("update coupon_info set status = ? where coupon_no = ?");
						cs = cn.prepareStatement(sql.toString());
						cs.setInt(1, 0); // 优惠券状态：0初始化状态可用， 1：使用中 9：已用
						cs.setString(2, couponNo);
						int updateNum = cs.executeUpdate();

						logger.info("执行释放京东优惠券的操作，执行的SQL语句次数为：" + updateNum);

					}
				}

				return 0;
			}
		});
	}

	/**
	 * 给状态为“开始预定”的京东订单匹配处理人和账号信息
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int selectJDAllBookParams(final Map<String, String> map, final Order order)
			throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				String orderId = map.get("order_id");
				String manualOrder = map.get("manual_order");// 京东出票模式： 44
				Double payMoney = Double.valueOf(map.get("pay_money"));// 支付金额

				// 获取京东预定机器人
				try {
					getJDBookWorkInfo();
				} catch (RepeatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null == worker) {
					logger.info("JD_book worker is null");
					return 1;
				}

				// 获取京东账号
				try {
					getJDAccInfo(payMoney);
				} catch (RepeatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null == jdAcc) {
					logger.info("JD account is null");
					return 1;
				}

				// 获取京东预付卡信息
				try {
					getJDPreCardInfos(payMoney);
				} catch (RepeatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (jdPrePayCardList.size() == 0) {
					logger.info("JD_预付卡 is null");
					return 1;
				}

				StringBuffer JDlog = new StringBuffer();
				JDlog.append("JD bookWorker and jdAcc,jdPrePayCard ~~")
						.append("workerID:" + worker.getWorkerId() + ",workerEXT" + worker.getWorkerExt())
						.append("jdAcc" + jdAcc.getAccountName());

				logger.info("获取的京东账号，预定机器人信息，预付卡信息为：" + JDlog.toString());

				// 获取出票人员，并锁定该人员
				StringBuffer sql = new StringBuffer();

				// 获取出票账号
				/* 按乘客证件匹配 */
				List<Passenger> passengers = App.orderService.getPassengersByOrderId(orderId);

				// 多人匹配模式
				if (passengers != null && passengers.size() > 0) {

					// 把订单下面的多个身份证号用","分隔，拼成一个全新的字符串。
					StringBuilder passportNos = new StringBuilder();
					String passportNo = null;
					for (int i = 0; i < passengers.size(); i++) {
						passportNo = passengers.get(i).getPassportNo();
						passportNos.append(passportNo);
						passportNos.append(",");
					}
					account = App.accountService.filterAccount(passportNos.toString());

					if (account != null) {
						// 如果白名单匹配成功了，则更新白名单表的匹配次数和时间
						try {
							updateMatchingNumAndTime(order, account, passengers);
						} catch (RepeatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (DatabaseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				/* 按渠道获取账号 */
				if (account == null) {
					// 取账号联系人个数+要添加的联系人个数等于15的12306账号
					account = App.accountService.getChannelAccount(Order.CHANNEL_19E, passengers.size());

					if (account == null) {
						// 取账号联系人个数小于10的账号
						account = App.accountService.getChannelAccount(Order.CHANNEL_19E, 0);
					}
				}
				// 未获取到账号
				if (account == null) {
					return 1;
				}

				logger.info("send lock account");
				String opt_logs = "app order锁定账号(send)";
				addAccLog(cn, cs, account.getUsername(), orderId, opt_logs);

				/**
				 * 执行锁定京东账号，优惠券，预付卡操作
				 */
				// 锁定京东账号
				sql = new StringBuffer();
				sql.append("update jd_account set account_status = ?,opt_time = now() where jd_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, JdAcc.STATUS_WORKING); // 11：使用中
				cs.setInt(2, jdAcc.getJdId());
				int row = cs.executeUpdate();

				logger.info("执行锁定京东账号操作，执行的SQL语句次数为：" + row);

				// 锁定京东优惠券
				sql = new StringBuffer();
				sql.append("update coupon_info set status = ? where auto_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, 1); // 优惠券状态：0初始化状态可用， 1：使用中 9：已用
				cs.setInt(2, jdAcc.getAutoId());
				int updateNum = cs.executeUpdate();

				logger.info("执行锁定京东优惠券的操作，执行的SQL语句次数为：" + updateNum);

				// 锁定京东预付卡
				for (JdPrePayCard jdPrePayCard : jdPrePayCardList) {

					sql = new StringBuffer();
					sql.append("update jd_card set card_status = ?,option_time = now() where card_id = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, JdPrePayCard.CARD_STATUS_WORKING); // 11：使用中
					cs.setInt(2, jdPrePayCard.getJdCardId());
					int num = cs.executeUpdate();

					logger.info("执行锁定京东预付卡操作，执行的SQL语句次数为：" + num);
				}

				// start 更新订单出票人员和账号信息
				logger.info("start update orderinfo account:" + account.getId() + " workerid:" + worker.getWorkerId()
						+ " orderid:" + orderId);
				sql = new StringBuffer();

				sql.append(
						"update cp_orderinfo set account_id=?, worker_id=?, option_time=now(),manual_order='44' where order_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(1, account.getId());
				cs.setInt(2, worker.getWorkerId());
				cs.setString(3, orderId);

				cs.executeUpdate();

				logger.info("end update orderinfo account:" + account.getId() + " workerid:" + worker.getWorkerId()
						+ " orderid:" + orderId);
				// end

				// start 更新员工处理量
				logger.info("start update workerinfo workerid:" + worker.getWorkerId() + " orderid:" + orderId);
				sql = new StringBuffer();

				sql.append(
						"update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");

				cs = cn.prepareStatement(sql.toString());

				cs.setInt(1, worker.getWorkerId());
				cs.executeUpdate();
				logger.info("end update workerinfo workerid:" + worker.getWorkerId() + " orderid:" + orderId);
				// end

				// 插入京东订单表/更新京东订单表
				ResultSet rs = null;
				sql = new StringBuffer();

				sql.append("select count(*) num from jd_orderinfo where order_id=?");
				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, orderId);
				rs = cs.executeQuery();
				int count = 0;
				while (rs.next()) {
					count = rs.getInt("num");
				}
				if (count > 0) {
					// 更新操作
					sql = new StringBuffer();

					sql.append(
							"update jd_orderinfo set go_status='00',option_time=now(),acc_id=?,jd_id=?,coupon_no=? where order_id=?");

					cs = cn.prepareStatement(sql.toString());

					cs.setInt(1, account.getId());
					cs.setInt(2, jdAcc.getJdId());
					cs.setString(3, jdAcc.getCouponNo());
					cs.setString(4, orderId);
					cs.executeUpdate();

					logger.info("end update jd_orderinfo jd_id:" + jdAcc.getJdId() + " orderid:" + orderId + " accID:"
							+ account.getId() + " 优惠券编号:" + jdAcc.getCouponNo());
				} else {
					// 插入操作
					sql = new StringBuffer();

					sql.append(
							"insert into jd_orderinfo(order_id,jd_id,go_status,create_time,option_time,opt_ren,acc_id,coupon_no) values(?, ?,'00',now(),now(),'train_app', ?,?)");

					cs = cn.prepareStatement(sql.toString());

					cs.setString(1, orderId);
					cs.setInt(2, jdAcc.getJdId());
					cs.setInt(3, account.getId());
					cs.setString(4, jdAcc.getCouponNo());
					cs.executeUpdate();

					logger.info("end insert jd_orderinfo jd_id:" + jdAcc.getJdId() + " orderid:" + orderId + " accID:"
							+ account.getId() + " 优惠券编号:" + jdAcc.getCouponNo());
				}

				return 0;

			}
		});
	}

	/**
	 * 预定重发时，重新获取匹配京东订单的处理人和账号信息
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int reSelectJDAllBookParams(final Map<String, String> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				String orderId = map.get("order_id");
				String resendIdentify = map.get("resend_identify");
				logger.info(orderId + " 京东预定重发时，重发类型标识为：" + resendIdentify);
				Double payMoney = Double.valueOf(map.get("pay_money"));// 支付金额

				StringBuilder sql = new StringBuilder();
				// 获取京东预定机器人
				try {
					getJDBookWorkInfo();
				} catch (RepeatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null == worker) {
					logger.info("JD_book worker is null");
					return 1;
				}

				/**
				 * 获取京东预付卡 如果重发类型为3，则切换预付卡后在重发 否则还用第一次请求时的预付卡
				 * 该功能暂时不用，每次都随机新取一个预付卡
				 */
				// if("3".equals(resendIdentify)){}
				try {
					getJDPreCardInfos(payMoney);
				} catch (RepeatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (jdPrePayCardList.size() == 0) {
					logger.info("JD_预付卡 is null");
					return 1;
				}

				/**
				 * 获取请求分配的账号 如果重发类型为1，则不走白名单，按渠道随机在取一个12306账号 其它类型，则还获取第一次请求的账号
				 */
				if ("1".equals(resendIdentify)) {
					// 获取出票账号

					/* 按乘客证件匹配 */
					List<Passenger> passengers = App.orderService.getPassengersByOrderId(orderId);

					// 取账号联系人个数+要添加的联系人个数等于15的12306账号
					account = App.accountService.getChannelAccount(Order.CHANNEL_19E, passengers.size());
					if (account == null) {
						// 取账号联系人个数小于10的账号
						account = App.accountService.getChannelAccount(Order.CHANNEL_19E, 0);
					}

				} else {
					Integer accountId = Integer.valueOf(map.get("acc_id"));
					account = App.accountService.getOrderAccount(accountId);
				}
				if (null == account) {
					logger.info("account is null");
					return 1;
				}

				// start 更新账号状态为正在下单
				// logger.info("start update account
				// accountid:"+account.getId()+" orderid:"+orderId);
				// sql.setLength(0);
				// sql.append("update cp_accountinfo set acc_status=?,
				// order_id=? , option_time=now(),opt_person='order app' where
				// acc_id=?");
				//
				// cs = cn.prepareStatement(sql.toString());
				// cs.setString(1, Account.STATUS_PLACING_ORDER);
				// cs.setString(2, orderId);
				// cs.setInt(3, account.getId());
				// cs.executeUpdate();
				// logger.info("end update account accountid:"+account.getId()+"
				// orderid:"+orderId);
				// end

				logger.info("resend lock account");
				String opt_logs = "app order锁定账号(resend)";
				addAccLog(cn, cs, account.getUsername(), orderId, opt_logs);

				// start 更新订单出票人员和账号信息
				sql.setLength(0);
				sql.append("update cp_orderinfo set account_id=?, worker_id=?, option_time=now() where order_id=?");

				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, account.getId());
				cs.setInt(2, worker.getWorkerId());
				cs.setString(3, orderId);

				cs.executeUpdate();
				logger.info("update orderinfo account:" + account.getId() + " workerid:" + worker.getWorkerId());
				// end

				// start 更新员工处理量
				sql.setLength(0);
				sql.append(
						"update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, worker.getWorkerId());
				cs.executeUpdate();
				// end

				/**
				 * 查询京东账号 如果重发类型为2，则先切换京东账号后在重发 其它类型则还用第一次请求时获取的京东账号
				 */

				if ("2".equals(resendIdentify)) {
					// 获取京东账号
					try {
						getJDAccInfo(payMoney);
					} catch (RepeatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (null == jdAcc) {
						logger.info("JD account is null");
						return 1;
					}
				} else {
					sql.setLength(0);
					sql.append("select jd_id,coupon_no from jd_orderinfo where order_id=?");
					cs = cn.prepareStatement(sql.toString());

					cs.setString(1, orderId);
					rs = cs.executeQuery();

					if (rs.next()) {
						int jdId = rs.getInt(1);
						String couponNo = rs.getString(2);// 优惠券编号

						sql.setLength(0);
						sql.append(
								"select jd_id,account_name,account_pwd,account_paypwd,account_email,account_emailpwd,account_status from jd_account where jd_id=?");
						cs = cn.prepareStatement(sql.toString());
						cs.setInt(1, jdId);

						rs = cs.executeQuery();
						if (rs.next()) {// 获取到账号
							jdAcc = new JdAcc();
							jdAcc.setJdId(rs.getInt(1));
							jdAcc.setAccountName(rs.getString(2));
							jdAcc.setAccountPwd(rs.getString(3));
							jdAcc.setAccountPaypwd(rs.getString(4));
							jdAcc.setAccountEmail(rs.getString(5));
							jdAcc.setAccountEmailpwd(rs.getString(6));
							jdAcc.setAccountStatus(rs.getString(7));
							jdAcc.setCouponNo(couponNo);

							logger.info(
									"预定重发时，获取到的京东账号信息为： 账号ID：" + jdAcc.getJdId() + " 账号名字为：" + jdAcc.getAccountName()
											+ " 账号密码为：" + jdAcc.getAccountPwd() + " 优惠券编号为：" + jdAcc.getCouponNo());

						} else {
							logger.info("JD account is null");
							return 1;
						}

					} else {
						logger.info("JD account is null");
						return 1;
					}
				}

				/**
				 * 执行锁定京东账号，优惠券，预付卡操作
				 */
				// 锁定京东账号
				sql.setLength(0);
				sql.append("update jd_account set account_status = ?,opt_time = now() where jd_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, JdAcc.STATUS_WORKING); // 11：使用中
				cs.setInt(2, jdAcc.getJdId());
				int row = cs.executeUpdate();

				logger.info("执行锁定京东账号操作，执行的SQL语句次数为：" + row);

				// 锁定京东优惠券
				sql.setLength(0);
				sql.append("update coupon_info set status = ? where coupon_no = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, 1); // 优惠券状态：0初始化状态可用， 1：使用中 9：已用
				cs.setString(2, jdAcc.getCouponNo());
				int updateNum = cs.executeUpdate();

				logger.info("执行锁定京东优惠券的操作，执行的SQL语句次数为：" + updateNum);

				// 锁定京东预付卡
				for (JdPrePayCard jdPrePayCard : jdPrePayCardList) {

					sql.setLength(0);
					sql.append("update jd_card set card_status = ?,option_time = now() where card_id = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, JdPrePayCard.CARD_STATUS_WORKING); // 11：使用中
					cs.setInt(2, jdPrePayCard.getJdCardId());
					int num = cs.executeUpdate();

					logger.info("执行锁定京东预付卡操作，执行的SQL语句次数为：" + num);
				}

				// 插入京东订单表/更新京东订单表
				ResultSet rs = null;
				sql.setLength(0);

				sql.append("select count(*) num from jd_orderinfo where order_id=?");
				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, orderId);
				rs = cs.executeQuery();
				int count = 0;
				while (rs.next()) {
					count = rs.getInt("num");
				}
				if (count > 0) {
					// 更新操作
					sql.setLength(0);

					sql.append(
							"update jd_orderinfo set go_status='00',option_time=now(),acc_id=?,jd_id=?,coupon_no=? where order_id=?");

					cs = cn.prepareStatement(sql.toString());

					cs.setInt(1, account.getId());
					cs.setInt(2, jdAcc.getJdId());
					cs.setString(3, jdAcc.getCouponNo());
					cs.setString(4, orderId);
					cs.executeUpdate();

					logger.info("京东预定重发，end update jd_orderinfo jd_id:" + jdAcc.getJdId() + " orderid:" + orderId
							+ " accID:" + account.getId() + " 优惠券编号:" + jdAcc.getCouponNo());
				} else {
					// 插入操作
					sql.setLength(0);

					sql.append(
							"insert into jd_orderinfo(order_id,jd_id,go_status,create_time,option_time,opt_ren,acc_id,coupon_no) values(?, ?,'00',now(),now(),'train_app', ?,?)");

					cs = cn.prepareStatement(sql.toString());

					cs.setString(1, orderId);
					cs.setInt(2, jdAcc.getJdId());
					cs.setInt(3, account.getId());
					cs.setString(4, jdAcc.getCouponNo());
					cs.executeUpdate();

					logger.info("京东预定重发，end insert jd_orderinfo jd_id:" + jdAcc.getJdId() + " orderid:" + orderId
							+ " accID:" + account.getId() + " 优惠券编号:" + jdAcc.getCouponNo());
				}

				return 0;

			}
		});
	}

	/**
	 * 按支付金额匹配京东出票账号和优惠券
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int getJDAccInfo(final Double payMoney) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				logger.info("开始根据支付金额匹配京东账号和优惠券~~start");
				// 获取随机出票人员ID
				Integer count = 1;
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append("select count(1) num ")
						.append("from coupon_info ci LEFT JOIN jd_account ja ON ci.account_id = ja.jd_id ")
						.append("where ? >= ci.limit_price AND ci.channal = ? AND (ci.is_lock = 0 OR ci.is_lock IS NULL) ")
						.append("AND ci.status = 0 AND ja.account_status =?");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setDouble(1, payMoney);
				cs.setString(2, "44");// 44：京东
				cs.setString(3, JdAcc.STATUS_FREE);

				rs = cs.executeQuery();

				while (rs.next()) {
					count = rs.getInt("num");
				}
				logger.info("匹配到京东账号的空闲数量为：" + count);
				int num = WorkIdNum.getNextJdAccNum(count);

				sql_rand = new StringBuffer();
				sql_rand.append(
						"select ci.auto_id,ci.coupon_no,ja.jd_id,ja.account_name,ja.account_pwd,ja.account_paypwd ")
						.append("from coupon_info ci LEFT JOIN jd_account ja ON ci.account_id = ja.jd_id ")
						.append("where ? >= ci.limit_price AND ci.channal = ? AND (ci.is_lock = 0 OR ci.is_lock IS NULL) ")
						.append("AND ci.status = 0 AND ja.account_status =? ORDER BY ci.end_time,ci.limit_price DESC,ci.price DESC LIMIT ?,1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setDouble(1, payMoney);
				cs.setString(2, "44");// 44：京东
				cs.setString(3, JdAcc.STATUS_FREE);
				cs.setInt(4, num - 1);
				rs = cs.executeQuery();
				if (rs.next()) {
					jdAcc = new JdAcc();
					jdAcc.setAutoId(rs.getInt(1));// 京东优惠券ID
					jdAcc.setCouponNo(rs.getString(2));// 京东优惠券编号
					jdAcc.setJdId(rs.getInt(3));
					jdAcc.setAccountName(rs.getString(4));
					jdAcc.setAccountPwd(rs.getString(5));
					jdAcc.setAccountPaypwd(rs.getString(6));

					logger.info("根据支付金额匹配到的京东账号信息为： 账号ID：" + jdAcc.getJdId() + " 账号名字为：" + jdAcc.getAccountName()
							+ " 账号密码为：" + jdAcc.getAccountPwd() + " 优惠券ID为：" + jdAcc.getAutoId() + " 优惠券编号为："
							+ jdAcc.getCouponNo());

					// //锁定京东账号
					// sql_rand = new StringBuffer();
					// sql_rand.append("update jd_account set account_status =
					// ?,opt_time = now() where jd_id = ?");
					// cs = cn.prepareStatement(sql_rand.toString());
					// cs.setString(1,JdAcc.STATUS_WORKING); //11：使用中
					// cs.setInt(2,jdAcc.getJdId());
					// int row = cs.executeUpdate();
					//
					// logger.info("执行锁定京东账号操作，执行的SQL语句次数为："+row);
					//
					// //锁定京东优惠券
					// sql_rand = new StringBuffer();
					// sql_rand.append("update coupon_info set status = ? where
					// auto_id = ?");
					// cs = cn.prepareStatement(sql_rand.toString());
					// cs.setInt(1,1); //优惠券状态：0初始化状态可用， 1：使用中 9：已用
					// cs.setInt(2,jdAcc.getAutoId());
					// int updateNum = cs.executeUpdate();
					//
					// logger.info("执行锁定京东优惠券的操作，执行的SQL语句次数为："+updateNum);

				}
				logger.info("开始根据支付金额匹配京东账号和优惠券~~end");
				return num;
			}
		});
	}

	/**
	 * 根据支付金额匹配预付卡，优先使用金额少的预付卡，可以多卡支付
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int getJDPreCardInfos(final Double payMoney) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				logger.info("开始获取按支付金额匹配到的京东预付卡~~start");
				// 获取随机预付卡账号ID
				Integer count = 1;
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append("select count(1) num from jd_card where card_status = ? and card_money > 0");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setString(1, JdPrePayCard.CARD_STATUS_FREE);// 00：空闲

				rs = cs.executeQuery();

				while (rs.next()) {
					count = rs.getInt("num");
				}
				logger.info("京东预付卡的可用数量为：" + count);
				int num = WorkIdNum.getNextJdPreCardNum(count);

				// 可以获取预付卡列表
				sql_rand = new StringBuffer();
				sql_rand.append("select card_id,card_no,card_pwd,card_money,card_status")
						.append(" from jd_card where card_status = ? and CAST(card_money AS DECIMAL(11,3)) > 0")
						.append(" order by CAST(card_money AS DECIMAL(11,3)) ASC");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setString(1, JdPrePayCard.CARD_STATUS_FREE);
				rs = cs.executeQuery();

				JdPrePayCard jdPrePayCard = null;
				jdPrePayCardList = new ArrayList<JdPrePayCard>();
				double cardMoneySum = 0;// 预付卡金额累积数
				while (rs.next()) {
					jdPrePayCard = new JdPrePayCard();
					jdPrePayCard.setJdCardId(rs.getInt(1));
					jdPrePayCard.setCardNo(rs.getString(2));
					jdPrePayCard.setCardPwd(rs.getString(3));
					jdPrePayCard.setCardMoney(rs.getString(4));
					jdPrePayCard.setCardStatus(rs.getString(5));

					jdPrePayCardList.add(jdPrePayCard);

					logger.info("获取到的京东预付卡账号信息为： 预付卡账号ID：" + jdPrePayCard.getJdCardId() + " 预付卡账号为："
							+ jdPrePayCard.getCardNo() + " 预付卡密码为：" + jdPrePayCard.getCardPwd() + " 预付卡余额为："
							+ jdPrePayCard.getCardMoney());

					logger.info("获取到的京东预付卡列表大小为：" + jdPrePayCardList.size());

					// //锁定京东预付卡
					// sql_rand = new StringBuffer();
					// sql_rand.append("update jd_card set card_status =
					// ?,option_time = now() where card_id = ?");
					// cs = cn.prepareStatement(sql_rand.toString());
					// cs.setString(1,JdPrePayCard.CARD_STATUS_WORKING);
					// //11：使用中
					// cs.setInt(2, jdPrePayCard.getJdCardId());
					// int row = cs.executeUpdate();
					//
					// logger.info("执行锁定京东预付卡操作，执行的SQL语句次数为："+row);

					double cardMoneyDouble = Double.parseDouble(jdPrePayCard.getCardMoney());
					cardMoneySum += cardMoneyDouble;
					logger.info("获取到的京东预付卡余额为：" + cardMoneyDouble);
					logger.info("京东预定订单的待支付金额为：" + payMoney);
					logger.info("获取到的京东预付卡金额累积数为：" + cardMoneySum);

					/**
					 * 如果获取到的预付卡的金额累计数大于等于订单待支付的金额， 则结束循环
					 */
					if (cardMoneySum >= payMoney.doubleValue()) {
						break;
					}
				}
				logger.info("开始获取按支付金额匹配到的京东预付卡~~end");

				return num;
			}
		});
	}

	/**
	 * 解锁京东预付卡
	 * 
	 * @param jdPrePayCardList
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int unLockJDPreCardInfos(final List<JdPrePayCard> jdPrePayCardList)
			throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				logger.info("开始解锁按支付金额匹配到的京东预付卡 ~~start");

				for (JdPrePayCard jdPrePayCard : jdPrePayCardList) {
					// 解锁京东预付卡
					StringBuffer sql = new StringBuffer();
					sql.append("update jd_card set card_status = ?,option_time = now() where card_id = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, JdPrePayCard.CARD_STATUS_FREE);// 00:空闲
					cs.setInt(2, jdPrePayCard.getJdCardId());
					int row = cs.executeUpdate();

					logger.info("执行解锁京东预付卡操作，执行的SQL语句次数为：" + row);
				}

				logger.info("开始解锁按支付金额匹配到的京东预付卡~~end");
				return 0;
			}
		});
	}

	/**
	 * 获取京东预订机器人
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int getJDBookWorkInfo() throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				logger.info("开始随机获取京东预定机器人");
				// 获取随机出票人员ID
				Integer count = 1;
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append(
						"select count(1) num from cp_workerinfo where worker_type=? and worker_status not in(?,?) ");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.TYPE_BOOK);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_PREPARED);

				rs = cs.executeQuery();
				while (rs.next()) {
					count = rs.getInt("num");
				}
				logger.info("空闲的京东预定机器人数量为：" + count);

				int num = WorkIdNum.getNextJdWorkerNum(count);

				sql_rand = new StringBuffer();
				sql_rand.append("select worker_id,worker_name, worker_type,worker_ext ")
						.append("from cp_workerinfo where worker_type=? and worker_status not in(?,?) ")
						.append("  order by worker_id limit ?,1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.TYPE_BOOK);
				cs.setString(2, Worker.STATUS_STOP);
				cs.setString(3, Worker.STATUS_PREPARED);
				cs.setInt(4, num - 1);
				rs = cs.executeQuery();
				if (rs.next()) {
					worker = new Worker();
					// 正常分配到的机器人参数
					worker.setWorkerId(rs.getInt(1));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(rs.getInt(3));
					worker.setWorkerExt(rs.getString(4));

					logger.info("随机获取到的京东预定机器人为： 机器ID：" + worker.getWorkerId() + " 机器名字为：" + worker.getWorkerName()
							+ " 机器类型为：" + worker.getWorkerType() + " 机器IP地址为：" + worker.getWorkerExt());
				}
				return num;
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
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				// 更新订单状态为正在预定
				StringBuffer sql = new StringBuffer();

				sql.append(
						"insert into cp_orderinfo_history (order_id, order_optlog, create_time, opter) values(?, ?, now(), ?)");

				ps = cn.prepareStatement(sql.toString());

				ps.setString(1, orderId);
				ps.setString(2, optlog);
				ps.setString(3, "order app");

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
	public void addAccLog(Connection cn, PreparedStatement cs, final String acc_name, final String orderId,
			final String opt_logs) {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"INSERT INTO account_logs(acc_username,order_id,opt_logs,create_time,opt_person )VALUES(?,?,?,NOW(),'order app')");

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
	 * 账号加入账号过滤表cp_accountinfo_filter
	 * 
	 * @param orderbill
	 * @param result
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int addAccountFilter(final Order orderbill, final Result result) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				int acc_id = 0;
				String idsCard = null;// 证件号
				String realName = null;// 姓名
				String orderId = orderbill.getOrderId();// 订单号

				acc_id = result.getAccount().getId();

				List<AccountFilter> filterList = new ArrayList<AccountFilter>();
				List<String> cpInfos = new ArrayList<String>();
				StringBuilder cpBuilder = new StringBuilder();
				AccountFilter filter = null;

				if (Result.FILTER_PART.equalsIgnoreCase(result.getFilterScope())) {
					String[] passengers = result.getErrorInfo().split("\\|");
					// 身份证,成人,姓名，状态 0、已通过 1、待审核 2、未通过
					for (String passenger : passengers) {
						if (StringUtils.isNotEmpty(passenger)) {
							String[] str = passenger.split(",");
							if ("0".equals(str[3])) {
								idsCard = str[0];
								realName = str[2];
								// if(!isIdsCardExist(cn, cs, idsCard)){
								// filter = new AccountFilter();
								// filter.setAccount_id(acc_id);
								// filter.setIds_card(idsCard);
								// filter.setReal_name(realName);
								// filterList.add(filter);
								// }
								filter = new AccountFilter();
								filter.setAccount_id(acc_id);
								filter.setIds_card(idsCard);
								filter.setReal_name(realName);
								filterList.add(filter);
							}
						}
					}
				} else if (Result.FILTER_ALL.equalsIgnoreCase(result.getFilterScope())) {
					StringBuffer sql = new StringBuffer();
					sql.append("SELECT cert_no,user_name FROM cp_orderinfo_cp WHERE order_id=? ");

					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, orderId);
					ResultSet rs = cs.executeQuery();
					while (rs.next()) {
						idsCard = rs.getString(1);
						realName = rs.getString(2);
						// if(!isIdsCardExist(cn, cs, idsCard)){
						// filter = new AccountFilter();
						// filter.setAccount_id(acc_id);
						// filter.setIds_card(idsCard);
						// filterList.add(filter);
						// }
						filter = new AccountFilter();
						filter.setAccount_id(acc_id);
						filter.setIds_card(idsCard);
						filter.setReal_name(realName);
						filterList.add(filter);
					}
				}

				if (filterList != null && filterList.size() > 0) {
					StringBuffer sql = new StringBuffer();
					sql.append(
							"INSERT INTO cp_accountinfo_filter (ids_card, account_id, create_time) VALUES (?, ?, NOW()) ");
					cs = cn.prepareStatement(sql.toString());

					// 将数据保存到账号过滤表
					for (AccountFilter af : filterList) {
						StringBuffer sql_up = new StringBuffer();
						sql_up.append(
								"select count(1),account_id from cp_accountinfo_filter where ids_card=? GROUP BY account_id ");
						PreparedStatement cs_up = cn.prepareStatement(sql_up.toString());
						cs_up.setString(1, af.getIds_card());
						ResultSet rs_up = cs_up.executeQuery();
						if (rs_up.next()) {
							if (af.getAccount_id() == rs_up.getInt(2)) {
								continue;
							}
							StringBuffer sql_2 = new StringBuffer();
							sql_2.append("update cp_accountinfo_filter set account_id=? where ids_card=?");
							PreparedStatement cs_2 = cn.prepareStatement(sql_2.toString());
							cs_2.setInt(1, af.getAccount_id());
							cs_2.setString(2, af.getIds_card());
							cs_2.executeUpdate();
							logger.info(
									"update cp_accountinfo_filter " + af.getIds_card() + " to " + af.getAccount_id());

						} else {
							cs.setString(1, af.getIds_card());
							cs.setInt(2, af.getAccount_id());
							cs.addBatch();
							logger.info(
									"insert cp_accountinfo_filter " + af.getIds_card() + " to " + af.getAccount_id());
						}
						/* 手机变更乘旅信息的乘客 */
						/* 目前只支持同程 */
						cpBuilder.setLength(0);
						cpBuilder.append(af.getIds_card()).append("|").append(af.getReal_name()).append("|")
								.append("1|100");
						cpInfos.add(cpBuilder.toString());
					}
					cs.executeBatch();
				}

				/* 乘旅账号变动信息入库，目前只支持同程 */
				try {

					if (Order.CHANNEL_TC.equals(orderbill.getChannel())) {
						addPassengerNotify(cn, cs, orderId, result.getAccount().getUsername(), cpInfos,
								Order.CHANNEL_TC);
					}
				} catch (Exception e) {
					logger.info("常旅账号正常变动异常, e:" + e.getMessage());
					e.printStackTrace();
				}

				return 0;
			}

		});

	}

	/**
	 * 根据订单号查询常旅变动乘客信息
	 * 
	 * @param order_id
	 * @param cn
	 * @param ps
	 * @return
	 */
	public List<String> findPassengerSimpleInfo(String order_id, Connection cn, PreparedStatement ps) {

		List<String> cpInfos = null;

		try {
			StringBuilder sql = new StringBuilder();
			/* 冒用同程要求为删除操作2、删除1、新增3、修改 */
			sql.append("select CONCAT(cert_no,'|',user_name,'|',2) cp_info from cp_orderinfo_cp where order_id = ?");
			ps = cn.prepareStatement(sql.toString());
			ps.setString(1, order_id);

			ResultSet rs = ps.executeQuery();
			cpInfos = new ArrayList<String>();
			while (rs.next()) {
				cpInfos.add(rs.getString("cp_info"));
			}
		} catch (SQLException e) {
			logger.info("查询乘客cpinfos异常: " + order_id);
			e.printStackTrace();
		}
		return cpInfos;
	}

	/**
	 * 添加乘旅信息变动记录
	 * 
	 * @param cn
	 * @param cs
	 * @param accountUsername
	 * @param cpInfos
	 */
	public void addPassengerNotify(Connection cn, PreparedStatement cs, String orderId, String accountUsername,
			List<String> cpInfos, String channel) {
		try {
			logger.info("常旅信息变动,order_id:" + orderId + " ,account_name:" + accountUsername + " ,channel:" + channel
					+ " ,cpinfos:" + cpInfos.toString());
			StringBuilder builder = new StringBuilder();

			for (String cpInfo : cpInfos) {
				builder.append(cpInfo).append(",");
			}
			if (cpInfos == null || cpInfos.size() == 0)
				return;
			builder.setLength(builder.length() - 1);

			String cpInfoStr = builder.toString();

			builder.setLength(0);
			builder.append("insert into elong_passenger_change(order_id,account_name,cp_infos,create_time,opt_person)")
					.append(" values (?,?,?,NOW(),'train_fh')");

			cs = cn.prepareStatement(builder.toString());
			cs.setString(1, orderId);
			cs.setString(2, accountUsername);
			cs.setString(3, cpInfoStr);

			cs.executeUpdate();
		} catch (Exception e) {
			logger.info("常旅变更信息存储异常");
			e.printStackTrace();
		}
	}

	/**
	 * 判读身份证在账号过滤表是否存在
	 * 
	 * @param cn
	 * @param cs
	 * @param idsCard
	 * @return
	 * @throws SQLException
	 */
	public boolean isIdsCardExist(Connection cn, PreparedStatement cs, String idsCard) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) FROM cp_accountinfo_filter WHERE ids_card=? ");

		cs = cn.prepareStatement(sql.toString());
		cs.setString(1, idsCard);
		ResultSet rs = cs.executeQuery();
		int count = 0;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 根据订单号查询账号id
	 * 
	 * @param cn
	 * @param cs
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	public int queryAccountIdByOrderNo(Connection cn, PreparedStatement cs, String orderId) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT IFNULL(account_id,0) AS account_id FROM cp_orderinfo WHERE order_id=? ");

		cs = cn.prepareStatement(sql.toString());
		cs.setString(1, orderId);
		ResultSet rs = cs.executeQuery();
		int account_id = 0;
		if (rs.next()) {
			account_id = rs.getInt(1);
		}

		return account_id;
	}

	/**
	 * 查询订单内用户证件号
	 * 
	 * @param cn
	 * @param cs
	 * @param orderId
	 * @return
	 */
	public List<String> queryRandIdsCard(Connection cn, PreparedStatement cs, String orderId) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cert_no FROM cp_orderinfo_cp WHERE order_id=?");

		cs = cn.prepareStatement(sql.toString());
		cs.setString(1, orderId);
		ResultSet rs = cs.executeQuery();
		List<String> list = new ArrayList<String>();
		while (rs.next()) {
			list.add(rs.getString(1));
		}
		return list;
	}

	/**
	 * 根据身份证号查询账号id
	 * 
	 * @param cn
	 * @param cs
	 * @param orderId
	 * @return
	 */
	public int queryAccountIdByCardsId(Connection cn, PreparedStatement cs, List<String> list) throws SQLException {
		String date = DateUtil.dateAddMin(new Date(), -10);
		int account_id = 0;
		if (list.size() == 1) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT account_id FROM cp_accountinfo_filter WHERE ids_card=?");
			cs = cn.prepareStatement(sql.toString());
			cs.setString(1, list.get(0));
			ResultSet rs = cs.executeQuery();
			if (rs.next()) {
				account_id = rs.getInt(1);
			}
			return account_id;
		} else if (list.size() <= 3) {

			StringBuffer sql2 = new StringBuffer();
			sql2.append("SELECT cp1.account_id FROM");
			int index = 1;
			for (int i = 0; i < list.size(); i++) {
				String table = "cp" + index;
				sql2.append(" ( SELECT account_id FROM cp_accountinfo_filter ").append(" WHERE").append(" ids_card=? ");
				if (WorkIdNum.limit_day == 1) {
					sql2.append("AND create_time > '").append(date).append("'");
				}
				sql2.append(") AS ").append(table);
				if (i != list.size() - 1) {
					sql2.append(", ");
				}
				index++;
			}
			int cp_index = 2;
			sql2.append(" WHERE ");
			for (int i = 0; i < list.size() - 1; i++) {
				String table = "cp" + cp_index;
				sql2.append("cp1.account_id = ").append(table).append(".account_id");
				if (i != list.size() - 2) {
					sql2.append(" AND ");
				}
				cp_index++;
			}
			cs = cn.prepareStatement(sql2.toString());
			int str_index = 1;
			for (String str : list) {
				cs.setString(str_index++, str);
			}
			logger.info("select account_id sql：" + sql2.toString());
			ResultSet rs = cs.executeQuery();
			if (rs.next()) {
				account_id = rs.getInt(1);
			}
			return account_id;
		} else {
			return account_id;
		}
	}

	// /**
	// * 获取出票账号
	// *
	// * @param cn
	// * @param cs
	// * @param channel
	// * @return
	// * @throws SQLException
	// */
	// public int getAccountForNewOrder(Connection cn, PreparedStatement cs,
	// String orderId,
	// String channel,String acc_id) throws SQLException{
	// // step1:看acc_filter是否有现成的账号
	// StringBuffer sql = null;
	// ResultSet rs = null;
	// DBBeanUtil util = null;
	// List<Account> alist = null;
	// int index = 1;
	//
	// int rand_acc_id = 0;
	// List<String> list= queryRandIdsCard(cn, cs, orderId);
	// logger.info("get certNo orderId : " + orderId + ", certNo : " +
	// list.toString());
	//
	// /*订单中只有一个乘客才匹配旧账号*/
	// if(list.size() == 1) {
	// rand_acc_id = queryAccountIdByCardsId(cn, cs, list);
	// }
	//
	// logger.info("filter account result orderId : " + orderId + ", rand_acc_id
	// : " + rand_acc_id + ", certNo num : " + list.size());
	// if(rand_acc_id > 0){
	// sql = new StringBuffer();
	// sql.append("SELECT acc_id, acc_username, acc_password FROM cp_accountinfo
	// WHERE acc_id=? ");
	// sql.append("AND stop_reason <> '1' AND stop_reason <> '7' AND acc_status
	// not in ('00','44','66') FOR UPDATE");
	// cs = cn.prepareStatement(sql.toString());
	// cs.setInt(index++, rand_acc_id);
	//
	//// try {
	//// int channel_result = querySettingValue("account_limit_channel");
	////
	//// String join_acc_channel = getChannel();
	//// if(channel_result==0 && join_acc_channel.contains(channel)){
	//// cs.setString(index, channel);
	//// }else{
	//// cs.setString(index, "19e");
	//// }
	//// } catch (Exception e) {
	//// logger.info("没有单独使用账号的渠道！");
	//// cs.setString(index, "19e");
	//// }
	//
	// rs = cs.executeQuery();
	// util = new DBBeanUtil();
	// alist = util.rs2List(rs, "com.l9e.train.po.Account");
	// logger.info("filter account list size:"+alist.size());
	// if(alist.size()>0){// 获取到账号
	// account = alist.get(0);
	// logger.info("step1: orderByAccountFilter
	// account:"+account.getUsername()+" password:"+account.getPassword());
	//
	// if(Integer.valueOf(acc_id)!=0){
	// // start 更新最初分配的账号状态为空闲
	// logger.info("start update account accountid:"+account.getId()+"
	// orderid:"+orderId);
	// sql = new StringBuffer();
	// sql.append("update cp_accountinfo set acc_status=? where acc_id=?");
	//
	// cs = cn.prepareStatement(sql.toString());
	//
	// cs.setString(1, Account.STATUS_FREE);
	// cs.setInt(2, Integer.valueOf(acc_id));
	// cs.executeUpdate();
	// logger.info("end update account accountid:"+account.getId()+"
	// orderid:"+orderId);
	// // end
	// }
	// return 0;
	// }else{
	// logger.info("step1: orderByAccount accountId "+rand_acc_id+" is busy,try
	// to get a rand account");
	// }
	// }
	//
	// if(Integer.valueOf(acc_id)==0){
	// logger.info("can not find the account! "+orderId);
	// return 1;
	// }
	// logger.info("acc_filter has a account for orderId="+orderId+"
	// account_id="+acc_id);
	// // step3:根据随机获取的账号ID对该账号进行行级加锁
	// sql = new StringBuffer();
	// sql.append("select acc_id, acc_username, acc_password from cp_accountinfo
	// where acc_id=? and acc_status=?");
	// cs = cn.prepareStatement(sql.toString());
	// cs.setInt(1, Integer.valueOf(acc_id));
	// cs.setString(2, Account.STATUS_PLACING_ORDER);
	// rs = cs.executeQuery();
	// util = new DBBeanUtil();
	// alist = util.rs2List(rs, "com.l9e.train.po.Account");
	// logger.info("alist size:"+alist.size());
	// if(alist.size()>0){// 获取到账号
	// account = alist.get(0);
	// logger.info("step2: orderByAccount account:"+account.getUsername()+"
	// password:"+account.getPassword());
	// return 0;
	// }else{
	// return 1;
	// }
	// }

	/**
	 * 更新常用联系人个数
	 * 
	 * @param result
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int updateContactsNum(final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				int contactNum = Integer.parseInt(result.getContactsNum());
				int accId = result.getAccount().getId();
				logger.info("updateContactsNum, acc_id=" + accId + " contact_num=" + contactNum);

				StringBuffer sql = new StringBuffer();
				sql.append("UPDATE cp_accountinfo SET contact_num=? WHERE acc_id=? ");

				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, contactNum);
				cs.setInt(2, accId);
				cs.executeUpdate();

				return 0;
			}

		});
	}

	/**
	 * 更新基础车票价格
	 * 
	 * @param orderbill
	 * @param result
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int updateBasePrice(final Order orderbill, final Result result) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				StringBuffer sql = null;
				// 录入车票相关信息
				sql = new StringBuffer();
				sql.append("select count(1) from t_zjpj_a WHERE cc=? AND fz=? AND dz=?");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, result.getTrainNo());
				cs.setString(2, result.getFromCity());
				cs.setString(3, result.getToCity());
				rs = cs.executeQuery();
				boolean back = false;
				int type_query = 0;
				if (rs.next()) {
					int num = rs.getInt(1);
					if (num >= 1) {
						back = true;
						type_query = 1;
					} else {
						sql = new StringBuffer();
						sql.append("select count(1) from t_zjpj_a WHERE fz=? AND dz=? AND cc LIKE '")
								.append(result.getTrainNo()).append("/%'");

						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, result.getFromCity());
						cs.setString(2, result.getToCity());
						logger.info("query t_zjpj_a:" + sql.toString());
						rs = cs.executeQuery();
						if (rs.next()) {
							int num_2 = rs.getInt(1);
							if (num_2 >= 1) {
								back = true;
								type_query = 2;
							} else {
								sql = new StringBuffer();
								sql.append("select count(1) from t_zjpj_a WHERE fz=? AND dz=? AND cc LIKE '%/")
										.append(result.getTrainNo()).append("'");

								cs = cn.prepareStatement(sql.toString());
								cs.setString(1, result.getFromCity());
								cs.setString(2, result.getToCity());
								logger.info("query t_zjpj_a:" + sql.toString());
								rs = cs.executeQuery();
								if (rs.next()) {
									int num_3 = rs.getInt(1);
									if (num_3 >= 1) {
										back = true;
										type_query = 3;
									}
								}
							}
						}
					}
				}

				if (!back) {
					return 1;
				}
				// 更新表内票价数据
				sql = new StringBuffer();
				if (type_query == 1) {
					sql.append(
							"select cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1,swz,tdz,gws,gwx from t_zjpj_a WHERE fz=? AND dz=? AND cc=? limit 1");
				} else if (type_query == 2) {
					sql.append(
							"select cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1,swz,tdz,gws,gwx from t_zjpj_a WHERE fz=? AND dz=? AND cc LIKE '")
							.append(result.getTrainNo()).append("/%' limit 1");
				} else {
					sql.append(
							"select cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1,swz,tdz,gws,gwx from t_zjpj_a WHERE fz=? AND dz=? AND cc LIKE '%/")
							.append(result.getTrainNo()).append("' limit 1");
				}
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, result.getFromCity());
				cs.setString(2, result.getToCity());
				if (type_query == 1) {
					cs.setString(3, result.getTrainNo());
				}
				rs = cs.executeQuery();
				DataMaintainVo dataMainOld = new DataMaintainVo();
				int index = 1;
				while (rs.next()) {
					dataMainOld.setCc(rs.getString(index++));
					dataMainOld.setFz(rs.getString(index++));
					dataMainOld.setDz(rs.getString(index++));
					dataMainOld.setYz(rs.getFloat(index++));
					dataMainOld.setRz(rs.getFloat(index++));
					dataMainOld.setYws(rs.getFloat(index++));
					dataMainOld.setYwz(rs.getFloat(index++));
					dataMainOld.setYwx(rs.getFloat(index++));
					dataMainOld.setRws(rs.getFloat(index++));
					dataMainOld.setRz2(rs.getFloat(index++));
					dataMainOld.setRz1(rs.getFloat(index++));
					dataMainOld.setSwz(rs.getFloat(index++));
					dataMainOld.setTdz(rs.getFloat(index++));
					dataMainOld.setGws(rs.getFloat(index++));
					dataMainOld.setGwx(rs.getFloat(index++));
				}

				String seat_type = orderbill.getSeatType();
				String seat_no = null;
				Double pay_money = 0.0;
				Map<String, Double> map = new HashMap<String, Double>();
				for (OrderCP cp : result.getOrderCps()) {
					seat_no = cp.getSeatNo();
					pay_money = Double.parseDouble(cp.getPaymoney());
					if (StringUtils.isEmpty(seat_type)) {
						logger.error("seat type is empty!");
						return -1;
					} else if ("2".equals(seat_type)) {// 一等座rz1
						if (dataMainOld.getRz1() != pay_money) {
							map.put("rz1", pay_money);
						}
					} else if ("3".equals(seat_type)) {// 二等座rz2
						if (dataMainOld.getRz2() != pay_money) {
							map.put("rz2", pay_money);
						}
					} else if ("5".equals(seat_type)) {// 软卧
						if (seat_no.contains("上")) {
							if (dataMainOld.getRws() != pay_money) {
								map.put("rws", pay_money);// 软卧上rws
							}
						} else if (seat_no.contains("下")) {
							if (dataMainOld.getRwx() != pay_money) {
								map.put("rwx", pay_money);// 软卧下rwx
							}
						}
					} else if ("6".equals(seat_type)) {// 6、硬卧
						if (seat_no.contains("上")) {
							if (dataMainOld.getYws() != pay_money) {
								map.put("yws", pay_money);// 硬卧上yws
							}
						} else if (seat_no.contains("中")) {
							if (dataMainOld.getYwz() != pay_money) {
								map.put("ywz", pay_money);// 硬卧中ywz
							}
						} else if (seat_no.contains("下")) {
							if (dataMainOld.getYwx() != pay_money) {
								map.put("ywx", pay_money);// 硬卧下ywx
							}
						}
					} else if ("7".equals(seat_type)) {// 7 软座
						if (dataMainOld.getRz() != pay_money) {
							map.put("rz", pay_money);
						}
					} else if ("8".equals(seat_type)) {// 8硬座
						if (dataMainOld.getYz() != pay_money) {
							map.put("yz", pay_money);
						}
					} else if ("0".equals(seat_type)) {// 0 商务座
						if (dataMainOld.getSwz() != pay_money) {
							map.put("swz", pay_money);
						}
					} else if ("1".equals(seat_type)) {// 1特等座
						if (dataMainOld.getTdz() != pay_money) {
							map.put("tdz", pay_money);
						}
					} else if ("4".equals(seat_type)) {// 4、高级软卧
						if (seat_no.contains("上")) {
							if (dataMainOld.getGws() != pay_money) {
								map.put("gws", pay_money);
							}
						} else if (seat_no.contains("下")) {
							if (dataMainOld.getGwx() != pay_money) {
								map.put("gwx", pay_money);
							}
						}
					}
				}

				String splitstr = ",";
				String[] seats = { "yz", "rz", "yws", "ywz", "ywx", "rws", "rwx", "rz1", "rz2", "swz", "tdz", "gws",
						"gwx" };

				for (int i = 1; i <= 3; i++) {
					sql = new StringBuffer();
					// 录入车票相关信息
					sql.append("UPDATE t_zjpj_a SET ");
					for (String str : seats) {
						if (map.containsKey(str)) {
							if (sql.indexOf("?") != -1) {
								sql.append(splitstr);
							}
							sql.append(str + "=? ");
						}
					}
					if (i == 1) {
						sql.append(" WHERE fz=? AND dz=? AND cc=?");
					} else if (i == 2) {
						sql.append(" WHERE fz=? AND dz=? AND cc LIKE '").append(result.getTrainNo()).append("/%'");
					} else {
						sql.append(" WHERE fz=? AND dz=? AND cc LIKE '%/").append(result.getTrainNo()).append("'");
					}
					logger.info(sql.toString());
					cs = cn.prepareStatement(sql.toString());
					int ind = 1;
					if (map.isEmpty()) {
						return 1;
					}
					for (String str : seats) {
						if (map.containsKey(str)) {
							cs.setDouble(ind++, map.get(str));
						}
					}
					cs.setString(ind++, result.getFromCity());
					cs.setString(ind++, result.getToCity());
					if (i == 1) {
						cs.setString(ind, result.getTrainNo());
					}

					cs.executeUpdate();
				}
				logger.info("update base price -" + result.getTrainNo());

				return 0;
			}

		});
	}

	/**
	 * 获得系统设置
	 * 
	 * @param key
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int querySettingValue(final String key) throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				StringBuffer sql = new StringBuffer();
				sql.append(
						"select setting_value from train_system_setting where setting_name=? and setting_status='1'");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, key);
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					join_acc_channel = rs.getString("setting_value");
					return 0;
				} else {
					return 1;
				}
			}
		});
	}

	/**
	 * 获得帐号下单次数
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryTrainSystemSettingNum(final int default_num, final String key)
			throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				StringBuffer sql = new StringBuffer();
				int count = default_num;
				sql.append(
						"select setting_value from train_system_setting where setting_name=? and setting_status='1'");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, key);
				ResultSet rs = cs.executeQuery();
				while (rs.next()) {
					String setting_value = rs.getString("setting_value");
					count = Integer.valueOf(setting_value);
				}

				return count;
			}
		});
	}

	/**
	 * 账号是否可用
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int weatherUsed(final int acc_id) throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				StringBuffer sql = new StringBuffer();
				sql = new StringBuffer();
				sql.append(
						"select acc_id, acc_username, acc_password from cp_accountinfo where acc_id=? and (acc_status ='00' OR acc_status ='66')");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, acc_id);
				ResultSet rs = cs.executeQuery();
				DBBeanUtil util = new DBBeanUtil();
				List<Account> alist = util.rs2List(rs, "com.l9e.train.po.Account");
				if (alist.size() > 0) {// 获取到账号
					logger.info("the acc_id:" + acc_id + " can used!");
					return 0;
				} else {
					return 1;
				}
			}
		});
	}

	// /**
	// * 释放机器人
	// * @param orderId
	// * @return
	// * @throws RepeatException
	// * @throws DatabaseException
	// */
	// public int releaseWorker(final Integer workerId)throws RepeatException,
	// DatabaseException {
	// return dbbean.executeMethod(new ICallBack() {
	//
	// @Override
	// public int execute(Connection cn, PreparedStatement cs) throws
	// SQLException {
	//
	// StringBuilder sql = new StringBuilder();
	//
	// sql.append("update cp_workerinfo set worker_status = ? where worker_id =
	// ? and worker_status <> '22'");
	// cs = cn.prepareStatement(sql.toString());
	// cs.setString(1, Worker.STATUS_FREE);
	// cs.setInt(2, workerId);
	//
	// cs.executeUpdate();
	// return 0;
	//
	// }
	// });
	// }

	// /**
	// * 停用机器人
	// * @param orderId
	// * @return
	// * @throws RepeatException
	// * @throws DatabaseException
	// */
	// public int stopWorker(final Worker worker)throws RepeatException,
	// DatabaseException {
	// return dbbean.executeMethod(new ICallBack() {
	//
	// @Override
	// public int execute(Connection cn, PreparedStatement cs) throws
	// SQLException {
	//
	// StringBuilder sql = new StringBuilder();
	// Integer workerId = Integer.valueOf(worker.getWorkerId());
	//
	// sql.append("update cp_workerinfo set worker_status = ?,stop_reason='22'
	// where worker_id = ?");
	// cs = cn.prepareStatement(sql.toString());
	// cs.setString(1, Worker.STATUS_STOP);
	// cs.setInt(2, workerId);
	//
	// cs.executeUpdate();
	// return 0;
	//
	// }
	// });
	// }

	/**
	 * 机器人使用记录-开始
	 * 
	 * @param worker
	 * @param order
	 * @param optType
	 *            1、预订 2、支付
	 * @return 插入记录的主键 int
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public Integer startWorkerReport(final Worker worker, final Order order, final String optType)
			throws RepeatException, DatabaseException {

		final StringBuilder builder = new StringBuilder();

		int ret = dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				ResultSet rs = null;

				if (worker == null || order == null) {
					return 1;
				}

				StringBuilder sql = new StringBuilder();
				Integer workerId = Integer.valueOf(worker.getWorkerId());
				String orderId = order.getOrderId();

				sql.append(
						"insert into cp_workerinfo_report(worker_id,worker_name,order_id,request_time,create_time,opt_type) values")
						.append(" (?,?,?,NOW(),NOW(),?)");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, workerId);
				cs.setString(2, worker.getWorkerName());
				cs.setString(3, orderId);
				cs.setString(4, optType);

				cs.executeUpdate();

				sql.setLength(0);
				sql.append("select report_id from cp_workerinfo_report where worker_id = ? and order_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, workerId);
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
		} else {
			return Integer.valueOf(builder.toString());
		}
	}

	/**
	 * 机器人使用记录-结束
	 * 
	 * @param worker
	 * @param order
	 * @param optType
	 *            1、预订 2、支付
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int endWorkerReport(final Integer reportId) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				if (reportId == null) {
					return 1;
				}

				StringBuilder sql = new StringBuilder();

				sql.append("update cp_workerinfo_report set release_time = NOW() where report_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, reportId);

				cs.executeUpdate();
				return 0;

			}
		});
	}

	public int loadAccPool(final LinkedBlockingQueue queue, final String channel)
			throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {

				StringBuffer sql = new StringBuffer();
				logger.info("开始补充账号：" + channel + "账号数量：" + queue.size());
				// 获取普通账号
				if (queue.size() >= 15) {
					return 0;
				} else {
					logger.info(
							channel + "补充账号WorkIdNum.limit_day:" + WorkIdNum.limit_day + "-----WorkIdNum.no_order_day:"
									+ WorkIdNum.no_order_day + "--- WorkIdNum.contact_num:" + WorkIdNum.contact_num);
					sql.append("select acc_id from cp_accountinfo where acc_status=? and book_num<=? and ");
					if (WorkIdNum.limit_day == 1) {
						sql.append(" priority > ? and ");
					}
					sql.append("contact_num <= ? AND is_alive = '1' AND channel=? ");
					// train_app使用
					sql.append("order by option_time desc LIMIT 20");

					// train_app_new 使用
					// sql.append("order by option_time asc LIMIT 20,20");

					int index = 1;
					cs = cn.prepareStatement(sql.toString());
					cs.setString(index++, Account.STATUS_FREE);
					cs.setInt(index++, WorkIdNum.book_num);
					if (WorkIdNum.limit_day == 1) {
						cs.setInt(index++, WorkIdNum.no_order_day);
					}
					cs.setInt(index++, WorkIdNum.contact_num);
					cs.setString(index++, channel);
					rs = cs.executeQuery();

					logger.info("补充账号：" + sql.toString() + "sql账号获取数量：" + rs.getRow() + "book_num:" + WorkIdNum.book_num
							+ ",priority:" + WorkIdNum.no_order_day + ",contact_num:" + WorkIdNum.contact_num
							+ ",channel" + channel);
					// 查询当前常用联系人数限制下的账号没有，则修改当前常用联系人数限制，重新查询
					if (!rs.next()) {
						sql = new StringBuffer();
						sql.append("select acc_id from cp_accountinfo where acc_status=? and book_num<=? and ");
						if (WorkIdNum.limit_day == 1) {
							sql.append(" priority > ? and ");
						}
						sql.append("contact_num <= ? AND is_alive = '1' AND channel=? ");
						sql.append("order by option_time asc LIMIT 15");

						index = 1;
						cs = cn.prepareStatement(sql.toString());
						cs.setString(index++, Account.STATUS_FREE);
						cs.setInt(index++, WorkIdNum.book_num);
						if (WorkIdNum.limit_day == 1) {
							cs.setInt(index++, WorkIdNum.no_order_day);
						}
						cs.setInt(index++, WorkIdNum.contact_num + 10);
						cs.setString(index, channel);
						rs = cs.executeQuery();
					}

					while (rs.next()) {
						if (queue.size() <= 29) {
							int acc_id = rs.getInt("acc_id");

							logger.info("==========>Save to " + channel + " acc pool acc_id:" + acc_id);
							// 乐观锁，更新成功，则添加到队列中
							sql = new StringBuffer();
							sql.append("UPDATE cp_accountinfo SET acc_status=?,option_time=now() ");
							sql.append(" WHERE acc_id = ? AND acc_status='33' ");
							cs = cn.prepareStatement(sql.toString());
							cs.setString(1, Account.STATUS_PLACING_ORDER);
							cs.setInt(2, acc_id);
							int up_result = cs.executeUpdate();

							if (up_result == 1) {
								logger.info(
										"==========>Save to " + channel + " acc pool acc_id:" + acc_id + ",success");
								queue.offer(acc_id);
							}
						} else {
							logger.info(channel + " acc pool is full");
							break;
						}
					}
				}
				return 1;
			}
		});
	}

	/**
	 * 查询学生票附属下单信息
	 */
	public int queryStudentInfo(final String order_id, final String cp_id, final String paramCp)
			throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select province_name,province_code,school_code,school_name,student_no,school_system,enter_year,preference_from_station_name,preference_from_station_code,preference_to_station_name,preference_to_station_code from  cp_orderinfo_student where order_id=? and cp_id=?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				cs.setString(index++, order_id);
				cs.setString(index++, cp_id);
				rs = cs.executeQuery();
				StringBuffer paramSb = new StringBuffer();
				paramSb.append(paramCp);
				if (rs.next()) {
					paramSb.append("|").append(rs.getString(1)).append("|").append(rs.getString(2)).append("|")
							.append(rs.getString(3)).append("|").append(rs.getString(4)).append("|")
							.append(rs.getString(5)).append("|").append(rs.getString(6)).append("|")
							.append(rs.getString(7)).append("|").append(rs.getString(8) == null ? "" : rs.getString(8))
							.append("|").append(rs.getString(9) == null ? "" : rs.getString(9)).append("|")
							.append(rs.getString(10) == null ? "" : rs.getString(10)).append("|")
							.append(rs.getString(11) == null ? "" : rs.getString(11));
				}
				param = paramSb.toString();
				return 1;
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

	// /**
	// * 从机器人服务获取预订机器人
	// */
	// public void getBookWorkerFromService() {
	// try {
	//
	// int count = 0;
	// do{
	// String resultJson =
	// HttpUtil.sendByPost("http://10.16.22.34:20116/worker/getWorker",
	// "type=1", "utf-8");
	// logger.info("获取机器人返回结果 result : " + resultJson);
	// if(resultJson != null && !"".equals(resultJson)) {
	// JSONObject resultJsonObject = JSONObject.fromObject(resultJson);
	// if(resultJsonObject.has("success") &&
	// resultJsonObject.getBoolean("success")) {
	// worker = (Worker)
	// JSONObject.toBean(resultJsonObject.getJSONObject("data"), Worker.class);
	// }
	// }
	//
	// if(worker != null)
	// break;
	//
	// count++;
	// }while(count < 5);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// /**
	// * 从机器人服务获取预登录预订机器人
	// */
	// public void getPreSignBookWorkerFromService(String workerId) {
	// try {
	//
	// String resultJson =
	// HttpUtil.sendByPost("http://10.16.22.34:20116/worker/getWorkerById",
	// "workerId=" + workerId, "utf-8");
	// logger.info("获取机器人返回结果 result : " + resultJson);
	// if(resultJson != null && !"".equals(resultJson)) {
	// JSONObject resultJsonObject = JSONObject.fromObject(resultJson);
	// if(resultJsonObject.has("success") &&
	// resultJsonObject.getBoolean("success")) {
	// worker = (Worker)
	// JSONObject.toBean(resultJsonObject.getJSONObject("data"), Worker.class);
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 记录账号机器人的登录关系，辅助订单关系
	 * 
	 * @param orderId
	 * @param accountName
	 * @param workerId
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public void recordRelation(final String orderId, final String accountName, final Integer workerId)
			throws RepeatException, DatabaseException {

		logger.info("record relation , orderId : " + orderId + ", accountName : " + accountName + ", workerId : "
				+ workerId);
		final StringBuilder sql = new StringBuilder();
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection conn, PreparedStatement ps) throws SQLException {
				ResultSet rs = null;

				/* 检查对应关系是否已存在(预登录可能导致关系已存在) */
				sql.append("select id,order_id,account_name,worker_id,login_time from cp_orderinfo_relation ")
						.append(" where account_name = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, accountName);
				rs = ps.executeQuery();

				sql.setLength(0);
				if (rs.next()) {

					/* 存在则更新 */
					sql.append("update cp_orderinfo_relation set order_id = ?, worker_id = ?, login_time = NOW()")
							.append(" where account_name = ?");
					ps = conn.prepareStatement(sql.toString());
					ps.setString(1, orderId);
					ps.setInt(2, workerId);
					ps.setString(3, accountName);

					ps.executeUpdate();

					logger.info("update relation : " + orderId + ", " + accountName + ", " + workerId);
				} else {

					/* 不存在则保存关系 */
					sql.append(
							"insert into cp_orderinfo_relation(order_id,account_name,worker_id,create_time,login_time) ")
							.append(" values(?,?,?,NOW(),NOW())");
					ps = conn.prepareStatement(sql.toString());
					ps.setString(1, orderId);
					ps.setString(2, accountName);
					ps.setInt(3, workerId);

					ps.executeUpdate();

					logger.info("add new relation : " + orderId + ", " + accountName + ", " + workerId);
				}

				return 0;
			}
		});
	}

	/**
	 * 根据账号查找登录过的机器人
	 * 
	 * @param accountName
	 * @throws RepeatException
	 * @throws DatabaseException
	 * @throws SQLException
	 */
	public void relationWorker(String accountName, Connection conn, PreparedStatement ps) throws SQLException {

		StringBuilder sql = new StringBuilder();
		Integer result = null;

		ResultSet rs = null;
		sql.append("select worker_id from cp_orderinfo_relation where ")
				.append(" account_name = ? and login_time > DATE_SUB(NOW(), INTERVAL 25 MINUTE)");
		ps = conn.prepareStatement(sql.toString());
		ps.setString(1, accountName);

		rs = ps.executeQuery();
		if (rs.next()) {
			result = rs.getInt(1);
		}

		if (result == null || result <= 0) {
			return;
		}

		worker = App.workerService.getWorkerById(result);
	}

	/**
	 * 更新出票设备
	 * 
	 * @param orderId
	 * @param device
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public void updateDevice(final String orderId, final Integer device) throws RepeatException, DatabaseException {

		logger.info("order_id : " + orderId + " ,device : " + device);
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection conn, PreparedStatement ps) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append("update cp_orderinfo set device_type = ? where order_id = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setInt(1, device);
				ps.setString(2, orderId);

				return ps.executeUpdate();
			}

		});
	}

	/**
	 * 获取订单状态
	 * 
	 * @param orderId
	 * @param device
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public String getOrderStatus(final String orderId) throws RepeatException, DatabaseException {

		logger.info("get status , order_id : " + orderId);
		final StringBuilder builder = new StringBuilder();
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection conn, PreparedStatement ps) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append("select order_status from cp_orderinfo where order_id = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, orderId);

				ResultSet rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					builder.append(rs.getString("order_status"));
				}
				return 1;
			}

		});
		return builder.toString();
	}

	/**
	 * 修改订单状态
	 * 
	 * @param orderId
	 * @param device
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public void updateOrderStatus(final String orderId, final String status) throws RepeatException, DatabaseException {

		logger.info("update status , order_id : " + orderId + ", status : " + status);
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection conn, PreparedStatement ps) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append("update cp_orderinfo set order_status = ? where order_id = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, status);
				ps.setString(2, orderId);

				return ps.executeUpdate();
			}

		});
	}

	/**
	 * 更新客户可以选座的坐席类型（可能有多个）
	 * 
	 * @param orderId
	 * @param chooseSeatType
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public void updateOrderChooseSeatType(final String orderId, final String chooseSeatType)
			throws RepeatException, DatabaseException {

		logger.info("update chooseSeatType, order_id : " + orderId + ", choose_seat_type : " + chooseSeatType);
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection conn, PreparedStatement ps) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append("update cp_orderinfo set choose_seat_type = ? where order_id = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, chooseSeatType);
				ps.setString(2, orderId);

				int num = ps.executeUpdate();
				logger.info("更新客户可以选座的坐席类型，更新语句执行次数为：" + num);
				return 0;
			}

		});
	}

	/**
	 * 填充白名单账号变动表
	 * 
	 * @param acc_id,minute,acc_username,acc_password,worker_ext
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public String updatePassAcc(final String acc_id, final String minute, final String acc_username,
			final String acc_password, final String worker_ext) throws RepeatException, DatabaseException {
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				int existCount = 0;
				StringBuilder sql = new StringBuilder();

				ResultSet rs = null;
				sql.append(
						"SELECT  count(1) from cp_pass_acc where acc_id=? and UNIX_TIMESTAMP(now())-UNIX_TIMESTAMP(create_time)<?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, acc_id);
				cs.setString(2, minute);

				rs = cs.executeQuery();
				if (rs.next()) {
					existCount = rs.getInt(1);
					logger.info("existCount" + existCount);
					if (existCount == 0) {
						sql = new StringBuilder();
						sql.append(
								"insert into cp_pass_acc (acc_id,acc_username,acc_password,worker_ext,create_time) values(?,?,?,?,now())");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, acc_id);
						cs.setString(2, acc_username);
						cs.setString(3, acc_password);// 开始订购
						cs.setString(4, worker_ext);// 重发出票

						if (cs.executeUpdate() != 1) {
							logger.info("insert cp_pass_acc success:" + acc_id);
							return 1;
						}
					} else {
						logger.info("账号" + minute + "分钟内已插入过");
					}
				}
				return 0;
			}
		});
		return null;
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
	 * 从库中查询携程要用到的手机号码列表
	 * 
	 * @return list
	 */
	public void queryPhoneNumList() throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {

				try {
					StringBuffer sql = new StringBuffer();
					sql.append("SELECT phone_number FROM ctrip_phonenum ");
					ps = cn.prepareStatement(sql.toString());
					ResultSet rs = ps.executeQuery();
					ctripPhoneNumList = new ArrayList<String>();
					while (rs.next()) {
						ctripPhoneNumList.add(rs.getString(1));
					}
				} catch (SQLException e) {
					logger.info("查询携程手机号码异常!");
					e.printStackTrace();
				}

				return 0;
			}
		});
	}

	/**
	 * 匹配到白名单出票时，使白名单表count_num匹配次数加1，并更新匹配时间
	 * 并且更新cp表中乘客属于白名单的记录is_white_list标识为2
	 */
	public void updateMatchingNumAndTime(final Order order, final Account account, final List<Passenger> passengers)
			throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {

				// 更新白名单表的匹配成功次数及cp表是否属于标明的的标识
				logger.info(order.getOrderId() + " 预定白名单匹配成功后，更新白名单表的匹配次数,匹配时间及乘客表记录是否属于白名单的标识~~start");
				Integer accID = account.getId();// 账号ID
				logger.info(order.getOrderId() + " 预定白名单匹配成功后，更新白名单表及乘客表，accId为:" + accID);
				String passportNo = null;// 乘客证件号
				StringBuffer sql = null;

				if (passengers != null && passengers.size() > 0) {
					for (int i = 0; i < passengers.size(); i++) {
						passportNo = passengers.get(i).getPassportNo();
						logger.info(order.getOrderId() + " 预定白名单匹配成功后，更新白名单表的匹配次数，开始匹配的乘客身份证号为:" + passportNo);

						// 先查询出匹配到白名单的乘客证件号
						sql = new StringBuffer();
						sql.append("select cert_no,acc_id from cp_pass_whitelist");
						sql.append(" where cert_no=? and acc_id=?");

						logger.info(order.getOrderId() + " sql of select cert_no:" + sql.toString());
						ps = cn.prepareStatement(sql.toString());

						ps.setString(1, passportNo);
						ps.setInt(2, accID);

						ResultSet rs = ps.executeQuery();

						String certNo = null;// 乘客表中的乘客证件号
						if (rs.next()) {
							certNo = rs.getString(1);
							logger.info(order.getOrderId() + " 预定白名单匹配成功后，匹配到的乘客身份证号为:" + certNo);
						}

						if (null != certNo) {

							// 更新白名单表的匹配次数和时间
							sql = new StringBuffer();
							sql.append(
									"update cp_pass_whitelist set count_num=count_num+1,last_match_time=now(),update_time=now() ");
							sql.append(" where cert_no=? and acc_id=?");

							logger.info(order.getOrderId() + " sql of update count_num:" + sql.toString());
							ps = cn.prepareStatement(sql.toString());

							ps.setString(1, certNo);
							ps.setInt(2, accID);

							int num = ps.executeUpdate();
							logger.info(order.getOrderId() + " 预定白名单匹配成功后，更新白名单表的匹配次数，更新语句次数为:" + num);

							// 开始更新乘客是否属于白名单的标识
							sql = new StringBuffer();
							sql.append("update cp_orderinfo_cp set is_white_list=?,modify_time=now() ");
							sql.append(" where cert_no=? and order_id=?");

							logger.info(order.getOrderId() + " sql of update is_white_list:" + sql.toString());
							ps = cn.prepareStatement(sql.toString());

							ps.setInt(1, 2);// is_white_list是否属于白名单 1：否 2：是
							ps.setString(2, certNo);
							ps.setString(3, order.getOrderId());

							int updateNum = ps.executeUpdate();
							logger.info(order.getOrderId() + " 预定白名单匹配成功后，更新cp表乘客是否属于白名单的标识，更新语句次数为:" + updateNum);

						}

					}
				}
				logger.info(order.getOrderId() + " 预定白名单匹配成功后，更新白名单表的匹配次数,匹配时间及乘客表记录是否属于白名单的标识~~end");
				return 0;
			}
		});
	}

	/**
	 * 插入途牛排队订单表
	 * 
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int insertTuniuOrderQueue(final Map<String, Object> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				StringBuffer sql = new StringBuffer();
				logger.info(map.get("order_id") + " insertTuniuOrderQueue map:" + map);
				sql.append(
						"insert into tuniu_order_queue (order_id,queue_number,wait_time,msg,create_time,notify_status) values(?,?,?,?,now(),?)");

				logger.info(map.get("order_id") + " sql of insert:" + sql.toString());
				ps = cn.prepareStatement(sql.toString());

				ps.setString(1, String.valueOf(map.get("order_id")));
				ps.setInt(2, Integer.valueOf((map.get("queue_number").toString())));
				ps.setString(3, String.valueOf(map.get("wait_time")));
				ps.setString(4, String.valueOf(map.get("msg")));
				ps.setInt(5, Integer.valueOf((map.get("notify_status").toString())));

				int row = ps.executeUpdate();
				logger.info(map.get("order_id") + " insertTuniuOrderQueue,插入成功的条数为：" + row);
				return 0;
			}
		});
	}

	/**
	 * 插入需要释放的IP
	 * 
	 * @author: taoka
	 * @date: 2018年1月31日 下午5:52:28
	 * @param ipId
	 * @return
	 * @throws Exception
	 *             int
	 */
	public int updateIpStatus(final int ipId) throws Exception {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection conn, PreparedStatement ps) throws SQLException {
				StringBuilder sql = new StringBuilder();
				sql.append("UPDATE cp_ipinfo SET ").append("option_time = now(), ")
						.append("ip_status = '00', request_num = 1 ").append("WHERE ").append("ip_id = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setInt(1, ipId);
				int num = ps.executeUpdate();
				return num;
			}
		});
	}

	/**
	 * 查询一人多单
	 * 
	 * @author: taoka
	 * @date: 2018年4月8日 下午4:47:36
	 * @param string
	 * @return int
	 * @throws DatabaseException
	 * @throws RepeatException
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

}