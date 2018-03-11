package com.l9e.transaction.vo;
/**
 * 白名单实体类
 * @author caona
 * @date 2016年8月5日
 */
public class TuniuWhitePass {
	private Integer id;
	//联系人姓名
	private String contact_name;
	//联系人状态0-已通过 1-待核验 2-未通过 
	private Integer contact_status;
	//身份证号
	private String cert_no;
	// 证件类型2、一代身份证、1、二代身份证、C、港澳通行证、G、台湾通行证、B、护照  
	private String cert_type;
	//联系人类型：1成人 2儿童 3学生
	private Integer person_type;
	//12306账号
	private String acc_username;
	//账号表主键
	private Integer acc_id;
	//账号状态 0-已锁定  1-待手机核验  2-正常使用 99-未确定
	private Integer acc_status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contactName) {
		contact_name = contactName;
	}
	public Integer getContact_status() {
		return contact_status;
	}
	public void setContact_status(Integer contactStatus) {
		contact_status = contactStatus;
	}
	public String getCert_no() {
		return cert_no;
	}
	public void setCert_no(String certNo) {
		cert_no = certNo;
	}
	public String getCert_type() {
		return cert_type;
	}
	public void setCert_type(String certType) {
		cert_type = certType;
	}
	public Integer getPerson_type() {
		return person_type;
	}
	public void setPerson_type(Integer personType) {
		person_type = personType;
	}
	public String getAcc_username() {
		return acc_username;
	}
	public void setAcc_username(String accUsername) {
		acc_username = accUsername;
	}
	public Integer getAcc_id() {
		return acc_id;
	}
	public void setAcc_id(Integer accId) {
		acc_id = accId;
	}
	public Integer getAcc_status() {
		return acc_status;
	}
	public void setAcc_status(Integer accStatus) {
		acc_status = accStatus;
	}
	

}
