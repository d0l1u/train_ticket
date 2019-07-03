package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.servlet.InitConfigServlet;
import com.l9e.transaction.service.BookService;
import com.l9e.transaction.service.ComplainService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.ComplainVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.SwitchUtils;

/**
 * 投诉建议管理
 * @author liht
 *
 */
@Controller
@RequestMapping("/complain")
public class ComplainController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(ComplainController.class);
	
	@Resource
	private ComplainService complainService;
	
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryComplainPage.do")
	public String queryComplainPage(HttpServletRequest request,
			HttpServletResponse response){
		/*****************************省级代理省份方法*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		if(loginUserVo.getUser_level().equals("1.1")&& loginUserVo.getSupervise_name()!=null){
			String str = loginUserVo.getSupervise_name();
			List<String>Supervise_name_List =  SwitchUtils.strToList(str);
			List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
			Map<String,String>area_Map = new HashMap<String,String>();
			for(int i=0;i<Supervise_name_List.size();i++){
				area_Map=complainService.querySupervise_nameToArea_no(Supervise_name_List.get(i).toString());
				area_no_list.add(area_Map);
			}
			request.setAttribute("province",area_no_list); 
		}else{
			request.setAttribute("province", complainService.getProvince()); 
		}
		request.setAttribute("questionType", ComplainVo.getQuestionType());
		return "complain/complainList";
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
		String province_id = this.getParam(request, "province_id");
		String city_id = this.getParam(request, "city_id");
		String district_id = this.getParam(request, "district_id") ;
		String user_phone = this.getParam(request, "user_phone");
		List<String> question_type_List = this.getParamToList(request, "question_type_List");
	
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("province_id",province_id);
		paramMap.put("city_id", city_id);
		paramMap.put("district_id", district_id) ;
		paramMap.put("user_phone", this.getParam(request, "user_phone"));
		paramMap.put("borough_id", this.getParam(request, "borough_id"));
		paramMap.put("question_type", question_type_List);
		
		int totalCount = complainService.queryComplainListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, String>> complainList = complainService.queryComplainList(paramMap);
		Map<String, String> idAndName = new HashMap<String,String>();
		for(int i=0;i<complainList.size();i++){
			Map<String, String> bookInfo = complainList.get(i);
			String provinceId = bookInfo.get("province_id");
			String provinceName = InitConfigServlet.PROMAP.get(provinceId);//省id和name
			String cityId = bookInfo.get("city_id");
			String cityName = InitConfigServlet.CITYMAP.get(cityId);//市id和name
			idAndName.put(provinceId, provinceName);
			idAndName.put(cityId, cityName);
		}
		/*****************************省级代理省份方法*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		if(loginUserVo.getUser_level().equals("1.1")&& loginUserVo.getSupervise_name()!=null){
			String str = loginUserVo.getSupervise_name();
			List<String>Supervise_name_List =  SwitchUtils.strToList(str);
			List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
			Map<String,String>area_Map = new HashMap<String,String>();
			for(int i=0;i<Supervise_name_List.size();i++){
				area_Map=complainService.querySupervise_nameToArea_no(Supervise_name_List.get(i).toString());
				area_no_list.add(area_Map);
			}
			request.setAttribute("province",area_no_list); 
		}else{
			request.setAttribute("province", complainService.getProvince()); 
		}
		
		request.setAttribute("question_typeStr", question_type_List.toString());
		request.setAttribute("complainList", complainList);
		request.setAttribute("questionType", ComplainVo.getQuestionType());
		request.setAttribute("purview", ComplainVo.getPurview()) ;
		request.setAttribute("isShowList", 1);
		
		request.setAttribute("province_id", province_id);
		request.setAttribute("city_id", city_id);
		request.setAttribute("district_id", district_id) ;
		request.setAttribute("city", complainService.getCity(province_id));
		request.setAttribute("district", complainService.getArea(city_id)); 
		
		// 统计
		List<Map<String,String>> complainStatCount = complainService.queryComplainStatCount();
		int order_question = 0 ,joinUs_question = 0,remand_question = 0, acquire_question = 0 ,
			operation_advice=0,other_advice=0;
		int count = 0 , selfLook = 0 , allLook = 0 ,answerCount = 0 ;
		for(int i = 0; i < complainStatCount.size(); i++){
			count++;
			if(complainStatCount.get(i).get("question_type")!= null &&
					complainStatCount.get(i).get("question_type").equals("0")){
				order_question++;
			}
			if(complainStatCount.get(i).get("question_type")!= null &&
					complainStatCount.get(i).get("question_type").equals("1")){
				joinUs_question++;
			}
			if(complainStatCount.get(i).get("question_type")!= null &&
					complainStatCount.get(i).get("question_type").equals("2")){
				remand_question++;
			}
			if(complainStatCount.get(i).get("question_type")!= null && 
					complainStatCount.get(i).get("question_type").equals("3")){
				acquire_question++;
			}
			if(complainStatCount.get(i).get("question_type")!= null && 
					complainStatCount.get(i).get("question_type").equals("4")){
				operation_advice++;
			}
			if(complainStatCount.get(i).get("question_type")!= null && 
					complainStatCount.get(i).get("question_type").equals("5")){
				other_advice++;
			}
			if(complainStatCount.get(i).get("permission")!= null && 
					complainStatCount.get(i).get("permission").equals("0")){
				allLook++;
			}
			if(complainStatCount.get(i).get("permission")!= null && 
					complainStatCount.get(i).get("permission").equals("1")){
				selfLook++;
			}
			if(complainStatCount.get(i).get("reply_time")!= null){
				answerCount++;
			}
		}
		request.setAttribute("idAndName", idAndName);
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
		request.setAttribute("user_phone", user_phone);
		return "complain/complainList";
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
		
		Map<String, String> complainInfo = complainService.queryComplainParticularInfo(complain_id) ; //查询投诉明细
		List<Map<String, Object>> history = complainService.queryHistroyByComplainId(complain_id); //查询历史记录

		request.setAttribute("complainInfo", complainInfo);
		request.setAttribute("questionType", ComplainVo.getQuestionType());
		request.setAttribute("purview", ComplainVo.getPurview()) ;
		request.setAttribute("history", history);
		
		return "complain/complainInfo";
	}
	/**
	 * 修改详细信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateComplainInfo.do")
	public String updateComplainInfo(ComplainVo complain,HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		String opt_person = loginUserVo.getReal_name();
		complain.setOpt_person(opt_person);
		complainService.updateComplainInfo(complain) ;
		
		return "redirect:/complain/queryComplainList.do";
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
		complainService.deleteComplain(complain_id) ;
		
		return "redirect:/complain/queryComplainList.do" ;
	}
	
	@RequestMapping("/queryGetCity.do")
	@ResponseBody
	public String getCity(String provinceid,HttpServletRequest request,HttpServletResponse response){
	
		
		List<AreaVo> list = complainService.getCity(provinceid);
		
		
		ObjectMapper map = new ObjectMapper();
		try {
			map.writeValue(response.getOutputStream(), list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	@RequestMapping("/queryGetArea.do")
	@ResponseBody
	public String getArea(String cityid,HttpServletRequest request,HttpServletResponse response){
	
		List<AreaVo> list = complainService.getArea(cityid);
		
		ObjectMapper map = new ObjectMapper();
		try {
			map.writeValue(response.getOutputStream(), list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
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
}
