package com.l9e.transaction.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpUtils;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.RobotSetNewDao;
import com.l9e.transaction.service.RobotSetNewService;
import com.l9e.util.HttpPostRobotUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.HttpUtilNew;

@Service("robotSetNewService")
public class RobotSetNewServiceImpl implements RobotSetNewService {
	
	private Logger logger=Logger.getLogger(this.getClass());
	@Resource
	private RobotSetNewDao robotSetNewDao;
	@Override
	public int queryRobotSetCount(Map<String, Object> paramMap) {
		return robotSetNewDao.queryRobotSetCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryRobotSetList(
			Map<String, Object> paramMap) {
		return robotSetNewDao.queryRobotSetList(paramMap);
	}

	@Override
	public int changeStatus(Map<String, String> worker, Map<String, String> log) {
		robotSetNewDao.insertLog(log);
		return robotSetNewDao.changeStatus(worker);
	}

	@Override
	public int changeAlipayAccount(Map<String, String> worker) {
		return robotSetNewDao.changeAlipayAccount(worker);
	}
	
	@Override
	public int changeAlipayAccountType(Map<String, String> worker) {
		return robotSetNewDao.changeAlipayAccountType(worker);
	}
	
	@Override
	public List<Map<String, Object>> queryWorkerLog(String workerId) {
		return robotSetNewDao.queryWorkerLog(workerId);
	}

	@Override
	public Map<String, String> queryRobotSetInfo(String workerId) {
		return robotSetNewDao.queryRobotSetInfo(workerId);
	}

	@Override
	public List<Map<String, Object>> queryReportLog(String workerId) {
		return robotSetNewDao.queryReportLog(workerId);
	}

	@Override
	public void deleteByWorkId(Map<String, String> worker,
			Map<String, String> log) {
		robotSetNewDao.insertLog(log);
	    robotSetNewDao.deleteByWorkId(worker);
	}

	@Override
	public List<Map<String, Object>> queryZhanghaoList() {
		return robotSetNewDao.queryZhanghaoList();
	}

	@Override
	public void addVerificationCode(Map<String, Object> paramMap) {
		robotSetNewDao.addVerificationCode(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryVerificationCode(Map<String, Object> paramMap) {
		return robotSetNewDao.queryVerificationCode(paramMap);
	}

	@Override
	public String   modify12306Host(Map<String, String> worker,
			Map<String, String> log) {
		// TODO Auto-generated method stub
		robotSetNewDao.insertLog(log);
		String url=worker.get("work_ext");//机器人路径
		String ip=worker.get("ip");//12306pc的Host
		
		Map<String, String> log1 = new HashMap<String, String>();
		
		log1.put("opt_person",log.get("opt_person"));
		log1.put("worker_id", log.get("worker_id"));
		
		
		Map<String, String> maps = new HashMap<String,String>();
		maps.put("ScriptPath", "modifyHost.lua");
		
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));//任务完成后，机器人回应该请求时携带
		maps.put("Timeout", "240000");//同步超时时间
		maps.put("ParamCount", String.valueOf(1));
		StringBuffer sb = new StringBuffer();
		sb.append(ip+"|");
		sb.append("modifyHost");
		maps.put("Param1", sb.toString());
		
		String param=null;
		try {
			param=CreateUrl("",maps,"utf-8");
		} catch (Exception e1) {
			logger.info("参数：",e1);
		}
		logger.info("URL->"+url+"?"+param);//参数未编码
		 String reqResult = null;
	    try {
				reqResult = HttpPostRobotUtil.sendAndRecive(url, param);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				 logger.info("请求异常:",e1);
			}
	    logger.info("返回结果:"+reqResult);
	    
	    if (StringUtils.isEmpty(reqResult)) {
	    	 return "FAILURE";
		}
	    reqResult = reqResult.replace("[\"{", "[{");
		reqResult = reqResult.replace("}\"]", "}]");
		reqResult = reqResult.replace("\\\"", "\"");
		logger.info(url+" reValue:"+reqResult);
		
		StringBuffer content =new StringBuffer();
		content.append("机器人返回结果：");
		content.append(reqResult);
		log1.put("content", content.toString());
		
		robotSetNewDao.insertLog(log1);//插入机器人返回日志
		
		////////////////////////////////////////////////////////////////////
		boolean status = false;
		boolean flag = false;
		String data = null;
		try {
			JSONObject object=JSONObject.fromObject(reqResult);
			List<Map<String, Object>>  errinfo=object.getJSONArray("ErrorInfo");
			String message;
			for(Map<String, Object> map :errinfo){
				status=(Boolean) map.get("status");
				flag=(Boolean) map.get("flag");
				message=(String)map.get("message");
				data=(String)map.get("data");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 logger.info(url+"解析异常:",e);
		}
		/////////////////////////////////////////////////////////////////////////////
		
		 if(status&&flag){
			 logger.info("修改成功："+data);
			
			 return "SUCCESS";
		 }else {
			 logger.info("修改失败："+data);
			 return "FAILURE";
		}
	
	}

	/**
	 * @param gateway
	 * @param params
	 * @param charSet
	 * @return
	 * @throws Exception
	 */
	public static  String CreateUrl(String gateway,Map params,String charSet) throws Exception{
		
		String parameter = "";
		if (StringUtils.isNotEmpty(gateway) && gateway.indexOf("?") < 0) {
			parameter = parameter + "?";
		}
		List keys = new ArrayList(params.keySet());

		for (int i = 0; i < keys.size(); i++) {
			String value = (String) params.get(keys.get(i));
			if (value == null || value.trim().length() == 0) {
				continue;
			}

			parameter = parameter
					+ (i == 0 && gateway.indexOf("?") < 0 ? "" : "&")
					+ keys.get(i) + "=" + value;

		}
        
		return parameter;
	}
	
  public static void main(String[] args) {
	  Map<String, String> maps = new HashMap<String,String>();
		maps.put("ScriptPath", "modifyHost.lua");
		
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));//任务完成后，机器人回应该请求时携带
		maps.put("Timeout", "240000");//同步超时时间
		maps.put("ParamCount", String.valueOf(1));
		StringBuffer sb = new StringBuffer();
		sb.append(""+"|");
		sb.append("modifyHost");
		maps.put("Param1", sb.toString());
		
		String param=null;
		try {
			param=CreateUrl("",maps,"utf-8");
		} catch (Exception e1) {
	//	logger.info("参数：",e1);
		}
		
		System.out.println("url:"+"http://139.196.46.27:8091/RunScript"+"?"+param);
	    String reqResult=HttpUtil.sendByPost("http://139.196.46.27:8091/RunScript", param, "utf-8");
	    System.out.println("返回结果:"+reqResult);
	    
}
	
}
