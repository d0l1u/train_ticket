 package com.l9e.transaction.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.l9e.util.StringUtil;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.UploadExcelService;
import com.l9e.util.DbContextHolder;

/**
 * 自动下载明细表Excel
 * @author zhangjc02
 *
 */
@Component("UploadExcelJob")
public class UploadExcelJob {
	private static final Logger logger = Logger.getLogger(UploadExcelJob.class);
	@Resource
	private UploadExcelService uploadExcelService;
	
	public void uploadExcel(){
		logger.info("uploadExcelJob "+this.getDate("1")+" start...");
		this.elongBookExcel();
		this.elongRefundExcel();
		this.tongchengExcel();
		this.allBookExcel();
		this.allRefundExcel();
		
		this.gtBookExcel();
		this.gtRefundExcel();
		this.mtBookExcel();
		this.mtRefundExcel();
		this.xcBookExcel();
		this.xcRefundExcel();
		this.tuniuBookExcel();
		this.tuniuRefundExcel();
		
		this.addTcCheckTable();//插入同程对账表--谨慎
		this.addCheckTable();//直接对账表格--谨慎
		this.tuniuExcel();
		this.meituanExcel();
		this.gtExcel();
//		this.test1Excel();//跑数据专用，平时需注掉
		
		logger.info("uploadExcelJob "+this.getDate("1")+" end.");
	}
	
	
	//获取前一天日期
	@SuppressWarnings({  "static-access" })
	private String getDate(String type){
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, -1); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("MM.dd");
		DateFormat df3 = new SimpleDateFormat("MM-dd");
		String querydate=df.format(date);
		if(type=="2"){
			querydate=df2.format(date);
		}else if(type=="3"){
			querydate=df3.format(date);
		}
		return querydate;
	}
	
	//elong 预订表格
	private void elongBookExcel() {
		logger.info("elong book load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		List<String> order_status = new ArrayList<String>();
		order_status.add("33");
		List<String> notify_status = new ArrayList<String>();
		notify_status.add("22");
		List<String> channel = new ArrayList<String>();
		channel.add("elong");
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("begin_time","2015-01-01");
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("order_status",order_status);
		map.put("notify_status",notify_status);
		map.put("channel", channel);
		List<Map<String, String>> reslist = uploadExcelService.queryElongBookCp(map);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("cp_id"));
			linkedList.add(m.get("user_name"));
			linkedList.add(m.get("order_name"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(m.get("out_ticket_time"));
			String buy_money = m.get("buy_money");
			linkedList.add(buy_money);
			linkedList.add("1");
			list.add(linkedList);
		}
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"elongbook.xls"; 
		String type = "11";
		String[] secondTitles = { "序号", "订单号", "姓名" ,"出发/到达","12306订单号", "出票时间","实际订票价","票数"};
		 Integer[] weights = {10,35,10,15,20,35,15,10};
		this.createExcel(path,"elong",type,querydate,secondTitles,weights,list);
		logger.info("elong book load end...");
	}
	
	//elong 退款表格
	private void elongRefundExcel() {
		logger.info("elong refund load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		List<String> refund_status = new ArrayList<String>();
		refund_status.add("11");
		List<String> notify_status = new ArrayList<String>();
		notify_status.add("22");
		List<String> channel = new ArrayList<String>();
		channel.add("elong");
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("begin_time","2015-01-01");
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("refund_status", refund_status);
		map.put("notify_status", notify_status);
		map.put("channel", channel);
		List<Map<String, String>> reslist = uploadExcelService.queryElongRefundTicket(map);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("cp_id"));
			String user_name =uploadExcelService.queryElongRefundTicketName(m.get("cp_id")); 
			linkedList.add(user_name);
			linkedList.add(m.get("order_name"));
			
			if((m.get("verify_time")==null)){
				linkedList.add(m.get("create_time"));
			}else{
				linkedList.add(m.get("verify_time"));
			}
			
			String refund_money = m.get("refund_money");
			if (refund_money != null){
				linkedList.add(refund_money);
			}else{
				linkedList.add("0");
			}
			linkedList.add("1");
			list.add(linkedList);
		}
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"elongrefund.xls";
		String type = "22";
		String[] secondTitles = { "序号", "订单号", "姓名" ,"出发/到达", "创建时间","退款额","票数"};
		 Integer[] weights = {10,35,10,15,35,15,10};
		this.createExcel(path,"elong",type,querydate,secondTitles,weights,list);
		logger.info("elong refund load end...");
	}
	
	//tongcheng 表格
	private void tongchengExcel() {
		logger.info("tongcheng load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("begin_time","2015-01-01");
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("channel", "tongcheng");
		//下载表格list
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		//预订
		List<Map<String, String>> booklist = uploadExcelService.queryTcBookList(map);
		logger.info("tongcheng book load start...+size="+booklist.size());
		for (Map<String, String> bm : booklist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(bm.get("order_id"));
			linkedList.add(bm.get("out_ticket_billno"));
			linkedList.add("出票成功");
			String all_buy_money = bm.get("all_buy_money");
			linkedList.add(all_buy_money);
			linkedList.add(bm.get("ticket_num"));
			linkedList.add(bm.get("out_ticket_time"));
			linkedList.add(bm.get("out_ticket_time"));
			linkedList.add("酷游");
			linkedList.add("0");
			linkedList.add("0");
			list.add(linkedList);
		}
		//退款
		List<Map<String, String>> refundlist = uploadExcelService.queryTcRefundList(map);
		logger.info("tongcheng refund load start...+size="+refundlist.size());

		for (Map<String, String> rm : refundlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(rm.get("order_id"));
			linkedList.add(rm.get("out_ticket_billno"));
			if("22".equals(rm.get("refund_type")) || "33".equals(rm.get("refund_type"))){
				linkedList.add("线下退票");
			}else{
				linkedList.add("线上退票");
			}
			String refund_money = rm.get("refund_money");
			if (refund_money != null){
				linkedList.add(refund_money);
			}else{
				linkedList.add("0");
			}
			linkedList.add("1");
			if((rm.get("verify_time")==null)){
				linkedList.add(rm.get("create_time"));
				linkedList.add(rm.get("create_time"));
			}else{
				linkedList.add(rm.get("verify_time"));
				linkedList.add(rm.get("verify_time"));
			}
			
			linkedList.add("酷游");
			linkedList.add("0");
			linkedList.add("0");
			list.add(linkedList);
		}
		
		//改签
		List<Map<String, String>> alterlist = uploadExcelService.queryTcAlterList(map);
		logger.info("tongcheng alter load start...+size="+alterlist.size());

		for (Map<String, String> am : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(am.get("order_id"));
			linkedList.add(am.get("out_ticket_billno"));
			linkedList.add("改签扣款");
			double alter_price=0.0;
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			for (Map<String, String> c : alterCp) {
				alter_price += Double.parseDouble(String.valueOf(c.get("change_buy_money")));
			}
			linkedList.add(String.valueOf(alter_price));
			linkedList.add("0");
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			linkedList.add("酷游");
			linkedList.add("0");
			linkedList.add("0");
			list.add(linkedList);
		}
		
		for (Map<String, String> am : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(am.get("order_id"));
			linkedList.add(am.get("out_ticket_billno"));
			linkedList.add("改签退票");
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			double price=0.0;
			for (Map<String, String> c : alterCp) {
				if (c.get("buy_money")!= null){
				price += Double.parseDouble(c.get("buy_money"));
				}
			}
			if (am.get("fee")!= null){
				linkedList.add(String.valueOf(price-Double.parseDouble(am.get("fee"))));
			}else{
				linkedList.add(String.valueOf(price-Double.parseDouble("0")));
			}
			linkedList.add("0");
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			linkedList.add("酷游");
			linkedList.add("0");
			linkedList.add("0");
			list.add(linkedList);
		}
		String path ="/data/train/upExcel/"+this.getDate("3")+"tongcheng.xls";
		String type = "33";
		String[] secondTitles = {"序号","供应商订单号","12306订单号","结算类型","结算金额","张数","交易时间","结算归属日期","供应商","余额","异常订单"};
		 Integer[] weights = {10,35,20,10,10,10,35,35,10,10,10};
		this.createExcel(path,"tongcheng",type,querydate,secondTitles,weights,list);
		logger.info("tongcheng load end...");
	}
	
	
	
	//对账表格（各个渠道出票成功）
	private void allBookExcel() {
		logger.info("check allbook load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("begin_time","2015-01-01");
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		List<Map<String, String>> allbook = uploadExcelService.queryAllBook(map);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : allbook) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(String.valueOf(m.get("out_ticket_time")));
			linkedList.add(String.valueOf(m.get("buy_money")));
			linkedList.add(m.get("bank_pay_seq"));
			list.add(linkedList);
		}
		
		List<Map<String, String>> alterlist = uploadExcelService.queryTcAlterList(map);
		logger.info("allbook alter+ load start...+size="+alterlist.size());
		for (Map<String, String> am : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(am.get("order_id"));
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			double price=0.0,alter_price=0.0;
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			for (Map<String, String> c : alterCp) {
				price += Double.parseDouble(String.valueOf(c.get("buy_money")));
				alter_price += Double.parseDouble(String.valueOf(c.get("change_buy_money")));
			}
			linkedList.add(String.valueOf(alter_price));
			linkedList.add(am.get("bank_pay_seq"));
			if(alter_price-price > 0.0){
				list.add(linkedList);
			}
		}
		
		String 	path ="/data/train/upExcel/"+this.getDate("2")+" allbook.xls"; 
//		String 	path ="e:/elongExcel/"+this.getDate("2")+"allbook.xls"; 
		String type = "44";
		String[] secondTitles = { "序号", "订单号", "出票时间","实际订票价","付款流水号"};
		 Integer[] weights = {10,35,35,15,25};
		this.createExcel(path,"check",type,querydate,secondTitles,weights,list);
		logger.info("check allbook load end...");
	}
	
	
	//对账表格（各个渠道退款成功）
	private void allRefundExcel() {
		logger.info("allrefund load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("begin_time","2015-01-01");
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		//19eRefund
		List<Map<String, String>> l9eRefund = uploadExcelService.queryl9eRefundList(map);
		logger.info("19e退款总数："+l9eRefund.size());
		for (Map<String, String> m : l9eRefund) {
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
			
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("create_time"));
			
			if(m.get("verify_time")!=null){			
			linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
			
			if(m.get("refund_plan_time")!=null){
			linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
			
			linkedList.add("19e");
			list.add(linkedList);
		}
		
		//innerRefund
		List<Map<String, String>> innerRefund = uploadExcelService.queryInnerRefundList(map);
		logger.info("内嵌退款总数："+innerRefund.size());
		for (Map<String, String> m : innerRefund) {
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
				
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("create_time"));
				
			if(m.get("verify_time")!=null){			
				linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
				
			if(m.get("refund_plan_time")!=null){
				linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
				
			linkedList.add("内嵌");
			list.add(linkedList);
		}
		
		//extRefund 对外商户
		List<Map<String, String>> extRefund = uploadExcelService.queryExtRefundList(map);
		logger.info("商户退款总数："+extRefund.size());
		for (Map<String, String> m : extRefund) {
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
				
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("create_time"));
				
			if(m.get("verify_time")!=null){			
				linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
				
			if(m.get("refund_plan_time")!=null){
				linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
			
			 linkedList.add("商户");
			list.add(linkedList);
		}
		
		
		//高铁管家
		List<Map<String, String>> gtRefund = uploadExcelService.queryGtRefundList(map);
		logger.info("高铁管家总数："+gtRefund.size());
		for (Map<String, String> m : gtRefund) {
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
				
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("create_time"));
				
			if(m.get("verify_time")!=null){			
				linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
				
			if(m.get("refund_plan_time")!=null){
				linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
			
			 linkedList.add("高铁管家");
			list.add(linkedList);
		}
		
		//携程
		List<Map<String, String>> xcRefund = uploadExcelService.queryXcRefundList(map);
		logger.info("携程总数："+xcRefund.size());
		for (Map<String, String> m : xcRefund) {
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
				
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("create_time"));
				
			if(m.get("verify_time")!=null){			
				linkedList.add(m.get("verify_time"));}
			else{ linkedList.add("");}
				
			if(m.get("refund_plan_time")!=null){
				linkedList.add(m.get("refund_plan_time"));}
			else{ linkedList.add("");}
			
			 linkedList.add("携程");
			list.add(linkedList);
		}
		
		//appRefund B2C
		List<Map<String, String>> appRefund = uploadExcelService.queryAppRefundList(map);
		logger.info("app退款总数："+appRefund.size());
		for (Map<String, String> m : appRefund) {
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
				
			linkedList.add(m.get("refund_12306_seq"));
			linkedList.add(m.get("create_time"));
			linkedList.add("");
			linkedList.add("");
			linkedList.add("app");
			list.add(linkedList);
		}
		
		//qunarRefund 去哪
		List<Map<String, String>> qunarRefund = uploadExcelService.queryQunarRefundList(map);
		logger.info("去哪退款总数："+qunarRefund.size());
		for (Map<String, String> m : qunarRefund) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
			if(m.get("detail_refund")!=null){
				linkedList.add(String.valueOf(m.get("detail_refund")));}
			else{ linkedList.add("0");}
			if(m.get("detail_alter_tickets")!=null){
				linkedList.add(String.valueOf(m.get("detail_alter_tickets")));}
			else{ linkedList.add("0");}
			linkedList.add(m.get("refund_12306_seq"));
			
			linkedList.add(m.get("create_time"));
			linkedList.add(m.get("verify_time"));
			linkedList.add("");
			linkedList.add("去哪");
			list.add(linkedList);
		}
		
		//elongRefund 代理管理
		List<Map<String, String>> elongRefund = uploadExcelService.queryElongRefundList(map);
		logger.info("代理退款总数："+elongRefund.size());
		for (Map<String, String> m : elongRefund) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
			String actual_refund_money = m.get("actual_refund_money");
			String alter_tickets_money = m.get("alter_tickets_money");
			if (actual_refund_money != null)
				linkedList.add(actual_refund_money);
			else
				linkedList.add(m.get("detail_refund"));
			if (alter_tickets_money != null)
				linkedList.add(alter_tickets_money);
			else
				linkedList.add(m.get("detail_alter_tickets"));
			
			linkedList.add(m.get("refund_12306_seq"));
			
			linkedList.add(m.get("create_time"));
			if((m.get("verify_time")==null)){
				linkedList.add(m.get("create_time"));
			}else{
				linkedList.add(m.get("verify_time"));
			}
			linkedList.add("");
			if("tongcheng".equals(m.get("channel")))linkedList.add("同程");
			else if("elong".equals(m.get("channel")))linkedList.add("艺龙");
			list.add(linkedList);
		}
		
		//mtRefund 美团管理
		List<Map<String, String>> mtRefund = uploadExcelService.queryMtRefundList(map);
		logger.info("美团退款总数："+mtRefund.size());
		for (Map<String, String> m : mtRefund) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
			String actual_refund_money = m.get("actual_refund_money");
			String alter_tickets_money = m.get("alter_tickets_money");
			if (actual_refund_money != null)
				linkedList.add(actual_refund_money);
			else
				linkedList.add(m.get("detail_refund"));
			if (alter_tickets_money != null)
				linkedList.add(alter_tickets_money);
			else
				linkedList.add(m.get("detail_alter_tickets"));
			
			linkedList.add(m.get("refund_12306_seq"));
			
			linkedList.add(m.get("create_time"));
			if((m.get("verify_time")==null)){
				linkedList.add(m.get("create_time"));
			}else{
				linkedList.add(m.get("verify_time"));
			}
			linkedList.add("");
			
			linkedList.add("美团");
			list.add(linkedList);
		}
		
		
		//tuniuRefund 途牛管理
		List<Map<String, String>>tuniuRefund = uploadExcelService.queryTuniuRefundList(map);
		logger.info("途牛退款总数："+tuniuRefund.size());
		for (Map<String, String> m : tuniuRefund) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}
			String actual_refund_money = String.valueOf(m.get("actual_refund_money"));
			String alter_tickets_money = String.valueOf(m.get("alter_tickets_money"));
			if (actual_refund_money != null)
				linkedList.add(actual_refund_money);
			else
				linkedList.add(String.valueOf(m.get("detail_refund")));
			if (alter_tickets_money != null)
				linkedList.add(alter_tickets_money);
			else
				linkedList.add(m.get("detail_alter_tickets"));
			
			linkedList.add(m.get("refund_12306_seq"));
			
			linkedList.add(m.get("create_time"));
			if((m.get("verify_time")==null)){
				linkedList.add(m.get("create_time"));
			}else{
				linkedList.add(m.get("verify_time"));
			}
			linkedList.add("");
			
			linkedList.add("途牛");
			list.add(linkedList);
		}
		
		String 	path ="/data/train/upExcel/"+this.getDate("2")+" allrefund.xls"; 
//		String 	path ="e:/elongExcel/"+this.getDate("2")+"allrefund.xls"; 
		String type = "55";
		String[] secondTitles = { "序号", "订单号", "退款金额","实际退款金额","改签差额","12306退款流水号","退款订单创建时间","退款审核时间","计划退款时间","渠道"};
		 Integer[] weights = {10,35,25,25,25,35,35,35,35,15};
		this.createExcel(path,"allrefund",type,querydate,secondTitles,weights,list);
		logger.info("allrefund load end...");
	}
	
	//跑数据专用
	private void test1Excel() {
		logger.info("跑数据 load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate ="";
		for(int i=1;i<90;i++){
			List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
			Calendar theCa = Calendar.getInstance(); 
			theCa.setTime(new Date());  
			theCa.add(theCa.DATE, -i); 
			Date date = theCa.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			querydate=df.format(date);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("begin_time",querydate);
			map.put("end_time", querydate);
			List<Map<String, String>> testList = uploadExcelService.queryTestList(map);
			logger.info("下载："+querydate+"的数据,共"+testList.size());
			if(i == 1){
			String title = "证件类型，姓名，证件号，12306账号";
			LinkedList<String> titleList = new LinkedList<String>();
			titleList.add(title);
			list.add(titleList);
			}
			
			for (Map<String, String> m : testList) {
				List<Map<String, String>> testMap = uploadExcelService.queryTestMap(m.get("key1"));
				for (Map<String, String> z : testMap) {
					LinkedList<String> linkedList = new LinkedList<String>();
					String test2 = String.valueOf(z.get("test2"));
//					证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
					String value = "";
					if("3".equals(test2))value="C";
					else if("4".equals(test2))value="G";
					else if("5".equals(test2))value="B";
					else value="1";
					linkedList.add(value+","+String.valueOf(z.get("test1"))+","+String.valueOf(z.get("test3"))+","+m.get("key2"));
					list.add(linkedList);
				}
			}
			String 	path ="/data/train/upExcel/test/"+"test.txt"; 
//			String 	path ="e:/elongExcel/"+"test.txt"; 
			this.createTxt(path,querydate,list);
		}
		logger.info("跑数据 load end...");
	}
	
	@SuppressWarnings("unchecked")
	private void createExcel(String path,String channel,String type,String querydate,String[] secondTitles,Integer[] weights ,List<LinkedList<String>> list){
		logger.info("uploadExcel  start.");
		try 
		{ 
			WritableWorkbook book=Workbook.createWorkbook(new File(path)); 
			//生成名为“第一页”的工作表，参数0表示这是第一页 
			for(int a=0;a<list.size()/65530+1;a++){
//			for(int a=0;a<list.size()/6+1;a++){
			WritableSheet sheet=book.createSheet("第"+(a+1)+"页",a); 
			sheet.setColumnView(2, 10);
			sheet.setColumnView(2, 10);
			int index = 0;
				/**
		        * 内容格式
		        */
		       jxl.write.WritableFont wfcontent =new jxl.write.WritableFont(WritableFont.ARIAL,10, WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
		       WritableCellFormat contentFromart = new WritableCellFormat(wfcontent);
		       contentFromart.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		       contentFromart.setAlignment(Alignment.CENTRE);
		       jxl.write.WritableFont wfcontent2 =new jxl.write.WritableFont(WritableFont.ARIAL,11, WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
		       WritableCellFormat contentFromart2 = new WritableCellFormat(wfcontent2);
		       contentFromart2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		       contentFromart2.setAlignment(Alignment.CENTRE);
		       contentFromart2.setBackground(Colour.SKY_BLUE);
		   
//			//标题
//			jxl.write.Label titlelabel = new jxl.write.Label(0,index, title, contentFromart);
//			sheet.addCell(titlelabel); index++;
//			//日期
//			jxl.write.Label datelabel = new jxl.write.Label(0,index, querydate, contentFromart);
//			sheet.addCell(datelabel); index++;
			//列表
			for (int i = 0; i < secondTitles.length; i++) {
				sheet.setColumnView(i,weights[i]);
				jxl.write.Label titles = new jxl.write.Label(i,index, secondTitles[i], contentFromart2);
				sheet.addCell(titles); 
			}index++;
			//数据
			double price_all = 0.00;
			int cpNum = 0;
			List<LinkedList<String>> newlist = new ArrayList<LinkedList<String>>();
			//System.out.println("m*mus="+m*mus);
			int n=a*65530;
			for(int x=n;x<n+65530;x++){
				if(x>=list.size()){
					break;
				}
				newlist.add(list.get(x));
			}
			for (LinkedList<String> set : newlist) {
				Iterator it = set.iterator();
				int j = 0;
				jxl.write.Number num = new jxl.write.Number(j++,index, index, contentFromart);
				sheet.addCell(num); 
				while (it.hasNext()) {
					String content = (String) it.next();
					if(type=="11"){
						if(j==6){
							price_all+=Double.parseDouble(content);
						}
					}else if(type=="22"){
						if(j==5){
							price_all+=Double.parseDouble(content);
						}
					}else if(type=="G1" || type=="X1"){
						if(j==5){
							price_all+=Double.parseDouble(content);
						}else if(j==6){
							cpNum+=Integer.parseInt(content);
						}
					}else if(type=="G2" || type=="X2"){
						if(j==4){
							price_all+=Double.parseDouble(content);
						}
					}else if(type=="M1"){
						if(j==4){
							price_all+=Double.parseDouble(content);
						}else if(j==5){
							cpNum+=Integer.parseInt(content);
						}
					}else if(type=="M2"){
						if(j==4){
							price_all+=Double.parseDouble(content);
						}
					}else if(type=="T1"){
						if(j==6){
							price_all+=Double.parseDouble(content);
						}
					}else if(type=="T2"){
						if(j==5){
							price_all+=Double.parseDouble(content);
						}else if(j==6){
							cpNum+=Integer.parseInt(content);
						}
					}
					
					if (content != null) {
						jxl.write.Label contents1 = new jxl.write.Label(j++,index, content, contentFromart);
						sheet.addCell(contents1); 
					} else {
						jxl.write.Label contents2 = new jxl.write.Label(j++,index, "", contentFromart);
						sheet.addCell(contents2); 
					}
				}
				index++;
			}
			if(type=="11" ){
				//合计
				jxl.write.Label total = new jxl.write.Label(0,index, "合计", contentFromart);
				sheet.addCell(total);
				jxl.write.Label totalPrice = new jxl.write.Label(6,index, String.valueOf(price_all), contentFromart);
				sheet.addCell(totalPrice);//总金额
				jxl.write.Label totalNumber = new jxl.write.Label(7,index,String.valueOf(index-1), contentFromart);
				sheet.addCell(totalNumber);//总票数
			}else if(type == "22"){
				//合计
				jxl.write.Label total = new jxl.write.Label(0,index, "合计", contentFromart);
				sheet.addCell(total);
				jxl.write.Label totalPrice = new jxl.write.Label(5,index, String.valueOf(price_all), contentFromart);
				sheet.addCell(totalPrice);//总金额
				jxl.write.Label totalNumber = new jxl.write.Label(6,index,String.valueOf(index-1), contentFromart);
				sheet.addCell(totalNumber);//总票数
			}else if(type == "G1" || type == "X1" ){
				//合计
				jxl.write.Label total = new jxl.write.Label(0,index, "合计", contentFromart);
				sheet.addCell(total);
				jxl.write.Label totalPrice = new jxl.write.Label(5,index, String.valueOf(price_all), contentFromart);
				sheet.addCell(totalPrice);//总金额
				jxl.write.Label totalNumber = new jxl.write.Label(6,index,String.valueOf(cpNum), contentFromart);
				sheet.addCell(totalNumber);//总票数
			}else if(type == "G2" || type == "X2" ){
				//合计
				jxl.write.Label total = new jxl.write.Label(0,index, "合计", contentFromart);
				sheet.addCell(total);
				jxl.write.Label totalPrice = new jxl.write.Label(4,index, String.valueOf(price_all), contentFromart);
				sheet.addCell(totalPrice);//总金额
			}else if(type == "M1"){
				//合计
				jxl.write.Label total = new jxl.write.Label(0,index, "合计", contentFromart);
				sheet.addCell(total);
				jxl.write.Label totalPrice = new jxl.write.Label(4,index, String.valueOf(price_all), contentFromart);
				sheet.addCell(totalPrice);//总金额
				jxl.write.Label totalNumber = new jxl.write.Label(5,index,String.valueOf(cpNum), contentFromart);
				sheet.addCell(totalNumber);//总票数
			}else if(type == "M2"){
				//合计
				jxl.write.Label total = new jxl.write.Label(0,index, "合计", contentFromart);
				sheet.addCell(total);
				jxl.write.Label totalPrice = new jxl.write.Label(4,index, String.valueOf(price_all), contentFromart);
				sheet.addCell(totalPrice);//总金额
			}else if(type == "T1"){
				//合计
				jxl.write.Label total = new jxl.write.Label(0,index, "合计", contentFromart);
				sheet.addCell(total);
				jxl.write.Label totalPrice = new jxl.write.Label(6,index, String.valueOf(price_all), contentFromart);
				sheet.addCell(totalPrice);//总金额
				jxl.write.Label totalNumber = new jxl.write.Label(7,index,String.valueOf(index-1), contentFromart);
				sheet.addCell(totalNumber);//总票数
			}else if(type == "T2"){
				//合计
				jxl.write.Label total = new jxl.write.Label(0,index, "合计", contentFromart);
				sheet.addCell(total);
				jxl.write.Label totalPrice = new jxl.write.Label(5,index, String.valueOf(price_all), contentFromart);
				sheet.addCell(totalPrice);//总金额
				jxl.write.Label totalNumber = new jxl.write.Label(6,index,String.valueOf(cpNum), contentFromart);
				sheet.addCell(totalNumber);//总票数
			}
			}
			
			//写入数据并关闭文件 
			book.write(); 
			book.close(); 
			
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("excel_url", path);
			paramMap.put("excel_name", this.getDate("1"));
			paramMap.put("excel_channel",channel);
			paramMap.put("excel_type",type);
			DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
			if(type !="99"){
				uploadExcelService.insertIntoExcel(paramMap);	
			}
		}catch(Exception e){
			logger.error(e); 
		} 
		logger.info("uploadExcel  end.");
	}




	private static void createCellAndSetNumberVal(HSSFRow row, int num, HSSFCellStyle style, String value) {
		HSSFCell cell = row.createCell((short) num);
		// HSSFCell cell = row.createCell(num);
		if (style != null) {
			cell.setCellStyle(style);
		}
		if (StringUtil.isNotBlank(value)) {
			cell.setCellValue(Double.parseDouble(value));
		}
	}

	private static void createCellAndSetStrVal(HSSFRow row, int num, HSSFCellStyle style, String value) {
		HSSFCell cell = row.createCell((short) num);
		// HSSFCell cell = row.createCell(num);
		if (style != null) {
			cell.setCellStyle(style);
		}
		if (StringUtil.isNotEmpty(value)) {
			HSSFRichTextString richstr = new HSSFRichTextString(value);
			cell.setCellValue(richstr);
		}
	}

	private static HSSFCellStyle createMyCellStyle(HSSFWorkbook workbook,
												   int fontHeight, boolean isBold, boolean isAlignCenter,
												   boolean isVerticalCenter, String fontName) {
		// 设置字体和样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		if (isAlignCenter) {
			style.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		}
		if (isVerticalCenter) {
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
		}
		HSSFFont font = workbook.createFont();
		if (fontName != null) {
			font.setFontName(fontName);
		} else {
			font.setFontName("微软雅黑");
		}
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) fontHeight);
		if (isBold) {
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		style.setFont(font);
		return style;
	}

	@SuppressWarnings("unchecked")
	//使用poi改写jxl,由于jxl导出数据有bug
	private void createExcelNew(String path, String channel, String type, String querydate, String[] secondTitles, Integer[] weights, List<LinkedList<String>> list) {
		logger.info("uploadExcel  start.");
		try {

			HSSFWorkbook book = new HSSFWorkbook();
			//生成名为“第一页”的工作表，参数0表示这是第一页
			for (int a = 0; a < list.size() / 65530 + 1; a++) {

				HSSFSheet sheet = book.createSheet("第" + (a + 1) + "页");
				HSSFCellStyle thirStyle = createMyCellStyle(book, 10, true, true,
						true, null);// 小标题 第四行样式
				HSSFCellStyle contentStyle = createMyCellStyle(book, 10, false,
						true, true, "宋体");// 内容
				HSSFRow row = null;
				int index = 0;


				// 标题栏
				row = sheet.createRow(index);
				row.setHeight((short) 1200);
				for (int i = 0; i < secondTitles.length; i++) {
					createCellAndSetStrVal(row, i, thirStyle, secondTitles[i]);
				}
				index++;

				//数据
				double price_all = 0.00;
				int cpNum = 0;
				List<LinkedList<String>> newlist = new ArrayList<LinkedList<String>>();
				//System.out.println("m*mus="+m*mus);
				int n = a * 65530;
				for (int x = n; x < n + 65530; x++) {
					if (x >= list.size()) {
						//创建新的一页
						break;
					}
					newlist.add(list.get(x));
				}

				for (LinkedList<String> set : newlist) {

					row = sheet.createRow(index);
					row.setHeight((short) 400);
					createCellAndSetNumberVal(row, 0, contentStyle,
							String.valueOf(index));

					Iterator it = set.iterator();
					int j = 1;
					while (it.hasNext()) {
						String content = (String) it.next();       //每行的列处理
						if (type == "11") {
							if (j == 6) {
								price_all += Double.parseDouble(content);
							}
						} else if (type == "22") {
							if (j == 5) {
								price_all += Double.parseDouble(content);
							}
						} else if (type == "G1" || type == "X1") {
							if (j == 5) {
								price_all += Double.parseDouble(content);
							} else if (j == 6) {
								cpNum += Integer.parseInt(content);
							}
						} else if (type == "G2" || type == "X2") {
							if (j == 4) {
								price_all += Double.parseDouble(content);
							}
						} else if (type == "M1") {
							if (j == 4) {
								price_all += Double.parseDouble(content);
							} else if (j == 5) {
								cpNum += Integer.parseInt(content);
							}
						} else if (type == "M2") {
							if (j == 4) {
								price_all += Double.parseDouble(content);
							}
						} else if (type == "T1") {
							if (j == 6) {
								price_all += Double.parseDouble(content);
							}
						} else if (type == "T2") {
							if (j == 5) {
								price_all += Double.parseDouble(content);
							} else if (j == 6) {
								cpNum += Integer.parseInt(content);
							}
						}

						if (content != null) {
							createCellAndSetStrVal(row, j++, contentStyle, content);
						} else {
							createCellAndSetStrVal(row, j++, contentStyle, "");
						}

					}

					index++;
				}


				if (type == "11") {
					//合计
                  /*  Label total = new Label(0, index, "合计", contentFromart);
                    sheet.addCell(total);
                    Label totalPrice = new Label(6, index, String.valueOf(price_all), contentFromart);
                    sheet.addCell(totalPrice);//总金额
                    Label totalNumber = new Label(7, index, String.valueOf(index - 1), contentFromart);
                    sheet.addCell(totalNumber);//总票数*/

					createCellAndSetStrVal(row, 0, contentStyle, "合计");
					createCellAndSetStrVal(row, 6, contentStyle, String.valueOf(price_all));
					createCellAndSetStrVal(row, 7, contentStyle, String.valueOf(index - 1));



				} else if (type == "22") {
					//合计
                 /*   Label total = new Label(0, index, "合计", contentFromart);
                    sheet.addCell(total);
                    Label totalPrice = new Label(5, index, String.valueOf(price_all), contentFromart);
                    sheet.addCell(totalPrice);//总金额
                    Label totalNumber = new Label(6, index, String.valueOf(index - 1), contentFromart);
                    sheet.addCell(totalNumber);//总票数*/

					createCellAndSetStrVal(row, 0, contentStyle, "合计");
					createCellAndSetStrVal(row, 5, contentStyle, String.valueOf(price_all));
					createCellAndSetStrVal(row, 6, contentStyle, String.valueOf(index - 1));


				} else if (type == "G1" || type == "X1") {
					//合计
                   /* Label total = new Label(0, index, "合计", contentFromart);
                    sheet.addCell(total);
                    Label totalPrice = new Label(5, index, String.valueOf(price_all), contentFromart);
                    sheet.addCell(totalPrice);//总金额
                    Label totalNumber = new Label(6, index, String.valueOf(cpNum), contentFromart);
                    sheet.addCell(totalNumber);//总票数*/

					createCellAndSetStrVal(row, 0, contentStyle, "合计");
					createCellAndSetStrVal(row, 5, contentStyle, String.valueOf(price_all));
					createCellAndSetStrVal(row, 6, contentStyle, String.valueOf(index - 1));


				} else if (type == "G2" || type == "X2") {
					//合计
                /*    Label total = new Label(0, index, "合计", contentFromart);
                    sheet.addCell(total);
                    Label totalPrice = new Label(4, index, String.valueOf(price_all), contentFromart);
                    sheet.addCell(totalPrice);//总金额*/

					createCellAndSetStrVal(row, 0, contentStyle, "合计");
					createCellAndSetStrVal(row, 4, contentStyle, String.valueOf(price_all));


				} else if (type == "M1") {
					//合计
             /*       Label total = new Label(0, index, "合计", contentFromart);
                    sheet.addCell(total);
                    Label totalPrice = new Label(4, index, String.valueOf(price_all), contentFromart);
                    sheet.addCell(totalPrice);//总金额
                    Label totalNumber = new Label(5, index, String.valueOf(cpNum), contentFromart);
                    sheet.addCell(totalNumber);//总票数*/

					createCellAndSetStrVal(row, 0, contentStyle, "合计");
					createCellAndSetStrVal(row, 4, contentStyle, String.valueOf(price_all));
					createCellAndSetStrVal(row, 5, contentStyle, String.valueOf(index - 1));


				} else if (type == "M2") {
					//合计
                  /*  Label total = new Label(0, index, "合计", contentFromart);
                    sheet.addCell(total);
                    Label totalPrice = new Label(4, index, String.valueOf(price_all), contentFromart);
                    sheet.addCell(totalPrice);//总金额*/

					createCellAndSetStrVal(row, 0, contentStyle, "合计");
					createCellAndSetStrVal(row, 4, contentStyle, String.valueOf(price_all));



				} else if (type == "T1") {
					//合计
                 /*   Label total = new Label(0, index, "合计", contentFromart);
                    sheet.addCell(total);
                    Label totalPrice = new Label(6, index, String.valueOf(price_all), contentFromart);
                    sheet.addCell(totalPrice);//总金额
                    Label totalNumber = new Label(7, index, String.valueOf(index - 1), contentFromart);
                    sheet.addCell(totalNumber);//总票数*/


					createCellAndSetStrVal(row, 0, contentStyle, "合计");
					createCellAndSetStrVal(row, 6, contentStyle, String.valueOf(price_all));
					createCellAndSetStrVal(row, 7, contentStyle, String.valueOf(index - 1));



				} else if (type == "T2") {
					//合计
                 /*   Label total = new Label(0, index, "合计", contentFromart);
                    sheet.addCell(total);
                    Label totalPrice = new Label(5, index, String.valueOf(price_all), contentFromart);
                    sheet.addCell(totalPrice);//总金额
                    Label totalNumber = new Label(6, index, String.valueOf(cpNum), contentFromart);
                    sheet.addCell(totalNumber);//总票数*/


					createCellAndSetStrVal(row, 0, contentStyle, "合计");
					createCellAndSetStrVal(row, 5, contentStyle, String.valueOf(price_all));
					createCellAndSetStrVal(row, 6, contentStyle, String.valueOf(index - 1));

				}

				//设置列宽
				for (short i = 0; i < secondTitles.length; i++) {
					sheet.autoSizeColumn(i);
				}


			}



			//写入数据并关闭文件
        /*    book.write();
            book.close();*/

			FileOutputStream fileOutputStream=new FileOutputStream(new File(path));
			book.write(fileOutputStream);
			fileOutputStream.close();

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("excel_url", path);
			paramMap.put("excel_name", this.getDate("1"));
			paramMap.put("excel_channel", channel);
			paramMap.put("excel_type", type);
			DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
			if (type != "99") {
				uploadExcelService.insertIntoExcel(paramMap);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("uploadExcel  end.");
	}





	//写入txt格式文件
	@SuppressWarnings("unchecked")
	private void createTxt(String path,String querydate,List<LinkedList<String>> list){
		logger.info("uploadTxt  start.");
		try 
		{ 
			 OutputStreamWriter pw = null;//定义一个流
			 pw = new OutputStreamWriter(new FileOutputStream(path,true),"UTF-8");//确认流的输出文件和编码格式，此过程创建了“test.txt”实例
//			 String title = "证件类型，姓名，证件号，12306账号";
//			 pw.write(title+"\r\n");   
			 for(LinkedList<String> aa:list){
				 Iterator it = aa.iterator();
					while (it.hasNext()) {
						String content = (String) it.next();
						pw.write(content+"\r\n");          
					}
			 }
			 pw.close();
		}catch (Exception e) {
			logger.error(e); 
		}
		logger.info("uploadTxt  end.");
		
	}
	
	//插入tongcheng对账表格
	private void addTcCheckTable() {
		logger.info("插入tongcheng对账表格 start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("channel", "tongcheng");
		//对账表格list
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		//settlement_type结算类型 1：出票成功；2：出票不成功；3：线上退票；4：线下退票；5：改签退票；6：赔偿退款；7：异常扣款；
		//8：异常加款；9：手续费；10：改签扣款；11：线下改签；12：财务充值；13：期末余额；14：期初余额；
		//预订
		List<Map<String, String>> booklist = uploadExcelService.queryTcBookList(map);
		logger.info("插入tongcheng对账表格book load start...+size="+booklist.size());
		for (Map<String, String> bm : booklist) {
			Map<String, String> checkMap = new HashMap<String, String>();
			checkMap.put("order_id", bm.get("order_id"));
			checkMap.put("out_ticket_billno", bm.get("out_ticket_billno"));
			checkMap.put("settlement_type","1");
			checkMap.put("amount", String.valueOf(bm.get("all_buy_money")));
			checkMap.put("quantity", bm.get("ticket_num"));
       		checkMap.put("trade_date", bm.get("out_ticket_time"));
       		checkMap.put("settlement_date", bm.get("out_ticket_time"));
       		checkMap.put("channel", "10");
       		checkMap.put("account_balance", "0");
       		checkMap.put("create_time", "now()");
       		checkMap.put("opt_person", "check-job");
			list.add(checkMap);
		}
		//退款
		List<Map<String, String>> refundlist = uploadExcelService.queryTcRefundList(map);
		logger.info("插入tongcheng对账表格 refund load start...+size="+refundlist.size());
		for (Map<String, String> rm : refundlist) {
			Map<String, String> checkMap = new HashMap<String, String>();
			checkMap.put("order_id", rm.get("order_id"));
			checkMap.put("out_ticket_billno", rm.get("out_ticket_billno"));
			if("22".equals(rm.get("refund_type")) || "33".equals(rm.get("refund_type"))){
				checkMap.put("settlement_type","4");
			}else{
				checkMap.put("settlement_type","3");
			}
			String refund_money = rm.get("refund_money");
			if (refund_money != null){
				checkMap.put("amount", refund_money);
			}else{
				checkMap.put("amount", "0");
			}
			checkMap.put("quantity", "1");
			if((rm.get("verify_time")==null)){
				checkMap.put("trade_date", rm.get("create_time"));
				checkMap.put("settlement_date", rm.get("create_time"));
			}else{
				checkMap.put("trade_date", rm.get("verify_time"));
				checkMap.put("settlement_date", rm.get("verify_time"));
			}
       		checkMap.put("channel", "10");
       		checkMap.put("account_balance", "0");
       		checkMap.put("create_time", "now()");
       		checkMap.put("opt_person", "check-job");
			list.add(checkMap);
		}
		
		//改签
		List<Map<String, String>> alterlist = uploadExcelService.queryTcAlterList(map);
		logger.info("插入tongcheng对账表格 alter load start...+size="+alterlist.size());
		for (Map<String, String> am : alterlist) {
			Map<String, String> checkMap = new HashMap<String, String>();
			checkMap.put("order_id", am.get("order_id"));
			checkMap.put("out_ticket_billno", am.get("out_ticket_billno"));
			checkMap.put("settlement_type","10");
			double alter_price=0.0;
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			for (Map<String, String> c : alterCp) {
				alter_price += Double.parseDouble(String.valueOf(c.get("change_buy_money")));
			}
			checkMap.put("amount", String.valueOf(alter_price));
			checkMap.put("quantity","0");
       		checkMap.put("trade_date", String.valueOf(am.get("change_notify_finish_time")));
       		checkMap.put("settlement_date", String.valueOf(am.get("change_notify_finish_time")));
       		checkMap.put("channel", "10");
       		checkMap.put("account_balance", "0");
       		checkMap.put("create_time", "now()");
       		checkMap.put("opt_person", "check-job");
			list.add(checkMap);
		}
		
		for (Map<String, String> am : alterlist) {
			Map<String, String> checkMap = new HashMap<String, String>();
			checkMap.put("order_id", am.get("order_id"));
			checkMap.put("out_ticket_billno", am.get("out_ticket_billno"));
			checkMap.put("settlement_type","5");
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			double price=0.0;
			for (Map<String, String> c : alterCp) {
				if (c.get("buy_money")!= null){
				price += Double.parseDouble(c.get("buy_money"));
				}
			}
			if (am.get("fee")!= null){
				checkMap.put("amount",String.valueOf(price-Double.parseDouble(am.get("fee"))));
			}else{
				checkMap.put("amount",String.valueOf(price-Double.parseDouble("0")));
			}
			checkMap.put("quantity","0");
       		checkMap.put("trade_date", String.valueOf(am.get("change_notify_finish_time")));
       		checkMap.put("settlement_date", String.valueOf(am.get("change_notify_finish_time")));
       		checkMap.put("channel", "10");
       		checkMap.put("account_balance", "0");
       		checkMap.put("create_time", "now()");
       		checkMap.put("opt_person", "check-job");
			list.add(checkMap);
		}
		
		logger.info("addList.size()="+list.size());
		for(Map<String, String> addMap :list){
			String orderId =addMap.get("order_id");
			if((!orderId.equals(""))&&orderId!=null){
				try {
					uploadExcelService.tcAddTcCheck(addMap);
				} catch (Exception e) {
					logger.error("插入elong_refundstation失败order_id："+orderId+":"+e);
				}
			}
		}
		logger.info("插入tongcheng对账表格 end...");
	}
		
		

		
	//高铁  预订表格
	private void gtBookExcel() {
		logger.info("高铁预订表格 load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		List<String> order_status = new ArrayList<String>();
		order_status.add("44");
		List<String> notify_status = new ArrayList<String>();
		notify_status.add("22");
//		List<String> channel = new ArrayList<String>();
//		channel.add("301030");
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("begin_time","2015-01-01");
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("order_status",order_status);
		map.put("notify_status",notify_status);
//		map.put("channel", channel);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		//预订
		List<Map<String, String>> booklist = uploadExcelService.queryGtBookList(map);
		logger.info("高铁预订load start...+size="+booklist.size());
		for (Map<String, String> bm : booklist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(bm.get("order_id"));
			linkedList.add(bm.get("merchant_order_id"));
			linkedList.add(bm.get("out_ticket_time"));
			linkedList.add(bm.get("notify_finish_time"));
			String all_buy_money = bm.get("all_buy_money");
			linkedList.add(all_buy_money);
			linkedList.add(bm.get("ticket_num"));
			list.add(linkedList);
		}
//		String 	path ="e:/elongExcel/"+this.getDate("2")+"gtbook.xls"; 	
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"gtbook.xls"; 
		String type = "G1";
		String[] secondTitles = { "序号", "订单号","商户订单号","出票时间","通知完成时间","实际订票总价","票数"};
		 Integer[] weights = {10,30,30,30,30,15,10};
		this.createExcel(path,"301030",type,querydate,secondTitles,weights,list);
		logger.info("高铁预订load end...");
	}
	
	
	//高铁 退款表格
	private void gtRefundExcel() {
		logger.info("高铁退款表格 load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		List<String> refund_status = new ArrayList<String>();
		refund_status.add("33");
		List<String> notify_status = new ArrayList<String>();
		notify_status.add("33");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("refund_status",refund_status);
		map.put("notify_status",notify_status);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		List<Map<String, String>> gtRefund = uploadExcelService.queryGtRefundNotifyList(map);
		logger.info("高铁管家总数："+gtRefund.size());
		for (Map<String, String> m : gtRefund) {
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
				
			linkedList.add(m.get("create_time"));
				
			if(m.get("notify_finish_time")!=null){
				linkedList.add(m.get("notify_finish_time"));}
			else{ linkedList.add("");}
			
			list.add(linkedList);
		}
//		String 	path ="e:/elongExcel/"+this.getDate("2")+"gtrefund.xls"; 
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"gtrefund.xls"; 
		String type = "G2";
		String[] secondTitles = { "序号", "订单号", "商户订单号" ,"车票号","退款金额", "实际退款金额","创建时间","通知完成时间"};
		 Integer[] weights = {10,30,30,30,15,15,30,30};
		this.createExcel(path,"301030",type,querydate,secondTitles,weights,list);
		logger.info("高铁管家退款load end...");
	}
	
	//美团  预订表格
	private void mtBookExcel() {
		logger.info("美团预订表格 load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		List<String> order_status = new ArrayList<String>();
		order_status.add("33");
		List<String> channel = new ArrayList<String>();
		channel.add("meituan");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("order_status",order_status);
		map.put("channel", channel);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		//预订
		List<Map<String, String>> booklist = uploadExcelService.queryMtBookList(map);
		logger.info("美团预订load start...+size="+booklist.size());
		for (Map<String, String> bm : booklist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(bm.get("order_id"));
			linkedList.add(bm.get("out_ticket_billno"));
			linkedList.add("出票");
			String all_buy_money = bm.get("all_buy_money");
			linkedList.add(all_buy_money);
			linkedList.add(bm.get("ticket_num"));
			linkedList.add(bm.get("out_ticket_time"));
			list.add(linkedList);
		}
		
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"mtbook.xls"; 
		String type = "M1";
		String[] secondTitles = { "序号", "订单号","12306单号","订单类型","实际订票总价","票数","出票时间"};
		 Integer[] weights = {10,35,25,25,35,10,30};
		this.createExcel(path,"meituan",type,querydate,secondTitles,weights,list);
		logger.info("美团预订load end...");
	}
	
	
	//美团  退款表格
	private void mtRefundExcel() {
		logger.info("美团退款表格 load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		List<String> order_status = new ArrayList<String>();
		order_status.add("33");
//		List<String> notify_status = new ArrayList<String>();
//		notify_status.add("22");
		List<String> channel = new ArrayList<String>();
		channel.add("meituan");
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("begin_time","2015-01-01");
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("order_status",order_status);
//		map.put("notify_status",notify_status);
		map.put("channel", channel);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		List<Map<String, String>> mtRefund = uploadExcelService.queryMtRefundList(map);
		logger.info("美团退款总数："+mtRefund.size());
		for (Map<String, String> m : mtRefund) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add("退票");
			if(m.get("refund_money")!=null){
				linkedList.add(String.valueOf(m.get("refund_money")));
			}else{ 
				linkedList.add("0");
			}
//			String actual_refund_money = m.get("actual_refund_money");
//			if (actual_refund_money != null)
//				linkedList.add(actual_refund_money);
//			else
//				linkedList.add(m.get("detail_refund"));
			linkedList.add("1");
			linkedList.add(m.get("verify_time"));
			
			list.add(linkedList);
		}
		
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"mtrefund.xls"; 
		String type = "M2";
		String[] secondTitles = { "序号", "订单号","12306单号","订单类型","退款金额","票数","退款完成时间"};
		 Integer[] weights = {10,35,25,15,15,10,30};
		this.createExcel(path,"meituan",type,querydate,secondTitles,weights,list);
		logger.info("美团退款load end...");
	}
	
	
	//携程预订表格
	private void xcBookExcel() {
		logger.info("携程表格 load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		List<String> order_status = new ArrayList<String>();
		order_status.add("44");
		List<String> notify_status = new ArrayList<String>();
		notify_status.add("22");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("order_status",order_status);
		map.put("notify_status",notify_status);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		//预订
		List<Map<String, String>> booklist = uploadExcelService.queryXcBookList(map);
		logger.info("携程load start...+size="+booklist.size());
		for (Map<String, String> bm : booklist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(bm.get("order_id"));
			linkedList.add(bm.get("merchant_order_id"));
			linkedList.add(bm.get("out_ticket_time"));
			linkedList.add(bm.get("notify_finish_time"));
			String all_buy_money = bm.get("all_buy_money");
			linkedList.add(all_buy_money);
			linkedList.add(bm.get("ticket_num"));
			list.add(linkedList);
		}
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"xcbook.xls"; 
		String type = "X1";
		String[] secondTitles = { "序号", "订单号","商户订单号","出票时间","通知完成时间","实际订票总价","票数"};
		 Integer[] weights = {10,30,30,30,30,15,10};
		this.createExcel(path,"301031",type,querydate,secondTitles,weights,list);
		logger.info("携程load end...");
	}
	
	
	//携程 退款表格
	private void xcRefundExcel() {
		logger.info("携程退款表格 load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		List<String> refund_status = new ArrayList<String>();
		refund_status.add("33");
		List<String> notify_status = new ArrayList<String>();
		notify_status.add("33");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("refund_status",refund_status);
		map.put("notify_status",notify_status);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		List<Map<String, String>> gtRefund = uploadExcelService.queryXcRefundNotifyList(map);
		logger.info("携程总数："+gtRefund.size());
		for (Map<String, String> m : gtRefund) {
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
				
			linkedList.add(m.get("create_time"));
				
			if(m.get("notify_finish_time")!=null){
				linkedList.add(m.get("notify_finish_time"));}
			else{ linkedList.add("");}
			
			list.add(linkedList);
		}
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"xcrefund.xls"; 
		String type = "X2";
		String[] secondTitles = { "序号", "订单号", "商户订单号" ,"车票号","退款金额", "实际退款金额","创建时间","通知完成时间"};
		 Integer[] weights = {10,30,30,30,15,15,30,30};
		this.createExcel(path,"301031",type,querydate,secondTitles,weights,list);
		logger.info("携程退款load end...");
	}
	
	
	//添加到对账表格
	private void addCheckTable() {
		logger.info("添加到对账表格 load start...");
		String querydate = this.getDate("1");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		List<Map<String, String>> allbook = uploadExcelService.queryAllBook(map);
		for (Map<String, String> m : allbook) {
			try {
				uploadExcelService.addCheckOrderInfo(m);
			} catch (Exception e) {
				logger.info("出票插入表格失败！order_id:"+m.get("order_id")+"|e："+e);
			}
		}
		List<Map<String, String>> alterlist = uploadExcelService.queryCheckTcAlter(map);
		for (Map<String, String> am : alterlist) {
			try {
				uploadExcelService.addCheckOrderInfo(am);
			} catch (Exception e) {
				logger.info("改签插入表格失败！order_id:"+am.get("order_id")+"|e："+e);
			}
		}
		logger.info("添加到对账表格 load end...");
	}
	
	//途牛  预订表格
	public void tuniuBookExcel() {
		logger.info("途牛预订表格 load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1");
		List<String> order_status = new ArrayList<String>();
		order_status.add("33");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("order_status",order_status);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		//预订
		List<Map<String, String>> booklist = uploadExcelService.queryTuniuBookList(map);
		logger.info("途牛预订load start...+size="+booklist.size());
//		String bfOrderId = "";
		for (Map<String, String> bm : booklist) {
			LinkedList<String> linkedList = new LinkedList<String>();
//			if(bfOrderId.equals(bm.get("order_id"))){
//				linkedList.add("-");
//			}else{
//				bfOrderId=bm.get("order_id");
				linkedList.add(bm.get("order_id"));
//			}
			linkedList.add(bm.get("out_ticket_billno"));
			linkedList.add(bm.get("user_name"));
			linkedList.add(bm.get("out_ticket_time"));
			linkedList.add(String.valueOf(bm.get("all_buy_money")));
			linkedList.add(String.valueOf(bm.get("buy_money")));
			linkedList.add(String.valueOf(bm.get("ticket_num")));
			list.add(linkedList);
		}
				
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"tuniubook.xls"; 
		String type = "T1";
		String[] secondTitles = { "序号", "订单号","12306单号","乘客姓名","出票时间","实际订票总价","实际订票单价","票数"};
		 Integer[] weights = {10,25,25,15,25,25,25,10};
		this.createExcel(path,"tuniu",type,querydate,secondTitles,weights,list);
		logger.info("途牛预订load end...");
	}
	
	//途牛 退款表格
	private void tuniuRefundExcel() {
		logger.info("途牛退款 load start...");
		String querydate = this.getDate("1");
		List<String> refund_status = new ArrayList<String>();
		refund_status.add("11");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("refund_status", refund_status);
		List<Map<String, String>> reslist = uploadExcelService.queryTuniuRefundTicket(map);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(m.get("user_name"));
			linkedList.add(String.valueOf(m.get("verify_time")));
			linkedList.add(m.get("refund_money"));
			linkedList.add("1");
			list.add(linkedList);
		}
		String 	path ="/data/train/upExcel/"+this.getDate("2")+"tuniurefund.xls";
		String type = "T2";
		String[] secondTitles = { "序号", "订单号","12306单号","乘客姓名","退票时间","退款金额","票数"};
		 Integer[] weights = {10,25,25,25,25,25,15};
		this.createExcel(path,"tuniu",type,querydate,secondTitles,weights,list);
		logger.info("途牛退款load end...");
	}
	//途牛 表格
	public void tuniuExcel() {
		logger.info("tuniu load start..."); 
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1"); 
		 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate); 
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		//预订
		List<Map<String, String>> booklist = uploadExcelService.queryTuniuBookList(map);
		logger.info("途牛预订load start...+size="+booklist.size());
//		String bfOrderId = "";
		for (Map<String, String> bm : booklist) {
			LinkedList<String> linkedList = new LinkedList<String>(); 
			linkedList.add(bm.get("order_id"));
			linkedList.add(bm.get("out_ticket_billno"));
			linkedList.add(bm.get("user_name"));
			linkedList.add(bm.get("out_ticket_time"));
//			linkedList.add(String.valueOf(bm.get("all_buy_money")));
			linkedList.add(String.valueOf(bm.get("buy_money")));
			linkedList.add(String.valueOf(bm.get("ticket_num")));
			linkedList.add("出票");
			linkedList.add("0");
			list.add(linkedList);
		} 
		
		//退款 
		List<Map<String, String>> reslist = uploadExcelService.queryTuniuRefundTicket(map); 
		logger.info("tuniu refund load start...+size="+reslist.size());
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(m.get("user_name"));
			linkedList.add(String.valueOf(m.get("verify_time")));
			linkedList.add("-"+m.get("refund_money"));
			linkedList.add("1");
			linkedList.add("退票");
			linkedList.add("0");
			list.add(linkedList);
		} 
		
		map.put("channel", "tuniu");
		//改签
		List<Map<String, String>> alterlist = uploadExcelService.queryTcAlterList(map);
		logger.info("tuniu alter load start...+size="+alterlist.size());

		for (Map<String, String> am : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(am.get("order_id"));
			linkedList.add(am.get("out_ticket_billno")); 
			double alter_price=0.0;
			String userNameList = "";
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			
			for (Map<String, String> c : alterCp) {
				alter_price += Double.parseDouble(String.valueOf(c.get("change_buy_money")));
				userNameList += c.get("user_name")+"  ";
			}
			linkedList.add(userNameList);
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			linkedList.add(String.valueOf(alter_price));
			linkedList.add("0");
			linkedList.add("改签扣款");
			linkedList.add("0");
			list.add(linkedList);
		}
		
		for (Map<String, String> am : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(am.get("order_id"));
			linkedList.add(am.get("out_ticket_billno")); 
			
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			double price=0.0;
			String userNameList = "";
			for (Map<String, String> c : alterCp) {
				if (c.get("buy_money")!= null){
				price += Double.parseDouble(c.get("buy_money"));
				}
				userNameList += c.get("user_name")+"  ";
			}
			linkedList.add(userNameList);
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			if (am.get("fee")!= null){
				linkedList.add("-"+String.valueOf(price-Double.parseDouble(am.get("fee"))));
			}else{
				linkedList.add("-"+String.valueOf(price-Double.parseDouble("0")));
			} 
			linkedList.add("0");
			linkedList.add("改签退票");
			linkedList.add("0");
			list.add(linkedList);
		}
		
		
		String path ="/data/train/upExcel/"+this.getDate("2")+"tuniu.xls";
		String type = "T3";
		String[] secondTitles = {"序号","订单号","12306订单号","乘客姓名","交易时间","金额","票数","备注","手续费"};
		Integer[] weights = {10,30,20,20,30,15,10,20,10};
		this.createExcelNew(path,"tuniu",type,querydate,secondTitles,weights,list);
		logger.info("tuniu load end...");
	}	
	
	
	//meituan 表格
	public void meituanExcel() {
		logger.info("meituan load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1"); 
		 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",querydate);
		map.put("end_time", querydate); 
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		//预订
		List<String> order_status = new ArrayList<String>();
		order_status.add("33");
		List<String> channel = new ArrayList<String>();
		channel.add("meituan"); 
		map.put("order_status",order_status);
		map.put("channel", channel); 
		//预订
		List<Map<String, String>> booklist = uploadExcelService.queryMtBookList(map);
		logger.info("美团预订load start...+size="+booklist.size());
		for (Map<String, String> bm : booklist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(bm.get("order_id"));
			linkedList.add(bm.get("out_ticket_billno"));
			linkedList.add("出票");
			String all_buy_money = bm.get("all_buy_money");
			linkedList.add(all_buy_money);
			linkedList.add(bm.get("ticket_num"));
			linkedList.add(bm.get("out_ticket_time"));
			list.add(linkedList);
		}
		//退票
		List<Map<String, String>> mtRefund = uploadExcelService.queryMtRefundList(map);
		logger.info("美团退款总数："+mtRefund.size());
		for (Map<String, String> m : mtRefund) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add("退票");
			if(m.get("refund_money")!=null){
				linkedList.add("-"+String.valueOf(m.get("refund_money")));
			}else{ 
				linkedList.add("0");
			} 
			linkedList.add("1");
			linkedList.add(m.get("verify_time")); 
			list.add(linkedList);
		} 
		
		HashMap<String, Object> newMap = new HashMap<String, Object>();
		newMap.put("begin_time",querydate);
		newMap.put("end_time", querydate); 
		newMap.put("channel", "meituan");
		//改签
		List<Map<String, String>> alterlist = uploadExcelService.queryTcAlterList(newMap);
		logger.info("meituan alter load start...+size="+alterlist.size());

		for (Map<String, String> am : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(am.get("order_id"));
			linkedList.add(am.get("out_ticket_billno")); 
			linkedList.add("改签扣款");
			double alter_price=0.0;
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			for (Map<String, String> c : alterCp) {
				alter_price += Double.parseDouble(String.valueOf(c.get("change_buy_money")));
			}
			linkedList.add(String.valueOf(alter_price));
			linkedList.add("0");
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			list.add(linkedList);
		}
		
		for (Map<String, String> am : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(am.get("order_id"));
			linkedList.add(am.get("out_ticket_billno")); 
			linkedList.add("改签退票");
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			double price=0.0;
			for (Map<String, String> c : alterCp) {
				if (c.get("buy_money")!= null){
				price += Double.parseDouble(c.get("buy_money"));
				}
			}
			if (am.get("fee")!= null){
				linkedList.add("-"+String.valueOf(price-Double.parseDouble(am.get("fee"))));
			}else{
				linkedList.add("-"+String.valueOf(price-Double.parseDouble("0")));
			} 
			linkedList.add("0");
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			list.add(linkedList);
		}
		 
		String path ="/data/train/upExcel/"+this.getDate("2")+"meituan.xls";
		String type = "M3";
		String[] secondTitles = {"序号","订单号","12306订单号","订单类型","实际订票总价","票数","出票时间"};
		Integer[] weights = {10,35,20,15,15,10,30};
		this.createExcel(path,"meituan",type,querydate,secondTitles,weights,list);
		logger.info("meituan load end...");
	}
	
	
	//gt 表格
	public void gtExcel() {
		logger.info("高铁管家 load start...");
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1"); 
		  
		//预订
		List<String> order_status = new ArrayList<String>();
		order_status.add("44");
		List<String> notify_status = new ArrayList<String>();
		notify_status.add("22");
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("begin_time","2015-01-01");
		map.put("begin_time",querydate);
		map.put("end_time", querydate);
		map.put("order_status",order_status);
		map.put("notify_status",notify_status);
//		map.put("channel", channel);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		//预订
		List<Map<String, String>> booklist = uploadExcelService.queryGtBookList(map);
		logger.info("高铁预订load start...+size="+booklist.size());
		for (Map<String, String> bm : booklist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(bm.get("order_id"));
			linkedList.add(bm.get("merchant_order_id"));
			linkedList.add(bm.get("out_ticket_billno"));
			linkedList.add("");
			linkedList.add("出票");
			linkedList.add(bm.get("out_ticket_time"));
			linkedList.add(bm.get("notify_finish_time"));
			String all_buy_money = bm.get("all_buy_money");
			linkedList.add(all_buy_money);
			linkedList.add(bm.get("ticket_num"));
			list.add(linkedList);
		}
		//退票
		List<String> refund_status = new ArrayList<String>();
		refund_status.add("33");
		List<String> refund_notify_status = new ArrayList<String>();
		notify_status.add("33");
		HashMap<String, Object> refundMap = new HashMap<String, Object>();
		refundMap.put("begin_time",querydate);
		refundMap.put("end_time", querydate);
		refundMap.put("refund_status",refund_status);
		refundMap.put("notify_status",refund_notify_status); 
		List<Map<String, String>> gtRefund = uploadExcelService.queryGtRefundNotifyList(refundMap);
		logger.info("高铁管家总数："+gtRefund.size());
		for (Map<String, String> m : gtRefund) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("merchant_order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			linkedList.add(m.get("cp_id"));
			linkedList.add("退票");
			linkedList.add(m.get("create_time"));
			if(m.get("notify_finish_time")!=null){
				linkedList.add(m.get("notify_finish_time"));}
			else{ linkedList.add("");} 
			if(m.get("refund_money")!=null){
				linkedList.add("-"+String.valueOf(m.get("refund_money")));}
			else{ linkedList.add("0");}  
			linkedList.add("1");
			
			list.add(linkedList);
		}
		
		HashMap<String, Object> newMap = new HashMap<String, Object>();
		newMap.put("begin_time",querydate);
		newMap.put("end_time", querydate); 
		newMap.put("channel", "301030");
		//改签
		List<Map<String, String>> alterlist = uploadExcelService.queryGtgjAlterList(newMap);
		logger.info("gtgj alter load start...+size="+alterlist.size());

		for (Map<String, String> am : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(am.get("order_id"));
			linkedList.add(am.get("merchant_order_id"));
			linkedList.add(am.get("out_ticket_billno")); 
			linkedList.add("");
			linkedList.add("改签扣款");
			linkedList.add(String.valueOf(am.get("create_time")));
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			double alter_price=0.0; 
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			for (Map<String, String> c : alterCp) {
				alter_price += Double.parseDouble(String.valueOf(c.get("change_buy_money")));
			}
			linkedList.add(String.valueOf(alter_price));
			linkedList.add(String.valueOf(alterCp.size()));
			list.add(linkedList);
		}
		
		for (Map<String, String> am : alterlist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(am.get("order_id"));
			linkedList.add(am.get("merchant_order_id"));
			linkedList.add(am.get("out_ticket_billno")); 
			linkedList.add("");
			linkedList.add("改签退票");
			linkedList.add(String.valueOf(am.get("create_time")));
			linkedList.add(String.valueOf(am.get("change_notify_finish_time")));
			List<Map<String,String>> alterCp = uploadExcelService.getElongAlterCpList(String.valueOf(am.get("change_id")));
			double price=0.0;
			for (Map<String, String> c : alterCp) {
				if (c.get("buy_money")!= null){
				price += Double.parseDouble(c.get("buy_money"));
				}
			}
			if (am.get("fee")!= null){
				linkedList.add("-"+String.valueOf(price-Double.parseDouble(am.get("fee"))));
			}else{
				linkedList.add("-"+String.valueOf(price-Double.parseDouble("0")));
			}  
			linkedList.add(String.valueOf(alterCp.size()));
			list.add(linkedList);
		}
		 
		String path ="/data/train/upExcel/"+this.getDate("2")+"gtExcel.xls";
		String type = "G3";
		String[] secondTitles = {"序号","订单号","商户订单号","12306订单号","车票号","订单类型","创建时间","通知完成时间","价格","票数"};
		Integer[] weights = {10,30,30,30,30,15,30,30,15,10};
		this.createExcel(path,"301030",type,querydate,secondTitles,weights,list);
		logger.info("gtgj load end...");
	}
}
