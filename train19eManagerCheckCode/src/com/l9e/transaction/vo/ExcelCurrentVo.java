package com.l9e.transaction.vo;

public class ExcelCurrentVo {
	private String opt_ren;//用户名
	private String opt_name;//真实姓名
	private String department;//部门
	private Integer pic_count;//打码总数
	private Integer pic_success;//打码成功数
	private Integer pic_fail;//打码失败数
	private Integer pic_unkonwn;//打码超时数
	private String time;//渠道
	private String successPreCount;//上月收入
	public String getOpt_ren() {
		return opt_ren;
	}
	public void setOpt_ren(String optRen) {
		opt_ren = optRen;
	}
	public String getOpt_name() {
		return opt_name;
	}
	public void setOpt_name(String optName) {
		opt_name = optName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Integer getPic_count() {
		return pic_count;
	}
	public void setPic_count(Integer picCount) {
		pic_count = picCount;
	}
	public Integer getPic_success() {
		return pic_success;
	}
	public void setPic_success(Integer picSuccess) {
		pic_success = picSuccess;
	}
	public Integer getPic_fail() {
		return pic_fail;
	}
	public void setPic_fail(Integer picFail) {
		pic_fail = picFail;
	}
	public Integer getPic_unkonwn() {
		return pic_unkonwn;
	}
	public void setPic_unkonwn(Integer picUnkonwn) {
		pic_unkonwn = picUnkonwn;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSuccessPreCount() {
		return successPreCount;
	}
	public void setSuccessPreCount(String successPreCount) {
		this.successPreCount = successPreCount;
	}
	
	
}
