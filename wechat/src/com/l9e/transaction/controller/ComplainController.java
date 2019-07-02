package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ComplainService;
import com.l9e.util.CreateIDUtil;

/**
 * 投诉建议
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/complain")
public class ComplainController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(ComplainController.class);
	
	@Resource
	private ComplainService complainService;
	
	@RequestMapping("/complainIndex.jhtml")
	public String complainIndex(HttpServletRequest request, 
			HttpServletResponse response){
		String question = this.getParam(request, "question");
		String ques_Id = this.getParam(request, "ques_Id");
		LoginUserInfo loginUser = this.getLoginUser(request);
		String cm_phone = loginUser.getUser_phone();
		List<Map<String, String>> complainList = complainService.queryComplainList(cm_phone);
		
		request.setAttribute("questionTypeMap", TrainConsts.getQuestionType());//问题类型
		request.setAttribute("complainList", complainList);
		request.setAttribute("question", question);
		request.setAttribute("ques_Id", ques_Id);
		return "complain/complain";
	}
	
	@RequestMapping("/addComplainInfo.jhtml")
	public String addComplainInfo(HttpServletRequest request, 
			HttpServletResponse response){
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String cm_phone = loginUser.getUser_phone();
		String question_type = this.getParam(request, "question_type");
		String question = this.getParam(request, "question");
		logger.info("complainInfo|agentId="+cm_phone);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("complain_id", CreateIDUtil.createID("QA"));
		paramMap.put("cm_phone", cm_phone);
		paramMap.put("question_type", question_type);
		paramMap.put("question", question);
		
		complainService.addComplainInfo(paramMap);
		
		return "redirect:/complain/complainIndex.jhtml";
	}
	
	@RequestMapping("/queryTodayCan.jhtml")
	@ResponseBody
	public void queryTodayCan(HttpServletRequest request, 
			HttpServletResponse response){
		try {
			LoginUserInfo loginUser = this.getLoginUser(request);
			String cm_phone = loginUser.getUser_phone();
			int count = complainService.queryDailyCount(cm_phone);
			String result = count > 0 ? "no" : "yes";
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
