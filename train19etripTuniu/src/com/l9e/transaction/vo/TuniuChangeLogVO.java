package com.l9e.transaction.vo;

/**
 * 同程改签日志
 * 
 * @author licheng
 * 
 */
public class TuniuChangeLogVO {

	/**
	 * 主键
	 */
	private Integer log_id;
	/**
	 * 订单号
	 */
	private String order_id;
	/**
	 * 改签id
	 */
	private Integer change_id;
	/**
	 * 日志内容
	 */
	private String content;
	/**
	 * 操作人
	 */
	private String opt_person;

	public TuniuChangeLogVO() {
		super();
	}

	public TuniuChangeLogVO(Integer logId, String orderId,
			Integer changeId, String content, String optPerson) {
		super();
		log_id = logId;
		order_id = orderId;
		change_id = changeId;
		this.content = content;
		opt_person = optPerson;
	}

	public Integer getLog_id() {
		return log_id;
	}

	public void setLog_id(Integer logId) {
		log_id = logId;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String orderId) {
		order_id = orderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOpt_person() {
		return opt_person;
	}

	public void setOpt_person(String optPerson) {
		opt_person = optPerson;
	}

	public Integer getChange_id() {
		return change_id;
	}

	public void setChange_id(Integer changeId) {
		change_id = changeId;
	}

}
