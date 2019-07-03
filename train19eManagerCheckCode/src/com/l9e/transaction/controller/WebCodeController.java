package com.l9e.transaction.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.redis.RedisService;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.OnlineMessage;
import com.l9e.transaction.vo.Picture;
import com.l9e.transaction.vo.PlayCardResult;
import com.l9e.transaction.vo.UserInfo;
import com.l9e.util.SelectSort;
import com.l9e.util.StringUtil;
/**
 * 页面打码
 */
@Controller
@RequestMapping("/web")
public class WebCodeController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(WebCodeController.class);

	@Resource
	RobotCodeService robotService;
	
	@Resource
	RedisService redisService;
	
	@RequestMapping("webCodePage.do")
	public String webCode(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String username = loginUserVo.getUsername();
		
		UserInfo userInfo=robotService.queryUserInfo(username);
		logger.info("用户名："+userInfo.getUsername()+";pwd:"+userInfo.getPwd());
		
		if(userInfo.getLogin_status()!=0 ){
			logger.info("login than one ");
			return null;
		}
		if(!StringUtil.isNotEmpty(username)){
			return null;
		}else{
			logger.info(username + " update login_status");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("login_status", "3");
			map.put("username", username);
			robotService.updateUserInfo(map);
			
			//request.getSession().setAttribute("userInfo", userInfo);
			redisService.setVal(username+"userInfo", userInfo, 1*60*60*1000);
			
			String last_login_time = userInfo.getLast_login_time();
			if(last_login_time!=null){
				PlayCardResult pcr = robotService.queryUserPlayCardTotal(username);
				request.setAttribute("user_total_num", pcr.getTotal_num());
				request.setAttribute("user_right_num", pcr.getRight_num());
				request.setAttribute("user_error_num", pcr.getError_num());
			}else{
				
			}
		}
		return "login/webCodePage";
	}
	
	
	@RequestMapping("startCodePage.do")
	public String startCode(HttpServletRequest request,HttpServletResponse response){
		String status = request.getParameter("status");
		String pic_id = request.getParameter("pic_id");
		
		//两次未打码则暂停
		String noplaycode = request.getParameter("noplaycode");
		if(StringUtil.isNotEmpty(noplaycode) && StringUtil.isNotEmpty(pic_id)){
			request.setAttribute("noplaycode", "1");
		}else{
			request.setAttribute("noplaycode", "0");
		}
		
		logger.info("status values:"+status);
		logger.info("pic_id:"+pic_id);
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String username = loginUserVo.getUsername();
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(username);
		userInfo.setPwd(loginUserVo.getPwd());
		redisService.setVal(username+"userInfo", userInfo, 1*60*60*1000);
//		request.getSession().setAttribute("userInfo",userInfo);
		
//		int noplay_num = 0;
//		if(null != MemcachedUtil.getInstance().getAttribute("noplay_code_num_"+username)){
//			noplay_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("noplay_code_num_"+username)));
//			logger.info(username+" noplay_num:"+noplay_num);
//		}
//		if("restart".equals(status) && StringUtil.isNotEmpty(pic_id)){
//			MemcachedUtil.getInstance().setAttribute("noplay_code_num_"+username, ++noplay_num, 30*1000);
//		}
		
		Picture picture = null;
		try {
			if(!"stop".equals(status)){
				//用户开始查询动态码，更新用户状态为打码状态
				robotService.updateAndFindUserStatus(userInfo.getUsername());
				//系统设置--查看当前打码权重开关
				String code_weight_switch = robotService.querySystemValue("code_weight_switch");//打码权重开关：1开启 0关闭
				//查询该动态码picture信息同时更新该动态码被占用
				picture = robotService.updateAndFindPictureStatus(loginUserVo.getDepartment(), code_weight_switch);
				status = "start";
			}else{
				status="stop";
//				MemcachedUtil.getInstance().setAttribute("noplay_code_num_"+username, 0, 10*1000);
			}
		} catch (Exception e) {
			logger.error("find picturelist dberror:",e);
		}
		request.setAttribute("picture", picture);
		request.setAttribute("status", status);
		return "login/webCodePage";
	}
	
	@RequestMapping("submitCode.do")
	public void submitCode(HttpServletRequest request,HttpServletResponse response){
		/***************************获取Session*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		
		logger.info(loginUserVo.getUsername() + " web submit picture verifycode start");
		String verifycode = request.getParameter("result");
		logger.info("verifycode:"+verifycode);
		String pic_id = request.getParameter("pic_id");
		String finishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String,String> map = new HashMap<String,String>();
		//打码结果排序
		verifycode = sortVerifycode(verifycode);
		map.put("user_pic_status", "0");
		try {
			if(StringUtil.isNotEmpty(pic_id)){
				if(StringUtil.isNotEmpty(verifycode)){
					map.put("status", "11");
					map.put("verify_code", verifycode);
					map.put("opt_ren", loginUserVo.getUsername());
					map.put("finish_time", finishTime);
				}else{
					map.put("status", "00");
				}
				map.put("pic_id", pic_id);
				logger.info(map.toString());
				robotService.updatePicturesMap(map);
			}
			int result_status = 0;
			try{
				result_status = robotService.findResultStatus(loginUserVo.getUsername());
			}catch(Exception el){
				logger.info("query user play code status error",el);
			}
			
			OnlineMessage onl=robotService.getOnlineMessage();
			if(result_status==0){
				String str="false*"+onl.getOnline_num()+"*"+onl.getUncode_num()+"*";
				writeN2Response(response,str);
				System.out.println("失败"+str);
			}else{
				String str="true*"+onl.getOnline_num()+"*"+onl.getUncode_num()+"*";
				writeN2Response(response,str);
				System.out.println("成功"+str);
			}
		} catch (Exception e) {
			logger.error("update picture dberror:",e);
			writeN2Response(response,"false");
		}
		
//		UserInfo userInfo = (UserInfo)request.getSession().getAttribute("userInfo");
		UserInfo userInfo = (UserInfo)redisService.getVal(loginUserVo.getUsername()+"userInfo");
		
		Picture picture = null;
		try {
			//用户开始查询动态码，更新用户状态为打码状态
			robotService.updateAndFindUserStatus(userInfo.getUsername());
			//系统设置--查看当前打码权重开关
			String code_weight_switch = robotService.querySystemValue("code_weight_switch");//打码权重开关：1开启 0关闭
			//查询该动态码picture信息同时更新该动态码被占用
			picture = robotService.updateAndFindPictureStatus(loginUserVo.getDepartment(), code_weight_switch);
		} catch (Exception e) {
			logger.error("find picturelist dberror:",e);
		}
		request.setAttribute("picture", picture);
		request.setAttribute("status", "start");
		
	}
	
	public String sortVerifycode(String verifycode){
		SelectSort ss = new SelectSort(verifycode.length());
		int num = verifycode.length();
		for(int i=0; i<num; i++){
			char code = verifycode.charAt(i);
			ss.insert(code);
		}
		ss.selectSort();	
		ss.display();
		return ss.getResult();
	}
	
	public static void main(String[] args) {
		WebCodeController wc = new WebCodeController();
		wc.sortVerifycode("43261");
	}
}
