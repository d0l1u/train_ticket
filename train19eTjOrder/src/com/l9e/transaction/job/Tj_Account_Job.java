package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.Tj_Account_Service;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.DbContextHolder;

/**
 * 对当天的数据每隔十分钟进行一次统计，然后更新tj_account表里面的数据
 * @author zhangjc
 * 
 */
@Component("tj_Account_Job")
public class Tj_Account_Job {

	private static final Logger logger = Logger.getLogger(Tj_Account_Job.class);
	
	@Resource
	Tj_Account_Service tj_Account_Service;
	
	public void queryAccountToInsertUpdateTj() {
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
		logger.info("tj_Account_Job自动执行JOB，统计时间为："+ df2.format(date));
		String create_time = df.format(date);// 时间
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("create_time", create_time);
		paramMap.put("account_source", "1");
		Map<String, Object> noMap = new HashMap<String, Object>();
		noMap.put("regist_status", "22");
		noMap.put("is_output", "0");
		int regist_free=tj_Account_Service.regist_num(noMap);
		
		int add_regist=tj_Account_Service.regist_num(paramMap);
		paramMap.put("regist_status", "22");
		int regist_pass=tj_Account_Service.regist_num(paramMap);
		paramMap.put("regist_status", "55");
		int regist_wait=tj_Account_Service.regist_num(paramMap);
		paramMap.put("regist_status", "33");
		int regist_allfail=tj_Account_Service.regist_num(paramMap);
		paramMap.put("fail_reason", "2");
		int regist_success=tj_Account_Service.regist_num(paramMap);
		paramMap.put("fail_reason", "1");
		int regist_fail=tj_Account_Service.regist_num(paramMap);
		int regist_other=regist_allfail-regist_success-regist_fail;
		
		Map<String, Object> paramMap2 = new HashMap<String, Object>();	
		int account=tj_Account_Service.account_num(paramMap2);
		paramMap2.put("acc_status", "33");
		int account_wait=tj_Account_Service.account_num(paramMap2);
		paramMap2.put("acc_status", "22");
		int account_stop=tj_Account_Service.account_num(paramMap2);
		Map<String, Object> paramMap3 = new HashMap<String, Object>();	
		paramMap3.put("active_time", 1);
		int account_land=tj_Account_Service.account_num(paramMap3);
		int account_add=tj_Account_Service.account_num(paramMap);
		
		Map<String, Object> qunarMap = new HashMap<String, Object>();	
		qunarMap.put("channel", "qunar");
		int qunar_num=tj_Account_Service.account_num(qunarMap);
		qunarMap.put("acc_status", "22");
		int qunar_stop=tj_Account_Service.account_num(qunarMap);
		qunarMap.put("stop_reason", "1");
		int qunar_seal=tj_Account_Service.account_num(qunarMap);
		qunarMap.put("stop_reason", "2");
		int qunar_toomuch=tj_Account_Service.account_num(qunarMap);
		qunarMap.put("stop_reason", "3");
		int qunar_out=tj_Account_Service.account_num(qunarMap);
		qunarMap.put("stop_reason", "4");
		int qunar_not_real=tj_Account_Service.account_num(qunarMap);
		qunarMap.put("stop_reason", "5");
		int qunar_shici=tj_Account_Service.account_num(qunarMap);
		qunarMap.put("stop_reason", "6");
		int qunar_goback=tj_Account_Service.account_num(qunarMap);
		Map<String, Object> qunarMap2 = new HashMap<String, Object>();	
		qunarMap2.put("channel", "qunar");
		qunarMap2.put("acc_status", "22");
		qunarMap2.put("create_time", create_time);
		int qunar_stop_today=tj_Account_Service.account_num(qunarMap2);
		
		Map<String, Object> tongchengMap = new HashMap<String, Object>();	
		tongchengMap.put("channel", "tongcheng");
		int tongcheng_num=tj_Account_Service.account_num(tongchengMap);
		tongchengMap.put("acc_status", "22");
		int tongcheng_stop=tj_Account_Service.account_num(tongchengMap);
		tongchengMap.put("stop_reason", "1");
		int tongcheng_seal=tj_Account_Service.account_num(tongchengMap);
		tongchengMap.put("stop_reason", "2");
		int tongcheng_toomuch=tj_Account_Service.account_num(tongchengMap);
		tongchengMap.put("stop_reason", "3");
		int tongcheng_out=tj_Account_Service.account_num(tongchengMap);
		tongchengMap.put("stop_reason", "4");
		int tongcheng_not_real=tj_Account_Service.account_num(tongchengMap);
		tongchengMap.put("stop_reason", "5");
		int tongcheng_shici=tj_Account_Service.account_num(tongchengMap);
		tongchengMap.put("stop_reason", "6");
		int tongcheng_goback=tj_Account_Service.account_num(tongchengMap);
		Map<String, Object> tongchengMap2 = new HashMap<String, Object>();	
		tongchengMap2.put("channel", "tongcheng");
		tongchengMap2.put("acc_status", "22");
		tongchengMap2.put("create_time", create_time);
		int tongcheng_stop_today=tj_Account_Service.account_num(tongchengMap2);
		
		Map<String, Object> elongMap = new HashMap<String, Object>();	
		elongMap.put("channel", "elong");
		int elong_num=tj_Account_Service.account_num(elongMap);
		elongMap.put("acc_status", "22");
		int elong_stop=tj_Account_Service.account_num(elongMap);
		elongMap.put("stop_reason", "1");
		int elong_seal=tj_Account_Service.account_num(elongMap);
		elongMap.put("stop_reason", "2");
		int elong_toomuch=tj_Account_Service.account_num(elongMap);
		elongMap.put("stop_reason", "3");
		int elong_out=tj_Account_Service.account_num(elongMap);
		elongMap.put("stop_reason", "4");
		int elong_not_real=tj_Account_Service.account_num(elongMap);
		elongMap.put("stop_reason", "5");
		int elong_shici=tj_Account_Service.account_num(elongMap);
		elongMap.put("stop_reason", "6");
		int elong_goback=tj_Account_Service.account_num(elongMap);
		Map<String, Object> elongMap2 = new HashMap<String, Object>();	
		elongMap2.put("channel", "elong");
		elongMap2.put("acc_status", "22");
		elongMap2.put("create_time", create_time);
		int elong_stop_today=tj_Account_Service.account_num(elongMap2);
		
		
		Map<String, Object> dMap = new HashMap<String, Object>();
		dMap.put("regist_status", "55");
		int regist_all_wait=tj_Account_Service.regist_num(dMap);
		dMap.put("regist_status", "44");
		int regist_all_hand=tj_Account_Service.regist_num(dMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tj_id", CreateIDUtil.createID("TJ"));
		map.put("create_time", df.format(date));
		map.put("add_regist", add_regist);
		map.put("regist_pass", regist_pass);
		map.put("regist_wait", regist_wait);
		map.put("regist_success", regist_success);
		map.put("regist_fail", regist_fail);
		map.put("regist_other", regist_other);
		map.put("regist_free", regist_free);
		map.put("account", account);
		map.put("account_wait", account_wait);
		map.put("account_stop", account_stop);
		
		map.put("regist_all_wait", regist_all_wait);//注册表全部待核验的
		map.put("regist_all_hand", regist_all_hand);//注册表全部人工注册的
		
		map.put("account_land", account_land);
		map.put("account_add", account_add);
		
		map.put("qunar_num", qunar_num);
		map.put("qunar_stop", qunar_stop);
		map.put("qunar_stop_today", qunar_stop_today);
		map.put("qunar_seal", qunar_seal);
		map.put("qunar_toomuch", qunar_toomuch);
		map.put("qunar_out", qunar_out);
		map.put("qunar_not_real", qunar_not_real);
		map.put("qunar_shici", qunar_shici);
		map.put("qunar_goback", qunar_goback);
		
		map.put("tongcheng_num", tongcheng_num);
		map.put("tongcheng_stop", tongcheng_stop);
		map.put("tongcheng_stop_today", tongcheng_stop_today);
		map.put("tongcheng_seal", tongcheng_seal);
		map.put("tongcheng_toomuch", tongcheng_toomuch);
		map.put("tongcheng_out", tongcheng_out);
		map.put("tongcheng_not_real", tongcheng_not_real);
		map.put("tongcheng_shici", tongcheng_shici);
		map.put("tongcheng_goback", tongcheng_goback);
		
		map.put("elong_num", elong_num);
		map.put("elong_stop", elong_stop);
		map.put("elong_stop_today", elong_stop_today);
		map.put("elong_seal", elong_seal);
		map.put("elong_toomuch", elong_toomuch);
		map.put("elong_out", elong_out);
		map.put("elong_not_real", elong_not_real);
		map.put("elong_shici", elong_shici);
		map.put("elong_goback", elong_goback);
		
		
		String book_num = tj_Account_Service.setting_value("robot_app_book_num");
		String contact_num = tj_Account_Service.setting_value("contact_num");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("book_num", book_num);
		param.put("contact_num", contact_num);
		
		param.put("channel", "qunar");
		int qunar_can_use = tj_Account_Service.account_can_use(param);
		map.put("qunar_can_use", qunar_can_use);
		param.put("channel", "elong");
		int elong_can_use = tj_Account_Service.account_can_use(param);
		map.put("elong_can_use", elong_can_use);
		param.put("channel", "tongcheng");
		int tongcheng_can_use = tj_Account_Service.account_can_use(param);
		map.put("tongcheng_can_use", tongcheng_can_use);
		
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
		int today19eCount = tj_Account_Service.query19eTodayCount(map);
		if (today19eCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Account_Service.addToTj_Account(map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Account_Service.updateToTj_Account(map);
		}
		/************************************ 19e统计结束 *****************************************/
		logger.info("tj_Account_Job自动执行JOB结束");
		
	}
	

}
