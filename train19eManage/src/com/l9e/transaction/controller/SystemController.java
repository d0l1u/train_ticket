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

import com.l9e.common.BaseController;
import com.l9e.transaction.service.SystemService;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.SystemService;
import com.l9e.transaction.vo.JoinUsVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.SystemVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;


/**
 * 系统配置
 * @author liht
 *
 */
@Controller
@RequestMapping("/system")
public class SystemController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(SystemController.class);

	
	@Resource
	private SystemService systemService;
	
	
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/querySystemPage.do")
	public String querySystemPage(HttpServletRequest request,
			HttpServletResponse response){
		String setting_value = systemService.querySetting();
		request.setAttribute("setting",SystemVo.getSETTING());
		request.setAttribute("setting_value", setting_value);
		request.setAttribute("province", systemService.getProvince());
		return "system/systemList";
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/querySystemList.do")
	public String querySystemList(HttpServletRequest request,HttpServletResponse response){
		String province_id = this.getParam(request, "province_id");
	
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("province_id", province_id);
		int totalCount = systemService.querySystemListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, String>> SystemList = systemService.querySystemList(paramMap);
		request.setAttribute("systemList", SystemList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("isopen", SystemVo.getOpenStatus()); 
		request.setAttribute("iscost", SystemVo.getCostStatus());
		request.setAttribute("isps", SystemVo.getCostStatus());
		request.setAttribute("isbuy", SystemVo.getISBUYMAP());
		request.setAttribute("province", systemService.getProvince()); 
		request.setAttribute("province_id", province_id);
		request.setAttribute("setting",SystemVo.getSETTING());
		String setting_value = systemService.querySetting();
		request.setAttribute("setting_value", setting_value);
		request.setAttribute("set", systemService.querySetting()) ;
		
		return "system/systemList";
	}
	
	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/querySystem.do")
	public String querySystem(HttpServletRequest request,
			HttpServletResponse response){
		String config_id = this.getParam(request, "config_id");
		Map<String, String> System = systemService.querySystem(config_id);
		//BookService.queryBookOrderInfoPs();
		request.setAttribute("system", System);
		//request.setAttribute("bxList", bxList);
		
		return "system/systemInfo";
	}
	
	
	/**
	 * 进入更新页面
	 * @param System
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updatePreSystem.do")
	public String preUpdateSystem(String config_id,HttpServletRequest request,HttpServletResponse response){
		Map<String, String> system = systemService.querySystem(config_id);
		request.setAttribute("system", system);
		request.setAttribute("isopen", SystemVo.getOpenStatus()); 
		request.setAttribute("iscost", SystemVo.getCostStatus());
		request.setAttribute("isps", SystemVo.getCostStatus());
		request.setAttribute("isbuy", SystemVo.getISBUYMAP());
		
		return "system/systemModify";
	}
	
	
	/**
	 * 更新账号信息
	 * @param System
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateSystem.do")
	public String updateSystem(SystemVo system, HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		String opt_ren = loginUserVo.getReal_name();
		system.setOpt_ren(opt_ren);
		systemService.updateSystem(system);
		
		return "redirect:/system/querySystemList.do";
	}
	
	/**
	 * 进入增加页面
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addPreSystem.do")
	public String preAddSystem(Map<String, Object> params, HttpServletRequest request,HttpServletResponse response){
	
		request.setAttribute("isopen", SystemVo.getOpenStatus()); 
		request.setAttribute("iscost", SystemVo.getCostStatus());
		request.setAttribute("isps", SystemVo.getCostStatus());
		request.setAttribute("isbuy", SystemVo.getISBUYMAP());
		request.setAttribute("province", systemService.getProvince());
		
		return "system/systemAdd";
	}
	
	
	/**
	 * 删除账号信息
	 * @param System
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteSystem.do")
	public String deleteSystem(SystemVo system, HttpServletRequest request,HttpServletResponse response){
	
		systemService.deleteSystem(system);
		
		return "redirect:/system/querySystemList.do";
	}
	
	/**
	 * 添加账号信息
	 * @param System
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addSystem.do")
	public String addSystem(SystemVo system,HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		String opt_ren = loginUserVo.getReal_name();
		system.setOpt_ren(opt_ren);
		systemService.insertSystem(system);
		
		return "redirect:/system/querySystemList.do";
	}
	
	
	/**
	 *修改接口
	 *@param request
	 *@param response 
	 */
	@RequestMapping("/updateSetting.do")
	public String  updateSetting(HttpServletRequest request,HttpServletResponse response) {
		String setting_value = this.getParam(request, "setting_value") ;
		systemService.updateSetting(setting_value) ;
		return "redirect:/system/querySystemPage.do" ;
	}
	
	@RequestMapping("updateIsBuy_True.do")
	public String updateIsBuy_True(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		String opt_ren = loginUserVo.getReal_name();
		String [] arry = request.getParameterValues("changeIsBuyTrue") ;
		Map<String,String>modify_Map = null ;
		if(arry != null){
			for(int i=0;i<arry.length;i++){
				String config_id = arry[i];
				modify_Map = new HashMap<String,String>();
				modify_Map.put("config_id", config_id);
				modify_Map.put("is_buyable", "11");
				modify_Map.put("opt_ren", opt_ren);
				systemService.updateEstateNot(modify_Map) ;
			}
		}
		return "redirect:/system/querySystemList.do";
	}
}
