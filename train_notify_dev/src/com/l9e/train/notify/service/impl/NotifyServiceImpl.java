package com.l9e.train.notify.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.train.po.Notify;
import com.l9e.train.po.OrderBill;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.DBBeanUtil;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;



/**
 * 
 * @author guobin
 *
 */
public class NotifyServiceImpl {
	
	private Logger logger=Logger.getLogger(this.getClass());
	private DBBean dbbean = null;

	public List<OrderBill> list = null;
	
	public NotifyServiceImpl() {
		// TODO Auto-generated constructor stub
		
		dbbean = new DBBean();
	}
	
	/**
	 * 获取所有需要通知的列表
	 * @return
	 */
	public List<OrderBill> getNotify() {
		// TODO Auto-generated method stub
		
		return this.list;
	}

	public void setNotify(List<OrderBill> list) {
		
		this.list = list;
	}
	
	
	/**
	 * 查询需要通知的数据，并且进行更新
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int notifyOrderByList(final int getNum) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				DBBeanUtil dbUtil = new DBBeanUtil();
				
				//需要发送通知的记录
				StringBuffer sql = new StringBuffer();
				/*9月24日修改
				 sql.append("select o.order_id, o.out_ticket_billno, o.buy_money,  o.order_status, n.notify_url, o.error_info, a.acc_username, a.acc_password, o.ext_seattype from cp_orderinfo o, cp_orderinfo_notify n, cp_accountinfo a");
				sql.append(" where o.order_id=n.order_id and a.acc_id=o.account_id and n.notify_status in(?,?) and now()>n.notify_next_time and n.notify_num<5 ");*/
				sql.append("select o.order_id, o.out_ticket_billno, o.buy_money,  o.order_status, n.notify_url, ")
					.append(" o.from_time,o.to_time,o.error_info,o.return_optlog, a.acc_username, a.acc_password, ")
					.append(" o.ext_seattype, o.level,o.is_pay,o.pay_limit_time,o.out_ticket_time ")
				    .append("from cp_orderinfo o left join  cp_accountinfo a on a.acc_id=o.account_id, cp_orderinfo_notify n")
				    .append(" where o.order_id=n.order_id  and n.notify_status in(?,?) and now()>n.notify_next_time and n.notify_num<5 ")
				    .append(" order by n.create_time asc limit ?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, String.valueOf(Notify.NOTIFY_RESTORE));
				cs.setString(2, String.valueOf(Notify.NOTIFY_WAIT));
				cs.setInt(3, getNum);
				
				cs.executeQuery();
				ResultSet rs = cs.getResultSet();
				
				List<OrderBill> aList = dbUtil.rs2List(rs, "com.l9e.train.po.OrderBill"); 
				
				list = new ArrayList<OrderBill>();
				
				for (OrderBill orderbill : aList) {
					StringBuffer sb = new StringBuffer();
					sb.append("select CONCAT(cp_id,'|',buy_money, '|', train_box, '|',seat_no,'|',seat_type) from cp_orderinfo_cp where order_id=?");
				
					PreparedStatement cpCs = cn.prepareStatement(sb.toString());
					cpCs.setString(1, orderbill.getOrderId());
					
					ResultSet cpRs = cpCs.executeQuery();
					
					while(cpRs.next()){
						logger.info("traininfo:"+cpRs.getString(1));
						orderbill.addOrderCp(cpRs.getString(1));
					}
					
					
					sb = new StringBuffer();
					sb.append("select CONCAT(cp_id,'|',user_name,'|',cert_no,'|',IFNULL(check_status,'0'))  from cp_orderinfo_cp where order_id=?");
				
					cpCs = cn.prepareStatement(sb.toString());
					cpCs.setString(1, orderbill.getOrderId());
					
					cpRs = cpCs.executeQuery();
					
					while(cpRs.next()){
						orderbill.addPassengers(cpRs.getString(1));
					}
					
					logger.info(orderbill.getOrderId()+" status:"+orderbill.getOrderStatus());
					list.add(orderbill);
				}
				
				//更新通知记录,为通知中
				sql = new StringBuffer();
				sql.append("update cp_orderinfo_notify hn set hn.notify_status=?, hn.notify_num=hn.notify_num+1, hn.notify_time=now() where hn.order_id=?");
				cs = cn.prepareStatement(sql.toString());
				
				for (OrderBill orderbill : list) {
					cs.setString(1, String.valueOf(Notify.NOTIFY_ING));
					cs.setString(2, orderbill.getOrderId());
					
					cs.addBatch();
				}
				
				cs.executeBatch();
				return 0;
			}
		});
	}
	
	/**
	 * 把通知状态修改为重新通知
	 * @param orderbill
	 * @return
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	public int restoreNotify(final OrderBill orderbill) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				// TODO Auto-generated method stub
				String sql = "update cp_orderinfo_notify hn set hn.notify_status=?, hn.notify_num=hn.notify_num+1, hn.notify_next_time = DATE_ADD(now(), interval 2 minute) where hn.order_id=?";
				cs = cn.prepareStatement(sql);
				
				cs.setInt(1, Notify.NOTIFY_RESTORE);
				cs.setString(2, orderbill.getOrderId());
				
				cs.executeUpdate();
				return 0;
			}
		});
	}
	
	/**
	 * 更新成功通知状态
	 * @param orderbill
	 * @return
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	public int successNotify(final OrderBill orderbill) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack(){

			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				// TODO Auto-generated method stub
				String sql = "update cp_orderinfo_notify hn set hn.notify_status=?, hn.notify_time=now() where hn.order_id=?";
				cs = cn.prepareStatement(sql);
				
				cs.setInt(1, Notify.NOTIFY_SUCCESS);
				cs.setString(2, orderbill.getOrderId());
				
				cs.executeUpdate();
				
				return 0;
			}} );
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
				ps.setString(3, "notify app");
				
				ps.executeUpdate();
				
				return 0;
			}
		});
	}
	
	/**
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryWaitPayTime() throws RepeatException, DatabaseException {

		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

				StringBuffer sql = new StringBuffer();
				int count = 45;
				sql.append("select setting_value from train_system_setting where setting_name='12306_pay_wait_time' and setting_status='1'");

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

}
