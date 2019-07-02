package com.l9e.transaction.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.ReturnOptlog;
import com.l9e.transaction.vo.Worker;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class SysSettingServiceImpl {
	private Logger logger=Logger.getLogger(this.getClass());
	private DBBean dbbean = null;
	private String sysVal = null;
	
	private List<ReturnOptlog> list_return = null;

	public SysSettingServiceImpl() {
		dbbean = new DBBean();
	}

	
	/**
	 * 日志内容
	 * 
	 * @param key
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryLogList() throws RepeatException,
			DatabaseException {
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT return_id,return_name,return_type,return_join,return_fail_reason FROM train_return_optlog ")
						.append("WHERE return_status='1' and return_ticket='11' and return_active='00'");

				ps = cn.prepareStatement(sql.toString());
				ResultSet rs = ps.executeQuery();
				list_return = new ArrayList<ReturnOptlog>();
				ReturnOptlog ro = null;
				while (rs.next()) {
					ro = new ReturnOptlog();
					ro.setReturn_id(rs.getString("return_id"));
					ro.setReturn_join(rs.getString("return_join"));
					ro.setReturn_name(rs.getString("return_name"));
					ro.setReturn_type(rs.getString("return_type"));
					ro.setFail_reason(rs.getString("return_fail_reason"));
					list_return.add(ro);
				}
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

	/**
	 * 根据work_id值将机器人停用
	 * 
	 * @param key
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int updateRobot(final String work_id) throws RepeatException,
			DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				StringBuffer sql = new StringBuffer();

				sql.append(
						"UPDATE cp_workinfo set worker_status=?, opt_time=? ")
						.append("WHERE work_id=? ");

				ps = cn.prepareStatement(sql.toString());
				ps.setString(1, Worker.STATUS_STOP);
				ps.setDate(2, new java.sql.Date(new Date().getTime()));
				ps.setInt(3, Integer.parseInt(work_id));
				int rs = ps.executeUpdate();

				return 0;
			}
		});
	}
	
	/**
	 * 向警告表中插入警告信息
	 * 
	 * @param key
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int insertWarning(final String robot_name) throws RepeatException,
			DatabaseException {
		// TODO Auto-generated method stub
		return dbbean.executeMethod(new ICallBack() {

			@Override
			public int execute(Connection cn, PreparedStatement ps)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				StringBuffer sb = new StringBuffer();
				String telephone = null;
				sb.append("SELECT warn_telephone FROM hc_system_setting WHERE setting_name='warn_telephone'");
				ps = cn.prepareStatement(sb.toString());
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					telephone = rs.getString("warn_telephone");
				}
				sql.append(
						"INSERT INTO robot_warning(telephone, robot_name, status, create_time, opt_person, content) ")
						.append("VALUE (?, ?, ?, NOW(), 'APP_ROBOT', ?)");

				ps = cn.prepareStatement(sql.toString());
				ps.setString(1, telephone);
				ps.setString(2, robot_name);
				ps.setString(3, "0");
				ps.setString(4, "机器人" + robot_name + "需要重启，请尽快处理！");
				boolean result = ps.execute();

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


	public List<ReturnOptlog> getList_return() {
		return list_return;
	}


	public void setList_return(List<ReturnOptlog> listReturn) {
		list_return = listReturn;
	}

}
