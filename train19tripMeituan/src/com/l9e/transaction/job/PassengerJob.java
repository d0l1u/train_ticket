package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.common.ElongConsts;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpPostJsonUtil;

/**
 * 代理商乘车人数据实时更新----增量
 **/
@Component("passengerJob")
public class PassengerJob {
	private static Logger logger=Logger.getLogger(PassengerJob.class);
	@Resource
	private OrderService orderService;
	/**
	 * 所有渠道的出票成功乘客信息实时更新
	 * */
	public void queryPassengers(){
		String url = "http://i.meituan.com/uts/train/agentpassenger/updaterealtime/106/19E";
		//前20分钟的时间
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(Calendar.MINUTE, -2); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String querydate = df.format(date);
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("begin_time", querydate.concat(":00"));
		param.put("end_time", querydate.concat(":59"));
		
		//证件类型（1  身份证）身份证号码  姓名  添加时间（如没有，可填0）
		List<Map<String, String>> passList = orderService.queryPassengerList(param);
		if(passList.size()>0){
			logger.info("[query 19e passengers] date="+querydate+", count="+passList.size());
			for(Map<String, String> map : passList){
				JSONObject json = new JSONObject();
				
				if("0".equals(map.get("contact_status"))){//联系人状态 0-已通过 1-待核验 2-未通过
					json.put("operationtypeid", "0");//操作类型 ID 0:新增，1:删除
					json.put("operationtypename", "新增");//操作类型名称
				}else{
					json.put("operationtypeid", "1");//操作类型 ID 0:新增，1:删除
					json.put("operationtypename", "删除");//操作类型名称
				}
				json.put("passportseno", map.get("cert_no"));//乘客证件号码
				json.put("passengersename", map.get("contact_name"));//乘客姓名
				json.put("passporttypeseid", map.get("cert_type"));//乘客证件类型 1：二代身份证
				json.put("passporttypeseidname", ElongConsts.getElongIdsTypeName(map.get("cert_type")));//证件类型名称
				json.put("passengertypeid", map.get("person_type"));//1:成人票，2:儿童票，3:学生票，4:残军票
				json.put("passengertypename", TrainConsts.getWhitePersonType().get(map.get("person_type")));
				json.put("operationtime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));//操作时间 格式：yyyyMMddHHmmss（非空）
//				logger.info(json.toString());
				String result = HttpPostJsonUtil.sendJsonPost(url, json.toString(), "utf-8");
				logger.info(json.get("operationtypename")+"--"+map.get("user_name")+"/"+map.get("cert_no")+"【增量接口返回result】"+result);
			}
		}
	}
	
}
