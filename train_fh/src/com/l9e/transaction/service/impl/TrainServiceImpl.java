package com.l9e.transaction.service.impl;

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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.common.Consts;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.AccountFilter;
import com.l9e.transaction.vo.DataMaintainVo;
import com.l9e.transaction.vo.Notify;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.OrderCP;
import com.l9e.transaction.vo.OrderVo;
import com.l9e.transaction.vo.Passenger;
import com.l9e.transaction.vo.Result;
import com.l9e.transaction.vo.Worker;
import com.l9e.transaction.vo.WorkerWeight;
import com.l9e.util.DBObjectUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.UrlFormatUtil;
import com.l9e.util.WorkIdNum;
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
public class TrainServiceImpl{
	
	private Logger logger=Logger.getLogger(this.getClass());
	private DBBean dbbean = null;

	public List<Order> list = null;
	public Account account = null;
	public Worker worker = null;
	public List<Map<String, String>> warnList = new ArrayList<Map<String, String>>();
	
	public String join_acc_channel = "";

    public Order order3c = null; //新增
	
	public Order getOrder3c() {
		return order3c;
	}
	
	public String param="";
	public String getParam(){
		return this.param;
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

	public TrainServiceImpl() {
		
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
				/*// 获取订单信息 train_app_new 使用
				StringBuffer sql = new StringBuffer();
				int index = 1;
				String now_date = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
				String sub_hour = DateUtil.dateAddHours(new Date(),-24);
				String sub_minute = DateUtil.dateAddMin(new Date(),-1);
				sql.append("select c.order_id,c.order_status from cp_orderinfo c where c.order_status in (?,?) ");
				sql.append("AND c.create_time < ?");
				sql.append("AND c.option_time < ?");
				sql.append("AND c.create_time > ? ");
				sql.append("order by c.create_time desc limit ?");
			
				cs = cn.prepareStatement(sql.toString());
				cs.setString(index++, Order.STATUS_ORDER_START);// 开始订购
				cs.setString(index++, Order.STATUS_ORDER_RESEND);// 重发出票
				cs.setString(index++, sub_minute);	// create_time_xiao
				cs.setString(index++, now_date);	// option_time
				cs.setString(index++, sub_hour);	// 12小时
				cs.setInt(index++, getNum);*/
				
				// 获取订单信息 train_app 使用
				StringBuffer sql = new StringBuffer();
				int index = 1;
				String now_date = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
				String sub_hour = DateUtil.dateAddHours(new Date(),-24);
				sql.append("select c.order_id,c.order_status from cp_orderinfo c where c.order_status in (?,?) ");
				sql.append("AND c.create_time < ?");
				sql.append("AND c.option_time < ?");
				sql.append("AND c.create_time > ? ");
				sql.append("order by c.create_time asc limit ?");
			
				cs = cn.prepareStatement(sql.toString());
				cs.setString(index++, Order.STATUS_ORDER_START);// 开始订购
				cs.setString(index++, Order.STATUS_ORDER_RESEND);// 重发出票
				cs.setString(index++, now_date);	// create_time
				cs.setString(index++, now_date);	// option_time
				cs.setString(index++, sub_hour);	// 12小时
				cs.setInt(index++, getNum);
				
				
				ResultSet rs = cs.executeQuery();
				List<Map<String,String>> order_list = new ArrayList<Map<String,String>>();
				while(rs.next()){
					Map<String,String> map = new HashMap<String,String>();
					map.put("order_id", rs.getString(1));
					map.put("order_status", rs.getString(2));
					order_list.add(map);
				}
				
				list = new ArrayList<Order>();
				Order order = null;
				for(Map<String,String> order_info:order_list){
					sql = new StringBuffer();
					sql.append("select order_id from cp_orderinfo where order_id=? AND order_status in(?,?) FOR UPDATE");
					cs = cn.prepareStatement(sql.toString());	
					cs.setString(1, order_info.get("order_id"));
					cs.setString(2, Order.STATUS_ORDER_START);// 开始订购
					cs.setString(3, Order.STATUS_ORDER_RESEND);// 重发出票
					
					String order_id = "";
					rs = cs.executeQuery();
					if(rs.next()){
						order_id = rs.getString(1);
					}else{
						continue;
					}
//					logger.info("get the order_id to update :"+order_id);
					//先订单修改状态
					sql = new StringBuffer();
					sql.append("update cp_orderinfo c set c.order_status=? where c.order_id=? AND c.order_status in(?,?)");
					cs = cn.prepareStatement(sql.toString());	
					cs.setString(1, Order.STATUS_ORDER_ING);
					cs.setString(2, order_id);
					cs.setString(3, Order.STATUS_ORDER_START);// 开始订购
					cs.setString(4, Order.STATUS_ORDER_RESEND);// 重发出票
					
					if(cs.executeUpdate()!=1){
						logger.info("this order is locked:"+order_id);
						continue;
					}
//					logger.info("update order_id to downing order status:"+order_id);
					//获取订单信息
					sql = new StringBuffer();
					sql.append("select c.order_id, c.order_status,c.account_id, c.pay_money,");
					sql.append(" CONCAT(c.order_id,'|',c.from_city,'|', c.to_city, '|',");
					sql.append(" DATE_FORMAT(c.travel_time,'%Y-%m-%d'), '|', c.train_no), c.seat_type,");
					sql.append(" c.from_time, c.channel, c.level, c.ext_seattype,");
					sql.append("DATE_FORMAT(c.travel_time,'%Y-%m-%d') travel_time,account_id ");
					sql.append("from cp_orderinfo c where c.order_id=? ");
					
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, order_id);// 开始订购
					rs = cs.executeQuery();
					order = new Order();
					if(rs.next()){
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
					}else{
						continue;
					}
					logger.info("get the orderinfo notice robot to book :"+order_id);
					sql = new StringBuffer();
					if("tongcheng".equals(order.getChannel())){
						sql.append("select cp_id, CONCAT(cp_id, '|', user_name, '|',CONVERT(ticket_type,CHAR), '|',CONVERT(cert_type,CHAR), '|',cert_no, '|',seat_type) ,ticket_type from cp_orderinfo_cp where order_id=?");
					}else{
						sql.append("select cp_id, CONCAT(cp_id, '|', user_name, '|',CONVERT(ticket_type,CHAR), '|',CONVERT(cert_type,CHAR), '|',cert_no) ,ticket_type from cp_orderinfo_cp where order_id=?");
					}
					
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, order_id);
					
					rs = cs.executeQuery();
					
					while(rs.next()){
						order.addOrderCp(rs.getString(2));
						if(rs.getInt(3)==1){
							order.setWea_price(true);
						}
					}
//					logger.info("list add orderinfo :"+order_info.get("order_id"));
					list.add(order);
					
				}
				logger.info("get orderbill size:"+list.size());
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
	public int orderIsWait(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新系统订单为排队订单
				StringBuffer sql = null;
				
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?,return_optlog=?, ");
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					sql.append("to_time=?,");
				}
				sql.append("option_time=DATE_ADD(NOW(), INTERVAL 1 MINUTE) where order_id=?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(index++, Order.STATUS_ORDER_RESEND);
					
				cs.setString(index++, result.getReturnOptlog());
				
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
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
	public int orderIsResend(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新系统订单
				StringBuffer sql = null;
				
				String status=null;
				
				sql = new StringBuffer();
				sql.append("SELECT count(ho.order_id) FROM cp_orderinfo ho where timediff(now() , ho.option_time)<'00:10:00'  and ho.order_id=?");

				cs = cn.prepareCall(sql.toString());
				cs.setString(1, order.getOrderId());
			
				ResultSet rs = cs.executeQuery();
				int size = 0;
				if(rs.next()){
					size =rs.getInt(1);
				}

				if(size>0){// 未超规定的时间
					status = Order.STATUS_ORDER_RESEND;
				}else{// 如果时间不充裕，进入人工处理
					// status = Order.STATUS_ORDER_MANUAL;
					try {
						
						insertHistory(order.getOrderId(),"订单操作10分钟进入人工处理。");
						
						return orderIsManual(order, result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?, option_time=now(),return_optlog=? ");
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					sql.append(",to_time=? ");
				}		
				sql.append("where order_id=? AND order_status = ?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(index++, status);
					
				cs.setString(index++, result.getReturnOptlog());
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
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
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsFailure(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				// 更新订单状态
				logger.info("workerType:"+result.getWorker().getWorkerType()+" update order failure:"+order.getOrderId());

				StringBuffer sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?,buy_money=?, error_info=?, option_time=now(),return_optlog=? ,opt_ren=? ");
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					sql.append(",to_time=?");
				}
				sql.append(" where order_id=?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				cs.setString(index++, Order.STATUS_BILL_FAILURE);
				cs.setString(index++, result.getPayMoney());
				cs.setString(index++, result.getFailReason());
				cs.setString(index++, result.getReturnOptlog());
				cs.setString(index++, "robot");
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					cs.setString(index++, order.getArrivetime());
				}
				cs.setString(index++, order.getOrderId());
				logger.info("change status:"+Order.STATUS_BILL_FAILURE);

				cs.executeUpdate();
				
				if(result.isPriceModify()){
					// 录入车票相关信息
					sql = new StringBuffer();
					sql.append("update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?,seat_type=?, modify_time=now() where cp_id=? and order_id=?");
					
					cs = cn.prepareStatement(sql.toString());
					logger.info("failure update cp info :"+order.getOrderId());
					for (OrderCP cp : result.getOrderCps()) {
						cs.setString(1, cp.getPaymoney());
						cs.setString(2, cp.getTrainbox());
						cs.setString(3, cp.getSeatNo());
						cs.setString(4, order.getSeatType());
						cs.setString(5, cp.getCpId());
						cs.setString(6, order.getOrderId());
						
						cs.addBatch();
					}
					
					cs.executeBatch();
				}
				// 开始发送通知给前台
				sql = new StringBuffer();
				sql.append("update cp_orderinfo_notify set notify_status = ?, notify_num=0, notify_next_time=DATE_ADD(now(), interval 20 second), modify_time=now() where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Notify.WAIT);
				cs.setString(2, order.getOrderId());
				
				cs.executeUpdate();

				// 根据订单号查询账号id
				int acc_id = 0;
				if(order.getChannel().equals(Order.CHANNEL_TC)) {
					acc_id = order.getAcc_id();
					logger.info("test_tongcheng -- acc_id : " + acc_id);
				} else {
					acc_id = queryAccountIdByOrderNo(cn, cs, order.getOrderId());
				}
				
				String account_user = "";
				int account_num = 0;
				//查询账号信息
				account_user = result.getAccount().getUsername();
				account_num = result.getAccount().getContact();
				
				if(account_num>=15){
					//账号停用
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status=?,order_id='',opt_person='pay app',stop_reason=? where acc_id=?");
					
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Account.STATUS_STOP);
					cs.setString(2, "3");
					cs.setInt(3, acc_id);
					
					cs.executeUpdate();
				}else{
					//重发订单释放账号
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status=?, order_id='' where acc_id=?");
					
					cs = cn.prepareStatement(sql.toString());
					
					cs.setString(1, Account.STATUS_FREE);
					cs.setInt(2, acc_id);
					
					cs.executeUpdate();
				}
				
				// 添加操作账号日志
				logger.info("release 12306 account:"+account_user);
				String opt_logs = "order order释放账号";
				addAccLog(cn, cs, account_user, order.getOrderId(), opt_logs);
				
				if(StringUtils.equals(result.getRetValue(), Result.NOPASS)){
					// 更新乘客12306审核状态
					sql = new StringBuffer();
					sql.append("UPDATE cp_orderinfo_cp SET check_status=? WHERE order_id=? AND user_name=? AND cert_no=? ");
					cs = cn.prepareStatement(sql.toString());
					// 身份证,成人,姓名，状态 0、已通过 1、待审核 2、未通过
					String[] passengers = result.getErrorInfo().split("\\|");
					for(String passenger : passengers){
						if(StringUtils.isNotEmpty(passenger)){
							String[] str = passenger.split(",");
							if(str.length<4){
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
					if(order.getChannel().equals(Order.CHANNEL_TC) && 
							result.getErrorInfo().contains("身份信息涉嫌被他人冒用")) {
						/*同程常旅变更，315标识*/
						String orderId = order.getOrderId();
						String userName = StrUtil.findRegex(result.getErrorInfo(), "(?<=【).+(?=的身份信息涉嫌被他人冒用)");
						if(!StringUtils.isEmpty(userName)) {
							
							List<String> infos = findPassengerSimpleInfo(orderId, cn, cs);
							if(infos != null && infos.size() > 0) {
								List<String> cpInfos = new ArrayList<String>();
								for(String infoStr : infos) {
									if(infoStr.contains(userName)) {
										infoStr += "|315";
										cpInfos.add(infoStr);
										addPassengerNotify(cn, cs, orderId, result.getAccount().getUsername(), cpInfos, Order.CHANNEL_TC);
										break;
									}
								}
							}
						}
					}
				} catch (Exception e) {
					logger.info("身份冒用常旅信息异常, e:" + e.getMessage());
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
	public int orderIsManual(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新订单状态
				
				// 获取随机出票人员ID
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append("select worker_id from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? order by spare_thread desc limit 1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.TYPE_MANUAL);
				cs.setString(2, Worker.STATUS_STOP); 
				cs.setString(3, Worker.STATUS_PREPARED); 
				rs = cs.executeQuery(); 
				String rand_worker_id = "";
				while(rs.next()){
					rand_worker_id = rs.getString("worker_id");
				}
				
				// 获取出票人员，并锁定该人员
				StringBuffer sql = new StringBuffer();
				
				sql.append("select worker_id, worker_name, worker_type,worker_ext from cp_workerinfo where worker_id=? for update");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Integer.valueOf(rand_worker_id));
				
				rs = cs.executeQuery();
				DBBeanUtil util = new DBBeanUtil();
				
				List<Worker> list = util.rs2List(rs, "com.l9e.transaction.vo.Worker");
				
				Worker worker = null;
				if(list.size()>0){
					worker = list.get(0);
				}else{
					logger.warn("please add a worker!");
					return 2;
				}
				
				// 更新订单状态为人工预定
				sql = new StringBuffer();
				
				sql.append("update cp_orderinfo set worker_id=?, order_status=?, return_optlog=?,");
						
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					sql.append("to_time=?,");
				}
				sql.append(" option_time=now() where order_id=? AND order_status=?");
				int index = 1;
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setInt(index++, worker.getWorkerId());
				
				cs.setString(index++, Order.STATUS_ORDER_MANUAL);
				
				if(null==result){
					cs.setString(index++, "03");//无账号
				}else{
					cs.setString(index++, result.getReturnOptlog());
				}
				
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					cs.setString(index++, order.getArrivetime());
				}
				
				cs.setString(index++, order.getOrderId());
				
				cs.setString(index, Order.STATUS_ORDER_ING);
				
				cs.executeUpdate();
				
				// start 更新员工处理量
				sql = new StringBuffer();
				
				sql.append("update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");
				
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
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsStop(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新订单状态
				// 根据订单号查询账号id
				int acc_id = queryAccountIdByOrderNo(cn, cs, order.getOrderId());
				logger.info("stop acc_id:"+acc_id+" stop order failure:"+order.getOrderId());

				StringBuilder sql = new StringBuilder();
				
				sql.append("update cp_orderinfo set order_status=?, ");
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					sql.append("to_time=?,");
				}
				sql.append("error_info=?, account_id=?, option_time=now(),opt_ren=? where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, Order.STATUS_ORDER_RESEND);
				
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					cs.setString(index++, order.getArrivetime());
				}
				
				cs.setString(index++, result.getFailReason());
				cs.setInt(index++, 0);
				cs.setString(index++, "robot");
				cs.setString(index++, order.getOrderId());
				logger.info("change status:"+Order.STATUS_ORDER_RESEND);

				cs.executeUpdate();
				
				
				//账号停用
				sql.setLength(0);
				sql.append("update cp_accountinfo set acc_status=?,order_id='',opt_person='pay_app',stop_reason=? where acc_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				String errorInfo = result.getErrorInfo();
				if(StrUtil.containRegex(errorInfo, "手机.*核验") || StrUtil.containRegex(errorInfo, "联系人.*上限")
						|| errorInfo.contains("账号状态为：未通过")
						|| StrUtil.containRegex(errorInfo, "重新.*注册")
						|| errorInfo.contains("未通过核验，不能添加常用联系人")
						|| errorInfo.contains("身份证件号码填写是否正确")) {
					cs.setString(1, Account.STATUS_STOP);
				} else {
					cs.setString(1, Account.STATUS_TEMP_STOP);
				}
				if(StrUtil.containRegex(errorInfo, "手机.*核验")) {
					cs.setString(2, "7");
				} else if(StrUtil.containRegex(errorInfo, "联系人.*上限")) {
					cs.setString(2, "3");
				} else if(StrUtil.containRegex(errorInfo, "重新.*注册") || errorInfo.contains("账号状态为：未通过")
						|| errorInfo.contains("未通过核验，不能添加常用联系人")
						|| errorInfo.contains("通过后即可在网上购票")
						|| errorInfo.contains("身份证件号码填写是否正确")){
					cs.setString(2, "1");
				} else {
					cs.setString(2, "2");
				}
				cs.setInt(3, acc_id);
				
				cs.executeUpdate();
				
				// 添加操作账号日志
				logger.info("stop 12306 account:"+acc_id);
				String opt_logs = "暂时停用账号";
				addAccLog(cn, cs, String.valueOf(acc_id), order.getOrderId(), opt_logs);
				return 0;
			}
		});
	}
	
	/**
	 * 帐号封停
	 * 
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsEnd(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
//			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新订单状态
				// 根据订单号查询账号id
				int acc_id = queryAccountIdByOrderNo(cn, cs, order.getOrderId());
				/**
				logger.info("stop acc_id:"+acc_id+" stop order failure:"+order.getOrderId());

				StringBuffer sql = new StringBuffer();
				
				int rand_acc_id = 0;
				// step2:从账号表中随机获取一个账号id
				sql.append("select acc_id from cp_accountinfo where acc_status=? and contact_num <=95 AND is_alive = '1' order by rand() limit 3 ");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Account.FREE);
				rs = cs.executeQuery(); 
				List<Integer> acc_list = new ArrayList<Integer>(); 
				while(rs.next()){
					acc_list.add(rs.getInt(1));
				}
				boolean acc_has = false;
				//依次加锁获取帐号信息
				for(int account_id : acc_list){
					// step3:根据随机获取的账号ID对该账号进行行级加锁
					sql = new StringBuffer();
					sql.append("select acc_id, acc_username, acc_password from cp_accountinfo where acc_id=? for update");
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, account_id);
					rs = cs.executeQuery();
					DBBeanUtil util = new DBBeanUtil();
					List<Account> alist = util.rs2List(rs, "com.l9e.transaction.vo.Account");
					if(alist.size()>0){// 获取到账号
						account = alist.get(0);
						logger.info("stop and reset: orderByAccount account:"+account.getAccUsername()+" password:"+account.getAccPassword());
						acc_has = true;
						rand_acc_id = account_id;
						break;
					}else{// 未获取到账号
						acc_has = false;
						continue;
					}
				}
				logger.info("acc_filter has a account for orderId="+order.getOrderId()+" account_id="+rand_acc_id);
				*/
				StringBuffer sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?, ");
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					sql.append("to_time=?,");
				}
				sql.append("error_info=?, account_id=?, option_time=now(),opt_ren=? where order_id=?");
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, Order.STATUS_ORDER_RESEND);
				
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					cs.setString(index++, order.getArrivetime());
				}
				
				cs.setString(index++, result.getFailReason());
				cs.setInt(index++, 0);
				cs.setString(index++, "robot");
				cs.setString(index++, order.getOrderId());
				logger.info("change status:"+Order.STATUS_ORDER_RESEND);

				cs.executeUpdate();
				
				
				//账号封停用
				sql = new StringBuffer();
				sql.append("update cp_accountinfo set acc_status=?,order_id=?,option_time=NOW(),");
				if("WAITCHECK".equals(result.getErrorInfo())){
					sql.append("opt_person='check_app',");
				}else if("STOP".equals(result.getErrorInfo())){
					sql.append("opt_person='stop_app',");
				}
				sql.append("stop_reason=? where acc_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				index = 1;
				cs.setString(index++, Account.STATUS_STOP);
				cs.setString(index++, order.getOrderId());
				if("WAITCHECK".equals(result.getErrorInfo())){
					cs.setString(index++, "5");
				}else if("STOP".equals(result.getErrorInfo())){
					cs.setString(index++, "1");
				}
				cs.setInt(index, acc_id);
				
				cs.executeUpdate();
				
				// 添加操作账号日志
				logger.info("end 12306 account:"+acc_id);
				String opt_logs = "封停账号"+acc_id;
				addAccLog(cn, cs, String.valueOf(acc_id), order.getOrderId(), opt_logs);
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
	public int orderIsNopass(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// 更新订单状态
				
				// 获取随机出票人员ID
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append("select worker_id from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? order by spare_thread desc limit 1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.TYPE_MANUAL);
				cs.setString(2, Worker.STATUS_STOP); 
				cs.setString(3, Worker.STATUS_PREPARED);
				rs = cs.executeQuery(); 
				String rand_worker_id = "";
				while(rs.next()){
					rand_worker_id = rs.getString("worker_id");
				}
				
				// 获取出票人员，并锁定该人员
				StringBuffer sql = new StringBuffer();
				
				sql.append("select worker_id, worker_name, worker_type,worker_ext from cp_workerinfo where worker_id=? for update");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Integer.valueOf(rand_worker_id));
				
				rs = cs.executeQuery();
				
				DBBeanUtil util = new DBBeanUtil();
				
				List<Worker> list = util.rs2List(rs, "com.l9e.transaction.vo.Worker");
				
				Worker worker = null;
				if(list.size()>0){
					worker = list.get(0);
				}else{
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
 * sql = new StringBuffer(); sql.append("INSERT INTO cp_orderinfo_ext
 * (order_id,passenger_reason,create_time) VALUES (?,?,NOW()) "); cs =
 * cn.prepareStatement(sql.toString());
 * 
 * cs.setString(1, order.getOrderId()); cs.setString(2, result.getErrorInfo());
 * cs.executeUpdate();
 */

				// 更新乘客12306审核状态
				sql = new StringBuffer();
				sql.append("UPDATE cp_orderinfo_cp SET check_status=? WHERE order_id=? AND user_name=? AND cert_no=? ");
				cs = cn.prepareStatement(sql.toString());
				// 身份证,成人,姓名，状态 0、已通过 1、待审核 2、未通过
				String[] passengers = result.getErrorInfo().split("\\|");
				for(String passenger : passengers){
					if(StringUtils.isNotEmpty(passenger)){
						String[] str = passenger.split(",");
						if(str.length<4){
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
				
				sql.append("update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");
				
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
	public int orderIsSuccess(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				/**是否开启支付  预登入状态*/
				boolean is_ready_pay=false;
				logger.info("workettype:"+result.getWorker().getWorkerType());
				StringBuffer sql = null;
				
				int isPayManual = 1;
				
				if(!StringUtils.equals("0", order.getLevel())){// 级别较高的用户，直接支付，否则看配置进行支付
					isPayManual = 1;
				}else{
					// 读取是否需要机器支付的配置
					sql = new StringBuffer();
					
					sql.append("SELECT setting_value FROM hc_system_setting WHERE setting_name='robot_pay_ctrl'");
					
					cs = cn.prepareStatement(sql.toString());
					
					ResultSet rs = cs.executeQuery();
					
					while(rs.next()){
						isPayManual = rs.getInt(1);
						logger.info("robot_pay_ctrl:"+isPayManual);
					}
					// 读取配置结束
				}
				
				// 录入车票相关信息
				sql = new StringBuffer();
				//sql.append("update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?,seat_type=?, modify_time=now() where cp_id=? and order_id=?");
				//sql.append("update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?, modify_time=now() where cp_id=? and order_id=?");
				if("tongcheng".equals(order.getChannel())){
					sql.append("update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?,seat_type=?, modify_time=now() where cp_id=? and order_id=?");
				}else{
					sql.append("update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?, modify_time=now() where cp_id=? and order_id=?");
				}
				cs = cn.prepareStatement(sql.toString());
				
				logger.info("ordercps:"+result.getOrderCps().size());
				/*
				 * boolean isNotify = false; boolean isNotSeat = false;
				 */
				for (OrderCP cp : result.getOrderCps()) {
					cs.setString(1, cp.getPaymoney());
					cs.setString(2, cp.getTrainbox());
					cs.setString(3, cp.getSeatNo());
					if("tongcheng".equals(order.getChannel())){
						logger.info("tongcheng cp_info for update,getSeatType:"+cp.getSeatType()+",order_id"+order.getOrderId());
						/*机器人返回的结果
						  ["0"]="9",--商务座
						["1"]="P",--特等座
						["2"]="M",--一等座
						["3"]="O",--二等座
						["4"]="6",--高级软座
						["5"]="4",--软卧
						["6"]="3",--硬卧
						["7"]="2",--软座
						["8"]="1",--硬座
						["9"]="1",--无座 动车无座为二等座
						["10"]="",--其他
						["11"]="H",--包厢硬卧
						["20"]="F",	--动卧
						7 一等软座
						8 二等软座
						F 动卧
						A 高级动卧*/
						
						/**
						 *系统席别类型
						 *21动卧  22高级动卧  23一等软座  24二等软座
						 * */
						if("7".equals(cp.getSeatType())){
							cs.setString(4,"23");
						}else if("8".equals(cp.getSeatType())){
							cs.setString(4,"24");
						}else if("F".equalsIgnoreCase(cp.getSeatType())){
							cs.setString(4,"21");
						}else if("A".equals(cp.getSeatType())){
							cs.setString(4,"22");
						}else{
							cs.setString(4,order.getSeatType());
						}
						cs.setString(5, cp.getCpId());
						cs.setString(6, order.getOrderId());
					}else{
						cs.setString(4, cp.getCpId());
						cs.setString(5, order.getOrderId());
					}
					//cs.setString(4, order.getSeatType());
					cs.addBatch();
					//
					if(StringUtils.isEmpty(cp.getTrainbox()) && StringUtils.isEmpty(cp.getSeatNo()) && StringUtils.isEmpty(cp.getPaymoney())){
						result.setManual(true);
					}
				}
				
				cs.executeBatch();

				if(!result.isManual()){
					// 开始发送通知给前台
					sql = new StringBuffer();
					sql.append("update cp_orderinfo_notify set notify_status = ?, notify_next_time=now(), modify_time=now() where order_id=? and notify_status=0");
					
					cs = cn.prepareStatement(sql.toString());
					
					cs.setString(1, Notify.WAIT);
					cs.setString(2, order.getOrderId());
					
					cs.executeUpdate();
					// 开始发送通知 end
				}
				
			
				logger.info("orderid:"+order.getOrderId()+" update cp_orderinfo");
				
				sql = new StringBuffer();
				sql.append("SELECT is_pay,account_id FROM cp_orderinfo WHERE order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, order.getOrderId());
				String is_pay = "00";
				String account_id = "0";
				ResultSet rs = cs.executeQuery();
				if(rs.next()){
					is_pay = rs.getString(1);
					account_id = rs.getString(2);
				}
				
				if("11".equals(is_pay)){
					isPayManual = 2;
					//修改账号为占座中
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status='66',book_num=book_num+1,option_time=now() where acc_id=?");
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, Integer.valueOf(account_id));
					cs.executeUpdate();
					logger.info("update account save a seat :"+order.getAccountId());
				}else{
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set book_num=book_num+1 where acc_id=?");
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, Integer.valueOf(account_id));
					cs.executeUpdate();
					logger.info(order.getOrderId()+"update account book_num+1 :"+order.getAccountId());
				}
				
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?, out_ticket_time=now(), ")
					.append("out_ticket_billno=?, buy_money=?,seat_type=?, option_time=now(), pay_limit_time=? ");
					if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup())){
						sql.append(",from_time=? "); 
						if(!"".equals(order.getArrivetime())){
							sql.append(",to_time=?");
						}
						if("Y".equalsIgnoreCase(result.getRefundOnline())){
							sql.append(",error_info=?");
						}
					}
				sql.append(" where order_id=?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				if(!result.isManual()){// 是否需要人工核对
					switch (isPayManual) {
					case 0:
						cs.setString(index++, Order.STATUS_PAY_MANUAL);
						break;
					case 1:
						cs.setString(index++, Order.STATUS_PAY_START);
						break;
					case 2:
						cs.setString(index++, Order.STATUS_PAY_WAIT);
						if(Order.CHANNEL_TC.equals(order.getChannel())||Order.CHANNEL_ELONG.equals(order.getChannel())){
							is_ready_pay=true;
						}
						break;
					default:
						cs.setString(index++, Order.STATUS_PAY_MANUAL);
						break;
					}
				}else{
					cs.setString(index++, Order.STATUS_ORDER_MANUAL);
				}
				
				cs.setString(index++, result.getSheId());
				cs.setString(index++, result.getPayMoney());
				cs.setString(index++, order.getSeatType());
				cs.setString(index++, order.getLoseTime());
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup())){
					cs.setString(index++, order.getSeattime());
					if(!"".equals(order.getArrivetime())){
						cs.setString(index++, order.getArrivetime());
					}if("Y".equalsIgnoreCase(result.getRefundOnline())){
						cs.setString(index++,"12");//同城渠道标识热门景点票 不支持线上退票
						logger.info("orderid:"+order.getOrderId()+",refund_online is:Y");
					}
				}
				cs.setString(index, order.getOrderId());
				cs.executeUpdate();
				
				logger.info("orderid:"+order.getOrderId()+" update cp_orderinfo_cp");
				
				return is_ready_pay?1:0;
			}
		});

	}
	
	//订单是否支付
	public int orderIsPay(final String order_id)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT is_pay,account_id FROM cp_orderinfo WHERE order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, order_id);
				
				ResultSet rs = cs.executeQuery();
				if(rs.next()){
					if("00".equals(rs.getString(1))){
						return 1;
					}else{
						return 0;
					}
				}else{
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
	public int orderIsCancel(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				
				logger.info("orderIsCancel begin orderid:"+order.getOrderId()+" update cp_orderinfo_cp");
				StringBuffer sql = null;
				// 录入车票相关信息
				sql = new StringBuffer();
				sql.append("update cp_orderinfo_cp set buy_money=?, train_box=?, seat_no=?, modify_time=now() where cp_id=? and order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				logger.info("ordercps:"+result.getOrderCps().size());
				
				for (int i = 0; i < result.getOrderCps().size(); i++) {
					OrderCP cp = result.getOrderCps().get(i);
					logger.info("cp:"+cp);
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
				
				logger.info("orderIsCancel orderid:"+order.getOrderId()+" update cp_orderinfo");
				
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?,");
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					sql.append("to_time=?,");
				}
				sql.append("error_info=?,out_ticket_time=now(), out_ticket_billno=?, buy_money=?, option_time=now(),opt_ren=? where order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, Order.STATUS_CANCEL_START);
				if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup()) && !"".equals(order.getArrivetime())){
					cs.setString(index++, order.getArrivetime());
				}
				cs.setString(index++, result.getFailReason());
				cs.setString(index++, result.getSheId());
				cs.setString(index++, result.getPayMoney());
				cs.setString(index++, "robot");
				cs.setString(index, order.getOrderId());
				cs.executeUpdate();
				
				logger.info("orderIsCancel end orderid:"+order.getOrderId()+" update cp_orderinfo");
				
				
				return 0;
			}
			
		});
	}
	
	/**
	 * 排队订单处理
	 * @param order
	 * @param result
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderIsQueue(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection conn, PreparedStatement ps)
					throws SQLException {
				StringBuilder sql = new StringBuilder();
				
				String orderId = order.getOrderId();
				/*获取等待时间*/
				String timeStr = StrUtil.findRegex(result.getErrorInfo(), "\\d+");
				Integer time = null;
				
				try {
					time = Integer.valueOf(timeStr);
				} catch (NumberFormatException e) {
					logger.info("queue order waitTime invalid : " + timeStr);
					return 1;
				}
				
				if(time <= 0)
					return 1;
				if(time > 300)
					time = 300;
				if(time < 10)
					time = 10;
				
				logger.info("order is queue, orderId : " + orderId);
				
				/*更新订单为排队中状态*/
				sql.append("update cp_orderinfo set order_status = ?")
					.append(" where order_id = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, Order.STATUS_ORDER_QUEUE);
				ps.setString(2, orderId);
				
				ps.executeUpdate();
				logger.info("update cp_orderinfo order_status to queue, orderId : " + orderId);
				
				/*将排队订单记录到cp_orderinfo_queue表中等待重新尝试*/
				sql.setLength(0);
				sql.append("insert into cp_orderinfo_queue(order_id, resend_num, resend_time, create_time)")
					.append(" values (?, 1, date_add(now(), interval ? second), now())")
					.append(" on duplicate key update ")
					.append(" resend_num = resend_num + 1,queue_status = ?,resend_time = date_add(now(), interval ? second)");
				
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, orderId);
				ps.setInt(2, time/2);
				ps.setString(3, "00");
				ps.setInt(4, time/2);
				
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
	public int reGetAccountAndWorkerBy(final Map<String,String> map)throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				String orderId = map.get("order_id");
				// 获取预订机器人
//				getBookWorkerFromService();
				worker = Consts.workerService.getWorkerByType(Worker.TYPE_BOOK);
				
				if(null == worker){
					return 0;
				}
				StringBuffer log_work = new StringBuffer();
				log_work.append(" worker.getWorkerName()").append(worker.getWorkerName())
					.append(" work_url").append(worker.getWorkerExt());
				logger.info(log_work.toString());
				
				
				StringBuffer sql = new StringBuffer();
				sql.append("select acc_id id, acc_username username, acc_password password from cp_accountinfo where acc_id=? and acc_status =?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Integer.valueOf(map.get("acc_id")));
				cs.setString(2, Account.STATUS_PLACING_ORDER);
				rs = cs.executeQuery();
				
				List<Account> alist = DBObjectUtil.rs2List(rs, Account.class);
				if(alist.size()>0){// 获取到账号
					account = alist.get(0);
					logger.info("step2: orderByAccount account:"+account.getUsername()+" password:"+account.getPassword());
				}else{
					return 1;
				}
				// start 更新账号状态为正在下单
				logger.info("start update account accountid:"+account.getId()+" orderid:"+orderId);
				sql = new StringBuffer();
				sql.append("update cp_accountinfo set acc_status=?, order_id=? , option_time=now(),opt_person='order app'  where acc_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Account.STATUS_PLACING_ORDER);
				cs.setString(2, orderId);
				cs.setInt(3, account.getId());
				cs.executeUpdate();
				logger.info("end update account accountid:"+account.getId()+" orderid:"+orderId);
				// end
				
				logger.info("resend lock account");
				String opt_logs = "app order锁定账号(resend)";
				addAccLog(cn, cs, account.getUsername(), orderId, opt_logs);
				
				// start 更新订单出票人员和账号信息
				sql = new StringBuffer();
				sql.append("update cp_orderinfo set account_id=?, worker_id=?, option_time=now() where order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, account.getId());
				cs.setInt(2, worker.getWorkerId());
				cs.setString(3, orderId);
				
				cs.executeUpdate();
				logger.info("update orderinfo account:"+account.getId()+" workerid:"+worker.getWorkerId());
				// end
				
				// start 更新员工处理量
//				sql = new StringBuffer();
//				sql.append("update cp_workerinfo set worker_status=?,order_num=order_num+1,spare_thread=spare_thread-1,work_num=work_num+1,work_time=NOW() where worker_id=?");
//				cs = cn.prepareStatement(sql.toString());
//				
//				cs.setString(1, Worker.STATUS_WORKING);
//				cs.setString(2, worker.getWorkerId());
//				cs.executeUpdate();
//				// end
//				worker.setWorkerStatus(Worker.STATUS_WORKING);
				
				return 0;
				
			}});
	}
	
	public int selectAccountAndWorker(Order order,String workerWeight) {
		
		/**
		 * 在此先判断订单中的accountId是否为空
		 * 对订单中自带12306账号的处理，如果订单中没带12306的账号密码，
		 * 则跳过下面一步的流程，直接走 按乘客证件匹配账号的流程
		 */
		Integer accountId = order.getAccountId();
		Integer accountFromWay = order.getAccountFromWay();
		if ((null != accountId && 0 != accountId) && (null != accountFromWay && 1 == accountFromWay)) {

			account = Consts.accountService.getOrderAccount(accountId); // 查询账号

		} else {

			/* 按乘客证件匹配账号 */
			List<Passenger> passengers = Consts.orderService.getPassengersByOrderId(order.getOrderId());
			// 单人匹配模式
			// if(passengers != null && passengers.size() == 1) {
			// String passportNo = passengers.get(0).getPassportNo();
			// account = Consts.accountService.filterAccount(passportNo);
			// }
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
				account = Consts.accountService.filterAccount(passportNos.toString());
				//测试用
//				account =new Account();
//				account.setId(1477266);
//				account.setUsername("aiLFfOlfBi");
//				account.setPassword("aiLFfOlfBi_");
			}

			/* 按渠道获取账号 */
			if (account == null) {
				account = Consts.accountService.getChannelAccount(Order.CHANNEL_19E);
			}
			logger.info("test_tongcheng -- account result : " + account);

		}

		if(account == null)
			return 1;
		
		/*匹配机器人*/
		Object cacheValue = null;
		try {
			cacheValue = Consts.redisDao.getCacheVal(account.getUsername());
		} catch (Exception e) {
			logger.info("get account cache error , e : " + e.getMessage());
		}
		logger.info("test_tongcheng -- username map workerId cacheValue : " + cacheValue);
		
		/*获取机器人*/
		if(cacheValue != null) {
			try {
				Integer workerId = (Integer) cacheValue;
				worker = Consts.workerService.getWorkerById(workerId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * 在此先判断机器的权重，如果权重为lua，则原先的流程不变，机器人还是从队列里取；
		 * 如果权重为java，则直接扫库随机取一个java脚本的机器人。
		 */
		if (WorkerWeight.WEIGHT_LUA.equals(workerWeight)) {
			if (worker == null) {
				worker = Consts.workerService.getWorkerByType(Worker.TYPE_BOOK);
			}
			logger.info("lua脚本的机器人 -- get worker : " + worker);

		} else if (WorkerWeight.WEIGHT_JAVA.equals(workerWeight)) {
			if (worker == null) {
				worker = Consts.workerService.getOneJavaWorker();
				logger.info("java脚本的机器人 -- get worker : " + worker);
			}
		}

		//如果上面还是没有获取到机器，则释放账号
		if (worker == null) {
			Consts.accountService.releaseAcoount(account.getId());
			return 0;
		}
		order.setAcc_id(account.getId());
		order.setAccountId(account.getId());
		order.setWorker_id(worker.getWorkerId());
		
		/*订单关联关系建立*/
		OrderVo orderVo = new OrderVo();
		orderVo.setId(order.getOrderId());
		orderVo.setAccountId(account.getId());
		orderVo.setWorkerId(worker.getWorkerId());
		
		Consts.orderService.updateOrder(orderVo);
		
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
	public int selectOrderAccountAndWorkerBy(final Map<String,String> map)throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				String orderId = map.get("order_id");
				String channel = map.get("channel");
				// 获取出票机器人
				
				/**匹配预登入worker_id*/
				if(map.get("worker_id")!=null){
					Integer id = Integer.valueOf(map.get("worker_id"));
					worker = Consts.workerService.getWorkerById(id);
//					getPreSignBookWorkerFromService(map.get("worker_id"));
				}
				/**预登入  机器人查询为null*/
				if(getWorker()==null){
					worker = Consts.workerService.getWorkerByType(Worker.TYPE_BOOK);
//					getBookWorkerFromService();
				}
				
				if(null == worker){
					return 0;
				}
				StringBuilder log_work = new StringBuilder();
				log_work.append(" worker.getWorkerName()").append(worker.getWorkerName())
					.append(" work_url").append(worker.getWorkerExt());
				logger.info(log_work.toString());
				
				// 获取出票人员，并锁定该人员
				StringBuilder sql = new StringBuilder();
				
				// 获取出票账号
				int recode = getAccountForNewOrder(cn, cs, orderId, channel, map.get("acc_id"));
				// 未获取到账号
				if(recode==1){
					return 1;
				}
				// start 更新账号状态为正在下单
				logger.info("start update account accountid:"+account.getId()+" orderid:"+orderId);
				sql.append("update cp_accountinfo set acc_status=?, order_id=? , option_time=now(),opt_person='order app' where acc_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, Account.STATUS_PLACING_ORDER);
				cs.setString(2, orderId);
				cs.setInt(3, account.getId());
				cs.executeUpdate();
				logger.info("end update account accountid:"+account.getId()+" orderid:"+orderId);
				// end
				
				logger.info("send lock account");
				String opt_logs = "app order锁定账号(send)";
				addAccLog(cn, cs, account.getUsername(), orderId, opt_logs);
				
				// start 更新订单出票人员和账号信息
				logger.info("start update orderinfo account:"+account.getId()+" workerid:"+worker.getWorkerId()+" orderid:"+orderId);
				sql.setLength(0);
				
				sql.append("update cp_orderinfo set account_id=?, worker_id=?, option_time=now() where order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setInt(1, account.getId());
				cs.setInt(2, worker.getWorkerId());
				cs.setString(3, orderId);
				
				cs.executeUpdate();
				
				logger.info("end update orderinfo account:"+account.getId()+" workerid:"+worker.getWorkerId()+" orderid:"+orderId);
				// end
				
				
				
				// start 更新员工处理量
				logger.info("start update workerinfo workerid:"+worker.getWorkerId()+" orderid:"+orderId);
//				sql.setLength(0);
//				
//				sql.append("update cp_workerinfo set worker_status=?,order_num=order_num+1,spare_thread=spare_thread-1,work_num=work_num+1,work_time=NOW() where worker_id=?");
//				
//				cs = cn.prepareStatement(sql.toString());
//				
//				cs.setString(1, Worker.STATUS_WORKING);
//				cs.setString(2, worker.getWorkerId());
//				cs.executeUpdate();
//				logger.info("end update workerinfo workerid:"+worker.getWorkerId()+" orderid:"+orderId);
//				// end
//				worker.setWorkerStatus(Worker.STATUS_WORKING);
				
				return 0;
				
			}
		});
	}
	
	/**
	 * 获取预订机器人
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int getBookWorkInfo() throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				// 获取随机出票人员ID
				StringBuilder sql = new StringBuilder();
				int intervalTime = 40;//默认分发间隔40秒
				sql.append("SELECT setting_value FROM train_system_setting WHERE setting_name = 'book_interval_time'");
				cs = cn.prepareStatement(sql.toString());
				rs = cs.executeQuery();
				if(rs.next()) {
					String settingValue = rs.getString("setting_value");
					try {
						intervalTime = Integer.valueOf(settingValue);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					logger.info("机器人预订分发使用间隔时间 (秒): " + intervalTime);
				}
				
				sql.setLength(0);
				sql.append("select worker_id,worker_name, worker_type,worker_ext,worker_status ")
						.append(" from cp_workerinfo where worker_type=? and worker_status = ? ")
						.append(" and work_time < DATE_SUB(NOW(), INTERVAL ? SECOND) ORDER BY work_time  LIMIT 1");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, Worker.TYPE_BOOK);
				cs.setString(2, Worker.STATUS_FREE); 
				cs.setInt(3, intervalTime);
				rs = cs.executeQuery(); 
				if(rs.next()){
					worker = new Worker();
					//正常分配到的机器人参数
					worker.setWorkerId(rs.getInt(1));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(rs.getInt(3));
					worker.setWorkerExt(rs.getString(4));
					worker.setWorkerStatus(rs.getString(5));
				}else{
					logger.info("没有获取到预订机器人，机器人数量不足");
				}
				return 0;
			}
		});
	}
	
	
	
	/**
	 * 获取预订机器人 通过ID
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int getBookWorkInfo(final String worker_id) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				// 获取随机出票人员ID
				logger.info("获取预订机器人workerId :"+worker_id);
				StringBuilder sql = new StringBuilder();
//				int intervalTime = 40;//默认分发间隔40秒
//				sql.append("SELECT setting_value FROM train_system_setting WHERE setting_name = 'book_interval_time'");
//				cs = cn.prepareStatement(sql.toString());
//				rs = cs.executeQuery();
//				if(rs.next()) {
//					String settingValue = rs.getString("setting_value");
//					try {
//						intervalTime = Integer.valueOf(settingValue);
//					} catch (NumberFormatException e) {
//						e.printStackTrace();
//					}
//					logger.info("机器人预订分发使用间隔时间 (秒): " + intervalTime);
//				}
//				
//				sql.setLength(0);
				sql.append("select worker_id,worker_name, worker_type,worker_ext,worker_status ")
						.append("from cp_workerinfo where worker_id=? and worker_type=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, worker_id);
				cs.setInt(2, Worker.TYPE_BOOK);
				rs = cs.executeQuery(); 
				if(rs.next()){
					logger.info("根据worker_id获取机器人成功:"+worker_id);
					worker = new Worker();
					//正常分配到的机器人参数
					worker.setWorkerId(rs.getInt(1));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(rs.getInt(3));
					worker.setWorkerExt(rs.getString(4));
					worker.setWorkerStatus(rs.getString(5));
				}else{
					logger.info("getBookWorkInfo by worker_id Fail:"+worker_id+",maybe the robot is closed");
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
	public int insertHistory(final String orderId,final String optlog) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				// 更新订单状态为正在预定
				StringBuffer sql = new StringBuffer();
				
				sql.append("insert into cp_orderinfo_history (order_id, order_optlog, create_time, opter) values(?, ?, now(), ?)");
				
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
	public void addAccLog(Connection cn, PreparedStatement cs, final String acc_name, final String orderId, final String opt_logs){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO account_logs(acc_username,order_id,opt_logs,create_time,opt_person )VALUES(?,?,?,NOW(),'order app')");
		
		try {
			cs = cn.prepareStatement(sql.toString());
			cs.setString(1, acc_name);
			cs.setString(2, orderId);
			cs.setString(3, opt_logs);
			cs.executeUpdate();
		} catch (SQLException e) {
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
	public int addAccountFilter(final Order orderbill, final Result result) throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				int acc_id = 0;
				String idsCard = null;// 证件号
				String realName = null;// 姓名
				String orderId = orderbill.getOrderId();// 订单号
				
				acc_id = result.getAccount().getId();
				
				List<AccountFilter> filterList = new ArrayList<AccountFilter>();
				List<String> cpInfos = new ArrayList<String>();
				StringBuilder cpBuilder = new StringBuilder();
				AccountFilter filter = null;
				
				if(Result.FILTER_PART.equalsIgnoreCase(result.getFilterScope())){
					String[] passengers = result.getErrorInfo().split("\\|");
					// 身份证,成人,姓名，状态 0、已通过 1、待审核 2、未通过
					for(String passenger : passengers){
						if(StringUtils.isNotEmpty(passenger)){
							String[] str = passenger.split(",");
							if("0".equals(str[3])){
								idsCard = str[0];
								realName = str[2];
//								if(!isIdsCardExist(cn, cs, idsCard)){
//									filter = new AccountFilter();
//									filter.setAccount_id(acc_id);
//									filter.setIds_card(idsCard);
//									filter.setReal_name(realName);
//									filterList.add(filter);
//								}
								
								filter = new AccountFilter();
								filter.setAccount_id(acc_id);
								filter.setIds_card(idsCard);
								filter.setReal_name(realName);
								filterList.add(filter);
								
							}
						}
					}
				}else if(Result.FILTER_ALL.equalsIgnoreCase(result.getFilterScope())){
					StringBuffer sql = new StringBuffer();
					sql.append("SELECT cert_no,user_name FROM cp_orderinfo_cp WHERE order_id=? ");
					
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, orderId);
					ResultSet rs = cs.executeQuery();
					while(rs.next()){
						idsCard = rs.getString(1);
						realName = rs.getString(2);
//						if(!isIdsCardExist(cn, cs, idsCard)){
//							filter = new AccountFilter();
//							filter.setAccount_id(acc_id);
//							filter.setIds_card(idsCard);
//							filter.setReal_name(realName);
//							filterList.add(filter);
//						}
					
						filter = new AccountFilter();
						filter.setAccount_id(acc_id);
						filter.setIds_card(idsCard);
						filter.setReal_name(realName);
						filterList.add(filter);
					}
				}
				
				if(filterList!=null && filterList.size()>0){
					StringBuffer sql = new StringBuffer();
					sql.append("INSERT INTO cp_accountinfo_filter (ids_card, account_id, create_time) VALUES (?, ?, NOW()) ");
					cs = cn.prepareStatement(sql.toString());
					
					// 将数据保存到账号过滤表
					for(AccountFilter af : filterList){
						StringBuffer sql_up = new StringBuffer();
						sql_up.append("select count(1),account_id from cp_accountinfo_filter where ids_card=? GROUP BY account_id ");
						PreparedStatement cs_up = cn.prepareStatement(sql_up.toString());
						cs_up.setString(1, af.getIds_card());
						ResultSet rs_up = cs_up.executeQuery();
						if(rs_up.next()){
							if(af.getAccount_id() == rs_up.getInt(2)){
								continue;
							}
							StringBuffer sql_2 = new StringBuffer();
							sql_2.append("update cp_accountinfo_filter set account_id=? where ids_card=?");
							PreparedStatement cs_2 = cn.prepareStatement(sql_2.toString());
							cs_2.setInt(1, af.getAccount_id());
							cs_2.setString(2, af.getIds_card());
							cs_2.executeUpdate();
							logger.info("update cp_accountinfo_filter "+af.getIds_card() +" to "+af.getAccount_id());
							
						}else{
							cs.setString(1, af.getIds_card());
							cs.setInt(2, af.getAccount_id());
							cs.addBatch();
							logger.info("insert cp_accountinfo_filter "+af.getIds_card() +" to "+af.getAccount_id());
						}
						/*手机变更乘旅信息的乘客*/
						/*目前只支持同程*/
						cpBuilder.setLength(0);
						cpBuilder.append(af.getIds_card())
						.append("|")
						.append(af.getReal_name())
						.append("|")
						.append("1|100");
						cpInfos.add(cpBuilder.toString());
					}
					cs.executeBatch();
				}
				/*乘旅账号变动信息入库，目前只支持同程*/
				try {
					
					if(Order.CHANNEL_TC.equals(orderbill.getChannel())) {
						addPassengerNotify(cn, cs, orderId, result.getAccount().getUsername(), cpInfos, Order.CHANNEL_TC);
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
	 * @param order_id
	 * @param cn
	 * @param ps
	 * @return
	 */
	public List<String> findPassengerSimpleInfo(String order_id, Connection cn, PreparedStatement ps) {
		
		List<String> cpInfos = null;
		
		try {
			StringBuilder sql = new StringBuilder();
			/*冒用同程要求为删除操作2、删除1、新增3、修改*/
			sql.append("select CONCAT(cert_no,'|',user_name,'|',2) cp_info from cp_orderinfo_cp where order_id = ?");
			ps = cn.prepareStatement(sql.toString());
			ps.setString(1, order_id);
			
			ResultSet rs = ps.executeQuery();
			cpInfos = new ArrayList<String>();
			while(rs.next()) {
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
	 * @param cn
	 * @param cs
	 * @param accountUsername 账号名称
	 * @param cpInfos 身份证号|姓名|操作编号
	 */
	public void addPassengerNotify(Connection cn, PreparedStatement cs, String orderId, String accountUsername, List<String> cpInfos, String channel) {
		try {
			logger.info("常旅信息变动,order_id:" + orderId + " ,account_name:" + accountUsername + " ,channel:" + channel + " ,cpinfos:" + cpInfos.toString());
			StringBuilder builder = new StringBuilder();
			
			for(String cpInfo : cpInfos) {
				builder.append(cpInfo)
					.append(",");
			}
			if(cpInfos == null || cpInfos.size() == 0)
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
	public boolean isIdsCardExist(Connection cn, PreparedStatement cs, String idsCard) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) FROM cp_accountinfo_filter WHERE ids_card=? ");
		
		cs = cn.prepareStatement(sql.toString());
		cs.setString(1, idsCard);
		ResultSet rs = cs.executeQuery();
		int count = 0;
		if(rs.next()){
			count = rs.getInt(1);
		}
		if(count>0){
			return true;
		}else{
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
	public int queryAccountIdByOrderNo(Connection cn, PreparedStatement cs, String orderId) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT IFNULL(account_id,0) AS account_id FROM cp_orderinfo WHERE order_id=? ");
		
		cs = cn.prepareStatement(sql.toString());
		cs.setString(1, orderId);
		ResultSet rs = cs.executeQuery();
		int account_id = 0;
		if(rs.next()){
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
	public List<String> queryRandIdsCard(Connection cn, PreparedStatement cs, String orderId) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cert_no FROM cp_orderinfo_cp WHERE order_id=?");
		
		cs = cn.prepareStatement(sql.toString());
		cs.setString(1, orderId);
		ResultSet rs = cs.executeQuery();
		List<String> list = new ArrayList<String>();
		while(rs.next()){
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
	public int queryAccountIdByCardsId(Connection cn, PreparedStatement cs, List<String> list) throws SQLException{
//		String date = DateUtil.dateAddMin(new Date(), -10);
//		int account_id = 0;
//		if(list.size()==1){
//			StringBuffer sql = new StringBuffer();
//			sql.append("SELECT account_id FROM cp_accountinfo_filter WHERE ids_card=?");
//			cs = cn.prepareStatement(sql.toString());
//			cs.setString(1, list.get(0));
//			ResultSet rs = cs.executeQuery();
//			if(rs.next()){
//				account_id = rs.getInt(1);
//			}
//			return account_id;
//		}else if(list.size()<=3){
//			
//			StringBuffer sql2 = new StringBuffer();
//			sql2.append("SELECT cp1.account_id FROM");
//			int index = 1;
//			for(int i=0; i<list.size(); i++){	
//			    String table = "cp"+index;
//			    sql2.append(" ( SELECT account_id FROM cp_accountinfo_filter ")
//				.append(" WHERE").append(" ids_card=? ");
//			    if(WorkIdNum.limit_day==1){
//			    	sql2.append("AND create_time > '").append(date).append("'");
//			    }
//			    sql2.append(") AS ").append(table);
//			    if(i!=list.size()-1){
//			    	sql2.append(", ");
//			    }
//			    index++;
//			}
//			int cp_index = 2;
//			sql2.append(" WHERE ");
//			for(int i=0; i<list.size()-1; i++){
//				String table = "cp"+cp_index;
//				sql2.append("cp1.account_id = ")
//				.append(table).append(".account_id");
//				if(i!=list.size()-2){
//			    	sql2.append(" AND ");
//			    }
//				cp_index++;
//			}
//			cs = cn.prepareStatement(sql2.toString());
//			int str_index =1;
//			for(String str:list){
//				cs.setString(str_index++, str);
//			}
//			logger.info("select account_id sql："+sql2.toString());
//			ResultSet rs = cs.executeQuery();
//			if(rs.next()){
//				account_id = rs.getInt(1);
//			}
//			return account_id;
//		}else{
//			return account_id;
//		}
	
		int account_id = 0;
		//多人匹配模式
		if(list.size() > 0) {
			
			//把订单下面的多个身份证号用","分隔，拼成一个全新的字符串。
			StringBuilder passportNos = new StringBuilder(); 
			String passportNo=null;
			for (int i = 0; i < list.size(); i++) {
				passportNo = list.get(i);
				passportNos.append(passportNo);
				passportNos.append(",");
			}
			account = Consts.accountService.filterAccount(passportNos.toString());
			account_id=account.getId();
		}
		return account_id;
	}
	
	/**
	 * 获取出票账号
	 * 
	 * @param cn
	 * @param cs
	 * @param channel
	 * @return
	 * @throws SQLException
	 */
	public int getAccountForNewOrder(Connection cn, PreparedStatement cs, String orderId,
			String channel,String acc_id) throws SQLException{
		// step1:看acc_filter是否有现成的账号
		StringBuffer sql = null;
		ResultSet rs = null;
		List<Account> alist = null;
		int index = 1;
		
		int rand_acc_id = 0;
		List<String> list= queryRandIdsCard(cn, cs, orderId);
		logger.info("get certNo orderId : " + orderId + ", certNo : " + list.toString());
		
//		/*订单中只有一个乘客才匹配旧账号*/
//		if(list.size() == 1) {
//			rand_acc_id = queryAccountIdByCardsId(cn, cs, list);
//		}
		
		/*多用户匹配*/
		if(list.size() > 0) {
			rand_acc_id = queryAccountIdByCardsId(cn, cs, list);
		}
		
		logger.info("filter account result orderId : " + orderId + ", rand_acc_id : " + rand_acc_id + ", certNo num : " + list.size());
		if(rand_acc_id > 0){
			sql = new StringBuffer();
			sql.append("SELECT acc_id id, acc_username username, acc_password password FROM cp_accountinfo WHERE acc_id=? ");
			sql.append("AND stop_reason <> '1' AND stop_reason <> '7' AND acc_status not in ('00','44','66') FOR UPDATE");
			cs = cn.prepareStatement(sql.toString());
			cs.setInt(index++, rand_acc_id);
			
//			try {
//				int channel_result = querySettingValue("account_limit_channel");
//				
//				String join_acc_channel = getChannel();
//				if(channel_result==0 && join_acc_channel.contains(channel)){
//					cs.setString(index, channel);
//				}else{
//					cs.setString(index, "19e");
//				}
//			} catch (Exception e) {
//				logger.info("没有单独使用账号的渠道！");
//				cs.setString(index, "19e");
//			}
			
			rs = cs.executeQuery();
			
			alist = DBObjectUtil.rs2List(rs, Account.class);
			logger.info("filter account list size:"+alist.size());
			if(alist.size()>0){// 获取到账号
				account = alist.get(0);
				logger.info("step1: orderByAccountFilter account:"+account.getUsername()+" password:"+account.getPassword());
				
				if(Integer.valueOf(acc_id)!=0){
					// start 更新最初分配的账号状态为空闲
					logger.info("start update account accountid:"+account.getId()+" orderid:"+orderId);
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status=? where acc_id=?");
					
					cs = cn.prepareStatement(sql.toString());
					
					cs.setString(1, Account.STATUS_FREE);
					cs.setInt(2, Integer.valueOf(acc_id));
					cs.executeUpdate();
					logger.info("end update account accountid:"+account.getId()+" orderid:"+orderId);
					// end
				}
				return 0;
			}else{
				logger.info("step1: orderByAccount accountId "+rand_acc_id+" is busy,try to get a rand account");
			}
		}
		
		if(Integer.valueOf(acc_id)==0){
			logger.info("can not find the account! "+orderId);
			return 1;
		}
		logger.info("acc_filter has a account for orderId="+orderId+" account_id="+acc_id);
		// step3:根据随机获取的账号ID对该账号进行行级加锁
		sql = new StringBuffer();
		sql.append("select acc_id id, acc_username username, acc_password password from cp_accountinfo where acc_id=? and acc_status=?");
		cs = cn.prepareStatement(sql.toString());
		cs.setInt(1, Integer.valueOf(acc_id));
		cs.setString(2, Account.STATUS_PLACING_ORDER);
		rs = cs.executeQuery();
		
		alist = DBObjectUtil.rs2List(rs, Account.class);
		logger.info("alist size:"+alist.size());
		if(alist.size()>0){// 获取到账号
			account = alist.get(0);
			logger.info("step2: orderByAccount account:"+account.getUsername()+" password:"+account.getPassword());
			return 0;
		}else{
			return 1;
		}
	}
	
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
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				int contactNum = Integer.parseInt(result.getContactsNum());
				int accId = result.getAccount().getId();
				logger.info("updateContactsNum, acc_id="+accId+" contact_num="+contactNum);
				
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
		return  dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				
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
				if(rs.next()){
					int num = rs.getInt(1);
					if(num>=1){
						back = true;
						type_query = 1;
					}else{
						sql = new StringBuffer();
						sql.append("select count(1) from t_zjpj_a WHERE fz=? AND dz=? AND cc LIKE '")
							.append(result.getTrainNo()).append("/%'");
						
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, result.getFromCity());
						cs.setString(2, result.getToCity());
						logger.info("query t_zjpj_a:"+sql.toString());
						rs = cs.executeQuery();
						if(rs.next()){
							int num_2 = rs.getInt(1);
							if(num_2>=1){
								back = true;
								type_query = 2;
							}else{
								sql = new StringBuffer();
								sql.append("select count(1) from t_zjpj_a WHERE fz=? AND dz=? AND cc LIKE '%/")
									.append(result.getTrainNo()).append("'");
								
								cs = cn.prepareStatement(sql.toString());
								cs.setString(1, result.getFromCity());
								cs.setString(2, result.getToCity());
								logger.info("query t_zjpj_a:"+sql.toString());
								rs = cs.executeQuery();
								if(rs.next()){
									int num_3 = rs.getInt(1);
									if(num_3>=1){
										back = true;
										type_query = 3;
									}
								}
							}
						}
					}
				}
				
				if(!back){
					return 1;
				}
				//更新表内票价数据
				sql = new StringBuffer();
				if(type_query==1){
					sql.append("select cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1,swz,tdz,gws,gwx from t_zjpj_a WHERE fz=? AND dz=? AND cc=? limit 1");
				}else if(type_query==2){
					sql.append("select cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1,swz,tdz,gws,gwx from t_zjpj_a WHERE fz=? AND dz=? AND cc LIKE '")
						.append(result.getTrainNo()).append("/%' limit 1");
				}else{
					sql.append("select cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1,swz,tdz,gws,gwx from t_zjpj_a WHERE fz=? AND dz=? AND cc LIKE '%/")
						.append(result.getTrainNo()).append("' limit 1");
				}
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, result.getFromCity());
				cs.setString(2, result.getToCity());
				if(type_query==1){
					cs.setString(3,result.getTrainNo());
				}
				rs = cs.executeQuery();
				DataMaintainVo dataMainOld = new DataMaintainVo();
				int index = 1;
				while(rs.next()){
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
				for(OrderCP cp : result.getOrderCps()){
					seat_no = cp.getSeatNo();
					pay_money = Double.parseDouble(cp.getPaymoney());
					if(StringUtils.isEmpty(seat_type)){
						logger.error("seat type is empty!");
						return -1;
					}else if("2".equals(seat_type)){// 一等座rz1
						if(dataMainOld.getRz1()!=pay_money){
							map.put("rz1", pay_money);
						}
					}else if("3".equals(seat_type)){// 二等座rz2
						if(dataMainOld.getRz2()!=pay_money){
							map.put("rz2", pay_money);
						}
					}else if("5".equals(seat_type)){// 软卧
						if(seat_no.contains("上")){
							if(dataMainOld.getRws()!=pay_money){
								map.put("rws", pay_money);//软卧上rws
							}
						}else if(seat_no.contains("下")){
							if(dataMainOld.getRwx()!=pay_money){
								map.put("rwx", pay_money);//软卧下rwx
							}
						}
					}else if("6".equals(seat_type)){// 6、硬卧
						if(seat_no.contains("上")){
							if(dataMainOld.getYws()!=pay_money){
								map.put("yws", pay_money);//硬卧上yws
							}
						}else if(seat_no.contains("中")){
							if(dataMainOld.getYwz()!=pay_money){
								map.put("ywz", pay_money);// 硬卧中ywz
							}
						}else if(seat_no.contains("下")){
							if(dataMainOld.getYwx()!=pay_money){
								map.put("ywx", pay_money);// 硬卧下ywx
							}
						}
					}else if("7".equals(seat_type)){// 7 软座
						if(dataMainOld.getRz()!=pay_money){
							map.put("rz", pay_money);
						}
					}else if("8".equals(seat_type)){// 8硬座
						if(dataMainOld.getYz()!=pay_money){
							map.put("yz", pay_money);
						}
					}else if("0".equals(seat_type)){// 0 商务座
						if(dataMainOld.getSwz()!=pay_money){
							map.put("swz", pay_money);
						}
					}else if("1".equals(seat_type)){// 1特等座
						if(dataMainOld.getTdz()!=pay_money){
							map.put("tdz", pay_money);
						}
					}else if("4".equals(seat_type)){// 4、高级软卧
						if(seat_no.contains("上")){
							if(dataMainOld.getGws()!=pay_money){
								map.put("gws", pay_money);
							}
						}else if(seat_no.contains("下")){
							if(dataMainOld.getGwx()!=pay_money){
								map.put("gwx", pay_money);
							}
						}
					}
				}
				
				String splitstr = ",";
				String [] seats = {"yz","rz","yws","ywz","ywx","rws","rwx","rz1","rz2","swz","tdz","gws","gwx"};
				
				for(int i=1; i<=3;i++){
					sql = new StringBuffer();
					// 录入车票相关信息
					sql.append("UPDATE t_zjpj_a SET ");
					for(String str : seats){
						if(map.containsKey(str)){
							if(sql.indexOf("?") != -1){
								sql.append(splitstr);
							}
							sql.append(str+"=? ");
						}
					}
					if(i==1){
						sql.append(" WHERE fz=? AND dz=? AND cc=?");
					}else if(i==2){
						sql.append(" WHERE fz=? AND dz=? AND cc LIKE '")
							.append(result.getTrainNo()).append("/%'");
					}else{
						sql.append(" WHERE fz=? AND dz=? AND cc LIKE '%/")
							.append(result.getTrainNo()).append("'");
					}
					logger.info(sql.toString());
					cs = cn.prepareStatement(sql.toString());
					int ind = 1;
					if(map.isEmpty()){
						return 1;
					}
					for(String str : seats){
						if(map.containsKey(str)){
							cs.setDouble(ind++, map.get(str));
						}
					}
					cs.setString(ind++, result.getFromCity());
					cs.setString(ind++, result.getToCity());
					if(i==1){
						cs.setString(ind, result.getTrainNo());
					}
					
					cs.executeUpdate();
				}
				logger.info("update base price -"+ result.getTrainNo());
				
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
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

				StringBuffer sql = new StringBuffer();
				sql.append("select setting_value from train_system_setting where setting_name=? and setting_status='1'");

				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, key);
				ResultSet rs = cs.executeQuery();
				if(rs.next()) {
					join_acc_channel = rs.getString("setting_value");
					return 0;
				}else{
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
	public int queryTrainSystemSettingNum(final int default_num, final String key) throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

				StringBuffer sql = new StringBuffer();
				int count = default_num;
				sql.append("select setting_value from train_system_setting where setting_name=? and setting_status='1'");
				
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
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

				StringBuffer sql = new StringBuffer();
				sql = new StringBuffer();
				sql.append("select acc_id id, acc_username username, acc_password password from cp_accountinfo where acc_id=? and acc_status =?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, acc_id);
				cs.setString(2, Account.STATUS_PLACING_ORDER);
				ResultSet rs = cs.executeQuery();
				List<Account> alist = DBObjectUtil.rs2List(rs, Account.class);
				if(alist.size()>0){// 获取到账号
					logger.info("the acc_id:"+acc_id+" can used!");
					return 0;
				}else{
					return 1;
				}
			}
		});
	}
	
	
	public int loadAccPool(final LinkedBlockingQueue queue,final String channel) throws RepeatException, DatabaseException {
		
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

				StringBuffer sql = new StringBuffer();
				//获取普通账号
				if(queue.size()>=15){
					return 0;
				}else{
					synchronized (Consts.ACCLOCK) {
						if(queue.size()>=15){
							return 0;
						}
						sql.append("select acc_id from cp_accountinfo where acc_status=? and book_num<=? and ");
						if(WorkIdNum.limit_day==1){
							sql.append(" priority > ? and ");
						}
						sql.append("contact_num <= ? AND is_alive = '1' AND channel=? ");
						//train_app使用
						//sql.append("order by option_time desc LIMIT 20");
	
						//train_app_new 使用
						sql.append("order by option_time asc LIMIT 20,20");
						
						int index = 1;
						cs = cn.prepareStatement(sql.toString());
						cs.setString(index++, Account.STATUS_FREE);
						cs.setInt(index++, WorkIdNum.book_num);
						if(WorkIdNum.limit_day==1){
							cs.setInt(index++, WorkIdNum.no_order_day);
						}
						cs.setInt(index++, WorkIdNum.contact_num);
						cs.setString(index, channel);
						logger.info(sql.toString()+"_"+channel+"_"+WorkIdNum.contact_num+"_"+WorkIdNum.limit_day+"_"+WorkIdNum.book_num+"_"+Account.STATUS_FREE);
						rs = cs.executeQuery();
						
						//查询当前常用联系人数限制下的账号没有，则修改当前常用联系人数限制，重新查询
						if(!rs.next()){
							sql = new StringBuffer();
							sql.append("select acc_id from cp_accountinfo where acc_status=? and book_num<=? and ");
							if(WorkIdNum.limit_day==1){
								sql.append(" priority > ? and ");
							}
							sql.append("contact_num <= ? AND is_alive = '1' AND channel=? ");
							sql.append("order by option_time asc LIMIT 15");
	
							index = 1;
							cs = cn.prepareStatement(sql.toString());
							cs.setString(index++, Account.STATUS_FREE);
							cs.setInt(index++, WorkIdNum.book_num);
							if(WorkIdNum.limit_day==1){
								cs.setInt(index++, WorkIdNum.no_order_day);
							}
							cs.setInt(index++, WorkIdNum.contact_num+10);
							cs.setString(index, channel);
							rs = cs.executeQuery();
						}
						
						while(rs.next()){
							if(queue.size()<=29){
								int acc_id = rs.getInt("acc_id");
								
								logger.info("==========>Save to "+channel+" acc pool acc_id:"+acc_id);
								//乐观锁，更新成功，则添加到队列中
								sql = new StringBuffer();
								sql.append("UPDATE cp_accountinfo SET acc_status=?,option_time=now() ");
								sql.append(" WHERE acc_id = ? AND acc_status='33' ");
								cs = cn.prepareStatement(sql.toString());  
								cs.setString(1, Account.STATUS_PLACING_ORDER);
								cs.setInt(2, acc_id);
								int up_result = cs.executeUpdate();
								
								if(up_result==1){
									logger.info("==========>Save to "+channel+" acc pool acc_id:"+acc_id+",success");
									queue.offer(acc_id);
								}
							}else{
								logger.info(channel+" acc pool is full");
								break;
							}
						}
					}
				}
				return 1;
			}
		});
	}
	
	
	
	
	/**
	 * 开始预订动作，更新订单状态
	 * 
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int startOrder(final String order_id) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql = new StringBuffer();
				sql.append("update cp_orderinfo c set c.order_status=? where c.order_id=? AND c.order_status=?");
				cs = cn.prepareStatement(sql.toString());	
				cs.setString(1, Order.STATUS_ORDER_ING);
				cs.setString(2, order_id);
				cs.setString(3, Order.STATUS_ORDER_INMQ);// 开始订购
				if(cs.executeUpdate()!=1){
					logger.info("this order is locked:"+order_id);
					return 1;
				}
				return 0;	
			}
		});
	}
	
	
	/**
	 * 异常处理 ，转人工处理
	 * 
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderToRg(final String order_id) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql = new StringBuffer();
				sql.append("update cp_orderinfo c set c.order_status=? where c.order_id=? AND c.order_status=?");
				cs = cn.prepareStatement(sql.toString());	
				cs.setString(1, Order.STATUS_ORDER_MANUAL);
				cs.setString(2, order_id);
				cs.setString(3, Order.STATUS_ORDER_ING);// 开始订购
				if(cs.executeUpdate()!=1){
				}
				return 0;	
			}
		});
	}
	
	
	
	/**
	 * 查询学生票附属下单信息
	 * */
	public int queryStudentInfo(final String order_id,final String cp_id,final String paramCp) throws RepeatException, DatabaseException {
		
		return dbbean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("select province_name,province_code,school_code,school_name,student_no,school_system,enter_year,preference_from_station_name,preference_from_station_code,preference_to_station_name,preference_to_station_code from  cp_orderinfo_student where order_id=? and cp_id=?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				cs.setString(index++, order_id);
				cs.setString(index++, cp_id);
				rs = cs.executeQuery();
				StringBuffer paramSb = new StringBuffer();
				paramSb.append(paramCp);
				if(rs.next()){
					paramSb.append("|").append(rs.getString(1))
					.append("|").append(rs.getString(2))
					.append("|").append(rs.getString(3))
					.append("|").append(rs.getString(4))
					.append("|").append(rs.getString(5))
					.append("|").append(rs.getString(6))
						.append("|").append(rs.getString(7))
					.append("|").append(rs.getString(8)==null?"":rs.getString(8))
					.append("|").append(rs.getString(9)==null?"":rs.getString(9))
					.append("|").append(rs.getString(10)==null?"":rs.getString(10))
					.append("|").append(rs.getString(11)==null?"":rs.getString(11));
				}
				param=paramSb.toString();
				return 1;
			}
		});
	}
	
	
	
	/**
	 * 预登入支付机器人
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int initPayAccountAndWorkerBy(final String orderId)throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// TODO 
				StringBuffer sql = null;
				ResultSet rs = null;
				
				
				// 获取随机出票人员ID
				Integer count = 1;
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append("select count(1) num from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? ");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, new Integer(3));
				cs.setString(2, Worker.STATUS_STOP); 
				cs.setString(3, Worker.STATUS_PREPARED); 
				
				rs = cs.executeQuery(); 
				while(rs.next()){
					count = rs.getInt("num");
				}
				int num = WorkIdNum.getNextNum(count);
				sql_rand = new StringBuffer();
				sql_rand.append("select worker_id,worker_name, worker_type,worker_ext ")
						.append("from cp_workerinfo where worker_type=? and worker_status <>? ")
						.append("and worker_status <>? order by worker_id limit ?,1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, new Integer(3));
				cs.setString(2, Worker.STATUS_STOP); 
				cs.setString(3, Worker.STATUS_PREPARED); 
				cs.setInt(4, num-1);
				rs = cs.executeQuery(); 
				while(rs.next()){
					worker = new Worker();
					//正常分配到的机器人参数
					worker.setWorkerId(rs.getInt(1));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(rs.getInt(3));
					worker.setWorkerExt(rs.getString(4));
				}
				logger.info("Login pay robot机器人:"+worker.getWorkerId()+",order_id:"+orderId);
			
				StringBuffer log_work = new StringBuffer();
				log_work.append("log_work: num:").append(num).append(" worker.getWorkerName()").append(worker.getWorkerName())
					.append(" work_url").append(worker.getWorkerExt());
				logger.info(log_work.toString());
				//start 更新订单状态为正在预定
				sql = new StringBuffer();
				
				sql.append("update cp_orderinfo set worker_id=?, option_time=now() where order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, worker.getWorkerId());
				cs.setString(2, orderId);
				
				cs.executeUpdate();
				return 0;
				
			}
		});
	}
	
	/**
	 * 机器人使用记录-开始
	 * @param worker
	 * @param order
	 * @param optType 1、预订 2、支付
	 * @return 插入记录的主键 int
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public Integer startWorkerReport(final Worker worker, final Order order, final String optType)throws RepeatException, DatabaseException {
		
		final StringBuilder builder = new StringBuilder();
		
		int ret = dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				ResultSet rs = null;
				
				if(worker == null || order == null) {
					return 1;
				}
				
				StringBuilder sql = new StringBuilder();
				Integer workerId = Integer.valueOf(worker.getWorkerId());
				String orderId = order.getOrderId();
				
				sql.append("insert into cp_workerinfo_report(worker_id,worker_name,order_id,request_time,create_time,opt_type) values")
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
				if(rs.next()) {
					builder.append(rs.getInt(1));
				} else {
					return 1;
				}
				return 0;
				
			}
		});
		if(ret == 1) {
			return null;
		} else {
			return Integer.valueOf(builder.toString());
		}
	}
	
	/**
	 * 机器人使用记录-结束
	 * @param worker
	 * @param order
	 * @param optType 1、预订 2、支付
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int endWorkerReport(final Integer reportId)throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				if(reportId == null) {
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
	
	/**
	 * 更新出票设备
	 * @param orderId
	 * @param device
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public void updateDevice(final String orderId, final Integer device) throws RepeatException, DatabaseException {
		
		logger.info("order_id : " + orderId + " ,device : " + device);
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection conn, PreparedStatement ps)
					throws SQLException {
				
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
			public int execute(Connection conn, PreparedStatement ps)
					throws SQLException {
				
				StringBuilder sql = new StringBuilder();
				sql.append("select order_status from cp_orderinfo where order_id = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, orderId);
				
				ResultSet rs = ps.executeQuery();
				if(rs != null && rs.next()) {
					builder.append(rs.getString("order_status"));
				}
				return 1;
			}
			
		});
		return builder.toString();
	}
	
	/**
	 * 修改订单状态
	 * @param orderId
	 * @param device
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public void updateOrderStatus(final String orderId, final String status) throws RepeatException, DatabaseException {
		
		logger.info("update status , order_id : " + orderId + ", status : " + status);
		dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection conn, PreparedStatement ps)
					throws SQLException {
				
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
	 * 查询系统设置值
	 * @param settingName
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public String getSysSettingValue(final String settingName)throws RepeatException, DatabaseException {
		
		final StringBuilder builder = new StringBuilder();
		dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				if(settingName == null || "".equals(settingName))
					return 1;
				
				StringBuilder sql = new StringBuilder();
				
				ResultSet rs = null;
				sql.append("select setting_value from train_system_setting where setting_name = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, settingName);
				
				rs = cs.executeQuery();
				if(rs.next()) {
					builder.append(rs.getString(1));
				}
				return 0;
				
			}
		});
		
		return builder.length() == 0 ? null : builder.toString();
	}
	
	/**
	 * 填充白名单账号变动表
	 * @param acc_id,minute,acc_username,acc_password,worker_ext
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public String updatePassAcc(final String acc_id,final String minute,final String acc_username,final String acc_password,final String worker_ext)throws RepeatException, DatabaseException {
		dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				int existCount = 0 ;
				StringBuilder sql = new StringBuilder();
				
				ResultSet rs = null;
				sql.append("SELECT  count(1) from cp_pass_acc where acc_id=? and UNIX_TIMESTAMP(now())-UNIX_TIMESTAMP(create_time)>?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, acc_id);
				cs.setString(2, minute);
				
				rs = cs.executeQuery();
				if(rs.next()) {
					existCount = rs.getInt(1);
					logger.info("existCount"+existCount);
					if(existCount == 0){
						sql = new StringBuilder();
						sql.append("insert into cp_pass_acc (acc_id,acc_username,acc_password,worker_ext,create_time) values(?,?,?,?,now())");
						cs = cn.prepareStatement(sql.toString());	
						cs.setString(1, acc_id);
						cs.setString(2, acc_username);
						cs.setString(3, acc_password);// 开始订购
						cs.setString(4, worker_ext);// 重发出票
						
						if(cs.executeUpdate()!=1){
							logger.info("insert cp_pass_acc success:"+acc_id);
							return 1;
						}
					}else{
						logger.info("账号"+minute+"分钟内已插入过");
					}
				}
				return 0;
			}
		});
		return null;
	}
	
	/**
	 * 查询订单表，获得包含到达城市与出发城市三字码的实体
	 * add by wangsf
	 * @return 
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public void queryOrderCity3c(final String fromCity,final String toCity) throws RepeatException, DatabaseException {

		 dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				//获取跟订单中的出发和到达城市相对应的三字码
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
					lh =rs.getString(2);
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
	 * 12306登录账号验证
	 */
	public String validateAccount(Account account) {

		Map params = new HashMap();
		String url = Consts.VALIDATE_ACCOUNT;
		params.put("command", "verifyAccount");
		params.put("channel", "19e");
		String username = account.getUsername();
		String pass = account.getPassword();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", username);
		jsonObject.put("pass", pass);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonObject);
		params.put("data", jsonArray.toString());
		String uri = UrlFormatUtil.createUrl("", params, "utf-8");
		String result = HttpUtil.sendByPost(url, uri, "UTF-8",30000,30000);
		// // {"ErrorCode":0,"ErrorInfo":[{"accId":"","robotNum":9,
		// // "retValue":"","retInfo":{“user_status”:”1”}}]}
		// // retValue为success时，帐号登陆成功，retInfo为帐号状态，如下：
		// // retValue为failure时，账号登陆失败，retInfo为原因
		// {"ErrorCode":7,"ErrorInfo":"脚本执行异常。"}
		JSONObject strObject = JSON.parseObject(result);
		String flag = strObject.getString("ErrorInfo").replace("\\", "").replace("[\"{", "[{").replace("}\"]", "}]");
		if (flag.startsWith("脚本")) {
			return "0";
		} else {
			JSONArray array = JSON
					.parseArray(flag);
			for (int i = 0; i < array.size(); i++) {
				Map maps = (Map) array.get(i);
				String results = maps.get("retValue").toString();
				String retInfo = maps.get("retInfo").toString();
				if ("success".equals(results)) {
					return "1";
				}
				return retInfo;
			}
		}
		return "0";
	}
			
}
	
	

