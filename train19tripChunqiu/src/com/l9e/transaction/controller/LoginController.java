package com.l9e.transaction.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.util.Md5Encrypt;

/**
 * 登录
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/chunqiu/login")
public class LoginController extends BaseController {
	protected static final Logger logger = Logger.getLogger(LoginController.class);
	
	@RequestMapping("/trainIndex.jhtml")
	public String login(HttpServletRequest request, 
			HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		String hmac = this.getParam(request, "hmac");
		String url_type = this.getParam(request, "url_type");
		
		String md5 = Md5Encrypt.getKeyedDigestFor19Pay(user_id+"SPRINGAIRLINES","","utf-8");
		if(hmac.equals(md5)){
			LoginUserInfo loginUser = new LoginUserInfo();
			loginUser.setUserId(user_id);
			logger.info("【接入春秋】成功user_id:"+user_id);
			//用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
			if("query_order".equals(url_type)){
				return "redirect:/chunqiu/query/queryOrderList.jhtml";
			}else{
				return "redirect:/chunqiu/buyTicket/bookIndex.jhtml";
			}
		}else{
			logger.info("chuqiu:"+hmac+";19trip:"+md5);
			logger.info("【接入春秋】验签失败！");
			request.setAttribute("errMsg", "验证用户信息失败，本次请求为为非法请求，禁止访问！");
			return "/common/error";
		}
//		LoginUserInfo loginUser = new LoginUserInfo();
//		loginUser.setUserId("test");
//		
//		//用户信息放入session
//		request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
//		return "redirect:/chunqiu/buyTicket/bookIndex.jhtml";
	}
	
	public static void main(String[] args){
		String user_id = "15201169346";
		String md5 = Md5Encrypt.getKeyedDigestFor19Pay(user_id+"SPRINGAIRLINES","","utf-8");
		System.out.println(md5);
	}
}
