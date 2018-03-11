package com.l9e.util.hcpjar;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.util.hcpjar.bean.QueryTicketNumBackBean;
import com.l9e.util.hcpjar.bean.TrainBean;
import com.l9e.util.hcpjar.util.DateUtil;
import com.l9e.util.hcpjar.util.HttpsUtil;
/**
 * 待开发：
 * 添加外部加载文件
 * 调整train-js HTTPS下载读取方式(套接字 256字符长度读取)
 * 添加站站信息数据扒取 数据库方式存储
 * 
 * 12306 相关接口
 * @author liuyi02
 * 
 * */
public class HcpTicketQuery{
	//String station_names=HttpsUtil.sendHttps("https://kyfw.12306.cn/otn/resources/js/framework/station_name.js");
	private static String QUERY_STATION_NAMES="https://kyfw.12306.cn/otn/resources/js/framework/station_name.js";
	/**列车信息*/
	private static String QUERY="https://kyfw.12306.cn/otn/lcxxcx/query";
	/**票价查询*/
	private static String QUERY_TICKET_PRICE="https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice";
	/**站站信息*/
	private static String QUERY_STATIONS_INFO="https://kyfw.12306.cn/otn/czxx/queryByTrainNo";
	
	/**成人票*/
	private static String PURPOSE_CODES_ADULT="ADULT";
	/**学生票*/
	private static String PURPOSE_CODES_STUDENT="0X00";
	
	/**车站-编号信息*/
	public static Map<String,String> stationName=null;
	
	/**外部加载 车站-编号信息*/
	public static Map<String,String> outUploadStationName=null;
	static{
		/**初始化本地车站-编号信息*/
		stationName=new HashMap<String,String>();
		try {
			BufferedInputStream bufferedInputStream=new BufferedInputStream(HcpTicketQuery.class.getResource("/station_name.js").openStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(bufferedInputStream,"utf-8"));
			int b; 
			String stationNamesStr="";
			while((b=in.read())!=-1){
				stationNamesStr=stationNamesStr+in.readLine(); 
			}
			int len = bufferedInputStream.available();
			byte[] bytes=new byte[len];
		    int r=bufferedInputStream.read(bytes);
		    String[] stationNamesArr=stationNamesStr.split("@");
		    int size=stationNamesArr.length;
		    stationName=new HashMap<String,String>(size);
		    for(int i=1;i<size;i++){
		    	String[] keyValue=stationNamesArr[i].split("\\|");
		    	stationName.put(keyValue[1], keyValue[2]);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**外部加载车站-编号文件信息*/
	public static void uploadStationNameFile(File stationNameFile){
		outUploadStationName=null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(stationNameFile));
			int b; 
			String stationNamesStr="";
			while((b=in.read())!=-1){
				stationNamesStr=stationNamesStr+in.readLine(); 
			} String[] stationNamesArr=stationNamesStr.split("@");
		    int size=stationNamesArr.length;
		    outUploadStationName=new HashMap<String,String>(size);
		    for(int i=1;i<size;i++){
		    	String[] keyValue=stationNamesArr[i].split("\\|");
		    	outUploadStationName.put(keyValue[1], keyValue[2]);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 	车次信息查询
	 * @param fromStationName
	 * @param toStationName
	 * */
	public static String queryTicketNumByName(String fromStationName,String toStationName){
		return queryTicketNumByName(fromStationName,toStationName,null,PURPOSE_CODES_ADULT);
	}
	
	/**
	 * 	车次信息查询
	 * @param fromStationName
	 * @param toStationName
	 * @param queryDate
	 * */
	public static String queryTicketNumByName(String fromStationName,String toStationName,String queryDate){
		return queryTicketNumByName(fromStationName,toStationName,queryDate,PURPOSE_CODES_ADULT);
	}
	/**
	 * 	车次信息查询
	 * @param fromStationName
	 * @param toStationName
	 * @param queryDate
	 * @param purposeCodes
	 * */
	public static String queryTicketNumByName(String fromStationName,String toStationName,String queryDate,String purposeCodes){
		String fromStationId=null;
		String toStationId=null;
		if(outUploadStationName!=null){
			fromStationId=getValueFromStationNameOutUpload(fromStationName);
			toStationId=getValueFromStationNameOutUpload(toStationName);
		}else{
			fromStationId=getValueFromStationNameOnSys(fromStationName);
			toStationId=getValueFromStationNameOnSys(toStationName);
		}
		if(fromStationId==null&&toStationId==null){
			fromStationId=getValueFromStationNameOnTime(fromStationName);
			toStationId=getValueFromStationNameOnTime(toStationName);
		}
		if(fromStationId!=null&&toStationId!=null){
			return queryTicketNumById(fromStationId,toStationId,queryDate,purposeCodes==null?PURPOSE_CODES_ADULT:purposeCodes);
		}else{
			//返回错误车站
			return "{\"validateMessagesShowId\":\"_validatorMessage\",\"status\":true,\"httpstatus\":200,\"data\":{\"message\":\"错误车站！\",\"flag\":false},\"messages\":[],\"validateMessages\":{}}";
		}
	}
	
	/**
	 * 	车次票价查询 默认
	 * @param fromStationName
	 * @param toStationName
	 * @param trainNo
	 * */
	public static String queryTicketPrice(String fromStationName,String toStationName,String trainNo){
		return queryTicketPrice(fromStationName,toStationName,PURPOSE_CODES_ADULT,trainNo,null);
	}
	
	/**
	 * 	车次票价查询 默认
	 * @param fromStationName
	 * @param toStationName
	 * @param trainNo
	 * @param trainDate
	 * */
	public static String queryTicketPrice(String fromStationName,String toStationName,String trainNo,String trainDate){
		return queryTicketPrice(fromStationName,toStationName,PURPOSE_CODES_ADULT,trainNo,trainDate);
	}
	
	/**
	 * 	车次票价查询 
	 * @param fromStationName
	 * @param toStationName
	 * @param purposeCodes
	 * @param trainNo
	 * @param trainDate
	 * */
	public static String queryTicketPrice(String fromStationName,String toStationName,String purposeCodes,String trainNo,String trainDate){
		TrainBean bean=findTrainBeanByTrainNo(queryTicketNumByName(fromStationName,toStationName,trainDate,purposeCodes),trainNo);
		if(bean!=null){
			String url=QUERY_TICKET_PRICE+"?train_no="+bean.getTrain_no()+
			"&from_station_no="+bean.getFrom_station_no()+
			"&to_station_no="+bean.getTo_station_no()+
			"&seat_types="+bean.getSeat_types()+
			"&train_date="+(trainDate==null?DateUtil.dateToString(new Date(),"yyyy-MM-dd"):trainDate);
			return HttpsUtil.sendHttps(url);
		}else{
			return "";
		}
	}
	
	/**
	 * 外部加载文件 查询车站编号
	 * @param name
	 * @return String
	 * */
	public static String getValueFromStationNameOutUpload(String name){
		return getValueFromStationName(outUploadStationName,name);
	}
	
	/**
	 * 外部加载文件 查询车站编号
	 * @param name
	 * @param stationNameFile
	 * @return String
	 * */
	public static String getValueFromStationNameOutUpload(String name,File stationNameFile){
		if(outUploadStationName==null){
			 uploadStationNameFile(stationNameFile);
		}
		return getValueFromStationName(outUploadStationName,name);
	}
	/**
	 * 本地查询车站编号
	 * @param name
	 * @return String
	 * */
	public static String getValueFromStationNameOnSys(String name){
		return getValueFromStationName(stationName,name);
	}
	/**
	 * 线上查询车站编号
	 * @param name
	 * @return String
	 * */
	public static String getValueFromStationNameOnTime(String name){
		return getValueFromStationName(findStaticNameMap(),name);
	}
	private static String getValueFromStationName(Map<String,String>stationName,String name){
		return stationName.get(name);
	}
	
	private static String queryTicketNumById(String fromStationId,String toStationId,String queryDate,String purposeCodes){
		
		//System.out.println(QUERY+"?purpose_codes="+purposeCodes+"&queryDate="+(queryDate==null?DateUtil.dateToString(new Date(),"yyyy-MM-dd"):queryDate)+"&from_station="+fromStationId+"&to_station="+toStationId);
		String queryInfo =HttpsUtil.sendHttps(QUERY+"?purpose_codes="+purposeCodes+"&queryDate="+(queryDate==null?DateUtil.dateToString(new Date(),"yyyy-MM-dd"):queryDate)+"&from_station="+fromStationId+"&to_station="+toStationId);
		//return HttpsUtil.sendHttps(QUERY+"?purpose_codes="+purposeCodes+"&queryDate="+(queryDate==null?DateUtil.dateToString(new Date(),"yyyy-MM-dd"):queryDate)+"&from_station="+fromStationId+"&to_station="+toStationId);
		//System.out.println(queryInfo);
		return queryInfo;
	}
	
	/**
	 * 	匹配某车次信息
	 * @param ticketNumInfo
	 * @param trainNo
	 * @return TrainBean
	 * */
	private static TrainBean findTrainBeanByTrainNo(String ticketNumInfo,String trainNo){
		
		//System.out.println("ticketNumInfo"+ticketNumInfo);
		ObjectMapper mapper = new ObjectMapper();
		QueryTicketNumBackBean bean = null;
		try {
			bean = mapper.readValue(ticketNumInfo, QueryTicketNumBackBean.class);
			TrainBean[] trainBeans=bean.getData().getDatas();
			for(TrainBean trainBean: trainBeans){
				if(trainBean.getStation_train_code().equals(trainNo)){
					return trainBean;
				}
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**线上实时查询*/
	private static Map<String,String> findStaticNameMap(){
		String stationNamesStr=HttpsUtil.sendHttps(QUERY_STATION_NAMES);
		String[] stationNamesArr=stationNamesStr.split("@");
	    int size=stationNamesArr.length;
	    Map<String,String>  stationName=new HashMap<String,String>(size);
	    for(int i=1;i<size;i++){
	    	String[] keyValue=stationNamesArr[i].split("\\|");
	    	stationName.put(keyValue[1], keyValue[2]);
	    }
	    return stationName;
	}
	
	public static void main(String[] args){
		System.setProperty("http.proxyHost", "192.168.65.126");
		System.setProperty("http.proxyPort", "3128");
		
		
		
		/**余票查询*/
		//System.out.println(HcpTicketQuery.queryTicketNumByName("北京", "上海"));
		/**票价查询*/
		System.out.println(HcpTicketQuery.queryTicketPrice("北京", "上海","G139"));
	//	System.out.println(HttpsUtil.sendHttps("https://kyfw.12306.cn/otn/resources/js/query/train_list.js"));
	}
	
}
