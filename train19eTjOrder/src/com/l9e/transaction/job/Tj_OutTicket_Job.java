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

import com.l9e.transaction.service.Tj_OutTicket_Service;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.DbContextHolder;

/**
 * 对当天的数据每隔十分钟进行一次统计，然后更新tj表里面的数据
 * 
 * @author zhangjc
 * 效率统计
 */
@Component("tj_OutTicket_Job")
public class Tj_OutTicket_Job {

	private static final Logger logger = Logger.getLogger(Tj_OutTicket_Job.class);
//	@Resource
//	Tj_Hc_orderInfo_Today_Service tj_Hc_orderInfo_Today_Service;
	
	@Resource
	Tj_OutTicket_Service tj_OutTicket_Service;
	
	public void queryXlToInsertUpdateTj() {
		Date date1 = new Date();
		String datePre = DateUtil.dateToString(date1, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "060000", "yyyyMMddHHmmss");// 6:00
		Date end = DateUtil.stringToDate(datePre + "235959", "yyyyMMddHHmmss");// 23:59
		if (date1.before(begin) || date1.after(end)) {
			logger.info("06:00-23:59可以更新统计表！");
			return;
		}
		// List<Map<String, String>> merchantIdList =
		// tj_Hc_orderInfo_Today_Service.queryMerchantId();
		List<String> merchantId_list = new ArrayList<String>();// 存放对外商户的渠道
		// merchantId_list.add("19e");
		// merchantId_list.add("qunar");
		// merchantId_list.add("cmpay");
		// merchantId_list.add("cmwap");
		// merchantId_list.add("19pay");
		// merchantId_list.add("web");
		// merchantId_list.add("app");
		// merchantId_list.add("ccb");
		// merchantId_list.add("weixin");
		merchantId_list.add("elong");
		merchantId_list.add("tongcheng");
		merchantId_list.add("tuniu");
		// merchantId_list.add("chq");
		// for(int i=0; i<merchantIdList.size(); i++){
		// String merchant_id =
		// merchantIdList.get(i).get("merchant_id").toString();
		// if(!"301016".equals(merchant_id)
		// ||!"30101601".equals(merchant_id)||!"30101602".equals(merchant_id)){
		// System.out.println("merchantIdList.size()"+merchantIdList.size());
		// merchantId_list.add(merchant_id);
		// }
		// }
		/************************************ 分时出票效率统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0); // 得到
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// DateFormat df3 = new SimpleDateFormat("HH");
		String create_time = df.format(date);// 日期
		// String hour = df3.format(date);// 时间
		String hour = null;
		String hour2 = null;
		String payType = "00";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("create_time", create_time);

		logger.info("tj_OutTicket_Job自动执行JOB，统计时间为：" + df2.format(date));

		for (int j = 0; j < merchantId_list.size(); j++) {
			String channel = merchantId_list.get(j);
			if ("elong".equals(channel)) {
				payType = "00";
			} else if ("tongcheng".equals(channel)) {
				payType = "11";
			}
			paramMap.put("payType", payType);
			for (int N = 0; N < 2; N++) {
				paramMap.put("device_type", N);
				for (int i = 0; i < 24; i++) {// 主要是对应12306出票规则,23-6点是不能出票,要求改成统计0点到24点之间的数据
					if (i < 10) {
						hour = " 0" + i;
						hour2 = "0" + i;
					} else {
						hour = " " + i;
						hour2 = String.valueOf(i);
					}
					paramMap.put("hour", hour);
					paramMap.put("channel", channel);
					int shoudan=0, book_xl=0, notify=0, notify_pay=0, outticket_xl=0;
					if("tuniu".equals(channel)){
						shoudan = tj_OutTicket_Service.queryShoudanTuniu(paramMap);
						book_xl = tj_OutTicket_Service.queryBook_xlTuniu(paramMap);
						notify = tj_OutTicket_Service.queryNotifyTuniu(paramMap);
						notify_pay = tj_OutTicket_Service.queryNotifyPayTuniu(paramMap);
						outticket_xl = tj_OutTicket_Service.queryOutticket_xlTuniu(paramMap);
					}else{
						shoudan = tj_OutTicket_Service.queryShoudan(paramMap);
						book_xl = tj_OutTicket_Service.queryBook_xl(paramMap);
						notify = tj_OutTicket_Service.queryNotify(paramMap);
						notify_pay = tj_OutTicket_Service.queryNotifyPay(paramMap);
						outticket_xl = tj_OutTicket_Service.queryOutticket_xl(paramMap);
					}
					int fenfa = tj_OutTicket_Service.queryFenfa(paramMap);
					int book = tj_OutTicket_Service.queryBook(paramMap);
					int pay_xl = tj_OutTicket_Service.queryPay_xl(paramMap);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tj_id", CreateIDUtil.createID("TJ"));
					map.put("book_xl", book_xl);
					map.put("shoudan", shoudan);
					map.put("fenfa", fenfa);
					map.put("book", book);
					map.put("notify", notify);
					map.put("pay_xl", pay_xl);
					map.put("notify_pay", notify_pay);
					map.put("outticket_xl", outticket_xl);
					map.put("create_time", df.format(date));
					map.put("hour", hour2);
					map.put("channel", merchantId_list.get(j));
					map.put("type", "00");
					if (N == 1) {
						map.put("device_type", "app");
					} else {
						map.put("device_type", "pc");
					}
					int today19eCount = tj_OutTicket_Service
							.query19eTodayCount(map);
					if (today19eCount == 0) {
						// 添加到表tj_hc_orderInfo表中
						tj_OutTicket_Service.addToTj_OutTicket(map);
					} else {
						tj_OutTicket_Service.updateToTj_OutTicket(map);
					}
					// /************************************ 分时出票效率统计结束
					// *****************************************/
					map.clear();
				}
			}
		}
		/************************************ 当天出票效率统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		Map<String, Object> todayMap = new HashMap<String, Object>();
		todayMap.put("create_time", create_time);
	
		logger.info("tj_OutTicket_Job自动执行JOB，统计时间为：" + df2.format(date));

		for (int M = 0; M < merchantId_list.size(); M++) {

			for (int K = 0; K < 2; K++) {
				todayMap.put("channel", merchantId_list.get(M));
				if ("elong".equals(merchantId_list.get(M))) {
					payType = "00";
				} else if ("tongcheng".equals(merchantId_list.get(M))) {
					payType = "11";
				}
				todayMap.put("payType", payType);
				todayMap.put("device_type", K);
				
				int shoudan=0, book_xl=0, notify=0, notify_pay=0, outticket_xl=0;
				if("tuniu".equals(merchantId_list.get(M))){
					shoudan = tj_OutTicket_Service.queryShoudanTuniu(paramMap);
					book_xl = tj_OutTicket_Service.queryBook_xlTuniu(paramMap);
					notify = tj_OutTicket_Service.queryNotifyTuniu(paramMap);
					notify_pay = tj_OutTicket_Service.queryNotifyPayTuniu(paramMap);
					outticket_xl = tj_OutTicket_Service.queryOutticket_xlTuniu(paramMap);
				}else{
					shoudan = tj_OutTicket_Service.queryShoudan(paramMap);
					book_xl = tj_OutTicket_Service.queryBook_xl(paramMap);
					notify = tj_OutTicket_Service.queryNotify(paramMap);
					notify_pay = tj_OutTicket_Service.queryNotifyPay(paramMap);
					outticket_xl = tj_OutTicket_Service.queryOutticket_xl(paramMap);
				}
				
				int fenfa = tj_OutTicket_Service.queryFenfa(todayMap);
				int book = tj_OutTicket_Service.queryBook(todayMap);
				int pay_xl = tj_OutTicket_Service.queryPay_xl(todayMap);

				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("tj_id", CreateIDUtil.createID("TJ"));
				map2.put("book_xl", book_xl);
				map2.put("shoudan", shoudan);
				map2.put("fenfa", fenfa);
				map2.put("book", book);
				map2.put("notify", notify);
				map2.put("pay_xl", pay_xl);
				map2.put("notify_pay", notify_pay);
				map2.put("outticket_xl", outticket_xl);
				map2.put("create_time", df.format(date));
				map2.put("channel", merchantId_list.get(M));
				map2.put("type", "11");

				// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
				DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
				if (K == 0) {
					map2.put("device_type", "pc");
				} else {
					map2.put("device_type", "app");
				}
				int today19eCount2 = tj_OutTicket_Service
						.query19eTodayCount(map2);
				if (today19eCount2 == 0) {
					// 添加到表tj_hc_orderInfo表中
					tj_OutTicket_Service.addToTj_OutTicket(map2);
				} else {
					tj_OutTicket_Service.updateToTj_OutTicket(map2);
				}
			}
		}
		/************************************ 当天出票效率统计结束 *****************************************/
	}
}
