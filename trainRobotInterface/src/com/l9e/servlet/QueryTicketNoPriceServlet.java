package com.l9e.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.l9e.common.TrainConsts;
import com.l9e.service.RobotServiceImp;
import com.l9e.util.FileUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;
import com.l9e.vo.RobotSetVo;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * 查询余票主控路口
 * 
 * */
public class QueryTicketNoPriceServlet extends HttpServlet{
	private static final long serialVersionUID = 3264299111876887524L;
	
	RobotServiceImp robotServiceImp=new RobotServiceImp();
	private static Logger logger=Logger.getLogger(QueryTicketNoPriceServlet.class);
	private Map<String,Integer> selectNums; //多类型机器人分配数
	
	private Boolean cache_file = false;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
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
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		/**分发参数*/
		String channel=req.getParameter("channel");
		/**业务参数*/
		String from_station =req.getParameter("from_station");
		String arrive_station =req.getParameter("arrive_station");
		String travel_time =req.getParameter("travel_time");
		//查询余票
		String check_spare_num = req.getParameter("check_spare_num");
		StringBuffer log_req = new StringBuffer();
		//日志
		log_req.append("query_noprice_channel[").append(channel).append("]").append("param:")
			.append("travel_time[").append(travel_time).append("],from_station[")
			.append(from_station).append("],arrive_station[").append(arrive_station).append("]");
		logger.info(log_req.toString());
		Map<String,String> param=new HashMap<String, String>();
		param.put("from_station", from_station);
		param.put("arrive_station", arrive_station);
		param.put("travel_time", travel_time);
		param.put("method", "DGTrain");
		String key = getFileName(param);
		param.put("channel", channel);
		Object memcache = MemcachedUtil.getInstance().getAttribute(key);
		if("true".equals(check_spare_num)){
			memcache = null;
		}
		if(null == memcache){
			String jsonStr = TrainConsts.NO_DATAS;
			//java查询
			jsonStr = queryTrainNoPriceInfo(param, cache_file);
			if(TrainConsts.NO_ROBOT.equals(jsonStr) || TrainConsts.ERROR.equals(jsonStr)){
				jsonStr = queryTrainNoPriceInfo(param,cache_file);
			}
			if(jsonStr == null || TrainConsts.NO_ROBOT.equals(jsonStr)){
				write2Response(resp,TrainConsts.ERROR);
				return;
			}
			try{
				if(jsonStr.contains("\"code\":\"000\"") && cache_file){
					/**线程分布任务可做*/
					String prePath = req.getSession().getServletContext().getRealPath("/files");
					long startFile=System.currentTimeMillis();
					String fileDir = prePath + "/" + travel_time;
					String fileName = Md5Encrypt.md5(key, "gbk") + ".txt";
					String filePath = fileDir + "/" + fileName;
					logger.info("filePath="+filePath);
					//创建文件保存接口返回数据
					FileUtil.removeFile(filePath);
					boolean isSucess = FileUtil.createFile(fileDir, fileName, jsonStr, "UTF-8");
					//文件生成成功则把文件名写入Memcache
					if(isSucess){
						MemcachedUtil.getInstance().setAttribute(key, filePath, 5*60*1000);
						logger.info("文件缓存耗时ms"+(System.currentTimeMillis()-startFile));
					}
				}
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
			file_str.append("读文件耗时[").append(System.currentTimeMillis() - start).append("]ms");
			logger.info(file_str.toString());
			write2Response(resp,fileContent);
		}
	}
	
	
	//机器人新接口
	public String queryTrainNoPriceInfo(Map<String,String> param,boolean cache_file){
		QueryControll qc = new QueryControll();
		String jsonStr=TrainConsts.NO_DATAS;
		RobotSetVo robot = null;
		try {
			robot = getRobot("java_query",param.get("channel"));
			StringBuffer content = new StringBuffer();
			if(null==robot){
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
					//再次发起查询
					jsonStr = qc.javaQueryNoPriceData(param,robot,cache_file);
				}else{
					robot = getRobot("java_query",param.get("channel"));
					jsonStr = qc.javaQueryNoPriceData(param,robot,cache_file);
				}
			}else{
				jsonStr = qc.javaQueryNoPriceData(param,robot,cache_file);
			}
		} catch (Exception e) {
			logger.info("解析12306新接口异常",e);
			try{
				logger.info("robot is not null exception robot_name:"+robot.getRobot_name());
				int ro = exceptionManager(robot,param);
				if(ro == 0){
					jsonStr = qc.javaQueryNoPriceData(param,robot,cache_file);
				}else{
					robot = getRobot("java_query",param.get("channel"));
					jsonStr = qc.javaQueryNoPriceData(param,robot,cache_file);
				}
			}catch(Exception ef){
				logger.error("异常处理机制异常，没有发起短信通知", ef);
				return TrainConsts.ERROR;
			}
		}
		return jsonStr;
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
