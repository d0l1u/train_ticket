package com.l9e.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class SysSettingServiceImpl {

	private Logger logger = LoggerFactory.getLogger(SysSettingServiceImpl.class);
	
	private DBBean dbbean = null;
	private String sysVal = null;
	
	public SysSettingServiceImpl() {
		dbbean = new DBBean();
	}
	
	/**
	 * 根据key值查询系统变量
	 * @param key
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int querySysVal(final String key) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				
				sql.append("SELECT setting_value FROM hc_system_setting ")
				   .append("WHERE setting_name=? ")
				   .append("LIMIT 0,1 ");
				
				ps = cn.prepareStatement(sql.toString());
				ps.setString(1, key);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					sysVal = rs.getString("setting_value");
				}
				return 0;
			}
		});
	}
	
	public int queryTrainSystem(final String key) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				
				sql.append("SELECT setting_value FROM train_system_setting ")
				   .append("WHERE setting_name=? ")
				   .append("LIMIT 0,1 ");
				
				ps = cn.prepareStatement(sql.toString());
				ps.setString(1, key);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					sysVal = rs.getString("setting_value");
				}
				return 0;
			}
		});
	}
	
	
	/**
	 * 更新机器人空闲进程数
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int updateWorkerSpareThread(final String worker_id,final int spare_num) throws RepeatException, DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				// 更新机器人空闲进程数
				StringBuffer sql = new StringBuffer();
				sql.append("update cp_workerinfo set spare_thread=? where worker_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, spare_num);
				cs.setString(2, worker_id);
				cs.executeUpdate();
				logger.info("end update workerinfo workerid:"+worker_id+" spare_thread:"+spare_num);
				return 0;
			}
		});
	}
	public String getSysVal() {
		return sysVal;
	}

	public void setSysVal(String sysVal) {
		this.sysVal = sysVal;
	}

}
