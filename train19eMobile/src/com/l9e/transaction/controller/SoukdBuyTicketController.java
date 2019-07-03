package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.l9e.common.ExternalBase;
import com.l9e.transaction.vo.ExternalInterfaceVo;
import com.l9e.transaction.vo.ExternalTrainData;
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
	
	//第三方接口站站查询
	public void soukdQueryData(Map<String, String> paramMap,HttpServletRequest request,HttpServletResponse response ){
		long log_begin_time = System.currentTimeMillis();//日志查询开始时间
		ExternalInterfaceVo foreignInterfaceVo = new ExternalInterfaceVo();
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
		if(otVo!=null && otVo.getDataList()!=null && otVo.getDataList().size()>0){
			//追加时间限制
			limitTimeTicket(paramMap.get("travel_time"),otVo);
			String speed_only = this.getParam(request, "speed_only");//只搜高铁动车
			formatStandardData(foreignInterfaceVo,otVo,speed_only);
		}
		foreignInterfaceVo.setLeft_limit(this.getLeftTikectLimit());//余票阀值
		
		JSONObject jsonObject = JSONObject.fromObject(foreignInterfaceVo);
		if(!StringUtils.isEmpty(paramMap.get("train_code"))){//实时余票验证
			JSONObject json = new JSONObject();
			JSONArray jsonArray = (JSONArray) jsonObject.get("trainData");
			for(int i=0; i<jsonArray.size(); i++){
				if(paramMap.get("train_code").equals(jsonArray.getJSONObject(i).get("train_code"))){
					json = jsonArray.getJSONObject(i);
					json.put("return_code", foreignInterfaceVo.getReturn_code());
					json.put("message", foreignInterfaceVo.getMessage());
					break;
				}
			}
			otVo = null;
			printJson(response,json.toString());
		}else{
			otVo = null;
			printJson(response,jsonObject.toString());
			logger.info("<火车票查询>总计查询"
					+paramMap.get("from_station")+"/"+paramMap.get("arrive_station")
					+"("+paramMap.get("travel_time")+")的列车信息，耗时" + (System.currentTimeMillis() - log_begin_time)+ "ms");
//			queryTimeMillis(request, paramMap, log_begin_time);
		}
	}
	
	public void formatStandardData(ExternalInterfaceVo foreignInterfaceVo, OuterSoukdData otVo, String speed_only){
		foreignInterfaceVo.setReturn_code("SUCCESS");
		foreignInterfaceVo.setMessage("查询车票信息成功！");
		List<ExternalTrainData> list = new ArrayList<ExternalTrainData>();
		ExternalTrainData data = null;
		String train_no = null;
		for(TrainData train:otVo.getDataList()){
			train_no = train.getTrainCode();
			if(StringUtils.isNotEmpty(speed_only) && "1".equals(speed_only)){
				if(StringUtils.isEmpty(train_no)){
					continue;
				}else if(!train_no.startsWith("D") && !train_no.startsWith("G") && !train_no.startsWith("C")){
					continue;
				}
			}
			
			data = new ExternalTrainData();
			data.setTrain_code(train.getTrainCode());
//			data.setStart_station(train.getStart_station_name());
//			data.setEnd_station(train.getEnd_station_name());
//			data.setFrom_time(train.getStart_time());
//			data.setArrive_time(train.getArrive_time());
			data.setFrom_station(train.getStartCity());
			data.setArrive_station(train.getEndCity());
			data.setCost_time(train.getCostTime());
			data.setWz(train.getYz());
			data.setYz(train.getYz());
			data.setWz_num(train.getWz_yp());
			data.setYz_num(train.getYz_yp());
			data.setRz(train.getRz());
			data.setRz_num(train.getRz_yp());
			data.setRz1(train.getRz1());
			data.setRz1_num(train.getRz1_yp());
			data.setRz2(train.getRz2());
			data.setRz2_num(train.getRz2_yp());
			data.setYws(train.getYws());
			data.setYwz(train.getYwz());
			data.setYwx(train.getYwx());
			data.setYw_num(train.getYw_yp());
			data.setRws(train.getRws());
			data.setRwx(train.getRwx());
			data.setRw_num(train.getRw_yp());
			data.setSwz(train.getSwz());
			data.setSwz_num(train.getSwz_yp());
			data.setTdz(train.getTdz());
			data.setTdz_num(train.getTdz_yp());
			data.setGws(train.getGws());
			data.setGwx(train.getGwx());
			data.setGw_num(train.getGw_yp());
			list.add(data);
		}
		foreignInterfaceVo.setTrainData(list);
	}
	/**
	 * 追加余票时间限制
	 * @param travel_time
	 */
	public void limitTimeTicket(String travel_time,OuterSoukdData otVo){
		String yp_show = "-";
		String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		//查询系统配置的停止购票时间
		String stopBuyTicketTime=commonService.querySysStopTime();
		logger.info("停止购票时间为："+stopBuyTicketTime);
		int stopTime=0;
		int stopNum=600;
		if(StringUtils.isNotEmpty(stopBuyTicketTime)){
			stopTime=Integer.valueOf(stopBuyTicketTime);
		}
		if(stopTime>=3 && stopTime<=10){
			stopNum=stopTime*100;
		}
		//stopTime小时之内的票不能订购
		if(travel_time.equals(nowDate)){
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			if(otVo!=null){
				for (TrainData trainData : otVo.getDataList()) {
					int beginTime = Integer.parseInt(trainData.getStartTime().replaceAll(":", ""));
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
	 * 第三方接口
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
		.append("&arrive_station=").append(paramMap.get("arrive_station")).append("&channel=").append("19e");
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
