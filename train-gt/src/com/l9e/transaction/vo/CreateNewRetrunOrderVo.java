package com.l9e.transaction.vo;

/**
 * @author Administrator 先下单后付款 ,返回值
 */
public class CreateNewRetrunOrderVo {

	private String supplierOrderId; // 供应商订单号
	private String order12306; // 12306订单号
	private String msg; // 失败原因
	private String success; // 请求结果
	private String gtgjOrderId; // 高铁管家订单号
	private String reqtoken; // 请求token
	private String return_code;

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getSupplierOrderId() {
		return supplierOrderId;
	}

	public void setSupplierOrderId(String supplierOrderId) {
		this.supplierOrderId = supplierOrderId;
	}

	public String getOrder12306() {
		return order12306;
	}

	public void setOrder12306(String order12306) {
		this.order12306 = order12306;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getGtgjOrderId() {
		return gtgjOrderId;
	}

	public void setGtgjOrderId(String gtgjOrderId) {
		this.gtgjOrderId = gtgjOrderId;
	}

	public String getReqtoken() {
		return reqtoken;
	}

	public void setReqtoken(String reqtoken) {
		this.reqtoken = reqtoken;
	}

}
