package com.l9e.transaction.thread;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.l9e.transaction.service.impl.TrainServiceImpl;
import com.l9e.util.DateUtil;



public class NorQunAccData implements Runnable{
	private static final Logger logger = 
		Logger.getLogger(NorQunAccData.class);
	
	//普通账号
	private static final LinkedBlockingQueue norQueue = new LinkedBlockingQueue(30);
	//去哪账号
	private static final LinkedBlockingQueue qunQueue = new LinkedBlockingQueue(30);
	
	private static class StackDataHolder{
		private static final NorQunAccData INSTANCE = new NorQunAccData();
	}
	
	public static final NorQunAccData getInstance(){
		return StackDataHolder.INSTANCE;
	}

	public int getNorAccId(){
		if(null == norQueue.peek()){
			return 0;
		}else{
			return (Integer) norQueue.poll();
		}
	}
	
	public int getQunAccId(){
		if(null == qunQueue.peek()){
			return 0;
		}else{
			return (Integer)qunQueue.poll();
		}
	}
	
	public int getNorQueueSize(){
		return norQueue.size();
	}
	
	public int getQunQueueSize(){
		return qunQueue.size();
	}
	
	public void startPoolLoad(TrainServiceImpl dao){
		try {
			Date date = new Date();
			String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
			Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");//7:00
			Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
			if(date.before(begin) || date.after(end)){
				logger.info("7-23点方可启用打码器！");
				Thread.sleep(60*1000);//睡眠半小时
			}
			//qunar线程池加载
			logger.info("========开始补充qunar账号池==========");
			dao.loadAccPool(qunQueue,"qunar");
			logger.info("========结束补充qunar账号池=======size:"+qunQueue.size());
			//19e线程池加载
			logger.info("========开始补充19e账号池==========");
			dao.loadAccPool(norQueue,"19e");
			logger.info("========结束补充19e账号池=======size:"+norQueue.size());
		} catch (Exception e) {
			logger.error("thread pool is error!",e);
		}
	}
	
	
	
	
	/*
	public void loadAccPool(Connection conn,PreparedStatement stmt,ResultSet rs,
			LinkedBlockingQueue queue,String channel) throws Exception{
		StringBuffer sql = new StringBuffer();
		//获取普通账号
		if(queue.size()>=20){
			return;
		}else{
			sql.append("select acc_id from cp_accountinfo where acc_status=? and book_num<=? and ");
			if(WorkIdNum.limit_day==1){
				sql.append(" priority > ? and ");
			}
			sql.append("contact_num <= ? AND is_alive = '1' AND channel=? ");
			//train_app使用
			sql.append("order by option_time desc LIMIT 20");

			//train_app_new 使用
//			sql.append("order by option_time desc LIMIT 20,20");
			
			int index = 1;
			stmt = (PreparedStatement) conn.prepareStatement(sql.toString());
			stmt.setString(index++, Account.FREE);
			stmt.setInt(index++, WorkIdNum.book_num);
			if(WorkIdNum.limit_day==1){
				stmt.setInt(index++, WorkIdNum.no_order_day);
			}
			stmt.setInt(index++, WorkIdNum.contact_num);
			stmt.setString(index, channel);
			rs = stmt.executeQuery();
			
			//查询当前常用联系人数限制下的账号没有，则修改当前常用联系人数限制，重新查询
			if(!rs.next()){
				sql = new StringBuffer();
				sql.append("select acc_id from cp_accountinfo where acc_status=? and book_num<=? and ");
				if(WorkIdNum.limit_day==1){
					sql.append(" priority > ? and ");
				}
				sql.append("contact_num <= ? AND is_alive = '1' AND channel=? ");
				sql.append("order by option_time desc LIMIT 20");

				index = 1;
				stmt = (PreparedStatement) conn.prepareStatement(sql.toString());
				stmt.setString(index++, Account.FREE);
				stmt.setInt(index++, WorkIdNum.book_num);
				if(WorkIdNum.limit_day==1){
					stmt.setInt(index++, WorkIdNum.no_order_day);
				}
				stmt.setInt(index++, WorkIdNum.contact_num+10);
				stmt.setString(index, channel);
				rs = stmt.executeQuery();
			}
			
			while(rs.next()){
				if(queue.size()<=29){
					int acc_id = rs.getInt("acc_id");
					
					logger.info("==========>Save to "+channel+" acc pool acc_id:"+acc_id);
					//乐观锁，更新成功，则添加到队列中
					sql = new StringBuffer();
					sql.append("UPDATE cp_accountinfo SET acc_status=?,option_time=now() ");
					sql.append(" WHERE acc_id = ? AND acc_status='33' ");
					stmt = (PreparedStatement) conn.prepareStatement(sql.toString());  
					stmt.setString(1, Account.DOWNING);
					stmt.setInt(2, acc_id);
					int up_result = stmt.executeUpdate();
					
					if(up_result==1){
						queue.offer(acc_id);
					}
				}else{
					break;
				}
			}
		}
	}
	*/
	@Override
	public void run() {
		
	}
}
