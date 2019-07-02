package com.l9e.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.train.po.ReturnOptlog;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class SysSettingServiceImpl {
	private Logger logger=Logger.getLogger(this.getClass());
	
	private DBBean dbbean = null;
	private String sysVal = null;
	public String phones = "";
	
	private List<ReturnOptlog> list_return = null;
	
	public List<ReturnOptlog> getList_return() {
		return list_return;
	}


	public void setList_return(List<ReturnOptlog> listReturn) {
		list_return = listReturn;
	}


	public SysSettingServiceImpl() {
		dbbean = new DBBean();
	}
	
	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
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
				sql.append("SELECT return_id,return_name,return_type,return_join FROM train_return_optlog ")
						.append("WHERE return_status='1' and return_ticket='11' and return_active='11'");

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
					list_return.add(ro);
				}
				return 0;
			}
		});
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
	
	/**
	 * 停用机器人
	 * 
	 * @param orderId
	 * @param optlog
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int stopWorker(final String worker_id) throws RepeatException, DatabaseException {
		return dbbean.executeMethod(new ICallBack() {
			
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				// 更新机器人空闲进程数
				StringBuffer sql = new StringBuffer();
				sql.append("update cp_workerinfo set worker_status='22' where worker_id=?");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, worker_id);
				cs.executeUpdate();
				logger.info("end update workerinfo stop");
				return 0;
			}
		});
	}
	
	/**
	 * 获取机器人异常待通知人员联系方式
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryRobotWarnPhone() throws RepeatException, DatabaseException{
		return dbbean.executeMethod(new ICallBack(){
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("select setting_value from train_system_setting where setting_name='pay_robot_telephone'");
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				if(rs.next()){
					phones = rs.getString("setting_value");
				}
				return 0;
			}} );
	}
	public String getSysVal() {
		return sysVal;
	}

	public void setSysVal(String sysVal) {
		this.sysVal = sysVal;
	}

}
