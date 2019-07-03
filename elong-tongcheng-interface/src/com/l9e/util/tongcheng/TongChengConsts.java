package com.l9e.util.tongcheng;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.l9e.common.Consts;
import com.l9e.util.DateUtil;
import com.l9e.util.elong.StrUtil;
public class TongChengConsts {
	
	private static Map<String, String> to19eSeatMap = new HashMap<String, String>();
	
	private static Map<String, String> toTCSeatMap = new HashMap<String, String>();
	
	private static Map<String, String> zwname = new HashMap<String, String>();
	private static Map<String, String> piaotypename = new HashMap<String, String>();
	private static Map<String, String> passporttypeseidname = new HashMap<String, String>();
	private static Map<String, String> changeErrorInfo = new HashMap<String, String>();
	
	//同城订单状态描述
	private static Map<String, String> descriptionname = new HashMap<String, String>();
	
	private static Map<String, String> errorinfo = new HashMap<String, String>();
	
	private static final Pattern trainCodeRegex = Pattern.compile("[A-Z]+");
	
	static {
		//1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验
		errorinfo.put("error_1", "所购买的车次坐席已无票");
		errorinfo.put("error_2", "身份证件已经实名制购票");
		errorinfo.put("error_3", "票价和12306不符");
		errorinfo.put("error_4", "乘车时间异常");
		errorinfo.put("error_5", "证件错误");
		errorinfo.put("error_6", "用户要求取消订单");
		errorinfo.put("error_7", "未通过12306实名认证");
		errorinfo.put("error_8", "乘客身份信息待核验");
		errorinfo.put("error_11", "超时未支付");
		errorinfo.put("error_12", "身份信息冒用");
		/*占座失败 1
		占座成功 2
		正在出票 3
		出票成功 4
		出票失败 5
		订单已取消 6
		补单成功 7
		正在退票 8
		退票成功 9
		退票失败 10
		正在改签 11
		改签成功 12
		改签失败 13*/
		descriptionname.put("description_1", "占座失败");
		descriptionname.put("description_2", "占座成功");
		descriptionname.put("description_3", "正在出票");
		descriptionname.put("description_4", "出票成功");
		descriptionname.put("description_5", "出票失败");
		descriptionname.put("description_6", "订单已取消");
		descriptionname.put("description_7", "补单成功");
		descriptionname.put("description_8", "正在退票");
		descriptionname.put("description_9", "退票成功");
		descriptionname.put("description_10", "退票失败");
		descriptionname.put("description_11", "正在改签");
		descriptionname.put("description_12", "改签成功");
		descriptionname.put("description_13", "改签失败");
		
		//9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，
		//4:软卧，3:硬卧，2:软座，1:硬座 7:一等软座 8:二等软座 A:高级动卧 F:动卧 
		zwname.put("zw_9", "商务座");
		zwname.put("zw_P", "特等座");
		zwname.put("zw_M", "一等座");
		zwname.put("zw_7", "一等软座");
		zwname.put("zw_O", "二等座");
		zwname.put("zw_8", "二等软座");
		zwname.put("zw_6", "高级软卧");
		zwname.put("zw_A", "高级动卧");
		zwname.put("zw_4", "软卧");
		zwname.put("zw_F", "动卧");
		
		zwname.put("zw_3", "硬卧");
		zwname.put("zw_2", "软座");
		zwname.put("zw_1", "硬座");
		
		
		//1:成人票，2:儿童票，3:学生票，4:残军票
		piaotypename.put("piaotype_1", "成人票");
		piaotypename.put("piaotype_2", "儿童票");
		piaotypename.put("piaotype_3", "学生票");
		piaotypename.put("piaotype_4", "残军票");
		//1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通行证，B:护照
		passporttypeseidname.put("passport_1", "二代身份证");
		passporttypeseidname.put("passport_2", "一代身份证");
		passporttypeseidname.put("passport_C", "港澳通行证");
		passporttypeseidname.put("passport_G", "台湾通行证");
		passporttypeseidname.put("passport_B", "护照");
		
		
		
		to19eSeatMap.put("tc_1", "8");//硬座
		to19eSeatMap.put("tc_2", "7");//软座
		to19eSeatMap.put("tc_3", "6");//硬卧
		to19eSeatMap.put("tc_4", "5");//软卧
		to19eSeatMap.put("tc_6", "4");//高级软卧
		to19eSeatMap.put("tc_O", "3");//二等软座	
		to19eSeatMap.put("tc_M", "2");//一等软座	
		to19eSeatMap.put("tc_P", "0");//商务座
		to19eSeatMap.put("tc_9", "0");//商务座
		/*9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，
		 * 4:软卧，3:硬卧，2:软座，1:硬座
		 * 
		 * 座位类型：0、商务座
		 * 1、特等座 2、一等座3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下）6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下）7、软座 8、硬座 9、无座 10、其他
		 * 2、一等座
		 *  3、二等座
		 *   4、高级软卧（41、高级软卧上 42、高级软卧下） 
		 *   5、软卧 （51、软卧上 52、软卧下）
		 *    6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下）
		 *     7、软座 
		 *     8、硬座
		 *      9、无座 10、其他
		 *      11   一人软包  12 观光座
	*/
	/*	toTCSeatMap.put("19e_0", "8");//商务座  sys座位类型
		toTCSeatMap.put("19e_1", "11");//特等座 
		toTCSeatMap.put("19e_2", "9");//一等座
		toTCSeatMap.put("19e_3", "10");//二等座
		
		toTCSeatMap.put("19e_4", "5");//高级软卧
		toTCSeatMap.put("19e_5", "4");//软卧
		toTCSeatMap.put("19e_6", "3");//硬卧 
		toTCSeatMap.put("19e_7", "2");//软座 
		toTCSeatMap.put("19e_8", "1");//硬座
		toTCSeatMap.put("19e_9", "0");//无座
		toTCSeatMap.put("10", "1");//其他
	 */		
		
		/*同程改签失败错误信息*/
		/*
		 * 401、请求时间已超时，出票失败
		 * 403、账户余额不足
		 * 301、没有余票
		 * 305、乘客已经预定过该车次
		 * 308、乘客身份信息未通过验证
		 * 309、没有足够的票
		 * 310、本次购票与其他订单冲突
		 * 
		 * 
		 * 
		 * false	1002	距离开车时间太近无法改签
			false	1004	取消改签次数超过上限，无法继续操作
			false	301	没有余票
			false	305	乘客已经预订过该车次
			false	309	没有足够的票
			false	310	本次购票与其他订单行程冲突
			false	506	实际系统错误原因（即供应商本身系统问题导致无法占座操作）
			false	313	示例：“张三已被法院依法限制高消费，禁止乘坐列车XX座位“，具体按12306实际提示结果返回
			false	998	订单所属账号被封，无法处理
			false	999	所有未定义的12306即时返回的错误提示


		 * 
		 */
		changeErrorInfo.put("1002","距离开车时间太近无法改签");
		changeErrorInfo.put("1004","取消改签次数超过上限,无法继续操作");
		changeErrorInfo.put("301","没有余票");
		changeErrorInfo.put("305","乘客已经预定过该车次");
		changeErrorInfo.put("309","没有足够的票");
		changeErrorInfo.put("310","本次购票与其他订单冲突");
		changeErrorInfo.put("506","系统异常,无法正常占座操作");
		changeErrorInfo.put("313","订单内乘客已被法院依法限制高消费，禁止乘坐当前预订席别");
		changeErrorInfo.put("314","12306账号异常_该账号需要核验，请确认");
		changeErrorInfo.put("998","订单所属账号被封，无法处理");
		changeErrorInfo.put("999","未定义的12306错误");
		changeErrorInfo.put("9991","旅游票,请到车站办理");
		changeErrorInfo.put("401", "请求时间已超时,出票失败");
		changeErrorInfo.put("403", "账户余额不足");
		changeErrorInfo.put("308", "乘客身份信息未通过验证");
		changeErrorInfo.put("1010", "不符合变更到站条件");

	}
	
	/**系统 坐席类型*/
	public static String SEAT_TYPE_0="0";//商务座
	public static String SEAT_TYPE_1="1";//特等座 
	public static String SEAT_TYPE_2="2";//一等座
	public static String SEAT_TYPE_3="3";//二等座 
	public static String SEAT_TYPE_4="4";//高级软卧
	public static String SEAT_TYPE_5="5";//软卧 
	public static String SEAT_TYPE_6="6";//硬卧
	public static String SEAT_TYPE_7="7";//软座
	public static String SEAT_TYPE_8="8";///硬座
	public static String SEAT_TYPE_9="9";//无座
	//public static String SEAT_TYPE_10="10";//10、其他
	public static String SEAT_TYPE_11="11";//一人软包
	public static String SEAT_TYPE_12="12";//观光座
	
	/**同城坐席类型转换成系统坐席类型*/
	public static String get19eSeatType(String tcSeat){
		return to19eSeatMap.get("tc_"+tcSeat);
	}
	
	/* 同城证件号转换成 系统证件号 */
	/* sys证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 */
	/*	1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通 行证，B:护照*/
	public static String get19eIdsType(String certType){
		if("1".equals(certType) ){//身份证
			return "2";
		}else if("2".equals(certType)){
			return "1";
		}else if("C".equalsIgnoreCase(certType)){//港澳通行证
			return "3";
		}else if("G".equalsIgnoreCase(certType)){//台湾通行证
			return "4";
		}else if("B".equalsIgnoreCase(certType)){//护照
			return "5";
		}else{
			return "2";
		}
	}
	
	
	
	
	/* 同城证件号转换成 系统证件号 */
	/* sys证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 */
	/*	1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通 行证，B:护照*/
	public static String getTcIdsType(String certType){
		if("1".equals(certType) ){//身份证
			return "2";
		}else if("2".equals(certType)){
			return "1";
		}else if("3".equalsIgnoreCase(certType)){//港澳通行证
			return "C";
		}else if("4".equalsIgnoreCase(certType)){//台湾通行证
			return "G";
		}else if("5".equalsIgnoreCase(certType)){//护照
			return "B";
		}else{
			return "1";
		}
	}
	/**
	 * sys:车票类型0成人票  1儿童票   3:学生票
	 *1:成人票，2:儿童票，3:学生票，4:残军票
	 * */
	public static String get19eTicketType(String tcTicketType){
		if(StringUtils.isEmpty(tcTicketType)){
			return "0";
		}else if("2".equals(tcTicketType)){
			return "1";
		}else if("3".equals(tcTicketType)){
			return "3";
		}else{
			return "0";//成人
		}
	}
	
	
	/**
	 * sys:车票类型0成人票  1儿童票   3:学生票
	 *1:成人票，2:儿童票，3:学生票，4:残军票
	 * */
	public static String getTcTicketType(String ticketType){
		if(StringUtils.isEmpty(ticketType)){
			return "1";
		}else if("1".equals(ticketType)){
			return "2";
		}else if("3".equals(ticketType)){
			return "3";
		}else{
			return "1";//成人
		}
	}
	
	/**
	 * 同程车次类型粗略匹配
	 * @param train_code
	 * @return
	 */
	public static String getTongChengTrainType(String train_code) {
		if(StrUtil.isEmpty(train_code)) return "";
		Matcher m = trainCodeRegex.matcher(train_code);
		String result = "";
		if(m.find()) {
			String key = m.group();
			if(key.equals("K") || key.equals("Y") || key.equals("S")) {
				result = "空调快速";
			} else if(key.equals("C")) {
				result = "城际高速";
			} else if(key.equals("G")) {
				result = "高速动车";
			} else if(key.equals("D")) {
				result = "动车组";
			} else if(key.equals("T")) {
				result = "空调特快";
			} else if(key.equals("Z")) {
				result = "直达特快";
			} else if(key.equals("XGZ")) {
				result = "香港直通车";
			}
		} else {
			result = "空调普快";
		}
		return result;
	}
	public static Double getDiffRate(String from_time, Date current) {
		Double rate = 0.0;//默认值
		
		Date from = DateUtil.stringToDate(from_time, DateUtil.DATE_FMT3);
		Date from_24hour = DateUtil.dateAddDays(from, -1);//24小时以内
		Date from_48hour = DateUtil.dateAddDays(from, -2);//48小时以内
		Date from_15Day = DateUtil.dateAddDays(from, -15);
		
		if(current.after(from_24hour)) {
			rate = Consts.TC_DIFFRATE_LESS_24;
		} else if(current.before(from_24hour) && current.after(from_48hour)) {
			rate = Consts.TC_DIFFRATE_24_48;
		} else if(current.before(from_48hour) && current.after(from_15Day)) {
			rate = Consts.TC_DIFFRATE_48_MORE;
		} else {
			
		}
		return rate;
	}
	public static String getZwname(String zwcode){
		return zwname.get("zw_"+zwcode);
	}
	public static String getPassporttypeseidname(String passporttypeseid){
		return passporttypeseidname.get("passport_"+passporttypeseid);
	}
	public static String getPiaotypename(String piaotype){
		return piaotypename.get("piaotype_"+piaotype);
	}
	public static String getDescriptionname(String orderStatus){
		return descriptionname.get("piaotype_"+orderStatus);
	}
	
	public static String getErrorInfo(String errorCode){
		String errorInfo=errorinfo.get("error_"+errorCode);
		return errorInfo==null?errorinfo.get("error_1"):errorInfo;
	}

	public static String getTcChangeErrorInfo(String errorCode) {
		String errorInfo = changeErrorInfo.get(errorCode);
		return errorInfo;
	}
}
