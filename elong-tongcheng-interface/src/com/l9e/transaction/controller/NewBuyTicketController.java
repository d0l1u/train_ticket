package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.common.ExternalBase;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.Outer12306NewData;
import com.l9e.transaction.vo.OuterSoukdNewData;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 车票预订
 * 
 * @author zuoyuxing
 *
 */
@Component
public class NewBuyTicketController extends ExternalBase {
	@Resource
	private QueryTicketService ticketService;

	@Resource
	private SoukdBuyTicketController soukd;
	
	@Value("#{propertiesReader[query_left_ticket_url]}")
	protected String query_left_ticket_url;// 查询中枢

	/**
	 * 根据车站查询
	 * 
	 * @param paramMap
	 * @param response
	 */
	public void newQueryData(Map<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) {
		String travel_time = paramMap.get("travel_time");
		ObjectMapper mapper = new ObjectMapper();
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		StringBuffer param = new StringBuffer();
		param.append("travel_time=").append(travel_time).append("&from_station=").append(paramMap.get("from_station")).append("&arrive_station=")
				.append(paramMap.get("arrive_station")).append("&channel=").append("elong");
		try {
			String jsonStr = HttpUtil.sendByPost(query_left_ticket_url, param.toString(), "utf-8");// 调用接口
			if ("error".equals(jsonStr)) {
				logger.info("查询机器人异常");
				printJson(response, getJson("001").toString());
				// printJson(response, "null");
				// soukd.soukdQueryData(paramMap, request, response);
				return;
			} else if (jsonStr.contains("\"code\":\"000\"")) {
				osnd = mapper.readValue(jsonStr, OuterSoukdNewData.class);
			} else {
				logger.info("查询返回数据失败，暂无列车信息");
				printJson(response, getJson("106").toString());
				return;
			}
			// Outer12306NewData outer12306NewData = mapper.readValue(jsonStr,
			// Outer12306NewData.class);
			// osnd = outer12306NewData.getErrorInfo().get(0);
			// if(osnd != null){
			// //统计列车列数
			// if(osnd.getDatajson() == null || osnd.getDatajson().size()==0){
			// String erroInfo = "";
			// if(!StringUtils.isEmpty(osnd.getErrInfo())){
			// erroInfo = osnd.getErrInfo();
			// }
			// logger.info("[火车票查询"
			// +paramMap.get("from_station")+"/"+paramMap.get("arrive_station")
			// +"("+paramMap.get("travel_time")+")的列车共计0列"+erroInfo);
			// }else{
			// logger.info("[火车票查询"
			// +paramMap.get("from_station")+"/"+paramMap.get("arrive_station")
			// +"("+paramMap.get("travel_time")+")的列车共计"+osnd.getDatajson().size()+"列");
			// if(!"000".equals(osnd.getCode())){
			//// printJson(response, "null");
			// printJson(response, getJson(osnd.getCode()).toString());
			// return;
			// }else{
			// if(osnd.getDatajson()!=null && osnd.getDatajson().size()>0){
			// trainInfoAppendPrice(paramMap,request,osnd);
			// }
			// }
			// }
			// }else{
			// //查询异常
			//// printJson(response, "null");
			// printJson(response, getJson("001").toString());
			// return;
			// }
		} catch (Exception e) {
			logger.error("查询余票异常！", e);
			printJson(response, getJson("001").toString());
			// printJson(response, "null");
			return;
		}
		JSONObject jsonObject = new JSONObject();
		// 追加时间限制
		limitTimeTicket(travel_time, paramMap.get("stop_buy_time"), osnd);
		if (osnd != null && osnd.getDatajson() != null && osnd.getDatajson().size() > 0) {
			formatStandardData(jsonObject, osnd, paramMap);
		}
		if (!StringUtils.isEmpty(paramMap.get("train_code"))) {// 实时余票验证
			JSONObject json = new JSONObject();
			JSONArray jsonArray = (JSONArray) jsonObject.get("train_data");
			for (int i = 0; i < jsonArray.size(); i++) {
				if (paramMap.get("train_code").equals(jsonArray.getJSONObject(i).get("train_code"))) {
					json = jsonArray.getJSONObject(i);
					json.put("return_code", jsonObject.get("return_code"));
					json.put("message", jsonObject.get("message"));
					json.remove("yz");
					json.remove("wz");
					json.remove("yws");
					json.remove("ywx");
					json.remove("ywz");
					json.remove("rws");
					json.remove("rwx");
					json.remove("tdz");
					json.remove("swz");
					json.remove("rz");
					json.remove("rz1");
					json.remove("rz2");
					json.remove("gws");
					json.remove("gwx");
					break;
				}
			}
			osnd = null;
			if (json.isEmpty()) {
				json.put("return_code", "107");
				json.put("message", "车次信息有误，暂无该列车信息！");
			}
			printJson(response, json.toString());
			// if("000".equals(json.get("retunr_code"))){
			// printJson(response,json.toString());
			// }else{
			// printJson(response, "null");
			// }
		} else {
			osnd = null;
			printJson(response, jsonObject.toString());
			// if("000".equals(jsonObject.get("retunr_code"))){
			// printJson(response,jsonObject.toString());
			// }else{
			// printJson(response, "null");
			// }
		}
	}

	public void formatStandardData(JSONObject json, OuterSoukdNewData osnd, Map<String, String> paramMap) {
		json.put("return_code", osnd.getCode());
		json.put("message", osnd.getErrInfo());
		JSONArray jsonArr = new JSONArray();
		for (TrainNewData train : osnd.getDatajson()) {
			if ("Y".equals(paramMap.get("only_gd"))) {
				String regEx = "G|D|C"; // 表示高铁或动车
				Pattern pat = Pattern.compile(regEx);
				Matcher mat = pat.matcher(train.getStation_train_code());
				if (!mat.find()) {
					continue;
				}
			}
			String spare_amount = paramMap.get("spare_ticket_amount");
			JSONObject jsonData = new JSONObject();
			jsonData.put("train_code", train.getStation_train_code());
			jsonData.put("start_station", train.getStart_station_name());
			jsonData.put("end_station", train.getEnd_station_name());
			jsonData.put("start_time", "");
			jsonData.put("end_time", "");
			jsonData.put("from_time", train.getStart_time());
			jsonData.put("arrive_time", train.getArrive_time());
			jsonData.put("from_station", train.getFrom_station_name());
			jsonData.put("arrive_station", train.getTo_station_name());
			jsonData.put("cost_time", train.getLishiValue());
			jsonData.put("wz", ("-".equals(train.getYz()) || "".equals(train.getYz())) ? train.getZe() : train.getYz());
			jsonData.put("yz", train.getYz());
			jsonData.put("wz_num", replaceNumVal(train.getWz_num(), spare_amount));
			jsonData.put("yz_num", replaceNumVal(train.getYz_num(), spare_amount));
			jsonData.put("rz", train.getRz());
			jsonData.put("rz_num", replaceNumVal(train.getRz_num(), spare_amount));
			jsonData.put("rz1", train.getZy());
			jsonData.put("rz1_num", replaceNumVal(train.getZy_num(), spare_amount));
			jsonData.put("rz2", train.getZe());
			jsonData.put("rz2_num", replaceNumVal(train.getZe_num(), spare_amount));
			jsonData.put("yws", train.getYws());
			jsonData.put("ywz", train.getYwz());
			jsonData.put("ywx", train.getYwx());
			jsonData.put("yw_num", replaceNumVal(train.getYw_num(), spare_amount));
			jsonData.put("rws", train.getRws());
			jsonData.put("rwx", train.getRwx());
			jsonData.put("rw_num", replaceNumVal(train.getRw_num(), spare_amount));
			jsonData.put("swz", train.getSwz());
			jsonData.put("swz_num", train.getSwz_num());
			jsonData.put("tdz", train.getTdz());
			jsonData.put("tdz_num", train.getTz_num());
			jsonData.put("gws", train.getGws());
			jsonData.put("gwx", train.getGwx());
			jsonData.put("gw_num", train.getGr_num());
			jsonArr.add(jsonData);
		}
		json.put("train_data", jsonArr.toString());
	}

	/**
	 * 拼接车次信息票价
	 * 
	 */
	public void trainInfoAppendPrice(Map<String, String> paramMap, HttpServletRequest request, OuterSoukdNewData osnd) {
		List<TrainNewDataFake> list = new ArrayList<TrainNewDataFake>();
		if (osnd != null && osnd.getDatajson() != null && osnd.getDatajson().size() > 0) {
			List<TrainNewData> new_list = new ArrayList<TrainNewData>();
			list = ticketService.queryProperTrainNewData(paramMap);
			TrainNewDataFake tndf = null;
			boolean exist = false;
			for (TrainNewData trainNewData : osnd.getDatajson()) {
				trainNewData.initPrice();
				for (int i = 0; i < list.size(); i++) {
					String[] arrCc = list.get(i).getCc().split("/");
					String trainCode = trainNewData.getStation_train_code();
					int len = arrCc.length;
					for (int m = 0; m < len; m++) {
						if (arrCc[m].equals(trainCode)) {
							if (list.get(i).getFz().equals(trainNewData.getFrom_station_name())
									&& list.get(i).getDz().equals(trainNewData.getTo_station_name())) {
								tndf = list.get(i);
								if (!"0".equals(tndf.getYz())) {
									trainNewData.setYz(tndf.getYz());
									exist = true;
								}
								if (!"0".equals(tndf.getRz())) {
									trainNewData.setRz(tndf.getRz());
									exist = true;
								}
								if (!"0".equals(tndf.getYws())) {
									trainNewData.setYws(tndf.getYws());
									exist = true;
								}
								if (!"0".equals(tndf.getYwz())) {
									trainNewData.setYwz(tndf.getYwz());
									exist = true;
								}
								if (!"0".equals(tndf.getYwx())) {
									trainNewData.setYwx(tndf.getYwx());
									exist = true;
								}
								if (!"0".equals(tndf.getRws())) {
									trainNewData.setRws(tndf.getRws());
									exist = true;
								}
								if (!"0".equals(tndf.getRwx())) {
									trainNewData.setRwx(tndf.getRwx());
									exist = true;
								}
								if (!"0".equals(tndf.getRz1())) {
									trainNewData.setZy(tndf.getRz1());
									exist = true;
								}
								if (!"0".equals(tndf.getRz2())) {
									trainNewData.setZe(tndf.getRz2());
									exist = true;
								}
								if (exist) {
									new_list.add(trainNewData);
								}
							}
						}
					}
					if (exist) {
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
	 * 
	 * @param paramMap
	 * @param response
	 */
	public void limitTimeTicket(String travel_time, String limit_time, OuterSoukdNewData osnd) {
		String num_show = "-";
		String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		// limit_time小时之内的票不能订购
		if (travel_time.equals(nowDate)) {
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			if (osnd != null) {
				for (TrainNewData trainNewData : osnd.getDatajson()) {
					int beginTime = Integer.parseInt(trainNewData.getStart_time().replaceAll(":", ""));
					if (beginTime - currentTime < Integer.valueOf(limit_time) * 100) {
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
		if (travel_time.equals(DateUtil.dateAddDays(nowDate, "1"))) {
			if ("23".compareTo((DateUtil.dateToString(new Date(), "HH"))) <= 0) {
				if (osnd != null) {
					for (TrainNewData trainNewData : osnd.getDatajson()) {
						if (trainNewData.getStart_time().compareTo("09:00") < 0) {
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
	}

	/**
	 * 余票查询新接口
	 * 
	 * @param paramMap
	 * @rerurn OuterSoukdNewData
	 */
	public OuterSoukdNewData getNewDataInterface(Map<String, String> paramMap) {
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		String url = null;
		try {
			long start = System.currentTimeMillis();
			url = get12306Url(paramMap, this.getSysInterface12306Url("interface_12306_new_url"));
			// 请求超时时间
			String con_timeout = this.queryElongSysValueByName("con_timeout");
			String read_timeout = this.queryElongSysValueByName("read_timeout");
			String jsonStr = HttpUtil.sendByGet(url, "UTF-8", con_timeout, read_timeout);// 调用接口

			if (jsonStr == null) {
				osnd.setCode("001");
				osnd.setErrInfo("系统错误，查询超时。");
			}
			jsonStr = jsonStr.replace("\\", "").replace("[\"{", "[{").replace("}\"]", "}]").replace("\"[", "[").replace("]\"", "]").replace("--", "-");
			ObjectMapper mapper = new ObjectMapper();
			Outer12306NewData outer12306NewData = mapper.readValue(jsonStr, Outer12306NewData.class);
			osnd = outer12306NewData.getErrorInfo().get(0);
			if (osnd != null) {
				logger.info("<火车票查询>调用12306机器人新接口成功查询" + paramMap.get("from_station") + "/" + paramMap.get("arrive_station") + "(" + paramMap.get("travel_time")
						+ ")的列车信息，耗时" + (System.currentTimeMillis() - start) + "ms");

				// 统计列车列数
				if (osnd.getDatajson() == null || osnd.getDatajson().size() == 0) {
					String erroInfo = "";
					if (!StringUtils.isEmpty(osnd.getErrInfo())) {
						erroInfo = osnd.getErrInfo();
					}
					logger.info("[火车票查询" + paramMap.get("from_station") + "/" + paramMap.get("arrive_station") + "(" + paramMap.get("travel_time") + ")的列车共计0列"
							+ erroInfo);
				} else {
					logger.info("[火车票查询" + paramMap.get("from_station") + "/" + paramMap.get("arrive_station") + "(" + paramMap.get("travel_time") + ")的列车共计"
							+ osnd.getDatajson().size() + "列");
				}
			}
		} catch (Exception e) {// 没有查询到数据
			logger.error("解析12306机器人返回数据异常", e);
			osnd.setCode("001");
			osnd.setErrInfo("系统错误，未知服务异常。");
		}
		return osnd;
	}

	// 网站终止预订功能
	// 进行车次查询
	public void unableBookTicketsQuery(Map<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) {
		List<TrainNewDataFakeAppendTrain> list = ticketService.queryAppendTrainNewData(paramMap);
		for (TrainNewDataFakeAppendTrain appTrain : list) {
			appTrain.changeData();
		}
		unableStandardData(list, response, paramMap.get("only_gd"), paramMap.get("terminal"));
	}

	public void unableStandardData(List<TrainNewDataFakeAppendTrain> list, HttpServletResponse response, String only_gd, String terminal) {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		for (TrainNewDataFakeAppendTrain train : list) {
			if ("Y".equals(only_gd)) {
				String regEx = "G|D|C"; // 表示高铁或动车
				Pattern pat = Pattern.compile(regEx);
				Matcher mat = pat.matcher(train.getCc());
				if (!mat.find()) {
					continue;
				}
			}
			JSONObject jsonData = new JSONObject();
			jsonData.put("train_code", train.getCc());
			jsonData.put("start_time", "");
			jsonData.put("end_time", "");
			jsonData.put("start_station", train.getStart_station_name());
			jsonData.put("end_station", train.getEnd_station_name());
			jsonData.put("from_time", train.getStart_time());
			jsonData.put("arrive_time", train.getArrive_time());
			jsonData.put("from_station", train.getFz());
			jsonData.put("arrive_station", train.getDz());
			jsonData.put("cost_time", train.getLishi());
			jsonData.put("wz", ("-".equals(train.getYz()) || "".equals(train.getYz())) ? train.getRz2() : train.getYz());
			jsonData.put("yz", train.getYz());
			jsonData.put("wz_num", "-");
			jsonData.put("yz_num", "-");
			jsonData.put("rz", train.getRz());
			jsonData.put("rz_num", "-");
			jsonData.put("rz1", train.getRz1());
			jsonData.put("rz1_num", "-");
			jsonData.put("rz2", train.getRz2());
			jsonData.put("rz2_num", "-");
			jsonData.put("yws", train.getYws());
			jsonData.put("ywz", train.getYwz());
			jsonData.put("ywx", train.getYwx());
			jsonData.put("yw_num", "-");
			jsonData.put("rws", train.getRws());
			jsonData.put("rwx", train.getRwx());
			jsonData.put("rw_num", "-");
			jsonData.put("swz", "-");
			jsonData.put("swz_num", "-");
			jsonData.put("tdz", "-");
			jsonData.put("tdz_num", "-");
			jsonData.put("gws", "-");
			jsonData.put("gwx", "-");
			jsonData.put("gw_num", "-");
			if ("APP".equals(terminal)) {
				jsonData.put("total_num", "无");
			}
			jsonArr.add(jsonData);
		}
		jsonObject.put("return_code", "000");
		jsonObject.put("message", "");
		jsonObject.put("train_data", jsonArr.toString());
		printJson(response, jsonObject.toString());
		return;
	}
}
