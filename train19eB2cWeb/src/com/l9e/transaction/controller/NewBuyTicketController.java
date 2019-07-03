package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.OuterSoukdNewData;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;

/**
 * 车票预订
 * @author zuoyuxing
 *
 */
@Controller
public class NewBuyTicketController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(NewBuyTicketController.class);
	
	@Resource
	private QueryTicketService ticketService;
	
	@Resource
	private CommonService commonService;
	
	/**
	 * 根据车站查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	//新接口
	public void newQueryData(Map<String, String> paramMap,HttpServletRequest request,HttpServletResponse response){
		long log_begin_time = System.currentTimeMillis();//日志查询开始时间
		String travel_time = paramMap.get("travel_time");
		
		ObjectMapper mapper = new ObjectMapper();
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		StringBuffer param = new StringBuffer();
		param.append("travel_time=").append(travel_time).append("&from_station=").append(paramMap.get("from_city"))
			.append("&arrive_station=").append(paramMap.get("to_city")).append("&channel=").append("app");
		try{
			String jsonStr = HttpPostUtil.sendAndRecive(getSysSettingValue("query_left_ticket_url","query_left_ticket_url"),param.toString());//调用接口
			if("NO_DATAS".equals(jsonStr) || "ERROR".equals(jsonStr)){
				osnd = null;
			}else{
				osnd = mapper.readValue(jsonStr, OuterSoukdNewData.class);
				osnd.setSdate(travel_time);
			}
		}catch(Exception e){
			logger.error("查询余票异常！", e);
			osnd = null;
		}
		request.setAttribute("paramMap", paramMap);
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
		//ticketLimit时间之内的票不能订购
		if(paramMap.get("travel_time").equals(nowDate)){
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			if(osnd!=null){
				for (TrainNewData trainNewData : osnd.getDatajson()) {
					int beginTime = Integer.parseInt(trainNewData.getStart_time().replaceAll(":", ""));
					String num_show = "0";
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
			System.out.println("zuoyuxing:"+DateUtil.dateToString(new Date(), "HH"));
			//定当天票同时订票时间大于23:00点,则23:00到24:00之间的票的不能预订
			if("23".compareTo((DateUtil.dateToString(new Date(), "HH")))<=0){
				if(osnd!=null){
					for (TrainNewData trainNewData : osnd.getDatajson()) {
						if(trainNewData.getStart_time().compareTo("23:00")>=0){
							trainNewData.setCanBook("0");
						}
					}
				}
			}
		}
		if(paramMap.get("travel_time").equals(DateUtil.dateAddDays(nowDate,"1"))){
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
		//若发车日期超过20天，则将余票更新为固定值（因为该票还不存在）
		long canBookTime = DateUtil.dateAddDays(new Date(),59).getTime();//12306可以预定的时间
		long travelTime = DateUtil.stringToDate(paramMap.get("travel_time"), DateUtil.DATE_FMT1).getTime();//出发日期
		String num1 = "105";
		String num2 = "105";
		if(travelTime > canBookTime){
			if(osnd!=null){
				for (TrainNewData trainNewData : osnd.getDatajson()) {
					//trainNewData.setWz_num_show(num1);
					//trainNewData.setWz_num(num1);
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
						trainNewData.setYw_num_show(num2);
						trainNewData.setYw_num(num2);
					}
					if(trainNewData.getRws()!=null && trainNewData.getRws().length()>0){
						trainNewData.setRw_num_show(num2);
						trainNewData.setRw_num(num2);
					}
					if(trainNewData.getGws()!=null && trainNewData.getGws().length()>0){
						trainNewData.setGr_num_show(num2);
						trainNewData.setGr_num(num2);
					}
					
					trainNewData.setCanBook("1");//可以预定
				}
			}
			
		}
		
		osnd = checkTrainType(osnd, paramMap);//仅查询高铁和动车
		request.setAttribute("osnd", osnd);
		
		request.setAttribute("spareTicket", MemcachedUtil.spareTicket);//余票阀值
		logger.info("<火车票查询>总计查询"
				+paramMap.get("from_city")+"/"+paramMap.get("to_city")
				+"("+paramMap.get("travel_time")+")的列车信息，耗时" + (System.currentTimeMillis() - log_begin_time)+ "ms");
	}
	
	/*
	 * 筛选出所有车次信息中为动车（D）、高铁（G）、城际列车(C)的车次
	 * @param data为查询到得所有车次信息
	 * @param paramMap从首页传过来的参数值
	 */
	public OuterSoukdNewData checkTrainType(OuterSoukdNewData data,
			Map<String, String> paramMap) {
		if (data!=null && "on".equals(paramMap.get("gaotie"))) {
			logger.info("仅查询高铁和动车");
			List<TrainNewData> trainDataList = data.getDatajson();
			List<TrainNewData> newTrainDataList = new ArrayList<TrainNewData>();
			for (TrainNewData trainData : trainDataList) {
				if (trainData.getStation_train_code().contains("D")
						|| trainData.getStation_train_code().contains("C")
						|| trainData.getStation_train_code().contains("G")) {
					newTrainDataList.add(trainData);
				}
			}
			data.setDatajson(newTrainDataList);
			return data;
		} else {
			return data;
		}
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
	
	public String unableBookTicketsQuery(Map<String, String> paramMap,HttpServletRequest request){
		List<TrainNewDataFakeAppendTrain> list = ticketService.queryAppendTrainNewData(paramMap);
		for(TrainNewDataFakeAppendTrain appTrain : list){
			appTrain.changeData();
		}
		request.setAttribute("paramMap", paramMap);
		request.setAttribute("unBookList", list);
		return "book/bookIndexNew";
	}
}
