package com.l9e;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.entity.TrainConsts;
import com.l9e.entity.TrainNewData;
import com.l9e.entity.TrainNewMidData_new;
import com.l9e.entity.TrainReturnData_new;
import com.l9e.util.DateUtil;

public class TEST {
	private static Logger logger = Logger.getLogger(TEST.class);
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
    	String jsonStr ="";

	/*	String jsonStr = "{\r\n" + 
				"    \"validateMessagesShowId\": \"_validatorMessage\",\r\n" + 
				"    \"status\": true,\r\n" + 
				"    \"httpstatus\": 200,\r\n" + 
				"    \"data\": [\r\n" + 
				"        {\r\n" + 
				"            \"queryLeftNewDTO\": {\r\n" + 
				"                \"train_no\": \"5l000G150360\",\r\n" + 
				"                \"station_train_code\": \"G1503\",\r\n" + 
				"                \"start_station_telecode\": \"NKH\",\r\n" + 
				"                \"start_station_name\": \"南京南\",\r\n" + 
				"                \"end_station_telecode\": \"NFZ\",\r\n" + 
				"                \"end_station_name\": \"南宁东\",\r\n" + 
				"                \"from_station_telecode\": \"NXG\",\r\n" + 
				"                \"from_station_name\": \"南昌西\",\r\n" + 
				"                \"to_station_telecode\": \"UCZ\",\r\n" + 
				"                \"to_station_name\": \"来宾北\",\r\n" + 
				"                \"start_time\": \"13:08\",\r\n" + 
				"                \"arrive_time\": \"20:01\",\r\n" + 
				"                \"day_difference\": \"0\",\r\n" + 
				"                \"train_class_name\": \"\",\r\n" + 
				"                \"lishi\": \"06:53\",\r\n" + 
				"                \"canWebBuy\": \"Y\",\r\n" + 
				"                \"lishiValue\": \"413\",\r\n" + 
				"                \"yp_info\": \"uLnuUFmaDfz43eyiTSek56fQw75FmBIjfroJEtMwiA2IHoQz\",\r\n" + 
				"                \"control_train_day\": \"20981231\",\r\n" + 
				"                \"start_train_date\": \"20170426\",\r\n" + 
				"                \"seat_feature\": \"O3M393\",\r\n" + 
				"                \"yp_ex\": \"O0M090\",\r\n" + 
				"                \"train_seat_feature\": \"3\",\r\n" + 
				"                \"train_type_code\": \"2\",\r\n" + 
				"                \"start_province_code\": \"07\",\r\n" + 
				"                \"start_city_code\": \"0705\",\r\n" + 
				"                \"end_province_code\": \"17\",\r\n" + 
				"                \"end_city_code\": \"1607\",\r\n" + 
				"                \"seat_types\": \"OM9\",\r\n" + 
				"                \"location_code\": \"H2\",\r\n" + 
				"                \"from_station_no\": \"13\",\r\n" + 
				"                \"to_station_no\": \"26\",\r\n" + 
				"                \"control_day\": 29,\r\n" + 
				"                \"sale_time\": \"0930\",\r\n" + 
				"                \"is_support_card\": \"0\",\r\n" + 
				"                \"controlled_train_flag\": \"0\",\r\n" + 
				"                \"controlled_train_message\": \"正常车次，不受控\",\r\n" + 
				"                \"gg_num\": \"--\",\r\n" + 
				"                \"gr_num\": \"--\",\r\n" + 
				"                \"qt_num\": \"--\",\r\n" + 
				"                \"rw_num\": \"--\",\r\n" + 
				"                \"rz_num\": \"--\",\r\n" + 
				"                \"tz_num\": \"--\",\r\n" + 
				"                \"wz_num\": \"--\",\r\n" + 
				"                \"yb_num\": \"--\",\r\n" + 
				"                \"yw_num\": \"--\",\r\n" + 
				"                \"yz_num\": \"--\",\r\n" + 
				"                \"ze_num\": \"有\",\r\n" + 
				"                \"zy_num\": \"9\",\r\n" + 
				"                \"swz_num\": \"6\"\r\n" + 
				"            },\r\n" + 
				"            \"secretStr\": \"IT6j%%2BqZtXoum4ijye7rmStA60%%2FhIaiV2o3Jjsidp8QP363oPUbPAyPn6cxOhSKQDfgo%%2FDQYgi1Xx%%0AyfA1OBAgOXAAN2OmCk2%%2Bnnj%%2Bit%%2FeUqcEO%%2FVcT04vsctm%%2BV7o6myQEOqlqt8ShLY2wsSxWWODDKLX%%0Apf7jANFwaT3wqxC5XPr5l606TyWMzDHrUybUi7e1dLsPaG1MGnG2BcnWfvvJrXJiUFNSPo9Ko%%2Fp3%%0Ao%%2FY2R4qUURLRf0XkeP4DKcnPeef4ruxpLp0kcN5wIuvKEn%%2BhpdLfRhUy8Hv8vp7S%%2Bplnqr049DVV%%0AKe7n1acELog%%3D\",\r\n" + 
				"            \"buttonTextInfo\": \"预订\"\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"messages\": [],\r\n" + 
				"    \"validateMessages\": {}\r\n" + 
				"}";*/
		
		//解析字符串
		JSONObject json = JSONObject.fromObject(jsonStr);

		int httpstatus = json.getInt("httpstatus");
		logger.info("httpstatus:"+httpstatus);
		if(httpstatus != 200){
			logger.info("请求结果不为200");
			//TODO
		}
		
		boolean status = json.getBoolean("status");
		logger.info("status:"+status);
		if(status){
			logger.info("请求结果不为ture");
		}
		
		//获取data里的数据
		boolean bool  = false;
		JSONArray jsonArray = null;
		try {
			jsonArray = json.getJSONArray("data");
		} catch (Exception e) {
			bool = e.getMessage().contains("JSONObject[\"data\"] is not a JSONArray");
			if(bool){
				jsonArray = json.getJSONObject("data").getJSONArray("result");
			}
		}


		JSONObject result = new JSONObject();
		result.put("flag", "true");
		result.put("message", "");
		result.put("searchDate", "");
		//{"CWQ":"长沙南","CSQ":"长沙","BXP":"北京西","BJP":"北京"}
		Map<String, String> station_name =new HashMap<String, String>();
		if(jsonArray != null){
			if(bool){
				logger.info("new-----");
				JSONObject  mapObject= (JSONObject) json.getJSONObject("data").get("map");
				Iterator<String> sIterator = mapObject.keys();  
				while(sIterator.hasNext()){
				    // 获得key  
				    String key = sIterator.next();  
				    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可  
				    String value = mapObject.getString(key);  
				    station_name.put(key, value);
				} 
				
				System.out.println(mapObject);
				
				List<TrainNewData> list = parseArray(jsonArray,station_name);
				result.put("datas", list);
				
				 ObjectMapper objectMapper = new ObjectMapper();
				 String oMj = objectMapper.writeValueAsString(list);
				 logger.info("----->"+oMj);
				
			}else{
				logger.info("old-----");
				JSONObject json_old = new JSONObject();
				 JsonConfig config = new JsonConfig();
			       config.setJsonPropertyFilter(new PropertyFilter(){
			       public boolean apply(Object arg0, String arg1, Object arg2) {
			          if ((arg1.equals("controlled_train_flag")) || (arg1.equals("controlled_train_message")) 
			        		  ||(arg1.equals("train_type_code"))|| (arg1.equals("start_province_code"))||(arg1.equals("start_city_code"))
			        		  ||(arg1.equals("end_province_code"))|| (arg1.equals("end_city_code"))
			        		  ||(arg1.equals("START_ARRAY"))
			        		  ||(arg1.equals("seat_feature"))) {
			            return true;
			          }
			          return false;
			        }
			      });
			       
			    jsonStr = JSONObject.fromObject(jsonStr, config).toString();
			    ObjectMapper mapper = new ObjectMapper();
				TrainReturnData_new return_data = mapper.readValue(jsonStr, TrainReturnData_new.class);
				if("true".equals(return_data.getStatus()) && "200".equals(return_data.getHttpstatus())){
					json_old.put("flag", "true");
					json_old.put("message", "");
					json_old.put("searchDate", "");
					
					JSONArray array = new JSONArray();
					List<TrainNewMidData_new> list = return_data.getData();
					if(list != null && list.size() != 0){
						for (int i = 0; i < list.size(); i++) {
							TrainNewMidData_new train_data = list.get(i);
							jsonStr = JSONObject.fromObject(train_data).toString();
							jsonStr = JSONObject.fromObject(jsonStr, config).toString();
							TrainNewMidData_new readValue = mapper.readValue(jsonStr, TrainNewMidData_new.class);
							TrainNewData newDTO = readValue.getQueryLeftNewDTO();
							jsonStr = JSONObject.fromObject(newDTO).toString();
							jsonStr = jsonStr.replace("--", "-");
							array.add(jsonStr);
						}
					}else{
						jsonStr = TrainConsts.NO_DATAS;
					}
					
					json_old.put("datas", array);
					jsonStr = json_old.toString();
				}else{
					jsonStr = TrainConsts.ERROR;
				}
				
				logger.info("返回结果："+jsonStr);
				
				return;
				
				
			}
		}else{
			logger.info("error");
		}
		
		logger.info("返回结果："+result.toString());

	}


	public static String getStr(String source, String target) {
		return StringUtils.isBlank(source) ? target : source;
	}
	
	private  static List<TrainNewData> parseArray(JSONArray  arrayData, Map<String, String> station_name) {
		List<TrainNewData> array = new ArrayList<TrainNewData>();
		
		for (int i = 0; i < arrayData.size(); i++) {
			String data = arrayData.getString(i);
			
			
			
			String[] leftTiket =replace(data);
			
			logger.info(leftTiket.length);
			System.out.println(leftTiket[3]);
			
			for (int j = 0; j < leftTiket.length; j++) {
				System.out.println(j+"-->"+leftTiket[j]);
			}
			
			
			System.out.println(data);
			TrainNewData trainNewData = new TrainNewData();
			trainNewData.setTrain_no(leftTiket[2]);
			trainNewData.setStation_train_code(leftTiket[3]);
			trainNewData.setStart_station_telecode(leftTiket[4]);
			trainNewData.setEnd_station_telecode(leftTiket[5]);
			trainNewData.setFrom_station_telecode(leftTiket[6]);
			trainNewData.setTo_station_telecode(leftTiket[7]);
			trainNewData.setStart_time(leftTiket[8]);
			trainNewData.setArrive_time(leftTiket[9]);
			trainNewData.setLishi(leftTiket[10]);
			trainNewData.setLishiValue(DateUtil.dateTimeToMinute(leftTiket[10]));
			trainNewData.setDay_difference(DateUtil.dayDiffer(leftTiket[8], leftTiket[10], leftTiket[13]));
			trainNewData.setCanWebBuy(leftTiket[11]);
			trainNewData.setYp_info(leftTiket[12]);
			trainNewData.setControl_train_day(leftTiket[19]);
			trainNewData.setStart_train_date(leftTiket[13]);
			trainNewData.setSeat_feature(leftTiket[14]);
			trainNewData.setYp_ex(leftTiket[33]);
			// trainNewData.setTrain_seat_feature(leftTiket[14]);
			trainNewData.setSeat_types(leftTiket[34]);
			trainNewData.setLocation_code(leftTiket[15]);
			trainNewData.setFrom_station_no(leftTiket[16]);
			trainNewData.setTo_station_no(leftTiket[17]);
			trainNewData.setFrom_station_name(station_name.get(leftTiket[6]));
			trainNewData.setTo_station_name(station_name.get(leftTiket[7]));
			trainNewData.setStart_station_name(station_name.get(leftTiket[4]));
			trainNewData.setEnd_station_name(station_name.get(leftTiket[5]));
			trainNewData.setNote(leftTiket[1]);
			// trainNewData.setTo_station_no(leftTiket[17]);
			// trainNewData.setSale_time("");

			// trainNewData.setIs_support_card(leftTiket[18]);
			// trainNewData.setYz(leftTiket[29]);
			// trainNewData.setRz(leftTiket[24]);
			// trainNewData.setZe(leftTiket[30]);
			// trainNewData.setZy(leftTiket[31]);
			// trainNewData.setYws("");
			// trainNewData.setYwx("");
			// trainNewData.setYwz("");
			// trainNewData.setRws("");
			// trainNewData.setRwx("");
			// trainNewData.setGws("");
			// trainNewData.setGwx("");
			// trainNewData.setTdz(leftTiket[25]);
			// trainNewData.setSwz(leftTiket[32]);
			
			/*
						20-->
						21-->(高级软卧)
						22-->(其它)
						23-->(软卧)
						24-->(软座)
						25-->(特等座)  --暂无
						26-->(无座)
						27-->
						28-->(硬卧)
						29-->(硬座)
						30-->(二等座)
						31-->(一等座)
						32-->(商务座)
						33-->(动卧)
			 */
			

			trainNewData.setGg_num(getStr(leftTiket[20], "-"));
			trainNewData.setGr_num(getStr(leftTiket[21], "-")); //高级软卧票数
			trainNewData.setGw_num(getStr(leftTiket[21], "-"));
			trainNewData.setQt_num(getStr(leftTiket[22], "-")); //其它
			trainNewData.setRw_num(getStr(leftTiket[23], "-"));//软卧票数
			trainNewData.setRz_num(getStr(leftTiket[24], "-"));//软座票数
			trainNewData.setTdz_num(getStr(leftTiket[25], "-"));//特等座票数
			trainNewData.setTz_num(getStr(leftTiket[25], "-"));
			trainNewData.setWz_num(getStr(leftTiket[26], "-")); //无座
			trainNewData.setYw_num(getStr(leftTiket[28], "-")); //硬卧票数
			trainNewData.setYz_num(getStr(leftTiket[29], "-")); //硬座票数
			trainNewData.setZe_num(getStr(leftTiket[30], "-")); //二等座票数
			trainNewData.setZy_num(getStr(leftTiket[31], "-")); //一等座票数
			trainNewData.setSwz_num(getStr(leftTiket[32], "-")); //商务座票数
			trainNewData.setDw_num(getStr(leftTiket[33], "-")); //动卧票数
		
			array.add(trainNewData);
		}

		return array;
	}
	
	
	public static   String[] replace(String  str) {
		
		//String str = "|列车运行图调整,暂停发售|550000T11061|T110|SHH|BJP|SHH|SZH|24:00|24:00|99:59|IS_TIME_NOT_BUY||20170501||H3|01|02|0|1|||||||||||||||";
		str = "#"+str;
		str = str + "#";
		str = str.replaceAll("\\|\\|", "|#|");
		str = str.replaceAll("\\|\\|", "|#|");
		String[] split = str.split("[\\|]{1}");
		String[]  array= new String[split.length];
		
		for (int i = 0; i < split.length; i++) {
			array[i]=split[i].replace("#", "");
		}
		return array;
		
	}
	
	
}
