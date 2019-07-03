package com.l9e.transaction.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.AppUserService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AppUserVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;

@Controller
@RequestMapping("/appUser")
public class AppUserController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(AppUserController.class);

	@Resource
	private AppUserService appUserService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAppUserPage.do")
	public String queryAppUserPage(HttpServletRequest request,HttpServletResponse response){
		return "redirect:/appUser/queryAppUserList.do";
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAppUserList.do")
	public String queryAppUserList(HttpServletRequest request,HttpServletResponse response){
		/*************************查询条件***************************/
		String user_name = this.getParam(request, "user_name");
		String user_phone = this.getParam(request, "user_phone");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> user_source = this.getParamToList(request, "channel");
		List<String> weather_able = this.getParamToList(request, "weather_able");
		String score_num = this.getParam(request, "score_num");//积分(0-99)
		String score_num_begin = null;
		String score_num_end = null;
		if(StringUtil.isNotEmpty(score_num)){
			score_num_begin = score_num.split("-")[0];
			score_num_end = score_num.split("-")[1];
		}
		String login_num = this.getParam(request, "login_num");//登陆次数(0-99)
		String login_num_begin = null;
		String login_num_end = null;
		if(StringUtil.isNotEmpty(login_num)){
			login_num_begin = login_num.split("-")[0];
			login_num_end = login_num.split("-")[1];
		}
		/*************************创建Map***************************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_name", user_name);
		paramMap.put("user_phone", user_phone);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("user_source", user_source);
		paramMap.put("weather_able", weather_able);
		paramMap.put("score_num_begin", score_num_begin);
		paramMap.put("score_num_end", score_num_end);
		paramMap.put("login_num_begin", login_num_begin);
		paramMap.put("login_num_end", login_num_end);
		/*************************分页条件***************************/
		int totalCount = appUserService.queryAppUserListCount(paramMap);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/*************************执行查询***************************/
		List<Map<String, Object>> appUserList = appUserService.queryAppUserList(paramMap);
		for(int i=0; i<appUserList.size(); i++){
			Map<String, Object> user = appUserList.get(i);
			String user_id = user.get("user_id").toString();
			int referee_account_num = appUserService.queryRefereeAccountNum(user_id);
			int linker_num = appUserService.queryLinkerNum(user_id);
			user.put("referee_account_num", referee_account_num);
			user.put("linker_num", linker_num);
		}
		/*************************request绑定***************************/
		request.setAttribute("channelSource", AppUserVo.getChannels());
		request.setAttribute("weatherAble", AppUserVo.getWeatherAble());
		request.setAttribute("user_name", user_name);
		request.setAttribute("isShowList", 1);
		request.setAttribute("user_phone", user_phone);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time); 
		request.setAttribute("channel", user_source);
		request.setAttribute("weather_able", weather_able);
		request.setAttribute("score_num", score_num);
		request.setAttribute("login_num", login_num); 
		request.setAttribute("appUserList", appUserList);
		request.setAttribute("totalCount", totalCount);
		return "appUser/appUserList";
	}
	
	
	
	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAppUserInfo.do")
	public String queryAppUserInfo(HttpServletRequest request,HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		String referee_account_num = this.getParam(request, "referee_account_num");//联系人个数
		Map<String, String> userInfo = appUserService.queryAppUserInfo(user_id);
		/*************************分页条件***************************/
		int totalCount = appUserService.queryAppUserLinkerListCount(user_id);//总条数	
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, Object>> linkerList = appUserService.queryAppUserLinkerList(paramMap);
		
		request.setAttribute("userInfo", userInfo);
		request.setAttribute("linkerList", linkerList);
		request.setAttribute("channelSource", AppUserVo.getChannels());
		request.setAttribute("weatherAble", AppUserVo.getWeatherAble());
		request.setAttribute("ticketType", AppUserVo.getTicket_Types());
		request.setAttribute("verifyStatus", AppUserVo.getLinkerVerifyStatus());
		request.setAttribute("referee_account_num", referee_account_num);
		request.setAttribute("totalCount", totalCount);
		
		return "appUser/appUserInfo";
	}
	
	
	/**
	 * 进入更新页面
	 * @param account
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toUpdateAppUser.do")
	public String toUpdateAppUser(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		Map<String, String> userInfo = appUserService.queryAppUserInfo(user_id);
		request.setAttribute("userInfo", userInfo);
		return "appUser/appUserUpdate";
	}

	
	/**
	 * 更新账号信息
	 * @param account
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateAppUser.do")
	public String updateAppUser(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_ren =loginUserVo.getReal_name();//获取当前登录的人
		String user_id = this.getParam(request, "user_id");
		String user_password = this.getParam(request, "user_password");
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("user_id", user_id);
		paramMap.put("user_password", user_password);
		paramMap.put("opt_ren", opt_ren);
		appUserService.updateAppUser(paramMap);
		logger.info(opt_ren+"【更新】"+user_id+"的密码为"+user_password);
		//return "account/accountModify";
		return "redirect:/appUser/queryAppUserList.do";
	}
	
	
	/**
	 * 删除账号信息
	 * @param account
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteAppUser.do")
	public void deleteAppUser(HttpServletRequest request,HttpServletResponse response){
		String result="yes";
		try {
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String opt_ren =loginUserVo.getReal_name();//获取当前登录的人
			String user_id = this.getParam(request, "user_id");
			appUserService.deleteAppUser(user_id);
			logger.info(opt_ren+"【删除】"+user_id+"的信息");
		} catch (Exception e) {
			result="no";
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//return "redirect:/appUser/queryAppUserList.do";
	}
	
	//批量停用
	@RequestMapping("/updateAppUserbatch.do")
	public String updateAbatch(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_ren =loginUserVo.getReal_name();//获取当前登录的人
		String[] user_ids = request.getParameterValues("user_id");
		String pageIndex = this.getParam(request, "pageIndex");
		String type = this.getParam(request, "type").toString();
		if(type.equals("4")){
			for (int i = 0; i < user_ids.length; i++) {
				String user_id = user_ids[i];
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("user_id", user_id);
				paramMap.put("weather_able", "0");
				paramMap.put("opt_ren", opt_ren);
				appUserService.updateAppUserStop(paramMap);
			}
			logger.info(opt_ren+"批量停用"+user_ids.length+"个用户，其user_id为："+Arrays.toString(user_ids));
		}else if(type.equals("1")){
			for (int i = 0; i < user_ids.length; i++) {
				String user_id = user_ids[i];
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("user_id", user_id);
				paramMap.put("weather_able", "1");
				paramMap.put("opt_ren", opt_ren);
				appUserService.updateAppUserStop(paramMap);
			}
			logger.info(opt_ren+"批量启用"+user_ids.length+"个用户，其user_id为："+Arrays.toString(user_ids));
		}
		
		return "redirect:/appUser/queryAppUserList.do?pageIndex="+pageIndex;
	}
	
}
