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

import com.l9e.transaction.service.OrderInfoToOrder_provinceService;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.JoinUsVo;
import com.l9e.util.StringUtil;
/**
 * 定点查询前一日省市出售情况
 * @author liht
 *
 */
@Component("orderInfoToOrder_province_job")
public class OrderInfoToOrder_province {
	private static final Logger logger = Logger.getLogger(OrderInfoToOrder_province.class);
	@Resource
	OrderInfoToOrder_provinceService orderInfoToOrder_provinceService;
	
	public void queryToInsertOrderProvince(){
		int count = orderInfoToOrder_provinceService.queryHc_statInfo_provinceCount();
		if(count==0){
			logger.info("【定时任务：queryToInsertOrderProvince】count="+count+"【第一次执行...】");
			List<Map<String,Object>>time_list = orderInfoToOrder_provinceService.queryDate();
			String create_time = null ;
			for(int k=0;k<time_list.size()-1;k++){
				create_time=time_list.get(k).get("create_time").toString();
				
				Map<String,Object>paramMap = new HashMap<String,Object>();
				paramMap.put("create_time", create_time);
				paramMap.put("order_status", BookVo.OUT_SUCCESS);
			
				List<Map<String,Object>>provinceCount = orderInfoToOrder_provinceService.queryProvinceCount(paramMap);
				
				List<Map<String,Object>>provinceCountList = new ArrayList<Map<String,Object>>();//创建新的容器 
				double bx_Money_Sum,succeed_money,defeated_money;
				String area_no ;
				for(int i=0;i<provinceCount.size();i++){
					long ticket ,activeAgent,bx_count,order_count=0,succeed_count=0,defeated_count=0,want_outTicket=0,pay_fall=0,mouth_count=0,last_mouth_count=0 ; 
					int joinCount=0,through=0,not_through=0,wait_through=0;
					String today_money = null;
					Map<String,Object> hashMap = provinceCount.get(i);
					Map<String,String> query_map = new HashMap<String,String>();
					Map<String, Object> map_t_m = new HashMap<String, Object>();//用于存放当日的总票数t和总金额m的map
					List<String> estateCount = new ArrayList<String>();
					query_map.put("create_time", create_time);
					query_map.put("provinceName", hashMap.get("provinceName").toString());
					area_no = hashMap.get("area_no").toString();
					query_map.put("order_status", BookVo.OUT_SUCCESS);
					query_map.put("area_no", area_no);
					map_t_m = (Map<String, Object>) orderInfoToOrder_provinceService.queryProvinceTicket(query_map);//查询各省出售的票数
					ticket = (Long) map_t_m.get("ticket");//查询当日总票数
					today_money = map_t_m.get("money").toString();//查询当日总金额  
					activeAgent = orderInfoToOrder_provinceService.queryProvinceActiveAgent(query_map);//查询各省的活跃用户
					bx_count = orderInfoToOrder_provinceService.query_Province_Bx_count(query_map);//查询本省当天销售保险数
					bx_Money_Sum = orderInfoToOrder_provinceService.query_Province_Bx_Money_Sum(query_map);//查询本省当天销售保险的价钱
					List<String>orderList = orderInfoToOrder_provinceService.query_Province_Succeed_count(query_map);//总个数
					for(int z=0 ;z<orderList.size();z++){
						order_count++;
						if(StringUtil.isNotEmpty(orderList.get(z).toString())&& orderList.get(z).toString().equals(BookVo.OUT_SUCCESS)){
							succeed_count++;
						}
						if(StringUtil.isNotEmpty(orderList.get(z).toString())&& orderList.get(z).toString().equals(BookVo.OUT_TICKET_FAIL)){
							defeated_count++;
						}
						if(StringUtil.isNotEmpty(orderList.get(z).toString())&& orderList.get(z).toString().equals(BookVo.PRE_ORDER)){
							want_outTicket++;
						}
						if(StringUtil.isNotEmpty(orderList.get(z).toString())&& orderList.get(z).toString().equals(BookVo.PAY_FAIL)){
							pay_fall++;
						}
					}
					hashMap.put("order_count", order_count);
					hashMap.put("succeed_count", succeed_count);
					hashMap.put("defeated_count", defeated_count);
					hashMap.put("want_outTicket", want_outTicket);
					hashMap.put("pay_fall", pay_fall);
					succeed_money=orderInfoToOrder_provinceService.query_Province_Succeed_money(query_map);//成功价钱
					defeated_money = orderInfoToOrder_provinceService.query_Province_defeated_money(query_map);//失败价钱
					estateCount = orderInfoToOrder_provinceService.queryEstateCount(area_no);
					mouth_count = orderInfoToOrder_provinceService.query_Province_Mount_count(query_map);
					last_mouth_count = orderInfoToOrder_provinceService.query_Province_Last_mount_count(query_map);
					System.out.println("~~~~~~~~~~~~~~~~mouth_count"+mouth_count+"last_mouth_count"+last_mouth_count);
					
					for(int j=0;j<estateCount.size();j++){
						joinCount++;
						if(estateCount.get(j).equals(JoinUsVo.PASS)){
							through++;
						}
						if(estateCount.get(j).equals(JoinUsVo.DOESNOT)){
							not_through++;
						}
						if(estateCount.get(j).equals(JoinUsVo.WAIT)){
							wait_through++;
						}
						joinCount -= joinCount;
					}
					hashMap.put("bx_count", bx_count);
					hashMap.put("bx_Money_Sum", bx_Money_Sum);
					hashMap.put("succeed_money", succeed_money);
					hashMap.put("defeated_money", defeated_money);
					hashMap.put("mouth_count", mouth_count);//本月订单数
					hashMap.put("last_mouth_count", last_mouth_count);//上月订单数
					hashMap.put("province_id", area_no);
					hashMap.put("create_time", create_time);
					hashMap.put("ticket", ticket);//将当日总票数存入map
					hashMap.put("today_money", Double.parseDouble(today_money));//将当日总金额存入map
					hashMap.put("activeAgent", activeAgent);
					hashMap.put("joinCount", joinCount);//本省申请加盟的总数
					hashMap.put("through", through);//通过数
					hashMap.put("not_through", not_through);//未通过数
					hashMap.put("wait_through", wait_through);//等待审核数
					provinceCountList.add(hashMap);//将map存入List
				}
				for(Map<String,Object> insert_Map : provinceCountList){
					orderInfoToOrder_provinceService.addOrder_province(insert_Map);//把数据添加到hc_statInfo_province表中
				}
			}
		}else{
			logger.info("【定时任务：queryToInsertOrderProvince】count="+count);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);    //得到前一天
			Date date = calendar.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String create_time = df.format(date);//前一天时间
			//String create_time= "2013-08-13";
			Map<String,Object>paramMap = new HashMap<String,Object>();
			paramMap.put("create_time", create_time);
			paramMap.put("order_status", BookVo.OUT_SUCCESS);
		
			List<Map<String,Object>>provinceCount = orderInfoToOrder_provinceService.queryProvinceCount(paramMap);
			
			List<Map<String,Object>>provinceCountList = new ArrayList<Map<String,Object>>();//创建新的容器 
			double bx_Money_Sum,succeed_money,defeated_money;
			String area_no ;
			for(int i=0;i<provinceCount.size();i++){
				long ticket ,activeAgent,bx_count,order_count=0,succeed_count=0,defeated_count=0,want_outTicket=0,pay_fall=0,mouth_count=0,last_mouth_count=0 ; 
				int joinCount=0,through=0,not_through=0,wait_through=0;
				String today_money = null;
				Map<String,Object> hashMap = provinceCount.get(i);
				Map<String,String> query_map = new HashMap<String,String>();
				Map<String, Object> map_t_m = new HashMap<String, Object>();//用于存放当日的总票数和总金额的map
				List<String> estateCount = new ArrayList<String>();
				query_map.put("create_time", create_time);
				query_map.put("provinceName", hashMap.get("provinceName").toString());
				query_map.put("order_status", BookVo.OUT_SUCCESS);
				area_no = hashMap.get("area_no").toString();
				query_map.put("area_no", area_no);
				map_t_m = (Map<String, Object>) orderInfoToOrder_provinceService.queryProvinceTicket(query_map);//查询各省出售的票数
				ticket = (Long) map_t_m.get("ticket");//查询当日总票数
				today_money = map_t_m.get("money").toString();//查询当日总金额
				
				activeAgent = orderInfoToOrder_provinceService.queryProvinceActiveAgent(query_map);//查询各省的活跃用户
				bx_count = orderInfoToOrder_provinceService.query_Province_Bx_count(query_map);//查询本省当天销售保险数
				bx_Money_Sum = orderInfoToOrder_provinceService.query_Province_Bx_Money_Sum(query_map);//查询本省当天销售保险的价钱
				List<String>orderList = orderInfoToOrder_provinceService.query_Province_Succeed_count(query_map);//总个数
				for(int z=0 ;z<orderList.size();z++){
					order_count++;
					if(StringUtil.isNotEmpty(orderList.get(z).toString())&& orderList.get(z).toString().equals(BookVo.OUT_SUCCESS)){
						succeed_count++;
					}
					if(StringUtil.isNotEmpty(orderList.get(z).toString())&& orderList.get(z).toString().equals(BookVo.OUT_TICKET_FAIL)){
						defeated_count++;
					}
					if(StringUtil.isNotEmpty(orderList.get(z).toString())&& orderList.get(z).toString().equals(BookVo.PRE_ORDER)){
						want_outTicket++;
					}
					if(StringUtil.isNotEmpty(orderList.get(z).toString())&& orderList.get(z).toString().equals(BookVo.PAY_FAIL)){
						pay_fall++;
					}
				}
				hashMap.put("order_count", order_count);
				hashMap.put("succeed_count", succeed_count);
				hashMap.put("defeated_count", defeated_count);
				hashMap.put("want_outTicket", want_outTicket);
				hashMap.put("pay_fall", pay_fall);
			
				
				succeed_money=orderInfoToOrder_provinceService.query_Province_Succeed_money(query_map);//成功价钱
				defeated_money = orderInfoToOrder_provinceService.query_Province_defeated_money(query_map);//失败价钱
				estateCount = orderInfoToOrder_provinceService.queryEstateCount(area_no);
				mouth_count = orderInfoToOrder_provinceService.query_Province_Mount_count(query_map);
				last_mouth_count = orderInfoToOrder_provinceService.query_Province_Last_mount_count(query_map);
				for(int j=0;j<estateCount.size();j++){
					joinCount++;
					if(estateCount.get(j).equals(JoinUsVo.PASS)){ //PASS="33" ;//审核通过
						through++;
					}
					if(estateCount.get(j).equals(JoinUsVo.DOESNOT)){//DOESNOT="22" ;//审核未通过
						not_through++;
					}
					if(estateCount.get(j).equals(JoinUsVo.WAIT)){ //WAIT="11" ; //等待审核
						wait_through++;
					}
					joinCount-=joinCount;
				}
				hashMap.put("bx_count", bx_count);
				hashMap.put("bx_Money_Sum", bx_Money_Sum);
				hashMap.put("mouth_count", mouth_count);//本月订单数
				hashMap.put("last_mouth_count", last_mouth_count);//上月订单数
				hashMap.put("succeed_money", succeed_money);
				hashMap.put("defeated_money", defeated_money);
				hashMap.put("province_id", area_no);
				hashMap.put("create_time", create_time);
				hashMap.put("ticket", ticket);//将当日总票数存入map
				hashMap.put("today_money", Double.parseDouble(today_money));//将当日总金额存入map
				hashMap.put("activeAgent", activeAgent);
				hashMap.put("joinCount", joinCount);//本省申请加盟的总数
				hashMap.put("through", through);//通过数
				hashMap.put("not_through", not_through);//未通过数
				hashMap.put("wait_through", wait_through);//等待审核数
				provinceCountList.add(hashMap);//将map存入List
			}
			for(Map<String,Object> insert_Map : provinceCountList){
				orderInfoToOrder_provinceService.addOrder_province(insert_Map);
			}
		}
	}
}
