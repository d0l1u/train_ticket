package com.l9e.transaction.controller;

import java.util.ArrayList;
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
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.vo.AgentVo;
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
	@Resource
	private JoinUsService  joinUsService;

	@RequestMapping("/complainIndex.jhtml")
	public String complainIndex(HttpServletRequest request, 
			HttpServletResponse response){
		String question = this.getParam(request, "question");
		String ques_Id = this.getParam(request, "ques_Id");
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		List<Map<String, String>> complainList = complainService.queryComplainList(agentId);
		question = question.replace("script", "").replace("SCRIPT", "");
		request.setAttribute("questionTypeMap", TrainConsts.getQuestionType());//问题类型
		List<Map<String, String>> otherComplainList = new ArrayList<Map<String,String>>();
		List<Map<String, String>> myComplainList = new ArrayList<Map<String,String>>();
		for(int i=0;i<complainList.size();i++){
			if("1".equals(complainList.get(i).get("permission")))
				myComplainList.add(complainList.get(i));
			else 
				otherComplainList.add(complainList.get(i));
		}
		
		
		request.setAttribute("complainList", complainList);
		request.setAttribute("otherComplainList", otherComplainList);
		request.setAttribute("myComplainList", myComplainList);
		request.setAttribute("question", question);
		request.setAttribute("ques_Id", ques_Id);
		return "complain/complain";
	}
	
	@RequestMapping("/addComplainInfo.jhtml")
	public String addComplainInfo(HttpServletRequest request, 
			HttpServletResponse response){
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String provinceId = loginUser.getProvinceId();
		String cityId = loginUser.getCityId();
		String agentId = loginUser.getAgentId();
		String question_type = this.getParam(request, "question_type");
		String question = this.getParam(request, "question");
		String eop_user = loginUser.getAgentName();
		logger.info("complainInfo|agentId="+agentId+"&name="+eop_user
				+"&provinceId="+provinceId+"&cityId="+cityId);
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		String user_name = agentVo.getUser_name();//代理商姓名
		String user_phone = agentVo.getUser_phone();//代理商电话
		
		int count = complainService.queryDailyCount(agentId);
		String result = count > 0 ? "no" : "yes";
		if(result.equals("no")){
			return "redirect:/complain/complainIndex.jhtml";
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("complain_id", CreateIDUtil.createID("QA"));
		paramMap.put("province_id", provinceId);
		paramMap.put("city_id", cityId);
//		paramMap.put("district_id", districtId);
		paramMap.put("agent_id", agentId);
		paramMap.put("question_type", question_type);
		paramMap.put("question", question);
		paramMap.put("eop_user", eop_user);
		paramMap.put("user_name", user_name);
		paramMap.put("user_phone", user_phone);
		
		complainService.addComplainInfo(paramMap);
		
		return "redirect:/complain/complainIndex.jhtml";
	}
	
	@RequestMapping("/queryTodayCan.jhtml")
	@ResponseBody
	public void queryTodayCan(HttpServletRequest request, 
			HttpServletResponse response){
		try {
			LoginUserInfo loginUser = this.getLoginUser(request);
			String agentId = loginUser.getAgentId();
			int count = complainService.queryDailyCount(agentId);
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
