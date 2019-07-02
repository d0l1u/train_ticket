package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.MobileMsgUtil;

@Controller
@RequestMapping("/register")
public class RegisterController extends BaseController {
	protected static final Logger logger = Logger
			.getLogger(RegisterController.class);
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private OrderService orderService;
	@Resource
	private MobileMsgUtil mobileMsgUtil;

	/*
	 * 检测数据库中是否存在符合该微信openID的账户
	 */
	@RequestMapping("/check.jhtml")
	public String check(HttpServletRequest request, HttpServletResponse response) {
		String openID = this.getParam(request, "openID");
		if(openID.length() <= 0) {
			openID = (String) request.getSession().getAttribute("weixinOpenID");
		}
		logger.info("openID" + openID);
		request.getSession().setAttribute("weixinOpenID", openID);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("openID", openID);
		List<Map<String, String>> userInfoList = userInfoService
				.queryWeChartUser(paramMap);
		if (userInfoList != null && userInfoList.size() > 0) {
			LoginUserInfo loginUser = new LoginUserInfo();
			loginUser.setUser_phone(userInfoList.get(0).get("user_phone"));
			loginUser.setUser_id(userInfoList.get(0).get("user_id"));
			loginUser.setTerminal("weixin");
			// 用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER,
					loginUser);
			request.setAttribute("user_phone", userInfoList.get(0).get("user_phone"));
		}
		request.setAttribute("openID", openID);
		return "book/trainHome";
	}

	@RequestMapping("/turnToChangePwd.jhtml")
	public String turnToChangePwd(HttpServletRequest request,
			HttpServletResponse response) {
		String openID = this.getParam(request, "openID");
		String isRegister = this.getParam(request, "r");
		if(openID.length() <= 0) {
			openID = (String) request.getSession().getAttribute("weixinOpenID");
		}
		request.getSession().setAttribute("weixinOpenID", openID);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("openID", openID);
		List<Map<String, String>> userInfoList = userInfoService
				.queryWeChartUser(paramMap);
		boolean flag = true;
		if (userInfoList != null && userInfoList.size() > 0) {
			LoginUserInfo loginUser = new LoginUserInfo();
			loginUser.setUser_phone(userInfoList.get(0).get("user_phone"));
			loginUser.setUser_id(userInfoList.get(0).get("user_id"));
			loginUser.setTerminal("weixin");
			// 用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER,
					loginUser);
			request.setAttribute("openID", openID);
			flag = false;
		}
		String title = null;
		if("1".equals(isRegister) && flag) {
			title = "注册账号";
		}else{
			title="更改密码";
		}
		request.setAttribute("title", title);
		
		return "book/user";
	}

	@RequestMapping("/register.jhtml")
	public void register(HttpServletRequest request,
			HttpServletResponse response) {
		String username = this.getParam(request, "username");
		String location = this.getParam(request, "location");
		String password = this.getParam(request, "password");
		try {
			location = URLDecoder.decode(location, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("URLDecoder不支持utf-8解码");
		}

		String openID = this.getParam(request, "openID");
		String user_phone = this.getParam(request, "user_phone");

		if (!"".equals(username) && !"".equals(password)) {
			Map<String, String> registerMap = new HashMap<String, String>();
			registerMap.put("user_phone", username);
			registerMap.put("user_password", password);
			registerMap.put("openID", openID);
			String user_id = CreateIDUtil.createID("WXU");
			List<Map<String, String>> userList = userInfoService
					.queryWeChartUser(registerMap);

			logger.info("注册用户信息为" + registerMap.toString());
			if (userList.size() <= 0) {
				registerMap.put("user_id", user_id);
				userInfoService.saveUserInfo(registerMap);
			} else {
				user_id = userList.get(0).get("user_id");
				registerMap.put("user_id", user_id);
				userInfoService.updateUserInfo(registerMap);
			}
			LoginUserInfo loginUser = new LoginUserInfo();
			loginUser.setTerminal("weixin");
			loginUser.setUser_phone(user_phone);
			loginUser.setUser_id(user_id);
			request.setAttribute("user_phone", username);
			request.setAttribute("openID", openID);
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER,
					loginUser);
			RequestDispatcher requestDispatcher = request
					.getRequestDispatcher("/register/check.jhtml?openID=" + openID);
			try {
				requestDispatcher.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		String vertify_code = this.getParam(request, "vertify_code");
		String saved_vertify_code = (String) MemcachedUtil.getInstance()
				.getAttribute(user_phone.trim() + openID.trim());
		String result = "";
		if (vertify_code.equals(saved_vertify_code)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			String user_id = CreateIDUtil.createID("WEU");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 6; i++) {
				sb.append(String.valueOf(Math.random() * 10).charAt(0));
			}

			paramMap.put("user_phone", user_phone);
			paramMap.put("user_password", sb.toString());
			List<Map<String, String>> userList = userInfoService
					.queryWeChartUser(paramMap);
			
			logger.info("注册用户信息为" + paramMap.toString());
			if (userList.size() <= 0) {
				paramMap.put("openID", openID);
				paramMap.put("user_id", user_id);
				userInfoService.saveUserInfo(paramMap);
				StringBuffer msgsb = new StringBuffer("恭喜您成功开通账号：");
				msgsb.append(user_phone).append("，密码是").append(sb.toString()).append("，登陆后可在“个人中心”修改密码！");
				mobileMsgUtil.sendByQyt(user_phone.trim(), msgsb.toString());
			} else {
				user_id = userList.get(0).get("user_id");
				paramMap.put("user_id", user_id);
				userInfoService.updateUserInfo(paramMap);
			}
			LoginUserInfo loginUser = new LoginUserInfo();
			loginUser.setTerminal("weixin");
			loginUser.setUser_phone(user_phone);
			loginUser.setUser_id(user_id);
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER,
					loginUser);
			result = "0";
		} else {
			logger.info("验证码错误，正确验证码为" + saved_vertify_code + ",而提交的验证码为"
					+ vertify_code);
			result = "-1";
		}
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			logger.error("注册响应异常！");
		}
	}

	@RequestMapping("/updateWeChatUser.jhtml")
	public String updateWeChatUser(HttpServletRequest request,
			HttpServletResponse response) {
		String userPhone = this.getParam(request, "phone");
		String password = this.getParam(request, "password");
		String openID = this.getParam(request, "openID");
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_phone", userPhone);
		map.put("openID", openID);
		map.put("user_password", password);
		List<Map<String, String>> userList = userInfoService.queryWeChartUser(map);
		if(userList.size() > 0) {
			String user_id= userList.get(0).get("user_id");
			map.put("user_id", user_id);
			userInfoService.updateUserInfo(map);
		}
		return "redirect:/register/check.jhtml?openID=" + openID;
	}

	@RequestMapping("/sendSms.jhtml")
	public void sendSms(HttpServletRequest request, HttpServletResponse response) {

		String openID = this.getParam(request, "openID");
		String user_phone = this.getParam(request, "user_phone");
		logger.info("[验证码发送接口]openID：" + openID + ", user_phone:" + user_phone);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			sb.append(String.valueOf(Math.random() * 10).charAt(0));
		}

		String captcha = sb.toString();
		String message = "验证码：" + captcha + "，请在180秒内完成验证。";
		MemcachedUtil.getInstance().setAttribute(
				user_phone.trim() + openID.trim(), captcha, 180 * 1000);
		mobileMsgUtil.sendByQyt(user_phone.trim(), message);
		return;
	}

	@RequestMapping("/checkUser.jhtml")
	public void checkUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String user_id = this.getParam(request, "user_id");
		String user_phone = this.getParam(request, "user_phone");
		String openID = this.getParam(request, "openID");
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", user_id);
		map.put("user_phone", user_phone);
		map.put("openID", openID);
		if ("".equals(user_id) && "".equals(user_phone) && "".equals(openID)) {
			response.getWriter().print("0");
			return;
		}
		List<Map<String, String>> userList = userInfoService
				.queryWeChartUser(map);
		if (userList == null || userList.size() == 0) {
			write2Response(response, "0");
			response.getWriter().print("0");
		} else {
			LoginUserInfo loginUser = new LoginUserInfo();
			loginUser.setTerminal("weixin");
			loginUser.setUser_id(userList.get(0).get("user_id"));
			loginUser.setUser_phone(userList.get(0).get("user_phone"));
			// 用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER,
					loginUser);
			write2Response(response, String.valueOf(map.size()));
		}
	}

}