package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.UserInfoService;

/**
 * 登录
 * 
 * @author zhangjun
 * 
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	protected static final Logger logger = Logger
			.getLogger(LoginController.class);

	@Value("#{propertiesReader[login_key]}")
	private String key;// key

	@Value("#{propertiesReader[iframeSetHeightPath]}")
	private String iframeSetHeightPath;// iframe高度设置路径
	@Resource
	private UserInfoService userInfoService;

	@RequestMapping("/trainIndex.jhtml")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		// logger.info("[验签key]key=" + key);

		String phone = this.getParam(request, "phone");
		String password = this.getParam(request, "password");
		// String digest = this.getParam(request, "digest");
		// String digest2 = encryptStringByMD5(askId + mobileNo + key);
		//		
		logger.info("【微信登陆】phone=" + phone + "&password=" + password);

		Map<String, String> loginMap = new HashMap<String, String>();
		loginMap.put("user_phone", phone);
		loginMap.put("user_password", password);

		List<Map<String, String>> userInfoList = userInfoService
				.queryWeChartUser(loginMap);
		if (userInfoList.size() <= 0) {
			request.setAttribute("errorMsg", "用户名或密码错误，请重新登陆！");
			request.setAttribute("phone", phone);
			return "book/login";
		}
		//		
		// if(!digest.equals(digest2)){
		// logger.info("【接入cpmay】验签失败！");
		// request.setAttribute("errMsg", "验证用户信息失败，本次请求为为非法请求，禁止访问！");
		// return "/common/error";
		else {
			logger.info("【微信登陆】phone:" + phone + "成功！");
			LoginUserInfo loginUser = new LoginUserInfo();
			// loginUser.setCm_phone("1524587458");
			loginUser.setUser_phone(phone);
			loginUser.setTerminal("weixin");
			loginUser.setUser_id(userInfoList.get(0).get("user_id"));

			// 用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER,
					loginUser);

			// request.getSession().setAttribute("iframeSetHeightPath",
			// iframeSetHeightPath);

		}
		return "redirect:/buyTicket/bookIndex.jhtml";

	}

	@RequestMapping("/turnToLogin.jhtml")
	public String turnToLogin(HttpServletRequest request,
			HttpServletResponse response) {
		String openID = this.getParam(request, "openID");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("openID", openID);
		List<Map<String, String>> userInfoList = userInfoService.queryWeChartUser(
				paramMap);
		if(userInfoList.size() <= 0) {
			request.setAttribute("openID", openID);
			request.setAttribute("title", "注册账户");
			return "book/user";
		}
		Map<String, String> userInfoMap = userInfoList.get(0);
		request.setAttribute("phone", userInfoMap.get("user_phone"));
		request.getSession().setAttribute("userInfoMap", userInfoMap);
		request.setAttribute("openID", openID);
		return "book/login";
	}

	private String encryptStringByMD5(String strSource) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] strTemp = strSource.getBytes();
			java.security.MessageDigest mdTemp = java.security.MessageDigest
					.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
