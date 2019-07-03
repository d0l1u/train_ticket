package com.l9e.transaction.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.ElongBookService;
import com.l9e.transaction.service.OrderForExcelService;
import com.l9e.transaction.service.TuniuRefundService;
import com.l9e.transaction.service.TuniuUrgeRefundService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.TuniuRefundVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;
/**
 * 导出Excel
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/orderForExcel")
public class OrderForExcelController extends BaseController {
	private static final Logger logger = Logger.getLogger(OrderForExcelController.class);
	@Resource
	private OrderForExcelService orderForExcelService;
	
	@Resource
	private TuniuRefundService tuniuRefundService;
	
	@Resource
	private TuniuUrgeRefundService tuniuUrgeRefundService;
	
	/**
	 * 19e预订管理中到处Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelexport.do")
	public void ExcelExport(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了19e预定管理导出");
		String at_province_id = this.getParam(request, "at_province_id");
		String at_city_id = this.getParam(request, "at_city_id");
		String order_id = this.getParam(request, "order_id");
		String manual_order = this.getParam(request, "manual_order");
		String user_phone = this.getParam(request,"user_phone");
		String eop_order_id = this.getParam(request,"eop_order_id");
		String out_ticket_billno = this.getParam(request,"out_ticket_billno");
		String begin_info_time = this.getParam(request,"begin_info_time");
		String end_info_time = this.getParam(request,"end_info_time");
		String begin_out_time = this.getParam(request,"begin_out_time");
		String end_out_time = this.getParam(request,"end_out_time");
		List<String> order_status = this.getParamToList(request, "order_status");
		List<String> order_sourceList = this.getParamToList(request, "order_source");
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		if(at_province_id.equals("000000")){			//如果省为全国时，at_province_id设置为null
			at_province_id = null;
		}
		paramMap.put("at_province_id", at_province_id);
		paramMap.put("at_city_id",at_city_id);
		paramMap.put("order_id",order_id);
		paramMap.put("manual_order",manual_order);
		paramMap.put("user_phone",user_phone);
		paramMap.put("eop_order_id",eop_order_id);
		paramMap.put("out_ticket_billno",out_ticket_billno);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time",end_info_time);
		paramMap.put("begin_out_time", begin_out_time);
		paramMap.put("end_out_time",end_out_time);
		paramMap.put("order_status",order_status);
		paramMap.put("order_source", order_sourceList);
		List<Map<String, String>> reslist = orderForExcelService.getOrderForExcelList(paramMap);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			if(m.get("create_time")!=null){
			linkedList.add(String.valueOf(m.get("create_time")));}
			else{ linkedList.add("");}
			if(m.get("out_ticket_time")!=null){
			linkedList.add(String.valueOf(m.get("out_ticket_time")));}
			else{ linkedList.add("");}
			if(m.get("bx_pay_money")!=null){
			linkedList.add(String.valueOf(m.get("bx_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("server_pay_money")!=null){
			linkedList.add(String.valueOf(m.get("server_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("ticket_pay_money")!=null){
			linkedList.add(String.valueOf(m.get("ticket_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("buy_money")!=null){
			linkedList.add(String.valueOf(m.get("buy_money")));}
			else{ linkedList.add("0");}
			if(m.get("pay_money")!=null){
			linkedList.add(String.valueOf(m.get("pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("differ")!=null){
			linkedList.add(String.valueOf(m.get("differ")));}
			else{ linkedList.add("0");}
			
//			linkedList.add(m.get("out_ticket_account"));
//			linkedList.add(m.get("bank_pay_seq"));
//			linkedList.add(m.get("dealer_phone"));
			//保险
			List<Map<String,Object>>info = orderForExcelService.queryBxInfo(m.get("order_id"));
			String str =null;String str1 =null;
			StringBuffer sb = new StringBuffer();
//			StringBuffer sb1 = new StringBuffer();
//			long bx_count = 0;
			for(int i=0;i<info.size();i++){
//				bx_count=bx_count+(Long)info.get(i).get("bx_count");
				long thiscount= (Long)info.get(i).get("bx_count");
				str=info.get(i).get("bx_info").toString();
				str1=thiscount+"*"+str;
				sb=sb.append(str1);
				sb.append("  ");
//				sb1=sb1.append(str);
//				sb1.append("  ");
			}
//			linkedList.add(String.valueOf(bx_count));
//			linkedList.add(String.valueOf(sb1));
			linkedList.add(String.valueOf(sb));
			if(m.get("ticket_num")!=null){
			linkedList.add(String.valueOf(m.get("ticket_num")));}
			else{ linkedList.add("0");}
			list.add(linkedList);
		}
		
		String title = "19e预定管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "19e预定管理.xls";
		String[] secondTitles = {"序号", "订单号（HC）", "取票单号",
				"创建时间","出票时间", "保险", "SVIP", "票价", "实际订票价","代理商支付金额","差价",
//				"出票账号","支付流水号","代理商手机号","保险张数","保险类别",
				"保险明细","票数" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * 19e退款管理导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelexportForRefund.do")
	public void excelexportForRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了19e退款管理导出");
		/******************************页面表单参数********************************/
		String begin_create_time = this.getParam(request, "begin_create_time");
		String end_create_time = this.getParam(request, "end_create_time");
		String order_id = this.getParam(request, "order_id");
		String eop_order_id = this.getParam(request, "eop_order_id");
		String refund_seq = this.getParam(request, "refund_seq");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");
		List<String> refund_type = this.getParamToList(request, "refund_type");
		List<String> refund_status = this.getParamToList(request, "refund_status");
		List<String> order_sourceList = this.getParamToList(request, "order_source");
		if(refund_status.contains("012")){
			refund_status.add("01");
			refund_status.add("02");
			refund_status.remove("012");
		}
		if(refund_status.contains("456")){
			refund_status.add("04");
			refund_status.add("05");
			refund_status.add("06");
			refund_status.remove("456");
		}
		/******************************Map存储********************************/
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_create_time", begin_create_time);
		paramMap.put("end_create_time", end_create_time);
		paramMap.put("order_id", order_id);
		paramMap.put("eop_order_id", eop_order_id);
		paramMap.put("refund_seq", refund_seq);
		paramMap.put("refund_12306_seq", refund_12306_seq);
		paramMap.put("refund_type", refund_type);
		paramMap.put("refund_status", refund_status);
		paramMap.put("order_source", order_sourceList);
		
		List<Map<String, String>> reslist = orderForExcelService.getOrderForExcelRefundList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("cp_id"));
			
			if(m.get("refund_money")!=null){
			linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
			
			if(m.get("actual_refund_money")!=null){
			linkedList.add(String.valueOf(m.get("actual_refund_money")));}
			else{ linkedList.add("0");}
			
			if(m.get("alter_tickets_money")!=null){
			linkedList.add(String.valueOf(m.get("alter_tickets_money")));}
			else{ linkedList.add("0");}
			
			linkedList.add(m.get("create_time"));
			
			if(m.get("verify_time")!=null){			
			linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
			
			if(m.get("refund_plan_time")!=null){
			linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
			
			linkedList.add(m.get("dealer_phone"));
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("change_ticket_info"));
			linkedList.add(m.get("eop_order_id"));
			list.add(linkedList);
		}
		
		String title = "19e退款管理明细";

		String date = createDate(begin_create_time, end_create_time);
		String filename = "19e退款管理.xls";
		String[] secondTitles = {"序号", "订单号（HC）", "车票单号", "退款金额", "实际退款金额",
				"改签差价","退款订单创建时间", "退款审核时间", "计划退款时间", "代理商电话","12306退款流水号" ,"改签明细","eop订单号"};
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * 人员效率考核导出Excel
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("excelexportOpter.do")
	public void  excelexportOpter(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了考核统计导出");
		/******************************页面表单参数********************************/
		String opt_person = this.getParam(request, "real_name");
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		/******************************Map存储********************************/
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("opt_person", opt_person);
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		/******************************执行查询********************************/
		List<Map<String, String>> reslist = orderForExcelService.getOrderForExcelOpterList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(String.valueOf(m.get("tj_time")));
			linkedList.add(m.get("opt_person"));
			linkedList.add(String.valueOf(m.get("out_ticket_total")));
			linkedList.add(String.valueOf(m.get("refund_total")));
			linkedList.add(String.valueOf(m.get("refund_total_19e")));
			linkedList.add(String.valueOf(m.get("refund_total_qunar")));
			linkedList.add(String.valueOf(m.get("refund_total_elong")));
			linkedList.add(String.valueOf(m.get("refund_total_tongcheng")));
			linkedList.add(String.valueOf(m.get("refund_total_meituan")));
			linkedList.add(String.valueOf(m.get("refund_total_inner")));
			linkedList.add(String.valueOf(m.get("refund_total_app")));
			linkedList.add(String.valueOf(m.get("refund_total_gtgj")));
			linkedList.add(String.valueOf(m.get("refund_total_tuniu")));
			linkedList.add(String.valueOf(m.get("refund_total_ctrip")));
//			linkedList.add(String.valueOf(m.get("refund_total_failure")));
//			linkedList.add(String.valueOf(m.get("refund_total_differ")));
			linkedList.add(String.valueOf(m.get("refund_total_ext")));
			linkedList.add(String.valueOf(m.get("refund_total_verify")));
			linkedList.add(String.valueOf(m.get("refund_total_refuse")));
			linkedList.add(String.valueOf(m.get("refund_total_holdon")));
			list.add(linkedList);
		}
		
		String title = "考核统计明细";

		String date = createDate(begin_time, end_time);
		String filename = "考核统计.xls";
		String[] secondTitles = { "序号","统计日期", "统计人员", "订单数","退款数","19e退款","去哪退款","艺龙退款","同程退款","美团退款"
				,"内嵌退款","B2C退款","高铁管家退款","途牛退款","携程退款","商户退款","审核退款","拒绝退票数","搁置订单数"};//2017-3-3 删除-"出票失败退款","差额退款"-增加-高铁途牛携程
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * 出票统计-详细：导出excel
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("excelexportProvinceStat.do")
	public void excelexportProvinceStat(HttpServletResponse response,HttpServletRequest request){
		String create_time = this.getParam(request, "create_time");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了出票统计——详情导出");
		/******************************执行查询********************************/
		List<Map<String, String>> reslist = orderForExcelService.getOrderForExcelProvinceStatList(create_time);
		java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");   
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("province_name"));
			linkedList.add(String.valueOf(m.get("user_count")));
			linkedList.add(String.valueOf(m.get("activeAgent")));
			linkedList.add(String.valueOf(m.get("apply_count")));
			linkedList.add(String.valueOf(m.get("not_pass")));
			linkedList.add(String.valueOf(m.get("wait_pass")));
			linkedList.add(String.valueOf(m.get("order_count")));
			linkedList.add(String.valueOf(m.get("succeed_count")));
			linkedList.add(String.valueOf(m.get("defeated_count")));
			linkedList.add(String.valueOf(m.get("want_outTicket")));
			linkedList.add(String.valueOf(m.get("pay_fall")));
			linkedList.add(String.valueOf(m.get("ticket_count")));
			
			double cg = Double.parseDouble(String.valueOf(m.get("succeed_count")));
			double sb = Double.parseDouble(String.valueOf(m.get("defeated_count")));
			String cgl ="";
			String sbl ="";
			String zhl ="";
			double order_count = Double.parseDouble(String.valueOf(m.get("order_count")));
			if((cg+sb) != 0.00){
				cgl =df.format(cg/(cg+sb)*100)+"%".toString();
				sbl =df.format(sb/(cg+sb)*100)+"%".toString();
			}else{
				cgl = "0.00%";
				sbl = "0.00%";
			}
			if( order_count!= 0.00){
				zhl =df.format(cg/order_count*100)+"%".toString();
			}else{
				zhl = "0.00%";
			}
			String province_name = m.get("province_name");
			HashMap<String,Object> map=new HashMap<String, Object>();
			map.put("province_name", province_name);
			map.put("create_time", create_time);
			Map<String,String> map_Tic = orderForExcelService.queryProvinceTicketAndMoney(map);
			
			linkedList.add(map_Tic.get("total_Ticket"));
			linkedList.add(map_Tic.get("total_Money"));
			linkedList.add(cgl);
			linkedList.add(sbl);
			linkedList.add(zhl);
			list.add(linkedList);
		}
		
		String title = "出票统计中各省明细";

		String date = createDate(create_time, create_time);
		String filename = "出票统计中各省明细.xls";
		String[] secondTitles = {"序号","省","总用户","活跃用户","申请数","未通过数","待审核数","总订单数",
				"订单成功","订单失败","预下单","支付失败","票数","总票数","总金额","成功率","失败率","转化率" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	
	/**
	 * 内嵌预订管理中导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelExportInnerBook.do")
	public void excelExportInnerBook(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了内嵌预定管理导出");
		String order_id = this.getParam(request, "order_id");
		String user_phone = this.getParam(request,"user_phone");
		String out_ticket_billno = this.getParam(request,"out_ticket_billno");
		String begin_info_time = this.getParam(request,"begin_info_time");
		String end_info_time = this.getParam(request,"end_info_time");
		List<String> order_statusList = this.getParamToList(request, "order_status");
		List<String> inner_channel = this.getParamToList(request, "inner_channel");
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id",order_id);
		paramMap.put("user_phone",user_phone);
		paramMap.put("out_ticket_billno",out_ticket_billno);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time",end_info_time);
		paramMap.put("order_status",order_statusList);
		paramMap.put("inner_channel",inner_channel);
		List<Map<String,String>> reslist = orderForExcelService.queryInnerBookList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(String.valueOf(m.get("create_time")));
			if(m.get("out_ticket_time")!=null){
			linkedList.add(String.valueOf(m.get("out_ticket_time")));}
			else{ linkedList.add("");}
			if(m.get("bx_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("bx_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("ticket_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("ticket_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("buy_money")!=null){
				linkedList.add(String.valueOf(m.get("buy_money")));}
			else{ linkedList.add("0");}
			if(m.get("pay_money")!=null){
				linkedList.add(String.valueOf(m.get("pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("differ")!=null){
				linkedList.add(String.valueOf(m.get("differ")));}
			else{ linkedList.add("0");}
				
			linkedList.add(m.get("out_ticket_account"));
			linkedList.add(m.get("bank_pay_seq"));
			linkedList.add(m.get("dealer_phone"));
			//保险
			List<Map<String,Object>>info = orderForExcelService.queryBxInfo(m.get("order_id"));
			String str =null;String str1 =null;
			StringBuffer sb = new StringBuffer();
			StringBuffer sb1 = new StringBuffer();
			long bx_count = 0;
			for(int i=0;i<info.size();i++){
				bx_count=bx_count+(Long)info.get(i).get("bx_count");
				long thiscount= (Long)info.get(i).get("bx_count");
				str=info.get(i).get("bx_info").toString();
				str1=thiscount+"*"+str;
				sb=sb.append(str1);
				sb.append("  ");
				sb1=sb1.append(str);
				sb1.append("  ");
			}
			linkedList.add(String.valueOf(bx_count));
			linkedList.add(String.valueOf(sb1));
			linkedList.add(String.valueOf(sb));
			
			linkedList.add(m.get("from_city")+"/"+m.get("to_city"));
			linkedList.add(m.get("train_no"));
			if(m.get("from_time")!=null){
			linkedList.add(String.valueOf(m.get("from_time")));}
			else{ linkedList.add("");}
			if(m.get("pay_time")!=null){
			linkedList.add(String.valueOf(m.get("pay_time")));}
			else{ linkedList.add("");}
			if("44".equals(m.get("order_status"))){
				linkedList.add("出票成功");
			}else if("45".equals(m.get("order_status"))){
				linkedList.add(AccountVo.getErrorInfos().get(String.valueOf(m.get("error_info")).trim()));
			}else{
				linkedList.add("");
			}
			
			list.add(linkedList);
		}
		
		String title = "内嵌预定管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "内嵌预定管理.xls";
		String[] secondTitles =  {"序号", "订单号", "取票单号",
		"创建时间","出票时间", "保险", "票价", "实际订票价","总价",
		"差价","出票账号","支付流水号","用户账号","保险张数","保险类别","保险明细","出发/到达","车次","出发时间","支付时间","备注" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * inner退款管理中导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelexportForInnerRefund.do")
	public void excelexportForInnerRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了内嵌退款管理导出");
		/******************************页面表单参数********************************/
		String begin_create_time = this.getParam(request, "begin_create_time");
		String end_create_time = this.getParam(request, "end_create_time");
		String order_id = this.getParam(request, "order_id");
		String refund_seq = this.getParam(request, "refund_seq");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");
		List<String> refund_type = this.getParamToList(request, "refund_type");
		List<String> refund_status = this.getParamToList(request, "refund_status");
		List<String> inner_channel = this.getParamToList(request, "inner_channel");
		/******************************Map存储********************************/
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_create_time", begin_create_time);
		paramMap.put("end_create_time", end_create_time);
		paramMap.put("order_id", order_id);
		paramMap.put("refund_seq", refund_seq);
		paramMap.put("refund_12306_seq", refund_12306_seq);
		paramMap.put("refund_type", refund_type);
		paramMap.put("refund_status", refund_status);
		paramMap.put("inner_channel", inner_channel);
		List<Map<String,String>> reslist = orderForExcelService.getOrderForExcelInnerRefundList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("cp_id"));
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("actual_refund_money")!=null){
				linkedList.add(String.valueOf(m.get("actual_refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("alter_tickets_money")!=null){
				linkedList.add(String.valueOf(m.get("alter_tickets_money")));}
			else{ linkedList.add("0");}
				
			linkedList.add(m.get("create_time"));
				
			if(m.get("verify_time")!=null){			
				linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
				
			if(m.get("refund_plan_time")!=null){
				linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
				
			linkedList.add(m.get("user_id"));
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("change_ticket_info"));
			list.add(linkedList);
		}
		
		String title = "内嵌退款管理明细";

		String date = createDate(begin_create_time, end_create_time);
		String filename = "内嵌退款管理.xls";
		String[] secondTitles =  { "序号", "订单号（HC）", "车票单号", "退款金额", "实际退款金额",
				"改签差价","退款订单创建时间", "退款审核时间", "计划退款时间", "用户账号","12306退款流水号","改签明细" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * 合作商户--预订管理中导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelExportExtBooking.do")
	public void excelExportExtBooking(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了预定管理导出");
		String order_id = this.getParam(request, "order_id"); // 订单号
		String merchant_id = this.getParam(request, "user_id"); // 合作商户id
		List<String> statusList = this.getParamToList(request, "order_status"); // 订单状态
		String out_ticket_billno = this.getParam(request, "out_ticket_billno"); // 12306订单号
		String begin_info_time = this.getParam(request, "begin_info_time"); // 查询开始时间
		String end_info_time = this.getParam(request, "end_info_time"); // 查询结束时间
		String queryTime = this.getParam(request, "queryTime"); // 查询的当前时间
		List<String> notify_status = this.getParamToList(request,"notify_status");
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		List<String> merchant_idList = this.getParamToList(request, "merchant_id");
		List<String> merchant_idList2 = new ArrayList<String>(merchant_idList);
		if(merchant_idList2.contains("30101612")){
			merchant_idList2.add("301016");
			merchant_idList2.add("30101601");
			merchant_idList2.add("30101602");
			merchant_idList2.remove("30101612");
		}
		paramMap.put("order_id", order_id);
		paramMap.put("merchant_id", merchant_id);
		paramMap.put("out_ticket_billno", out_ticket_billno);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("order_status", statusList);
		paramMap.put("queryTime", queryTime);
		paramMap.put("notify_status", notify_status);
		paramMap.put("merchant_id", merchant_idList2);
		List<Map<String,String>> reslist = orderForExcelService.queryExtBookList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(String.valueOf(m.get("create_time")));
			if(m.get("out_ticket_time")!=null){
			linkedList.add(String.valueOf(m.get("out_ticket_time")));}
			else{ linkedList.add("");}
			if(m.get("bx_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("bx_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("ticket_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("ticket_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("buy_money")!=null){
				linkedList.add(String.valueOf(m.get("buy_money")));}
			else{ linkedList.add("0");}
			if(m.get("pay_money")!=null){
				linkedList.add(String.valueOf(m.get("pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("differ")!=null){
				linkedList.add(String.valueOf(m.get("differ")));}
			else{ linkedList.add("0");}
			
			linkedList.add(m.get("out_ticket_account"));
			linkedList.add(m.get("bank_pay_seq"));
			linkedList.add(m.get("dealer_phone"));
			//保险
			List<Map<String,Object>>info = orderForExcelService.queryBxInfo(m.get("order_id"));
			String str =null;String str1 =null;
			StringBuffer sb = new StringBuffer();
			StringBuffer sb1 = new StringBuffer();
			long bx_count = 0;
			for(int i=0;i<info.size();i++){
				bx_count=bx_count+(Long)info.get(i).get("bx_count");
				long thiscount= (Long)info.get(i).get("bx_count");
				str=info.get(i).get("bx_info").toString();
				str1=thiscount+"*"+str;
				sb=sb.append(str1);
				sb.append("  ");
				sb1=sb1.append(str);
				sb1.append("  ");
			}
			linkedList.add(String.valueOf(bx_count));
			linkedList.add(String.valueOf(sb1));
			linkedList.add(String.valueOf(sb));
			
			linkedList.add(m.get("from_city")+"/"+m.get("to_city"));
			linkedList.add(m.get("train_no"));
			if(m.get("from_time")!=null){
				linkedList.add(String.valueOf(m.get("from_time")));}
			else{ linkedList.add("");}
			if(m.get("pay_time")!=null){
				linkedList.add(String.valueOf(m.get("pay_time")));}
			else{ linkedList.add("");}
				
			if(m.get("merchant_fee")=="19e_bx"){
				linkedList.add("VIP");
			}else if(m.get("merchant_fee")=="merchant_bx"){
				linkedList.add("SVIP");
			}else{
				linkedList.add(m.get("merchant_fee"));
			}
			
			linkedList.add(m.get("eop_order_id"));
			if(m.get("ticket_num")!=null){
			linkedList.add(String.valueOf(m.get("ticket_num")));}
			else {linkedList.add(m.get("0"));}
			
			if("44".equals(m.get("order_status"))){
				linkedList.add("出票成功");
			}else if("45".equals(m.get("order_status"))){
				linkedList.add(AccountVo.getErrorInfos().get(String.valueOf(m.get("error_info")).trim()));
			}else{
				linkedList.add("");
			}
			
			list.add(linkedList);
		}
		
		String title = "对外商户预定管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "对外商户预定管理.xls";
		String[] secondTitles =  {"序号", "订单号", "取票单号","创建时间","出票时间", "保险", "票价", "实际订票价","总价","差价","出票账号",
				"支付流水号","合作商户账号","保险张数","保险类别","保险明细","出发/到达","车次","出发时间","支付时间","手续费","EOP订单号","票数","备注" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * 对外商户管理中退款导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelexportForExtRefund.do")
	public void excelexportForExtRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了对外商户退票管理导出");
		/******************************页面表单参数********************************/
		String begin_create_time = this.getParam(request, "begin_create_time");
		String end_create_time = this.getParam(request, "end_create_time");
		String order_id = this.getParam(request, "order_id");
		String refund_seq = this.getParam(request, "refund_seq");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");
		List<String> refund_type = this.getParamToList(request, "refund_type");
		List<String> refund_status = this.getParamToList(request, "refund_status");
		List<String> notify_status = this.getParamToList(request,"notify_status");
		List<String> merchant_idList = this.getParamToList(request, "merchant_id");
		List<String> merchant_idList2 = new ArrayList<String>(merchant_idList);
		if(merchant_idList2.contains("30101612")){
			merchant_idList2.add("301016");
			merchant_idList2.add("30101601");
			merchant_idList2.add("30101602");
			merchant_idList2.remove("30101612");
		}
		if(refund_status.contains("012")){
			refund_status.add("01");
			refund_status.add("02");
			refund_status.remove("012");
		}
		if(refund_status.contains("456")){
			refund_status.add("04");
			refund_status.add("05");
			refund_status.add("06");
			refund_status.remove("456");
		}
		/******************************Map存储********************************/
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_create_time", begin_create_time);
		paramMap.put("end_create_time", end_create_time);
		paramMap.put("order_id", order_id);
		paramMap.put("refund_seq", refund_seq);
		paramMap.put("refund_12306_seq", refund_12306_seq);
		paramMap.put("refund_type", refund_type);
		paramMap.put("refund_status", refund_status);
		paramMap.put("notify_status", notify_status);
		paramMap.put("merchant_id", merchant_idList2);
				
		List<Map<String,String>> reslist = orderForExcelService.getOrderForExcelExtRefundList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("cp_id"));
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("actual_refund_money")!=null){
				linkedList.add(String.valueOf(m.get("actual_refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("alter_tickets_money")!=null){
				linkedList.add(String.valueOf(m.get("alter_tickets_money")));}
			else{ linkedList.add("0");}
				
			linkedList.add(m.get("create_time"));
				
			if(m.get("verify_time")!=null){			
				linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
				
			if(m.get("refund_plan_time")!=null){
				linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
			
			linkedList.add(m.get("dealer_phone"));
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("change_ticket_info"));
			linkedList.add(String.valueOf(m.get("ticket_num")));
			linkedList.add(m.get("eop_order_id"));
			list.add(linkedList);
		}
		
		String title = "对外商户退款管理明细";

		String date = createDate(begin_create_time, end_create_time);
		String filename = "对外商户退款管理.xls";
		String[] secondTitles =  { "序号", "订单号（HC）", "车票单号", "退款金额", "实际退款金额","改签差价",
				"退款订单创建时间", "退款审核时间", "计划退款时间", "合作商户账号","12306退款流水号","改签明细","票数","eop订单号" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * 保险管理导出Excel
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("excelexportAllInsurance.do")
	public void excelexportAllInsurance(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了保险管理的导出");
		/******************************页面表单参数********************************/
		String order_id = this.getParam(request, "order_id");
		String telephone = this.getParam(request, "telephone");
		String begin_time = this.getParam(request, "begin_create_time");
		String end_time = this.getParam(request, "end_create_time");
		String bx_code = this.getParam(request, "bx_code");
		List<String>bx_status = this.getParamToList(request, "insueance_status");
		List<String>bx_channel = this.getParamToList(request, "insueance_bx_channel");
		List<String>channel = this.getParamToList(request, "channel_types");
		/******************************Map存储********************************/
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("telephone", telephone);
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("bx_code", bx_code);
		paramMap.put("bx_status", bx_status);
		paramMap.put("bx_channel", bx_channel);
		paramMap.put("channel", channel);
		/******************************执行查询********************************/
			List<Map<String,String>> reslist = orderForExcelService.getOrderForExcelAllInsuranceList(paramMap);
			List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
			for (Map<String, String> m : reslist) {
				LinkedList<String> linkedList = new LinkedList<String>();
				/**
				 * "序号","保险单号（BX）", "订单号", "车票单号（CP）", "创建时间", "生效日期",
					"支付金额","保险单号" ,"代理商姓名","代理商账号"};
				 */
				linkedList.add(m.get("bx_id"));
				linkedList.add(m.get("order_id"));
				linkedList.add(m.get("cp_id"));
				linkedList.add(m.get("create_time"));
				linkedList.add(m.get("effect_date"));
				if(m.get("pay_money")!=null){
					linkedList.add(String.valueOf(m.get("pay_money")));}
				else{ linkedList.add("0");}
				linkedList.add(m.get("bx_code"));
				linkedList.add(m.get("dealer_name"));
				linkedList.add(m.get("user_phone"));
				list.add(linkedList);
			}
			
			String title = "保险管理明细";

			String date = createDate(begin_time, end_time);
			String filename = "保险管理.xls";
			String[] secondTitles =  {"序号","保险单号（BX）", "订单号", "车票单号（CP）", "创建时间", "生效日期",
					"支付金额","保险单号" ,"代理商姓名","代理商账号"};
			@SuppressWarnings("unused")
			HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
					secondTitles, list, request, response);
		}
	/**
	 * app--预订管理中导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelExportAppBooking.do")
	public void excelExportAppBooking(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了‘B2C预订管理’的‘导出Excel’");
		String order_id = this.getParam(request, "order_id"); // 订单号
		String merchant_id = this.getParam(request, "user_id"); // 合作商户id
		List<String> statusList = this.getParamToList(request, "order_status"); // 订单状态
		String out_ticket_billno = this.getParam(request, "out_ticket_billno"); // 12306订单号
		String begin_info_time = this.getParam(request, "begin_info_time"); // 查询开始时间
		String end_info_time = this.getParam(request, "end_info_time"); // 查询结束时间
		String queryTime = this.getParam(request, "queryTime"); // 查询的当前时间
		List<String> pay_typeList = this.getParamToList(request, "pay_type"); // 支付方式
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("merchant_id", merchant_id);
		paramMap.put("out_ticket_billno", out_ticket_billno);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("order_status", statusList);
		paramMap.put("pay_type", pay_typeList);
		paramMap.put("queryTime", queryTime);
		List<Map<String,String>> reslist = orderForExcelService.queryAppBookList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(String.valueOf(m.get("create_time")));
			if(m.get("out_ticket_time")!=null){
			linkedList.add(String.valueOf(m.get("out_ticket_time")));}
			else{ linkedList.add("");}
			if(m.get("bx_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("bx_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("ticket_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("ticket_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("buy_money")!=null){
				linkedList.add(String.valueOf(m.get("buy_money")));}
			else{ linkedList.add("0");}
			if(m.get("pay_money")!=null){
				linkedList.add(String.valueOf(m.get("pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("differ")!=null){
				linkedList.add(String.valueOf(m.get("differ")));}
			else{ linkedList.add("0");}
			linkedList.add(m.get("out_ticket_account"));
			linkedList.add(m.get("bank_pay_seq"));
			linkedList.add(m.get("dealer_phone"));
			//保险
			List<Map<String,Object>>info = orderForExcelService.queryBxInfo(m.get("order_id"));
			String str =null;String str1 =null;
			StringBuffer sb = new StringBuffer();
			StringBuffer sb1 = new StringBuffer();
			long bx_count = 0;
			for(int i=0;i<info.size();i++){
				bx_count=bx_count+(Long)info.get(i).get("bx_count");
				long thiscount= (Long)info.get(i).get("bx_count");
				str=info.get(i).get("bx_info").toString();
				str1=thiscount+"*"+str;
				sb=sb.append(str1);
				sb.append("  ");
				sb1=sb1.append(str);
				sb1.append("  ");
			}
			linkedList.add(String.valueOf(bx_count));
			linkedList.add(String.valueOf(sb1));
			linkedList.add(String.valueOf(sb));
			
			String type =m.get("pay_type");
			if( type=="11"){
				linkedList.add("联动优势");
			}else if(type=="22"){
				linkedList.add("支付宝");
			}else if(type=="33"){
				linkedList.add("微信支付");
			}else{
				linkedList.add("");
			}
			linkedList.add(m.get("from_city")+"/"+m.get("to_city"));
			linkedList.add(m.get("train_no"));
			if(m.get("from_time")!=null){
			linkedList.add(String.valueOf(m.get("from_time")));}
			else {linkedList.add(m.get(""));}
			if(m.get("pay_time")!=null){
			linkedList.add(String.valueOf(m.get("pay_time")));}
			else {linkedList.add(m.get(""));}
			list.add(linkedList);
		}
		
		String title = "B2C预定管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "B2C预定管理.xls";
		String[] secondTitles =  { "序号", "订单号", "取票单号","创建时间","出票时间", "保险", "票价", "实际订票价","总价",
				"差价","出票账号","支付流水号","合作商户账号","保险张数","保险类别","保险明细","支付方式","出发/到达","车次","出发时间","支付时间" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * app管理--退款导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelexportForAppRefund.do")
	public void excelexportForAppRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了‘app退票管理’的‘导出Excel’");
		/******************************页面表单参数********************************/
		String begin_create_time = this.getParam(request, "begin_create_time");
		String end_create_time = this.getParam(request, "end_create_time");
		String order_id = this.getParam(request, "order_id");
		String refund_seq = this.getParam(request, "refund_seq");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");
		List<String> refund_type = this.getParamToList(request, "refund_type");
		List<String> refund_status = this.getParamToList(request, "refund_status");
		/******************************Map存储********************************/
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_create_time", begin_create_time);
		paramMap.put("end_create_time", end_create_time);
		paramMap.put("order_id", order_id);
		paramMap.put("refund_seq", refund_seq);
		paramMap.put("refund_12306_seq", refund_12306_seq);
		paramMap.put("refund_type", refund_type);
		paramMap.put("refund_status", refund_status);
		
		List<Map<String,String>> reslist = orderForExcelService.getOrderForExcelAppRefundList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("actual_refund_money")!=null){
				linkedList.add(String.valueOf(m.get("actual_refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("alter_tickets_money")!=null){
				linkedList.add(String.valueOf(m.get("alter_tickets_money")));}
			else{ linkedList.add("0");}
				
			linkedList.add(m.get("create_time"));
			linkedList.add(m.get("bank_username"));
			linkedList.add(m.get("bank_type"));
			linkedList.add(m.get("bank_account"));
			linkedList.add(m.get("bank_openName"));
			linkedList.add(m.get("dealer_phone"));
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("change_ticket_info"));
			list.add(linkedList);
		}
		
		String title = "B2C退款管理明细";

		String date = createDate(begin_create_time, end_create_time);
		String filename = "B2C退款管理.xls";
		String[] secondTitles =  {"序号", "订单号", "退款金额", "实际退款金额", "改签差价","退款订单创建时间", 
				"银行账户姓名", "乘客银行类型", "乘客银行账号", "开户行名称", "注册电话","12306退款流水号","改签明细" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * 对账管理导出Excel
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("excelexportBalanceAccount.do")
	public void excelexportBalanceAccount(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了‘对账管理’的‘导出附件’");
		/******************************页面表单参数********************************/
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		/******************************Map存储********************************/
		HashMap<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		/******************************执行查询********************************/
		List<Map<String,String>> reslist = orderForExcelService.getBalancAccountExcelList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("stream_id"));
			linkedList.add(m.get("cw_seq"));
			linkedList.add(m.get("yw_seq"));
			linkedList.add(m.get("sh_order_id"));
			linkedList.add(m.get("sp_name"));
			linkedList.add(m.get("sp_create_time"));
			linkedList.add(m.get("user_account"));
			linkedList.add(m.get("in_money"));
			linkedList.add(m.get("out_money"));
			linkedList.add(m.get("surplus_money"));
			linkedList.add(m.get("channel"));
			linkedList.add(m.get("business_status"));
			linkedList.add(m.get("remark"));
			linkedList.add(m.get("our_account"));
			String order_id="";
			if(m.get("order_id")!=null && m.get("order_id")!=""){
				order_id=m.get("order_id");
			}
			linkedList.add(order_id);
			if(m.get("getRefund_result")=="2")linkedList.add("匹配");
			if(m.get("getRefund_result")=="3")linkedList.add("未匹配");
			
			list.add(linkedList);
		}
		
		String title = "对账管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "对账管理.xls";
		String[] secondTitles =  {"序号", "账务流水号", "业务流水号","商户订单号","商品名称","发生时间",
				"对方账号","收入金额（+元）","支出金额（元）","账户余额（元）","交易渠道",
				"业务类型","备注","账户","订单号","退款匹配"};
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * 代理改签导出Excel
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("excelElongAlter.do")
	public void excelElongAlter(HttpServletResponse response,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了‘代理改签’的‘导出附件’");
		/******************************页面表单参数********************************/
		String order_id = this.getParam(request, "order_id");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> change_statusList = this.getParamToList(request,"change_status");
		String opt_person = this.getParam(request, "opt_person");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		List<String> change_channel = this.getParamToList(request, "change_channel"); 
		
		/******************************Map存储********************************/
		List<String> change_status = new ArrayList<String>(change_statusList);
		if(order_id.trim().length()<=0){
			request.setAttribute("change_status", change_status.toString());
		}
		List<String> change_notify_status = this.getParamToList(request,"change_notify_status");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("change_status", change_status);
			paramMap.put("change_notify_status", change_notify_status);
			paramMap.put("merchant_id", change_channel);
			paramMap.put("out_ticket_billno", out_ticket_billno);
			paramMap.put("opt_person", opt_person);
		}
		/******************************执行查询********************************/
		List<Map<String,String>> alterlist = orderForExcelService.getElongAlterExcelList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(String.valueOf(m.get("create_time")));
			linkedList.add(String.valueOf(m.get("change_notify_finish_time")));//回调完成时间，用于审核完成时间
			linkedList.add(m.get("from_city")+"/"+m.get("to_city"));
			
			linkedList.add(m.get("train_no")+"/"+m.get("change_train_no"));
			linkedList.add(String.valueOf(m.get("travel_time")).split(" ")[0]+"/"+String.valueOf(m.get("change_travel_time")).split(" ")[0]);
			linkedList.add(String.valueOf(m.get("from_time")).split(" ")[1]+"/"+String.valueOf(m.get("change_from_time")).split(" ")[1]);
			List<Map<String,String>> alterCp = orderForExcelService.getElongAlterCpList(String.valueOf(m.get("change_id")));
			double price=0.0,alter_price=0.0;
			for (Map<String, String> c : alterCp) {
				price += Double.parseDouble(String.valueOf(c.get("buy_money")));
				alter_price += Double.parseDouble(String.valueOf(c.get("change_buy_money")));
			}
			linkedList.add(String.valueOf(price));
			linkedList.add(String.valueOf(price-Double.parseDouble(String.valueOf(m.get("fee")))));
			linkedList.add(String.valueOf(alter_price));
			linkedList.add(String.valueOf(alter_price-price));
			if(alter_price-price >= 0.0){
				linkedList.add(String.valueOf(m.get("totalpricediff")));
			}else{
				linkedList.add("-"+String.valueOf(m.get("totalpricediff")));
			}
			linkedList.add(String.valueOf(alterCp.size()));
			list.add(linkedList);
		}
		
		String title = "代理改签明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "代理改签.xls";
		String[] secondTitles =  {"序号", "订单号", "12306单号","创建时间","审核退款时间","出发/到达","原车次/改签车次","乘车日期/改签乘车日期",
				"发车时间/改签发车时间","原总票价","对账总票价（原总-手续费）","改签后总票价","改签差额","实际差额(差额-手续费)","票数"};
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	
	//携程管理--导出excel
	@RequestMapping("/ctripExportexcel.do")
	public void ctripExportexcel(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了‘携程出票’的‘导出附件’");
		/******************************页面表单参数********************************/
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> statusList = this.getParamToList(request, "go_status");
		String order_id = this.getParam(request, "order_id");
		String ctrip_order_id = this.getParam(request, "ctrip_order_id");
		/******************************Map存储********************************/
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
		paramMap.put("manual_order", "22");
		/******************************执行查询********************************/
		List<Map<String,String>> ctriplist = orderForExcelService.getCtripExcelList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : ctriplist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("ctrip_order_id"));
			
			linkedList.add(m.get("out_ticket_billno"));
			String order_name=m.get("from_city")+"/"+m.get("to_city");
			linkedList.add(order_name);
			linkedList.add(m.get("train_no"));
			linkedList.add(m.get("pay_money"));
			linkedList.add(m.get("buy_money"));
			
			linkedList.add(m.get("create_time"));
			linkedList.add(m.get("out_ticket_time"));
			linkedList.add(m.get("pay_time"));
			linkedList.add(m.get("channel"));
			linkedList.add(m.get("from_time"));
			
			linkedList.add(AcquireVo.getAcquireStatus().get(m.get("order_status")));
			
			linkedList.add(("22".equals(m.get("manual_order"))?"携程出票":"12306出票"));
			list.add(linkedList);
		}
		
		String title = "携程订单明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "携程出票.xls";
		String[] secondTitles = { "序号", "订单号","携程单号","12306单号", "出发/到达", "车次",
				 "票价", "进价", "创建时间 ", "预订时间", "出票时间", "渠道","乘车时间","订单状态","出票渠道"};
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
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
	 * 高铁管家--预订管理中导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelExportGtBooking.do")
	public void excelExportGtBooking(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了高铁管家预定管理导出");
		String order_id = this.getParam(request, "order_id"); // 订单号
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		List<String> statusList = this.getParamToList(request, "order_status"); // 订单状态
		List<String> notify_status = this.getParamToList(request,"notify_status");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno"); // 12306订单号
		String begin_info_time = this.getParam(request, "begin_info_time"); // 查询开始时间
		String end_info_time = this.getParam(request, "end_info_time"); // 查询结束时间
		String queryTime = this.getParam(request, "queryTime"); // 查询的当前时间
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else if(merchant_order_id.trim().length()>0){
			paramMap.put("merchant_order_id", merchant_order_id);
		}else{
			paramMap.put("out_ticket_billno", out_ticket_billno);
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("order_status", statusList);
			paramMap.put("notify_status", notify_status);
			paramMap.put("queryTime", queryTime);
		}
		List<Map<String,String>> reslist = orderForExcelService.queryGtBookList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
//			String[] secondTitles =  {"序号", "订单号", "商户订单号", "取票单号","创建时间","出票时间", "保险", "票价", "实际订票价","总价","差价","出票账号",
//					"支付流水号","保险明细","出发/到达","车次","出发时间","手续费","票数","备注" };
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("merchant_order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(String.valueOf(m.get("create_time")));
			if(m.get("out_ticket_time")!=null){
			linkedList.add(String.valueOf(m.get("out_ticket_time")));}
			else{ linkedList.add("");}
			if(m.get("bx_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("bx_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("ticket_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("ticket_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("buy_money")!=null){
				linkedList.add(String.valueOf(m.get("buy_money")));}
			else{ linkedList.add("0");}
			if(m.get("pay_money")!=null){
				linkedList.add(String.valueOf(m.get("pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("differ")!=null){
				linkedList.add(String.valueOf(m.get("differ")));}
			else{ linkedList.add("0");}
			
			linkedList.add(m.get("out_ticket_account"));
			linkedList.add(m.get("bank_pay_seq"));
			//保险
			List<Map<String,Object>>info = orderForExcelService.queryBxInfo(m.get("order_id"));
			String str =null;String str1 =null;
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<info.size();i++){
				long thiscount= (Long)info.get(i).get("bx_count");
				str=info.get(i).get("bx_info").toString();
				str1=thiscount+"*"+str;
				sb=sb.append(str1);
				sb.append("  ");
			}
			linkedList.add(String.valueOf(sb));
			
			linkedList.add(m.get("from_city")+"/"+m.get("to_city"));
			linkedList.add(m.get("train_no"));
			if(m.get("from_time")!=null){
				linkedList.add(String.valueOf(m.get("from_time")));}
			else{ linkedList.add("");}
				
			if(m.get("merchant_fee")=="19e_bx"){
				linkedList.add("VIP");
			}else if(m.get("merchant_fee")=="merchant_bx"){
				linkedList.add("SVIP");
			}else{
				linkedList.add(m.get("merchant_fee"));
			}
			
			if(m.get("ticket_num")!=null){
			linkedList.add(String.valueOf(m.get("ticket_num")));}
			else {linkedList.add(m.get("0"));}
			
			if("44".equals(m.get("order_status"))){
				linkedList.add("出票成功");
			}else if("45".equals(m.get("order_status"))){
				linkedList.add(AccountVo.getErrorInfos().get(String.valueOf(m.get("error_info")).trim()));
			}else{
				linkedList.add("");
			}
			
			list.add(linkedList);
		}
		
		String title = "高铁管家预定管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "高铁管家预定管理.xls";
		String[] secondTitles =  {"序号", "订单号","商户订单号", "取票单号","创建时间","出票时间", "保险", "票价", "实际订票价","总价","差价","出票账号",
				"支付流水号","保险明细","出发/到达","车次","出发时间","手续费","票数","备注" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	
	/**
	 * 高铁管家管理中退款导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelexportForGtRefund.do")
	public void excelexportForGtRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了高铁管家退票管理导出");
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
		String begin_create_time = this.getParam(request, "begin_create_time");//开始时间
		String end_create_time = this.getParam(request, "end_create_time");//结束时间
		List<String>refund_TypeList = this.getParamToList(request, "refund_type");//退款类型
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> refund_status = new ArrayList<String>(refund_StatusList);
		List<String> notify_status = this.getParamToList(request,"notify_status");
		String opt_person = this.getParam(request, "opt_person");
		/******************查询Map********************/
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		if(refund_status.contains("012")){
			refund_status.add("01");
			refund_status.add("02");
			refund_status.remove("012");
		}
		if(refund_status.contains("456")){
			refund_status.add("04");
			refund_status.add("05");
			refund_status.add("06");
			refund_status.remove("456");
		}
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else if(merchant_order_id.trim().length()>0){
			paramMap.put("merchant_order_id", merchant_order_id);
		}else{
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("begin_create_time", begin_create_time);//开始时间
			paramMap.put("end_create_time", end_create_time);//结束时间
			paramMap.put("opt_person", opt_person);
			paramMap.put("refund_type", refund_TypeList);
			paramMap.put("refund_status", refund_status);
			paramMap.put("notify_status", notify_status);
		}
				
		List<Map<String,String>> reslist = orderForExcelService.queryGtRefundList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
//			String[] secondTitles =  { "序号", "订单号","商户订单号", "车票单号", "退款金额", "实际退款金额","改签差价",
//					"退款订单创建时间", "退款审核时间", "计划退款时间", "12306退款流水号","改签明细","票数"};
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("merchant_order_id"));
			linkedList.add(m.get("cp_id"));
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("actual_refund_money")!=null){
				linkedList.add(String.valueOf(m.get("actual_refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("alter_tickets_money")!=null){
				linkedList.add(String.valueOf(m.get("alter_tickets_money")));}
			else{ linkedList.add("0");}
				
			linkedList.add(m.get("create_time"));
				
			if(m.get("verify_time")!=null){			
				linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
				
			if(m.get("refund_plan_time")!=null){
				linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
			
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("change_ticket_info"));
			linkedList.add(String.valueOf(m.get("ticket_num")));
			list.add(linkedList);
		}
		
		String title = "高铁管家退款管理明细";

		String date = createDate(begin_create_time, end_create_time);
		String filename = "高铁管家退款管理.xls";
		String[] secondTitles =  { "序号", "订单号","商户订单号", "车票单号", "退款金额", "实际退款金额","改签差价",
				"退款订单创建时间", "退款审核时间", "计划退款时间", "12306退款流水号","改签明细","票数"};
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	
	/**
	 * 携程管理--预订管理中导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelExportXcBooking.do")
	public void excelExportXcBooking(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了携程管理预定管理导出");
		String order_id = this.getParam(request, "order_id"); // 订单号
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		List<String> statusList = this.getParamToList(request, "order_status"); // 订单状态
		List<String> notify_status = this.getParamToList(request,"notify_status");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno"); // 12306订单号
		String begin_info_time = this.getParam(request, "begin_info_time"); // 查询开始时间
		String end_info_time = this.getParam(request, "end_info_time"); // 查询结束时间
		String queryTime = this.getParam(request, "queryTime"); // 查询的当前时间
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else if(merchant_order_id.trim().length()>0){
			paramMap.put("merchant_order_id", merchant_order_id);
		}else{
			paramMap.put("out_ticket_billno", out_ticket_billno);
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("order_status", statusList);
			paramMap.put("notify_status", notify_status);
			paramMap.put("queryTime", queryTime);
		}
		List<Map<String,String>> reslist = orderForExcelService.queryXcBookList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("merchant_order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(String.valueOf(m.get("create_time")));
			if(m.get("out_ticket_time")!=null){
			linkedList.add(String.valueOf(m.get("out_ticket_time")));}
			else{ linkedList.add("");}
			if(m.get("bx_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("bx_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("ticket_pay_money")!=null){
				linkedList.add(String.valueOf(m.get("ticket_pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("buy_money")!=null){
				linkedList.add(String.valueOf(m.get("buy_money")));}
			else{ linkedList.add("0");}
			if(m.get("pay_money")!=null){
				linkedList.add(String.valueOf(m.get("pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("differ")!=null){
				linkedList.add(String.valueOf(m.get("differ")));}
			else{ linkedList.add("0");}
			
			linkedList.add(m.get("out_ticket_account"));
			linkedList.add(m.get("bank_pay_seq"));
			//保险
			List<Map<String,Object>>info = orderForExcelService.queryBxInfo(m.get("order_id"));
			String str =null;String str1 =null;
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<info.size();i++){
				long thiscount= (Long)info.get(i).get("bx_count");
				str=info.get(i).get("bx_info").toString();
				str1=thiscount+"*"+str;
				sb=sb.append(str1);
				sb.append("  ");
			}
			linkedList.add(String.valueOf(sb));
			
			linkedList.add(m.get("from_city")+"/"+m.get("to_city"));
			linkedList.add(m.get("train_no"));
			if(m.get("from_time")!=null){
				linkedList.add(String.valueOf(m.get("from_time")));}
			else{ linkedList.add("");}
				
			if(m.get("merchant_fee")=="19e_bx"){
				linkedList.add("VIP");
			}else if(m.get("merchant_fee")=="merchant_bx"){
				linkedList.add("SVIP");
			}else{
				linkedList.add(m.get("merchant_fee"));
			}
			
			if(m.get("ticket_num")!=null){
			linkedList.add(String.valueOf(m.get("ticket_num")));}
			else {linkedList.add(m.get("0"));}
			
			if("44".equals(m.get("order_status"))){
				linkedList.add("出票成功");
			}else if("45".equals(m.get("order_status"))){
				linkedList.add(AccountVo.getErrorInfos().get(String.valueOf(m.get("error_info")).trim()));
			}else{
				linkedList.add("");
			}
			
			list.add(linkedList);
		}
		
		String title = "携程管理预定管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "携程管理预定管理.xls";
		String[] secondTitles =  {"序号", "订单号","商户订单号", "取票单号","创建时间","出票时间", "保险", "票价", "实际订票价","总价","差价","出票账号",
				"支付流水号","保险明细","出发/到达","车次","出发时间","手续费","票数","备注" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	
	/**
	 * 携程管理管理中退款导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelexportForXcRefund.do")
	public void excelexportForXcRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了携程管理退票管理导出");
		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
		String begin_create_time = this.getParam(request, "begin_create_time");//开始时间
		String end_create_time = this.getParam(request, "end_create_time");//结束时间
		List<String>refund_TypeList = this.getParamToList(request, "refund_type");//退款类型
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> refund_status = new ArrayList<String>(refund_StatusList);
		List<String> notify_status = this.getParamToList(request,"notify_status");
		String opt_person = this.getParam(request, "opt_person");
		/******************查询Map********************/
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		if(refund_status.contains("012")){
			refund_status.add("01");
			refund_status.add("02");
			refund_status.remove("012");
		}
		if(refund_status.contains("456")){
			refund_status.add("04");
			refund_status.add("05");
			refund_status.add("06");
			refund_status.remove("456");
		}
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else if(merchant_order_id.trim().length()>0){
			paramMap.put("merchant_order_id", merchant_order_id);
		}else{
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("begin_create_time", begin_create_time);//开始时间
			paramMap.put("end_create_time", end_create_time);//结束时间
			paramMap.put("opt_person", opt_person);
			paramMap.put("refund_type", refund_TypeList);
			paramMap.put("refund_status", refund_status);
			paramMap.put("notify_status", notify_status);
		}
				
		List<Map<String,String>> reslist = orderForExcelService.queryXcRefundList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("merchant_order_id"));
			linkedList.add(m.get("cp_id"));
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("actual_refund_money")!=null){
				linkedList.add(String.valueOf(m.get("actual_refund_money")));}
			else{ linkedList.add("0");}
				
			if(m.get("alter_tickets_money")!=null){
				linkedList.add(String.valueOf(m.get("alter_tickets_money")));}
			else{ linkedList.add("0");}
				
			linkedList.add(m.get("create_time"));
				
			if(m.get("verify_time")!=null){			
				linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
				
			if(m.get("refund_plan_time")!=null){
				linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
			
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("change_ticket_info"));
			linkedList.add(String.valueOf(m.get("ticket_num")));
			list.add(linkedList);
		}
		
		String title = "携程管理退款管理明细";

		String date = createDate(begin_create_time, end_create_time);
		String filename = "携程管理退款管理.xls";
		String[] secondTitles =  { "序号", "订单号","商户订单号", "车票单号", "退款金额", "实际退款金额","改签差价",
				"退款订单创建时间", "退款审核时间", "计划退款时间", "12306退款流水号","改签明细","票数"};
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	/**
	 * 途牛管理--预订管理中导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelExportTuniuBooking.do")
	public void excelExportTuniuBooking(HttpServletRequest request,HttpServletResponse response){
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了途牛管理预定管理导出");
		
		String order_id = this.getParam(request, "order_id"); // 订单号
		List<String> statusList = this.getParamToList(request, "order_status"); // 订单状态
		List<String> notify_status_book = this.getParamToList(request,"notify_status_book");
		List<String> notify_status_out = this.getParamToList(request,"notify_status_out");
		String begin_info_time = this.getParam(request, "begin_info_time"); // 查询开始时间
		String end_info_time = this.getParam(request, "end_info_time");// 查询结束时间
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("order_status", statusList);
			paramMap.put("notify_status_book", notify_status_book);
			paramMap.put("notify_status_out", notify_status_out);
			if(notify_status_book.size()>0 || notify_status_out.size()>0){
				paramMap.put("notify_status", "1");
				List<String> notify_status = new ArrayList<String>();
				notify_status.add("BF");
				if(notify_status_book.size()<=0){
					paramMap.put("notify_status_book", notify_status);
				}
				if(notify_status_out.size()<=0){
					paramMap.put("notify_status_out", notify_status);
				}
			}
		}
		
		List<Map<String,String>> reslist = orderForExcelService.queryTuniuBookExcelList(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			if(m.get("out_ticket_time")!=null){
				linkedList.add(String.valueOf(m.get("out_ticket_time")));}
			else{ linkedList.add("");}
			if(m.get("pay_money")!=null){
				linkedList.add(String.valueOf(m.get("pay_money")));}
			else{ linkedList.add("0");}
			if(m.get("ticket_num")!=null){
				linkedList.add(String.valueOf(m.get("ticket_num")));}
			else {linkedList.add(m.get("0"));}
			list.add(linkedList);
		}
		
		String title = "途牛管理预定管理明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "途牛管理预定管理.xls";
		String[] secondTitles = {"序号", "订单号", "取票单号","出票时间", "实际订票价","票数" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
	}
	
	
	/**
	 * 途牛管理--退票管理中导出Excel
	 * @param request
	 * @param response
	 */
	@RequestMapping("excelexportForTuniuRefund.do")
	public void excelexportForTuniuRefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了途牛退票管理导出");

		/******************查询条件********************/
		String order_id=this.getParam(request, "order_id");//订单ID
		String refund_seq = this.getParam(request, "refund_seq");//退款流水号
		String refund_12306_seq = this.getParam(request, "refund_12306_seq");//12306退款流水单号
		String begin_create_time = this.getParam(request, "begin_create_time");//开始时间
		String end_create_time = this.getParam(request, "end_create_time");//结束时间
		List<String>refund_TypeList = this.getParamToList(request, "refund_type");//退款类型
		List<String>refund_StatusList = this.getParamToList(request, "refund_status");//退款状态
		List<String> refund_status = new ArrayList<String>(refund_StatusList);
		List<String> notify_status = this.getParamToList(request,"notify_status");
		String opt_person = this.getParam(request, "opt_person");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		if(order_id.trim().length()<=0){
			request.setAttribute("refund_status", refund_status.toString());
		}
		if(refund_status.contains("012")){
			refund_status.add("00");
			refund_status.add("01");
			refund_status.add("02");
			refund_status.remove("012");
		}
		if(refund_status.contains("456")){
			refund_status.add("04");
			refund_status.add("05");
			refund_status.add("06");
			refund_status.remove("456");
		}
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
			paramMap.put("refund_seq", refund_seq);
			paramMap.put("refund_12306_seq", refund_12306_seq);
			paramMap.put("begin_create_time", begin_create_time);//开始时间
			paramMap.put("end_create_time", end_create_time);//结束时间
			paramMap.put("opt_person", opt_person);
			paramMap.put("refund_type", refund_TypeList);
			paramMap.put("refund_status", refund_status);
			paramMap.put("notify_status", notify_status);
		}
		/******************分页条件开始********************/
		int totalCount = tuniuRefundService.queryRefundTicketCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", 0);//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount()*totalCount);//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> refundTicketList = tuniuRefundService.queryRefundTicketList(paramMap);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : refundTicketList) {
			
			LinkedList<String> linkedList = new LinkedList<String>();
			
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("cp_id"));
			if (m.get("ticket_num") != null) {
				linkedList.add(String.valueOf(m.get("ticket_num")));
			} else {
				linkedList.add(m.get("0"));
			}
			linkedList.add(TuniuRefundVo.getRefund_Types().get(m.get("refund_type")));
			
			if (m.get("refund_money") != null) {
				linkedList.add(String.valueOf(m.get("refund_money")));
			} else {
				linkedList.add("0");
			}
			if (m.get("detail_refund") != null) {
				linkedList.add(String.valueOf(m.get("detail_refund")));
			} else {
				linkedList.add("0");
			}
			if (m.get("detail_alter") != null) {
				linkedList.add(String.valueOf(m.get("detail_alter")));
			} else {
				linkedList.add("0");
			}
			
			if (m.get("refund_12306_seq") != null) {
				linkedList.add(String.valueOf(m.get("refund_12306_seq")));
			} else {
				linkedList.add("");
			}
			if (m.get("out_ticket_billno") != null) {
				linkedList.add(String.valueOf(m.get("out_ticket_billno")));
			} else {
				linkedList.add("");
			}
			
			if (m.get("create_time") != null) {
				linkedList.add(m.get("create_time"));
			} else {
				linkedList.add("");
			}
			if (m.get("verify_time") != null) {
				linkedList.add(m.get("verify_time"));
			} else {
				linkedList.add("");
			}
			
	
			linkedList.add(TuniuRefundVo.getRefund_Status().get(m.get("refund_status")));
			linkedList.add(TuniuRefundVo.getNotify_Status().get(m.get("notify_status")));
			linkedList.add("途牛");
			linkedList.add(m.get("opt_person"));
			
			
			list.add(linkedList);
		}
		
		String title = "途牛管理退票管理明细";
		String date = createDate(begin_create_time, end_create_time);
		String filename = "途牛管理退票管理.xls";
		String[] secondTitles = {"序号", "订单号", "车票ID","票数", "退款类型","退款金额","实际退款","改签差价","12306退款流水号","12306单号","创建时间","审核时间","退款状态","通知状态","渠道","操作人"};
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
		
	}
	
	
	
}
