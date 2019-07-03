package com.l9e.train.supplier.service.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.supplier.po.Order;
import com.l9e.train.supplier.po.OrderCP;
import com.l9e.train.supplier.po.OrderNotify;
import com.l9e.train.supplier.po.OrderRefund;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;


public class OrderService {

	private Logger logger=Logger.getLogger(OrderService.class);
	DBBean bean =null;
	
	private StringBuffer accountStr;
	
	private String channel;
	
	public String getChannel() {
		return channel;
	}
	
	public Integer accID;
	
	public Integer getAccID() {
		return accID;
	}
	
	public OrderService(){
		//Config.setConfigResource();
		bean = new DBBean();
	}
	
	public StringBuffer getAccountStr() {
		return accountStr;
	}


	public int accountByOrderid(final String orderid) throws RepeatException, DatabaseException{
		return bean.executeMethod(new ICallBack(){

			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				String sql = "select a.acc_username, a.acc_password from cp_orderinfo o, cp_accountinfo a where o.account_id=a.acc_id and o.order_id=?";
				cs = cn.prepareStatement(sql);
				
				cs.setString(1, orderid);
				
				ResultSet rs = cs.executeQuery();
				
				accountStr = new StringBuffer();
				while(rs.next()){
					accountStr.append(rs.getString(1));
					accountStr.append("|");
					accountStr.append(rs.getString(2));
				}
				return 0;
			}} );
	}
	/**
	 * 收单接口
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int downBill(final Order supOrder) throws RepeatException, DatabaseException{
		logger.debug("start insert downbill........");  
		return bean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs)throws SQLException {

				/**
				 * 在往主订单插入前，先判断订单有没有自带12306账号密码，如果有，则先往cp_accountinfo表里插入一条数据，
				 * 并把插入的那条数据的acc_id赋给order实体类，如果没有则不赋值；
				 * 然后根据accountId是否有值，来确定在插入主订单表时是否插入account_id字段并赋值。
				 * 在OrderServlet中对accountId是否有值做具体的操作
				 * add by wangsf
				 */
				
				Integer accountId = supOrder.getAccountId();
				
				//车站三字码
				String fromCity3c = supOrder.getFromCity3c();
				String toCity3c = supOrder.getToCity3c();
				
				//插入主订单
				StringBuffer sql =  new StringBuffer();
				sql.append("insert into cp_orderinfo (order_id, order_name, pay_money, order_status, train_no,");
				sql.append("from_city, to_city, from_time, to_time, travel_time, create_time, seat_type, out_ticket_type,");
				sql.append("channel, ext_seattype, level, is_pay,manual_order,wait_for_order,option_time,from_3c,to_3c," +
						"seat_detail_type,choose_seats,my_order_id");
				
				if (null != accountId && 0 != accountId) {
					sql.append(",account_id"); // 如果accountId有值，则在插入语句中添加account_id字段
					sql.append(",account_from_way"); //如果12306自带账号，则往订单表中插入一个标识
				}
				
				
				sql.append(") VALUES");
				sql.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?, ?, ?, ?,?,?,?,now(),?,?,?,?,?");
				
				if (null != accountId && 0 != accountId) {
					sql.append(",?"); // 如果accountId有值，则在插入语句中为account_id赋值
					sql.append(",?");
				}
				
				sql.append(");");
				
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, supOrder.getOrderid());
				cs.setString(2, supOrder.getOrdername());
				cs.setString(3, supOrder.getPaymoney());
				logger.debug("supOrder.getManualorder()........"+supOrder.getManualorder());  
				if(null!=supOrder.getManualorder() && !"".equals(supOrder.getManualorder())&&"11".equals(supOrder.getManualorder().trim())){
					cs.setString(4, Order.STATUS_ORDER_MANUAL_OUTTICKET);
				}else{
					cs.setString(4, supOrder.getOrderstatus());
				}
				cs.setString(5, supOrder.getTrainno());
				cs.setString(6, supOrder.getFromcity());
				cs.setString(7, supOrder.getTocity());
				cs.setString(8, supOrder.getFromtime());
				cs.setString(9, supOrder.getTotime());
				cs.setString(10, supOrder.getTraveltime());
				cs.setString(11, supOrder.getSeattype());
				cs.setString(12, supOrder.getOuttickettype());
				cs.setString(13, supOrder.getChannel());
				cs.setString(14, supOrder.getExtseattype());
				cs.setString(15, StringUtils.isEmpty(supOrder.getLevel())?"0":supOrder.getLevel());
				if(null!=supOrder.getIspay() && !"".equals(supOrder.getIspay())){
					cs.setString(16, supOrder.getIspay());
				}else{
					cs.setString(16, "00");
				}
				if(null!=supOrder.getManualorder() && !"".equals(supOrder.getManualorder())){
					cs.setString(17, supOrder.getManualorder());
				}else{
					cs.setString(17, "00");
				}
				if(null!=supOrder.getWaitfororder() && !"".equals(supOrder.getWaitfororder())){
					cs.setString(18, supOrder.getWaitfororder());
				}else{
					cs.setString(18, "00");
				}
				
				cs.setString(19, fromCity3c); //出发城市三字码
				cs.setString(20, toCity3c);   //到达城市三字码
				cs.setString(21, supOrder.getSeatDetailType());//客户预选的卧铺位置
				cs.setString(22, supOrder.getChoose_seats());//客户预选的座位号
				cs.setString(23, supOrder.getMyOrderId());//客户预选的座位号
				
				if (null != accountId && 0 != accountId) {
					cs.setInt(24, accountId); // 如果accountId有值，则在插入语句中为account_id赋值
					cs.setInt(25, 1); //账号来源： 0：公司自有账号 ； 1：12306自带账号			
				}
				
				
				logger.debug("supOrder.getAccountId()........"+supOrder.getAccountId());
				int count=cs.executeUpdate();
				logger.info(supOrder.getOrderid()
						+ " order insert update : " + count);
				
				//插入车票表
				sql = new StringBuffer();
				sql.append("insert into cp_orderinfo_cp (cp_id, order_id, user_name, ticket_type, cert_type, cert_no, create_time, seat_type, pay_money)");
				sql.append("values (?, ?, ?, ?, ?, ?, now(), ?, ?)");
				
				cs = cn.prepareCall(sql.toString());
				for (OrderCP cp : supOrder.getOrderCPs()) {
					cs.setString(1, cp.getCpId());
					cs.setString(2, supOrder.getOrderid());
					cs.setString(3, cp.getUsername());
					cs.setInt(4, cp.getTrainType());
					cs.setInt(5, cp.getCertType());
					cs.setString(6, cp.getCertNo());
					cs.setInt(7, cp.getSeatType());
					cs.setString(8, cp.getPaymoney());
					cs.addBatch();
				}
				
				cs.executeBatch();
				
				
				//插入通知表数据
				sql = new StringBuffer();
				sql.append("insert into cp_orderinfo_notify (order_id, notify_num, create_time, notify_status, notify_url)");
				sql.append("values (?, 0, now(), ?, ?)");
				
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, supOrder.getOrderid());
				cs.setInt(2, OrderNotify.NOT);
				cs.setString(3, supOrder.getBackurl());
				cs.executeUpdate();
				
				logger.debug("end insert downbill........");  
				return 0;
			}
		});
		
	
	}
	
	/**
	 * 自带12306账号的订单绑定处理
	 * @param order
	 * @author:wangsf
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int bind12306Account(final Order order) throws RepeatException, DatabaseException {
		
		return bean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				
				String username = order.getUsername();
				String password = order.getPassword();
				String channel = "12306";// 存入cp_accountinfo表中以此区分channel为19e的账号
				logger.info(order.getOrderid()
						+ " start bind ext account12306 : " + username + ", "
						+ password + "," + channel);

				if (!StringUtils.isEmpty(username)
						&& !StringUtils.isEmpty(password)) {

					StringBuilder sql = new StringBuilder();
					ResultSet rs = null;
					// 检查账号是否已在账号表中存在
					boolean isExist = false;
					Integer existAccId = null;
					String existUserName = "";
					String existPassword = "";
					sql.append("select acc_id,acc_username,acc_password,channel from cp_accountinfo where acc_username = ? limit 1");
					ps = cn.prepareStatement(sql.toString());
					ps.setString(1, username);
					rs = ps.executeQuery();
					if (rs.next()) {
						existAccId = Integer.valueOf(rs.getInt(1));
						existUserName = rs.getString(2);
						existPassword = rs.getString(3);
						if (null != existAccId && 0 != existAccId
								&& null != existUserName && "" != existUserName
								&& null != existPassword && "" != existPassword) {
							isExist = true;
						}
					}

					if (isExist) {
						// 如果传入的账号在表中存在，但是密码不一致，则把表中已经存在的密码更新成传进来的新密码
						if (!(password.equals(existPassword))) {

							StringBuilder sqlUpdate = new StringBuilder();
							sqlUpdate.append("update cp_accountinfo set acc_password = ? where acc_id = ? and acc_username = ?");
							ps = cn.prepareStatement(sqlUpdate.toString());
							ps.setString(1, password);
							ps.setInt(2, existAccId);
							ps.setString(3, existUserName);
							int count= ps.executeUpdate();	
							logger.info(order.getOrderid()
									+ " 当传入的账号密码和库里的不一致时，更新密码成功次数为: " + count);
						}
						
						accID = existAccId;
						logger.info("传入的账号在表中存在的情况下accID为：" + accID);
					} else {

						// 将12306账号插入账号表

						StringBuilder sqlInsert = new StringBuilder();
						sqlInsert.append("insert into cp_accountinfo(acc_username, acc_password, acc_mail,");
					    sqlInsert.append(" channel, acc_status, opt_person, option_time,real_name,");
						sqlInsert.append(" at_province_id, create_time,account_source,is_alive,real_check_status,real_receive)");
						sqlInsert.append(" values (?,?,'temp@126.com', ?, '33', 'interface_app', NOW(),?,'000000', NOW(), '5','1','1','Y')");

						ps = cn.prepareStatement(sqlInsert.toString());
						ps.setString(1, username);
						ps.setString(2, password);
						ps.setString(3, channel);
						ps.setString(4, channel);
						int count = ps.executeUpdate();
						logger.info(order.getOrderid()
								+ " account insert update : " + count);

						if (count > 0) {
							StringBuilder sqlQuery = new StringBuilder();
							ResultSet rsQuery = null;
							sqlQuery.append("select acc_id,acc_username,acc_password,channel from cp_accountinfo ");
							sqlQuery.append(" where acc_username = ? and acc_password = ? and channel = ? limit 1");
							ps = cn.prepareStatement(sqlQuery.toString());
							ps.setString(1, username);
							ps.setString(2, password);
							ps.setString(3, channel);
							rsQuery = ps.executeQuery();

							if (rsQuery.next()) {
								accID = rsQuery.getInt(1);
							}
							logger.info("把12306用户名和密码插到表里后，对应记录的accID为："+ accID);
						}
					}

				}
				return 0;
			}
		});
	}
	
	/**
	 * 接受支付接口
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int payReturnInfo(final String order_id,final String pay_money) throws RepeatException, DatabaseException{
		
		return bean.executeMethod(new ICallBack() {
			ResultSet rs = null;
			@Override
			public int execute(Connection cn, PreparedStatement cs)throws SQLException {
				
				//更新订单状态
				StringBuffer sql =  new StringBuffer();
				
				sql.append("select order_status,channel from cp_orderinfo where order_id=?");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, order_id);
				rs = cs.executeQuery();
				while(rs.next()){
					String order_status = rs.getString(1);
					channel = rs.getString(2);
					
					if("45".equals(order_status)){
						if("tongcheng".equals(channel)||"elong".equals(channel)){
							sql = new StringBuffer();
							sql.append("update cp_orderinfo set is_pay = '00',order_status = '46',pay_money=? ");
							sql.append(" where order_id=? and order_status = '45' ");
							logger.info("sql:"+sql.toString());
							cs = cn.prepareCall(sql.toString());
							cs.setDouble(1, Double.valueOf(pay_money));
							cs.setString(2, order_id);
							cs.executeUpdate();
						}else{
							sql = new StringBuffer();
							sql.append("update cp_orderinfo set is_pay = '00',order_status = '55',pay_money=? ");
							sql.append(" where order_id=? and order_status = '45' ");
							logger.info("sql:"+sql.toString());
							cs = cn.prepareCall(sql.toString());
							cs.setDouble(1, Double.valueOf(pay_money));
							cs.setString(2, order_id);
							cs.executeUpdate();
						}
					}else{
						sql = new StringBuffer();
						sql.append("update cp_orderinfo set is_pay = '00',pay_money=? ");
						sql.append(" where order_id=?");
						logger.info("sql:"+sql.toString());
						cs = cn.prepareCall(sql.toString());
						cs.setDouble(1, Double.valueOf(pay_money));
						cs.setString(2, order_id);
						cs.executeUpdate();
					}
				}
				return 0;
			}
		});
	}

	/**
	 * 接收取消接口
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int canaleOrderInfo(final String order_id) throws RepeatException, DatabaseException{
		
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)throws SQLException {
				
				//更新订单状态
				StringBuilder sql = new StringBuilder();
				sql.append("select order_status from cp_orderinfo where order_id=?");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1,order_id);
				ResultSet rs = cs.executeQuery();
				rs= cs.executeQuery();
				String old_order_status="";
				if(rs.next()){
					old_order_status=rs.getString(1);
				}
				
				sql.setLength(0);
				
				//支付机器处理状态不予处理
				if(Order.STATUS_PAY_START.equals(old_order_status)||Order.STATUS_PAY_ING.equals(old_order_status)){
					logger.info("cancle fail order Are paying "+old_order_status);
					return 0;
				}
				
				//取消环节状态默认取消请求成功
				if(Order.OUT_TICKET_FAIL.equals(old_order_status)||Order.STATUS_CANCEL_FAIL.equals(old_order_status)
						||Order.STATUS_CANCEL_ING.equals(old_order_status)||Order.STATUS_CANCEL_START.equals(old_order_status)){
					logger.info("cancle success order is cancled "+old_order_status);
					return 1;
				}
				
				sql.append("update cp_orderinfo set order_status=?,return_optlog=? where order_id=?");
				cs = cn.prepareCall(sql.toString());
				if(Order.STATUS_ORDER_ING.equals(old_order_status)) {
					cs.setString(1, Order.STATUS_CANCEL_PRE);//准备取消
				} else {
					cs.setString(1, Order.STATUS_CANCEL_START);//开始取消
				}
				cs.setString(2, "C2");
				cs.setString(3, order_id);
				int num = cs.executeUpdate();
				
				logger.info("cancle db old_order_status"+old_order_status+",update_num"+num);
				sql.setLength(0);
				sql.append("insert into cp_orderinfo_history (order_id, order_optlog, create_time, opter) values(?, ?, now(), ?)");
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, order_id);
				cs.setString(2, "用户申请取消,原订单状态:"+old_order_status);
				cs.setString(3, "order app");
				
				cs.executeUpdate();
				
				return num;
			}
		});
	}

	
	/**
	 * 接收退款接口
	 * @param refund
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int refundOrderInfo(final OrderRefund refund) throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)throws SQLException {
				String logid = String.valueOf(System.nanoTime());
				logid = "[" + logid.substring(logid.length()-6)+"] ";
				logger.info(logid+"start insert refundOrderInfo........"); 
				StringBuffer sql =  new StringBuffer();
				ResultSet rs = null;
				/**
				 * 先判断该订单是否属于京东订单
				 */
				sql =  new StringBuffer();
				sql.append("select order_id,jd_order_id,jd_id,jd_order_no from jd_orderinfo where order_id=? and go_status=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, refund.getOrderid());
				cs.setString(2, "33");//处理状态 00:初始状态 11:请求出票成功 22:请求出票失败 33:出票流程结束 44:切入12306出票 55:开始查询结果
				rs = cs.executeQuery();
				
				String jdOrderId = null;//京东订单ID
				String jdOrderNo = null;//京东流水号
				int jdId = -1;//京东账号ID
				String jdAccountName = null;//京东账号名
				String jdAccountPwd = null;//京东账号密码
				
				while(rs.next()){
					jdOrderId = rs.getString(2);
					jdId = rs.getInt(3);
					jdOrderNo = rs.getString(4);
					logger.info(refund.getOrderid()+" 京东订单退票，京东订单ID为："+jdOrderId);
					logger.info(refund.getOrderid()+" 京东订单退票，京东账号ID为："+jdId);
					logger.info(refund.getOrderid()+" 京东订单退票，京东流水号为："+jdOrderNo);
					
					//查询京东账号，密码
					sql =  new StringBuffer();
					sql.append("select jd_id,account_name,account_pwd from jd_account where jd_id=?");
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, jdId);
					ResultSet rrs = cs.executeQuery();
					
					while(rrs.next()){
						jdAccountName = rrs.getString(2);
						jdAccountPwd = rrs.getString(3);
						
						//往cp_orderinfo_refund_jd表插入退款信息
						sql = new StringBuffer();
						sql.append("insert into cp_orderinfo_refund_jd (order_id,cp_id,jd_order_id,jd_account_name,jd_account_pwd, buy_money, refund_money,")
						.append("order_status,create_time,out_ticket_time,out_ticket_billno,train_no,")
						.append("from_station, arrive_station, from_time,travel_time,seat_type,")
						.append("ticket_type, train_box, seat_no, cert_type, cert_no,channel,user_name,")
						.append("refund_12306_seq, refund_seq, user_remark,refund_type) values ")
						.append(" (?, ?, ?, ?, ?, ?, ?,?,now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
						
						cs = cn.prepareStatement(sql.toString());
						int index = 1;
						
						cs.setString(index++, refund.getOrderid());
						cs.setString(index++, refund.getCpid());
						cs.setString(index++, jdOrderNo);//京东流水号
						cs.setString(index++, jdAccountName);
						cs.setString(index++, jdAccountPwd);
						cs.setString(index++, refund.getBuymoney());
						cs.setString(index++, refund.getRefundmoney());
						cs.setString(index++, OrderRefund.ORDER_STATUS00);//00：开始退票
						cs.setString(index++, refund.getOuttickettime());
						cs.setString(index++, refund.getOutticketbillno());
						cs.setString(index++, refund.getTrainno());
						cs.setString(index++, refund.getFromstation());
						cs.setString(index++, refund.getArrivestation());
						cs.setString(index++, refund.getFromtime());
						cs.setString(index++, refund.getTraveltime());
						cs.setString(index++, refund.getSeattype());
						cs.setString(index++, refund.getTickettype());
						cs.setString(index++, refund.getTrainbox());
						cs.setString(index++, refund.getSeatno());
						cs.setString(index++, refund.getIdstype());
						cs.setString(index++, refund.getUserids());
						cs.setString(index++, refund.getChannel());
						cs.setString(index++, refund.getUsername());
						cs.setString(index++, refund.getRefund12306seq());
						cs.setString(index++, refund.getRefundseq());
						cs.setString(index++, refund.getUserremark());
						cs.setString(index, refund.getRefundtype());
						
						int row = cs.executeUpdate();
						logger.info(refund.getOrderid()+" 京东订单退票，往cp_orderinfo_refund_jd表中插入退票信息，SQL执行次数为："+row);
						
						sql = new StringBuffer(); 
						sql.append("select count(1) num from cp_orderinfo_refund_notify where order_id=? and cp_id=?");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, refund.getOrderid());
						cs.setString(2, refund.getCpid());
						rs = cs.executeQuery();
						int num = 0;
						if(rs.next()){
							num = rs.getInt(1);
							index  = 1;
							if(num==1){
								sql =  new StringBuffer();
								sql.append("update cp_orderinfo_refund_notify set notify_status=0,notify_num=0,modify_time=now()  where order_id=? and cp_id=?");
								cs = cn.prepareStatement(sql.toString());
								cs.setString(index++, refund.getOrderid());
								cs.setString(index, refund.getCpid());
								cs.executeUpdate();
								return 0;
							}
						}
						//插入退款通知表
						sql = new StringBuffer();
						sql.append("insert into cp_orderinfo_refund_notify (order_id, cp_id, notify_num, ")
							.append("notify_time, notify_status, notify_url, create_time, notify_type)")
							.append("values (?, ?, ?, now(), ?, ?, now(), ?)");
						
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, refund.getOrderid());
						cs.setString(2, refund.getCpid());
						cs.setInt(3, OrderNotify.NOT);
						cs.setString(4, "0");//notify_status通知状态 0、未通知 1、等待通知 2、正在通知 3、通知成功 4、通知失败 5、重新通知
						cs.setString(5, refund.getBackurl());
						cs.setInt(6, 1);//notify_type 通知类别：1、出票成功通知
						
						cs.executeUpdate();		
					}
					return 0;
				}
				String orderId = refund.getOrderid();
				String cpid = refund.getCpid();
				logger.info(logid+"退票订单:"+orderId+", CP_ID:"+cpid);
				//查询出票的供货商。
				sql =  new StringBuffer();
				sql.append("SELECT supplier_type FROM cp_orderinfo WHERE order_id = ?");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, refund.getOrderid());
				rs = cs.executeQuery();
				String supplierType = "";
				if(rs.next()){
					supplierType = rs.getString(1);
				}
				logger.info(logid+"支付订单绑定供货商:"+supplierType);
				
				//TODO 
				
				//不是京东订单的退票入库流程
				sql =  new StringBuffer();
				sql.append("select count(1) num,order_status from cp_orderinfo_refund where order_id=? and cp_id=?");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, refund.getOrderid());
				cs.setString(2, refund.getCpid());
				rs = cs.executeQuery();
				int num = 0;
				String order_status = "00";
				if(rs.next()){
					num = rs.getInt(1);
					order_status = rs.getString(2);
					int index = 1;
					if(num==1 && OrderRefund.ORDER_STATUS22.equals(order_status)){
						sql =  new StringBuffer();
						sql.append("update cp_orderinfo_refund set order_status=?,refund_seq=?,user_remark=?,create_time=now(),account_pwd=?, supplier_type = ? where order_id=? and cp_id=?");
						cs = cn.prepareCall(sql.toString());
						cs.setString(index++, OrderRefund.ORDER_STATUS07);
						cs.setString(index++, refund.getRefundseq());
						cs.setString(index++, refund.getUserremark());
						cs.setString(index++, refund.getAccountpwd());
						cs.setString(index++, supplierType);
						cs.setString(index++, refund.getOrderid());
						cs.setString(index++, refund.getCpid());
						cs.executeUpdate();
						//插入日志
						sql = new StringBuffer();
						sql.append("insert into cp_orderinfo_refund_history (order_id, cp_id, order_optlog, ")
							.append("create_time, opter ) ")
							.append("values (?, ?, ?, now(), ?)");
						
						cs = cn.prepareCall(sql.toString());
						cs.setString(1, refund.getOrderid());
						cs.setString(2, refund.getCpid());
						cs.setString(3, "已拒绝订单重新发起退款申请，重置订单状态为人工退票，必须小主处理（注：机器无能，别找我）！");
						cs.setString(4, "interface_app");
						
						cs.executeUpdate();
						return 0;
					}
				}
				
				//插入主退款表
				logger.info(logid+"插入主退款表");
				sql =  new StringBuffer();
				sql.append("insert into cp_orderinfo_refund (order_id, cp_id,account_name,account_pwd, buy_money, refund_money, ")
						.append("order_status, create_time, travel_time, out_ticket_billno, out_ticket_time, train_no, alter_train_no,")
						.append("from_station, arrive_station, from_time,alter_from_time, alter_travel_time, seat_type, alter_seat_type, ")
						.append("ticket_type, train_box, seat_no, ids_type, user_ids, ")
						.append("channel, user_name, refund_12306_seq, refund_seq, user_remark,refund_type,supplier_type ) values ")
						.append(" (?, ?, ?, ?, ?, ?, ?, now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?,?,? );");
				
				cs = cn.prepareCall(sql.toString());
				int index = 1;
				cs.setString(index++, refund.getOrderid());
				cs.setString(index++, refund.getCpid());
				cs.setString(index++, refund.getAccountname());
				cs.setString(index++, refund.getAccountpwd());
				cs.setString(index++, refund.getBuymoney());
				cs.setString(index++, refund.getRefundmoney());
				
				if(StringUtils.isNotBlank(supplierType) || "01".equals(supplierType)){
					cs.setString(index++, OrderRefund.ORDER_STATUS04);
				}else{
					if("55".equals(refund.getRefundtype())){
						cs.setString(index++, OrderRefund.ORDER_STATUS04);//order_status 00：等待机器改签 01：重新机器改签 02：开始机器改签 03：机器改签失败 04：等待机器退票 05：重新机器退票06：开始机器退票 07：机器退票失败 11：退票完成 22：拒绝退票 33：审核退款
					}else{
						cs.setString(index++, OrderRefund.ORDER_STATUS00);//order_status 00：等待机器改签 01：重新机器改签 02：开始机器改签 03：机器改签失败 04：等待机器退票 05：重新机器退票06：开始机器退票 07：机器退票失败 11：退票完成 22：拒绝退票 33：审核退款
					}
				}
				
				
				cs.setString(index++, refund.getTraveltime());
				cs.setString(index++, refund.getOutticketbillno());
				cs.setString(index++, refund.getOuttickettime());
				cs.setString(index++, refund.getTrainno());
				cs.setString(index++, refund.getAltertrainno());
				cs.setString(index++, refund.getFromstation());
				cs.setString(index++, refund.getArrivestation());
				cs.setString(index++, refund.getFromtime());
				cs.setString(index++, refund.getAlterfromtime());
				cs.setString(index++, refund.getAltertraveltime());
				cs.setString(index++, refund.getSeattype());
				cs.setString(index++, refund.getAlterseattype());
				cs.setString(index++, refund.getTickettype());
				cs.setString(index++, refund.getTrainbox());
				cs.setString(index++, refund.getSeatno());
				cs.setString(index++, refund.getIdstype());
				cs.setString(index++, refund.getUserids());
				cs.setString(index++, refund.getChannel());
				cs.setString(index++, refund.getUsername());
				cs.setString(index++, refund.getRefund12306seq());
				cs.setString(index++, refund.getRefundseq());
				cs.setString(index++, refund.getUserremark());
				cs.setString(index++, refund.getRefundtype());
				cs.setString(index, supplierType);
				
				cs.executeUpdate();
				
				sql = new StringBuffer(); 
				sql.append("select count(1) num from cp_orderinfo_refund_notify where order_id=? and cp_id=?");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, refund.getOrderid());
				cs.setString(2, refund.getCpid());
				rs = cs.executeQuery();
				num = 0;
				if(rs.next()){
					num = rs.getInt(1);
					index  = 1;
					if(num==1){
						sql =  new StringBuffer();
						sql.append("update cp_orderinfo_refund_notify set notify_status=0,notify_num=0,modify_time=now()  where order_id=? and cp_id=?");
						cs = cn.prepareCall(sql.toString());
						cs.setString(index++, refund.getOrderid());
						cs.setString(index, refund.getCpid());
						cs.executeUpdate();
						return 0;
					}
				}
				//插入退款通知表
				logger.info(logid+"插入退款通知表");
				sql = new StringBuffer();
				sql.append("insert into cp_orderinfo_refund_notify (order_id, cp_id, notify_num, ")
					.append("notify_time, notify_status, notify_url, create_time, notify_type)")
					.append("values (?, ?, ?, now(), ?, ?, now(), ?)");
				
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, refund.getOrderid());
				cs.setString(2, refund.getCpid());
				cs.setInt(3, OrderNotify.NOT);
				cs.setString(4, "0");//notify_status通知状态 0、未通知 1、等待通知 2、正在通知 3、通知成功 4、通知失败 5、重新通知
				cs.setString(5, refund.getBackurl());
				cs.setInt(6, 1);//notify_type 通知类别：0、改签成功通知 1、出票成功通知
				
				cs.executeUpdate();
				
				logger.info(logid+"end insert refundOrderInfo........");  
				return 0;
			}
		});
	}
	
	/**
	 * 根据key值查询系统变量
	 * 
	 * @param key
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public String querySysVal(final String key) throws RepeatException,
			DatabaseException {
		
		final StringBuilder builder = new StringBuilder();
		bean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				StringBuffer sql = new StringBuffer();

				sql.append("SELECT setting_value FROM train_system_setting ")
						.append("WHERE setting_name=? ").append("LIMIT 0,1 ");

				ps = cn.prepareStatement(sql.toString());
				ps.setString(1, key);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					String sysVal = rs.getString("setting_value");
					builder.append(sysVal);
				}
				return 0;
			}
		});
		return builder.toString();
	}
}
