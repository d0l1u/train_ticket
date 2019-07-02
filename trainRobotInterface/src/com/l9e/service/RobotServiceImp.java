package com.l9e.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseServlet;
import com.l9e.vo.RobotSetVo;
import com.l9e.vo.TrainNewData;
import com.l9e.vo.TrainNewDataFake;
import com.l9e.vo.WorkerVo;
import com.unlun.commons.database.DBBean;
import com.unlun.commons.database.ICallBack;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class RobotServiceImp {

	private Logger logger = Logger.getLogger(this.getClass());
	DBBean bean = null;

	private StringBuffer accountStr;
	public RobotSetVo robot = null;
	public List<TrainNewDataFake> list = null;
	public List<TrainNewData> list_data = null;
	public String phones = "";
	public String setting_value = "";

	public String getSetting_value() {
		return setting_value;
	}

	public void setSetting_value(String settingValue) {
		setting_value = settingValue;
	}

	public RobotServiceImp() {
		// Config.setConfigResource();
		bean = new DBBean();
	}

	public StringBuffer getAccountStr() {
		return accountStr;
	}

	public RobotSetVo getRobot() {
		return robot;
	}

	public void setRobot(RobotSetVo robot) {
		this.robot = robot;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	public List<TrainNewDataFake> getList() {
		return this.list;
	}
	
	public List<TrainNewData> getDataList() {
		return this.list_data;
	}
	

	/**
	 * 查询存活机器数
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryRobotCount(final String robot_type, final String channel)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select count(1) from robot_system_setting where robot_type='")
						.append(robot_type).append("'")
						.append(" and robot_channel like '%").append(channel)
						.append("%' and robot_query='1'");
				logger.info(sql.toString());
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					return Integer.parseInt(rs.getString(1));
				} else {
					return 0;
				}

			}
		});
	}

	/**
	 * 查询票价
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 * 
	 *             public int queryProperTrainNewData(final Map<String, String>
	 *             paramMap) throws RepeatException, DatabaseException{ return
	 *             bean.executeMethod(new ICallBack(){
	 * @Override public int execute(Connection cn, PreparedStatement cs) throws
	 *           SQLException { StringBuffer sql = new StringBuffer();
	 *           sql.append(
	 *           "SELECT cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1,swz,tdz,gws,gwx FROM t_zjpj_a "
	 *           )
	 *           .append("WHERE fz like '").append(paramMap.get("from_station"))
	 *           .append("%' AND dz like '").append(paramMap.get(
	 *           "arrive_station")) .append("%' "); cs =
	 *           cn.prepareStatement(sql.toString());
	 *           logger.info("query local price:"+sql.toString()); ResultSet rs
	 *           = cs.executeQuery(); list = new ArrayList<TrainNewDataFake>();
	 *           while(rs.next()){ TrainNewDataFake train = new
	 *           TrainNewDataFake(); train.setCc(rs.getString(1));
	 *           train.setFz(rs.getString(2)); train.setDz(rs.getString(3));
	 *           train.setYz(String.valueOf(rs.getDouble(4)));
	 *           train.setRz(String.valueOf(rs.getDouble(5)));
	 *           train.setYws(String.valueOf(rs.getDouble(6)));
	 *           train.setYwz(String.valueOf(rs.getDouble(7)));
	 *           train.setYwx(String.valueOf(rs.getDouble(8)));
	 *           train.setRws(String.valueOf(rs.getDouble(9)));
	 *           train.setRwx(String.valueOf(rs.getDouble(10)));
	 *           train.setRz2(String.valueOf(rs.getDouble(11)));
	 *           train.setRz1(String.valueOf(rs.getDouble(12)));
	 *           train.setSwz(String.valueOf(rs.getDouble(13)));
	 *           train.setTdz(String.valueOf(rs.getDouble(14)));
	 *           train.setGws(String.valueOf(rs.getDouble(15)));
	 *           train.setGwx(String.valueOf(rs.getDouble(16)));
	 *           list.add(train); } if(list.size()>0){ return 1; }else{ return
	 *           0; } }} ); }
	 */

	/**
	 * 查询票价
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryProperTrainNewData(final Map<String, String> paramMap)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				String from_station = paramMap.get("from_station");
				String arrive_station = paramMap.get("arrive_station");
				List<String> list_from = new ArrayList<String>();
				list_from.add(from_station);
				List<String> list_arrive = new ArrayList<String>();
				list_arrive.add(arrive_station);

				sql.append("SELECT station_key FROM t_station_match ")
						.append("WHERE station_name = '")
						.append(paramMap.get("from_station")).append("'");
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				String station_from_key = "";
				while (rs.next()) {
					station_from_key = rs.getString(1);
					sql = new StringBuffer();
					sql.append(
							"SELECT station_name,station_key FROM t_station_match ")
							.append("WHERE station_key = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, station_from_key);
					rs = cs.executeQuery();
					while (rs.next()) {
						String station_name = rs.getString(1);
						if (!from_station.equals(station_name)) {
							list_from.add(station_name);
						}
					}
				}

				String station_arrive_key = "";
				sql = new StringBuffer();
				sql.append("SELECT station_key FROM t_station_match ")
						.append("WHERE station_name = '")
						.append(paramMap.get("arrive_station")).append("'");
				cs = cn.prepareStatement(sql.toString());
				rs = cs.executeQuery();
				while (rs.next()) {
					station_arrive_key = rs.getString(1);
					sql = new StringBuffer();
					sql.append(
							"SELECT station_name,station_key FROM t_station_match ")
							.append("WHERE station_key = ?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, station_arrive_key);
					rs = cs.executeQuery();
					while (rs.next()) {
						String station_name = rs.getString(1);
						if (!arrive_station.equals(station_name)) {
							list_arrive.add(station_name);
						}
					}
				}
				list = new ArrayList<TrainNewDataFake>();
				if (station_from_key.equals(station_arrive_key)) {
					sql = new StringBuffer();
					sql.append(
							"SELECT cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1,swz,tdz,gws,gwx,dws,dwx,wz FROM t_zjpj_a ")
							.append("WHERE fz = '").append(from_station)
							.append("' AND dz = '").append(arrive_station)
							.append("' ");

					cs = cn.prepareStatement(sql.toString());
					rs = cs.executeQuery();
					while (rs.next()) {
						int index = 1;
						TrainNewDataFake train = new TrainNewDataFake();
						train.setCc(rs.getString(1));
						train.setFz(rs.getString(2));
						train.setDz(rs.getString(3));
						train.setYz(String.valueOf(rs.getDouble(4)));
						train.setRz(String.valueOf(rs.getDouble(5)));
						train.setYws(String.valueOf(rs.getDouble(6)));
						train.setYwz(String.valueOf(rs.getDouble(7)));
						train.setYwx(String.valueOf(rs.getDouble(8)));
						train.setRws(String.valueOf(rs.getDouble(9)));
						train.setRwx(String.valueOf(rs.getDouble(10)));
						train.setRz2(String.valueOf(rs.getDouble(11)));
						train.setRz1(String.valueOf(rs.getDouble(12)));
						train.setSwz(String.valueOf(rs.getDouble(13)));
						train.setTdz(String.valueOf(rs.getDouble(14)));
						train.setGws(String.valueOf(rs.getDouble(15)));
						train.setGwx(String.valueOf(rs.getDouble(16)));
						train.setDws(String.valueOf(rs.getDouble(17)));
						train.setDwx(String.valueOf(rs.getDouble(18)));
						train.setWz(String.valueOf(rs.getDouble(19)));
						list.add(train);
					}
				} else {
					
					
					for (String from_name : list_from) {
						
						for (String arrive_name : list_arrive) {
							
							long a=System.currentTimeMillis();
							
							sql = new StringBuffer();
							sql.append(
									"SELECT cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz2,rz1,swz,tdz,gws,gwx,dws,dwx,wz FROM t_zjpj_a ")
									.append("WHERE fz = '").append(from_name)
									.append("' AND dz = '").append(arrive_name)
									.append("' ");

							cs = cn.prepareStatement(sql.toString());
							rs = cs.executeQuery();
							while (rs.next()) {
								TrainNewDataFake train = new TrainNewDataFake();
								train.setCc(rs.getString(1));
								train.setFz(rs.getString(2));
								train.setDz(rs.getString(3));
								train.setYz(String.valueOf(rs.getDouble(4)));
								train.setRz(String.valueOf(rs.getDouble(5)));
								train.setYws(String.valueOf(rs.getDouble(6)));
								train.setYwz(String.valueOf(rs.getDouble(7)));
								train.setYwx(String.valueOf(rs.getDouble(8)));
								train.setRws(String.valueOf(rs.getDouble(9)));
								train.setRwx(String.valueOf(rs.getDouble(10)));
								train.setRz2(String.valueOf(rs.getDouble(11)));
								train.setRz1(String.valueOf(rs.getDouble(12)));
								train.setSwz(String.valueOf(rs.getDouble(13)));
								train.setTdz(String.valueOf(rs.getDouble(14)));
								train.setGws(String.valueOf(rs.getDouble(15)));
								train.setGwx(String.valueOf(rs.getDouble(16)));
								train.setDws(String.valueOf(rs.getDouble(17)));
								train.setDwx(String.valueOf(rs.getDouble(18)));
								train.setWz(String.valueOf(rs.getDouble(19)));
								list.add(train);
							}
							
							long b=System.currentTimeMillis();
							
						//	logger.info(from_station+arrive_station+"查询一次票价表所需时间："+list_from+","+list_arrive+","+(b-a)/1000f+"s");
							
						}
						
						
					}
				}

				if (list.size() > 0) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

	/**
	 * 查询具体机器配置
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public RobotSetVo queryRobot(final RobotSetVo robot)
			throws RepeatException, DatabaseException {
		bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select robot_id, robot_name,robot_url,robot_con_timeout,robot_read_timeout from robot_system_setting where robot_type='")
						.append(robot.getRobot_type()).append("'")
						.append(" and robot_channel like '%")
						.append(robot.getRobot_channel())
						.append("%' and robot_query='1' order by robot_id");
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();

				String robot_id = "";
				String robot_name = "";
				String robot_url = "";
				String robot_con_timeout = "";
				String robot_read_timeout = "";
				int i = 1;
				while (rs.next()) {
					if (i == 1) {
						// 备份机器人参数
						robot_id = rs.getString(1);
						robot_name = rs.getString(2);
						robot_url = rs.getString(3);
						robot_con_timeout = rs.getString(4);
						robot_read_timeout = rs.getString(5);
					}
					if (i == robot.getSelectNum()) {
						// 正常分配到的机器人参数
						robot_id = rs.getString(1);
						robot_name = rs.getString(2);
						robot_url = rs.getString(3);
						robot_con_timeout = rs.getString(4);
						robot_read_timeout = rs.getString(5);
						break;
					}
					i++;
				}
				robot.setRobot_id(robot_id);
				robot.setRobot_url(robot_url);
				robot.setRobot_name(robot_name);
				robot.setRobot_read_timeout(robot_read_timeout);
				robot.setRobot_con_timeout(robot_con_timeout);
				return 0;
			}
		});
		return robot;
	}

	/**
	 * 备用机器人
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int updateRobotSpareById(final String robot_id)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append(
						"update robot_system_setting set robot_query='3' where robot_id='")
						.append(robot_id).append("'");
				cs = cn.prepareStatement(sql.toString());
				cs.executeUpdate();
				return 0;
			}
		});
	}

	/**
	 * 停用机器人
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int updateRobotCloseById(final String robot_id)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append(
						"update robot_system_setting set robot_query='2' where robot_id='")
						.append(robot_id).append("'");
				cs = cn.prepareStatement(sql.toString());
				cs.executeUpdate();
				return 0;
			}
		});
	}

	/**
	 * 新增机器人异常通知信息
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int addRobotWarnMessage(final String phones,
			final String robot_name, final String content)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select count(1) from robot_warning where robot_name=?")
						.append(" AND  create_time > DATE_SUB(NOW(),INTERVAL 30 MINUTE)");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, robot_name);
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					int re_num = rs.getInt(1);
					if (re_num == 0) {
						sql = new StringBuffer();
						sql.append(
								"insert into robot_warning(telephone,robot_name,warn_status,create_time,send_num,content,notify_time) ")
								.append("values(?,?,'0',NOW(),0,?,NOW())");
						logger.info(sql.toString());
						cs = cn.prepareStatement(sql.toString());
						String[] phoneArr = phones.split(",");
						for (String phone : phoneArr) {
							cs.setString(1, phone);
							cs.setString(2, robot_name);
							cs.setString(3, content);

							cs.addBatch();
						}
						cs.executeBatch();
						return 0;
					} else {
						return 1;
					}
				} else {
					return 1;
				}
			}
		});
	}

	/**
	 * 随机获取一个备用机器并启用
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int querySpareRobot(final String robot_type, final String channel)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				robot = new RobotSetVo();

				sql = new StringBuffer();
				sql.append(
						"select robot_id, robot_name,robot_url,robot_con_timeout,robot_read_timeout from robot_system_setting where robot_query='3'")
						.append(" AND robot_type='").append(robot_type)
						.append("'").append(" AND robot_channel like '%")
						.append(channel).append("%' order by RAND() limit 1");
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {// 获取到账号
					robot.setRobot_id(rs.getString(1));
					robot.setRobot_name(rs.getString(2));
					robot.setRobot_url(rs.getString(3));
					robot.setRobot_con_timeout(rs.getString(4));
					robot.setRobot_read_timeout(rs.getString(5));
					sql = new StringBuffer();
					sql.append("update robot_system_setting set robot_query='1' where robot_id='");
					sql.append(robot.getRobot_id()).append("'");
					cs = cn.prepareStatement(sql.toString());
					cs.executeUpdate();

					return 0;
				} else {// 未获取到账号
					sql = new StringBuffer();
					sql.append(
							"select robot_id, robot_name,robot_url,robot_con_timeout,robot_read_timeout from robot_system_setting where robot_query='3' ")
							.append(" AND robot_type='").append(robot_type)
							.append("'").append(" order by RAND() limit 1");
					cs = cn.prepareStatement(sql.toString());
					rs = cs.executeQuery();
					if (rs.next()) {// 获取到账号
						robot.setRobot_id(rs.getString(1));
						robot.setRobot_name(rs.getString(2));
						robot.setRobot_url(rs.getString(3));
						robot.setRobot_con_timeout(rs.getString(4));
						robot.setRobot_read_timeout(rs.getString(5));
						sql = new StringBuffer();
						sql.append("update robot_system_setting set robot_query='1',robot_channel = CONCAT(robot_channel,?) where robot_id='");
						sql.append(robot.getRobot_id()).append("'");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, "," + channel);
						cs.executeUpdate();
						return 0;
					} else {
						return 1;
					}
				}
			}
		});
	}

	/**
	 * 给没有分配机器的渠道分配一个备用机器并启用
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int updateSpareRobot(final String rotbo_type, final String channel)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				robot = new RobotSetVo();
				sql.append(
						"select robot_id, robot_name,robot_url,robot_con_timeout,robot_read_timeout from robot_system_setting where robot_query='3'")
						.append(" AND robot_type='").append(rotbo_type)
						.append("'").append(" AND robot_channel like '%")
						.append(channel).append("%' limit 1");
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {// 获取到账号
					robot.setRobot_id(rs.getString(1));
					robot.setRobot_name(rs.getString(2));
					robot.setRobot_url(rs.getString(3));
					robot.setRobot_con_timeout(rs.getString(4));
					robot.setRobot_read_timeout(rs.getString(5));
					logger.info("spare:robot_id:" + robot.getRobot_id()
							+ " robot_name:" + robot.getRobot_name());
					sql = new StringBuffer();
					sql.append("update robot_system_setting set robot_query='1' where robot_id='");
					sql.append(robot.getRobot_id()).append("'");
					cs = cn.prepareStatement(sql.toString());
					cs.executeUpdate();
					return 0;
				} else {
					// sql = new StringBuffer();
					// sql.append("select robot_id, robot_name,robot_url,robot_con_timeout,robot_read_timeout from robot_system_setting where robot_query='1' AND robot_channel not like '%")
					// .append(channel).append("%'limit 1");
					// cs = cn.prepareStatement(sql.toString());
					// rs = cs.executeQuery();
					// if(rs.next()){// 获取到账号
					// robot.setRobot_id(rs.getString(1));
					// robot.setRobot_name(rs.getString(2));
					// robot.setRobot_url(rs.getString(3));
					// robot.setRobot_con_timeout(rs.getString(4));
					// robot.setRobot_read_timeout(rs.getString(5));
					// logger.info("work:robot_id:"+robot.getRobot_id()+" robot_name:"+robot.getRobot_name());
					// sql = new StringBuffer();
					// sql.append("update robot_system_setting set robot_channel = CONCAT(robot_channel,?) where robot_id='");
					// sql.append(robot.getRobot_id()).append("'");
					// cs = cn.prepareStatement(sql.toString());
					// cs.setString(1, ","+channel);
					// cs.executeUpdate();
					// return 0;
					// }else{
					logger.info("no spare robot allot");
					return 1;
					// }

				}

			}
		});
	}

	/**
	 * 获取机器人异常待通知人员联系方式
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int queryRobotWarnPhone() throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("select setting_value from hc_system_setting where setting_name='warn_telephone'");
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					phones = rs.getString("setting_value");
				}
				return 0;
			}
		});
	}

	/**
	 * 插入到带查询票价表中
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int addWaitPrice(final Map<String, String> map)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select count(1) from robot_price_manager where from_station_no='")
						.append(map.get("from_station_no"))
						.append("' AND train_no='").append(map.get("train_no"))
						.append("' AND to_station_no='")
						.append(map.get("to_station_no")).append("'")
						.append(" AND type = 'query'");
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					int count = rs.getInt(1);
					if (count == 0) {
						sql = new StringBuffer();
						sql.append(
								"insert into robot_price_manager(train_no,from_station_no,to_station_no,seat_types,train_date,train_code,from_station_name,to_station_name,status,create_time,type) ")
								.append(" values(?,?,?,?,?,?,?,?,'00',NOW(),'query')");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, map.get("train_no"));
						cs.setString(2, map.get("from_station_no"));
						cs.setString(3, map.get("to_station_no"));
						cs.setString(4, map.get("seat_types"));
						cs.setString(5, map.get("train_date"));
						cs.setString(6, map.get("train_code"));
						cs.setString(7, map.get("from_station_name"));
						cs.setString(8, map.get("to_station_name"));
						cs.executeUpdate();
					}
					return 1;
				}
				return 0;
			}
		});
	}

	/**
	 * 插入到待删除票价表中
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int addDeletePrice(final TrainNewDataFake train)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("select count(1) from robot_delete_price where cc='")
						.append(train.getCc()).append("' AND fz='")
						.append(train.getFz()).append("' AND dz='")
						.append(train.getDz()).append("'");
				cs = cn.prepareStatement(sql.toString());
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					int count = rs.getInt(1);
					if (count == 0) {
						sql = new StringBuffer();
						sql.append(
								"insert into robot_delete_price(cc,fz,dz,yz,rz,yws,ywz,ywx,rws,rwx,rz1,rz2,tdz,swz,gws,gwx) ")
								.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						cs = cn.prepareStatement(sql.toString());
						int index = 1;
						cs.setString(index++, train.getCc());
						cs.setString(index++, train.getFz());
						cs.setString(index++, train.getDz());
						// if("-".equals(train.getWz())){
						// cs.setDouble(index++, 0);
						// }else{
						// cs.setDouble(index++, Double.valueOf(train.getWz()));
						// }
						if ("-".equals(train.getYz())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++, Double.valueOf(train.getYz()));
						}
						if ("-".equals(train.getRz())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++, Double.valueOf(train.getRz()));
						}
						if ("-".equals(train.getYws())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getYws()));
						}
						if ("-".equals(train.getYwz())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getYwz()));
						}
						if ("-".equals(train.getYwx())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getYwx()));
						}
						if ("-".equals(train.getRws())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getRws()));
						}
						if ("-".equals(train.getRwx())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getRwx()));
						}
						if ("-".equals(train.getRz1())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getRz1()));
						}
						if ("-".equals(train.getRz2())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getRz2()));
						}
						if ("-".equals(train.getTdz())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getTdz()));
						}
						if ("-".equals(train.getSwz())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getSwz()));
						}
						if ("-".equals(train.getGws())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getGws()));
						}
						if ("-".equals(train.getGwx())) {
							cs.setDouble(index++, 0);
						} else {
							cs.setDouble(index++,
									Double.valueOf(train.getGwx()));
						}
						cs.executeUpdate();

						sql = new StringBuffer();
						sql = new StringBuffer();
						sql.append(
								"insert into robot_price_manager(train_code,from_station_name,to_station_name,create_time,type) ")
								.append(" values(?,?,?,NOW(),'delete')");
						cs = cn.prepareStatement(sql.toString());
						cs.setString(1, train.getCc());
						cs.setString(2, train.getFz());
						cs.setString(3, train.getDz());
						cs.executeUpdate();
						logger.info("wait delete train-code :" + train.getCc()
								+ "/" + train.getFz() + "/" + train.getDz());
					}
					return 1;
				}
				return 0;
			}
		});
	}

	/**
	 * 官网异常，暂时停用查询
	 * 
	 * @param supOrder
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public int querySystemSetting(final String sys_string)
			throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("select setting_value from train_system_setting where setting_name=? and setting_status='1'");
				cs = cn.prepareStatement(sql.toString());
				cs.setString(1, sys_string);
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					setting_value = rs.getString(1);
				}
				return 0;
			}
		});
	}

	//***************************************************************数据库操作新加入***********************************************************************//
	/**
	 * 查询数据库中的车次
	 * 
	 * @param param
	 * @return
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	public int  queryDataBaseCheci(final Map<String, String> param) throws RepeatException, DatabaseException {
		return bean.executeMethod(new ICallBack(){
			@Override
			public int execute(Connection cn, PreparedStatement cs)
					throws SQLException {
				
				StringBuffer sql = new StringBuffer();
				String  from_station=param.get("from_station");
				String  arrive_station=param.get("arrive_station");
				sql.append("SELECT  cc,fz,dz  FROM  t_zjpj_a   WHERE  fz LIKE '")
				.append(from_station).append("%'  AND   dz LIKE '")
				.append(arrive_station).append("%'");
				cs = cn.prepareStatement(sql.toString());
				logger.info(sql.toString());
				ResultSet rs = cs.executeQuery();
				List<TrainNewData> new_list = new ArrayList<TrainNewData>();
				while (rs.next()) {
					TrainNewData trainNewData=new TrainNewData();
					trainNewData.setStation_train_code(rs.getString(1));
					trainNewData.setFrom_station_name(rs.getString(2));
					trainNewData.setTo_station_name(rs.getString(3));
					new_list.add(trainNewData);
				}		
				List<TrainNewData> new_list1 = new ArrayList<TrainNewData>();
				for(TrainNewData trainNewData:new_list) {
					String station_train_code=trainNewData.getStation_train_code();
					String start_sation_name=trainNewData.getFrom_station_name();
					String end_sation_name=trainNewData.getTo_station_name();
					String startStation="";//起始站
					String endStation="";//到达站
				    String startTime=""; //出发时间
				    String arriveTime=""; //到达时间 
				    String costday="";//耗时天数

					sql = new StringBuffer();
					//SELECT  *   FROM  t_sinfo   WHERE   checi ='K1454'  ORDER  BY stationno  ASC  LIMIT  1
					sql.append("SELECT  name   FROM  t_sinfo   WHERE   checi =")
					.append("?")
					.append(" ORDER  BY stationno  ASC  LIMIT  1");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, station_train_code);		
					rs = cs.executeQuery();
					if(rs.next()) {
						startStation=rs.getString(1);
					}else {
						startStation="";
					}
					
					trainNewData.setStart_station_name(startStation);
					
					sql = new StringBuffer();
					//SELECT  *   FROM  t_sinfo   WHERE   checi ='K1454'  ORDER  BY stationno  ASC  LIMIT  1
					sql.append("SELECT  name   FROM  t_sinfo   WHERE   checi =")
					.append("?")
					.append("  ORDER  BY stationno  DESC  LIMIT  1");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, station_train_code);
					rs = cs.executeQuery();
					if(rs.next()) {
						endStation=rs.getString(1);
					}else {
						endStation="";
					}
					trainNewData.setEnd_station_name(endStation);
					
					sql = new StringBuffer();
					//SELECT   starttime   FROM  t_sinfo   WHERE   checi ='K1454'  AND  NAME ='霸州'
					sql.append("SELECT   starttime   FROM  t_sinfo   WHERE   checi =")
					.append("?")
					.append("  AND  name =")
					.append("?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, station_train_code);
					cs.setString(2, start_sation_name);//出发站
					rs = cs.executeQuery();
					if(rs.next()) {
						startTime=rs.getString(1);
					}else {
						startTime="";
					}
					if(StringUtil.isEmpty(startTime)) {
						logger.info("车次："+station_train_code+"没有途经站！");
						continue;
					}
					
					trainNewData.setStart_time(startTime);
					
					sql = new StringBuffer();
					//SELECT   arrtime   FROM  t_sinfo   WHERE   checi ='K1454'  AND  NAME ='兴国'
					sql.append("SELECT   arrtime   FROM  t_sinfo   WHERE   checi =")
					.append("?")
					.append("  AND  name =")
					.append("?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, station_train_code);
					cs.setString(2, end_sation_name);//到达站
				    rs = cs.executeQuery();
					if(rs.next()) {
						arriveTime=rs.getString(1);
					}else {
						arriveTime="";
					}
					
					if(StringUtil.isEmpty(arriveTime)) {
						logger.info("车次："+station_train_code+"没有途经站！");
						continue;
					}
					
					trainNewData.setArrive_time(arriveTime);
					
					 sql = new StringBuffer();
					//SELECT   costday   FROM  t_sinfo   WHERE   checi ='K1454'  AND  NAME ='霸州'
					sql.append("SELECT   costday   FROM  t_sinfo   WHERE   checi =")
					.append("?")
					.append("  AND  name =")
					.append("?");
					cs = cn.prepareStatement(sql.toString());
					cs.setString(1, station_train_code);
					cs.setString(2, end_sation_name);
					rs = cs.executeQuery();
					if(rs.next()) {
						costday=rs.getString(1);
					}else {
						costday="";
					}
					trainNewData.setControl_day(costday);
					new_list1.add(trainNewData);
				}

				list_data=new_list1;
				return 0;			
			}
		});
	}

	/**
	 * 查询新表的，查询机器人,
	 * @param robot2
	 * @return
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	public WorkerVo queryRobotNewOne(final WorkerVo workerVo) throws RepeatException, DatabaseException {
		bean.executeMethod(new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				StringBuffer sql = new StringBuffer();
				sql.append("select  worker_id ,worker_name,worker_type,worker_status,worker_ext  from  cp_workerinfo  where   worker_type=? and  worker_status =?   group by rand() limit 1");
				cs = cn.prepareStatement(sql.toString());
				int index=0;
				cs.setString(++index, "9");//查询机器人
				cs.setString(++index, "00");//空闲
				ResultSet rs = cs.executeQuery();

				String worker_id = "";
				String worker_name = "";
				String worker_type = "";
				String worker_status = "";
				String worker_ext = "";
				int i = 1;
				if (rs.next()) {
					worker_id = rs.getString(1);
					worker_name = rs.getString(2);
					worker_type = rs.getString(3);
					worker_status = rs.getString(4);
					worker_ext = rs.getString(5);
				}
				workerVo.setWorker_id(worker_id);
				workerVo.setWorker_name(worker_name);
				workerVo.setWorker_type(worker_type);
				workerVo.setWorker_status(worker_status);
				workerVo.setWorker_ext(worker_ext);
				return 0;
			}
		});
		
		return workerVo;
	}
	
	
	/**
	 * 查询新表的，查询机器人,
	 * @param paramMap 
	 * @param robot2
	 * @return
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	public List<WorkerVo> queryRobotNewList(final List<WorkerVo> workerVoList,final Map<String, Object> paramMap) throws RepeatException, DatabaseException {
		
		 ICallBack callBack=new ICallBack() {
			@Override
			public int execute(Connection cn, PreparedStatement cs) throws SQLException {
				
				StringBuffer sql = new StringBuffer();
				sql.append("select  worker_id ,worker_name,worker_type,worker_status,worker_ext  from  cp_workerinfo  where   worker_type=? and  worker_status =?  ORDER BY worker_id LIMIT 0,?");
				cs = cn.prepareStatement(sql.toString());
				int index=0;
				cs.setString(++index, (String)paramMap.get("worker_type"));//查询机器人
				cs.setString(++index, (String)paramMap.get("worker_status"));//空闲
				cs.setInt(++index, (Integer)paramMap.get("limitCount"));//取的个数
				ResultSet rs = cs.executeQuery();
				int i = 1;
				while (rs.next()) {
					WorkerVo workerVo=new WorkerVo();
					workerVo.setWorker_id(rs.getString(1));
					workerVo.setWorker_name(rs.getString(2));
					workerVo.setWorker_type(rs.getString(3));
					workerVo.setWorker_status(rs.getString(4));
					workerVo.setWorker_ext(rs.getString(5));
					workerVoList.add(workerVo);
				}
				
				return 0;
			}
		};
		
		int flag=bean.executeMethod(callBack);
		logger.info("queryRobotNewList:::"+flag);
		return workerVoList;
	}
	
	
}
