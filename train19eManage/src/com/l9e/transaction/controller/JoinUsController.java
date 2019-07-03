package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.JoinDetailVo;
import com.l9e.transaction.vo.JoinUsVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RegisterVo;
import com.l9e.util.MobileMsgUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.SwitchUtils;

/**
 * 加盟管理
 * @author liht
 *
 */
@Controller
@RequestMapping("/joinUs")
public class JoinUsController extends BaseController {
	
	private static final Logger logger = 
		Logger.getLogger(JoinUsController.class);
	
		@Resource
		private JoinUsService joinUsService ;
		
		@Resource
		private MobileMsgUtil mobileMsgUtil;
		/**
		 * 进入查询页面
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/queryJoinUsPage.do")
		public String queryJoinUsPage(HttpServletRequest request,
				HttpServletResponse response){
			
			/*****************************省级代理省份方法*****************************/
			LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
			if(loginUserVo.getUser_level().equals("1.1")&& loginUserVo.getSupervise_name()!=null){
				String str = loginUserVo.getSupervise_name();
			
				List<String>Supervise_name_List =  SwitchUtils.strToList(str);
				List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
				Map<String,String>area_Map = new HashMap<String,String>();
				for(int i=0;i<Supervise_name_List.size();i++){
					area_Map=joinUsService.querySupervise_nameToArea_no(Supervise_name_List.get(i).toString());
					area_no_list.add(area_Map);
				}
				request.setAttribute("province",area_no_list); 
			}else{
				request.setAttribute("province", joinUsService.getProvince()); 
			}
			
			Calendar theCa = Calendar.getInstance(); 

			theCa.setTime(new Date());  
			theCa.add(theCa.DATE, -6); 
			Date date = theCa.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String querydate=df.format(date);
			
			String selectAllagent_grade = this.getParam(request, "selectAllagent_grade");//全选
			request.setAttribute("estates",JoinUsVo.getEestat());
			request.setAttribute("level", JoinUsVo.getLevel());
			request.setAttribute("shop_type", JoinUsVo.getShop_Type());
			request.setAttribute("agent_grade", JoinUsVo.getAgent_Grade());
			request.setAttribute("selectAllagent_grade", selectAllagent_grade);

		return "redirect:/joinUs/queryJoinUsList.do?begin_auditing_time="+querydate;
		}
		
		/**
		 * 查询列表
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/queryJoinUsList.do")
		public String queryJoinUsList(HttpServletRequest request,
				HttpServletResponse response){
			//获得系统当前时间
			//String now = DateUtil.nowDate();
			String province_id = this.getParam(request, "province_id");
			String city_id = this.getParam(request, "city_id");
			String district_id = this.getParam(request, "district_id") ;
			String user_name = this.getParam(request, "user_name");
			String user_phone = this.getParam(request, "user_phone");
			String beginInfo_time = this.getParam(request, "beginInfo_time");
			String endInfo_time = this.getParam(request, "endInfo_time");
			String begin_pay_time = this.getParam(request, "begin_pay_time");
			String end_pay_time = this.getParam(request, "end_pay_time");
			String begin_auditing_time = this.getParam(request, "begin_auditing_time");
			String end_auditing_time = this.getParam(request, "end_auditing_time");
			String order_num = this.getParam(request, "order_num");//订单数
			String selectAllagent_grade = this.getParam(request, "selectAllagent_grade");//全选
			String order_num_begin = null;
			String order_num_end = null;
			if(StringUtil.isNotEmpty(order_num)){
				order_num_begin = order_num.split("-")[0];
				order_num_end = order_num.split("-")[1];
			}
			
			//查询类型（‘00’按申请时间查询，‘11’按最后订购时间查询）
			//String queryType = this.getParam(request, "queryType"); 
			List<String>estateList = this.getParamToList(request, "estate");
//			List<String>userGradeList = this.getParamToList(request, "user_grade");//用户等级
			List<String>shopTypeList = this.getParamToList(request, "shop_type");//店铺类型
			List<String>agent_gradeList = this.getParamToList(request, "agent_grade");//代理商等级
//			if(shopTypeList.contains("10")){
//				for(int i=0;i<shopTypeList.size();i++){
//					if(shopTypeList.get(i).equals("10")){
//						shopTypeList.get(i)="a";
//					}
//				}
//			}
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("province_id", province_id); //省id
			paramMap.put("city_id", city_id);		//市id
			paramMap.put("district_id", district_id) ; //区id
			paramMap.put("order_id", this.getParam(request, "order_id")); 
			paramMap.put("user_name",this.getParam(request, "user_name".trim()));
			//按代理商电话查询参数
			paramMap.put("user_phone", this.getParam(request, "user_phone".trim()));
			//按开始时间和结束时间查询参数
			paramMap.put("beginInfo_time", this.getParam(request, "beginInfo_time".trim())) ;
			paramMap.put("endInfo_time", this.getParam(request, "endInfo_time".trim())) ;
			paramMap.put("begin_pay_time", begin_pay_time);
			paramMap.put("end_pay_time", end_pay_time);
			paramMap.put("begin_auditing_time", begin_auditing_time);
			paramMap.put("end_auditing_time", end_auditing_time);
			//按审核状态查询参数
			paramMap.put("estate", estateList) ;
//			paramMap.put("user_grade", userGradeList);
			paramMap.put("shop_type", shopTypeList);
			paramMap.put("agent_grade", agent_gradeList);
			//按照订单数量查询
			paramMap.put("order_num_begin", order_num_begin);
			paramMap.put("order_num_end", order_num_end);
			int totalCount = joinUsService.queryJoinUsListCount(paramMap);//总条数	
			PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			List<Map<String, String>> joinUsList = joinUsService.queryJoinUsList(paramMap);
			
			List<Map<String, String>> userInfoList = new ArrayList<Map<String, String>>();
			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			String user_grade = "";
			for(int i =0 ; i < joinUsList.size(); i++){
				Map<String, String> joinUsMap = joinUsList.get(i);
				paramMap2.put("user_id", joinUsMap.get("user_id"));
				userInfoList = joinUsService.queryUserRegistInfo(paramMap2);
				user_grade =  getUser_grade(userInfoList);
				joinUsMap.put("user_grade", user_grade);
			}
			
			
			
			Map<String, String> idAndName = new HashMap<String,String>();
			for(int i=0;i<joinUsList.size();i++){
				Map<String, String> bookInfo = joinUsList.get(i);
				String provinceId = bookInfo.get("province_id");
				String provinceName = InitConfigServlet.PROMAP.get(provinceId);//省id和name
				String cityId = bookInfo.get("city_id");
				String cityName = InitConfigServlet.CITYMAP.get(cityId);//市id和name
				String regoinId = bookInfo.get("district_id");
				String regoinName = InitConfigServlet.REGIONMAP.get(regoinId);//区id和name
				idAndName.put(provinceId, provinceName);
				idAndName.put(cityId, cityName);
				idAndName.put(regoinId, regoinName);
			}
			//统计各个状态的条数
			List<String> joinUsCount = joinUsService.queryJoinUsEstateCount();
			int total = 0 ,needpay = 0 ,wait=0 ,doesnot=0 , pass=0 , repay=0;
			for(int i=0 ;i<joinUsCount.size();i++ ){
				total++ ; //全部条数   
//				if(joinUsCount.get(i)!=null &&joinUsCount.get(i).equals(JoinUsVo.NEEDPAY)){
//					needpay++ ; //需要付费条数
//				}
				if(joinUsCount.get(i)!=null &&joinUsCount.get(i).equals(JoinUsVo.WAIT)){
					wait++;	 //等待审核条数
				}
				if(joinUsCount.get(i)!=null &&joinUsCount.get(i).equals(JoinUsVo.DOESNOT)){
					doesnot++; //审核未通过条数	
				}
				if(joinUsCount.get(i)!=null &&joinUsCount.get(i).equals(JoinUsVo.PASS)){
					pass++;	//审核通过条数
				}
//				if(joinUsCount.get(i)!=null &&joinUsCount.get(i).equals(JoinUsVo.REPAY)){
//					repay++; //需要续费条数	
//				}
			}
			

			/*****************************省级代理省份方法*****************************/
			LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
			if(loginUserVo.getUser_level().equals("1.1")&& loginUserVo.getSupervise_name()!=null){
				String str = loginUserVo.getSupervise_name();
				List<String>Supervise_name_List =  SwitchUtils.strToList(str);
				List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
				Map<String,String>area_Map = new HashMap<String,String>();
				for(int i=0;i<Supervise_name_List.size();i++){
					area_Map=joinUsService.querySupervise_nameToArea_no(Supervise_name_List.get(i).toString());
					area_no_list.add(area_Map);
				}
				request.setAttribute("province",area_no_list); 
			}else{
				request.setAttribute("province", joinUsService.getProvince()); 
			}
			request.setAttribute("total", total);
			request.setAttribute("needpay", needpay);
			request.setAttribute("wait", wait);
			request.setAttribute("doesnot", doesnot);
			request.setAttribute("pass", pass);
			request.setAttribute("repay", repay);
			request.setAttribute("user_name",user_name);
			request.setAttribute("user_phone", user_phone);
			request.setAttribute("beginInfo_time", beginInfo_time);
			request.setAttribute("endInfo_time", endInfo_time);
			request.setAttribute("begin_pay_time", begin_pay_time);
			request.setAttribute("end_pay_time", end_pay_time);
			request.setAttribute("begin_auditing_time", begin_auditing_time);
			request.setAttribute("end_auditing_time", end_auditing_time);
			request.setAttribute("totalCount", totalCount);
			
			//如果beginInfo_time为空，则显示当前系统时间
//			if(beginInfo_time.equals("")){
//				request.setAttribute("beginInfo_time", now);
//			}else{
//				request.setAttribute("beginInfo_time", beginInfo_time);
//			}
//			//如果endInfo_time为空，则显示当前系统时间
//			if(endInfo_time.equals("")){
//				request.setAttribute("endInfo_time", now);
//			}else{
//				request.setAttribute("endInfo_time", endInfo_time);
//			}
			
			request.setAttribute("level", JoinUsVo.getLevel()) ;
			request.setAttribute("estates", JoinUsVo.getEestat()) ;
			request.setAttribute("shop_type", JoinUsVo.getShop_Type());
			request.setAttribute("agent_grade", JoinUsVo.getAgent_Grade());
			System.out.println(JoinUsVo.getShop_Type());
			request.setAttribute("estateStr", estateList.toString());
			request.setAttribute("shop_typeStr", shopTypeList.toString());
			request.setAttribute("shopTypeList", shopTypeList);
			request.setAttribute("agent_gradeStr", agent_gradeList.toString());
			request.setAttribute("agent_gradeList", agent_gradeList);
			request.setAttribute("joinUsList", joinUsList);
			request.setAttribute("isShowList", 1);
			request.setAttribute("idAndName", idAndName);
			
			request.setAttribute("province_id", province_id);
			request.setAttribute("city_id", city_id);
			request.setAttribute("district_id", district_id) ;
			request.setAttribute("city", joinUsService.getCity(province_id));
			request.setAttribute("district", joinUsService.getArea(city_id));
			request.setAttribute("order_num", order_num);
			request.setAttribute("selectAllagent_grade", selectAllagent_grade);

			return "joinUs/joinUsList";
		}
		
	     /**
	      * 查询修改详细
	      * @param request
	      * @param response
	      * @return
	      */	
		@RequestMapping("/queryUpdateJoinUsInfo.do")
		public String queryUpdateJoinUsInfo(HttpServletResponse response,
				HttpServletRequest request){
			
			String user_id = this.getParam(request, "user_id") ;
			String province_id = this.getParam(request, "province_id") ;
			String city_id = this.getParam(request,"city_id");
			String district_id = this.getParam(request, "district_id") ;
			
			Map<String,String>joinUpdateInfo = joinUsService.queryUpdateJoinUsInfo(user_id);
			request.setAttribute("joinUpdateInfo",joinUpdateInfo) ;
			request.setAttribute("estates", JoinUsVo.getEestat()) ;
			request.setAttribute("shop_types", JoinUsVo.getShop_Type());
			request.setAttribute("province", joinUsService.getProvince()); 
			request.setAttribute("province_id", province_id);
			request.setAttribute("city_id", city_id);
			request.setAttribute("district_id", district_id) ;
			request.setAttribute("city", joinUsService.getCity
					(joinUpdateInfo.get("province_id")));
			request.setAttribute("district", joinUsService.getArea
					(joinUpdateInfo.get("city_id")));
			
			return "joinUs/updateJoinUsInfo" ;
			
		}
		
		/**
		 * 修改详细
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/updateJoinUs.do")
		public String updateJoinUs(JoinUsVo join,HttpServletRequest request,HttpServletResponse response){
			 LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

			 joinUsService.updateJoinUs(join) ;
			 String user_id =  this.getParam(request, "user_id");
			 String user = loginUserVo.getReal_name();
			// String accountUser = user+"点击了修改按钮";
			 Map<String,String>map = new HashMap<String,String>();
			 map.put("user_id",user_id);
			 map.put("user", user);
			// map.put("accountUser", accountUser);
			 joinUsService.updateOpt_ren(map);
			 
			 //发送审核通过短信功能
			 String old_estate = this.getParam(request, "old_estate");
			 if(!StringUtils.isEmpty(join.getEstate()) 
					&& !join.getEstate().equals(old_estate) 
					&& join.getEstate().equals(JoinUsVo.PASS) 
					&& !StringUtils.isEmpty(join.getUser_phone())){
				 String content = "【19e火车票】恭喜您的申请已经审核通过！感谢您对火车票业务的支持，使用过程中如有问题请查看帮助指南！";
				try {
					 mobileMsgUtil.send(join.getUser_phone().trim(), content);
				} catch (Exception e) {
					logger.error("短信发送异常", e);
				}
			 }
			return "redirect:/joinUs/queryJoinUsList.do";
		}
		
		/**
		 * 删除加盟信息
		 * @param request
		 * @param response
		 * @return 
		 */
		@RequestMapping("/deleteJoinUs.do")
		public String deleteJoinUs(JoinUsVo join,HttpServletResponse response,
				HttpServletRequest request){
			joinUsService.deleteJoinUs(join) ;
			//String username = (String) request.getSession().
			//getAttribute(AccessFilter.CAS_FILTER_USER);//登陆人名称 
			//System.out.println("username="+username) ;
			return "redirect:/joinUs/queryJoinUsList.do" ;
		}
		/**
		 *加盟用户明细
		 *@param request
		 *@param response
		 */
		@RequestMapping("/queryJoinUsDetail.do")
		public String joinUsDetail(HttpServletResponse response, HttpServletRequest request){
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String user_id = this.getParam(request, "user_id");
			paramMap.put("user_id", user_id);
			List<Map<String,String>>joinUsDetail = joinUsService.queryJoinDetail(paramMap) ;
			request.setAttribute("joinUsDetail", joinUsDetail.get(0)) ;
			//request.setAttribute("level", JoinUsVo.getLevel()) ;
			//request.setAttribute("types", JoinDetailVo.getTypes()) ;
			request.setAttribute("shop_type", JoinUsVo.getShop_Type());

			int totalCount = joinUsService.ueryUserRegistInfoCount(paramMap);//注册信息总条数
			
			List<Map<String, String>> userRegistInfo = joinUsService.queryUserRegistInfo(paramMap);//注册信息明细
			List<String>agent_gradeList = this.getParamToList(request, "agent_grade");//代理商等级		    
			String user_status = null;
			int passNum = 0;
			int waitNum = 0;
			int nopassNum = 0;
			String user_grade = null;
		    for(int i=0; i<totalCount; i++){
		    	user_status = userRegistInfo.get(i).get("regist_status");
		    	if(user_status.equals("22")){
		    		passNum = passNum+1;
		    	}
		    	if(user_status.equals("00") || user_status.equals("11") ||user_status.equals("44") ||user_status.equals("55")){
		    		waitNum = waitNum+1;
		    	}
		    	if(user_status.equals("33")){
		    		nopassNum = nopassNum+1;
		    	}
		    }
		    if(passNum==0 && waitNum==0 && nopassNum==0){
		    	user_grade = "未认证";
		    }
		    if(passNum != 0){
		    	if(passNum == 1 ){
		    		user_grade = "铜牌用户";
		    	}if(passNum >=5 ){
		    		user_grade = "金牌用户";
		    	}if(passNum>=2 && passNum<=4){
		    		user_grade = "银牌用户";
		    	}
		    }
		    if(passNum==0 && waitNum!=0){
		    	user_grade = "待审核";
		    }
		    if(passNum==0 && waitNum==0 && nopassNum!=0){
		    	user_grade = "注册失败";
		    }
		    request.setAttribute("user_grade", user_grade);
		    paramMap.put("agent_grade", agent_gradeList);
		    
			request.setAttribute("userRegistInfo", userRegistInfo);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("regist_status", RegisterVo.getRegist_Status());
			request.setAttribute("agent_grade", JoinUsVo.getAgent_Grade());
			//查询详细的上月和本月的信息及总金钱
			//Map<String,String>lastCreate = joinUsService.queryLastCreate(user_id) ;最后订购金额/时间
			//request.setAttribute("lastCreate", lastCreate);
			ArrayList<JoinDetailVo> joinUsDetailNowMouth = joinUsService.queryJoinDetailNowMouth(user_id);
			request.setAttribute("joinUsDetailNowMouth", joinUsDetailNowMouth) ;
			ArrayList<JoinDetailVo> joinUsDetailPreMouth = joinUsService.queryJoinDetailPreMouth(user_id) ;
			request.setAttribute("joinUsDetailPreMouth", joinUsDetailPreMouth) ;
			Map<String,String>sumNow = joinUsService.querySumNow(user_id);
			request.setAttribute("sumNow",sumNow) ;
			Map<String,String>sumPre = joinUsService.querySumPre(user_id);                                                      
			request.setAttribute("sumPre",sumPre) ;
			return "joinUs/joinUsDetail" ;
		}
		/**
		 *查看加盟产品明细
		 *@param request
		 *@param response
		 *@return 
		 */
		@RequestMapping("/queryUserOrder.do")
		public String queryUserOrder(HttpServletResponse response,
				HttpServletRequest request) {
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String user_id = this.getParam(request, "user_id");
			paramMap.put("user_id", user_id);
			int totalCount = joinUsService.queryUserOrderCount(paramMap);//总条数	
			PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			
			ArrayList<JoinDetailVo> userOrder = joinUsService.queryUserOrder(user_id);
			request.setAttribute("userOrder", userOrder);
			request.setAttribute("order_status", JoinDetailVo.getStatus());
			request.setAttribute("isShowList", 1);
			return "joinUs/queryUserOrder" ;
		}
		
		@RequestMapping("/queryGetStatus.do")
		@ResponseBody
		public String useryGetEstate(HttpServletRequest request ,
				HttpServletResponse response ){
			String user_id = request.getParameter("user_id");
			String join =  joinUsService.queryGetEstate(user_id) ;
			try {
				PrintWriter out  = response.getWriter();
				out.write(join);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@RequestMapping("/queryGetCity.do")
		@ResponseBody
		public String getCity(String provinceid,HttpServletRequest request,
				HttpServletResponse response){
			List<AreaVo> list = joinUsService.getCity(provinceid);
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
		public String getArea(String cityid,HttpServletRequest request,
				HttpServletResponse response){
			List<AreaVo> list = joinUsService.getArea(cityid);
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
		
		//批量审核通过
		@RequestMapping("/updateEstatePass.do")
		public String updateEstatePass(HttpServletResponse response,HttpServletRequest request){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

			String user = loginUserVo.getReal_name();
			String province_id = this.getParam(request, "province_id");
			String city_id = this.getParam(request, "city_id");
			String district_id = this.getParam(request, "district_id") ;
			String user_phone = this.getParam(request, "user_phone");
			String beginInfo_time  =this.getParam(request, "beginInfo_time".trim());
			String endInfo_time = this.getParam(request, "endInfo_time".trim());
			List<String>estateList = this.getParamToList(request, "estate");
			StringBuffer sb = new StringBuffer();
			
			for(int i=0;i<estateList.size();i++){
				sb.append("&estate=").append(estateList.get(i));
			}
			
			String [] arry = request.getParameterValues("change") ;
			if(arry != null){
				String content = "【19e火车票】恭喜您的申请已经审核通过！感谢您对火车票业务的支持，使用过程中如有问题请查看帮助指南！";
				for(int i=0;i<arry.length;i++){
					String user_id = arry[i];
					joinUsService.updateEstatePass(user_id) ;
					//修改操作人
					Map<String,String>map = new HashMap<String,String>();
					map.put("user_id", user_id);
					map.put("user", user);
					joinUsService.updateOpt_ren(map);
					
					Map<String,String>maps = joinUsService.queryUpdateJoinUsInfo(user_id);
					
					if(!StringUtils.isEmpty(maps.get("user_phone"))){
						try {
							mobileMsgUtil.send(maps.get("user_phone"), content);
						} catch (Exception e) {
							logger.error("短信发送异常", e);
						}
					}
				}
			}
			request.setAttribute("user_phone", user_phone);
			request.setAttribute("beginInfo_time", beginInfo_time);
			request.setAttribute("endInfo_time", endInfo_time);
			request.setAttribute("province", joinUsService.getProvince()); 
			request.setAttribute("province_id", province_id);
			request.setAttribute("city_id", city_id);
			request.setAttribute("district_id", district_id) ;
			request.setAttribute("city", joinUsService.getCity(province_id));
			request.setAttribute("district", joinUsService.getArea(city_id));
		//request.setAttribute("estates", JoinUsVo.getESTAT()) ;
			//return "redirect:/joinUs/queryJoinUsList.do";
			return "redirect:/joinUs/queryJoinUsList.do?province_id="+province_id+"&city_id="+city_id+"&district_id="+district_id+
			"&user_phone="+user_phone+"&beginInfo_time="+beginInfo_time+"&endInfo_time="+endInfo_time+sb.toString();
		}
		
		//批量审核未通过
		@RequestMapping("/updateEstateNot.do")
		public String updateEstateNot(HttpServletResponse response,HttpServletRequest request){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

			String province_id = this.getParam(request, "province_id");
			String city_id = this.getParam(request, "city_id");
			String district_id = this.getParam(request, "district_id") ;
			String user =loginUserVo.getReal_name();
			String user_phone = this.getParam(request, "user_phone");
			String beginInfo_time  =this.getParam(request, "beginInfo_time".trim());
			String endInfo_time = this.getParam(request, "endInfo_time".trim());
			List<String>estateList = this.getParamToList(request, "estate");
			//保存状态
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<estateList.size();i++){
				sb.append("&estate=").append(estateList.get(i));
			}
			
			String [] arry = request.getParameterValues("change") ;
			if(arry != null){
				for(int i=0;i<arry.length;i++){
					String user_id = arry[i];
					joinUsService.updateEstateNot(user_id) ;
					//修改操作人
					Map<String,String>map = new HashMap<String,String>();
					map.put("user_id", user_id);
					map.put("user", user);
					joinUsService.updateOpt_ren(map);
				}
			}
			request.setAttribute("user_phone", user_phone);
			request.setAttribute("beginInfo_time", beginInfo_time);
			request.setAttribute("endInfo_time", endInfo_time);
			request.setAttribute("province", joinUsService.getProvince()); 
			request.setAttribute("province_id", province_id);
			request.setAttribute("city_id", city_id);
			request.setAttribute("district_id", district_id) ;
			request.setAttribute("city", joinUsService.getCity(province_id));
			request.setAttribute("district", joinUsService.getArea(city_id));
		//request.setAttribute("estates", JoinUsVo.getESTAT()) ;
			//return "redirect:/joinUs/queryJoinUsList.do";
			return "redirect:/joinUs/queryJoinUsList.do?province_id="+province_id+"&city_id="+city_id+"&district_id="+district_id+
			"&user_phone="+user_phone+"&beginInfo_time="+beginInfo_time+"&endInfo_time="+endInfo_time+sb;
		}
		
		
		private String getUser_grade(List<Map<String,String>> userInfoList){
			int passNum = 0;
			int waitNum = 0;
			int nopassNum = 0;
			String user_grade = null;
			if(userInfoList!=null && userInfoList.size()>0){
				//弹窗实名标记
				for(int i=0; i<userInfoList.size(); i++){
			    	String user_status=userInfoList.get(i).get("regist_status");
			    	if(user_status.equals("22")){
			    		passNum = passNum+1;
			    	}
			    	if(user_status.equals("00") || user_status.equals("11") ||user_status.equals("44") ||user_status.equals("55")){
			    		waitNum = waitNum+1;
			    	}
			    	if(user_status.equals("33")){
			    		nopassNum = nopassNum+1;
			    	}
			    }
			    if(passNum==0 && waitNum==0 && nopassNum==0){
			    	user_grade = "未认证";
			    }
			    if(passNum != 0){
			    	if(passNum == 1 ){
			    		user_grade = "铜牌用户";
			    	}if(passNum >=5 ){
			    		user_grade = "金牌用户";
			    	}if(passNum>=2 && passNum<=4){
			    		user_grade = "银牌用户";
			    	}
			    }
			    if(passNum==0 && waitNum!=0){
			    	user_grade = "待审核";
			    }
			    if(passNum==0 && waitNum==0 && nopassNum!=0){
			    	user_grade = "注册失败";
			    }
			    
			}
			return user_grade;
		}
		
}
