package com.l9e.vo;
/**
 * 机器人配置类
 * */
public class RobotSetVo {
	private String robot_id;
	private String robot_name;
	private String robot_url;
	private String robot_channel;
	private String robot_desc;
	private String robot_type;
	private String robot_status;
	private String robot_con_timeout;
	private String robot_read_timeout;
	
	private int selectNum;//被选择
	private int count;//
	public int getSelectNum() {
		return selectNum;
	}
	public void setSelectNum(int selectNum) {
		this.selectNum = selectNum;
	}
	public String getRobot_id() {
		return robot_id;
	}
	public void setRobot_id(String robotId) {
		robot_id = robotId;
	}
	public String getRobot_name() {
		return robot_name;
	}
	public void setRobot_name(String robotName) {
		robot_name = robotName;
	}
	public String getRobot_url() {
		return robot_url;
	}
	public void setRobot_url(String robotUrl) {
		robot_url = robotUrl;
	}
	public String getRobot_channel() {
		return robot_channel;
	}
	public void setRobot_channel(String robotChannel) {
		robot_channel = robotChannel;
	}
	public String getRobot_desc() {
		return robot_desc;
	}
	public void setRobot_desc(String robotDesc) {
		robot_desc = robotDesc;
	}
	public String getRobot_type() {
		return robot_type;
	}
	public void setRobot_type(String robotType) {
		robot_type = robotType;
	}
	public String getRobot_status() {
		return robot_status;
	}
	public void setRobot_status(String robotStatus) {
		robot_status = robotStatus;
	}
	public String getRobot_con_timeout() {
		return robot_con_timeout;
	}
	public void setRobot_con_timeout(String robotConTimeout) {
		robot_con_timeout = robotConTimeout;
	}
	public String getRobot_read_timeout() {
		return robot_read_timeout;
	}
	public void setRobot_read_timeout(String robotReadTimeout) {
		robot_read_timeout = robotReadTimeout;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
