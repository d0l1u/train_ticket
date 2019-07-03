package com.l9e.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.jiexun.iface.util.StringUtil;
import com.l9e.entity.TrainConsts;
import com.l9e.entity.TrainNewData;
import com.l9e.entity.TrainNewMidData;
import com.l9e.entity.TrainNewMidData_new;
import com.l9e.entity.TrainReturnData;
import com.l9e.entity.TrainReturnData_new;
import com.l9e.util.ConfigUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpsQueyUtil;
import com.l9e.util.HttpsUtil;
import com.l9e.util.StrUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

@SuppressWarnings("serial")
public class QueryTicketServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(QueryTicketServlet.class);
	SSLContext sc = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public static void main(String[] args) throws ServletException, IOException {
		QueryTicketServlet q = new QueryTicketServlet();
		// q.doPost(null, null);

		String url = q.getQueryUrl();
		System.out.println(url);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		String url_yupaio = "https://kyfw.12306.cn/otn/lcxxcx/query?purpose_codes=ADULT";
		String url_checi = "https://kyfw.12306.cn/otn/leftTicket/query";
		// String url_checi="https://kyfw.12306.cn/otn/leftTicket/queryZ?";
		// String url_checi="https://kyfw.12306.cn/otn/leftTicket/queryT?";
		// String url_checi="https://kyfw.12306.cn/otn/leftTicket/queryX?";
		// String url_checi="https://kyfw.12306.cn/otn/leftTicket/query?";
		String url = "https://kyfw.12306.cn/otn/leftTicket/query";

		req.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String travel_time = req.getParameter("travel_time");
		String from_station = req.getParameter("from_station");
		String arrive_station = req.getParameter("arrive_station");
		String url_ = req.getParameter("url"); // 查询地址的后缀
		String query_type = req.getParameter("query_type"); // 那种查询类型
		String purpose_codes = req.getParameter("purpose_codes");

		// String travel_time = "2017-03-08";
		// String from_station = "BTC";
		// String arrive_station = "BDC";
		// String url_ = "A";
		// String query_type = "02";
		// String purpose_codes = "ADULT";

		logger.info("url_::" + url_);

		if ("NO".equals(url_)) { // 直接从12306获取地址
			String str = this.getQueryUrl();
			if (StringUtil.isEmpty(str)) {
				url_checi = url + "?";
			} else {
				url_checi = str;
			}

		} else {
			url_checi = url + url_ + "?";
		}

		logger.info("url_checi::" + url_checi);

		if (null == query_type || "".equals(query_type)) {
			query_type = "02"; // 默认车次查询按钮
		}

		logger.info("余票查询类型：" + query_type); // query_type = 01(余票查询按钮) ,
												// 02（车次查询按钮)

		Map<String, String> param = new HashMap<String, String>();
		param.put("travel_time", travel_time);
		param.put("from_station", from_station);
		param.put("arrive_station", arrive_station);
		param.put("url_checi", url_checi);
		param.put("query_type", query_type);
		param.put("purpose_codes", purpose_codes);

		if ("01".equals(query_type)) {
			query_yupiao(response, param, url_yupaio);
		} else if ("02".equals(query_type)) {
			query_checi(response, param, url_checi);
		} else {
			logger.info("java查询余票异常，" + query_type + "query_type值不匹配！");
			write2Response(response, TrainConsts.ERROR);
		}

	}

	/**
	 * @return
	 */
	private String getUrl() {

		String result = HttpsUtil
				.sendHttps("https://kyfw.12306.cn/otn/leftTicket/query");
		String path = "";
		logger.info(result);
		try {
			String regex = "\"c_url\":\"([^\"]+)\"";
			Pattern pat = Pattern.compile(regex);
			Matcher mat = pat.matcher(result.replaceAll("\\s", ""));
			if (mat.find()) {
				path = mat.group(1);

			}
		} catch (Exception e) {
			logger.info("获取查询路径异常", e);
		}

		if (StringUtils.isNotBlank(path)) {
			return "https://kyfw.12306.cn/otn/" + path;
		}
		return "";
	}

	/**
	 * @param response
	 * @param travel_time
	 * @param from_station
	 * @param arrive_station
	 *            点击查询余票按钮，返回结果。
	 */
	public void query_yupiao(HttpServletResponse response,
			Map<String, String> param, String url_yupaio) {

		try {
			StringBuffer query_url = new StringBuffer();
			String params = this.dealUrlParams(param);

			query_url.append(url_yupaio).append(params);
			logger.info(query_url.toString());

			String jsonStr = HttpsUtil.sendHttps(query_url.toString());
			logger.info("query_yupiao---->" + jsonStr);

			JsonConfig config = new JsonConfig();

			config.setJsonPropertyFilter(new PropertyFilter() {
				public boolean apply(Object arg0, String arg1, Object arg2) {
					if ((arg1.equals("controlled_train_flag"))
							|| (arg1.equals("controlled_train_message"))
							|| (arg1.equals("train_type_code"))
							|| (arg1.equals("start_province_code"))
							|| (arg1.equals("start_city_code"))
							|| (arg1.equals("end_province_code"))
							|| (arg1.equals("end_city_code"))
							|| (arg1.equals("START_ARRAY"))
							|| (arg1.equals("seat_feature"))) {
						return true;
					}
					return false;
				}
			});

			jsonStr = JSONObject.fromObject(jsonStr, config).toString();

			ObjectMapper mapper = new ObjectMapper();
			TrainReturnData return_data = mapper.readValue(jsonStr.toString(),
					TrainReturnData.class);
			if ("true".equals(return_data.getStatus())
					&& "200".equals(return_data.getHttpstatus())) {
				TrainNewMidData train_data = return_data.getData();
				if ("true".equals(train_data.getFlag())) {
					jsonStr = JSONObject.fromObject(train_data).toString();
					jsonStr = jsonStr.replace("--", "-");
				} else {
					jsonStr = TrainConsts.NO_DATAS;
				}
			} else {
				jsonStr = TrainConsts.ERROR;
			}
			logger.info("返回结果：" + jsonStr);
			write2Response(response, jsonStr);
		} catch (Exception e) {
			logger.error("java查询余票异常", e);
			write2Response(response, TrainConsts.ERROR);
		}
	}

	/**
	 * @param response
	 * @param travel_time
	 * @param from_station
	 * @param arrive_station
	 *            通过车次查询按钮，查询12306的余票
	 * 
	 */
	public void query_checi(HttpServletResponse response,
			Map<String, String> param, String url_checi) {
		try {
			StringBuffer query_url = new StringBuffer();
			String params = this.dealUrlParams(param);
			query_url.append(url_checi).append(params);
			logger.info(query_url.toString());
			// String jsonStr = HttpsUtil.sendHttps(query_url.toString());
			logger.info("cookieStr--->" + ConfigUtil.getValue("cookieStr"));// 取一个固定的cookies值
			Map<String, String> result_data = HttpsQueyUtil.sendHttpsGET(
					query_url.toString(), ConfigUtil.getValue("cookieStr"));
			String jsonStr = result_data.get("result");
			logger.info("query_checi---->" + jsonStr);
			// 解析字符串
			JSONObject json = JSONObject.fromObject(jsonStr);
			int httpstatus = json.getInt("httpstatus");
			logger.info("httpstatus:" + httpstatus);
			if (httpstatus != 200) {
				throw new RuntimeException("请求结果httpstatus不为200");
			}
			boolean status = json.getBoolean("status");
			logger.info("status:" + status);
			if (!status) {
				throw new RuntimeException("请求结果status不为ture");
			}
			// 获取data里的数据
			boolean bool = false;
			JSONArray jsonArray = null;
			try {
				jsonArray = json.getJSONArray("data");
			} catch (Exception e) {
				bool = e.getMessage().contains(
						"JSONObject[\"data\"] is not a JSONArray");
				if (bool) {
					jsonArray = json.getJSONObject("data").getJSONArray(
							"result");
				}
			}
			JSONObject result = new JSONObject();
			// {"CWQ":"长沙南","CSQ":"长沙","BXP":"北京西","BJP":"北京"}
			Map<String, String> station_name = new HashMap<String, String>();
			result.put("flag", "true");
			result.put("message", "");
			result.put("searchDate", "");
			if (jsonArray != null && !jsonArray.isEmpty()) {
				if (bool) {
					logger.info("new-----******");
					JSONObject mapObject = (JSONObject) json.getJSONObject(
							"data").get("map");
					Iterator<String> sIterator = mapObject.keys();
					while (sIterator.hasNext()) {
						// 获得key
						String key = sIterator.next();
						// 根据key获得value,
						// value也可以是JSONObject,JSONArray,使用对应的参数接收即可
						String value = mapObject.getString(key);
						station_name.put(key, value);
					}
					List<TrainNewData> list = parseArray(jsonArray,
							station_name);
					result.put("datas", list);

				} else {
					// 老版查询
					queryOld(response, jsonStr, json);
					return;
				}
			} else {
				throw new RuntimeException("车次列表为空");
			}

			String resultStr = result.toString();
			logger.info("返回结果：" + resultStr);
			write2Response(response, resultStr);
		} catch (Exception e) {
			logger.info("java查询余票异常:" + e.getMessage(), e);
			if ("车次列表为空".equals(e.getMessage())) {
				write2Response(response, TrainConsts.NO_DATAS);
			} else {
				write2Response(response, TrainConsts.ERROR);
			}

		}

	}

	/**
	 * @param response
	 * @param jsonStr
	 * @param json
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	public void queryOld(HttpServletResponse response, String jsonStr,
			JSONObject json) throws IOException, JsonParseException,
			JsonMappingException {
		logger.info("old-------------------------------------------------------");
		JsonConfig config = new JsonConfig();
		config.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object arg0, String arg1, Object arg2) {
				if ((arg1.equals("controlled_train_flag"))
						|| (arg1.equals("controlled_train_message"))
						|| (arg1.equals("train_type_code"))
						|| (arg1.equals("start_province_code"))
						|| (arg1.equals("start_city_code"))
						|| (arg1.equals("end_province_code"))
						|| (arg1.equals("end_city_code"))
						|| (arg1.equals("START_ARRAY"))
						|| (arg1.equals("seat_feature"))) {
					return true;
				}
				return false;
			}
		});

		jsonStr = JSONObject.fromObject(jsonStr, config).toString();
		ObjectMapper mapper = new ObjectMapper();
		TrainReturnData_new return_data = mapper.readValue(jsonStr,
				TrainReturnData_new.class);
		if ("true".equals(return_data.getStatus())
				&& "200".equals(return_data.getHttpstatus())) {
			json.put("flag", "true");
			json.put("message", "");
			json.put("searchDate", "");

			JSONArray array = new JSONArray();
			List<TrainNewMidData_new> list = return_data.getData();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					TrainNewMidData_new train_data = list.get(i);
					jsonStr = JSONObject.fromObject(train_data).toString();
					jsonStr = JSONObject.fromObject(jsonStr, config).toString();
					TrainNewMidData_new readValue = mapper.readValue(jsonStr,
							TrainNewMidData_new.class);
					TrainNewData newDTO = readValue.getQueryLeftNewDTO();
					jsonStr = JSONObject.fromObject(newDTO).toString();
					jsonStr = jsonStr.replace("--", "-");
					array.add(jsonStr);
				}
			} else {
				jsonStr = TrainConsts.NO_DATAS;
			}

			json.put("datas", array);
			jsonStr = json.toString();
		} else {
			jsonStr = TrainConsts.ERROR;
		}

		logger.info("返回结果：" + jsonStr);
		write2Response(response, jsonStr);
		return;
	}

	public static String[] replace(String str) {

		// String str =
		// "|列车运行图调整,暂停发售|550000T11061|T110|SHH|BJP|SHH|SZH|24:00|24:00|99:59|IS_TIME_NOT_BUY||20170501||H3|01|02|0|1|||||||||||||||";
		str = "#" + str;
		str = str + "#";
		str = str.replaceAll("\\|\\|", "|#|");
		str = str.replaceAll("\\|\\|", "|#|");
		String[] split = str.split("[\\|]{1}");
		String[] array = new String[split.length];

		for (int i = 0; i < split.length; i++) {
			array[i] = split[i].replace("#", "");
		}
		return array;

	}

	public List<TrainNewData> parseArray(JSONArray arrayData,
			Map<String, String> station_name) {
		List<TrainNewData> array = new ArrayList<TrainNewData>();
		for (int i = 0; i < arrayData.size(); i++) {
			String data = arrayData.getString(i);

			String[] leftTiket = replace(data);
			logger.info(leftTiket.length);

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
			trainNewData
					.setLishiValue(DateUtil.dateTimeToMinute(leftTiket[10]));
			trainNewData.setDay_difference(DateUtil.dayDiffer(leftTiket[8],
					leftTiket[10], leftTiket[13]));
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

			/*
			 * 20--> 21-->(高级软卧) 22-->(其它) 23-->(软卧) 24-->(软座) 25-->(特等座)
			 * 26-->(无座) 27--> 28-->(硬卧) 29-->(硬座) 30-->(二等座) 31-->(一等座)
			 * 32-->(商务座) 33-->(动卧)
			 */

			trainNewData.setGg_num(getStr(leftTiket[20], "-"));

			trainNewData.setGr_num(getStr(leftTiket[21], "-")); // 高级软卧票数
			trainNewData.setGw_num(getStr(leftTiket[21], "-"));

			trainNewData.setQt_num(getStr(leftTiket[22], "-")); // 其它
			trainNewData.setRw_num(getStr(leftTiket[23], "-"));// 软卧票数
			trainNewData.setRz_num(getStr(leftTiket[24], "-"));// 软座票数

			trainNewData.setTdz_num(getStr(leftTiket[25], "-"));// 特等座票数
			trainNewData.setTz_num(getStr(leftTiket[25], "-"));

			trainNewData.setWz_num(getStr(leftTiket[26], "-")); // 无座

			trainNewData.setYw_num(getStr(leftTiket[28], "-")); // 硬卧票数
			trainNewData.setYz_num(getStr(leftTiket[29], "-")); // 硬座票数
			trainNewData.setZe_num(getStr(leftTiket[30], "-")); // 二等座票数
			trainNewData.setZy_num(getStr(leftTiket[31], "-")); // 一等座票数
			trainNewData.setSwz_num(getStr(leftTiket[32], "-")); // 商务座票数
			trainNewData.setDw_num(getStr(leftTiket[33], "-")); // 动卧票数

			array.add(trainNewData);
		}

		return array;
	}

	public String getStr(String source, String target) {
		return StringUtils.isBlank(source) ? target : source;
	}

	/**
	 * @param param
	 * @param url_type
	 *            ,余票查询url,车次查询url
	 * @return
	 */
	public String dealUrlParams(Map<String, String> param) {

		String purpose_codes = param.get("purpose_codes");
		String query_type = param.get("query_type");
		StringBuffer paramStringBuffer = new StringBuffer();

		if ("01".equals(query_type)) { // 余票查询按钮参数组合

			if (null == purpose_codes || "".equals(purpose_codes)
					|| "ADULT".equals(purpose_codes)) {
				logger.info("默认普通查询:" + purpose_codes);
				paramStringBuffer.append("queryDate=")
						.append(param.get("travel_time"))
						.append("&from_station=")
						.append(param.get("from_station"))
						.append("&to_station=")
						.append(param.get("arrive_station"));
			} else if ("0X00".equals(purpose_codes)) {
				logger.info("学生查询:" + purpose_codes);
				paramStringBuffer.append("&queryDate=")
						.append(param.get("travel_time"))
						.append("&from_station=")
						.append(param.get("from_station"))
						.append("&to_station=")
						.append(param.get("arrive_station"));
			} else {
				logger.info("没有对应的purpose_codes::" + purpose_codes);
			}
		}

		if ("02".equals(query_type)) { // 车次查询按钮参数组合

			if (null == purpose_codes || "".equals(purpose_codes)
					|| "ADULT".equals(purpose_codes)) {
				logger.info("默认普通查询:" + purpose_codes);
				paramStringBuffer.append("leftTicketDTO.train_date=")
						.append(param.get("travel_time"))
						.append("&leftTicketDTO.from_station=")
						.append(param.get("from_station"))
						.append("&leftTicketDTO.to_station=")
						.append(param.get("arrive_station"))
						.append("&purpose_codes=ADULT");
			} else if ("0X00".equals(purpose_codes)) {
				logger.info("学生查询:" + purpose_codes);
				paramStringBuffer.append("leftTicketDTO.train_date=")
						.append(param.get("travel_time"))
						.append("&leftTicketDTO.from_station=")
						.append(param.get("from_station"))
						.append("&leftTicketDTO.to_station=")
						.append(param.get("arrive_station"))
						.append("&purpose_codes=0X00");
			} else {
				logger.info("没有对应的purpose_codes::" + purpose_codes);
			}
		}

		return paramStringBuffer.toString();
	}

	/**
	 * 获取查询地址
	 * 
	 * @return
	 */
	public String getQueryUrl() {
		String CLeftTicketUrl = null;
		try {
			Map<String, String> result_data = null;
			result_data = HttpsQueyUtil.sendHttpsGET(
					"https://kyfw.12306.cn/otn/leftTicket/init", "");
			String init_html = result_data.get("result");
			Pattern p = Pattern.compile("(CLeftTicketUrl = ')(.*?)(';)");
			Matcher m = p.matcher(init_html);
			boolean b = m.matches();
			String sufix = null;
			while (m.find()) {
				System.out.println(m.group(0)); // 整个匹配到的内容
				System.out.println(m.group(1)); // (<textarea.*?>)
				System.out.println(m.group(2)); // (.*?)
				System.out.println(m.group(3)); // (</textarea>)
				sufix = m.group(2);
			}
			String url = "https://kyfw.12306.cn/otn";
			CLeftTicketUrl = url + "/" + sufix + "?";
			logger.info(CLeftTicketUrl + ",---");
		} catch (Exception e) {
			logger.info("获取url异常：：：", e);
			CLeftTicketUrl = null;
		}
		return CLeftTicketUrl;
	}

	/**
	 * 值写入response
	 * 
	 * @param response
	 * @param StatusStr
	 */
	public void write2Response(HttpServletResponse response, String StatusStr) {
		try {
			response.getWriter().write(StatusStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
