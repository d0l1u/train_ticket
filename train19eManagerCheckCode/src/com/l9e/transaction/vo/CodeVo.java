package com.l9e.transaction.vo;
/**
 * 验证码vo
 * */
public class CodeVo {
	private String pic_id;
	private String pic_filename;
	private String verify_code;
	private String create_time;
	private String effect_time;
	private String start_time;
	private String finish_time;
	private String opt_ren;
	private String status;
	private String channel;
	private String user_pic_status;
	private String result_status;
	private String shyzmid;//码商 商户验证码id
	private String back_status; //反馈错误 00默认态 11、成功 22、再次反馈 33 反馈失败
	private String update_status;//上传状态 11、成功 22、未上传    
	private String order_id;

	public String getBack_status() {
		return back_status;
	}
	public void setBack_status(String backStatus) {
		back_status = backStatus;
	}
	public String getUpdate_status() {
		return update_status;
	}
	public void setUpdate_status(String updateStatus) {
		update_status = updateStatus;
	}
	public String getShyzmid() {
		return shyzmid;
	}
	public void setShyzmid(String shyzmid) {
		this.shyzmid = shyzmid;
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
	public String getVerify_code() {
		return verify_code;
	}
	public void setVerify_code(String verifyCode) {
		verify_code = verifyCode;
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
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String startTime) {
		start_time = startTime;
	}
	public String getFinish_time() {
		return finish_time;
	}
	public void setFinish_time(String finishTime) {
		finish_time = finishTime;
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
	public String getUser_pic_status() {
		return user_pic_status;
	}
	public void setUser_pic_status(String userPicStatus) {
		user_pic_status = userPicStatus;
	}
	public String getResult_status() {
		return result_status;
	}
	public void setResult_status(String resultStatus) {
		result_status = resultStatus;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	
}
