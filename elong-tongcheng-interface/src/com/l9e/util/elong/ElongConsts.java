package com.l9e.util.elong;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ElongConsts {
	/**站票支持状态*/
	public static String ACCEPTSTAND="1";//支持站票
	public static String NOTACCEPTSTAND="0";//特等座 
	
	private static Map<String, String> to19eSeatMap = new HashMap<String, String>();
	
	private static Map<String, String> toElongSeatMap = new HashMap<String, String>();
	
	private static Map<String, String> RefundReasonDescMap = new HashMap<String, String>();
	
	private static Map<String, String> RefundCommentMap = new HashMap<String, String>();
	
	private static Map<String, String> seatTypeMessage = new HashMap<String, String>();
	
	private static Map<String, String> outFailReasonMessage = new HashMap<String, String>();
	
	private static Map<String,String> ElongIdsTypeName=new HashMap<String,String>();
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
	public static String SEAT_TYPE_15="15";//动卧
	public static String SEAT_TYPE_16="16";//高级动卧
	
	public static String ELONG_SEAT_TYPE_0="0";//站票
	public static String ELONG_SEAT_TYPE_1="1";//硬座
	public static String ELONG_SEAT_TYPE_2="2";//软座
	public static String ELONG_SEAT_TYPE_3="3";//硬卧
	public static String ELONG_SEAT_TYPE_4="4";//软卧
	public static String ELONG_SEAT_TYPE_5="5";//高级软卧
	public static String ELONG_SEAT_TYPE_6="6";//一等软座
	public static String ELONG_SEAT_TYPE_7="7";//二等软座
	public static String ELONG_SEAT_TYPE_8="8";///商务座
	public static String ELONG_SEAT_TYPE_9="9";//一等座
	public static String ELONG_SEAT_TYPE_10="10";//二等座
	public static String ELONG_SEAT_TYPE_11="11";//特等座
	public static String ELONG_SEAT_TYPE_12="12";//观光座
	public static String ELONG_SEAT_TYPE_13="13";//特等软座
	public static String ELONG_SEAT_TYPE_14="14";//一人软包   
	public static String ELONG_SEAT_TYPE_15="15";//动卧
	public static String ELONG_SEAT_TYPE_16="16";//高级动卧
	//elong座位类型 :0站票1硬座2软座3硬卧4软卧5高级软卧6一等软座7二等软座8商务座9一等座10二等座11特等座12观光座13特等软座14一人软包                                                                                                                  
	/*座位类型：0、商务座
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
	static {
		to19eSeatMap.put("elong_0", "9");//站票  elong座位类型
		to19eSeatMap.put("elong_1", "8");//硬座
		to19eSeatMap.put("elong_2", "7");//软座
		to19eSeatMap.put("elong_3", "6");//硬卧
		to19eSeatMap.put("elong_4", "5");//软卧
		to19eSeatMap.put("elong_5", "4");//高级软卧
		to19eSeatMap.put("elong_6", "2");//一等软座		
		to19eSeatMap.put("elong_7", "3");//二等软座		
		to19eSeatMap.put("elong_8", "0");//商务座
		to19eSeatMap.put("elong_9", "2");//一等座
		to19eSeatMap.put("elong_10", "3");//二等座
		to19eSeatMap.put("elong_11", "1");//特等座?
		to19eSeatMap.put("elong_12", "12");//观光座?
		to19eSeatMap.put("elong_13", "1");//特等软座?
		to19eSeatMap.put("elong_14", "11");//一人软包?  
		to19eSeatMap.put("elong_15", "15");//动卧
		to19eSeatMap.put("elong_16", "16");//高级动卧
		
		
		//elong座位类型 :0站票1硬座2软座3硬卧4软卧5高级软卧6一等软座7二等软座8商务座
		//9一等座10二等座11特等座12观光座13特等软座14一人软包15动卧                                                                                                            
		/*座位类型：0、商务座
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
		toElongSeatMap.put("19e_0", "8");//商务座  sys座位类型
		toElongSeatMap.put("19e_1", "11");//特等座 
		toElongSeatMap.put("19e_2", "9");//一等座
		toElongSeatMap.put("19e_3", "10");//二等座
		
		toElongSeatMap.put("19e_4", "5");//高级软卧
		toElongSeatMap.put("19e_5", "4");//软卧
		toElongSeatMap.put("19e_6", "3");//硬卧 
		toElongSeatMap.put("19e_7", "2");//软座 
		toElongSeatMap.put("19e_8", "1");//硬座
		toElongSeatMap.put("19e_9", "0");//无座
		toElongSeatMap.put("19e_10", "1");//其他
		toElongSeatMap.put("19e_15", "15");//动卧
		toElongSeatMap.put("19e_16", "16");//高级动卧
		
		RefundReasonDescMap.put("reason_1", "已取票");
		RefundReasonDescMap.put("reason_2", "已过时间");
		RefundReasonDescMap.put("reason_3", "来电取消");
		RefundReasonDescMap.put("reason_7", "窗口办理退票");
		//RefundReasonDescMap.put("reason_4", "");
		
		RefundCommentMap.put("comment_1", "已取出实体票，请持车票及乘车人身份证件原件，至火车站退票窗口办理退票业务。");
		RefundCommentMap.put("comment_2", "您的车次已开，无法办理退票");
		RefundCommentMap.put("comment_3", "用户已经通过电话取消退票");
		
		//elong座位类型 :0站票1硬座2软座3硬卧4软卧5高级软卧6一等软座7二等软座8商务座9一等座10二等座11特等座12观光座13特等软座14一人软包                                                                                                                  
		
		seatTypeMessage.put("elong_0","站票");
		seatTypeMessage.put("elong_1","硬座");
		seatTypeMessage.put("elong_2","软座");
		seatTypeMessage.put("elong_3","硬卧");
		seatTypeMessage.put("elong_4","软卧");
		seatTypeMessage.put("elong_5","高级软卧");
		seatTypeMessage.put("elong_6","一等软座");
		seatTypeMessage.put("elong_7","二等软座");
		seatTypeMessage.put("elong_8","商务座");
		seatTypeMessage.put("elong_9","一等座");
		seatTypeMessage.put("elong_10","二等座");
		seatTypeMessage.put("elong_11","特等座");
		seatTypeMessage.put("elong_12","观光座");
		seatTypeMessage.put("elong_13","特等软座");
		seatTypeMessage.put("elong_14","一人软包");
		seatTypeMessage.put("elong_15","动卧");
		seatTypeMessage.put("elong_16","高级动卧");
		
		outFailReasonMessage.put("error_0","其他");
		outFailReasonMessage.put("error_1","所购买的车次坐席已无票");
		outFailReasonMessage.put("error_2","身份证件已经实名制购票，不能再次购买同日期同车次的车票");
		outFailReasonMessage.put("error_3","票价和12306不符");
		outFailReasonMessage.put("error_4","车次数据与12306不一致");
		outFailReasonMessage.put("error_5","乘客信息错误");
		outFailReasonMessage.put("error_6","12306乘客身份信息核验失败");
		
		
		
		/*1身份证 C港澳通行证 G台湾通行证 B护照*/
		ElongIdsTypeName.put("elongIdsType_1", "身份证");
		ElongIdsTypeName.put("elongIdsType_C", "港澳通行证");
		ElongIdsTypeName.put("elongIdsType_G", "台湾通行证");
		ElongIdsTypeName.put("elongIdsType_B", "护照");
	}
	public static String getFailReasonMessage(String outFailReason){
		return outFailReasonMessage.get("error_"+outFailReason);
	}
	
	public static String getSeatTypeMessage(String seat){
		return seatTypeMessage.get("elong_"+seat);
	}
	
	public static String getRefundReasonDesc(String refundReason){
		return RefundReasonDescMap.get("reason_"+refundReason);
	}
	
	public static String getRefundComment(String refundReason){
		return RefundCommentMap.get("comment_"+refundReason);
	}
	
	public static String get19eSeatType(String seat){
		return to19eSeatMap.get("elong_"+seat);
	}
	
	public static String getElongSeatType(String seat){
		return toElongSeatMap.get("19e_"+seat);
	}
	
	
	
	/*证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照 */
	/*1身份证 C港澳通行证 G台湾通行证 B护照*/
	public static String get19eIdsType(String certType, String certNo){
		if("1".equals(certType) && certNo.length()==15){//身份证
			return "1";
		}else if("1".equals(certType) && certNo.length()==18){
			return "2";
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
	
	public static String getElongIdsType(String certType){
		if(StringUtils.isEmpty(certType)){
			return "1";
		}else if("2".equals(certType)){
			return "1";
		}else if("3".equals(certType)){
			return "C";
		}else if("4".equals(certType)){
			return "G";
		}else if("5".equals(certType)){
			return "B";
		}else{
			return "1";
		}
	}
	
	/**sys:车票类型0成人票  1儿童票    elong:0儿童票 1成人票 2学生票 */
	public static String getElongTicketType(String l9eTicketType){
		if(StringUtils.isEmpty(l9eTicketType)){
			return "1";
		}else if("1".equals(l9eTicketType)){
			return "0";
		}else{
			return "1";//成人
		}
		
	}
	
	/**sys:车票类型0成人票  1儿童票    elong:0儿童票 1成人票 2学生票 */
	public static String get19eTicketType(String elongTicketType){
		if(StringUtils.isEmpty(elongTicketType)){
			return "0";
		}else if("0".equals(elongTicketType)){
			return "1";
		}else{
			return "0";
		}
		
	}
	
	
	/**elong */
	/*0	其他	
	1	所购买的车次坐席已无票	
	2	身份证件已经实名制购票，不能再次购买同日期同车次的车票	
	3	票价和12306不符	
	4	车次数据与12306不一致	
	5	乘客信息错误	
	6	12306乘客身份信息核验失败	12306乘客身份信息核验失败，passengerReason必填*/
	/**sys*/
	/*：1所购买的车次坐席已无票 
	2身份证件已经实名制购票
	3票价和12306不符
	4乘车时间异常 
	5证件错误 
	6用户要求取消订单 
	7未通过12306实名认证 
	8乘客身份信息待核验  */
	public static String getElongErrorCode(String l9eErrorCode){
		if(StringUtils.isEmpty(l9eErrorCode)){
			return "0";
		}else if("1".equals(l9eErrorCode)){
			return "1";//所购买的车次坐席已无票
		}else if("2".equals(l9eErrorCode)){
			return "2";//身份证件已经实名制购票
		}else if("3".equals(l9eErrorCode)){
			return "3";//票价和12306不符
		}else if("4".equals(l9eErrorCode)){
			return "4";//乘车时间异常 
		}else if("5".equals(l9eErrorCode)){
			return "5";//证件错误 
		}else if("6".equals(l9eErrorCode)){
			return "0";//未通过12306实名认证 
		}else if("7".equals(l9eErrorCode)){
			return "6";//未通过12306实名认证 
		}else{//
			return "6";//未通过12306实名认证 
		}
		
	}
	
	public static String getSysOrderId(String elongOrderId){
		return "EL"+elongOrderId;
	}
	
	public static String getElongOrderId(String sysOrderId){
		return sysOrderId.replaceAll("EL", "");
	}

	public static String getElongIdsTypeName(String elongIdsType) {
		return ElongIdsTypeName.get("elongIdsType_"+elongIdsType);
	}
}
