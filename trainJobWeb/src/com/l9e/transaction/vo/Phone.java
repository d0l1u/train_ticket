package com.l9e.transaction.vo;

public class Phone {
	private Integer phone_id;
	
	private String telephone;
	
	private String phone_name;
	
	private String content;
	
	private Integer send_num;
	
	private String phone_channel;
	
	private String source_channel;
	
	private String msg_type;
	
	private String phone_channel_ext;
	
	private String fail_reason;

	public String getPhone_channel() {
		return phone_channel;
	}

	public void setPhone_channel(String phoneChannel) {
		phone_channel = phoneChannel;
	}

	public String getSource_channel() {
		return source_channel;
	}

	public void setSource_channel(String sourceChannel) {
		source_channel = sourceChannel;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msgType) {
		msg_type = msgType;
	}

	public String getPhone_channel_ext() {
		return phone_channel_ext;
	}

	public void setPhone_channel_ext(String phoneChannelExt) {
		phone_channel_ext = phoneChannelExt;
	}

	public Integer getPhone_id() {
		return phone_id;
	}

	public void setPhone_id(Integer phoneId) {
		phone_id = phoneId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPhone_name() {
		return phone_name;
	}

	public void setPhone_name(String phoneName) {
		phone_name = phoneName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSend_num() {
		return send_num;
	}

	public void setSend_num(Integer sendNum) {
		send_num = sendNum;
	}

	public String getFail_reason() {
		return fail_reason;
	}

	public void setFail_reason(String failReason) {
		fail_reason = failReason;
	}
	
}
