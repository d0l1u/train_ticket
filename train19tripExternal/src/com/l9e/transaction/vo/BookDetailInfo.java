package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * 预订明细信息vo
 * @author zhangjun
 *
 */
public class BookDetailInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String user_name;//姓名
	
	private String ticket_type;//车票类型0：成人票 1：儿童票 3:学生票
	
	private String ids_type;//证件类型1、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
	
	private String user_ids;//证件号码
	
	private String bx;		//是否购买保险，1：购买，0：不购买
	
	private String bx_code;		//保险单号
	
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

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getTicket_type() {
		return ticket_type;
	}

	public void setTicket_type(String ticket_type) {
		this.ticket_type = ticket_type;
	}

	public String getIds_type() {
		return ids_type;
	}

	public void setIds_type(String ids_type) {
		this.ids_type = ids_type;
	}

	public String getUser_ids() {
		return user_ids;
	}

	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
	}

	public String getBx() {
		return bx;
	}

	public void setBx(String bx) {
		this.bx = bx;
	}

	public String getBx_code() {
		return bx_code;
	}

	public void setBx_code(String bxCode) {
		bx_code = bxCode;
	}

}
