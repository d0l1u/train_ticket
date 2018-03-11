package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 投诉与建议的对象
 */
public class ComplainVo{
	private String complain_id ;
	private long province_id ;
	private long city_id ;
	private long agent_id ;
	private String question_type ;
	private String question ;
	private String answer ;
	private String permission ;
	private String create_time ;
	private String reply_time ;
	private String opt_person ;
	private String provincename ;
	private String cityname ;
	
	public static String ORDER_FORM = "0" ;//订单问题
	public static String JOIN_IN = "1" ; //加盟问题
//	public static String GLUCODIDE = "2" ; // 配送问题
	public static String OUT_TICKET = "3" ; //出票问题
	public static String OPERATION_ADVICE = "4" ; //业务建议
	public static String OTHER_ADVICE = "5" ; //其他 
	
	public static String ALL_LOOK = "0" ; //全部可见
	public static String SELF_LOOK = "1" ; //自己可见
	
	
	
	public ComplainVo(){}
	
	public static Map<String, String> QUESTION_TYPE = new LinkedHashMap<String, String>();
	public static Map<String, String> PURVIEW= new LinkedHashMap<String, String>();
	
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
	
	//app投诉建议问题状态
	//问题类别 00：订单问题 11：出票问题 22：业务建议 55：其他
	public static Map<String, String> APP_QUESTION_TYPE = new LinkedHashMap<String, String>();
	public static String APP_ORDER_FORM = "00" ;//订单问题
	public static String APP_OUT_TICKET = "11" ; //出票问题
	public static String APP_OPERATION_ADVICE = "22" ; //业务建议
	public static String APP_OTHER_ADVICE = "55" ; //其他 
	public static Map<String,String> getAppQuestionType(){
		if(APP_QUESTION_TYPE.isEmpty()){
			APP_QUESTION_TYPE.put(APP_ORDER_FORM, "订单问题") ;
			//APP_QUESTION_TYPE.put(APP_JOIN_IN, "加盟问题") ;
			//APP_QUESTION_TYPE.put(APP_GLUCODIDE, "配送问题") ;
			APP_QUESTION_TYPE.put(APP_OUT_TICKET, "出票问题") ;
			APP_QUESTION_TYPE.put(APP_OPERATION_ADVICE, "业务建议") ;
			APP_QUESTION_TYPE.put(APP_OTHER_ADVICE, "其他");
		}
		return APP_QUESTION_TYPE;
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
	public long getProvince_id() {
		return province_id;
	}
	public void setProvince_id(long province_id) {
		this.province_id = province_id;
	}
	public long getCity_id() {
		return city_id;
	}
	public void setCity_id(long city_id) {
		this.city_id = city_id;
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


		
	
}
