package com.l9e.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.Order;
import com.l9e.train.po.TrainNewDataFake;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class AlterServiceImpl {

	private Logger logger = LoggerFactory.getLogger(AlterServiceImpl.class);

	private DBBean dbbean = null;

	public AlterServiceImpl() {
		dbbean = new DBBean();
	}

	// 获取所有需要通知的列表
	public List<TrainNewDataFake> list = null;

	public List<TrainNewDataFake> getOrderbill() {
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
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				//
				// list = new ArrayList<TrainNewDataFake>();
				// StringBuffer sql = new StringBuffer();
				//
				// //获取订单信息 start
				// sql = new StringBuffer();
				// sql.append("SELECT cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1
				// FROM t_zjpj_a ");
				// sql.append("WHERE fz like CONCAT(?, '%') AND dz like
				// CONCAT(?, '%')");
				// cs = cn.prepareStatement(sql.toString());
				// cs.setString(1, order_id);
				// cs.setString(2, Order.WAIT_ROBOT_ALTER);//00等待机器改签
				// cs.setString(3, Order.REPEAT_ROBOT_ALTER);//01重新机器改签
				// ResultSet rs = cs.executeQuery();
				//
				// if(rs.next()){
				// Order order = new Order();
				// order.setOrderId(rs.getString(1));
				// order.setOrderStatus(rs.getString(2));
				// order.setCreateTime(rs.getString(3));
				// order.setOutTicketBillno(rs.getString(4));
				// order.setTrainNo(rs.getString(5));
				// order.setAlterTrainNo(rs.getString(6));
				// order.setFromStation(rs.getString(7));
				// order.setArriveStation(rs.getString(8));
				// order.setFromTime(rs.getString(9));
				// order.setTravelTime(rs.getString(10));
				// order.setAlterTravelTime(rs.getString(11));
				// order.setSeatType(rs.getString(12));
				// order.setAlterSeatType(rs.getString(13));
				// order.setChannel(rs.getString(14));
				//
				// list.add(order);
				// }else{
				// continue;
				// }
				// //获取订单信息 end
				// logger.info("get orderbill size:"+list.size());
				return 0;
			}
		});
	}

	// 更新改签后的车票信息
	public int updateElongAlterToRefund(final Order order, final String cp_id) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				// start
				StringBuffer sql = new StringBuffer();

				sql.append("UPDATE cp_orderinfo_refund set alter_travel_time=travel_time , alter_train_box=train_box,alter_train_no=train_no, ")
						.append("alter_seat_type=seat_type,alter_seat_no=seat_no,alter_buy_money = buy_money ").append("WHERE order_id=? AND cp_id=?");
				cs = cn.prepareStatement(sql.toString());

				cs.setString(1, order.getOrderId());
				cs.setString(2, cp_id);

				cs.executeUpdate();
				// end
				return 0;
			}
		});
	}

}
