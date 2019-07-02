/**
 * 
 */
package com.l9e.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.l9e.service.RobotServiceImp;
import com.l9e.util.WorkerQueue;
import com.l9e.vo.WorkerVo;
import com.unlun.commons.res.Config;



/**
 * @author salesforce.g
 *
 */
public class InitConfigServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	RobotServiceImp robotServiceImp=new RobotServiceImp();
	
	public void init(){
		try{
			logger.info("START | 加载配置文件");
			
			String databasePath = this.getClass().getClassLoader().getResource("database.properties").getPath();
            Config.setConfigResource(databasePath);
            
            logger.info("END   | 加载配置文件");
            
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");   
	  		Properties p = new Properties();   
	  		try {   
	  		  p.load(inputStream);   
	  		} catch (IOException e1) {   
	  		  e1.printStackTrace();   
	  		}
	  		BaseServlet.SOUKD_CHECKCODE = p.getProperty("SOUKD_CHECKCODE");
	  		BaseServlet.SOUKD_METHOD_DGTRAIN = p.getProperty("SOUKD_METHOD_DGTRAIN");
	  		BaseServlet.SOUKD_USERID = p.getProperty("SOUKD_USERID");
	  		BaseServlet.SOUKD_URL = p.getProperty("SOUKD_URL");
	  		if(!"".equals(p.getProperty("12306_url")) && null!=p.getProperty("12306_url")){
	  			BaseServlet._url_12306 = p.getProperty("12306_url");
	  		}
	  		
			BufferedInputStream bufferedInputStream=new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream("station_name.js"));
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
		    for(int i=1;i<size;i++){
		    	String[] keyValue=stationNamesArr[i].split("\\|");
			    logger.info("key:"+keyValue[1]+"__value:"+keyValue[2]);
		    	BaseServlet.stationName.put(keyValue[1], keyValue[2]);
		    	
		    	BaseServlet.backStationName.put(keyValue[2], keyValue[1]);
		    }
		}catch(Exception e){
			logger.info("初始化配置文件失败");
			e.printStackTrace();
		}
		
		//启动Timer,向机器人内存队列加入机器,每30s执行一次
		Timer timer = new Timer();
		final int maxSize=100;
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				int queueSize=WorkerQueue.getInstance().size();
				logger.info("从数据库获取查询机器,当前内存队列大小："+queueSize);
				if(queueSize<maxSize) { //队列大小超过5000,不再补充
					logger.info("查询机器人补充队列！！！！！");
					List<WorkerVo> list = new ArrayList<WorkerVo>();
					Map<String, Object> paramMap=new HashMap<String,Object>();
					paramMap.put("worker_type", "9");
					paramMap.put("worker_status", "00");
					paramMap.put("limitCount", maxSize-queueSize);
					try {
						list = robotServiceImp.queryRobotNewList(list,paramMap);
					} catch (Exception e) {
						logger.info("查询异常：：", e);
					}
					for (WorkerVo workerVo : list) {
						WorkerQueue.getInstance().push(workerVo);
					}
				}else {
					logger.info("队列大小超过"+maxSize+",不再补充");
				}
				
			}
		}, 5 * 1000, 5 * 1000);
				
		logger.info("————————————任务已启动————————————");
		
	}
}
