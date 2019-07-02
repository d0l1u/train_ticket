package com.l9e.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.common.BaseServlet;
import com.l9e.common.TrainConsts;
import com.l9e.service.RobotServiceImp;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.vo.OuterSoukdNewData;
import com.l9e.vo.RobotSetVo;
import com.l9e.vo.TcTicketPriceVo;
import com.l9e.vo.TcTicketVo;
import com.l9e.vo.TrainNewData;
import com.l9e.vo.TrainNewDataFake;
import com.l9e.vo.TrainNewMidData;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class TcQueryControll {
private static final long serialVersionUID = 3264299111876887524L;
	
	RobotServiceImp robotServiceImp=new RobotServiceImp();
	private static Logger logger=Logger.getLogger(TcQueryControll.class);

	public String javaQueryData(Map<String,String> param,RobotSetVo robot) throws Exception{
		try{
			if(null == robot){
				return TrainConsts.NO_ROBOT;
			}
			List<TcTicketVo> tc=new ArrayList<TcTicketVo>();
			StringBuffer params = new StringBuffer();
			
			String from_station=param.get("from_station");
			String arrive_station=param.get("arrive_station");
			
			String ZW=param.get("isNotZW");
			logger.info("同程java query param:"+from_station+"_"+arrive_station+"_"+ZW);
			if("yes".equalsIgnoreCase(ZW)){//中文请求
				 from_station=BaseServlet.stationName.get(from_station);
				 arrive_station=BaseServlet.stationName.get(arrive_station);
			}
			if(null==from_station){
				logger.info("station is error: from_station:"+URLDecoder.decode(param.get("from_station"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			if(null==arrive_station){
				logger.info("station is error: arrive_station:"+URLDecoder.decode(param.get("arrive_station"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			params.append("travel_time=").append(param.get("travel_time")).append("&from_station=")
			.append(from_station).append("&arrive_station=").append(arrive_station).append("&purpose_codes=").append(param.get("purpose_codes"));
			
			logger.info("同程java query url:"+robot.getRobot_url() + "?" + URLDecoder.decode(params.toString(),"utf-8"));
			String jsonStr = HttpUtil.sendByPost(robot.getRobot_url(),params.toString(), "UTF-8");//调用接口
			logger.info("同程余票查询java返回结果 :"+jsonStr);
			if(TrainConsts.ERROR.equals(jsonStr) || jsonStr.equals(TrainConsts.NO_DATAS)){
				return jsonStr;
			}
			ObjectMapper mapper = new ObjectMapper();
			TrainNewMidData train_data = mapper.readValue(jsonStr.toString(), TrainNewMidData.class);
			if(train_data!=null && train_data.getDatas().size()>0){
				tc=getListTcTicketVo(train_data.getDatas());
				if(train_data.getDatas().size()>0){
					return JSONArray.fromObject(tc).toString();
				}else{
					return TrainConsts.ERROR;
				}
			}else{
				return TrainConsts.ERROR;
			}
		}catch(Exception e){
			logger.info("java程序查询余票异常！"+e);
			return TrainConsts.ERROR;
		}	
	}
	
	private List<TcTicketVo> getListTcTicketVo(List<TrainNewData> datas) {
		List<TcTicketVo> tc=new ArrayList<TcTicketVo>();
		for(TrainNewData data:datas ){
			TcTicketVo vo=new TcTicketVo();
			vo.setCan_buy_now(data.getCanWebBuy());
			vo.setArrive_days(data.getDay_difference());
			vo.setTrain_start_date(data.getStart_train_date());
			vo.setTrain_code(data.getStation_train_code());
			vo.setAccess_byidcard(data.getIs_support_card());
			vo.setTrain_no(data.getTrain_no());
			//getTrainType(data.getTrain_class_name());
			vo.setTrain_type("");
			vo.setFrom_station_name(data.getFrom_station_name());
			vo.setFrom_station_code(data.getFrom_station_telecode());
			vo.setTo_station_name(data.getTo_station_name());
			vo.setTo_station_code(data.getTo_station_telecode());
			vo.setStart_station_name(data.getStart_station_name());
			vo.setEnd_station_name(data.getEnd_station_name());
			vo.setStart_time(data.getStart_time());
			vo.setArrive_time(data.getArrive_time());
			vo.setRun_time(data.getLishi());
			vo.setRun_time_minute(data.getLishiValue());
			vo.setGjrw_num(data.getGr_num());
			vo.setQtxb_num(data.getQt_num());
			vo.setRw_num(data.getRw_num());
			vo.setRz_num(data.getRz_num());
			vo.setSwz_num(data.getSwz_num());
			vo.setTdz_num(data.getTdz_num());
			vo.setWz_num(data.getWz_num());
			vo.setYw_num(data.getYw_num());
			vo.setYz_num(data.getYz_num());
			vo.setEdz_num(data.getZe_num());
			vo.setYdz_num(data.getZy_num());
			vo.setNote(data.getNote().replaceAll("<br/>", ""));
			/*
			类型代码	对应车型
			D	动车组
			KT	空调特快
			KKS	空调快速
			KPK	空调普快
			KPM	空调普慢
			KS	快速
			PK	普快
			PM	普慢
			C	城际高速
			GD	高速动车
			XGZ	香港直通车
			Z	直达特快*/
			tc.add(vo);
		}
		return tc;
	}

	private String getTrainType(String trainClassName) {
		if(trainClassName==null||"".equals(trainClassName)){
			return "";
		}
		return "";
			// TODO Auto-generated method stub
		
	}

	public String javaQueryNoPriceData(Map<String,String> param,RobotSetVo robot,boolean cache_file) throws Exception{
		try{
			if(null == robot){
				return TrainConsts.NO_ROBOT;
			}
			OuterSoukdNewData osnd = new OuterSoukdNewData();
			StringBuffer params = new StringBuffer();
			
			String from_station=BaseServlet.stationName.get(param.get("from_station"));
			String arrive_station=BaseServlet.stationName.get(param.get("arrive_station"));
			params.append("travel_time=").append(param.get("travel_time")).append("&from_station=")
			.append(from_station).append("&arrive_station=").append(arrive_station);
			logger.info("java program query :"+params.toString());
			String jsonStr = HttpUtil.sendByPost(robot.getRobot_url(),params.toString(), "UTF-8");//调用接口
			if(TrainConsts.ERROR.equals(jsonStr) || jsonStr.equals(TrainConsts.NO_DATAS)){
				return jsonStr;
			}
			ObjectMapper mapper = new ObjectMapper();
			TrainNewMidData train_data = mapper.readValue(jsonStr.toString(), TrainNewMidData.class);
			
			if(train_data!=null && train_data.getDatas().size()>0){
				osnd.setCode("000");
				osnd.setDatajson(train_data.getDatas());
				return JSONObject.fromObject(osnd).toString();
			}else{
				return TrainConsts.ERROR;
			}
		}catch(Exception e){
			logger.info("java程序查询余票异常！",e);
			return TrainConsts.ERROR;
		}	
	}
	
	
	
	public String javaQueryDataAndPrice(Map<String,String> param,RobotSetVo robot) throws Exception{
		try{
			if(null == robot){
				return TrainConsts.NO_ROBOT;
			}
			//OuterSoukdNewData osnd = new OuterSoukdNewData();
			StringBuffer params = new StringBuffer();
			
			String from_station=param.get("from_station");
			String arrive_station=param.get("arrive_station");
			
			
			
			if(null==from_station){
				logger.info("station is error: from_station:"+URLDecoder.decode(param.get("from_station"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			if(null==arrive_station){
				logger.info("station is error: arrive_station:"+URLDecoder.decode(param.get("arrive_station"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			
			String from_station_name=BaseServlet.backStationName.get(param.get("from_station"));
			String arrive_station_name=BaseServlet.backStationName.get(param.get("arrive_station"));
			if(null==from_station_name){
				logger.info("station is error: from_station:"+URLDecoder.decode(param.get("from_station"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			if(null==arrive_station_name){
				logger.info("station is error: arrive_station:"+URLDecoder.decode(param.get("arrive_station"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			
			params.append("travel_time=").append(param.get("travel_time")).append("&from_station=")
			.append(from_station).append("&arrive_station=").append(arrive_station).append("&purpose_codes=").append(param.get("purpose_codes"));
			logger.info("java program query :"+URLDecoder.decode(params.toString(),"utf-8"));
			String jsonStr = HttpUtil.sendByPost(robot.getRobot_url(),params.toString(), "UTF-8");//调用接口
			
			logger.info("同程预订查询,java查询结果"+jsonStr);
			if(TrainConsts.ERROR.equals(jsonStr) || jsonStr.equals(TrainConsts.NO_DATAS)){
				return jsonStr;
			}
			ObjectMapper mapper = new ObjectMapper();
			TrainNewMidData train_data = mapper.readValue(jsonStr.toString(), TrainNewMidData.class);
			if(train_data!=null && train_data.getDatas().size()>0){
				
				List<TcTicketPriceVo> tc=new ArrayList<TcTicketPriceVo>();
				tc=getListTcTicketPriceVo(train_data.getDatas());
				//osnd.setCode("000");
				//osnd.setDatajson(train_data.getDatas());
				//trainInfoAppendPrice(param,osnd);
				tc=tcTrainInfoAppendPrice(param,tc);
				/*robotServiceImp.querySystemSetting("sys_weather_book");
				if("0".equals(robotServiceImp.getSetting_value())){
					for(TrainNewData data :osnd.getDatajson()){
						data.setWz_num("-");
						data.setYz_num("-");
						data.setYw_num("-");
						data.setRz_num("-");
						data.setRw_num("-");
						data.setZy_num("-");
						data.setZe_num("-");
						data.setGw_num("-");
						data.setTdz_num("-");
						data.setTz_num("-");
						data.setSwz_num("-");
						data.setGr_num("-");
					}
				}*/
				if(train_data.getDatas().size()>0){
					return JSONArray.fromObject(tc).toString();
				}else{
					return TrainConsts.ERROR;
				}
			}else{
				return TrainConsts.ERROR;
			}
		}catch(Exception e){
			logger.info("同程预订查询异常！"+e);
			return TrainConsts.ERROR;
		}	
	}
	
	
	public String javaQueryTrainData(Map<String,String> param,RobotSetVo robot) throws Exception{
		try{
			if(null == robot){
				return TrainConsts.NO_ROBOT;
			}
			StringBuffer params = new StringBuffer();
			
			String train_no = param.get("train_no");
			String from_station_telecode = param.get("from_station_telecode");
			String to_station_telecode = param.get("to_station_telecode");
			String depart_date = param.get("depart_date");
			
			
			logger.info("同程java query param:"+from_station_telecode+"_"+to_station_telecode);
			if(null==from_station_telecode){
				logger.info("station is error: from_station:"+URLDecoder.decode(param.get("from_station_telecode"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			if(null==to_station_telecode){
				logger.info("station is error: arrive_station:"+URLDecoder.decode(param.get("to_station_telecode"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			params.append("train_no=").append(train_no).append("&from_station_telecode=")
			.append(from_station_telecode).append("&to_station_telecode=").append(to_station_telecode).append("&depart_date=").append(depart_date);
			
			logger.info("同程java query url:"+URLDecoder.decode(params.toString(),"utf-8"));
			String jsonStr = HttpUtil.sendByPost(robot.getRobot_url(),params.toString(), "UTF-8");//调用接口
			logger.info("同程余票车次java返回结果 :"+jsonStr);
			if(TrainConsts.ERROR.equals(jsonStr) || jsonStr.equals(TrainConsts.NO_DATAS)){
				return TrainConsts.ERROR;
			}
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(jsonStr.toString());
			
			if(root.path("status").getBooleanValue() && root.path("httpstatus").getIntValue() == 200) {
				if(!root.path("data").isMissingNode()) {
					JsonNode data = root.path("data");
					if(!data.path("data").isMissingNode()){
						return data.path("data").size() > 0?data.path("data").toString() : TrainConsts.ERROR;
					}
				}
			}
			
			return TrainConsts.ERROR;
		}catch(Exception e){
			logger.info("java程序查询车次异常！"+e);
			return TrainConsts.ERROR;
		}	
	}
	
	private List<TcTicketPriceVo> tcTrainInfoAppendPrice(Map<String, String> paramMap,
			List<TcTicketPriceVo> tc) {
		List<TrainNewDataFake> list = new ArrayList<TrainNewDataFake>();
		//List<TrainNewData> train_list = osnd.getDatajson();
		if( tc.size()>0){
			List<TcTicketPriceVo> new_list = new ArrayList<TcTicketPriceVo>();
			try {
				paramMap.put("from_station", BaseServlet.backStationName.get(paramMap.get("from_station")));
				paramMap.put("arrive_station", BaseServlet.backStationName.get(paramMap.get("arrive_station")));
				robotServiceImp.queryProperTrainNewData(paramMap);
			} catch (RepeatException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			list = robotServiceImp.getList();
			TrainNewDataFake tndf = null;
			boolean exist = false;
			for (TcTicketPriceVo vo : tc){
					vo.tcInitPrice();
					for(int i=0; i<list.size(); i++){
						String[] arrCc = list.get(i).getCc().split("/");
						String trainCode = 
						vo.getTrain_code();
						int len = arrCc.length;
						for(int m=0; m<len; m++){
							if(arrCc[m].equals(trainCode)){
								if(list.get(i).getFz().equals(vo.getFrom_station_name()) &&
									list.get(i).getDz().equals(vo.getTo_station_name())){
									tndf = list.get(i);
//									if(!"-".equals(tndf.getWz())){
//										trainNewData.setWz(tndf.getWz());
//										exist = true;
//									}
									if(!"-".equals(tndf.getWz())){
										//vo.setYz_price(Double.parseDouble(tndf.getYz()));
										vo.setWz_price(Double.parseDouble(tndf.getWz()));
										exist = true;
									}
									if(!"-".equals(tndf.getYz())){
										vo.setYz_price(Double.parseDouble(tndf.getYz()));
										exist = true;
									}
									if(!"-".equals(tndf.getRz())){
										vo.setRz_price(Double.parseDouble(tndf.getRz()));
										exist = true;
									}
									if(!"-".equals(tndf.getYws())){
										vo.setYws_price(Double.parseDouble(tndf.getYws()));
										exist = true;
									}
									if(!"-".equals(tndf.getYwz())){
										vo.setYw_price(Double.parseDouble(tndf.getYwz()));
										exist = true;
									}
									if(!"-".equals(tndf.getYwx())){
										vo.setYwx_price(Double.parseDouble(tndf.getYwx()));
										exist = true;
									}
									if(!"-".equals(tndf.getRws())){
										vo.setRws_price(Double.parseDouble(tndf.getRws()));
										exist = true;
									}
									if(!"-".equals(tndf.getRwx())){
										vo.setRw_price(Double.parseDouble(tndf.getRwx()));
										exist = true;
									}
									if(!"-".equals(tndf.getRz1())){
										vo.setYdz_price(Double.parseDouble(tndf.getRz1()));
										exist = true;
									}
									if(!"-".equals(tndf.getRz2())){
										vo.setEdz_price(Double.parseDouble(tndf.getRz2()));
										exist = true;
									}
									if(!"-".equals(tndf.getSwz())){
										vo.setSwz_price(Double.parseDouble(tndf.getSwz()));
										exist = true;
									}
									if(!"-".equals(tndf.getTdz())){
										vo.setTdz_price(Double.parseDouble(tndf.getTdz()));
										exist = true;
									}
									if(!"-".equals(tndf.getGws())){
										exist = true;
									}
									if(!"-".equals(tndf.getGwx())){
										vo.setGjrw_price(Double.parseDouble(tndf.getGwx()));
										exist = true;
									}
									if(exist){
										new_list.add(vo);
									}
								}
							}
						}
						if(exist){
							break;
						}
					}
					/*if(!exist){
						Map<String,String> train_info = new HashMap<String,String>();
						train_info.put("train_no", trainNewData.getTrain_no());
						train_info.put("from_station_no", trainNewData.getFrom_station_no());
						train_info.put("to_station_no", trainNewData.getTo_station_no());
						train_info.put("seat_types", trainNewData.getSeat_types());
						train_info.put("train_date", paramMap.get("travel_time"));
						train_info.put("train_code", trainNewData.getStation_train_code());
						train_info.put("from_station_name", trainNewData.getFrom_station_name());
						train_info.put("to_station_name", trainNewData.getTo_station_name());
						logger.info("code:"+trainNewData.getStation_train_code()+trainNewData.getFrom_station_name()+"/"+trainNewData.getTo_station_name());
						try{
							robotServiceImp.addWaitPrice(train_info);
						}catch(Exception e){
							logger.info("插入待查询票价表异常");
						}
					}else{
						exist = false;
					}*/
					exist = false;
			}
			/*for(TrainNewDataFake train : list){
				String[] arrCc = train.getCc().split("/");
				int len = arrCc.length;
				boolean exist_train = false;
				if(len>1){
					for(int m=0; m<len; m++){
						for(int i=0; i<train_list.size(); i++){
							String trainCode = train_list.get(i).getStation_train_code();
							if(arrCc[m].equals(trainCode)){
								exist_train = true;
								break;
							}
						}
					}
				}else{
					for(int i=0; i<train_list.size(); i++){
						String trainCode = train_list.get(i).getStation_train_code();
						if(train.getCc().equals(trainCode)){
							exist_train = true;
							break;
						}
					}
				}
				if(!exist_train){
					try{
						robotServiceImp.addDeletePrice(train);;
					}catch(Exception e){
						logger.info("插入待删除票价表异常");
					}
				}
			}*/
			return new_list;
		}else{
			return null;
		}
		
		
	}

	private List<TcTicketPriceVo> getListTcTicketPriceVo(List<TrainNewData> datas) {
		List<TcTicketPriceVo> tc=new ArrayList<TcTicketPriceVo>();
		for(TrainNewData data:datas ){
			TcTicketPriceVo vo=new TcTicketPriceVo();
			vo.setCan_buy_now(data.getCanWebBuy());
			vo.setArrive_days(data.getDay_difference());
			vo.setTrain_start_date(data.getStart_train_date());
			vo.setTrain_code(data.getStation_train_code());
			vo.setAccess_byidcard(data.getIs_support_card());
			vo.setTrain_no(data.getTrain_no());
			getTrainType(data.getTrain_class_name());
			vo.setTrain_type("");
			vo.setFrom_station_name(data.getFrom_station_name());
			vo.setFrom_station_code(data.getFrom_station_telecode());
			vo.setTo_station_name(data.getTo_station_name());
			vo.setTo_station_code(data.getTo_station_telecode());
			vo.setStart_station_name(data.getStart_station_name());
			vo.setEnd_station_name(data.getEnd_station_name());
			vo.setStart_time(data.getStart_time());
			vo.setArrive_time(data.getArrive_time());
			vo.setRun_time(data.getLishi());
			vo.setRun_time_minute(data.getLishiValue());
			
			vo.setGjrw_num(data.getGr_num());
			vo.setQtxb_num(data.getQt_num());
			vo.setRw_num(data.getRw_num());
			vo.setRz_num(data.getRz_num());
			vo.setSwz_num(data.getSwz_num());
			vo.setTdz_num(data.getTdz_num());
			vo.setWz_num(data.getWz_num());
			vo.setYw_num(data.getYw_num());
			vo.setYz_num(data.getYz_num());
			vo.setEdz_num(data.getZe_num());
			vo.setYdz_num(data.getZy_num());
			vo.setNote(data.getNote().replaceAll("<br/>", ""));
			/*
			类型代码	对应车型
			D	动车组
			KT	空调特快
			KKS	空调快速
			KPK	空调普快
			KPM	空调普慢
			KS	快速
			PK	普快
			PM	普慢
			C	城际高速
			GD	高速动车
			XGZ	香港直通车
			Z	直达特快*/
			tc.add(vo);
		}
		return tc;
	}
	/**
	 * 拼接车次信息票价
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 * 
	 */
	public void trainInfoAppendPrice(Map<String, String> paramMap,OuterSoukdNewData osnd) throws Exception{
		List<TrainNewDataFake> list = new ArrayList<TrainNewDataFake>();
		List<TrainNewData> train_list = osnd.getDatajson();
		if(osnd != null && train_list.size()>0){
			List<TrainNewData> new_list = new ArrayList<TrainNewData>();
			robotServiceImp.queryProperTrainNewData(paramMap);
			list = robotServiceImp.getList();
			TrainNewDataFake tndf = null;
			boolean exist = false;
			for (TrainNewData trainNewData : train_list){
					trainNewData.initPrice();
					for(int i=0; i<list.size(); i++){
						String[] arrCc = list.get(i).getCc().split("/");
						String trainCode = trainNewData.getStation_train_code();
						int len = arrCc.length;
						for(int m=0; m<len; m++){
							if(arrCc[m].equals(trainCode)){
								if(list.get(i).getFz().equals(trainNewData.getFrom_station_name()) &&
									list.get(i).getDz().equals(trainNewData.getTo_station_name())){
									tndf = list.get(i);
//									if(!"-".equals(tndf.getWz())){
//										trainNewData.setWz(tndf.getWz());
//										exist = true;
//									}
									if(!"-".equals(tndf.getYz())){
										trainNewData.setYz(tndf.getYz());
										exist = true;
									}
									if(!"-".equals(tndf.getRz())){
										trainNewData.setRz(tndf.getRz());
										exist = true;
									}
									if(!"-".equals(tndf.getYws())){
										trainNewData.setYws(tndf.getYws());
										exist = true;
									}
									if(!"-".equals(tndf.getYwz())){
										trainNewData.setYwz(tndf.getYwz());
										exist = true;
									}
									if(!"-".equals(tndf.getYwx())){
										trainNewData.setYwx(tndf.getYwx());
										exist = true;
									}
									if(!"-".equals(tndf.getRws())){
										trainNewData.setRws(tndf.getRws());
										exist = true;
									}
									if(!"-".equals(tndf.getRwx())){
										trainNewData.setRwx(tndf.getRwx());
										exist = true;
									}
									if(!"-".equals(tndf.getRz1())){
										trainNewData.setZy(tndf.getRz1());
										exist = true;
									}
									if(!"-".equals(tndf.getRz2())){
										trainNewData.setZe(tndf.getRz2());
										exist = true;
									}
									if(!"-".equals(tndf.getSwz())){
										trainNewData.setSwz(tndf.getSwz());
										exist = true;
									}
									if(!"-".equals(tndf.getTdz())){
										trainNewData.setTdz(tndf.getTdz());
										exist = true;
									}
									if(!"-".equals(tndf.getGws())){
										trainNewData.setGws(tndf.getGws());
										exist = true;
									}
									if(!"-".equals(tndf.getGwx())){
										trainNewData.setGwx(tndf.getGwx());
										exist = true;
									}
									if(exist){
										new_list.add(trainNewData);
									}
								}
							}
						}
						if(exist){
							break;
						}
					}
					/*if(!exist){
						Map<String,String> train_info = new HashMap<String,String>();
						train_info.put("train_no", trainNewData.getTrain_no());
						train_info.put("from_station_no", trainNewData.getFrom_station_no());
						train_info.put("to_station_no", trainNewData.getTo_station_no());
						train_info.put("seat_types", trainNewData.getSeat_types());
						train_info.put("train_date", paramMap.get("travel_time"));
						train_info.put("train_code", trainNewData.getStation_train_code());
						train_info.put("from_station_name", trainNewData.getFrom_station_name());
						train_info.put("to_station_name", trainNewData.getTo_station_name());
						logger.info("code:"+trainNewData.getStation_train_code()+trainNewData.getFrom_station_name()+"/"+trainNewData.getTo_station_name());
						try{
							robotServiceImp.addWaitPrice(train_info);
						}catch(Exception e){
							logger.info("插入待查询票价表异常");
						}
					}else{
						exist = false;
					}*/
					exist = false;
			}
			/*for(TrainNewDataFake train : list){
				String[] arrCc = train.getCc().split("/");
				int len = arrCc.length;
				boolean exist_train = false;
				if(len>1){
					for(int m=0; m<len; m++){
						for(int i=0; i<train_list.size(); i++){
							String trainCode = train_list.get(i).getStation_train_code();
							if(arrCc[m].equals(trainCode)){
								exist_train = true;
								break;
							}
						}
					}
				}else{
					for(int i=0; i<train_list.size(); i++){
						String trainCode = train_list.get(i).getStation_train_code();
						if(train.getCc().equals(trainCode)){
							exist_train = true;
							break;
						}
					}
				}
				if(!exist_train){
					try{
						robotServiceImp.addDeletePrice(train);;
					}catch(Exception e){
						logger.info("插入待删除票价表异常");
					}
				}
			}*/
			osnd.setDatajson(new_list);
		}
	}
	//机器人异常处理机制
	public Integer exceptionManager(RobotSetVo robot,Map<String,String> param) throws Exception{
		String loginFailNum = String.valueOf(MemcachedUtil.getInstance().getAttribute("query_robot_" + robot.getRobot_id()));
		StringBuffer content = new StringBuffer();
		int num = 1;
		if(loginFailNum != null && !"".equals(loginFailNum)) {
			  int loginFailNumber = Integer.parseInt(loginFailNum);
			  loginFailNumber += 1;
			  MemcachedUtil.getInstance().setAttribute("query_robot_" + robot.getRobot_id(), loginFailNumber,90*1000);
			  logger.info(robot.getRobot_name()+" timeout num："+loginFailNumber);
			  if(loginFailNumber >= 10) {
				//停用当前机器人
				logger.info("close robot:robot_id:"+robot.getRobot_id()+" robot_name:"+robot.getRobot_name());
				robotServiceImp.updateRobotCloseById(robot.getRobot_id());
				content.append("[火车票]").append(robot.getRobot_name()).append("余票查询异常，已关闭！");
				//随机启用一个备用机器人
				num = robotServiceImp.querySpareRobot("java_query",param.get("channel"));
				int count=robotServiceImp.queryRobotCount(robot.getRobot_type(),param.get("channel"));
				if(num == 0){
					robot = robotServiceImp.getRobot();
					content.append("开启备用查询").append(robot.getRobot_name()).append("目前该渠道查询机器人数为：").append(count);
				}else{
					content.append("没有备用查询机器人分配给渠道[").append(param.get("channel")).append("],目前该渠道的查询机器人数为：").append(count);
				}
//				获取通知联系人电话并发送短信
				robotServiceImp.queryRobotWarnPhone();
				String phones = robotServiceImp.getPhones();
				robotServiceImp.addRobotWarnMessage(phones, robot.getRobot_name(),content.toString());
				MemcachedUtil.getInstance().setAttribute("query_robot_" + robot.getRobot_id(), 0, 90*1000);
			  }
		 }else{
			 MemcachedUtil.getInstance().setAttribute("query_robot_" + robot.getRobot_id(), 1, 90*1000);
		 }
		return num;
	}
	//机器人忙碌处理机制
	public Integer spareManager(RobotSetVo robot,Map<String,String> param) throws Exception{
		String loginFailNum = String.valueOf(MemcachedUtil.getInstance().getAttribute("query_robotspare_" + robot.getRobot_id()));
		StringBuffer content = new StringBuffer();
		int num = 1;
		if(loginFailNum != null && !"".equals(loginFailNum)) {
			  int loginFailNumber = Integer.parseInt(loginFailNum);
			  loginFailNumber += 1;
			  MemcachedUtil.getInstance().setAttribute("query_robotspare_" + robot.getRobot_id(), loginFailNumber,90*1000);
			  logger.info(robot.getRobot_name()+" busy num："+loginFailNumber);
			  if(loginFailNumber >= 10) {
				//停用当前机器人
				logger.info("spare robot:robot_id:"+robot.getRobot_id()+" robot_name:"+robot.getRobot_name());
				String robot_id = robot.getRobot_id();
				content.append("[火车票]").append(robot.getRobot_name()).append("余票查询处于忙碌，暂时改为备用状态！");
				//随机启用一个备用机器人
				num = robotServiceImp.querySpareRobot("java_query",param.get("channel"));
				int count=robotServiceImp.queryRobotCount(robot.getRobot_type(),param.get("channel"));
				if(num == 0){
					robot = robotServiceImp.getRobot();
					content.append("开启其他备用查询").append(robot.getRobot_name()).append("目前该渠道查询机器人数为：").append(count);
				}else{
					content.append("没有备用查询机器人分配给渠道[").append(param.get("channel")).append("],目前该渠道的查询机器人数为：").append(count);
				}
				robotServiceImp.updateRobotSpareById(robot_id);
//				获取通知联系人电话并发送短信
				robotServiceImp.queryRobotWarnPhone();
				String phones = robotServiceImp.getPhones();
				robotServiceImp.addRobotWarnMessage(phones, robot.getRobot_name(),content.toString());
				MemcachedUtil.getInstance().setAttribute("query_robotspare_" + robot.getRobot_id(), 0, 90*1000);
			  }
		 }else{
			 MemcachedUtil.getInstance().setAttribute("query_robotspare_" + robot.getRobot_id(), 1, 90*1000);
		 }
		return num;
	}
	
	/**
	 * 获取文件名(eg:all_北京_上海_2013-5-22)
	 * @return
	 */
	protected String getFileName(Map<String, String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("java_").append(map.get("from_station"))
		  .append("_")
		  .append(map.get("arrive_station"))
		  .append("_")
		  .append(map.get("travel_time"))
		  .append(map.get("method"));
		return sb.toString();
	}
	
	/**
	 * 值写入response
	 * @param response
	 * @param StatusStr
	 */
	public void write2Response(HttpServletResponse response, String StatusStr){
		try {
			response.getWriter().write(StatusStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws JsonProcessingException, IOException {
		String json = "{\"name\":\"Tom\",\"age\":24,\"status\":false}";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(json);
		System.out.println(root.path("age").isMissingNode());
	}
}
