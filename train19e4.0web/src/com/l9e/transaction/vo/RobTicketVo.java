package com.l9e.transaction.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 19e后台-抢票VO
 * 对应数据库表：hc_rob_orderinfo,hc_rob_orderinfo_cp,hc_rob_orderinfo_history
 * ,hc_rob_orderinfo_notify
 * 
 * @author yangwei01
 */
public class RobTicketVo implements Serializable {

	private static final long serialVersionUID = 5971444916932259688L;
	private RobTicket_OI oi;
	private List<RobTicket_CP> cps;
	private RobTicket_History history;
	private RobTicket_Notify notify;

	public RobTicket_OI getOi() {
		return oi;
	}

	public void setOi(RobTicket_OI oi) {
		this.oi = oi;
	}

	public List<RobTicket_CP> getCps() {
		return cps;
	}

	public void setCps(List<RobTicket_CP> cps) {
		this.cps = cps;
	}

	public RobTicket_History getHistory() {
		return history;
	}

	public void setHistory(RobTicket_History history) {
		this.history = history;
	}

	public RobTicket_Notify getNotify() {
		return notify;
	}

	public void setNotify(RobTicket_Notify notify) {
		this.notify = notify;
	}

	public RobTicket_CP getOneFreshCP() {
		return new RobTicket_CP();
	}

	public RobTicket_Notify getOneFreshNotify() {
		return new RobTicket_Notify();
	}

	public RobTicket_OI getOneFreshOI() {
		return new RobTicket_OI();
	}

	public RobTicket_History getOneFreshHistory() {
		return new RobTicket_History();
	}

	public RobTicketVo() {
		super();
		this.oi = new RobTicket_OI();
		this.cps = new ArrayList<RobTicket_CP>();
		this.history = new RobTicket_History();
		this.notify = new RobTicket_Notify();
	}

	/**
	 * ORDER_STATUS
	 * 出票状态 00、开始出票 01、重发出票 10、出票失败 11、正在预定  33、请求抢票成功 44、
	 * 预定人工 55、扣位成功（开始支付） 56、重新支付  61、人工支付  85、开始取消 83、
	 * 正在取消  84、取消重发 86、取消人工 87、取消失败  88、支付成功 99、出票成功 ',
	 * 70 退款中 71 退款成功 72 退款失败
	 */
	
	
    /**未支付*/
	public static final String OI_ORDER_STATUS_EXT_BEGIN = "00";
	/**已支付,出票中*/
	public static final String OI_ORDER_STATUS_PAY_SUCC = "88";
	/**出票失败,取消成功*/
	public static final String OI_ORDER_STATUS_EXT_FAIL = "10";
	/**出票成功*/
	public static final String OI_ORDER_STATUS_EXT_SUCC = "99";
	/**取消中*/
	public static final String OI_ORDER_STATUS_CANCELING = "83";
	/**订单锁定中 不允许取消*/
	public static final String OI_ORDER_STATUS_LOCK = "80";
	/**退票中*/
	public static final String OI_ORDER_STATUS_REFUNDING = "70";
	/**退票成功*/
	public static final String OI_ORDER_STATUS_REFUND_SUCC = "71";
	/**退票失败*/
	public static final String OI_ORDER_STATUS_REFUND_FAIL = "72";
	
	
	
	
	
	
	
	public static final String OI_ORDER_STATUS_EXT_CHONGFA = "01";
	public static final String OI_ORDER_STATUS_REQUEST_ROB_SUCC = "33";
	public static final String OI_ORDER_STATUS_BOOKING = "11";
	public static final String OI_ORDER_STATUS_BOOKING_MAN = "44";
	public static final String OI_ORDER_STATUS_KOUWEI_SUCC = "55";
	public static final String OI_ORDER_STATUS_REPAY = "56";
	public static final String OI_ORDER_STATUS_PAY_MAN = "61";
	public static final String OI_ORDER_STATUS_CANCEL_BEGIN = "85";
	
	public static final String OI_ORDER_STATUS_CANCEL_CHONGFA = "84";
	public static final String OI_ORDER_STATUS_CANCEL_MAN = "86";
	public static final String OI_ORDER_STATUS_CANCEL_FAIL = "87";
	
	

	/**
	 * out_ticket_type
	 */

	public static final int OI_OUT_TICKET_TYPE_ELEC = 11;
	public static final int OI_OUT_TICKET_TYPE_PEISONG = 22;

	/**
	 * seat_type 9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座 F:动卧
	 * A:高级动卧 H:包厢硬卧'
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
	 * wait_for_order '12306系统故障是否继续出票 11：继续出票 00：不继续出票 （目前只针对同程渠道）',
	 */
	public static final String OI_WAIT_FOR_ORDER_GO_ON = "11";
	public static final String OI_WAIT_FOR_ORDER_STOP = "00";

	/**
	 * device_type '0--PC端预订 1-APP端预订',
	 */
	public static final int OI_DEVICE_TYPE_PC = 0;
	public static final int OI_DEVICE_TYPE_APP = 1;

	/**
	 * account_from_way '账号来源： 0：公司自有账号 ； 1：12306自带账号',
	 */
	public static final int OI_ACCOUNT_FROM_WAY_19E = 0;
	public static final int OI_ACCOUNT_FROM_WAY_12306 = 1;

	/**
	 * ticket_type '车票类型 1：成人票 2：儿童票 3:学生票',
	 */
	public static final int CP_TICKET_TYPE_AD = 1;
	public static final int CP_TICKET_TYPE_CHILD = 2;
	public static final int CP_TICKET_TYPE_STU = 3;

	/**
	 * cert_type 证件类型1、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照,
	 */
	public static final String CP_CERT_TYPE_ONE_ID = "1";
	public static final String CP_CERT_TYPE_TWO_ID = "2";
	public static final String CP_CERT_TYPE_HK = "3";
	public static final String CP_CERT_TYPE_TW = "4";
	public static final String CP_CERT_TYPE_PASSPORT = "5";

	/**
	 * check_status '12306身份核验状态 0、已通过 1、审核中 2、未通过',
	 */
	public static final String CP_CHECK_STATUS_PASS = "0";
	public static final String CP_CHECK_STATUS_CHECKING = "1";
	public static final String CP_CHECK_STATUS_NOPASS = "2";

	/**
	 * task_type '任务类型 1：预定任务 2：支付任务 3：取消任务 4：向订单系统发送通知',
	 */
	public static final String NOTIFY_TASK_TYPE_BOOK = "1";
	public static final String NOTIFY_TASK_TYPE_PAY = "2";
	public static final String NOTIFY_TASK_TYPE_CANCEL = "3";
	public static final String NOTIFY_TASK_TYPE_SEND_NOTIFY = "4";

	/**
	 * task_status '任务状态 00：开始任务 11：正在任务 22：任务成功 33：任务失败',
	 */
	public static final String NOTIFY_TASK_STATUS_START = "00";
	public static final String NOTIFY_TASK_STATUS_DOING = "11";
	public static final String NOTIFY_TASK_STATUS_SUCC = "22";
	public static final String NOTIFY_TASK_STATUS_FAIL = "33";
	public static final String REFUND_TYPE = "refund";
	public static final String CANCEL_TYPE = "cancel";

	/**
	 * 帅锋
	 */
	public static String ORDER_ERROR = "1001";
	public static String ORDER_SUCCESS = "0000";

	public static String STATUS_ORDER_START = "00";// 开始出票
	public static String STATUS_ORDER_RESEND = "01";// 重发出票
	public static String STATUS_OUT_TICKET_FAIL = "10";// 出票失败
	public static String STATUS_ORDER_ING = "11";// 正在预定
	public static String STATUS_RESERVE_SEAT_FAILURE = "22";// 扣位失败
	public static String STATUS_VIEFOR_TICKET_SUCCESS = "33";// 请求抢票成功(预定成功)
	public static String STATUS_ORDER_MANUAL = "44";// 预定人工
	public static String STATUS_PAY_START = "55";// 扣位成功（开始支付）
	public static String STATUS_PAY_RESEND = "56";// 重新支付
	public static String STATUS_PAY_MANUAL = "61";// 人工支付
	public static String STATUS_PAY_ING = "66";// 正在支付
	public static String STATUS_PAY_FAILURE = "77";// 支付失败
	public static String STATUS_PAY_SUCCESS = "88";// 支付成功

	public static String STATUS_CANCEL_ING = "83";// 正在取消
	public static String STATUS_CANCEL_START = "85";// 开始取消
	public static String STATUS_OUT_TICKET_SUCCESS = "99";// 出票成功

}





