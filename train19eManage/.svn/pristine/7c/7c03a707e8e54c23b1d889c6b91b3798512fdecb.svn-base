package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.CtripAccountService;
import com.l9e.transaction.service.CtripService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.CtripVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.SwitchUtils;


/**
 * 携程出票管理
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/ctrip")
public class CtripOrderController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(CtripOrderController.class);
	@Resource
	private CtripService ctripService;

	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCtripPage.do")
	public String queryCtripPage(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入查询页面】queryCtripPage.do");
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, 0); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		return "redirect:/ctrip/queryCtripList.do?go_status=00&create_time_px=up&begin_info_time="+querydate;
		}
	
	
	/**
	 * 携程出票列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("queryCtripList.do")
	public String queryCtripList(HttpServletRequest request,HttpServletResponse response){
		logger.info("【查询列表】queryCtripList.do");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> statusList = this.getParamToList(request, "go_status");
		String order_id = this.getParam(request, "order_id");
		String ctrip_order_id = this.getParam(request, "ctrip_order_id");
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else if(ctrip_order_id.trim().length()>0){
			paramMap.put("ctrip_order_id", ctrip_order_id);
		}else{
		paramMap.put("go_status", statusList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		}
		//按乘车时间排序
		String travel_time_px=this.getParam(request, "travel_time_px");
		if("up".equals(travel_time_px))paramMap.put("travel_time_asc",travel_time_px);
		else if("down".equals(travel_time_px))paramMap.put("travel_time_desc",travel_time_px);
		request.setAttribute("travel_time_px", travel_time_px);
		//按创建时间排序
		String create_time_px=this.getParam(request, "create_time_px");
		if("up".equals(create_time_px))paramMap.put("create_time_asc",create_time_px);
		else if("down".equals(create_time_px))paramMap.put("create_time_desc",create_time_px);
		request.setAttribute("create_time_px", create_time_px);
		//按发车时间排序
		String out_ticket_time_px=this.getParam(request, "out_ticket_time_px");
		if("up".equals(out_ticket_time_px))paramMap.put("out_ticket_time_asc",out_ticket_time_px);
		else if("down".equals(out_ticket_time_px))paramMap.put("out_ticket_time_desc",out_ticket_time_px);
		
		if("".equals(travel_time_px) && "".equals(create_time_px) && "".equals(out_ticket_time_px)){
			paramMap.put("create_time_asc","up");
			request.setAttribute("create_time_px", "up");
		}
		request.setAttribute("out_ticket_time_px", out_ticket_time_px);
		
		paramMap.put("manual_order", "22");
		int totalCount = ctripService.queryCtripCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, String>> ctripList = ctripService.queryCtripList(paramMap);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else if(ctrip_order_id.trim().length()>0){
			request.setAttribute("ctrip_order_id", ctrip_order_id);
		}else{
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("statusStr", statusList.toString());
			
		}
		request.setAttribute("seat_Types", AcquireVo.getSEAT_TYPES());
		request.setAttribute("ctripStatus", CtripVo.getCtripGoStatus());
		request.setAttribute("ctripList", ctripList);
		request.setAttribute("isShowList", 1);
		return "ctrip/ctripList";
	}
	

	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCtripInfo.do")
	public String queryCtripInfo(HttpServletRequest request,
			HttpServletResponse response){
		String order_id = this.getParam(request,"order_id");
		String canOperation = this.getParam(request,"canOperation");
		String query_type = this.getParam(request, "query_type");
		logger.info("【查询明细】queryCtripInfo.do 【订单号："+order_id+"】");
		Map<String, String> orderInfo = ctripService.queryCtripOrderInfo(order_id);
		List<Map<String, Object>> cpList = ctripService.queryCtripOrderInfoCp(order_id);
		List<Map<String, Object>> history = ctripService.queryHistroyByOrderId(order_id);
		
		/*******************以下为订单与车票信息拼接的信息***********************/
		StringBuffer sb = new StringBuffer();
		String acc_username="",acc_password="";
		String from_time_all="",from_city="",to_city="",train_no="";
		String seat_type="",ticket_type="",user_name="",cert_type="",cert_no="",telephone="";
		if(orderInfo.get("acc_username")!=null){
			acc_username=orderInfo.get("acc_username");
		}
		if(orderInfo.get("acc_password")!=null){
			acc_password=orderInfo.get("acc_password");
		}
		if(orderInfo.get("from_time_all")!=null){
			from_time_all=orderInfo.get("from_time_all");
			from_time_all =from_time_all.substring(0,from_time_all.indexOf(" ")).trim();
		}
		if(orderInfo.get("from_city")!=null){
			from_city=orderInfo.get("from_city");
		}
		if(orderInfo.get("to_city")!=null){
			to_city=orderInfo.get("to_city");
		}
		if(orderInfo.get("train_no")!=null){
			train_no=orderInfo.get("train_no");
		}
		sb.append(acc_username+"|"+acc_password+"&");
		sb.append(order_id+"|"+from_time_all+"|"+from_city+"|"+to_city+"|"+train_no);
		int index = 0;
		for(Map<String,Object>cp_Map:cpList){
			index++;
			String cp_id= cp_Map.get("cp_id").toString();
			if(cp_Map.get("seat_type")!=null){
				seat_type=cp_Map.get("seat_type").toString();
				cp_Map.put("seat_type", Integer.parseInt(seat_type));
			}
			if(cp_Map.get("ticket_type")!=null){
				ticket_type=cp_Map.get("ticket_type").toString();
				cp_Map.put("ticket_type", Integer.parseInt(ticket_type));
			}
			if(cp_Map.get("user_name")!=null){
				user_name=cp_Map.get("user_name").toString();
			}
			if(cp_Map.get("cert_type")!=null){
				cert_type=cp_Map.get("cert_type").toString();
				cp_Map.put("cert_type", Integer.parseInt(cert_type));
			}
			if(cp_Map.get("cert_no")!=null){
				cert_no=cp_Map.get("cert_no").toString();
			}
			if(cp_Map.get("telephone")!=null){
				telephone=cp_Map.get("telephone").toString();
			}
			sb.append("&"+cp_id+"|"+seat_type+"|"+ticket_type+"|"+user_name+"|"+cert_type+"|"+
					cert_no+"|"+telephone+"|"+index);
		}
		/***********************拼接结束************************/
		String ext_seattype = orderInfo.get("ext_seattype").toString();// 41#1,23.00|2, 24.00|3,33.00
		List<Map<String, String>> seatList = new ArrayList<Map<String, String>>();
		if(StringUtil.isNotEmpty(ext_seattype) && !SwitchUtils.splitStr1Last(ext_seattype).equals("无")){
			String sp = SwitchUtils.splitStr1Pre(ext_seattype);//41
			String sp0 = sp.substring(0,1);//4
			String sp1 = SwitchUtils.splitStr1Last(ext_seattype);//1,23.00|2, 24.00|3,33.00
			
			Map<String, String> seatMap = null;
			if(StringUtil.isNotEmpty(sp1)){
				for(String str : sp1.split("\\|")){
					seatMap = new HashMap<String, String>();
					String type = str.split(",")[0];
					String money = str.split(",")[1];
					seatMap.put("s_type", type);
					seatMap.put("money", money);
					seatList.add(seatMap);
				}
			}
		}
		
		request.setAttribute("cpIndoSb", sb);
		request.setAttribute("seat_type", AcquireVo.getSEAT_TYPES());
		request.setAttribute("seatList", seatList);
		request.setAttribute("history", history);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("canOperation", canOperation);
		request.setAttribute("ctripStatus", CtripVo.getCtripGoStatus());
		request.setAttribute("idstype", BookVo.getIdstype());
		request.setAttribute("tickettype", BookVo.getTicketType());
		request.setAttribute("seattype", BookVo.getSeattype());
		request.setAttribute("bank_type", AcquireVo.getBank());
		request.setAttribute("cpList", cpList);
		request.setAttribute("query_type", query_type);
		return "ctrip/ctripInfo";
	}
	
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		List<Map<String, Object>> history = ctripService.queryHistroyByOrderId(order_id);
		JSONArray jsonArray = JSONArray.fromObject(history);  
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(jsonArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
//	/**
//	 *获取账号 
//	 */
//	@RequestMapping("/queryCtripAccount.do")
//	@ResponseBody
//	public void queryCtripAccount(HttpServletResponse response,HttpServletRequest request){
//		String result = "fail";
//		try{
//			String order_id = this.getParam(request, "order_id");
//				Map<String,String>map = new HashMap<String,String>();
//				map.put("order_id", order_id);
//			HashMap<String, String> ctripInfo = ctripService.queryCtripAccount(map);
//			
//			/* 创建JSON对象*/
//			JSONObject jsonObj = new JSONObject();  
//			/* 给JSON赋值 */
//			jsonObj.put("ctrip_order_id", ctripInfo.get("ctrip_order_id"));
//			jsonObj.put("ctrip_name", ctripInfo.get("ctrip_name"));
//			jsonObj.put("ctrip_password", ctripInfo.get("ctrip_password"));
//			result = jsonObj.toString();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		response.setCharacterEncoding("utf-8");
//		try {
//			response.getWriter().write(result);
//			response.getWriter().flush();
//			response.getWriter().close();
//		} catch (IOException e){
//			e.printStackTrace();
//		}
//	}
}
















