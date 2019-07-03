package com.l9e.transaction.vo;

public class DBStudentInfo {
	private String stu_name;
	private String cp_id      ;        
	private String order_id     ; //     
	private String province_name	; //  string	省份名称：参考附件5.4 的provinces
	private String province_code	; //  string	省份编号：参考附件5.4 的provinces
	private String school_code	; //  string	学校代号
	private String school_name	; //  string	学校名称
	private String student_no	; //  string	学号
	private String school_system	; //  string	学制
	private String enter_year	; //  string	入学年份：yyyy
	private String preference_from_station_name	; //  string	优惠区间起始地名称【选填】
	private String preference_from_station_code; //  	string	优惠区间起始地代号
	private String preference_to_station_name	; //  string	优惠区间到达地名称【选填】
	private String preference_to_station_code	; //  string	优惠区间到达地代号
	private String channel;
	
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCp_id() {
		return cp_id;
	}
	public void setCp_id(String cpId) {
		cp_id = cpId;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getProvince_name() {
		return province_name;
	}
	public void setProvince_name(String provinceName) {
		province_name = provinceName;
	}
	public String getProvince_code() {
		return province_code;
	}
	public void setProvince_code(String provinceCode) {
		province_code = provinceCode;
	}
	public String getSchool_code() {
		return school_code;
	}
	public void setSchool_code(String schoolCode) {
		school_code = schoolCode;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String schoolName) {
		school_name = schoolName;
	}
	public String getStudent_no() {
		return student_no;
	}
	public void setStudent_no(String studentNo) {
		student_no = studentNo;
	}
	public String getSchool_system() {
		return school_system;
	}
	public void setSchool_system(String schoolSystem) {
		school_system = schoolSystem;
	}
	public String getEnter_year() {
		return enter_year;
	}
	public void setEnter_year(String enterYear) {
		enter_year = enterYear;
	}
	public String getPreference_from_station_name() {
		return preference_from_station_name;
	}
	public void setPreference_from_station_name(String preferenceFromStationName) {
		preference_from_station_name = preferenceFromStationName;
	}
	public String getPreference_from_station_code() {
		return preference_from_station_code;
	}
	public void setPreference_from_station_code(String preferenceFromStationCode) {
		preference_from_station_code = preferenceFromStationCode;
	}
	public String getPreference_to_station_name() {
		return preference_to_station_name;
	}
	public void setPreference_to_station_name(String preferenceToStationName) {
		preference_to_station_name = preferenceToStationName;
	}
	public String getPreference_to_station_code() {
		return preference_to_station_code;
	}
	public void setPreference_to_station_code(String preferenceToStationCode) {
		preference_to_station_code = preferenceToStationCode;
	}

}
