package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.InterAccount;
import com.l9e.transaction.vo.QunarResult;
import com.l9e.transaction.vo.SysConfig;
import com.l9e.util.HttpUtil;
import com.l9e.util.TrainPropUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 代理商乘车人数据实时更新----去哪儿白名单增加删除接口
 **/
@Component("passengerJob")
public class PassengerJob {
	private static Logger logger=Logger.getLogger(PassengerJob.class);
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[qunarReqUrl]}")
	private String qunarReqUrl;//Qunar请求地址
	
	//去哪儿白名单增加删除接口
	public void queryPassengers() throws Exception{
		/**获取各账号下的数据**/
		for(InterAccount account : SysConfig.accountContainer){
			queryPassengersInfo(account);
		}
	}
	
	/**
	 * 所有渠道的出票成功\失败乘客信息实时更新
	 * */
	public void queryPassengersInfo(InterAccount account){
		try{
			//获取qunar订单来源账号----start
			String md5Key = account.getMd5Key();
			String merchantCode = account.getMerchantCode();
			String order_source = account.getName();
			String logPre = "【去哪儿白名单增加删除接口<"+order_source+">】";
			//获取qunar订单来源账号----end
			
			//前20分钟的时间
			Calendar theCa = Calendar.getInstance(); 
			theCa.setTime(new Date());  
			theCa.add(Calendar.MINUTE, -2); 
			Date date = theCa.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String querydate = df.format(date);
			
			Map<String, Object> param=new HashMap<String, Object>();
			param.put("begin_time", querydate.concat(":00"));
			param.put("end_time", querydate.concat(":59"));
			
			Integer orderCount = orderService.queryOrderCount(param);
			logger.info(logPre+" orderCount="+orderCount);
			if(orderCount>0){
				int size = 10;//每次查询的数量
				int pageCount = orderCount%size==0 ? orderCount/size : orderCount/size+1;
				for(int m=0; m<pageCount; m++){
					param.put("everyPagefrom", size*m);
					param.put("pageSize", size);
					List<Map<String, String>> orderList = orderService.queryOrderList(param);//order_id和acc_username\order_status\channel\error_info
					JSONArray jsonArray = new JSONArray();
				
					logger.info(logPre+" start~~~count="+orderList.size());
					for(Map<String, String> map : orderList){
						JSONObject json = new JSONObject();
						json.put("account", map.get("acc_username"));
						if("0".equals(map.get("contact_status"))){//出票成功
							json.put("flag", 2);//2-只增加帐号部分核验通过的乘车人；不删除之前映射
						}else{
							json.put("flag", 3);//3-删除该帐号乘车人映射，如该账号乘车人为空,删除该帐号全部映射，如果不为空，则只删passengers和帐号的映射
						}
						
						map.put("begin_time", querydate.concat(":00"));
						map.put("end_time", querydate.concat(":59"));
						List<Map<String, String>> passList = orderService.queryPassengerList(map);
						JSONArray passArray = new JSONArray();
						for(Map<String, String> passMap : passList){
							JSONObject passJson = new JSONObject();
							passJson.put("name", passMap.get("contact_name"));
							passJson.put("cardNo", passMap.get("cert_no"));
//							passJson.put("certType", TrainPropUtil.getQunarIdsType(passMap.get("cert_type")));//转换成qunar证件类型
							passJson.put("certType", passMap.get("cert_type"));//转换成qunar证件类型
							
							passArray.add(passJson);
						}
						json.put("passengers", passArray);
						jsonArray.add(json);
					}
					
					Map<String,String> map = new HashMap<String,String>();
					map.put("merchantCode", merchantCode);
					map.put("mappings", jsonArray.toString());
					String hMac = DigestUtils.md5Hex(md5Key + merchantCode + jsonArray.toString()).toUpperCase();
					map.put("HMAC", hMac);
					logger.info(logPre+"通知param"+map.toString());
					String reqParams = UrlFormatUtil.CreateUrl("", map, "", "UTF-8");
					
					StringBuffer reqUrl = new StringBuffer();
					reqUrl.append(qunarReqUrl).append("AccountMapping.do");
					
					String jsonRs = HttpUtil.sendByPost(reqUrl.toString(), reqParams, "UTF-8");
					logger.info(logPre+"通知返回："+jsonRs);
					if(StringUtils.isNotEmpty(jsonRs) && jsonRs!=null && !"".equals(jsonRs)){
						ObjectMapper mapper = new ObjectMapper();
						QunarResult rs = mapper.readValue(jsonRs, QunarResult.class);
						if(rs.isRet()){
							logger.info(logPre+"通知qunar成功");
						}else{
							logger.info(logPre+"通知qunar失败，errCode:"+rs.getErrCode() + "，errMsg:"+rs.getErrMsg());
						}
					}else{
						logger.info(logPre+"通知qunar失败返回："+jsonRs);
					}
					logger.info(logPre+" end~~~");
				}
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
