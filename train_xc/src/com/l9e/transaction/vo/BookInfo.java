package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.List;
/**
 * 页面参数
 * @author zhangjun
 *
 */
public class BookInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String user_phone;			//用户电话
	private String merchant_order_id; 	// 合作商户订单id
	private String order_level;			//	订单级别
	private String order_result_url; 	//订单处理结果通知合作商户url
	private String pay_result_url;		//支付结果异步通知地址
	private String book_result_url;		//预订结果异步通知地址
	private String train_code;			//车次	
	private String from_station;		//出发城市	
	private String arrive_station;		//到达城市	
	private String from_time;			//出发时间	
	private String arrive_time;			//到达时间	
	private String travel_time;			//乘车日期	
	private String link_name;			//联系人姓名	
	private String link_phone;			//联系人手机	
	private String link_mail;			//联系人邮箱	
	private String link_address;		//联系人地址	
	private String ticket_price;		//车票单价
	private String wz_ext;				//无座额外
	private String seat_type;			//坐席	
	private String sum_amount;			//支付总额	
	private String sms_notify;			//是否短信通知

	private String bx_invoice;			//是否需要保险发票
	private String bx_invoice_address;	//保险发票联系地址
	private String bx_invoice_phone;	//保险发票联系电话
	private String bx_invoice_receiver;	//保险发票收件人
	private String bx_invoice_zipcode;			//保险发票邮政编号
	private List<BookDetailInfo> book_detail_list;
	
	private String order_pro1;
	private String order_pro2;
	
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String userPhone) {
		user_phone = userPhone;
	}
	public String getMerchant_order_id() {
		return merchant_order_id;
	}
	public void setMerchant_order_id(String merchantOrderId) {
		merchant_order_id = merchantOrderId;
	}
	public String getOrder_level() {
		return order_level;
	}
	public void setOrder_level(String orderLevel) {
		order_level = orderLevel;
	}
	public String getOrder_result_url() {
		return order_result_url;
	}
	public void setOrder_result_url(String orderResultUrl) {
		order_result_url = orderResultUrl;
	}
	public String getPay_result_url() {
		return pay_result_url;
	}
	public void setPay_result_url(String payResultUrl) {
		pay_result_url = payResultUrl;
	}
	public String getBook_result_url() {
		return book_result_url;
	}
	public void setBook_result_url(String bookResultUrl) {
		book_result_url = bookResultUrl;
	}
	public String getTrain_code() {
		return train_code;
	}
	public void setTrain_code(String trainCode) {
		train_code = trainCode;
	}
	public String getFrom_station() {
		return from_station;
	}
	public void setFrom_station(String fromStation) {
		from_station = fromStation;
	}
	public String getArrive_station() {
		return arrive_station;
	}
	public void setArrive_station(String arriveStation) {
		arrive_station = arriveStation;
	}
	public String getFrom_time() {
		return from_time;
	}
	public void setFrom_time(String fromTime) {
		from_time = fromTime;
	}
	public String getArrive_time() {
		return arrive_time;
	}
	public void setArrive_time(String arriveTime) {
		arrive_time = arriveTime;
	}
	public String getTravel_time() {
		return travel_time;
	}
	public void setTravel_time(String travelTime) {
		travel_time = travelTime;
	}
	public String getLink_name() {
		return link_name;
	}
	public void setLink_name(String linkName) {
		link_name = linkName;
	}
	public String getLink_phone() {
		return link_phone;
	}
	public void setLink_phone(String linkPhone) {
		link_phone = linkPhone;
	}
	public String getLink_mail() {
		return link_mail;
	}
	public void setLink_mail(String linkMail) {
		link_mail = linkMail;
	}
	public String getLink_address() {
		return link_address;
	}
	public void setLink_address(String linkAddress) {
		link_address = linkAddress;
	}
	public String getTicket_price() {
		return ticket_price;
	}
	public void setTicket_price(String ticketPrice) {
		ticket_price = ticketPrice;
	}
	public String getWz_ext() {
		return wz_ext;
	}
	public void setWz_ext(String wzExt) {
		wz_ext = wzExt;
	}
	public String getSeat_type() {
		return seat_type;
	}
	public void setSeat_type(String seatType) {
		seat_type = seatType;
	}
	public String getSum_amount() {
		return sum_amount;
	}
	public void setSum_amount(String sumAmount) {
		sum_amount = sumAmount;
	}
	public String getSms_notify() {
		return sms_notify;
	}
	public void setSms_notify(String smsNotify) {
		sms_notify = smsNotify;
	}
	
	public String getBx_invoice() {
		return bx_invoice;
	}
	public void setBx_invoice(String bxInvoice) {
		bx_invoice = bxInvoice;
	}
	public String getBx_invoice_address() {
		return bx_invoice_address;
	}
	public void setBx_invoice_address(String bxInvoiceAddress) {
		bx_invoice_address = bxInvoiceAddress;
	}
	public String getBx_invoice_phone() {
		return bx_invoice_phone;
	}
	public void setBx_invoice_phone(String bxInvoicePhone) {
		bx_invoice_phone = bxInvoicePhone;
	}
	public String getBx_invoice_receiver() {
		return bx_invoice_receiver;
	}
	public void setBx_invoice_receiver(String bxInvoiceReceiver) {
		bx_invoice_receiver = bxInvoiceReceiver;
	}
	public String getBx_invoice_zipcode() {
		return bx_invoice_zipcode;
	}
	public void setBx_invoice_zipcode(String bxInvoiceZipcode) {
		bx_invoice_zipcode = bxInvoiceZipcode;
	}
	public List<BookDetailInfo> getBook_detail_list() {
		return book_detail_list;
	}
	public void setBook_detail_list(List<BookDetailInfo> bookDetailList) {
		book_detail_list = bookDetailList;
	}
	public String getOrder_pro1() {
		return order_pro1;
	}
	public void setOrder_pro1(String orderPro1) {
		order_pro1 = orderPro1;
	}
	public String getOrder_pro2() {
		return order_pro2;
	}
	public void setOrder_pro2(String orderPro2) {
		order_pro2 = orderPro2;
	}
	
}
