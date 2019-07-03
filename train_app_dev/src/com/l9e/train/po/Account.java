package com.l9e.train.po;

/**
 * 账号服务账号实体
 * 
 * @author licheng
 * 
 */
public class Account {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4953101739338894863L;
	/**
	 * 账号状态：正在下单
	 */
	public static final String STATUS_PLACING_ORDER = "00";
	/**
	 * 账号状态：队列中
	 */
	public static final String STATUS_QUEUE = "01";
	/**
	 * 账号状态：账号停用
	 */
	public static final String STATUS_STOP = "22";
	/**
	 * 账号状态：空闲
	 */
	public static final String STATUS_FREE = "33";
	/**
	 * 账号状态：临时停用
	 */
	public static final String STATUS_TEMP_STOP = "44";

	/**
	 * 账号id
	 */
	private Integer id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 渠道
	 */
	private String channel;
	/**
	 * 停用原因
	 */
	private String stopReason;
	/**
	 * 联系人数量
	 */
	private Integer contact;
	/**
	 * 预订次数
	 */
	private Integer bookNum;
	/**
	 * 停用时间(标记字符串)
	 */
	private String stopTime;
	/**
	 * 占座成功订单id
	 */
	private String orderId;

	public Account() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getStopReason() {
		return stopReason;
	}

	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}

	public Integer getContact() {
		return contact;
	}

	public void setContact(Integer contact) {
		this.contact = contact;
	}

	public Integer getBookNum() {
		return bookNum;
	}

	public void setBookNum(Integer bookNum) {
		this.bookNum = bookNum;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "Account [bookNum=" + bookNum + ", channel=" + channel
				+ ", contact=" + contact + ", id=" + id + ", password="
				+ password + ", status=" + status + ", stopReason="
				+ stopReason + ", username=" + username + "]";
	}

}
