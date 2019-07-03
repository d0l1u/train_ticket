package com.l9e.train.supplier.service.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.supplier.po.JlOrder;
import com.l9e.train.supplier.po.JlOrderCP;
import com.l9e.train.supplier.po.Order;
import com.l9e.train.supplier.po.OrderNotify;
import com.l9e.train.supplier.po.OrderRefund;
import com.l9e.train.util.DateUtil;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;


public class JlOrderService{
	
	private Logger logger=Logger.getLogger(this.getClass());
	DBBean bean =null;
	
	public JlOrderService(){
		//Config.setConfigResource();
		bean = new DBBean();
	}
	
	public static final Object LOCK = new Object();
	public static int no = 1;//序号

	/**
	 * 抢票订单收单接口
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int downBill(final JlOrder supOrder) throws RepeatException, DatabaseException{
		// TODO Auto-generated method stub
		logger.debug("jl_order start insert downbill........");  
		return bean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs)throws SQLException {
				//车站三字码
				String fromCity3c = supOrder.getFromCity3c();
				String toCity3c = supOrder.getToCity3c();
				//抢票订单传给携程时，咱们自己生成的抢票订单号
//				Date currentDate=new Date();
//				String formatStr = "yyyyMMddHHmmssSSS";//格式化到毫秒
//				String currentDateStr=DateUtil.dateToString(currentDate, formatStr);
//				String ctripOrderId = "19e"+currentDateStr;//咱们传给携程的订单号，自己生成				
				Date currentDate=new Date();
				String formatStr = "yyyyMMddHHmmss";
				String currentDateStr=DateUtil.dateToString(currentDate, formatStr);
				int current = 1000;
				synchronized (LOCK) {
					if (no > 9999) {
						no = 1000;
					}
					current += no++;
				}
				StringBuffer sb = new StringBuffer();
				sb.append(currentDateStr).append(current);

				String ctripOrderId = "19e"+sb.toString();//咱们传给携程的订单号，自己生成
				
				//插入抢票主订单表
				StringBuffer sql =  new StringBuffer();
				sql.append("insert into jl_orderinfo (order_id, pay_money,order_status, train_no,train_no_accept,");
				sql.append("from_city, to_city, from_time, to_time, travel_time, create_time, seat_type,seat_type_accept, out_ticket_type,");
				sql.append("channel,level,is_pay,option_time,from_3c,to_3c,pay_money_ext_sum,buy_money_ext_sum,leak_cut_offTime,manual_order,wait_for_order,ctrip_order_id,buy_money");						
				sql.append(") VALUES");
				
				sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?, ?, ?, ?,?,now(),?,?,?,?,?,?,?,?,?");		
				sql.append(");");
				
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, supOrder.getOrderId());//抢票订单ID
				cs.setString(2, supOrder.getPayMoney());//客户支付金额
			    cs.setString(3, supOrder.getOrderStatus());//抢票订单状态	
				cs.setString(4, supOrder.getTrainNo());//车次
				cs.setString(5, supOrder.getTrainNoAccept());//备选车次
				cs.setString(6, supOrder.getFromCity());//出发城市
				cs.setString(7, supOrder.getToCity());//到达城市
				cs.setString(8, supOrder.getFromTime());//车次出发时间
				cs.setString(9, supOrder.getToTime());//车次到达时间
				cs.setString(10, supOrder.getTravelTime());//乘车日期
				cs.setString(11, supOrder.getSeatType());//座席类型
				cs.setString(12, supOrder.getSeatTypeAccept());//备选座席类型（硬卧|硬座..)
				cs.setString(13, supOrder.getOutTicketType());//出票方式  11、电子票  22、配送票
				cs.setString(14, supOrder.getChannel());//渠道
				cs.setString(15, StringUtils.isEmpty(supOrder.getLevel())?"0":supOrder.getLevel());//抢票订单级别
				cs.setString(16, "11");//支付状态：00：已支付；11：未支付		
				cs.setString(17, fromCity3c); //出发城市三字码
				cs.setString(18, toCity3c);   //到达城市三字码
				
				//加价金额以后改造，现在先默认为0
				cs.setString(19, "0");   //咱们把抢票订单传给携程出票时，携程收取咱们的抢票服务费总价（咱们给携程的服务费，乘客总人数*单人抢票服务费）
				cs.setString(20, "0");   //抢票时咱们收取客户的抢票服务费总价（客户给咱们的服务费，乘客总人数*单人抢票服务费）
				
				cs.setString(21, supOrder.getLeakCutOffTime());//捡漏截止日期
				cs.setString(22, "00");//是否手工出票  11：人工出票  00：系统出票
				cs.setString(23, "11");//12306系统故障是否继续出票 11：继续出票  00：不继续出票
				cs.setString(24, ctripOrderId);//咱们传给携程的订单号，自己生成
				cs.setString(25, supOrder.getBuyMoney());
				logger.info("jl_order  supOrder.getAccountId()........"+supOrder.getOrderId());
				int count=cs.executeUpdate();
				logger.info(supOrder.getOrderId()
						+ " jl_order insert update : " + count);
				
				//插入抢票订单车票表
				sql = new StringBuffer();
				sql.append("insert into jl_orderinfo_cp (cp_id, order_id, user_name, ticket_type, cert_type, cert_no, create_time,pay_money,buy_money,pay_money_ext,buy_money_ext)");
				sql.append("values (?, ?, ?, ?, ?, ?, now(), ?,?,?,?)");
				
				cs = cn.prepareCall(sql.toString());
				for (JlOrderCP cp : supOrder.getOrderCPs()) {
					cs.setString(1, cp.getCpId());//车票ID
					cs.setString(2, supOrder.getOrderId());//抢票关联订单ID
					cs.setString(3, cp.getUserName());//车票信息中的乘客姓名
					cs.setInt(4, cp.getTicketType());//车票类型
					cs.setInt(5, cp.getCertType());//证件类型
					cs.setString(6, cp.getCertNo());//证件号码
					cs.setString(7, cp.getPayMoney());//客户支付价格(单人的）
					cs.setString(8, cp.getBuyMoney());//客户支付价格(单人的）
					cs.setString(9, "0");//咱们把抢票订单传给携程出票时，携程收取咱们的单人抢票服务费（咱们给携程的服务费，单人的）
					cs.setString(10, "0");//抢票时咱们收取客户的单人抢票服务费（客户给咱们的服务费，单人的）
					cs.addBatch();
				}
				
				cs.executeBatch();
				
				//插入通知表数据
				sql = new StringBuffer();
				sql.append("insert into jl_orderinfo_notify (order_id,task_type,task_num,task_status,create_time)");
				sql.append("values (?, ?, ?, ?, now())");
				
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, supOrder.getOrderId());
				cs.setString(2, "1");//任务类型  1：预定任务  2：支付任务  3：取消任务 4：向订单系统通知
				cs.setInt(3, 0);//任务次数
				cs.setString(4, "00");//任务状态   00：开始任务 11：正在任务  22：任务成功  33：任务失败
				cs.executeUpdate();
				
				logger.debug("jl_order end insert downbill........");  
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
	
	public int cancel(final String order_id) throws RepeatException, DatabaseException{
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)throws SQLException {
				
				//更新订单状态
				StringBuilder sql = new StringBuilder();
				sql.append("select order_status from jl_orderinfo where order_id=?");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1,order_id);
				ResultSet rs = cs.executeQuery();
				rs= cs.executeQuery();
				String old_order_status="";
				if(rs.next()){
					old_order_status=rs.getString(1);
				}
				
				sql.setLength(0);
				//支付机器处理状态不予处理( 开始支付,正在支付 )
				if(JlOrder.STATUS_PAY_START.equals(old_order_status)||JlOrder.STATUS_PAY_ING.equals(old_order_status)){
					logger.info("cancle fail order Are paying "+old_order_status);
					return 0;
				}
				//订单已经在 取消状态 后台会 处理
				if(JlOrder.OUT_TICKET_FAIL.equals(old_order_status)||JlOrder.STATUS_CANCEL_START.equals(old_order_status)
						||JlOrder.STATUS_CANCEL_MANUAL.equals(old_order_status)||JlOrder.STATUS_CANCEL_ING.equals(old_order_status)){
					logger.info("cancle success order is cancled "+old_order_status);
					return 1;
				}
				sql.append("update jl_orderinfo set order_status=?,return_optlog=? where order_id=?");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, JlOrder.STATUS_CANCEL_START);//开始取消
				cs.setString(2, "C3");
				cs.setString(3, order_id);
				int num = cs.executeUpdate();
				logger.info("cancle db old_order_status"+old_order_status+",update_num"+num);
				sql.setLength(0);
				sql.append("insert into jl_orderinfo_history (order_id, order_optlog, create_time, opter) values(?, ?, now(), ?)");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, order_id);
				cs.setString(2, "用户申请取消,原订单状态:"+old_order_status);
				cs.setString(3, "order app");
				cs.executeUpdate();
				sql.setLength(0);
				sql.append("insert into jl_orderinfo_notify (order_id,task_type,task_num,task_status,create_time)");
				sql.append("values (?, ?, ?, ?, now())");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, order_id);
				cs.setString(2, "3"); //1：预定任务  2：支付任务  3：取消任务 4：向订单系统发送通知',
				cs.setString(3, "0");
				cs.setString(4, "00"); // 00：开始任务 11：正在任务  22：任务成功  33：任务失败',
				cs.executeUpdate();
				return num;
			}
		});
		
	}
	
	/**
	 * 退款逻辑 
	 * @param refund
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int refund(final String order_id) throws RepeatException, DatabaseException{
		logger.debug("start refund ........"+order_id);  
		return bean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs)throws SQLException {
				//更新订单状态
				StringBuilder sql = new StringBuilder();
				sql.append("select order_status from jl_orderinfo where order_id=?");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1,order_id);
				ResultSet rs = cs.executeQuery();
				rs= cs.executeQuery();
				String old_order_status="";
				if(rs.next()){
					old_order_status=rs.getString(1);
				}
				sql.setLength(0);
				
				sql.append("update jl_orderinfo set order_status=?,return_optlog=? where order_id=?");
				cs = cn.prepareCall(sql.toString());
				cs.setString(1, JlOrder.STATUS_REFUND_MENUAL);//退款人工
				cs.setString(2, "C2");
				cs.setString(3, order_id);
				int num = cs.executeUpdate();
				logger.info("refund db old_order_status"+old_order_status+",update_num"+num);
				sql.setLength(0);
				sql.append("insert into jl_orderinfo_history (order_id, order_optlog, create_time, opter) values(?, ?, now(), ?)");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, order_id);
				cs.setString(2, "用户申请退款,原订单状态:"+old_order_status);
				cs.setString(3, "order app");
				cs.executeUpdate();
				/*cs = cn.prepareStatement("update jl_orderinfo_notify set task_type = ?,task_status = ?,option_time=now() where order_id = ?");
				cs.setString(1, "3"); //1：预定任务  2：支付任务  3：取消任务 4：向订单系统发送通知',
				cs.setString(2, "00"); // 00：开始任务 11：正在任务  22：任务成功  33：任务失败',
				cs.setString(3, order_id);*/
				return num;
			}
		});
	}
}
