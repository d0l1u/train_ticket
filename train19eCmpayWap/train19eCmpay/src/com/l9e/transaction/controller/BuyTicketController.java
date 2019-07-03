package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BuyTicketBase;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.Outer12306Data;
import com.l9e.transaction.vo.OuterSoukdData;
import com.l9e.transaction.vo.OuterSoukdNewData;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.TrainData;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.transaction.vo.TrainNewStationVo;
import com.l9e.transaction.vo.WeekDay;
import com.l9e.util.DateUtil;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;

/**
 * 车票预订
 * 
 * @author zhangjun
 * 
 */
@Controller
@RequestMapping("/buyTicket")
public class BuyTicketController extends BuyTicketBase {

	@Resource
	protected NewBuyTicketController newBuyTicketController;
	@Resource
	private SoukdBuyTicketController soukd;
	@Resource
	private QueryTicketService queryTicketService;
	@Resource
	private QueryStationInfoController queryStationInfoController;
	@Resource
	private QueryTicketService ticketService;
	
	/**
	 * 进入预定首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/bookIndex.jhtml")
	public String bookIndex(HttpServletRequest request,
			HttpServletResponse response) {
		// 公告
		List<Map<String, String>> noticeList = commonService.queryNoticeList();

		Map<String, String> paramMap = new HashMap<String, String>();
		String travel_time = DateUtil.dateToString(new Date(),
				DateUtil.DATE_FMT1);
		// 默认乘车日期的匹配（当天或3天）
		String ticketing_time_limit = this.getSysSettingValue(
				"ticketing_time_limit", "ticketing_time_limit");
		travel_time = DateUtil.dateAddDays(travel_time, ticketing_time_limit);
		paramMap.put("travel_time", travel_time);// 日期设置默认值
		request.setAttribute("paramMap", paramMap);
		request.setAttribute("noticeList", noticeList); // 公告

		Map<String, List<Map<String, String>>> highTrainList = new HashMap<String, List<Map<String, String>>>();
		List<String> list = new ArrayList<String>();
		list = queryTicketService.queryOftenStation();
		List<Map<String, String>> stationList = new ArrayList<Map<String, String>>();
		for (String station : list) {
			stationList = queryTicketService.queryOftenStationInfo(station);
			highTrainList.put(station, stationList);
		}
		request.setAttribute("highTrainList", highTrainList);
		//return "book/trainHome";
		return "book/trainHome";
	}

	/**
	 * 根据车站查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryByStation.jhtml")
	public String queryByStation(HttpServletRequest request,
			HttpServletResponse response) {

		String from_city = this.getParam(request, "from_city");
		String to_city = this.getParam(request, "to_city");
		String travel_time = this.getParam(request, "travel_time");
		String gaotie = this.getParam(request, "gaotie");

		// 设置缓存区余票阀值数
		MemcachedUtil.spareTicket = Integer.valueOf(this.getSysSettingValue(
				"spare_ticket_amount", "spare_ticket_amount"));

		Map<String, String> paramMap = new HashMap<String, String>();
		int day = DateUtil.getDay(travel_time, "yyyy-MM-dd");
		String dayOfWeek = WeekDay.getDay(day);
		paramMap.put("day", dayOfWeek);
		paramMap.put("from_city", from_city);
		paramMap.put("to_city", to_city);
		paramMap.put("travel_time", travel_time);
		paramMap.put("gaotie", gaotie);
		paramMap.put("method", "DGTrain");// 调用方法
		/** 1:12306接口 2:SOUKD接口* */
		String channel = getSysInterfaceChannel("INTERFACE_CHANNEL");
		String weather_book = getSysInterfaceChannel("sys_weather_book");
		if ("0".equals(weather_book)) {
			newBuyTicketController.unableBookTicketsQuery(paramMap, request);
		}
		if (StringUtils.isEmpty(channel) || "3".equals(channel)) {
			newBuyTicketController.newQueryData(paramMap, request);
		} else if ("2".equals(channel)) {
			soukd.soukdQueryData(paramMap, request);
		} else {
			oldQueryData(paramMap, request, channel);
		}

		// request.setAttribute("paramMap", paramMap);
		return "book/bookIndexNew";
	}

	public void oldQueryData(Map<String, String> paramMap,
			HttpServletRequest request, String channel) {
		long log_begin_time = System.currentTimeMillis();// 日志查询开始时间
		OuterSoukdData otVo = null;// 火车票数据
		String travel_time = paramMap.get("travel_time");
		String key = getFileName(paramMap);
		try {
			// Memcache中是否存在该记录
			if (null == MemcachedUtil.getInstance().getAttribute(key)) {
				/** 1:12306接口 2:SOUKD接口* */
				if (StringUtils.isEmpty(channel) || "1".equals(channel)) {
					otVo = getDataFromInterface(paramMap, null, this
							.getSysInterface12306Url("INTERFACE_12306_URL",
									interface_12306_url));// 默认查询12306接口
					if (otVo == null) {// 查询12306新接口异常，则查询Soukd
						soukd.soukdQueryData(paramMap, request);
						return;
					}
				}
				if (otVo != null && otVo.getDataList() != null
						&& otVo.getDataList().size() > 0) {// 如果有数据，则缓存数据
					String prePath = request.getSession().getServletContext()
							.getRealPath("/files");
					queryTicketCache(prePath, travel_time, otVo, key);
				}

			} else {// Memcache中读取文件路径
				long start = System.currentTimeMillis();
				String filePath = (String) MemcachedUtil.getInstance()
						.getAttribute(key);
				String fileContent = FileUtil.readFile(filePath, SOUKD_CHARSET);
				logger.info("读文件" + filePath + "耗时"
						+ (System.currentTimeMillis() - start) + "ms");
				logger.debug("读文件" + filePath + "返回内容：" + fileContent);

				ObjectMapper mapper = new ObjectMapper();
				otVo = mapper.readValue(fileContent, OuterSoukdData.class);
			}

		} catch (Exception e) {
			otVo = new OuterSoukdData();
			logger.error("火车票查询异常~~", e);
		}

		// 处理otVo对象在travel_time时间之内的票不能订购
		otVo = unableBookTicket(otVo, travel_time);

		request.setAttribute("paramMap", paramMap);
		request.setAttribute("otVo", otVo);
		queryTimeMillis(request, paramMap, log_begin_time);
	}

	private OuterSoukdData getDataFromInterface(Map<String, String> paramMap,
			String interfaceName, String interfaceUrl) {
		OuterSoukdData otVo = new OuterSoukdData();
		String url = null;
		try {
			long start = System.currentTimeMillis();
			url = get12306Url(paramMap, interfaceUrl);
			// 请求超时时间
			String con_timeout = this.getSysSettingValue(
					"INTERFACE_CON_TIMEOUT", "INTERFACE_CON_TIMEOUT");
			String read_timeout = this.getSysSettingValue(
					"INTERFACE_READ_TIMEOUT", "INTERFACE_READ_TIMEOUT");
			String jsonStr = HttpUtil.sendByGet(url, "UTF-8", con_timeout,
					read_timeout);// 调用接口
			jsonStr = jsonStr.replace("[\"{", "[{").replace("}\"]", "}]")
					.replace("\\", "").replace("--", "-");

			ObjectMapper mapper = new ObjectMapper();
			JSONObject jsonObject = new JSONObject();
			String result = jsonObject.fromObject(jsonStr).getString(
					"ErrorInfo");
			result = result.substring(1, result.length() - 1);
			jsonObject = jsonObject.fromObject(result);
			if (!"0".equals(jsonObject.getString("code"))) {
				logger.info("调用12306机器人旧接口查询失败原因："
						+ jsonObject.getString("errInfo"));
				return null;
			}
			Outer12306Data outer12306Data = mapper.readValue(jsonStr,
					Outer12306Data.class);
			otVo = outer12306Data.getErrorInfo().get(0);
			if (otVo != null) {
				if (StringUtils.isEmpty(paramMap.get("train_no"))) {
					logger.info("<火车票查询>调用12306机器人接口成功查询"
							+ paramMap.get("from_city") + "/"
							+ paramMap.get("to_city") + "("
							+ paramMap.get("travel_time") + ")的列车信息，耗时"
							+ (System.currentTimeMillis() - start) + "ms");
				} else {
					logger.info("<火车票查询>调用12306机器人接口成功查询"
							+ paramMap.get("train_no") + "("
							+ paramMap.get("from_city") + "/"
							+ paramMap.get("to_city") + " "
							+ paramMap.get("travel_time") + ")的列车信息，耗时"
							+ (System.currentTimeMillis() - start) + "ms");
				}

				// 统计列车列数
				if (otVo.getDataList() == null
						|| otVo.getDataList().size() == 0) {
					String erroInfo = "";
					if (!StringUtils.isEmpty(otVo.getErrInfo())) {
						erroInfo = otVo.getErrInfo();
					}
					logger.info("[火车票查询]查询" + paramMap.get("from_city") + "/"
							+ paramMap.get("to_city") + "("
							+ paramMap.get("travel_time") + ")的列车共计0列 "
							+ erroInfo);
				} else {
					logger.info("[火车票查询]查询" + paramMap.get("from_city") + "/"
							+ paramMap.get("to_city") + "("
							+ paramMap.get("travel_time") + ")的列车共计"
							+ otVo.getDataList().size() + "列");
				}
			}
		} catch (Exception e) {// 没有查询到数据
			// e.printStackTrace();
			logger.error("解析12306机器人返回数据异常", e);
			return null;

		}
		return otVo;
	}

	// 处理otVo对象在travel_time时间之内的票不能订购
	public OuterSoukdData unableBookTicket(OuterSoukdData otVo,
			String travle_time) {
		if (travle_time.equals(DateUtil.dateToString(new Date(),
				DateUtil.DATE_FMT1))) {
			int currentTime = Integer.parseInt(DateUtil.dateToString(
					new Date(), "HHmm"));
			if (otVo != null && otVo.getDataList() != null
					&& otVo.getDataList().size() > 0) {
				for (TrainData trainData : otVo.getDataList()) {
					int beginTime = Integer.parseInt(trainData.getStartTime()
							.replaceAll(":", ""));
					String yp_show = "无";
					if (beginTime - currentTime < 600) {
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
		return otVo;
	}

	/**
	 * 进入预订下单页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/gotoBookOrder.jhtml")
	public String gotoBookOrder(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/*
		 * //9点-21点可以 Date date = new Date(); String datePre =
		 * DateUtil.dateToString(date, "yyyyMMdd"); Date begin =
		 * DateUtil.stringToDate(datePre + "090000", "yyyyMMddHHmmss");//8:00
		 * Date end = DateUtil.stringToDate(datePre + "210000",
		 * "yyyyMMddHHmmss");//22:00 if(date.before(begin) || date.after(end)){
		 * String errMsg = "由于节前订票高峰，又遇火车票系统调整，临时订票时间修改为早9点-晚21点，带来的不便敬请谅解！";
		 * errMsg = URLEncoder.encode(errMsg, "UTF-8"); return
		 * "redirect:/common/goToErrPage.jhtml?errMsg="+errMsg; }
		 */
		String travelTime = this.getParam(request, "travelTime");
		String trainCode = this.getParam(request, "trainCode");
		String startCity = URLDecoder.decode(this
				.getParam(request, "startCity"), "UTF-8");
		String endCity = URLDecoder.decode(this.getParam(request, "endCity"),
				"UTF-8");
		String startTime = this.getParam(request, "startTime");
		String endTime = this.getParam(request, "endTime");
		String costTime = this.getParam(request, "costTime");
		String seat_type = this.getParam(request, "seat_type");
		String seat_price = this.getParam(request, "seat_price");
		String day = this.getParam(request, "day");
		//String seatMsg = URLDecoder.decode(this.getParam(request, "seatMsg"),"UTF-8");
		//baoxian="+baoxian+"&fpNeed"+fpNeed+"&fp_receiver"+fp_receiver+"&fp_phone"+fp_phone+"&fp_address"+fp_address+"&fp_zip_code"+fp_zip_code;
		String baoxian = this.getParam(request, "baoxian");
		String fpNeed = this.getParam(request, "fpNeed");
		String fp_receiver = this.getParam(request, "fp_receiver");
		String fp_phone = this.getParam(request, "fp_phone");
		String fp_address = this.getParam(request, "fp_address");
		String fp_zip_code = this.getParam(request, "fp_zip_code");
		
		List<Map<String, String>> seatInfoList = new ArrayList<Map<String, String>>();
		Map<String, String> seatPrizeMap = new HashMap<String, String>();// 座席与价格映射
		// 解析座位信息
		//this.deSeatMsg(seatInfoList, seatMsg, seatPrizeMap);

		LoginUserInfo loginUser = this.getLoginUser(request);

		// 查询保险产品信息
		ProductVo product = new ProductVo();
		product.setType(TrainConsts.PRODUCT_TYPE_1);// 保险
		product.setStatus(TrainConsts.PRODUCT_STATUS_1);// 上架
		// 排队人数以及等待时间
		int amount = Integer.valueOf(this.getSysSettingValue(
				"queuing_tickets_amount", "queuing_tickets_amount"));
		Random random = new Random();
		int wait_amount = amount + random.nextInt(10);
		String hours = this.getSysSettingValue("queuing_tickets_time",
				"queuing_tickets_time");

		List<ProductVo> productList = commonService
				.queryProductInfoList(product);
		request.setAttribute("cm_phone", loginUser.getCm_phone()); //"15201169346");//
		request.setAttribute("travelTime", travelTime);
		request.setAttribute("trainCode", trainCode);
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(trainCode));
		request.setAttribute("startCity", startCity);
		request.setAttribute("endCity", endCity);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("costTime", costTime);
		request.setAttribute("seatInfoList", seatInfoList);
		request.setAttribute("seatPrizeMapper", JSONObject.fromObject(seatPrizeMap).toString());
		request.setAttribute("productList", productList);
		request.setAttribute("wait_amount", wait_amount);
		request.setAttribute("wait_time", hours);
		request.setAttribute("seat_type", seat_type);//坐席
		request.setAttribute("seat_price", seat_price);//票价
		request.setAttribute("day", day);//星期
		request.setAttribute("baoxian", baoxian);//是否需要保险 1为需要
		request.setAttribute("fpNeed", fpNeed);//是否邮寄发票      1为邮寄
		request.setAttribute("fp_receiver", fp_receiver);//发票联系人
		request.setAttribute("fp_phone", fp_phone);//发票电话
		request.setAttribute("fp_address", fp_address);//发票地址
		request.setAttribute("fp_zip_code", fp_zip_code);//发票邮政编码
		
		return "book/bookInfoInput";
	}

	/**
	 * AJAX异步验证票源是否充足
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/checkTicketEnoughAjax.jhtml")
	@ResponseBody
	public void checkTicketEnoughAjax(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			/*
			 * String train_no = this.getParam(request, "train_no"); Map<String,
			 * String> paramMap = new HashMap<String, String>();
			 * paramMap.put("from_city", this.getParam(request, "from_city"));
			 * paramMap.put("to_city", this.getParam(request, "to_city"));
			 * paramMap.put("travel_time", this.getParam(request,
			 * "travel_time")); paramMap.put("train_no", train_no);
			 * 
			 * OuterSoukdData otVo = getDataFromInterface(paramMap, "12306",
			 * this.getSysInterface12306Url()); String yp_key =
			 * TrainConsts.getSeatIdYpMap().get(this.getParam(request,
			 * "seat_type"));
			 * 
			 * String result = "no"; if(otVo != null && otVo.getDataList() !=
			 * null && otVo.getDataList().size() > 0){ for(TrainData trainData :
			 * otVo.getDataList()){//遍历查询list获取本车次信息
			 * if(!StringUtils.isEmpty(trainData.getTrainCode()) &&
			 * train_no.equals(trainData.getTrainCode())){
			 * 
			 * JSONObject jo = JSONObject.fromObject(trainData); String yp =
			 * (String) jo.get(yp_key);
			 * 
			 * //余票小于等于10张则过滤该坐席 if(StringUtils.isEmpty(yp) || "-".equals(yp)){
			 * result = "no"; }else if(Double.parseDouble(yp)<=10){ result =
			 * "no"; }else{ result = "yes"; } break; } } }
			 */
			String result = "yes";
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/queryNearTrainInfo.jhtml")
	public String queryNearTrainInfo(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();

		String travelTime = this.getParam(request, "travel_time");
		String trainCode = this.getParam(request, "train_code");
		String startCity = this.getParam(request, "from_city");
		String endCity = this.getParam(request, "to_city");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("travel_time", travelTime);
		paramMap.put("from_city", startCity);
		paramMap.put("to_city", endCity);
		System.out.println(paramMap);
		newBuyTicketController.newQueryData(paramMap, request);
		OuterSoukdNewData osnd = (OuterSoukdNewData) request
				.getAttribute("osnd");
		System.out.println("ErrorInfo" + osnd.getDatajson());
		if (osnd == null) {
			System.out.println("the data from the request is null");
		}
		List<TrainNewData> trainDataList = osnd.getDatajson();
		for (TrainNewData trainData : trainDataList) {
			if (trainCode.equals(trainData.getStation_train_code())) {
				sb.append("travelTime=" + travelTime + "&trainCode="
						+ trainCode + "&startCity="
						+ URLEncoder.encode(startCity, "utf-8") + "&endCity="
						+ URLEncoder.encode(endCity, "utf-8"));
				sb.append("&startTime=" + trainData.getStart_time()
						+ "&endTime=" + trainData.getArrive_time()
						+ "&costTime=" + trainData.getLishiValue()
						+ "&seatMsg=");

				StringBuffer sb1 = new StringBuffer();
				sb1.append("无座_"
						+ ("-".equals(trainData.getYz()) ? trainData.getZe()
								: trainData.getYz()) + "_"
						+ trainData.getWz_num());
				sb1.append(",硬座_" + trainData.getYz() + "_"
						+ trainData.getYz_num() + ",软座_" + trainData.getRz()
						+ "_" + trainData.getRz_num());
				sb1.append(",一等座_" + trainData.getZy() + "_"
						+ trainData.getYz_num() + ",二等座_" + trainData.getZe()
						+ "_" + trainData.getZe_num());
				sb1.append(",硬卧_" + trainData.getYwx() + "_"
						+ trainData.getYw_num() + ",软卧_" + trainData.getRwx()
						+ "_" + trainData.getRw_num());
				sb1.append(",高级软卧_" + trainData.getGwx() + "_"
						+ trainData.getGr_num());
				sb1.append(",特等座_" + trainData.getTdz() + "_"
						+ trainData.getTz_num());
				sb1.append(",商务座_" + trainData.getSwz() + "_"
						+ trainData.getSwz_num());
				sb.append(URLEncoder.encode(sb1.toString(), "UTF-8"));
				break;
			}
		}
		System.out.println(sb.toString());
		return "redirect:/buyTicket/queryTrainInfo.jhtml?" + sb.toString();
	}

	@RequestMapping("/queryTrainInfo.jhtml")
	public String queryTrainInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String travelTime = this.getParam(request, "travelTime");
		String trainCode = this.getParam(request, "trainCode");
		String fromCity = this.getParam(request, "startCity");
		String endCity = this.getParam(request, "endCity");
		String startTime = this.getParam(request, "startTime");
		String endTime = this.getParam(request, "endTime");
		String costTime = this.getParam(request, "costTime");
		String seatMsg = this.getParam(request, "seatMsg");
		System.out.println("接收到的参数为:" + seatMsg);
		List<Map<String, String>> seatList = new ArrayList<Map<String, String>>();
		System.out.println(seatMsg);
		System.out.println("seatMsg分割");
		String[] seatData = seatMsg.split(",");
		for (String seat : seatData) {
			Map<String, String> seatMap = new HashMap<String, String>();
			System.out.println("seat + " + seat);
			String[] seatInfo = seat.split("_");
			for (int j = 0; j < seatInfo.length; j++) {
				System.out.println("seatInfo" + j + ":::::::" + seatInfo[j]);
			}
			if("无座".equals(seatInfo[0]) && "-".equals(seatInfo[2])){
				continue;
			}
			if (!"-".equals(equals(seatInfo[2])) && !"".equals(seatInfo[1])
					&& !"-".equals(seatInfo[1])) {
				if (!"null".equals(seatInfo[1])) {
					seatMap.put("seat_type", seatInfo[0]);
					if ("-".equals(seatInfo[2])) {
						seatMap.put("seat_num", "0");
					} else {
						seatMap.put("seat_num", seatInfo[2]);
					}
					seatMap.put("seat_price", seatInfo[1]);
					System.out.println(seatMap);
				}
			}
			if (!seatMap.isEmpty()) {
				System.out.println(seatMap);
				seatList.add(seatMap);
			}
		}
		String checi = trainCode;
		
		checi = ticketService.queryTheCheciForStation(checi);
		List<TrainNewStationVo> stationList = ticketService.queryWayStationInfo(checi);
		for(TrainNewStationVo tnv: stationList){
			tnv.reSetInterval();
		}
/*
		String trainStation = HttpUtil.sendByPost(
				"http://localhost:8080/station/subwayName.jhtml", param,
				"utf-8");
		System.out.println("返回数据：："+trainStation);
		List<StationVo> stationList = JSONArray.toList(JSONArray
				.fromObject(trainStation), new StationVo(), new JsonConfig());
*/
		int day = DateUtil.getDay(travelTime, "yyyy-MM-dd");
		String dayOfWeek = WeekDay.getDay(day);
		request.setAttribute("day", dayOfWeek);
		request.setAttribute("cost_time", costTime);
		request.setAttribute("stationList", stationList);
		request.setAttribute("travel_time", travelTime);
		request.setAttribute("train_code", trainCode);
		request.setAttribute("from_city", fromCity);
		request.setAttribute("to_city", endCity);
		request.setAttribute("seat_info", seatList);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		return "book/trainInfo";
	}
	
	
	
}
