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
import com.l9e.train.service.TrainService;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.DBBeanUtil;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * 同程改签TrainService
 * @author licheng
 *
 */
public class TrainServiceTCCImpl implements TrainService {
	
	private Logger logger=Logger.getLogger(this.getClass());
	private DBBean dbbean = null;

	public List<Order> list = null;
	public Account account = null;
	public Worker worker = null;
	
	public TrainServiceTCCImpl() {
		dbbean = new DBBean();
	}

	public Order order3c = null; // 新增

	public Order getOrder3c() {
		return order3c;
	}
	@Override
	public void addAccLog(Connection cn, PreparedStatement cs, String accName,
			String orderId, String optLogs) {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO account_logs(acc_username,order_id,opt_logs,create_time,opt_person )VALUES(?,?,?,NOW(),'cancel app')");
		
		try {
			cs = cn.prepareStatement(sql.toString());
			cs.setString(1, accName);
			cs.setString(2, orderId);
			cs.setString(3, optLogs);
			cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("add account log error", e);
		}

	}

	@Override
	public int findAccountAndWorker(final Order order) throws RepeatException,
			DatabaseException {
		//获取出票账号
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				
				
				//获取出票人员
				StringBuffer sql = new StringBuffer();
				
				sql.append("select worker_id, worker_name, "//
						+ "worker_type, worker_ext, "//
						+ "public_ip, worker_language_type "//
						+ "from cp_workerinfo where worker_type=? and worker_status = ? limit 1");
				logger.info("query worker : " + order.getOrderId());
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setInt(1, Worker.CANCEL_ROBOT);
				cs.setString(2, Worker.STATUS_WORKING);
				
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
				
				//获取出票账号
				sql = new StringBuffer();
				sql.append("select a.acc_id, a.acc_username, a.acc_password from cp_accountinfo a join elong_orderinfo_change o on a.acc_id=o.account_id where o.change_id=?");
				logger.info("query account : " + order.getOrderId());
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, order.getChangeId());
				rs = cs.executeQuery();
				DBBeanUtil util = new DBBeanUtil();
			
				List<Account> alist = util.rs2List(rs, "com.l9e.train.po.Account");
				logger.info("alist size:"+alist.size());
				if(alist.size()>0){//获取到账号
					account = alist.get(0);
					logger.info("tongcheng取消改签 account:"+account.getAccUsername()+" password:"+account.getAccPassword());
				}else{
					return 1;
				}
				
				return 0;
			}
		});
		
	}

	@Override
	public Account getAccount() {
		return account;
	}

	@Override
	public List<Order> getOrderbill() {
		
		return this.list;
	}

	@Override
	public Worker getWorker() {
		return worker;
	}

	@Override
	public int insertAccountHistory(final AccountLog log) throws RepeatException,
			DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement ps)throws SQLException {
	        StringBuffer sql = new StringBuffer();
	        sql.append("select acc_name,order_id,cz_type,create_time,opter from cp_account_refund_cancel_log where acc_name=? and order_id=? and cz_type=?");
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

	@Override
	public int insertHistory(final String orderId, final Integer changeId, final String optlog)
			throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				//更新订单状态为正在预定
				StringBuffer sql = new StringBuffer();
				
				sql.append("insert into elong_change_logs (order_id, change_id, content, create_time, opt_person) values(?, ?, ?, now(), ?)");
				
				ps = cn.prepareStatement(sql.toString());
				
				ps.setString(1, orderId);
				ps.setInt(2, changeId);
				ps.setString(3, optlog);
				ps.setString(4, "tc_cancel_robot");
				
				ps.executeUpdate();
				
				return 0;
			}
		});
	}

	@Override
	public int orderIsFail(final Order order, final Result result) throws RepeatException,
			DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				//更新系统订单
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("update elong_orderinfo_change set change_status=?, option_time=now() where change_id=?");
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, Order.STATUS_TCC_CANCEL_FAILURE);//取消失败
					
				cs.setInt(2, order.getChangeId());
				
				cs.executeUpdate();
					
				return 0;
			}
		});
	}
	
	/**
	 * 当订单已确认改签时，不能取消，直接失败，并更新错误码
	 * @param order
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int cancelFailUpdateStatus(final Order order) throws RepeatException,
			DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				//更新系统订单
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("update elong_orderinfo_change set change_status=?");
				sql.append(", option_time=now()");
				sql.append(", fail_reason=?");
				sql.append(" where change_id=? ");
				logger.info("cancel alterOrder failure,sql of update:" + sql.toString());
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, Order.STATUS_TCC_CANCEL_FAILURE);//取消失败
				cs.setString(2, Order.FAILCODE_VERIFYALTER);// 324 : 已确认改签，不可取消
				cs.setInt(3, order.getChangeId());
				
				cs.executeUpdate();
					
				return 0;
			}
		});
	}

	@Override
	public int orderIsManual(final Order order, final Result result)
			throws RepeatException, DatabaseException {
		/*这个方法好像在取消功能中没有使用?*/
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				//更新订单状态
				
//				StringBuffer sql = new StringBuffer();
//				
//				sql.append("select worker_id, worker_name, worker_type,worker_ext from cp_workerinfo where worker_type = ? and worker_status<>?  order by rand() limit 1");
//				
//				cs = cn.prepareStatement(sql.toString());
//				cs.setInt(1, Worker.ORDER_Manual);
//				cs.setString(2, Worker.STATUS_STOP);
//				
//				ResultSet rs = cs.executeQuery();
//				
//				DBBeanUtil util = new DBBeanUtil();
//				
//				List<Worker> list = util.rs2List(rs, "com.l9e.train.po.Worker");
//				
//				Worker worker = null;
//				if(list.size()>0){
//					worker = list.get(0);
//				}else{
//					return 2;
//				}
//				
//				/*change_id*/
//				queryLatestChangeId(order.getOrderId(), cn, cs);
//				
//				//更新订单状态为正在预定
//				sql = new StringBuffer();
//				
//				sql.append("update elong_orderinfo_change set worker_id=?, change_status=?, option_time=now() where change_id=?");
//				
//				cs = cn.prepareStatement(sql.toString());
//				
//				cs.setString(1, worker.getWorkerId());
//				cs.setString(2, Order.STATUS_TCC_CHANGE_FAILURE);
//				cs.setInt(3, change_id);
//				
//				cs.executeUpdate();
//				
//				
//				//start 更新员工处理量
//				sql = new StringBuffer();
//				
//				sql.append("update cp_workerinfo set order_num=order_num+1 where worker_id=?");
//				
//				cs = cn.prepareStatement(sql.toString());
//				
//				cs.setString(1, worker.getWorkerId());
//				cs.executeUpdate();
//				//end
//				
//				
//				
//				//暂停账号
//				sql = new StringBuffer();
//				
//				sql.append("update cp_accountinfo set acc_status=?, option_time=now() where acc_id=? and order_id=?");
//				
//				cs = cn.prepareStatement(sql.toString());
//				
//				cs.setString(1, Account.STOP);
//				cs.setInt(2, order.getAccountId());
//				cs.setString(3, order.getOrderId());
//				
//				cs.executeUpdate();
//				
				return 0;
			}
		});
	}

	@Override
	public int orderIsResend(final Order order, final Result result)
			throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				//更新系统订单
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("update elong_orderinfo_change set change_status=?, option_time=now() where change_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, Order.STATUS_TCC_CANCEL_START);//开始取消
					
				cs.setInt(2, order.getChangeId());
				
				cs.executeUpdate();
				
				return 0;
			}
		});
	}

	@Override
	public int orderIsSuccess(final Order order, final Result result)
			throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				logger.info("[orderIsSuccess]订单号为：" + order.getOrderId() + "的订单取消成功，开始更新数据库！");
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("update elong_orderinfo_change set change_status=?,  option_time=now() ,opt_ren=? where change_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, Order.STATUS_TCC_CANCEL_SUCCESS);
				cs.setString(2, "cancel_robot");
				cs.setInt(3, order.getChangeId());
				
				logger.info("[orderIsSuccess]将订单号为："+ order.getOrderId() + "的订单状态更新为取消成功：" + Order.STATUS_TCC_CANCEL_SUCCESS);
				cs.executeUpdate();
				
				String account_user = "";
				int account_num = 0;
				//查询账号信息
				sql = new StringBuffer();
				
				sql.append("SELECT acc_username,contact_num FROM cp_accountinfo where acc_id=? for update");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, order.getAccountId());
				ResultSet rs = cs.executeQuery();
				
				if(rs.next()){
					account_num = rs.getInt("contact_num");
					account_user = rs.getString("acc_username");
				}
				logger.info("acc_id:"+order.getAccountId()+";account_num:"+account_num+";account_user:"+account_user);
				if(account_num==20){
					//账号停用
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status=?,order_id='',opt_person='cancle app',stop_reason=? where acc_id=?");
					
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Account.STOP);
					cs.setString(2, "3");
					cs.setInt(3, order.getAccountId());
					
					cs.executeUpdate();
				}else{
					//重发订单释放账号
					sql = new StringBuffer();
					sql.append("update cp_accountinfo set acc_status=?, order_id='' where acc_id=?");
					
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Account.FREE);
					cs.setInt(2, order.getAccountId());
					
					cs.executeUpdate();
				}
				
				
				//添加操作账号日志
				logger.info("释放 12306 account:"+account_user);
				String opt_logs = "cancel order释放账号";
				addAccLog(cn, cs, account_user, order.getOrderId(), opt_logs);
				
				return 0;
				
			}
		});

	}

	@Override
	public int orderbillByList(final int getNum) throws RepeatException,
			DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				//获取订单信息
				StringBuffer sql = new StringBuffer();
				sql.append("select c.order_id, c.change_status, c.out_ticket_billno, c.account_id,c.change_id,c.from_city,c.to_city " +
						" from elong_orderinfo_change c where c.change_status=? AND (c.supplier_type ='00' OR c.supplier_type is null) ");
				sql.append("ORDER BY c.create_time DESC limit 0,?");
			
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Order.STATUS_TCC_CANCEL_START);
				cs.setInt(2, getNum);
				
				ResultSet rs = cs.executeQuery();
				
				list = new ArrayList<Order>();
				while(rs.next()){
					Order order = new Order();
					order.setOrderId(rs.getString(1));
					order.setOrderStatus(rs.getString(2));
					order.setOutTicketBillNo(rs.getString(3));
					order.setAccountId(rs.getInt(4));
					order.setChangeId(rs.getInt(5));
					order.setFromCity(rs.getString(6));//出发站
					order.setToCity(rs.getString(7));//到达站
					list.add(order);
				}
				
				logger.info("tongcheng改签取消list："+list.size());
				
				//获取车票信息
				for (Order order : list) {
					sql = new StringBuffer();
					sql.append("select cp_id, CONCAT(user_name, '|',ticket_type, '|',ids_type, '|',user_ids, '|',seat_type) from elong_change_cp where change_id=?");
				
					PreparedStatement cpCs = cn.prepareStatement(sql.toString());
					cpCs.setInt(1, order.getChangeId());
					
					ResultSet cpRs = cpCs.executeQuery();
					
					while(cpRs.next()){
						order.addOrderCp(cpRs.getString(2));
					}
					
				}
				
				
				//更新订单为正在取消
				sql = new StringBuffer();
				
				sql.append("update elong_orderinfo_change set change_status=?, option_time=now() where change_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				for(Order order: list){
					//更新订单状态为正在取消
					cs.setString(1, Order.STATUS_TCC_CANCEL_ING);
					cs.setInt(2, order.getChangeId());
					
					cs.addBatch();
				}
				
				cs.executeBatch();
				
				return 0;	
			}
		});
	}
	
//	/**
//	 * 查询最新的一个change_id,查询条件order_id
//	 * @param order_id
//	 * @return
//	 * @throws DatabaseException 
//	 * @throws RepeatException 
//	 */
//	private int queryLatestChangeId(final String order_id, Connection cn, PreparedStatement ps){
//		
//		try {
//			logger.info("start query change_id , order_id : " + order_id);
//			
//			StringBuffer sql = new StringBuffer();
//			sql.append("select o.change_id from elong_orderinfo_change o where o.order_id = ? order by create_time desc limit 0,1");
//			
//			logger.info("before query change_id prepare: " + order_id);
//			ps = cn.prepareStatement(sql.toString());
//			ps.setString(1, order_id);
//			
//			ResultSet rs = ps.executeQuery();
//			if(rs.next()) {
//				change_id =  rs.getInt(1);
//			}
//			logger.info(" order id : " + order_id + ", change_id : " + change_id);
//			return 0;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			logger.error("get change_id error", e);
//		}
//		return 0;
//	}

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
