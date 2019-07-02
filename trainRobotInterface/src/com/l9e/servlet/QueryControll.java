package com.l9e.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseServlet;
import com.l9e.common.TrainConsts;
import com.l9e.service.RobotServiceImp;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.WorkerQueue;
import com.l9e.vo.OuterSoukdNewData;
import com.l9e.vo.RobotSetVo;
import com.l9e.vo.TrainNewData;
import com.l9e.vo.TrainNewDataFake;
import com.l9e.vo.TrainNewMidData;
import com.l9e.vo.WorkerVo;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * 查询余票主控路口
 * 
 * */
public class QueryControll{
	private static final long serialVersionUID = 3264299111876887524L;
	
	RobotServiceImp robotServiceImp=new RobotServiceImp();
	private static Logger logger=Logger.getLogger(QueryControll.class);
	
	
	/**
	 * 数据库查询的车次，格式化为12306的字符串
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String dataBaseQueryData(Map<String,String> param) throws Exception{
		
		String  from_station=param.get("from_station");
		String  arrive_station=param.get("arrive_station");
		String  travel_time=param.get("travel_time");
		logger.info("from_station:"+from_station+",arrive_station:"+",travel_time:"+travel_time);
		robotServiceImp.queryDataBaseCheci(param);
		List<TrainNewData> data_train_list =robotServiceImp.getDataList();
		List<TrainNewData> data_train_list_new=new  ArrayList<TrainNewData>();
		for(TrainNewData trainNewData:data_train_list) {
			
			String costday=trainNewData.getControl_day();
			String start_time =trainNewData.getStart_time();
			String arrive_time =trainNewData.getArrive_time();
			
			logger.info("start_time:"+start_time+",arrive_time:"+arrive_time+",train_code:"+trainNewData.getStation_train_code());
			trainNewData.setStart_train_date(DateUtil.dateToString(DateUtil.stringToDate(travel_time, "yyyy-MM-dd"), "yyyyMMdd"));
			try {	
				String lishiValue = DateUtil.dateDifferMin(start_time, arrive_time, travel_time, costday);
				Long  hour=Long.valueOf(lishiValue)/60;
				Long  hour1=Long.valueOf(lishiValue)%60;
				String lishi = (hour <10?"0"+hour:hour)+":"+(hour1%60<10?"0"+hour1:hour1);
				trainNewData.setLishiValue(lishiValue);
				trainNewData.setLishi(lishi);
			} catch (Exception e) {
				logger.info("Exception",e);
				continue;
			}
			trainNewData.setDay_difference(costday);
			trainNewData.setCanWebBuy("Y");
			trainNewData.setTrain_no("");
			trainNewData.setSeat_types("");
			trainNewData.setNote("");
			trainNewData.setLocation_code("");
			trainNewData.setTo_station_no("");
			trainNewData.setFrom_station_no("");
			trainNewData.setYp_info("");
			trainNewData.setYp_ex("");
			trainNewData.setStart_station_telecode("");
			trainNewData.setEnd_station_telecode("");
			trainNewData.setFrom_station_telecode("");
			trainNewData.setTo_station_telecode("");
			trainNewData.setYp_info("");
			trainNewData.setSeat_feature("");
			
			
			trainNewData.setGg_num("100");
			trainNewData.setGr_num("100"); //高级软卧票数
			trainNewData.setGw_num("100");
			
			trainNewData.setQt_num("100"); //其它
			trainNewData.setRw_num("100");//软卧票数
			trainNewData.setRz_num("100");//软座票数
			
			trainNewData.setTdz_num("100");//特等座票数
			trainNewData.setTz_num("100");
			
			trainNewData.setWz_num("100"); //无座
			
			trainNewData.setYw_num("100"); //硬卧票数
			trainNewData.setYz_num("100"); //硬座票数
			trainNewData.setZe_num("100"); //二等座票数
			trainNewData.setZy_num("100"); //一等座票数
			trainNewData.setSwz_num("100"); //商务座票数
			trainNewData.setDw_num("100"); //动卧票数

			data_train_list_new.add(trainNewData);
		}		
		JSONObject obj=new JSONObject();
		obj.put("flag", true);
		obj.put("message", "");
		obj.put("searchDate", "");
		obj.put("datas", data_train_list_new);
		
	    return  obj.toString(); 
	}

	public String javaQueryData(Map<String,String> param,WorkerVo  robot) throws Exception{
		try{
			if(null == robot){
				return TrainConsts.NO_ROBOT;
			}
			OuterSoukdNewData osnd = new OuterSoukdNewData();
			StringBuffer params = new StringBuffer();
			
			String from_station=BaseServlet.stationName.get(param.get("from_station"));
			String arrive_station=BaseServlet.stationName.get(param.get("arrive_station"));
			String purpose_codes=param.get("purpose_codes");
			if(null==from_station){
				logger.info("station is error: from_station:"+URLDecoder.decode(param.get("from_station"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			if(null==arrive_station){
				logger.info("station is error: arrive_station:"+URLDecoder.decode(param.get("arrive_station"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			params.append("travel_time=").append(param.get("travel_time")).append("&from_station=")
			.append(from_station).append("&arrive_station=").append(arrive_station);
			
			if(StringUtils.isNotEmpty(purpose_codes)){
				if("0X00".equals(purpose_codes)){
					params.append("&purpose_codes=").append(purpose_codes);
				}else if ("ADULT".equals(purpose_codes)) {
					params.append("&purpose_codes=").append(purpose_codes);
				}else{
					logger.info("purpose_codes,参数错误："+purpose_codes);
					return TrainConsts.ERROR;
				}
			}
			
			logger.info("java program query :"+URLDecoder.decode(params.toString(),"utf-8"));
			long a=System.currentTimeMillis();
			String jsonStr = HttpUtil.sendByPost(robot.getWorker_ext(),params.toString(), "UTF-8");//调用接口
			long b=System.currentTimeMillis();
			logger.info("查询机器人查询所需时间："+param.get("from_station")+param.get("arrive_station")+param.get("travel_time")+","+(b-a)/1000f+"s");
			//logger.info("根据车站请求查询车次余票接口的返回结果jsonStr为："+jsonStr);
			if(TrainConsts.ERROR.equals(jsonStr) || jsonStr.equals(TrainConsts.NO_DATAS)){
				return jsonStr;
			}
			ObjectMapper mapper = new ObjectMapper();
			TrainNewMidData train_data = mapper.readValue(jsonStr.toString(), TrainNewMidData.class);
			
			if(train_data!=null && train_data.getDatas().size()>0){
				osnd.setCode("000");
				osnd.setDatajson(train_data.getDatas());
				a=System.currentTimeMillis();
				trainInfoAppendPrice(param,osnd);
				b=System.currentTimeMillis();
				logger.info("数据库匹配票价所需时间："+param.get("from_station")+param.get("arrive_station")+param.get("travel_time")+","+(b-a)/1000f+"s");	
				robotServiceImp.querySystemSetting("sys_weather_book");
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
						data.setDw_num("-");
					}
				}
				
		//*************************************************************//

				if(train_data.getDatas().size()>0){
					JSONObject json_str=JSONObject.fromObject(osnd);
				
					//老版服务处理
					/*logger.info("去除动卧信息，保持原有接口的使用");
					JSONArray arr= json_str.getJSONArray("datajson");
					JSONObject json=new JSONObject();
					for(int i=0;i<arr.size();i++){
						json = arr.getJSONObject(i);
						json.remove("dwx");
						json.remove("dws");
						json.remove("dw_num");
					}*/
					logger.info("格式化后的串:"+json_str.toString());
					return json_str.toString();
				}else{
					return TrainConsts.ERROR;
				}
	
	  //**************************************************************//
				
			}else{
				return TrainConsts.ERROR;
			}
		
		}catch(Exception e){
			logger.info("java程序查询余票异常！",e);
			return TrainConsts.ERROR;
		}	
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
			long a=System.currentTimeMillis();
			robotServiceImp.queryProperTrainNewData(paramMap);
			long b=System.currentTimeMillis();
			logger.info("queryProperTrainNewData查询所需时间："+paramMap.get("from_station")+paramMap.get("arrive_station")+paramMap.get("travel_time")+","+(b-a)/1000f+"s");	
			list = robotServiceImp.getList();
			TrainNewDataFake tndf = null;
			boolean exist = false;
			
			a=System.currentTimeMillis();
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
									if(!"0".equals(tndf.getSwz())){
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
									if(!"-".equals(tndf.getDws())){
										trainNewData.setDws(tndf.getDws());
										exist = true;
									}
									if(!"-".equals(tndf.getDwx())){
										trainNewData.setDwx(tndf.getDwx());
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
			
			b=System.currentTimeMillis();
			logger.info("12306的车次与票价表匹配价格所需时间："+paramMap.get("from_station")+paramMap.get("arrive_station")+paramMap.get("travel_time")+","+(b-a)/1000f+"s");	

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
}
