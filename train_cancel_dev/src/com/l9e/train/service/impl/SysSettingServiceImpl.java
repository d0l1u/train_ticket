package com.l9e.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class SysSettingServiceImpl {
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
	
	public String getSysVal() {
		return sysVal;
	}

	public void setSysVal(String sysVal) {
		this.sysVal = sysVal;
	}

}
