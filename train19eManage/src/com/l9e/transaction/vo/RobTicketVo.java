package com.l9e.transaction.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * 19e后台-抢票VO 对应数据库表：jl_orderinfo,jl_orderinfo_cp,jl_orderinfo_history,jl_orderinfo_notify
 * @author yangwei01
 */
public class RobTicketVo {
	/**
	 * 抢票订单信息
	 * @author yangwei01
	 */
	public static class RobTicket_OI {

		private String order_id;
		private String ctrip_order_id;
		private String fromTo_zh;
		private BigDecimal pay_money;
		private BigDecimal buy_money;
		private String order_status;
		private Date create_time;
		private Date pay_time;
		private Date out_ticket_time;
		private int out_ticket_type;
		private String opt_ren;
		private String out_ticket_billno;
		private String train_no;
		private String train_no_accept;
		private String from_city;
		private String to_city;
		private Date from_time;
		private Date to_time;
		private Date travel_time;
		private String seat_type;
		private String seat_type_accept;
		private int account_id;
		private int worker_id;
		private String out_ticket_account;
		private String bank_pay_seq;
		private String error_info;
		private Date option_time;
		private String channel;
		private String level;
		private String pay_type;
		private String is_pay;
		private String return_optlog;
		private String pro_bak2;
		private Date pay_limit_time;
		private String manual_order;
		private String wait_for_order;
		private int device_type;
		private String from_3c;
		private String to_3c;
		private int account_from_way;
		private BigDecimal pay_money_ext_sum;
		private BigDecimal buy_money_ext_sum;
		private String final_train_no;
		private String pay_serial_number;
		private String final_seat_type;
		private Date leak_cut_offTime;

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String orderId) {
			order_id = orderId;
		}

		public String getCtrip_order_id() {
			return ctrip_order_id;
		}

		public void setCtrip_order_id(String ctripOrderId) {
			ctrip_order_id = ctripOrderId;
		}

		public String getFromTo_zh() {
			return fromTo_zh;
		}

		public void setFromTo_zh(String fromToZh) {
			fromTo_zh = fromToZh;
		}

		public BigDecimal getPay_money() {
			return pay_money;
		}

		public void setPay_money(BigDecimal payMoney) {
			pay_money = payMoney;
		}

		public BigDecimal getBuy_money() {
			return buy_money;
		}

		public void setBuy_money(BigDecimal buyMoney) {
			buy_money = buyMoney;
		}

		public String getOrder_status() {
			return order_status;
		}

		public void setOrder_status(String orderStatus) {
			order_status = orderStatus;
		}

		public Date getCreate_time() {
			return create_time;
		}

		public void setCreate_time(Date createTime) {
			create_time = createTime;
		}

		public Date getPay_time() {
			return pay_time;
		}

		public void setPay_time(Date payTime) {
			pay_time = payTime;
		}

		public Date getOut_ticket_time() {
			return out_ticket_time;
		}

		public void setOut_ticket_time(Date outTicketTime) {
			out_ticket_time = outTicketTime;
		}

		public int getOut_ticket_type() {
			return out_ticket_type;
		}

		public void setOut_ticket_type(int outTicketType) {
			out_ticket_type = outTicketType;
		}

		public String getOpt_ren() {
			return opt_ren;
		}

		public void setOpt_ren(String optRen) {
			opt_ren = optRen;
		}

		public String getOut_ticket_billno() {
			return out_ticket_billno;
		}

		public void setOut_ticket_billno(String outTicketBillno) {
			out_ticket_billno = outTicketBillno;
		}

		public String getTrain_no() {
			return train_no;
		}

		public void setTrain_no(String trainNo) {
			train_no = trainNo;
		}

		public String getTrain_no_accept() {
			return train_no_accept;
		}

		public void setTrain_no_accept(String trainNoAccept) {
			train_no_accept = trainNoAccept;
		}

		public String getFrom_city() {
			return from_city;
		}

		public void setFrom_city(String fromCity) {
			from_city = fromCity;
		}

		public String getTo_city() {
			return to_city;
		}

		public void setTo_city(String toCity) {
			to_city = toCity;
		}

		public Date getFrom_time() {
			return from_time;
		}

		public void setFrom_time(Date fromTime) {
			from_time = fromTime;
		}

		public Date getTo_time() {
			return to_time;
		}

		public void setTo_time(Date toTime) {
			to_time = toTime;
		}

		public Date getTravel_time() {
			return travel_time;
		}

		public void setTravel_time(Date travelTime) {
			travel_time = travelTime;
		}

		public String getSeat_type() {
			return seat_type;
		}

		public void setSeat_type(String seatType) {
			seat_type = seatType;
		}

		public String getSeat_type_accept() {
			return seat_type_accept;
		}

		public void setSeat_type_accept(String seatTypeAccept) {
			seat_type_accept = seatTypeAccept;
		}

		public int getAccount_id() {
			return account_id;
		}

		public void setAccount_id(int accountId) {
			account_id = accountId;
		}

		public int getWorker_id() {
			return worker_id;
		}

		public void setWorker_id(int workerId) {
			worker_id = workerId;
		}

		public String getOut_ticket_account() {
			return out_ticket_account;
		}

		public void setOut_ticket_account(String outTicketAccount) {
			out_ticket_account = outTicketAccount;
		}

		public String getBank_pay_seq() {
			return bank_pay_seq;
		}

		public void setBank_pay_seq(String bankPaySeq) {
			bank_pay_seq = bankPaySeq;
		}

		public String getError_info() {
			return error_info;
		}

		public void setError_info(String errorInfo) {
			error_info = errorInfo;
		}

		public Date getOption_time() {
			return option_time;
		}

		public void setOption_time(Date optionTime) {
			option_time = optionTime;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public String getPay_type() {
			return pay_type;
		}

		public void setPay_type(String payType) {
			pay_type = payType;
		}

		public String getIs_pay() {
			return is_pay;
		}

		public void setIs_pay(String isPay) {
			is_pay = isPay;
		}

		public String getReturn_optlog() {
			return return_optlog;
		}

		public void setReturn_optlog(String returnOptlog) {
			return_optlog = returnOptlog;
		}

		public String getPro_bak2() {
			return pro_bak2;
		}

		public void setPro_bak2(String proBak2) {
			pro_bak2 = proBak2;
		}

		public Date getPay_limit_time() {
			return pay_limit_time;
		}

		public void setPay_limit_time(Date payLimitTime) {
			pay_limit_time = payLimitTime;
		}

		public String getManual_order() {
			return manual_order;
		}

		public void setManual_order(String manualOrder) {
			manual_order = manualOrder;
		}

		public String getWait_for_order() {
			return wait_for_order;
		}

		public void setWait_for_order(String waitForOrder) {
			wait_for_order = waitForOrder;
		}

		public int getDevice_type() {
			return device_type;
		}

		public void setDevice_type(int deviceType) {
			device_type = deviceType;
		}

		public String getFrom_3c() {
			return from_3c;
		}

		public void setFrom_3c(String from_3c) {
			this.from_3c = from_3c;
		}

		public String getTo_3c() {
			return to_3c;
		}

		public void setTo_3c(String to_3c) {
			this.to_3c = to_3c;
		}

		public int getAccount_from_way() {
			return account_from_way;
		}

		public void setAccount_from_way(int accountFromWay) {
			account_from_way = accountFromWay;
		}

		public BigDecimal getPay_money_ext_sum() {
			return pay_money_ext_sum;
		}

		public void setPay_money_ext_sum(BigDecimal payMoneyExtSum) {
			pay_money_ext_sum = payMoneyExtSum;
		}

		public BigDecimal getBuy_money_ext_sum() {
			return buy_money_ext_sum;
		}

		public void setBuy_money_ext_sum(BigDecimal buyMoneyExtSum) {
			buy_money_ext_sum = buyMoneyExtSum;
		}

		public String getFinal_train_no() {
			return final_train_no;
		}

		public void setFinal_train_no(String finalTrainNo) {
			final_train_no = finalTrainNo;
		}

		public String getPay_serial_number() {
			return pay_serial_number;
		}

		public void setPay_serial_number(String paySerialNumber) {
			pay_serial_number = paySerialNumber;
		}

		public String getFinal_seat_type() {
			return final_seat_type;
		}

		public void setFinal_seat_type(String finalSeatType) {
			final_seat_type = finalSeatType;
		}

		public Date getLeak_cut_offTime() {
			return leak_cut_offTime;
		}

		public void setLeak_cut_offTime(Date leakCutOffTime) {
			leak_cut_offTime = leakCutOffTime;
		}
	}

	/**
	 * 单个车票信息
	 * @author yangwei01
	 */
	public static class RobTicket_CP {
		private String cp_id;
		private String order_id;
		private String user_name;
		private int ticket_type;
		private String cert_type;
		private String cert_no;
		private String telephone;
		private String create_time;
		private String pay_money;
		private String buy_money;
		private Date modify_time;
		private int seat_type;
		private String train_no;
		private String train_box;
		private String seat_no;
		private String check_status;
		private String pay_money_ext;
		private String buy_money_ext;
		public static final String REFUNDING = "开始退票";
		public static final String REFUND_SUCC = "退票成功";
		public static final String REFUND_FAIL = "退票失败";
		public static final String REFUND_REQ = "正在退票";

		public String getCp_id() {
			return cp_id;
		}

		public void setCp_id(String cpId) {
			cp_id = cpId;
		}

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String orderId) {
			order_id = orderId;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String userName) {
			user_name = userName;
		}

		public int getTicket_type() {
			return ticket_type;
		}

		public void setTicket_type(int ticketType) {
			ticket_type = ticketType;
		}

		public String getCert_type() {
			return cert_type;
		}

		public void setCert_type(String certType) {
			cert_type = certType;
		}

		public String getCert_no() {
			return cert_no;
		}

		public void setCert_no(String certNo) {
			cert_no = certNo;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String createTime) {
			create_time = createTime;
		}

		public String getPay_money() {
			return pay_money;
		}

		public void setPay_money(String payMoney) {
			pay_money = payMoney;
		}

		public String getBuy_money() {
			return buy_money;
		}

		public void setBuy_money(String buyMoney) {
			buy_money = buyMoney;
		}

		public Date getModify_time() {
			return modify_time;
		}

		public void setModify_time(Date modifyTime) {
			modify_time = modifyTime;
		}

		public int getSeat_type() {
			return seat_type;
		}

		public void setSeat_type(int seatType) {
			seat_type = seatType;
		}

		public String getTrain_no() {
			return train_no;
		}

		public void setTrain_no(String trainNo) {
			train_no = trainNo;
		}

		public String getTrain_box() {
			return train_box;
		}

		public void setTrain_box(String trainBox) {
			train_box = trainBox;
		}

		public String getSeat_no() {
			return seat_no;
		}

		public void setSeat_no(String seatNo) {
			seat_no = seatNo;
		}

		public String getCheck_status() {
			return check_status;
		}

		public void setCheck_status(String checkStatus) {
			check_status = checkStatus;
		}

		public String getPay_money_ext() {
			return pay_money_ext;
		}

		public void setPay_money_ext(String payMoneyExt) {
			pay_money_ext = payMoneyExt;
		}

		public String getBuy_money_ext() {
			return buy_money_ext;
		}

		public void setBuy_money_ext(String buyMoneyExt) {
			buy_money_ext = buyMoneyExt;
		}

	}

	/**
	 * 抢单历史记录
	 * @author yangwei01
	 */
	public static class RobTicket_History {
		private int history_id;
		private String order_id;
		private String order_optlog;
		private Date create_time;
		private String opter;

		public int getHistory_id() {
			return history_id;
		}

		public void setHistory_id(int historyId) {
			history_id = historyId;
		}

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String orderId) {
			order_id = orderId;
		}

		public String getOrder_optlog() {
			return order_optlog;
		}

		public void setOrder_optlog(String orderOptlog) {
			order_optlog = orderOptlog;
		}

		public Date getCreate_time() {
			return create_time;
		}

		public void setCreate_time(Date createTime) {
			create_time = createTime;
		}

		public String getOpter() {
			return opter;
		}

		public void setOpter(String opter) {
			this.opter = opter;
		}

	}

	/**
	 * 抢单通知
	 * @author yangwei01
	 */
	public static class RobTicket_Notify {
		private int task_id;
		private String order_id;
		private String task_type;
		private int task_num;
		private String task_status;
		private Date create_time;
		private Date option_time;

		public int getTask_id() {
			return task_id;
		}

		public void setTask_id(int taskId) {
			task_id = taskId;
		}

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String orderId) {
			order_id = orderId;
		}

		public String getTask_type() {
			return task_type;
		}

		public void setTask_type(String taskType) {
			task_type = taskType;
		}

		public int getTask_num() {
			return task_num;
		}

		public void setTask_num(int taskNum) {
			task_num = taskNum;
		}

		public String getTask_status() {
			return task_status;
		}

		public void setTask_status(String taskStatus) {
			task_status = taskStatus;
		}

		public Date getCreate_time() {
			return create_time;
		}

		public void setCreate_time(Date createTime) {
			create_time = createTime;
		}

		public Date getOption_time() {
			return option_time;
		}

		public void setOption_time(Date optionTime) {
			option_time = optionTime;
		}

	}
	
	
	/**
	 * ORDER_STATUS
	 */
	public static final String OI_ORDER_STATUS_EXT_BEGIN = "00";
	public static final String OI_ORDER_STATUS_EXT_CHONGFA = "01";
	public static final String OI_ORDER_STATUS_EXT_FAIL = "10";
	public static final String OI_ORDER_STATUS_EXT_SUCC = "99";
	public static final String OI_ORDER_STATUS_REQUEST_ROB_SUCC = "33";
	public static final String OI_ORDER_STATUS_BOOKING = "11";
	public static final String OI_ORDER_STATUS_BOOKING_MAN = "44";
	public static final String OI_ORDER_STATUS_KOUWEI_SUCC = "55";
	public static final String OI_ORDER_STATUS_REPAY = "56";
	public static final String OI_ORDER_STATUS_PAY_MAN = "61";
	public static final String OI_ORDER_STATUS_CANCEL_BEGIN = "85";
	public static final String OI_ORDER_STATUS_CANCELING = "83";
	public static final String OI_ORDER_STATUS_CANCEL_CHONGFA = "84";
	public static final String OI_ORDER_STATUS_CANCEL_MAN = "86";
	public static final String OI_ORDER_STATUS_CANCEL_FAIL = "87";
	public static final String OI_ORDER_STATUS_PAY_SUCC = "88";
	/**退款成功*/
	public static String STATUS_REFUND_SUCCESS = "71";
	/**退款人工*/
	public static String STATUS_REFUND_MENUAL = "72";
	
	/**
	 * out_ticket_type
	 */
	
	public static final int OI_OUT_TICKET_TYPE_ELEC = 11;
	public static final int OI_OUT_TICKET_TYPE_PEISONG = 22;
	
	
	/**
	 * seat_type
	 * 9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座    F:动卧  A:高级动卧  H:包厢硬卧'
	 */
	public static final String OI_SEAT_TYPE_SWZ = "9";
	public static final String OI_SEAT_TYPE_TDZ = "P";
	public static final String OI_SEAT_TYPE_YDZ = "M";
	public static final String OI_SEAT_TYPE_EDZ = "O";
	public static final String OI_SEAT_TYPE_GJRW = "6";
	public static final String OI_SEAT_TYPE_RW = "4";
	public static final String OI_SEAT_TYPE_YW = "3";
	public static final String OI_SEAT_TYPE_RZ = "2";
	public static final String OI_SEAT_TYPE_YZ = "1";
	public static final String OI_SEAT_TYPE_WZ = "0";
	public static final String OI_SEAT_TYPE_DW = "F";
	public static final String OI_SEAT_TYPE_GJDW = "A";
	public static final String OI_SEAT_TYPE_BXYW = "H";
	
	/**
	 * error_info
	 */
	public static final String OI_ERROR_INFO_ = "H";
	
	/**
	 * PAY_TYPE '支付方式：默认0机器支付，1是手动支付',
	 */
	public static final String OI_PAY_TYPE_ROBOT = "0";
	public static final String OI_PAY_TYPE_HAND = "0";
	
	/**
	 * is_pay '是否支付：00：已支付；11：未支付',
	 */
	public static final String OI_IS_PAY_YES = "00";
	public static final String OI_IS_PAY_NO = "11";
	
	/**
	 * pro_bak2 '00：正常 11：补单',
	 */
	public static final String OI_PRO_BAK2_GOOD = "00";
	public static final String OI_PRO_BAK2_BUDAN = "11";
	
	
	/**
	 * manual_order '出票方式 11：人工出票 00：系统出票',
	 */
	public static final String OI_MANUAL_ORDER_SYS = "00";
	public static final String OI_MANUAL_ORDER_MAN = "11";
	
	/**
	 * wait_for_order  '12306系统故障是否继续出票 11：继续出票 00：不继续出票 （目前只针对同程渠道）',
	 */
	public static final String OI_WAIT_FOR_ORDER_GO_ON = "11";
	public static final String OI_WAIT_FOR_ORDER_STOP = "00";
	
	/**
	 * device_type '0--PC端预订  1-APP端预订',
	 */
	public static final int OI_DEVICE_TYPE_PC = 0;
	public static final int OI_DEVICE_TYPE_APP = 1;
	
	/**
	 * account_from_way  '账号来源： 0：公司自有账号 ； 1：12306自带账号',
	 */
	public static final int OI_ACCOUNT_FROM_WAY_19E = 0;
	public static final int OI_ACCOUNT_FROM_WAY_12306 = 1;
	
	/**
	 * ticket_type '车票类型   1：成人票   2：儿童票   3:学生票',
	 */
	public static final int CP_TICKET_TYPE_AD = 1;
	public static final int CP_TICKET_TYPE_CHILD = 2;
	public static final int CP_TICKET_TYPE_STU = 3;
	
	/**
	 * cert_type '证件类型 2、一代身份证、1、二代身份证、C、港澳通行证、G、台湾通行证、B、护照 ',
	 */
	public static final String CP_CERT_TYPE_ONE_ID = "2";
	public static final String CP_CERT_TYPE_TWO_ID = "1";
	public static final String CP_CERT_TYPE_HK = "C";
	public static final String CP_CERT_TYPE_TW = "G";
	public static final String CP_CERT_TYPE_PASSPORT = "B";
	
	/**
	 * check_status  '12306身份核验状态 0、已通过 1、审核中 2、未通过',
	 */
	public static final String CP_CHECK_STATUS_PASS = "0";
	public static final String CP_CHECK_STATUS_CHECKING = "1";
	public static final String CP_CHECK_STATUS_NOPASS = "2";
	
	
	/**
	 * task_type '任务类型  1：预定任务  2：支付任务  3：取消任务 4：向订单系统发送通知',
	 */
	public static final String NOTIFY_TASK_TYPE_BOOK = "1";
	public static final String NOTIFY_TASK_TYPE_PAY = "2";
	public static final String NOTIFY_TASK_TYPE_CANCEL = "3";
	public static final String NOTIFY_TASK_TYPE_SEND_NOTIFY = "4";
	
	/**
	 * task_status '任务状态 00：开始任务 11：正在任务  22：任务成功  33：任务失败',
	 */
	public static final String NOTIFY_TASK_STATUS_START= "00";
	public static final String NOTIFY_TASK_STATUS_DOING = "11";
	public static final String NOTIFY_TASK_STATUS_SUCC = "22";
	public static final String NOTIFY_TASK_STATUS_FAIL = "33";
	
	
}
