package com.l9e.train.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.OuterSoukdNewData;
import com.l9e.train.po.TrainNewData;
import com.l9e.train.po.WaitAlterEntity;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.AmountUtil;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;



public class QueryTrainBase { 
	
	private static Logger logger = LoggerFactory.getLogger(QueryTrainBase.class);	
	Pattern pattern = Pattern.compile("^[0-9].*");
	TrainServiceImpl trainServiceImpl = new TrainServiceImpl();
	
	/**
	 * 
	 * @param request
	 * @param response
	 */
	public WaitAlterEntity newQueryData(Map<String, String> paramMap){
		List<WaitAlterEntity> list_train = null;
		String train_no = paramMap.get("train_no");
		String buy_money = paramMap.get("buy_money");//原车票的成本价格
		String now_date = DateUtil.dateToString(new Date(),DateUtil.DATE_FMT1);//今天
		String tom_date = DateUtil.dateAddDaysFmt3(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1),"1");//明天
		String sec_date = DateUtil.dateAddDaysFmt3(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1),"2");//后天
		String alter_time_limit = paramMap.get("alter_time_limit");//24hour_inner    48hour_inner
		
		if(alter_time_limit.equals("24hour_inner") && tom_date.equals(paramMap.get("travel_time"))){
			//24小时内发车，并且发车时间是第2天----可以改签到10%的手续费
			try{
				paramMap.put("type","all");
				list_train = queryTrainList(paramMap);
				if(null==list_train){
					return null;
				}
				WaitAlterEntity first_train = null;
				String old_date = paramMap.get("travel_time");
				if(DateUtil.weatherChunYun(old_date)){
					first_train = limitTimeTrain(list_train,train_no,buy_money,"now_date");
				}else{
					first_train = limitTimeTrain(list_train,train_no,buy_money,"tom_date");
				}
				
				if(null==first_train || null==first_train.getTrain_no()){
					trainServiceImpl.insertHistory(paramMap.get("order_id"),null, old_date+"没有符合条件的车次！");//插入日志
					return null;
				}else{
					first_train.setWea_48(true);
					return first_train;
				}
			}catch(Exception e){
				logger.error("24小时内筛选车次异常！",e);
				return null;
			}
		}else if(alter_time_limit.equals("48hour_inner") && sec_date.equals(paramMap.get("travel_time"))){
			//48小时之内，后天发车-----可以改签到手续费为5%
			try{
				//48小时内
				paramMap.put("type","all");
				list_train = queryTrainList(paramMap);
				if(null==list_train){
					return null;
				}
				String old_date = paramMap.get("travel_time");
				WaitAlterEntity first_train = null;
				if(DateUtil.weatherChunYun(old_date)){
					first_train = limitTimeTrain(list_train,train_no,buy_money,"now_date");
				}else{
					first_train = limitTimeTrain(list_train,train_no,buy_money,"sec_date");
				}
				 
				if(null==first_train || null==first_train.getTrain_no()){
					trainServiceImpl.insertHistory(paramMap.get("order_id"),null, old_date+"没有符合条件的车次！");//插入日志
					return null;
				}else{
					first_train.setWea_48(true);
					return first_train;
				}
			}catch(Exception e){
				logger.error("48小时内筛选车次异常！",e);
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	//获取最低票价的车次
	public WaitAlterEntity limitTimeTrain(List<WaitAlterEntity> list_train, String train_no, String buy_money, String type){
		String from_time = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT4);
		double low_price = 10000;//当前车次的手续费
		double mid_price = 0;//筛选出的车次的手续费
		double seat_price = Double.valueOf(buy_money);//原票价
		WaitAlterEntity last_entity = new WaitAlterEntity();
		String[] trains = train_no.split("/");//原车次
		
		//当前车次为最低票价车次
		for(String train:trains){//原车次
			for(WaitAlterEntity alter:list_train){
				if(train.equals(alter.getTrain_no()) && !alter.getSeat_price().equals("-")){
					last_entity = alter;
					if("now_date".equals(type)){
						low_price = Double.valueOf(alter.getSeat_price())*0.2;
					}else if("fif_after".equals(type)){
						low_price = Double.valueOf(alter.getSeat_price());
					}else if("tom_date".equals(type)){
						low_price = Double.valueOf(alter.getSeat_price())*0.2;
					}else if("sec_date".equals(type)){
						low_price = Double.valueOf(alter.getSeat_price())*0.1;
					}
				}
			}
		}
		
		//24小时内，now+1天
		if("tom_date".equals(type)){
			for(WaitAlterEntity entity:list_train){//筛选出来
				try{
					if(Integer.valueOf(entity.getFrom_time().replace(":", ""))>Integer.valueOf(from_time.replace(":", ""))){
						//改签的时间在此时间之后20%-----10%
//						mid_price = (seat_price-Double.valueOf(entity.getSeat_price()))*0.2 //改签差额的手续费
//									+ Double.valueOf(entity.getSeat_price())*0.1;//改签后的车票退票的手续费
						mid_price = AmountUtil.add((seat_price-Double.valueOf(entity.getSeat_price()))*0.2, Double.valueOf(entity.getSeat_price())*0.1);
						//logger.info("发车时间："+entity.getFrom_time()+"/"+from_time+"/"+mid_price+"/"+low_price+"----"+(mid_price<low_price)+"/"+(Double.parseDouble(entity.getSeat_price())<=seat_price));//21:40/16:52
						
						
					}else{
						mid_price = AmountUtil.add((seat_price-Double.valueOf(entity.getSeat_price()))*0.2, Double.valueOf(entity.getSeat_price())*0.2);
//						mid_price = (seat_price-Double.valueOf(entity.getSeat_price()))*0.2 //改签差额的手续费
//									+ Double.valueOf(entity.getSeat_price())*0.2;
//						logger.info("车次："+entity.getTrain_no()+" 手续费："+mid_price);
					}
					
					
					if(mid_price<low_price){
						if(Double.parseDouble(entity.getSeat_price())<=seat_price){
							low_price = mid_price;
							last_entity = entity;
							logger.info("手续费24："+low_price+"/"+entity.getFrom_time()+"/"+entity.getTrain_no());
						}
						
					}
//					if(mid_price<low_price){
//						low_price = mid_price;
//						last_entity = entity;
//					}
				}catch(Exception e){
					logger.error("筛选24小时内，now+1天车次异常！",e);
				}
			}
			logger.info("[24小时内，now+1天]"+low_price+"---"+last_entity.getSeat_price());
		}
		
		//48小时内，now+2天---------------改签到5%的手续费
		if("sec_date".equals(type)){
			for(WaitAlterEntity entity:list_train){
				try{
					logger.info("出发时间48："+entity.getFrom_time()+"/"+entity.getSeat_price()+"/"+seat_price);
					if(Integer.valueOf(entity.getFrom_time().replace(":", ""))>Integer.valueOf(from_time.replace(":", ""))){
						mid_price = AmountUtil.add((seat_price-Double.valueOf(entity.getSeat_price()))*0.1, Double.valueOf(entity.getSeat_price())*0.05);
						
//						mid_price = (seat_price-Double.valueOf(entity.getSeat_price()))*0.1 //改签差额的手续费
//									+ Double.valueOf(entity.getSeat_price())*0.05;
//						logger.info("车次："+entity.getTrain_no()+" 手续费："+mid_price);
						
					}else{
						mid_price = AmountUtil.add((seat_price-Double.valueOf(entity.getSeat_price()))*0.1, Double.valueOf(entity.getSeat_price())*0.1);
//						mid_price = (seat_price-Double.valueOf(entity.getSeat_price()))*0.1 //改签差额的手续费
//									+ Double.valueOf(entity.getSeat_price())*0.1;
//						logger.info("车次："+entity.getTrain_no()+" 手续费："+mid_price);
					}
					
					if(mid_price<low_price){
						if(Double.parseDouble(entity.getSeat_price())<=seat_price){
							low_price = mid_price;
							last_entity = entity;
							logger.info("手续费48："+low_price+"/"+entity.getFrom_time()+"/"+entity.getTrain_no());
						}
					}
					
//					if(mid_price<low_price){
//						low_price = mid_price;
//						last_entity = entity;
//					}
				}catch(Exception e){
					logger.error("筛选48小时内，now+2天车次异常！",e);
				}
			}
			logger.info("[48小时内，now+2天]"+low_price+""+last_entity.getSeat_price());
		}  
		
		if(low_price==0){
			return null;
		}
		if("fif_after".equals(type)){
			low_price = 0;
		}
		last_entity.setHand_price(low_price);
		logger.info("最低车次："+last_entity.getTrain_no()+" 手续费："+low_price);
		return last_entity;
	}
	
	
	public List<WaitAlterEntity> queryTrainList(Map<String, String> paramMap){
		ObjectMapper mapper = new ObjectMapper();
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		StringBuffer param = new StringBuffer();
		try {
			param.append("travel_time=").append(paramMap.get("travel_time")).append("&from_station=").append(URLEncoder.encode(paramMap.get("from_station"),"UTF-8"))
			.append("&arrive_station=").append(URLEncoder.encode(paramMap.get("arrive_station"), "utf-8")).append("&channel=").append("ext")
			.append("&alter_refund=").append("true");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if("true".equals(paramMap.get("check_spare_num"))){
			param.append("&check_spare_num=").append("true");
		}
		logger.info(paramMap.get("travel_time")+paramMap.get("from_station")+paramMap.get("arrive_station"));
		try{
			SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
			String query_left_ticket_url = "";
			try {
				sysImpl.querySysVal("query_left_ticket_url");
				if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
					query_left_ticket_url = sysImpl.getSysVal();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String jsonStr = HttpUtil.sendByPost(query_left_ticket_url, param.toString(), "utf-8");//调用接口
			if("NO_DATAS".equals(jsonStr) || "ERROR".equals(jsonStr)){
				osnd = null;
				trainServiceImpl.insertHistory(paramMap.get("order_id"),null, paramMap.get("travel_time")+paramMap.get("from_station")+"/"+paramMap.get("arrive_station")+"，查询余票异常！");//插入日志
			}else{
				osnd = mapper.readValue(jsonStr, OuterSoukdNewData.class);
			}
		}catch(Exception e){
			logger.error("查询余票异常！", e);
			osnd = null;
		}
		List<WaitAlterEntity> list = new ArrayList<WaitAlterEntity>();
		if(osnd!=null && osnd.getDatajson()!=null && osnd.getDatajson().size()>0){
			formatStandardData(list,osnd,paramMap);
		}
		return list;
	}
	
	
	
	//根据坐席类型获取票价
	//原座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 
	//5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他
	public String getAlterSeatPrice(TrainNewData train,String seat_type,String spare_amount){
		String result = "";
		if("0".equals(seat_type) && !"-".equals(replaceNumVal(train.getSwz_num(),spare_amount))){//0、商务座
			result = train.getSwz();
		}else if("1".equals(seat_type) && !"-".equals(replaceNumVal(train.getTz_num(),spare_amount))){//1、特等座
			result = train.getTdz();
		}else if("2".equals(seat_type) && !"-".equals(replaceNumVal(train.getZy_num(),spare_amount))){//2、一等座
			result = train.getZy();
		}else if("3".equals(seat_type) && !"-".equals(replaceNumVal(train.getZe_num(),spare_amount))){//3、二等座----无座
			result = train.getZe();
		}else if("4".equals(seat_type) && !"-".equals(replaceNumVal(train.getGr_num(),spare_amount))){//4、高级软卧（41、高级软卧上 42、高级软卧下）
			result = train.getGwx();
		}else if("5".equals(seat_type) && !"-".equals(replaceNumVal(train.getRw_num(),spare_amount))){//5、软卧 （51、软卧上 52、软卧下） 
			result = train.getRwx();
		}else if("6".equals(seat_type) && !"-".equals(replaceNumVal(train.getYw_num(),spare_amount))){//6、硬卧（61、硬卧上 62、硬卧中 63、硬卧下）
			result = train.getYwx();
		}else if("7".equals(seat_type) && !"-".equals(replaceNumVal(train.getRz_num(),spare_amount))){//7、软座 
			result = train.getRz();
		}else if("8".equals(seat_type) && !"-".equals(replaceNumVal(train.getYz_num(),spare_amount))){//8、硬座----无座
			result = train.getYz();
		}else if("9".equals(seat_type) && !"-".equals(replaceNumVal(train.getWz_num(),spare_amount))){//9、无座
			result = train.getWz();
		}
		return result;
	}
	  
	
	//查询车次信息-----全部车次信息
	public void formatStandardData(List<WaitAlterEntity> list,OuterSoukdNewData osnd,Map<String,String> paramMap){
		String spare_amount = "10";
		String train_no = paramMap.get("train_no");
		String type = paramMap.get("type");
		String seat_type = paramMap.get("seat_type");//原坐席
		String seat_price = paramMap.get("buy_money");//原票价
		
		//筛选普通列车和快速列车和临客
		for(TrainNewData train:osnd.getDatajson()){
			String seat_price_choose = getAlterSeatPrice(train, seat_type, "1");
			WaitAlterEntity waitEntity = new WaitAlterEntity();
			
			if(StringUtils.isNotEmpty(seat_price_choose) && !seat_price_choose.equals("-")){
				waitEntity.setSeat_price(seat_price_choose);
				waitEntity.setSeat_type(seat_type);
				waitEntity.setTrain_no(train.getStation_train_code());
				waitEntity.setTravel_time(paramMap.get("travel_time"));
				waitEntity.setFrom_time(train.getStart_time());
				list.add(waitEntity);
			}
			
			
		}
		if("all".equals(type)){
			for(TrainNewData train:osnd.getDatajson()){
				WaitAlterEntity waitEntity = new WaitAlterEntity();
				if(train_no.contains("G") || train_no.contains("C") || train_no.contains("D")){
					if(!"-".equals(replaceNumVal(train.getZe_num(),spare_amount))|| !"-".equals(replaceNumVal(train.getWz_num(),spare_amount))){
						String seat_price_choose = getAlterSeatPrice(train, seat_type, "1");
						if(StringUtils.isNotEmpty(seat_price_choose) && !seat_price_choose.equals("-")){
							waitEntity.setSeat_price(seat_price_choose);
							waitEntity.setSeat_type(seat_type);
							waitEntity.setTrain_no(train.getStation_train_code());
							waitEntity.setTravel_time(paramMap.get("travel_time"));
							waitEntity.setFrom_time(train.getStart_time());
							list.add(waitEntity);
						}
//						waitEntity.setTrain_no(train.getStation_train_code());
//						waitEntity.setSeat_price(train.getZe());
//						waitEntity.setTravel_time(paramMap.get("travel_time"));
//						waitEntity.setSeat_type("3");
//						waitEntity.setFrom_time(train.getStart_time());
//						list.add(waitEntity);
					}
				}
			}
		}
	}
	
	
	
	//余票阀值控制
	protected String replaceNumVal(String str,String limit_num){
		if(!StringUtils.isEmpty(str)){
			if(!"-".equals(str) && Integer.parseInt(str)<=Integer.parseInt(limit_num)){
				str = "-";
			}
		}
		return str;
	}
	
	
}
