
package com.l9e.train.service.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.l9e.train.po.WorkerVo;
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
	
	private Logger logger=Logger.getLogger(this.getClass());
	private DBBean dbbean = null;

	public List<Order> list = null;
	public Account account = null;
	public Worker worker = null;
	public PayCard payCard = null;
	
	
	public PayCard getPayCard() {
		return payCard;
	}

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
	 * @return
	 */
	public List<Order> getOrderbill() {
		// TODO Auto-generated method stub
		
		return this.list;
	}
	
	
	/**
	 * 查询需要通知的数据，并且进行更新
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int orderbillByList(final int getNum,final int time) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				list = new ArrayList<Order>();
				
				//获取订单主键
				StringBuffer sql = new StringBuffer();
				sql.append("select c.order_id from cp_orderinfo c where c.order_status=? ");
				sql.append("AND c.out_ticket_time< DATE_SUB(NOW(), INTERVAL ? MINUTE) ");
				sql.append("order by c.out_ticket_time asc limit ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, Order.STATUS_PAY_START);//开始支付
				cs.setInt(2, time);
				cs.setInt(3, getNum);
				ResultSet rs = cs.executeQuery();
				List<String> order_list = new ArrayList<String>();
				while(rs.next()){
					order_list.add(rs.getString(1));
				}
				
				for(String order_id:order_list){
					//获取订单加锁
					sql = new StringBuffer();
					sql.append("select c.order_id from cp_orderinfo c where c.order_status=? AND c.order_id=? FOR UPDATE");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, Order.STATUS_PAY_START);//开始支付
					cs.setString(2, order_id);
					rs = cs.executeQuery();
					if(!rs.next()){
						continue;
					}
					
					//获取订单信息
					sql = new StringBuffer();
					//在此新增订单出票时间(out_ticket_time)，列车发车时间(from_time)，订单级别(level)3个字段. add by wangsf
					sql.append("select c.order_id,c.order_status,c.account_id, c.buy_money, c.out_ticket_billno," +
							"c.out_ticket_time,c.from_time,c.level,is_click_button ");
					sql.append(" from cp_orderinfo c where c.order_id=? AND c.order_status=? ");
					
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, order_id);
					cs.setString(2, Order.STATUS_PAY_START);//开始支付
					
					rs = cs.executeQuery();
					
					if(rs.next()){
						Order order = new Order();
						order.setOrderId(rs.getString(1));
						order.setOrderStatus(rs.getString(2));
						order.setAccountId(rs.getInt(3));
						order.setBuymoney(rs.getString(4));
						order.setOutTicketBillNo(rs.getString(5));
						order.setOutTicketTime(rs.getTimestamp(6)); //下单时间
						order.setFromTime(rs.getTimestamp(7)); //列车发车时间
						order.setLevel(rs.getString(8));  //订单级别
						order.setIsClickButton(rs.getString(9));//点击某个按钮的标识：00：批量支付按钮  11：批量反支按钮
						
						sql = new StringBuffer();
						sql.append("select cp.user_name,cp.cert_no from cp_orderinfo_cp cp where cp.order_id=? limit 1");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, order.getOrderId());//订单号
						
						rs = cs.executeQuery();
						while(rs.next()){
							order.setUserName(rs.getString(1));
						}
						list.add(order);
					}else{
						continue;
					}
					//更新订单信息为“正在支付”状态
					sql = new StringBuffer();
					sql.append("update cp_orderinfo c set c.order_status=? where c.order_id=? and c.order_status=?");
					cs = cn.prepareStatement(sql.toString());	
					logger.info("update orderid:"+order_id+" status:"+Order.STATUS_PAY_ING);
					cs.setString(1, Order.STATUS_PAY_ING);
					cs.setString(2, order_id);
					cs.setString(3, Order.STATUS_PAY_START);
					
					if(cs.executeUpdate()!=1){
						continue;
					}
					logger.info("get the pay_orderinfo:"+order_id);
				}
				logger.info("get orderbill size:"+list.size());
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
	public int payIsResend(final Order order)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// TODO Auto-generated method stub
				//更新系统订单
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("SELECT count(ho.order_id) FROM cp_orderinfo ho where timediff(now() , ho.create_time)<'00:10:00'  and ho.order_id=?");

				cs = cn.prepareCall(sql.toString());
				cs.setString(1, order.getOrderId());
			
				ResultSet rs = cs.executeQuery();
				rs.next();
				int size =rs.getInt(1);

				if(size==0){//如果时间不充裕，进入人工处理
					//status = Order.STATUS_ORDER_MANUAL;
					try {
						
						insertHistory(order.getOrderId(),"订单超过10分钟进入人工支付。");
						
						return payIsManual(order);
					} catch (RepeatException e) {
						e.printStackTrace();
					} catch (DatabaseException e) {
						e.printStackTrace();
					}
				}

				sql = new StringBuffer();
				sql.append("update cp_orderinfo set order_status=?,");
				if(null!=order.getPaybillno() && !"".equals(order.getPaybillno())){
					sql.append("bank_pay_seq=?,");
				}
				sql.append("option_time=now() where order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, Order.STATUS_PAY_MANUAL);
				if(null!=order.getPaybillno() && !"".equals(order.getPaybillno())){
					cs.setString(index++, order.getPaybillno());
				}
				cs.setString(index++, order.getOrderId());
				
				cs.executeUpdate();
				
				
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
				// TODO Auto-generated method stub
				//更新订单状态
				
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
				sql_rand.append("select worker_id,worker_name, worker_type,worker_ext ")
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
					
				}
				StringBuffer log_work = new StringBuffer();
				log_work.append("log_work: num:").append(num).append(" worker.getWorkerName()").append(worker.getWorkerName())
					.append(" work_url").append(worker.getWorkerExt());
				logger.info(log_work.toString());
				
				//更新订单状态为人工预定
				StringBuffer sql = new StringBuffer();
				
				sql.append("update cp_orderinfo set worker_id=?, order_status=?,");
				if(null!=order.getPaybillno() && !"".equals(order.getPaybillno())){
					sql.append("bank_pay_seq=?,");
				}
				sql.append("option_time=now() where order_id=?");
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(index++, worker.getWorkerId());
				cs.setString(index++, Order.STATUS_PAY_MANUAL);
				if(null!=order.getPaybillno() && !"".equals(order.getPaybillno())){
					cs.setString(index++, order.getPaybillno());
				}
				cs.setString(index++, order.getOrderId());
				
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
				
				logger.info(order.getOrderId()+" workername:"+result.getWorker().getWorkerName() + " cardNo:" + result.getPayCard().getCardNo());
				
				StringBuilder sql = new StringBuilder();
				
				logger.info(order.getOrderId()+" update cp_orderinfo");
				
				sql.append("update cp_orderinfo set order_status=?, option_time=now(), pay_time=now(),");
				if(null!=order.getPaybillno() && !"".equals(order.getPaybillno())){
					sql.append("bank_pay_seq=?,");
				}
				sql.append("out_ticket_account=?, opt_ren=? where order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				int index = 1;
				cs.setString(index++, Order.STATUS_BILL_SUCCESS);
				cs.setString(index++, result.getPaybillno());
				if(null!=order.getPaybillno() && !"".equals(order.getPaybillno())){
					cs.setString(index++, order.getPaybillno());
				}
				cs.setString(index++, "robot");
				cs.setString(index++, order.getOrderId());
				
				cs.executeUpdate();
				
				logger.info(order.getOrderId()+" update cp_orderinfo_cp");
				
				
				//开始发送通知给前台
				sql.setLength(0);
				sql.append("update cp_orderinfo_notify set notify_status = ?, notify_num=0, notify_next_time=now(), modify_time=now() where order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, "1");
				cs.setString(2, order.getOrderId());
				
				cs.executeUpdate();
				//开始发送通知 end
				
				String account_user = "";
				int account_num = 0;
				int book_num = 0;
				//查询账号信息
				
				sql.setLength(0);
				sql.append("SELECT acc_username,contact_num,book_num FROM cp_accountinfo where acc_id=? and order_id=? limit 0,1");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, order.getAccountId());
				cs.setString(2, order.getOrderId());
				
				ResultSet rs = cs.executeQuery();
				
				if(rs.next()){
					account_user = rs.getString("acc_username");
					account_num = rs.getInt("contact_num");
					book_num = rs.getInt("book_num");
				}
				
				if(account_num>=99){
					//账号释放
					
					sql.setLength(0);
					sql.append("update cp_accountinfo set acc_status=?,order_id='',opt_person='pay app',stop_reason=? where acc_id=? and order_id=?");
					
					cs = cn.prepareStatement(sql.toString());
					
					cs.setString(1, Account.STOP);
					cs.setString(2, "3");
					cs.setInt(3, order.getAccountId());
					cs.setString(4, order.getOrderId());
					
					cs.executeUpdate();
				}else{
					//账号释放
					sql.setLength(0);
					sql.append("update cp_accountinfo set acc_status=?, order_id=?,opt_person='pay app' ");
					if(book_num>=WorkIdNum.book_num){
						sql.append(",stop_reason='5' ");
						
						sql.append(",book_num=4 ");
					}
					sql.append(" where acc_id=? and order_id=?");
					
					cs = cn.prepareStatement(sql.toString());
					index = 1;
					if(book_num>=WorkIdNum.book_num){
						cs.setString(index++, Account.FREE);
						logger.info("accountinfo linshi  stop !!"+order.getAccountId());
					}else{
						cs.setString(index++, Account.FREE);	
					}
					cs.setString(index++, order.getOrderId());
					cs.setInt(index++, order.getAccountId());
					cs.setString(index, order.getOrderId());
					
					cs.executeUpdate();
				}
				
				//账号释放 end
				
				
				//添加操作账号日志
				logger.info("release 12306 account:"+account_user);
				String opt_logs = "pay order释放账号";
				addAccLog(cn, cs, account_user, order.getOrderId(), opt_logs);
				
				
				
				return 0;
				
			}
		});

	}

	

	
	
	
	/**
	 * 给状态为“支付开始”的订单匹配处理人和账号信息
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int selectPayAccountAndWorkerBy(final Order order)throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
//				String orderId = order.getOrderId();
				// TODO 
				StringBuilder sql = new StringBuilder();
				DBBeanUtil util = null;
				ResultSet rs = null;
				
				// 获取随机出票人员ID
				getPayWorkerFromService();
//				sql = new StringBuilder();
//				sql.append("select worker_id,worker_name, worker_type,worker_ext ")
//						.append("from cp_workerinfo where worker_type=? and worker_status <>? ")
//						.append("and worker_status <>? order by work_time ASC LIMIT 1");
//				cs = cn.prepareStatement(sql.toString());
//				cs.setInt(1, Worker.Robot_pay);
//				cs.setString(2, Worker.STATUS_STOP); 
//				cs.setString(3, Worker.STATUS_SPARE); 
//				logger.info("取机器人sql为：" + sql.toString());
//				rs = cs.executeQuery(); 
//				while(rs.next()){
//					worker = new Worker();
//					//正常分配到的机器人参数
//					worker.setWorkerId(rs.getString(1));
//					worker.setWorkerName(rs.getString(2));
//					worker.setWorkerType(rs.getInt(3));
//					worker.setWorkerExt(rs.getString(4));
//					
//				}
				if(worker == null) {
					logger.info("没有获取待支付机器人！");
					return 2;
				}
				StringBuilder log_work = new StringBuilder();
				log_work.append("log_work:").append(" worker.getWorkerName()").append(worker.getWorkerName())
					.append(" work_url").append(worker.getWorkerExt());
				logger.info(log_work.toString());
				
				//start 获取卡
				sql.setLength(0);
				
				sql.append("select card_id, card_no, card_pwd, card_phone, bank_type, pay_type, card_remain, card_ext from cp_cardinfo where card_status<>? and worker_id=?");
				
				cs = cn.prepareStatement(sql.toString());				
				cs.setString(1, PayCard.STATUS_STOP);
				cs.setString(2, worker.getWorkerId());
				
				logger.info("机器人id为：：" + worker.getWorkerId());
				rs = cs.executeQuery();
				
				util = new DBBeanUtil();
				
				List<PayCard> plist = util.rs2List(rs, "com.l9e.train.po.PayCard");
				
				if(plist.size()>0){
					payCard = plist.get(0);
				}else{
					return 3;
				}
				//end 获取出票账号,为支付准备
				sql.setLength(0);
				
				//sql.append("select a.acc_id, a.acc_username, a.acc_password from cp_accountinfo a, cp_orderinfo o where a.acc_id=o.account_id and o.order_id=?");
				
				sql.append("select a.acc_id, a.acc_username, a.acc_password from cp_accountinfo a where a.acc_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, String.valueOf(order.getAccountId()));
				
				rs = cs.executeQuery();
				
				util = new DBBeanUtil();
			
				List<Account> alist = util.rs2List(rs, "com.l9e.train.po.Account");
				logger.info("alist size:"+alist.size());
				if(alist.size()>0){
					account = alist.get(0);
				}else{
					return 1;
				}
				//end
				
				//start 更新订单状态为正在预定
				sql.setLength(0);
				
				sql.append("update cp_orderinfo set worker_id=?, option_time=now() where order_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, worker.getWorkerId());
				cs.setString(2, order.getOrderId());
				
				cs.executeUpdate();
				//end
				
				
				//start 更新员工处理量
//				sql.setLength(0);
//				
//				sql.append("update cp_workerinfo set order_num=order_num+1,spare_thread=spare_thread-1,work_num=work_num+1,work_time=NOW() where worker_id=?");
//				
//				cs = cn.prepareStatement(sql.toString());
//				
//				cs.setString(1, worker.getWorkerId());
//				cs.executeUpdate();
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
	public int insertHistory(final String orderId,final String optlog) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				// TODO Auto-generated method stub
				//更新订单状态为正在预定
				StringBuffer sql = new StringBuffer();
				
				sql.append("insert into cp_orderinfo_history (order_id, order_optlog, create_time, opter) values(?, ?, now(), ?)");
				
				ps = cn.prepareStatement(sql.toString());
				
				ps.setString(1, orderId);
				ps.setString(2, optlog);
				ps.setString(3, "pay app");
				
				ps.executeUpdate();
				
				return 0;
			}
		});
	}

	public int freeCard(final String cardId)  throws RepeatException, DatabaseException{
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				// TODO Auto-generated method stub
				//更新订单状态为正在预定
				//start 更新卡状态
				StringBuffer sql = new StringBuffer();
				
				sql.append("update cp_cardinfo set card_status=? where card_id=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, PayCard.STATUS_WAITUSER);
				cs.setString(2, cardId);
				
				cs.executeUpdate();
				//end
				
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("add account log error", e);
		}

	}
	
	/**
	 * 获得支付订单数量
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
				sql.append("select setting_value from train_system_setting where setting_name='robot_pay_product_num' and setting_status='1'");

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
	
	/**
	 * 获得帐号下单次数
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryProductBookNum() throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

				StringBuffer sql = new StringBuffer();
				int count = 0;
				sql.append("select setting_value from train_system_setting where setting_name='robot_app_book_num' and setting_status='1'");

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
	
	/**
	 * 获得支付订单数量
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryProductTime() throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

				// 获取一次预订数量
				StringBuffer sql = new StringBuffer();
				int count = 3;
				sql.append("select setting_value from train_system_setting where setting_name='robot_pay_time' and setting_status='1'");

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
	
	/**
	 * 获得新支付等侯时间
	 * 
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryProductNewTime() throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

				// 获取一次预订数量
				StringBuffer sql = new StringBuffer();
				int count = 5;
				sql.append("select setting_value from train_system_setting where setting_name='robot_pay_new_time' and setting_status='1'");

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
	
	/**
	 * 查询支付成功未找到完成订单检测功能是否启用
	 * @return 1、是 0、否
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int isPaySuccessNoOrder() throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				
				//获取订单信息
				StringBuilder sql = new StringBuilder();
				
				sql.append("select setting_value from train_system_setting where setting_name = ? and setting_status = ?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, "pay_to_success");
				cs.setString(2, "1");
				
				ResultSet rs = cs.executeQuery();
				int retVal = 0;
				while (rs.next()) {
					String setting_value = rs.getString("setting_value");
					if("00".equals(setting_value)) retVal = 0;
					else if("11".equals(setting_value)) retVal = 1;
				}
				return retVal;	
			}
		});
	}
	
	/**
	 * 支付成功，但未查询到完成订单，记录到库中(cp_orderinfo_find)
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int order2find(final String order_id) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				
				//获取订单信息
				StringBuffer sql = new StringBuffer();
				
				sql.append("insert into cp_orderinfo_find(order_id,create_time,find_status) values(?,NOW(),?) on duplicate key update create_time = NOW(),find_status=?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, order_id);
				cs.setString(2, "00");
				cs.setString(3, "00");
				
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
	 * 停用机器人
	 * @param orderId
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int stopWorker(final Worker worker)throws RepeatException, DatabaseException {
		
		String resultJson = HttpUtil.sendByPost("http://10.22.16.40:20116/worker/stopAndStartPreparedWorker", 
				"reason=22&workerId=" + worker.getWorkerId(), "utf-8");
		logger.info("停用支付机器人返回结果 result : " + resultJson);
		
		return 0;
	}
	
	/**
	 * 支付账号余额
	 * @param payCard
	 * @param result
	 * @return
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	public int balance4PayCard(final PayCard payCard, final Result result) throws RepeatException, DatabaseException {
		
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection conn, PreparedStatement ps)
					throws SQLException {
				logger.info(" start update balance for payCard!");
				if(StringUtils.isEmpty(result.getBalance())) {
					return 1;
				}
				if(payCard == null) {
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
				
				/*支付宝余额记录*/
				StringBuilder sql = new StringBuilder();
				sql.append("update cp_cardinfo set card_remain = ? where card_no = ?");
				ps = conn.prepareStatement(sql.toString());
				ps.setDouble(1, balance);
				ps.setString(2, cardNo);
				
				ps.executeUpdate();
				
				return 0;
			}
		});
	}
	
	/**
	 * 从新的机器人服务获取支付机器人
	 * @return
	 */
	private void getPayWorkerFromService() {
		try {
			int count = 0;
			do{
				String resultJson = HttpUtil.sendByPost("http://10.22.16.40:20116/worker/getWorker", "type=3", "utf-8");
				logger.info("获取机器人返回结果 result : " + resultJson);
				WorkerVo workerVo = null;
				if(resultJson != null && !"".equals(resultJson)) {
					JSONObject resultJsonObject = JSONObject.fromObject(resultJson);
					if(resultJsonObject.has("success") && resultJsonObject.getBoolean("success")) {
						workerVo = (WorkerVo) JSONObject.toBean(resultJsonObject.getJSONObject("data"), WorkerVo.class);
					}
				}
				
				if(workerVo != null) {
					worker = new Worker();
					worker.setWorkerExt(workerVo.getWorkerExt());
					worker.setWorkerId(workerVo.getWorkerId() + "");
					worker.setWorkerName(workerVo.getWorkerName());
					worker.setWorkerType(Worker.Robot_pay);
					worker.setPay_device_type(workerVo.getPay_device_type());
					break;
				}
				
				count++;
			}while(count < 5);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * 查询一人多票或者往返票
	 * qinsg
	 * @param notify
	 * @param supplier
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int querySameOut_ticket_billno(final Order order)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// TODO Auto-generated method stub
				//更新系统订单
				StringBuffer sql = null;
				sql = new StringBuffer();
				sql.append("SELECT count(ho.order_id) FROM cp_orderinfo ho where timediff(now() , ho.out_ticket_time)<'01:00:00'  and ho.out_ticket_billno=? ");

				cs = cn.prepareCall(sql.toString());
				cs.setString(1, order.getOutTicketBillNo());
			
				ResultSet rs = cs.executeQuery();
				rs.next();
				int size =rs.getInt(1);
				
				return size;
			}
		});
	}
}
