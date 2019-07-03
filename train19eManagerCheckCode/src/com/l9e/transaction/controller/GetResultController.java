package com.l9e.transaction.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.RobotCodeService;



@Controller
public class GetResultController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(GetResultController.class);
	@Resource
	RobotCodeService robotService;
	
	@RequestMapping("/getResult.do")
	@ResponseBody
	public void getResult(HttpServletRequest request,HttpServletResponse response){
		String picName=request.getParameter("picName");
		if(picName==null||picName.equals("")){
			writeN2Response(response,"actionError");
			return;
		}
		logger.info("robot find 结果");
		String pic_filename="/upload/"+picName;
		Map<String,String> map = robotService.findByPicName(pic_filename);
		if(map.get("pic_filename")==null||"".equals(map.get("pic_filename"))){
			writeN2Response(response,"noPic");
			return;
		}
		if(map.get("verify_code")==null||"".equals(map.get("verify_code"))){
			writeN2Response(response,"noResult");
			return;
		}else{
			writeN2Response(response,map.get("verify_code"));
			return;
		}
	}
}
