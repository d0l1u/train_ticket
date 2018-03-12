package com.l9e.transaction.vo;

/**
 * 改签车票信息实体类
 * 
 * @author licheng
 * 
 */
public class TuniuChangePassengerInfo {

	/**
	 * 旧车票id
	 */
	private String cp_id;
	/**
	 * 新车票id
	 */
	private String new_cp_id;
	/**
	 * 订单id
	 */
	private String order_id;
	/**
	 * 改签ID
	 */
	private Integer change_id;
	/**
	 * 成本价格
	 */
	private Double buy_money;
	/**
	 * 改签后成本价格
	 */
	private Double change_buy_money;
	/**
	 * 坐席类型
	 */
	private String seat_type;
	/**
	 * 改签后坐席类型
	 */
	private String change_seat_type;
	/**
	 * 车票类型：0、成人票, 1、儿童票
	 */
	private String ticket_type;
	/**
	 * 车厢
	 */
	private String train_box;
	/**
	 * 改签后车厢
	 */
	private String change_train_box;
	/**
	 * 座位号
	 */
	private String seat_no;
	/**
	 * 改签后座位号
	 */
	private String change_seat_no;
	/**
	 * 证件类型:1、一代身份证 2、二代身份证 3、港澳通行证 4、台湾通行证 5、护照
	 */
	private String ids_type;
	/**
	 * 证件号码
	 */
	private String user_ids;
	/**
	 * 乘客姓名
	 */
	private String user_name;
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 是否改签过
	 */
	private String is_changed;
	/**
	 * 途牛坐席类别
	 */
	private String tn_seat_type;
	/**
	 * 途牛票类别
	 */
	private String tn_ticket_type;
	/**
	 * 途牛改签后坐席类别
	 */
	private String tn_change_seat_type;
	/**
	 * 途牛证件类别
	 */
	private String tn_ids_type;

	public String getCp_id() {
		return cp_id;
	}

	public void setCp_id(String cpId) {
		cp_id = cpId;
	}

	public String getNew_cp_id() {
		return new_cp_id;
	}

	public void setNew_cp_id(String newCpId) {
		new_cp_id = newCpId;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String orderId) {
		order_id = orderId;
	}

	public Integer getChange_id() {
		return change_id;
	}

	public void setChange_id(Integer changeId) {
		change_id = changeId;
	}

	public Double getBuy_money() {
		return buy_money;
	}

	public void setBuy_money(Double buyMoney) {
		buy_money = buyMoney;
	}


	public Double getChange_buy_money() {
		return change_buy_money;
	}

	public void setChange_buy_money(Double changeBuyMoney) {
		change_buy_money = changeBuyMoney;
	}

	public String getSeat_type() {
		return seat_type;
	}

	public void setSeat_type(String seatType) {
		seat_type = seatType;
	}

	public String getChange_seat_type() {
		return change_seat_type;
	}

	public void setChange_seat_type(String changeSeatType) {
		change_seat_type = changeSeatType;
	}

	public String getTicket_type() {
		return ticket_type;
	}

	public void setTicket_type(String ticketType) {
		ticket_type = ticketType;
	}

	public String getTrain_box() {
		return train_box;
	}

	public void setTrain_box(String trainBox) {
		train_box = trainBox;
	}

	public String getChange_train_box() {
		return change_train_box;
	}

	public void setChange_train_box(String changeTrainBox) {
		change_train_box = changeTrainBox;
	}

	public String getSeat_no() {
		return seat_no;
	}

	public void setSeat_no(String seatNo) {
		seat_no = seatNo;
	}

	public String getChange_seat_no() {
		return change_seat_no;
	}

	public void setChange_seat_no(String changeSeatNo) {
		change_seat_no = changeSeatNo;
	}

	public String getIds_type() {
		return ids_type;
	}

	public void setIds_type(String idsType) {
		ids_type = idsType;
	}

	public String getUser_ids() {
		return user_ids;
	}

	public void setUser_ids(String userIds) {
		user_ids = userIds;
	}

	public String getUser_name() {
		return user_name;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String createTime) {
		create_time = createTime;
	}

	public String getIs_changed() {
		return is_changed;
	}

	public void setIs_changed(String isChanged) {
		is_changed = isChanged;
	}

	public void setUser_name(String userName) {
		user_name = userName;
	}

	public String getTn_seat_type() {
		return tn_seat_type;
	}

	public void setTn_seat_type(String tnSeatType) {
		tn_seat_type = tnSeatType;
	}

	public String getTn_ticket_type() {
		return tn_ticket_type;
	}

	public void setTn_ticket_type(String tnTicketType) {
		tn_ticket_type = tnTicketType;
	}

	public String getTn_change_seat_type() {
		return tn_change_seat_type;
	}

	public void setTn_change_seat_type(String tnChangeSeatType) {
		tn_change_seat_type = tnChangeSeatType;
	}

	public String getTn_ids_type() {
		return tn_ids_type;
	}

	public void setTn_ids_type(String tnIdsType) {
		tn_ids_type = tnIdsType;
	}

	

}