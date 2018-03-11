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
import com.l9e.transaction.service.AcquireService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.SwitchUtils;


/**
 * 人工出票管理
 * @author zhangjc02
 *
 */
@Controller
@RequestMapping("/manual")
public class ManualOrderController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(ManualOrderController.class);
	@Resource
	private AcquireService AcquireService;

	@Resource
	private ExtRefundService extRefundService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryManualPage.do")
	public String queryAcquirePage(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入查询页面】queryManualPage.do");
		Calendar theCa = Calendar.getInstance(); 

		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, -2); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		return "redirect:/manual/queryManualList.do?order_status=MM&order_status=82&create_time_px=up&begin_info_time="+querydate;
		}
	
	
	/**
	 * 人工查询列表
	 * @param request
	 * @param response
	 * @param order_status
	 * @return
	 */
	@RequestMapping("queryManualList.do")
	public String queryAcquireRengongList(HttpServletRequest request,HttpServletResponse response){
		logger.info("【查询列表】queryManualList.do");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> statusList = this.getParamToList(request, "order_status");
		List<String> channelList = this.getParamToList(request, "channel");
		String level = this.getParam(request, "level");	
		List<String> channel = new ArrayList<String>(channelList);
		if(channel.contains("30101612")){
			channel.add("301016");
			channel.add("30101601");
			channel.add("30101602");
		}
		String order_id = this.getParam(request, "order_id");
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
		paramMap.put("channel", channel);
		paramMap.put("order_status", statusList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("level", level);
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
		
		paramMap.put("manual_order", "11");
		int totalCount = AcquireService.queryAcquireListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, String>> manualList = AcquireService.queryAcquireList(paramMap);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantMap", merchantMap);
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("level", level);
			request.setAttribute("statusStr", statusList.toString());
			
		}
		request.setAttribute("seat_Types", AcquireVo.getSEAT_TYPES());
		request.setAttribute("channelStr", channel.toString());
		request.setAttribute("manualStatus", AcquireVo.getManualStatus());
		request.setAttribute("manualList", manualList);
		request.setAttribute("isShowList", 1);
		return "manual/manualList";
	}
	

	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryManualInfo.do")
	public String queryManualInfo(HttpServletRequest request,
			HttpServletResponse response){
		String canOperation = this.getParam(request, "canOperation");
		String order_id = this.getParam(request,"order_id");
		String budan = this.getParam(request,"budan");
		String channel = this.getParam(request, "channel");//获取渠道
		logger.info("【查询明细】queryManualInfo.do 【订单号："+order_id+"】");
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
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		request.setAttribute("cpIndoSb", sb);
		request.setAttribute("query_type", query_type);
		request.setAttribute("seat_type_qunar", AcquireVo.getSEAT_TYPES());
		request.setAttribute("channel_types", merchantMap);
		request.setAttribute("channel_type", errorMap);
		request.setAttribute("seatList", seatList);
		request.setAttribute("history", history);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("canOperation", canOperation);
		request.setAttribute("manualStatus", AcquireVo.getManualStatus());
		request.setAttribute("idstype", BookVo.getIdstype());
		request.setAttribute("tickettype", BookVo.getTicketType());
		request.setAttribute("seattype", BookVo.getSeattype());
		request.setAttribute("bank_type", AcquireVo.getBank());
		request.setAttribute("channel", channel);
		request.setAttribute("cpList", cpList);
		request.setAttribute("budan", budan);
		
		return "manual/manualInfo";
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
	 *获取账号 
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
			
			logger.info("【获取账号】queryManualInfo.do 【订单号："+order_id+"】获取前账号为：【"+acc_username+"|"+acc_password+"】");
			//找到当前登录人
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String opt_user = loginUserVo.getReal_name();
			String opt_userAccount = opt_user+"点击了获取账号按钮";
			//查询出空闲的优先级最高的账号
			String account_channel ="manual";
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
		String order_status = this.getParam(request, "order_status");
		String error_info = this.getParam(request, "error_info");
		String order_time = this.getParam(request, "create_time");
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
			if(order_status!=null && db_order_status.equals("MM") && order_status.equals("88")){//11人工查询，支付完成
				String buy_money_total = this.getParam(request, "buy_money_total");
				String out_ticket_billno =this.getParam(request, "out_ticket_billno");
				List<String> cp_id_list = this.getParamToList(request, "cp_id");
				List<String> train_box_list= this.getParamToList(request, "train_box");
				List<String> seat_no_list= this.getParamToList(request, "seat_no");
				List<String> buy_money_list = this.getParamToList(request, "per_buy_money");
				
				logger.info("【人工出票】开始!buy_money_total="+buy_money_total+
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
				
				AcquireService.updateOrderInfoFor61(update_Map);
				
				AcquireService.updateCpListFor61(cpList);
				logger.info("【修改子表中车厢，座位号与价钱成功】");
				AcquireService.updateNotify(order_id);
				logger.info("【修改通知】");
				acquire.setPay_type("1");
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【人工出票】》》》【支付完成】成功！order_id:"+order_id);
				String userAccount = user+"点击了支付成功按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("MM") && order_status.equals("10")){    //1人工查询，出票失败
				acquire.setError_info(error_info);
				account.setOpt_logs(user+"点击了出票失败");
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【人工出票】》》》【出票失败】成功！order_id:"+order_id);
				String userAccount = user+"点击了【人工出票】》》》【出票失败】按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}else if(order_status!=null && db_order_status.equals("82") && order_status.equals("88")){//人工出票，出票成功
				String buy_money_total = this.getParam(request, "buy_money_total");
				String out_ticket_billno =this.getParam(request, "out_ticket_billno");
				List<String> cp_id_list = this.getParamToList(request, "cp_id");
				List<String> train_box_list= this.getParamToList(request, "train_box");
				List<String> seat_no_list= this.getParamToList(request, "seat_no");
				List<String> buy_money_list = this.getParamToList(request, "per_buy_money");
				
				logger.info("【人工查询】开始!buy_money_total="+buy_money_total+
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
				
				AcquireService.updateOrderInfoFor61(update_Map);
				
				AcquireService.updateCpListFor61(cpList);
				logger.info("【修改子表中车厢，座位号与价钱成功】");
				AcquireService.updateNotify(order_id);
				logger.info("【修改通知】");
				acquire.setPay_type("1");
				AcquireService.updateAcquire(acquire,account);
				logger.info("【更新订单信息】【人工查询】》》》【出票完成】！order_id:"+order_id);
				String userAccount = user+"点击了【人工查询】》》》【出票完成】按钮!";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", order_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				logger.info("添加操作日志成功订单id为："+order_id+"操作人为【"+user+"】");
			}
		}else{
			logger.info("order_id为空");
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

	//出票管理--导出excel
	@RequestMapping("/exportexcel.do")
	public String exportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		String begin_info_time=this.getParam(request, "begin_info_time");
		String end_info_time=this.getParam(request, "end_info_time");
		
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
		paramMap.put("manual_order", "11");
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
		modify_CpMap.put("order_status", "MM");
		log_Map.put("order_id", order_id);
		log_Map.put("user", opt_person);
		log_Map.put("userAccount", userAccount);
		log_Map.put("order_time", order_time);
		AcquireService.updateChangeSeatTypeAndOrderInfo(modify_Map,modify_CpMap);
		AcquireService.addUserAccount(log_Map);
		return "redirect:/manual/queryManualInfo.do?order_id="+order_id+"&query_type=2";
	}
	
	//批量机器处理
	@RequestMapping("/updateBatchToRobot.do")
	public String updateBatchToRobot(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获得当前登录人姓名
		//获取url查询条件
		String pageIndex = this.getParam(request, "pageIndex");
		String jsonArr = "["+this.getParam(request, "jsonArr")+"]";
		
		JSONArray array = JSONArray.fromObject(jsonArr); //转换为json数组
		for(int i = 0; i < array.size(); i++){ 
			JSONObject jsonObject = array.getJSONObject(i); 
			String order_id = (String) jsonObject.get("order_id");
			String create_time = (String) jsonObject.get("create_time");
			String userAccount=user+"点击了人工出票  【机器处理】》》》将【人工出票】订单分到【出票管理】";
			if(StringUtil.isNotEmpty(order_id)){
				
				Map<String,String>updateMap = new HashMap<String,String>();
				updateMap.put("order_status", "44");
				updateMap.put("order_id", order_id);
				updateMap.put("opt_ren", user);
				Map<String, String> orderInfo = AcquireService.queryAcquireOrderInfo(order_id);
				String acc_username=orderInfo.get("acc_username");
				String channel=orderInfo.get("channel");
				String acc_id=String.valueOf(orderInfo.get("acc_id"));

				Map<String,String>map = new HashMap<String,String>();
				map.put("acc_id", " ");// cp_orderinfo 账号置为空
				map.put("order_id", order_id);
				map.put("channel",channel);
				map.put("old_acc_id", acc_id);
				map.put("opt_person", user);
				map.put("acc_username", acc_username);
				AcquireService.updateManualToRobot(map,updateMap);
				
				
				
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				paramMap.put("order_time", create_time);
				//添加操作日志
				AcquireService.addUserAccount(paramMap);
				
				logger.info("将【人工出票】订单分到【出票管理】 添加操作日志成功订单id为："+order_id+"，操作人为【"+user+"】");
			}
		} 
		return "redirect:/manual/queryManualPage.do";
	}
	
}
















