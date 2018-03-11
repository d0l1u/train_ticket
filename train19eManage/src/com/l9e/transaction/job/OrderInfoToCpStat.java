package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.OrderInfoToCpStatService;
import com.l9e.util.DateUtil;
import com.l9e.util.SwitchUtils;

/**
 * 统计流程
 * @author liht
 *
 */
@Component("orderInfoToCpStat")
public class OrderInfoToCpStat {
	private static final Logger logger = Logger.getLogger(OrderInfoToCpStat.class);
	@Resource
	OrderInfoToCpStatService orderInfoToCpStatService; 
	public void queryToInsertOrderStat(){
			
			int out_ticket_succeed = orderInfoToCpStatService.queryPre_day_out_ticket_succeed();//前一天出票成功的个数
			int out_ticket_defeated = orderInfoToCpStatService.queryPre_day_out_ticket_defeated();//前一天出票失败的个数
			int order_count = orderInfoToCpStatService.queryPre_day_order_count();//前一天的总个数
			int preparative_count = orderInfoToCpStatService.queryPre_preparative_count();//查询前一天预下单的个数
			int pay_defeated = orderInfoToCpStatService.queryPre_pay_defeated();//查询前一天支付失败的个数
			int refund_count = orderInfoToCpStatService.queryPre_refund_count();//查询前一天发起退款并成功的个数
			double succeed_money = orderInfoToCpStatService.queryPre_succeed_money();//前一天出票成功的总金额
			double defeated_money = orderInfoToCpStatService.queryPre_defeated_money();//前一天出票失败的总金额
			int bx_count = orderInfoToCpStatService.queryPre_bx_count();//前一天出售保险的个数
			int ticket_count = orderInfoToCpStatService.queryPre_ticket_count();//查询出前一天出售的票数
			double bx_countMoney = orderInfoToCpStatService.queryPre_bx_countMoney();//前一天出售保险的总价
			int activeAgent = orderInfoToCpStatService.queryPre_activeAgent();//前一天活跃用户数
			
			double succeed_oddsStr;//出票成功转化率
			double defeated_oddsStr;///出票失败转化率
			int succeed_defeated_count = out_ticket_succeed+out_ticket_defeated;//出票成功的个数加出票失败的个数
			double ticket_cgl ;//订单成功率
			double ticket_sbl ;//订单失败率
			String succeed_odds = null;//出票成功转化率(数据库字段)
			String defeated_odds = null;//出票失败转化率(数据库字段)
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
			map.put("order_time", df.format(date));
			map.put("preparative_count", preparative_count);
			map.put("refund_count", refund_count);
			map.put("ticket_count", ticket_count);
			map.put("succeed_cgl", succeed_cgl);
			map.put("succeed_sbl", succeed_sbl);
			map.put("pay_defeated", pay_defeated);
			map.put("activeAgent", activeAgent);
			//添加到表cp_statInfo表中
			orderInfoToCpStatService.addOrderInfoToCpStat(map);
		}
		
}
