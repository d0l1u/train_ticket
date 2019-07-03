package com.l9e.train.po;

public class WorkerResult {
	private Boolean success = Boolean.valueOf(true);
	private String msg = "操作成功";
	private WorkerVo data;

	public Boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public WorkerVo getData() {
		return this.data;
	}

	public void setData(WorkerVo data) {
		this.data = data;
	}
}
