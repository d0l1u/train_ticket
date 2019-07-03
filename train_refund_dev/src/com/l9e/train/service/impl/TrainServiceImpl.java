
package com.l9e.train.service.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.po.OrderCP;
import com.l9e.train.po.ResultCP;
import com.l9e.train.po.TrainConsts;
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
	
	private Logger logger=Logger.getLogger(this.getClass());
	private DBBean dbbean = null;

	public List<OrderCP> list = null;
	public Worker worker = null;
	
	
	public Worker getWorker() {
		return worker;
	}

	public TrainServiceImpl() {
		dbbean = new DBBean();
	}

	/**
	 * 获取所有需要通知的列表
	 * @return
	 */
	public List<OrderCP> getOrderbill() {
		
		return this.list;
	}
	
	
	/**
	 * 查询需要通知的数据，并且进行更新
	 * @param logid 
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderbillByList(final int getNum, final String logid) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				logger.info(logid+"Select Wait Refund Order");
				list = new ArrayList<OrderCP>();
				
				//获取订单主键
				StringBuffer sql = new StringBuffer();
				sql.append("select c.order_id from cp_orderinfo_refund c where c.order_status=? AND (c.supplier_type ='00' OR c.supplier_type is null) ");
				sql.append(" group by order_id ORDER BY RAND() limit ?");
//				sql.append(" group by order_id order by c.from_time asc limit ?");
//				sql.append(" group by order_id order by c.alter_from_time asc limit ?");
//				sql.append(" order by c.alter_from_time asc limit ? ");
				
				logger.info(logid+"SQL:"+sql.toString());
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, OrderCP.STATUS_REFUND_START);//开始退票
				cs.setInt(2, getNum);
				ResultSet rs = cs.executeQuery();
				List<Map<String, String>> order_list = new ArrayList<Map<String, String>>();
				while(rs.next()){
					
					String orderId = rs.getString(1);
					//String cpId = rs.getString(2);
					
					//判断该订单内是否存在别的车票号是06“正在机器退票”状态的，若有则return，若无再发起请求
					sql = new StringBuffer();
//					sql.append("SELECT COUNT(1) FROM cp_orderinfo_refund WHERE order_id=? AND order_status=? AND cp_id<>? ");
					sql.append("SELECT COUNT(1) FROM cp_orderinfo_refund WHERE order_id=? AND order_status=? ");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, orderId); 
					cs.setString(2, OrderCP.STATUS_REFUND_ING);//06
//					cs.setString(3, cpId);
					rs = cs.executeQuery(); 
					while(rs.next()){
						int count = rs.getInt(1);
						
						if(count==0){
							Map<String, String> map = new HashMap<String, String>();
							map.put("order_id", orderId);
//							map.put("cp_id", cpId);
							order_list.add(map);
						}else{
							logger.info(logid+"订单号"+orderId+",存在06状态车票的数量是："+count);
						}
					}
				}
				
				for(Map<String, String> map : order_list){
					String order_id = map.get("order_id");
//					String cp_id = map.get("cp_id");
					String cp_id = "", refund_type="";
					//获取订单加锁
					sql = new StringBuffer();
					sql.append("select c.order_id,c.cp_id, c.refund_type from cp_orderinfo_refund c where c.order_status=? AND c.order_id=? ORDER BY RAND() limit 1");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, OrderCP.STATUS_REFUND_START);//开始退票04
					cs.setString(2, order_id);
//					cs.setString(3, cp_id);
					rs = cs.executeQuery();
					while(rs.next()){
						cp_id = rs.getString(2);
						refund_type = rs.getString(3);
					}
					logger.info(logid+"query start refund order_id:"+order_id+",cp_id:"+cp_id+",refund_type:"+refund_type);
					
					int num = 0;
					if("55".equals(refund_type)){//退票类型 11线上退票 55 改签退票
						logger.info("tc alter ---- order_id="+order_id+",cp_id="+cp_id);
						//更新订单车票信息为“正在退票”状态
						sql = new StringBuffer();
						sql.append("update cp_orderinfo_refund c set c.order_status=?,option_time=now(), ")
						.append(" alter_travel_time=travel_time, alter_train_box=train_box, alter_train_no=train_no, ")
						.append(" alter_seat_type=seat_type, alter_seat_no=seat_no, alter_buy_money = buy_money ")
						.append(" where c.order_id=? and c.cp_id=? and c.order_status=? ");
						
						cs = cn.prepareStatement(sql.toString());	
						cs.setString(1, OrderCP.STATUS_REFUND_ING);
						cs.setString(2, order_id);
						cs.setString(3, cp_id);	
						cs.setString(4, OrderCP.STATUS_REFUND_START);
						num = cs.executeUpdate();
						logger.info("tc alter ---- order_id="+order_id+",cp_id="+cp_id+"   success,num=" +num);
					}else{
						//更新订单车票信息为“正在退票”状态
						sql = new StringBuffer();
						sql.append("update cp_orderinfo_refund c set c.order_status=?, c.option_time=now() where c.order_id=? and c.cp_id=? and c.order_status=?");
						cs = cn.prepareStatement(sql.toString());	
						cs.setString(1, OrderCP.STATUS_REFUND_ING);
						cs.setString(2, order_id);
						cs.setString(3, cp_id);	
						cs.setString(4, OrderCP.STATUS_REFUND_START);
						num = cs.executeUpdate();
					}
					
					
					if(num == 1){//update执行成功
						logger.info(logid+"获取退票订单信息");
						//获取退票订单信息
//						sql = new StringBuffer();
//						sql.append("select c.order_id,c.cp_id,c.account_name,c.account_pwd,c.out_ticket_billno,c.out_ticket_time ");
//						sql.append(",c.user_name,c.ticket_type,c.ids_type,c.user_ids,c.channel,c.refund_money,c.alter_myself, ");
//						sql.append("c.alter_seat_type,c.alter_train_box,c.alter_train_no,c.alter_seat_no,c.order_status ");
//						sql.append(" from cp_orderinfo_refund c where c.order_id=? AND c.cp_id=? AND c.order_status=?");
						sql = new StringBuffer();
						sql.append("select c.order_id, ")
							.append(" c.cp_id, ")
							.append(" c.account_name, ")
							.append(" c.account_pwd, ")
							.append(" c.out_ticket_billno, ")
							.append(" c.out_ticket_time  , ")
							.append(" c.user_name, ")
							.append(" c.ticket_type, ")
							.append(" c.ids_type, ")
							.append(" c.user_ids, ")
							.append(" c.channel, ")
							.append(" c.refund_money, ")
							.append(" c.alter_myself, ")
							.append(" ifnull(c.alter_seat_type , c.seat_type), ")
							.append(" ifnull(c.alter_train_box , c.train_box), ")
							.append(" ifnull(c.alter_train_no , c.train_no), ")
							.append(" ifnull(c.alter_seat_no , c.seat_no), ")
							.append(" ifnull(c.alter_travel_time , c.travel_time), ")
							.append(" c.order_status ")
							.append(" from ")
							.append(" cp_orderinfo_refund c ")
							.append(" where ")
							.append(" c.order_id = ? ")
							.append(" AND c.cp_id = ? ")
							.append(" AND c.order_status = ? ");
						logger.info(logid + "QUERY SQL:" + sql.toString());
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, order_id);
						cs.setString(2, cp_id);
						cs.setString(3, OrderCP.STATUS_REFUND_ING);//正在退票06
						
						rs = cs.executeQuery();
						OrderCP order = null;
						while(rs.next()){
							int index = 1;
							order = new OrderCP();
							order.setOrderId(rs.getString(index++));
							order.setCpId(rs.getString(index++));
							order.setAccountName(rs.getString(index++));
							order.setAccountPwd(rs.getString(index++));
							order.setOutTicketBillno(rs.getString(index++));
							order.setOutTicketTime(DateUtil.stringToString(rs.getString(index++),DateUtil.DATE_FMT1));
							order.setUserName(rs.getString(index++));
							order.setTicketType(rs.getString(index++));
							order.setIdsType(rs.getString(index++));
							order.setIdsCard(rs.getString(index++));
							order.setChannel(rs.getString(index++));
							order.setRefundMoney(String.valueOf(rs.getBigDecimal(index++)));
							order.setAlterMyself(String.valueOf(rs.getInt(index++)));//是否是咱们自己改签的订单   0：否  1：是
							order.setSeatType(rs.getString(index++));
							order.setTrainBox(rs.getString(index++));
							order.setTrainNo(rs.getString(index++));
							order.setSeatNo(rs.getString(index++));
							order.setTrainDate(DateUtil.stringToString(rs.getString(index++),DateUtil.DATE_FMT1));
							order.setOrderStatus(rs.getString(index));
							list.add(order);
							
							logger.info(logid+"refund add list success:" + order_id + "|" + cp_id);
//							//更新订单车票信息为“正在退票”状态
//							sql = new StringBuffer();
//							sql.append("update cp_orderinfo_refund c set c.order_status=?,option_time=now() where c.order_id=? and c.cp_id=? and c.order_status=?");
//							cs = cn.prepareStatement(sql.toString());	
//							cs.setString(1, OrderCP.STATUS_REFUND_ING);
//							cs.setString(2, order_id);
//							cs.setString(3, order.getCpId());	
//							cs.setString(4, OrderCP.STATUS_REFUND_START);
//							
//							cs.executeUpdate();
						}
						logger.info(logid+"get the order_id info:"+order_id+",cp_id:"+cp_id);
					}
 				}
				logger.info(logid+"get orderbill size:"+list.size());
				return 0;	
			}
		});
	}
	/**
	 * 退票成功处理结果数据
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int updateCPOrderRefund(final String prefix , final Map<String,String> refund_map)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				StringBuffer sqlParam = new StringBuffer("[");
				//先判断渠道是否属于高铁，如果是，或者退款金额大于12306的退款金额，则退款金额要和12306保持一致
//				String channel = refund_map.get("channel");
//				logger.info(refund_map.get("order_id")+" 退票服务，订单渠道channel为："+channel);
//				String refundMoney = refund_map.get("refund_money");//退款金额
//				logger.info(refund_map.get("order_id")+" 退票服务，退款金额refundMoney为："+refundMoney);
//				String refund12306Money = refund_map.get("refund_12306_money");//12306退款金额
//				logger.info(refund_map.get("order_id")+" 退票服务，12306退款金额refund12306Money为："+refund12306Money);
//				double refundMoneyDouble = Double.parseDouble(refundMoney);
//				double refund12306MoneyDouble = Double.parseDouble(refund12306Money);
				
				//判断是否是咱们自己改签的订单，如果是，则退款金额按收费比例得出，如果不是，则退款金额要和12306退款金额保持一致
				String alterMyself = refund_map.get("alter_myself");//是否是咱们自己改签的订单   0：否  1：是
				
				String refund12306Money = refund_map.get("refund_12306_money");
				logger.info(prefix + "12306实际退票金额:" + refund12306Money);
				
				logger.info(prefix + "自己改签标识:" + alterMyself + " [ 0:否, 1:是](机器改签赚取差价)");
				
				sql.append("update cp_orderinfo_refund set refund_12306_money=?, refund_12306_seq=? ");
				if(!"1".equals(alterMyself)){
					sql.append(",refund_money=?");
					logger.info(prefix + "不是咱们自己改签的订单,开始更新咱们实际退款金额为12306实际退款金额!");
				}
				sql.append(" where order_id=? AND cp_id=?");
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, refund12306Money);
				sqlParam.append(refund12306Money).append(", ");
				
				String seq = refund_map.get("refund_12306_seq");
				cs.setString(index++, seq);
				sqlParam.append(seq).append(", ");
				
				if(!"1".equals(alterMyself)){
					cs.setString(index++, refund12306Money);
					sqlParam.append(refund12306Money).append(", ");
				}
				String orderId = refund_map.get("order_id");
				cs.setString(index++, orderId);
				sqlParam.append(orderId).append(", ");
				
				String cpId = refund_map.get("cp_id");
				cs.setString(index++, cpId);
				sqlParam.append(cpId).append("]");
				
				logger.info(prefix + "SQL:" + sql.toString() + ", PARAM:" + sqlParam.toString());
				cs.executeUpdate();
				return 0;
			}
		});
	}
	
	/**
	 * 退票成功处理
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int refundIsSuccess(final OrderCP order,final ResultCP resultCp)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				//sql.append("update cp_orderinfo_refund set order_status=?,alter_diff_money=buy_money-alter_buy_money ");
				sql.append("update cp_orderinfo_refund set order_status=? ");
				sql.append(" where order_id=? AND cp_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				if (StringUtils.equals(resultCp.getRetValue(), ResultCP.OVER)){
					cs.setString(index++, OrderCP.STATUS_REFUND_SUC);
				}else{
					//cs.setString(index++, OrderCP.STATUS_REFUND_FIN);
					cs.setString(index++, OrderCP.STATUS_REFUND_SUC);
				}
				cs.setString(index++, order.getOrderId());
				cs.setString(index++, order.getCpId());
				
				cs.executeUpdate();
				
				//用于统计数据
				sql = new StringBuffer();
				sql.append("insert into cp_count(source,channel,type,create_time,message,code) values(?,?,?,NOW(),?,?)");
				cs = cn.prepareStatement(sql.toString());	
				index = 1;
				cs.setString(index++, "train_refund_dev");
				cs.setString(index++, order.getChannel());
				cs.setString(index++, "04");
				cs.setString(index++, "退票成功！");
				cs.setString(index, "01");
				cs.executeUpdate();
				return 0;
			}
		});
	}
	/**
	 * 需要重新发送订单的处理
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int refundIsResend(final OrderCP order,final String cp_id)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("update cp_orderinfo_refund set order_status=?");
				sql.append(" where order_id=? AND cp_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, OrderCP.STATUS_REFUND_START);
				cs.setString(index++, order.getOrderId());
				cs.setString(index, cp_id);
				
				cs.executeUpdate();
				
				return 0;
			}
		});
	}
	
	
	
	
	/**
	 * 退票订单人工处理
	 * @param notify
	 * @param supplier
	 * @param respStatus
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int refundIsManual(final OrderCP order,final ResultCP resultCp)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("update cp_orderinfo_refund set order_status=?, return_optlog=? ");
				sql.append(" where order_id=? AND cp_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, OrderCP.STATUS_REFUND_MANUAL);
				cs.setString(index++, resultCp.getReturnOptlog());
				cs.setString(index++, order.getOrderId());
				cs.setString(index, resultCp.getCpId());
				cs.executeUpdate();
				
				return 0;
			}
		});
	}
	
	/**
	 * 给状态为“退票开始”的订单匹配机器人
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int selectPayAccountAndWorkerBy(final String orderId)throws RepeatException, DatabaseException {
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
					cs.setInt(1, Worker.Robot_refund); //退票机器人
					cs.setString(2, Worker.STATUS_STOP); //22、停用
					cs.setString(3, Worker.STATUS_SPARE); //33、备用
					
					rs = cs.executeQuery(); 
					while(rs.next()){
						count = rs.getInt("num");
					}
					int num = WorkIdNum.getNextNum(count);
					sql_rand = new StringBuffer();
					sql_rand.append("select worker_id, worker_name, worker_type, worker_ext, robot_id, worker_language_type, public_ip ")
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
						worker.setScript(rs.getString(6));
						worker.setPublicIp(rs.getString(7));
						logger.info(orderId+" 查询订单表的robot_id为空,随机获取一个机器的workerName为："+worker.getWorkerName());
					}
				}else{
					sql_rand = new StringBuffer();
					sql_rand.append("select worker_id, worker_name, worker_type, worker_ext, robot_id, worker_status, worker_language_type, public_ip ")
							.append("from cp_workerinfo where worker_type=? AND robot_id=?");
					cs = cn.prepareStatement(sql_rand.toString());
					cs.setInt(1, Worker.Robot_refund);
					cs.setString(2, robot_id); 
					rs = cs.executeQuery(); 
					logger.info(orderId + " rs为：" + rs);
					
					if(null != rs){
						while(rs.next()){
							String worker_status = rs.getString(6);//工作状态：00、工作中 11、空闲 22、停用 33、备用 44、占座中
							
							if(worker_status.equals(Worker.STATUS_STOP) || worker_status.equals(Worker.STATUS_SPARE)){//判断所选用的机器人的状态
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
								sql_rand.append("select worker_id, worker_name, worker_type, worker_ext, robot_id, worker_language_type, public_ip ")
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
									worker.setScript(rs.getString(6));
									worker.setPublicIp(rs.getString(7));
									logger.info(orderId+" 查询订单表的robot_id不为空,并且查询到机器的情况下workerName为："+worker.getWorkerName());
								}
								
							}else{//所选用的机器人状态不为22停用或33备用
								worker = new Worker();
								//正常分配到的机器人参数
								worker.setWorkerId(rs.getString(1));
								worker.setWorkerName(rs.getString(2));
								worker.setWorkerType(rs.getInt(3));
								worker.setWorkerExt(rs.getString(4));
								worker.setRobotId(rs.getString(5));
								worker.setScript(rs.getString(7));
								worker.setPublicIp(rs.getString(8));
								logger.info(orderId+" 查询订单表的robot_id不为空，并且查询到机器的情况下workerName为："+worker.getWorkerName());
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
						sql_rand.append("select worker_id, worker_name, worker_type, worker_ext, robot_id, worker_language_type, public_ip ")
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
							worker.setScript(rs.getString(6));
							worker.setPublicIp(rs.getString(7));
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
				//end
				
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
	public int insertHistory(final String orderId,final String cpId,final String optlog) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				//更新订单状态为正在预定
				StringBuffer sql = new StringBuffer();
				
				sql.append("insert into cp_orderinfo_refund_history (order_id,cp_id, order_optlog, create_time, opter) values(?, ?,?, now(),?)");
				
				ps = cn.prepareStatement(sql.toString());
				
				ps.setString(1, orderId);
				ps.setString(2, cpId);
				ps.setString(3, optlog);
				ps.setString(4, "refund app");
				
				ps.executeUpdate();
				
				return 0;
			}
		});
	}

	public int refundFailIsManual(final OrderCP order,final ResultCP resultCp)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("update cp_orderinfo_refund set order_status=?, return_optlog=? ");
				sql.append(" where order_id=? AND cp_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, OrderCP.STATUS_REFUND_MANUAL);
				cs.setString(index++, resultCp.getReturnOptlog());
				cs.setString(index++, order.getOrderId());
				cs.setString(index, resultCp.getCpId());
				
				cs.executeUpdate();
				//用于统计数据
				sql = new StringBuffer();
				sql.append("insert into cp_count(source,channel,type,create_time,message,code) values(?,?,?,NOW(),?,?)");
				cs = cn.prepareStatement(sql.toString());	
				index = 1;
				cs.setString(index++, "train_refund_dev");
				cs.setString(index++, order.getChannel());
				cs.setString(index++, "04");
				cs.setString(index++, "退票失败！");
				cs.setString(index, "02");
				cs.executeUpdate();
				return 0;
			}
		});
	}

	public int ishaveOtherCp(final OrderCP orderbill) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = null;
				ResultSet rs = null;
				sql = new StringBuffer();
				sql.append("SELECT COUNT(1) FROM cp_orderinfo_refund WHERE order_id=? AND order_status=? AND cp_id<>? ");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, orderbill.getOrderId()); 
				cs.setString(2, TrainConsts.ROBOT_REFUND_STATUS06);
				cs.setString(3, orderbill.getCpId());
				rs = cs.executeQuery(); 
				if(rs.next()){
					orderbill.setIsRefunding(rs.getInt(1)+"");
				}
				return 0;
			}
			
		});
	}

	//直接机器退款完成
	public int refundAutoSuccess(final OrderCP order,final ResultCP resultCp)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				//更改cp_orderinfo_refund表为退款完成状态
				StringBuffer sql = new StringBuffer();
				//sql.append("update cp_orderinfo_refund set order_status=?,alter_diff_money=buy_money-alter_buy_money ");
				sql.append("update cp_orderinfo_refund set order_status=? ");
				sql.append(" where order_id=? AND cp_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				if (StringUtils.equals(resultCp.getRetValue(), ResultCP.OVER)){//机器审核退款
					cs.setString(index++, OrderCP.STATUS_REFUND_AUTO);//改成退款成功
				}else{
					//cs.setString(index++, OrderCP.STATUS_REFUND_FIN);
					cs.setString(index++, OrderCP.STATUS_REFUND_AUTO);
				}
				cs.setString(index++, order.getOrderId());
				cs.setString(index++, order.getCpId());
				cs.executeUpdate();
				
				//更改cp_orderinfo_refund_notify表为准备通知状态
				sql = new StringBuffer();
				sql.append("UPDATE cp_orderinfo_refund_notify AS qo SET qo.notify_status=?, qo.notify_type=?, ");
				sql.append(" qo.notify_num=0, qo.notify_time=NOW() WHERE order_id=? AND cp_id=?");
				cs = cn.prepareStatement(sql.toString());	
				index = 1;
				cs.setString(index++, "1");//修改为准备通知
				cs.setInt(index++, 1);//修改退票结果通知
				cs.setString(index++, order.getOrderId());
				cs.setString(index++, order.getCpId());
				cs.executeUpdate();
				
				//用于统计数据
				sql = new StringBuffer();
				sql.append("insert into cp_count(source,channel,type,create_time,message,code) values(?,?,?,NOW(),?,?)");
				cs = cn.prepareStatement(sql.toString());	
				index = 1;
				cs.setString(index++, "train_refund_dev");
				cs.setString(index++, order.getChannel());
				cs.setString(index++, "04");
				cs.setString(index++, "退票成功！");
				cs.setString(index, "01");
				cs.executeUpdate();
				return 0;
			}
		});
	}

	//查询是否自带账号
	public int ishaveOwnAccount(final OrderCP orderbill)throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = null;
				ResultSet rs = null;
				sql = new StringBuffer();
				sql.append("SELECT account_from_way FROM cp_orderinfo WHERE order_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, orderbill.getOrderId()); 
				rs = cs.executeQuery(); 
				if(rs.next()){
					orderbill.setAccount_from_way(rs.getInt(1)+"");
				}
				return 0;
			}
			
		});
	}

	//拒绝退票，并自动通知
	public int refundIsRefuse(final OrderCP orderbill, final ResultCP resultCp) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				//更改cp_orderinfo_refund表为拒绝退票22状态
				StringBuffer sql = new StringBuffer();
				//sql.append("update cp_orderinfo_refund set order_status=?,alter_diff_money=buy_money-alter_buy_money ");
				sql.append("update cp_orderinfo_refund set order_status=?, refuse_reason=?, our_remark=? ");
				sql.append(" where order_id=? AND cp_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, OrderCP.STATUS_REFUND_REFUSE);//22
				cs.setString(index++, orderbill.getRefuse_reason());
				cs.setString(index++, orderbill.getOur_remark());
				cs.setString(index++, orderbill.getOrderId());
				cs.setString(index++, orderbill.getCpId());
				cs.executeUpdate();
				
				//更改cp_orderinfo_refund_notify表为准备通知状态
				sql = new StringBuffer();
				sql.append("UPDATE cp_orderinfo_refund_notify AS qo SET qo.notify_status=?, qo.notify_type=?, ");
				sql.append(" qo.notify_num=0, qo.notify_time=NOW() WHERE order_id=? AND cp_id=?");
				cs = cn.prepareStatement(sql.toString());	
				index = 1;
				cs.setString(index++, "1");//修改为准备通知
				cs.setInt(index++, 1);//修改退票结果通知
				cs.setString(index++, orderbill.getOrderId());
				cs.setString(index++, orderbill.getCpId());
				cs.executeUpdate();
				
				//用于统计数据
				sql = new StringBuffer();
				sql.append("insert into cp_count(source,channel,type,create_time,message,code) values(?,?,?,NOW(),?,?)");
				cs = cn.prepareStatement(sql.toString());	
				index = 1;
				cs.setString(index++, "train_refund_dev");
				cs.setString(index++, orderbill.getChannel());
				cs.setString(index++, "04");
				cs.setString(index++, "拒绝退票！");
				cs.setString(index, "01");
				cs.executeUpdate();
				return 0;
			}
			
		});
	}
		
}
