package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.TjOrderSaleReportService;
import com.l9e.util.CreateIDUtil;

@Component("tjOrderSaleReportJob")
public class TjOrderSaleReportJob {
	private static final Logger logger = Logger.getLogger(TjOrderSaleReportJob.class);
	@Resource
	private TjOrderSaleReportService tjOrderSaleReportService;

	public void tjOrderSaleReport(){
//		int table_count = tjOrderSaleReportService.queryTableCount();  //查询统计表条数 
//		if(table_count==0){												//若为0则全部日期统计，否者只统计前一天的数据
//			logger.info("tjOrderSaleReportJob自动执行JOB，表中数据为："+table_count+"条，进入第一次执行！");
//			//List<String> date_List = tjOrderSaleReportService.queryDateList();    //查出来一串时间（仅限于一个月之内）
//			//交易日期、当天销售金额、当天退款金额、当天保险利润、当天订单数、当天票数 、本月总订单数 、本月总票数、本月保险利润
//			//for(int i =0;i<date_List.size()-1;i++){
//				//String create_time = date_List.get(i).toString();
//				String create_time = "2014-09-05";
//				
//				List<String> dealerIdList = tjOrderSaleReportService.queryDealeiIdList(create_time);//查询当天出票成功的代理商ID
//				
//				logger.info("查询当天出票成功的代理商ID的个数是："+dealerIdList.size());
//				for(int j=0; j<dealerIdList.size(); j++){
//					String dealer_id = dealerIdList.get(j).toString();
//					Map<String,Object>paramMap = new HashMap<String,Object>();
//					paramMap.put("dealer_id", dealer_id);
//					paramMap.put("create_time", create_time);
//					
//					double pay_money = tjOrderSaleReportService.queryThisPaymoney(paramMap);//当天销售金额
//					double refund_money = tjOrderSaleReportService.queryThisRefundmoney(paramMap);//当天退款金额
//					
//					paramMap.put("bxMoney", 10);
//					double bx_countMoney10 = tjOrderSaleReportService.queryThisBxcountMoney10(paramMap);//10块钱保险的利润
//					double month_bx_countMoney10 = tjOrderSaleReportService.queryMonthBxcountMoney10(paramMap);//本月10块钱保险的利润
//					paramMap.put("bxMoney", 20);
//					double bx_countMoney20 = tjOrderSaleReportService.queryThisBxcountMoney20(paramMap);//20块钱保险的利润
//					double month_bx_countMoney20 = tjOrderSaleReportService.queryMonthBxcountMoney20(paramMap);//本月20块钱保险的利润
//					double bx_money = bx_countMoney10+bx_countMoney20;//当天保险总利润
//					double month_bx_money = month_bx_countMoney10+month_bx_countMoney20;//本月保险总利润
//					
//					int order_count = tjOrderSaleReportService.queryThisOrdercount(paramMap);//订单总计
//					int ticket_count = tjOrderSaleReportService.queryThisTicketcount(paramMap);//票数总计
//					
//					int month_order_count = tjOrderSaleReportService.queryMonthOrdercount(paramMap);//本月订单总计
//					int month_ticket_count = tjOrderSaleReportService.queryMonthTicketcount(paramMap);//本月票数总计
//					
//					Map<String,Object>map = new HashMap<String,Object>();
//					map.put("tj_id", CreateIDUtil.createID("TJ"));
//					map.put("order_time", create_time);//交易日期
//					map.put("dealer_id", dealer_id);//代理商ID
//					map.put("pay_money", pay_money);//当天销售金额
//					map.put("refund_money", refund_money);//当天退款金额
//					map.put("bx_money", bx_money);//当天保险利润
//					map.put("order_count", order_count);//当天订单数
//					map.put("ticket_count", ticket_count);//当天票数
//					map.put("month_order_count", month_order_count);//本月总订单数
//					map.put("month_ticket_count", month_ticket_count);// 本月总票数
//					map.put("month_bx_money", month_bx_money);//本月保险利润
//					//添加到表cp_statInfo表中
//					tjOrderSaleReportService.addToTjOrderSaleReportJob(map);
//				}
//			//}	
//			logger.info("tjOrderSaleReportJob自动执行JOB，第一次执行完毕！");
//		}else{
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);    //得到
			Date date = calendar.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String create_time = df.format(date);//得到昨天的时间
			//String create_time = "2014-09-10";//得到昨天的时间
			logger.info("TjOrderSaleReportJob自动执行JOB，统计时间为："+create_time+"日！");
			
			List<String> dealerIdList = tjOrderSaleReportService.queryDealeiIdList(create_time);//查询当天出票成功的代理商ID
			logger.info("查询当天出票成功的代理商ID的个数是："+dealerIdList.size());
			for(int j=0; j<dealerIdList.size(); j++){String dealer_id = dealerIdList.get(j).toString();
			Map<String,Object>paramMap = new HashMap<String,Object>();
			paramMap.put("dealer_id", dealer_id);
			paramMap.put("create_time", create_time);
			
			double pay_money = tjOrderSaleReportService.queryThisPaymoney(paramMap);//当天销售金额
			double refund_money = tjOrderSaleReportService.queryThisRefundmoney(paramMap);//当天退款金额
			
			paramMap.put("bxMoney", 10);
			double bx_countMoney10 = tjOrderSaleReportService.queryThisBxcountMoney10(paramMap);//10块钱保险的利润
			double month_bx_countMoney10 = tjOrderSaleReportService.queryMonthBxcountMoney10(paramMap);//本月10块钱保险的利润
			paramMap.put("bxMoney", 20);
			double bx_countMoney20 = tjOrderSaleReportService.queryThisBxcountMoney20(paramMap);//20块钱保险的利润
			double month_bx_countMoney20 = tjOrderSaleReportService.queryMonthBxcountMoney20(paramMap);//本月20块钱保险的利润
			double bx_money = bx_countMoney10+bx_countMoney20;//当天保险总利润
			double month_bx_money = month_bx_countMoney10+month_bx_countMoney20;//本月保险总利润
			
			int order_count = tjOrderSaleReportService.queryThisOrdercount(paramMap);//订单总计
			int ticket_count = tjOrderSaleReportService.queryThisTicketcount(paramMap);//票数总计
			
			int month_order_count = tjOrderSaleReportService.queryMonthOrdercount(paramMap);//本月订单总计
			int month_ticket_count = tjOrderSaleReportService.queryMonthTicketcount(paramMap);//本月票数总计
			
			Map<String,Object>map = new HashMap<String,Object>();
			map.put("tj_id", CreateIDUtil.createID("TJ"));
			map.put("order_time", create_time);//交易日期
			map.put("dealer_id", dealer_id);//代理商ID
			map.put("pay_money", pay_money);//当天销售金额
			map.put("refund_money", refund_money);//当天退款金额
			map.put("bx_money", bx_money);//当天保险利润
			map.put("order_count", order_count);//当天订单数
			map.put("ticket_count", ticket_count);//当天票数
			map.put("month_order_count", month_order_count);//本月总订单数
			map.put("month_ticket_count", month_ticket_count);// 本月总票数
			map.put("month_bx_money", month_bx_money);//本月保险利润
			//添加到表cp_statInfo表中
			tjOrderSaleReportService.addToTjOrderSaleReportJob(map);}
			logger.info("TjOrderSaleReportJob自动执行JOB，统计时间为："+create_time+"日，执行完毕！");
		//}
	}
}
