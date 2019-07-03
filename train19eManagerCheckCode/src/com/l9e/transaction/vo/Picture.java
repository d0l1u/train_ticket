package com.l9e.transaction.vo;

import java.io.Serializable;

public class Picture implements Serializable {
	public static final int PICOFF=0;//闲置状态
	public static final int PICON=1;//处理中
	public static final int SUCCESS=1;//验证成功   合作伙伴提交的验证结果
	public static final int FAIL=0;//验证失败  合作伙伴提交的验证结果
	public static final String INIT_TIME_SPAN="25";
	
	private String pic_id;//图片ID
	private String pic_filename;//图片保存文件名
	private String verify_code;//图片验证码
	private String create_time;//图片创建时间
	private String effect_time;//有效时间
	private String finish_time;//完成时间
	private String opt_ren;//操作人
	private String status;//00、未输入 11、已输入
	private String channel;//渠道
	private String order_id;
	private int result_status;//图片验证结果
	private int user_pic_status;//图片是否被占用
	private String span_time;//有效的生存期  //验证码的有效时间间隔 单位为秒
	private String mistiming;//距离现在的时间差
	private String shyzmid;//商户验证码id（用于反馈商户 验证码错误）
	private String qunar_code;
	private String update_status;//上传状态 11、成功 22、未上传
	private String department;//验证码分配部门   11打码团队1    22打码团队2   33打码团队3    99打码团队其他
	private String verify_code_coord;//图片验证码结果的坐标位置
	
	public String getQunar_code() {
		return qunar_code;
	}
	public void setQunar_code(String qunarCode) {
		qunar_code = qunarCode;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getPic_id() {
		return pic_id;
	}
	public void setPic_id(String picId) {
		pic_id = picId;
	}
	public String getPic_filename() {
		return pic_filename;
	}
	public void setPic_filename(String picFilename) {
		pic_filename = picFilename;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getEffect_time() {
		return effect_time;
	}
	public void setEffect_time(String effectTime) {
		effect_time = effectTime;
	}
	public String getFinish_time() {
		return finish_time;
	}
	public void setFinish_time(String finishTime) {
		finish_time = finishTime;
	}
	public String getVerify_code() {
		return verify_code;
	}
	public void setVerify_code(String verifyCode) {
		verify_code = verifyCode;
	}
	public String getOpt_ren() {
		return opt_ren;
	}
	public void setOpt_ren(String optRen) {
		opt_ren = optRen;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public int getResult_status() {
		return result_status;
	}
	public void setResult_status(int resultStatus) {
		result_status = resultStatus;
	}
	public int getUser_pic_status() {
		return user_pic_status;
	}
	public void setUser_pic_status(int userPicStatus) {
		user_pic_status = userPicStatus;
	}
	public String getSpan_time() {
		return span_time;
	}
	public void setSpan_time(String spanTime) {
		span_time = spanTime;
	}
	public String getMistiming() {
		return mistiming;
	}
	public void setMistiming(String mistiming) {
		this.mistiming = mistiming;
	}
	public String getShyzmid() {
		return shyzmid;
	}
	public void setShyzmid(String shyzmid) {
		this.shyzmid = shyzmid;
	}
	public String getUpdate_status() {
		return update_status;
	}
	public void setUpdate_status(String updateStatus) {
		update_status = updateStatus;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getVerify_code_coord() {
		return verify_code_coord;
	}
	public void setVerify_code_coord(String verifyCodeCoord) {
		verify_code_coord = verifyCodeCoord;
	}
	
}
