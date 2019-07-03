package com.l9e.transaction.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;

import com.l9e.common.BuyTicketBase;
import com.l9e.transaction.vo.OuterSoukdData;
import com.l9e.transaction.vo.TrainData;
import com.l9e.util.DateUtil;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.XmlUtil;

/**
 * 车票预订
 * @author zuoyuxing
 *
 */
@Controller
public class SoukdBuyTicketController extends BuyTicketBase { 
	
	/**
	 * 根据车站查询
	 * @param request
	 * @param paramMap
	 */
	public void soukdQueryData(Map<String, String> paramMap,HttpServletRequest request){
		long log_begin_time = System.currentTimeMillis();//日志查询开始时间
		request.setAttribute("otVo", obtainSoukdData(paramMap,request));
		request.setAttribute("paramMap", paramMap);
		queryTimeMillis(request, paramMap, log_begin_time);
	}
	//获取余票结果数据 
	public OuterSoukdData obtainSoukdData(Map<String, String> paramMap,HttpServletRequest request ){
		String key = getFileName(paramMap);
		OuterSoukdData otVo = null;
		try{
			//Memcache中是否存在该记录
			if(null == MemcachedUtil.getInstance().getAttribute(key)){
				otVo = getDataFromInterface(paramMap);
				String prePath = request.getSession().getServletContext().getRealPath("/files");
				if(otVo!=null && otVo.getDataList() != null && otVo.getDataList().size()!=0){
					//保存为缓存文件
					queryTicketCache(prePath,paramMap.get("travel_time"),otVo,key);
				}
				
			}else{//Memcache中读取文件路径
				long start = System.currentTimeMillis();
				String filePath = (String) MemcachedUtil.getInstance().getAttribute(key);
				String fileContent = FileUtil.readFile(filePath, SOUKD_CHARSET);
				logger.info("读文件"+filePath+"耗时" + (System.currentTimeMillis() - start)+ "ms");
				logger.debug("读文件"+filePath+"返回内容：" + fileContent);
				
				ObjectMapper mapper = new ObjectMapper();
				otVo = mapper.readValue(fileContent, OuterSoukdData.class);
			}
		}catch(Exception e){
			otVo = null;
			logger.error("火车票查询异常~~", e);
		}
		limitTimeTicket(paramMap.get("travel_time"),otVo);
		return otVo;
	}
	/**
	 * 追加余票时间限制
	 * @param travel_time
	 */
	public void limitTimeTicket(String travel_time,OuterSoukdData otVo){
		String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		//6小时之内的票不能订购
		if(travel_time.equals(nowDate)){
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			if(otVo!=null){
				for (TrainData trainData : otVo.getDataList()) {
					int beginTime = Integer.parseInt(trainData.getStartTime().replaceAll(":", ""));
					String yp_show = "无";
					if(beginTime-currentTime < 600){
						trainData.setWz_yp_show(yp_show);
						trainData.setYz_yp_show(yp_show);
						trainData.setRz1_yp_show(yp_show);
						trainData.setRz1_yp_show(yp_show);
						trainData.setRz2_yp_show(yp_show);
						trainData.setYw_yp_show(yp_show);
						trainData.setRw_yp_show(yp_show);
						trainData.setGw_yp_show(yp_show);
						trainData.setTdz_yp_show(yp_show);
						trainData.setSwz_yp_show(yp_show);
						trainData.setCanBook("0");
					}
				}
			}
		}
		if(travel_time.equals(DateUtil.dateAddDays(nowDate,"1"))){
			if("23".compareTo((DateUtil.dateToString(new Date(), "HH")))<=0){
				if(otVo!=null){
					for (TrainData trainData : otVo.getDataList()) {
						if(trainData.getStartTime().compareTo("09:00")<0){
							trainData.setCanBook("0");
						}
					}
				}
			}
		}
	}
	/**
	 * 第三方接口
	 * 查询车票信息
	 * 
	 * @param paramMap
	 * @param interfaceName
	 * @param interfaceUrl
	 */
	public OuterSoukdData getDataFromInterface(Map<String, String> paramMap){
		OuterSoukdData otVo = new OuterSoukdData();
		String url = null;
		url = getSoukdUrl(paramMap, soukd_Url);
		long start = System.currentTimeMillis();
//		 //临时测试代理
	    System.getProperties().setProperty("http.proxyHost", "192.168.63.238");
	    System.getProperties().setProperty("http.proxyPort", "6789" );
		String xmlStr = HttpUtil.sendByGet(url, SOUKD_CHARSET, "30000", "30000");//调用接口
		try{
			//XML序列化为bean
			otVo = XmlUtil.toBean(xmlStr, OuterSoukdData.class);
			if(otVo != null){
				logger.info("<火车票查询>调用SOUKD接口成功查询"
						+paramMap.get("from_city")+"/"+paramMap.get("to_city")
						+"("+paramMap.get("travel_time")+")的列车信息，耗时" + (System.currentTimeMillis() - start) + "ms");
				
				//统计列车列数
				if(otVo.getDataList() == null || otVo.getDataList().size()==0){
					logger.info("[火车票查询]查询"
							+paramMap.get("from_city")+"/"+paramMap.get("to_city")
							+"("+paramMap.get("travel_time")+")的列车共计0列");
				}else{
					//解决xstream bug
					ObjectMapper mapper = new ObjectMapper();
					otVo = mapper.readValue(mapper.writeValueAsString(otVo), OuterSoukdData.class);
					
					logger.info("[火车票查询]查询"
							+paramMap.get("from_city")+"/"+paramMap.get("to_city")
							+"("+paramMap.get("travel_time")+")的列车共计"+otVo.getDataList().size()+"列");
				}
			}
		}catch(Exception e){//没有查询到数据
			logger.error("解析SOUKD接口返回数据异常", e);
			return null;
		}
		return otVo;
	}
}
