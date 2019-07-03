package com.l9e.transaction.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.Picture;



@Controller
public class GetVerifyController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(GetVerifyController.class);
	@Resource
	RobotCodeService robotService;
	
	@RequestMapping("/verify.do")
	@ResponseBody
	public void verify(HttpServletRequest request,HttpServletResponse response){
		String action = request.getParameter("action");
		String picId = request.getParameter("picId");
		logger.info("getverify picId : "+picId);
		if("picture".equals(action)){
		  //根据查询ID,从Memcache获取图片验证码，并且进行返还
//			Object verifycode = MemcachedUtil.getInstance().getAttribute(picId+"_code");
			String verifycode = "";
			Picture picture;
			try {
				picture = robotService.getPicture(picId);
				verifycode = picture.getVerify_code().trim();
			} catch (Exception e) {
				logger.error("getPicutre dberror" + e.getMessage());
			} 
			writeN2Response(response, verifycode);
		}else{
			writeN2Response(response, "");
		}
	}
}
