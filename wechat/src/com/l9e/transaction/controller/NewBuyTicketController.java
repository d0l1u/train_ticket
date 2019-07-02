package com.l9e.transaction.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;

import com.l9e.common.BuyTicketBase;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.Outer12306NewData;
import com.l9e.transaction.vo.OuterSoukdNewData;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.util.DateUtil;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;

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
	private SoukdBuyTicketController soukd;
	
	/**
	 * 根据车站查询
	 * @param paramMap
	 * @param response
	 */
	public void newQueryData(Map<String, String> paramMap,HttpServletRequest request){
		long log_begin_time = System.currentTimeMillis();//日志查询开始时间
		List<TrainNewDataFake> list = null;
		String key = getFileName(paramMap);
		String travel_time = paramMap.get("travel_time");
		OuterSoukdNewData osnd = null;
		try{
			//Memcache中是否存在该记录
			if(null == MemcachedUtil.getInstance().getAttribute(key)){
				osnd = getNewDataInterface(paramMap);//默认新12306接口
				if(osnd == null){//查询12306新接口异常，则查询Soukd
					soukd.soukdQueryData(paramMap,request);
					return;
				}
				String prePath = request.getSession().getServletContext().getRealPath("/files");
				
				if(osnd != null && osnd.getDatajson() != null
						&& osnd.getDatajson().size()>0){
					list = ticketService.queryProperTrainNewData(paramMap);
					TrainNewDataFake tndf = null;
					for (TrainNewData trainNewData : osnd.getDatajson()){
							boolean exist = false;
							for(int i=0; i<list.size(); i++){
								String[] arrCc = list.get(i).getCc().split("/");
								String trainCOde = trainNewData.getStation_train_code();
								int len = arrCc.length;
								for(int m=0; m<len; m++){
									if(arrCc[m].equals(trainCOde)){
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
										}
									}
								}
							}
						if(!exist){
							trainNewData.setStation_train_code(null);
						}
					}
					queryTicketCache(prePath,travel_time,osnd,key);
				}
			}else{//Memcache中读取文件路径
				long start = System.currentTimeMillis();
				String filePath = (String) MemcachedUtil.getInstance().getAttribute(key);
				String fileContent = FileUtil.readFile(filePath, SOUKD_CHARSET);
				logger.info("读文件"+filePath+"耗时" + (System.currentTimeMillis() - start)+ "ms");
				logger.debug("读文件"+filePath+"返回内容：" + fileContent);
				
				ObjectMapper mapper = new ObjectMapper();
				osnd = mapper.readValue(fileContent, OuterSoukdNewData.class);
			}
		}catch(Exception e){
			osnd = null;
			logger.error("火车票查询异常~~", e);
		}
		//追加时间限制
		limitTimeTicket(travel_time,osnd);
		
		request.setAttribute("osnd", osnd);
		logger.info("osnd.getDatajson():"+osnd.getDatajson());
		request.setAttribute("paramMap", paramMap);
		osnd = null;
		queryTimeMillis(request, paramMap, log_begin_time);
	}
	/**
	 * 追加余票预订时间限制
	 * @param paramMap
	 * @param response
	 */
	public void limitTimeTicket(String travel_time,OuterSoukdNewData osnd){
		String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		//6小时之内的票不能订购
		if(travel_time.equals(nowDate)){
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			if(osnd!=null){
				for (TrainNewData trainNewData : osnd.getDatajson()) {
					int beginTime = Integer.parseInt(trainNewData.getStart_time().replaceAll(":", ""));
					String num_show = "无";
					if(beginTime-currentTime < 600){
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
	
	/**
	 * 余票查询新接口
	 * @param paramMap
	 * @rerurn OuterSoukdNewData
	 */
	public OuterSoukdNewData getNewDataInterface(Map<String, String> paramMap){
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		String url = null;
			try{
				long start = System.currentTimeMillis();
				url = get12306Url(paramMap,this.getSysInterface12306Url("INTERFACE_12306_NEW_URL",interface_12306_new_url));
				//请求超时时间
				String con_timeout = this.getSysSettingValue("INTERFACE_CON_TIMEOUT", "INTERFACE_CON_TIMEOUT");
				String read_timeout = this.getSysSettingValue("INTERFACE_READ_TIMEOUT", "INTERFACE_READ_TIMEOUT");
				String jsonStr = HttpUtil.sendByGet(url, "UTF-8", con_timeout, read_timeout);//调用接口
				jsonStr = jsonStr.replace("\\", "")
								 .replace("[\"{", "[{")
								 .replace("}\"]", "}]")
								 .replace("\"[", "[")
								 .replace("]\"", "]")
								 .replace("--", "-");
				ObjectMapper mapper = new ObjectMapper();
				Outer12306NewData outer12306NewData = mapper.readValue(jsonStr, Outer12306NewData.class);
				osnd = outer12306NewData.getErrorInfo().get(0);
				if(osnd != null){
					if(StringUtils.isEmpty(paramMap.get("train_no"))){
						logger.info("<火车票查询>调用12306机器人新接口成功查询"
								+paramMap.get("from_city")+"/"+paramMap.get("to_city")
								+"("+paramMap.get("travel_time")+")的列车信息，耗时" + (System.currentTimeMillis() - start)+ "ms");
					}else{
						logger.info("<火车票查询>调用12306机器人新接口成功查询"+paramMap.get("train_no")
								+"("+paramMap.get("from_city")+"/"+paramMap.get("to_city")+" "
								+paramMap.get("travel_time")+")的列车信息，耗时" + (System.currentTimeMillis() - start)+ "ms");
					}
					
					//统计列车列数
					if(osnd.getDatajson() == null || osnd.getDatajson().size()==0){
						String erroInfo = "";
						if(!StringUtils.isEmpty(osnd.getErrInfo())){
							erroInfo = osnd.getErrInfo();
						}
						logger.info("[火车票查询]查询"
								+paramMap.get("from_city")+"/"+paramMap.get("to_city")
								+"("+paramMap.get("travel_time")+")的列车共计0列 "+erroInfo);
					}else{
						logger.info("[火车票查询]查询"
								+paramMap.get("from_city")+"/"+paramMap.get("to_city")
								+"("+paramMap.get("travel_time")+")的列车共计"+osnd.getDatajson().size()+"列");
					}
				}
			}catch(Exception e){//没有查询到数据
				logger.error("解析12306机器人返回数据异常", e);
				return null;
			}
		return osnd;
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
