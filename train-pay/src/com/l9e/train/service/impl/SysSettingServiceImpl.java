package com.l9e.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.ReturnOptlog;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class SysSettingServiceImpl {

	private Logger logger = LoggerFactory.getLogger(SysSettingServiceImpl.class);	
	
	private DBBean dbbean = null;
	private String sysVal = null;
	public String phones = "";
	private List<ReturnOptlog> list_return = null;

	public List<ReturnOptlog> getList_return() {
		return this.list_return;
	}

	public void setList_return(List<ReturnOptlog> listReturn) {
		this.list_return = listReturn;
	}

	public SysSettingServiceImpl() {
		this.dbbean = new DBBean();
	}

	public String getPhones() {
		return this.phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	public int queryLogList() throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT return_id,return_name,return_type,return_join FROM train_return_optlog ")
						.append("WHERE return_status='1' and return_ticket='11' and return_active='11'");

				ps = cn.prepareStatement(sql.toString());
				ResultSet rs = ps.executeQuery();
				SysSettingServiceImpl.this.list_return = new ArrayList();
				ReturnOptlog ro = null;
				while (rs.next()) {
					ro = new ReturnOptlog();
					ro.setReturn_id(rs.getString("return_id"));
					ro.setReturn_join(rs.getString("return_join"));
					ro.setReturn_name(rs.getString("return_name"));
					ro.setReturn_type(rs.getString("return_type"));
					SysSettingServiceImpl.this.list_return.add(ro);
				}
				return 0;
			}
		});
	}

	public int querySysVal(final String key) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement ps) throws SQLException {
				StringBuffer sql = new StringBuffer();

				sql.append("SELECT setting_value FROM train_system_setting ").append("WHERE setting_name=? ").append("LIMIT 0,1 ");

				ps = cn.prepareStatement(sql.toString());
				ps.setString(1, key);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					SysSettingServiceImpl.this.sysVal = rs.getString("setting_value");
				}
				return 0;
			}
		});
	}

	public int updateWorkerSpareThread(final String worker_id, final int spare_num) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("update cp_workerinfo set spare_thread=? where worker_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setInt(1, spare_num);
				cs.setString(2, worker_id);
				cs.executeUpdate();
				SysSettingServiceImpl.this.logger.info("end update workerinfo workerid:" + worker_id + " spare_thread:" + spare_num);
				return 0;
			}
		});
	}

	public int stopWorker(final String worker_id) throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("update cp_workerinfo set worker_status='22' where worker_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, worker_id);
				cs.executeUpdate();
				SysSettingServiceImpl.this.logger.info("end update workerinfo stop");
				return 0;
			}
		});
	}

	public int queryRobotWarnPhone() throws RepeatException, DatabaseException {
		return this.dbbean.executeMethod(new ICallBack() {
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("select setting_value from train_system_setting where setting_name='pay_robot_telephone'");
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					SysSettingServiceImpl.this.phones = rs.getString("setting_value");
				}
				return 0;
			}
		});
	}

	public String getSysVal() {
		return this.sysVal;
	}

	public void setSysVal(String sysVal) {
		this.sysVal = sysVal;
	}
}
