package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
		String user_queryPhone=this.getParam(request, "user_queryPhone");
		List<Map<String, String>> complainList=null;
		String user_id="";
		//如果是游客点击查询
		if(StringUtils.isNotEmpty(user_queryPhone)){
			complainList = complainService.queryComplainList(user_queryPhone);
		}else{
			LoginUserInfo loginUser = this.getLoginUser(request);
			user_id = loginUser.getUserId();
			complainList = complainService.queryComplainList(user_id);
		}
	
		//如果是空的，则显示手机号输入框
		if(StringUtils.isEmpty(user_id)){
			//空
			request.setAttribute("isNULL", "0");
		}else{
			request.setAttribute("isNULL", "1");
		}
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
		String user_id = loginUser.getUserId();
		if(StringUtils.isEmpty(user_id)){
			//如果是游客，则使用其填入的手机号。
			user_id=this.getParam(request, "user_phone");
		}
	
		String question_type = this.getParam(request, "question_type");
		String question = this.getParam(request, "question");
		logger.info("complainInfo|agentId="+user_id);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("complain_id", CreateIDUtil.createID("QA"));
		paramMap.put("user_id", user_id);
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
			String user_id = loginUser.getUserId();
			if(StringUtils.isEmpty(user_id)){
				//如果是游客，则根据手机号判断今天是不是已经提交过订单
				user_id=this.getParam(request, "user_phone");
			}
			int count = complainService.queryDailyCount(user_id);
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
