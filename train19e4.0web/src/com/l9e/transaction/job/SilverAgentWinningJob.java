package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.l9e.transaction.service.UserInfoService;
import common.Logger;
/**
 * 处理银牌代理商中奖job
 * @author yangchao
 *
 */

@Component("silverAgentWinningJob")
public class SilverAgentWinningJob {

	private static final Logger logger=Logger.getLogger(SilverAgentWinningJob.class);
	
	@Resource
	private UserInfoService userInfoService;
	
	public void getSilverAgentWinningInfo(){
		logger.info("开始抽取银牌代理商的中奖信息！");
		List<Map<String,Object>> passNumAgentIdList=userInfoService.queryAgentPassNumAndId();
		if(passNumAgentIdList!=null && passNumAgentIdList.size()>0){
			this.silverWinningInfo(passNumAgentIdList);
		}
	}
	
	public void silverWinningInfo(List<Map<String,Object>> passNumAgentIdList){
		logger.info("开始抽取银牌代理商的中奖信息！");
		List<String> silverAgentList=new ArrayList<String>();
		for(int i=0;i<passNumAgentIdList.size();i++){
			Long num=(Long) passNumAgentIdList.get(i).get("passNum");
			if(num<=4 && num>=2){
				silverAgentList.add((String) passNumAgentIdList.get(i).get("user_id"));
			}
		}
		logger.info("银牌代理商数量："+silverAgentList.size());
		int moneyStart=0;
		int moneyEnd=0;
		//这里循环中的i代表的是中奖的类型
		for(int i=0;i<3;i++){
			Map<String,Object> goldMap=new HashMap<String,Object>();
			//选择不同的价格区间
			if(i==0){
				moneyStart=0;
				moneyEnd=49;
			}else if(i==1){
				moneyStart=50;
				moneyEnd=79;
			}else {
				moneyStart=80;
				moneyEnd=100;
			}
			goldMap.put("moneyStart", moneyStart);
			goldMap.put("moneyEnd", moneyEnd);
			goldMap.put("dealer_id",silverAgentList);
			List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
			if(silverAgentList!=null && silverAgentList.size()>0){
				agentOrderList=userInfoService.queryAgentOrderInfo(goldMap);
				logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的银牌代理商订单数量为："+agentOrderList.size());
			}else{
				logger.info("银牌代理商人数为0，无法抽奖！");
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("extend", "1");
				map.put("opt_person", "silverJob");
				userInfoService.addAgentwinning(map);
				return;
			}
			if(agentOrderList!=null && agentOrderList.size()>0){
				Map<String,Object> agentWinningMap=new HashMap<String,Object>();
				//标识抽奖次数
				int num=0;
				//用来标识是否已经中奖
				boolean check=true;
				//如果抽50次都抽到同一个代理商，那就认命吧！
				while(num<50 && check==true){
					num=num+1;
					int orderNum=agentOrderList.size();
					int winningNum=(int) (orderNum*Math.random());
					logger.info("得到的随机数为："+winningNum);
					//先验证该代理商今天是否已经中奖
					agentWinningMap.put("agent_id", agentOrderList.get(winningNum).get("dealer_id"));
					agentWinningMap.put("agent_type","1");
					List<Map<String,Object>> list=userInfoService.queryAgentWinningDay(agentWinningMap);
					if(list==null || list.size()==0 ){
						check=false;
						agentWinningMap.put("dealer_name", agentOrderList.get(winningNum).get("dealer_name"));
						agentWinningMap.put("order_id", agentOrderList.get(winningNum).get("order_id"));
						agentWinningMap.put("winning_money", agentOrderList.get(winningNum).get("ticket_pay_money"));
						agentWinningMap.put("order_time",agentOrderList.get(winningNum).get("out_ticket_time"));
						agentWinningMap.put("opt_person","silverJob");
						agentWinningMap.put("winning_type",i+"");
						agentWinningMap.put("from_city", agentOrderList.get(winningNum).get("from_city"));
						agentWinningMap.put("to_city", agentOrderList.get(winningNum).get("to_city"));
						agentWinningMap.put("train_no", agentOrderList.get(winningNum).get("train_no"));
						agentWinningMap.put("seat_type", agentOrderList.get(winningNum).get("seat_type"));
						userInfoService.addAgentwinning(agentWinningMap);
						logger.info("插入银牌中奖数据成功，中奖代理商id为："+agentOrderList.get(winningNum).get("dealer_id")+" 中奖类型为："+i+"  订单金额为："+agentOrderList.get(winningNum).get("ticket_pay_money"));
					}else{
						if(num!=50){
							logger.info("该用户已经中过奖，开始重新抽奖！");
						}else{
							//抽奖50次都抽到同一用户！
							agentWinningMap.put("dealer_name", agentOrderList.get(winningNum).get("dealer_name"));
							agentWinningMap.put("order_id", agentOrderList.get(winningNum).get("order_id"));
							agentWinningMap.put("winning_money", agentOrderList.get(winningNum).get("ticket_pay_money"));
							agentWinningMap.put("order_time",agentOrderList.get(winningNum).get("out_ticket_time"));
							agentWinningMap.put("opt_person","silverJob");
							agentWinningMap.put("winning_type",i+"");
							agentWinningMap.put("from_city", agentOrderList.get(winningNum).get("from_city"));
							agentWinningMap.put("to_city", agentOrderList.get(winningNum).get("to_city"));
							agentWinningMap.put("train_no", agentOrderList.get(winningNum).get("train_no"));
							agentWinningMap.put("seat_type", agentOrderList.get(winningNum).get("seat_type"));
							userInfoService.addAgentwinning(agentWinningMap);
							logger.info("抽奖50次都抽到同一代理商！插入银牌中奖数据成功，中奖代理商id为："+agentOrderList.get(winningNum).get("dealer_id")+" 中奖类型为："+i+"  订单金额为："+agentOrderList.get(winningNum).get("ticket_pay_money"));
						}
					}
				}
				
			}else{
				logger.info("银牌代理商在七天内，价格区间为 "+moneyStart+"元和"+moneyEnd+"元之间没有订单！");
				if(moneyEnd==49){
					logger.info("银牌代理商在价格区间0到49元不存在订单！准备抽取50到79之间的订单，取订单金额最小的作为中奖订单！");
					moneyStart=50;
					moneyEnd=79;
					this.silverWinning1(moneyStart, moneyEnd, silverAgentList);
				}else if(moneyStart==50 && moneyEnd==79){
					logger.info("银牌代理商在价格区间50到79元不存在订单！准备抽取0到49之间的订单，取订单金额最大的作为中奖订单！");
					moneyStart=0;
					moneyEnd=49;
					this.silverWinning2(moneyStart, moneyEnd, silverAgentList);
				}else if(moneyStart==80 && moneyEnd==100){
					logger.info("银牌代理商在价格区间80到100元不存在订单！准备抽取50到79之间的订单，取订单金额最大的作为中奖订单！");
					moneyStart=50;
					moneyEnd=79;
					this.silverWinning3(moneyStart, moneyEnd, silverAgentList);
				}
			}
		}
	}
	
	public void silverWinning1(int moneyStart,int moneyEnd,List<String> silverAgentList){
		logger.info("开始处理0到49元没有订单的情况！");
		Map<String,Object> goldMap=new HashMap<String,Object>();
		goldMap.put("moneyStart", moneyStart);
		goldMap.put("moneyEnd", moneyEnd);
		goldMap.put("dealer_id",silverAgentList);
		List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
		agentOrderList=userInfoService.queryAgentOrderInfo(goldMap);
		logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的银牌代理商订单数量为："+agentOrderList.size());
		if(agentOrderList!=null && agentOrderList.size()>0){
			List<Object> moneyList=new ArrayList<Object>();
			for(int i=0;i<agentOrderList.size();i++){
				moneyList.add(agentOrderList.get(i).get("ticket_pay_money"));
			}
			//对价钱进行排序
			Collections.sort(moneyList, new ValComparator());
			String winning_money=((Object) moneyList.get(0)).toString().trim();
			logger.info("得到排序过后的价钱"+winning_money);
			int num=0;
			for(int i=0;i<agentOrderList.size();i++){
				String orderMoney=((Object)agentOrderList.get(i).get("ticket_pay_money")).toString().trim();
				//用排序过后取到的价钱和原先的价钱相比，如果相等，则直接取这个订单，不管后面有没有价钱一样的订单
				if(orderMoney.equals(winning_money)){
					num=i;
					break;
				}
			}
			Map<String,Object> agentWinningMap=new HashMap<String,Object>();
			agentWinningMap.put("order_id", agentOrderList.get(num).get("order_id"));
			agentWinningMap.put("agent_id", agentOrderList.get(num).get("dealer_id"));
			agentWinningMap.put("dealer_name", agentOrderList.get(num).get("dealer_name"));
			agentWinningMap.put("winning_money", winning_money);
			agentWinningMap.put("order_time",agentOrderList.get(num).get("out_ticket_time"));
			agentWinningMap.put("opt_person","silverJob");
			agentWinningMap.put("agent_type","1");
			agentWinningMap.put("winning_type","0");
			agentWinningMap.put("from_city", agentOrderList.get(num).get("from_city"));
			agentWinningMap.put("to_city", agentOrderList.get(num).get("to_city"));
			agentWinningMap.put("train_no", agentOrderList.get(num).get("train_no"));
			agentWinningMap.put("seat_type", agentOrderList.get(num).get("seat_type"));
			userInfoService.addAgentwinning(agentWinningMap);
			logger.info("插入银牌中奖数据成功，中奖代理商id为："+agentOrderList.get(num).get("dealer_id")+" 中奖类型为：0"+"  订单金额为："+agentOrderList.get(num).get("ticket_pay_money"));
		}else{
			logger.info("0到49元没有订单，50到79也没有订单！");
		}
		
	}
	
	public void silverWinning2(int moneyStart,int moneyEnd,List<String> silverAgentList){
		logger.info("开始处理50到79元没有订单的情况！");
		Map<String,Object> goldMap=new HashMap<String,Object>();
		goldMap.put("moneyStart", moneyStart);
		goldMap.put("moneyEnd", moneyEnd);
		goldMap.put("dealer_id",silverAgentList);
		List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
		agentOrderList=userInfoService.queryAgentOrderInfo(goldMap);
		logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的银牌代理商订单数量为："+agentOrderList.size());
		if(agentOrderList!=null && agentOrderList.size()>0){
			List<Object> moneyList=new ArrayList<Object>();
			for(int i=0;i<agentOrderList.size();i++){
				moneyList.add(agentOrderList.get(i).get("ticket_pay_money"));
			}
			//对价钱进行排序
			Collections.sort(moneyList, new AlComparator());
			//取0到100价钱中最大的价钱
			String winning_money=((Object) moneyList.get(moneyList.size()-1)).toString().trim();
			logger.info("价钱"+winning_money);
			int num=0;
			for(int i=0;i<agentOrderList.size();i++){
				String orderMoney=((Object)agentOrderList.get(i).get("ticket_pay_money")).toString().trim();
				//用排序过后取到的价钱和原先的价钱相比，如果相等，则直接取这个订单，不管后面有没有价钱一样的订单
				if(orderMoney.equals(winning_money.trim())){
					num=i;
					break;
				}
			}
			Map<String,Object> agentWinningMap=new HashMap<String,Object>();
			agentWinningMap.put("order_id", agentOrderList.get(num).get("order_id"));
			agentWinningMap.put("agent_id", agentOrderList.get(num).get("dealer_id"));
			agentWinningMap.put("dealer_name", agentOrderList.get(num).get("dealer_name"));
			agentWinningMap.put("winning_money", winning_money);
			agentWinningMap.put("order_time",agentOrderList.get(num).get("out_ticket_time"));
			agentWinningMap.put("opt_person","silverJob");
			agentWinningMap.put("agent_type","1");
			agentWinningMap.put("winning_type","1");
			agentWinningMap.put("from_city", agentOrderList.get(num).get("from_city"));
			agentWinningMap.put("to_city", agentOrderList.get(num).get("to_city"));
			agentWinningMap.put("train_no", agentOrderList.get(num).get("train_no"));
			agentWinningMap.put("seat_type", agentOrderList.get(num).get("seat_type"));
			userInfoService.addAgentwinning(agentWinningMap);
			logger.info("插入银牌中奖数据成功，中奖代理商id为："+agentOrderList.get(num).get("dealer_id")+" 中奖类型为：0"+"  订单金额为："+agentOrderList.get(num).get("ticket_pay_money"));
		}else{
			logger.info("50到79元没有订单，0到49也没有订单！");
		}
		
	}
	
	public void silverWinning3(int moneyStart,int moneyEnd,List<String> silverAgentList){
		logger.info("开始处理80到100元没有订单的情况！");
		Map<String,Object> goldMap=new HashMap<String,Object>();
		goldMap.put("moneyStart", moneyStart);
		goldMap.put("moneyEnd", moneyEnd);
		goldMap.put("dealer_id",silverAgentList);
		List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
		agentOrderList=userInfoService.queryAgentOrderInfo(goldMap);
		logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的银牌代理商订单数量为："+agentOrderList.size());
		if(agentOrderList!=null && agentOrderList.size()>0){
			List<Object> moneyList=new ArrayList<Object>();
			for(int i=0;i<agentOrderList.size();i++){
				moneyList.add(agentOrderList.get(i).get("ticket_pay_money"));
			}
			//对价钱进行排序
			Collections.sort(moneyList, new ValComparator());
			//取0到120价钱中最大的价钱
			String winning_money=((Object) moneyList.get(moneyList.size()-1)).toString().trim();
			logger.info("价钱"+winning_money);
			int num=0;
			for(int i=0;i<agentOrderList.size();i++){
				String orderMoney=((Object)agentOrderList.get(i).get("ticket_pay_money")).toString().trim();
				//用排序过后取到的价钱和原先的价钱相比，如果相等，则直接取这个订单，不管后面有没有价钱一样的订单
				if(orderMoney.equals(winning_money.trim())){
					num=i;
					break;
				}
			}
			Map<String,Object> agentWinningMap=new HashMap<String,Object>();
			agentWinningMap.put("order_id", agentOrderList.get(num).get("order_id"));
			agentWinningMap.put("agent_id", agentOrderList.get(num).get("dealer_id"));
			agentWinningMap.put("dealer_name", agentOrderList.get(num).get("dealer_name"));
			agentWinningMap.put("winning_money", winning_money);
			agentWinningMap.put("order_time",agentOrderList.get(num).get("out_ticket_time"));
			agentWinningMap.put("opt_person","silverJob");
			agentWinningMap.put("agent_type","1");
			agentWinningMap.put("winning_type","2");
			agentWinningMap.put("from_city", agentOrderList.get(num).get("from_city"));
			agentWinningMap.put("to_city", agentOrderList.get(num).get("to_city"));
			agentWinningMap.put("train_no", agentOrderList.get(num).get("train_no"));
			agentWinningMap.put("seat_type", agentOrderList.get(num).get("seat_type"));
			userInfoService.addAgentwinning(agentWinningMap);
			logger.info("插入银牌中奖数据成功，中奖代理商id为："+agentOrderList.get(num).get("dealer_id")+" 中奖类型为：0"+"  订单金额为："+agentOrderList.get(num).get("ticket_pay_money"));
		}else{
			logger.info("80到100元没有订单，50到79也没有订单！");
		}
		
	}
}



class AlComparator implements Comparator<Object> {   
	public int compare(Object o1, Object o2) {   
		int seq1 = 0;   
		int seq2 = 0;   
     	try {   
           seq1 = (int) Double.parseDouble(((Object)o1).toString());   
           seq2 = (int) Double.parseDouble(((Object)o2).toString());   
    	} catch (Exception e) {
    		e.printStackTrace();
    	}   
      return seq1 - seq2;   
   }   
}   

