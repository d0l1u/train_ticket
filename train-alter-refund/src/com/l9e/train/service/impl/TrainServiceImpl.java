
package com.l9e.train.service.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.common.TrainConsts;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.WorkIdNum;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;





/**
 * @author wangdong
 * 
 * 
 */
public class TrainServiceImpl {
	
	private static Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);
	
	private DBBean dbbean = null;

	public List<Order> list = null;
	public Worker worker = null;

	public Worker getWorker() {
		return worker;
	}
	public TrainServiceImpl() {
		dbbean = new DBBean();
	}
	
    public Order order3c = null; //新增
	
	public Order getOrder3c() {
		return order3c;
	}
	
	/**
	 * 获取所有需要通知的列表
	 * @return
	 */
	public List<Order> getOrderbill() {
		return this.list;
	}
	
	
	/**
	 * 查询需要通知的数据，并且进行更新
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderbillByList(final int getNum) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				list = new ArrayList<Order>();
				
				//获取退款订单主键
				StringBuffer sql = new StringBuffer();
				sql.append("select c.order_id from cp_orderinfo_refund c where c.order_status=? AND (c.supplier_type ='00' OR c.supplier_type is null)");
				sql.append("group by c.order_id ORDER BY RAND() limit ?");
				//sql.append("group by c.order_id order by c.from_time asc limit ?");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, TrainConsts.ROBOT_REFUND_STATUS00);//00等待机器改签
				cs.setInt(2, getNum);
				ResultSet rs = cs.executeQuery();
				List<String> order_list = new ArrayList<String>();
				while(rs.next()){
					order_list.add(rs.getString(1));
				}
				
				for(String order_id:order_list){
					//获取订单加锁 start
					sql = new StringBuffer();
					sql.append("select c.order_id from cp_orderinfo_refund c where c.order_id=? AND c.order_status=? FOR UPDATE");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, order_id);
					cs.setString(2, TrainConsts.ROBOT_REFUND_STATUS00);//00等待机器改签
					rs = cs.executeQuery();
					if(!rs.next()){
						continue;
					}
					//获取订单加锁 end
					
					//获取订单信息 start
					sql = new StringBuffer();
					sql.append("select order_id, create_time, out_ticket_billno, train_no, alter_train_no, ")
						.append("from_station, arrive_station, from_time, travel_time, alter_travel_time, seat_type, alter_seat_type, channel, ")
						.append("account_name, account_pwd,DATE_FORMAT(DATE_SUB(c.from_time, INTERVAL 15 DAY),'%Y-%m-%d') AS from_time_fifd,")
						.append("DATE_FORMAT(DATE_SUB(c.from_time, INTERVAL 24 HOUR),'%Y-%m-%d %H:%i:%s') AS from_time_tfh,")
						.append("DATE_FORMAT(DATE_SUB(c.from_time, INTERVAL 48 HOUR),'%Y-%m-%d %H:%i:%s') AS from_time_feh ")
						.append(" from cp_orderinfo_refund c where c.order_id=? AND c.order_status=?  ");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, order_id);
					cs.setString(2, TrainConsts.ROBOT_REFUND_STATUS00);//00等待机器改签
					rs = cs.executeQuery();
					int index=1;
					if(rs.next()){
						Order order = new Order();
						order.setOrderId(rs.getString(index++));
						order.setCreateTime(rs.getString(index++));
						order.setOutTicketBillno(rs.getString(index++));
						order.setTrainNo(rs.getString(index++));
						order.setAlterTrainNo(rs.getString(index++));
						order.setFromStation(rs.getString(index++));
						order.setArriveStation(rs.getString(index++));
						order.setFromTime(rs.getString(index++));
						order.setTravelTime(DateUtil.stringToString(rs.getString(index++), DateUtil.DATE_FMT1));
						order.setAlterTravelTime(rs.getString(index++));
						order.setSeatType(rs.getString(index++));
						order.setAlterSeatType(rs.getString(index++));
						order.setChannel(rs.getString(index++));
						order.setAccountName(rs.getString(index++));
						order.setAccountPwd(rs.getString(index++));
						order.setFrom_time_fifd(rs.getString(index++));
						order.setFrom_time_tfh(rs.getString(index++));
						order.setFrom_time_feh(rs.getString(index++));
						order.setBuy_money("0");
						sql = new StringBuffer();
						sql.append("select cp_id, buy_money, alter_buy_money, order_status, ")
							.append("option_time, opt_ren, ticket_type, train_box, alter_train_box, seat_no, alter_seat_no, ")
							.append("ids_type, user_name, user_ids, error_info, return_optlog ")
							.append("from cp_orderinfo_refund cp ")
							.append("where cp.order_id=? AND cp.order_status=?");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, order.getOrderId());//订单号
						cs.setString(2, TrainConsts.ROBOT_REFUND_STATUS00);//00等待机器改签
						
						rs = cs.executeQuery();
						List<OrderCP> cps = new ArrayList<OrderCP>();
						while(rs.next()){
							OrderCP cp = new OrderCP();
							cp.setCpId(rs.getString(1));
							cp.setBuyMoney(rs.getString(2));
							cp.setAlterBuyMoney(rs.getString(3));
							cp.setOrderStatus(rs.getString(4));
							cp.setOptionTime(rs.getString(5));
							cp.setOptRen(rs.getString(6));
							cp.setTicketType(rs.getString(7));
							cp.setTrainBox(rs.getString(8));
							cp.setAlterTrainBox(rs.getString(9));
							cp.setSeatNo(rs.getString(10));
							cp.setAlterSeatNo(rs.getString(11));
							cp.setIdsType(rs.getString(12));
							cp.setUserName(rs.getString(13));
							cp.setUserIds(rs.getString(14));
							cp.setErrorInfo(rs.getString(15));
							cp.setReturnOptlog(rs.getString(16));
							
							if(Double.valueOf(cp.getBuyMoney()) > Double.valueOf(order.getBuy_money())){
								order.setBuy_money(cp.getBuyMoney());
							}
							cps.add(cp);
							
							sql = new StringBuffer();
							sql.append("update cp_orderinfo_refund set order_status=? , option_time=now() where order_id=? ")
								.append(" AND cp_id=? and order_status=?");
							cs = cn.prepareStatement(sql.toString());
							cs.setString(1, TrainConsts.ROBOT_REFUND_STATUS02);//02开始机器改签
							cs.setString(2, order.getOrderId());//订单号
							cs.setString(3, cp.getCpId());//车票号
							cs.setString(4, TrainConsts.ROBOT_REFUND_STATUS00);//00等待机器改签
							
							cs.executeUpdate();
						}
						order.setCps(cps);
						
						list.add(order);
					}else{
						continue;
					}
					logger.info("get the alert_orderinfo:"+order_id);
				}
				logger.info("get orderbill size:"+list.size());
				return 0;	
			}
		});
	}
	
	/**
	 * 超时订单人工处理
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int payIsManual(final Order order)throws RepeatException, DatabaseException{
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
				while(rs.next()){
					count = rs.getInt("num");
				}
				int num = WorkIdNum.getNextNum(count);
				sql_rand = new StringBuffer();
				sql_rand.append("select worker_id, worker_name, worker_type, worker_ext, public_ip, worker_language_type ")
						.append("from cp_workerinfo where worker_type=? and worker_status <>? ")
						.append("and worker_status <>? order by worker_id limit ?,1");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setInt(1, Worker.Robot_pay);
				cs.setString(2, Worker.STATUS_STOP); 
				cs.setString(3, Worker.STATUS_SPARE); 
				cs.setInt(4, num-1);
				rs = cs.executeQuery(); 
				while(rs.next()){
					worker = new Worker();
					//正常分配到的机器人参数
					worker.setWorkerId(rs.getString(1));
					worker.setWorkerName(rs.getString(2));
					worker.setWorkerType(rs.getInt(3));
					worker.setWorkerExt(rs.getString(4));
					worker.setPublicIp(rs.getString(5));
					worker.setScript(rs.getString(6));
				}
				StringBuffer log_work = new StringBuffer();
				log_work.append("log_work: num:").append(num).append(" worker.getWorkerName()").append(worker.getWorkerName())
					.append(" work_url").append(worker.getWorkerExt());
				logger.info(log_work.toString());
				//start 更新员工处理量
				StringBuffer sql = new StringBuffer();
				
				sql.append("update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, worker.getWorkerId());
				cs.executeUpdate();
				//end
				
				
				
				return 0;
			}
		});
	}
	
	/**
	 * 发送成功后订单的处理
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int payIsSuccess(final Order order, final Result result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				logger.info(order.getOrderId()+" workettype:"+result.getWorker().getWorkerType());
				StringBuffer sql = null;
				logger.info(order.getOrderId()+" update cp_orderinfo");
				//开始发送通知给前台
				sql = new StringBuffer();
				sql.append("update cp_orderinfo_notify set notify_status = ?, notify_num=0, notify_next_time=DATE_ADD(now(), interval 20 second), modify_time=now() where order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, "1");
				cs.setString(2, order.getOrderId());
				
				cs.executeUpdate();
				//开始发送通知 end
				
				return 0;
				
			}
		});

	}

	

	
	
	
	/**
	 * 给状态为“开始机器改签02”的订单匹配处理人和账号信息
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int selectPayAccountAndWorkerBy(final String orderId)throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = null;
				ResultSet rs = null;
				// 获取随机出票人员ID
				Integer count = 1;
				StringBuffer sql_rand = new StringBuffer();
				sql_rand = new StringBuffer();
				sql_rand.append("select robot_id from cp_orderinfo_refund where order_id=? AND robot_id IS NOT NULL group by order_id");
				cs = cn.prepareStatement(sql_rand.toString());
				cs.setString(1, orderId); 
				rs = cs.executeQuery(); 
				String robot_id = "";
				if(rs.next()){
					robot_id = rs.getString("robot_id");
				}
				logger.info(orderId+" 查询订单表的robot_id为："+robot_id);
				
				if("".equals(robot_id)){
					sql_rand = new StringBuffer();
					sql_rand.append("select count(1) num from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? ");
					cs = cn.prepareStatement(sql_rand.toString());
					cs.setInt(1, Worker.Robot_alert); //改签机器人
					cs.setString(2, Worker.STATUS_STOP); //22、停用
					cs.setString(3, Worker.STATUS_SPARE); //33、备用
					
					rs = cs.executeQuery(); 
					while(rs.next()){
						count = rs.getInt("num");
					}
					int num = WorkIdNum.getNextNum(count);
					sql_rand = new StringBuffer();
					sql_rand.append("select worker_id, worker_name, worker_type, worker_ext, robot_id, public_ip, worker_language_type ")
							.append("from cp_workerinfo where worker_type=? and worker_status <>? ")
							.append("and worker_status <>? order by worker_id limit ?,1");
					cs = cn.prepareStatement(sql_rand.toString());
					cs.setInt(1, Worker.Robot_alert);
					cs.setString(2, Worker.STATUS_STOP); 
					cs.setString(3, Worker.STATUS_SPARE); 
					cs.setInt(4, num-1);
					rs = cs.executeQuery(); 
					while(rs.next()){
						worker = new Worker();
						//正常分配到的机器人参数
						worker.setWorkerId(rs.getString(1));
						worker.setWorkerName(rs.getString(2));
						worker.setWorkerType(rs.getInt(3));
						worker.setWorkerExt(rs.getString(4));
						worker.setRobotId(rs.getString(5));
						worker.setPublicIp(rs.getString(6));
						worker.setScript(rs.getString(7)); 
						logger.info(orderId+" 查询订单表的robot_id为空,随机获取一个机器的workerName为："+worker.getWorkerName());
					}
				}else{
					sql_rand = new StringBuffer();
					sql_rand.append("select worker_id, worker_name, worker_type, worker_ext, robot_id, worker_status, public_ip, worker_language_type ")
							.append("from cp_workerinfo where worker_type=? AND robot_id=?");
					cs = cn.prepareStatement(sql_rand.toString());
					cs.setInt(1, Worker.Robot_alert);
					cs.setString(2, robot_id); 
					rs = cs.executeQuery(); 
					logger.info(orderId+" rs为："+rs);
					
					if(null != rs){
					while(rs.next()){
						String worker_status = rs.getString(6);//工作状态：00、工作中 11、空闲 22、停用 33、备用 44、占座中
						if(worker_status.equals(Worker.STATUS_STOP) || worker_status.equals(Worker.STATUS_SPARE)){//判断所选用的机器人的状态
							sql_rand = new StringBuffer();
							sql_rand.append("select count(1) num from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? ");
							cs = cn.prepareStatement(sql_rand.toString());
							cs.setInt(1, Worker.Robot_alert); //改签机器人
							cs.setString(2, Worker.STATUS_STOP); //22、停用
							cs.setString(3, Worker.STATUS_SPARE); //33、备用
							
							rs = cs.executeQuery(); 
							while(rs.next()){
								count = rs.getInt("num");
							}
							int num = WorkIdNum.getNextNum(count);
							sql_rand = new StringBuffer();
							sql_rand.append("select worker_id, worker_name, worker_type, worker_ext, robot_id, public_ip, worker_language_type ")
									.append("from cp_workerinfo where worker_type=? and worker_status <>? ")
									.append("and worker_status <>? order by worker_id limit ?,1");
							cs = cn.prepareStatement(sql_rand.toString());
							cs.setInt(1, Worker.Robot_alert);
							cs.setString(2, Worker.STATUS_STOP); 
							cs.setString(3, Worker.STATUS_SPARE); 
							cs.setInt(4, num-1);
							rs = cs.executeQuery(); 
							while(rs.next()){
								worker = new Worker();
								//正常分配到的机器人参数
								worker.setWorkerId(rs.getString(1));
								worker.setWorkerName(rs.getString(2));
								worker.setWorkerType(rs.getInt(3));
								worker.setWorkerExt(rs.getString(4));
								worker.setRobotId(rs.getString(5));
								worker.setPublicIp(rs.getString(6));
								worker.setScript(rs.getString(7));
							}
						}else{//所选用的机器人状态不为22停用或33备用
							worker = new Worker();
							//正常分配到的机器人参数
							worker.setWorkerId(rs.getString(1));
							worker.setWorkerName(rs.getString(2));
							worker.setWorkerType(rs.getInt(3));
							worker.setWorkerExt(rs.getString(4));
							worker.setRobotId(rs.getString(5));
							//worker_status
							worker.setPublicIp(rs.getString(7));
							worker.setScript(rs.getString(8));
						}
						
						
					}
				  }
					if(null == worker){
					    sql_rand = new StringBuffer();
						sql_rand.append("select count(1) num from cp_workerinfo where worker_type=? and worker_status <>? and worker_status <>? ");
						cs = cn.prepareStatement(sql_rand.toString());
						cs.setInt(1, Worker.Robot_refund); //退票机器人
						cs.setString(2, Worker.STATUS_STOP); //22、停用
						cs.setString(3, Worker.STATUS_SPARE); //33、备用
						
						rs = cs.executeQuery(); 
						while(rs.next()){
							count = rs.getInt("num");
						}
						int num = WorkIdNum.getNextNum(count);
						sql_rand = new StringBuffer();
						sql_rand.append("select worker_id, worker_name, worker_type, worker_ext, robot_id, public_ip, worker_language_type ")
								.append("from cp_workerinfo where worker_type=? and worker_status <>? ")
								.append("and worker_status <>? order by worker_id limit ?,1");
						cs = cn.prepareStatement(sql_rand.toString());
						cs.setInt(1, Worker.Robot_refund);
						cs.setString(2, Worker.STATUS_STOP); 
						cs.setString(3, Worker.STATUS_SPARE); 
						cs.setInt(4, num-1);
						rs = cs.executeQuery(); 
						while(rs.next()){
							worker = new Worker();
							//正常分配到的机器人参数
							worker.setWorkerId(rs.getString(1));
							worker.setWorkerName(rs.getString(2));
							worker.setWorkerType(rs.getInt(3));
							worker.setWorkerExt(rs.getString(4));
							worker.setRobotId(rs.getString(5));
							worker.setPublicIp(rs.getString(6));
							worker.setScript(rs.getString(7)); 
							logger.info(orderId+" 查询订单表的robot_id不为空，但是查询不到机器的情况下，随机查询一个机器的workerName为："+worker.getWorkerName());
						} 
				  }
				}
				StringBuffer log_work = new StringBuffer();
				log_work.append("log_alert_work:").append(" worker.getWorkerName()").append(worker.getWorkerName())
					.append(" work_url").append(worker.getWorkerExt());
				logger.info(log_work.toString());
				
				//更新订单改签机器人
				sql = new StringBuffer();
				sql.append("update cp_orderinfo_refund set robot_id=? where order_id = ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, worker.getRobotId());
				cs.setString(2, orderId);
				cs.executeUpdate();
				
				//start 更新员工处理量
				sql = new StringBuffer();
				sql.append("update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1 where worker_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, worker.getWorkerId());
				cs.executeUpdate();
				//end 更新员工处理量
				
				return 0;
			}
		});
	}
	
	/**
	 * 插入历史记录
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int insertHistory(final String order_id, final String cp_id, final String optlog) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("insert into cp_orderinfo_refund_history (order_id, cp_id, order_optlog, create_time, opter) values(?, ?, ?, now(), ?)");
				ps = cn.prepareStatement(sql.toString());
				ps.setString(1, order_id);
				ps.setString(2, cp_id);
				ps.setString(3, optlog);
				ps.setString(4, "alert app");
				ps.executeUpdate();
				return 0;
			}
		});
	}

	
	/**
	 * 添加账号日志
	 * @param cn
	 * @param cs
	 * @param acc_name
	 * @param orderId
	 * @param opt_logs
	 */
	public void addAccLog(Connection cn, PreparedStatement cs, final String acc_name, final String orderId, final String opt_logs){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO account_logs(acc_username,order_id,opt_logs,create_time,opt_person )VALUES(?,?,?,NOW(),'pay app')");
		
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
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

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

	//更改退款表的订单状态
	public int updateOrderStatus(final Map<String,String> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				int index = 1;
				logger.info(map.get("order_id")+" updateOrderStatus map:" + map);
				if("ALL".equals(map.get("type"))){
					sql.append("UPDATE cp_orderinfo_refund SET order_status=?");
					if(null!=map.get("return_optlog")){
						sql.append(",return_optlog=?");
					}
					//新增 是否咱们自己改签的订单标识字段
					if(null != map.get("alter_myself")){
					sql.append(",alter_myself=?");
					}
					sql.append(",option_time=NOW() WHERE order_id=? AND order_status=? ");
					logger.info(map.get("order_id")+" sql of update:" + sql.toString());
					cs = cn.prepareStatement(sql.toString());	
					logger.info(map.get("order_id")+" update orderid:"+map.get("order_id")+" status:"+map.get("new_order_status"));
					cs.setString(index++, map.get("new_order_status"));
					
					if(null!=map.get("return_optlog")){
						cs.setString(index++, map.get("return_optlog"));
					}
					
					//新增 是否咱们自己改签的订单标识字段
					if(null != map.get("alter_myself")){
					cs.setInt(index++, Integer.parseInt(map.get("alter_myself")));//是否是咱们自己改签的订单   0：否  1：是
					}
					
					cs.setString(index++, map.get("order_id"));
					cs.setString(index, map.get("order_status"));
					int row = cs.executeUpdate();
					logger.info(map.get("order_id")+" 更新状态受影响的条数为：" + row);
					//更新改签结果通知表
					sql = new StringBuffer();
					sql.append("UPDATE cp_orderinfo_refund_notify SET notify_type=0, notify_status=1,notify_time=now(),notify_next_time=now() WHERE order_id=?");
					cs = cn.prepareStatement(sql.toString());	
					cs.setString(1, map.get("order_id"));
					cs.executeUpdate();
					
					if("refund".equals(map.get("stract"))){
						//更新改签结果通知表
						sql = new StringBuffer();
						sql.append("UPDATE cp_orderinfo_refund SET alter_seat_type=seat_type, alter_train_box=train_box,alter_seat_no=seat_no WHERE order_id=?");
						cs = cn.prepareStatement(sql.toString());	
						cs.setString(1, map.get("order_id"));
						cs.executeUpdate();
					}
				}else{
					sql.append("UPDATE cp_orderinfo_refund SET order_status=? ");
					if(null!=map.get("return_optlog")){
						sql.append(",return_optlog=?");
					}
					
					//新增 是否咱们自己改签的订单标识字段
					if(null != map.get("alter_myself")){
					sql.append(",alter_myself=?");
					}
					
					sql.append(",option_time=NOW() WHERE order_id=? AND cp_id=? AND order_status=? ");
					logger.info("sql of update:" + sql.toString());
					index = 1;
					cs = cn.prepareStatement(sql.toString());	
					logger.info("update orderid:"+map.get("order_id")+" status:"+map.get("new_order_status"));
					cs.setString(index++, map.get("new_order_status"));
					if(null!=map.get("return_optlog")){
						cs.setString(index++, map.get("return_optlog"));
					}
					
					//新增 是否咱们自己改签的订单标识字段
					if(null != map.get("alter_myself")){
					cs.setInt(index++, Integer.parseInt(map.get("alter_myself")));//是否是咱们自己改签的订单   0：否  1：是
					}
					
					cs.setString(index++, map.get("order_id"));
					cs.setString(index++, map.get("cp_id"));
					cs.setString(index, map.get("order_status"));
					int row = cs.executeUpdate();
					logger.info("else update order_id" + map.get("order_id") + " the row is " + row);
					
					sql = new StringBuffer();
					sql.append("UPDATE cp_orderinfo_refund_notify SET notify_type=0, notify_status=1,notify_time=now(),notify_next_time=now() WHERE order_id=? and cp_id=? ");
					cs = cn.prepareStatement(sql.toString());	
					cs.setString(1, map.get("order_id"));
					cs.setString(2, map.get("cp_id"));
					cs.executeUpdate();
					
					if("refund".equals(map.get("stract"))){
						//更新改签结果通知表
						sql = new StringBuffer();
						sql.append("UPDATE cp_orderinfo_refund SET alter_seat_type=seat_type, alter_train_box=train_box,alter_seat_no=seat_no WHERE order_id=? and cp_id=?");
						cs = cn.prepareStatement(sql.toString());	
						cs.setString(1, map.get("order_id"));
						cs.setString(2, map.get("cp_id"));
						cs.executeUpdate();
					}
				}
				
				//用于统计数据
				if("true".equals(map.get("statistical"))){
					//插入日志
					sql = new StringBuffer();
					sql.append("insert into cp_count(source,channel,type,create_time,message,code) values(?,?,?,NOW(),?,?)");
					cs = cn.prepareStatement(sql.toString());	
					index = 1;
					cs.setString(index++, "train_alter_dev");
					cs.setString(index++, map.get("channel"));
					cs.setString(index++, "03");
					if(TrainConsts.ROBOT_REFUND_STATUS03.equals(map.get("new_order_status"))){
						cs.setString(index++, "改签失败！");
						cs.setString(index, "02");
					}else if(TrainConsts.ROBOT_REFUND_STATUS04.equals(map.get("new_order_status"))){
						cs.setString(index++, "改签成功！");
						cs.setString(index, "01");
					}else{
						cs.setString(index++, "改签一次！");
						cs.setString(index, "00");
					}
					cs.executeUpdate();
				}
				return 0;
			}
		});
	}
	
	
	//更新改签信息
	public int updateOrderAlter(final Map<String,String> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				logger.info("update orderid:"+map.get("order_id")+" 参数map为："+map);
				sql.append("UPDATE cp_orderinfo_refund SET alter_buy_money=?, alter_train_no=?, alter_from_time=?,  ")
					.append("alter_travel_time=?, alter_seat_type=?, alter_train_box=?, alter_seat_no=?, refund_percent=?,alter_myself=? ")
					.append("WHERE order_id=? AND cp_id=? ");
				cs = cn.prepareStatement(sql.toString());	
				logger.info(map.get("order_id")+" sql of update:" + sql.toString());
				
				cs.setString(1, map.get("alter_pay_money"));
				cs.setString(2, map.get("alter_train_no"));
				cs.setString(3, map.get("alter_from_time"));
				cs.setString(4, map.get("alter_travel_time"));
				cs.setString(5, map.get("alter_seat_type"));
				cs.setString(6, map.get("alter_train_box"));
				cs.setString(7, map.get("alter_seat_no"));
				cs.setString(8, map.get("refund_percent"));
				cs.setInt(9, Integer.parseInt(map.get("alter_myself")));//是否是咱们自己改签的订单   0：否  1：是
				cs.setString(10, map.get("order_id"));
				cs.setString(11, map.get("cp_id"));
				
				int row=cs.executeUpdate();
				logger.info(map.get("order_id")+" 更新次数row为:" + row);
				return 0;
			}
		});
	}
	
	//更新改签信息
	public int updateOrderAlterMoney(final Map<String,String> map) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				logger.info("update orderid alter_money:"+map.get("order_id"));
//				sql.append("UPDATE cp_orderinfo_refund SET alter_diff_money=buy_money-alter_buy_money ")
				sql.append("UPDATE cp_orderinfo_refund SET alter_diff_money=? ")
					.append("WHERE order_id=? AND cp_id=? ");
				cs = cn.prepareStatement(sql.toString());	
				
				cs.setString(1, map.get("alter_diff_money"));
				cs.setString(2, map.get("order_id"));
				cs.setString(3, map.get("cp_id"));
				
				cs.executeUpdate();
				return 0;
			}
		});
	}
	
	//查询是否自带账号
	public int ishaveOwnAccount(final Order order)throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = null;
				ResultSet rs = null;
				sql = new StringBuffer();
				sql.append("SELECT account_from_way FROM cp_orderinfo WHERE order_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, order.getOrderId()); 
				rs = cs.executeQuery(); 
				if(rs.next()){
					order.setAccount_from_way(rs.getInt(1)+"");
				}
				return 0;
			}
			
		});
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

}
