package com.l9e.transaction.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.redis.RedisService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.PlayCardResult;
import com.l9e.transaction.vo.UserInfo;
import com.l9e.util.DateUtil;



@Controller
public class LoginCodeController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(LoginCodeController.class);
	@Resource
	RobotCodeService robotService;
	
	@Resource
	RedisService redisService;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public void login(HttpServletRequest request,HttpServletResponse response){
		/***************************获取用户名和密码*****************************/
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String codeversion = request.getParameter("codeversion");//用户所使用的打码版本号
		UserInfo userInfo=robotService.queryUserInfo(username);
		logger.info("用户名："+userInfo.getUsername()+";pwd:"+userInfo.getPwd()+";login_status:"+userInfo.getLogin_status()+";code_version:"+codeversion);
		String diff_time = "10";
		if(null != userInfo.getLast_login_time()){
			String last_time = DateUtil.stringToString(userInfo.getLast_login_time(),DateUtil.DATE_HM);
			String now_time = DateUtil.dateToString(new Date(), DateUtil.DATE_HM);
			diff_time = DateUtil.minuteDiff(now_time, last_time);
		}
		
		//系统设置--查看当前打码器的可使用版本号
		String code_version = robotService.querySystemValueOpen("code_version");
		String last_code_version = robotService.querySystemValueOpen("last_code_version");//查看上一个打码器的可使用版本号
		if(code_version!=null){
			if(last_code_version==null){
				if(codeversion==null || StringUtils.isEmpty(codeversion) || !codeversion.equals(code_version)){
					logger.info(username + " ~~ code version is wrong! 系统设置版本："+code_version+",用户打码传来版本："+codeversion);
					writeN2Response(response, "false");
					return;
				}
			}else{
				if(codeversion==null || StringUtils.isEmpty(codeversion) || (!codeversion.equals(code_version) && !codeversion.equals(last_code_version))){
					logger.info(username + " ~~ code version is wrong! 系统设置版本："+code_version+"/"+last_code_version+",用户打码传来版本："+codeversion);
					writeN2Response(response, "false");
					return;
				}
			}
			
		}
		
		if(userInfo.getLogin_status() !=0 && Integer.valueOf(diff_time) < 5){
			logger.info(username + "login than one ");
			writeN2Response(response, "false");
			return;
		}
		userInfo.setLogin_status(3);
		if(username==null||pwd==null||userInfo==null||!username.equals(userInfo.getUsername())||
				!pwd.equals(userInfo.getPwd())){
			writeN2Response(response, "false");
		}else{
			logger.info(username + " update login_status");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("login_status", "3");
			map.put("username", username);
			map.put("user_code_version", codeversion);//用户打码器版本号
			robotService.updateUserInfo(map);
			
			try {
				/**记录操作开始*/
				Date now = new Date();
				String begin = DateUtil.dateToString(now,DateUtil.DATE_FMT4);
				String end= DateUtil.dateToString(DateUtil.dateAddDays(now, 1),DateUtil.DATE_FMT4);
				Map<String,String> param=new HashMap<String,String>();
				param.put("agent_id", username);
				param.put("begin", begin);
				param.put("end", end);
				int count=robotService.queryLoginCountById(param);
				if(count==0){
					param.put("hourNum", now.getHours()+"");
					robotService.insertToHistoryLogin(param);
					logger.info("登入记录,SUCCESS,"+username);
				}else{
					//去重
					logger.info("重复登入,免记录,"+username);
				}
				
			} catch (Exception e) {
				logger.info("登入次数记录异常"+e);
				e.printStackTrace();
			}
			
			//TODO 将session改变为redis
			redisService.setVal(username+"userInfo", userInfo, 1*60*60*1000);
//			UserInfo userInfo11 = (UserInfo) redisService.getVal(username+"userInfo");
//			logger.info(userInfo+"-----------------"+userInfo11.getUsername());
			
			//request.getSession().setAttribute("userInfo", userInfo);
			//token--username+100以内的随机数，md5加密后小写
			String token = DigestUtils.md5Hex(userInfo.getUsername()+new Random().nextInt(100)).toLowerCase();
			String last_login_time = userInfo.getLast_login_time();
			if(last_login_time!=null){
				PlayCardResult pcr = robotService.queryUserPlayCardTotal(username);
				//返回值改为：true*总数*正确数*错误数*超时数*---->true*总数*正确数*错误数*超时数*token
				String str = "true*"+pcr.getTotal_num()+"*"+pcr.getRight_num()+"*"+pcr.getError_num()+"*"+pcr.getOver_num()+"*"+token;
				robotService.updateUserInfoLoginTime(username);
				writeN2Response(response,str);
			}else{
				//返回值改为：true---->true|token
//				writeN2Response(response, "true|"+token);
				writeN2Response(response, "true");
			}
			redisService.setVal(username+"token", token, 5000);
		}
	}
	
	
	public static void main(String[] args) {
//		String str = "2015-03-19 19:00:00";
//		System.out.println(str.split(" ")[1]);
//		
		String now_time = DateUtil.dateToString(new Date(), DateUtil.DATE_HM);
		String diff_time = DateUtil.minuteDiff(now_time, "15:30");
		System.out.println(diff_time);
		
		System.out.println(DateUtil.stringToString("2015-03-19 19:00:00",DateUtil.DATE_HM));
		
		
		String token = "bf3625ca804b13d6c0cdd4254e2e25ba";
		String tokenRedis = "bf3625ca804b13d6c0cdd4254e2e25ba";
		System.out.println(token==null);
		System.out.println(tokenRedis==null);
		System.out.println(!token.equals(tokenRedis));
		System.out.println(token!=tokenRedis);
		System.out.println(token==null || tokenRedis==null || !token.equals(tokenRedis) || token!=tokenRedis);
		if(token==null || tokenRedis==null || !token.equals(tokenRedis) || token!=tokenRedis){
			System.out.println("aaaaaaaa");
		}
	}
}
