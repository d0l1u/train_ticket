package com.l9e.transaction.vo;

/**
 * 发送短信返回结果
 * 
 * @author licheng
 * 
 */
public class Result {

	/**
	 * 操作是否成功
	 */
	private Boolean success = true;
	/**
	 * 操作信息
	 */
	private String msg = "操作成功";
	/**
	 * 返回数据
	 */
	private Object data;

	public Result() {
		super();
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
