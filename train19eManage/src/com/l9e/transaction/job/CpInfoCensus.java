package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CpInfoCensusService;

@Component("cpInfoCensus_job")
public class CpInfoCensus {
	
	private static final Logger logger = Logger.getLogger(CpInfoCensus.class);
	@Resource
	private CpInfoCensusService cpInfoCensusService;
	public void queryToCp_Stat(){
		String channel ;
		int order_succeed ;
		int order_defeated ;
		int order_count ;
		int ticket_count ;
		double succeed_money ;
		double defeated_money ;
		
		int table_count = cpInfoCensusService.query_table_count();
		if(table_count==0){
			logger.info("table中"+table_count+"条信息，进入第一次执行统计..");
			List<Map<String,String>>date_List = cpInfoCensusService.queryDateList();
			String create_time = null ;
			for(int i=0;i<date_List.size()-1;i++){
				create_time=date_List.get(i).get("create_time").toString().trim();
				channel= "19e";
				Map<String,String>query_Map = new HashMap<String,String>();
				query_Map.put("channel", channel);
				query_Map.put("create_time", create_time);
				
				order_succeed = cpInfoCensusService.queryPre_day_order_succeed( query_Map);//前一天订单成功的个数
				order_defeated = cpInfoCensusService.queryPre_day_order_defeated( query_Map);//前一天订单失败的个数
				order_count = order_succeed+order_defeated;//前一天的总个数
				succeed_money = cpInfoCensusService.queryPre_succeed_money( query_Map);//前一天出票成功的总金额
				defeated_money = cpInfoCensusService.queryPre_defeated_money( query_Map);//前一天出票失败的总金额
				ticket_count = cpInfoCensusService.queryPre_ticket_count( query_Map);//查询出前一天出售的票数
				Map<String,Object>add_Map_19e = new HashMap<String,Object>();
				add_Map_19e.put("order_time", create_time);
				add_Map_19e.put("order_succeed", order_succeed);
				add_Map_19e.put("order_defeated", order_defeated);
				add_Map_19e.put("order_count", order_count);
				add_Map_19e.put("succeed_money", succeed_money);
				add_Map_19e.put("defeated_money", defeated_money);
				add_Map_19e.put("ticket_count", ticket_count);
				add_Map_19e.put("channel", channel);
				
				channel ="qunar";
				query_Map.put("channel", channel);
				order_succeed = cpInfoCensusService.queryPre_day_order_succeed( query_Map);//前一天订单成功的个数
				order_defeated = cpInfoCensusService.queryPre_day_order_defeated( query_Map);//前一天订单失败的个数
				order_count = order_succeed+order_defeated;//前一天的总个数
				succeed_money = cpInfoCensusService.queryPre_succeed_money( query_Map);//前一天出票成功的总金额
				defeated_money = cpInfoCensusService.queryPre_defeated_money( query_Map);//前一天出票失败的总金额
				ticket_count = cpInfoCensusService.queryPre_ticket_count( query_Map);//查询出前一天出售的票数
				Map<String,Object>add_Map_qunar = new HashMap<String,Object>();
				add_Map_qunar.put("order_time", create_time);
				add_Map_qunar.put("order_succeed", order_succeed);
				add_Map_qunar.put("order_defeated", order_defeated);
				add_Map_qunar.put("order_count", order_count);
				add_Map_qunar.put("succeed_money", succeed_money);
				add_Map_qunar.put("defeated_money", defeated_money);
				add_Map_qunar.put("ticket_count", ticket_count);
				add_Map_qunar.put("channel", channel);
				
				cpInfoCensusService.addCensusTocp_statInfo(add_Map_19e,add_Map_qunar);
			}
		}else{
			//获取当前时间的前一天 格式为yyyy-MM-dd
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);    //得到前一天
			Date date = calendar.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date_pre = df.format(date);
			
			channel= "19e";
			Map<String,String>query_Map = new HashMap<String,String>();
			query_Map.put("channel", channel);
			query_Map.put("create_time", date_pre);
			
			order_succeed = cpInfoCensusService.queryPre_day_order_succeed( query_Map);//前一天订单成功的个数
			order_defeated = cpInfoCensusService.queryPre_day_order_defeated( query_Map);//前一天订单失败的个数
			order_count = order_succeed+order_defeated;//前一天的总个数
			succeed_money = cpInfoCensusService.queryPre_succeed_money( query_Map);//前一天出票成功的总金额
			defeated_money = cpInfoCensusService.queryPre_defeated_money( query_Map);//前一天出票失败的总金额
			ticket_count = cpInfoCensusService.queryPre_ticket_count( query_Map);//查询出前一天出售的票数
			Map<String,Object>add_Map_19e = new HashMap<String,Object>();
			add_Map_19e.put("order_time", date_pre);
			add_Map_19e.put("order_succeed", order_succeed);
			add_Map_19e.put("order_defeated", order_defeated);
			add_Map_19e.put("order_count", order_count);
			add_Map_19e.put("succeed_money", succeed_money);
			add_Map_19e.put("defeated_money", defeated_money);
			add_Map_19e.put("ticket_count", ticket_count);
			add_Map_19e.put("channel", channel);
			
			channel ="qunar";
			query_Map.put("channel", channel);
			order_succeed = cpInfoCensusService.queryPre_day_order_succeed( query_Map);//前一天订单成功的个数
			order_defeated = cpInfoCensusService.queryPre_day_order_defeated( query_Map);//前一天订单失败的个数
			order_count = order_succeed+order_defeated;//前一天的总个数
			succeed_money = cpInfoCensusService.queryPre_succeed_money( query_Map);//前一天出票成功的总金额
			defeated_money = cpInfoCensusService.queryPre_defeated_money( query_Map);//前一天出票失败的总金额
			ticket_count = cpInfoCensusService.queryPre_ticket_count( query_Map);//查询出前一天出售的票数
			Map<String,Object>add_Map_qunar = new HashMap<String,Object>();
			add_Map_qunar.put("order_time", date_pre);
			add_Map_qunar.put("order_succeed", order_succeed);
			add_Map_qunar.put("order_defeated", order_defeated);
			add_Map_qunar.put("order_count", order_count);
			add_Map_qunar.put("succeed_money", succeed_money);
			add_Map_qunar.put("defeated_money", defeated_money);
			add_Map_qunar.put("ticket_count", ticket_count);
			add_Map_qunar.put("channel", channel);
			
			cpInfoCensusService.addCensusTocp_statInfo(add_Map_19e,add_Map_qunar);
		}
	}
}
