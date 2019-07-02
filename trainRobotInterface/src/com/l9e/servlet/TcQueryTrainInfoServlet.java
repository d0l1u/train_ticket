package com.l9e.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.common.TrainConsts;
import com.l9e.service.RobotServiceImp;
import com.l9e.util.FileUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.StrUtil;
import com.l9e.vo.RobotSetVo;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * 同程车次信息查询
 * @author licheng
 *
 */
public class TcQueryTrainInfoServlet extends HttpServlet {

	private static final long serialVersionUID = 5646052510310995515L;
	
	RobotServiceImp robotServiceImp=new RobotServiceImp();
	TcQueryControll qc = new TcQueryControll();
	private static Logger logger=Logger.getLogger(TcQueryTrainInfoServlet.class);
	private Map<String,Integer> selectNums; //多类型机器人分配数

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		/*分发参数*/
		String channel=req.getParameter("channel");
		/*业务参数*/
		String from_station =req.getParameter("from_station");
		String to_station =req.getParameter("to_station");
		String train_date =req.getParameter("train_date");
		String train_no = req.getParameter("train_no");
		String train_code = req.getParameter("train_code");
		
		StringBuffer log_req = new StringBuffer();
		/*日志*/
		log_req.append("同程车次查询").append("param:")
			.append("train_date[").append(train_date).append("],from_station[")
			.append(from_station).append("],to_station[").append(to_station).append("],train_no[").append(train_no).append("],train_code[").append(train_code).append("]");
		logger.info(log_req.toString());
		String trainInfoData = TrainConsts.NO_DATAS;
		JSONObject jsonResult = new JSONObject();
		
		if(!StrUtil.isNotEmpty(train_no)) {
			String trainJson = TrainConsts.NO_DATAS;
			/*
			 * 如果train_no为空，即为同程没有提供12306系统内部车次编号，
			 * 需要先执行一次查询以获得车次内部编号
			 */
			Map<String,String> param = new HashMap<String, String>();
			param.put("from_station", from_station);
			param.put("arrive_station", to_station);
			
			param.put("method", "getTrainNo");
			String key = getFileName1(param);
			param.put("travel_time", train_date);
			param.put("channel", channel);
			param.put("interfacePath", "queryTicket");
			
			Object memcache = MemcachedUtil.getInstance().getAttribute(key);
			
			if(memcache == null) {
				/*缓存*/

				//java查询
				trainJson = queryTrainList(param);
				logger.info("同程车次编号查询,查询结果jsonStr"+trainJson);
				if(TrainConsts.STATION_ERROR.equals(trainJson)){
					write2Response(resp,TrainConsts.ERROR);
					return;
				}
				if(trainJson == null || TrainConsts.NO_ROBOT.equals(trainJson)){
					write2Response(resp,TrainConsts.ERROR);
					return;
				}if(TrainConsts.ERROR.equals(trainJson)){
					write2Response(resp,TrainConsts.ERROR);
					return;
				}
				if(TrainConsts.NO_DATAS.equals(trainJson)) {
					write2Response(resp, TrainConsts.ERROR);
					return;
				}
				
				try{
					/**线程分布任务可做*/
//						String prePath = req.getSession().getServletContext().getRealPath("/files");
					long startFile=System.currentTimeMillis();
					String fileDir = "//data//cache_files//" + train_date;
					String fileName = Md5Encrypt.md5(key, "gbk") + ".txt";
					String filePath = fileDir + "//" + fileName;
					logger.info("---------------cache_files is filePath="+filePath);
					//创建文件保存接口返回数据
					FileUtil.removeFile(filePath);
					boolean isSucess = FileUtil.createFile(fileDir, fileName, trainJson, "UTF-8");
					//文件生成成功则把文件名写入Memcache
					if(isSucess){
						logger.info("startFile ---> " + startFile);
						MemcachedUtil.getInstance().setAttribute(key, filePath, 5*60*1000);
						logger.info("文件缓存耗时ms"+(System.currentTimeMillis()-startFile));
					}
				}catch(Exception e){
					logger.error("保存查询信息为缓存文件异常！",e);
					write2Response(resp,"ERROR");
				}
			
			} else {

				long start = System.currentTimeMillis();
				String filePath = (String) memcache;
				String fileContent = FileUtil.readFile(filePath, "UTF-8");
				StringBuffer file_str = new StringBuffer();
				file_str.append("读文件耗时[").append(System.currentTimeMillis() - start).append("]ms");
				logger.info(file_str.toString());
				trainJson = fileContent;
			
			}
			
			/*从json串中根据train_code获取train_no*/
			train_no = getTrainNo(trainJson, train_code);
		} 
		
		logger.info("train_no -----> " + train_no);
		if(!StrUtil.isNotEmpty(train_no)) {
			
			write2Response(resp,TrainConsts.ERROR);
			return;
		} else {
			jsonResult.put("train_no", train_no);
		}
		
		/*车次信息查询*/
		Map<String, String> param = new HashMap<String, String>();
		param.put("train_no", train_no);
		
		param.put("method", "getTrainInfo");
		String key = getFileName2(param);
		param.put("from_station_telecode", from_station);
		param.put("to_station_telecode", to_station);
		param.put("depart_date", train_date);
		param.put("channel", channel);
		param.put("interfacePath", "servlet/queryMidStation");
		
		Object memcache = MemcachedUtil.getInstance().getAttribute(key);
		if(memcache == null) {

			/*缓存*/

			//java查询
			trainInfoData = queryTrainInfo(param);
			logger.info("同程车次查询,查询结果jsonStr"+trainInfoData);
			if(TrainConsts.STATION_ERROR.equals(trainInfoData)){
				write2Response(resp,TrainConsts.ERROR);
				return;
			}
			if(trainInfoData == null || TrainConsts.NO_ROBOT.equals(trainInfoData)){
				write2Response(resp,TrainConsts.ERROR);
				return;
			}if(TrainConsts.ERROR.equals(trainInfoData)){
				write2Response(resp,TrainConsts.ERROR);
				return;
			}
			if(TrainConsts.NO_DATAS.equals(trainInfoData)) {
				write2Response(resp, TrainConsts.NO_DATAS);
				return;
			}
			
			try{
				/**线程分布任务可做*/
//					String prePath = req.getSession().getServletContext().getRealPath("/files");
				long startFile=System.currentTimeMillis();
				String fileDir = "//data//cache_files//" + train_date;
				String fileName = Md5Encrypt.md5(key, "gbk") + ".txt";
				String filePath = fileDir + "//" + fileName;
				logger.info("---------------cache_files is filePath="+filePath);
				//创建文件保存接口返回数据
				FileUtil.removeFile(filePath);
				boolean isSucess = FileUtil.createFile(fileDir, fileName, trainInfoData, "UTF-8");
				//文件生成成功则把文件名写入Memcache
				if(isSucess){
					MemcachedUtil.getInstance().setAttribute(key, filePath, 5*60*1000);
					logger.info("文件缓存耗时ms"+(System.currentTimeMillis()-startFile));
				}
				
			}catch(Exception e){
				logger.error("保存查询信息为缓存文件异常！",e);
				write2Response(resp,"ERROR");
			}
		
		} else {

			long start = System.currentTimeMillis();
			String filePath = (String) memcache;
			String fileContent = FileUtil.readFile(filePath, "UTF-8");
			StringBuffer file_str = new StringBuffer();
			file_str.append("读文件耗时[").append(System.currentTimeMillis() - start).append("]ms");
			logger.info(file_str.toString());
			trainInfoData = fileContent;
		
		}
		/*返回查询结果*/
		jsonResult.put("train_info_list", JSONArray.fromObject(trainInfoData));
		write2Response(resp, jsonResult.toString());
	}
	
	public String queryTrainList(Map<String, String> param) {
		String result = TrainConsts.NO_DATAS;
		RobotSetVo robot = null;
		
		try {
			robot = getRobot("java_query", param.get("channel"));
			StringBuffer content = new StringBuffer();
			if(robot == null) {
				logger.info("robot is null then try to start spare robot");
				//随机分配一个备用机器人
				int num = robotServiceImp.updateSpareRobot("java_query",param.get("channel"));
				if(num == 0){
					robot = robotServiceImp.getRobot();
					logger.info("random spare robot to work:"+robot.getRobot_name());
					content.append("[火车票]渠道").append(param.get("channel")).append("，没有分配查询机器人！")
					.append("临时使用").append(robot.getRobot_name()).append("，请尽快安排人员协调查询机器人分配！");
					//获取通知联系人电话并发送短信
					robotServiceImp.queryRobotWarnPhone();
					String phones = robotServiceImp.getPhones();
					robotServiceImp.addRobotWarnMessage(phones, robot.getRobot_name(),content.toString());
				}else{
					robot = getRobot("java_query",param.get("channel"));
				}
			}
			if(robot != null) {
				//此方法临时启用,业务接口更改后可能作废
				wrapInterfacePath(param.get("interfacePath"), robot);
				//再次发起查询
				result = qc.javaQueryData(param,robot);
			}
		} catch (Exception e) {
			logger.info("解析12306新接口异常",e);
			return TrainConsts.ERROR;
		} 
		return result;
	}

	public String queryTrainInfo(Map<String, String> param) {
		String result = TrainConsts.NO_DATAS;
		RobotSetVo robot = null;
		
		try {
			robot = getRobot("java_query", param.get("channel"));
			StringBuffer content = new StringBuffer();
			if(robot == null) {
				logger.info("robot is null then try to start spare robot");
				//随机分配一个备用机器人
				int num = robotServiceImp.updateSpareRobot("java_query",param.get("channel"));
				if(num == 0){
					robot = robotServiceImp.getRobot();
					logger.info("random spare robot to work:"+robot.getRobot_name());
					content.append("[火车票]渠道").append(param.get("channel")).append("，没有分配查询机器人！")
					.append("临时使用").append(robot.getRobot_name()).append("，请尽快安排人员协调查询机器人分配！");
					//获取通知联系人电话并发送短信
					robotServiceImp.queryRobotWarnPhone();
					String phones = robotServiceImp.getPhones();
					robotServiceImp.addRobotWarnMessage(phones, robot.getRobot_name(),content.toString());
				}else{
					robot = getRobot("java_query",param.get("channel"));
				}
			}
			if(robot != null) {
				//此方法临时启用,业务接口更改后可能作废
				wrapInterfacePath(param.get("interfacePath"), robot);
				//再次发起查询
				result = qc.javaQueryTrainData(param,robot);
			}
		} catch (Exception e) {
			logger.info("解析12306新接口异常",e);
			return TrainConsts.ERROR;
		} 
		return result;
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
		selectNums.put("selectNum_tongcheng", 1);
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
	 * 获取车次内部编码
	 * @param trainJson 查询列表结果jsonArray
	 * @param trainCode 车次编号
	 * @return 车次12306内部编码
	 */
	private String getTrainNo(String trainJson, String trainCode) {
		logger.info("getTrainNo");
		ObjectMapper mapper = new ObjectMapper();
		String train_no = null;
		try {
			JsonNode trainArray = mapper.readTree(trainJson);
			if(!trainArray.isMissingNode() && trainArray.size() != 0) {
				for(int i = 0; i < trainArray.size();i++) {
					JsonNode train = trainArray.get(i);
					String train_code = train.path("train_code").getTextValue();
					if(trainCode.equals(train_code.trim())) {
						train_no = train.path("train_no").getTextValue();
						break;
					}
				}
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return train_no;
	}
	
	/**
	 * 替换业务接口路径(应对库中存放的业务接口地址可能错误)
	 * @param interfacePath
	 * @param robot
	 */
	private void wrapInterfacePath(String interfacePath, RobotSetVo robot) {
		String robot_url = robot.getRobot_url();
		Pattern pattern = Pattern.compile("http://([0-9]{1,3}\\.){3}[0-9]{1,3}:\\d+/");
		Matcher matcher = pattern.matcher(robot_url);
		if(matcher.find()) {
			String scheme_ip_port = matcher.group();
			interfacePath = interfacePath == null ? "" : interfacePath;
			robot_url = scheme_ip_port + interfacePath;
			robot.setRobot_url(robot_url);
		}
	}
	
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
	
	/**
	 * 获取文件名(eg:javarobot_北京_上海_getTrainNo)
	 * @return
	 */
	protected String getFileName1(Map<String, String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("javarobot_").append(map.get("from_station"))
		  .append("_")
		  .append(map.get("arrive_station"))
		  .append("_")
		  .append(map.get("method"));
		return sb.toString();
	}
	
	/**
	 * 获取文件名(eg:javarobot_240000G1012300_getTrainInfo)
	 * @return
	 */
	protected String getFileName2(Map<String, String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("javarobot_").append(map.get("train_no"))
		  .append("_")
		  .append(map.get("arrive_station"))
		  .append("_")
		  .append(map.get("method"));
		return sb.toString();
	}
}
