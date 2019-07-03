package com.l9e.train.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.channel.request.impl.RobotAlterRequest;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.StrUtil;


public class TrainAlertBase {

	private Logger logger = LoggerFactory.getLogger(TrainAlertBase.class);
	
	/**
	 * alterURL
	 * @param map
	 * @return
	 */
	public String getAlterUrl(Order alter, OrderCP pas, String interfaceUrl){
		StringBuffer result = new StringBuffer();
		String url = new String(interfaceUrl);
		logger.info("发起改签或变更到站 url地址端口号变更前为:"+url);
		StringBuffer sb1 = new StringBuffer();
		sb1.append(alter.getAccountName())
		  .append("|")
		  .append(alter.getAccountPwd())
		  .append("|")
		  .append(alter.getOrderId())
		  .append("|")
		  .append(alter.getOutTicketBillno())
		  .append("|")
		  .append(alter.getFromCity())
		  .append("|")
		  .append(alter.getToCity())
		  .append("|")
		  .append(alter.getTravelTime())
		  .append("|")
		  .append(alter.getChangeTravelTime())
		  .append("|")
		  .append(alter.getTrainNo())
		  .append("|")
		  .append(alter.getChangeTrainNo())
		  .append("|")
		  .append(alter.getInputCode());
		 if(alter.getIsChangeTo()!=null&&alter.getIsChangeTo().equals(1)){
			 sb1.append("|")
			 .append("Y"); 
		  }			
			
		logger.info(alter.getOrderId()+";发起改签的车次信息:"+sb1.toString());
		String param1 = "";
		try {
			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuffer sb2 = new StringBuffer();
		sb2.append(pas.getCpId())
		  .append("|")
		  .append(pas.getUserName())
		  .append("|")
		  .append(pas.getTicketType())
		  .append("|")
		  .append(pas.getIdsType())
		  .append("|")
		  .append(pas.getUserIds())
		  .append("|")
		  .append(alter.getChangeSeatType())
		  .append("|")
		  .append(pas.getTrainBox())
		  .append("|")
		  .append(pas.getSeatNo());
		logger.info(alter.getOrderId()+";发起改签乘客信息:"+sb2.toString());
		String param2 = "";
		try {
			param2 = URLEncoder.encode(sb2.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info("[机器人session_id]=" + session_id);
		
		 //生成一个随机的16位的字符串(设备号）
		 String deviceNo=StrUtil.getRandomString(16);
		 //commond
		 String commondStr = "changeTicket";
		 
		 //改签是否接受无座票  1、不改到无座票     0、允许改到无座票
		 Integer hasSeat = alter.getHasSeat();
		 
		 String fromCity3c = alter.getFromCity_3c();
		 String toCity3c = alter.getToCity_3c();
		
		//申请改签的url端口号需要改成8291
		//url = url.replace("8290", "8291");
		logger.info("发起改签或变更到站 url地址端口号变更后为:"+url);
		url += "?ScriptPath=onlyResign.lua&SessionID=$session_id&Timeout=180000&ParamCount=$paramCount" +
				"&commond=$commondStr&DeviceNo=$deviceNo&FromCity3c=$fromCity3c&ToCity3c=$toCity3c&HasSeat=$hasSeat"
		 		+ "&Param1=$param1&Param2=$param2";
		result.append(url.replace("$session_id", session_id).replace("$paramCount", "2")
				.replace("$commondStr", commondStr).replace("$deviceNo", deviceNo)
				.replace("$fromCity3c", fromCity3c).replace("$toCity3c", toCity3c)
				.replace("$hasSeat", String.valueOf(hasSeat)).replace("$param1", param1).replace("$param2", param2));
		logger.info(alter.getOrderId()+";发起改签或变更到站 url地址为"+result.toString());
		return result.toString();
	}
	
	/**
	 * alter继续改签--低改高时调用
	 * @param map
	 * @return
	 */
	public String getPayLowToHighUrl(Order alter, List<OrderCP> pas_list, Worker worker,PayCard payCard){
		StringBuffer result = new StringBuffer();
		String url = new String(worker.getWorkerExt());
		
		
		TrainServiceImpl service = new TrainServiceImpl();
		//支付宝支付时选择的打码器。0:公司自己的打码动态库；1：第三方的打码动态库
		String codeEditor=null;		
		try {
			codeEditor = service.getSysSettingValue("codeEditorSelect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info(alter.getOrderId()+";发起低改高支付时[机器人url]="+url+ " [session_id]" + session_id);
		String paramCount = String.valueOf(pas_list.size()+1);
		
		//commond
		 String commondStr = "lowToHighPay";
		//生成一个随机的16位的字符串(设备号）
		 String deviceNo=StrUtil.getRandomString(16);
		 
		 //改签支付类型    1：平改   2：高改低   3：低改高
		 Integer alterPayType = alter.getAlterPayType();
		 if(null == alterPayType){
			 alterPayType = 0;
		 }
		 logger.info(alter.getOrderId()+";发起低改高支付时，改签支付类型AlterPayType为:"+alterPayType);
		
		
		//12306登陆账号|12306登陆账号密码|订单号|12306订单号|支付金额|支付账号|支付密码|安全手机号|机器人ID|低改高标识
		StringBuffer sb1 = new StringBuffer();
		sb1.append(alter.getAccountName())
		  .append("|")
		  .append(alter.getAccountPwd())
		  .append("|")
		  .append(alter.getOrderId())
		  .append("|")
		  .append(alter.getOutTicketBillno())
		  .append("|")
		  .append(alter.getChangeReceiveMoney())
		  .append("|")
		  .append(payCard.getCardNo())
		  .append("|")
		  .append(payCard.getCardPwd())
		  .append("|")
		  .append(payCard.getCardPhone())
		  .append("|")
		  .append(codeEditor)
		  .append("|")
		  .append(worker.getWorkerId())
		  .append("|")
		  .append(alterPayType);
		
		logger.info(alter.getOrderId()+";低改高支付时的支付账户信息为:"+sb1.toString());
		String param1 = "";
		try {
			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		url += "?ScriptPath=payResign.lua&SessionID=$session_id&Timeout=180000&ParamCount=$paramCount&commond=$commondStr"
				+ "&DeviceNo=$deviceNo&Param1=$param1";
		result.append(url.replace("$session_id", session_id)
				.replace("$paramCount", paramCount).replace("$commondStr", commondStr)
				.replace("$deviceNo", deviceNo).replace("$param1", param1));
	
	    logger.info(alter.getOrderId()+";低改高支付时，拼接起来的url为:"+result.toString());
				
		return result.toString();
	}
	
	/**
	 * alter继续改签(平改，高改低)
	 * @param map
	 * @return
	 */
	public String getContinuePayUrl(Order alter, List<OrderCP> pas_list, Worker worker){
		StringBuffer result = new StringBuffer();
		String url = new String(worker.getWorkerExt());

		StringBuffer sb1 = new StringBuffer();
		sb1.append(alter.getAccountName())
		  .append("|")
		  .append(alter.getAccountPwd())
		  .append("|")
		  .append(alter.getOrderId())
		  .append("|")
		  .append(alter.getOutTicketBillno())
		  .append("|")
		  .append(alter.getFromCity())
		  .append("|")
		  .append(alter.getToCity())
		  .append("|")
		  .append(alter.getTravelTime())
		  .append("|")
		  .append(alter.getChangeTravelTime())
		  .append("|")
		  .append(alter.getTrainNo())
		  .append("|")
		  .append(alter.getChangeTrainNo())
		  .append("|")
		  .append(alter.getInputCode());	
		logger.info(alter.getOrderId()+";发起继续支付的车次信息:"+sb1.toString());
		String param1 = "";
		try {
			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		int index = 2;
		for(OrderCP pas:alter.getCps()){
			StringBuffer sb2 = new StringBuffer();
			sb2.append(pas.getCpId())
			  .append("|")
			  .append(pas.getUserName())
			  .append("|")
			  .append(pas.getTicketType())
			  .append("|")
			  .append(pas.getIdsType())
			  .append("|")
			  .append(pas.getUserIds())
			  .append("|")
			  .append(alter.getChangeSeatType())
			  .append("|")
			  .append(pas.getTrainBox())
			  .append("|")
			  .append(pas.getSeatNo());
			
			String param2 = "";
			try {
				param2 = URLEncoder.encode(sb2.toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append("&Param"+index).append("=").append(param2);
			index++;
		}
		logger.info(alter.getOrderId()+";发起继续支付乘客信息:"+sb.toString());
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info(alter.getOrderId()+";发起继续支付[机器人url]="+url+ " [session_id]" + session_id);
		String paramCount = String.valueOf(pas_list.size()+1);
		
		//commond
		 String commondStr = "payTicket";
		//生成一个随机的16位的字符串(设备号）
		 String deviceNo=StrUtil.getRandomString(16);
		 
		 //改签支付类型    1：平改   2：高改低   3：低改高
		 Integer alterPayType = alter.getAlterPayType();
		 if(null == alterPayType){
			 alterPayType = 0;
		 }
		 logger.info(alter.getOrderId()+";发起继续支付时，改签支付类型AlterPayType为:"+alterPayType);
		 
		url += "?ScriptPath=payResign.lua&SessionID=$session_id&Timeout=180000&ParamCount=$paramCount&commond=$commondStr"
				+ "&AlterPayType=$alterPayType&DeviceNo=$deviceNo&Param1=$param1&Param2=$param2";
		result.append(url.replace("$session_id", session_id).replace(
				"$paramCount", paramCount).replace("$commondStr", commondStr)
				.replace("$alterPayType", String.valueOf(alterPayType))
				.replace("$deviceNo", deviceNo)
				.replace("$param1", param1).replace("&Param2=$param2",sb.toString()));
		
		logger.info(alter.getOrderId()+";发起继续支付时，拼接起来的url为:"+result.toString());
		return result.toString();
	}
	
	/**
	 * alterAllURL批量改签
	 * @param map
	 * @return
	 */
	public String getAlterAllUrl(Order alter, List<OrderCP> cpList, String interfaceUrl){
		StringBuffer result = new StringBuffer();
		String url = new String(interfaceUrl);
		StringBuffer sb1 = new StringBuffer();
		sb1.append(alter.getAccountName())
		  .append("|")
		  .append(alter.getAccountPwd())
		  .append("|")
		  .append(alter.getOrderId())
		  .append("|")
		  .append(alter.getOutTicketBillno())
		  .append("|")
		  .append(alter.getFromCity())
		  .append("|")
		  .append(alter.getToCity())
		  .append("|")
		  .append(alter.getTravelTime())
		  .append("|")
		  .append(alter.getChangeTravelTime())
		  .append("|")
		  .append(alter.getTrainNo())
		  .append("|")
		  .append(alter.getChangeTrainNo())
		  .append("|")
		  .append(alter.getInputCode());
		if (alter.getIsChangeTo() != null && alter.getIsChangeTo().equals(1)) {
			sb1.append("|").append("Y");
		}
		logger.info(alter.getOrderId()+";发起改签的车次信息:"+sb1.toString());
		String param1 = "";
		try {
			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		int index = 2;
		for(OrderCP pas:cpList){
			StringBuffer sb2 = new StringBuffer();
			sb2.append(pas.getCpId())
			  .append("|")
			  .append(pas.getUserName())
			  .append("|")
			  .append(pas.getTicketType())
			  .append("|")
			  .append(pas.getIdsType())
			  .append("|")
			  .append(pas.getUserIds())
			  .append("|")
			  .append(alter.getChangeSeatType())
			  .append("|")
			  .append(pas.getTrainBox())
			  .append("|")
			  .append(pas.getSeatNo());
			
			String param2 = "";
			try {
				param2 = URLEncoder.encode(sb2.toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append("&Param"+index).append("=").append(param2);
			index++;
		}
		logger.info(alter.getOrderId()+";发起改签乘客信息:"+sb.toString());
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info(alter.getOrderId()+";发起改签[机器人url]="+url+" [session_id]:" + session_id);
		String paramCount = String.valueOf(cpList.size()+1);
		
		 //生成一个随机的16位的字符串(设备号）
		 String deviceNo=StrUtil.getRandomString(16);
		 //commond
		 String commondStr = "changeTicket";
		 
		//改签是否接受无座票  1、不改到无座票     0、允许改到无座票
		 Integer hasSeat = alter.getHasSeat();
		 String fromCity3c = alter.getFromCity_3c();
		 String toCity3c = alter.getToCity_3c();
		
		//申请改签的url端口号需要改成8291
		//url = url.replace("8290", "8291");
		logger.info("发起改签或变更到站 url地址端口号变更后为:"+url);
		
		url += "?ScriptPath=onlyResignAll.lua&SessionID=$session_id&Timeout=180000&ParamCount=$paramCount"
				+ "&commond=$commondStr&DeviceNo=$deviceNo&FromCity3c=$fromCity3c&ToCity3c=$toCity3c&HasSeat=$hasSeat&Param1=$param1&Param2=$param2";
		result.append(url.replace("resign", "resignall").replace("$session_id", session_id)
				.replace("$paramCount", paramCount).replace("$commondStr", commondStr).replace("$deviceNo", deviceNo)
				.replace("$fromCity3c", fromCity3c).replace("$toCity3c", toCity3c)
				.replace("$hasSeat", String.valueOf(hasSeat)).replace("$param1", param1)
				.replace("&Param2=$param2", sb.toString()));
		logger.info(alter.getOrderId() + ";发起改签或变更到站 url地址为" + result.toString());
		return result.toString();
	}

//	public static void main(String[] args){
//		
//		TrainAlertBase ttt = new TrainAlertBase();	
//		Order alter=new Order();
//		alter.setAccountName("cnbf7727");
//		alter.setAccountPwd("yhfg2722");
//		alter.setOrderId("HC1610121336414679");
//		alter.setOutTicketBillno("E042098956");
//		alter.setFromCity("包头");
//		alter.setToCity("包头东");
//		alter.setTravelTime("2016-12-06");
//		alter.setChangeTravelTime("2016-12-06");
//		alter.setTrainNo("K7906");
//		alter.setChangeTrainNo("K7906");
//		alter.setInputCode("0");
//		alter.setChangeSeatType("8");
//		
//		OrderCP pas = new OrderCP();
//		pas.setCpId("CP1610121336414682");
//		pas.setUserName("李永照");
//		pas.setTicketType("0");
//		pas.setIdsType("2");
//		pas.setUserIds("370421195412162912");
//		pas.setTrainBox("14");
//		pas.setSeatNo("001号");
//		
////		OrderCP pas1 = new OrderCP();
////		pas1.setCpId("CP1610121336414684");
////		pas1.setUserName("王春花");
////		pas1.setTicketType("1");
////		pas1.setIdsType("2");
////		pas1.setUserIds("412828197907036029");
////		pas1.setTrainBox("14");
////		pas1.setSeatNo("028号");
//		
//		List<OrderCP> pas_list = new ArrayList<OrderCP>();
//		pas_list.add(pas);
//		//pas_list.add(pas1);
//		
//		StringBuffer result = new StringBuffer();
//		String url = "";
//		System.out.println("发起改签或变更到站 url地址端口号变更前为:"+url);
//		StringBuffer sb1 = new StringBuffer();
//		sb1.append(alter.getAccountName())
//		  .append("|")
//		  .append(alter.getAccountPwd())
//		  .append("|")
//		  .append(alter.getOrderId())
//		  .append("|")
//		  .append(alter.getOutTicketBillno())
//		  .append("|")
//		  .append(alter.getFromCity())
//		  .append("|")
//		  .append(alter.getToCity())
//		  .append("|")
//		  .append(alter.getTravelTime())
//		  .append("|")
//		  .append(alter.getChangeTravelTime())
//		  .append("|")
//		  .append(alter.getTrainNo())
//		  .append("|")
//		  .append(alter.getChangeTrainNo())
//		  .append("|")
//		  .append(alter.getInputCode());
//		 if(alter.getIsChangeTo()!=null&&alter.getIsChangeTo().equals(1)){
//			 sb1.append("|")
//			 .append("Y"); 
//		  }			
//			
//		 System.out.println("发起改签的车次信息:"+sb1.toString());
//		String param1 = "";
//		try {
//			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
////		StringBuffer sb2 = new StringBuffer();
////		sb2.append(pas.getCpId())
////		  .append("|")
////		  .append(pas.getUserName())
////		  .append("|")
////		  .append(pas.getTicketType())
////		  .append("|")
////		  .append(pas.getIdsType())
////		  .append("|")
////		  .append(pas.getUserIds())
////		  .append("|")
////		  .append(alter.getChangeSeatType())
////		  .append("|")
////		  .append(pas.getTrainBox())
////		  .append("|")
////		  .append(pas.getSeatNo());
//		StringBuffer sb = new StringBuffer();
//		int index = 2;
//		for(OrderCP pasList:pas_list){
//			StringBuffer sb2 = new StringBuffer();
//			sb2.append(pasList.getCpId())
//			  .append("|")
//			  .append(pasList.getUserName())
//			  .append("|")
//			  .append(pasList.getTicketType())
//			  .append("|")
//			  .append(pasList.getIdsType())
//			  .append("|")
//			  .append(pasList.getUserIds())
//			  .append("|")
//			  .append(alter.getChangeSeatType())
//			  .append("|")
//			  .append(pasList.getTrainBox())
//			  .append("|")
//			  .append(pasList.getSeatNo());
//			
//			String param2 = "";
//			try {
//				param2 = URLEncoder.encode(sb2.toString(), "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			sb.append("&Param"+index).append("=").append(param2);
//			index++;
//		}
//		
//		System.out.println(alter.getOrderId()+";发起改签乘客信息:"+sb.toString());
////		String param2 = "";
////		try {
////			param2 = URLEncoder.encode(sb.toString(), "UTF-8");
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}  
//		String session_id = String.valueOf(System.currentTimeMillis());
//		System.out.println("[机器人session_id]=" + session_id);
//		
//		//新增参数:设备号,三字码,端口号
//		 TrainServiceImpl service = new TrainServiceImpl();
//			
//		 //生成一个随机的16位的字符串(设备号）
//		 String deviceNo=StrUtil.getRandomString(16);
//		 //commond
//		 String commondStr = "changeTicket";
//		 //在此获取订单中出发城市和到达城市的三字码  add by wangsf
//		String fromCity = alter.getFromCity();
//		System.out.println("订单中出发城市为：" + fromCity);
//		String toCity = alter.getToCity();
//		System.out.println("订单中到达城市为：" + toCity);
//		String fromCity3c = "BTC";//出发城市三字码
//		String toCity3c = "BDC";//到达城市三字码
//		String paramCount = String.valueOf(pas_list.size()+1);
//		String hasSeat = "1";
//		//申请改签的url端口号需要改成8291
//		//url = url.replace("8290", "8291");
//		url = "http://43.241.225.165:8290/RunScript";
//		url += "?ScriptPath=onlyResignAll.lua&SessionID=$session_id&Timeout=180000&ParamCount=$paramCount&commond=$commondStr&DeviceNo=$deviceNo&FromCity3c=$fromCity3c&ToCity3c=$toCity3c&HasSeat=$hasSeat"
//				+ "&Param1=$param1&Param2=$param2";
//		result.append(url.replace("resign", "resignall").replace("$session_id", session_id).replace("$paramCount", paramCount)
//				.replace("$commondStr", commondStr).replace("$deviceNo", deviceNo)
//				.replace("$fromCity3c", fromCity3c).replace("$toCity3c", toCity3c)
//				.replace("$hasSeat", hasSeat)
//				.replace("$param1", param1).replace("&Param2=$param2", sb.toString()));
//		System.out.println("发起改签或变更到站 url地址为"+result.toString());
//		
//		String jsonStr = HttpUtil.sendByGet(result.toString(), "UTF-8", "200000", "200000");//调用接口
//		if(jsonStr.contains("获取WL-Instance-Id为空")){
//			jsonStr = HttpUtil.sendByGet(result.toString(), "UTF-8", "200000", "200000");//调用接口
//		}
//		
//		//String jsonStr = "{\"ErrorInfo\":[{\"contactsnum\":0,\"outTicketBillno\":\"EC02483541\",\"orderId\":\"14759423310001\",\"retInfo\":\"1\",\"arrivetime\":\"2016-11-07 05:57\",\"summoney\":\"8.0\",\"from\":\"包头\",\"seattime\":\"2016-11-07 05:42\",\"trainno\":\"K1674\",\"retValue\":\"success\",\"to\":\"包头东\",\"cps\":[{\"resign_flag\":\"\",\"trainbox\":\"15\",\"cpId\":\"tcCF3B9C8F02CF259F\",\"id\":\"340402198410180067\",\"seattype\":\"1\",\"seatNo\":\"104号\",\"name\":\"胡娜\",\"idtype\":\"1\",\"status\":\"04\",\"paymoney\":\"8.0\",\"return_flag\":\"\",\"tickettype\":\"1\",\"msg\":\"改签待支付\"}]}],\"ErrorCode\":0}";
//		jsonStr = jsonStr.replace("\\\"", "\"").replace("[\"{", "[{").replace("}\"]", "}]");
//		System.out.println(alter.getOrderId()+"；格式转换后改签返回结果："+jsonStr);
//		try {
//			ttt.executeAlterResult(alter,pas_list, jsonStr, 0);
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (RepeatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	public void executeAlterResult(Order alter, List<OrderCP> pas_list, String jsonStr,
//			int wea_con) throws JsonParseException, JsonMappingException, IOException, RepeatException, DatabaseException{
////		Map<String,String> map = new HashMap<String,String>();
////		TrainServiceImpl trainServiceImpl = new TrainServiceImpl();
////		String order_id = alter.getOrderId();
////		map.put("order_id", order_id);
////		map.put("change_id", alter.getChangeId());
////		//map.put("cp_id", cp_id);
////		map.put("type", "SINGLE");
////		map.put("statistical", "true");
////		map.put("channel", alter.getChannel()); 
////		String alter_fail_cp = "";
////		
////		//改造：如果返回结果中出发和到达时间有值，则把出发，到达时间更新到数据库
////		ObjectMapper mapper = new ObjectMapper();
////		ReturnAlterInfo alterResult = mapper.readValue(jsonStr, ReturnAlterInfo.class);
////		
////		//只有订单状态不为改签待支付，才更新车次出发，到达时间
////		if(wea_con == 0){
////		String changeFromTime = alterResult.getErrorInfo().get(0).getSeattime(); //改签后的出发时间
////		String changeToTime = alterResult.getErrorInfo().get(0).getArrivetime();//改签后的到达时间
////		map.put("change_from_time", changeFromTime);
////		map.put("change_to_time", changeToTime);
////		}
////		
////		if(jsonStr.contains("\"retValue\":\"failure\"")){
////			trainServiceImpl.insertHistory(order_id, null, "发起机器改签失败,转为人工处理！");
////			logger.info("发起机器改签失败,转为人工处理---"+jsonStr);
////			int fail_reason_start_index = jsonStr.indexOf("orderId");
////			int fail_reason_end_index = jsonStr.indexOf("retInfo");
////			String fail_reason = jsonStr.substring(fail_reason_start_index, fail_reason_end_index-3);
////			logger.info("错误原因为"+fail_reason);
////			fail_reason_start_index = fail_reason.indexOf(":");
////			fail_reason = fail_reason.substring(fail_reason_start_index+2);
////			logger.info("错误原因1为"+fail_reason);
////			trainServiceImpl.insertHistory(order_id, null, fail_reason);
////			//转为人工处理
////			if(wea_con == 2){
////				map.put("order_status", Order.PAYING);
////				map.put("new_order_status", Order.ARTIFICIAL_PAY);
////			}else{
////				map.put("order_status", Order.RESIGNING);
////				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////			}
////			//map.put("fail_reason", fail_reason);
////			trainServiceImpl.updateOrderStatus(map);//更新退款状态
////		if(jsonStr.contains("没有查到该列车")|| jsonStr.contains("未找到要改签的车次")){
////			trainServiceImpl.insertHistory(order_id, null, "未找到要改签的车次，请确认该列车出发与否或者现在是否允许订票，改签自动失败！");
////			logger.info("发起机器改签失败,未找到要改签的车次，改签自动失败---"+jsonStr);
////			//自动失败
////			if(wea_con == 2){
////				map.put("order_status", Order.PAYING);
////				map.put("new_order_status", Order.ARTIFICIAL_PAY);
////			}else{
////				map.put("order_status", Order.RESIGNING);
////				map.put("new_order_status", Order.RESIGN_FAILURE);
////				map.put("fail_reason", TrainConsts.FAILCODE_UNFINDTRAINNO);
////			}
////			
////			trainServiceImpl.updateOrderStatus(map);//更新改签订单状态
////		}else if(jsonStr.contains("已出票")){
////			trainServiceImpl.insertHistory(order_id, null, " 已出票,无法改签，改签自动失败！");
////			logger.info("已出票,无法改签，改签自动失败--"+jsonStr);
////			//改签失败
////			if(wea_con == 2){
////				map.put("order_status", Order.PAYING);
////				map.put("new_order_status", Order.ARTIFICIAL_PAY);
////			}else{
////				map.put("order_status", Order.RESIGNING);
////				map.put("new_order_status", Order.RESIGN_FAILURE);
////				map.put("fail_reason", TrainConsts.FAILCODE_YETOUTTICKET);
////			}
////			trainServiceImpl.updateOrderStatus(map);//更新改签订单状态
////		}else if(jsonStr.contains("已改签")){
////			trainServiceImpl.insertHistory(order_id, null, " 已改签,不可改签，改签自动失败！");
////			logger.info("已改签,无法改签，改签自动失败--"+jsonStr);
////			//改签失败
////			if(wea_con == 2){
////				map.put("order_status", Order.PAYING);
////				map.put("new_order_status", Order.ARTIFICIAL_PAY);
////			}else{
////				map.put("order_status", Order.RESIGNING);
////				map.put("new_order_status", Order.RESIGN_FAILURE);
////				map.put("fail_reason", TrainConsts.FAILCODE_YETALTERTICKET);
////			}
////			trainServiceImpl.updateOrderStatus(map);//更新改签订单状态
////		}else if(jsonStr.contains("行程冲突")){
////			trainServiceImpl.insertHistory(order_id, null, " 本次购票与其它订单行程冲突，改签失败！");
////			logger.info("行程冲突，改签失败--"+jsonStr);
////			//改签失败
////			if(wea_con == 2){
////				map.put("order_status", Order.PAYING);
////				map.put("new_order_status", Order.ARTIFICIAL_PAY);
////			}else{
////				map.put("order_status", Order.RESIGNING);
////				map.put("new_order_status", Order.RESIGN_FAILURE);
////				map.put("fail_reason", TrainConsts.FAILCODE_TRAVELCONFLICT);
////			}
////			trainServiceImpl.updateOrderStatus(map);//更新改签订单状态
////		}else if(jsonStr.contains("您还有未处理的订单")||jsonStr.contains("存在未完成订单")){
////			trainServiceImpl.insertHistory(order_id, null, "该帐号还有未处理(未支付)的订单，转为人工处理！");
////			logger.info("存在未完成订单,转为人工处理---"+jsonStr);
////			//转为人工处理
////			if(wea_con == 2){
////				map.put("order_status", Order.PAYING);
////				map.put("new_order_status", Order.ARTIFICIAL_PAY);
////			}else{
////				map.put("order_status", Order.RESIGNING);
////				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////				map.put("fail_reason", TrainConsts.FAILCODE_HAVEUNFINISHEDORDER);
////			}
////			trainServiceImpl.updateOrderStatus(map);//更新改签订单状态
////		}else if(jsonStr.contains("没有余票")){
////			trainServiceImpl.insertHistory(order_id, null, "没有余票，改签失败！");
////			logger.info("没有余票，改签失败---"+jsonStr);
////			//改签失败
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.RESIGN_FAILURE);
////			map.put("fail_reason", TrainConsts.FAILCODE_NOTICKET);
////			
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		}else if(jsonStr.contains("系统错误")){
////			trainServiceImpl.insertHistory(order_id, null, "系统错误，转为人工处理！");
////			logger.info("系统错误，转为人工处理---"+jsonStr);
////			//改签转人工
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////			map.put("fail_reason", TrainConsts.FAILCODE_SYSTEMERROR);
////			
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		}else if(jsonStr.contains("高消费")){
////			trainServiceImpl.insertHistory(order_id, null, "法院依法限制高消费，禁止乘坐列车**座位，改签失败！");
////			logger.info("法院依法限制高消费，禁止乘坐列车**座位，改签失败---"+jsonStr);
////			//改签失败
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.RESIGN_FAILURE);
////			map.put("fail_reason", TrainConsts.FAILCODE_HIGHCONSUME);
////			
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		}else if(jsonStr.contains("未定义的12306错误")){
////			trainServiceImpl.insertHistory(order_id, null, "未定义的12306错误，转为人工处理！");
////			logger.info("未定义的12306错误，转为人工处理---"+jsonStr);
////			//改签转人工
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////			map.put("fail_reason", TrainConsts.FAILCODE_UNDEFINEDERROR);
////			
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		}else if(jsonStr.contains("账号登陆失败")){
////			trainServiceImpl.insertHistory(order_id, null, "账号登陆失败，转为人工处理！");
////			logger.info("账号登陆失败，转为人工处理---"+jsonStr);
////			//改签转人工
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////			map.put("fail_reason", TrainConsts.FAILCODE_LOGINFAIL);
////			
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		}else if(jsonStr.contains("距离开车时间太近")){
////			trainServiceImpl.insertHistory(order_id, null, "距离开车时间太近，改签失败！");
////			logger.info("距离开车时间太近，改签失败---"+jsonStr);
////			//改签失败
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.RESIGN_FAILURE);
////			map.put("fail_reason", TrainConsts.FAILCODE_DRIVETIMECLOSE);
////			
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		}else if(jsonStr.contains("取消改签次数超过上限")){
////			trainServiceImpl.insertHistory(order_id, null, "取消改签次数超过上限，改签失败！");
////			logger.info("取消改签次数超过上限，改签失败---"+jsonStr);
////			//改签失败
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.RESIGN_FAILURE);
////			map.put("fail_reason", TrainConsts.FAILCODE_CANCELUPTOLIMIT);
////			
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		}else if(jsonStr.contains("旅游票")){
////			trainServiceImpl.insertHistory(order_id, null, "旅游票，请到车站办理改签，改签失败！");
////			logger.info("旅游票，请到车站办理改签，改签失败---"+jsonStr);
////			//改签失败
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.RESIGN_FAILURE);
////			map.put("fail_reason", TrainConsts.FAILCODE_TRAVELTICKET);
////			
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		}else if(jsonStr.contains("不允许改签到指定时间的车票")){
////			trainServiceImpl.insertHistory(order_id, null, "不允许改签到指定时间的车票，改签失败！");
////			logger.info("不允许改签到指定时间的车票，改签失败---"+jsonStr);
////			//改签失败
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.RESIGN_FAILURE);
////			map.put("fail_reason", TrainConsts.FAILCODE_APPOINTTIME);
////			
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		} else if (jsonStr.contains("已退票")) {
////			trainServiceImpl.insertHistory(order_id, null, "已退票，改签失败！");
////			logger.info("已退票，改签失败---" + jsonStr);
////			// 改签失败
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.RESIGN_FAILURE);
////			map.put("fail_reason", TrainConsts.FAILCODE_YETRETURNTICKET);
////
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		} else if (jsonStr.contains("排队购票人数过多")) {
////			trainServiceImpl.insertHistory(order_id, null,
////					"排队购票人数过多，转为人工处理！");
////			logger.info("排队购票人数过多，转为人工处理---" + jsonStr);
////			// 改签转人工
////			map.put("order_status", Order.RESIGNING);
////			map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////			map.put("fail_reason", TrainConsts.FAILCODE_LISTTOOMUCH);
////
////			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
////		  }
////		}else{
////			
//////			ObjectMapper mapper = new ObjectMapper();
//////			ReturnAlterInfo alterResult = mapper.readValue(jsonStr, ReturnAlterInfo.class);
////			List<ReturnAlterPasEntity> list_pas = alterResult.getErrorInfo().get(0).getCps();
////			if(list_pas.size()==0){
////				trainServiceImpl.insertHistory(order_id, null, "机器改签结果返回空！请人工确认并处理！");
////				logger.info(alter.getOrderId()+"机器改签返回空值！请人工确认并处理");
////				//转为人工处理
////				
////				map.put("order_status", Order.RESIGNING);
////				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////				
////				trainServiceImpl.updateOrderStatus(map);//更新退款状态
////				return;
////			}
////			boolean repeatPay = false;
////			//double diff_money = 0;
////			for(ReturnAlterPasEntity pape : list_pas){
//////				if (wea_con == 2) {
//////					if("00".equals(pape.getStatus())){
//////						map.put("order_status", Order.PAYING);
//////						map.put("new_order_status", Order.PAY_FINISH);
//////						trainServiceImpl.updateOrderStatus(map);//更新退款状态
//////						StringBuffer sb_success = new StringBuffer();
//////						sb_success.append("车票：").append(pape.getCpId())
//////						.append("支付成功，支付金额为：")
//////						.append(pape.getPaymoney());
//////						trainServiceImpl.insertHistory(order_id, pape.getCpId(), sb_success.toString());
//////					}else{
//////						map.put("order_status", Order.PAYING);
//////						map.put("new_order_status", Order.ARTIFICIAL_PAY);
//////						trainServiceImpl.updateOrderStatus(map);//更新退款状态
//////						StringBuffer sb_success = new StringBuffer();
//////						sb_success.append("车票：").append(pape.getCpId())
//////						.append("支付失败，失败原因为：")
//////						.append(pape.getMsg());
//////						trainServiceImpl.insertHistory(order_id, pape.getCpId(), sb_success.toString());
//////					}
//////				}
////				if (wea_con == 0) {
////					if("00".equals(pape.getStatus()) || "05".equals(pape.getStatus())){
////						//更新车票表改签后信息
////						Map<String,String> alter_map = new HashMap<String,String>();
////						alter_map.put("order_id", order_id);
////						alter_map.put("cp_id",pape.getCpId());
////						alter_map.put("alter_train_no",alter.getChangeTrainNo());
////						alter_map.put("alter_seat_type",TrainConsts.getQunarSeatType().get(pape.getSeattype()));
////						alter_map.put("alter_seat_no",pape.getSeatNo());
////						alter_map.put("alter_train_box",pape.getTrainbox());
////						alter_map.put("alter_pay_money",pape.getPaymoney());
////						alter_map.put("alter_travel_time",alter.getChangeTravelTime());
////						alter_map.put("alter_pay_money",pape.getPaymoney());
////						trainServiceImpl.updateOrderCp(alter_map);
////	
////						map.put("order_status", Order.RESIGNING);
////						map.put("new_order_status", Order.WAITING_CONFIRM);
////						map.put("change_to_time", alterResult.getErrorInfo().get(0).getArrivetime());
////						map.put("bookFlag", "1");
////						trainServiceImpl.updateOrderStatus(map);//更新改签状态
////						
////						StringBuffer sb_success = new StringBuffer();
////						sb_success.append("车票：").append(pape.getCpId())
////						.append("改签成功，改签后车次为：")
////						.append(alter.getChangeTrainNo())
////						.append(";改签后日期：")
////						.append(alter.getChangeTravelTime())
////						.append(";坐席为：")
////						.append(TrainConsts.getSeatType().get(alter.getChangeSeatType()))
////						.append(";改签后票价为：")
////						.append(pape.getPaymoney());
////						logger.info(alter.getOrderId()+sb_success.toString());
////						trainServiceImpl.insertHistory(order_id, null, sb_success.toString());
////					}/*else if("05".equals(pape.getStatus())){
////						repeatPay = true;
////						if(wea_con!=0){
////							//转为人工处理
////							map.put("order_status", Order.RESIGNING);
////							map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////							trainServiceImpl.updateOrderStatus(map);//更新退款状态
////							return;
////						}
////					}*/
////					else if("04".equals(pape.getStatus())){
////						if(pape.getMsg().contains("改签中") || pape.getMsg().contains("改签待支付")){
////							if(wea_con == 0){
////								trainServiceImpl.insertHistory(order_id, null, "改签中或者改签待支付，调用继续支付机器人");
////								repeatPay = true;
////								break;
////							}else{
////								trainServiceImpl.insertHistory(order_id, null, "调用继续支付机器人,支付失败，请人工处理");
////								map.put("order_status", Order.RESIGNING);
////								map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////								map.put("return_optlog", TrainConsts.FAILCODE_NOPAYMENTORDER);
////								trainServiceImpl.updateOrderStatus(map);//更新退款状态
////								return;
////							}
////						}else{
////							if("N".equals(pape.getResign_flag())){
////								alter_fail_cp +="车票号："+pape.getCpId()+pape.getMsg()+"，12306无法改签，请主银出手，么么哒";
////							}else{
////								alter_fail_cp +="车票号："+pape.getCpId()+pape.getMsg()+"改签失败，请主银出手，么么哒";
////							}
////							logger.info(alter_fail_cp);
////							trainServiceImpl.insertHistory(order_id, pape.getCpId(), alter_fail_cp);
////							//转为人工处理
////							map.put("order_status", Order.RESIGNING);
////							map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
////							trainServiceImpl.updateOrderStatus(map);//更新退款状态
////							return;
////						}
////					
////					}
////					
////					if(repeatPay && wea_con == 0){
////						for(ReturnAlterPasEntity pape1 : list_pas){
////							for(OrderCP pas : pas_list){
////								if(pas.getCpId().equals(pape1.getCpId())){
////									pas.setTrainBox((pape1.getTrainbox()));
////									alter.setChangeSeatType((TrainConsts.getQunarSeatType().get(pape1.getSeattype())));
////									pas.setSeatNo(pape1.getSeatNo());
////								}
////							}
////						}
////						
////						/*//重新支付
////						String url = tab.getContinuePayUrl(alter,pas_list,worker.getWorkerExt());
////						String repay_jsonStr = HttpUtil.sendByGet(url, "UTF-8", "30000", "30000");//调用接口
////						repay_jsonStr = repay_jsonStr.replace("\\\"", "\"")
////										.replace("[\"{", "[{")
////										.replace("}\"]", "}]");
////						logger.info(order_id+"；继续支付返回结果："+repay_jsonStr);
////						trainServiceImpl.insertHistory(order_id, cp_id, "调用继续支付机器人！");
////						executeAlterResult(alter,cp_id,pas_list,repay_jsonStr,1);*/
////					}
////				}
////			}
////		}
//	}
}
