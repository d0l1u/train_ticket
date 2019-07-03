package com.l9e.train.service.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.train.po.BillOrder;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class TrainServiceImpl{
	
	private Logger logger=Logger.getLogger(this.getClass());
	private DBBean dbbean = null;

	public List<BillOrder> list = null;
	
	public TrainServiceImpl() {
		
		dbbean = new DBBean();
	}
	/**
	 * 获取所有需要通知的列表
	 * 
	 * @return
	 */
	public List<BillOrder> getOrderbill() {
		
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
				
				
				//获取待通知的结算记录
				StringBuilder sql = new StringBuilder();
				
				sql.append("SELECT bill_id,notify_status FROM elong_billinfo WHERE notify_status = '00' ");
				sql.append("OR (notify_status = '11' AND notify_num < 5 AND notify_time < DATE_SUB(NOW(), INTERVAL 1 MINUTE)) LIMIT ?");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, getNum);
				
				ResultSet rs = cs.executeQuery();
				List<BillOrder> bills = new ArrayList<BillOrder>();
				while(rs.next()){
					Integer billId = rs.getInt("bill_id");
					String notifyStatus = rs.getString("notify_status");
					BillOrder bill = new BillOrder();
					bill.setBillId(billId);
					bill.setNotifyStatus(notifyStatus);
					bills.add(bill);
				}
				
				list = new ArrayList<BillOrder>();
				for(BillOrder bill : bills){
					Integer billId = bill.getBillId();
					if(billId == null) continue;
					sql.setLength(0);
					sql.append("SELECT bill_id FROM elong_billinfo WHERE bill_id = ? AND notify_status = ? FOR UPDATE");
					cs = cn.prepareStatement(sql.toString());	
					cs.setInt(1, billId);
					cs.setString(2, bill.getNotifyStatus());
					
					rs = cs.executeQuery();
					if(!rs.next()){
						continue;
					}
					/*修改通知状态*/
					sql.setLength(0);
					sql.append("UPDATE elong_billinfo SET notify_status = ?,notify_time = NOW() WHERE bill_id = ? AND notify_status = ?");
					cs = cn.prepareStatement(sql.toString());	
					cs.setString(1, BillOrder.NOTIFY_ING);
					cs.setInt(2, billId);
					cs.setString(3, bill.getNotifyStatus());
					
					if(cs.executeUpdate()!=1){
						logger.info("修改结算记录状态失败: " + billId);
						continue;
					}
					/*获取结算记录详细信息*/
					sql.setLength(0);
					sql.append("SELECT bill_id,order_id,out_ticket_billno,amount,settlement_type,");
					sql.append(" quantity,trade_date,settlement_date,channel,account_balance,");
					sql.append(" notify_status,notify_num,notify_time");
					sql.append(" FROM elong_billinfo WHERE bill_id = ?");
					
					cs = cn.prepareStatement(sql.toString());
					cs.setInt(1, billId);
					rs = cs.executeQuery();
					BillOrder billOrder = new BillOrder();
					if(rs.next()){
						billOrder.setBillId(billId);
						billOrder.setOrderId(rs.getString("order_id"));
						billOrder.setOut_ticket_billno(rs.getString("out_ticket_billno"));
						billOrder.setAmount(rs.getDouble("amount"));
						billOrder.setSettlementType(rs.getInt("settlement_type"));
						billOrder.setQuantity(rs.getInt("quantity"));
						billOrder.setTradeDate(rs.getTimestamp("trade_date"));
						billOrder.setSettlementDate(rs.getDate("settlement_date"));
						billOrder.setChannel(rs.getString("channel"));
						billOrder.setAccountBalance(rs.getDouble("account_balance"));
						billOrder.setNotifyStatus(rs.getString("notify_status"));
						billOrder.setNotifyNum(rs.getInt("notify_num"));
						billOrder.setNotifyTime(rs.getDate("notify_time"));
					}else{
						continue;
					}
					logger.info("获取结算信息完成："+billId);
					
					list.add(billOrder);
					
				}
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
	public int orderIsFailure(final BillOrder billOrder, final String result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				logger.info("对账通知失败,更新通知信息,bill_id:" + billOrder.getBillId());
				StringBuilder sql = new StringBuilder();
				sql.append("UPDATE elong_billinfo SET notify_num = notify_num + 1");
				if(billOrder.getNotifyNum() == 4) {
					sql.append(" ,notify_finish_time = NOW(),notify_status = ?");
				}
				sql.append(" WHERE bill_id = ?");
				
				int index = 1;
				cs = cn.prepareStatement(sql.toString());
				if(billOrder.getNotifyNum() == 4) {
					cs.setString(index++, BillOrder.NOTIFY_FAIL);
				}
				cs.setInt(index++, billOrder.getBillId());
				
				cs.executeUpdate();
				
				logger.info("通知失败," + (billOrder.getNotifyNum() == 4 ? "更新状态为33," : "") + " 通知次数加1,bill_id: " + billOrder.getBillId());
				
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
	public int orderIsSuccess(final BillOrder billOrder, final String result)throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				logger.info("对账通知成功,更新通知状态,bill_id:" + billOrder.getBillId());
				StringBuilder sql = new StringBuilder();
				
				/*更新通知信息*/
				sql.append("UPDATE elong_billinfo SET notify_status = ?,notify_num = notify_num + 1,notify_finish_time = NOW() WHERE bill_id = ?");
				
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, BillOrder.NOTIFY_SUCCESS);
				cs.setInt(2, billOrder.getBillId());
				
				cs.executeUpdate();
				
				logger.info("通知成功，更新状态为22，bill_id : " + billOrder.getBillId());
				
				return 0;
			}
		});

	}
	
	/**
	 * 插入日志
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int insertHistory(final BillOrder billOrder,final String optlog) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				
				StringBuilder sql = new StringBuilder();
				
				sql.append("INSERT INTO elong_billinfo_logs (bill_id,order_id,content,opt_person,create_time) VALUES (?,?,?,?,NOW())");
				
				ps = cn.prepareStatement(sql.toString());
				
				ps.setInt(1, billOrder.getBillId());
				ps.setString(2, billOrder.getOrderId());
				ps.setString(3, optlog);
				ps.setString(4, "bill_app");
				
				ps.executeUpdate();
				
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
					return 0;
				}else{
					return 1;
				}
			}
		});
	}
	
}
	
	

