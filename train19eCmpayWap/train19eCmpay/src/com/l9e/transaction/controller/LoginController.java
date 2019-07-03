package com.l9e.transaction.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;

/**
 * 登录
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	protected static final Logger logger = Logger.getLogger(LoginController.class);
	
	@Value("#{propertiesReader[login_key]}")
	private String key;//key

	@Value("#{propertiesReader[iframeSetHeightPath]}")
	private String iframeSetHeightPath;//iframe高度设置路径
	
	@RequestMapping("/trainIndex.jhtml")
	public String login(HttpServletRequest request, 
			HttpServletResponse response){
		logger.info("[验签key]key=" + key);
		
		String askId = this.getParam(request, "askId");
		String mobileNo = this.getParam(request, "mobileNo");
		String digest = this.getParam(request, "digest");
		String digest2 =  encryptStringByMD5(askId + mobileNo + key);
		logger.info("mobileNo=" + mobileNo);
		
		logger.info("【接入cpmay】askId="+askId+"&mobileNo="+mobileNo+"&digest="+digest+"&digest2="+digest2);
//		
		if(!digest.equals(digest2)){
			logger.info("【接入cpmay】验签失败！");
			request.setAttribute("errMsg", "验证用户信息失败，本次请求为为非法请求，禁止访问！");
			return "/common/error";
		}else{
			logger.info("【接入cpmay】验签成功！");
			LoginUserInfo loginUser = new LoginUserInfo();
			loginUser.setCm_phone(mobileNo);
			//loginUser.setCm_phone("1524587458");
			loginUser.setTerminal("mobile");
			
			//用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
			
			//request.getSession().setAttribute("iframeSetHeightPath", iframeSetHeightPath);
			
			return "redirect:/buyTicket/bookIndex.jhtml";
		}

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
	
	public static void main(String[] args) {
		String key = new LoginController().encryptStringByMD5("2014111718243880056918373197535123456");
		System.out.println(key);
	}
}
