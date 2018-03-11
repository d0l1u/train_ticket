package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.OrderInfoToCpStatOneService;

import com.l9e.util.SwitchUtils;

/**
 * 统计流程
 * @author liht
 *
 */
@Component("orderInfoToCpStatOne")
public class OrderInfoToCpStatOne {
	private static final Logger logger = Logger.getLogger(OrderInfoToCpStatOne.class);
	@Resource
	OrderInfoToCpStatOneService orderInfoToCpStatOneService; 
	public void queryToInsertOrderStatOne(){
			int query_Hc_StatInfo = orderInfoToCpStatOneService.query_Hc_StatInfo();
			List<Map<String,Object>>map_list = orderInfoToCpStatOneService.queryDate();
			Map<String,Object>hashMap = map_list.get(0);
			List<Map<String,Object>>list =  orderInfoToCpStatOneService.createDateList(hashMap);
				
		if(query_Hc_StatInfo==0){
			for(int i=0;i<list.size()-1;i++){
				Map<String, Object> date_map = list.get(i);
				String date_time = (String) date_map.get("create_time");
				int out_ticket_succeed = orderInfoToCpStatOneService.queryPre_day_out_ticket_succeed(date_time);//当天出票成功的个数
				int out_ticket_defeated = orderInfoToCpStatOneService.queryPre_day_out_ticket_defeated(date_time);//当出票失败的个数
				int order_count = orderInfoToCpStatOneService.queryPre_day_order_count(date_time);//当天的总个数
				int preparative_count = orderInfoToCpStatOneService.queryPre_preparative_count(date_time);//查询当天预下单的个数
				int pay_defeated = orderInfoToCpStatOneService.queryPre_pay_defeated(date_time);//查询当天支付失败的个数
				int refund_count = orderInfoToCpStatOneService.queryPre_refund_count(date_time);//查询当天发起退款并成功的个数
				double succeed_money = orderInfoToCpStatOneService.queryPre_succeed_money(date_time);//当天出票成功的总金额
				double defeated_money = orderInfoToCpStatOneService.queryPre_defeated_money(date_time);//当天出票失败的总金额
				int bx_count = orderInfoToCpStatOneService.queryPre_bx_count(date_time);//当天出售保险的个数
				int ticket_count = orderInfoToCpStatOneService.queryPre_ticket_count(date_time);//查询出当天出售的票数
				double bx_countMoney = orderInfoToCpStatOneService.queryPre_bx_countMoney(date_time);//当天出售保险的总价
				int activeAgent = orderInfoToCpStatOneService.queryPre_activeAgent(date_time);//当天活跃用户数
				
				double succeed_oddsStr;//出票成功转化率
				double defeated_oddsStr;///出票失败转化率
				int succeed_defeated_count = out_ticket_succeed+out_ticket_defeated;//出票成功的个数加出票失败的个数
				double ticket_cgl ;//订单成功率
				double ticket_sbl ;//订单失败率
				String succeed_odds = null;//出票成功转化率(转换为0.00%后)
				String defeated_odds = null;//出票失败转化率(转换为0.00%后)
				String succeed_cgl = null ;//订单成功率(转换为0.00%后)
				String succeed_sbl = null ;//订单失败率(转换为0.00%后)
				if(order_count != 0){
					succeed_oddsStr = ((double)out_ticket_succeed/(double)order_count) ; //出票成功转化率
					defeated_oddsStr = ((double)out_ticket_defeated/(double)order_count);//出票失败转化率
					//doble 转换为 String类型的“**.**%”
					succeed_odds = SwitchUtils.format_perCent(succeed_oddsStr);
					defeated_odds =  SwitchUtils.format_perCent(defeated_oddsStr);
				}else{
					succeed_odds="0.00%"; //出票成功率
					defeated_odds ="0.00%";;//出票失败率
				}
				if(succeed_defeated_count!=0){
					ticket_cgl=((double)out_ticket_succeed/(double)succeed_defeated_count);//订单成功率
					ticket_sbl=((double)out_ticket_defeated/(double)succeed_defeated_count);//订单失败率
					succeed_cgl=SwitchUtils.format_perCent(ticket_cgl);
				    succeed_sbl=SwitchUtils.format_perCent(ticket_sbl);
				}else{
					succeed_cgl="0.00%";
					succeed_sbl="0.00%";
				}
				//获取当前时间的前一天 格式为yyyy-MM-dd
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -1);    //得到前一天
				Date date = calendar.getTime();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Map<String,Object>map = new HashMap<String,Object>();
				map.put("out_ticket_succeed", out_ticket_succeed);
				map.put("out_ticket_defeated", out_ticket_defeated);
				map.put("order_count", order_count);
				map.put("succeed_money", succeed_money);
				map.put("defeated_money", defeated_money);
				map.put("bx_count", bx_count);
				map.put("bx_countMoney", bx_countMoney);
				map.put("succeed_odds", succeed_odds);
				map.put("defeated_odds", defeated_odds);
				map.put("order_time",date_time);
				map.put("preparative_count", preparative_count);
				map.put("refund_count", refund_count);
				map.put("ticket_count", ticket_count);
				map.put("succeed_cgl", succeed_cgl);
				map.put("succeed_sbl", succeed_sbl);
				map.put("pay_defeated", pay_defeated);
				map.put("activeAgent", activeAgent);
				//添加到表cp_statInfo表中
				orderInfoToCpStatOneService.addOrderInfoToCpStat(map);
		}
	
	}

	}
	
		
}
