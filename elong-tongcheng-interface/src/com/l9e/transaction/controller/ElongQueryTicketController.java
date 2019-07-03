package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseController;
import com.l9e.common.Consts;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.ElongQueryTicketService;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.TrainStationVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.ParamCheckUtil;


/**
 * elong查询余票主控制器
 * @author liuyi2
 *
 */
@Controller
@RequestMapping("/elong")
public class ElongQueryTicketController extends BaseController{
	private static Logger logger= Logger.getLogger(ElongQueryTicketController.class);
	@Resource
	private CommonService commonService;
	@Resource
	private SoukdBuyTicketController soukd;
	@Resource
	protected NewBuyTicketController newBuyTicketController;
	@Resource
	private ElongQueryTicketService elongQueryTicketService;
	@Resource
	private QueryTicketService ticketService;
	/**
	 * 出票结果通知
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/query.jhtml")
	public void orderNotice(HttpServletRequest request, 
			HttpServletResponse response) {
		String type = this.getParam(request, "type");
		if ("queryLeftTicket".equals(type)) {
			queryLeftTicket(request, response); // 查询余票
		} else if ("checkTicketNum".equals(type)) {
			checkTicketNum(request, response); // 验证余票
		}else if ("querySubwayStation".equals(type)) {
			querySubwayStation(request, response); // 途经站
		}
		else{
			logger.info("hcp对外接口-非法的type接口参数 ：type=" + type==null?"null":type);
			printJson(response, getJson("003").toString());
		}
	}
	
	// 实时验证余票信息
	public void checkTicketNum(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(CreateIDUtil.createID("HC"));
		// queryOrder.queryOrderList(request,response);
		logger.info("实时余票查询接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String train_code = this.getParam(request, "train_code");
		String travel_time = this.getParam(request, "travel_time");
		String from_station = this.getParam(request, "from_station");
		String arrive_station = this.getParam(request, "arrive_station");
		String hmac = this.getParam(request, "hmac");
		
		
		/*String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
*/		
		String sys_merchant_id=this.queryElongSysValueByName("query_merchant_id");
		String sys_sign_key=this.queryElongSysValueByName("query_sign_key");
		
		/*String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
*/		
		if (!sys_merchant_id .equals(merchant_id) ) {
			//logger.info("实时余票查询接口-用户身份校验error:不存在该用户!merchant_id:"+merchant_id);
			printJson(response, getJson("004").toString());
			return;
		}
		
		
		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				|| StringUtil.isEmpty(train_code)
				|| StringUtil.isEmpty(from_station)
				|| StringUtil.isEmpty(arrive_station)
				|| StringUtil.isEmpty(travel_time)) {
			logger.info("实时余票查询接口-参数校验error:必要参数为空");
			printJson(response, getJson("003").toString());
			return;
		}
		if (ParamCheckUtil.isNotCheck(travel_time, ParamCheckUtil.DATA_REGEX)
				|| ParamCheckUtil.isNotCheck(from_station,
						ParamCheckUtil.ZHONGWEN_REGEX)
				|| ParamCheckUtil.isNotCheck(arrive_station,
						ParamCheckUtil.ZHONGWEN_REGEX)
				|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("实时余票查询接口-参数校验error:输入参数格式错误!");
			printJson(response, getJson("003").toString());
			return;
		}

		if((DateUtil.dateAddDays(new Date(), -1)).getTime()>DateUtil.stringToDate(travel_time, "yyyy-MM-dd").getTime()){
			logger.info("车票查询接口-参数校验error:输入参数为空!");
			printJson(response, getJson("003").toString());
			return;
		};
		
		

		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ train_code + travel_time + from_station + arrive_station;
		String hmac_19 = "";
		hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str+sys_sign_key, "", "utf-8");
		if (!hmac_19.equals(hmac)) {
			//logger.info("实时余票查询接口-安全校验error:安全验证错误,不符合安全校验规则!用户传递hmac【" + hmac + "】用户请求参数" + md_str+"系统加密hmac" + hmac_19);
			printJson(response, getJson("002").toString());
			return;
		} else {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("from_station", from_station);
			paramMap.put("arrive_station", arrive_station);
			paramMap.put("travel_time", travel_time);
			paramMap.put("method", "DGTrain");// 调用方法
			paramMap.put("train_code", train_code);
			paramMap.put("terminal", terminal);
			/***/
			paramMap.put("stop_buy_time", this.queryElongSysValueByName("stop_buyTicket_time"));
			paramMap.put("spare_ticket_amount", this.queryElongSysValueByName("spare_ticket_amount"));
			/** 1:12306接口 2:SOUKD接口 **/
			String channel = getSysInterfaceChannel("interface_channel");
			if (StringUtils.isEmpty(channel) || "3".equals(channel)) {
				logger.info("实时余票查询接口-调用实时余票查询接口开始![3:12306新接口]");
				newBuyTicketController
						.newQueryData(paramMap, request, response);
			} else if ("2".equals(channel)) {
				logger.info("实时余票查询接口-调用实时余票查询接口开始![2:SOUKD接口]");
				soukd.soukdQueryData(paramMap, request, response);
			}
		}
	}
	/**
	 * 对外车票查询接口
	 * 
	 * @param request
	 * @param response
	 */
	public void queryLeftTicket(HttpServletRequest request,
			HttpServletResponse response) {
		String from_station = this.getParam(request, "from_station");
		String arrive_station = this.getParam(request, "arrive_station");
		String travel_time = this.getParam(request, "travel_time");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String hmac = this.getParam(request, "hmac");
		
		/*String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
		 */		
		String sys_merchant_id=this.queryElongSysValueByName("query_merchant_id");
		String sys_sign_key=this.queryElongSysValueByName("query_sign_key");
		
		if (StringUtil.isEmpty(from_station)
				|| StringUtil.isEmpty(arrive_station)
				|| StringUtil.isEmpty(travel_time)
				|| StringUtil.isEmpty(terminal)
				|| StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)) {
			logger.info("车票查询接口-参数校验error:输入参数为空!"+"from_station:"+from_station
					+",arrive_station:"+arrive_station+",travel_time:"+travel_time
					+",terminal:"+terminal+",merchant_id:"+merchant_id+",timestamp:"+timestamp
					+",version:"+version+",hmac:"+hmac);
			printJson(response, getJson("003").toString());
			return;
		}
		if (ParamCheckUtil.isNotCheck(travel_time, ParamCheckUtil.DATA_REGEX)
				|| ParamCheckUtil.isNotCheck(from_station,
						ParamCheckUtil.ZHONGWEN_REGEX)
				|| ParamCheckUtil.isNotCheck(arrive_station,
						ParamCheckUtil.ZHONGWEN_REGEX)
				|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("车票查询接口-参数校验error:输入参数格式错误!");
			printJson(response, getJson("003").toString());
			return;
		}
		if((DateUtil.dateAddDays(new Date(), -1)).getTime()>DateUtil.stringToDate(travel_time, "yyyy-MM-dd").getTime()){
			logger.info("车票查询接口-参数校验error:查询车次时间错误!");
			printJson(response, getJson("003").toString());
			return;
		};
		if (!sys_merchant_id.equals(merchant_id)) {
			//logger.info("车票查询接口-用户身份校验error:不存在的用户!merchant_id:"+merchant_id);
			printJson(response, getJson("004").toString());
			return;
		}
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ travel_time + from_station + arrive_station;
		String hmac_19 = "";
			hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ sys_sign_key, "", "utf-8");
		if (!hmac_19.equals(hmac)) {
			//logger.info("车票查询接口-安全校验error:不符合安全校验规则。用户传递hmac：【" + hmac + "】用户传递参数：【" + md_str+ sys_sign_key+"】系统加密hmac：【" + hmac_19 + "】");
			printJson(response, getJson("002").toString());
			return;
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("from_station", from_station);
		paramMap.put("arrive_station", arrive_station);
		paramMap.put("travel_time", travel_time);
		paramMap.put("method", "DGTrain");// 调用方法
		paramMap.put("terminal", terminal);
		paramMap.put("stop_buy_time",  this.queryElongSysValueByName("stop_buyTicket_time"));
		paramMap.put("spare_ticket_amount",this.queryElongSysValueByName("spare_ticket_amount"));
		/** 3:12306新接口 2:SOUKD接口 **/
		String channel = getSysInterfaceChannel("interface_channel");
		String weather_book = getSysInterfaceChannel("sys_weather_book");
		if ("0".equals(weather_book)) {
			newBuyTicketController.unableBookTicketsQuery(paramMap, request,
					response);
		}
		if (StringUtils.isEmpty(channel) || "3".equals(channel)) {
			logger.info("车票查询接口-开始调用余票查询接口[3:12306新接口]");
			newBuyTicketController.newQueryData(paramMap, request, response);
		} else if ("2".equals(channel)) {
			logger.info("车票查询接口-开始调用余票查询接口[2:SOUKD接口]");
			soukd.soukdQueryData(paramMap, request, response);
		}
	}
	
	
	
	// 查询途经站信息
	public void querySubwayStation(HttpServletRequest request,
			HttpServletResponse response) {
		// queryOrder.queryOrderList(request,response);
		logger.info("查询途经站信息接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String train_code = this.getParam(request, "train_code");
		String hmac = this.getParam(request, "hmac");

		/*String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
*/		String sys_merchant_id=this.queryElongSysValueByName("query_merchant_id");
		String sys_sign_key=this.queryElongSysValueByName("query_sign_key");
		
		JSONObject json = new JSONObject();
		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				|| StringUtil.isEmpty(train_code)|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("查询途经站信息接口-参数校验error:必要参数为空或格式错误!");
			printJson(response, getJson("003").toString());
			return;
		}
		if (!sys_merchant_id.equals(merchant_id)) {
			//logger.info("车票查询接口-用户身份校验error:不存在的用户!");
			printJson(response, getJson("004").toString());
			return;
		}
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ train_code;
		logger.info(md_str + sys_sign_key);
		String hmac_19 = "";
		hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ sys_sign_key, "", "utf-8");
		if (!hmac_19.equals(hmac)) {
			//logger.info("查询途经站信息接口-安全验证error:不符合安全校验的规则用户传递hmac【" + hmac+"】用户传递查询参数【" + md_str+"】系统加密hmac【" + hmac_19+"】");
			printJson(response, getJson("002").toString());
			return;
		} else {
			try {
				train_code = ticketService.queryTheCheciForStation(train_code);
				List<TrainStationVo> list = ticketService
						.queryWayStationInfo(train_code);

				if (list == null || list.size() == 0) {
					logger.info("查询途经站信息接口-查询结果error:暂时没有该途经站信息!");
					printJson(response, getJson("501").toString());
					return;
				} else {
					for (TrainStationVo tnv : list) {
						tnv.reSetInterval();
					}
					// 返回正确结果值
					json.put("return_code", "000");
					json.put("message", "");
					JSONArray jsonArray = JSONArray.fromObject(list);
					json.put("train_stationinfo", jsonArray);
					printJson(response, json.toString());
				}
			} catch (Exception e) {
				logger.error("查询途经站信息接口-途径车站解析失败Exception", e);
				printJson(response, getJson("001").toString());
				return;
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
	//输出jsonObject
	public static void printJson(HttpServletResponse response,String str){
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
			//根据状态码获取输出jsonObject对象
	public JSONObject getJson(String code){
		JSONObject json = new JSONObject();
		json.put("return_code", code);
		json.put("message", TrainConsts.getReturnCode().get(code));
		return json;
	}
}
