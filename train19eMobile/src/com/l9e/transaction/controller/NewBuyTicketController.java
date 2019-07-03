package com.l9e.transaction.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.l9e.common.ExternalBase;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.ExternalInterfaceVo;
import com.l9e.transaction.vo.ExternalTrainData;
import com.l9e.transaction.vo.OuterSoukdNewData;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;

/**
 * 车票预订
 * @author zuoyuxing
 *
 */
@Component
public class NewBuyTicketController extends ExternalBase { 
	@Resource
	private QueryTicketService ticketService;
	@Resource
	private SoukdBuyTicketController soukd;
	/**
	 * 根据车站查询
	 * @param paramMap
	 * @param response
	 */
	public void newQueryData(Map<String, String> paramMap,HttpServletRequest request,HttpServletResponse response){
		//设置缓存区余票阀值数
		MemcachedUtil.spareTicket = Integer.valueOf(this.getSysSettingValue("spare_ticket_amount","spare_ticket_amount"));
		long log_begin_time = System.currentTimeMillis();//日志查询开始时间
		String travel_time = paramMap.get("travel_time");
		ExternalInterfaceVo externalVo = new ExternalInterfaceVo();
		ObjectMapper mapper = new ObjectMapper();
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		StringBuffer param = new StringBuffer();
		try {
			param.append("travel_time=").append(travel_time).append("&from_station=").append(URLEncoder.encode(paramMap.get("from_station"), "utf-8"))
				.append("&arrive_station=").append(URLEncoder.encode(paramMap.get("arrive_station"), "utf-8")).append("&channel=").append("19e");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			String jsonStr = HttpUtil.sendByPost(query_left_ticket_url,param.toString(),"UTF-8");//调用接口
//			logger.info("----------------------"+jsonStr);
			if("error".equals(jsonStr)){
//				soukd.soukdQueryData(paramMap, request, response);
				JSONObject json = new JSONObject();
				json.put("return_code", "FAIL");
				json.put("message", "系统异常！");
				printJson(response, json.toString());
				return;
			}
			osnd = mapper.readValue(jsonStr, OuterSoukdNewData.class);
		}catch(Exception e){
			logger.error("查询余票异常！", e);
			JSONObject json = new JSONObject();
			json.put("return_code", "FAIL");
			json.put("message", "系统异常！");
			printJson(response, json.toString());
			return;
		}
		if(osnd!=null && osnd.getDatajson()!=null && osnd.getDatajson().size()>0){
			
			//追加时间限制
			limitTimeTicket(travel_time,osnd);
			
			String speed_only = this.getParam(request, "speed_only");//只搜高铁动车
			formatStandardData(externalVo,osnd, speed_only);
		}
		
		externalVo.setLeft_limit(this.getLeftTikectLimit());//余票阀值
		JSONObject jsonObject = JSONObject.fromObject(externalVo);
		if(!StringUtils.isEmpty(paramMap.get("train_code"))){//实时余票验证
			JSONObject json = new JSONObject();
			JSONArray jsonArray = (JSONArray) jsonObject.get("trainData");
			for(int i=0; i<jsonArray.size(); i++){
				if(paramMap.get("train_code").equals(jsonArray.getJSONObject(i).get("train_code"))){
					json = jsonArray.getJSONObject(i);
					json.put("return_code", externalVo.getReturn_code());
					json.put("message", externalVo.getMessage());
					logger.info(json.toString());
					break;
				}
			}
			osnd = null;
			printJson(response,json.toString());
		}else{
			osnd = null;
			printJson(response,jsonObject.toString());
			logger.info("<火车票查询>总计查询"
					+paramMap.get("from_station")+"/"+paramMap.get("arrive_station")
					+"("+paramMap.get("travel_time")+")的列车信息，耗时" + (System.currentTimeMillis() - log_begin_time)+ "ms");
		}
	}
	
	public void formatStandardData(ExternalInterfaceVo externalVo,OuterSoukdNewData osnd, String speed_only){
		externalVo.setReturn_code("SUCCESS");
		externalVo.setMessage("查询车票信息成功！");
		List<ExternalTrainData> list = new ArrayList<ExternalTrainData>();
		ExternalTrainData data = null;
		String train_no = null;
		for(TrainNewData train:osnd.getDatajson()){
			train_no = train.getStation_train_code();
			if(StringUtils.isNotEmpty(speed_only) && "1".equals(speed_only)){
				if(StringUtils.isEmpty(train_no)){
					continue;
				}else if(!train_no.startsWith("D") && !train_no.startsWith("G") && !train_no.startsWith("C")){
					continue;
				}
			}
			
			data = new ExternalTrainData();
			data.setTrain_code(train.getStation_train_code());
			data.setStart_station(train.getStart_station_name());
			data.setEnd_station(train.getEnd_station_name());
			data.setFrom_time(train.getStart_time());
			data.setArrive_time(train.getArrive_time());
			data.setFrom_station(train.getFrom_station_name());
			data.setArrive_station(train.getTo_station_name());
			data.setCost_time(train.getLishiValue());
			data.setWz(train.getYz());
			data.setYz(train.getYz());
			data.setWz_num(train.getWz_num());
			data.setYz_num(train.getYz_num());
			data.setRz(train.getRz());
			data.setRz_num(train.getRz_num());
			data.setRz1(train.getZy());
			data.setRz1_num(train.getZy_num());
			data.setRz2(train.getZe());
			data.setRz2_num(train.getZe_num());
			data.setYws(train.getYws());
			data.setYwz(train.getYwz());
			data.setYwx(train.getYwx());
			data.setYw_num(train.getYw_num());
			data.setRws(train.getRws());
			data.setRwx(train.getRwx());
			data.setRw_num(train.getRw_num());
			data.setSwz(train.getSwz());
			data.setSwz_num(train.getSwz_num());
			data.setTdz(train.getTdz());
			data.setTdz_num(train.getTz_num());
			data.setGws(train.getGws());
			data.setGwx(train.getGwx());
			data.setGw_num(train.getGr_num());
			list.add(data);
		}
		externalVo.setTrainData(list);
	}
	
	/**
	 * 拼接车次信息票价
	 * 
	 */
	public void trainInfoAppendPrice(Map<String, String> paramMap,HttpServletRequest request,OuterSoukdNewData osnd){
		String key = getFileName(paramMap);
		String travel_time = paramMap.get("travel_time");
		List<TrainNewDataFake> list = new ArrayList<TrainNewDataFake>();
		String prePath = request.getSession().getServletContext().getRealPath("/files");
		if(osnd != null && osnd.getDatajson() != null
				&& osnd.getDatajson().size()>0){
			List<TrainNewData> new_list = new ArrayList<TrainNewData>();
			list = ticketService.queryProperTrainNewData(paramMap);
			TrainNewDataFake tndf = null;
			boolean exist = false;
			for (TrainNewData trainNewData : osnd.getDatajson()){
					for(int i=0; i<list.size(); i++){
						String[] arrCc = list.get(i).getCc().split("/");
						String trainCode = trainNewData.getStation_train_code();
						int len = arrCc.length;
						for(int m=0; m<len; m++){
							if(arrCc[m].equals(trainCode)){
								if(list.get(i).getFz().equals(trainNewData.getFrom_station_name()) &&
									list.get(i).getDz().equals(trainNewData.getTo_station_name())){
									tndf = list.get(i);
									if(!"0".equals(tndf.getYz())){
										trainNewData.setYz(tndf.getYz());
										trainNewData.setWz(tndf.getYz());
										exist = true;
									}
									if(!"0".equals(tndf.getRz())){
										trainNewData.setRz(tndf.getRz());
										exist = true;
									}
									if(!"0".equals(tndf.getYws())){
										trainNewData.setYws(tndf.getYws());
										exist = true;
									}
									if(!"0".equals(tndf.getYwz())){
										trainNewData.setYwz(tndf.getYwz());
										exist = true;
									}
									if(!"0".equals(tndf.getYwx())){
										trainNewData.setYwx(tndf.getYwx());
										exist = true;
									}
									if(!"0".equals(tndf.getRws())){
										trainNewData.setRws(tndf.getRws());
										exist = true;
									}
									if(!"0".equals(tndf.getRwx())){
										trainNewData.setRwx(tndf.getRwx());
										exist = true;
									}
									if(!"0".equals(tndf.getRz1())){
										trainNewData.setZy(tndf.getRz1());
										exist = true;
									}
									if(!"0".equals(tndf.getRz2())){
										trainNewData.setZe(tndf.getRz2());
										exist = true;
									}
									if(exist){
										new_list.add(trainNewData);
									}
								}
							}
						}
						if(exist){
							exist = false;
							break;
						}
					}
			}
			osnd.setDatajson(new_list);
		}
	}
	/**
	 * 追加余票预订时间限制
	 * @param paramMap
	 * @param response
	 */
	public void limitTimeTicket(String travel_time,OuterSoukdNewData osnd){
		String num_show = "-";
		String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		
		//查询系统配置的停止购票时间
		String stopBuyTicketTime=commonService.querySysStopTime();
		logger.info("停止购票时间为："+stopBuyTicketTime);
		int stopTime=0;
		int stopNum=600;
		if(StringUtils.isNotEmpty(stopBuyTicketTime)){
			stopTime=Integer.valueOf(stopBuyTicketTime);
		}
		if(stopTime>=3 && stopTime<=10){
			stopNum=stopTime*100;
		}
		
		//stopTime小时之内的票不能订购
		if(travel_time.equals(nowDate)){
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			if(osnd!=null){
				for (TrainNewData trainNewData : osnd.getDatajson()) {
					int beginTime = Integer.parseInt(trainNewData.getStart_time().replaceAll(":", ""));
					if(beginTime-currentTime < stopNum){
						trainNewData.setWz_num(num_show);
						trainNewData.setYz_num(num_show);
						trainNewData.setRz_num(num_show);
						trainNewData.setZy_num(num_show);
						trainNewData.setZe_num(num_show);
						trainNewData.setYw_num(num_show);
						trainNewData.setRw_num(num_show);
						trainNewData.setGr_num(num_show);
						trainNewData.setTz_num(num_show);
						trainNewData.setSwz_num(num_show);
					}
				}
			}
		}
		if(travel_time.equals(DateUtil.dateAddDays(nowDate,"1"))){
			if("23".compareTo((DateUtil.dateToString(new Date(), "HH")))<=0){
				if(osnd!=null){
					for (TrainNewData trainNewData : osnd.getDatajson()) {
						if(trainNewData.getStart_time().compareTo("09:00")<0){
							trainNewData.setWz_num(num_show);
							trainNewData.setYz_num(num_show);
							trainNewData.setRz_num(num_show);
							trainNewData.setZy_num(num_show);
							trainNewData.setZe_num(num_show);
							trainNewData.setYw_num(num_show);
							trainNewData.setRw_num(num_show);
							trainNewData.setGr_num(num_show);
							trainNewData.setTz_num(num_show);
							trainNewData.setSwz_num(num_show);
						}
					}
				}
			}
		}
		//若发车日期超过20天，则将余票更新为固定值（因为该票还不存在）
		//预购天数查询
		String book_day_num=commonService.querySysSettingByKey("book_day_num");
		int book_day_number=Integer.parseInt(book_day_num);
		int i=book_day_number-1;
		long canBookTime = DateUtil.dateAddDays(new Date(),i).getTime();//12306可以预定的时间
		long travelTime = DateUtil.stringToDate(travel_time, DateUtil.DATE_FMT1).getTime();//出发日期
		String num1 = "105";
		if(travelTime > canBookTime){
			if(osnd!=null){
				for (TrainNewData trainNewData : osnd.getDatajson()) {
					if(trainNewData.getWz()!=null && trainNewData.getWz().length()>0){
						trainNewData.setWz_num_show(num1);//余票页面显示
						trainNewData.setWz_num(num1);//余票数量
					}
					if(trainNewData.getYz()!=null && trainNewData.getYz().length()>0){
						trainNewData.setYz_num_show(num1);//余票页面显示
						trainNewData.setYz_num(num1);//余票数量
					}
					if(trainNewData.getRz()!=null && trainNewData.getRz().length()>0){
						trainNewData.setRz_num_show(num1);
						trainNewData.setRz_num(num1);
					}
					if(trainNewData.getZy()!=null && trainNewData.getZy().length()>0){
						trainNewData.setZy_num_show(num1);
						trainNewData.setZy_num(num1);
					}
					if(trainNewData.getZe()!=null && trainNewData.getZe().length()>0){
						trainNewData.setZe_num_show(num1);
						trainNewData.setZe_num(num1);
					}
					if(trainNewData.getYws()!=null && trainNewData.getYws().length()>0){
						trainNewData.setYw_num_show(num1);
						trainNewData.setYw_num(num1);
					}
					if(trainNewData.getRws()!=null && trainNewData.getRws().length()>0){
						trainNewData.setRw_num_show(num1);
						trainNewData.setRw_num(num1);
					}
					if(trainNewData.getGws()!=null && trainNewData.getGws().length()>0){
						trainNewData.setGr_num_show(num1);
						trainNewData.setGr_num(num1);
					}
					if(trainNewData.getSwz()!=null && trainNewData.getSwz().length()>0){
						trainNewData.setSwz_num_show(num1);
						trainNewData.setSwz_num(num1);
					}
					if(trainNewData.getTdz()!=null && trainNewData.getTdz().length()>0){
						trainNewData.setTz_num_show(num1);
						trainNewData.setTz_num(num1);
						trainNewData.setTdz_num(num1);
					}
					
					trainNewData.setCanBook("1");//可以预定
				}
			}
		}
	}
	
	
	//网站终止预订功能
	//进行车次查询
	public void unableBookTicketsQuery(Map<String, String> paramMap,HttpServletRequest request,HttpServletResponse response){
		List<TrainNewDataFakeAppendTrain> list = ticketService.queryAppendTrainNewData(paramMap);
		for(TrainNewDataFakeAppendTrain appTrain : list){
			appTrain.changeData();
		}
		String speed_only = this.getParam(request, "speed_only");//只搜高铁动车
		unableStandardData(list,response, speed_only);
	}
	
	public void unableStandardData(List<TrainNewDataFakeAppendTrain> list,HttpServletResponse response, String speed_only){
		ExternalInterfaceVo externalVo = new ExternalInterfaceVo();
		List<ExternalTrainData> trainList = new ArrayList<ExternalTrainData>();
		String train_no = null;
		for(TrainNewDataFakeAppendTrain train : list){
			train_no = train.getCc();
			if(StringUtils.isNotEmpty(speed_only) && "1".equals(speed_only)){
				if(StringUtils.isEmpty(train_no)){
					continue;
				}else if(!train_no.startsWith("D") && !train_no.startsWith("G")&& !train_no.startsWith("C")){
					continue;
				}
			}
			
			ExternalTrainData trainData = new ExternalTrainData();
			trainData.setArrive_station(train.getDz());
			trainData.setFrom_station(train.getFz());
			trainData.setArrive_time(train.getArrive_time());
			trainData.setCost_time(train.getLishi());
			trainData.setEnd_station(train.getStart_station_name());
			trainData.setFrom_time(train.getStart_time());
			trainData.setGw_num("-");
			trainData.setGws("-");
			trainData.setGwx("-");
			trainData.setRw_num("-");
			trainData.setRws(train.getRws());
			trainData.setRwx(train.getRwx());
			trainData.setRz1_num("-");
			trainData.setRz1(train.getRz1());
			trainData.setRz2(train.getRz2());
			trainData.setRz2_num("-");
			trainData.setRz(train.getRz());
			trainData.setRz_num("-");
			trainData.setSwz("-");
			trainData.setSwz_num("-");
			trainData.setTdz("-");
			trainData.setTdz_num("-");
			trainData.setTrain_code(train.getCc());
			trainData.setWz(train.getYz());
			trainData.setWz_num("-");
			trainData.setYz(train.getYz());
			trainData.setYws(train.getYws());
			trainData.setYwx(train.getYwx());
			trainData.setYw_num("-");
			trainData.setYz_num("-");
			
			trainList.add(trainData);
		}
		externalVo.setReturn_code("SUCCESS");
		externalVo.setMessage("查询车票信息成功！");
		externalVo.setLeft_limit(this.getLeftTikectLimit());//余票阀值
		externalVo.setTrainData(trainList);
		
		JSONObject jsonObject = JSONObject.fromObject(externalVo);
		printJson(response, jsonObject.toString());
		return;
	}
}
