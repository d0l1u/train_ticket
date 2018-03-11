package com.l9e.transaction.controller;

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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.service.OldOrderService;
import com.l9e.transaction.service.TrainSystemSettingService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.SwitchUtils;

/**
 * 历史订单
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/oldOrder")
public class OldOrderController extends BaseController{
	@Resource
	private OldOrderService oldOrderService;
	
	@Resource
	private ExtRefundService extRefundService;
	
	@Resource
	private TrainSystemSettingService trainSystemSettingService;
	/**
	 * 查询页面
	
	 */
	@RequestMapping("/queryOldOrderPage.do")
	public String queryOldOrderPage(HttpServletResponse response ,HttpServletRequest request){
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		request.setAttribute("merchantList", merchantList);
		return "redirect:/oldOrder/queryOldOrderList.do?begin_info_time="+querydate;
	}

	/**
	 * 查询列表
	 * @param request 
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOldOrderList.do")
	public String queryOldOrderList(HttpServletRequest request,
			HttpServletResponse response){
		
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
			//获得系统当前时间
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
				paramMap.put("channel", channel);
				paramMap.put("order_status", statusList);
				paramMap.put("level", level);
				paramMap.put("pay_type", pay_typeList);
				paramMap.put("begin_info_time", begin_info_time);
				paramMap.put("end_info_time", end_info_time);
			}
	
			List<Map<String, String>> acquireList=new ArrayList<Map<String,String>>();
			if(user_name.isEmpty() || "".equals(user_name)){
				int totalCount = oldOrderService.queryOldOrderCount(paramMap);//总条数	
				//分页
				PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
				paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
				paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
				
				acquireList = oldOrderService.queryOldOrderList(paramMap);
			}else{
				int totalCount = oldOrderService.queryOldOrderCountCp(paramMap);//总条数	
				//分页
				PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
				paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
				paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
				
				acquireList = oldOrderService.queryOldOrderListCp(paramMap);
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
			}
			request.setAttribute("channel_types", merchantMap);
			request.setAttribute("seat_Types", AcquireVo.getSEAT_TYPES());
			request.setAttribute("acquireStatus", AcquireVo.getAcquireStatus());
			request.setAttribute("acquirePayType", AcquireVo.getAcquirePayType());
			request.setAttribute("workertype", AcquireVo.getWorkerType());
			request.setAttribute("acquireList", acquireList);
			request.setAttribute("isShowList", 1);
		
		return "oldOrder/oldOrderList";
	}


	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOldOrderInfo.do")
	public String queryAcquireInfo(HttpServletRequest request,
			HttpServletResponse response){
		String canOperation = this.getParam(request, "canOperation");
		String order_id = this.getParam(request,"order_id");
		String budan = this.getParam(request,"budan");
		String channel = this.getParam(request, "channel");//获取渠道
		Map<String, String> orderInfo = oldOrderService.queryOldOrderInfo(order_id);
		List<Map<String, Object>> cpList = oldOrderService.queryOldOrderInfoCp(order_id);
		
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
		
		return "oldOrder/oldOrderInfo";
	}
	
	//导出excel
	@RequestMapping("/exportChanneltjexcel.do")
	public String exportChanneltjexcel(HttpServletRequest request,
			HttpServletResponse response) {
		/******************查询条件********************/
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		List<String> channelList = this.getParamToList(request, "channel");//渠道
		List<String> channel = new ArrayList<String>(channelList);
		if(channel.contains("30101612")){
			channel.add("301016");
			channel.add("30101601");
			channel.add("30101602");
		}
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("channel", channel);
		
		List<Map<String, String>> reslist = oldOrderService.queryOldOrderExcel(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("channel"));
			linkedList.add(String.valueOf(m.get("create_time")));
			linkedList.add(String.valueOf(m.get("search_count")));
			linkedList.add(String.valueOf(m.get("search_success")));
			linkedList.add(String.valueOf(m.get("search_fail")));
			linkedList.add(String.valueOf(m.get("msg_count")));
			linkedList.add(String.valueOf(m.get("alter_count")));
			linkedList.add(String.valueOf(m.get("alter_success")));
			linkedList.add(String.valueOf(m.get("alter_fail")));
			linkedList.add(String.valueOf(m.get("refund_count")));
			linkedList.add(String.valueOf(m.get("refund_success")));
			linkedList.add(String.valueOf(m.get("refund_fail")));
			linkedList.add(m.get("alter_success_cgl"));
			linkedList.add(m.get("refund_success_cgl"));
			list.add(linkedList);
		}

		String title = "火车票渠道统计明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票渠道统计.xls";
		String[] secondTitles = { "序号", "渠道", "日期", "查询总数","查询成功", "查询失败",
				"下发短信", "改签总数", "改签成功", "改签失败",  "退款总数" ,"退款成功","退款失败","改签成功率","退款成功率"};
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

}
