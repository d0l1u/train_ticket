package com.l9e.transaction.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.l9e.util.DateUtil;

public class RobTicketFormVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String order_id;// 订单号
	private String train_no;// 车次
	private String from_city;// 出发城市
	private String to_city;// 到达城市
	private String from_time;// 出发时间
	private String to_time;// 到达时间
	private String travelTime;// 乘车日期
	private String link_name;// 联系人姓名
	private String link_phone;// 联系人手机
	private String link_mail;// 联系人邮箱
	private String link_address;// 联系人地址
	private String danjia;// 车票单价
	private String wz_ext;// 无座额外
	private String ticket_pay_money;// 票价总额
	private String bx_pay_money;// 保险总额
	private String ps_pay_money;// 配送总额
	private String server_pay_money;// SVIP服务费总额
	private String seat_type;// 坐席
	private String pay_money;// 支付金额
	private String out_ticket_type;// 出票方式
	private String defaultSeat;// 默认选座类型 价格
	private String selectSeats;// 用户勾选的 多个 备选坐席
	private String yun_qiangpiao;// 用户选择的 抢票 时间
	private String svip_price;// 系统算出的本地订单SVIP服务费
	private BigDecimal max12306Price;
	private BigDecimal totalPay;
	private List<BookDetailInfo> bookDetailInfoList;
	private BigDecimal total12306Price;
	private String leakcutStr;

	public String getLeakcutStr() {
		return leakcutStr;
	}

	public void setLeakcutStr(String leakcutStr) {
		this.leakcutStr = leakcutStr;
	}

	public BigDecimal getSingleSVIP_Price() {
		BigDecimal decimal = new BigDecimal(svip_price);
		BigDecimal divide = decimal.divide(new BigDecimal(bookDetailInfoList
				.size()), BigDecimal.ROUND_HALF_UP);
		return divide;
	}

	public BigDecimal getTotal12306Price() {
		return getTotalPay().subtract(new BigDecimal(svip_price));
	}

	public void setTotal12306Price(BigDecimal total12306Price) {
		this.total12306Price = total12306Price;
	}

	public BigDecimal getMax12306Price() {
		return max12306Price;
	}

	public void setMax12306Price(BigDecimal max12306Price) {
		this.max12306Price = max12306Price;
	}

	public BigDecimal getTotalPay() {
		BigDecimal value = new BigDecimal(0.00);
		for (BookDetailInfo info : bookDetailInfoList) {
			value = value.add(max12306Price);
		}
		BigDecimal value2 = new BigDecimal(svip_price);
		value = value.add(value2);
		return value;
	}

	public void setTotalPay(BigDecimal totalPay) {
		this.totalPay = totalPay;
	}

	public String getDefaultSeat() {
		return defaultSeat;
	}

	public void setDefaultSeat(String defaultSeat) {
		this.defaultSeat = defaultSeat;
	}

	public String getSelectSeats() {
		return selectSeats;
	}

	public void setSelectSeats(String selectSeats) {
		this.selectSeats = selectSeats;
	}

	public String getYun_qiangpiao() {
		return yun_qiangpiao;
	}

	public void setYun_qiangpiao(String yunQiangpiao) {
		yun_qiangpiao = yunQiangpiao;
	}

	public String getSvip_price() {
		return svip_price;
	}

	public void setSvip_price(String svipPrice) {
		svip_price = svipPrice;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getTrain_no() {
		return train_no;
	}

	public void setTrain_no(String train_no) {
		this.train_no = train_no;
	}

	public String getFrom_city() {
		return from_city;
	}

	public void setFrom_city(String from_city) {
		this.from_city = from_city;
	}

	public String getTo_city() {
		return to_city;
	}

	public void setTo_city(String to_city) {
		this.to_city = to_city;
	}

	public String getFrom_time() {
		return from_time;
	}

	public void setFrom_time(String from_time) {
		this.from_time = from_time;
	}

	public String getTo_time() {
		return to_time;
	}

	public void setTo_time(String to_time) {
		this.to_time = to_time;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getLink_name() {
		return link_name;
	}

	public void setLink_name(String link_name) {
		this.link_name = link_name;
	}

	public String getLink_phone() {
		return link_phone;
	}

	public void setLink_phone(String link_phone) {
		this.link_phone = link_phone;
	}

	public String getLink_mail() {
		return link_mail;
	}

	public void setLink_mail(String link_mail) {
		this.link_mail = link_mail;
	}

	public String getLink_address() {
		return link_address;
	}

	public void setLink_address(String link_address) {
		this.link_address = link_address;
	}

	public String getDanjia() {
		return danjia;
	}

	public void setDanjia(String danjia) {
		this.danjia = danjia;
	}

	public String getTicket_pay_money() {
		return ticket_pay_money;
	}

	public void setTicket_pay_money(String ticket_pay_money) {
		this.ticket_pay_money = ticket_pay_money;
	}

	public String getBx_pay_money() {
		return bx_pay_money;
	}

	public void setBx_pay_money(String bx_pay_money) {
		this.bx_pay_money = bx_pay_money;
	}

	public String getPs_pay_money() {
		return ps_pay_money;
	}

	public void setPs_pay_money(String ps_pay_money) {
		this.ps_pay_money = ps_pay_money;
	}

	public String getSeat_type() {
		return seat_type;
	}

	public void setSeat_type(String seat_type) {
		this.seat_type = seat_type;
	}

	public String getPay_money() {
		return pay_money;
	}

	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}

	public String getOut_ticket_type() {
		return out_ticket_type;
	}

	public void setOut_ticket_type(String out_ticket_type) {
		this.out_ticket_type = out_ticket_type;
	}

	public List<BookDetailInfo> getBookDetailInfoList() {
		return bookDetailInfoList;
	}

	public void setBookDetailInfoList(List<BookDetailInfo> bookDetailInfoList) {
		this.bookDetailInfoList = bookDetailInfoList;
	}

	public String getWz_ext() {
		return wz_ext;
	}

	public void setWz_ext(String wz_ext) {
		this.wz_ext = wz_ext;
	}

	public String getServer_pay_money() {
		return server_pay_money;
	}

	public void setServer_pay_money(String serverPayMoney) {
		server_pay_money = serverPayMoney;
	}

	public static void main(String[] args) {
		String str = ":2016-12-07 23:00:00";
		str = str.trim();
		if (str.startsWith(":")) {
			str = str.replaceFirst(":", "");
		}
		System.out.println(str);
	}
	
	public String leakCutStrFormat(){
		String str = "";
		if(yun_qiangpiao.equals("-1")){
			Date fromTime = DateUtil.stringToDate(this.getFrom_time()+":00", "yyyy-MM-dd HH:mm:ss");
			Date leakDate = new Date(fromTime.getTime()-60*1000*60);
			str = DateUtil.dateToString(leakDate, DateUtil.DATE_FMT3);
		}else {
			str = leakcutStr.trim();
			if (str.startsWith(":")) {
				str = str.replaceFirst(":", "");
			}
		}
		return str;
	}
}
