package com.l9e.transaction.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;

/**
 * 车票预订
 * 
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
	 * 
	 * @param paramMap
	 * @param response
	 */
	public void newQueryData(Map<String, String> paramMap,
			HttpServletRequest request) {
		long log_begin_time = System.currentTimeMillis();// 日志查询开始时间
		String travel_time = paramMap.get("travel_time");
		ObjectMapper mapper = new ObjectMapper();
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		StringBuffer param = new StringBuffer();
		try {
			param.append("travel_time=").append(travel_time).append(
					"&from_station=").append(URLEncoder.encode(paramMap.get("from_city"), "UTF-8")).append(
					"&arrive_station=").append(
					URLEncoder.encode(paramMap.get("to_city"), "UTF-8")).append(
					"&channel=").append("inner");
		} catch (UnsupportedEncodingException e1) {
			logger.error("汉字编码发生错误：" + e1.getMessage());
		}
		logger.info(travel_time + paramMap.get("from_city")
				+ paramMap.get("to_city") + "#" + paramMap.get("gaotie"));
		try {
			String jsonQuery = HttpUtil.sendByPost(
					getSysInterfaceChannel("query_left_ticket_url"), param
							.toString(), "UTF-8");// 调用接口
			logger.info("查询返回结果数据：" + jsonQuery);
			if ("NO_DATAS".equals(jsonQuery) || "ERROR".equals(jsonQuery)) {
				osnd = null;
			} else {
				osnd = mapper.readValue(jsonQuery, OuterSoukdNewData.class);
				osnd.setSdate(travel_time);
			}
		} catch (Exception e) {
			logger.error("查询余票异常！", e);
			osnd = null;
		}
		// 追加时间限制
		limitTimeTicket(travel_time, osnd);
		osnd = checkTrainType(osnd, paramMap);
		request.setAttribute("osnd", osnd);
		logger.info("osnd.getDatajson():" + osnd.getDatajson());
		request.setAttribute("paramMap", paramMap);
		osnd = null;
		queryTimeMillis(request, paramMap, log_begin_time);
	}

	/*
	 * 筛选出所有车次信息中为动车（D）、高铁（G）、城际列车(C)的车次 @param data为查询到得所有车次信息 @param
	 * paramMap从首页传过来的参数值
	 */
	public OuterSoukdNewData checkTrainType(OuterSoukdNewData data,
			Map<String, String> paramMap) {
		if ("on".equals(paramMap.get("gaotie"))) {
			List<TrainNewData> trainDataList = data.getDatajson();
			List<TrainNewData> newTrainDataList = new ArrayList<TrainNewData>();
			for (TrainNewData trainData : trainDataList) {
				if (trainData.getStation_train_code().contains("D")
						|| trainData.getStation_train_code().contains("C")
						|| trainData.getStation_train_code().contains("G")) {
					newTrainDataList.add(trainData);
				}
			}
			logger.info("查询到的动车有" + newTrainDataList.size() + "列");
			data.setDatajson(newTrainDataList);
			return data;

		} else {
			return data;
		}
	}

	/**
	 * 追加余票预订时间限制
	 * 
	 * @param paramMap
	 * @param response
	 */
	public void limitTimeTicket(String travel_time, OuterSoukdNewData osnd) {
		String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		// 6小时之内的票不能订购
		if (travel_time.equals(nowDate)) {
			int currentTime = Integer.parseInt(DateUtil.dateToString(
					new Date(), "HHmm"));
			if (osnd != null) {
				for (TrainNewData trainNewData : osnd.getDatajson()) {
					int beginTime = Integer.parseInt(trainNewData
							.getStart_time().replaceAll(":", ""));
					String num_show = "无";
					if (beginTime - currentTime < 600) {
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
		if (travel_time.equals(DateUtil.dateAddDays(nowDate, "1"))) {
			if ("23".compareTo((DateUtil.dateToString(new Date(), "HH"))) <= 0) {
				if (osnd != null) {
					for (TrainNewData trainNewData : osnd.getDatajson()) {
						if (trainNewData.getStart_time().compareTo("09:00") < 0) {
							trainNewData.setCanBook("0");
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
			url = get12306Url(paramMap, this.getSysInterface12306Url(
					"INTERFACE_12306_NEW_URL", interface_12306_new_url));
			// 请求超时时间
			String con_timeout = this.getSysSettingValue(
					"INTERFACE_CON_TIMEOUT", "INTERFACE_CON_TIMEOUT");
			String read_timeout = this.getSysSettingValue(
					"INTERFACE_READ_TIMEOUT", "INTERFACE_READ_TIMEOUT");
			String jsonStr = HttpUtil.sendByGet(url, "UTF-8", con_timeout,
					read_timeout);// 调用接口
			jsonStr = jsonStr.replace("\\", "").replace("[\"{", "[{").replace(
					"}\"]", "}]").replace("\"[", "[").replace("]\"", "]")
					.replace("--", "-");
			ObjectMapper mapper = new ObjectMapper();
			Outer12306NewData outer12306NewData = mapper.readValue(jsonStr,
					Outer12306NewData.class);
			osnd = outer12306NewData.getErrorInfo().get(0);
			if (osnd != null) {
				if (StringUtils.isEmpty(paramMap.get("train_no"))) {
					logger.info("<火车票查询>调用12306机器人新接口成功查询"
							+ paramMap.get("from_city") + "/"
							+ paramMap.get("to_city") + "("
							+ paramMap.get("travel_time") + ")的列车信息，耗时"
							+ (System.currentTimeMillis() - start) + "ms");
				} else {
					logger.info("<火车票查询>调用12306机器人新接口成功查询"
							+ paramMap.get("train_no") + "("
							+ paramMap.get("from_city") + "/"
							+ paramMap.get("to_city") + " "
							+ paramMap.get("travel_time") + ")的列车信息，耗时"
							+ (System.currentTimeMillis() - start) + "ms");
				}

				// 统计列车列数
				if (osnd.getDatajson() == null
						|| osnd.getDatajson().size() == 0) {
					String erroInfo = "";
					if (!StringUtils.isEmpty(osnd.getErrInfo())) {
						erroInfo = osnd.getErrInfo();
					}
					logger.info("[火车票查询]查询" + paramMap.get("from_city") + "/"
							+ paramMap.get("to_city") + "("
							+ paramMap.get("travel_time") + ")的列车共计0列 "
							+ erroInfo);
				} else {
					logger.info("[火车票查询]查询" + paramMap.get("from_city") + "/"
							+ paramMap.get("to_city") + "("
							+ paramMap.get("travel_time") + ")的列车共计"
							+ osnd.getDatajson().size() + "列");
				}
			}
		} catch (Exception e) {// 没有查询到数据
			logger.error("解析12306机器人返回数据异常", e);
			return null;
		}
		return osnd;
	}

	// 网站终止预订功能
	// 进行车次查询
	public void unableBookTicketsQuery(Map<String, String> paramMap,
			HttpServletRequest request) {
		List<TrainNewDataFakeAppendTrain> list = ticketService
				.queryAppendTrainNewData(paramMap);
		for (TrainNewDataFakeAppendTrain appTrain : list) {
			appTrain.changeData();
		}
		request.setAttribute("paramMap", paramMap);
		request.setAttribute("unBookList", list);
	}
}
