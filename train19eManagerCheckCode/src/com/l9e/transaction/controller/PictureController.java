package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import com.l9e.transaction.service.PictureService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;

@Controller
@RequestMapping("/picture")
public class PictureController extends BaseController {
	private static final Logger logger = Logger.getLogger(PictureController.class);
	@Resource
	private PictureService pictureService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryPicturePage.do")
	public String queryPicturePage(HttpServletRequest request, HttpServletResponse response){
		return "picture/pictureList";
	}
	
	/**
	 * 查询列表页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryPicturePageList.do")
	public String queryPicturePageList(HttpServletRequest request, HttpServletResponse response){
		/******************查询条件********************/
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
		String department = this.getParam(request, "department");
		//String channel = this.getParam(request, "channel");//渠道
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("department", department);
		//paramMap.put("channel", channel);
		/******************分页条件开始********************/
		int totalCount = pictureService.queryPictureCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> pictureList = 
			pictureService.queryPictureList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("pictureList", pictureList);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		//request.setAttribute("channel", channel);
		
		request.setAttribute("isShowList", 1);
		return "picture/pictureList";
	}
	
	/**
	 * 查询打码分时统计页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCodeHour.do")
	public String queryCodeHour(HttpServletRequest request, HttpServletResponse response){
		/******************查询条件********************/
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
//		String department = this.getParam(request, "department");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
//		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = pictureService.queryCodeHourCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 8, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();
		List<Map<String,String>> codeList = pictureService.queryCodeHourList(paramMap);//day_stat\code_count
		
		int ss = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		System.out.println("ss="+ss);
		Map<String, String> mapHour = new HashMap<String, String>();
		for(int i=7;i<23;i++){
			if(i<10){
				mapHour.put("hour"+"0"+i, "0"+i);
			}else{
				mapHour.put("hour"+i+"", i+"");
			}
		}
		for(Map<String, String> daymap : codeList){
			int codeNowCount = 0, sevenCount = 0, afterCount = 0;//当前小时
			List<Map<String, String>> hourcountList = pictureService.queryCodeHourEveryDayList(daymap.get("day_stat"));
			Map<String, Object> map_hour = new HashMap<String, Object>();
			for (Map<String, String> map : hourcountList) {
				String hour_stat = "hour"+map.get("hour_stat").toString();
				long code_count = Integer.valueOf(map.get("code_count").toString()).longValue();
				
				if(Integer.parseInt(map.get("hour_stat"))<=07){
					sevenCount+=code_count;
					map_hour.put("hour07", sevenCount);
				}else if(Integer.parseInt(map.get("hour_stat"))>=22){
					afterCount+=code_count;
					map_hour.put("hour22", afterCount);
				}else{
					map_hour.put(hour_stat, code_count);
				}
				if(Integer.parseInt(map.get("hour_stat"))<=ss){//截止当前
					codeNowCount+=code_count;
				}
			}
			daymap.put("codeNowCount", codeNowCount+"");
			System.out.println("map_hour="+map_hour);//map_hour={08=374, 09=427, 19=420, 22=153}
			String[] keySet2 = (String[]) mapHour.keySet().toArray(new String[16]);
			Arrays.sort(keySet2);
			System.out.println("keySet2="+Arrays.toString(keySet2));//keySet2=[hour07, hour08, hour09, hour10]
			Map<String, String> mapList=new HashMap<String, String>();
			for (Object keyHour : keySet2) {
				if (map_hour.containsKey(keyHour)) {
					mapList.put(keyHour+"", map_hour.get(keyHour)+"");
				} else {
					mapList.put(keyHour+"", "0");
				}
			}
			daymap.putAll(mapList);
			list.add(daymap);
		}
		System.out.println("list="+list);
		/******************Request绑定开始********************/
		request.setAttribute("codeList", list);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
//		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		request.setAttribute("isShowList", 1);
		return "picture/picCodeHour";
	}
	
	
	
	
	/**
	 * 查询人数分时统计页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryUserHour.do")
	public String queryUserHour(HttpServletRequest request, HttpServletResponse response){
		/******************查询条件********************/
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
//		String department = this.getParam(request, "department");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
//		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = pictureService.queryUserHourCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 8, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();
		List<Map<String,String>> codeList = pictureService.queryUserHourList(paramMap);//day_stat\code_count
		
		int ss = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		System.out.println("ss="+ss);
		Map<String, String> mapHour = new HashMap<String, String>();
		for(int i=7;i<23;i++){
			if(i<10){
				mapHour.put("hour"+"0"+i, "0"+i);
			}else{
				mapHour.put("hour"+i+"", i+"");
			}
		}
		for(Map<String, String> daymap : codeList){
			int codeNowCount = 0, sevenCount = 0, afterCount = 0;//当前小时
			List<Map<String, String>> hourcountList = pictureService.queryUserHourEveryDayList(daymap.get("day_stat"));
			Map<String, Object> map_hour = new HashMap<String, Object>();
			for (Map<String, String> map : hourcountList) {
				String hour_stat = "hour"+map.get("hour_stat").toString();
				long code_count = Integer.valueOf(map.get("code_count").toString()).longValue();
				
				if(Integer.parseInt(map.get("hour_stat"))<=07){
					sevenCount+=code_count;
					map_hour.put("hour07", sevenCount);
				}else if(Integer.parseInt(map.get("hour_stat"))>=22){
					afterCount+=code_count;
					map_hour.put("hour22", afterCount);
				}else{
					if(map.get("hour_stat").length()==1){
						map_hour.put("hour0"+map.get("hour_stat").toString(), code_count);
					}else{
						map_hour.put(hour_stat, code_count);
					}
				}
				if(Integer.parseInt(map.get("hour_stat"))<=ss){//截止当前
					codeNowCount+=code_count;
				}
			}
			daymap.put("codeNowCount", codeNowCount+"");
			System.out.println("map_hour="+map_hour);//map_hour={08=374, 09=427, 19=420, 22=153}
			String[] keySet2 = (String[]) mapHour.keySet().toArray(new String[16]);
			Arrays.sort(keySet2);
			System.out.println("keySet2="+Arrays.toString(keySet2));//keySet2=[hour07, hour08, hour09, hour10]
			Map<String, String> mapList=new HashMap<String, String>();
			for (Object keyHour : keySet2) {
				if (map_hour.containsKey(keyHour)) {
					mapList.put(keyHour+"", map_hour.get(keyHour)+"");
				} else {
					mapList.put(keyHour+"", "0");
				}
			}
			daymap.putAll(mapList);
			list.add(daymap);
		}
		System.out.println("list="+list);
		/******************Request绑定开始********************/
		request.setAttribute("userList", list);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
//		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		request.setAttribute("isShowList", 1);
		return "picture/picUserHour";
	}
	
	
	/**
	 * 查询打码成功分时统计页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/querySuccessCodeHour.do")
	public String querySuccessCodeHour(HttpServletRequest request, HttpServletResponse response){
		/******************查询条件********************/
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
//		String department = this.getParam(request, "department");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
//		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = pictureService.queryCodeHourCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 8, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();
		List<Map<String,String>> codeList = pictureService.querySuccessCodeHourList(paramMap);//day_stat\code_count
		
		int ss = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		System.out.println("ss="+ss);
		Map<String, String> mapHour = new HashMap<String, String>();
		for(int i=7;i<23;i++){
			if(i<10){
				mapHour.put("hour"+"0"+i, "0"+i);
			}else{
				mapHour.put("hour"+i+"", i+"");
			}
		}
		for(Map<String, String> daymap : codeList){
			int codeNowCount = 0, sevenCount = 0, afterCount = 0;//当前小时
			List<Map<String, String>> hourcountList = pictureService.querySuccessCodeHourEveryDayList(daymap.get("day_stat"));
			Map<String, Object> map_hour = new HashMap<String, Object>();
			for (Map<String, String> map : hourcountList) {
				String hour_stat = "hour"+map.get("hour_stat").toString();
				long code_count = Integer.valueOf(map.get("code_count").toString()).longValue();
				
				if(Integer.parseInt(map.get("hour_stat"))<=07){
					sevenCount+=code_count;
					map_hour.put("hour07", sevenCount);
				}else if(Integer.parseInt(map.get("hour_stat"))>=22){
					afterCount+=code_count;
					map_hour.put("hour22", afterCount);
				}else{
					map_hour.put(hour_stat, code_count);
				}
				if(Integer.parseInt(map.get("hour_stat"))<=ss){//截止当前
					codeNowCount+=code_count;
				}
			}
			daymap.put("codeNowCount", codeNowCount+"");
			System.out.println("map_hour="+map_hour);//map_hour={08=374, 09=427, 19=420, 22=153}
			String[] keySet2 = (String[]) mapHour.keySet().toArray(new String[16]);
			Arrays.sort(keySet2);
			System.out.println("keySet2="+Arrays.toString(keySet2));//keySet2=[hour07, hour08, hour09, hour10]
			Map<String, String> mapList=new HashMap<String, String>();
			for (Object keyHour : keySet2) {
				if (map_hour.containsKey(keyHour)) {
					mapList.put(keyHour+"", map_hour.get(keyHour)+"");
				} else {
					mapList.put(keyHour+"", "0");
				}
			}
			daymap.putAll(mapList);
			list.add(daymap);
		}
		System.out.println("list="+list);
		/******************Request绑定开始********************/
		request.setAttribute("codeList", list);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
//		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		request.setAttribute("isShowList", 1);
		return "picture/picSuccessCodeHour";
	}
	
	
	
	/**
	 * 查询打码失败分时统计页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryFailCodeHour.do")
	public String queryFailCodeHour(HttpServletRequest request, HttpServletResponse response){
		/******************查询条件********************/
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
//		String department = this.getParam(request, "department");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
//		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = pictureService.queryCodeHourCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 8, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();
		List<Map<String,String>> codeList = pictureService.queryFailCodeHourList(paramMap);//day_stat\code_count
		
		int ss = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		System.out.println("ss="+ss);
		Map<String, String> mapHour = new HashMap<String, String>();
		for(int i=7;i<23;i++){
			if(i<10){
				mapHour.put("hour"+"0"+i, "0"+i);
			}else{
				mapHour.put("hour"+i+"", i+"");
			}
		}
		for(Map<String, String> daymap : codeList){
			int codeNowCount = 0, sevenCount = 0, afterCount = 0;//当前小时
			List<Map<String, String>> hourcountList = pictureService.queryFailCodeHourEveryDayList(daymap.get("day_stat"));
			Map<String, Object> map_hour = new HashMap<String, Object>();
			for (Map<String, String> map : hourcountList) {
				String hour_stat = "hour"+map.get("hour_stat").toString();
				long code_count = Integer.valueOf(map.get("code_count").toString()).longValue();
				
				if(Integer.parseInt(map.get("hour_stat"))<=07){
					sevenCount+=code_count;
					map_hour.put("hour07", sevenCount);
				}else if(Integer.parseInt(map.get("hour_stat"))>=22){
					afterCount+=code_count;
					map_hour.put("hour22", afterCount);
				}else{
					map_hour.put(hour_stat, code_count);
				}
				if(Integer.parseInt(map.get("hour_stat"))<=ss){//截止当前
					codeNowCount+=code_count;
				}
			}
			daymap.put("codeNowCount", codeNowCount+"");
			System.out.println("map_hour="+map_hour);//map_hour={08=374, 09=427, 19=420, 22=153}
			String[] keySet2 = (String[]) mapHour.keySet().toArray(new String[16]);
			Arrays.sort(keySet2);
			System.out.println("keySet2="+Arrays.toString(keySet2));//keySet2=[hour07, hour08, hour09, hour10]
			Map<String, String> mapList=new HashMap<String, String>();
			for (Object keyHour : keySet2) {
				if (map_hour.containsKey(keyHour)) {
					mapList.put(keyHour+"", map_hour.get(keyHour)+"");
				} else {
					mapList.put(keyHour+"", "0");
				}
			}
			daymap.putAll(mapList);
			list.add(daymap);
		}
		System.out.println("list="+list);
		/******************Request绑定开始********************/
		request.setAttribute("codeList", list);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
//		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		request.setAttribute("isShowList", 1);
		return "picture/picFailCodeHour";
	}
}
