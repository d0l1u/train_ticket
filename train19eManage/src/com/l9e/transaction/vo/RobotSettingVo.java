package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RobotSettingVo {
	//机器人控制表
	private String robot_id;//主键
	private String robot_name;//名称
	private String robot_url;//内容(请求url)
	private String robot_status;//1 启用 2停用 3 备用
	private String robot_desc;//说明
	private String robot_channel;//分配渠道：19e：19e平台 ext：对外商户 (qunar：去哪) elong：艺龙 app：手机端 embed：内嵌
	private String robot_type;//query:查询机器人
	private String robot_con_timeout;//连接超时时间
	private String robot_read_timeout;//读取超时时间
	
	private String 	robot_money;//查询票价
	private String	robot_register;//实名认证
	private String	robot_book;//预订
	private String	robot_pay;//支付
	private String	robot_check;//核验订单
	private String	robot_cancel;//取消订单
	private String	robot_endorse;//改签
	private String	robot_refund;//退票
	private String	robot_query;//查询余票
	private String  robot_delete;//删除
	private String  robot_enroll;//注册
	private String  robot_activate;//激活
	
	private String 	robot_money_num;//查询票价
	private String	robot_register_num;//实名认证
	private String	robot_book_num;//预订
	private String	robot_pay_num;//支付
	private String	robot_check_num;//核验订单
	private String	robot_cancel_num;//取消订单
	private String	robot_endorse_num;//改签
	private String	robot_refund_num;//退票
	private String	robot_query_num;//查询余票
	private String	robot_delete_num;//删除
	private String  robot_enroll_num;//注册
	private String  robot_activate_num;//激活
	
	private String 	robot_money_stopReason;//查询票价
	private String	robot_register_stopReason;//实名认证
	private String	robot_book_stopReason;//预订
	private String	robot_pay_stopReason;//支付
	private String	robot_check_stopReason;//核验订单
	private String	robot_cancel_stopReason;//取消订单
	private String	robot_endorse_stopReason;//改签
	private String	robot_refund_stopReason;//退票
	private String	robot_query_stopReason;//查询余票
	private String	robot_delete_stopReason;//删除
	private String  robot_enroll_stopReason;//注册
	private String  robot_activate_stopReason;//激活
	
	private String 	robot_money_status;//查询票价
	private String	robot_register_status;//实名认证
	private String	robot_book_status;//预订
	private String	robot_pay_status;//支付
	private String	robot_check_status;//核验订单
	private String	robot_cancel_status;//取消订单
	private String	robot_endorse_status;//改签
	private String	robot_refund_status;//退票
	private String	robot_query_status;//查询余票
	private String	robot_delete_status;//删除
	private String  robot_enroll_status;//注册
	private String  robot_activate_status;//激活
	
	
	private String 	robot_money_proxy;//查询票价
	private String	robot_register_proxy;//实名认证
	private String	robot_book_proxy;//预订
	private String	robot_pay_proxy;//支付
	private String	robot_check_proxy;//核验订单
	private String	robot_cancel_proxy;//取消订单
	private String	robot_endorse_proxy;//改签
	private String	robot_refund_proxy;//退票
	private String	robot_query_proxy;//查询余票
	private String	robot_delete_proxy;//删除
	private String  robot_enroll_proxy;//注册
	private String  robot_activate_proxy;//激活
	
	private List<WorkerVo> robot_workList;//配置代理
	
	private List<String> worker_typeList;//配置代理
	
	public List<String> getWorker_typeList() {
		return worker_typeList;
	}
	public void setWorker_typeList(List<String> workerTypeList) {
		worker_typeList = workerTypeList;
	}
	public List<WorkerVo> getRobot_workList() {
		return robot_workList;
	}
	public void setRobot_workList(List<WorkerVo> robotWorkList) {
		robot_workList = robotWorkList;
	}


	private String	stop_reason;//停用原因
	
	private String	spare_thread;//空闲
	private String  opt_name;//操作人
	private String  priority;//优先级
	
	private String  zhifubao;//支付宝账号
	
	
	
	public String getRobot_money_stopReason() {
		return robot_money_stopReason;
	}
	public void setRobot_money_stopReason(String robotMoneyStopReason) {
		robot_money_stopReason = robotMoneyStopReason;
	}
	public String getRobot_register_stopReason() {
		return robot_register_stopReason;
	}
	public void setRobot_register_stopReason(String robotRegisterStopReason) {
		robot_register_stopReason = robotRegisterStopReason;
	}
	public String getRobot_book_stopReason() {
		return robot_book_stopReason;
	}
	public void setRobot_book_stopReason(String robotBookStopReason) {
		robot_book_stopReason = robotBookStopReason;
	}
	public String getRobot_pay_stopReason() {
		return robot_pay_stopReason;
	}
	public void setRobot_pay_stopReason(String robotPayStopReason) {
		robot_pay_stopReason = robotPayStopReason;
	}
	public String getRobot_check_stopReason() {
		return robot_check_stopReason;
	}
	public void setRobot_check_stopReason(String robotCheckStopReason) {
		robot_check_stopReason = robotCheckStopReason;
	}
	public String getRobot_cancel_stopReason() {
		return robot_cancel_stopReason;
	}
	public void setRobot_cancel_stopReason(String robotCancelStopReason) {
		robot_cancel_stopReason = robotCancelStopReason;
	}
	public String getRobot_endorse_stopReason() {
		return robot_endorse_stopReason;
	}
	public void setRobot_endorse_stopReason(String robotEndorseStopReason) {
		robot_endorse_stopReason = robotEndorseStopReason;
	}
	public String getRobot_refund_stopReason() {
		return robot_refund_stopReason;
	}
	public void setRobot_refund_stopReason(String robotRefundStopReason) {
		robot_refund_stopReason = robotRefundStopReason;
	}
	public String getRobot_query_stopReason() {
		return robot_query_stopReason;
	}
	public void setRobot_query_stopReason(String robotQueryStopReason) {
		robot_query_stopReason = robotQueryStopReason;
	}
	public String getRobot_delete_stopReason() {
		return robot_delete_stopReason;
	}
	public void setRobot_delete_stopReason(String robotDeleteStopReason) {
		robot_delete_stopReason = robotDeleteStopReason;
	}
	public String getRobot_enroll_stopReason() {
		return robot_enroll_stopReason;
	}
	public void setRobot_enroll_stopReason(String robotEnrollStopReason) {
		robot_enroll_stopReason = robotEnrollStopReason;
	}
	public String getRobot_activate_stopReason() {
		return robot_activate_stopReason;
	}
	public void setRobot_activate_stopReason(String robotActivateStopReason) {
		robot_activate_stopReason = robotActivateStopReason;
	}
	public String getStop_reason() {
		return stop_reason;
	}
	public void setStop_reason(String stopReason) {
		stop_reason = stopReason;
	}
	public String getZhifubao() {
		return zhifubao;
	}
	public void setZhifubao(String zhifubao) {
		this.zhifubao = zhifubao;
	}


	private static Map<String, String> robot_setList = new LinkedHashMap<String, String>();
	public static final String	SETLIST_11="11";
	public static final String	SETLIST_10="10";
	public static final String	SETLIST_1="1";
	public static final String	SETLIST_3="3";
	public static final String	SETLIST_5="5";
	public static final String	SETLIST_6="6";
	public static final String	SETLIST_7="7";
	public static final String	SETLIST_8="8";
	public static final String	SETLIST_9="9";
	public static final String	SETLIST_13="13";
	public static final String	SETLIST_14="14";
	public static final String	SETLIST_15="15";
	public static Map<String, String> getRobot_setList(){
			if(robot_setList.isEmpty()){
				robot_setList.put(SETLIST_11, "查询票价");
				robot_setList.put(SETLIST_10, "实名认证");
				robot_setList.put(SETLIST_1, "预订");
				robot_setList.put(SETLIST_3, "支付");
				robot_setList.put(SETLIST_5, "核验订单");
				robot_setList.put(SETLIST_6, "取消订单");
				robot_setList.put(SETLIST_7, "改签");
				robot_setList.put(SETLIST_8, "退票");
				robot_setList.put(SETLIST_9, "余票查询");
				robot_setList.put(SETLIST_13, "删除");
				robot_setList.put(SETLIST_14, "注册账号");
				robot_setList.put(SETLIST_15, "激活账号");
			}
			return robot_setList;
		}
	
	
	private static Map<String, String> robot_statusList = new LinkedHashMap<String, String>();
	public static final String STATUS_1 ="1";
	public static final String STATUS_2 ="2";
	public static final String STATUS_3 ="3";
	
	public static Map<String, String> getRobot_statusList(){
		if(robot_statusList.isEmpty()){
			robot_statusList.put(STATUS_1, "启用");
			robot_statusList.put(STATUS_2, "停用");
			robot_statusList.put(STATUS_3, "备用");
		}
		return robot_statusList;
	}
	
	private static Map<String, String> robot_stopReason = new LinkedHashMap<String, String>();
	public static final String STOPREASON_1 ="11";
	public static final String STOPREASON_2 ="22";
	public static final String STOPREASON_3 ="33";
	public static final String STOPREASON_4 ="44";
	//① 机器人需要重启	② 机器人IP被封	③ 该支付机器人余额不足	④ 其他
	public static Map<String, String> getRobot_stopReson(){
		if(robot_stopReason.isEmpty()){
			robot_stopReason.put(STOPREASON_1, "需要重启");
			robot_stopReason.put(STOPREASON_2, "IP被封");
			robot_stopReason.put(STOPREASON_3, "余额不足");
			robot_stopReason.put(STOPREASON_4, "其他");
		}
		return robot_stopReason;
	}
	
	
	private static Map<String, String> proxyStatus = new LinkedHashMap<String, String>();
	public static final String PROXYSTATUS_00 ="00";
	public static final String PROXYSTATUS_11 ="11";
	//00 配置代理中 11 配置完成
	public static Map<String, String> getProxyStatus(){
		if(proxyStatus.isEmpty()){
			proxyStatus.put(PROXYSTATUS_00, "配置中");
			proxyStatus.put(PROXYSTATUS_11, "配置完成");
		}
		return proxyStatus;
	}
	
	public String getOpt_name() {
		return opt_name;
	}
	public void setOpt_name(String optName) {
		opt_name = optName;
	}
	public String getRobot_register() {
		return robot_register;
	}
	public void setRobot_register(String robotRegister) {
		robot_register = robotRegister;
	}
	public String getRobot_book() {
		return robot_book;
	}
	public void setRobot_book(String robotBook) {
		robot_book = robotBook;
	}
	public String getRobot_pay() {
		return robot_pay;
	}
	public void setRobot_pay(String robotPay) {
		robot_pay = robotPay;
	}
	public String getRobot_check() {
		return robot_check;
	}
	public void setRobot_check(String robotCheck) {
		robot_check = robotCheck;
	}
	public String getRobot_cancel() {
		return robot_cancel;
	}
	public void setRobot_cancel(String robotCancel) {
		robot_cancel = robotCancel;
	}
	public String getRobot_endorse() {
		return robot_endorse;
	}
	public void setRobot_endorse(String robotEndorse) {
		robot_endorse = robotEndorse;
	}
	public String getRobot_refund() {
		return robot_refund;
	}
	public void setRobot_refund(String robotRefund) {
		robot_refund = robotRefund;
	}
	public String getRobot_query() {
		return robot_query;
	}
	public void setRobot_query(String robotQuery) {
		robot_query = robotQuery;
	}
	public String getSpare_thread() {
		return spare_thread;
	}
	public void setSpare_thread(String spareThread) {
		spare_thread = spareThread;
	}
	public static void setRobot_channelList(Map<String, String> robotChannelList) {
		robot_channelList = robotChannelList;
	}
	//分配渠道：19e：19e平台 ext：对外商户 (qunar：去哪) elong：艺龙 app：手机端 embed：内嵌
	private static Map<String, String> robot_channelList = new LinkedHashMap<String, String>();
	public static final String CHANNEL_1 ="19e";
	public static final String CHANNEL_2 ="ext";
	public static final String CHANNEL_3 ="elong";
	public static final String CHANNEL_4 ="app";
	public static final String CHANNEL_5 ="inner";
	
	public static Map<String, String> getRobot_channelList(){
		
		if(robot_channelList.isEmpty()){
			
			robot_channelList.put(CHANNEL_1, "19e");
			robot_channelList.put(CHANNEL_2, "商户");
			robot_channelList.put(CHANNEL_4, "B2C");
			robot_channelList.put(CHANNEL_3, "艺龙");
			robot_channelList.put(CHANNEL_5, "内嵌");
		}
		
		return robot_channelList;
	}
	public String getRobot_id() {
		return robot_id;
	}
	public void setRobot_id(String robotId) {
		robot_id = robotId;
	}
	public String getRobot_name() {
		return robot_name;
	}
	public void setRobot_name(String robotName) {
		robot_name = robotName;
	}
	public String getRobot_url() {
		return robot_url;
	}
	public void setRobot_url(String robotUrl) {
		robot_url = robotUrl;
	}
	public String getRobot_status() {
		return robot_status;
	}
	public void setRobot_status(String robotStatus) {
		robot_status = robotStatus;
	}
	public String getRobot_desc() {
		return robot_desc;
	}
	public void setRobot_desc(String robotDesc) {
		robot_desc = robotDesc;
	}
	public String getRobot_channel() {
		return robot_channel;
	}
	public void setRobot_channel(String robotChannel) {
		robot_channel = robotChannel;
	}
	public String getRobot_type() {
		return robot_type;
	}
	public void setRobot_type(String robotType) {
		robot_type = robotType;
	}
	public String getRobot_con_timeout() {
		return robot_con_timeout;
	}
	public void setRobot_con_timeout(String robotConTimeout) {
		robot_con_timeout = robotConTimeout;
	}
	public String getRobot_read_timeout() {
		return robot_read_timeout;
	}
	public void setRobot_read_timeout(String robotReadTimeout) {
		robot_read_timeout = robotReadTimeout;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getRobot_money() {
		return robot_money;
	}
	public void setRobot_money(String robotMoney) {
		robot_money = robotMoney;
	}
	public String getRobot_money_num() {
		return robot_money_num;
	}
	public void setRobot_money_num(String robotMoneyNum) {
		robot_money_num = robotMoneyNum;
	}
	public String getRobot_register_num() {
		return robot_register_num;
	}
	public void setRobot_register_num(String robotRegisterNum) {
		robot_register_num = robotRegisterNum;
	}
	public String getRobot_book_num() {
		return robot_book_num;
	}
	public void setRobot_book_num(String robotBookNum) {
		robot_book_num = robotBookNum;
	}
	public String getRobot_pay_num() {
		return robot_pay_num;
	}
	public void setRobot_pay_num(String robotPayNum) {
		robot_pay_num = robotPayNum;
	}
	public String getRobot_check_num() {
		return robot_check_num;
	}
	public void setRobot_check_num(String robotCheckNum) {
		robot_check_num = robotCheckNum;
	}
	public String getRobot_cancel_num() {
		return robot_cancel_num;
	}
	public void setRobot_cancel_num(String robotCancelNum) {
		robot_cancel_num = robotCancelNum;
	}
	public String getRobot_endorse_num() {
		return robot_endorse_num;
	}
	public void setRobot_endorse_num(String robotEndorseNum) {
		robot_endorse_num = robotEndorseNum;
	}
	public String getRobot_refund_num() {
		return robot_refund_num;
	}
	public void setRobot_refund_num(String robotRefundNum) {
		robot_refund_num = robotRefundNum;
	}
	public String getRobot_query_num() {
		return robot_query_num;
	}
	public void setRobot_query_num(String robotQueryNum) {
		robot_query_num = robotQueryNum;
	}
	public String getRobot_delete() {
		return robot_delete;
	}
	public void setRobot_delete(String robotDelete) {
		robot_delete = robotDelete;
	}
	public String getRobot_delete_num() {
		return robot_delete_num;
	}
	public void setRobot_delete_num(String robotDeleteNum) {
		robot_delete_num = robotDeleteNum;
	}
	
	public String getRobot_enroll() {
		return robot_enroll;
	}
	public void setRobot_enroll(String robotEnroll) {
		robot_enroll = robotEnroll;
	}
	public String getRobot_enroll_num() {
		return robot_enroll_num;
	}
	public void setRobot_enroll_num(String robotEnrollNum) {
		robot_enroll_num = robotEnrollNum;
	}
	public String getRobot_activate() {
		return robot_activate;
	}
	public void setRobot_activate(String robotActivate) {
		robot_activate = robotActivate;
	}
	public String getRobot_activate_num() {
		return robot_activate_num;
	}
	public void setRobot_activate_num(String robotActivateNum) {
		robot_activate_num = robotActivateNum;
	}
	public String getRobot_money_status() {
		return robot_money_status;
	}
	public void setRobot_money_status(String robotMoneyStatus) {
		robot_money_status = robotMoneyStatus;
	}
	public String getRobot_register_status() {
		return robot_register_status;
	}
	public void setRobot_register_status(String robotRegisterStatus) {
		robot_register_status = robotRegisterStatus;
	}
	public String getRobot_book_status() {
		return robot_book_status;
	}
	public void setRobot_book_status(String robotBookStatus) {
		robot_book_status = robotBookStatus;
	}
	public String getRobot_pay_status() {
		return robot_pay_status;
	}
	public void setRobot_pay_status(String robotPayStatus) {
		robot_pay_status = robotPayStatus;
	}
	public String getRobot_check_status() {
		return robot_check_status;
	}
	public void setRobot_check_status(String robotCheckStatus) {
		robot_check_status = robotCheckStatus;
	}
	public String getRobot_cancel_status() {
		return robot_cancel_status;
	}
	public void setRobot_cancel_status(String robotCancelStatus) {
		robot_cancel_status = robotCancelStatus;
	}
	public String getRobot_endorse_status() {
		return robot_endorse_status;
	}
	public void setRobot_endorse_status(String robotEndorseStatus) {
		robot_endorse_status = robotEndorseStatus;
	}
	public String getRobot_refund_status() {
		return robot_refund_status;
	}
	public void setRobot_refund_status(String robotRefundStatus) {
		robot_refund_status = robotRefundStatus;
	}
	public String getRobot_query_status() {
		return robot_query_status;
	}
	public void setRobot_query_status(String robotQueryStatus) {
		robot_query_status = robotQueryStatus;
	}
	public String getRobot_delete_status() {
		return robot_delete_status;
	}
	public void setRobot_delete_status(String robotDeleteStatus) {
		robot_delete_status = robotDeleteStatus;
	}
	public String getRobot_enroll_status() {
		return robot_enroll_status;
	}
	public void setRobot_enroll_status(String robotEnrollStatus) {
		robot_enroll_status = robotEnrollStatus;
	}
	public String getRobot_activate_status() {
		return robot_activate_status;
	}
	public void setRobot_activate_status(String robotActivateStatus) {
		robot_activate_status = robotActivateStatus;
	}
	public String getRobot_money_proxy() {
		return robot_money_proxy;
	}
	public void setRobot_money_proxy(String robotMoneyProxy) {
		robot_money_proxy = robotMoneyProxy;
	}
	public String getRobot_register_proxy() {
		return robot_register_proxy;
	}
	public void setRobot_register_proxy(String robotRegisterProxy) {
		robot_register_proxy = robotRegisterProxy;
	}
	public String getRobot_book_proxy() {
		return robot_book_proxy;
	}
	public void setRobot_book_proxy(String robotBookProxy) {
		robot_book_proxy = robotBookProxy;
	}
	public String getRobot_pay_proxy() {
		return robot_pay_proxy;
	}
	public void setRobot_pay_proxy(String robotPayProxy) {
		robot_pay_proxy = robotPayProxy;
	}
	public String getRobot_check_proxy() {
		return robot_check_proxy;
	}
	public void setRobot_check_proxy(String robotCheckProxy) {
		robot_check_proxy = robotCheckProxy;
	}
	public String getRobot_cancel_proxy() {
		return robot_cancel_proxy;
	}
	public void setRobot_cancel_proxy(String robotCancelProxy) {
		robot_cancel_proxy = robotCancelProxy;
	}
	public String getRobot_endorse_proxy() {
		return robot_endorse_proxy;
	}
	public void setRobot_endorse_proxy(String robotEndorseProxy) {
		robot_endorse_proxy = robotEndorseProxy;
	}
	public String getRobot_refund_proxy() {
		return robot_refund_proxy;
	}
	public void setRobot_refund_proxy(String robotRefundProxy) {
		robot_refund_proxy = robotRefundProxy;
	}
	public String getRobot_query_proxy() {
		return robot_query_proxy;
	}
	public void setRobot_query_proxy(String robotQueryProxy) {
		robot_query_proxy = robotQueryProxy;
	}
	public String getRobot_delete_proxy() {
		return robot_delete_proxy;
	}
	public void setRobot_delete_proxy(String robotDeleteProxy) {
		robot_delete_proxy = robotDeleteProxy;
	}
	public String getRobot_enroll_proxy() {
		return robot_enroll_proxy;
	}
	public void setRobot_enroll_proxy(String robotEnrollProxy) {
		robot_enroll_proxy = robotEnrollProxy;
	}
	public String getRobot_activate_proxy() {
		return robot_activate_proxy;
	}
	public void setRobot_activate_proxy(String robotActivateProxy) {
		robot_activate_proxy = robotActivateProxy;
	}
	
}
