package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 投诉与建议的对象
 */
public class AllComplainVo{
	private String complain_id ;//投诉建议id
	private long agent_id ;//代理商id
	private String question_type ;//问题类别
	private String question ;//问题或建议
	private String answer ;//答复
	private String permission ;//权限
	private String create_time ;//提问时间
	private String reply_time ;//答复时间
	private String opt_person ;//操作人
	private String channel;//渠道
	private String reply_season;//处理状态
	
	//19e渠道投诉时附加4个条件
	private String user_name;//代理商姓名
	private String user_phone;//代理商号码
	private String province_id;//代理商所在省
	private String provincename ;
	private String city_id;//代理商所在城市
	private String cityname ;
	
	public static String ORDER_FORM = "0" ;//订单问题
	public static String JOIN_IN = "1" ; //加盟问题
//	public static String GLUCODIDE = "2" ; // 配送问题
	public static String OUT_TICKET = "3" ; //出票问题
	public static String OPERATION_ADVICE = "4" ; //业务建议
	public static String OTHER_ADVICE = "5" ; //其他 
	
	public static String ALL_LOOK = "0" ; //全部可见
	public static String SELF_LOOK = "1" ; //自己可见
	
	public static String CHANNEL_19E = "19e";
	public static String CHANNEL_QUNAR = "qunar";
	public static String CHANNEL_19PAY = "19pay";
	public static String CHANNEL_CMPAY = "cmpay";
	public static String CHANNEL_19TRIP = "web";
	public static String CHANNEL_APP = "app";
	public static String CHANNEL_CBCPAY = "ccb";
	public static String CHANNEL_WEIXIN = "weixin";
	public static String CHANNEL_CHQ = "chq";
	
	
	public static String WEICHULI = "00";
	public static String YICHULI = "11";
	
	public AllComplainVo(){}
	
	public static Map<String, String> QUESTION_TYPE = new LinkedHashMap<String, String>();
	public static Map<String, String> PURVIEW= new LinkedHashMap<String, String>();
	private static Map<String,String>QUESTION_CHANNEL = new LinkedHashMap<String,String>();
	private static Map<String,String>REPLY_SEASON = new LinkedHashMap<String,String>();
	
	public static Map<String,String> getReplySeason(){
		if(REPLY_SEASON.isEmpty()){
			REPLY_SEASON.put(WEICHULI, "未处理");
			REPLY_SEASON.put(YICHULI, "已处理");
		}
		return REPLY_SEASON;
	}
	
	public static Map<String,String>getQuestionChannel(){
		if(QUESTION_CHANNEL.isEmpty()){
			QUESTION_CHANNEL.put(CHANNEL_19E, "19e");
//			QUESTION_CHANNEL.put(CHANNEL_QUNAR, "去哪");
			QUESTION_CHANNEL.put(CHANNEL_19PAY, "19pay");
			QUESTION_CHANNEL.put(CHANNEL_CMPAY, "cmpay");
			QUESTION_CHANNEL.put(CHANNEL_19TRIP, "19trip");
			QUESTION_CHANNEL.put(CHANNEL_APP, "app");
			QUESTION_CHANNEL.put(CHANNEL_CBCPAY, "建行");
			QUESTION_CHANNEL.put(CHANNEL_WEIXIN, "微信");
			QUESTION_CHANNEL.put(CHANNEL_CHQ, "春秋");
		}
		return QUESTION_CHANNEL;
	}
	public static Map<String,String> getQuestionType(){
		if(QUESTION_TYPE.isEmpty()){
			QUESTION_TYPE.put(ORDER_FORM, "订单问题") ;
			QUESTION_TYPE.put(JOIN_IN, "加盟问题") ;
//			QUESTION_TYPE.put(GLUCODIDE, "配送问题") ;
			QUESTION_TYPE.put(OUT_TICKET, "出票问题") ;
			QUESTION_TYPE.put(OPERATION_ADVICE, "业务建议") ;
			QUESTION_TYPE.put(OTHER_ADVICE, "其他");
		}
		return QUESTION_TYPE;
		
	}
	public static Map<String,String> getPurview(){
		if(PURVIEW.isEmpty()){
			PURVIEW.put(ALL_LOOK, "全部可见") ;
			PURVIEW.put(SELF_LOOK, "自己可见") ;
		}
		return PURVIEW;
		
	}
	public String getComplain_id() {
		return complain_id;
	}
	public void setComplain_id(String complain_id) {
		this.complain_id = complain_id;
	}

	public long getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(long agent_id) {
		this.agent_id = agent_id;
	}
	public String getQuestion_type() {
		return question_type;
	}
	public void setQuestion_type(String question_type) {
		this.question_type = question_type;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getReply_time() {
		return reply_time;
	}
	public void setReply_time(String reply_time) {
		this.reply_time = reply_time;
	}
	public String getOpt_person() {
		return opt_person;
	}
	public void setOpt_person(String opt_person) {
		this.opt_person = opt_person;
	}


	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String userPhone) {
		user_phone = userPhone;
	}
	public String getProvince_id() {
		return province_id;
	}
	public void setProvince_id(String provinceId) {
		province_id = provinceId;
	}
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String cityId) {
		city_id = cityId;
	}
	public String getProvincename() {
		return provincename;
	}
	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getReply_season() {
		return reply_season;
	}
	public void setReply_season(String replySeason) {
		reply_season = replySeason;
	}



		
	
}
