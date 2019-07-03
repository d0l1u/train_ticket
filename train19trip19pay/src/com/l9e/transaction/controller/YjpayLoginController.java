package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.util.EmappSignService;

/**
 * 登录
 * @author yangchao
 *
 */
@Controller
@RequestMapping("/login")
public class YjpayLoginController extends BaseController {
	protected static final Logger logger = Logger.getLogger(YjpayLoginController.class);
	
	
	@Value("#{propertiesReader[appInitKey]}")
	private String appInitKey;//key

	
	@RequestMapping("/trainIndex.jhtml")
	public String login(HttpServletRequest request, 
			HttpServletResponse response){
		logger.info("[验签key]appInitKey=" + appInitKey);
		Map<String,String> checkMap=new HashMap<String,String>();
		String service=this.getParam(request, "service");
		String sign=this.getParam(request, "sign");
		String sign_type=this.getParam(request, "sign_type");
		String timestamp=this.getParam(request, "timestamp");
		String partner_id=this.getParam(request, "partner_id");//合作伙伴id
		String plat_order_url=this.getParam(request, "plat_order_url");//下单地址,train通知19PAY下单时使用该地址。
		String user_id=this.getParam(request, "user_id");//用户id
		String user_name=this.getParam(request, "user_name");//用户姓名
		String terminal=this.getParam(request, "terminal");//终端接入类型,EOP提供（web、wap、pc）
		
 		
		checkMap.put("service", service);
		checkMap.put("sign_type", sign_type);
		checkMap.put("timestamp", timestamp);
		checkMap.put("partner_id", partner_id);
		checkMap.put("plat_order_url", plat_order_url);
		checkMap.put("user_id", user_id);
		checkMap.put("user_name", user_name);
		checkMap.put("terminal", terminal);
		
		logger.info("签名:"+sign);
		boolean flag = false;
		flag=EmappSignService.checkReqSign(checkMap, sign, appInitKey);
		if(flag){
			logger.info("【接入19pay】验签成功！");
			LoginUserInfo loginUser = new LoginUserInfo();
			loginUser.setUser_id(user_id);
			loginUser.setPlat_order_url(plat_order_url);
			loginUser.setUser_name(user_name);
			loginUser.setTerminal(terminal);
			
			//用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
			return "redirect:/buyTicket/bookIndex.jhtml";
		}else{
			logger.info("【接入19pay】验签失败！");
			//request.setAttribute("errMsg", "验证用户信息失败，本次请求为为非法请求，禁止访问！");
			return "/common/error";
		}

	}
}
