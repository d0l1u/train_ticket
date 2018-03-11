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
import com.l9e.transaction.service.LoginService;
import com.l9e.transaction.service.OpterStatService;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;
/**
 * 登陆统计
 * @author liht
 *
 */
@Controller
@RequestMapping("/opterStat")
public class OpterStatController extends BaseController{
	private static final Logger logger = Logger.getLogger(OpterStatController.class);
	@Resource
	private OpterStatService opterStatService;
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOpterStatPage.do")
	public String queryOpterStatPage(HttpServletRequest request,HttpServletResponse response){
		
		return "opterStat/opterStatList";
	}
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOpterStatList.do")
	public String queryOpterStatList(HttpServletRequest request,HttpServletResponse response){
		//获得系统当前时间
		//String now = DateUtil.nowDate();
		String real_name = this.getParam(request, "real_name");
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request,"end_time");
		Map<String,Object>query_Map = new HashMap<String,Object>();
		query_Map.put("opt_person", real_name);
		query_Map.put("begin_time", begin_time);
		query_Map.put("end_time", end_time);
		int totalCount = opterStatService.queryOpterStatCount(query_Map);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		query_Map.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		query_Map.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String,Object>>opter_List = opterStatService.queryOpterStatList(query_Map);
		
		request.setAttribute("opter_List", opter_List);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		//如果begin_time为空，则显示当前系统时间
//		if(begin_time.equals("")){
//			request.setAttribute("begin_time", now);
//		}else{
//			request.setAttribute("begin_time", begin_time);
//		}
//		//如果end_create_time为空，则显示当前系统时间
//		if(end_time.equals("")){
//			request.setAttribute("end_time", now);
//		}else{
//			request.setAttribute("end_time", end_time);
//		}
		
		request.setAttribute("real_name", real_name);
		request.setAttribute("isShowList", 1);
		
		return "opterStat/opterStatList";
	}
	
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOpterInfo.do")
	public String queryOpterInfo(HttpServletRequest request,HttpServletResponse response){
		String tj_id = this.getParam(request, "tj_id");
		HashMap<String,Object> map = opterStatService.queryOpterInfo(tj_id);
		request.setAttribute("opterInfo", map);
		return "opterStat/opterInfo";
	}
}
