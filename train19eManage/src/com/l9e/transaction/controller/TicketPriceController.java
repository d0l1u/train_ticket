package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.TicketPriceService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
/**
 * 票价管理
 * @author guona
 *
 */
@Controller
@RequestMapping("/ticketPrice")
public class TicketPriceController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(NoticeController.class);
	@Resource
	private TicketPriceService ticketPriceService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryTicketPricePage.do")
	public String queryTicketPricePage(HttpServletRequest request, HttpServletResponse response){
		return "ticketPrice/ticketPriceList";
	}
	
	/**
	 * 查询列表页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryTicketPricePageList.do")
	public String queryTicketPricePageList(HttpServletRequest request, HttpServletResponse response){
		
		
		/******************查询条件********************/
		String cc =this.getParam(request, "cc");//车次
		String fz = this.getParam(request, "fz");//发站
		String dz = this.getParam(request, "dz");//到站
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("cc", cc);
		paramMap.put("fz", fz);
		paramMap.put("dz", dz);
		/******************分页条件开始********************/
		int totalCount = ticketPriceService.queryRefundTicketCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> ticketPriceList = 
			ticketPriceService.queryTicketPriceList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("ticketPriceList", ticketPriceList);
		request.setAttribute("cc", cc);
		request.setAttribute("fz", fz);
		request.setAttribute("dz", dz);
		
		request.setAttribute("isShowList", 1);
		return "ticketPrice/ticketPriceList";
	}
	
	/**
	 * 进入更新页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toUpdateTicketPrice.do")
	public String toUpdateTicketPrice(HttpServletRequest request,HttpServletResponse response){
		
		
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String user_Session_level = loginUserVo.getUser_level();
	//	String xh = this.getParam(request, "xh");//票价ID
		String cc = this.getParam(request, "cc");//车次checi
		String fz = this.getParam(request, "fz");//发站fazhan
		String dz = this.getParam(request, "dz");//到站daozhan
		
		try {
			fz = URLDecoder.decode(fz, "utf-8");
			dz = URLDecoder.decode(dz, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String,String>update_Map = new HashMap<String,String>();
		update_Map.put("cc", cc);
		update_Map.put("fz", fz);
		update_Map.put("dz", dz);
		Map<String, String> ticket = ticketPriceService.queryTicketPriceModify(update_Map);
		
		//request.setAttribute("xh", xh);
		request.setAttribute("cc", cc);
		request.setAttribute("fz", fz);
		request.setAttribute("dz", dz);
		request.setAttribute("ticket", ticket);
		return "ticketPrice/ticketPriceModify";
	}
	
	/**
	 * 更新账号信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateTicketPrice.do")
	public String updateAccount(HttpServletRequest request,HttpServletResponse response){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();
		request.setAttribute("opt_name", loginUserVo.getReal_name());
		
		String xh = this.getParam(request, "xh");//票价ID
		String cc = this.getParam(request, "cc");//原车次
		String fz = this.getParam(request, "fz");//原发站
		String dz = this.getParam(request, "dz");//原到站
		String new_cc = this.getParam(request, "new_cc");//新车次
		String new_fz = this.getParam(request, "new_fz");//新发站
		String new_dz = this.getParam(request, "new_dz");//新到站
		
		String yz = this.getParam(request, "yz");//硬座
		String rz = this.getParam(request, "rz");//软座
		String yws = this.getParam(request, "yws");//硬卧上
		String ywz = this.getParam(request, "ywz");//硬卧中
		String ywx = this.getParam(request, "ywx");//硬卧下
		String rws = this.getParam(request, "rws");//软卧上
		String rwx = this.getParam(request, "rwx");//软卧下
		String rz1 = this.getParam(request, "rz1");//一等座
		String rz2 = this.getParam(request, "rz2");//二等座
		String  swz= this.getParam(request, "swz");//商务座
		String tdz = this.getParam(request, "tdz");//特等座
		String gws = this.getParam(request, "gws");//高级软卧上
		String gwx = this.getParam(request, "gwx");//高级软卧下
		String dws = this.getParam(request, "dws");//动卧上
		String dwx = this.getParam(request, "dwx");//动卧下
		logger.info(date+":"+loginUserVo.getReal_name()+"修改票价，票价ID为"+xh);
		/***************************Map存储并修改*****************************/
		Map<String,String>update_Map = new HashMap<String,String>();
		update_Map.put("cc", cc);
		update_Map.put("fz", fz);
		update_Map.put("dz", dz);
		
		update_Map.put("new_cc", new_cc);
		update_Map.put("new_fz", new_fz);
		update_Map.put("new_dz", new_dz);
		
		update_Map.put("xh", xh);
		update_Map.put("yz", yz);
		update_Map.put("rz", rz);
		update_Map.put("yws", yws);
		update_Map.put("ywz", ywz);
		update_Map.put("ywx", ywx);
		update_Map.put("rws", rws);
		update_Map.put("rwx", rwx);
		update_Map.put("rz1", rz1);
		update_Map.put("rz2", rz2);
		update_Map.put("swz", swz);
		update_Map.put("tdz", tdz);
		update_Map.put("gws", gws);
		update_Map.put("gwx", gwx);
		update_Map.put("dws", dws);
		update_Map.put("dwx", dwx);
		Map<String, String> ticket = ticketPriceService.queryTicketPriceModify(update_Map);
		ticketPriceService.updateTicketPrice(update_Map);
		/***************************Map存储并添加到log表里*****************************/
		Map<String,String> log_map = new HashMap<String,String>();
		log_map.put("fz", fz);
		log_map.put("dz", dz);
		log_map.put("cc", cc);
		log_map.put("opt_name", opt_name);
		log_map.put("before_data",ticket.toString() );
		log_map.put("after_data", update_Map.toString());
		//System.out.println("-----------"+update_Map.toString());
		log_map.put("opt_type", "update");
		ticketPriceService.addTicletPriceLogs(log_map);
		
		String fz1=null,dz1=null;
		try {
			fz1 = URLEncoder.encode(fz,"utf-8");
			dz1 = URLEncoder.encode(dz,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "redirect:/ticketPrice/queryTicketPricePageList.do?cc="+cc+"&fz="+fz1+"&dz="+dz1;
	}
	
	/**
	 * 进入增加票价页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAddTicketPrice.do")
	public String ToAddTicketPrice(HttpServletRequest request, HttpServletResponse response){
		String cc = this.getParam(request, "cc");//车次checi
		String fz = this.getParam(request, "fz");//发站fazhan
		String dz = this.getParam(request, "dz");//到站daozhan
		request.setAttribute("cc", cc);
		request.setAttribute("fz", fz);
		request.setAttribute("dz", dz);
		return "ticketPrice/ticketPriceAdd";
	}
	
	/**
	 * 增加票价页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addTicketPrice.do")
	public String AddTicketPrice(HttpServletRequest request, HttpServletResponse response){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();
		request.setAttribute("opt_name", loginUserVo.getReal_name());
		
		
		String cc1 = this.getParam(request, "cc");
		String fz1 = this.getParam(request, "fz");
		String dz1 = this.getParam(request, "dz");
		
		String cc = this.getParam(request, "checi");
		String fz = this.getParam(request, "fazhan");
		String dz = this.getParam(request, "daozhan");
		String yz = this.getParam(request, "yz");//硬座
		String rz = this.getParam(request, "rz");//软座
		String yws = this.getParam(request, "yws");//硬卧上
		String ywz = this.getParam(request, "ywz");//硬卧中
		String ywx = this.getParam(request, "ywx");//硬卧下
		String rws = this.getParam(request, "rws");//软卧上
		String rwx = this.getParam(request, "rwx");//软卧下
		String rz1 = this.getParam(request, "rz1");//一等座
		String rz2 = this.getParam(request, "rz2");//二等座
		String swz = this.getParam(request, "swz");//商务座
		String tdz = this.getParam(request, "tdz");//特等座
		String gws = this.getParam(request, "gws");//高级软卧上
		String gwx = this.getParam(request, "gwx");//高级软卧下
		String dws = this.getParam(request, "dws");//动卧上
		String dwx = this.getParam(request, "dwx");//动卧下
		/***************************Map存储并修改*****************************/
		Map<String,String> add_Map = new HashMap<String,String>();
		add_Map.put("cc", cc);
		add_Map.put("fz", fz);
		add_Map.put("dz", dz);
		add_Map.put("yz", yz);
		add_Map.put("rz", rz);
		add_Map.put("yws", yws);
		add_Map.put("ywz", ywz);
		add_Map.put("ywx", ywx);
		add_Map.put("rws", rws);
		add_Map.put("rwx", rwx);
		add_Map.put("rz1", rz1);
		add_Map.put("rz2", rz2);
		add_Map.put("swz", swz);
		add_Map.put("tdz", tdz);
		add_Map.put("gws", gws);
		add_Map.put("gwx", gwx);
		add_Map.put("dws", dws);
		add_Map.put("dwx", dwx);
		ticketPriceService.addTicketPrice(add_Map);
		/***************************Map存储并添加到log表里*****************************/
		Map<String,String> log_map = new HashMap<String,String>();
		log_map.put("fz", fz);
		log_map.put("dz", dz);
		log_map.put("cc", cc);
		log_map.put("opt_name", opt_name);
		log_map.put("before_data","");
		log_map.put("after_date", add_Map.toString());
		log_map.put("opt_type", "insert");
		ticketPriceService.addTicletPriceLogs(log_map);
		
		String fazhan=null,daozhan=null;
		try {
			fazhan = URLEncoder.encode(fz1,"utf-8");
			daozhan = URLEncoder.encode(dz1,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "redirect:/ticketPrice/queryTicketPricePageList.do?cc="+cc1+"&fz="+fazhan+"&dz="+daozhan;
//		return "redirect:/ticketPrice/queryTicketPricePageList.do";
	}
	
	//查看车次cc、发站fz、到站dz是否存在
	@RequestMapping("/queryTicketPriceAdd.do")
	@ResponseBody
	public String queryTicketPriceAdd(HttpServletRequest request ,HttpServletResponse response){
		String cc = this.getParam(request, "cc");
		String fz=null,dz=null;
		try {
			fz = java.net.URLDecoder.decode(this.getParam(request, "fz"),"UTF-8");
			dz = java.net.URLDecoder.decode(this.getParam(request, "dz"),"UTF-8");;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} 
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("cc", cc);
		paramMap.put("fz", fz);
		paramMap.put("dz", dz);
		int count = ticketPriceService.queryTicketPriceCheci(paramMap);
		String result = null;
		if(count==0){
			result = "yes";
		}else{
			result = "no";
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 进入删除页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toDeleteTicketPrice.do")
	public String toDeleteTicketPrice(HttpServletRequest request,HttpServletResponse response){
		String xh = this.getParam(request, "xh");//票价ID
		String cc = this.getParam(request, "cc");//车次checi
		String fz = this.getParam(request, "fz");//发站fazhan
		String dz = this.getParam(request, "dz");//到站daozhan
		
		String checi = this.getParam(request, "checi");//车次checi
		String fazhan = this.getParam(request, "fazhan");//发站fazhan
		String daozhan = this.getParam(request, "daozhan");//到站daozhan
		Map<String,String>delete_Map = new HashMap<String,String>();
		delete_Map.put("xh", xh);
		delete_Map.put("cc", cc);
		delete_Map.put("fz", fz);
		delete_Map.put("dz", dz);
		logger.info("################start##########");
		logger.info(xh+cc+fz+dz);
		try{
			ticketPriceService.deleteTicketPrice(delete_Map);
		}catch (Exception e) {
			logger.error("异常"+e);
		}
		String fazhan1=null,daozhan1=null;
		try {
			fazhan1 = URLEncoder.encode(fazhan,"utf-8");
			daozhan1 = URLEncoder.encode(daozhan,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "redirect:/ticketPrice/queryTicketPricePageList.do?cc="+checi+"&fz="+fazhan1+"&dz="+daozhan1;
	}
	
	/**
	 * 查看操作日志
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryTicketPriceLogList.do")
	public String queryTicketPriceLogList(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
//		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String,Object>paramMap = new HashMap<String,Object>();
		//分页条件开始
		int totalCount = ticketPriceService.queryTicketPriceLogCount();
		PageVo page = PageUtil.getInstance().paging(request, 5, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		//业务操作
		List<Map<String, Object>> logList = ticketPriceService.queryTicketPriceLogList(paramMap);
		request.setAttribute("logList", logList);
		request.setAttribute("isShowList", 1);
		return "ticketPrice/ticketPriceLogList";
	}
}
