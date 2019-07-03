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
 * 处理代理商金牌中奖job
 * @author yangchao
 *
 */

@Component("goldAgentWinningJob")
public class GoldAgentWinningJob {

	private static final Logger logger=Logger.getLogger(GoldAgentWinningJob.class);
	
	@Resource
	private UserInfoService userInfoService;
	
	public void getGoldAgentWinningInfo(){
		logger.info("开始抽取金牌代理商的中奖信息！");
		List<Map<String,Object>> passNumAgentIdList=userInfoService.queryAgentPassNumAndId();
		if(passNumAgentIdList!=null && passNumAgentIdList.size()>0){
			this.goldWinningInfo(passNumAgentIdList);
		}
	}
	
	public void goldWinningInfo(List<Map<String,Object>> passNumAgentIdList){
		logger.info("开始抽取金牌代理商的中奖信息！");
		List<String> goldAgentList=new ArrayList<String>();
		for(int i=0;i<passNumAgentIdList.size();i++){
			Long num=(Long) passNumAgentIdList.get(i).get("passNum");
			if(num>=5){
				goldAgentList.add((String) passNumAgentIdList.get(i).get("user_id"));
			}
		}
		logger.info("金牌代理商数量："+goldAgentList);
		int moneyStart=0;
		int moneyEnd=0;
		//这里循环中的i代表的是中奖的类型，i=0表示第一次抽奖，i=1表示第二次抽奖，i=2表示第三次抽奖
		for(int i=0;i<3;i++){
			Map<String,Object> goldMap=new HashMap<String,Object>();
			//选择不同的价格区间
			if(i==0){
				moneyStart=50;
				moneyEnd=99;
			}else if(i==1){
				moneyStart=90;
				moneyEnd=119;
			}else {
				moneyStart=120;
				moneyEnd=150;
			}
			goldMap.put("moneyStart", moneyStart);
			goldMap.put("moneyEnd", moneyEnd);
			goldMap.put("dealer_id",goldAgentList);
			List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
			if(goldAgentList!=null && goldAgentList.size()>0){
				//根据金牌代理商的id查询他们在七天之内、每个价格区间内的订单
				agentOrderList=userInfoService.queryAgentOrderInfo(goldMap);
				logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的金牌代理商订单数量为："+agentOrderList.size());
			}else{
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("extend", "0");
				map.put("opt_person", "goldJob");
				userInfoService.addAgentwinning(map);
				logger.info("金牌代理商人数为0，无法抽奖！已添加不能抽奖时的数据！");
				return;
			}
			if(agentOrderList!=null && agentOrderList.size()>0){
				Map<String,Object> agentWinningMap=new HashMap<String,Object>();
				//标识抽奖次数
				int num=0;
				//用来标识是否已经中奖
				boolean check=true;
				//如果抽50次都抽到同一个代理商！
				while(num<50 && check==true){
					num=num+1;
					int orderNum=agentOrderList.size();
					int winningNum=(int) (orderNum*Math.random());
					logger.info("得到的随机数为："+winningNum);
					//先验证该代理商今天是否已经中奖--->验证代理商是否中过奖
					agentWinningMap.put("agent_id", agentOrderList.get(winningNum).get("dealer_id"));
					agentWinningMap.put("agent_type","0");
					List<Map<String,Object>> list=userInfoService.queryAgentWinningDay(agentWinningMap);
					if(list==null || list.size()==0 ){
						agentWinningMap.put("dealer_name", agentOrderList.get(winningNum).get("dealer_name"));
						agentWinningMap.put("order_id", agentOrderList.get(winningNum).get("order_id"));
						agentWinningMap.put("winning_money", agentOrderList.get(winningNum).get("ticket_pay_money"));
						agentWinningMap.put("order_time",agentOrderList.get(winningNum).get("out_ticket_time"));
						agentWinningMap.put("opt_person","goldJob");
						agentWinningMap.put("winning_type",i+"");
						agentWinningMap.put("from_city", agentOrderList.get(winningNum).get("from_city"));
						agentWinningMap.put("to_city", agentOrderList.get(winningNum).get("to_city"));
						agentWinningMap.put("train_no", agentOrderList.get(winningNum).get("train_no"));
						agentWinningMap.put("seat_type", agentOrderList.get(winningNum).get("seat_type"));
						userInfoService.addAgentwinning(agentWinningMap);
						
						check=false;
						logger.info("插入金牌中奖数据成功，中奖代理商id为："+agentOrderList.get(winningNum).get("dealer_id")+" 中奖类型为："+i+"  订单金额为："+agentOrderList.get(winningNum).get("ticket_pay_money"));
					}else{
						if(num!=50){
							logger.info("该用户已经中过奖，开始重新抽奖！");
						}else{
							//抽奖50次都抽到同一代理商！
							agentWinningMap.put("dealer_name", agentOrderList.get(winningNum).get("dealer_name"));
							agentWinningMap.put("order_id", agentOrderList.get(winningNum).get("order_id"));
							agentWinningMap.put("winning_money", agentOrderList.get(winningNum).get("ticket_pay_money"));
							agentWinningMap.put("order_time",agentOrderList.get(winningNum).get("out_ticket_time"));
							agentWinningMap.put("opt_person","goldJob");
							agentWinningMap.put("winning_type",i+"");
							agentWinningMap.put("from_city", agentOrderList.get(winningNum).get("from_city"));
							agentWinningMap.put("to_city", agentOrderList.get(winningNum).get("to_city"));
							agentWinningMap.put("train_no", agentOrderList.get(winningNum).get("train_no"));
							agentWinningMap.put("seat_type", agentOrderList.get(winningNum).get("seat_type"));
							userInfoService.addAgentwinning(agentWinningMap);
							logger.info("抽奖50次都抽到同一用户，插入金牌中奖数据成功，中奖代理商id为："+agentOrderList.get(winningNum).get("dealer_id")+" 中奖类型为："+i+"  订单金额为："+agentOrderList.get(winningNum).get("ticket_pay_money"));
						}
					}
				}
				
			}else{
				logger.info("金牌代理商在七天内，价格区间为 "+moneyStart+"元和"+moneyEnd+"元之间没有订单！");
				if(moneyEnd==99){
					logger.info("金牌代理商在价格区间50到99元不存在订单！准备抽取90到119之间的订单，取订单金额最小的作为中奖订单！");
					moneyStart=90;
					moneyEnd=119;
					this.goldWinning1(moneyStart, moneyEnd, goldAgentList);
				}else if(moneyStart==90 && moneyEnd==119){
					logger.info("金牌代理商在价格区间90到119元不存在订单！准备抽取0到100之间的订单，取订单金额最大的作为中奖订单！");
					moneyStart=50;
					moneyEnd=99;
					this.goldWinning2(moneyStart, moneyEnd, goldAgentList);
				}else if(moneyStart==120 && moneyEnd==150){
					logger.info("金牌代理商在价格区间120到150元不存在订单！准备抽取0到100之间的订单，取订单金额最大的作为中奖订单！");
					moneyStart=90;
					moneyEnd=119;
					this.goldWinning3(moneyStart, moneyEnd, goldAgentList);
				}else{
					logger.info("抽奖出错！");
				}
			}
		}
	}
	
	public void goldWinning1(int moneyStart,int moneyEnd,List<String> goldAgentList){
		logger.info("开始处理0到50元没有订单的情况！");
		Map<String,Object> goldMap=new HashMap<String,Object>();
		goldMap.put("moneyStart", moneyStart);
		goldMap.put("moneyEnd", moneyEnd);
		goldMap.put("dealer_id",goldAgentList);
		List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
		agentOrderList=userInfoService.queryAgentOrderInfo(goldMap);
		logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的金牌代理商订单数量为："+agentOrderList.size());
		if(agentOrderList!=null && agentOrderList.size()>0){
			List<Object> moneyList=new ArrayList<Object>();
			for(int i=0;i<agentOrderList.size();i++){
				moneyList.add(agentOrderList.get(i).get("ticket_pay_money"));
			}
			//对价钱进行排序
			Collections.sort(moneyList, new ValComparator());
			String winning_money=((Object)moneyList.get(0)).toString().trim();
			logger.info("价钱"+winning_money);
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
			agentWinningMap.put("opt_person","goldJob");
			agentWinningMap.put("agent_type","0");
			agentWinningMap.put("winning_type","0");
			agentWinningMap.put("from_city", agentOrderList.get(num).get("from_city"));
			agentWinningMap.put("to_city", agentOrderList.get(num).get("to_city"));
			agentWinningMap.put("train_no", agentOrderList.get(num).get("train_no"));
			agentWinningMap.put("seat_type", agentOrderList.get(num).get("seat_type"));

			userInfoService.addAgentwinning(agentWinningMap);
			logger.info("插入金牌中奖数据成功，中奖代理商id为："+agentOrderList.get(num).get("dealer_id")+" 中奖类型为：0"+"  订单金额为："+agentOrderList.get(num).get("ticket_pay_money"));
		}else{
			logger.info("50到99元没有订单，90到120也没有订单！");
		}
		
	}
	
	public void goldWinning2(int moneyStart,int moneyEnd,List<String> goldAgentList){
		logger.info("开始处理90到119元没有订单的情况！");
		Map<String,Object> goldMap=new HashMap<String,Object>();
		goldMap.put("moneyStart", moneyStart);
		goldMap.put("moneyEnd", moneyEnd);
		goldMap.put("dealer_id",goldAgentList);
		List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
		agentOrderList=userInfoService.queryAgentOrderInfo(goldMap);
		logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的金牌代理商订单数量为："+agentOrderList.size());
		if(agentOrderList!=null && agentOrderList.size()>0){
			List<Object> moneyList=new ArrayList<Object>();
			for(int i=0;i<agentOrderList.size();i++){
				moneyList.add(agentOrderList.get(i).get("ticket_pay_money"));
			}
			//对价钱进行排序
			Collections.sort(moneyList, new ValComparator());
			//取0到100价钱中最大的价钱
			String winning_money=((Object) moneyList.get(moneyList.size()-1)).toString().trim();
			logger.info("价钱"+winning_money);
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
			agentWinningMap.put("opt_person","goldJob");
			agentWinningMap.put("agent_type","0");
			agentWinningMap.put("winning_type","1");
			agentWinningMap.put("from_city", agentOrderList.get(num).get("from_city"));
			agentWinningMap.put("to_city", agentOrderList.get(num).get("to_city"));
			agentWinningMap.put("train_no", agentOrderList.get(num).get("train_no"));
			agentWinningMap.put("seat_type", agentOrderList.get(num).get("seat_type"));
			userInfoService.addAgentwinning(agentWinningMap);
			logger.info("插入金牌中奖数据成功，中奖代理商id为："+agentOrderList.get(num).get("dealer_id")+" 中奖类型为：0"+"  订单金额为："+agentOrderList.get(num).get("ticket_pay_money"));
		}else{
			logger.info("90到119元没有订单，50到99也没有订单！");
		}
		
	}
	
	public void goldWinning3(int moneyStart,int moneyEnd,List<String> goldAgentList){
		logger.info("开始处理120到150元没有订单的情况！");
		Map<String,Object> goldMap=new HashMap<String,Object>();
		goldMap.put("moneyStart", moneyStart);
		goldMap.put("moneyEnd", moneyEnd);
		goldMap.put("dealer_id",goldAgentList);
		List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
		agentOrderList=userInfoService.queryAgentOrderInfo(goldMap);
		logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的金牌代理商订单数量为："+agentOrderList.size());
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
			agentWinningMap.put("opt_person","goldJob");
			agentWinningMap.put("agent_type","0");
			agentWinningMap.put("winning_type","2");
			agentWinningMap.put("from_city", agentOrderList.get(num).get("from_city"));
			agentWinningMap.put("to_city", agentOrderList.get(num).get("to_city"));
			agentWinningMap.put("train_no", agentOrderList.get(num).get("train_no"));
			agentWinningMap.put("seat_type", agentOrderList.get(num).get("seat_type"));
			userInfoService.addAgentwinning(agentWinningMap);
			logger.info("插入金牌中奖数据成功，中奖代理商id为："+agentOrderList.get(num).get("dealer_id")+" 中奖类型为：0"+"  订单金额为："+agentOrderList.get(num).get("ticket_pay_money"));
		}else{
			logger.info("120到150元没有订单，99到119也没有订单！");
		}
		
	}
}



class ValComparator implements Comparator<Object> {   
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

