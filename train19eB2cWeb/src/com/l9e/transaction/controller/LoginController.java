package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.sign.InjectSign;
import com.jiexun.iface.sign.SignService;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.checkCode.RandomValidateCode;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.MobileMsgUtil;

/**
 * 登录
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	protected static final Logger logger = Logger.getLogger(LoginController.class);
	
	@Resource
	private UserInfoService userInfoService;
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
	
	//跳转到登录页面
	@RequestMapping("/toUserLogin.jhtml")
	public String toUserLogin(HttpServletRequest request, HttpServletResponse response){
		logger.info("进入用户登录页面");
		String user_phone = this.getParam(request, "user_phone");
		request.setAttribute("user_phone", user_phone);
		return "login/login";
	}
	
	//跳转到注册页面
	@RequestMapping("/toUserRegister.jhtml")
	public String toUserRegister(HttpServletRequest request, HttpServletResponse response){
		logger.info("进入用户注册页面");
		return "login/register";
	}
	
	//跳转到手机预定、手机客户端页面
	@RequestMapping("/toUserApp.jhtml")
	public String toUserApp(HttpServletRequest request, HttpServletResponse response){
		logger.info("进入手机预定、手机客户端页面");
		return "app/appIndex";
	}
	
	//用户登出系统
	@RequestMapping("/loginOutUser.jhtml")
	public void logOutUser(HttpServletResponse response,HttpServletRequest request){
		request.getSession().removeAttribute("loginUser");
		write2Response(response, "success");
	}
	
	//预订页面的用户登录
	@RequestMapping("/userLogin.jhtml")
	public void userLogin(HttpServletRequest request, HttpServletResponse response){
		String user_phone = this.getParam(request, "user_phone");
		String user_password = this.getParam(request, "user_password");
		String sessionVal = this.getParam(request, "sessionVal");//是否两周内免登录
		LoginUserInfo loginUser = userInfoService.queryUserInfo(user_phone);  //查询用户名和密码是否正确
		String result="";
		if(loginUser==null){
			result = "no";//用户账号不存在
		}else if(loginUser.getUser_password().equals(user_password) && loginUser.getWeather_able().equals("1")){
			result = "yes";
			//用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
			if(sessionVal.equals("yes")){//两周内免登陆
				request.getSession().setMaxInactiveInterval(14*24*60*60*1000);
			}
		}else{
			result = "exception";
		}
		write2Response(response, result);
	}
	
	//用户注册，看该用户是否已经注册过
	@RequestMapping("/userPhoneCanUse.jhtml")
	public void userPhoneCanUse(HttpServletRequest request, HttpServletResponse response){
		String user_phone = this.getParam(request, "user_phone");
		LoginUserInfo loginUser = userInfoService.queryUserInfo(user_phone);  //查询用户名和密码是否正确
		String result="";
		if(loginUser==null){
			result = "yes";//用户账号不存在
		}else{
			result = "no";//已经存在
		}
		write2Response(response, result);
	}
	
	//预订页面的用户注册
	@RequestMapping("/userRegister.jhtml")
	public void userRegister(HttpServletRequest request, HttpServletResponse response){
		String user_phone = this.getParam(request, "user_phone");
		String user_password = this.getParam(request, "user_password");
		String user_email = this.getParam(request, "user_email");
		String check_code = this.getParam(request, "check_code");
		
		String result="";
		
		//获取session中的验证码
		String sessionCheckCode = request.getSession().getAttribute(RandomValidateCode.RANDOMCODEKEY).toString();
		if(sessionCheckCode==null || !sessionCheckCode.equalsIgnoreCase(check_code)){
			result="checkCodeNo";//验证码输入有误
		}else{
			LoginUserInfo loginUser = new LoginUserInfo();
			String user_id = CreateIDUtil.createID("WBU");
			loginUser.setUser_id(user_id);
			loginUser.setUser_name(user_phone);
			loginUser.setUser_phone(user_phone);
			loginUser.setUser_password(user_password);//用户密码
			loginUser.setUser_email(user_email);
			loginUser.setUser_source("web");
			loginUser.setWeather_able("1");
			loginUser.setLogin_num("1");
			loginUser.setLogin_ip(request.getRemoteAddr());//获取登录IP地址
			loginUser.setUser_verify("00");//用户证件信息审核 00：正在审核 11：审核通过 22：审核未通过
			loginUser.setScore_num("0");//用户积分
			loginUser.setCreate_time("now()");
			loginUser.setLogin_time("now()");
			userInfoService.addUserInfo(loginUser);//数据库添加新用户
			
			//用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
			result="yes";
		}
		write2Response(response, result);
	}
	
	
	
	//用户注册下发短信
	@RequestMapping("/sendRegisterMsn.jhtml")
	public void sendRegisterMsn(HttpServletRequest request, HttpServletResponse response){
		String user_phone = this.getParam(request, "user_phone");//用户所填写的手机号码
		StringBuffer content = new StringBuffer();
		String phoneCode = Long.toString(Math.round(Math.random()*899999+100000));//随机生成六位数的验证码
		//【19旅行】尊敬的用户，您好！您在19旅行网申请的验证码为：567010，为了您的账号安全，请在90秒内完成验证。 
		content.append("【19旅行】尊敬的用户，您好！您在19旅行网申请的验证码为："+phoneCode+"，为了您的账号安全，请在90秒内完成验证。 ");
		
		String result = "";
		//发送短信通知车票预订成功
		boolean flag = mobileMsgUtil.send(user_phone, content.toString(), "22");
		logger.info("用户注册下发短信："+user_phone+"【"+content+"】");
		if(flag){
			Cookie cookie = new Cookie("phoneCode", phoneCode);
			cookie.setMaxAge(90);//3分钟
			response.addCookie(cookie);
			result = "yes:"+phoneCode;
		}else{
			result = "no";
		}
		write2Response(response, result);
	}
	
	//校验手机验证码是否正确
	@RequestMapping("/judgeRegisterMsn.jhtml")
	public void judgeRegisterMsn(HttpServletRequest request, HttpServletResponse response){
		String phone_code = this.getParam(request, "phone_code");
		String result="no";
		//获取cookie中的验证码
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) { 
			Cookie cookie = cookies[i]; 
			if(cookie.getName().equals("phoneCode")){//获取键  
				String phoneCode = cookie.getValue().toString();//获取值  
				if(phoneCode.equals(phone_code)){
					result="yes";
				}
			} 
		}  
		write2Response(response, result);
	}
	
	//校验验证码是否正确
	@RequestMapping("/checkCode.jhtml")
	public void checkCode(HttpServletRequest request, HttpServletResponse response){
		String check_code = this.getParam(request, "check_code");
		String result="";
		//获取session中的验证码
		String sessionCheckCode = request.getSession().getAttribute(RandomValidateCode.RANDOMCODEKEY).toString();
		if(sessionCheckCode==null || !sessionCheckCode.equalsIgnoreCase(check_code)){
			result="checkCodeNo";//验证码输入有误
		}else{
			result="yes";
		}
		write2Response(response, result);
	}
	
	//跳转至注册成功页面
	@RequestMapping("/registerSuccess.jhtml")
	public String registerSuccess(HttpServletRequest request, HttpServletResponse response){
		return "login/registerSuccess";
	}
	
	//忘记密码
	//跳转至忘记密码页面01
	@RequestMapping("/forgetPwd.jhtml")
	public String forgetPwd(HttpServletRequest request, HttpServletResponse response){
		return "password/forgetPwdOne";
	}
	//跳转至忘记密码页面02
	@RequestMapping("/forgetPwdTwo.jhtml")
	public String forgetPwdTwo(HttpServletRequest request, HttpServletResponse response){
		String user_phone = this.getParam(request, "user_phone");
		request.setAttribute("user_phone", user_phone);
		request.setAttribute("user_phone_info", user_phone.substring(0, 3)+"****"+user_phone.substring(7));
		return "password/forgetPwdTwo";
	}
	//跳转至忘记密码页面03
	@RequestMapping("/forgetPwdThree.jhtml")
	public String forgetPwdThree(HttpServletRequest request, HttpServletResponse response){
		String user_phone = this.getParam(request, "user_phone");
		request.setAttribute("user_phone", user_phone);
		return "password/forgetPwdThree";
	}
	//更改密码并且跳转至忘记密码页面04
	@RequestMapping("/forgetPwdUpdate.jhtml")
	public String forgetPwdUpdate(HttpServletRequest request, HttpServletResponse response){
		String user_phone = this.getParam(request, "user_phone");
		String user_password = this.getParam(request, "user_password");
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_phone", user_phone);
		map.put("user_password", user_password);
		userInfoService.updateUserPwd(map);
		logger.info(user_phone+"忘记密码，更改密码为："+user_password);
		request.setAttribute("user_phone", user_phone);
		return "password/forgetPwdSuccess";
	}
	
	
	//跳转至我的信息页面
	@RequestMapping("/myInfo.jhtml")
	public String myInfo(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			String user_id = loginUser.getUser_id();
			Map<String, String> loginMap = userInfoService.queryUserAllInfo(user_id);
			request.setAttribute("loginMap", loginMap);
			return "login/myInfo";
		}
	}
	
	//跳转至更新我的信息页面
	@RequestMapping("/toUpdateUserinfo.jhtml")
	public String toUpdateUserinfo(HttpServletRequest request, HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		Map<String, String> loginMap = userInfoService.queryUserAllInfo(user_id);
		String user_birth = loginMap.get("user_birth");//1990-01-01
		if(user_birth != null || StringUtils.isNotEmpty(user_birth)){
			request.setAttribute("year", user_birth.split("-")[0]);
			String month = user_birth.split("-")[1];
			if(month.startsWith("0")){
				month = month.substring(1);
			}
			request.setAttribute("month", month);
			String day = user_birth.split("-")[2];
			if(day.startsWith("0")){
				day = day.substring(1);
			}
			request.setAttribute("day", day);
		}
		request.setAttribute("loginMap", loginMap);
		return "login/myInfoUpdate";
	}
	
	//保存我的信息页面
	@RequestMapping("/saveUserinfo.jhtml")
	public void saveUserinfo(HttpServletRequest request, HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		String user_email = this.getParam(request, "user_email");
		String user_name = this.getParam(request, "user_name");
		String user_sex = this.getParam(request, "user_sex");
		String year = this.getParam(request, "year");
		String month = this.getParam(request, "month");
		String day = this.getParam(request, "day");
		//month.length()=1 ? month="0"+month : month=month;
		String user_birth = null;
		if(!year.equals("年") && !month.equals("月") && !day.equals("日")){
			user_birth = year+"-"+month+"-"+day;
		}
		
		Map<String, String> loginMap = new HashMap<String, String>();
		loginMap.put("user_id", user_id);
		loginMap.put("user_email", user_email);
		loginMap.put("user_name", user_name);
		loginMap.put("user_sex", user_sex);
		loginMap.put("user_birth", user_birth);
		userInfoService.updateUserInfo(loginMap);
		
		JSONObject json = new JSONObject();
		json.put("result", "SUCCESS");
		write2Response(response, json.toString());
	}
	
	
	//跳转至修改密码页面
	@RequestMapping("/toUpdatePassword.jhtml")
	public String toUpdatePassword(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			String user_id = loginUser.getUser_id();
			Map<String, String> loginMap = userInfoService.queryUserAllInfo(user_id);
			request.setAttribute("loginMap", loginMap);
			return "login/passwordUpdate";
		}
	}
	
	//保存修改密码页面
	@RequestMapping("/saveUserPassword.jhtml")
	public void saveUserPassword(HttpServletRequest request, HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		String user_password = this.getParam(request, "user_password_ok");
		
		Map<String, String> loginMap = new HashMap<String, String>();
		loginMap.put("user_id", user_id);
		loginMap.put("user_password", user_password);
		userInfoService.updateUserInfo(loginMap);
		
		JSONObject json = new JSONObject();
		json.put("result", "SUCCESS");
		write2Response(response, json.toString());
	}
	
	/*************************** 我的旅行--常用信息--常用旅客 start *************************************/
	//跳转至常用旅客页面
	@RequestMapping("/queryPassenger.jhtml")
	public String queryPassenger(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			String user_id = loginUser.getUser_id();
			String link_name = this.getParam(request, "link_name");//常用联系人的姓名
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("user_id", user_id);
			paramMap.put("link_name", link_name);
			
			List<Map<String, String>> linkerList = userInfoService.queryPassengerList(paramMap);
			
			request.setAttribute("linkerList", linkerList);
			request.setAttribute("count", linkerList.size());
			request.setAttribute("link_name", link_name);
			request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());//乘客类型：0成人票 1儿童票
			request.setAttribute("idsTypeMap", TrainConsts.getIdsType());//证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
			return "oftenInfo/passengerList";
		}
	}
	
	//跳转至添加常用旅客页面
	@RequestMapping("/toAddPassenger.jhtml")
	public String toAddPassenger(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			return "oftenInfo/passengerAdd";
		}
	}
	
	//跳转至添加常用旅客页面
	@RequestMapping("/addPassenger.jhtml")
	public void addPassenger(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String user_id = loginUser.getUser_id();
		String link_name = this.getParam(request, "link_name");//常用联系人的姓名
		String ids_card = this.getParam(request, "ids_card");//常用联系人的姓名
		String link_phone = this.getParam(request, "link_phone");//常用联系人的姓名
		String ids_type = this.getParam(request, "ids_type");//常用联系人的姓名
		String passenger_type = this.getParam(request, "passenger_type");//常用联系人的姓名
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_id", user_id);
		paramMap.put("link_name", link_name);
		paramMap.put("ids_card", ids_card);
		paramMap.put("link_phone", link_phone);//可为空
		paramMap.put("ids_type", ids_type);
		paramMap.put("passenger_type", passenger_type);
		
		userInfoService.addPassenger(paramMap);
		
		write2Response(response, "success");
	}
	
	//删除常用旅客
	@RequestMapping("/deletePassenger.jhtml")
	public String deletePassenger(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String user_id = loginUser.getUser_id();
		String link_id = this.getParam(request, "link_id");//常用联系人的姓名
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_id", user_id);
		paramMap.put("link_id", link_id);
		
		userInfoService.deletePassenger(paramMap);
		
		return "redirect:/login/queryPassenger.jhtml";
	}
	
	//跳转至修改常用旅客页面
	@RequestMapping("/toUpdatePassenger.jhtml")
	public String toUpdatePassenger(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			String user_id = loginUser.getUser_id();
			String link_id = this.getParam(request, "link_id");//常用联系人的id
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("user_id", user_id);
			paramMap.put("link_id", link_id);
			
			Map<String, String> linkerMap = userInfoService.queryPassenger(paramMap);
			
			request.setAttribute("linkerMap", linkerMap);
			
			return "oftenInfo/passengerUpdate";
		}
	}
	
	//跳转至修改常用旅客页面
	@RequestMapping("/updatePassenger.jhtml")
	public void updatePassenger(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String user_id = loginUser.getUser_id();
		String link_id = this.getParam(request, "link_id");//常用联系人的id
		String link_name = this.getParam(request, "link_name");//常用联系人的姓名
		String ids_card = this.getParam(request, "ids_card");//常用联系人的姓名
		String link_phone = this.getParam(request, "link_phone");//常用联系人的姓名
		String ids_type = this.getParam(request, "ids_type");//常用联系人的姓名
		String passenger_type = this.getParam(request, "passenger_type");//常用联系人的姓名
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_id", user_id);
		paramMap.put("link_id", link_id);
		paramMap.put("link_name", link_name);
		paramMap.put("ids_card", ids_card);
		paramMap.put("link_phone", link_phone);//可为空
		paramMap.put("ids_type", ids_type);
		paramMap.put("passenger_type", passenger_type);
		
		userInfoService.updatePassenger(paramMap);
		
		write2Response(response, "success");
	}
	/*************************** 我的旅行--常用信息--常用旅客 end *************************************/
	
	
	
	/*************************** 我的旅行--常用信息--邮寄地址 start *************************************/
	//跳转至邮寄地址页面
	@RequestMapping("/queryAddress.jhtml")
	public String queryAddress(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			String user_id = loginUser.getUser_id();
			String addressee = this.getParam(request, "addressee");//邮寄地址的姓名
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("user_id", user_id);
			paramMap.put("addressee", addressee);
			
			int sunCount = userInfoService.queryAddressListCount(paramMap);
			List<Map<String, String>> addressList1 = new ArrayList<Map<String, String>>();
			List<Map<String, String>> addressList = userInfoService.queryAddressList(paramMap);
			for(Map<String, String> map : addressList){
				String address_name = map.get("address_name");
				if(address_name.length()>3){
					address_name = address_name.substring(0, 3)+"******";
					map.put("address_name", address_name);
				}
				addressList1.add(map);
			}
			request.setAttribute("addressList", addressList1);
			request.setAttribute("count", addressList1.size());
			request.setAttribute("sunCount", sunCount);
			request.setAttribute("addressee", addressee);
			return "oftenInfo/addressList";
		}
	}
	
	//跳转至添加邮寄地址页面
	@RequestMapping("/toAddAddress.jhtml")
	public String toAddAddress(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			return "oftenInfo/addressAdd";
		}
	}
	
	//跳转至添加邮寄地址页面
	@RequestMapping("/addAddress.jhtml")
	public void addAddress(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String user_id = loginUser.getUser_id();
		String addressee = this.getParam(request, "addressee");//收件人姓名
		String province = this.getParam(request, "province");//省
		String city = this.getParam(request, "city");//市
		String district = this.getParam(request, "district");//区
		String address_name = this.getParam(request, "address_name");//详细地址
		String zip_code = this.getParam(request, "zip_code");//邮政编码
		String addressee_phone = this.getParam(request, "addressee_phone");//收件人手机号码
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_id", user_id);
		paramMap.put("addressee", addressee);
		paramMap.put("province", province);
		paramMap.put("city", city);
		paramMap.put("district", district);
		paramMap.put("address_name", address_name);
		paramMap.put("zip_code", zip_code);
		paramMap.put("addressee_phone", addressee_phone);
		
		userInfoService.addAddress(paramMap);
		
		write2Response(response, "success");
	}
	
	//删除邮寄地址
	@RequestMapping("/deleteAddress.jhtml")
	public String deleteAddress(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String user_id = loginUser.getUser_id();
		String address_id = this.getParam(request, "address_id");//邮寄地址ID
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_id", user_id);
		paramMap.put("address_id", address_id);
		
		userInfoService.deleteAddress(paramMap);
		
		return "redirect:/login/queryAddress.jhtml";
	}
	
	//跳转至修改邮寄地址页面
	@RequestMapping("/toUpdateAddress.jhtml")
	public String toUpdateAddress(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			return "redirect:/login/toUserLogin.jhtml";
		}else{
			String user_id = loginUser.getUser_id();
			String address_id = this.getParam(request, "address_id");//邮寄地址id
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("user_id", user_id);
			paramMap.put("address_id", address_id);
			
			Map<String, String> addressMap = userInfoService.queryAddress(paramMap);
			
			request.setAttribute("addressMap", addressMap);
			
			return "oftenInfo/addressUpdate";
		}
	}
	
	//跳转至修改邮寄地址页面
	@RequestMapping("/updateAddress.jhtml")
	public void updateAddress(HttpServletRequest request, HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String user_id = loginUser.getUser_id();
		String address_id = this.getParam(request, "address_id");//邮寄地址id
		String addressee = this.getParam(request, "addressee");//收件人姓名
		String province = this.getParam(request, "province");//省
		String city = this.getParam(request, "city");//市
		String district = this.getParam(request, "district");//区
		String address_name = this.getParam(request, "address_name");//详细地址
		String zip_code = this.getParam(request, "zip_code");//邮政编码
		String addressee_phone = this.getParam(request, "addressee_phone");//收件人手机号码
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_id", user_id);
		paramMap.put("address_id", address_id);
		paramMap.put("addressee", addressee);
		paramMap.put("province", province);
		paramMap.put("city", city);
		paramMap.put("district", district);
		paramMap.put("address_name", address_name);
		paramMap.put("zip_code", zip_code);
		paramMap.put("addressee_phone", addressee_phone);
		
		userInfoService.updateAddress(paramMap);
		
		write2Response(response, "success");
	}
	/*************************** 我的旅行--常用信息--邮寄地址 end *************************************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/trainIndex.jhtml")
	public String login(HttpServletRequest request, 
			HttpServletResponse response){
		
		SignService ss = new InjectSign();
//		logger.info("[验签key]asp_verify_key="+asp_verify_key);
//		ASPUtil.sign(request, asp_verify_key, ss);
		if (!"SUCCESS".equalsIgnoreCase(ss.getSignResult())) {
			// 验签错误
			logger.info("[接入EOP]验签结果为"+ss.getSignResult());
			InjectSign is = (InjectSign) ss;
			logger.info("接收协议参数，" + "service：["
					+ is.getService() + "]," + "sign：[" + is.getSign() + "],"
					+ "sign_type：[" + is.getSign_type() + "]," + "timestamp：["
					+ is.getTimestamp() + "]," + "data_type：["
					+ is.getData_type() + "]," + "partner_id：["
					+ is.getPartner_id() + "]," + "input_charset：["
					+ is.getInput_charset() + "]," + "version：["
					+ is.getVersion() + "]," + "接收业务参数，" + "eop_order_url：["
					+ is.getEop_order_url() + "]," + "agent_id：["
					+ is.getAgent_id() + "]," + "agent_name：["
					+ is.getAgent_name() + "]," + "agent_level：["
					+ is.getAgent_level() + "]," + "agent_province：["
					+ is.getAgent_province() + "]," + "agent_city：["
					+ is.getAgent_city() + "]," + "agent_district：["
					+ is.getAgent_district() + "]," + "terminal：["
					+ is.getTerminal() + "]");
			request.setAttribute("errMsg", "系统错误，请稍后再试！");
			return "/common/error";
		}

		InjectSign is = (InjectSign) ss;

		logger.info("19e门户进入ASP首页面，" + "接收协议参数，" + "service：["
				+ is.getService() + "]," + "sign：[" + is.getSign() + "],"
				+ "sign_type：[" + is.getSign_type() + "]," + "timestamp：["
				+ is.getTimestamp() + "]," + "data_type：["
				+ is.getData_type() + "]," + "partner_id：["
				+ is.getPartner_id() + "]," + "input_charset：["
				+ is.getInput_charset() + "]," + "version：["
				+ is.getVersion() + "]," + "接收业务参数，" + "eop_order_url：["
				+ is.getEop_order_url() + "]," + "agent_id：["
				+ is.getAgent_id() + "]," + "agent_name：["
				+ is.getAgent_name() + "]," + "agent_level：["
				+ is.getAgent_level() + "]," + "agent_province：["
				+ is.getAgent_province() + "]," + "agent_city：["
				+ is.getAgent_city() + "]," + "agent_district：["
				+ is.getAgent_district() + "]," + "terminal：["
				+ is.getTerminal() + "]");
		
		logger.info("dealerinfo|id="+is.getAgent_id()+"&name="+is.getAgent_name()
				+"&provinceId="+is.getAgent_province()+"&cityId="+is.getAgent_city());

		// 验证参数是否为空
		if (StringUtils.isEmpty(is.getAgent_city())
				|| StringUtils.isEmpty(is.getAgent_province())
				|| StringUtils.isEmpty(is.getAgent_id())) {
			logger.info("EOP进入ASP系统，参数不正确：" + "用户ID：["
					+ is.getAgent_city() + "]," + "省份编号：["
					+ is.getAgent_province() + "],"
					+ "城市编号：[" + is.getAgent_id() + "]");

			request.setAttribute("errMsg", "系统错误，请稍后再试！");
			return "/common/error";
		}

		LoginUserInfo loginUser = new LoginUserInfo();
//		loginUser.setEop_order_url(is.getEop_order_url());
//		loginUser.setAgentId(is.getAgent_id());
//		loginUser.setAgentName(is.getAgent_name());
//		loginUser.setEopAgentLevel(is.getAgent_level());
//		loginUser.setProvinceId(is.getAgent_province());
//		loginUser.setCityId(is.getAgent_city());
//		loginUser.setDistrictId(is.getAgent_district());
//		loginUser.setTerminal(is.getTerminal());
		//用户信息放入session
		//request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
		
//		request.getSession().setAttribute("iframeSetHeightPath", iframeSetHeightPath);
		
/*		Map<String, String> oldAreaMap = commonService.queryOldAreaInfo(is.getAgent_province());
		StringBuffer ad = new StringBuffer();
		ad.append(eop_notice_url)
		  .append("?provinceId=")
		  .append(oldAreaMap.get("area_oldno"))
		  .append("&serviceId=")
		  .append(asp_biz_id);
			
		request.getSession().setAttribute("eopAdUrl", ad.toString());//EOP公告
*/		
		//request.getSession().setAttribute(TrainConsts.INF_SYS_CONF, sysConf);
		
		
		
		request.getSession().setAttribute("menuDisplay", "all");//显示全部菜单
		return "redirect:/buyTicket/bookIndex.jhtml";
	}
	
	
}
