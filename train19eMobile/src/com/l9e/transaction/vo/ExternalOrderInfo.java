package com.l9e.transaction.vo;

import java.util.List;

public class ExternalOrderInfo {
	private String order_id;
	
	private String merchant_order_id;	//合作商户订单号
	
	private String return_code;
	
	private String message;

	private List<ExternalTicketInfo> ticket_list;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String orderId) {
		order_id = orderId;
	}

	public String getMerchant_order_id() {
		return merchant_order_id;
	}

	public void setMerchant_order_id(String merchantOrderId) {
		merchant_order_id = merchantOrderId;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String returnCode) {
		return_code = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ExternalTicketInfo> getTicket_list() {
		return ticket_list;
	}

	public void setTicket_list(List<ExternalTicketInfo> ticketList) {
		ticket_list = ticketList;
	}
	
}
