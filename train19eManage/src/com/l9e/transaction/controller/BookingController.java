package com.l9e.transaction.controller;

import java.io.IOException;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.servlet.InitConfigServlet;
import com.l9e.transaction.service.BookService;
import com.l9e.transaction.service.LoginService;
import com.l9e.transaction.service.RefundTicketService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.DifferVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RefundTicketVo;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatUtil;

/**
 * 预订管理
 * @author liht
 *
 */
@Controller
@RequestMapping("/booking")
public class BookingController extends BaseController  {
	
	private static final Logger logger = Logger.getLogger(BookingController.class);
	
	@Resource
	private BookService BookService;
	@Resource
	private RefundTicketService refundTicketService;
	
	
	private String find_accountinfo;
	private String cancle_order_url;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryBookPage.do")
	public String queryBookPage(HttpServletRequest request,
			HttpServletResponse response){
		/*****************************省级代理省份方法*****************************/
		System.out.println("123333");
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, -3); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		if(loginUserVo.getUser_level().equals("1.1")&& loginUserVo.getSupervise_name()!=null){
			String str = loginUserVo.getSupervise_name();
			List<String>Supervise_name_List =  SwitchUtils.strToList(str);
			List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
			Map<String,String>area_Map = new HashMap<String,String>();
			for(int i=0;i<Supervise_name_List.size();i++){
				area_Map=BookService.querySupervise_nameToArea_no(Supervise_name_List.get(i).toString());
				area_no_list.add(area_Map);
			}
			request.setAttribute("province",area_no_list); 
			request.setAttribute("bookStatus", BookVo.getBookStatus());
			return "book/bookList"; 
		}else{
			return "redirect:/booking/queryBookList.do?order_status=11&order_status=12&order_status=22&order_status=33&queryTime=0&begin_info_time="+querydate;
		}
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryBookList.do")
	public String queryBookList(HttpServletRequest request,
			HttpServletResponse response){
		//获得系统当前时间
		//String now = DateUtil.nowDate();
		//查询参数
		String order_id = this.getParam(request, "order_id");
		String user_phone = this.getParam(request, "user_phone");
		String at_province_id = this.getParam(request, "at_province_id");
		String at_city_id = this.getParam(request, "at_city_id");
		List<String> statusList = this.getParamToList(request, "order_status");
		List<String> order_sourceList = this.getParamToList(request, "order_source");
		//String eop_order_id = this.getParam(request, "eop_order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		//出票开始时间结束时间
		String begin_out_time = this.getParam(request, "begin_out_time");
		String end_out_time = this.getParam(request, "end_out_time");
		String queryTime = this.getParam(request, "queryTime");
		
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("at_province_id", at_province_id);
			paramMap.put("at_city_id", this.getParam(request, "at_city_id"));
			paramMap.put("user_phone",user_phone );
			//paramMap.put("eop_order_id",eop_order_id);
			paramMap.put("out_ticket_billno",out_ticket_billno );
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("begin_out_time", begin_out_time);
			paramMap.put("end_out_time", end_out_time);
			paramMap.put("order_status", statusList);
			paramMap.put("order_source", order_sourceList);
			paramMap.put("queryTime", queryTime);
		}
		int totalCount = BookService.queryBookListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String, String>> bookList = BookService.queryBookList(paramMap);
		Map<String, String> bookIdAndName = new HashMap<String,String>();
		for(int i=0;i<bookList.size();i++){
			Map<String, String> bookInfo = bookList.get(i);
			String province_id = bookInfo.get("at_province_id");
			String provinceName = InitConfigServlet.PROMAP.get(province_id);
			String city_id = bookInfo.get("at_city_id");
			String cityName = InitConfigServlet.CITYMAP.get(city_id);
			bookIdAndName.put(province_id, provinceName);
			bookIdAndName.put(city_id, cityName);
		}
		/*****************************省级代理省份方法*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		if(loginUserVo.getUser_level().equals("1.1")&& loginUserVo.getSupervise_name()!=null){
			String str = loginUserVo.getSupervise_name();
			List<String>Supervise_name_List =  SwitchUtils.strToList(str);
			List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
			Map<String,String>area_Map = new HashMap<String,String>();
			for(int i=0;i<Supervise_name_List.size();i++){
				area_Map=BookService.querySupervise_nameToArea_no(Supervise_name_List.get(i).toString());
				area_no_list.add(area_Map);
			}
			request.setAttribute("province",area_no_list); 
		}else{
			request.setAttribute("province", BookService.getProvince()); 
		}
		if(order_id.trim().length()>0){
		request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("user_phone", user_phone);
			request.setAttribute("statusStr", statusList.toString());
			request.setAttribute("orderSourceStr", order_sourceList.toString());
			request.setAttribute("at_province_id", at_province_id);
			request.setAttribute("at_city_id", at_city_id);
			//request.setAttribute("eop_order_id", eop_order_id);
			request.setAttribute("out_ticket_billno", out_ticket_billno);
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("begin_out_time", begin_out_time);
			request.setAttribute("end_out_time", end_out_time);
			request.setAttribute("queryTime", queryTime);
		}
		request.setAttribute("bookStatus", BookVo.getBookStatus());
		request.setAttribute("orderSource", BookVo.getOrderSource());
		request.setAttribute("refundStatus", RefundVo.getStatus());
		request.setAttribute("differStatus", DifferVo.getStatus());
		request.setAttribute("bookList", bookList);
		request.setAttribute("isShowList", 1);
		
		request.setAttribute("city", BookService.getCity(at_province_id));
		request.setAttribute("proMap", bookIdAndName);
		return "book/bookList";
	}
	
	
	
	
	@Value("#{propertiesReader[find_accountinfo]}")
	public void setFind_accountinfo(String find_accountinfo) {
		this.find_accountinfo = find_accountinfo;
	}
	
	@Value("#{propertiesReader[cancle_order_url]}")
	public void setCancle_order_url(String cancle_order_url) {
		this.cancle_order_url = cancle_order_url;
	}
	
	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryBookInfo.do")
	public String queryBookInfo(HttpServletRequest request,
			HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");
		Map<String, String> orderInfo = BookService.queryBookOrderInfo(order_id);
		List<Map<String, String>> bxList = BookService.queryBookOrderInfoBx(order_id);
		List<Map<String, String>> cpList = BookService.queryBookOrderInfoCp(order_id);
		List<Map<String, Object>> history = BookService.queryHistroyByOrderId(order_id);
		List<Map<String, Object>> outTicket_info = BookService.queryOutTicket_info(order_id);
		String account = null;
		//if(StringUtils.equals(orderInfo.get("refund_status"), RefundVo.PRE_REFUND)){
			Map<String, String> paramMap  = new HashMap<String, String>();
			
			paramMap.put("orderid", order_id);
			
			String params=null;
			try {
				params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				
				account = HttpUtil.sendByPost(find_accountinfo, params, "UTF-8");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}
		String str = orderInfo.get("refund_deadline_ignore");
		String strSwitch = SwitchUtils.switch1Or0(str);
		
		request.setAttribute("ignoreStatus", BookVo.getIgnore());
		request.setAttribute("strSwitch", strSwitch);
		request.setAttribute("history", history);
		request.setAttribute("outTicket_info", outTicket_info);
		request.setAttribute("account", account);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("refund_types", RefundTicketVo.getRefund_Types());
		request.setAttribute("refund_statuses", RefundTicketVo.getRefund_Status());
		request.setAttribute("bookStatus", BookVo.getBookStatus());
		request.setAttribute("refundStatus", RefundVo.getStatus());
		request.setAttribute("idstype", BookVo.getIdstype());
		request.setAttribute("tickettype", BookVo.getTicketType());
		request.setAttribute("seattype", BookVo.getSeattype());
		
		request.setAttribute("cpList", cpList);
		
		return "book/bookInfo";
	}

	
	@RequestMapping("/updateRefund.do")
	public String refunding(RefundVo refund, HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		//获取当前登录人
		String user = loginUserVo.getReal_name();
		String order_id = this.getParam(request, "order_id");
		//创建一个Map保存order_id和登录人
		Map<String,String>userMap=new HashMap<String,String>();
		userMap.put("opt_ren", user);
		userMap.put("order_id", order_id);
		//修改订单状态
		BookService.refunding(refund,userMap);
		
		String userAccount = user+"点击了退款按钮!";
		Map<String,String>paramMap = new HashMap<String,String>();
		paramMap.put("order_id", order_id);
		paramMap.put("user", user);
		paramMap.put("userAccount", userAccount);
		BookService.addUserAccount(paramMap);
		return "redirect:/booking/queryBookList.do";
	}
	
	
	//判断该用户是否已经提交电话
	@RequestMapping("/refund_register.do")
	public String refund_register(HttpServletRequest request ,HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");
		String cp_id = this.getParam(request, "cp_id");	
		String cp=null;
		String result = null;
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("cp_id", cp_id);
		List<Map<String, String>> cpList = refundTicketService.queryRefundTicket(paramMap);
		if(cpList.size()>0){
			cp=cpList.get(0).toString();
		}
		
		if(StringUtils.isEmpty( cp ) || cp.length()==0){
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
	 * 电话退款
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/refund_success.do")
	public String refund_success(RefundVo refund, HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		//获取当前登录人
		String user = loginUserVo.getReal_name();
		String order_id = this.getParam(request, "order_id");			//订单号
		String eop_order_id = this.getParam(request, "eop_order_id");			//eop
		List<String> cpIdList=this.getParamToList(request, "checkcp");
		String user_remark ="距发车4小时以上且未换取纸质车票";
		String opt_person_log ="";
//		Map<String,String>userMap=new HashMap<String,String>();
//		userMap.put("opt_ren", user);
//		userMap.put("order_id", order_id);
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~"+order_id);
		for(int i=0;i<cpIdList.size();i++){
			String cpId = cpIdList.get(i);
			String refund_money = this.getParam(request, "refund_money_"+cpId);	
			String refund_12306_seq = this.getParam(request, "refund_12306_seq_"+cpId);	
			System.out.println("!!!!!!!!!!!"+cpId+"###########"+refund_money);
			opt_person_log = user+"点击了【电话退款】退票完成按钮!orderId为："+order_id+",金额为："+refund_money+",退款原因为："+user_remark+",退款流水号："+refund_12306_seq;
			//创建一个Map保存order_id和登录人
			Map<String, Object> map_add = new HashMap<String, Object>();
			map_add.put("order_id", order_id);
			map_add.put("eop_order_id", eop_order_id);
			map_add.put("cp_id", cpId);
			map_add.put("actual_refund_money", refund_money);
			map_add.put("refund_money", refund_money);
			map_add.put("refund_type", "6");
			map_add.put("refund_seq", CreateIDUtil.createID("CE"));
			map_add.put("refund_12306_seq", refund_12306_seq);
			
			map_add.put("create_time", "now()");
			map_add.put("refund_plan_time", "DATE_ADD(now(),INTERVAL 5 MINUTE)");
			map_add.put("refund_status", "00");
			map_add.put("notify_num", "0");
			map_add.put("opt_person", user);
			map_add.put("user_remark", user_remark);
			Map<String,String>log_Map = new HashMap<String,String>();    
			/****************将条件添加到容器******************/
			log_Map.put("order_id", order_id);
			log_Map.put("eop_order_id", eop_order_id);
			log_Map.put("cp_id", cpId);
			log_Map.put("actual_refund_money",refund_money );
			log_Map.put("opt_person", user);
			log_Map.put("refund_money", refund_money);
			log_Map.put("refund_type", "2");
			log_Map.put("refund_seq", CreateIDUtil.createID("CE"));
			log_Map.put("refund_12306_seq", refund_12306_seq);
			log_Map.put("refund_status", "11");
			log_Map.put("notify_num", "0");
			log_Map.put("user_remark", user_remark);
			log_Map.put("opt_person_log", opt_person_log);
			refundTicketService.queryrefundTicketTelAdd(log_Map,map_add);//退款的乘客信息添加到hc_orderinfo_refundstream表
		}
		
		/****************执行操作******************/
		logger.info(opt_person_log+"验证未通过"+"order_id="+order_id);
		//修改订单状态
//		BookService.refunding(refund,userMap);
		
//		String userAccount = user+"点击了【电话退款】退票完成按钮!";
//		Map<String,String>paramMap = new HashMap<String,String>();
//		paramMap.put("order_id", order_id);
//		paramMap.put("user", user);
//		paramMap.put("userAccount", userAccount);
//		BookService.addUserAccount(paramMap);
		return "redirect:/booking/queryBookList.do";
	}
	
	
	
	/**
	 * 取消预约
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/cancel_book.do")
	public String cancel_book(RefundVo refund, HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		//获取当前登录人
		String opt_ren = loginUserVo.getReal_name();
		String orderId = this.getParam(request, "orderId");			//订单号
		//String params=null;
		String result = null;
//		Map<String, String> paramMap  = new HashMap<String, String>();
//		paramMap.put("orderId", orderId);
//		
//		try {
//			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
//			
//			result = HttpUtil.sendByPost(cancle_order_url, params, "UTF-8");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		Map<String, String> paramMap1  = new HashMap<String, String>();
		paramMap1.put("orderId", orderId);
		String params=null;
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap1, "", "UTF-8");
			//acquire = HttpUtil.sendByPost(currentCodingInfo, params, "UTF-8");
			//acquire = PostRequestUtil.getPostRes("UTF-8", params, currentCodingInfo);
			result = HttpPostUtil.sendAndRecive(cancle_order_url,params);
			logger.info("取消预约："+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("请求通知出票接口返回：" + result);
		
		if(result.equals("success")){
			Map<String, String> paramMap  = new HashMap<String, String>();
			paramMap.put("order_id", orderId);
			paramMap.put("opt_ren", opt_ren);
			paramMap.put("order_status", "78");
			BookService.updateCancel_book(paramMap);
			String userAccount = opt_ren+"在预订管理点击了【取消预约】按钮!";
			paramMap.put("user", opt_ren);
			paramMap.put("userAccount", userAccount);
			BookService.addUserAccount(paramMap);			
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		//return "redirect:/booking/queryBookList.do";
	}
	
	
	/**
	 * 切换无视截止时间
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateSwitch_ignore.do")
	public String updateSwitch_ignore(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		String opt_person = loginUserVo.getReal_name();
		String order_id=this.getParam(request, "order_id");
		String refund_deadline_ignore = this.getParam(request, "refund_deadline_ignore");
		String userAccount;
		if(refund_deadline_ignore.equals("1")){
			userAccount = opt_person+"点击了关闭无视退款时间";
		}else{
			userAccount = opt_person+"点击了开启无视退款时间";
		}
		Map<String,String> log_Map = new HashMap<String,String>();
		log_Map.put("order_id", order_id);
		log_Map.put("user", opt_person);
		log_Map.put("userAccount", userAccount);
		BookService.addUserAccount(log_Map);
		Map<String,String>map = new HashMap<String,String>();
		map.put("order_id", order_id);
		map.put("opt_ren", opt_person);
		map.put("refund_deadline_ignore", refund_deadline_ignore);
		
		BookService.updateSwitch_ignore(map);
		return "redirect:/booking/queryBookInfo.do?order_id="+order_id;
	}
	/**
	 *拒绝退款流程
	 *@param request
	 *@param response
	 *@return
	 */
	@RequestMapping("/updateRefuseRefund.do")
	public String updaterefuseRefund(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

		//获取当前登录人
		String user = loginUserVo.getReal_name();
		String order_id = this.getParam(request, "order_id");
		//创建一个Map保存order_id和登录人
		Map<String,String>userMap=new HashMap<String,String>();
		userMap.put("opt_ren", user);
		userMap.put("order_id", order_id);
		
		String refund_memo = this.getParam(request, "refund_memo");
		BookService.updateRefuseRefund(order_id,userMap);
		Map<String,String>map = new HashMap<String,String>();
		map.put("order_id", order_id);
		map.put("refund_memo", refund_memo);
		BookService.updateRefund_memo(map);
		String userAccount = user+"点击了拒绝退票按钮";
		Map<String,String>paramMap = new HashMap<String,String>();
		paramMap.put("order_id", order_id);
		paramMap.put("user", user);
		paramMap.put("userAccount", userAccount);
		BookService.addUserAccount(paramMap);
		return "redirect:/booking/queryBookList.do";
	}
	
	/**
	 *差额退款流程 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateDifferRefunding.do")
	public String updateDifferRefunding(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");

			String order_id = this.getParam(request, "order_id");
			String refund_money = this.getParam(request, "refund_money");
			String user = loginUserVo.getReal_name();
			String userAccount = user+"点击了差额退款按钮";
			Map<String,String> map = new HashMap<String,String>();
			map.put("order_id", order_id);
			map.put("refund_money", refund_money);
			map.put("user", user);
			map.put("userAccount", userAccount);
			
			BookService.updateDifferRefunding(map);
		return "redirect:/booking/queryBookList.do";
	}
	@RequestMapping("/queryGetCity.do")
	@ResponseBody
	public String getCity(String provinceid,HttpServletRequest request,HttpServletResponse response){
	
		
		List<AreaVo> list = BookService.getCity(provinceid);
		
		
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
	
		
		List<AreaVo> list = BookService.getArea(cityid);
		
		
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
	
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		List<Map<String, Object>> history = BookService.queryHistroyByOrderId(order_id);
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
