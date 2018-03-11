package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.l9e.common.BaseController;
import com.l9e.transaction.service.AcquireService;
import com.l9e.transaction.service.CtripService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.transaction.service.TrainSystemSettingService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.CtripVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.DateUtil;
import com.l9e.util.ExcelUtil;
import com.l9e.util.HttpPostRobotUtil;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.JSONUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatRobotUtil;
import com.l9e.util.UrlFormatUtil;


/**
 * 出票管理
 * @author liht
 *
 */
@Controller
@RequestMapping("/acquire")
public class AcquireController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(AcquireController.class);
	@Resource
	private AcquireService AcquireService;

	@Resource
	private ExtRefundService extRefundService;
	
	@Resource
	private TrainSystemSettingService trainSystemSettingService;
	
	@Resource
	private Tj_OpterService tj_OpterService;
	
	@Resource
	private CtripService ctripService;

	private String currentCodingInfo;
	@Value("#{propertiesReader[currentCodingInfo]}")
	public void setCurrentCodingInfo(String currentCodingInfo) {
		this.currentCodingInfo = currentCodingInfo;
	}
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAcquirePage.do")
	public String queryAcquirePage(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入查询页面】queryAcquirePage.do");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
		Calendar theCa = Calendar.getInstance(); 

		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, -3); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String querydate=df.format(date);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
			if(user_level.equals("0")){
				request.getSession().setAttribute("order_status", this.getParamToList(request, "order_status"));
				//request.getSession().setAttribute("channel", this.getParam(request, "channel"));
				return "redirect:/acquire/queryAcquireList.do?order_status=44&order_status=61&order_status=82&order_status=77" +
						"&order_status=83&order_status=85&begin_info_time="+querydate;
			}else{
				return "redirect:/acquire/queryAcquireList.do?order_status=22&order_status=44&order_status=77&order_status=82" +
						"&order_status=83&order_status=61&order_status=85&order_status=AA&begin_info_time="+querydate+"&create_time_px=up";
			}
		}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAcquireList.do")
	public String queryAcquireList(HttpServletRequest request,HttpServletResponse response){
		logger.info("【查询列表】queryAcquireList.do");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
//		String user_level = loginUserVo.getUser_level();
		
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String now = df.format(date);//今天时间
		String user_name = this.getParam(request, "user_name");
		String opt_ren = this.getParam(request, "opt_ren");
		
		List<Map<String,Object>> returnlogList = trainSystemSettingService.querytrain_return_optlog();//查询返回日志的id及简称
		Map<String, Object> returnlogMap = new HashMap<String, Object>();
		for(int i = 0 ; i < returnlogList.size() ; i++){
			returnlogMap.put((String) returnlogList.get(i).get("return_id"), returnlogList.get(i).get("return_value"));
		}
		request.setAttribute("returnlogMap", returnlogMap);
		
		
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		request.setAttribute("now", now);
//		String over_time = this.getParam(request, "over_time");
//		if(over_time.equals("1")){
//			String begin_info_time=this.getParam(request, "begin_info_time");
//			String end_info_time=this.getParam(request, "end_info_time");
//			List<String> statusList = this.getParamToList(request, "orderStatus");
//			List<String> channelList = this.getParamToList(request, "channel");
//			List<String> pay_typeList = this.getParamToList(request, "pay_type");
//			if(statusList==null || statusList.equals("") ||statusList.isEmpty()){
//				statusList = this.getParamToList(request, "order_status");
//			}
//			
//			List<String> channel = new ArrayList<String>(channelList);
//			if(channel.contains("30101612")){
//				channel.add("301016");
//				channel.add("30101601");
//				channel.add("30101602");
//			}
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			paramMap.put("order_status", statusList);
//			paramMap.put("pay_type", pay_typeList);
//			paramMap.put("begin_info_time", begin_info_time);
//			paramMap.put("end_info_time", end_info_time);
//			paramMap.put("channel", channel);
//			
//			int totalCount = AcquireService.queryOvertimeListCount(paramMap);//总条数	
//			//分页
//			PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
//			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
//			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
//			
//			List<Map<String, String>> OvertimeList = AcquireService.queryAcquireOvertimeList(paramMap);
//
//			request.setAttribute("acquireList", OvertimeList);
//			request.setAttribute("isShowList", 1);
//			request.setAttribute("acquireStatus", AcquireVo.getAcquireStatus());
//			request.setAttribute("acquirePayType", AcquireVo.getAcquirePayType());
//			request.setAttribute("channel_types", AccountVo.getChannels());
//
//			request.setAttribute("begin_info_time", begin_info_time);
//			request.setAttribute("end_info_time", end_info_time);
//			request.setAttribute("statusStr", statusList.toString());
//			request.setAttribute("pay_typeStr", pay_typeList.toString());
//			request.setAttribute("over_time", "1");
//			request.setAttribute("channelStr", channel.toString());
//		}else{
			String begin_info_time=this.getParam(request, "begin_info_time");
			if(StringUtils.isEmpty(begin_info_time)){
				Calendar theCa2 = Calendar.getInstance(); 
				theCa2.setTime(new Date());  
				theCa2.add(theCa2.DATE, -1); 
				Date date2 = theCa2.getTime();
				DateFormat dff2 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
				begin_info_time=dff2.format(date2);
			}
			
			
			String end_info_time=this.getParam(request, "end_info_time");
			String manual_order2=this.getParam(request, "manual_order2");
			String pro_bak2 = this.getParam(request, "pro_bak2");
			List<String> statusList = this.getParamToList(request, "order_status");
			List<String> channelList = this.getParamToList(request, "channel");
			List<String> channel = new ArrayList<String>(channelList);
			if(channel.contains("30101612")){
				channel.add("301016");
				channel.add("30101601");
				channel.add("30101602");
			}
			
			String level = this.getParam(request, "level");	
		//	String pay_type =this.getParam(request, "pay_type");
			List<String> pay_typeList = this.getParamToList(request, "pay_type");
			//获得系统当前时间
			//String now = DateUtil.nowDate();
			String order_id = this.getParam(request, "order_id");
			String out_ticket_billno = this.getParam(request, "out_ticket_billno");
			//查询参数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			if(order_id.trim().length()>0){
				paramMap.put("order_id", order_id);
			}else{
				paramMap.put("opt_ren", opt_ren);
				paramMap.put("user_name", user_name);
				paramMap.put("out_ticket_billno", out_ticket_billno);
				if(channel.size()>1){
					paramMap.put("channel", channel);
				}else if(channel.size()==1){
					paramMap.put("channel_only", channel.get(0));
				}
				if(statusList.size()>1){
					paramMap.put("order_status", statusList);
				}else if(statusList.size()==1){
					paramMap.put("status_only", statusList.get(0));
				}
				paramMap.put("level", level);
				paramMap.put("pay_type", pay_typeList);
				paramMap.put("begin_info_time", begin_info_time);
				paramMap.put("end_info_time", end_info_time);
				paramMap.put("pro_bak2", pro_bak2);
				paramMap.put("manual_order2",manual_order2);
			}
			
//			if(user_level.equals("0")){
//			//	channelList = new ArrayList<String>();
//			//	channelList.add((String)request.getSession().getAttribute("channel"));
//			//	paramMap.put("channel", channelList);
//				List<String> status_List = new ArrayList<String>();
//					status_List.add("44");
//					status_List.add("61");
//					status_List.add("82");
//					status_List.add("77");
//					status_List.add("83");
//					status_List.add("85");
//				paramMap.put("order_status", status_List);
//			}
			
			//按乘车时间排序
			String travel_time_px=this.getParam(request, "travel_time_px");
			if("up".equals(travel_time_px))paramMap.put("travel_time_asc",travel_time_px);
			else if("down".equals(travel_time_px))paramMap.put("travel_time_desc",travel_time_px);
			//按创建时间排序
			String create_time_px=this.getParam(request, "create_time_px");
			if("up".equals(create_time_px))paramMap.put("create_time_asc",create_time_px);
			else if("down".equals(create_time_px))paramMap.put("create_time_desc",create_time_px);
			//按发车时间排序
			String out_ticket_time_px=this.getParam(request, "out_ticket_time_px");
			if("up".equals(out_ticket_time_px))paramMap.put("out_ticket_time_asc",out_ticket_time_px);
			else if("down".equals(out_ticket_time_px))paramMap.put("out_ticket_time_desc",out_ticket_time_px);
			
			if("".equals(travel_time_px) && "".equals(create_time_px) && "".equals(out_ticket_time_px)){
				paramMap.put("create_time_asc","up");
			}
			List<Map<String, String>> acquireList=new ArrayList<Map<String,String>>();
			paramMap.put("manual_order", "00");
			if(user_name.isEmpty() || "".equals(user_name)){
				int totalCount = AcquireService.queryAcquireListCount(paramMap);//总条数	
				//分页
				PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
				paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
				paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
				
				acquireList = AcquireService.queryAcquireList(paramMap);
			}else{
				int totalCount = AcquireService.queryAcquireListCountCp(paramMap);//总条数	
				//分页
				PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
				paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
				paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
				
				acquireList = AcquireService.queryAcquireListCp(paramMap);
			}
			String key = this.getParam(request, "key");
			MemcachedUtil.getInstance().removeAttribute(key);

			if(order_id.trim().length()>0){
				request.setAttribute("order_id", order_id);
			}else{
				request.setAttribute("opt_ren", opt_ren);
				request.setAttribute("user_name", user_name);
				request.setAttribute("out_ticket_billno", out_ticket_billno);
				request.setAttribute("channelStr", channel.toString());
				request.setAttribute("statusStr", statusList.toString());
				request.setAttribute("level", level);
				request.setAttribute("pay_typeStr", pay_typeList.toString());
				request.setAttribute("begin_info_time", begin_info_time);
				request.setAttribute("end_info_time", end_info_time);
				request.setAttribute("pro_bak2", pro_bak2);
				request.setAttribute("manual_order2", manual_order2);
			}

			if("".equals(travel_time_px) && "".equals(create_time_px) && "".equals(out_ticket_time_px)){
				request.setAttribute("create_time_px", "up");
			}else{
				request.setAttribute("travel_time_px", travel_time_px);
				request.setAttribute("create_time_px", create_time_px);
				request.setAttribute("out_ticket_time_px", out_ticket_time_px);
			}
			
			request.setAttribute("channel_types", merchantMap);
			request.setAttribute("seat_Types", AcquireVo.getSEAT_TYPES());
			request.setAttribute("acquireStatus", AcquireVo.getAcquireStatus());
			request.setAttribute("acquirePayType", AcquireVo.getAcquirePayType());
			request.setAttribute("workertype", AcquireVo.getWorkerType());
			request.setAttribute("acquireList", acquireList);
			request.setAttribute("isShowList", 1);
			request.setAttribute("manualOrder", CtripVo.getManualOrder());
//		}
		
		return "acquire/acquireList";
	}
	
	/**
	 * 大于十分钟订单
	 */
	@RequestMapping("queryAcquireOvertimeList.do")
	public String queryAcquireOvertimeList(HttpServletRequest request,HttpServletResponse response,String order_status){
		logger.info("【查询列表】queryAcquireOvertimeList.do");
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String now = df.format(date);//今天时间
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		List<String> statusList = new ArrayList<String>();
		statusList.add(order_status);
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_status", statusList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		
		int totalCount = AcquireService.queryOvertimeListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, String>> OvertimeList = AcquireService.queryAcquireOvertimeList(paramMap);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = new HashMap<String, String>();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("now", now);
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		request.setAttribute("acquireList", OvertimeList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("acquireStatus", AcquireVo.getAcquireStatus());
		request.setAttribute("channel_types", AccountVo.getChannels());
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("statusStr", statusList.toString());
		return "acquire/acquireList";
	}
	
	/**
	 * 查看系统设置的当前打码方式
	 * rand_code_type：打码方式：0、手动打码 1、机器识别
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCurrentCodeType.do")
	public void queryCurrentCodeType(HttpServletRequest request,HttpServletResponse response){
		String rand_code_type = AcquireService.queryCurrentCodeType();
		try {
			response.getWriter().write(rand_code_type);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 接收当前打码返回的数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCurrentCode.do")
	public void queryCurrentCode(HttpServletRequest request,HttpServletResponse response){
		String acquire = null;
			Map<String, String> paramMap1  = new HashMap<String, String>();
			String params=null;
			try {
				params = UrlFormatUtil.CreateUrl("", paramMap1, "", "UTF-8");
				//acquire = HttpUtil.sendByPost(currentCodingInfo, params, "UTF-8");
				//acquire = PostRequestUtil.getPostRes("UTF-8", params, currentCodingInfo);
				acquire = HttpPostUtil.sendAndRecive(currentCodingInfo,params);
				logger.info("当前打码接口数据："+acquire);
			} catch (Exception e) {
				e.printStackTrace();
			}
		//解析JSON
//			JSONObject jsonObject = JSONObject.fromObject(acquire); 
//	        Map map = new HashMap(); 
//	        for(Iterator iter = jsonObject.keys(); iter.hasNext();){ 
//	            String key1 = (String)iter.next(); 
//	            map.put(key1, jsonObject.get(key1)); 
//	        } 
//	        Map<String,Object> resultMap= map;
//		Map<String,Object> resultMap= JSONUtil.getMapFromJson(acquire);
	       // request.setAttribute("resultMap", resultMap);
	        request.setAttribute("acquire", acquire);
			System.out.println(acquire);
			response.setCharacterEncoding("utf-8");
			try {
				response.getWriter().write(acquire);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 机器人扫描12306未完成订单页面，若订单存在则发送取消订单请求，状态为出票失败，原因无票，发通知给销售平台
	 * 若无此订单，返回后台，订单设为出票失败，无票，发通知
	 * http://localhost:8099/RunScript?ScriptPath=cancelordernew.lua&SessionID=1&Timeout=1200000&ParamCount=1&Param1=traintest001|123456a|HC1312201753232888|E315160797|0
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRobotCancleOrder.do")
	//TODO
	public void queryRobotCancleOrder(AcquireVo acquire,HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		String acc_username = this.getParam(request, "acc_username");//登陆账号
		String acc_password = this.getParam(request, "acc_password");//登录密码
		String order_id = this.getParam(request, "order_id");//订单号
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");//12306订单号
		String channel = this.getParam(request, "channel");//来源渠道
		String status = this.getParam(request, "status");//状态
		String error_info = this.getParam(request, "error_info");
//		String acc_username = "traintest001";//登陆账号
//		String acc_password = "123456a";//登录密码
//		String order_id = "sjysz1402190943507e7";//订单号
//		String out_ticket_billno = "E380108199";//12306订单号
//		String channel = "19e";//来源渠道
		
		//打码方式：0、手动打码 1、机器识别
		String codeType = AcquireService.queryCodeType();
		//生成时间戳(sessionId)
		String sessionId = DateUtil.dateToString(new Date(),DateUtil.DATE_FMT2);
		//随机选择一个预定机器人来执行操作
		String robotRandom = AcquireService.queryRobotRandom();
		
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("Param1", acc_username+"|"+acc_password+"|"+order_id+"|"+out_ticket_billno+"|"+codeType);
			paramMap.put("ParamCount", "1");
			paramMap.put("Timeout", "1200000");
			paramMap.put("SessionID", sessionId);
			paramMap.put("ScriptPath", "cancelorder.lua");
			logger.info("【取消订单1】机器人接口参数："+paramMap.toString());
		String result = "";
		String params="";
		String retValue = "";
			try {
				params = UrlFormatRobotUtil.CreateUrl("", paramMap, "", "UTF-8");
				result = HttpPostRobotUtil.sendAndRecive(robotRandom, params);
				
			} catch (Exception e) {
				logger.info("【取消订单1】机器人接口返回数据："+result);
					try {
						if(robotRandom.equals(null)||robotRandom.equals("")||params.equals("")||result.equals("")||result.equals(null)){
							robotRandom = AcquireService.queryRobotRandom();
							paramMap.put("Param1", acc_username+"|"+acc_password+"|"+order_id+"|"+out_ticket_billno+"|"+codeType);
							logger.info("【取消订单2】机器人接口参数："+paramMap.toString());
							params = UrlFormatRobotUtil.CreateUrl("", paramMap, "", "UTF-8");
							result = HttpPostRobotUtil.sendAndRecive(robotRandom, params);
						}
					} catch (Exception e1) {
						logger.info("【取消订单2】机器人接口返回数据："+result);
						try {
							if(robotRandom.equals(null)||robotRandom.equals("")||params.equals("")||result.equals("")||result.equals(null)){
								robotRandom = AcquireService.queryRobotRandom();
								paramMap.put("Param1", acc_username+"|"+acc_password+"|"+order_id+"|"+out_ticket_billno+"|"+codeType);
								logger.info("【取消订单3】机器人接口参数："+paramMap.toString());
								params = UrlFormatRobotUtil.CreateUrl("", paramMap, "", "UTF-8");
								result = HttpPostRobotUtil.sendAndRecive(robotRandom, params);
							}
						} catch (Exception e2) {
							logger.info("【取消订单3】机器人接口返回数据："+result);
						}
				}
				retValue = "failure";
				result = "error";
				acquire.setOrder_status("77");//设置成取消失败
				AcquireService.updateAcquire(acquire, null);
			}
		
		if(result.equals("error")||result.equals("")){
			retValue = "failure";
			logger.info("【取消订单】机器人接口连接出错");
		}else{
			//解析JSON
			Map<String,Object> resultMap = JSONUtil.getMapFromJson(result);
			Map<String,Object> errorInfo = null;
			
			if(resultMap.get("ErrorCode").equals(0)){
				String ErrorInfo = resultMap.get("ErrorInfo").toString();
				//String errorInfo11 = ErrorInfo.substring(1, ErrorInfo.length()-1);
				String errorInfo11 = ErrorInfo.substring(4, ErrorInfo.length()-4);
				while (errorInfo11.contains("\\\""))             
					errorInfo11 = errorInfo11.replace("\\\"", "\""); 
				//retInfo:返回信息，如果retValue为success，则retInfo为空
				//orderInfo:订单号
				//retValue:取消订单状态，分别为success表示取消订单成功， failure表示取消订单失败， noorder表示没有在账号里查到该订单
				//outTicketBillno:12306订单号
				errorInfo = JSONUtil.getMapFromJson(errorInfo11);//{retInfo=, outTicketBillno=E327917809, retValue=success, orderId=HC1312201753232888}
				//success表示取消订单成功， failure表示取消订单失败， noorder表示没有在账号里查到该订单
				retValue = errorInfo.get("retValue").toString();
				if(retValue.equals("success")){
					logger.info("【取消订单】成功");
				}else if(retValue.equals("failure")){
					logger.info("【取消订单】失败");
				}else{
					logger.info("【取消订单】没有在账号里查到该订单:"+out_ticket_billno);
					retValue = "success";
				}
			}else{
				//ErrorCode为机器人平台返回异常代码
				//{"ErrorCode":5,"ErrorInfo":"请求脚本不存在。"}
				logger.info("【取消订单】机器人接口发生异常，ErrorInfo："+resultMap.get("ErrorInfo"));
			}
		}
		
		logger.info("【取消订单】机器人接口返回数据："+result);
		AccountVo account = new AccountVo();
		account.setAcc_username(acquire.getAcc_username());
		account.setOpt_person(user);
		account.setOrder_id(order_id);
		String db_order_status = AcquireService.queryDbOrder_status(order_id);//cp_orderinfo表中的order_status（82：人工查询）
		String order_time = this.getParam(request, "create_time");
		//String error_info = this.getParam(request, "error_info");
		if(retValue.equals("failure")){
			System.out.println("【取消订单】失败!");
		}else{
			account.setOpt_logs(user+"点击了取消订单");
			acquire.setOrder_status("10");//设置成出票失败
			if(db_order_status.equals("61")){
				acquire.setError_info("1");//失败原因为：无票
			}else{
				acquire.setError_info(error_info);
			}
			AcquireService.updateAcquire(acquire,account);
			logger.info("【更新订单信息】》》》【取消订单】成功！order_id:"+order_id);
			String userAccount = user+"点击了取消订单按钮!";
			Map<String,String>paramMap1 = new HashMap<String,String>();
			paramMap1.put("order_id", order_id);
			paramMap1.put("user", user);
			paramMap1.put("userAccount", userAccount);
			paramMap1.put("order_time", order_time);
			//添加操作日志
			AcquireService.addUserAccount(paramMap1);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", user);
			optMap.put("all", "out_ticket");
			tj_OpterService.operate(optMap);
			logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(retValue);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 人工查询列表
	 * @param request
	 * @param response
	 * @param order_status
	 * @return
	 */
	@RequestMapping("queryAcquireRengongList.do")
	public String queryAcquireRengongList(HttpServletRequest request,HttpServletResponse response){
		logger.info("【查询列表】queryAcquireRengongList.do");
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String now = df.format(date);//今天时间
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> statusList = this.getParamToList(request, "order_status");
		List<String> channelList = this.getParamToList(request, "channel");
		List<String> channel = new ArrayList<String>(channelList);
		if(channel.contains("30101612")){
			channel.add("301016");
			channel.add("30101601");
			channel.add("30101602");
		}
		String level = this.getParam(request, "level");
		String pay_type = this.getParam(request, "pay_type");
		String order_id = this.getParam(request, "order_id");
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("channel", channel);
		paramMap.put("order_status", statusList);
		paramMap.put("level", level);
		paramMap.put("pay_type", pay_type);
		
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		if(user_level.equals("0")){
			channel = new ArrayList<String>();
			channel.add((String)request.getSession().getAttribute("channel"));
			paramMap.put("channel", channel);
			statusList = new ArrayList<String>();
			statusList.add((String)request.getSession().getAttribute("order_status"));
			paramMap.put("order_status", statusList);
		}
		int totalCount = AcquireService.queryAcquireListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, String>> acquireList = AcquireService.queryAcquireList(paramMap);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = new HashMap<String, String>();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		String key = this.getParam(request, "key");
		MemcachedUtil.getInstance().removeAttribute(key);
		request.setAttribute("now", now);
		request.setAttribute("order_id", order_id);
		request.setAttribute("channel_types", AccountVo.getChannels());
		request.setAttribute("seat_Types", AcquireVo.getSEAT_TYPES());
		request.setAttribute("channelStr", channel.toString());
		request.setAttribute("statusStr", statusList.toString());
		request.setAttribute("acquireStatus", AcquireVo.getAcquireStatus());
		request.setAttribute("level", level);
		request.setAttribute("pay_type", pay_type);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("workertype", AcquireVo.getWorkerType());
		request.setAttribute("acquireList", acquireList);
		request.setAttribute("isShowList", 1);
		return "acquire/acquireList";
	}
	

	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAcquireInfo.do")
	public String queryAcquireInfo(HttpServletRequest request,
			HttpServletResponse response){
		String canOperation = this.getParam(request, "canOperation");
		String order_id = this.getParam(request,"order_id");
		String budan = this.getParam(request,"budan");
		String channel = this.getParam(request, "channel");//获取渠道
		logger.info("【查询明细】queryAcquireInfo.do 【订单号："+order_id+"】");
		Map<String, String> orderInfo = AcquireService.queryAcquireOrderInfo(order_id);
		logger.info("查询明细获取orderId"+orderInfo.get("order_id"));
		List<Map<String, Object>> cpList = AcquireService.queryAcquireOrderInfoCp(order_id);
		
		List<Map<String, Object>> history = AcquireService.queryHistroyByOrderId(order_id);
		String query_type = this.getParam(request, "query_type");
		
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
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		Map<String, String> errorMap = AccountVo.getErrorInfoChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
			errorMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		
		if("22".equals(orderInfo.get("manual_order"))){
			Map<String,String>map = new HashMap<String,String>();
			map.put("order_id", order_id);
			HashMap<String, String> ctripInfo = ctripService.queryCtripAccount(map);
			request.setAttribute("ctripInfo", ctripInfo);
		}
		
		String chooseSeats=orderInfo.get("choose_seats");
		
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		request.setAttribute("cpIndoSb", sb);
		request.setAttribute("chooseSeats", chooseSeats);
		request.setAttribute("query_type", query_type);
		request.setAttribute("seat_type_qunar", AcquireVo.getSEAT_TYPES());
		request.setAttribute("channel_types", merchantMap);
		request.setAttribute("channel_type", errorMap);
		request.setAttribute("seatList", seatList);
		request.setAttribute("history", history);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("canOperation", canOperation);
		request.setAttribute("acquireStatus", AcquireVo.getAcquireStatus());
		request.setAttribute("idstype", BookVo.getIdstype());
		request.setAttribute("tickettype", BookVo.getTicketType());
		request.setAttribute("seattype", BookVo.getSeattype());
		request.setAttribute("bank_type", AcquireVo.getBank());
		request.setAttribute("channel", channel);
		request.setAttribute("cpList", cpList);
		request.setAttribute("budan", budan);
		
		return "acquire/acquireInfo";
	}
	
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		List<Map<String, Object>> history = AcquireService.queryHistroyByOrderId(order_id);
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
	
	/**
	 *切换账号 
	 */
	@RequestMapping("/updateAccount.do")
	@ResponseBody
	public void updateAccount(HttpServletResponse response,HttpServletRequest request){
		String result = "yes";
		try{
			String order_id = this.getParam(request, "order_id");
			String acc_id=this.getParam(request, "account_id");
			String channel =this.getParam(request, "channel");   
			String acc_username = this.getParam(request, "acc_username");
			String acc_password = this.getParam(request, "acc_password");
			String order_time = this.getParam(request, "create_time");
			String stop_reason = this.getParam(request, "stop_reason");
			
			logger.info("【切换账号】queryAcquireInfo.do 【订单号："+order_id+"】切换前账号为：【"+acc_username+"|"+acc_password+"】");
			//找到当前登录人
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String opt_user = loginUserVo.getReal_name();
			String opt_userAccount = opt_user+"点击了切换账号按钮,切换前前账号为："+acc_username+"|"+acc_password;
			//查询出空闲的优先级最高的账号
			String account_channel = "19e";
			logger.info("获取的账号渠道"+account_channel);
			String acc = AcquireService.queryAccount(account_channel);
			if(!StringUtil.isNotEmpty(acc)){
				result="no";
			}else{
				//取出之前的
				//切换账号
				AccountVo account = new AccountVo();
				account.setAcc_username(acc_username);
				account.setOpt_logs(opt_user+"点击了切换账号");
				account.setOpt_person(opt_user);
				account.setOrder_id(order_id);
				Map<String,String>map = new HashMap<String,String>();
				map.put("acc_id", acc);
				map.put("order_id", order_id);
				map.put("channel",channel);
				map.put("old_acc_id", acc_id);
				map.put("opt_person", opt_user);
				map.put("stop_reason", stop_reason);
				map.put("acc_username", acc_username);
				map.put("isStopAccount", this.getParam(request, "stopAccount"));
				AcquireService.updateAccount(map,account);
				//添加日志
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", opt_user);
				paramMap.put("userAccount", opt_userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				//客服操作记录
				Map<String, Object> optMap = new HashMap<String, Object>();
				optMap.put("userName", opt_user);
				optMap.put("all", "out_ticket");
				tj_OpterService.operate(optMap);
				//return "redirect:/acquire/queryAcquireInfo.do?order_id="+order_id+"&query_type=1";
			}
		}catch(Exception e){
			e.printStackTrace();
			result="no";
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 更新订单信息
	 * @param acquire
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateAcquireInfo.do")
	public void updateAcquireInfo(AcquireVo acquire, HttpServletRequest request,HttpServletResponse response){
		logger.info("【更新订单信息】updateAcquireInfo order_id:"+acquire.getOrder_id());
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		String order_id = this.getParam(request, "order_id");
		String budan = this.getParam(request, "budan");
		String order_status = this.getParam(request, "order_status");
		String error_info = this.getParam(request, "error_info");
		String order_time = this.getParam(request, "create_time");
		String channel = this.getParam(request, "channel");//获取当前的渠道
		String buy_money = acquire.getBuy_money();
		String account_from_way = this.getParam(request, "account_from_way");//账号来源 0：自有账号 1渠道账号
		acquire.setOpt_ren(user);
		
		AccountVo account = new AccountVo();
		account.setAcc_username(acquire.getAcc_username());
		account.setOpt_person(user);
		account.setOrder_id(order_id);
		//创建一个Map用来保存登录人的信息与订单号，然后每一个修改订单状态的方法中都添加本Map集合
		//然后在ServiceImpl方法中写一个Dao方法并使用本Map来修改
		//查询出数据库中的订单状态
		if(!StringUtils.isEmpty(order_id)){
			String db_order_status = AcquireService.queryDbOrder_status(order_id);//cp_orderinfo表中的order_status（82：人工查询）
			if(order_status!=null && db_order_status.equals("82") && order_status.equals("10")){    //1人工查询，出票失败
				acquire.setError_info(error_info);
				account.setOpt_logs(user+"点击了出票失败");
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【人工查询】》》》【出票失败】成功！order_id:"+order_id);
				String userAccount = user+"点击了出票失败按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//paramMap.put("channel", channel);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("82") && order_status.equals("01")){//2人工查询，重新出票
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【人工查询】》》》【重新出票】成功！order_id:"+order_id);
				String userAccount = user+"点击了重新出票按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("66") && order_status.equals("61")){//3正在支付，人工支付
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【正在支付】》》》【人工支付】成功！order_id:"+order_id);
				String userAccount = user+"点击了人工支付按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("66") && order_status.equals("55")){//4正在支付，开始支付
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【正在支付】》》》【重新支付】成功！order_id:"+order_id);
				String userAccount = user+"点击了重新支付按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null  && order_status.equals("83")){ //5正在取消
				account.setOpt_logs(user+"点击了取消订单");
//				acquire.setOrder_status("10");//设置成出票失败
//				if(db_order_status.equals("61")){
//					acquire.setError_info("1");//失败原因为：无票
//				}
				AcquireService.updateAcquire(acquire,account);
				String userAccount = user+"点击了取消订单按钮!";
				Map<String,String>paramMap1 = new HashMap<String,String>();
				paramMap1.put("order_id", order_id);
				paramMap1.put("user", user);
				paramMap1.put("userAccount", userAccount);
				paramMap1.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap1);
				logger.info("【更新订单信息】》》》【取消订单】更改状态成功！order_id:"+order_id);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null  && order_status.equals("85")){ //6开始取消
				account.setOpt_logs(user+"点击了取消订单");
				if(db_order_status.equals("61")){
					acquire.setError_info("1");//失败原因为：无票
					if(channel.equals("tuniu")&& account_from_way.equals("1")){
						acquire.setError_info("28");//失败原因为：不存在未支付订单或该订单状态不可支付
					}
				}
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】》》》【取消订单】更改状态'开始取消'成功！order_id:"+order_id);
				String userAccount = user+"点击了取消订单按钮!";
				Map<String,String>paramMap1 = new HashMap<String,String>();
				paramMap1.put("order_id", order_id);
				paramMap1.put("user", user);
				paramMap1.put("userAccount", userAccount);
				paramMap1.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap1);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("61") && order_status.equals("88")){//7人工支付，支付成功
				String buy_money_total = this.getParam(request, "buy_money_total");
				acquire.setBuy_money(buy_money_total);
				acquire.setPay_type("1");
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【人工支付】》》》【支付完成】成功！order_id:"+order_id);
				String userAccount = user+"点击了支付完成按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("61") && order_status.equals("55")){//8人工支付，开始支付
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【人工支付】》》》【重新支付】成功！order_id:"+order_id);
				String userAccount = user+"点击了重新支付按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("61") && order_status.equals("01")){//9人工支付，预订重发
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【人工支付】》》》【重新预订】成功！order_id:"+order_id);
				String userAccount = user+"点击了重新预订按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("01") && order_status.equals("11")){//10重发出票，正在预定
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【正在预定】》》》【重新预订】成功！order_id:"+order_id);
				String userAccount = user+"点击了重新预订按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && (db_order_status.equals("44")||db_order_status.equals("AA") ) && order_status.equals("61")){//11人工预订，人工支付
				String buy_money_total = this.getParam(request, "buy_money_total");
				String out_ticket_billno =this.getParam(request, "out_ticket_billno");
				List<String> cp_id_list = this.getParamToList(request, "cp_id");
				List<String> train_box_list= this.getParamToList(request, "train_box");
				List<String> seat_no_list= this.getParamToList(request, "seat_no");
				List<String> buy_money_list = this.getParamToList(request, "per_buy_money");
				
				logger.info("【人工预订】开始!buy_money_total="+buy_money_total+
						",out_ticket_billno="+out_ticket_billno+
						",cp_id_list="+cp_id_list.toString()+
						",train_box_list="+train_box_list.toString()+
						",seat_no_list="+seat_no_list.toString()+
						",buy_money_list"+buy_money_list.toString());
				
				List<Map<String, String>> cpList = new ArrayList<Map<String, String>>();
				Map<String,String>update_Map = new HashMap<String,String>();
				update_Map.put("order_id", order_id);
				update_Map.put("buy_money", buy_money_total);
				update_Map.put("out_ticket_billno", out_ticket_billno);
				update_Map.put("user", user);
				for(int i=0;i<cp_id_list.size();i++){
					Map<String, String> temp = new HashMap<String, String>();
					temp.put("cp_id", cp_id_list.get(i));
					temp.put("order_id", order_id);
					temp.put("train_box", train_box_list.get(i));
					temp.put("seat_no", seat_no_list.get(i));
					temp.put("buy_money", buy_money_list.get(i));
					cpList.add(temp);
				}
				
				String is_pay=AcquireService.queryOrderIsPay(order_id);
				if("11".equals(is_pay)){
					AcquireService.updateOrderInfoFor45(update_Map);
					logger.info("【人工预订状态修改主表信息】【修改为等待支付成功】");
				}
				else{
					AcquireService.updateOrderInfoFor61(update_Map);
					logger.info("【人工预订状态修改主表信息】【修改为人工支付成功】");
				}
				AcquireService.updateCpListFor61(cpList);
				logger.info("【修改子表中车厢，座位号与价钱成功】");
				AcquireService.updateNotify(order_id);
				logger.info("【修改通知】");
				String userAccount = user+"点击了预订成功按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && (db_order_status.equals("44")||db_order_status.equals("AA") ) && order_status.equals("10")){//12人工预订，出票失败
				acquire.setError_info(error_info);
				account.setOpt_logs(user+"点击了出票失败");

				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【人工预定】》》》【出票失败】成功！order_id:"+order_id);
				String userAccount = user+"点击了出票失败按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("77") && order_status.equals("10")){//13取消失败，出票失败
				acquire.setError_info(error_info);
				account.setOpt_logs(user+"点击了出票失败");

				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【取消失败】》》》【出票失败】成功！order_id:"+order_id);
				String userAccount = user+"点击了【取消失败】出票失败按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
//			}else if(order_status!=null && db_order_status.equals("44") && order_status.equals("01")){
//				AcquireService.updateAcquire(acquire);
//				logger.info("【"+user+"】点击了机器人处理！order_id:"+order_id);
//				String userAccount = user+"点击了机器人处理按钮!";
//				Map<String,String>paramMap = new HashMap<String,String>();
//				paramMap.put("order_id", order_id);
//				paramMap.put("user", user);
//				paramMap.put("userAccount", userAccount);
//				paramMap.put("order_time", order_time);
//				//添加操作日志
//				AcquireService.addUserAccount(paramMap);
//				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else{
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【操作】成功！order_id:"+order_id);
				String userAccount = user+"点击了【操作】按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}
			Map<String,String>budanMap = new HashMap<String,String>();
			budanMap.put("order_id", order_id);
			if("1".equals(budan))
				AcquireService.updateStatus00To11(budanMap);
		}else{
			logger.info("order_id为空");
		}
		
		//客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", user);
		optMap.put("all", "out_ticket");
		tj_OpterService.operate(optMap);
		//return "redirect:/acquire/queryAcquireList.do?order_status=00&order_status=01&order_status=11&order_status=22" +
			//	"&order_status=33&order_status=44&order_status=55&order_status=66&order_status=77&order_status=88" +
				//"&order_status=81&order_status=82&order_status=83&order_status=61";
	
		
	}
	
	@RequestMapping("/updateAcquireToRobot.do")
	@ResponseBody
	public void updateAcquireToRobot(HttpServletResponse response,HttpServletRequest request,String order_id,String order_status,String create_time){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		
		//PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		//int pageIndex = pageObject.getPageIndex();//获得当前页
		String userAccount=user+"点击了机器处理";
		String result = null;
		if(StringUtil.isNotEmpty(order_id)){
			String db_order_status = AcquireService.queryDbOrder_status(order_id);
			String manual_order = this.getParam(request, "manual_order");
			if(order_status!=null && order_status.equals("01")
					&& (db_order_status.equals("44") || db_order_status.equals("AA") || db_order_status.equals("05"))){
				Map<String,String>updateMap = new HashMap<String,String>();
				updateMap.put("order_status", order_status);
				updateMap.put("order_id", order_id);
				updateMap.put("opt_ren", user);
				result=AcquireService.updateAcquireToRobot(updateMap);
				if("22".equals(manual_order)){
					userAccount=user+"点击了携程处理";
				}else if("00".equals(manual_order)){
					userAccount=user+"点击了正常处理";
					AcquireService.updateCtripToRobot(updateMap);
					logger.info("点击了正常处理 manual_order由22修改为00");
				}
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", create_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				//客服操作记录
				Map<String, Object> optMap = new HashMap<String, Object>();
				optMap.put("userName", user);
				optMap.put("all", "out_ticket");
				tj_OpterService.operate(optMap);
				if(db_order_status.equals("AA"))
					AcquireService.updateStatus00To11(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}
		}
		
		
		
			//request.setAttribute("pageIndex", pageIndex);
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.error("机器处理response返回参数时异常");
			e.printStackTrace();
		}
	}
	/**
	 *如果状态为 82人工查询 修改信息
	 *@param acquire
	 *@param request
	 *@param response
	 *@return
	 */
	@RequestMapping("/updateCpDetail.do")
	public void updateCpDetail(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request, "order_id");
		logger.info("出票成功"+order_id);
		String buy_money_total = this.getParam(request, "buy_money_total");
		String out_ticket_account = this.getParam(request, "out_ticket_account");
		String out_ticket_billno =this.getParam(request, "out_ticket_billno");
		String bank_pay_seq = this.getParam(request, "bank_pay_seq");
		String acc_id = this.getParam(request, "account_id");
		String acc_username = this.getParam(request, "acc_username");
		String order_time = this.getParam(request, "create_time");
		List<String> cp_id_list = this.getParamToList(request, "cp_id");
		List<String> train_box_list= this.getParamToList(request, "train_box");
		List<String> seat_no_list= this.getParamToList(request, "seat_no");
		List<String> buy_money_list = this.getParamToList(request, "per_buy_money");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		
		if(!StringUtils.isEmpty(order_id)&& SwitchUtils.isNum(buy_money_total)){
	
			
			logger.info("【更新为出票成功开始】order_id="+order_id+
					",buy_money_total="+buy_money_total+
					",out_ticket_account="+out_ticket_account+
					",out_ticket_billno="+out_ticket_billno+
					",bank_pay_seq="+bank_pay_seq+
					",acc_id="+acc_id+"cp_id_list="+cp_id_list.toString()+
					",train_box_list="+train_box_list.toString()+
					",seat_no_list="+seat_no_list.toString()+
					",buy_money_list="+buy_money_list);
			Map<String, String> map = new HashMap<String, String>();
			map.put("order_id", order_id);
			map.put("buy_money", buy_money_total);
			map.put("out_ticket_account", out_ticket_account);
			map.put("user", user);
			map.put("out_ticket_billno", out_ticket_billno);
			map.put("bank_pay_seq", bank_pay_seq);
			map.put("old_acc_id", acc_id);
			map.put("acc_status", "33");
			map.put("acc_username", acc_username);
			List<Map<String, String>> cpList = new ArrayList<Map<String, String>>();
			for(int i=0;i<cp_id_list.size();i++){
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("cp_id", cp_id_list.get(i));
				temp.put("order_id", order_id);
				temp.put("train_box", train_box_list.get(i));
				temp.put("seat_no", seat_no_list.get(i));
				temp.put("buy_money", buy_money_list.get(i));
				cpList.add(temp);
			}
			AcquireService.updateCpDetail(map, cpList);
			logger.info("【更新订单信息】【人工查询】》》》【出票成功】成功");
			String userAccount = user+"点击了出票成功按钮!";
			Map<String,String>paramMap = new HashMap<String,String>();
			paramMap.put("order_id", order_id);
			paramMap.put("user", user);
			paramMap.put("userAccount", userAccount);
			paramMap.put("order_time", order_time);
			//添加操作日志
			AcquireService.addUserAccount(paramMap);
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", user);
			optMap.put("all", "out_ticket");
			tj_OpterService.operate(optMap);
			logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
		}else{
			logger.info("order_id为:"+order_id+"buy_money_total:"+buy_money_total+"元");
		}
		//return "redirect:/acquire/queryAcquireList.do";
	}
	/**
	 *  在正在预定状态下的明细中添加一个重发出票按钮 点击之后执行这个方法
	 *  本方法作用为：把订单状态为11、正在预定 修改为 01、重发出票 
	 *@param request 
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateInfoOrderStatus.do")
	@ResponseBody
	public void updateInfoOrderStatus(HttpServletRequest request,HttpServletResponse response){
			String result="yes";
		try{
			String order_id = this.getParam(request, "order_id");
			logger.info("【更新订单信息】updateAcquireInfo order_id:"+order_id);
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user =loginUserVo.getReal_name();//获取当前登录的人
			//String order_time = this.getParam(request, "create_time");
			AcquireService.updateInfoOrderStatus(order_id,user);
			logger.info("【更新订单信息】【预定重发】order_id:"+order_id);
			String userAccount = user+"点击了‘预定重发’按钮!";
			Map<String,String>paramMap = new HashMap<String,String>();
			paramMap.put("order_id", order_id);
			paramMap.put("user", user);
			paramMap.put("userAccount", userAccount);
			paramMap.put("order_time", null);
			//添加操作日志
			AcquireService.addUserAccount(paramMap);
			logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			//return "redirect:/acquire/queryAcquireList.do";
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		}catch(Exception e){
			e.printStackTrace();
			result="no";
		}
	}
	/**
	 *  重新支付
	 *  本方法作用为：把订单状态为61、人工支付   修改为 55、重新支付
	 *@param request 
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateInfoOrderStatusTo55.do")
	@ResponseBody
	public void updateInfoOrderStatusTo55(HttpServletRequest request,HttpServletResponse response){
			String result="yes";
		try{
			String order_id = this.getParam(request, "order_id");
			String order_status = this.getParam(request, "order_status");
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user =loginUserVo.getReal_name();//获取当前登录的人
			Map<String, String> param_map = new HashMap<String, String>();
			param_map.put("order_id", order_id);
			param_map.put("user", user);
			param_map.put("order_status", order_status);
			AcquireService.updateInfoOrderStatusTo55(param_map);
			String userAccount = "";
			if(order_status.equals("55")){
				logger.info("【更新订单信息】【重新支付】order_id:"+order_id);
				userAccount = user+"点击了‘重新支付’按钮!";
			}else{
				logger.info("【更新订单信息】【支付完成】order_id:"+order_id);
				userAccount = user+"点击了‘支付完成’按钮!";
			}
			Map<String,String>paramMap = new HashMap<String,String>();
			paramMap.put("order_id", order_id);
			paramMap.put("user", user);
			paramMap.put("userAccount", userAccount);
			paramMap.put("order_time", null);
			//添加操作日志
			AcquireService.addUserAccount(paramMap);
			logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		}catch(Exception e){
			e.printStackTrace();
			result="no";
		}
	}
	/**
	 * 支付锁
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryPayIsLock.do")
	@ResponseBody
	public void queryPayIsLock(HttpServletResponse response ,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		String order_id = this.getParam(request, "order_id");
		if(order_id.contains("_")){//“联程”车票订单锁
			order_id = order_id.substring(0, order_id.length()-2);
		}
		String opt_person = loginUserVo.getReal_name();
		String key = "Lock_" + order_id;
		String value = "Lock_"+order_id+"&"+opt_person;
		String isLock;
		isLock = (String) MemcachedUtil.getInstance().getAttribute(key); //读值
		if(StringUtils.isEmpty(isLock)){
			MemcachedUtil.getInstance().setAttribute(key, value, 5*60*1000); //写值
			Map<String,String>updateMap = new HashMap<String,String>();
			updateMap.put("order_id", order_id);
			updateMap.put("user", user);
			AcquireService.updateEndOpt_Ren(updateMap);
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

	/**
	* 人工处理-->支付锁
	* @param response
	* @param request
	* @return
	*/
		@RequestMapping("/queryPayIsPayLock.do")
		@ResponseBody
		public void queryPayIsPayLock(HttpServletResponse response ,HttpServletRequest request){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String order_id = this.getParam(request, "order_id");
			if(order_id.contains("_")){//“联程”车票订单锁
				order_id = order_id.substring(0, order_id.length()-2);
			}
			String opt_person = loginUserVo.getReal_name();
			String key = "PayLock_" + order_id;
			String value = "PayLock_"+order_id+"&"+opt_person;
			String isLock;
			isLock = (String) MemcachedUtil.getInstance().getAttribute(key); //读值
			if(StringUtils.isEmpty(isLock)){
				MemcachedUtil.getInstance().setAttribute(key, value, 5*60*1000); //写值
				isLock="";
			}else if(isLock.indexOf(opt_person) != -1){
				isLock = "";
			}
			request.setAttribute("key", key);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.write(isLock);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("人工处理-->支付锁response.getWriter()异常");
			}
		}	
	
	/**
	 * 切换备选坐席
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateChangeSeatType.do")
	public String updateChangeSeatType(HttpServletResponse response ,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String order_time = this.getParam(request, "create_time");
		String order_id = this.getParam(request, "order_id");
		String extMap = this.getParam(request, "ext_seattype");
		String ext =SwitchUtils.splitStr_Pre(extMap);
		String opt_person = loginUserVo.getReal_name();
		String userAccount = opt_person+"点击了切换坐席，类型为!"+ext;
		String seat_money = SwitchUtils.splitStr_Last(extMap);
		String extStr = this.getParam(request, "ext_seattypeStr");
		
		String ext_seattype=null;
		int total = SwitchUtils.queryTotal(extStr);//  "#" 出现的次数
		if(total>=2){
			String str = SwitchUtils.splitStr1Pre(extStr);
			String str1 = SwitchUtils.splitStr1Last(extStr);
			ext_seattype = str+"#"+str1+"#"+ext;
		}else{
		ext_seattype = extStr+"#"+ext;
		}
		if(ext.length()>1){
			ext = ext.substring(0,1);
		}
		
		Map<String,String>modify_Map = new HashMap<String,String>();
		Map<String,String>modify_CpMap = new HashMap<String,String>();
		Map<String,String>log_Map = new HashMap<String,String>();
		modify_Map.put("order_id", order_id);
		modify_Map.put("ext_seattype", ext_seattype);
		modify_CpMap.put("order_id", order_id);
		modify_CpMap.put("pay_money", seat_money);
		modify_CpMap.put("seat_type", ext);
		modify_CpMap.put("order_status", "44");
		log_Map.put("order_id", order_id);
		log_Map.put("user", opt_person);
		log_Map.put("userAccount", userAccount);
		log_Map.put("order_time", order_time);
		AcquireService.updateChangeSeatTypeAndOrderInfo(modify_Map,modify_CpMap);
		AcquireService.addUserAccount(log_Map);
		//客服操作记录
		Map<String, Object> optMap = new HashMap<String, Object>();
		optMap.put("userName", opt_person);
		optMap.put("all", "out_ticket");
		tj_OpterService.operate(optMap);
		return "redirect:/acquire/queryAcquireInfo.do?order_id="+order_id+"&query_type=1";
	}
	
	/**
	 * 接收通知
	 * @param request
	 * @param response
	 * @param result
	 */
	@RequestMapping("/receiveOrderInfo.do")
	public void receiveOrderInfo( HttpServletRequest request ,HttpServletResponse response,String result){
		response.setCharacterEncoding("utf-8");
		try{
		logger.info(request.getParameter("result").toString());
			logger.info("data:"+result);
			String flag = AcquireService.updateReceiveOrderInfo(result);
			response.getWriter().write(flag);
			response.getWriter().flush();
			response.getWriter().close();
		}catch(IOException e){
			logger.error("订单更新状态出错"+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	@RequestMapping("/lockAccount.do")
	@ResponseBody
	public void lockAccount(HttpServletRequest request ,HttpServletResponse response){
		String lockAccount = this.getParam(request, "lockAccount");
		String result = null;
		boolean flag = AcquireService.updateLockAccount(lockAccount);
		if(flag){
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
			logger.error("lockAccount.do时response.getWriter().write(result)异常");
		}
	}
	
	/**
	 * 修改差价，并且更新数据到数据库
	 *@param acquire
	 *@param request
	 *@param response
	 *@return
	 */
	@RequestMapping("/updateCpTicketPrice.do")
	public void updateCpTicketPrice(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		String order_id = this.getParam(request, "order_id");
		String pay_money = this.getParam(request, "pay_money");//票价
		String buy_money = this.getParam(request, "buy_money");//进价
		String train_no = this.getParam(request, "train_no");//车次
		String from_city = this.getParam(request, "from_city");//发站
		String to_city = this.getParam(request, "to_city");//到站
		String from_time = this.getParam(request, "from_time");//出发时间
		String to_time = this.getParam(request, "to_time");//到达时间
		String seat_type = this.getParam(request, "seat_type");//席别
		String seat_no = this.getParam(request, "seat_no");//乘客座位号   eg：02号上铺 
		//2、一等座rz1  3、二等座rz2   5、软卧    6、硬卧  7、 软座  8、硬座  9、无座 
		//先根据车次、发站、到站、席别，查询数据库中的票价，若与进价不符则进行修改
		/***************************Map存储并修改*****************************/
		Map<String,String>update_Map = new HashMap<String,String>();
		update_Map.put("cc", train_no);
		update_Map.put("fz", from_city);
		update_Map.put("dz", to_city);
		if(seat_type.equals("2")){//一等座rz1
			update_Map.put("rz1", "rz1");
		}else if(seat_type.equals("3")){//二等座rz2
			update_Map.put("rz2", "rz2");
		}else if(seat_type.equals("5")){//软卧
			if(seat_no.contains("上")){
				update_Map.put("rws", "rws");//软卧上rws
			}else if(seat_no.contains("下")){
				update_Map.put("rwx", "rwx");//软卧下rwx
			}
		}else if(seat_type.equals("6")){//6、硬卧
			if(seat_no.contains("上")){
				update_Map.put("yws", "yws");//硬卧上yws
			}else if(seat_no.contains("中")){
				update_Map.put("ywz", "ywz");//硬卧中ywz
			}else if(seat_no.contains("下")){
				update_Map.put("ywx", "ywx");//硬卧下ywx
			}
		}else if(seat_type.equals("7")){//7 软座
			update_Map.put("rz", "rz");
		}else if(seat_type.equals("8") || seat_type.equals("9")){//8硬座  9无座 
			update_Map.put("yz", "yz");
		}
		String flag = "error";
		//String money = AcquireService.queryOrderMoney(update_Map);
		/**
//		Map<String,String> time_map = AcquireService.queryMoney(update_Map);
//		String start_time = time_map.get("start_time");
//		String arrive_time = time_map.get("arrive_time");
 * **/
		//if(new BigDecimal(buy_money).doubleValue() == new BigDecimal(money).doubleValue()){
		//	flag = "equils";
		//}else if(!buy_money.equals(pay_money)){//进行票价修改
			logger.info("修改票价，【更新票价】之前的数据：进价为"+buy_money+",票价为"+pay_money);
			update_Map.put("buy_money", buy_money);
			AcquireService.updateCpPrice(update_Map);
			flag = "success";
			String userAccount = user+"点击了更新差价按钮!";
			Map<String,String>paramMap = new HashMap<String,String>();
			paramMap.put("order_id", order_id);
			paramMap.put("user", user);
			paramMap.put("userAccount", userAccount);
			paramMap.put("order_time", null);
			//添加操作日志
			AcquireService.addUserAccount(paramMap);
		//}
		/**
//		if(from_time=="" || to_time=="" || start_time!=from_time || arrive_time!=to_time){
//			update_Map.put("start_time", from_time);
//			update_Map.put("arrive_time", to_time);
//			AcquireService.updateCpPrice(update_Map);
//			flag = "success";
//		}**/
		try {
			response.getWriter().write(flag);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args){
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String now = df.format(date);//时间
		System.out.println(now);
	}
	
	
	
	
	
	/**
	 * 根据机器人返回的retInfo（出票失败原因）使后台自动选择出票失败原因以及相应的操作
	 * @param response
	 * @param request
	 * @param retInfo
	 * @return
	 */
	public String receiveOrderFailReason(HttpServletResponse response ,HttpServletRequest request,String retInfo){
		//TODO 封装出票失败
		return "";
	}
	
	
	
	/**
	 * 给CMpay提供接口，根据order_id，merchant_order_id（合作商户订单id），merchant_id（合作商户编号）
	 * 判断order_status是否为44
	 * 是则返回success
	 * 否则返回fail，以及失败原因
	 * @param request
	 * @param response
	 */
	//TODO 对外接口，未完
	@RequestMapping("/queryCMpayOrder.do")
	public void queryCMpayOrder(HttpServletRequest request, HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		String merchant_id = this.getParam(request, "merchant_id");
		Map<String,String>paramMap = new HashMap<String,String>();
		paramMap.put("order_id", order_id);
		paramMap.put("merchant_order_id", merchant_order_id);
		paramMap.put("merchant_id", merchant_id);
		//根据order_id，merchant_order_id（合作商户订单id），merchant_id（合作商户编号）查询order_status
		String order_status = AcquireService.queryCMpayOrderStatus(paramMap);
		JSONObject jsonObj = new JSONObject();  
		if(order_status.equals("44")){
			jsonObj.put("result", "success");
			jsonObj.put("reason", null);
		}else{
			jsonObj.put("result", "fail");
			jsonObj.put("reason", "拒绝取消订单，请走正常流程");
		}
		System.out.println(jsonObj.toString());
		logger.info("CMpay接口:"+jsonObj.toString());
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.write(jsonObj.toString());
	}

	
	/**
	 * 进入查询订单失败页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAcquireFailPage.do")
	public String queryAcquireFailPage(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入查询页面】queryAcquireFailPage.do");
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		return "redirect:/acquire/queryAcquireFailList.do?order_status=10&begin_info_time="+querydate;
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAcquireFailList.do")
	public String queryAcquireFailList(HttpServletRequest request,HttpServletResponse response){
		logger.info("【查询订单失败列表】queryAcquireFailList.do");
		
		String order_id = this.getParam(request, "order_id");//订单号
		String level = this.getParam(request, "level");//订单级别
		String pay_type = this.getParam(request, "pay_type");
		String opt_ren = this.getParam(request, "opt_ren");//操作人
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		List<String> statusList = this.getParamToList(request, "orderStatus");
		List<String> channelList = this.getParamToList(request, "channel");//渠道

		List<String> channel = new ArrayList<String>(channelList);
					if(channel.contains("30101612")){
						channel.add("301016");
						channel.add("30101601");
						channel.add("30101602");
					}
		List<String> errorInfoList = new ArrayList<String>();//失败原因
		String ERROR_INFO_1 = this.getParam(request, "ERROR_INFO_1");
		String ERROR_INFO_2 = this.getParam(request, "ERROR_INFO_2");
		String ERROR_INFO_3 = this.getParam(request, "ERROR_INFO_3");
		String ERROR_INFO_4 = this.getParam(request, "ERROR_INFO_4");
		String ERROR_INFO_5 = this.getParam(request, "ERROR_INFO_5");
		String ERROR_INFO_6 = this.getParam(request, "ERROR_INFO_6");
		String ERROR_INFO_7 = this.getParam(request, "ERROR_INFO_7");
		String ERROR_INFO_8 = this.getParam(request, "ERROR_INFO_8");
		String ERROR_INFO_11 = this.getParam(request, "ERROR_INFO_11");
		String ERROR_INFO_9 = this.getParam(request, "ERROR_INFO_9");
		if(!"".equals(ERROR_INFO_1))errorInfoList.add("1");
		if(!"".equals(ERROR_INFO_2))errorInfoList.add("2");
		if(!"".equals(ERROR_INFO_3))errorInfoList.add("3");
		if(!"".equals(ERROR_INFO_4))errorInfoList.add("4");
		if(!"".equals(ERROR_INFO_5))errorInfoList.add("5");
		if(!"".equals(ERROR_INFO_6))errorInfoList.add("6");
		if(!"".equals(ERROR_INFO_7))errorInfoList.add("7");
		if(!"".equals(ERROR_INFO_8))errorInfoList.add("8");
		if(!"".equals(ERROR_INFO_11))errorInfoList.add("11");
		if(!"".equals(ERROR_INFO_9))errorInfoList.add("9");
		request.setAttribute("ERROR_INFO_1", ERROR_INFO_1);
		request.setAttribute("ERROR_INFO_2", ERROR_INFO_2);
		request.setAttribute("ERROR_INFO_3", ERROR_INFO_3);
		request.setAttribute("ERROR_INFO_4", ERROR_INFO_4);
		request.setAttribute("ERROR_INFO_5", ERROR_INFO_5);
		request.setAttribute("ERROR_INFO_6", ERROR_INFO_6);
		request.setAttribute("ERROR_INFO_7", ERROR_INFO_7);
		request.setAttribute("ERROR_INFO_8", ERROR_INFO_8);
		request.setAttribute("ERROR_INFO_11", ERROR_INFO_11);
		request.setAttribute("ERROR_INFO_9", ERROR_INFO_9);
		
		List<String> errorInfoQunarList = this.getParamToList(request, "error_qunar_info");//去哪儿失败原因
		//List<String> errorInfoElongList = this.getParamToList(request, "error_elong_info");//elong失败原因
		String channel1 = this.getParam(request, "channel1");
		if(statusList==null || statusList.equals("") ||statusList.isEmpty()){
			statusList = this.getParamToList(request, "order_status");
		}
		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		paramMap2.put("begin_info_time", begin_info_time);
		paramMap2.put("end_info_time", end_info_time);
		paramMap2.put("order_status", this.getParamToList(request, "order_status"));
		paramMap2.put("channel", channel);
		int totalCount2 = AcquireService.queryAcquireFailListCount(paramMap2);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("level", level);
		paramMap.put("pay_type", pay_type);
		paramMap.put("opt_ren", opt_ren);
		paramMap.put("order_status", statusList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("channel", channel);
		if(channel.indexOf("qunar")>=0){
			paramMap.put("error_info", errorInfoQunarList);
		}	
		else{
			paramMap.put("error_info", errorInfoList);
		}
		
			
		int totalCount = AcquireService.queryAcquireFailListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			
		List<Map<String, String>> acquireFailList = AcquireService.queryAcquireFailList(paramMap);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> channelTypes = AccountVo.getChannels();
		Map<String, String> channel_types = AccountVo.getErrorInfoChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			channelTypes.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
			channel_types.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
		
		request.setAttribute("acquireList", acquireFailList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("acquireStatus", AcquireVo.getAcquireStatus());
		request.setAttribute("statusStr", statusList.toString());
		request.setAttribute("order_id", order_id);
		request.setAttribute("level", level);
		request.setAttribute("pay_type", pay_type);
		request.setAttribute("opt_ren", opt_ren);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("over_time", "1");
		request.setAttribute("seat_Types", AcquireVo.getSEAT_TYPES());
		request.setAttribute("channelStr", channel.toString());
		request.setAttribute("channel_types", channel_types);
		request.setAttribute("channelTypes", channelTypes);
		request.setAttribute("errorInfoStr", errorInfoList.toString());
		request.setAttribute("errorInfoQunarStr", errorInfoQunarList.toString());
		//request.setAttribute("errorInfoElongStr", errorInfoElongList.toString());
		request.setAttribute("error_infos", AccountVo.getErrorInfos());
		request.setAttribute("error_info_qunars", AccountVo.getErrorInfoQunars());
	//	request.setAttribute("error_info_elongs", AccountVo.getErrorInfoElongs());
		request.setAttribute("channel1", channel1);
		request.setAttribute("totalCount", totalCount);
		DecimalFormat decimal = new DecimalFormat("0.00");
		double zhanbi=(double)totalCount*100/(double)totalCount2;
		request.setAttribute("totalCount2", totalCount2);
		if(totalCount2==0)
			request.setAttribute("zhanbi", "0.00");
			else
			request.setAttribute("zhanbi",  decimal.format(zhanbi));
		return "acquire/acquireFailList";
	}
	
	//批量机器处理
	@RequestMapping("/updateBatchToRobot.do")
	public String updateBatchToRobot(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获得当前登录人姓名
		//获取url查询条件
		String travel_time_px=this.getParam(request, "travel_time_px");
		String create_time_px=this.getParam(request, "create_time_px");
		String out_ticket_time_px=this.getParam(request, "out_ticket_time_px");
		String pageIndex = this.getParam(request, "pageIndex");
		String statusList = this.getParam(request, "statusList");
		String channelList = this.getParam(request, "channelList");
		String jsonArr = "["+this.getParam(request, "jsonArr")+"]";
		
		JSONArray array = JSONArray.fromObject(jsonArr); //转换为json数组
		for(int i = 0; i < array.size(); i++){ 
			JSONObject jsonObject = array.getJSONObject(i); 
			String order_id = (String) jsonObject.get("order_id");
			String create_time = (String) jsonObject.get("create_time");
			String userAccount=user+"点击了【批量机器处理】";
			if(StringUtil.isNotEmpty(order_id)){
				
				Map<String,String>updateMap = new HashMap<String,String>();
				updateMap.put("order_status", "01");
				updateMap.put("order_id", order_id);
				String db_order_status = AcquireService.queryDbOrder_status(order_id);
				if("AA".equals(db_order_status))AcquireService.updateStatus00To11(updateMap);
				updateMap.put("opt_ren", user);
				AcquireService.updateAcquireToRobot(updateMap);
					
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", create_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				
				logger.info("【批量机器处理】添加操作日志成功订单id为："+order_id+"，操作人为【"+user+"】");
			}
		} 
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
			String[] arr1 = statusList.split(",");
			for(int i=0;i<arr1.length;i++){
				str1 += "&order_status="+arr1[i];
			}
		}
		String str2 = "";
		if("".equals(channelList)||channelList==null){
			str2 = "";
		}else{
			String[] arr2 = channelList.split(",");
			for(int i=0;i<arr2.length;i++){
				str2 += "&channel="+arr2[i];
			}
		}
		return "redirect:/acquire/queryAcquireList.do?pageIndex="+pageIndex+str1+str2+"&travel_time_px="+travel_time_px+"&out_ticket_time_px="+out_ticket_time_px+"&create_time_px="+create_time_px;
	}
	
    //批量切换帐号处理
	@RequestMapping("/updateBatchToSwitchAccount.do")
	public void updateBatchToSwitchAccount(HttpServletResponse response,HttpServletRequest request){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user = loginUserVo.getReal_name();//获得当前登录人姓名
			//获取url查询条件
			String travel_time_px=this.getParam(request, "travel_time_px");
			String create_time_px=this.getParam(request, "create_time_px");
			String out_ticket_time_px=this.getParam(request, "out_ticket_time_px");
			String pageIndex = this.getParam(request, "pageIndex");
			String statusList = this.getParam(request, "statusList");
			String channelList = this.getParam(request, "channelList");
			String jsonArr = "["+this.getParam(request, "jsonArr")+"]";
			
			JSONArray array = JSONArray.fromObject(jsonArr); //转换为json数组
			//找到当前登录人
			String opt_user = loginUserVo.getReal_name();
			int  succ_num=0;
			int  fail_num=0;
			for(int i = 0; i < array.size(); i++){ 
				JSONObject jsonObject = array.getJSONObject(i); 
				String order_id = (String) jsonObject.get("order_id");
				String create_time = (String) jsonObject.get("create_time");
				String userAccount=user+"点击了【批量切换处理】";
				String result = "yes";
				if(StringUtil.isNotEmpty(order_id)){		
				  //批量切换帐号处理
				  logger.info("手机核验start...");			
				  try{
						Map<String, String> orderInfo = AcquireService.queryAcquireOrderInfo(order_id);
						String acc_id = String.valueOf(orderInfo.get("acc_id"));
						String channel = String.valueOf(orderInfo.get("channel"));
						String acc_username = String.valueOf(orderInfo.get("acc_username"));
						String acc_password = String.valueOf(orderInfo.get("acc_password"));
						String order_time = String.valueOf(orderInfo.get("create_time"));
						String stop_reason = "7";//帐号密码错误
						
						logger.info("【切换账号】orderId="+order_id+"#acc_id="+acc_id+"#channel="+channel+"#acc_username="+acc_username+"#acc_password="+acc_password+"#order_time="+order_time+"#stop_reason="+stop_reason);
						logger.info("【切换账号】queryAcquireInfo.do 【订单号："+order_id+"】切换前账号为：【"+acc_username+"|"+acc_password+"】");
						
						String opt_userAccount = opt_user+"点击了批量切换帐号按钮,切换前账号为："+acc_username+"|"+acc_password;
						//查询出空闲的优先级最高的账号
						String account_channel = channel;
						//if((!"elong".equals(channel)) && (!"tongcheng".equals(channel))&&(!"19e".equals(channel))&&(!"qunar".equals(channel))){
							account_channel = "19e";
						//}
						logger.info("获取的账号渠道"+account_channel);
						String acc = AcquireService.queryAccount(account_channel);
						if(!StringUtil.isNotEmpty(acc)){
							result="no";
						}else{
							//取出之前的
							//切换账号
							AccountVo account = new AccountVo();
							account.setAcc_username(acc_username);
							account.setOpt_logs(opt_user+"批量切换帐号");
							account.setOpt_person(opt_user);
							account.setOrder_id(order_id);
							Map<String,String>map = new HashMap<String,String>();
							map.put("acc_id", acc);
							map.put("order_id", order_id);
							map.put("channel",channel);
							map.put("old_acc_id", acc_id);
							map.put("opt_person", opt_user);
							map.put("stop_reason", stop_reason);
							map.put("acc_username", acc_username);
							map.put("isStopAccount", "yes");
							AcquireService.updateAccount(map,account);
							
							//更新订单状态为，预定重发
							Map<String,String> map2=new HashMap<String, String>();
							map2.put("order_id", order_id);
							map2.put("order_status", "01");
							map2.put("opt_ren", opt_user);
							AcquireService.updateAcquireToRobot(map2);
							
							//添加日志
							Map<String,String>paramMap = new HashMap<String,String>();
							paramMap.put("order_id", order_id);
							paramMap.put("user", opt_user);
							paramMap.put("userAccount", opt_userAccount);
							paramMap.put("order_time", order_time);
							//添加操作日志
							AcquireService.addUserAccount(paramMap);
							
						}
					}catch(Exception e){
						e.printStackTrace();
						result="no";
					}
					
				}
				
				if("yes".equals(result)) {
					succ_num++;
				}else {
					fail_num++;
				}
				
				
			}
			
			//客服操作记录
			Map<String, Object> optMap = new HashMap<String, Object>();
			optMap.put("userName", opt_user);
			optMap.put("all", "out_ticket");
			tj_OpterService.operate(optMap);
			
			String str1 = "";
			if("".equals(statusList)||statusList==null){
				str1 = "";
			}else{
				String[] arr1 = statusList.split(",");
				for(int i=0;i<arr1.length;i++){
					str1 += "&order_status="+arr1[i];
				}
			}
			String str2 = "";
			if("".equals(channelList)||channelList==null){
				str2 = "";
			}else{
				String[] arr2 = channelList.split(",");
				for(int i=0;i<arr2.length;i++){
					str2 += "&channel="+arr2[i];
				}
			}
			
			response.setCharacterEncoding("utf-8");
			String resultString="成功：" + succ_num + "个,失败：" + fail_num + "个";
			try {
				response.getWriter().write(resultString);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e){
				e.printStackTrace();
			}
			
	//		return "redirect:/acquire/queryAcquireList.do?pageIndex="+pageIndex+str1+str2+"&travel_time_px="+travel_time_px+"&out_ticket_time_px="+out_ticket_time_px+"&create_time_px="+create_time_px;
	}

		
	//批量重新支付
	@RequestMapping("/updateBatchToPay.do")
	public String updateBatchToPay(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获得当前登录人姓名
		//获取url查询条件
		String travel_time_px=this.getParam(request, "travel_time_px");
		String create_time_px=this.getParam(request, "create_time_px");
		String out_ticket_time_px=this.getParam(request, "out_ticket_time_px");
		String pageIndex = this.getParam(request, "pageIndex");
		String statusList = this.getParam(request, "statusList");
		String channelList = this.getParam(request, "channelList");
		String jsonArrPay = "["+this.getParam(request, "jsonArrPay")+"]";

		JSONArray array = JSONArray.fromObject(jsonArrPay); //转换为json数组
		for(int i = 0; i < array.size(); i++){ 
			JSONObject jsonObject = array.getJSONObject(i); 
			String order_id = (String) jsonObject.get("order_id");
			String create_time = (String) jsonObject.get("create_time");
			String userAccount=user+"点击了【批量重新支付】";
			Map<String,String>param_map = new HashMap<String,String>();
			if(StringUtil.isNotEmpty(order_id)){
				param_map.put("order_id", order_id);
				param_map.put("user", user);
				param_map.put("order_status", "55");
				param_map.put("isClickButton", "00"); //点击那个按钮的标识： 00：批量支付按钮  11：批量反支按钮
				AcquireService.updateInfoOrderStatusTo55(param_map);
					
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", create_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("【批量重新支付】添加操作日志成功订单id为："+order_id+"，操作人为【"+user+"】");
			}
		} 
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
			String[] arr1 = statusList.split(",");
			for(int i=0;i<arr1.length;i++){
				str1 += "&order_status="+arr1[i];
			}
		}
		String str2 = "";
		if("".equals(channelList)||channelList==null){
			str2 = "";
		}else{
			String[] arr2 = channelList.split(",");
			for(int i=0;i<arr2.length;i++){
				str2 += "&channel="+arr2[i];
			}
		}
		return "redirect:/acquire/queryAcquireList.do?pageIndex="+pageIndex+str1+str2+"&travel_time_px="+travel_time_px+"&out_ticket_time_px="+out_ticket_time_px+"&create_time_px="+create_time_px;
	}
	
	//批量反支
	@RequestMapping("/updateBatchAnomalyPay.do")
	public String updateBatchAnomalyPay(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获得当前登录人姓名
		//获取url查询条件
		String travel_time_px=this.getParam(request, "travel_time_px");
		String create_time_px=this.getParam(request, "create_time_px");
		String out_ticket_time_px=this.getParam(request, "out_ticket_time_px");
		String pageIndex = this.getParam(request, "pageIndex");
		String statusList = this.getParam(request, "statusList");
		String channelList = this.getParam(request, "channelList");
		String jsonArrPay = "["+this.getParam(request, "jsonArrPay")+"]";

		JSONArray array = JSONArray.fromObject(jsonArrPay); //转换为json数组
		for(int i = 0; i < array.size(); i++){ 
			JSONObject jsonObject = array.getJSONObject(i); 
			String order_id = (String) jsonObject.get("order_id");
			String create_time = (String) jsonObject.get("create_time");
			String userAccount=user+"点击了【批量反支】";
			Map<String,String>param_map = new HashMap<String,String>();
			if(StringUtil.isNotEmpty(order_id)){
				param_map.put("order_id", order_id);
				param_map.put("user", user);
				param_map.put("order_status", "55");
				param_map.put("isClickButton", "11"); //点击那个按钮的标识： 00：批量支付按钮  11：批量反支按钮
				AcquireService.updateInfoOrderStatusTo55(param_map);
					
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", create_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("【批量反支】添加操作日志成功订单id为："+order_id+"，操作人为【"+user+"】");
			}
		} 
		String str1 = "";
		if("".equals(statusList)||statusList==null){
			str1 = "";
		}else{
			String[] arr1 = statusList.split(",");
			for(int i=0;i<arr1.length;i++){
				str1 += "&order_status="+arr1[i];
			}
		}
		String str2 = "";
		if("".equals(channelList)||channelList==null){
			str2 = "";
		}else{
			String[] arr2 = channelList.split(",");
			for(int i=0;i<arr2.length;i++){
				str2 += "&channel="+arr2[i];
			}
		}
		return "redirect:/acquire/queryAcquireList.do?pageIndex="+pageIndex+str1+str2+"&travel_time_px="+travel_time_px+"&out_ticket_time_px="+out_ticket_time_px+"&create_time_px="+create_time_px;
	}
	
	//跳转批量失败页面
	@RequestMapping("/queryAcquirePlFail.do")
	public String queryAcquirePlFail(HttpServletResponse response,HttpServletRequest request){
		logger.info("【批量失败页面】queryAcquirePlFail.do");
		String order_idList = this.getParam(request, "orderIdStr");
//		System.out.println("order_idList="+order_idList);
		String channel = this.getParam(request, "channel");
		request.setAttribute("order_idList", order_idList);
		request.setAttribute("channel", channel);
		return "acquire/acquirePlFailOrder";
	}
	
		//批量出票失败
		@RequestMapping("/updateFailOrder.do")
		public void updateFailOrder(HttpServletResponse response,HttpServletRequest request){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user = loginUserVo.getReal_name();//获得当前登录人姓名
			String order_idList = this.getParam(request, "order_idList");
			String error_info  = this.getParam(request, "error_info");
			String[] arr = order_idList.split(",");
			for(int i=0;i<arr.length;i++){
				String order_id = arr[i];
				System.out.println("order_id="+order_id);
				String userAccount=user+"点击了【批量出票失败】";
				AccountVo account = new AccountVo();
				AcquireVo acquire =new AcquireVo();
				if(StringUtil.isNotEmpty(order_id)){
						account.setAcc_username(acquire.getAcc_username());
						account.setOpt_person(user);
						account.setOrder_id(order_id);
						account.setOpt_logs(user+"点击了批量出票失败");
						
						acquire.setOpt_ren(user);
						acquire.setError_info(error_info);
						acquire.setOrder_id(order_id);
						acquire.setOrder_status("10");
						Map<String, String> orderInfo = AcquireService.queryAcquireOrderInfo(order_id);
						String account_id=String.valueOf(orderInfo.get("acc_id"));
						acquire.setAccount_id(account_id);
						try{
							AcquireService.updateAcquire(acquire,account);
						}catch (Exception e) {
							logger.error("批量出票失败：修改状态失败"+e);
						}
					Map<String,String>paramMap = new HashMap<String,String>();
					paramMap.put("order_id", order_id);
					paramMap.put("user", user);
					paramMap.put("userAccount", userAccount);
					//添加操作日志
					AcquireService.addUserAccount(paramMap);
					logger.info("【批量出票失败】添加操作日志成功订单id为："+order_id+"，操作人为【"+user+"】");
				}
			} 
		}	

	
	//出票管理--导出excel
	@RequestMapping("/exportexcel.do")
	public String exportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		String begin_info_time=this.getParam(request, "begin_info_time");
		String end_info_time=this.getParam(request, "end_info_time");
		String manual_order2 = this.getParam(request, "manual_order2");
		String pro_bak2 = this.getParam(request, "pro_bak2");
		List<String> statusList = this.getParamToList(request, "orderStatus");
		List<String> channelList = this.getParamToList(request, "channel");

		List<String> channel = new ArrayList<String>(channelList);
					if(channel.contains("30101612")){
						channel.add("301016");
						channel.add("30101601");
						channel.add("30101602");
					}
		List<String> pay_typeList = this.getParamToList(request, "pay_type");
		if(statusList==null || statusList.equals("") ||statusList.isEmpty()){
			statusList = this.getParamToList(request, "order_status");
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_status", statusList);
		paramMap.put("pay_type", pay_typeList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("channel", channel);
		paramMap.put("pro_bak2", pro_bak2);
		paramMap.put("manual_order2", manual_order2);
		paramMap.put("manual_order", "00");
		List<Map<String, String>> reslist = AcquireService.queryAcquireExcel(paramMap);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			String order_name=m.get("from_city")+"/"+m.get("to_city");
			linkedList.add(order_name);
			linkedList.add(m.get("train_no"));
			Object ob = m.get("buy_money");
			linkedList.add(m.get("pay_money"));
			if(null!=ob){
			linkedList.add(ob.toString());
			}else{
				linkedList.add("");
			}
			linkedList.add(m.get("create_time"));
			linkedList.add(m.get("out_ticket_time"));
			linkedList.add(m.get("pay_time"));
			linkedList.add(m.get("channel"));
			linkedList.add(m.get("from_time"));
			
			list.add(linkedList);
		}
		String title = "火车票出票管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票出票管理.xls";
		String[] secondTitles = { "序号", "订单号","12306单号", "出发/到达", "车次",
				 "票价", "进价", "创建时间 ", "预订时间", "出票时间", "渠道","乘车时间"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);

		return null;
	}
	
	//出票失败--导出excel
	@RequestMapping("/exportFailexcel.do")
	public String exportFailexcel(HttpServletRequest request,
			HttpServletResponse response) {

		String order_id = this.getParam(request, "order_id");//订单号
		String level = this.getParam(request, "level");//订单级别
		String pay_type = this.getParam(request, "pay_type");
		String opt_ren = this.getParam(request, "opt_ren");//操作人
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		List<String> statusList = this.getParamToList(request, "orderStatus");
		List<String> channelList = this.getParamToList(request, "channel");//渠道

		List<String> channel = new ArrayList<String>(channelList);
					if(channel.contains("30101612")){
						channel.add("301016");
						channel.add("30101601");
						channel.add("30101602");
					}
		List<String> errorInfoList = new ArrayList<String>();//失败原因
		String ERROR_INFO_1 = this.getParam(request, "ERROR_INFO_1");
		String ERROR_INFO_2 = this.getParam(request, "ERROR_INFO_2");
		String ERROR_INFO_3 = this.getParam(request, "ERROR_INFO_3");
		String ERROR_INFO_4 = this.getParam(request, "ERROR_INFO_4");
		String ERROR_INFO_5 = this.getParam(request, "ERROR_INFO_5");
		String ERROR_INFO_6 = this.getParam(request, "ERROR_INFO_6");
		String ERROR_INFO_7 = this.getParam(request, "ERROR_INFO_7");
		String ERROR_INFO_8 = this.getParam(request, "ERROR_INFO_8");
		String ERROR_INFO_11 = this.getParam(request, "ERROR_INFO_11");
		String ERROR_INFO_9 = this.getParam(request, "ERROR_INFO_9");
		if(!"".equals(ERROR_INFO_1))errorInfoList.add("1");
		if(!"".equals(ERROR_INFO_2))errorInfoList.add("2");
		if(!"".equals(ERROR_INFO_3))errorInfoList.add("3");
		if(!"".equals(ERROR_INFO_4))errorInfoList.add("4");
		if(!"".equals(ERROR_INFO_5))errorInfoList.add("5");
		if(!"".equals(ERROR_INFO_6))errorInfoList.add("6");
		if(!"".equals(ERROR_INFO_7))errorInfoList.add("7");
		if(!"".equals(ERROR_INFO_8))errorInfoList.add("8");
		if(!"".equals(ERROR_INFO_11))errorInfoList.add("11");
		if(!"".equals(ERROR_INFO_9))errorInfoList.add("9");
		request.setAttribute("ERROR_INFO_1", ERROR_INFO_1);
		request.setAttribute("ERROR_INFO_2", ERROR_INFO_2);
		request.setAttribute("ERROR_INFO_3", ERROR_INFO_3);
		request.setAttribute("ERROR_INFO_4", ERROR_INFO_4);
		request.setAttribute("ERROR_INFO_5", ERROR_INFO_5);
		request.setAttribute("ERROR_INFO_6", ERROR_INFO_6);
		request.setAttribute("ERROR_INFO_7", ERROR_INFO_7);
		request.setAttribute("ERROR_INFO_8", ERROR_INFO_8);
		request.setAttribute("ERROR_INFO_11", ERROR_INFO_11);
		request.setAttribute("ERROR_INFO_9", ERROR_INFO_9);
		List<String> errorInfoQunarList = this.getParamToList(request, "error_qunar_info");//去哪儿失败原因
		if(statusList==null || statusList.equals("") ||statusList.isEmpty()){
			statusList = this.getParamToList(request, "order_status");
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("level", level);
		paramMap.put("pay_type", pay_type);
		paramMap.put("opt_ren", opt_ren);
		paramMap.put("order_status", statusList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("channel", channel);
		if(channel.indexOf("qunar")>=0){
			paramMap.put("error_info", errorInfoQunarList);
		}	
		else{
			paramMap.put("error_info", errorInfoList);
		}
		List<Map<String, String>> reslist = AcquireService.queryAcquireFailExcel(paramMap);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			String order_name=m.get("from_city")+"/"+m.get("to_city");
			linkedList.add(order_name);
			linkedList.add(m.get("train_no"));
			Object ob = m.get("buy_money");
			linkedList.add(m.get("pay_money"));
			if(null!=ob){
			linkedList.add(ob.toString());
			}else{
				linkedList.add("");
			}
			linkedList.add(m.get("create_time"));
			linkedList.add(m.get("out_ticket_time"));
			linkedList.add(m.get("pay_time"));
			linkedList.add(m.get("from_time"));
			List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
			Map<String, String> merchantMap = AccountVo.getChannels();
			for(int i = 0 ; i < merchantList.size() ; i++){
				merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
			}
			merchantMap.put("30101612", "利安");
			
			linkedList.add(merchantMap.get(m.get("channel")));
//			错误信息：1所购买的车次坐席已无票2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 
//			5证件错误6用户要求取消订单7未通过12306实名认证8乘客身份信息待核验 
//			【qunar】错误信息：0、其他 1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票
//			3、qunar票价和12306不符 4、车次数据与12306不一致 5、乘客信息错误6、12306乘客身份信息核验失败
			String error_info= m.get("error_info");
			if("qunar".equals(m.get("channel"))){
				linkedList.add(AccountVo.getErrorInfoQunars().get(error_info));
			}else{
				linkedList.add(AccountVo.getErrorInfos().get(error_info));
			}
			list.add(linkedList);
		}
		String title = "火车票失败订单明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票失败订单.xls";
		String[] secondTitles = { "序号", "订单号","12306单号", "出发/到达", "车次",
				 "票价", "进价", "创建时间 ", "预订时间", "出票时间", "发车时间", "渠道","失败原因"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);

		return null;
	}
	
	
	private String createDate(String begin_info_time, String end_info_time) {
		String date = "日期：";
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		if (begin_info_time.equals(end_info_time)
				|| begin_info_time == end_info_time) {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				date += ss.format(new Date());
			} else {
				date += begin_info_time;
			}
		} else {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += ss.format(new Date()) + "之前";
				} else {
					date += end_info_time + "之前";
				}
			} else {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += begin_info_time + "-------" + ss.format(new Date());
				} else {
					date += begin_info_time + "-------" + end_info_time;
				}
			}
		}
		return date;
	}
	
	/**
	 * 进入出票效率页面
	 */
	@RequestMapping("/queryAcquireXlPage.do")
	public String queryAcquireXlPage(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入出票效率】queryAcquirePage.do");
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
		return "redirect:/acquire/queryAcquireXlList.do?begin_info_time="+querydate;
		}
	/**
	 * 查询出票效率列表
	 */
	@RequestMapping("/queryAcquireXlList.do")
	public String queryAcquireXlList(HttpServletRequest request,HttpServletResponse response){
		logger.info("【查询出票效率列表】queryAcquireXlList.do");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
		
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String now = df.format(date);//今天时间
		String user_name = this.getParam(request, "user_name");
		
		String opt_ren = this.getParam(request, "opt_ren");
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		request.setAttribute("now", now);
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> statusList = this.getParamToList(request, "order_status");
		List<String> channelList = this.getParamToList(request, "channel");

		List<String> channel = new ArrayList<String>(channelList);
					if(channel.contains("30101612")){
						channel.add("301016");
						channel.add("30101601");
						channel.add("30101602");
					}
		String level = this.getParam(request, "level");	
		List<String> pay_typeList = this.getParamToList(request, "pay_type");
		String order_id = this.getParam(request, "order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		String book_time = this.getParam(request, "book_time");//预订效率
		String pay_time = this.getParam(request, "pay_time");//支付效率
		System.out.println("book_time= "+book_time +"pay_time = "+pay_time);
		//查询参数
		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		paramMap2.put("acquireXl", 1);
		paramMap2.put("channel", channel);
		paramMap2.put("begin_info_time", begin_info_time);
		paramMap2.put("end_info_time_xl", end_info_time);
		int totalCount2 = AcquireService.queryAcquireListCountXl(paramMap2);//总条数	
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("acquireXl", 1);
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("opt_ren", opt_ren);
			paramMap.put("user_name", user_name);
			paramMap.put("out_ticket_billno", out_ticket_billno);
			paramMap.put("channel", channel);
			paramMap.put("order_status", statusList);
			paramMap.put("level", level);
			paramMap.put("pay_type", pay_typeList);
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time_xl", end_info_time);
			//预订效率
			if("20".equals(book_time)){
				paramMap.put("begin_book_time", "0");
				paramMap.put("end_book_time", book_time);
			}
			if("30".equals(book_time)){
				paramMap.put("begin_book_time", "20");
				paramMap.put("end_book_time", book_time);
			}
			if("45".equals(book_time)){
				paramMap.put("end_book_time", book_time);
				paramMap.put("begin_book_time", "30");
			}
			if("60".equals(book_time)){
				paramMap.put("begin_book_time", "45");
				paramMap.put("end_book_time", book_time);
			}
			if("90".equals(book_time)){
				paramMap.put("begin_book_time", "60");
				paramMap.put("end_book_time", book_time);
			}
			if("120".equals(book_time)){
				paramMap.put("begin_book_time", "90");
				paramMap.put("end_book_time", book_time);
			}
			if("180".equals(book_time)){
				paramMap.put("begin_book_time", "120");
				paramMap.put("end_book_time", book_time);
			}
			if("300".equals(book_time)){
				paramMap.put("begin_book_time", "180");
				paramMap.put("end_book_time", book_time);
			}
			if("600".equals(book_time)){
				paramMap.put("begin_book_time", "300");
				paramMap.put("end_book_time", book_time);
			}
			if("none".equals(book_time))
				paramMap.put("begin_book_time", "600");
			if("2700".equals(book_time))
				paramMap.put("begin_book_time", "2700");
			//支付效率
			if("20".equals(pay_time)){
				paramMap.put("begin_pay_time", "0");
				paramMap.put("end_pay_time", pay_time);
			}
			if("30".equals(pay_time)){
				paramMap.put("begin_pay_time", "20");
				paramMap.put("end_pay_time", pay_time);
			}
			if("45".equals(pay_time)){
				paramMap.put("end_pay_time", pay_time);
				paramMap.put("begin_pay_time", "30");
			}
			if("60".equals(pay_time)){
				paramMap.put("begin_pay_time", "45");
				paramMap.put("end_pay_time", pay_time);
			}
			if("90".equals(pay_time)){
				paramMap.put("begin_pay_time", "60");
				paramMap.put("end_pay_time", pay_time);
			}
			if("120".equals(pay_time)){
				paramMap.put("begin_pay_time", "90");
				paramMap.put("end_pay_time", pay_time);
			}
			if("180".equals(pay_time)){
				paramMap.put("begin_pay_time", "120");
				paramMap.put("end_pay_time", pay_time);
			}
			if("300".equals(pay_time)){
				paramMap.put("begin_pay_time", "180");
				paramMap.put("end_pay_time", pay_time);
			}
			if("600".equals(pay_time)){
				paramMap.put("begin_pay_time", "300");
				paramMap.put("end_pay_time", pay_time);
			}
			if("none".equals(pay_time))
				paramMap.put("begin_pay_time", "600");
			if("2700".equals(pay_time))
				paramMap.put("begin_pay_time", "2700");
		}
		List<Map<String, String>> acquireList=new ArrayList<Map<String,String>>();
			int totalCount = AcquireService.queryAcquireListCountXl(paramMap);//总条数	
			//分页
			PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			
			acquireList = AcquireService.queryAcquireListXl(paramMap);
		String key = this.getParam(request, "key");
		MemcachedUtil.getInstance().removeAttribute(key);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("opt_ren", opt_ren);
			request.setAttribute("user_name", user_name);
			request.setAttribute("out_ticket_billno", out_ticket_billno);
			request.setAttribute("channelStr", channel.toString());
			request.setAttribute("statusStr", statusList.toString());
			request.setAttribute("level", level);
			request.setAttribute("pay_typeStr", pay_typeList.toString());
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("book_time", book_time);
			request.setAttribute("pay_time", pay_time);
		}
		request.setAttribute("channel_types", merchantMap);
		request.setAttribute("seat_Types", AcquireVo.getSEAT_TYPES());
		request.setAttribute("acquireStatus", AcquireVo.getAcquireStatus());
		request.setAttribute("acquirePayType", AcquireVo.getAcquirePayType());
		request.setAttribute("workertype", AcquireVo.getWorkerType());
		request.setAttribute("acquireList", acquireList);
		request.setAttribute("totalCount", totalCount);
		DecimalFormat decimal = new DecimalFormat("0.00");
		double zhanbi=(double)totalCount*100/(double)totalCount2;
		request.setAttribute("totalCount2", totalCount2);
		if(totalCount2==0)
			request.setAttribute("zhanbi", "0.00");
			else
			request.setAttribute("zhanbi",  decimal.format(zhanbi));
		request.setAttribute("isShowList", 1);
		return "acquire/acquireXlList";
	}
	
	//出票效率--导出excel
	@RequestMapping("/exportexcelXl.do")
	public String exportexcelXl(HttpServletRequest request,
			HttpServletResponse response) {

		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> statusList = this.getParamToList(request, "orderStatus");
		List<String> channelList = this.getParamToList(request, "channel");

		List<String> channel = new ArrayList<String>(channelList);
					if(channel.contains("30101612")){
						channel.add("301016");
						channel.add("30101601");
						channel.add("30101602");
					}
		List<String> pay_typeList = this.getParamToList(request, "pay_type");
		String book_time = this.getParam(request, "book_time");//预订效率
		String pay_time = this.getParam(request, "pay_time");//支付效率
		if(statusList==null || statusList.equals("") ||statusList.isEmpty()){
			statusList = this.getParamToList(request, "order_status");
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("acquireXl", 1);
		paramMap.put("order_status", statusList);
		paramMap.put("pay_type", pay_typeList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time_xl", end_info_time);
		paramMap.put("channel", channel);
		//预订效率
		if("20".equals(book_time)){
			paramMap.put("begin_book_time", "0");
			paramMap.put("end_book_time", book_time);
		}
		if("30".equals(book_time)){
			paramMap.put("begin_book_time", "20");
			paramMap.put("end_book_time", book_time);
		}
		if("45".equals(book_time)){
			paramMap.put("end_book_time", book_time);
			paramMap.put("begin_book_time", "30");
		}
		if("60".equals(book_time)){
			paramMap.put("begin_book_time", "45");
			paramMap.put("end_book_time", book_time);
		}
		if("90".equals(book_time)){
			paramMap.put("begin_book_time", "60");
			paramMap.put("end_book_time", book_time);
		}
		if("120".equals(book_time)){
			paramMap.put("begin_book_time", "90");
			paramMap.put("end_book_time", book_time);
		}
		if("180".equals(book_time)){
			paramMap.put("begin_book_time", "120");
			paramMap.put("end_book_time", book_time);
		}
		if("300".equals(book_time)){
			paramMap.put("begin_book_time", "180");
			paramMap.put("end_book_time", book_time);
		}
		if("600".equals(book_time)){
			paramMap.put("begin_book_time", "300");
			paramMap.put("end_book_time", book_time);
		}
		if("none".equals(book_time))
			paramMap.put("begin_book_time", "600");
		if("2700".equals(book_time))
			paramMap.put("begin_book_time", "2700");
		//支付效率
		if("20".equals(pay_time)){
			paramMap.put("begin_pay_time", "0");
			paramMap.put("end_pay_time", pay_time);
		}
		if("30".equals(pay_time)){
			paramMap.put("begin_pay_time", "20");
			paramMap.put("end_pay_time", pay_time);
		}
		if("45".equals(pay_time)){
			paramMap.put("end_pay_time", pay_time);
			paramMap.put("begin_pay_time", "30");
		}
		if("60".equals(pay_time)){
			paramMap.put("begin_pay_time", "45");
			paramMap.put("end_pay_time", pay_time);
		}
		if("90".equals(pay_time)){
			paramMap.put("begin_pay_time", "60");
			paramMap.put("end_pay_time", pay_time);
		}
		if("120".equals(pay_time)){
			paramMap.put("begin_pay_time", "90");
			paramMap.put("end_pay_time", pay_time);
		}
		if("180".equals(pay_time)){
			paramMap.put("begin_pay_time", "120");
			paramMap.put("end_pay_time", pay_time);
		}
		if("300".equals(pay_time)){
			paramMap.put("begin_pay_time", "180");
			paramMap.put("end_pay_time", pay_time);
		}
		if("600".equals(pay_time)){
			paramMap.put("begin_pay_time", "300");
			paramMap.put("end_pay_time", pay_time);
		}
		if("none".equals(pay_time))
			paramMap.put("begin_pay_time", "600");
		if("2700".equals(pay_time))
			paramMap.put("begin_pay_time", "2700");
		List<Map<String, String>> reslist = AcquireService.queryAcquireExcelXl(paramMap);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(m.get("create_time"));
			linkedList.add(m.get("out_ticket_time"));
			linkedList.add(m.get("pay_time"));
			linkedList.add(m.get("channel"));
			linkedList.add(m.get("from_time"));
			String book_time_xl=String.valueOf(m.get("book_time_xl"));
			String pay_time_xl=String.valueOf(m.get("pay_time_xl"));
			linkedList.add(book_time_xl);
			linkedList.add(pay_time_xl);
			list.add(linkedList);
		}
		String title = "火车票出票管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票出票管理.xls";
		String[] secondTitles = { "序号", "订单号","12306单号","创建时间 ", "预订时间", 
				"出票时间", "渠道","乘车时间","预订时长","支付时长"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);

		return null;
	}
	
	/**
	 *手机核验
	 */
	@RequestMapping("/phoneCheck.do")
	@ResponseBody
	public void phoneCheck(HttpServletResponse response,HttpServletRequest request){
		logger.info("手机核验start...");
		String result = "yes";
		try{
			String order_id = this.getParam(request, "order_id");
			Map<String, String> orderInfo = AcquireService.queryAcquireOrderInfo(order_id);
			String acc_id = String.valueOf(orderInfo.get("acc_id"));
			String channel = String.valueOf(orderInfo.get("channel"));
			String acc_username = String.valueOf(orderInfo.get("acc_username"));
			String acc_password = String.valueOf(orderInfo.get("acc_password"));
			String order_time = String.valueOf(orderInfo.get("create_time"));
			String stop_reason = "7";//帐号密码错误
			
			logger.info("【切换账号】orderId="+order_id+"#acc_id="+acc_id+"#channel="+channel+"#acc_username="+acc_username+"#acc_password="+acc_password+"#order_time="+order_time+"#stop_reason="+stop_reason);
			logger.info("【切换账号】queryAcquireInfo.do 【订单号："+order_id+"】切换前账号为：【"+acc_username+"|"+acc_password+"】");
			//找到当前登录人
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String opt_user = loginUserVo.getReal_name();
			String opt_userAccount = opt_user+"点击了切换按钮,切换前账号为："+acc_username+"|"+acc_password;
			//查询出空闲的优先级最高的账号
			String account_channel = channel;
			//if((!"elong".equals(channel)) && (!"tongcheng".equals(channel))&&(!"19e".equals(channel))&&(!"qunar".equals(channel))){
				account_channel = "19e";
			//}
			logger.info("获取的账号渠道"+account_channel);
			String acc = AcquireService.queryAccount(account_channel);
			if(!StringUtil.isNotEmpty(acc)){
				result="no";
			}else{
				//取出之前的
				//切换账号
				AccountVo account = new AccountVo();
				account.setAcc_username(acc_username);
				account.setOpt_logs(opt_user+"点击了切换");
				account.setOpt_person(opt_user);
				account.setOrder_id(order_id);
				Map<String,String>map = new HashMap<String,String>();
				map.put("acc_id", acc);
				map.put("order_id", order_id);
				map.put("channel",channel);
				map.put("old_acc_id", acc_id);
				map.put("opt_person", opt_user);
				map.put("stop_reason", stop_reason);
				map.put("acc_username", acc_username);
				map.put("isStopAccount", "yes");
				AcquireService.updateAccount(map,account);
				//添加日志
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", opt_user);
				paramMap.put("userAccount", opt_userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				//客服操作记录
				Map<String, Object> optMap = new HashMap<String, Object>();
				optMap.put("userName", opt_user);
				optMap.put("all", "out_ticket");
				tj_OpterService.operate(optMap);
			}
		}catch(Exception e){
			e.printStackTrace();
			result="no";
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 *携程重新查询
	 */
	@RequestMapping("/ctripSearchAgain.do")
	public void ctripSearchAgain(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request, "order_id");
		String create_time = this.getParam(request, "create_time");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_user = loginUserVo.getReal_name();
		
		//携程重新查询(ctrip_orderinfo中 go_status-->11)
		Map<String,String>map = new HashMap<String,String>();
		map.put("order_id", order_id);
		map.put("user", opt_user);
		AcquireService.ctripSearchAgain(map);
		//人工预订改成正在预订
		Map<String,String>updateMap = new HashMap<String,String>();
		updateMap.put("order_status", "11");
		updateMap.put("order_id", order_id);
		updateMap.put("opt_ren", opt_user);
		AcquireService.updateAcquireToRobot(updateMap);
		//添加日志
		Map<String,String>paramMap = new HashMap<String,String>();
		paramMap.put("order_id", order_id);
		paramMap.put("user", opt_user);
		paramMap.put("userAccount",opt_user+ "点击了携程重新 查询");
		paramMap.put("order_time", create_time);
		//添加操作日志
		AcquireService.addUserAccount(paramMap);
	}
	
	/**
	 *修改携程表流程结束
	 */
	@RequestMapping("/updateCtripGoStatusTo44.do")
	public void updateCtripGoStatusTo44(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		String order_id = this.getParam(request, "order_id");
		String ctrip_order_id = this.getParam(request, "ctrip_order_id");
		
		logger.info("修改携程表流程结束order_id=="+order_id); 
		Map<String,String>updateMap = new HashMap<String,String>();
		updateMap.put("order_id", order_id);
		updateMap.put("opt_ren", user);
		updateMap.put("ctrip_order_id", ctrip_order_id);
		AcquireService.updateCtripGoStatusTo44(updateMap);
	}
	/**
	 *  跳转到订单转人工页面
	 *  @param request
	 *  @param response
	 *  @return
	 */
	@RequestMapping("/orderChangeManual.do")
	public String queryOrderStatusManualDeal(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		logger.info(loginUserVo.getUser_name()+","+"进入【订单转人工页面】queryAcquirePage.do");
		return "acquire/orderStatusManualDeal";
	}
	
	/**
	 * 更新订单状态为人工处理
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateOrderStatusToManual.do")
	public void  updateOrderStatusToManual(HttpServletRequest request,HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		logger.info(user+", 进入订单修改操作！");
		String jsonArrPay = "["+this.getParam(request, "jsonArr")+"]";
		String type = this.getParam(request, "type");
		logger.info(","+jsonArrPay);
		int succnum=0;
		int failnum=0;
		String result = null;
		Map<String,Object> jsonMap=new HashMap<String,Object>();
		try {
			JSONArray array = JSONArray.fromObject(jsonArrPay); //转换为json数组
			for(int i = 0; i < array.size(); i++){
				JSONObject jsonObject = array.getJSONObject(i);
				String order_id = (String) jsonObject.get("order_id");
				order_id=order_id.replaceAll("\\s", "");
				Map<String,String>param_map = new HashMap<String,String>();
				if(StringUtil.isNotEmpty(order_id)){
					param_map.put("order_id", order_id);
					param_map.put("user", user);
					String str="";
					if("book".equals(type)) {
						param_map.put("new_order_status", "44");//预定人工
						param_map.put("old_order_status", "11");//正在预定
						str="预定";
					}else if("pay".equals(type)) {
						param_map.put("new_order_status", "61");//支付人工
						param_map.put("old_order_status", "66");//正在支付
						str="支付";
					}

					int count=AcquireService.updateInfoOrderStatusToManual(param_map);
					if(count!=0) {
						succnum++;
					}else {
						failnum++;
					}
					
					String userAccount=user+"点击了订单转【"+str+"】人工操作";
					Map<String,String>paramMap = new HashMap<String,String>();
					paramMap.put("order_id", order_id);
					paramMap.put("user", user);
					paramMap.put("userAccount", userAccount);
					paramMap.put("order_time", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
					//添加操作日志
					AcquireService.addUserAccount(paramMap);
					logger.info("【更新订单状态为人工处理】添加操作日志成功订单id为："+order_id+"，操作人为【"+user+"】");	
				}	
						
			}
			
			jsonMap.put("succnum", succnum);
			jsonMap.put("failnum", failnum);
			jsonMap.put("msg", "操作成功");
			jsonMap.put("status", true);

		} catch(Exception e){
			logger.info("updateOrderStatusToManual出现异常",e);
			jsonMap.put("succnum", succnum);
			jsonMap.put("failnum", failnum);
			jsonMap.put("msg", "异常失败,"+e.getMessage());
			jsonMap.put("status", false);
		}
		
		result=JSON.toJSONString(jsonMap);
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			logger.info("response输出异常",e);
		}
		
		
	}
	

}
















