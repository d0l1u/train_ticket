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
 * 每周三从VIP和SVIP订单中抽奖免单。
 * 规则为：0-49元抽2笔(VIP和SVIP)；50-109元抽2笔(VIP和SVIP)；100-150元抽1笔(VIP或SVIP)。
 * 抽取后将右侧“中奖展示”之前的内容去掉，仅显示此活动的中奖信息
 * 1、每周三抽取【上周一--上周日】的vip和svip订单
 * 2、以前中过奖的代理商不可再中奖
 * @author guona
 *
 */
@Component("svipAgentWinningJob")
public class SvipAgentWinningJob {

	private static final Logger logger=Logger.getLogger(SvipAgentWinningJob.class);
	
	@Resource
	private UserInfoService userInfoService;
	
	public void getSvipAgentWinningInfo(){
		logger.info("【开始】抽取上周一--上周日的SVIP和VIP订单的中奖信息！");
		List<Map<String,String>> passNumAgentIdList=userInfoService.queryAgentLevelAndId();
		if(passNumAgentIdList!=null && passNumAgentIdList.size()>0){
			this.goldWinningInfo(passNumAgentIdList);
		}
	}
	
	public void goldWinningInfo(List<Map<String,String>> passNumAgentIdList){
		logger.info("开始从上周一--上周日的SVIP和VIP订单中抽取中奖信息！");
		logger.info("vip和svip订单的数量："+passNumAgentIdList);
//		List<String> goldAgentList=new ArrayList<String>();
//		for(int i=0;i<passNumAgentIdList.size();i++){
//			Long num=(Long) passNumAgentIdList.get(i).get("passNum");
//			if(num>=5){
//				goldAgentList.add((String) passNumAgentIdList.get(i).get("user_id"));
//			}
//		}
//		logger.info("金牌代理商数量："+goldAgentList);
		int moneyStart=0;
		int moneyEnd=0;
		//这里循环中的i代表的是中奖的类型，i=0表示第一次抽奖，i=1表示第二次抽奖，i=2表示第三次抽奖
		//0-49元抽2笔(VIP和SVIP)；50-109元抽2笔(VIP和SVIP)；100-150元抽1笔(VIP或SVIP)
		for(int i=0;i<5;i++){
			Map<String,Object> goldMap=new HashMap<String,Object>();
			//选择不同的价格区间
			if(i==0 || i==1){
				moneyStart=0;
				moneyEnd=49;
			}else if(i==2 || i==3){
				moneyStart=50;
				moneyEnd=109;
			}else {
				moneyStart=100;
				moneyEnd=150;
			}
			goldMap.put("moneyStart", moneyStart);
			goldMap.put("moneyEnd", moneyEnd);
//			goldMap.put("dealer_id",goldAgentList);
			List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
			if(passNumAgentIdList!=null && passNumAgentIdList.size()>0){
				//查询他们在【上周一--上周日】每个价格区间内的订单
				agentOrderList=userInfoService.queryMayBeWinOrderInfo(goldMap);
				logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的vip、svip订单数量为："+agentOrderList.size());
			}else{
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("extend", "2");//2、vip抽奖时订单数为0
				map.put("opt_person", "vipJob");
				userInfoService.addAgentwinning(map);
				logger.info("vip、svip订单数量为0，无法抽奖！已添加不能抽奖时的数据！");
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
					List<Map<String,Object>> list=userInfoService.queryAgentWinningDay(agentWinningMap);
					if(list==null || list.size()==0 ){//该代理商没有中过奖
						agentWinningMap.put("dealer_name", agentOrderList.get(winningNum).get("dealer_name"));
						agentWinningMap.put("order_id", agentOrderList.get(winningNum).get("order_id"));
						agentWinningMap.put("winning_money", agentOrderList.get(winningNum).get("ticket_pay_money"));
						agentWinningMap.put("order_time",agentOrderList.get(winningNum).get("out_ticket_time"));
						if(agentOrderList.get(winningNum).get("order_level").equals("vip")){
							agentWinningMap.put("agent_type","2");//vip
						}else{
							agentWinningMap.put("agent_type","3");//svip
						}
						agentWinningMap.put("opt_person","vipJob");
						if(i==0 || i==1){
							agentWinningMap.put("winning_type","0");
						}else if(i==2|| i==3){
							agentWinningMap.put("winning_type","1");
						}else{
							agentWinningMap.put("winning_type","2");
						}
						agentWinningMap.put("from_city", agentOrderList.get(winningNum).get("from_city"));
						agentWinningMap.put("to_city", agentOrderList.get(winningNum).get("to_city"));
						agentWinningMap.put("train_no", agentOrderList.get(winningNum).get("train_no"));
						agentWinningMap.put("seat_type", agentOrderList.get(winningNum).get("seat_type"));
						userInfoService.addAgentwinning(agentWinningMap);
						
						check=false;
						logger.info("插入vip、svip中奖数据成功，中奖代理商id为："+agentOrderList.get(winningNum).get("dealer_id")+" 中奖类型为："+i+"  订单金额为："+agentOrderList.get(winningNum).get("ticket_pay_money"));
					}else{
						if(num!=50){
							logger.info("该用户已经中过奖，开始重新抽奖！");
						}else{
							//抽奖50次都抽到同一代理商！
							agentWinningMap.put("dealer_name", agentOrderList.get(winningNum).get("dealer_name"));
							agentWinningMap.put("order_id", agentOrderList.get(winningNum).get("order_id"));
							agentWinningMap.put("winning_money", agentOrderList.get(winningNum).get("ticket_pay_money"));
							agentWinningMap.put("order_time",agentOrderList.get(winningNum).get("out_ticket_time"));
							agentWinningMap.put("opt_person","vipJob");
							if(i==0 || i==1){
								agentWinningMap.put("winning_type","0");
							}else if(i==2|| i==3){
								agentWinningMap.put("winning_type","1");
							}else{
								agentWinningMap.put("winning_type","2");
							}
							if(agentOrderList.get(winningNum).get("order_level").equals("vip")){
								agentWinningMap.put("agent_type","2");//vip
							}else{
								agentWinningMap.put("agent_type","3");//svip
							}
							agentWinningMap.put("from_city", agentOrderList.get(winningNum).get("from_city"));
							agentWinningMap.put("to_city", agentOrderList.get(winningNum).get("to_city"));
							agentWinningMap.put("train_no", agentOrderList.get(winningNum).get("train_no"));
							agentWinningMap.put("seat_type", agentOrderList.get(winningNum).get("seat_type"));
							userInfoService.addAgentwinning(agentWinningMap);
							logger.info("抽奖50次都抽到同一用户，插入vip、svip中奖数据成功，中奖代理商id为："+agentOrderList.get(winningNum).get("dealer_id")+" 中奖类型为："+i+"  订单金额为："+agentOrderList.get(winningNum).get("ticket_pay_money"));
						}
					}
				}
				
			}else{
				//0-49元抽2笔(0 1)；50-109元抽2笔(2 3)；100-150元抽1笔(4)
				logger.info("vip在七天内，价格区间为 "+moneyStart+"元和"+moneyEnd+"元之间没有订单！");
				if(moneyEnd==49){
					logger.info("vip在价格区间0到49元不存在订单！准备抽取50到109之间的订单，取订单金额最小的作为中奖订单！");
					moneyStart=50;
					moneyEnd=109;
					this.goldWinning1(moneyStart, moneyEnd);
				}else if(moneyStart==50 && moneyEnd==109){
					logger.info("vip在价格区间50到109元不存在订单！准备抽取0到109之间的订单，取订单金额最大的作为中奖订单！");
					moneyStart=0;
					moneyEnd=49;
					this.goldWinning2(moneyStart, moneyEnd);
				}else if(moneyStart==100 && moneyEnd==150){
					logger.info("vip在价格区间100到150元不存在订单！准备抽取0到100之间的订单，取订单金额最大的作为中奖订单！");
					moneyStart=50;
					moneyEnd=109;
//					this.goldWinning3(moneyStart, moneyEnd, goldAgentList);
					this.goldWinning3(moneyStart, moneyEnd);
				}else{
					logger.info("抽奖出错！");
				}
			}
		}
		logger.info("抽取上周一--上周日的SVIP和VIP订单的中奖信息【结束】！");
	}
	
	//vip在价格区间0到49元不存在订单！准备抽取50到109之间的订单，取订单金额最小的作为中奖订单！
	public void goldWinning1(int moneyStart,int moneyEnd){
		logger.info("开始处理0到49元没有订单的情况！");
		Map<String,Object> goldMap=new HashMap<String,Object>();
		goldMap.put("moneyStart", moneyStart);
		goldMap.put("moneyEnd", moneyEnd);
//		goldMap.put("dealer_id",goldAgentList);
		List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
		agentOrderList=userInfoService.queryMayBeWinOrderInfo(goldMap);//查询符合条件的可能中奖的订单信息
		logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的vip、svip订单数量为："+agentOrderList.size());
		if(agentOrderList!=null && agentOrderList.size()>0){
			List<Object> moneyList=new ArrayList<Object>();
			for(int i=0;i<agentOrderList.size();i++){
				moneyList.add(agentOrderList.get(i).get("ticket_pay_money"));
			}
			//对价钱进行排序
			Collections.sort(moneyList, new SvipValComparator());
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
			agentWinningMap.put("opt_person","vipJob");
			if(agentOrderList.get(num).get("order_level").equals("vip")){
				agentWinningMap.put("agent_type","2");//vip
			}else{
				agentWinningMap.put("agent_type","3");//svip
			}
			agentWinningMap.put("winning_type","0");
			agentWinningMap.put("from_city", agentOrderList.get(num).get("from_city"));
			agentWinningMap.put("to_city", agentOrderList.get(num).get("to_city"));
			agentWinningMap.put("train_no", agentOrderList.get(num).get("train_no"));
			agentWinningMap.put("seat_type", agentOrderList.get(num).get("seat_type"));

			userInfoService.addAgentwinning(agentWinningMap);
			logger.info("插入vip、svip中奖数据成功，中奖代理商id为："+agentOrderList.get(num).get("dealer_id")+" 中奖类型为：0"+"  订单金额为："+agentOrderList.get(num).get("ticket_pay_money"));
		}else{
			logger.info("0到49元没有订单，50到109也没有订单！");
		}
		
	}
	
	//vip在价格区间50到109元不存在订单！准备抽取0到109之间的订单，取订单金额最大的作为中奖订单！
	public void goldWinning2(int moneyStart,int moneyEnd){
		logger.info("开始处理50到109元没有订单的情况！");//查询符合条件的可能中奖的订单信息
		Map<String,Object> goldMap=new HashMap<String,Object>();
		goldMap.put("moneyStart", moneyStart);
		goldMap.put("moneyEnd", moneyEnd);
//		goldMap.put("dealer_id",goldAgentList);
		List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
		agentOrderList=userInfoService.queryMayBeWinOrderInfo(goldMap);
		logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的vip、svip订单数量为："+agentOrderList.size());
		if(agentOrderList!=null && agentOrderList.size()>0){
			List<Object> moneyList=new ArrayList<Object>();
			for(int i=0;i<agentOrderList.size();i++){
				moneyList.add(agentOrderList.get(i).get("ticket_pay_money"));
			}
			//对价钱进行排序
			Collections.sort(moneyList, new SvipValComparator());
			//取0到109价钱中最大的价钱
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
			agentWinningMap.put("opt_person","vipJob");
			if(agentOrderList.get(num).get("order_level").equals("vip")){
				agentWinningMap.put("agent_type","2");//vip
			}else{
				agentWinningMap.put("agent_type","3");//svip
			}
			agentWinningMap.put("winning_type","1");
			agentWinningMap.put("from_city", agentOrderList.get(num).get("from_city"));
			agentWinningMap.put("to_city", agentOrderList.get(num).get("to_city"));
			agentWinningMap.put("train_no", agentOrderList.get(num).get("train_no"));
			agentWinningMap.put("seat_type", agentOrderList.get(num).get("seat_type"));
			userInfoService.addAgentwinning(agentWinningMap);
			logger.info("插入vip、svip中奖数据成功，中奖代理商id为："+agentOrderList.get(num).get("dealer_id")+" 中奖类型为：0"+"  订单金额为："+agentOrderList.get(num).get("ticket_pay_money"));
		}else{
			logger.info("50到109元没有订单，0到109也没有订单！");
		}
		
	}
	
	//vip在价格区间100到150元不存在订单！准备抽取0到100之间的订单，取订单金额最大的作为中奖订单！
	public void goldWinning3(int moneyStart,int moneyEnd){
		logger.info("开始处理100到150元没有订单的情况！");
		Map<String,Object> goldMap=new HashMap<String,Object>();
		goldMap.put("moneyStart", moneyStart);
		goldMap.put("moneyEnd", moneyEnd);
//		goldMap.put("dealer_id",goldAgentList);
		List<Map<String,Object>> agentOrderList=new ArrayList<Map<String,Object>>();
		agentOrderList=userInfoService.queryMayBeWinOrderInfo(goldMap);//查询符合条件的可能中奖的订单信息
		logger.info("在价格区间 "+moneyStart+"元与 "+moneyEnd+"元之间的vip、svip订单数量为："+agentOrderList.size());
		if(agentOrderList!=null && agentOrderList.size()>0){
			List<Object> moneyList=new ArrayList<Object>();
			for(int i=0;i<agentOrderList.size();i++){
				moneyList.add(agentOrderList.get(i).get("ticket_pay_money"));
			}
			//对价钱进行排序
			Collections.sort(moneyList, new SvipValComparator());
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
			agentWinningMap.put("opt_person","vipJob");
			if(agentOrderList.get(num).get("order_level").equals("vip")){
				agentWinningMap.put("agent_type","2");//vip
			}else{
				agentWinningMap.put("agent_type","3");//svip
			}
			agentWinningMap.put("winning_type","2");
			agentWinningMap.put("from_city", agentOrderList.get(num).get("from_city"));
			agentWinningMap.put("to_city", agentOrderList.get(num).get("to_city"));
			agentWinningMap.put("train_no", agentOrderList.get(num).get("train_no"));
			agentWinningMap.put("seat_type", agentOrderList.get(num).get("seat_type"));
			userInfoService.addAgentwinning(agentWinningMap);
			logger.info("插入vip、svip中奖数据成功，中奖代理商id为："+agentOrderList.get(num).get("dealer_id")+" 中奖类型为：0"+"  订单金额为："+agentOrderList.get(num).get("ticket_pay_money"));
		}else{
			logger.info("100到150元没有订单，0到100也没有订单！");
		}
		
	}
}

class SvipValComparator implements Comparator<Object> {   
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