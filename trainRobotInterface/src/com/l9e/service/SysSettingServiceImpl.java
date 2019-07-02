package com.l9e.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class SysSettingServiceImpl {
	private Logger logger=Logger.getLogger(this.getClass());
	private DBBean dbbean = null;
	private String sysVal = null;
	
	public SysSettingServiceImpl() {
		dbbean = new DBBean();
	}

	/**
	 * 根据key值查询系统变量
	 * 
	 * @param key
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int querySysVal(final String key) throws RepeatException,
			DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
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
					sysVal = rs.getString("setting_value");
				}
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
