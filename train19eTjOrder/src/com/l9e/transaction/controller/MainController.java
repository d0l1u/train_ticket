package com.l9e.transaction.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.job.UploadExcelJob;
import com.l9e.transaction.service.UploadExcelService;
import com.l9e.util.DbContextHolder;

@Controller 
public class MainController extends BaseController{ 
	private static Logger logger = Logger.getLogger(MainController.class); 
	@Resource
	private UploadExcelService uploadExcelService;
	
	// 初始化入口
	@RequestMapping("/newUploadExcel.do")
	public void upload(HttpServletRequest request,HttpServletResponse response){   
		logger.info("开始下载数据...."); 
//		this.tuniuExcel();
//		this.meituanExcel();
		this.gtExcel();
		printJson(response, "success");
	}
	// 初始化入口
	@RequestMapping("/uploadExcelForDate.do")
	public void uploadDate(HttpServletRequest request,HttpServletResponse response){   
		logger.info("开始下载数据...."); 
		String startDate = this.getParam(request, "startDate");
		String endDate = this.getParam(request, "endDate");
		String channel = this.getParam(request, "channel");
		String status = this.getParam(request, "status");
		this.uploadDateExcel(startDate,endDate,channel,status);
		printJson(response, "success");
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
		this.createExcel(path,"tuniu",type,querydate,secondTitles,weights,list);
		logger.info("tuniu load end...");
	}	
	
	
	
	//途牛 表格
	public void uploadDateExcel(String begin_time,String end_time,String channel,String change_notify_status) {
		logger.info("tuniu load start..."); 
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		String querydate = this.getDate("1"); 
		 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time",begin_time);
		map.put("end_time", end_time); 
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>(); 
		if(!"".equals(change_notify_status))
			map.put("change_notify_status", change_notify_status);
		map.put("channel", channel); 
		//改签
		List<Map<String, String>> alterlist = uploadExcelService.queryAlterList(map);
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
			linkedList.add(String.valueOf(am.get("create_time")));
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
			linkedList.add(String.valueOf(am.get("create_time")));
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
		
		String path ="/data/train/upExcel/"+this.getDate("2")+"ForData.xls";
		String type = "99";
		String[] secondTitles = {"序号","订单号","12306订单号","乘客姓名","交易时间","金额","票数","备注","手续费"};
		Integer[] weights = {10,30,20,30,30,15,10,20,10};
		this.createExcel(path,"tuniu",type,querydate,secondTitles,weights,list);
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
	
	
	/**
	 * 响应json字符串
	 * @param response
	 * @param jsonStr
	 */
	public void printJson(HttpServletResponse response,String jsonStr){
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(jsonStr);
		} catch (IOException e) {
		}finally{
				out.flush();
				out.close();
		}
	} 
	
}
