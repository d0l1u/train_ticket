package com.l9e.transaction.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.AllComplainService;
import com.l9e.transaction.vo.AllComplainVo;
import com.l9e.transaction.vo.ComplainVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;

	/**
	 * 投诉建议管理整合
	 * @author zhangjc
	 *
	 */
@Controller
@RequestMapping("/allComplain")
public class AllComplainController  extends BaseController {
		
		private static final Logger logger = Logger.getLogger(AllComplainController.class);
		
		@Resource
		private AllComplainService allComplainService;
		
		
		/**
		 * 进入查询页面
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/queryComplainPage.do")
		public String queryComplainPage(HttpServletRequest request,
				HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
			
			request.setAttribute("questionType", AllComplainVo.getQuestionType());
			request.setAttribute("questionChannel", AllComplainVo.getQuestionChannel());
			request.setAttribute("replySeason", AllComplainVo.getReplySeason());
			request.setAttribute("purview", AllComplainVo.getPurview());
			return "redirect:/allComplain/queryComplainList.do?reply_season=00";
				}
				
				/**
				 * 查询列表
				 * @param request
				 * @param response
				 * @return
				 */
				@RequestMapping("/queryComplainList.do")
				public String queryComplainList(HttpServletRequest request,
						HttpServletResponse response){
					List<String> question_type_List = this.getParamToList(request, "question_type");
					List<String> purviewList = this.getParamToList(request, "permission");
					List<String> question_channel_List = this.getParamToList(request, "channel");
					List<String> reply_seasons = this.getParamToList(request, "reply_season");
					String create_time = this.getParam(request, "create_time");
					String reply_time = this.getParam(request, "reply_time");
					String begin_create_time = this.getParam(request, "begin_create_time");
					String end_create_time = this.getParam(request, "end_create_time");
					//查询参数
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("create_time", create_time);
					paramMap.put("reply_time", reply_time);
					paramMap.put("begin_create_time", begin_create_time);
					paramMap.put("end_create_time", end_create_time);
					paramMap.put("question_type", question_type_List);
					paramMap.put("permission", purviewList);
					paramMap.put("channel", question_channel_List);
					paramMap.put("reply_season", reply_seasons);
					int totalCount = allComplainService.queryComplainListCount(paramMap);//总条数	
					//分页
					PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
					paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
					paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
					
					List<Map<String, String>> allComplainList = allComplainService.queryComplainList(paramMap);
					
					request.setAttribute("question_typeStr", question_type_List.toString());
					request.setAttribute("permissionStr", purviewList.toString());
					request.setAttribute("question_channelStr", question_channel_List.toString());
					request.setAttribute("reply_seasonStr", reply_seasons.toString());
					request.setAttribute("allComplainList", allComplainList);
					request.setAttribute("questionType", AllComplainVo.getQuestionType());
					request.setAttribute("questionChannel", AllComplainVo.getQuestionChannel());
					request.setAttribute("replySeason", AllComplainVo.getReplySeason());
					request.setAttribute("purview", AllComplainVo.getPurview()) ;
					request.setAttribute("isShowList", 1);
					
					// 统计
					List<Map<String,String>> allComplainStatCount = allComplainService.queryComplainStatCount();
					int order_question = 0 ,joinUs_question = 0,remand_question = 0, acquire_question = 0 ,
						operation_advice=0,other_advice=0;
					int count = 0 , selfLook = 0 , allLook = 0 ,answerCount = 0 ;
					for(int i = 0; i < allComplainStatCount.size(); i++){
						count++;
						if(allComplainStatCount.get(i).get("question_type")!= null &&
								allComplainStatCount.get(i).get("question_type").equals("0")){
							order_question++;
						}
						if(allComplainStatCount.get(i).get("question_type")!= null &&
								allComplainStatCount.get(i).get("question_type").equals("1")){
							joinUs_question++;
						}
						if(allComplainStatCount.get(i).get("question_type")!= null &&
								allComplainStatCount.get(i).get("question_type").equals("2")){
							remand_question++;
						}
						if(allComplainStatCount.get(i).get("question_type")!= null && 
								allComplainStatCount.get(i).get("question_type").equals("3")){
							acquire_question++;
						}
						if(allComplainStatCount.get(i).get("question_type")!= null && 
								allComplainStatCount.get(i).get("question_type").equals("4")){
							operation_advice++;
						}
						if(allComplainStatCount.get(i).get("question_type")!= null && 
								allComplainStatCount.get(i).get("question_type").equals("5")){
							other_advice++;
						}
						if(allComplainStatCount.get(i).get("permission")!= null && 
								allComplainStatCount.get(i).get("permission").equals("0")){
							allLook++;
						}
						if(allComplainStatCount.get(i).get("permission")!= null && 
								allComplainStatCount.get(i).get("permission").equals("1")){
							selfLook++;
						}
						if(allComplainStatCount.get(i).get("reply_time")!= null){
							answerCount++;
						}
					}
					request.setAttribute("create_time", create_time);
					request.setAttribute("reply_time", reply_time);
					request.setAttribute("begin_create_time", begin_create_time);
					request.setAttribute("end_create_time", end_create_time);
					request.setAttribute("count", count);
					request.setAttribute("order_question", order_question);
					request.setAttribute("joinUs_question", joinUs_question);
					request.setAttribute("remand_question", remand_question);
					request.setAttribute("acquire_question", acquire_question);
					request.setAttribute("operation_advice", operation_advice);
					request.setAttribute("other_advice", other_advice);
					request.setAttribute("allLook", allLook);
					request.setAttribute("selfLook", selfLook);
					request.setAttribute("answerCount", answerCount);
					return "allComplain/allComplainList";
				}
				/**
				 * 查询明细
				 * @param request
				 * @param response
				 * @return
				 */
				@RequestMapping("/queryComplainInfo.do")
				public String queryComplainInfo(HttpServletRequest request, HttpServletResponse response){
					String complain_id = this.getParam(request, "complain_id");
					
					Map<String, String> allComplainInfo = allComplainService.queryComplainParticularInfo(complain_id) ; //查询投诉信息
					List<Map<String, Object>> history = allComplainService.queryHistroyByComplainId(complain_id); //查询历史记录
					String user_name = this.getParam(request, "user_name");
					String user_phone = this.getParam(request, "user_phone");
					
					request.setAttribute("allComplainInfo", allComplainInfo);
					request.setAttribute("questionType", AllComplainVo.getQuestionType());
					request.setAttribute("questionChannel", AllComplainVo.getQuestionChannel());
					request.setAttribute("replySeason", AllComplainVo.getReplySeason());
					request.setAttribute("user_name",user_name);
					request.setAttribute("user_phone", user_phone);
					request.setAttribute("purview", AllComplainVo.getPurview()) ;
					request.setAttribute("history", history);
					String type = this.getParam(request, "type");
						return "allComplain/allComplainInfo";
				}
				
				/**
				 * 跳转修改页面明细
				 * @param request
				 * @param response
				 * @return
				 */
				@RequestMapping("/queryComplainUpdate.do")
				public String queryComplainUpdate(HttpServletRequest request, HttpServletResponse response){
					String complain_id = this.getParam(request, "complain_id");
					
					Map<String, String> allComplainInfo = allComplainService.queryComplainParticularInfo(complain_id) ; //查询投诉信息
					List<Map<String, Object>> history = allComplainService.queryHistroyByComplainId(complain_id); //查询历史记录
					String user_name = this.getParam(request, "user_name");
					String user_phone = this.getParam(request, "user_phone");
					
					request.setAttribute("allComplainInfo", allComplainInfo);
					request.setAttribute("questionType", AllComplainVo.getQuestionType());
					request.setAttribute("questionChannel", AllComplainVo.getQuestionChannel());
					request.setAttribute("replySeason", AllComplainVo.getReplySeason());
					request.setAttribute("user_name",user_name);
					request.setAttribute("user_phone", user_phone);
					request.setAttribute("purview", AllComplainVo.getPurview()) ;
					request.setAttribute("history", history);
						return "allComplain/allComplainUpdate";
					
				}
					
				
				/**
				 * 修改详细信息
				 * @param request
				 * @param response
				 * @return
				 */
				@RequestMapping("/updateComplainInfo.do")
				public String updateComplainInfo(AllComplainVo complain,HttpServletRequest request, HttpServletResponse response){
					LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
//					Map<String, String> log = new HashMap<String, String>();
					String opt_person = loginUserVo.getReal_name();//当前登录人 
//					String reply_time=request.getParameter("reply_time");
//					String reply_person=request.getParameter("reply_person");
//					String our_reply=request.getParameter("our_reply");
//					String complain_id = this.getParam(request, "complain_id") ;
					complain.setOpt_person(opt_person);
					complain.setReply_season("11");
//					log.put("reply_time", reply_time);
//					log.put("reply_person", reply_person);
//					log.put("our_reply", our_reply);
//					log.put("complain_id", complain_id);
//					allComplainService.insertLog(log);
					allComplainService.updateComplainInfo(complain) ;
				
					return "redirect:/allComplain/queryComplainList.do?reply_season=00";
				}

				/**
				 *删除投诉信息
				 *@param request
				 *@param response
				 *@return 
				 */
				@RequestMapping("/deleteComplain.do")
				public String deleteComplain(HttpServletRequest request,
						HttpServletResponse response ){
					String complain_id = this.getParam(request, "complain_id") ;
					allComplainService.deleteComplain(complain_id) ;
					
					return "redirect:/allComplain/queryComplainList.do?reply_season=00" ;
				}
				
				/**
				 * 订单锁
				 * @param response
				 * @param request
				 * @return
				 */
				@RequestMapping("/queryComplainIsLock.do")
				@ResponseBody
				public void queryComplainIsLock(HttpServletResponse response ,HttpServletRequest request){
					LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
					String complain_id = this.getParam(request, "complain_id");
					String opt_person = loginUserVo.getReal_name();
					String key = "Lock_" + complain_id;
					String value = "Lock_"+complain_id+"&"+opt_person;
					String isLock;
					isLock = (String) MemcachedUtil.getInstance().getAttribute(key); //读值
					if(StringUtils.isEmpty(isLock)){
						MemcachedUtil.getInstance().setAttribute(key, value, 3*60*1000); //写值
						isLock="";
					}else if(isLock.indexOf(opt_person) != -1){
						isLock = "";
					}
					PrintWriter out;
					try {
						out = response.getWriter();
						out.write(isLock);
						out.flush();
						out.close();
					} catch (IOException e) {
						logger.error("支付锁时response.getWriter()异常");
						e.printStackTrace();
					}
					
				}
				
				//查询订单的操作日志
				@RequestMapping("/queryOrderOperHistory.do")
				@ResponseBody
				public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
					String complain_id = this.getParam(request, "complain_id");
					Map<String, String> allComplainInfo = allComplainService.queryComplainParticularInfo(complain_id) ; //查询投诉信息
					List<Map<String, Object>> history = allComplainService.queryHistroyByComplainId(complain_id);
					if(!history.isEmpty()){
						allComplainInfo.put("our_reply", (String) history.get(0).get("our_reply"));
					}else{
						allComplainInfo.put("our_reply", "");
					}
					JSONArray jsonArray = JSONArray.fromObject(allComplainInfo);  
					response.setCharacterEncoding("utf-8");
					try {
						response.getWriter().write(jsonArray.toString());
						response.getWriter().flush();
						response.getWriter().close();
					} catch (IOException e){
						e.printStackTrace();
					}
				}
			}
