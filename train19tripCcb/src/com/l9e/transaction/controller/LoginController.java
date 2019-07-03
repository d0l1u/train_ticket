package com.l9e.transaction.controller;

import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.util.CCBRsaUtil;
import com.l9e.util.Help;
import com.l9e.util.PayUtil;

/**
 * 登录
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	protected static final Logger logger = Logger.getLogger(LoginController.class);
	
	@Value("#{propertiesReader[iframeSetHeightPath]}")
	private String iframeSetHeightPath;//iframe高度设置路径
	
	@RequestMapping("/trainIndex.jhtml")
	public String login(HttpServletRequest request, 
			HttpServletResponse response){
//		logger.info("接入建行！");
		String encString = this.getParam(request, "encString");
		String sid = this.getParam(request, "sid");
		String result_str = "";	//解密结果
//		logger.info("接收到的加密串："+encString);
		String sidCertName = request.getSession().getServletContext().getRealPath("/files")+"/E9PayTrain_public.cert";
		if(StringUtil.isEmpty(encString)){
			//接收加密串为空则默认为游客登录
			encString = "23191953d59586ced422edd872bd49a78c21a316505436e0f663eeef7d6a182c386f6def2ae6d469c3b0f316d026816cfa0148ce34dcdb896a6038ac2e679c0f7dc878fe1461737b7779d90b26db8dc2cc70b357a25c400cdd1164f37c553faae55aef6b193dc1ad3fa1bfc05749375c60b3e957458f3e0bf048dab78359236d";
		}
		try{
			RSAPublicKey  rsap2 = (RSAPublicKey) CCBRsaUtil.getInstance().getKeyPair(sidCertName).getPublic();
			byte[] en_test1 = Help.decodeHex(encString.toCharArray());
			byte[] de_test = CCBRsaUtil.getInstance().decryptVers(rsap2, en_test1);  
			result_str = new String(de_test);
				
			logger.info("加密数据结果："+result_str);
		}catch(Exception e){
			logger.info("【接入建行】验签失败！");
			request.setAttribute("errMsg", "验证用户信息失败，本次请求为为非法请求，禁止访问！");
			return "/common/error";
		}
		String userId=PayUtil.getValue(result_str, "userId");
		String itemNo=PayUtil.getValue(result_str, "itemNo");
		String cityName=PayUtil.getValue(result_str, "cityName");
		String cellPhone=PayUtil.getValue(result_str, "cellPhone");
		if(!"D63D1A292A70CA8E".equals(userId)){  //非游客登陆验证手机号不为空。
			if(null==cellPhone||"".equals(cellPhone)){
				logger.info("【接入建行】验签失败！验证用户信息失败，手机号为空,禁止访问！");
				request.setAttribute("errMsg", "验证用户信息失败，手机号为空,禁止访问！");
				return "/common/error";
			}
		}
		String  info=userId+"|"+itemNo+"|"+cityName+"|"+cellPhone;
		logger.info("传入登陆信息："+info);
		logger.info(userId+"【接入建行】成功！");
		LoginUserInfo loginUser = new LoginUserInfo();
		loginUser.setSid(sid);
		loginUser.setUserId(userId);
		loginUser.setItemNo(itemNo);
		loginUser.setCityName(cityName);
		loginUser.setCellPhone(cellPhone);
		loginUser.setTerminal("pc");
		
		//用户信息放入session
		request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
		return "redirect:/buyTicket/bookIndex.jhtml";
	}
	
	//	RSAPrivateKey  rsap1 = (RSAPrivateKey) CCBRsaUtil.getInstance().getKeyPair(sidCertName).getPrivate();
	//	String de_test1 = "userId=D63D1A292A70CA8E&itemNo=02006&cityName=&cellPhone=";
	//	byte[] en_test1 = CCBRsaUtil.getInstance().encryptVers(rsap1, de_test1.getBytes());
	//	encString = Help.encodeHexStr(en_test1);
}
