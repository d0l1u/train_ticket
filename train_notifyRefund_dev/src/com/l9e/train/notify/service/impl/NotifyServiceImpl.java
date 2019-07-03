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

	private Logger logger = Logger.getLogger(this.getClass());
	private DBBean dbbean = null;

	public List<OrderBill> list = null;

	public NotifyServiceImpl() {
		dbbean = new DBBean();
	}

	/**
	 * 获取所有需要通知的列表
	 * 
	 * @return
	 */
	public List<OrderBill> getNotify() {
		return this.list;
	}

	public void setNotify(List<OrderBill> list) {
		this.list = list;
	}

	/**
	 * 查询需要通知的数据，并且进行更新
	 * 
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int notifyOrderByList(final int getNum) throws RepeatException,
			DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {

				DBBeanUtil dbUtil = new DBBeanUtil();

				// 需要发送通知的数据
				StringBuffer sql = new StringBuffer();
				sql.append("select re.order_id, re.cp_id,re.refund_money, re.buy_money,re.alter_diff_money, re.alter_buy_money, re.refund_12306_money,")
				    .append(" re.create_time, re.alter_buy_money, re.alter_travel_time, re.alter_seat_type, ")
					.append("re.alter_train_box, re.alter_seat_no, rn.notify_url, rn.notify_type,re.refund_12306_seq,")
					.append("re.order_status,re.our_remark,re.refuse_reason,re.refund_seq ")
				    .append("from cp_orderinfo_refund re, cp_orderinfo_refund_notify rn ")
				    .append("where re.order_id=rn.order_id and re.cp_id=rn.cp_id and rn.notify_status in (?,?) ")
				    .append("and now() > rn.notify_next_time and rn.notify_num < 5 ")
				    .append(" and notify_type in (?,?) order by rn.create_time asc limit ?");
//				    .append(" order by rn.create_time asc limit ?");
				
				cs = cn.prepareStatement(sql.toString());
				
				cs.setString(1, String.valueOf(Notify.NOTIFY_RESTORE));
				cs.setString(2, String.valueOf(Notify.NOTIFY_WAIT));
				cs.setString(3, String.valueOf(Notify.TYPE_1));
				cs.setString(4, String.valueOf(Notify.TYPE_2));
				cs.setInt(5, getNum);

				cs.executeQuery();
				ResultSet rs = cs.getResultSet();

				List<OrderBill> aList = dbUtil.rs2List(rs, "com.l9e.train.po.OrderBill");

				list = new ArrayList<OrderBill>();

				for (OrderBill orderbill : aList) {
					list.add(orderbill);
				}

				// 更新通知记录,为通知中
				sql = new StringBuffer();
				sql.append("update cp_orderinfo_refund_notify hn set hn.notify_status=?, hn.notify_num=hn.notify_num+1, hn.notify_time=now() where hn.order_id=? and hn.cp_id=?");
				cs = cn.prepareStatement(sql.toString());

				for (OrderBill orderbill : list) {
					cs.setString(1, String.valueOf(Notify.NOTIFY_ING));
					cs.setString(2, orderbill.getOrderId());
					cs.setString(3, orderbill.getCpId());
					
					cs.addBatch();
				}

				cs.executeBatch();
				return 0;
			}
		});
	}

	/**
	 * 把通知状态修改为重新通知
	 * 
	 * @param orderbill
	 * @return
	 * @throws DatabaseException
	 * @throws RepeatException
	 */
	public int restoreNotify(final OrderBill orderbill) throws RepeatException,
			DatabaseException {
		
		return dbbean.executeMethod(new ICallBack() {

			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				String sql = "update cp_orderinfo_refund_notify hn set hn.notify_status=?, hn.notify_num=hn.notify_num+1, " +
						"hn.notify_next_time = DATE_ADD(now(), interval 2 minute) where hn.order_id=? and hn.cp_id=?";
				cs = cn.prepareStatement(sql);

				cs.setInt(1, Notify.NOTIFY_RESTORE);
				cs.setString(2, orderbill.getOrderId());
				cs.setString(3, orderbill.getCpId());

				cs.executeUpdate();
				return 0;
			}
		});
	}

	/**
	 * 更新成功通知状态
	 * 
	 * @param orderbill
	 * @return
	 * @throws DatabaseException
	 * @throws RepeatException
	 */
	public int successNotify(final OrderBill orderbill) throws RepeatException,
			DatabaseException {
		
		return dbbean.executeMethod(new ICallBack() {

			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				String sql = "update cp_orderinfo_refund_notify hn set hn.notify_status=?, hn.notify_time=now() where hn.order_id=? and hn.cp_id=?";
				cs = cn.prepareStatement(sql);

				cs.setInt(1, Notify.NOTIFY_SUCCESS);
				cs.setString(2, orderbill.getOrderId());
				cs.setString(3, orderbill.getCpId());

				cs.executeUpdate();

				return 0;
			}
		});
	}

	/**
	 * 插入历史记录cp_orderinfo_refund_history
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int insertHistory(final OrderBill orderBill, final String optlog)
			throws RepeatException, DatabaseException {
		
		return dbbean.executeMethod(new ICallBack() {

			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				StringBuffer sql = new StringBuffer();

				sql.append("insert into cp_orderinfo_refund_history (order_id, cp_id, order_optlog, create_time, order_time, opter)" +
						" values(?, ?, ?, now(), ?, ?)");

				ps = cn.prepareStatement(sql.toString());

				ps.setString(1, orderBill.getOrderId());
				ps.setString(2, orderBill.getCpId());
				ps.setString(3, optlog);
				ps.setString(4, orderBill.getCreateTime());
				ps.setString(5, "notify app");

				ps.executeUpdate();

				return 0;
			}
		});
	}

}
