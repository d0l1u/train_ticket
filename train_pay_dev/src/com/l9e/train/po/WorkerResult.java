package com.l9e.train.po;

/**
 * 发送短信返回结果
 * 
 * @author licheng
 * 
 */
public class WorkerResult {

	/**
	 * 操作是否成功
	 */
	private Boolean success = true;;
	/**
	 * 操作信息
	 */
	private String msg = "操作成功";
	/**
	 * 返回数据
	 */
	private WorkerVo data;

	public WorkerResult() {
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

	public WorkerVo getData() {
		return data;
	}

	public void setData(WorkerVo data) {
		this.data = data;
	}

}
