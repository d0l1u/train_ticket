package com.l9e.util;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.common.TrainConsts;
import com.l9e.entity.TrainNewMidData;
import com.l9e.entity.TrainReturnData;

public class TestQuery {
	public static String queryTest(String map){
		String url = "https://kyfw.12306.cn/otn/lcxxcx/query?purpose_codes=ADULT";
		String jsonStr = HttpsUtil.sendHttps(url+"&"+map);
		try{
			
			ObjectMapper mapper = new ObjectMapper();
			TrainReturnData return_data = mapper.readValue(jsonStr.toString(), TrainReturnData.class);
			if("true".equals(return_data.getStatus()) && "200".equals(return_data.getHttpstatus())){
				TrainNewMidData train_data =  return_data.getData();
				if("true".equals(train_data.getFlag())){
					jsonStr = JSONObject.fromObject(train_data).toString();
					jsonStr = jsonStr.replace("--", "-");
				}else{
					jsonStr = TrainConsts.NO_DATAS;
				}
			}else{
				jsonStr = TrainConsts.ERROR;
			}
		}catch(Exception e){
			e.printStackTrace();
			jsonStr = null;
		}
		return jsonStr;
	} 
}
