package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.Tj_DealerService;
import com.l9e.util.DbContextHolder;
import com.sun.xml.internal.fastinfoset.util.StringArray;

/**
 * 
 * @author zhangjc
 * 
 */
@Component("tj_Dealer_Job")
public class Tj_Dealer_Job {

	private static final Logger logger = Logger.getLogger(Tj_Dealer_Job.class);
	@Resource
	Tj_DealerService tj_dealerService;
	
	public void queryDealerToInsertTj() {
		/************************************ 开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0); // 得到
		calendar.set(2015,05,01);
		DateFormat df = new SimpleDateFormat("yyyy-MM-01");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DATE, 0); 
		Date date2 = calendar2.getTime();
		String create_time = df.format(date2);// 日期
		logger.info("tj_Dealer_Job自动执行JOB，统计时间为："+ create_time);
		for(int j=0;j<2;j++){
			if(j==1){
				calendar.set(2015,06,01);
			}
			Date date = calendar.getTime();
			String next_mouth = df.format(date);// 当前月
			calendar.add(Calendar.MONTH, -1);
			Date date3 = calendar.getTime();
			String this_mouth = df.format(date3);// 下个月
			paramMap.put("this_mouth", this_mouth);
			paramMap.put("next_mouth", next_mouth);
			System.out.println(this_mouth+"########" +next_mouth);
			List<String> listDealer = tj_dealerService.queryDealerIdByMouth(paramMap);
			if(listDealer.size() == 0){
				logger.info("没有该"+this_mouth+"日期内的数据！");
			}else{
				System.out.println(listDealer.size());
				for(int d=0;d<listDealer.size();d++){
					String dealer_id =  listDealer.get(d).toString();
					paramMap.put("dealer_id",dealer_id);
					String area_name = tj_dealerService.queryAreaNameByDealerId(paramMap);
					String pay_money = tj_dealerService.queryPayMoneyByDealerId(paramMap);
					String order_count = tj_dealerService.queryOrderCountByDealerId(paramMap);
					String refund_count = tj_dealerService.queryRefundCountByDealerId(paramMap);
					String refund_money = tj_dealerService.queryRefundMoneyByDealerId(paramMap);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("dealer_id", dealer_id);
					map.put("area_name", area_name);
					map.put("pay_money", pay_money);
					map.put("order_count", order_count);
					map.put("refund_count", refund_count);
					map.put("refund_money", refund_money);
					map.put("date_time",this_mouth);
					map.put("create_time","1");
					DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
					try{
						tj_dealerService.addToTj_Dealer(map);
						logger.info("添加代理商统计数据 成功，代理商id："+dealer_id);
					}catch (Exception e) {
						logger.error("添加代理商统计数据失败"+e);
					}
				}
			}
		}
		logger.info("tj_Dealer_Job自动执行JOB，结束");
		/************************************ 结束 *****************************************/
	}
	public static void main(String[] args) {
		Tj_Dealer_Job tj=new Tj_Dealer_Job();
		tj.queryDealerToInsertTj();
	}
}
