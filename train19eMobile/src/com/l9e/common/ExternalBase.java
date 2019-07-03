package com.l9e.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import com.l9e.util.FileUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;

/**
 * 车票预订
 * @author zhangjun
 *
 */
public class ExternalBase extends BaseController {
	
	protected static final Logger logger = Logger.getLogger(ExternalBase.class);
	
	protected static final String SOUKD_CHARSET = "GBK";
	
	@Value("#{propertiesReader[SOUKD_URL]}")
	protected String soukd_Url;//接口url
	
	@Value("#{propertiesReader[SOUKD_METHOD_DGTRAIN]}")
	protected String soukd_DGTrain;//接口方法
	
	@Value("#{propertiesReader[SOUKD_USERID]}")
	protected String soukd_UserId;//接口用户名
	
	@Value("#{propertiesReader[SOUKD_CHECKCODE]}")
	protected String soukd_CheckCode;//接口验证码
	
	@Value("#{propertiesReader[query_left_ticket_url]}")
	protected String query_left_ticket_url;//查询中枢
	
	@Value("#{propertiesReader[soukd_query_left_ticket_url]}")
	protected String soukd_query_left_ticket_url;//soukd接口
	
	protected static Random random = new Random();
	
	public void queryTicketCache(String prePath,String travel_time,Object object,String key) {
		ObjectMapper mapper = new ObjectMapper();
		String fileDir = prePath + "/" + travel_time;
		String fileName = Md5Encrypt.md5(key, "gbk") + ".txt";
		String filePath = fileDir + "/" + fileName;
		logger.info("filePath="+filePath);
		//创建文件保存接口返回数据
		FileUtil.removeFile(filePath);
		try{
			boolean isSucess = FileUtil.createFile(fileDir, fileName, mapper.writeValueAsString(object), SOUKD_CHARSET);

			//文件生成成功则把文件名写入Memcache
			if(isSucess){
				MemcachedUtil.getInstance().setAttribute(key, filePath, 60*1000);
			}
		}catch(Exception e){
			logger.error("保存查询信息为缓存文件异常！",e);
		}
	}
	
	/**
	 * SOUKD URL
	 * @param map
	 * @return
	 */
	protected String getSoukdUrl(Map<String, String> map, String interfaceUrl){
		String fromCity = "";
		String toCity = "";
		
		try {
			fromCity = URLEncoder.encode(map.get("from_station"), SOUKD_CHARSET);
			toCity = URLEncoder.encode(map.get("arrive_station"), SOUKD_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
			
		StringBuffer sb = new StringBuffer();
		if("DGTrain".equals(map.get("method"))){
			sb.append(interfaceUrl)
			  .append(soukd_DGTrain)
			  .append("?FromCity=")
			  .append(fromCity)
			  .append("&ToCity=")
			  .append(toCity)
			  .append("&sDate=")
			  .append(map.get("travel_time"))
			  .append("&UserID=")
			  .append(soukd_UserId)
			  .append("&CheckCode=")
			  .append(soukd_CheckCode);
		}
		return sb.toString();
	}
	/**
	 * 12306 URL
	 * @param map
	 * @return
	 */
	protected String get12306Url(Map<String, String> map, String interfaceUrl){
		String url = new String(interfaceUrl);
		StringBuffer sb = new StringBuffer();
		sb.append(map.get("travel_time"))
		  .append("|")
		  .append(map.get("from_station"))
		  .append("|")
		  .append(map.get("arrive_station"));
		String param = "";
		try {
			param = URLEncoder.encode(sb.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info("[机器人session_id]=" + session_id);
		return url.replace("$session_id", session_id)
		 		  .replace("$param1", param);
	}
	
	/**
	 * 获取文件名(eg:北京_上海_2013-5-22_DGTrain)
	 * @return
	 */
	protected String getFileName(Map<String, String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("19eMobile_");
		sb.append(map.get("from_station"))
		  .append("_")
		  .append(map.get("arrive_station"))
		  .append("_")
		  .append(map.get("travel_time"))
		  .append("_")
		  .append(map.get("method"));
		return sb.toString();
		
	}
	
	
	/**
	 * 解析座位信息
	 * @param seatInfoList
	 * @param seatMsg
	 */
	protected void deSeatMsg(List<Map<String, String>> seatInfoList, String seatMsg, Map<String, String> seatPrizeMap){
		String[] seats = seatMsg.split(",");
		for (String seat : seats) {
			String[] element = seat.split("_");
			String price = element[1].trim();
			String yp = element[2].trim();
			if(!StringUtils.isEmpty(price) && !"-".equals(price) && !"-".equals(yp)){//有该类别坐席
				
				//余票小于等于10张则过滤该坐席
				if(StringUtils.isEmpty(yp)){
					continue;
				}else if(Double.parseDouble(yp)<=10){
					continue;
				}
				Map<String, String> map = new HashMap<String, String>(3);
				
				String seatType = null;
				
				//根据坐席名称取得坐席ID
				for(Map.Entry<String, String> entry : TrainConsts.getSeatType().entrySet()){
					if(!StringUtils.isEmpty(element[0]) && entry.getValue().equals(element[0])){
						seatType = entry.getKey();
					}
				}
				map.put("seatName", element[0]);
				map.put("seatType", seatType);
				map.put("price", element[1]);
				map.put("yp", element[2]);
				seatInfoList.add(map);
				
				seatPrizeMap.put("seatType"+seatType, element[1]);//坐席价格映射
			}
		}
	}
	
	/**
	 * 获取系统接口频道
	 * @param provinceId
	 * @return
	 */
	protected String getSysInterfaceChannel(String key){
		String channel = null;//1:12306接口 2:SOUKD接口,3:新接口
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			channel = commonService.querySysSettingByKey(key);
			MemcachedUtil.getInstance().setAttribute(key, channel, 60*1000);
			
		}else{
			channel = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return channel;
	}
	
	/**
	 * 获取系统配置的启用的12306接口url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected String getSysInterface12306Url(String key,String interfaceUrl){
		String url = "";
		List<String> urlList = null;
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			urlList = this.getSysSettingValueAttr(key, "1", key);
			MemcachedUtil.getInstance().setAttribute(key, urlList, 10*60*1000);
		}else{
			urlList = (List<String>) MemcachedUtil.getInstance().getAttribute(key);
		}
		if(urlList == null || urlList.size() == 0){
			url = new String(interfaceUrl);
		}else{
			int index = random.nextInt(urlList.size());
			url = urlList.get(index);
		}
		logger.info("[12306 interface selected]"+url);
		return url;
	}
	//输出jsonObject
	public void printJson(HttpServletResponse response,String str){
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(str);
		} catch (IOException e) {
			logger.error("输出异常！",e);
		}finally{
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 获取余票阀值
	 * @return
	 */
	protected String getLeftTikectLimit(){
		//设置缓存区余票阀值数
		MemcachedUtil.spareTicket = Integer.valueOf(this.getSysSettingValue("spare_ticket_amount","spare_ticket_amount"));
		String limit = this.getSysSettingValue("spare_ticket_amount","spare_ticket_amount");
		if(StringUtils.isEmpty(limit)){
			limit = "10";
		}
		return limit;
	}
}
