package com.l9e.transaction.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.TuniuBookService;
import com.l9e.transaction.service.TuniuPushTimeoutOrderService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.TuniuBookVo;
import com.l9e.transaction.vo.TuniuPushTimeOutVO;
import com.l9e.util.HttpUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.UrlFormatUtil;

@Controller
@RequestMapping("/tuniuTimeOutOrder")
public class TuniuPushTimeoutOrderController extends BaseController {
	private static final Logger logger = Logger.getLogger(TuniuPushTimeoutOrderController.class);
	
	@Resource
	private TuniuBookService tuniuBookService;

	@Resource
	private TuniuPushTimeoutOrderService tuniuPushTimeoutOrderService;

	private String find_accountinfo;

	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryTuniuTimeOutPage.do")
	public String queryTuniuBookPage(HttpServletRequest request,HttpServletResponse response) {
		Calendar theCa = Calendar.getInstance();
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, -3);
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate = df.format(date);
		//默认勾选未处理
		return "redirect:/tuniuTimeOutOrder/queryTuniuTimeOutList.do?dealStatus=00&dealStatus=22&dealStatus=33&begin_info_time="
					+ querydate;
	}

	@RequestMapping("/queryTuniuTimeOutList.do")
	public String queryTuniuTimeOutList(HttpServletRequest request, HttpServletResponse response) {
		
		//status(超时订单类型)
		//deal_status(处理状态),tuniu_order_pushtimeout
 		String order_id = this.getParam(request, "order_id"); // 订单号
		List<String> dealStatusList = this.getParamToList(request, "dealStatus");//处理状态
		List<String> statusList = this.getParamToList(request,"status"); //超时订单类型
		String begin_info_time = this.getParam(request, "begin_info_time"); //查询开始时间
		String end_info_time = this.getParam(request, "end_info_time"); // 查询结束时间
		Map<String, Object> paramMap = new HashMap<String, Object>(); //   查询map
		
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("deal_status", dealStatusList);
			paramMap.put("status", statusList);
		}
		
		int totalCount = tuniuPushTimeoutOrderService.queryTuniuTimeOutListCount(paramMap);// 总条数
		// 分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数

		List<Map<String, String>> tuniuTimeOutList = tuniuPushTimeoutOrderService.queryTuniuTimeOutList(paramMap);
		
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("statusStr", statusList.toString());
			request.setAttribute("dealStatusStr", dealStatusList.toString());
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
		}
		
		request.setAttribute("Status", TuniuPushTimeOutVO.getPushTimeOutStatus()); // 订单类型
		request.setAttribute("Deal_Status", TuniuPushTimeOutVO.getPushTimeOutDealStatus()); //处理状态
		request.setAttribute("TuniuTimeOutList", tuniuTimeOutList);
		request.setAttribute("isShowList", 1);
		
		return "tuniuPushTimeoutOrder/tuniuPushOrderList";
	}

	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}

	/**
	 * 查询明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/tuniuQueryBookInfo.do")
	public String queryBookInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		Map<String, String> orderInfo = tuniuBookService.queryTuniuBookOrderInfo(order_id);
		List<Map<String, String>> cpList = tuniuBookService.queryTuniuBookOrderInfoCp(order_id);
		List<Map<String, Object>> history = tuniuBookService.queryTuniuHistroyByOrderId(order_id);
//		List<Map<String, Object>> outTicket_info = tuniuBookService.queryTuniuOutTicketInfo(order_id);
		String account = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("orderid", order_id);
		String params = null;
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");

			account = HttpUtil.sendByPost(find_accountinfo, params, "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("history", history);
//		request.setAttribute("outTicket_info", outTicket_info);
		request.setAttribute("account", account);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("bookStatus", TuniuBookVo.getBookStatus());
		request.setAttribute("idstype", TuniuBookVo.getIdstype());
		request.setAttribute("tickettype", TuniuBookVo.getTicketType());
		request.setAttribute("seattype", TuniuBookVo.getSeatType());
		request.setAttribute("cpList", cpList);
		return "tuniuBook/tuniuBookInfo";
	}
	/**
	 * 切换无视截止时间
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateTuniuSwitch_ignore.do")
	public String updateSwitch_ignore(HttpServletResponse response,HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");

		String opt_person = loginUserVo.getReal_name();
		String order_id = this.getParam(request, "order_id");
		String refund_deadline_ignore = this.getParam(request,
				"refund_deadline_ignore");
		String userAccount;
		if (refund_deadline_ignore.equals("1")) {
			userAccount = opt_person + "点击了关闭无视退款时间";
		} else {
			userAccount = opt_person + "点击了开启无视退款时间";
		}
		Map<String, String> log_Map = new HashMap<String, String>();
		log_Map.put("order_id", order_id);
		log_Map.put("user", opt_person);
		log_Map.put("userAccount", userAccount);
		tuniuBookService.addTuniuUserAccount(log_Map);
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", order_id);
		map.put("opt_ren", opt_person);
		map.put("refund_deadline_ignore", refund_deadline_ignore);

		tuniuBookService.updateTuniuSwitch_ignore(map);
		return "redirect:/extBooking/extQueryBookInfo.do?order_id=" + order_id;
	}
	
	
	/**
	 * 修改超时订单的处理状态
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/changeDealStatus.do")
	public void changeDealStatus(HttpServletResponse response,HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
				.getAttribute("loginUserVo");

		String opt_person = loginUserVo.getReal_name();
		String order_id = this.getParam(request, "orderId");
		String changeId = this.getParam(request, "changeId");
		String deal_status = this.getParam(request, "deal_status"); //处理状态
		String type_status = this.getParam(request, "status"); //超时订单类型
			
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("order_id", order_id);
			map.put("changeId", changeId);
			map.put("deal_status", deal_status);
			map.put("type_status", this.getKeyByValue(TuniuPushTimeOutVO.getPushTimeOutStatus(),type_status));
			map.put("opt_ren", opt_person);
			tuniuPushTimeoutOrderService.changeDealStatus(map);
			
			this.writeN2Response(response, "yes");
		} catch (Exception e) {
			logger.info("订单changeDealStatus",e);
			this.writeN2Response(response, "no");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public  String getKeyByValue(Map map, Object value) {  
	        String keys="";  
	        Iterator it = map.entrySet().iterator();  
	        while (it.hasNext()) {
	            Map.Entry entry = (Entry) it.next();  
	            Object obj = entry.getValue();  
	            if (obj != null && obj.equals(value)) {  
	                keys=(String) entry.getKey();
	            }  
	        }  
	        return keys;  
	 }
	 
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		List<Map<String, Object>> history = tuniuBookService.queryTuniuHistroyByOrderId(order_id);
		JSONArray jsonArray = JSONArray.fromObject(history);  
		response.setCharacterEncoding("utf-8");
		//System.out.println(jsonArray.toString());
		try {
			response.getWriter().write(jsonArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
}
