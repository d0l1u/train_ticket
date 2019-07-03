package com.l9e.transaction.vo;

public class Count {
	private String countId;
	private String source;
	private String channel;
	private String type;
	private String ip;
	private String code;
	private String message;
	private String createTime;
	
	public static final String TYPE_QUERY = "01";
	public static final String TYPE_CHECK_PASSENGER = "02";
	public static final String SUCCESS_CODE = "01";
	public static final String FAILURE_CODE = "02";
	public static final String OTHER_CODE = "0";

	public Count() {
		super();
	}

	public Count(String countId, String source, String channel, String type,
			String ip, String code, String message, String createTime) {
		super();
		this.countId = countId;
		this.source = source;
		this.channel = channel;
		this.type = type;
		this.ip = ip;
		this.code = code;
		this.message = message;
		this.createTime = createTime;
	}

	public String getCountId() {
		return countId;
	}

	public void setCountId(String countId) {
		this.countId = countId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
