package com.l9e.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.common.BaseServlet;
import com.l9e.common.TrainConsts;
import com.l9e.service.RobotServiceImp;
import com.l9e.service.SysSettingServiceImpl;
import com.l9e.util.CalendarUtil;
import com.l9e.util.ConfigUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.WorkerQueue;
import com.l9e.vo.Outer12306NewData;
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
public class QueryTicketServlet extends HttpServlet{
	private static final long serialVersionUID = 3264299111876887524L;
	
	private static  String  ctrip_data_url=ConfigUtil.getValue("ctrip_data_url");//
	
	RobotServiceImp robotServiceImp=new RobotServiceImp();
	SysSettingServiceImpl sysSettingServiceImpl=new SysSettingServiceImpl();
	private static Logger logger=Logger.getLogger(QueryTicketServlet.class);
	private Map<String,Integer> selectNums; //多类型机器人分配数
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	
	
	@Override
	public void init() throws ServletException {
		logger.info("init channel_robot selected num!");
		selectNums=new HashMap<String, Integer>();
		selectNums.put("selectNum_19e", 1);
		selectNums.put("selectNum_ext", 1);
		selectNums.put("selectNum_app", 1);
		selectNums.put("selectNum_elong", 1);
		selectNums.put("selectNum_inner", 1);
	}

	/**查询余票入口*/
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		long  a=System.currentTimeMillis();
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		/**分发参数*/
		String channel=req.getParameter("channel");
		String ctripChannel=channel;//保存发往携程服务的渠道名
		logger.info("ctripChannel渠道名称："+ctripChannel);
		if(channel.indexOf("ext")!=-1) {
			channel="ext"; //转换为本地需要的渠道名,用于查询机器人
		}
		String type=req.getParameter("type");
		
		/**业务参数*/
		String from_station =req.getParameter("from_station");
		String arrive_station =req.getParameter("arrive_station");
		String travel_time =req.getParameter("travel_time");
		String purpose_codes =req.getParameter("purpose_codes");//学生(0X00)、普通(ADULT)的余票查询,
		String train_code =req.getParameter("train_code");
		//查询余票
		String check_spare_num = req.getParameter("check_spare_num");
		String alter_refund = req.getParameter("alter_refund");
		StringBuffer log_req = new StringBuffer();
		//日志
		log_req.append("channel[").append(channel).append("]").append("param:")
			.append("travel_time[").append(travel_time).append("],from_station[")
			.append(URLDecoder.decode(from_station, "utf-8")).append("],arrive_station[")
			.append(URLDecoder.decode(arrive_station, "utf-8")).append("],purpose_codes[")
			.append(purpose_codes).append("],").append("type:").append(type).append("train_code:").append(train_code);
		
		logger.info(log_req.toString());
		
		Map<String,String> param=new HashMap<String, String>();
		param.put("from_station", from_station);
		param.put("arrive_station", arrive_station);
		param.put("travel_time", travel_time);
		param.put("purpose_codes", purpose_codes);
		if(StringUtils.isNotEmpty(train_code)) {
			param.put("train_code", train_code);//携程可选参数，查询单个车次
		}else {
			param.put("train_code", "");
		}
		
		param.put("method", "DGTrain");
		String key = getFileName(param);
		param.put("channel", channel);
		Object memcache = MemcachedUtil.getInstance().getAttribute(key);
		if("true".equals(check_spare_num)){
			memcache = null;
		}
		if("true".equals(alter_refund)){
			memcache = null;
		}
		if(null == memcache){
			String  queryTicketLine=this.getTrainSystemSetting("queryTicketLine");//1.代表查询12306  2 .查询库进行数据伪造 ,[查询系统异常,让其可以订票] 3.携程查询	
			logger.info("查询余票线路,queryTicketLine::"+queryTicketLine);
			String jsonStr = TrainConsts.NO_DATAS;
			if("1".equals(queryTicketLine)) { //查询12306
				//java查询
				jsonStr = queryTrainInfo(param);
				if(TrainConsts.STATION_ERROR.equals(jsonStr)){
					write2Response(resp,TrainConsts.STATION_ERROR);
					return;
				}
				
				if(TrainConsts.NO_ROBOT.equals(jsonStr) || TrainConsts.ERROR.equals(jsonStr)){
					jsonStr = queryTrainInfo(param);
					if(TrainConsts.NO_ROBOT.equals(jsonStr) || TrainConsts.ERROR.equals(jsonStr)) {
						jsonStr = queryTrainInfo(param);
					}
				}
				
				if(TrainConsts.NO_ROBOT.equals(jsonStr) || TrainConsts.ERROR.equals(jsonStr)) {
					logger.info(param+",重试3次,查询车次失败,查询数据库车次:"+jsonStr);
					boolean ysFlag=false;
					if("ADULT".equals(purpose_codes)) {//成人预售30天
						ysFlag= CalendarUtil.checkNowDateInterval(DateUtil.stringToDate(travel_time, DateUtil.DATE_FMT1), 30);
					} else if("0X00".equals(purpose_codes)) {//学生预售60天
						ysFlag= CalendarUtil.checkNowDateInterval(DateUtil.stringToDate(travel_time, DateUtil.DATE_FMT1), 60);
					}else {
						ysFlag= CalendarUtil.checkNowDateInterval(DateUtil.stringToDate(travel_time, DateUtil.DATE_FMT1), 30);
					}
					
					if(ysFlag) {//在预售期内
						queryTicketLine="2";
						logger.info("查询机器人失败，查询数据库的数据，不缓存！！！,queryTicketLine::"+queryTicketLine);
						jsonStr = queryTrainInfoNew(param);// 查询数据库,进行匹配
					}else {
						jsonStr=TrainConsts.NO_DATAS;
					}
					
				}
				
			}else if ("2".equals(queryTicketLine)){ //查询库进行数据伪造
				logger.info("----不查询12306----,查询数据库,进行匹配,数据伪造！");
				try {
					jsonStr = queryTrainInfoNew(param);// 查询数据库,进行匹配
				} catch (Exception e) {
						logger.info("查询数据库发生异常！！！", e);
				}
			}else if("3".equals(queryTicketLine)){//查询携程的数据接口	
				logger.info("-------查询携程余票-----------------------");
				param.put("ctripChannel", ctripChannel);
				jsonStr=queryTrainInfoCtrip(param);
				
			}
					
			if(jsonStr == null || TrainConsts.NO_ROBOT.equals(jsonStr)||TrainConsts.ERROR.equals(jsonStr)){
				write2Response(resp,TrainConsts.NO_DATAS);//所有错误全部转换成，未获取到数据
				return;
			}
				
			try{
				if(jsonStr.contains("\"code\":\"000\"")&&"1".equals(queryTicketLine)){//从12306查询的余票信息，需要缓存
					logger.info(param+",12306数据查询成功");
					/**线程分布任务可做*/
//					String prePath = req.getSession().getServletContext().getRealPath("/files");
					long startFile=System.currentTimeMillis();
				//	String fileDir = "//data//cache_files_old//" + travel_time; //老版服务处理
					String fileDir = "//data//cache_files//" + travel_time;
					String fileName = Md5Encrypt.md5(key, "gbk") + ".txt";
					String filePath = fileDir + "//" + fileName;
					logger.info("---------------cache_files is filePath="+filePath);
					//创建文件保存接口返回数据
					FileUtil.removeFile(filePath);
					boolean isSucess = FileUtil.createFile(fileDir, fileName, jsonStr, "UTF-8");
					//文件生成成功则把文件名写入Memcache
					if(isSucess){
						MemcachedUtil.getInstance().setAttribute(key, filePath, 1*60*1000);
						logger.info(param.get("from_station")+param.get("arrive_station")+param.get("travel_time")+","+","+"文件缓存耗时:"+(System.currentTimeMillis()-startFile)/1000f+"s");
					}
				}else {
					logger.info("queryTicketLine::"+queryTicketLine+",查询的返回数据："+jsonStr);
				}				
				 long b=System.currentTimeMillis();
			     logger.info(param.get("from_station")+param.get("arrive_station")+param.get("travel_time")+","+(b-a)/1000f+"秒");
				//返回查询结果
				write2Response(resp,jsonStr);
			}catch(Exception e){
				logger.error("保存查询信息为缓存文件异常！",e);
				write2Response(resp,"error");
			}
		}else{
			long start = System.currentTimeMillis();
			String filePath = (String) memcache;
			String fileContent = FileUtil.readFile(filePath, "UTF-8");
			StringBuffer file_str = new StringBuffer();
			file_str.append(param.get("from_station")+param.get("arrive_station")+param.get("travel_time")+",读文件耗时[").append(System.currentTimeMillis() - start).append("]ms");
			logger.info(file_str.toString());
			write2Response(resp,fileContent);
		}
		   
	}
	
	//查询携程的余票数据
	public String queryTrainInfoCtrip(Map<String,String> param) {
		String jsonStr=TrainConsts.NO_DATAS;
		StringBuffer postParam = new StringBuffer();
		try {
			postParam.append("travel_time=").append(param.get("travel_time"))
			.append("&from_station=").append(URLEncoder.encode(param.get("from_station"), "utf-8"))
			.append("&arrive_station=").append(URLEncoder.encode(param.get("arrive_station"), "utf-8"))
			.append("&train_code=").append(param.get("train_code"))
			.append("&channel=").append(param.get("ctripChannel"))
			.append("&type=").append("yupiao");
		} catch (UnsupportedEncodingException e) {
			logger.info("***", e);
			jsonStr=TrainConsts.ERROR;
		}
		
		logger.info("请求携程服务接口："+ctrip_data_url+"?"+postParam.toString());
		jsonStr=HttpUtil.sendByPost(ctrip_data_url, postParam.toString(), "utf-8");
		logger.info("携程接口返回数据："+jsonStr);
		
		//logger.info("*****去除动卧信息后的字符串*****");
		/*if(jsonStr.contains("\"code\":\"000\"")) {//查询成功
			JSONObject obj=JSONObject.fromObject(jsonStr);
			JSONArray arr=obj.getJSONArray("datajson");
			for(int i=0;i<arr.size();i++) {
				JSONObject json=new JSONObject();
				json = arr.getJSONObject(i);
				json.remove("dwx");
				json.remove("dws");
				json.remove("dw_num");
			}
			return obj.toString();
		}*/
		
		return jsonStr;
	}
	
	
	//查询数据库，获取车次和票价
	public String queryTrainInfoNew(Map<String, String> param) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		try {
			QueryControll qc = new QueryControll();
			OuterSoukdNewData osnd = new OuterSoukdNewData();
			StringBuffer params = new StringBuffer();

			String from_station = BaseServlet.stationName.get(param
					.get("from_station"));
			String arrive_station = BaseServlet.stationName.get(param
					.get("arrive_station"));
			String purpose_codes = param.get("purpose_codes");
			if (null == from_station) {
				logger.info("station is error: from_station:"
						+ URLDecoder.decode(param.get("from_station"), "utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			if (null == arrive_station) {
				logger.info("station is error: arrive_station:"
						+ URLDecoder.decode(param.get("arrive_station"),
								"utf-8"));
				return TrainConsts.STATION_ERROR;
			}
			params.append("travel_time=").append(param.get("travel_time"))
					.append("&from_station=").append(from_station)
					.append("&arrive_station=").append(arrive_station);

			if (StringUtils.isNotEmpty(purpose_codes)) {
				if ("0X00".equals(purpose_codes)) {
					params.append("&purpose_codes=").append(purpose_codes);
				} else if ("ADULT".equals(purpose_codes)) {
					params.append("&purpose_codes=").append(purpose_codes);
				} else {
					logger.info("purpose_codes,参数错误：" + purpose_codes);
					return TrainConsts.ERROR;
				}
			}
			logger.info("database program query :"
					+ URLDecoder.decode(params.toString(), "utf-8"));

			logger.info("java program query :"
					+ URLDecoder.decode(params.toString(), "utf-8"));
			
			logger.info("---------查询数据库，获取车次，然后拼接票价----------");
				
			String jsonStr=qc.dataBaseQueryData(param);
				
			logger.info("根据车站请求查询车次余票接口的返回结果jsonStr为：" + jsonStr);
			if (TrainConsts.ERROR.equals(jsonStr)
					|| jsonStr.equals(TrainConsts.NO_DATAS)) {
				return jsonStr;
			}
			ObjectMapper mapper = new ObjectMapper();
			TrainNewMidData train_data = mapper.readValue(jsonStr.toString(),
					TrainNewMidData.class);
			if (train_data != null && train_data.getDatas().size() > 0) {
				osnd.setCode("000");
				osnd.setDatajson(train_data.getDatas());
				trainInfoAppendPrice(param, osnd);

				robotServiceImp.querySystemSetting("sys_weather_book");
				if ("0".equals(robotServiceImp.getSetting_value())) {
					for (TrainNewData data : osnd.getDatajson()) {
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
				if (train_data.getDatas().size() > 0) {
					JSONObject json_str=JSONObject.fromObject(osnd);
					//老版服务处理
				//	logger.info("******去除动卧信息，保持原有接口的使用*****");
				  /*JSONArray arr = json_str.getJSONArray("datajson");
					JSONObject json = new JSONObject();
					for (int i = 0; i < arr.size(); i++){
						json = arr.getJSONObject(i);
						json.remove("dwx");
						json.remove("dws");
						json.remove("dw_num");
					}*/

					return json_str.toString();

				} else {
					return TrainConsts.ERROR;
				}
			} else {
				return TrainConsts.ERROR;
			}
		} catch (Exception e) {
			logger.info("java程序查询余票异常！", e);
			return TrainConsts.ERROR;
		}
	
	}

	//机器人新接口，java版
	public String queryTrainInfo(Map<String,String> param){
		QueryControll qc = new QueryControll();
		String jsonStr=TrainConsts.NO_DATAS;
		//从内存队列取机器人
		WorkerVo robot=null;
		try {
			robot = getWorkerByQueue();
			if(null==robot) {
				robot = getWorkerByQueue();
			}
			if (null==robot) {
			    logger.info("*****从队列获取四次,没有获取到机器*******");
			   jsonStr=TrainConsts.ERROR;
			}else {
				StringBuffer buff = new StringBuffer();
				buff.append("travel_time=").append(param.get("travel_time"))
						.append("|from_station=")
						.append(param.get("from_station"))
						.append("|arrive_station=")
						.append(param.get("arrive_station"))
						.append("|worker_ext=").append(robot.getWorker_ext());

				logger.info("queryTrainInfo信息：" + buff.toString());
				jsonStr = qc.javaQueryData(param, robot);
			}
		} catch (Exception e) {
			logger.info("获取查询机器失败",e);
			jsonStr= TrainConsts.ERROR;
		}
		return jsonStr;
	}
	
	
	//机器人新接口,lua
	public String queryLeftTicket(HttpServletRequest req,Map<String,String> param){
		String jsonStr="";
		RobotSetVo robot = null;
		try {
			robot = getRobot("query", param.get("channel"));
			jsonStr = robotQueryData(param,robot);
			
			if(jsonStr.contains("没有空闲机器人")){
				logger.info("no work robot,then use other robto query!");
				int ro = spareManager(robot,param);
				//再次发起查询
				if(ro == 0){
					jsonStr = robotQueryData(param,robot);
				}else{
					robot = getRobot("query", param.get("channel"));
					jsonStr = robotQueryData(param,robot);
				}
			}else if(jsonStr.contains("任务超时")){
				logger.info("timeout:robot_id:"+robot.getRobot_id()+" robot_name:"+robot.getRobot_name());
				int ro = exceptionManager(robot,param);
				//再次发起查询
				if(ro == 0){
					jsonStr = robotQueryData(param,robot);
				}else{
					robot = getRobot("query", param.get("channel"));
					jsonStr = robotQueryData(param,robot);
				}
			}else{
				if(null!=robot.getRobot_id()){
					MemcachedUtil.getInstance().setAttribute("query_robot_" + robot.getRobot_id(), 0);
					MemcachedUtil.getInstance().setAttribute("query_robotspare_" + robot.getRobot_id(), 0);
				}
			}
			
		} catch (Exception e) {
			logger.info("解析12306新接口异常",e);
			try{
				StringBuffer content = new StringBuffer();
				if(null==robot || "".equals(robot.getRobot_id()) || null==robot.getRobot_id()){
					logger.info("robot is null then try to start spare robot");
					//随机分配一个备用机器人
					int num = robotServiceImp.updateSpareRobot("query",param.get("channel"));
					if(num == 0){
						robot = robotServiceImp.getRobot();
						logger.info("random spare robot to work:"+robot.getRobot_name());
						content.append("[火车票]渠道").append(param.get("channel")).append("，没有分配查询机器人！")
						.append("临时使用").append(robot.getRobot_name()).append("，请尽快安排人员协调查询机器人分配！");
						//获取通知联系人电话并发送短信
						robotServiceImp.queryRobotWarnPhone();
						String phones = robotServiceImp.getPhones();
						robotServiceImp.addRobotWarnMessage(phones, robot.getRobot_name(),content.toString());
						//再次发起查询
						jsonStr = robotQueryData(param,robot);
					}else{
						robot = getRobot("query", param.get("channel"));
						jsonStr = robotQueryData(param,robot);
					}
				}else{
					logger.info("robot is not null exception robot_name:"+robot.getRobot_name());
					int ro = exceptionManager(robot,param);
					if(ro == 0){
						jsonStr = robotQueryData(param,robot);
					}else{
						robot = getRobot("query", param.get("channel"));
						jsonStr = robotQueryData(param,robot);
					}
				}
			}catch(Exception ef){
				logger.error("异常处理机制异常，没有发起短信通知", ef);
				return TrainConsts.ERROR;
			}
		}
		return jsonStr;
	}
	
	/**
	 * 从内存队列取机器人,取两次
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public WorkerVo getWorkerByQueue() throws RepeatException, DatabaseException{
		WorkerVo  robot =WorkerQueue.getInstance().poll();
		if(null!=robot){
			return robot;
		}else{
			return WorkerQueue.getInstance().poll();
		}
	}
	/**
	 * 原有的从数据库获取查询机器
	 * @param robot_type
	 * @param channel
	 * @return
	 * @throws RepeatException
	 * @throws DatabaseException
	 */
	public RobotSetVo getRobot(String robot_type,String channel) throws RepeatException, DatabaseException{
		RobotSetVo robot = new RobotSetVo();
		int selectNum = -1;
		robot.setRobot_type(robot_type);
		robot.setRobot_channel(channel);
		int count=robotServiceImp.queryRobotCount(robot_type,channel);
		selectNum = getNextNum(count,channel);
		robot.setSelectNum(selectNum);
		robot=robotServiceImp.queryRobot(robot);
		StringBuffer sb = new StringBuffer();
		sb.append(" get selectNum:").append(selectNum).append(" Name:").append(robot.getRobot_name())
			.append(",Url:").append(robot.getRobot_url()).append(",ConTimeout:")
			.append(robot.getRobot_con_timeout()).append(",ReadTimeout:").append(robot.getRobot_read_timeout());
		logger.info(sb.toString());
		if(null!=robot.getRobot_id()){
			return robot;
		}else{
			return null;
		}
	}
	
	public String robotQueryData(Map<String,String> param,RobotSetVo robot) throws Exception{
		if(null==robot){
			return TrainConsts.NO_ROBOT;
		}
		long start=System.currentTimeMillis();
		String result = TrainConsts.ERROR;
		String  jsonStr = HttpUtil.sendByGet(get12306Url(param,robot.getRobot_url()), "UTF-8", robot.getRobot_con_timeout(), robot.getRobot_read_timeout());//调用接口
		logger.info("根据车站请求查询车次余票接口的返回结果jsonStr为："+jsonStr);
		jsonStr = jsonStr.replace("\\", "")
		 .replace("[\"{", "[{")
		 .replace("}\"]", "}]")
		 .replace("\"[", "[")
		 .replace("]\"", "]")
		 .replace("--", "-");
		
		  JsonConfig config = new JsonConfig();

	      config.setJsonPropertyFilter(new PropertyFilter()
	      {
	        public boolean apply(Object arg0, String arg1, Object arg2) {
	          if ((arg1.equals("controlled_train_flag")) || (arg1.equals("controlled_train_message"))) {
	            return true;
	          }
	          return false;
	        }
	      });
	    jsonStr=JSONObject.fromObject(jsonStr, config).toString();
		ObjectMapper mapper = new ObjectMapper();
		Outer12306NewData outer12306NewData = null;
		OuterSoukdNewData osnd = null;
		try{
			outer12306NewData = mapper.readValue(jsonStr, Outer12306NewData.class);
			osnd = outer12306NewData.getErrorInfo().get(0);
		}catch(Exception e){
			logger.error("查询返回结果格式异常："+jsonStr,e);
			osnd = null;
		}
		StringBuffer result_log = new StringBuffer();
		if(osnd != null){
			//统计列车列数
			if(osnd.getDatajson() == null || osnd.getDatajson().size()==0){
				if(!StringUtils.isEmpty(osnd.getErrInfo())){
					result = osnd.getErrInfo();
				}
				result_log.append("query result is :").append("travel_time[").append(param.get("travel_time")).append("]")
				.append(param.get("from_station")).append("/").append(param.get("arrive_station")).append("all time[")
				.append(System.currentTimeMillis() - start).append("]ms").append(";train 0 nums");
				logger.info(result_log.toString());
			}else{
				result_log.append("query result is :").append("travel_time[").append(param.get("travel_time")).append("]")
				.append(param.get("from_station")).append("/").append(param.get("arrive_station")).append("all time[")
				.append(System.currentTimeMillis() - start).append("]ms").append(";train ").append(osnd.getDatajson().size())
				.append(" nums");
				logger.info(result_log.toString());
				if(!"000".equals(osnd.getCode())){
					result = TrainConsts.ERROR;
				}else{
					if(osnd.getDatajson()!=null && osnd.getDatajson().size()>0){
						trainInfoAppendPrice(param,osnd);
					}
					robotServiceImp.querySystemSetting("sys_weather_book");
					if("0".equals(robotServiceImp.getSetting_value())){
						logger.info("start unbook setting");
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
					}
					
					result = JSONObject.fromObject(osnd).toString();
				}
			}
		}
		return result;
	}
	
	/**
	 * 拼接车次信息票价
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 * 
	 */
	public void trainInfoAppendPrice(Map<String, String> paramMap,OuterSoukdNewData osnd) throws Exception{
		List<TrainNewDataFake> list = new ArrayList<TrainNewDataFake>();
		List<TrainNewData> list_train = osnd.getDatajson();
		if(osnd != null && list_train != null
				&& list_train.size()>0){
			List<TrainNewData> new_list = new ArrayList<TrainNewData>();
			robotServiceImp.queryProperTrainNewData(paramMap);
			list = robotServiceImp.getList();
			TrainNewDataFake tndf = null;
			boolean exist = false;
			for (TrainNewData trainNewData : list_train){
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
					if(!exist){
						
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
					}
			}
			for(TrainNewDataFake train : list){
				String[] arrCc = train.getCc().split("/");
				int len = arrCc.length;
				boolean exist_train = false;
				if(len>1){
					for(int m=0; m<len; m++){
						for(int i=0; i<list_train.size(); i++){
							String trainCode = list_train.get(i).getStation_train_code();
							if(arrCc[m].equals(trainCode)){
								exist_train = true;
								break;
							}
						}
					}
				}else{
					for(int i=0; i<list_train.size(); i++){
						String trainCode = list_train.get(i).getStation_train_code();
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
			}
			osnd.setDatajson(new_list);
		}
	}
	//机器人异常处理机制
	public Integer exceptionManager(RobotSetVo robot,Map<String,String> param) throws Exception{
		String loginFailNum = null;
		if(null!=MemcachedUtil.getInstance().getAttribute("query_robot_" + robot.getRobot_id())){
			loginFailNum = String.valueOf(MemcachedUtil.getInstance().getAttribute("query_robot_" + robot.getRobot_id()));
		}
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
				num = robotServiceImp.querySpareRobot("query",param.get("channel"));
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
		String loginFailNum = null;
		if(null!=MemcachedUtil.getInstance().getAttribute("query_robotspare_" + robot.getRobot_id())){
			loginFailNum = String.valueOf(MemcachedUtil.getInstance().getAttribute("query_robotspare_" + robot.getRobot_id()));
		}
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
				num = robotServiceImp.querySpareRobot("query",param.get("channel"));
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
	
	/**机器人分发 调整selectNum*/
	private int getNextNum(int count,String channel) {
		synchronized (selectNums.get("selectNum_"+channel)){//对指定渠道的分配num进行加锁
			int num=selectNums.get("selectNum_"+channel);
			if (num >= count) {
				selectNums.put("selectNum_"+channel,1);
			} else {
				selectNums.put("selectNum_"+channel,1+num);
			}
			return selectNums.get("selectNum_"+channel);
		}
		
	}
	
	
	/**
	 * 获取文件名(eg:all_北京_上海_2013-5-22)
	 * @return
	 */
	protected String getFileName(Map<String, String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("javarobot_").append(map.get("from_station"))
		  .append("_")
		  .append(map.get("arrive_station"))
		  .append("_")
		  .append(map.get("travel_time"))
		  .append(map.get("method"));
		return sb.toString();
	}
	
	/**
	 * 12306 URL
	 * @param map
	 * @return
	 */
	protected String get12306Url(Map<String, String> map, String interfaceUrl){
		String url = new String(interfaceUrl);
		StringBuffer sb = new StringBuffer();
		sb.append(map.get("travel_time"))
		  .append("|")
		  .append(map.get("from_station"))
		  .append("|")
		  .append(map.get("arrive_station"));
		String param = "";
		try {
			param = URLEncoder.encode(sb.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info("[机器人session_id]=" + session_id);
		return url.replace("$session_id", session_id)
		 		  .replace("$param1", param);
	}
	
	
	/**
	 * 查询余票线路选择
	 * @param key
	 * @return
	 */
	public String getTrainSystemSetting(String key){
		String queryTicketLine = null;//1.代表查询12306  2 .查询库进行数据伪造 3.携程数据
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			try {
				int status = sysSettingServiceImpl.querySysVal(key);
				logger.info("***" + status);
			} catch (RepeatException e) {
				logger.info("*********", e);
			} catch (DatabaseException e) {
				logger.info("********", e);
			}
			queryTicketLine=sysSettingServiceImpl.getSysVal();
			MemcachedUtil.getInstance().setAttribute(key, queryTicketLine, 30*1000);
		}else{
			queryTicketLine = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		logger.info("queryTicketLine:::"+queryTicketLine);
		return queryTicketLine;
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
