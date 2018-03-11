package com.l9e.transaction.controller;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.l9e.common.ExternalBase;
import com.l9e.transaction.vo.OuterSoukdData;
import com.l9e.transaction.vo.TrainData;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.XmlUtil;

/**
 * 车票预订
 * @author zuoyuxing
 *
 */
@Component
public class SoukdBuyTicketController extends ExternalBase { 
	
	//soukd接口站站查询
	public void soukdQueryData(Map<String, String> paramMap,HttpServletRequest request,HttpServletResponse response ){
		long log_begin_time = System.currentTimeMillis();//日志查询开始时间
		OuterSoukdData otVo = new OuterSoukdData();
		JSONObject errReturn = new JSONObject();
		
		otVo = getDataFromInterface(paramMap);
		if("1".equals(otVo.getCode())){
			otVo.setCode("000");
		}
		if(otVo.getDataList() == null || otVo.getDataList().size()==0){
			if("000".equals(otVo.getCode()) && "0".equals(otVo.getIcount())){
				errReturn.put("return_code", "106");
				errReturn.put("message", "查询返回数据失败，暂无列车信息.");
				printJson(response, errReturn.toString());
				return;
			}else{
				if(!"000".equals(otVo.getCode())){
					errReturn.put("return_code", "101");
				}
				errReturn.put("message", otVo.getErrInfo());
				printJson(response, errReturn.toString());
				return;
			}
		}
		JSONObject jsonObject = new JSONObject();
		if(otVo!=null && otVo.getDataList()!=null && otVo.getDataList().size()>0){
			//追加时间限制
			limitTimeTicket(paramMap.get("travel_time"),paramMap.get("stop_buy_time"),otVo);
			formatStandardData(paramMap,jsonObject,otVo);
		}
		
		if(!StringUtils.isEmpty(paramMap.get("train_code"))){//实时余票验证
			JSONObject json = new JSONObject();
			JSONArray jsonArray = (JSONArray) jsonObject.get("trainData");
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
			otVo = null;
			if(json.isEmpty()){
				json.put("return_code", "107");
				json.put("message", "车次信息有误，暂无该列车信息！");
			}
			printJson(response,json.toString());
		}else{
			otVo = null;
			printJson(response,jsonObject.toString());
			logger.info("<火车票查询>总计查询"
					+paramMap.get("from_station")+"/"+paramMap.get("arrive_station")
					+"("+paramMap.get("travel_time")+")的列车信息，耗时" + (System.currentTimeMillis() - log_begin_time)+ "ms");
		}
	}
	
	public void formatStandardData(Map<String, String> paramMap,JSONObject json, OuterSoukdData otVo){
		json.put("return_code", otVo.getCode());
		json.put("message", otVo.getErrInfo());
		JSONArray jsonArr = new JSONArray();
		for(TrainData train:otVo.getDataList()){
			if("Y".equals(paramMap.get("only_gd"))){
				String regEx = "G|D|C"; //表示高铁或动车
				Pattern pat = Pattern.compile(regEx);
				Matcher mat = pat.matcher(train.getTrainCode());
				if(!mat.find()){
					continue;
				}
			}
			String spare_amount = paramMap.get("spare_ticket_amount");
			JSONObject jsonData = new JSONObject();
			jsonData.put("train_code", train.getTrainCode());
			jsonData.put("start_station", "");
			jsonData.put("end_station", "");
			jsonData.put("start_time", "");
			jsonData.put("end_time", "");
			jsonData.put("from_time", train.getStartTime());
			jsonData.put("arrive_time", train.getEndTime());
			jsonData.put("from_station", train.getStartCity());
			jsonData.put("arrive_station", train.getEndCity());
			jsonData.put("cost_time", train.getCostTime());
			jsonData.put("wz", ("-".equals(train.getYz()) || "".equals(train.getYz())) ? train.getRz2(): train.getYz());
			jsonData.put("yz", train.getYz());
			jsonData.put("wz_num", replaceNumVal(train.getWz_yp(),spare_amount));
			jsonData.put("yz_num", replaceNumVal(train.getYz_yp(),spare_amount));
			jsonData.put("rz", train.getRz());
			jsonData.put("rz_num", replaceNumVal(train.getRz_yp(),spare_amount));
			jsonData.put("rz1", train.getRz1());
			jsonData.put("rz1_num", replaceNumVal(train.getRz1_yp(),spare_amount));
			jsonData.put("rz2", train.getRz2());
			jsonData.put("rz2_num", replaceNumVal(train.getRz2_yp(),spare_amount));
			jsonData.put("yws", train.getYws());
			jsonData.put("ywz", train.getYwz());
			jsonData.put("ywx", train.getYwx());
			jsonData.put("yw_num", replaceNumVal(train.getYw_yp(),spare_amount));
			jsonData.put("rws", train.getRws());
			jsonData.put("rwx", train.getRwx());
			jsonData.put("rw_num", replaceNumVal(train.getRw_yp(),spare_amount));
			jsonData.put("swz", train.getSwz());
			jsonData.put("swz_num", train.getSwz_yp());
			jsonData.put("tdz", train.getTdz());
			jsonData.put("tdz_num", train.getTdz_yp());
			jsonData.put("gws", train.getGws());
			jsonData.put("gwx", train.getGwx());
			jsonData.put("gw_num", train.getGw_yp());
			jsonArr.add(jsonData);
		}
		json.put("train_data", jsonArr.toString());
	}
	/**
	 * 追加余票时间限制
	 * @param travel_time
	 */
	public void limitTimeTicket(String travel_time,String limit_time,OuterSoukdData otVo){
		String yp_show = "-";
		String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		//6小时之内的票不能订购
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
			if(otVo!=null){
				for (TrainData trainData : otVo.getDataList()) {
					int beginTime = Integer.parseInt(trainData.getStartTime().replaceAll(":", ""));
					
					if(stopNum == 3000){
						int beginTime_HH = Integer.parseInt(trainData.getStartTime().substring(0,2));
						int beginTime_mm = Integer.parseInt(trainData.getStartTime().substring(3));
						if((beginTime_HH-currentTime_HH <=0 && beginTime_mm-currentTime_mm<30)
						|| (beginTime_HH-currentTime_HH ==1 && 60-currentTime_mm+beginTime_mm<30)	
						){
							trainData.setWz_yp(yp_show);
							trainData.setYz_yp(yp_show);
							trainData.setRz1_yp(yp_show);
							trainData.setRz1_yp(yp_show);
							trainData.setRz2_yp(yp_show);
							trainData.setYw_yp(yp_show);
							trainData.setRw_yp(yp_show);
							trainData.setGw_yp(yp_show);
							trainData.setTdz_yp(yp_show);
							trainData.setSwz_yp(yp_show);
						}
					}else{
						if(beginTime-currentTime < stopNum){
							trainData.setWz_yp(yp_show);
							trainData.setYz_yp(yp_show);
							trainData.setRz1_yp(yp_show);
							trainData.setRz1_yp(yp_show);
							trainData.setRz2_yp(yp_show);
							trainData.setYw_yp(yp_show);
							trainData.setRw_yp(yp_show);
							trainData.setGw_yp(yp_show);
							trainData.setTdz_yp(yp_show);
							trainData.setSwz_yp(yp_show);
						}
					}
				}
			}
		}
		if(travel_time.equals(DateUtil.dateAddDays(nowDate,"1"))){
			if("23".compareTo((DateUtil.dateToString(new Date(), "HH")))<=0){
				if(otVo!=null){
					for (TrainData trainData : otVo.getDataList()) {
						if(trainData.getStartTime().compareTo("09:00")<0){
							trainData.setWz_yp(yp_show);
							trainData.setYz_yp(yp_show);
							trainData.setRz1_yp(yp_show);
							trainData.setRz1_yp(yp_show);
							trainData.setRz2_yp(yp_show);
							trainData.setYw_yp(yp_show);
							trainData.setRw_yp(yp_show);
							trainData.setGw_yp(yp_show);
							trainData.setTdz_yp(yp_show);
							trainData.setSwz_yp(yp_show);
						}
					}
				}
			}
		}
	}
	/**
	 * soukd
	 * 查询车票信息
	 * 
	 * @param paramMap
	 * @param interfaceName
	 * @param interfaceUrl
	 */
	public OuterSoukdData getDataFromInterface(Map<String, String> paramMap){
		OuterSoukdData otVo = new OuterSoukdData();
		long start = System.currentTimeMillis();
		StringBuffer param = new StringBuffer();
		param.append("travel_time=").append(paramMap.get("travel_time")).append("&from_station=").append(paramMap.get("from_station"))
		.append("&arrive_station=").append(paramMap.get("arrive_station")).append("&channel=").append("ext");
		try{
			String xmlStr = HttpPostUtil.sendAndRecive(soukd_query_left_ticket_url,param.toString());//调用接口
			otVo = XmlUtil.toBean(xmlStr, OuterSoukdData.class);
			if(otVo != null){
				logger.info("<火车票查询>调用SOUKD接口成功查询"
						+paramMap.get("from_station")+"/"+paramMap.get("arrive_station")
						+"("+paramMap.get("travel_time")+")的列车信息，耗时" + (System.currentTimeMillis() - start) + "ms");
				
				//统计列车列数
				if(otVo.getDataList() == null || otVo.getDataList().size()==0){
					logger.info("[火车票查询"
							+paramMap.get("from_station")+"/"+paramMap.get("arrive_station")
							+"("+paramMap.get("travel_time")+")的列车共计0列");
				}else{
					//解决xstream bug
					ObjectMapper mapper = new ObjectMapper();
					otVo = mapper.readValue(mapper.writeValueAsString(otVo), OuterSoukdData.class);
					
					logger.info("[火车票查询"
							+paramMap.get("from_station")+"/"+paramMap.get("arrive_station")
							+"("+paramMap.get("travel_time")+")的列车共计"+otVo.getDataList().size()+"列");
				}
			}
		}catch(Exception e){//没有查询到数据
			logger.error("解析SOUKD接口返回数据异常", e);
			otVo.setCode("001");
			otVo.setErrInfo("系统错误，未知服务异常。");
		}
		return otVo;
	}
}
