package com.l9e.transaction.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.redis.RedisService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.OnlineMessage;
import com.l9e.transaction.vo.Picture;
import com.l9e.transaction.vo.PlayCardResult;
import com.l9e.transaction.vo.UserInfo;
import com.l9e.util.MemcachedUtil;


//与打码器交互
@Controller
public class OptVerifyController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(OptVerifyController.class);
	@Resource
	RobotCodeService robotService;
	
	@Resource
	RedisService redisService;
	
	@Value("#{propertiesReader[pic_root]}")
	private String pic_root;
	
	@RequestMapping("/optverify.do")
	@ResponseBody
	public void login(HttpServletRequest request,HttpServletResponse response){
		String action = request.getParameter("action");
		String username = request.getParameter("username");
		//logger.info(username+"+++++++++++");
		if("close".equals(action)){
			//关闭打码器，退出
			logger.info(username +" quit system!");
			//TODO 清除redis里面的token和userInfo信息
			redisService.delVal(username+"userInfo");
			//request.getSession().setAttribute("userInfo",null);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("login_status", "0");
			map.put("username", username);
			robotService.updateUserInfo(map);
			
			return;
		}
		//UserInfo userInfo = (UserInfo)request.getSession().getAttribute("userInfo");
		//logger.info(username+"---------------");
		UserInfo userInfo = (UserInfo) redisService.getVal(username+"userInfo");
		//logger.info(username+"---------------"+userInfo.getUsername());
		if(userInfo==null){
			logger.info("session timeout");
			writeN2Response(response,"relogin");
			return;
		}
		//应该是一个自定义的类吧，按照命名习惯可能是个连接数据库的类
		try{
			if("lock".equals(action)){
				yardLock(request,response,userInfo);
				return;
			}else if("find".equals(action)){
				//查询图片信息并更新状态
				queryPicInfoAndModStatus(request,response,userInfo, username);
				return;
			}else if("update".equals(action)){
				//提交打码值
				submitPlayCardsValue(request,response,userInfo);
				return;
			}else if("calibration".equals(action)){
				//核验打码错误数、打码正确数、打码总数
				PlayCardResult pcr = robotService.queryUserPlayCardTotal(userInfo.getUsername());
				
				//系统设置--查看当前打码器
				String code_back_result = robotService.querySystemValue("code_back_result");//打码器打码结果返回时间限制（秒）
				
//				String user_code_time_limit = userInfo.getGet_code_time_limit();
				String user_code_time_limit = robotService.queryUserCodeTimeLimit(userInfo.getUsername());
				if(user_code_time_limit==null || StringUtils.isEmpty(user_code_time_limit)){
					String sys_code_time_limit = robotService.querySystemValue("code_time_limit");//打码器拉码时间间隔（毫秒ms）
					user_code_time_limit = sys_code_time_limit;
				}
				
				String str = pcr.getTotal_num()+"*"+pcr.getRight_num()+"*"+pcr.getError_num()+"*"
							 +user_code_time_limit+"*"+code_back_result+"*"+pcr.getOver_num()+"*";
				//logger.info(userInfo.getUsername() + " request calibration code:"+str);
				writeN2Response(response,str);
			}else if("stop".equals(action)){
				//暂停
				logger.info(userInfo.getUsername()+"：stop play code");
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("login_status", "3");
				map.put("username", userInfo.getUsername());
				robotService.updateUserInfo(map);
				//更新图片锁定状态为空
				if(userInfo.getPicture()!=null){
					if(userInfo.getPicture().getFinish_time()==null){
						logger.info(userInfo.getUsername()+"：stop play code, update pic status:"+userInfo.getPicture().getPic_id());
						robotService.updatePictures(userInfo.getPicture());
					}
				}
			}
		}catch(IOException e){
			logger.error("",e);
		}
	}
	
	public void queryPicInfoAndModStatus(HttpServletRequest request, HttpServletResponse response,UserInfo userInfo, String username) throws IOException{
		Picture picture = null;
		OutputStream os = null;
		
		String token = request.getParameter("token");
		String tokenRedis = (String) redisService.getVal(userInfo.getUsername()+"token");
		
		if(token==null || tokenRedis==null || !token.equals(tokenRedis)){
			logger.info("[query code]token:"+token+", tokenRedis:"+tokenRedis);
			logger.info("【token error】have no picture:" + userInfo.getUsername() + ",the pic_id:"+picture);
			writeN2Response(response,"relogin");//重新登录
			return;
		}
		
		Map<String,String> sysInfo=robotService.querySysInfo();
		String code_channel=sysInfo.get("code_channel");
		if(TrainConsts.CODE_RG.equals(code_channel)||TrainConsts.CODE_LZANDRG.equals(code_channel)||TrainConsts.CODE_DAMATUANDRG.equals(code_channel)){
			try {
				//用户开始查询动态码，更新用户状态为打码状态
				robotService.updateAndFindUserStatus(userInfo.getUsername());
				//系统设置--查看当前打码权重开关
				String code_weight_switch = robotService.querySystemValue("code_weight_switch");//打码权重开关：1开启 0关闭
				//查询该动态码picture信息同时更新该动态码被占用
				picture = robotService.updateAndFindPictureStatus(userInfo.getDepartment(), code_weight_switch);
				//logger.info(userInfo.getUsername()+" get code from mq : the pic_id:"+picture.getPic_id() + ", pic_filename:" + picture.getPic_filename());
				if(null==picture || picture.getPic_filename()==null){
					logger.error("【过滤码】have no picture:" + userInfo.getUsername() + ",the pic_id:"+picture);
					writeN2Response(response,"false");
					
				}else{
					logger.info(userInfo.getUsername()+"user get image:=======>>>"+picture.getPic_filename());
					userInfo.setLogin_status(1);
					userInfo.setPicture(picture);
					logger.info(username+"===="+userInfo.getUsername()+"----"+userInfo.getPicture());
					redisService.setVal(username+"userInfo", userInfo, 1*60*60*1000);
//					request.getSession().setAttribute("userInfo", userInfo);
					os = response.getOutputStream(); 
					//注意 地方
					//File file = new File(request.getSession().getServletContext().getRealPath(picture.getPic_filename())); 
					File file = new File(pic_root+picture.getPic_filename()); 
					FileInputStream fips = new FileInputStream(file);   
				    byte[] btImg = readStream(fips);   
				    if(btImg.length<800){
				    	logger.info(picture.getPic_filename()+" image length："+btImg.length);
				    	writeN2Response(response,"false");
				    }
				    os.write(btImg);   
				}
			} catch (Exception e) {
				logger.error("get code erxception: ",e);
				writeN2Response(response,"false");
			}finally{
				 if(os!=null){
					 os.flush();   
					 os.close();
				 }
			}
		}else{
			logger.info("人工状态已关闭，无法获取验证码图片");
		}
	}
	
	public void yardLock(HttpServletRequest request, HttpServletResponse response,UserInfo userInfo){
		logger.info(userInfo.getUsername() + " lock verifycode start");
		String result = "fail";
		String picId = request.getParameter("picId");
		Object optRen = MemcachedUtil.getInstance().getAttribute(picId+"_optRen");
		if(optRen == null){
			MemcachedUtil.getInstance().setAttribute(picId+"_optRen",userInfo.getUsername(), 10*60*1000);
			result = "ok";
			logger.info("lock verifycode success");
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		try{
			response.getWriter().print(result);
			response.getWriter().close();
		}catch(Exception e){
			logger.error("yardLock方法，输出结果失败！",e);
		}
	}
	
	public void submitPlayCardsValue(HttpServletRequest request, HttpServletResponse response,UserInfo userInfo){
		String verifycode = request.getParameter("verifycode");

		String finishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Picture picture =userInfo.getPicture(); 
		if(picture != null ){
			try {
				logger.info(userInfo.getUsername() + " submit picture verifycode :"+verifycode);
				picture.setVerify_code(verifycode);
				picture.setOpt_ren(userInfo.getUsername());
				picture.setFinish_time(finishTime);
				picture.setDepartment(userInfo.getDepartment());
				robotService.updatePictures(picture);
				int result_status = 0;
				try{
					result_status = robotService.findResultStatus(userInfo.getUsername());
				}catch(Exception el){
					logger.info("query user play code status error",el);
				}
				
				OnlineMessage onl=robotService.getOnlineMessage();
				String str= "";
				//token--username+100以内的随机数，md5加密后小写
				String token = DigestUtils.md5Hex(userInfo.getUsername()+new Random().nextInt(100)).toLowerCase();
				if(result_status==0){
					str= "false*"+onl.getOnline_num()+"*"+onl.getUncode_num()+"*"+token;
				}else{
					str= "true*"+onl.getOnline_num()+"*"+onl.getUncode_num()+"*"+token;
				}
				redisService.setVal(userInfo.getUsername()+"token", token, 5000);
				writeN2Response(response, str);
			} catch (Exception e) {
				logger.error("update picture dberror:",e);
				writeN2Response(response,"false");
			}
		}
	}
	
	private byte[] readStream(InputStream inStream) {   
        ByteArrayOutputStream bops = new ByteArrayOutputStream();   
        int data = -1;   
        try {   
            while((data = inStream.read()) != -1){   
                bops.write(data);   
            }   
            return bops.toByteArray();   
        }catch(Exception e){   
            return null;   
        }   
    }
}
