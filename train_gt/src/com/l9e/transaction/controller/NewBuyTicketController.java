package com.l9e.transaction.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.l9e.common.ExternalBase;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.OuterSoukdNewData;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;

/**
 * 车票预订
 * @author zuoyuxing
 *
 */
@Component
public class NewBuyTicketController extends ExternalBase { 
	@Resource
	private QueryTicketService ticketService;
	
	/**
	 * 根据车站查询
	 * @param paramMap
	 * @param response
	 */
	public void newQueryData(Map<String, String> paramMap,HttpServletRequest request,HttpServletResponse response){
		String travel_time = paramMap.get("travel_time");
		
		ObjectMapper mapper = new ObjectMapper();
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		StringBuffer param = new StringBuffer();
		try {
			param.append("travel_time=").append(travel_time).append("&from_station=").append(URLEncoder.encode(paramMap.get("from_station"), "utf-8"))
				.append("&arrive_station=").append(URLEncoder.encode(paramMap.get("arrive_station"), "utf-8")).append("&channel=").append("ext");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if("true".equals(paramMap.get("check_spare_num"))){
			param.append("&check_spare_num=").append("true");
		}
		logger.info(travel_time+paramMap.get("from_station")+paramMap.get("arrive_station"));
		try{
			String jsonStr = HttpUtil.sendByPost(getSysInterfaceChannel("query_left_ticket_url"), param.toString(), "utf-8");//调用接口
			if("NO_DATAS".equals(jsonStr)){
				printJson(response, getJson("106").toString());
				return;
			}
			if("ERROR".equals(jsonStr)){
				printJson(response, getJson("001").toString());
				return;
			}
			if("STATION_ERROR".equals(jsonStr)){
				printJson(response, getJson("105").toString());
				return;
			}
			osnd = mapper.readValue(jsonStr, OuterSoukdNewData.class);
		}catch(Exception e){
			logger.error("查询余票异常！", e);
			printJson(response, getJson("106").toString());
			return;
		}
		JSONObject jsonObject = new JSONObject();
		//追加时间限制
		limitTimeTicket(travel_time,paramMap.get("stop_buy_time"),osnd);
		if(osnd!=null && osnd.getDatajson()!=null && osnd.getDatajson().size()>0){
			//转换成标准格式输出
			formatStandardData(jsonObject,osnd,paramMap);
		}else{
			jsonObject.put("return_code", "000");
			jsonObject.put("message", "");
			jsonObject.put("train_data", "");
		}
		if(!StringUtils.isEmpty(paramMap.get("train_code"))){//实时余票验证
			JSONObject json = new JSONObject();
			JSONArray jsonArray = (JSONArray) jsonObject.get("train_data");
			for(int i=0; i<jsonArray.size(); i++){
				if(paramMap.get("train_code").equals(jsonArray.getJSONObject(i).get("train_code"))){
					json = jsonArray.getJSONObject(i);
					json.put("return_code", jsonObject.get("return_code"));
					json.put("message", jsonObject.get("message"));
					json.remove("yz");
					json.remove("wz");
					json.remove("yws");
					json.remove("ywx");
					json.remove("ywz");
					json.remove("rws");
					json.remove("rwx");
					json.remove("tdz");
					json.remove("swz");
					json.remove("rz");
					json.remove("rz1");
					json.remove("rz2");
					json.remove("gws");
					json.remove("gwx");
					break;
				}
			}
			if(json.isEmpty()){
				json.put("return_code", "107");
				json.put("message", "车次信息有误，暂无该列车信息！");
			}
			printJson(response,json.toString());
		}else{
			printJson(response,jsonObject.toString());
		}
	}
	//新接口数据转换成标准格式输出
	public void formatStandardData(JSONObject json,OuterSoukdNewData osnd,Map<String,String> paramMap){
		json.put("return_code", osnd.getCode());
		json.put("message", osnd.getErrInfo());
		JSONArray jsonArr = new JSONArray();
		for(TrainNewData train:osnd.getDatajson()){
//			if("Y".equals(paramMap.get("only_gd"))){
//				String regEx = "G|D|C"; //表示高铁或动车
//				Pattern pat = Pattern.compile(regEx);
//				Matcher mat = pat.matcher(train.getStation_train_code());
//				if(!mat.find()){
//					continue;
//				}
//			}
			String spare_amount = paramMap.get("spare_ticket_amount");
			JSONObject jsonData = new JSONObject();
			jsonData.put("train_code", train.getStation_train_code());
			jsonData.put("start_station", train.getStart_station_name());
			jsonData.put("end_station", train.getEnd_station_name());
			jsonData.put("start_time", "");
			jsonData.put("end_time", "");
			jsonData.put("from_time", train.getStart_time());
			jsonData.put("arrive_time", train.getArrive_time());
			jsonData.put("from_station", train.getFrom_station_name());
			jsonData.put("arrive_station", train.getTo_station_name());
			jsonData.put("cost_time", train.getLishiValue());
			
			jsonData.put("wz",!("-".equals(train.getYz()) || "".equals(train.getYz()))?train.getYz():(
					!("-".equals(train.getZe()) || "".equals(train.getZe()))?train.getZe():train.getRz())
					);
			jsonData.put("yz", train.getYz());
			jsonData.put("wz_num", replaceNumVal(train.getWz_num(),spare_amount));
			jsonData.put("yz_num", replaceNumVal(train.getYz_num(),spare_amount));
			jsonData.put("rz", train.getRz());
			jsonData.put("rz_num", replaceNumVal(train.getRz_num(),spare_amount));
			jsonData.put("rz1", train.getZy());
			jsonData.put("rz1_num", replaceNumVal(train.getZy_num(),spare_amount));
			jsonData.put("rz2", train.getZe());
			jsonData.put("rz2_num", replaceNumVal(train.getZe_num(),spare_amount));
			jsonData.put("yws", train.getYws());
			jsonData.put("ywz", train.getYwz());
			jsonData.put("ywx", train.getYwx());
			jsonData.put("yw_num", replaceNumVal(train.getYw_num(),spare_amount));
			jsonData.put("rws", train.getRws());
			jsonData.put("rwx", train.getRwx());
			jsonData.put("rw_num", replaceNumVal(train.getRw_num(),spare_amount));
			jsonData.put("swz", train.getSwz());
			jsonData.put("swz_num", train.getSwz_num());
			jsonData.put("tdz", train.getTdz());
			jsonData.put("tdz_num", train.getTz_num());
			jsonData.put("gws", train.getGws());
			jsonData.put("gwx", train.getGwx());
			jsonData.put("gw_num", train.getGr_num());
			jsonArr.add(jsonData);
		}
		json.put("train_data", jsonArr.toString());
	}
	
	/**
	 * 新接口数据追加余票预订时间限制
	 * @param paramMap
	 * @param response
	 */
	public void limitTimeTicket(String travel_time,String limit_time,OuterSoukdNewData osnd){
		String num_show = "-";
		String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		//limit_time小时之内的票不能订购
		if(travel_time.equals(nowDate)){
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			int currentTime_HH = Integer.parseInt(DateUtil.dateToString(new Date(), "HH"));
			int currentTime_mm = Integer.parseInt(DateUtil.dateToString(new Date(), "mm"));
			int stopNum=200;
			if(StringUtils.isNotEmpty(limit_time)){
				if(!"0.5".equals(limit_time)){
					stopNum=Integer.valueOf(limit_time)*100;
				}else{
					stopNum=3000;
				}
			}
			if(osnd!=null){
				for (TrainNewData trainNewData : osnd.getDatajson()) {
					int beginTime = Integer.parseInt(trainNewData.getStart_time().replaceAll(":", ""));
					
					if(stopNum == 3000){
						int beginTime_HH = Integer.parseInt(trainNewData.getStart_time().substring(0,2));
						int beginTime_mm = Integer.parseInt(trainNewData.getStart_time().substring(3));
						if((beginTime_HH-currentTime_HH <=0 && beginTime_mm-currentTime_mm<30)
						|| (beginTime_HH-currentTime_HH ==1 && 60-currentTime_mm+beginTime_mm<30)	
						){
							trainNewData.setWz_num(num_show);
							trainNewData.setYz_num(num_show);
							trainNewData.setRz_num(num_show);
							trainNewData.setZy_num(num_show);
							trainNewData.setZe_num(num_show);
							trainNewData.setYw_num(num_show);
							trainNewData.setRw_num(num_show);
							trainNewData.setGr_num(num_show);
							trainNewData.setTz_num(num_show);
							trainNewData.setSwz_num(num_show);
						}
					}else{
						if(beginTime-currentTime < stopNum){
							trainNewData.setWz_num(num_show);
							trainNewData.setYz_num(num_show);
							trainNewData.setRz_num(num_show);
							trainNewData.setZy_num(num_show);
							trainNewData.setZe_num(num_show);
							trainNewData.setYw_num(num_show);
							trainNewData.setRw_num(num_show);
							trainNewData.setGr_num(num_show);
							trainNewData.setTz_num(num_show);
							trainNewData.setSwz_num(num_show);
						}
					}
				}
			}
		}
		if(travel_time.equals(DateUtil.dateAddDays(nowDate,"1"))){
			if("23".compareTo((DateUtil.dateToString(new Date(), "HH")))<=0){
				if(osnd!=null){
					for (TrainNewData trainNewData : osnd.getDatajson()) {
						if(trainNewData.getStart_time().compareTo("09:00")<0){
							trainNewData.setWz_num(num_show);
							trainNewData.setYz_num(num_show);
							trainNewData.setRz_num(num_show);
							trainNewData.setZy_num(num_show);
							trainNewData.setZe_num(num_show);
							trainNewData.setYw_num(num_show);
							trainNewData.setRw_num(num_show);
							trainNewData.setGr_num(num_show);
							trainNewData.setTz_num(num_show);
							trainNewData.setSwz_num(num_show);
						}
					}
				}
			}
		}
	}
	
	//网站终止预订功能
	//进行车次查询
	public void unableBookTicketsQuery(Map<String, String> paramMap,HttpServletRequest request,HttpServletResponse response){
		List<TrainNewDataFakeAppendTrain> list = ticketService.queryAppendTrainNewData(paramMap);
		for(TrainNewDataFakeAppendTrain appTrain : list){
			appTrain.changeData();
		}
		unableStandardData(list,response,paramMap.get("only_gd"),paramMap.get("terminal"));
	}
	
	public void unableStandardData(List<TrainNewDataFakeAppendTrain> list,HttpServletResponse response,
			String only_gd, String terminal){
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		for(TrainNewDataFakeAppendTrain train : list){
			if("Y".equals(only_gd)){
				String regEx = "G|D|C"; //表示高铁或动车
				Pattern pat = Pattern.compile(regEx);
				Matcher mat = pat.matcher(train.getCc());
				if(!mat.find()){
					continue;
				}
			}
			JSONObject jsonData = new JSONObject();
			jsonData.put("train_code", train.getCc());
			jsonData.put("start_time", "");
			jsonData.put("end_time", "");
			jsonData.put("start_station", train.getStart_station_name());
			jsonData.put("end_station", train.getEnd_station_name());
			jsonData.put("from_time", train.getStart_time());
			jsonData.put("arrive_time", train.getArrive_time());
			jsonData.put("from_station", train.getFz());
			jsonData.put("arrive_station", train.getDz());
			jsonData.put("cost_time", train.getLishi());
			jsonData.put("wz", ("-".equals(train.getYz()) || "".equals(train.getYz())) ? train.getRz2(): train.getYz());
			jsonData.put("yz", train.getYz());
			jsonData.put("wz_num", "-");
			jsonData.put("yz_num", "-");
			jsonData.put("rz", train.getRz());
			jsonData.put("rz_num", "-");
			jsonData.put("rz1", train.getRz1());
			jsonData.put("rz1_num", "-");
			jsonData.put("rz2", train.getRz2());
			jsonData.put("rz2_num", "-");
			jsonData.put("yws", train.getYws());
			jsonData.put("ywz", train.getYwz());
			jsonData.put("ywx", train.getYwx());
			jsonData.put("yw_num", "-");
			jsonData.put("rws", train.getRws());
			jsonData.put("rwx", train.getRwx());
			jsonData.put("rw_num", "-");
			jsonData.put("swz", "-");
			jsonData.put("swz_num", "-");
			jsonData.put("tdz", "-");
			jsonData.put("tdz_num", "-");
			jsonData.put("gws", "-");
			jsonData.put("gwx", "-");
			jsonData.put("gw_num", "-");
			if("APP".equals(terminal)){
				jsonData.put("total_num", "无");
			}
			jsonArr.add(jsonData);
		}
		jsonObject.put("return_code","000");
		jsonObject.put("message", "");
		jsonObject.put("train_data", jsonArr.toString());
		printJson(response, jsonObject.toString());
		return;
	}
	
	public static void main(String[] args) {
		
		StringBuffer param = new StringBuffer();
		try {
			param.append("travel_time=").append("2015-09-31").append("&from_station=").append(URLEncoder.encode("北京", "utf-8"))
				.append("&arrive_station=").append(URLEncoder.encode("天津", "utf-8")).append("&channel=").append("ext");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
//		if("true".equals(paramMap.get("check_spare_num"))){
			param.append("&check_spare_num=").append("true");
//		}
		try{
			String jsonStr = HttpUtil.sendByPost("http://192.168.63.245:37055/queryTicket", param.toString(), "utf-8");//调用接口
			logger.info(jsonStr);
		}catch (Exception e) {
			// TODO: handle exception
			logger.info(e);
		}
	}
}
