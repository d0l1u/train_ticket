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

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.apache.commons.lang.StringUtils;

import com.l9e.common.BuyTicketBase;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.OuterSoukdNewData;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;

/**
 * 车票预订
 * @author zuoyuxing 
 *
 */
@Controller
public class NewBuyTicketController extends BuyTicketBase { 
	@Resource
	private QueryTicketService ticketService;

	@Resource
	protected CommonService commonService;
	/**
	 * 根据车站查询
	 * @param paramMap
	 * @param response
	 * @throws UnsupportedEncodingException 
	 */
	public void newQueryData(Map<String, String> paramMap,HttpServletRequest request,HttpServletResponse response){
		long log_begin_time = System.currentTimeMillis();//日志查询开始时间
		String travel_time = paramMap.get("travel_time");
		ObjectMapper mapper = new ObjectMapper();
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		StringBuffer param = new StringBuffer();
		/*try {
			param.append("travel_time=").append(travel_time).append("&from_station=").append(URLEncoder.encode(paramMap.get("from_station"), "utf-8"))
			.append("&arrive_station=").append(URLEncoder.encode(paramMap.get("arrive_station"), "utf-8")).append("&channel=").append("inner");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		param.append("travel_time=").append(travel_time).append("&from_station=").append(paramMap.get("from_station"))
		.append("&arrive_station=").append(paramMap.get("arrive_station")).append("&channel=").append("inner");
		logger.info(travel_time+paramMap.get("from_station")+paramMap.get("arrive_station"));
		try{
			String jsonQuery = HttpUtil.sendByPost(getSysInterfaceChannel("query_left_ticket_url"),param.toString(),"UTF-8");//调用接口
			logger.info("查询返回结果数据："+jsonQuery);
			if("NO_DATAS".equals(jsonQuery) || "ERROR".equals(jsonQuery)){
				osnd = null;
			}else{
				osnd = mapper.readValue(jsonQuery, OuterSoukdNewData.class);
				osnd.setSdate(travel_time);
			}
		}catch(Exception e){
			logger.error("查询余票异常！", e);
			osnd = null;
		}
		
		//追加时间限制
		limitTimeTicket(travel_time,osnd);
		
		request.setAttribute("osnd", osnd);
		request.setAttribute("paramMap", paramMap);
		queryTimeMillis(request, paramMap, log_begin_time);
	}
	
	/**
	 * 新接口数据拼接车次信息票价
	 * 
	 */
	public void trainInfoAppendPrice(Map<String, String> paramMap,HttpServletRequest request,OuterSoukdNewData osnd){
		List<TrainNewDataFake> list = new ArrayList<TrainNewDataFake>();
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
		String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		//6小时之内的票不能订购
		String stopBuyTicketTime=commonService.querySysStopTime();
		logger.info("停止购票时间为："+stopBuyTicketTime);
		int stopTime=0;
		int stopNum=600;
		if(StringUtils.isNotEmpty(stopBuyTicketTime)){
			if(!"0.5".equals(stopBuyTicketTime)){
				stopTime=Integer.valueOf(stopBuyTicketTime);
			}else{
				stopTime=30;
				stopNum=3000;
			}
		}
		if(stopTime>=2 && stopTime<=10){ 
			stopNum=stopTime*100;
		}
		if(travel_time.equals(nowDate)){
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			int currentTime_HH = Integer.parseInt(DateUtil.dateToString(new Date(), "HH"));
			int currentTime_mm = Integer.parseInt(DateUtil.dateToString(new Date(), "mm"));
			if(osnd!=null){
				for (TrainNewData trainNewData : osnd.getDatajson()) {
					int beginTime = Integer.parseInt(trainNewData.getStart_time().replaceAll(":", ""));
					String num_show = "无";
					if(stopNum == 3000){
						int beginTime_HH = Integer.parseInt(trainNewData.getStart_time().substring(0,2));
						int beginTime_mm = Integer.parseInt(trainNewData.getStart_time().substring(3));
						if((beginTime_HH-currentTime_HH <=0 && beginTime_mm-currentTime_mm<30)
						|| (beginTime_HH-currentTime_HH ==1 && 60-currentTime_mm+beginTime_mm<30)	
						){
							trainNewData.setWz_num_show(num_show);
							trainNewData.setYz_num_show(num_show);
							trainNewData.setRz_num_show(num_show);
							trainNewData.setZy_num_show(num_show);
							trainNewData.setZe_num_show(num_show);
							trainNewData.setYw_num_show(num_show);
							trainNewData.setRw_num_show(num_show);
							trainNewData.setGr_num_show(num_show);
							trainNewData.setTz_num_show(num_show);
							trainNewData.setSwz_num_show(num_show);
							trainNewData.setCanBook("0");
						}
					}else{
					if(beginTime-currentTime < stopNum){
						trainNewData.setWz_num_show(num_show);
						trainNewData.setYz_num_show(num_show);
						trainNewData.setRz_num_show(num_show);
						trainNewData.setZy_num_show(num_show);
						trainNewData.setZe_num_show(num_show);
						trainNewData.setYw_num_show(num_show);
						trainNewData.setRw_num_show(num_show);
						trainNewData.setGr_num_show(num_show);
						trainNewData.setTz_num_show(num_show);
						trainNewData.setSwz_num_show(num_show);
						trainNewData.setCanBook("0");
					}
					}
				}
			}
		}
		if(travel_time.equals(DateUtil.dateAddDays(nowDate,"1"))){
			if("23".compareTo((DateUtil.dateToString(new Date(), "HH")))<=0){
				if(osnd!=null){
					for (TrainNewData trainNewData : osnd.getDatajson()) {
						if(trainNewData.getStart_time().compareTo("09:00")<0){
							trainNewData.setCanBook("0");
						}
					}
				}
			}
		}
	}
	
	//网站终止预订功能
	//进行车次查询
	public void unableBookTicketsQuery(Map<String, String> paramMap,HttpServletRequest request){
		List<TrainNewDataFakeAppendTrain> list = ticketService.queryAppendTrainNewData(paramMap);
		for(TrainNewDataFakeAppendTrain appTrain : list){
			appTrain.changeData();
		}
		request.setAttribute("paramMap", paramMap);
		request.setAttribute("unBookList", list);
	}
}
