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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.Tj_Hc_orderInfo_Today_Service;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.DbContextHolder;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.SwitchUtils;
import com.l9e.util.UrlFormatUtil;

/**
 * 对当天的数据每隔十分钟进行一次统计，然后更新tj表里面的数据
 * 
 * @author guona
 * 
 */
@Component("hc_orderInfo_Today_Job")
public class Tj_Hc_OrderInfo_Today_Job {

	private static final Logger logger = Logger.getLogger(Tj_Hc_OrderInfo_Today_Job.class);
	@Resource
	Tj_Hc_orderInfo_Today_Service tj_Hc_orderInfo_Today_Service;
	
	private String agentLoginNum;
	@Value("#{propertiesReader[agentLoginNum]}")
	public void setAgentLoginNum(String agentLoginNum) {
		this.agentLoginNum = agentLoginNum;
	}
	
	public void queryHcToInsertUpdateTj() {
		Date date1 = new Date();
		String datePre = DateUtil.dateToString(date1, "yyyyMMdd");
		Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");// 6:00
		Date end = DateUtil.stringToDate(datePre + "235959", "yyyyMMddHHmmss");// 23:00
		if (date1.before(begin) || date1.after(end)) {
			logger.info("06:00-23:59可以更新统计表！");
			return;
		}
		/************************************ 19e统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0); // 得到
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String create_time = df.format(date);// 时间
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("create_time", create_time);
		paramMap.put("channel", "19e");
		paramMap.put("bxMoney", 10);
		logger.info("Tj_Hc_OrderInfo_Today_Job自动执行JOB，统计时间为："+ df2.format(date));
		int out_ticket_succeed = tj_Hc_orderInfo_Today_Service.queryPre_day_out_ticket_succeed(paramMap);// 出票成功的个数
		int out_ticket_defeated = tj_Hc_orderInfo_Today_Service.queryPre_day_out_ticket_defeated(paramMap);// 出票失败的个数
		int order_count = tj_Hc_orderInfo_Today_Service.queryPre_day_order_count(paramMap);// 订单的总个数
		int preparative_count = tj_Hc_orderInfo_Today_Service.queryPre_preparative_count(paramMap);// 查询预下单的个数
		int pay_defeated = tj_Hc_orderInfo_Today_Service.queryPre_pay_defeated(paramMap);// 查询支付失败的个数
		int over_time = tj_Hc_orderInfo_Today_Service.queryPre_over_time(paramMap); // 查询超过十分钟订单
		int refund_count = tj_Hc_orderInfo_Today_Service.queryPre_refund_count(paramMap);// 查询发起退款并成功的个数
		double succeed_money = tj_Hc_orderInfo_Today_Service.queryPre_succeed_money(paramMap);// 出票成功的总金额
		double defeated_money = tj_Hc_orderInfo_Today_Service.queryPre_defeated_money(paramMap);// 出票失败的总金额
		double change_money = tj_Hc_orderInfo_Today_Service.queryPre_change_money(paramMap);// 查询因改签赚取的钱数
		double refund_money = tj_Hc_orderInfo_Today_Service.queryPre_refund_money(paramMap);// 查询总退款金额
		int bx_count = tj_Hc_orderInfo_Today_Service.queryPre_bx_count(paramMap);// 出售保险的个数
		int ticket_count = tj_Hc_orderInfo_Today_Service.queryPre_ticket_count(paramMap);// 查询出出售的票数
		
		double bx_countMoney10 = tj_Hc_orderInfo_Today_Service.queryPre_bx_countMoney10(paramMap);
		paramMap.put("bxMoney", 20);
		double bx_countMoney20 = tj_Hc_orderInfo_Today_Service.queryPre_bx_countMoney20(paramMap);
		double bx_countMoney = bx_countMoney10 + bx_countMoney20;// 出售保险的总价
		int active = tj_Hc_orderInfo_Today_Service.queryActiveTotal(paramMap);// 活跃用户数
		int vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败数
		int vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数
		int svip_num = tj_Hc_orderInfo_Today_Service.queryPre_svip_count(paramMap);//SVIP总数
		int svip_lose = tj_Hc_orderInfo_Today_Service.queryPre_svip_lose(paramMap);//SVIP失败数
		double vip_sbl;// VIP失败率
		String succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double out_ticket_XL19E = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率/预订时长
		double pay_time19E = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长
		
		double succeed_oddsStr;// 出票成功转化率
		int succeed_defeated_count = out_ticket_succeed + out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double ticket_cgl;// 订单成功率
		double ticket_sbl;// 订单失败率
		String succeed_odds = null;// 出票成功转化率(数据库字段)
		String succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String succeed_sbl = null;// 订单失败率(转换为0.00%后)
		if (vip_count != 0) {
			vip_sbl = ((double) vip_lose / (double) vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			succeed_vip_sbl = SwitchUtils.format_perCent(vip_sbl);
		} else {
			succeed_vip_sbl = "0.00%";
		}
		
		if (order_count != 0) {
			succeed_oddsStr = ((double) out_ticket_succeed / (double) order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			succeed_odds = SwitchUtils.format_perCent(succeed_oddsStr);
		} else {
			succeed_odds = "0.00%"; // 出票成功率
		}

		if (succeed_defeated_count != 0) {
			ticket_cgl = ((double) out_ticket_succeed / (double) succeed_defeated_count);// 订单成功率
			ticket_sbl = ((double) out_ticket_defeated / (double) succeed_defeated_count);// 订单失败率
			succeed_cgl = SwitchUtils.format_perCent(ticket_cgl);
			succeed_sbl = SwitchUtils.format_perCent(ticket_sbl);
		} else {
			succeed_cgl = "0.00%";
			succeed_sbl = "0.00%";
		}
//		Map<String, String> paramMap1  = new HashMap<String, String>();
//		String params = null;String agent_login_num1 = null;
//		try {
//			params = UrlFormatUtil.CreateUrl("", paramMap1, "", "UTF-8");
//			agent_login_num1 = HttpPostUtil.sendAndRecive(agentLoginNum, params);
//			logger.info("代理商登陆次数为：" + agent_login_num1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		String agent_login_num1 = this.getCountsValue("agent_login_num1", "agent_login_num1");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tj_id", CreateIDUtil.createID("TJ"));
		map.put("out_ticket_succeed", out_ticket_succeed);
		map.put("out_ticket_defeated", out_ticket_defeated);
		map.put("order_count", order_count);
		map.put("succeed_money", succeed_money);
		map.put("defeated_money", defeated_money);
		map.put("bx_count", bx_count);
		map.put("bx_countMoney", bx_countMoney);
		map.put("change_money", change_money);
		map.put("refund_money", refund_money);
		map.put("succeed_odds", succeed_odds);
		map.put("order_time", df.format(date));
		map.put("preparative_count", preparative_count);
		map.put("refund_count", refund_count);
		map.put("ticket_count", ticket_count);
		map.put("succeed_cgl", succeed_cgl);
		map.put("succeed_sbl", succeed_sbl);
		map.put("succeed_vip_sbl", succeed_vip_sbl);
		map.put("pay_defeated", pay_defeated);
		map.put("over_time", over_time);
		map.put("active", active);
		map.put("svip_num", svip_num);
		map.put("svip_lose", svip_lose);
		
		map.put("agent_login_num",agent_login_num1);
		
		map.put("vip_lose", vip_lose);
		map.put("out_ticket_XL", out_ticket_XL19E);
		map.put("pay_time", pay_time19E);
		map.put("channel", "19e");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
		int today19eCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(map);
		if (today19eCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(map);
		}
		/************************************ 19e统计结束 *****************************************/
		
		/************************************ 19e掌铺统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("create_time", create_time);
		paramMap.put("channel", "mobile");
		paramMap.put("bxMoney", 10);
		int mobile_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.queryPre_day_out_ticket_succeed(paramMap);// 出票成功的个数
		int mobile_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.queryPre_day_out_ticket_defeated(paramMap);// 出票失败的个数
		int mobile_order_count = tj_Hc_orderInfo_Today_Service.queryPre_day_order_count(paramMap);// 订单的总个数
		int mobile_preparative_count = tj_Hc_orderInfo_Today_Service.queryPre_preparative_count(paramMap);// 查询预下单的个数
		int mobile_refund_count = tj_Hc_orderInfo_Today_Service.queryPre_refund_count(paramMap);// 查询发起退款并成功的个数
		double mobile_succeed_money = tj_Hc_orderInfo_Today_Service.queryPre_succeed_money(paramMap);// 出票成功的总金额
		double mobile_defeated_money = tj_Hc_orderInfo_Today_Service.queryPre_defeated_money(paramMap);// 出票失败的总金额
		double mobile_refund_money = tj_Hc_orderInfo_Today_Service.queryPre_refund_money(paramMap);// 查询总退款金额
		int mobile_bx_count = tj_Hc_orderInfo_Today_Service.queryPre_bx_count(paramMap);// 出售保险的个数
		int mobile_ticket_count = tj_Hc_orderInfo_Today_Service.queryPre_ticket_count(paramMap);// 查询出出售的票数
		
		double mobile_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.queryPre_bx_countMoney10(paramMap);
		paramMap.put("bxMoney", 20);
		double mobile_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.queryPre_bx_countMoney20(paramMap);
		double mobile_bx_countMoney = mobile_bx_countMoney10 + mobile_bx_countMoney20;// 出售保险的总价
		int mobile_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败数
		int mobile_svip_num = tj_Hc_orderInfo_Today_Service.queryPre_svip_count(paramMap);//SVIP总数
		int mobile_svip_lose = tj_Hc_orderInfo_Today_Service.queryPre_svip_lose(paramMap);//SVIP失败数
		double mobile_out_ticket_XL19E = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率/预订时长
		double mobile_pay_time19E = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长
		
		int mobile_succeed_defeated_count = mobile_out_ticket_succeed + mobile_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double mobile_ticket_cgl;// 订单成功率
		double mobile_ticket_sbl;// 订单失败率
		String mobile_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String mobile_succeed_sbl = null;// 订单失败率(转换为0.00%后)
		if (mobile_succeed_defeated_count != 0) {
			mobile_ticket_cgl = ((double) mobile_out_ticket_succeed / (double) mobile_succeed_defeated_count);// 订单成功率
			mobile_ticket_sbl = ((double) mobile_out_ticket_defeated / (double) mobile_succeed_defeated_count);// 订单失败率
			mobile_succeed_cgl = SwitchUtils.format_perCent(mobile_ticket_cgl);
			mobile_succeed_sbl = SwitchUtils.format_perCent(mobile_ticket_sbl);
		} else {
			mobile_succeed_cgl = "0.00%";
			mobile_succeed_sbl = "0.00%";
		}
		
		Map<String, Object> mobilemap = new HashMap<String, Object>();
		mobilemap.put("tj_id", CreateIDUtil.createID("TJ"));
		mobilemap.put("out_ticket_succeed", mobile_out_ticket_succeed);
		mobilemap.put("out_ticket_defeated", mobile_out_ticket_defeated);
		mobilemap.put("order_count", mobile_order_count);
		mobilemap.put("succeed_money", mobile_succeed_money);
		mobilemap.put("defeated_money", mobile_defeated_money);
		mobilemap.put("bx_count", mobile_bx_count);
		mobilemap.put("bx_countMoney", mobile_bx_countMoney);
		mobilemap.put("refund_money", mobile_refund_money);
		mobilemap.put("succeed_odds", succeed_odds);
		mobilemap.put("order_time", df.format(date));
		mobilemap.put("preparative_count", mobile_preparative_count);
		mobilemap.put("refund_count", mobile_refund_count);
		mobilemap.put("ticket_count", mobile_ticket_count);
		mobilemap.put("succeed_cgl", mobile_succeed_cgl);
		mobilemap.put("succeed_sbl", mobile_succeed_sbl);
		mobilemap.put("svip_num", mobile_svip_num);
		mobilemap.put("svip_lose", mobile_svip_lose);
		
		mobilemap.put("vip_lose", mobile_vip_lose);
		mobilemap.put("out_ticket_XL", mobile_out_ticket_XL19E);
		mobilemap.put("pay_time", mobile_pay_time19E);
		mobilemap.put("channel", "mobile");
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
		int today19emobileCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(mobilemap);
		if (today19emobileCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(mobilemap);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(mobilemap);
		}
		/************************************ 19e掌铺统计结束 *****************************************/
		
		/************************************ 去哪儿统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "qunar");
		int qunar_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.qunar_query_day_out_ticket_succeed(paramMap);// 当天出票成功的个数
		int qunar_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.qunar_query_day_out_ticket_defeated(paramMap);// 当出票失败的个数
		int qunar_order_count = tj_Hc_orderInfo_Today_Service.qunar_query_day_order_count(paramMap);// 当天的总个数
		int qunar_refund_count = tj_Hc_orderInfo_Today_Service.qunar_query_refund_count(paramMap);// 查询当天发起退款并成功的个数
		double qunar_succeed_money = tj_Hc_orderInfo_Today_Service.qunar_query_succeed_money(paramMap);// 当天出票成功的总金额
		double qunar_defeated_money = tj_Hc_orderInfo_Today_Service.qunar_query_defeated_money(paramMap);// 当天出票失败的总金额
		double qunar_refund_money = tj_Hc_orderInfo_Today_Service.qunar_query_refund_money(paramMap);// 查询总退款金额
		int qunar_ticket_count = tj_Hc_orderInfo_Today_Service.qunar_query_ticket_count(paramMap);// 查询出当天出售的票数
		int qunar_over_time = tj_Hc_orderInfo_Today_Service.qunar_over_time(paramMap);
		double qunar_succeed_oddsStr;// 出票成功转化率
		int qunar_succeed_defeated_count = qunar_out_ticket_succeed + qunar_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLQunar = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timeQunar = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长
		double qunar_ticket_cgl;// 订单成功率
		double qunar_ticket_sbl;// 订单失败率
		String qunar_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String qunar_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String qunar_succeed_sbl = null;// 订单失败率(转换为0.00%后)
		if (qunar_order_count != 0) {
			qunar_succeed_oddsStr = ((double) qunar_out_ticket_succeed / (double) qunar_order_count); // 出票成功转化率
			// double 转换为 String类型的“**.**%”
			qunar_succeed_odds = SwitchUtils.format_perCent(qunar_succeed_oddsStr);
		} else {
			qunar_succeed_odds = "0.00%"; // 出票成功率
		}
		if (qunar_succeed_defeated_count != 0) {
			qunar_ticket_cgl = ((double) qunar_out_ticket_succeed / (double) qunar_succeed_defeated_count);// 订单成功率
			qunar_ticket_sbl = ((double) qunar_out_ticket_defeated / (double) qunar_succeed_defeated_count);// 订单失败率
			qunar_succeed_cgl = SwitchUtils.format_perCent(qunar_ticket_cgl);
			qunar_succeed_sbl = SwitchUtils.format_perCent(qunar_ticket_sbl);
		} else {
			qunar_succeed_cgl = "0.00%";
			qunar_succeed_sbl = "0.00%";
		}
		Map<String, Object> qunar_map = new HashMap<String, Object>();
		qunar_map.put("tj_id", CreateIDUtil.createID("TJ"));
		qunar_map.put("out_ticket_succeed", qunar_out_ticket_succeed);
		qunar_map.put("out_ticket_defeated", qunar_out_ticket_defeated);
		qunar_map.put("order_count", qunar_order_count);
		qunar_map.put("succeed_money", qunar_succeed_money);
		qunar_map.put("defeated_money", qunar_defeated_money);
		qunar_map.put("refund_money", qunar_refund_money);
		qunar_map.put("succeed_odds", qunar_succeed_odds);
		qunar_map.put("order_time", create_time);
		qunar_map.put("refund_count", qunar_refund_count);
		qunar_map.put("ticket_count", qunar_ticket_count);
		qunar_map.put("succeed_cgl", qunar_succeed_cgl);
		qunar_map.put("succeed_sbl", qunar_succeed_sbl);
		qunar_map.put("out_ticket_XL", out_ticket_XLQunar);
		qunar_map.put("pay_time", pay_timeQunar);
		qunar_map.put("over_time", qunar_over_time);
		qunar_map.put("channel", "qunar");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todayqunarCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(qunar_map);
		if (todayqunarCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(qunar_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(qunar_map);
		}
		/************************************ 去哪儿统计结束 *****************************************/
		
		
		
		
		/************************************ 去哪儿--久久商旅统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "qunar");
		paramMap.put("order_source", "qunar2");
		int qunar2_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.qunar_query_day_out_ticket_succeed(paramMap);// 当天出票成功的个数
		int qunar2_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.qunar_query_day_out_ticket_defeated(paramMap);// 当出票失败的个数
		int qunar2_order_count = tj_Hc_orderInfo_Today_Service.qunar_query_day_order_count(paramMap);// 当天的总个数
		int qunar2_refund_count = tj_Hc_orderInfo_Today_Service.qunar_query_refund_count(paramMap);// 查询当天发起退款并成功的个数
		double qunar2_succeed_money = tj_Hc_orderInfo_Today_Service.qunar_query_succeed_money(paramMap);// 当天出票成功的总金额
		double qunar2_defeated_money = tj_Hc_orderInfo_Today_Service.qunar_query_defeated_money(paramMap);// 当天出票失败的总金额
		double qunar2_refund_money = tj_Hc_orderInfo_Today_Service.qunar_query_refund_money(paramMap);// 查询总退款金额
		int qunar2_ticket_count = tj_Hc_orderInfo_Today_Service.qunar_query_ticket_count(paramMap);// 查询出当天出售的票数
		int qunar2_over_time = tj_Hc_orderInfo_Today_Service.qunar_over_time(paramMap);
		double qunar2_succeed_oddsStr;// 出票成功转化率
		int qunar2_succeed_defeated_count = qunar2_out_ticket_succeed + qunar2_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLQunar2 = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timeQunar2  = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double qunar2_ticket_cgl;// 订单成功率
		double qunar2_ticket_sbl;// 订单失败率
		String qunar2_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String qunar2_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String qunar2_succeed_sbl = null;// 订单失败率(转换为0.00%后)
		if (qunar2_order_count != 0) {
			qunar2_succeed_oddsStr = ((double) qunar2_out_ticket_succeed / (double) qunar2_order_count); // 出票成功转化率
			// double 转换为 String类型的“**.**%”
			qunar2_succeed_odds = SwitchUtils.format_perCent(qunar2_succeed_oddsStr);
		} else {
			qunar2_succeed_odds = "0.00%"; // 出票成功率
		}
		if (qunar2_succeed_defeated_count != 0) {
			qunar2_ticket_cgl = ((double) qunar2_out_ticket_succeed / (double) qunar2_succeed_defeated_count);// 订单成功率
			qunar2_ticket_sbl = ((double) qunar2_out_ticket_defeated / (double) qunar2_succeed_defeated_count);// 订单失败率
			qunar2_succeed_cgl = SwitchUtils.format_perCent(qunar2_ticket_cgl);
			qunar2_succeed_sbl = SwitchUtils.format_perCent(qunar2_ticket_sbl);
		} else {
			qunar2_succeed_cgl = "0.00%";
			qunar2_succeed_sbl = "0.00%";
		}
		Map<String, Object> qunar2_map = new HashMap<String, Object>();
		qunar2_map.put("tj_id", CreateIDUtil.createID("TJ"));
		qunar2_map.put("out_ticket_succeed", qunar2_out_ticket_succeed);
		qunar2_map.put("out_ticket_defeated", qunar2_out_ticket_defeated);
		qunar2_map.put("order_count", qunar2_order_count);
		qunar2_map.put("succeed_money", qunar2_succeed_money);
		qunar2_map.put("defeated_money", qunar2_defeated_money);
		qunar2_map.put("refund_money", qunar2_refund_money);
		qunar2_map.put("succeed_odds", qunar2_succeed_odds);
		qunar2_map.put("order_time", create_time);
		qunar2_map.put("refund_count", qunar2_refund_count);
		qunar2_map.put("ticket_count", qunar2_ticket_count);
		qunar2_map.put("succeed_cgl", qunar2_succeed_cgl);
		qunar2_map.put("succeed_sbl", qunar2_succeed_sbl);
		qunar2_map.put("out_ticket_XL", out_ticket_XLQunar2);
		qunar2_map.put("pay_time", pay_timeQunar2);
		qunar2_map.put("over_time", qunar2_over_time);
		qunar2_map.put("channel", "qunar2");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todayqunar2Count = tj_Hc_orderInfo_Today_Service.query19eTodayCount(qunar2_map);
		if (todayqunar2Count == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(qunar2_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(qunar2_map);
		}
		/************************************ 去哪儿--久久商旅统计结束 *****************************************/
		/************************************ 去哪儿--19旅行统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "qunar");
		paramMap.put("order_source", "qunar1");
		int qunar1_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.qunar_query_day_out_ticket_succeed(paramMap);// 当天出票成功的个数
		int qunar1_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.qunar_query_day_out_ticket_defeated(paramMap);// 当出票失败的个数
		int qunar1_order_count = tj_Hc_orderInfo_Today_Service.qunar_query_day_order_count(paramMap);// 当天的总个数
		int qunar1_refund_count = tj_Hc_orderInfo_Today_Service.qunar_query_refund_count(paramMap);// 查询当天发起退款并成功的个数
		double qunar1_succeed_money = tj_Hc_orderInfo_Today_Service.qunar_query_succeed_money(paramMap);// 当天出票成功的总金额
		double qunar1_defeated_money = tj_Hc_orderInfo_Today_Service.qunar_query_defeated_money(paramMap);// 当天出票失败的总金额
		double qunar1_refund_money = tj_Hc_orderInfo_Today_Service.qunar_query_refund_money(paramMap);// 查询总退款金额
		int qunar1_ticket_count = tj_Hc_orderInfo_Today_Service.qunar_query_ticket_count(paramMap);// 查询出当天出售的票数
		int qunar1_over_time = tj_Hc_orderInfo_Today_Service.qunar_over_time(paramMap);
		double qunar1_succeed_oddsStr;// 出票成功转化率
		int qunar1_succeed_defeated_count = qunar1_out_ticket_succeed + qunar1_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLQunar1 = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timeQunar1 = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长
		double qunar1_ticket_cgl;// 订单成功率
		double qunar1_ticket_sbl;// 订单失败率
		String qunar1_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String qunar1_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String qunar1_succeed_sbl = null;// 订单失败率(转换为0.00%后)
		if (qunar1_order_count != 0) {
			qunar1_succeed_oddsStr = ((double) qunar1_out_ticket_succeed / (double) qunar1_order_count); // 出票成功转化率
			// double 转换为 String类型的“**.**%”
			qunar1_succeed_odds = SwitchUtils.format_perCent(qunar1_succeed_oddsStr);
		} else {
			qunar1_succeed_odds = "0.00%"; // 出票成功率
		}
		if (qunar1_succeed_defeated_count != 0) {
			qunar1_ticket_cgl = ((double) qunar1_out_ticket_succeed / (double) qunar1_succeed_defeated_count);// 订单成功率
			qunar1_ticket_sbl = ((double) qunar1_out_ticket_defeated / (double) qunar1_succeed_defeated_count);// 订单失败率
			qunar1_succeed_cgl = SwitchUtils.format_perCent(qunar1_ticket_cgl);
			qunar1_succeed_sbl = SwitchUtils.format_perCent(qunar1_ticket_sbl);
		} else {
			qunar1_succeed_cgl = "0.00%";
			qunar1_succeed_sbl = "0.00%";
		}
		Map<String, Object> qunar1_map = new HashMap<String, Object>();
		qunar1_map.put("tj_id", CreateIDUtil.createID("TJ"));
		qunar1_map.put("out_ticket_succeed", qunar1_out_ticket_succeed);
		qunar1_map.put("out_ticket_defeated", qunar1_out_ticket_defeated);
		qunar1_map.put("order_count", qunar1_order_count);
		qunar1_map.put("succeed_money", qunar1_succeed_money);
		qunar1_map.put("defeated_money", qunar1_defeated_money);
		qunar1_map.put("refund_money", qunar1_refund_money);
		qunar1_map.put("succeed_odds", qunar1_succeed_odds);
		qunar1_map.put("order_time", create_time);
		qunar1_map.put("refund_count", qunar1_refund_count);
		qunar1_map.put("ticket_count", qunar1_ticket_count);
		qunar1_map.put("succeed_cgl", qunar1_succeed_cgl);
		qunar1_map.put("succeed_sbl", qunar1_succeed_sbl);
		qunar1_map.put("out_ticket_XL", out_ticket_XLQunar1);
		qunar1_map.put("pay_time", pay_timeQunar1);
		qunar1_map.put("over_time", qunar1_over_time);
		qunar1_map.put("channel", "qunar1");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todayqunar1Count = tj_Hc_orderInfo_Today_Service.query19eTodayCount(qunar1_map);
		if (todayqunar1Count == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(qunar1_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(qunar1_map);
		}
		/************************************ 去哪儿--19旅行统计结束 *****************************************/
		
		
		
		/************************************ 19pay统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "19pay");
		int pay_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.pay_query_day_out_ticket_succeed(paramMap);// 订单成功数
		int pay_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.pay_query_day_out_ticket_defeated(paramMap);// 订单失败数
		int pay_preparative_count = tj_Hc_orderInfo_Today_Service.pay_queryPre_preparative_count(paramMap);// 预下单
		int pay_pay_defeated = tj_Hc_orderInfo_Today_Service.pay_queryPre_pay_defeated(paramMap);// 支付失败的个数
		int pay_order_count = tj_Hc_orderInfo_Today_Service.pay_query_day_order_count(paramMap);// 总订单数
		int pay_refund_count = tj_Hc_orderInfo_Today_Service.pay_query_refund_count(paramMap);// 退款成功
		double pay_succeed_money = tj_Hc_orderInfo_Today_Service.pay_query_succeed_money(paramMap);// 出票成功的总金额
		double pay_defeated_money = tj_Hc_orderInfo_Today_Service.pay_query_defeated_money(paramMap);// 出票失败的总金额
		double pay_change_money = tj_Hc_orderInfo_Today_Service.pay_queryPre_change_money(paramMap);// 改签差价
		double pay_refund_money = tj_Hc_orderInfo_Today_Service.pay_query_refund_money(paramMap);// 查询总退款金额
		int pay_ticket_count = tj_Hc_orderInfo_Today_Service.pay_query_ticket_count(paramMap);// 票数总计
		int pay_over_time = tj_Hc_orderInfo_Today_Service.pay_over_time(paramMap); // 超过十分钟订单数
		int pay_bx_count = tj_Hc_orderInfo_Today_Service.pay_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
		paramMap.put("bxMoney", 10);
		double pay_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.pay_queryPre_bx_countMoney10(paramMap);// 10元保险利润
		paramMap.put("bxMoney", 20);
		double pay_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.pay_queryPre_bx_countMoney20(paramMap);// 20元保险利润
		double pay_bx_countMoney = pay_bx_countMoney10 + pay_bx_countMoney20; // 保险总利润
		int pay_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		int pay_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数
		double pay_vip_sbl;// VIP失败率
		String pay_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double pay_succeed_oddsStr;// 出票成功转化率
		int pay_succeed_defeated_count = pay_out_ticket_succeed + pay_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XL19pay = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_time19pay = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double pay_ticket_cgl;// 订单成功率
		double pay_ticket_sbl;// 订单失败率
		String pay_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String pay_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String pay_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (pay_vip_count != 0) {
			pay_vip_sbl = ((double) pay_vip_lose / (double) pay_vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			pay_succeed_vip_sbl = SwitchUtils.format_perCent(pay_vip_sbl);
		} else {
			pay_succeed_vip_sbl = "0.00%";
		}

		if (pay_order_count != 0) {
			pay_succeed_oddsStr = ((double) pay_out_ticket_succeed / (double) pay_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			pay_succeed_odds = SwitchUtils.format_perCent(pay_succeed_oddsStr);
		} else {
			pay_succeed_odds = "0.00%"; // 出票成功率
		}

		if (pay_succeed_defeated_count != 0) {
			pay_ticket_cgl = ((double) pay_out_ticket_succeed / (double) pay_succeed_defeated_count);// 订单成功率
			pay_ticket_sbl = ((double) pay_out_ticket_defeated / (double) pay_succeed_defeated_count);// 订单失败率
			pay_succeed_cgl = SwitchUtils.format_perCent(pay_ticket_cgl);
			pay_succeed_sbl = SwitchUtils.format_perCent(pay_ticket_sbl);
		} else {
			pay_succeed_cgl = "0.00%";
			pay_succeed_sbl = "0.00%";
		}

		Map<String, Object> pay_map = new HashMap<String, Object>();
		pay_map.put("tj_id", CreateIDUtil.createID("TJ"));
		pay_map.put("out_ticket_succeed", pay_out_ticket_succeed);
		pay_map.put("out_ticket_defeated", pay_out_ticket_defeated);
		pay_map.put("order_count", pay_order_count);
		pay_map.put("succeed_money", pay_succeed_money);
		pay_map.put("defeated_money", pay_defeated_money);
		pay_map.put("succeed_odds", pay_succeed_odds);
		pay_map.put("order_time", create_time);
		pay_map.put("refund_count", pay_refund_count);
		pay_map.put("ticket_count", pay_ticket_count);
		pay_map.put("succeed_cgl", pay_succeed_cgl);
		pay_map.put("succeed_sbl", pay_succeed_sbl);
		pay_map.put("over_time", pay_over_time);
		pay_map.put("preparative_count", pay_preparative_count);
		pay_map.put("pay_defeated", pay_pay_defeated);
		pay_map.put("change_money", pay_change_money);
		pay_map.put("refund_money", pay_refund_money);
		pay_map.put("bx_count", pay_bx_count);//出售保险个数
		pay_map.put("bx_countMoney", pay_bx_countMoney);//保险金额
		pay_map.put("vip_lose", pay_vip_lose);
		pay_map.put("succeed_vip_sbl", pay_succeed_vip_sbl);
		pay_map.put("out_ticket_XL", out_ticket_XL19pay);
		pay_map.put("pay_time", pay_time19pay);
		// pay_map.put("active", active);
		pay_map.put("channel", "19pay");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int today19payCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(pay_map);
		if (today19payCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(pay_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(pay_map);
		}
		/************************************ 19pay统计结束 *****************************************/
		
		/************************************ cmpay统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "cmpay");
		int cmpay_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.cmpay_query_day_out_ticket_succeed(paramMap);// 订单成功数
		int cmpay_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.cmpay_query_day_out_ticket_defeated(paramMap);// 订单失败数
		int cmpay_preparative_count = tj_Hc_orderInfo_Today_Service.cmpay_queryPre_preparative_count(paramMap);// 预下单
		int cmpay_pay_defeated = tj_Hc_orderInfo_Today_Service.cmpay_queryPre_pay_defeated(paramMap);// 支付失败的个数
		int cmpay_order_count = tj_Hc_orderInfo_Today_Service.cmpay_query_day_order_count(paramMap);// 总订单数
		int cmpay_refund_count = tj_Hc_orderInfo_Today_Service.cmpay_query_refund_count(paramMap);// 退款成功
		double cmpay_succeed_money = tj_Hc_orderInfo_Today_Service.cmpay_query_succeed_money(paramMap);// 出票成功的总金额
		double cmpay_defeated_money = tj_Hc_orderInfo_Today_Service.cmpay_query_defeated_money(paramMap);// 出票失败的总金额
		double cmpay_change_money = tj_Hc_orderInfo_Today_Service.cmpay_queryPre_change_money(paramMap);// 改签差价
		double cmpay_refund_money = tj_Hc_orderInfo_Today_Service.cmpay_query_refund_money(paramMap);// 查询总退款金额
		int cmpay_ticket_count = tj_Hc_orderInfo_Today_Service.cmpay_query_ticket_count(paramMap);// 票数总计
		int cmpay_over_time = tj_Hc_orderInfo_Today_Service.cmpay_over_time(paramMap); // 超过十分钟订单数
		int cmpay_bx_count = tj_Hc_orderInfo_Today_Service.cmpay_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
		paramMap.put("bxMoney", 10);
		double cmpay_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.cmpay_queryPre_bx_countMoney10(paramMap);// 10元保险利润
		paramMap.put("bxMoney", 20);
		double cmpay_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.cmpay_queryPre_bx_countMoney20(paramMap);// 20元保险利润
		double cmpay_bx_countMoney = cmpay_bx_countMoney10 + cmpay_bx_countMoney20; // 保险总利润
		int cmpay_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		int cmpay_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数

		double cmpay_vip_sbl;// VIP失败率
		String cmpay_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double cmpay_succeed_oddsStr;// 出票成功转化率
		int cmpay_succeed_defeated_count = cmpay_out_ticket_succeed + cmpay_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLCmpay = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timeCmpay = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double cmpay_ticket_cgl;// 订单成功率
		double cmpay_ticket_sbl;// 订单失败率
		String cmpay_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String cmpay_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String cmpay_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (cmpay_vip_count != 0) {
			cmpay_vip_sbl = ((double) cmpay_vip_lose / (double) cmpay_vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			cmpay_succeed_vip_sbl = SwitchUtils.format_perCent(cmpay_vip_sbl);
		} else {
			cmpay_succeed_vip_sbl = "0.00%";
		}

		if (cmpay_order_count != 0) {
			cmpay_succeed_oddsStr = ((double) cmpay_out_ticket_succeed / (double) cmpay_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			cmpay_succeed_odds = SwitchUtils.format_perCent(cmpay_succeed_oddsStr);
		} else {
			cmpay_succeed_odds = "0.00%"; // 出票成功率
		}

		if (cmpay_succeed_defeated_count != 0) {
			cmpay_ticket_cgl = ((double) cmpay_out_ticket_succeed / (double) cmpay_succeed_defeated_count);// 订单成功率
			cmpay_ticket_sbl = ((double) cmpay_out_ticket_defeated / (double) cmpay_succeed_defeated_count);// 订单失败率
			cmpay_succeed_cgl = SwitchUtils.format_perCent(cmpay_ticket_cgl);
			cmpay_succeed_sbl = SwitchUtils.format_perCent(cmpay_ticket_sbl);
		} else {
			cmpay_succeed_cgl = "0.00%";
			cmpay_succeed_sbl = "0.00%";
		}

		Map<String, Object> cmpay_map = new HashMap<String, Object>();
		cmpay_map.put("tj_id", CreateIDUtil.createID("TJ"));
		cmpay_map.put("out_ticket_succeed", cmpay_out_ticket_succeed);
		cmpay_map.put("out_ticket_defeated", cmpay_out_ticket_defeated);
		cmpay_map.put("order_count", cmpay_order_count);
		cmpay_map.put("succeed_money", cmpay_succeed_money);
		cmpay_map.put("defeated_money", cmpay_defeated_money);
		cmpay_map.put("refund_money", cmpay_refund_money);
		cmpay_map.put("succeed_odds", cmpay_succeed_odds);
		cmpay_map.put("order_time", create_time);
		cmpay_map.put("refund_count", cmpay_refund_count);
		cmpay_map.put("ticket_count", cmpay_ticket_count);
		cmpay_map.put("succeed_cgl", cmpay_succeed_cgl);
		cmpay_map.put("succeed_sbl", cmpay_succeed_sbl);
		cmpay_map.put("out_ticket_XL", out_ticket_XLCmpay);
		cmpay_map.put("pay_time", pay_timeCmpay);
		cmpay_map.put("over_time", cmpay_over_time);
		cmpay_map.put("preparative_count", cmpay_preparative_count);
		cmpay_map.put("pay_defeated", cmpay_pay_defeated);
		cmpay_map.put("change_money", cmpay_change_money);
		cmpay_map.put("bx_count", cmpay_bx_count);
		cmpay_map.put("bx_countMoney", cmpay_bx_countMoney);
		cmpay_map.put("vip_lose", cmpay_vip_lose);
		cmpay_map.put("succeed_vip_sbl", cmpay_succeed_vip_sbl);
		// cmpay_map.put("active", active);
		cmpay_map.put("channel", "cmpay");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todaycmpayCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(cmpay_map);
		if (todaycmpayCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(cmpay_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(cmpay_map);
		}
		/************************************ cmpay统计结束 *****************************************/
		
		/************************************ cmwap统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "cmwap");
		int cmwap_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.cmwap_query_day_out_ticket_succeed(paramMap);// 订单成功数
		int cmwap_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.cmwap_query_day_out_ticket_defeated(paramMap);// 订单失败数
		int cmwap_preparative_count = tj_Hc_orderInfo_Today_Service.cmwap_queryPre_preparative_count(paramMap);// 预下单
		int cmwap_pay_defeated = tj_Hc_orderInfo_Today_Service.cmwap_queryPre_pay_defeated(paramMap);// 支付失败的个数
		int cmwap_order_count = tj_Hc_orderInfo_Today_Service.cmwap_query_day_order_count(paramMap);// 总订单数
		int cmwap_refund_count = tj_Hc_orderInfo_Today_Service.cmwap_query_refund_count(paramMap);// 退款成功
		double cmwap_succeed_money = tj_Hc_orderInfo_Today_Service.cmwap_query_succeed_money(paramMap);// 出票成功的总金额
		double cmwap_defeated_money = tj_Hc_orderInfo_Today_Service.cmwap_query_defeated_money(paramMap);// 出票失败的总金额
		double cmwap_change_money = tj_Hc_orderInfo_Today_Service.cmwap_queryPre_change_money(paramMap);// 改签差价
		double cmwap_refund_money = tj_Hc_orderInfo_Today_Service.cmwap_query_refund_money(paramMap);// 查询总退款金额
		int cmwap_ticket_count = tj_Hc_orderInfo_Today_Service.cmwap_query_ticket_count(paramMap);// 票数总计
		int cmwap_over_time = tj_Hc_orderInfo_Today_Service.cmwap_over_time(paramMap); // 超过十分钟订单数
		int cmwap_bx_count = tj_Hc_orderInfo_Today_Service.cmwap_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
		paramMap.put("bxMoney", 10);
		double cmwap_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.cmwap_queryPre_bx_countMoney10(paramMap);// 10元保险利润
		paramMap.put("bxMoney", 20);
		double cmwap_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.cmwap_queryPre_bx_countMoney20(paramMap);// 20元保险利润
		double cmwap_bx_countMoney = cmwap_bx_countMoney10 + cmwap_bx_countMoney20; // 保险总利润
		int cmwap_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		int cmwap_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数

		double cmwap_vip_sbl;// VIP失败率
		String cmwap_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double cmwap_succeed_oddsStr;// 出票成功转化率
		int cmwap_succeed_defeated_count = cmwap_out_ticket_succeed + cmwap_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLcmwap = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timecmwap = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double cmwap_ticket_cgl;// 订单成功率
		double cmwap_ticket_sbl;// 订单失败率
		String cmwap_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String cmwap_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String cmwap_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (cmwap_vip_count != 0) {
			cmwap_vip_sbl = ((double) cmwap_vip_lose / (double) cmwap_vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			cmwap_succeed_vip_sbl = SwitchUtils.format_perCent(cmwap_vip_sbl);
		} else {
			cmwap_succeed_vip_sbl = "0.00%";
		}

		if (cmwap_order_count != 0) {
			cmwap_succeed_oddsStr = ((double) cmwap_out_ticket_succeed / (double) cmwap_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			cmwap_succeed_odds = SwitchUtils.format_perCent(cmwap_succeed_oddsStr);
		} else {
			cmwap_succeed_odds = "0.00%"; // 出票成功率
		}

		if (cmwap_succeed_defeated_count != 0) {
			cmwap_ticket_cgl = ((double) cmwap_out_ticket_succeed / (double) cmwap_succeed_defeated_count);// 订单成功率
			cmwap_ticket_sbl = ((double) cmwap_out_ticket_defeated / (double) cmwap_succeed_defeated_count);// 订单失败率
			cmwap_succeed_cgl = SwitchUtils.format_perCent(cmwap_ticket_cgl);
			cmwap_succeed_sbl = SwitchUtils.format_perCent(cmwap_ticket_sbl);
		} else {
			cmwap_succeed_cgl = "0.00%";
			cmwap_succeed_sbl = "0.00%";
		}

		Map<String, Object> cmwap_map = new HashMap<String, Object>();
		cmwap_map.put("tj_id", CreateIDUtil.createID("TJ"));
		cmwap_map.put("out_ticket_succeed", cmwap_out_ticket_succeed);
		cmwap_map.put("out_ticket_defeated", cmwap_out_ticket_defeated);
		cmwap_map.put("order_count", cmwap_order_count);
		cmwap_map.put("succeed_money", cmwap_succeed_money);
		cmwap_map.put("defeated_money", cmwap_defeated_money);
		cmwap_map.put("refund_money", cmwap_refund_money);
		cmwap_map.put("succeed_odds", cmwap_succeed_odds);
		cmwap_map.put("order_time", create_time);
		cmwap_map.put("refund_count", cmwap_refund_count);
		cmwap_map.put("ticket_count", cmwap_ticket_count);
		cmwap_map.put("succeed_cgl", cmwap_succeed_cgl);
		cmwap_map.put("succeed_sbl", cmwap_succeed_sbl);
		cmwap_map.put("out_ticket_XL", out_ticket_XLcmwap);
		cmwap_map.put("pay_time", pay_timecmwap);
		cmwap_map.put("over_time", cmwap_over_time);
		cmwap_map.put("preparative_count", cmwap_preparative_count);
		cmwap_map.put("pay_defeated", cmwap_pay_defeated);
		cmwap_map.put("change_money", cmwap_change_money);
		cmwap_map.put("bx_count", cmwap_bx_count);
		cmwap_map.put("bx_countMoney", cmwap_bx_countMoney);
		cmwap_map.put("vip_lose", cmwap_vip_lose);
		cmwap_map.put("succeed_vip_sbl", cmwap_succeed_vip_sbl);
		// cmwap_map.put("active", active);
		cmwap_map.put("channel", "cmwap");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todaycmwapCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(cmwap_map);
		if (todaycmwapCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(cmwap_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(cmwap_map);
		}
		/************************************ cmwap统计结束 *****************************************/
		
		/************************************ app统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "app");
		int app_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.app_query_day_out_ticket_succeed(paramMap);// 订单成功数
		int app_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.app_query_day_out_ticket_defeated(paramMap);// 订单失败数
		int app_preparative_count = tj_Hc_orderInfo_Today_Service.app_queryPre_preparative_count(paramMap);// 预下单
		int app_pay_defeated = tj_Hc_orderInfo_Today_Service.app_queryPre_pay_defeated(paramMap);// 支付失败的个数
		int app_order_count = tj_Hc_orderInfo_Today_Service.app_query_day_order_count(paramMap);// 总订单数
		int app_refund_count = tj_Hc_orderInfo_Today_Service.app_query_refund_count(paramMap);// 退款成功
		double app_succeed_money = tj_Hc_orderInfo_Today_Service.app_query_succeed_money(paramMap);// 出票成功的总金额
		double app_defeated_money = tj_Hc_orderInfo_Today_Service.app_query_defeated_money(paramMap);// 出票失败的总金额
		double app_change_money = tj_Hc_orderInfo_Today_Service.app_queryPre_change_money(paramMap);// 改签差价
		double app_refund_money = tj_Hc_orderInfo_Today_Service.app_query_refund_money(paramMap);// 查询总退款金额
		int app_ticket_count = tj_Hc_orderInfo_Today_Service.app_query_ticket_count(paramMap);// 票数总计
		int app_over_time = tj_Hc_orderInfo_Today_Service.app_over_time(paramMap); // 超过十分钟订单数
		int app_bx_count = tj_Hc_orderInfo_Today_Service.app_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
		paramMap.put("bxMoney", 10);
		double app_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.app_queryPre_bx_countMoney10(paramMap);// 10元保险利润
		paramMap.put("bxMoney", 20);
		double app_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.app_queryPre_bx_countMoney20(paramMap);// 20元保险利润
		double app_bx_countMoney = app_bx_countMoney10 + app_bx_countMoney20; // 保险总利润
		int app_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		int app_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数

		double app_vip_sbl;// VIP失败率
		String app_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double app_succeed_oddsStr;// 出票成功转化率
		int app_succeed_defeated_count = app_out_ticket_succeed	+ app_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLApp = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timeApp = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double app_ticket_cgl;// 订单成功率
		double app_ticket_sbl;// 订单失败率
		String app_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String app_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String app_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (app_vip_count != 0) {
			app_vip_sbl = ((double) app_vip_lose / (double) app_vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			app_succeed_vip_sbl = SwitchUtils.format_perCent(app_vip_sbl);
		} else {
			app_succeed_vip_sbl = "0.00%";
		}

		if (app_order_count != 0) {
			app_succeed_oddsStr = ((double) app_out_ticket_succeed / (double) app_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			app_succeed_odds = SwitchUtils.format_perCent(app_succeed_oddsStr);
		} else {
			app_succeed_odds = "0.00%"; // 出票成功率
		}

		if (app_succeed_defeated_count != 0) {
			app_ticket_cgl = ((double) app_out_ticket_succeed / (double) app_succeed_defeated_count);// 订单成功率
			app_ticket_sbl = ((double) app_out_ticket_defeated / (double) app_succeed_defeated_count);// 订单失败率
			app_succeed_cgl = SwitchUtils.format_perCent(app_ticket_cgl);
			app_succeed_sbl = SwitchUtils.format_perCent(app_ticket_sbl);
		} else {
			app_succeed_cgl = "0.00%";
			app_succeed_sbl = "0.00%";
		}

		Map<String, Object> app_map = new HashMap<String, Object>();
		app_map.put("tj_id", CreateIDUtil.createID("TJ"));
		app_map.put("out_ticket_succeed", app_out_ticket_succeed);
		app_map.put("out_ticket_defeated", app_out_ticket_defeated);
		app_map.put("order_count", app_order_count);
		app_map.put("succeed_money", app_succeed_money);
		app_map.put("defeated_money", app_defeated_money);
		app_map.put("refund_money", app_refund_money);
		app_map.put("succeed_odds", app_succeed_odds);
		app_map.put("order_time", create_time);
		app_map.put("refund_count", app_refund_count);
		app_map.put("ticket_count", app_ticket_count);
		app_map.put("succeed_cgl", app_succeed_cgl);
		app_map.put("succeed_sbl", app_succeed_sbl);
		app_map.put("out_ticket_XL", out_ticket_XLApp);
		app_map.put("pay_time", pay_timeApp);
		app_map.put("over_time", app_over_time);
		app_map.put("preparative_count", app_preparative_count);
		app_map.put("pay_defeated", app_pay_defeated);
		app_map.put("change_money", app_change_money);
		app_map.put("bx_count", app_bx_count);
		app_map.put("bx_countMoney", app_bx_countMoney);
		app_map.put("vip_lose", app_vip_lose);
		app_map.put("succeed_vip_sbl", app_succeed_vip_sbl);
		// app_map.put("active", active);
		app_map.put("channel", "app");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todayappCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(app_map);
		if (todayappCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(app_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(app_map);
		}
		/************************************ app统计结束 *****************************************/
		
		/************************************ web统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "web");
		int web_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.app_query_day_out_ticket_succeed(paramMap);// 订单成功数
		int web_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.app_query_day_out_ticket_defeated(paramMap);// 订单失败数
		int web_preparative_count = tj_Hc_orderInfo_Today_Service.app_queryPre_preparative_count(paramMap);// 预下单
		int web_pay_defeated = tj_Hc_orderInfo_Today_Service.app_queryPre_pay_defeated(paramMap);// 支付失败的个数
		int web_order_count = tj_Hc_orderInfo_Today_Service.app_query_day_order_count(paramMap);// 总订单数
		int web_refund_count = tj_Hc_orderInfo_Today_Service.app_query_refund_count(paramMap);// 退款成功
		double web_succeed_money = tj_Hc_orderInfo_Today_Service.app_query_succeed_money(paramMap);// 出票成功的总金额
		double web_defeated_money = tj_Hc_orderInfo_Today_Service.app_query_defeated_money(paramMap);// 出票失败的总金额
		double web_change_money = tj_Hc_orderInfo_Today_Service.app_queryPre_change_money(paramMap);// 改签差价
		double web_refund_money = tj_Hc_orderInfo_Today_Service.app_query_refund_money(paramMap);// 查询总退款金额
		int web_ticket_count = tj_Hc_orderInfo_Today_Service.app_query_ticket_count(paramMap);// 票数总计
		int web_over_time = tj_Hc_orderInfo_Today_Service.app_over_time(paramMap); // 超过十分钟订单数
		int web_bx_count = tj_Hc_orderInfo_Today_Service.app_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
		paramMap.put("bxMoney", 10);
		double web_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.app_queryPre_bx_countMoney10(paramMap);// 10元保险利润
		paramMap.put("bxMoney", 20);
		double web_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.app_queryPre_bx_countMoney20(paramMap);// 20元保险利润
		double web_bx_countMoney = web_bx_countMoney10 + web_bx_countMoney20; // 保险总利润
		int web_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		int web_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数

		double web_vip_sbl;// VIP失败率
		String web_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double web_succeed_oddsStr;// 出票成功转化率
		int web_succeed_defeated_count = web_out_ticket_succeed	+ web_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLweb = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timeweb = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double web_ticket_cgl;// 订单成功率
		double web_ticket_sbl;// 订单失败率
		String web_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String web_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String web_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (web_vip_count != 0) {
			web_vip_sbl = ((double) web_vip_lose / (double) web_vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			web_succeed_vip_sbl = SwitchUtils.format_perCent(web_vip_sbl);
		} else {
			web_succeed_vip_sbl = "0.00%";
		}

		if (web_order_count != 0) {
			web_succeed_oddsStr = ((double) web_out_ticket_succeed / (double) web_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			web_succeed_odds = SwitchUtils.format_perCent(web_succeed_oddsStr);
		} else {
			web_succeed_odds = "0.00%"; // 出票成功率
		}

		if (web_succeed_defeated_count != 0) {
			web_ticket_cgl = ((double) web_out_ticket_succeed / (double) web_succeed_defeated_count);// 订单成功率
			web_ticket_sbl = ((double) web_out_ticket_defeated / (double) web_succeed_defeated_count);// 订单失败率
			web_succeed_cgl = SwitchUtils.format_perCent(web_ticket_cgl);
			web_succeed_sbl = SwitchUtils.format_perCent(web_ticket_sbl);
		} else {
			web_succeed_cgl = "0.00%";
			web_succeed_sbl = "0.00%";
		}

		Map<String, Object> web_map = new HashMap<String, Object>();
		web_map.put("tj_id", CreateIDUtil.createID("TJ"));
		web_map.put("out_ticket_succeed", web_out_ticket_succeed);
		web_map.put("out_ticket_defeated", web_out_ticket_defeated);
		web_map.put("order_count", web_order_count);
		web_map.put("succeed_money", web_succeed_money);
		web_map.put("defeated_money", web_defeated_money);
		web_map.put("refund_money", web_refund_money);
		web_map.put("succeed_odds", web_succeed_odds);
		web_map.put("order_time", create_time);
		web_map.put("refund_count", web_refund_count);
		web_map.put("ticket_count", web_ticket_count);
		web_map.put("succeed_cgl", web_succeed_cgl);
		web_map.put("succeed_sbl", web_succeed_sbl);
		web_map.put("out_ticket_XL", out_ticket_XLweb);
		web_map.put("pay_time", pay_timeweb);
		web_map.put("over_time", web_over_time);
		web_map.put("preparative_count", web_preparative_count);
		web_map.put("pay_defeated", web_pay_defeated);
		web_map.put("change_money", web_change_money);
		web_map.put("bx_count", web_bx_count);
		web_map.put("bx_countMoney", web_bx_countMoney);
		web_map.put("vip_lose", web_vip_lose);
		web_map.put("succeed_vip_sbl", web_succeed_vip_sbl);
		// web_map.put("active", active);
		web_map.put("channel", "web");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todaywebCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(web_map);
		if (todaywebCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(web_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(web_map);
		}
		/************************************ web统计结束 *****************************************/
		
		
		/************************************ weixin统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "weixin");
		int weixin_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.app_query_day_out_ticket_succeed(paramMap);// 订单成功数
		int weixin_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.app_query_day_out_ticket_defeated(paramMap);// 订单失败数
		int weixin_preparative_count = tj_Hc_orderInfo_Today_Service.app_queryPre_preparative_count(paramMap);// 预下单
		int weixin_pay_defeated = tj_Hc_orderInfo_Today_Service.app_queryPre_pay_defeated(paramMap);// 支付失败的个数
		int weixin_order_count = tj_Hc_orderInfo_Today_Service.app_query_day_order_count(paramMap);// 总订单数
		int weixin_refund_count = tj_Hc_orderInfo_Today_Service.app_query_refund_count(paramMap);// 退款成功
		double weixin_succeed_money = tj_Hc_orderInfo_Today_Service.app_query_succeed_money(paramMap);// 出票成功的总金额
		double weixin_defeated_money = tj_Hc_orderInfo_Today_Service.app_query_defeated_money(paramMap);// 出票失败的总金额
		double weixin_change_money = tj_Hc_orderInfo_Today_Service.app_queryPre_change_money(paramMap);// 改签差价
		double weixin_refund_money = tj_Hc_orderInfo_Today_Service.app_query_refund_money(paramMap);// 查询总退款金额
		int weixin_ticket_count = tj_Hc_orderInfo_Today_Service.app_query_ticket_count(paramMap);// 票数总计
		int weixin_over_time = tj_Hc_orderInfo_Today_Service.app_over_time(paramMap); // 超过十分钟订单数
		int weixin_bx_count = tj_Hc_orderInfo_Today_Service.app_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
		paramMap.put("bxMoney", 10);
		double weixin_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.app_queryPre_bx_countMoney10(paramMap);// 10元保险利润
		paramMap.put("bxMoney", 20);
		double weixin_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.app_queryPre_bx_countMoney20(paramMap);// 20元保险利润
		double weixin_bx_countMoney = weixin_bx_countMoney10 + weixin_bx_countMoney20; // 保险总利润
		int weixin_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		int weixin_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数

		double weixin_vip_sbl;// VIP失败率
		String weixin_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double weixin_succeed_oddsStr;// 出票成功转化率
		int weixin_succeed_defeated_count = weixin_out_ticket_succeed	+ weixin_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double weixin_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double weixin_pay_time = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double weixin_ticket_cgl;// 订单成功率
		double weixin_ticket_sbl;// 订单失败率
		String weixin_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String weixin_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String weixin_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (weixin_vip_count != 0) {
			weixin_vip_sbl = ((double) weixin_vip_lose / (double) weixin_vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			weixin_succeed_vip_sbl = SwitchUtils.format_perCent(weixin_vip_sbl);
		} else {
			weixin_succeed_vip_sbl = "0.00%";
		}

		if (weixin_order_count != 0) {
			weixin_succeed_oddsStr = ((double) weixin_out_ticket_succeed / (double) weixin_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			weixin_succeed_odds = SwitchUtils.format_perCent(weixin_succeed_oddsStr);
		} else {
			weixin_succeed_odds = "0.00%"; // 出票成功率
		}

		if (weixin_succeed_defeated_count != 0) {
			weixin_ticket_cgl = ((double) weixin_out_ticket_succeed / (double) weixin_succeed_defeated_count);// 订单成功率
			weixin_ticket_sbl = ((double) weixin_out_ticket_defeated / (double) weixin_succeed_defeated_count);// 订单失败率
			weixin_succeed_cgl = SwitchUtils.format_perCent(weixin_ticket_cgl);
			weixin_succeed_sbl = SwitchUtils.format_perCent(weixin_ticket_sbl);
		} else {
			weixin_succeed_cgl = "0.00%";
			weixin_succeed_sbl = "0.00%";
		}

		Map<String, Object> weixin_map = new HashMap<String, Object>();
		weixin_map.put("tj_id", CreateIDUtil.createID("TJ"));
		weixin_map.put("out_ticket_succeed", weixin_out_ticket_succeed);
		weixin_map.put("out_ticket_defeated", weixin_out_ticket_defeated);
		weixin_map.put("order_count", weixin_order_count);
		weixin_map.put("succeed_money", weixin_succeed_money);
		weixin_map.put("defeated_money", weixin_defeated_money);
		weixin_map.put("refund_money", weixin_refund_money);
		weixin_map.put("succeed_odds", weixin_succeed_odds);
		weixin_map.put("order_time", create_time);
		weixin_map.put("refund_count", weixin_refund_count);
		weixin_map.put("ticket_count", weixin_ticket_count);
		weixin_map.put("succeed_cgl", weixin_succeed_cgl);
		weixin_map.put("succeed_sbl", weixin_succeed_sbl);
		weixin_map.put("out_ticket_XL", weixin_out_ticket_XL);
		weixin_map.put("pay_time", weixin_pay_time);
		weixin_map.put("over_time", weixin_over_time);
		weixin_map.put("preparative_count", weixin_preparative_count);
		weixin_map.put("pay_defeated", weixin_pay_defeated);
		weixin_map.put("change_money", weixin_change_money);
		weixin_map.put("bx_count", weixin_bx_count);
		weixin_map.put("bx_countMoney", weixin_bx_countMoney);
		weixin_map.put("vip_lose", weixin_vip_lose);
		weixin_map.put("succeed_vip_sbl", weixin_succeed_vip_sbl);
		weixin_map.put("channel", "weixin");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int weixin_todayCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(weixin_map);
		if (weixin_todayCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(weixin_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(weixin_map);
		}
		/************************************ weixin统计结束 *****************************************/
		/************************************ ccb统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "ccb");
		int ccb_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.ccb_query_day_out_ticket_succeed(paramMap);// 订单成功数
		int ccb_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.ccb_query_day_out_ticket_defeated(paramMap);// 订单失败数
		int ccb_preparative_count = tj_Hc_orderInfo_Today_Service.ccb_queryPre_preparative_count(paramMap);// 预下单
		int ccb_pay_defeated = tj_Hc_orderInfo_Today_Service.ccb_queryPre_pay_defeated(paramMap);// 支付失败的个数
		int ccb_order_count = tj_Hc_orderInfo_Today_Service.ccb_query_day_order_count(paramMap);// 总订单数
		int ccb_refund_count = tj_Hc_orderInfo_Today_Service.ccb_query_refund_count(paramMap);// 退款成功
		double ccb_succeed_money = tj_Hc_orderInfo_Today_Service.ccb_query_succeed_money(paramMap);// 出票成功的总金额
		double ccb_defeated_money = tj_Hc_orderInfo_Today_Service.ccb_query_defeated_money(paramMap);// 出票失败的总金额
		double ccb_change_money = tj_Hc_orderInfo_Today_Service.ccb_queryPre_change_money(paramMap);// 改签差价
		double ccb_refund_money = tj_Hc_orderInfo_Today_Service.ccb_query_refund_money(paramMap);// 查询总退款金额
		int ccb_ticket_count = tj_Hc_orderInfo_Today_Service.ccb_query_ticket_count(paramMap);// 票数总计
		int ccb_over_time = tj_Hc_orderInfo_Today_Service.ccb_over_time(paramMap); // 超过十分钟订单数
		int ccb_bx_count = tj_Hc_orderInfo_Today_Service.ccb_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
		paramMap.put("bxMoney", 10);
		double ccb_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.ccb_queryPre_bx_countMoney10(paramMap);// 10元保险利润
		paramMap.put("bxMoney", 20);
		double ccb_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.ccb_queryPre_bx_countMoney20(paramMap);// 20元保险利润
		double ccb_bx_countMoney = ccb_bx_countMoney10 + ccb_bx_countMoney20; // 保险总利润
		int ccb_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		int ccb_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数

		double ccb_vip_sbl;// VIP失败率
		String ccb_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double ccb_succeed_oddsStr;// 出票成功转化率
		int ccb_succeed_defeated_count = ccb_out_ticket_succeed + ccb_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLCCB = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timeCCB = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double ccb_ticket_cgl;// 订单成功率
		double ccb_ticket_sbl;// 订单失败率
		String ccb_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String ccb_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String ccb_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (ccb_vip_count != 0) {
			ccb_vip_sbl = ((double) ccb_vip_lose / (double) ccb_vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			ccb_succeed_vip_sbl = SwitchUtils.format_perCent(ccb_vip_sbl);
		} else {
			ccb_succeed_vip_sbl = "0.00%";
		}

		if (ccb_order_count != 0) {
			ccb_succeed_oddsStr = ((double) ccb_out_ticket_succeed / (double) ccb_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			ccb_succeed_odds = SwitchUtils.format_perCent(ccb_succeed_oddsStr);
		} else {
			ccb_succeed_odds = "0.00%"; // 出票成功率
		}

		if (ccb_succeed_defeated_count != 0) {
			ccb_ticket_cgl = ((double) ccb_out_ticket_succeed / (double) ccb_succeed_defeated_count);// 订单成功率
			ccb_ticket_sbl = ((double) ccb_out_ticket_defeated / (double) ccb_succeed_defeated_count);// 订单失败率
			ccb_succeed_cgl = SwitchUtils.format_perCent(ccb_ticket_cgl);
			ccb_succeed_sbl = SwitchUtils.format_perCent(ccb_ticket_sbl);
		} else {
			ccb_succeed_cgl = "0.00%";
			ccb_succeed_sbl = "0.00%";
		}

		Map<String, Object> ccb_map = new HashMap<String, Object>();
		ccb_map.put("tj_id", CreateIDUtil.createID("TJ"));
		ccb_map.put("out_ticket_succeed", ccb_out_ticket_succeed);
		ccb_map.put("out_ticket_defeated", ccb_out_ticket_defeated);
		ccb_map.put("order_count", ccb_order_count);
		ccb_map.put("succeed_money", ccb_succeed_money);
		ccb_map.put("defeated_money", ccb_defeated_money);
		ccb_map.put("refund_money", ccb_refund_money);
		ccb_map.put("succeed_odds", ccb_succeed_odds);
		ccb_map.put("order_time", create_time);
		ccb_map.put("refund_count", ccb_refund_count);
		ccb_map.put("ticket_count", ccb_ticket_count);
		ccb_map.put("succeed_cgl", ccb_succeed_cgl);
		ccb_map.put("succeed_sbl", ccb_succeed_sbl);
		ccb_map.put("out_ticket_XL", out_ticket_XLCCB);
		ccb_map.put("pay_time", pay_timeCCB);
		ccb_map.put("over_time", ccb_over_time);
		ccb_map.put("preparative_count", ccb_preparative_count);
		ccb_map.put("pay_defeated", ccb_pay_defeated);
		ccb_map.put("change_money", ccb_change_money);
		ccb_map.put("bx_count", ccb_bx_count);
		ccb_map.put("bx_countMoney", ccb_bx_countMoney);
		ccb_map.put("vip_lose", ccb_vip_lose);
		ccb_map.put("succeed_vip_sbl", ccb_succeed_vip_sbl);
		// ccb_map.put("active", active);
		ccb_map.put("channel", "ccb");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todayccbCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(ccb_map);
		if (todayccbCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(ccb_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(ccb_map);
		}
		/************************************ ccb统计结束 *****************************************/
		
		/************************************ chq统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "chq");
		int chq_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.chq_query_day_out_ticket_succeed(paramMap);// 订单成功数
		int chq_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.chq_query_day_out_ticket_defeated(paramMap);// 订单失败数
		int chq_preparative_count = tj_Hc_orderInfo_Today_Service.chq_queryPre_preparative_count(paramMap);// 预下单
		int chq_pay_defeated = tj_Hc_orderInfo_Today_Service.chq_queryPre_pay_defeated(paramMap);// 支付失败的个数
		int chq_order_count = tj_Hc_orderInfo_Today_Service.chq_query_day_order_count(paramMap);// 总订单数
		int chq_refund_count = tj_Hc_orderInfo_Today_Service.chq_query_refund_count(paramMap);// 退款成功
		double chq_succeed_money = tj_Hc_orderInfo_Today_Service.chq_query_succeed_money(paramMap);// 出票成功的总金额
		double chq_defeated_money = tj_Hc_orderInfo_Today_Service.chq_query_defeated_money(paramMap);// 出票失败的总金额
		double chq_change_money = tj_Hc_orderInfo_Today_Service.chq_queryPre_change_money(paramMap);// 改签差价
		double chq_refund_money = tj_Hc_orderInfo_Today_Service.chq_query_refund_money(paramMap);// 查询总退款金额
		int chq_ticket_count = tj_Hc_orderInfo_Today_Service.chq_query_ticket_count(paramMap);// 票数总计
		int chq_over_time = tj_Hc_orderInfo_Today_Service.chq_over_time(paramMap); // 超过十分钟订单数
		int chq_bx_count = tj_Hc_orderInfo_Today_Service.chq_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
		//paramMap.put("bxMoney", 10);
		//double chq_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.chq_queryPre_bx_countMoney10(paramMap);// 10元保险利润
		paramMap.put("bxMoney", 20);
		double chq_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.chq_queryPre_bx_countMoney20(paramMap);// 20元保险利润
		double chq_bx_countMoney =chq_bx_countMoney20; // 保险总利润
		int chq_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		int chq_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数

		double chq_vip_sbl;// VIP失败率
		String chq_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double chq_succeed_oddsStr;// 出票成功转化率
		int chq_succeed_defeated_count = chq_out_ticket_succeed + chq_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLchq = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timechq = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double chq_ticket_cgl;// 订单成功率
		double chq_ticket_sbl;// 订单失败率
		String chq_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String chq_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String chq_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (chq_vip_count != 0) {
			chq_vip_sbl = ((double) chq_vip_lose / (double) chq_vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			chq_succeed_vip_sbl = SwitchUtils.format_perCent(chq_vip_sbl);
		} else {
			chq_succeed_vip_sbl = "0.00%";
		}

		if (chq_order_count != 0) {
			chq_succeed_oddsStr = ((double) chq_out_ticket_succeed / (double) chq_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			chq_succeed_odds = SwitchUtils.format_perCent(chq_succeed_oddsStr);
		} else {
			chq_succeed_odds = "0.00%"; // 出票成功率
		}

		if (chq_succeed_defeated_count != 0) {
			chq_ticket_cgl = ((double) chq_out_ticket_succeed / (double) chq_succeed_defeated_count);// 订单成功率
			chq_ticket_sbl = ((double) chq_out_ticket_defeated / (double) chq_succeed_defeated_count);// 订单失败率
			chq_succeed_cgl = SwitchUtils.format_perCent(chq_ticket_cgl);
			chq_succeed_sbl = SwitchUtils.format_perCent(chq_ticket_sbl);
		} else {
			chq_succeed_cgl = "0.00%";
			chq_succeed_sbl = "0.00%";
		}

		Map<String, Object> chq_map = new HashMap<String, Object>();
		chq_map.put("tj_id", CreateIDUtil.createID("TJ"));
		chq_map.put("out_ticket_succeed", chq_out_ticket_succeed);
		chq_map.put("out_ticket_defeated", chq_out_ticket_defeated);
		chq_map.put("order_count", chq_order_count);
		chq_map.put("succeed_money", chq_succeed_money);
		chq_map.put("defeated_money", chq_defeated_money);
		chq_map.put("succeed_odds", chq_succeed_odds);
		chq_map.put("order_time", create_time);
		chq_map.put("refund_count", chq_refund_count);
		chq_map.put("ticket_count", chq_ticket_count);
		chq_map.put("succeed_cgl", chq_succeed_cgl);
		chq_map.put("succeed_sbl", chq_succeed_sbl);
		chq_map.put("out_ticket_XL", out_ticket_XLchq);
		chq_map.put("pay_time", pay_timechq);
		chq_map.put("over_time", chq_over_time);
		chq_map.put("preparative_count", chq_preparative_count);
		chq_map.put("pay_defeated", chq_pay_defeated);
		chq_map.put("change_money", chq_change_money);
		chq_map.put("refund_money", chq_refund_money);
		chq_map.put("bx_count", chq_bx_count);
		chq_map.put("bx_countMoney", chq_bx_countMoney);
		chq_map.put("vip_lose", chq_vip_lose);
		chq_map.put("succeed_vip_sbl", chq_succeed_vip_sbl);
		// chq_map.put("active", active);
		chq_map.put("channel", "chq");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todaychqCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(chq_map);
		if (todaychqCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(chq_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(chq_map);
		}
		/************************************ chq统计结束 *****************************************/
		
		/************************************ inner统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "inner");
		int inner_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.inner_query_day_out_ticket_succeed(paramMap);// 订单成功数
		int inner_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.inner_query_day_out_ticket_defeated(paramMap);// 订单失败数
		int inner_preparative_count = tj_Hc_orderInfo_Today_Service.inner_queryPre_preparative_count(paramMap);// 预下单
		int inner_pay_defeated = tj_Hc_orderInfo_Today_Service.inner_queryPre_pay_defeated(paramMap);// 支付失败的个数
		int inner_order_count = tj_Hc_orderInfo_Today_Service.inner_query_day_order_count(paramMap);// 总订单数
		int inner_refund_count = tj_Hc_orderInfo_Today_Service.inner_query_refund_count(paramMap);// 退款成功
		double inner_succeed_money = tj_Hc_orderInfo_Today_Service.inner_query_succeed_money(paramMap);// 出票成功的总金额
		double inner_defeated_money = tj_Hc_orderInfo_Today_Service.inner_query_defeated_money(paramMap);// 出票失败的总金额
		double inner_change_money = tj_Hc_orderInfo_Today_Service.inner_queryPre_change_money(paramMap);// 改签差价
		double inner_refund_money = tj_Hc_orderInfo_Today_Service.inner_query_refund_money(paramMap);// 查询总退款金额
		int inner_ticket_count = tj_Hc_orderInfo_Today_Service.inner_query_ticket_count(paramMap);// 票数总计
		int inner_over_time = tj_Hc_orderInfo_Today_Service.inner_over_time(paramMap); // 超过十分钟订单数
		int inner_bx_count = tj_Hc_orderInfo_Today_Service.inner_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
//		paramMap.put("bxMoney", 10);
//		double inner_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.inner_queryPre_bx_countMoney10(paramMap);// 10元保险利润
//		paramMap.put("bxMoney", 20);
//		double inner_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.inner_queryPre_bx_countMoney20(paramMap);// 20元保险利润
		double inner_bx_countMoney = chq_bx_countMoney + ccb_bx_countMoney+cmpay_bx_countMoney+pay_bx_countMoney; // 保险总利润
		int inner_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		int inner_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数
		double inner_vip_sbl;// VIP失败率
		String inner_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double inner_succeed_oddsStr;// 出票成功转化率
		int inner_succeed_defeated_count = inner_out_ticket_succeed + inner_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double out_ticket_XLInner = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double pay_timeInner = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double inner_ticket_cgl;// 订单成功率
		double inner_ticket_sbl;// 订单失败率
		String inner_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String inner_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String inner_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (inner_vip_count != 0) {
			inner_vip_sbl = ((double) inner_vip_lose / (double) inner_vip_count); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			inner_succeed_vip_sbl = SwitchUtils.format_perCent(inner_vip_sbl);
		} else {
			inner_succeed_vip_sbl = "0.00%";
		}

		if (inner_order_count != 0) {
			inner_succeed_oddsStr = ((double) inner_out_ticket_succeed / (double) inner_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			inner_succeed_odds = SwitchUtils.format_perCent(inner_succeed_oddsStr);
		} else {
			inner_succeed_odds = "0.00%"; // 出票成功率
		}

		if (inner_succeed_defeated_count != 0) {
			inner_ticket_cgl = ((double) inner_out_ticket_succeed / (double) inner_succeed_defeated_count);// 订单成功率
			inner_ticket_sbl = ((double) inner_out_ticket_defeated / (double) inner_succeed_defeated_count);// 订单失败率
			inner_succeed_cgl = SwitchUtils.format_perCent(inner_ticket_cgl);
			inner_succeed_sbl = SwitchUtils.format_perCent(inner_ticket_sbl);
		} else {
			inner_succeed_cgl = "0.00%";
			inner_succeed_sbl = "0.00%";
		}

		Map<String, Object> inner_map = new HashMap<String, Object>();
		inner_map.put("tj_id", CreateIDUtil.createID("TJ"));
		inner_map.put("out_ticket_succeed", inner_out_ticket_succeed);
		inner_map.put("out_ticket_defeated", inner_out_ticket_defeated);
		inner_map.put("order_count", inner_order_count);
		inner_map.put("succeed_money", inner_succeed_money);
		inner_map.put("defeated_money", inner_defeated_money);
		inner_map.put("succeed_odds", inner_succeed_odds);
		inner_map.put("order_time", create_time);
		inner_map.put("refund_count", inner_refund_count);
		inner_map.put("ticket_count", inner_ticket_count);
		inner_map.put("succeed_cgl", inner_succeed_cgl);
		inner_map.put("succeed_sbl", inner_succeed_sbl);
		inner_map.put("over_time", inner_over_time);
		inner_map.put("preparative_count", inner_preparative_count);
		inner_map.put("pay_defeated", inner_pay_defeated);
		inner_map.put("change_money", inner_change_money);
		inner_map.put("refund_money", inner_refund_money);
		inner_map.put("bx_count", inner_bx_count);//出售保险个数
		inner_map.put("bx_countMoney", inner_bx_countMoney);//保险金额
		inner_map.put("vip_lose", inner_vip_lose);
		inner_map.put("succeed_vip_sbl", inner_succeed_vip_sbl);
		inner_map.put("out_ticket_XL", out_ticket_XLInner);
		inner_map.put("pay_time", pay_timeInner);
		inner_map.put("channel", "inner");
		// pay_map.put("active", active);
//		inner_map.put("channel", "19pay");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todayInnerCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(inner_map);
		if (todayInnerCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(inner_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(inner_map);
		}
		/************************************ inner统计结束 *****************************************/
		
		
		/****************************** 对外商户ext--各个商户分开统计数据  统计开始**********************************/
		int ext_over_time_count = 0;//对外商户总的超时十分钟的订单
		int ext_vip_lose_count = 0;//对外商户总的VIP失败订单数
		int ext_vip_count_sum = 0;//对外商户总的vip订单数
		double ext_bx_countAllMoney=0.000;
		/**
		 * 新疆利安总数据
		 */
		int ext_lian_out_ticket_succeed=0;
		int ext_lian_out_ticket_defeated=0;
		int ext_lian_order_count=0;
		double ext_lian_succeed_money=0.000;
		double ext_lian_defeated_money=0.000;
		int ext_lian_refund_count=0;
		int ext_lian_ticket_count=0;
		int ext_lian_over_time=0;
		int ext_lian_preparative_count=0;
		int ext_lian_pay_defeated=0;
		double ext_lian_change_money=0.000;
		double ext_lian_refund_money=0.000;
		int ext_lian_bx_count=0;
		double ext_lian_bx_countMoney=0.000;
		int ext_lian_vip_lose =0;
		//查询对外商户的渠道（merchant_id）
		List<Map<String, String>> merchantIdList =  tj_Hc_orderInfo_Today_Service.queryMerchantId();
		
		List<String> merchantId_list = new ArrayList<String>();//存放对外商户的渠道
		for(int i=0; i<merchantIdList.size(); i++){
			String merchant_id = merchantIdList.get(i).get("merchant_id").toString();
			if("301030".equals(merchant_id) || "301031".equals(merchant_id)){
				logger.info("统计商户时，跳过高铁管家和携程。");
				continue;
			}
				merchantId_list.add(merchant_id);
			DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
			paramMap.put("channel", merchant_id);
			int ext_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.ext_query_day_out_ticket_succeed(paramMap);// 订单成功数
			int ext_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.ext_query_day_out_ticket_defeated(paramMap);// 订单失败数
			int ext_preparative_count = tj_Hc_orderInfo_Today_Service.ext_queryPre_preparative_count(paramMap);// 预下单
			int ext_pay_defeated = tj_Hc_orderInfo_Today_Service.ext_queryPre_pay_defeated(paramMap);// 支付失败的个数
			int ext_order_count = tj_Hc_orderInfo_Today_Service.ext_query_day_order_count(paramMap);// 总订单数
			int ext_refund_count = tj_Hc_orderInfo_Today_Service.ext_query_refund_count(paramMap);// 退款成功
			double ext_succeed_money = tj_Hc_orderInfo_Today_Service.ext_query_succeed_money(paramMap);// 出票成功的总金额
			double ext_defeated_money = tj_Hc_orderInfo_Today_Service.ext_query_defeated_money(paramMap);// 出票失败的总金额
			double ext_change_money = tj_Hc_orderInfo_Today_Service.ext_queryPre_change_money(paramMap);// 改签差价
			double ext_refund_money = tj_Hc_orderInfo_Today_Service.ext_query_refund_money(paramMap);// 查询总退款金额
			int ext_ticket_count = tj_Hc_orderInfo_Today_Service.ext_query_ticket_count(paramMap);// 票数总计
			int ext_over_time = tj_Hc_orderInfo_Today_Service.ext_over_time(paramMap); // 超过十分钟订单数
			ext_over_time_count += ext_over_time;
			int ext_bx_count = tj_Hc_orderInfo_Today_Service.ext_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
			
//			double ext_bx_countMoney = ext_bx_countMoney10 + ext_bx_countMoney20; // 保险总利润
		//商户各渠道利润分开统计
			double ext_bx_countMoney=0.000;
		
//			1.世纪：每份保险给我们6元，不管是10元还是20元
			if("301005".equals(merchant_id)){
				ext_bx_countMoney= ext_bx_count*6;
				ext_bx_countAllMoney+=ext_bx_countMoney;
			}
//			2.	捷成：每份保险给我们3元
			if("301006".equals(merchant_id)){
				ext_bx_countMoney= ext_bx_count*3;
				ext_bx_countAllMoney+=ext_bx_countMoney;
			}
//			3.	宝库：每张票（出票成功）给我们2元
			if("301008".equals(merchant_id)){
				ext_bx_countMoney= ext_ticket_count*2;
				ext_bx_countAllMoney+=ext_bx_countMoney;
			}
//			4.	山东赢联：梯形交易（给我们）
//			山东赢联梯形交易明细
//			服务名称	交易量（车票数）	技术服务费
//			单笔阶梯费用	5000张/日	0.5元/张
//				1000-5000张/日	0.7元/张
//				＜1000张/日	1.0元/张
			if("301009".equals(merchant_id)){
				if(ext_ticket_count>5000){
					ext_bx_countMoney=ext_ticket_count*0.5;
					ext_bx_countAllMoney+=ext_bx_countMoney;
				}else if(ext_ticket_count<1000){
					ext_bx_countMoney=ext_ticket_count*1;
					ext_bx_countAllMoney+=ext_bx_countMoney;
				}else{
					ext_bx_countMoney=ext_ticket_count*0.7;
					ext_bx_countAllMoney+=ext_bx_countMoney;
				}
			}
//			5.	金色：每笔订单给我们1元
			if("301012".equals(merchant_id)){
				ext_bx_countMoney= ext_out_ticket_succeed*1;
				ext_bx_countAllMoney+=ext_bx_countMoney;
			}
//			6.	517：梯形交易（给我们）
//			　	517梯形交易明细	　
//			服务名称	交易量（车票数）	技术服务费
//			单笔阶梯费用	每日5000张以上	0.6元/张车票
//				每日5000张以下	0.8元/张车票
			if("301013".equals(merchant_id)){
				if(ext_ticket_count>5000){
					ext_bx_countMoney=ext_ticket_count*0.6;
					ext_bx_countAllMoney+=ext_bx_countMoney;
				}else{
					ext_bx_countMoney=ext_ticket_count*0.8;
					ext_bx_countAllMoney+=ext_bx_countMoney;
				}
			}
//			7.	中源：①10元保险给我们5元；②20元保险给我们6元；③票价的千分之五
			if("301014".equals(merchant_id)){
				paramMap.put("bxMoney", 10);
				int ext_bx_count10 = tj_Hc_orderInfo_Today_Service.ext_queryPre_bx_count10(paramMap);// 10元保险个数
				paramMap.put("bxMoney", 20);
				int ext_bx_count20 = tj_Hc_orderInfo_Today_Service.ext_queryPre_bx_count20(paramMap);// 20元保险个数
			
				ext_bx_countMoney= ext_bx_count10*5+ext_bx_count20*6+ext_succeed_money*0.005;
				ext_bx_countAllMoney+=ext_bx_countMoney;
			}
//			8.新疆利安10元保险to our5元；20元保险to our6元
			if("301016".equals(merchant_id) ||"30101601".equals(merchant_id)||"30101602".equals(merchant_id)){
				paramMap.put("bxMoney", 10);
				int ext_bx_count10 = tj_Hc_orderInfo_Today_Service.ext_queryPre_bx_count10(paramMap);// 10元保险个数
				paramMap.put("bxMoney", 20);
				int ext_bx_count20 = tj_Hc_orderInfo_Today_Service.ext_queryPre_bx_count20(paramMap);// 20元保险个数
			
				ext_bx_countMoney= ext_bx_count10*5+ext_bx_count20*6;
				ext_bx_countAllMoney+=ext_bx_countMoney;
			}
		
			int ext_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
			ext_vip_lose_count += ext_vip_lose;
			int ext_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数
			ext_vip_count_sum += ext_vip_count;
			
			double ext_vip_sbl;// VIP失败率
			String ext_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
			double ext_succeed_oddsStr;// 出票成功转化率
			int ext_succeed_defeated_count = ext_out_ticket_succeed	+ ext_out_ticket_defeated;// 出票成功的个数加出票失败的个数
			double ext_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 出票效率
			double ext_pay_time = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

			double ext_ticket_cgl;// 订单成功率
			double ext_ticket_sbl;// 订单失败率
			String ext_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
			String ext_succeed_cgl = null;// 订单成功率(转换为0.00%后)
			String ext_succeed_sbl = null;// 订单失败率(转换为0.00%后)

			if (ext_vip_count != 0) {
				ext_vip_sbl = ((double) ext_vip_lose / (double) ext_vip_count); // VIP出票失败率
				// doble 转换为 String类型的“**.**%”
				ext_succeed_vip_sbl = SwitchUtils.format_perCent(ext_vip_sbl);
			} else {
				ext_succeed_vip_sbl = "0.00%";
			}

			if (ext_order_count != 0) {
				ext_succeed_oddsStr = ((double) ext_out_ticket_succeed / (double) ext_order_count); // 出票成功转化率
				// doble 转换为 String类型的“**.**%”
				ext_succeed_odds = SwitchUtils.format_perCent(ext_succeed_oddsStr);
			} else {
				ext_succeed_odds = "0.00%"; // 出票成功率
			}

			if (ext_succeed_defeated_count != 0) {
				ext_ticket_cgl = ((double) ext_out_ticket_succeed / (double) ext_succeed_defeated_count);// 订单成功率
				ext_ticket_sbl = ((double) ext_out_ticket_defeated / (double) ext_succeed_defeated_count);// 订单失败率
				ext_succeed_cgl = SwitchUtils.format_perCent(ext_ticket_cgl);
				ext_succeed_sbl = SwitchUtils.format_perCent(ext_ticket_sbl);
			} else {
				ext_succeed_cgl = "0.00%";
				ext_succeed_sbl = "0.00%";
			}

			Map<String, Object> ext_map = new HashMap<String, Object>();
			ext_map.put("tj_id", CreateIDUtil.createID("TJ"));
			ext_map.put("out_ticket_succeed", ext_out_ticket_succeed);
			ext_map.put("out_ticket_defeated", ext_out_ticket_defeated);
			ext_map.put("order_count", ext_order_count);
			ext_map.put("succeed_money", ext_succeed_money);
			ext_map.put("defeated_money", ext_defeated_money);
			ext_map.put("succeed_odds", ext_succeed_odds);
			ext_map.put("order_time", create_time);
			ext_map.put("refund_count", ext_refund_count);
			ext_map.put("ticket_count", ext_ticket_count);
			ext_map.put("succeed_cgl", ext_succeed_cgl);
			ext_map.put("succeed_sbl", ext_succeed_sbl);
			ext_map.put("out_ticket_XL", ext_out_ticket_XL);
			ext_map.put("pay_time", ext_pay_time);
			ext_map.put("over_time", ext_over_time);
			ext_map.put("preparative_count", ext_preparative_count);
			ext_map.put("pay_defeated", ext_pay_defeated);
			ext_map.put("change_money", ext_change_money);
			ext_map.put("refund_money", ext_refund_money);
			ext_map.put("bx_count", ext_bx_count);
			ext_map.put("bx_countMoney", ext_bx_countMoney);
			ext_map.put("vip_lose", ext_vip_lose);
			ext_map.put("succeed_vip_sbl", ext_succeed_vip_sbl);
			ext_map.put("channel", merchant_id);
		    //新疆利安总数据
			if("301016".equals(merchant_id) ||"30101601".equals(merchant_id)||"30101602".equals(merchant_id)){
				ext_lian_out_ticket_succeed+=ext_out_ticket_succeed;
				ext_lian_out_ticket_defeated+=ext_out_ticket_defeated;
				ext_lian_order_count+=ext_order_count;
				ext_lian_succeed_money+=ext_succeed_money;
				ext_lian_defeated_money+=ext_defeated_money;
				ext_lian_refund_count+=ext_refund_count;
				ext_lian_ticket_count+=ext_ticket_count;
				ext_lian_over_time+=ext_over_time;
				ext_lian_preparative_count+=ext_preparative_count;
				ext_lian_pay_defeated+=ext_pay_defeated;
				ext_lian_change_money+=ext_change_money;
				ext_lian_refund_money+=ext_refund_money;
				ext_lian_bx_count+=ext_bx_count;
			    ext_lian_bx_countMoney+=ext_bx_countMoney;
				ext_lian_vip_lose +=ext_vip_lose;
			}
			// TODO
			// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
			DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
			int ext_todayCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(ext_map);
			if(!"301016".equals(merchant_id)&&!"30101601".equals(merchant_id)&&!"30101602".equals(merchant_id)){
			if (ext_todayCount == 0) {
				// 添加到表tj_hc_orderInfo表中
				tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(ext_map);
			} else {
				// 更新tj_hc_orderInfo表中数据
				tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(ext_map);
			}
			}
		}
		/**************************** 对外商户ext--各个商户分开统计数据  统计结束  ****************************/
	
		/**************************** 对外商户新疆利安总的统计数据 统计开始  ****************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		double ext_lian_succeed_oddsStr;// 出票成功转化率
		String ext_lian_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		if (ext_lian_order_count != 0) {
			ext_lian_succeed_oddsStr = ((double) ext_lian_out_ticket_succeed / (double) ext_lian_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			ext_lian_succeed_odds = SwitchUtils.format_perCent(ext_lian_succeed_oddsStr);
		} else {
			ext_lian_succeed_odds = "0.00%"; // 出票成功率
		}
		double ext_lian_ticket_cgl;// 订单成功率
		double ext_lian_ticket_sbl;// 订单失败率
		String ext_lian_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String ext_lian_succeed_sbl = null;// 订单失败率(转换为0.00%后)
		int ext_lian_succeed_defeated_count = ext_lian_out_ticket_succeed	+ ext_lian_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		if (ext_lian_succeed_defeated_count != 0) {
			ext_lian_ticket_cgl = ((double) ext_lian_out_ticket_succeed / (double) ext_lian_succeed_defeated_count);// 订单成功率
			ext_lian_ticket_sbl = ((double) ext_lian_out_ticket_defeated / (double) ext_lian_succeed_defeated_count);// 订单失败率
			ext_lian_succeed_cgl = SwitchUtils.format_perCent(ext_lian_ticket_cgl);
			ext_lian_succeed_sbl = SwitchUtils.format_perCent(ext_lian_ticket_sbl);
		} else {
			ext_lian_succeed_cgl = "0.00%";
			ext_lian_succeed_sbl = "0.00%";
		}
		
		Map<String, Object> Map_lian = new HashMap<String, Object>();
		Map_lian.put("create_time", create_time);
		double ext_lian_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryExtOut_ticket_XL_lian(Map_lian);// 出票效率
		double ext_lian_pay_time = tj_Hc_orderInfo_Today_Service.queryExtPay_time_lian(Map_lian);// 支付时长
		Map<String, Object> lian_map = new HashMap<String, Object>();
		lian_map.put("tj_id", CreateIDUtil.createID("TJ"));
		lian_map.put("out_ticket_succeed", ext_lian_out_ticket_succeed);
		lian_map.put("out_ticket_defeated", ext_lian_out_ticket_defeated);
		lian_map.put("order_count", ext_lian_order_count);
		lian_map.put("succeed_money", ext_lian_succeed_money);
		lian_map.put("defeated_money", ext_lian_defeated_money);
		lian_map.put("succeed_odds", ext_lian_succeed_odds);
		lian_map.put("order_time", create_time);
		lian_map.put("refund_count", ext_lian_refund_count);
		lian_map.put("ticket_count", ext_lian_ticket_count);
		lian_map.put("succeed_cgl", ext_lian_succeed_cgl);
		lian_map.put("succeed_sbl", ext_lian_succeed_sbl);
		lian_map.put("out_ticket_XL", ext_lian_out_ticket_XL);
		lian_map.put("pay_time", ext_lian_pay_time);
		lian_map.put("over_time", ext_lian_over_time);
		lian_map.put("preparative_count", ext_lian_preparative_count);
		lian_map.put("pay_defeated", ext_lian_pay_defeated);
		lian_map.put("change_money", ext_lian_change_money);
		lian_map.put("refund_money", ext_lian_refund_money);
		lian_map.put("bx_count", ext_lian_bx_count);
		lian_map.put("bx_countMoney", ext_lian_bx_countMoney);
		lian_map.put("vip_lose", ext_lian_vip_lose);
		lian_map.put("channel", "30101612");
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int ext_todayCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(lian_map);
		if (ext_todayCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(lian_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(lian_map);
		}
		/**************************** 对外商户新疆利安总的统计数据  统计结束  ****************************/
		
		
		/****************************** 高铁管家  统计开始**********************************/
			logger.info("高铁管家  统计开始");
			DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
			paramMap.put("channel", "301030");
			int gt_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.gt_query_day_out_ticket_succeed(paramMap);// 订单成功数
			int gt_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.gt_query_day_out_ticket_defeated(paramMap);// 订单失败数
			int gt_preparative_count = tj_Hc_orderInfo_Today_Service.gt_queryPre_preparative_count(paramMap);// 预下单
			int gt_pay_defeated = tj_Hc_orderInfo_Today_Service.gt_queryPre_pay_defeated(paramMap);// 支付失败的个数
			int gt_order_count = tj_Hc_orderInfo_Today_Service.gt_query_day_order_count(paramMap);// 总订单数
			int gt_refund_count = tj_Hc_orderInfo_Today_Service.gt_query_refund_count(paramMap);// 退款成功
			double gt_succeed_money = tj_Hc_orderInfo_Today_Service.gt_query_succeed_money(paramMap);// 出票成功的总金额
			double gt_defeated_money = tj_Hc_orderInfo_Today_Service.gt_query_defeated_money(paramMap);// 出票失败的总金额
			double gt_change_money = tj_Hc_orderInfo_Today_Service.gt_queryPre_change_money(paramMap);// 改签差价
			double gt_refund_money = tj_Hc_orderInfo_Today_Service.gt_query_refund_money(paramMap);// 查询总退款金额
			int gt_ticket_count = tj_Hc_orderInfo_Today_Service.gt_query_ticket_count(paramMap);// 票数总计
			int gt_over_time = tj_Hc_orderInfo_Today_Service.gt_over_time(paramMap); // 超过十分钟订单数
			int gt_bx_count = tj_Hc_orderInfo_Today_Service.gt_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
			int gt_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
			int gt_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数
			
			double gt_vip_sbl;// VIP失败率
			String gt_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
			double gt_succeed_oddsStr;// 出票成功转化率
			int gt_succeed_defeated_count = gt_out_ticket_succeed	+ gt_out_ticket_defeated;// 出票成功的个数加出票失败的个数
			double gt_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 出票效率
			double gt_pay_time = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

			double gt_ticket_cgl;// 订单成功率
			double gt_ticket_sbl;// 订单失败率
			String gt_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
			String gt_succeed_cgl = null;// 订单成功率(转换为0.00%后)
			String gt_succeed_sbl = null;// 订单失败率(转换为0.00%后)

			if (gt_vip_count != 0) {
				gt_vip_sbl = ((double) gt_vip_lose / (double) gt_vip_count); // VIP出票失败率
				// doble 转换为 String类型的“**.**%”
				gt_succeed_vip_sbl = SwitchUtils.format_perCent(gt_vip_sbl);
			} else {
				gt_succeed_vip_sbl = "0.00%";
			}

			if (gt_order_count != 0) {
				gt_succeed_oddsStr = ((double) gt_out_ticket_succeed / (double) gt_order_count); // 出票成功转化率
				// doble 转换为 String类型的“**.**%”
				gt_succeed_odds = SwitchUtils.format_perCent(gt_succeed_oddsStr);
			} else {
				gt_succeed_odds = "0.00%"; // 出票成功率
			}

			if (gt_succeed_defeated_count != 0) {
				gt_ticket_cgl = ((double) gt_out_ticket_succeed / (double) gt_succeed_defeated_count);// 订单成功率
				gt_ticket_sbl = ((double) gt_out_ticket_defeated / (double) gt_succeed_defeated_count);// 订单失败率
				gt_succeed_cgl = SwitchUtils.format_perCent(gt_ticket_cgl);
				gt_succeed_sbl = SwitchUtils.format_perCent(gt_ticket_sbl);
			} else {
				gt_succeed_cgl = "0.00%";
				gt_succeed_sbl = "0.00%";
			}

			Map<String, Object> gt_map = new HashMap<String, Object>();
			gt_map.put("tj_id", CreateIDUtil.createID("TJ"));
			gt_map.put("out_ticket_succeed", gt_out_ticket_succeed);
			gt_map.put("out_ticket_defeated", gt_out_ticket_defeated);
			gt_map.put("order_count", gt_order_count);
			gt_map.put("succeed_money", gt_succeed_money);
			gt_map.put("defeated_money", gt_defeated_money);
			gt_map.put("succeed_odds", gt_succeed_odds);
			gt_map.put("order_time", create_time);
			gt_map.put("refund_count", gt_refund_count);
			gt_map.put("ticket_count", gt_ticket_count);
			gt_map.put("succeed_cgl", gt_succeed_cgl);
			gt_map.put("succeed_sbl", gt_succeed_sbl);
			gt_map.put("out_ticket_XL", gt_out_ticket_XL);
			gt_map.put("pay_time", gt_pay_time);
			gt_map.put("over_time", gt_over_time);
			gt_map.put("preparative_count", gt_preparative_count);
			gt_map.put("pay_defeated", gt_pay_defeated);
			gt_map.put("change_money", gt_change_money);
			gt_map.put("refund_money", gt_refund_money);
			gt_map.put("bx_count", gt_bx_count);
			gt_map.put("vip_lose", gt_vip_lose);
			gt_map.put("succeed_vip_sbl", gt_succeed_vip_sbl);
			gt_map.put("channel", "301030");
			// TODO
			// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
			DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
			int gt_todayCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(gt_map);
			if (gt_todayCount == 0) {
				// 添加到表tj_hc_orderInfo表中
				tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(gt_map);
			} else {
				// 更新tj_hc_orderInfo表中数据
				tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(gt_map);
			}
		/**************************** 高铁管家统计结束  ****************************/
			logger.info("高铁管家  统计结束");
			
			/****************************** 携程  统计开始**********************************/
			logger.info("携程  统计开始");
			DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
			paramMap.put("channel", "301031");
			int xc_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.xc_query_day_out_ticket_succeed(paramMap);// 订单成功数
			int xc_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.xc_query_day_out_ticket_defeated(paramMap);// 订单失败数
//			int xc_preparative_count = tj_Hc_orderInfo_Today_Service.xc_queryPre_preparative_count(paramMap);// 预下单
//			int xc_pay_defeated = tj_Hc_orderInfo_Today_Service.xc_queryPre_pay_defeated(paramMap);// 支付失败的个数
			int xc_order_count = tj_Hc_orderInfo_Today_Service.xc_query_day_order_count(paramMap);// 总订单数
			int xc_refund_count = tj_Hc_orderInfo_Today_Service.xc_query_refund_count(paramMap);// 退款成功
			double xc_succeed_money = tj_Hc_orderInfo_Today_Service.xc_query_succeed_money(paramMap);// 出票成功的总金额
			double xc_defeated_money = tj_Hc_orderInfo_Today_Service.xc_query_defeated_money(paramMap);// 出票失败的总金额
			double xc_change_money = tj_Hc_orderInfo_Today_Service.xc_queryPre_change_money(paramMap);// 改签差价
			double xc_refund_money = tj_Hc_orderInfo_Today_Service.xc_query_refund_money(paramMap);// 查询总退款金额
			int xc_ticket_count = tj_Hc_orderInfo_Today_Service.xc_query_ticket_count(paramMap);// 票数总计
//			int xc_over_time = tj_Hc_orderInfo_Today_Service.xc_over_time(paramMap); // 超过十分钟订单数
			int xc_bx_count = tj_Hc_orderInfo_Today_Service.xc_queryPre_bx_count(paramMap);// 出售保险的个数（发送完成和失败都算）
			int xc_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
			int xc_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数
			
			double xc_vip_sbl;// VIP失败率
			String xc_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
			double xc_succeed_oddsStr;// 出票成功转化率
			int xc_succeed_defeated_count = xc_out_ticket_succeed	+ xc_out_ticket_defeated;// 出票成功的个数加出票失败的个数
			double xc_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 出票效率
			double xc_pay_time = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

			double xc_ticket_cgl;// 订单成功率
			double xc_ticket_sbl;// 订单失败率
			String xc_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
			String xc_succeed_cgl = null;// 订单成功率(转换为0.00%后)
			String xc_succeed_sbl = null;// 订单失败率(转换为0.00%后)

			if (xc_vip_count != 0) {
				xc_vip_sbl = ((double) xc_vip_lose / (double) xc_vip_count); // VIP出票失败率
				// doble 转换为 String类型的“**.**%”
				xc_succeed_vip_sbl = SwitchUtils.format_perCent(xc_vip_sbl);
			} else {
				xc_succeed_vip_sbl = "0.00%";
			}

			if (xc_order_count != 0) {
				xc_succeed_oddsStr = ((double) xc_out_ticket_succeed / (double) xc_order_count); // 出票成功转化率
				// doble 转换为 String类型的“**.**%”
				xc_succeed_odds = SwitchUtils.format_perCent(xc_succeed_oddsStr);
			} else {
				xc_succeed_odds = "0.00%"; // 出票成功率
			}

			if (xc_succeed_defeated_count != 0) {
				xc_ticket_cgl = ((double) xc_out_ticket_succeed / (double) xc_succeed_defeated_count);// 订单成功率
				xc_ticket_sbl = ((double) xc_out_ticket_defeated / (double) xc_succeed_defeated_count);// 订单失败率
				xc_succeed_cgl = SwitchUtils.format_perCent(xc_ticket_cgl);
				xc_succeed_sbl = SwitchUtils.format_perCent(xc_ticket_sbl);
			} else {
				xc_succeed_cgl = "0.00%";
				xc_succeed_sbl = "0.00%";
			}

			Map<String, Object> xc_map = new HashMap<String, Object>();
			xc_map.put("tj_id", CreateIDUtil.createID("TJ"));
			xc_map.put("out_ticket_succeed", xc_out_ticket_succeed);
			xc_map.put("out_ticket_defeated", xc_out_ticket_defeated);
			xc_map.put("order_count", xc_order_count);
			xc_map.put("succeed_money", xc_succeed_money);
			xc_map.put("defeated_money", xc_defeated_money);
			xc_map.put("succeed_odds", xc_succeed_odds);
			xc_map.put("order_time", create_time);
			xc_map.put("refund_count", xc_refund_count);
			xc_map.put("ticket_count", xc_ticket_count);
			xc_map.put("succeed_cgl", xc_succeed_cgl);
			xc_map.put("succeed_sbl", xc_succeed_sbl);
			xc_map.put("out_ticket_XL", xc_out_ticket_XL);
			xc_map.put("pay_time", xc_pay_time);
//			xc_map.put("over_time", xc_over_time);
//			xc_map.put("preparative_count", xc_preparative_count);
//			xc_map.put("pay_defeated", xc_pay_defeated);
			xc_map.put("change_money", xc_change_money);
			xc_map.put("refund_money", xc_refund_money);
			xc_map.put("bx_count", xc_bx_count);
			xc_map.put("vip_lose", xc_vip_lose);
			xc_map.put("succeed_vip_sbl", xc_succeed_vip_sbl);
			xc_map.put("channel", "301031");
			// TODO
			// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
			DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
			int xc_todayCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(xc_map);
			if (xc_todayCount == 0) {
				// 添加到表tj_hc_orderInfo表中
				tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(xc_map);
			} else {
				// 更新tj_hc_orderInfo表中数据
				tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(xc_map);
			}
		/**************************** 携程统计结束  ****************************/
			logger.info("携程  统计结束");
		
		/****************************** 对外商户ext--总数据  统计开始**********************************/
		//查询对外商户的渠道（merchant_id）
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		Map<String, Object> extMap = new HashMap<String, Object>();
		extMap.put("create_time", create_time);
		extMap.put("channelList", merchantId_list);
		
		int ext_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.ext_query_day_out_ticket_succeed(extMap);// 订单成功数
		int ext_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.ext_query_day_out_ticket_defeated(extMap);// 订单失败数
		int ext_preparative_count = tj_Hc_orderInfo_Today_Service.ext_queryPre_preparative_count(extMap);// 预下单
		int ext_pay_defeated = tj_Hc_orderInfo_Today_Service.ext_queryPre_pay_defeated(extMap);// 支付失败的个数
		int ext_order_count = tj_Hc_orderInfo_Today_Service.ext_query_day_order_count(extMap);// 总订单数
		int ext_refund_count = tj_Hc_orderInfo_Today_Service.ext_query_refund_count(extMap);// 退款成功
		double ext_succeed_money = tj_Hc_orderInfo_Today_Service.ext_query_succeed_money(extMap);// 出票成功的总金额
		double ext_defeated_money = tj_Hc_orderInfo_Today_Service.ext_query_defeated_money(extMap);// 出票失败的总金额
		double ext_change_money = tj_Hc_orderInfo_Today_Service.ext_queryPre_change_money(extMap);// 改签差价
		double ext_refund_money = tj_Hc_orderInfo_Today_Service.ext_query_refund_money(extMap);// 查询总退款金额
		int ext_ticket_count = tj_Hc_orderInfo_Today_Service.ext_query_ticket_count(extMap);// 票数总计
		//int ext_over_time = tj_Hc_orderInfo_Today_Service.ext_over_time(paramMap); // 超过十分钟订单数
		int ext_bx_count = tj_Hc_orderInfo_Today_Service.ext_queryPre_bx_count(extMap);// 出售保险的个数（发送完成和失败都算）
//		extMap.put("bxMoney", 10);
//		double ext_bx_countMoney10 = tj_Hc_orderInfo_Today_Service.ext_queryPre_bx_countMoney10(extMap);// 10元保险利润
//		extMap.put("bxMoney", 20);
//		double ext_bx_countMoney20 = tj_Hc_orderInfo_Today_Service.ext_queryPre_bx_countMoney20(extMap);// 20元保险利润
		
		 // 保险总利润
		//int ext_vip_lose = tj_Hc_orderInfo_Today_Service.queryVip_lose(paramMap);// VIP失败订单数
		//int ext_vip_count = tj_Hc_orderInfo_Today_Service.queryVip_count(paramMap);// VIP总数

		double ext_vip_sbl;// VIP失败率
		String ext_succeed_vip_sbl = null;// VIP失败率（转换为0.00%）
		double ext_succeed_oddsStr;// 出票成功转化率
		int ext_succeed_defeated_count = ext_out_ticket_succeed	+ ext_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double ext_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryExtOut_ticket_XL(extMap);// 总出票效率
		double ext_pay_time = tj_Hc_orderInfo_Today_Service.queryExtPay_time(paramMap);// 支付时长
		double ext_ticket_cgl;// 订单成功率
		double ext_ticket_sbl;// 订单失败率
		String ext_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String ext_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String ext_succeed_sbl = null;// 订单失败率(转换为0.00%后)

		if (ext_vip_count_sum != 0) {//对外商户总的vip订单数
			ext_vip_sbl = ((double) ext_vip_lose_count / (double) ext_vip_count_sum); // VIP出票失败率
			// doble 转换为 String类型的“**.**%”
			ext_succeed_vip_sbl = SwitchUtils.format_perCent(ext_vip_sbl);
		} else {
			ext_succeed_vip_sbl = "0.00%";
		}

		if (ext_order_count != 0) {
			ext_succeed_oddsStr = ((double) ext_out_ticket_succeed / (double) ext_order_count); // 出票成功转化率
			// doble 转换为 String类型的“**.**%”
			ext_succeed_odds = SwitchUtils.format_perCent(ext_succeed_oddsStr);
		} else {
			ext_succeed_odds = "0.00%"; // 出票成功率
		}

		if (ext_succeed_defeated_count != 0) {
			ext_ticket_cgl = ((double) ext_out_ticket_succeed / (double) ext_succeed_defeated_count);// 订单成功率
			ext_ticket_sbl = ((double) ext_out_ticket_defeated / (double) ext_succeed_defeated_count);// 订单失败率
			ext_succeed_cgl = SwitchUtils.format_perCent(ext_ticket_cgl);
			ext_succeed_sbl = SwitchUtils.format_perCent(ext_ticket_sbl);
		} else {
			ext_succeed_cgl = "0.00%";
			ext_succeed_sbl = "0.00%";
		}

		Map<String, Object> ext_map = new HashMap<String, Object>();
		ext_map.put("tj_id", CreateIDUtil.createID("TJ"));
		ext_map.put("out_ticket_succeed", ext_out_ticket_succeed);
		ext_map.put("out_ticket_defeated", ext_out_ticket_defeated);
		ext_map.put("order_count", ext_order_count);
		ext_map.put("succeed_money", ext_succeed_money);
		ext_map.put("defeated_money", ext_defeated_money);
		ext_map.put("succeed_odds", ext_succeed_odds);
		ext_map.put("order_time", create_time);
		ext_map.put("refund_count", ext_refund_count);
		ext_map.put("ticket_count", ext_ticket_count);
		ext_map.put("succeed_cgl", ext_succeed_cgl);
		ext_map.put("succeed_sbl", ext_succeed_sbl);
		ext_map.put("out_ticket_XL", ext_out_ticket_XL);//对外商户总出票率
		ext_map.put("pay_time", ext_pay_time);
		ext_map.put("over_time", ext_over_time_count);//对外商户总的超时十分钟的订单
		ext_map.put("preparative_count", ext_preparative_count);
		ext_map.put("pay_defeated", ext_pay_defeated);
		ext_map.put("change_money", ext_change_money);
		ext_map.put("refund_money", ext_refund_money);
		ext_map.put("bx_count", ext_bx_count);
		ext_map.put("bx_countMoney", ext_bx_countAllMoney);
		ext_map.put("vip_lose", ext_vip_lose_count);//对外商户总的VIP失败订单数
		ext_map.put("succeed_vip_sbl", ext_succeed_vip_sbl);
		ext_map.put("channel", "ext");//对外商户总的统计数据
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int ext_lian_todayCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(ext_map);
		if (ext_lian_todayCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(ext_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(ext_map);
		}	
		/**************************** 对外商户ext--总数据  统计结束  ****************************/
		
		/************************************ 艺龙统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "elong");
		for(int i=0;i<3;i++){
			if(i==0){
//				paramMap.put("order_type", "00");
			}else if(i==1){
				paramMap.put("order_type", "11");
			}else if(i==2){
				paramMap.put("order_type", "22");
			}
		int elong_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.elong_query_day_out_ticket_succeed(paramMap);// 当天出票成功的个数（订单数）
		int elong_out_ticket_num_succeed= tj_Hc_orderInfo_Today_Service.elong_query_day_out_ticket_num_succeed(paramMap);//当天出票成功的票数
		int elong_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.elong_query_day_out_ticket_defeated(paramMap);// 当出票失败的个数
		int elong_order_count = tj_Hc_orderInfo_Today_Service.elong_query_day_order_count(paramMap);// 当天的总个数
		int elong_refund_count = tj_Hc_orderInfo_Today_Service.elong_query_refund_count(paramMap);// 查询当天发起退款并成功的个数
		int elong_refund_cp_count= tj_Hc_orderInfo_Today_Service.elong_query_refund_cp_count(paramMap);//当天发起退款并成功的票数
		double elong_succeed_money = tj_Hc_orderInfo_Today_Service.elong_query_succeed_money(paramMap);// 当天出票成功的总金额
		double elong_defeated_money = tj_Hc_orderInfo_Today_Service.elong_query_defeated_money(paramMap);// 当天出票失败的总金额
		double elong_refund_money = tj_Hc_orderInfo_Today_Service.elong_query_refund_money(paramMap);// 查询总退款金额
		int elong_ticket_count = tj_Hc_orderInfo_Today_Service.elong_query_ticket_count(paramMap);// 查询出当天出售的票数
		int elong_over_time = tj_Hc_orderInfo_Today_Service.elong_over_time(paramMap);
		
		int elong_out_time_order = tj_Hc_orderInfo_Today_Service.elong_out_time_order(paramMap);// 超时订单
		int elong_cancel_order = tj_Hc_orderInfo_Today_Service.elong_cancel_order(paramMap);//取消订单
		
		double elong_succeed_oddsStr;// 出票成功转化率
		int elong_succeed_defeated_count = elong_out_ticket_succeed + elong_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double elong_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double elong_pay_time = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double elong_ticket_cgl;// 订单成功率
		double elong_ticket_sbl;// 订单失败率
		double elong_bx_countMoney = ((double)elong_out_ticket_num_succeed - (double)elong_refund_cp_count)*0.4; // 保险总利润
		String elong_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String elong_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String elong_succeed_sbl = null;// 订单失败率(转换为0.00%后)
		if (elong_order_count != 0) {
			elong_succeed_oddsStr = ((double) elong_out_ticket_succeed / (double) elong_order_count); // 出票成功转化率
			// double 转换为 String类型的“**.**%”
			elong_succeed_odds = SwitchUtils.format_perCent(elong_succeed_oddsStr);
		} else {
			elong_succeed_odds = "0.00%"; // 出票成功率
		}
		if (elong_order_count != 0) {
			elong_ticket_cgl = ((double) elong_out_ticket_succeed / (double) elong_order_count);// 订单成功率
			elong_ticket_sbl = ((double) (elong_out_ticket_defeated+elong_out_time_order+elong_cancel_order )/ (double) elong_order_count);// 订单失败率
			elong_succeed_cgl = SwitchUtils.format_perCent(elong_ticket_cgl);
			elong_succeed_sbl = SwitchUtils.format_perCent(elong_ticket_sbl);
		} else {
			elong_succeed_cgl = "0.00%";
			elong_succeed_sbl = "0.00%";
		}
		
		double elong_ticket_hold_cgl;// 订单占座成功率
		double elong_ticket_hold_sbl;// 订单占座失败率
		String elong_holdseat_cgl = null;// 订单占座成功率(转换为0.00%后)
		String elong_holdseat_sbl = null;// 订单占座失败率(转换为0.00%后)
		if (elong_order_count != 0) {
			elong_ticket_hold_cgl = ((double) (elong_out_ticket_succeed+elong_out_time_order+elong_cancel_order) / (double) elong_order_count);// 订单成功率
			elong_ticket_hold_sbl = ((double) elong_out_ticket_defeated/ (double) elong_order_count);// 订单失败率
			elong_holdseat_cgl = SwitchUtils.format_perCent(elong_ticket_hold_cgl);
			elong_holdseat_sbl = SwitchUtils.format_perCent(elong_ticket_hold_sbl);
		} else {
			elong_holdseat_cgl = "0.00%";
			elong_holdseat_sbl = "0.00%";
		}
		
		Map<String, Object> elong_map = new HashMap<String, Object>();
		elong_map.put("tj_id", CreateIDUtil.createID("TJ"));
		elong_map.put("out_ticket_succeed", elong_out_ticket_succeed);
		elong_map.put("out_ticket_defeated", elong_out_ticket_defeated);
		elong_map.put("order_count", elong_order_count);
		elong_map.put("succeed_money", elong_succeed_money);
		elong_map.put("defeated_money", elong_defeated_money);
		elong_map.put("refund_money", elong_refund_money);
		elong_map.put("succeed_odds", elong_succeed_odds);
		elong_map.put("order_time", create_time);
		elong_map.put("refund_count", elong_refund_count);
		elong_map.put("ticket_count", elong_ticket_count);
		elong_map.put("succeed_cgl", elong_succeed_cgl);
		elong_map.put("succeed_sbl", elong_succeed_sbl);
		
		elong_map.put("holdseat_cgl", elong_holdseat_cgl);
		elong_map.put("holdseat_sbl", elong_holdseat_sbl);
		
		elong_map.put("out_ticket_XL", elong_out_ticket_XL);
		elong_map.put("pay_time", elong_pay_time);
		elong_map.put("over_time", elong_over_time);
		if(i==1){
			elong_map.put("bx_countMoney", Double.parseDouble("0"));
		}else{
			elong_map.put("bx_countMoney", elong_bx_countMoney);
		}
		elong_map.put("out_time_order", elong_out_time_order);
		elong_map.put("cancel_order", elong_cancel_order);
		if(i==0){
			elong_map.put("channel", "elong");
		}else if(i==1){
			elong_map.put("channel", "elong1");
		}else if(i==2){
			elong_map.put("channel", "elong2");
		}
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todayelongCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(elong_map);
		if (todayelongCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(elong_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(elong_map);
		}
		}
		paramMap.remove("order_type");
		/************************************ 艺龙统计结束 *****************************************/
		
		/************************************ 同程统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "tongcheng");
		int tongcheng_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.elong_query_day_out_ticket_succeed(paramMap);// 当天出票成功的个数（订单数）
		int tongcheng_out_ticket_num_succeed= tj_Hc_orderInfo_Today_Service.elong_query_day_out_ticket_num_succeed(paramMap);//当天出票成功的票数
		int tongcheng_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.elong_query_day_out_ticket_defeated(paramMap);// 当出票失败的个数
		int tongcheng_order_count = tj_Hc_orderInfo_Today_Service.elong_query_day_order_count(paramMap);// 当天的总个数
		int tongcheng_refund_count = tj_Hc_orderInfo_Today_Service.elong_query_refund_count(paramMap);// 查询当天发起退款并成功的个数
		int tongcheng_refund_cp_count= tj_Hc_orderInfo_Today_Service.elong_query_refund_cp_count(paramMap);//当天发起退款并成功的票数
		double tongcheng_succeed_money = tj_Hc_orderInfo_Today_Service.elong_query_succeed_money(paramMap);// 当天出票成功的总金额
		double tongcheng_defeated_money = tj_Hc_orderInfo_Today_Service.elong_query_defeated_money(paramMap);// 当天出票失败的总金额
		double tongcheng_refund_money = tj_Hc_orderInfo_Today_Service.elong_query_refund_money(paramMap);// 查询总退款金额
		int tongcheng_ticket_count = tj_Hc_orderInfo_Today_Service.elong_query_ticket_count(paramMap);// 查询出当天出售的票数
		int tongcheng_over_time = tj_Hc_orderInfo_Today_Service.elong_over_time(paramMap);
		
		int tongcheng_out_time_order = tj_Hc_orderInfo_Today_Service.elong_out_time_order(paramMap);// 超时订单
		int tongcheng_cancel_order = tj_Hc_orderInfo_Today_Service.elong_cancel_order(paramMap);//取消订单
		
		double tongcheng_succeed_oddsStr;// 出票成功转化率
		int tongcheng_succeed_defeated_count = tongcheng_out_ticket_succeed + tongcheng_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double tongcheng_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double tongcheng_pay_time = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double tongcheng_ticket_cgl;// 订单成功率
		double tongcheng_ticket_sbl;// 订单失败率
		//double tongcheng_bx_countMoney = ((double)tongcheng_out_ticket_num_succeed - (double)tongcheng_refund_cp_count)*0.4; // 保险总利润
		String tongcheng_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String tongcheng_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String tongcheng_succeed_sbl = null;// 订单失败率(转换为0.00%后)
		if (tongcheng_order_count != 0) {
			tongcheng_succeed_oddsStr = ((double) tongcheng_out_ticket_succeed / (double) tongcheng_order_count); // 出票成功转化率
			// double 转换为 String类型的“**.**%”
			tongcheng_succeed_odds = SwitchUtils.format_perCent(tongcheng_succeed_oddsStr);
		} else {
			tongcheng_succeed_odds = "0.00%"; // 出票成功率
		}
		if (tongcheng_order_count != 0) {
			tongcheng_ticket_cgl = ((double) tongcheng_out_ticket_succeed / (double) tongcheng_order_count);// 订单成功率
			tongcheng_ticket_sbl = ((double) (tongcheng_out_ticket_defeated +tongcheng_out_time_order+tongcheng_cancel_order)/ (double) tongcheng_order_count);// 订单失败率
			tongcheng_succeed_cgl = SwitchUtils.format_perCent(tongcheng_ticket_cgl);
			tongcheng_succeed_sbl = SwitchUtils.format_perCent(tongcheng_ticket_sbl);
		} else {
			tongcheng_succeed_cgl = "0.00%";
			tongcheng_succeed_sbl = "0.00%";
		}
		

		double tongcheng_ticket_hold_cgl;// 订单占座成功率
		double tongcheng_ticket_hold_sbl;// 订单占座失败率
		String tongcheng_holdseat_cgl = null;// 订单占座成功率(转换为0.00%后)
		String tongcheng_holdseat_sbl = null;// 订单占座失败率(转换为0.00%后)
		if (tongcheng_order_count != 0) {
			tongcheng_ticket_hold_cgl = ((double) (tongcheng_out_ticket_succeed+tongcheng_out_time_order+tongcheng_cancel_order) / (double) tongcheng_order_count);// 订单成功率
			tongcheng_ticket_hold_sbl = ((double) tongcheng_out_ticket_defeated/ (double) tongcheng_order_count);// 订单失败率
			tongcheng_holdseat_cgl = SwitchUtils.format_perCent(tongcheng_ticket_hold_cgl);
			tongcheng_holdseat_sbl = SwitchUtils.format_perCent(tongcheng_ticket_hold_sbl);
		} else {
			tongcheng_holdseat_cgl = "0.00%";
			tongcheng_holdseat_sbl = "0.00%";
		}
		
		Map<String, Object> tongcheng_map = new HashMap<String, Object>();
		tongcheng_map.put("tj_id", CreateIDUtil.createID("TJ"));
		tongcheng_map.put("out_ticket_succeed", tongcheng_out_ticket_succeed);
		tongcheng_map.put("out_ticket_defeated", tongcheng_out_ticket_defeated);
		tongcheng_map.put("order_count", tongcheng_order_count);
		tongcheng_map.put("succeed_money", tongcheng_succeed_money);
		tongcheng_map.put("defeated_money", tongcheng_defeated_money);
		tongcheng_map.put("refund_money", tongcheng_refund_money);
		tongcheng_map.put("succeed_odds", tongcheng_succeed_odds);
		tongcheng_map.put("order_time", create_time);
		tongcheng_map.put("refund_count", tongcheng_refund_count);
		tongcheng_map.put("ticket_count", tongcheng_ticket_count);
		tongcheng_map.put("succeed_cgl", tongcheng_succeed_cgl);
		tongcheng_map.put("succeed_sbl", tongcheng_succeed_sbl);
		tongcheng_map.put("out_ticket_XL", tongcheng_out_ticket_XL);
		tongcheng_map.put("pay_time", tongcheng_pay_time);
		tongcheng_map.put("over_time", tongcheng_over_time);
		//tongcheng_map.put("bx_countMoney", tongcheng_bx_countMoney);
		tongcheng_map.put("out_time_order", tongcheng_out_time_order);
		tongcheng_map.put("cancel_order", tongcheng_cancel_order);
		
		tongcheng_map.put("holdseat_cgl", tongcheng_holdseat_cgl);
		tongcheng_map.put("holdseat_sbl", tongcheng_holdseat_sbl);
		
		tongcheng_map.put("channel", "tongcheng");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todaytongchengCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(tongcheng_map);
		if (todaytongchengCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(tongcheng_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(tongcheng_map);
		}
		/************************************ 同程统计结束 *****************************************/
		
		
		/************************************ 美团统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "meituan");
		int meituan_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.meituan_query_day_out_ticket_succeed(paramMap);// 当天出票成功的个数（订单数）
//		int meituan_out_ticket_num_succeed= tj_Hc_orderInfo_Today_Service.meituan_query_day_out_ticket_num_succeed(paramMap);//当天出票成功的票数
		int meituan_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.meituan_query_day_out_ticket_defeated(paramMap);// 当出票失败的个数
		int meituan_order_count = tj_Hc_orderInfo_Today_Service.meituan_query_day_order_count(paramMap);// 当天的总个数
		int meituan_refund_count = tj_Hc_orderInfo_Today_Service.meituan_query_refund_count(paramMap);// 查询当天发起退款并成功的个数
//		int meituan_refund_cp_count= tj_Hc_orderInfo_Today_Service.meituan_query_refund_cp_count(paramMap);//当天发起退款并成功的票数
		double meituan_succeed_money = tj_Hc_orderInfo_Today_Service.meituan_query_succeed_money(paramMap);// 当天出票成功的总金额
		double meituan_defeated_money = tj_Hc_orderInfo_Today_Service.meituan_query_defeated_money(paramMap);// 当天出票失败的总金额
		double meituan_refund_money = tj_Hc_orderInfo_Today_Service.meituan_query_refund_money(paramMap);// 查询总退款金额
		int meituan_ticket_count = tj_Hc_orderInfo_Today_Service.meituan_query_ticket_count(paramMap);// 查询出当天出售的票数
		int meituan_over_time = tj_Hc_orderInfo_Today_Service.meituan_over_time(paramMap);
		
		int meituan_out_time_order = tj_Hc_orderInfo_Today_Service.meituan_out_time_order(paramMap);// 超时订单
		int meituan_cancel_order = tj_Hc_orderInfo_Today_Service.meituan_cancel_order(paramMap);//取消订单
		
		double meituan_succeed_oddsStr;// 出票成功转化率
//		int meituan_succeed_defeated_count = meituan_out_ticket_succeed + meituan_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double meituan_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double meituan_pay_time = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double meituan_ticket_cgl;// 订单成功率
		double meituan_ticket_sbl;// 订单失败率
		//double meituan_bx_countMoney = ((double)meituan_out_ticket_num_succeed - (double)meituan_refund_cp_count)*0.4; // 保险总利润
		String meituan_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String meituan_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String meituan_succeed_sbl = null;// 订单失败率(转换为0.00%后)
		if (meituan_order_count != 0) {
			meituan_succeed_oddsStr = ((double) meituan_out_ticket_succeed / (double) meituan_order_count); // 出票成功转化率
			// double 转换为 String类型的“**.**%”
			meituan_succeed_odds = SwitchUtils.format_perCent(meituan_succeed_oddsStr);
		} else {
			meituan_succeed_odds = "0.00%"; // 出票成功率
		}
		if (meituan_order_count != 0) {
			meituan_ticket_cgl = ((double) meituan_out_ticket_succeed / (double) meituan_order_count);// 订单成功率
			meituan_ticket_sbl = ((double) (meituan_out_ticket_defeated +meituan_out_time_order+meituan_cancel_order)/ (double) meituan_order_count);// 订单失败率
			meituan_succeed_cgl = SwitchUtils.format_perCent(meituan_ticket_cgl);
			meituan_succeed_sbl = SwitchUtils.format_perCent(meituan_ticket_sbl);
		} else {
			meituan_succeed_cgl = "0.00%";
			meituan_succeed_sbl = "0.00%";
		}
		

		double meituan_ticket_hold_cgl;// 订单占座成功率
		double meituan_ticket_hold_sbl;// 订单占座失败率
		String meituan_holdseat_cgl = null;// 订单占座成功率(转换为0.00%后)
		String meituan_holdseat_sbl = null;// 订单占座失败率(转换为0.00%后)
		if (meituan_order_count != 0) {
			meituan_ticket_hold_cgl = ((double) (meituan_out_ticket_succeed+meituan_out_time_order+meituan_cancel_order) / (double) meituan_order_count);// 订单成功率
			meituan_ticket_hold_sbl = ((double) meituan_out_ticket_defeated/ (double) meituan_order_count);// 订单失败率
			meituan_holdseat_cgl = SwitchUtils.format_perCent(meituan_ticket_hold_cgl);
			meituan_holdseat_sbl = SwitchUtils.format_perCent(meituan_ticket_hold_sbl);
		} else {
			meituan_holdseat_cgl = "0.00%";
			meituan_holdseat_sbl = "0.00%";
		}
		
		Map<String, Object> meituan_map = new HashMap<String, Object>();
		meituan_map.put("tj_id", CreateIDUtil.createID("TJ"));
		meituan_map.put("out_ticket_succeed", meituan_out_ticket_succeed);
		meituan_map.put("out_ticket_defeated", meituan_out_ticket_defeated);
		meituan_map.put("order_count", meituan_order_count);
		meituan_map.put("succeed_money", meituan_succeed_money);
		meituan_map.put("defeated_money", meituan_defeated_money);
		meituan_map.put("refund_money", meituan_refund_money);
		meituan_map.put("succeed_odds", meituan_succeed_odds);
		meituan_map.put("order_time", create_time);
		meituan_map.put("refund_count", meituan_refund_count);
		meituan_map.put("ticket_count", meituan_ticket_count);
		meituan_map.put("succeed_cgl", meituan_succeed_cgl);
		meituan_map.put("succeed_sbl", meituan_succeed_sbl);
		meituan_map.put("out_ticket_XL", meituan_out_ticket_XL);
		meituan_map.put("pay_time", meituan_pay_time);
		meituan_map.put("over_time", meituan_over_time);
		//meituan_map.put("bx_countMoney", meituan_bx_countMoney);
		meituan_map.put("out_time_order", meituan_out_time_order);
		meituan_map.put("cancel_order", meituan_cancel_order);
		
		meituan_map.put("holdseat_cgl", meituan_holdseat_cgl);
		meituan_map.put("holdseat_sbl", meituan_holdseat_sbl);
		
		meituan_map.put("channel", "meituan");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todaymeituanCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(meituan_map);
		if (todaymeituanCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(meituan_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(meituan_map);
		}
		/************************************ 美团统计结束 *****************************************/
		
		/************************************ 途牛统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		paramMap.put("channel", "tuniu");
		int tuniu_out_ticket_succeed = tj_Hc_orderInfo_Today_Service.tuniu_query_day_out_ticket_succeed(paramMap);// 当天出票成功的个数（订单数）
//		int tuniu_out_ticket_num_succeed= tj_Hc_orderInfo_Today_Service.tuniu_query_day_out_ticket_num_succeed(paramMap);//当天出票成功的票数
		int tuniu_out_ticket_defeated = tj_Hc_orderInfo_Today_Service.tuniu_query_day_out_ticket_defeated(paramMap);// 当出票失败的个数
		int tuniu_order_count = tj_Hc_orderInfo_Today_Service.tuniu_query_day_order_count(paramMap);// 当天的总个数
		int tuniu_refund_count = tj_Hc_orderInfo_Today_Service.tuniu_query_refund_count(paramMap);// 查询当天发起退款并成功的个数
//		int tuniu_refund_cp_count= tj_Hc_orderInfo_Today_Service.tuniu_query_refund_cp_count(paramMap);//当天发起退款并成功的票数
		double tuniu_succeed_money = tj_Hc_orderInfo_Today_Service.tuniu_query_succeed_money(paramMap);// 当天出票成功的总金额
		double tuniu_defeated_money = tj_Hc_orderInfo_Today_Service.tuniu_query_defeated_money(paramMap);// 当天出票失败的总金额
		double tuniu_refund_money = tj_Hc_orderInfo_Today_Service.tuniu_query_refund_money(paramMap);// 查询总退款金额
		int tuniu_ticket_count = tj_Hc_orderInfo_Today_Service.tuniu_query_ticket_count(paramMap);// 查询出当天出售的票数
		int tuniu_over_time = tj_Hc_orderInfo_Today_Service.tuniu_over_time(paramMap);
		
		int tuniu_out_time_order = tj_Hc_orderInfo_Today_Service.tuniu_out_time_order(paramMap);// 超时订单
		int tuniu_cancel_order = tj_Hc_orderInfo_Today_Service.tuniu_cancel_order(paramMap);//取消订单
		
		double tuniu_succeed_oddsStr;// 出票成功转化率
//		int tuniu_succeed_defeated_count = tuniu_out_ticket_succeed + tuniu_out_ticket_defeated;// 出票成功的个数加出票失败的个数
		double tuniu_out_ticket_XL = tj_Hc_orderInfo_Today_Service.queryOut_ticket_XL(paramMap);// 总出票效率
		double tuniu_pay_time = tj_Hc_orderInfo_Today_Service.queryPay_time(paramMap);// 支付时长

		double tuniu_ticket_cgl;// 订单成功率
		double tuniu_ticket_sbl;// 订单失败率
		//double tuniu_bx_countMoney = ((double)tuniu_out_ticket_num_succeed - (double)tuniu_refund_cp_count)*0.4; // 保险总利润
		String tuniu_succeed_odds = null;// 出票成功转化率(转换为0.00%后)
		String tuniu_succeed_cgl = null;// 订单成功率(转换为0.00%后)
		String tuniu_succeed_sbl = null;// 订单失败率(转换为0.00%后)
		if (tuniu_order_count != 0) {
			tuniu_succeed_oddsStr = ((double) tuniu_out_ticket_succeed / (double) tuniu_order_count); // 出票成功转化率
			// double 转换为 String类型的“**.**%”
			tuniu_succeed_odds = SwitchUtils.format_perCent(tuniu_succeed_oddsStr);
		} else {
			tuniu_succeed_odds = "0.00%"; // 出票成功率
		}
		if (tuniu_order_count != 0) {
			tuniu_ticket_cgl = ((double) tuniu_out_ticket_succeed / (double) tuniu_order_count);// 订单成功率
			tuniu_ticket_sbl = ((double) (tuniu_out_ticket_defeated +tuniu_out_time_order+tuniu_cancel_order)/ (double) tuniu_order_count);// 订单失败率
			tuniu_succeed_cgl = SwitchUtils.format_perCent(tuniu_ticket_cgl);
			tuniu_succeed_sbl = SwitchUtils.format_perCent(tuniu_ticket_sbl);
		} else {
			tuniu_succeed_cgl = "0.00%";
			tuniu_succeed_sbl = "0.00%";
		}
		

		double tuniu_ticket_hold_cgl;// 订单占座成功率
		double tuniu_ticket_hold_sbl;// 订单占座失败率
		String tuniu_holdseat_cgl = null;// 订单占座成功率(转换为0.00%后)
		String tuniu_holdseat_sbl = null;// 订单占座失败率(转换为0.00%后)
		if (tuniu_order_count != 0) {
			tuniu_ticket_hold_cgl = ((double) (tuniu_out_ticket_succeed+tuniu_out_time_order+tuniu_cancel_order) / (double) tuniu_order_count);// 订单成功率
			tuniu_ticket_hold_sbl = ((double) tuniu_out_ticket_defeated/ (double) tuniu_order_count);// 订单失败率
			tuniu_holdseat_cgl = SwitchUtils.format_perCent(tuniu_ticket_hold_cgl);
			tuniu_holdseat_sbl = SwitchUtils.format_perCent(tuniu_ticket_hold_sbl);
		} else {
			tuniu_holdseat_cgl = "0.00%";
			tuniu_holdseat_sbl = "0.00%";
		}
		
		Map<String, Object> tuniu_map = new HashMap<String, Object>();
		tuniu_map.put("tj_id", CreateIDUtil.createID("TJ"));
		tuniu_map.put("out_ticket_succeed", tuniu_out_ticket_succeed);
		tuniu_map.put("out_ticket_defeated", tuniu_out_ticket_defeated);
		tuniu_map.put("order_count", tuniu_order_count);
		tuniu_map.put("succeed_money", tuniu_succeed_money);
		tuniu_map.put("defeated_money", tuniu_defeated_money);
		tuniu_map.put("refund_money", tuniu_refund_money);
		tuniu_map.put("succeed_odds", tuniu_succeed_odds);
		tuniu_map.put("order_time", create_time);
		tuniu_map.put("refund_count", tuniu_refund_count);
		tuniu_map.put("ticket_count", tuniu_ticket_count);
		tuniu_map.put("succeed_cgl", tuniu_succeed_cgl);
		tuniu_map.put("succeed_sbl", tuniu_succeed_sbl);
		tuniu_map.put("out_ticket_XL", tuniu_out_ticket_XL);
		tuniu_map.put("pay_time", tuniu_pay_time);
		tuniu_map.put("over_time", tuniu_over_time);
		//tuniu_map.put("bx_countMoney", tuniu_bx_countMoney);
		tuniu_map.put("out_time_order", tuniu_out_time_order);
		tuniu_map.put("cancel_order", tuniu_cancel_order);
		
		tuniu_map.put("holdseat_cgl", tuniu_holdseat_cgl);
		tuniu_map.put("holdseat_sbl", tuniu_holdseat_sbl);
		
		tuniu_map.put("channel", "tuniu");
		// TODO
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
		int todaytuniuCount = tj_Hc_orderInfo_Today_Service.query19eTodayCount(tuniu_map);
		if (todaytuniuCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Hc_orderInfo_Today_Service.addToTj_Hc_orderInfo(tuniu_map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Hc_orderInfo_Today_Service.updateToTj_Hc_orderInfo(tuniu_map);
		}
		/************************************ 途牛统计结束 *****************************************/
		
		
		
		logger.info("Tj_Hc_OrderInfo_Today_Job自动执行JOB结束");
	}
	

	/**
	 * 获取刷新页面各参数
	 * @param name
	 * @param key
	 * @return String
	 */
	public String getCountsValue(String name,String key){
			String value="";
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			Map<String, String> paramMap1  = new HashMap<String, String>();
			String params = null;
			try {
				params = UrlFormatUtil.CreateUrl("", paramMap1, "", "UTF-8");
				value = HttpPostUtil.sendAndRecive(agentLoginNum, params);
				
				if(value.indexOf("html")>-1){
					value = "";
					logger.info("代理商登陆次数为：空" );
				}else{
					value = String.valueOf(Integer.parseInt(value));
					logger.info("代理商登陆次数为：" + value );
				}
			} catch (Exception e) {
				logger.info("查询代理商登陆次数失败。"+e);
			}
			
			MemcachedUtil.getInstance().setAttribute(key, value, 60*60*1000);
		}else{
			value = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return value;
	}
	
}
