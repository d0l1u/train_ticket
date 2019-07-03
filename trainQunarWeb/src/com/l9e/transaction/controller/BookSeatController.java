package com.l9e.transaction.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.BookPassenger;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.InterAccount;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.PassengerStudentExt;
import com.l9e.transaction.vo.SysConfig;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.DecodeJson;
import com.l9e.util.HttpUtil;
import com.l9e.util.TrainPropUtil;
import com.l9e.util.UrlFormatUtil;

@Controller
@RequestMapping("/bookSeat")
public class BookSeatController extends BaseController {
	private static final Logger logger = Logger.getLogger(BookSeatController.class);
	
	@Resource
	private OrderService orderService;
	private String notify_cp_interface_url;
	private String notify_cp_back_url;
	private String notify_cancel_interface_url;
	
	@Value("#{propertiesReader[notify_cp_interface_url]}")
	public void setNotify_cp_interface_url(String notify_cp_interface_url) {
		this.notify_cp_interface_url = notify_cp_interface_url;//通知出票系统--下单订单地址
	}
	
	@Value("#{propertiesReader[notify_cp_back_url]}")
	public void setNotify_cp_back_url(String notify_cp_back_url) {
		this.notify_cp_back_url = notify_cp_back_url;//通知出票系统接口回调地址
	}
	 
	@Value("#{propertiesReader[notify_cancel_interface_url]}")
	public void setNotify_cancel_interface_url(String notifyCancelInterfaceUrl) {
		notify_cancel_interface_url = notifyCancelInterfaceUrl;//通知出票系统--取消订单地址
	}
	
	@Value("#{propertiesReader[notify_pay_interface_url]}")
	private String notify_pay_interface_url;//通知出票系统--支付订单地址
	
	
	/**
	 * 占座请求接口
	 * 该接口由去哪儿网调用代理商，为异步接口
	 * @param request
	 * @param response
	 */
	@RequestMapping("/bookSeat.jhtml")
	public void bookSeat(HttpServletRequest request, HttpServletResponse response){
		/**获取各账号下的数据**/
		for(InterAccount account : SysConfig.accountContainer){
			bookSeatMethod(request, response, account);
		}
	}
	

	private void bookSeatMethod(HttpServletRequest request,
			HttpServletResponse response, InterAccount account) {
		JSONObject result = new JSONObject();
		/**获取各账号下的数据**/
		String order_source = account.getName();//去哪订单来源
		String md5Key = account.getMd5Key();
		String merchantCode = account.getMerchantCode();
		
		//接收去哪儿的参数
		String orderNo = this.getParam(request, "orderNo");//订单号
		if(StringUtils.isEmpty(orderNo)){
			logger.info("【申请占座接口】获取订单号为空");
			return;
		}
		String reqFrom = this.getParam(request, "reqFrom");//请求来源:qunar
		String reqTime = this.getParam(request, "reqTime");//请求时间,格式：yyyyMMddhhmmss
		String trainNo = this.getParam(request, "trainNo");//车次
		String from = "", to = "";
		try {
			from = URLDecoder.decode(this.getParam(request, "from"), "utf-8");//出发站名称
			to = URLDecoder.decode(this.getParam(request, "to"), "utf-8");//到达站名称
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String date = this.getParam(request, "date");//乘车日期
		String retUrl = this.getParam(request, "retUrl");//回调地址:占座成功或失败后回调去哪儿网的接口
		String passengers = this.getParam(request, "passengers");//乘客信息的json字符串
		String extSeat = this.getParam(request, "extSeat");//备选坐席--坐席编码：价格
		String hmac = this.getParam(request, "HMAC");//商户密钥+订单号到乘客信息，将所有字段值拼接做MD5运算,运算结果为大写
		
		String str = md5Key + orderNo + reqFrom + reqTime + trainNo + from + to + date + retUrl + passengers + extSeat;
		logger.info("【申请占座接口】去哪儿传来的数据："+str);
		String sign = DigestUtils.md5Hex(str).toUpperCase();
		if(!hmac.equals(sign)){//校验密钥是否正确
			logger.info("【申请占座接口】密钥不匹配，订单号为"+orderNo+"，接口传来的hmac="+hmac+"，我们加密的sign="+sign+"，我们加密的sign="+sign);
			result.put("errCode", "111");
			result.put("ret", false);
			result.put("errMsg", "密钥不匹配");
		}else{//密钥匹配
			JSONArray jsonPasser = JSONArray.fromObject(passengers);
			int count = orderService.queryOrderCountByNo(orderNo);
			if(count == 0){//普通订单
				List<OrderInfoCp> cpList = new ArrayList<OrderInfoCp>();
				OrderInfo orderInfo = new OrderInfo();
				
				orderInfo.setOrder_id(orderNo);
				String order_type = "0";
				orderInfo.setOrder_type(order_type);//订单类别：0、普通订单 1、联程订单
				orderInfo.setOrder_name(from+"/"+to);
				orderInfo.setOrder_status("11");//支付成功
				orderInfo.setOrder_time(DateUtil.dateToString(DateUtil.stringToDate(reqTime, DateUtil.DATE_FMT2), DateUtil.DATE_FMT3));
				orderInfo.setOut_ticket_type("11");//电子票
				orderInfo.setIs_pay("11");//是否支付：00：已支付；11：未支付
				orderInfo.setTrain_no(trainNo);
				if("乌鲁木齐".equals(from)){
					from = "乌鲁木齐南";
				}
				if("乌鲁木齐".equals(to)){
					to = "乌鲁木齐南";
				}
				orderInfo.setFrom_city(from);
				orderInfo.setTo_city(to);
				date = DateUtil.dateToString(DateUtil.stringToDate(date, "yyyy_MM_dd"), DateUtil.DATE_FMT1);
				orderInfo.setFrom_time(date);//乘车日期
				orderInfo.setTravel_time(date);//乘车日期
				orderInfo.setChannel("qunar");
				orderInfo.setOrder_source(order_source);
				
				orderInfo.setRetUrl(retUrl);
				
				
				//String pay_money = null;//qunar给的默认坐席价格
				//解析坐席和面值
				String seat = jsonPasser.getJSONObject(0).getString("seatCode");
				//logger.info("////////////*******"+seat);
				/*Map<String, String> seatMap = DecodeJson.decode(seat);
				for(String key : seatMap.keySet()){
					orderInfo.setQunar_seat_type(key);
					orderInfo.setSeat_type(TrainPropUtil.get19eSeatType(key));
					//pay_money = seatMap.get(key);
				}*/
				//book_notify_num
				//9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，4:软卧，3:硬卧，2:软座，1:硬座
				//座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他
				//qunar座位类型：0、站票 1、硬座 2、软座 3、一等软座 4、二等软座 5、硬卧上 6、硬卧中 7、硬卧下 8、软卧上 9、软卧下 10、高级软卧上 11、高级软卧下12、特等座13、商务座
				if("13".equals(seat)){//商务座
					orderInfo.setSeat_type("0");
//					orderInfo.setQunar_seat_type(seat);
				}else if("12".equals(seat)){//特等座
					orderInfo.setSeat_type("1");
//					orderInfo.setQunar_seat_type(seat);
				}else if("3".equals(seat)){//一等座
					orderInfo.setSeat_type("2");
//					orderInfo.setQunar_seat_type("3");
				}else if("4".equals(seat)){//二等座
					orderInfo.setSeat_type("3");
//					orderInfo.setQunar_seat_type("4");
				}else if("10".equals(seat) || "11".equals(seat)){//高级软卧
					orderInfo.setSeat_type("4");
//					orderInfo.setQunar_seat_type("11");
				}else if("8".equals(seat) || "9".equals(seat)){//软卧
					orderInfo.setSeat_type("5");
//					orderInfo.setQunar_seat_type("9");
				}else if("5".equals(seat) || "6".equals(seat) || "7".equals(seat)){//硬卧
					orderInfo.setSeat_type("6");
//					orderInfo.setQunar_seat_type("7");
				}else if("2".equals(seat)){//软座
					orderInfo.setSeat_type("7");
//					orderInfo.setQunar_seat_type("2");
				}else if("1".equals(seat)){//硬座
					orderInfo.setSeat_type("8");
//					orderInfo.setQunar_seat_type("1");
				}else if("0".equals(seat)){//0、站票 
					orderInfo.setSeat_type("9");
				}
				orderInfo.setQunar_seat_type(seat);
				String seat_price = jsonPasser.getJSONObject(0).getString("ticketPrice");
				//无备选座席，无该字段。 有备选坐席，按照坐席编码：价格的格式传送。 
				if(!StringUtils.isEmpty(extSeat) && !"[]".equals(extSeat)){
					Map<String, String> extMap = DecodeJson.decode(extSeat);
					StringBuffer extSb = new StringBuffer();
					StringBuffer qunarExtSb = new StringBuffer();
					//判断是否是无座坐席，若用户选择的是无座坐席并且有其他的备选坐席，优先出最低价的票
					//logger.info("============="+seat+"--------"+"0".equals(seat)+"***********"+seat.equals("0")+"++++++"+"9".equals(orderInfo.getSeat_type()));
					if("0".equals(seat) || "9".equals(orderInfo.getSeat_type())){
						logger.info("用户选择的是无座坐席并且有其他的备选坐席"+extMap);
						extSb.append(orderInfo.getSeat_type()).append(",").append(seat_price).append("|");
						qunarExtSb.append(seat).append(",").append(seat_price).append("|");
						for(String key : extMap.keySet()){
							extSb.append(TrainPropUtil.get19eSeatType(key)).append(",").append(extMap.get(key)).append("|");
							qunarExtSb.append(key).append(",").append(extMap.get(key)).append("|");
							//logger.info("【】------------无座票价："+seat_price+", 备选坐席票价："+extMap.get(key));
							if(seat_price.equals(extMap.get(key)) || Float.parseFloat(seat_price)==Float.parseFloat(extMap.get(key))){//两个坐席的价钱一样
								logger.info("【】------------19e坐席："+TrainPropUtil.get19eSeatType(key)+", qunar："+key);
								orderInfo.setSeat_type(TrainPropUtil.get19eSeatType(key));
								orderInfo.setQunar_seat_type(key);
							}
						}
					}else{
						logger.info("不是无座");
						for(String key : extMap.keySet()){
							extSb.append(TrainPropUtil.get19eSeatType(key)).append(",").append(extMap.get(key)).append("|");
							qunarExtSb.append(key).append(",").append(extMap.get(key)).append("|");
						}
					}
					orderInfo.setExt_seat(extSb.toString().substring(0, extSb.toString().length()-1));//8,9.00|63,63.00|62,60.00|61,55.00
					orderInfo.setQunar_ext_seat(qunarExtSb.toString().substring(0, qunarExtSb.toString().length()-1));//1,9.00|7,63.00|6,60.00|5,55.00
				}
				
				//普通订单
				if(StringUtils.isEmpty(order_type) || "0".equals(order_type)){
					logger.info("【申请占座接口】order_id="+orderInfo.getOrder_id()+"，普通订单");
					List<DBStudentInfo> students=new ArrayList<DBStudentInfo>();//学生票
					
					double money = 0.0;
					for (int i = 0; i < jsonPasser.size(); i++) {
						JSONObject jsonPassenger = jsonPasser.getJSONObject(i);
						ObjectMapper mapper = new ObjectMapper();
						try {
							BookPassenger bookPassenger = mapper.readValue(jsonPassenger.toString(), BookPassenger.class);
							String name = bookPassenger.getName();//乘客姓名
							String certNo = bookPassenger.getCertNo();//乘客证件号码
							String certType = bookPassenger.getCertType();//证件类型ID 1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通行证，B:护照
							String certName = bookPassenger.getCertName();//证件类型名称
							String ticketType = bookPassenger.getTicketType();//票种ID 1:成人票，2:儿童票，3:学生票
							String ticketPrice = bookPassenger.getTicketPrice();//票价
							String ticketName = bookPassenger.getTicketName();//票种名称
							String seatCode = bookPassenger.getSeatCode();//座位编码 9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，4:软卧，3:硬卧，2:软座，1:硬座
							//注意：当最低的一种座位，无票时，购买选择该座位种类，买下的就是无座(也就说买无座的席别编码就是该车次的最低席别的编码)，另外，当最低席别的票卖完了的时候才可以卖无座的票
							String seatName = bookPassenger.getSeatName();//座位名称
							String status = bookPassenger.getStatus();//身份核验状态 0：正常 1：待审核 2：未通过----目前没有身份核验，传的是null
							
							OrderInfoCp cpInfo = new OrderInfoCp();
							cpInfo.setCp_id(CreateIDUtil.createID("CP"));
							cpInfo.setOrder_id(orderNo);
							cpInfo.setUser_name(name);
							cpInfo.setTicket_type(TrainPropUtil.get19eTicketType(ticketType));//0：成人票 1：儿童票
							cpInfo.setIds_type(TrainPropUtil.getIdstype(certType));
							cpInfo.setQunar_certtype(certType);
							cpInfo.setUser_ids(certNo);
							cpInfo.setPay_money(ticketPrice);
							cpInfo.setQunar_seat_type(seatCode);
							cpInfo.setSeat_type(orderInfo.getSeat_type());
							cpList.add(cpInfo);
							money = money + Double.parseDouble(ticketPrice) + Double.parseDouble("0.0");
							
							
							if("3".equals(cpInfo.getTicket_type())){//19e:3学生票
								PassengerStudentExt passengerStudentExt = bookPassenger.getPassengerStudentExt();
								DBStudentInfo s = new DBStudentInfo();
								/*province_code string 省份编号
								school_code string 学校代号
								school_name string 学校名称
								student_no string 学号
								school_system string 学制
								enrolment_year string 入学年份
								preference_from_station_name string 优惠区间起始地名称【选填】
								preference_from_station_code string 优惠区间起始地代号
								preference_to_station_name string 优惠区间到达地名称【选填】
								preference_to_station_code string 优惠区间到达地代号*/
								s.setOrder_id(orderInfo.getOrder_id());
								s.setCp_id(cpInfo.getCp_id());
								s.setProvince_name(passengerStudentExt.getProvince());//省份名称
								s.setProvince_code(passengerStudentExt.getProvinceCode());//省份编号
								s.setSchool_code(passengerStudentExt.getSchoolCode());//学校代号
								s.setSchool_name(passengerStudentExt.getSchoolName());//学校名称
								s.setStudent_no(passengerStudentExt.getStudentNo());//学号
								s.setSchool_system(passengerStudentExt.getSchooling());//学制
								s.setEnter_year(passengerStudentExt.getIntendedTime());//入学年份：yyyy
								s.setPreference_from_station_name(passengerStudentExt.getDiscountSectionBegin());//优惠区间起始地名称【选填】
								s.setPreference_from_station_code(passengerStudentExt.getDiscountSectionBeginCode());//优惠区间起始地代号
								s.setPreference_to_station_name(passengerStudentExt.getDiscountSectionEnd());//优惠区间到达地名称【选填】
								s.setPreference_to_station_code(passengerStudentExt.getDiscountSectionEndCode());//优惠区间到达地代号
								s.setChannel("qunar");
								students.add(s);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					orderInfo.setPay_money(String.valueOf(money+""));
					//保存订单信息
					orderService.addQunarOrder(orderInfo, cpList, null, students);
					
					
					/***********向出票系统下单start*************/
					try {
						Map<String, String> paramMap = null;
						orderInfo = orderService.queryOrderInfoById(orderNo);
						logger.info("【申请占座接口】向出票系统下单order_id:"+orderNo);
						if(!TrainConsts.PAY_SUCCESS.equals(orderInfo.getOrder_status())){
							logger.info("【出票系统接口】订单状态不是支付成功,过滤该条数据，order_id="+orderInfo.getOrder_id()
									+"&order_status="+orderInfo.getOrder_status());
						}
						paramMap = new HashMap<String, String>();
						paramMap.put("orderid", orderInfo.getOrder_id());
						paramMap.put("ordername", orderInfo.getOrder_name());
						paramMap.put("paymoney", orderInfo.getPay_money());//车票总价
						paramMap.put("trainno", orderInfo.getTrain_no());
						paramMap.put("fromcity", orderInfo.getFrom_city());
						paramMap.put("tocity", orderInfo.getTo_city());
						paramMap.put("fromtime", orderInfo.getFrom_time());
						paramMap.put("totime", orderInfo.getTo_time());
						paramMap.put("traveltime", orderInfo.getTravel_time());
						paramMap.put("seattype", orderInfo.getSeat_type().substring(0, 1));
						paramMap.put("outtickettype", orderInfo.getOut_ticket_type());
						paramMap.put("channel", orderInfo.getChannel());
						paramMap.put("ispay", "11");
						StringBuffer extSb = new StringBuffer();
						extSb.append(orderInfo.getSeat_type());
						if(!StringUtils.isEmpty(orderInfo.getExt_seat())){
							extSb.append("#").append(orderInfo.getExt_seat());
						}else{
							extSb.append("#").append("无");//无备选坐席
						}
						paramMap.put("extseattype", extSb.toString());
					
						this.sendRequest(orderInfo.getOrder_id(), paramMap, orderInfo.getOrder_id());
						logger.info("【申请占座接口】下单成功，订单号为："+orderNo);
						result.put("errCode", "");
						result.put("ret", true);
						result.put("errMsg", "下单成功");
					} catch (Exception e) {
						logger.info("【申请占座接口】下单失败，订单号为："+orderNo);
						e.printStackTrace();
						result.put("errCode", "111");
						result.put("ret", false);
						result.put("errMsg", "下单失败");
					}
					/***********向出票系统下单end*************/
				}
			}else{
				logger.info("【申请占座接口】订单已存在，订单号为："+orderNo);
				result.put("errCode", "112");
				result.put("ret", false);
				result.put("errMsg", "订单已存在");
			}
		}
		
		logger.info("【申请占座接口】向qunar返回占座申请结果："+result.toString());
		write2Response(response, result.toString());
		
	}

	public String ticketType(String type){
		return "0".equals(type) ? "1" : "0"; 
	}
	
	/**
	 * 发送请求
	 * @param corder_id 子订单号
	 * @param paramMap
	 * @param order_id qunar订单号
	 * @throws Exception
	 */
	public void sendRequest(String corder_id, Map<String, String> paramMap, String order_id) throws Exception{
		List<Map<String, String>> cpInfoList = null;
		cpInfoList = orderService.queryCpInfoList(corder_id);
		orderService.updateCpSysNotifyBegin(corder_id);
		StringBuffer sb = new StringBuffer();
		for (Map<String, String> cpInfo : cpInfoList) {
			if(sb.length()>0) {
				sb.append("#");
			}
			sb.append(cpInfo.get("cp_id")).append("|").append(cpInfo.get("user_name")).append("|")
			  .append(cpInfo.get("ticket_type")).append("|").append(cpInfo.get("ids_type")).append("|")
			  .append(cpInfo.get("user_ids")).append("|").append(paramMap.get("seattype")).append("|")
			  .append(cpInfo.get("pay_money"));
		}
		paramMap.put("payTimeDeadLine", "");
		paramMap.put("seattrains", sb.toString());
		paramMap.put("backurl", notify_cp_back_url);

		String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
		
		String result = HttpUtil.sendByPost(notify_cp_interface_url, params, "UTF-8");
		logger.info("【出票系统接口】通知返回：" + result);
		
		if(!StringUtils.isEmpty(result)){
			String[] results = result.trim().split("\\|");
			
			if("success".equalsIgnoreCase(results[0]) && results.length == 2
					&& corder_id.equals(results[1])){//通知成功
				logger.info("【出票系统接口】通知出票系统成功，order_id=" + corder_id);
				orderService.updateCpSysOutNotifyEnd(corder_id, order_id);
			}else{
				logger.info("【出票系统接口】通知出票系统失败，order_id="+ corder_id + "，系统将在约1分钟后重新通知");
				//添加日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id", order_id);
				logMap.put("content", "通知出票系统失败，"+corder_id);
				logMap.put("opt_person", "qunar_app");
				orderService.addOrderInfoLog(logMap);
			}
		}
	}
	
	
	
	/**
	 * 取消占座请求接口
	 * 该接口由去哪儿网调用代理商，只有当代理商回调占座成功后，并且未出票情况下取消订单才会触发
	 * @param request
	 * @param response
	 */
	@RequestMapping("/cancelBookSeat.jhtml")
	public void cancelBookSeat(HttpServletRequest request, HttpServletResponse response){
		/**获取各账号下的数据**/
		for(InterAccount account : SysConfig.accountContainer){
			cancelBookSeatMethod(request, response, account);
		}
	}
	
	private void cancelBookSeatMethod(HttpServletRequest request,
			HttpServletResponse response, InterAccount account) {
		/**获取各账号下的数据**/
		//接收去哪儿的参数
		String orderNo = this.getParam(request,"orderNo");//去哪儿网订单号
		String reqFrom = this.getParam(request,"reqFrom");//请求来源：qunar
		String reqTime = this.getParam(request,"reqTime");//请求时间，格式：yyyyMMddhhmmss例：20140101093518
		String hmac = this.getParam(request,"HMAC");//密钥（去哪儿网分配）+从orderNo开始到reqTime结束，将所有字段值顺序拼接（值为空或null不拼接），然后用MD5加密，小写
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(orderNo)){
			logger.info("【取消占座接口】获取订单号为空");
			result.put("errCode", "112");
			result.put("ret", false);
			result.put("errMsg", "获取订单号为空");
		}else{
			//校验密钥是否正确 start
			String str = account.getMd5Key() + orderNo + reqFrom + reqTime;
			String sign = DigestUtils.md5Hex(str).toUpperCase();
			if(!hmac.equals(sign)){
				logger.info("【取消占座接口】我们需要加密的数据是："+str);
				logger.info("【取消占座接口】密钥不匹配: "+orderNo+"，接口传来的hmac="+hmac+"，我们加密的sign="+sign);
				result.put("errCode", "111");
				result.put("ret", false);
				result.put("errMsg", "密钥不匹配");
			}else{
				//向取消订单接口发送请求
				//只有当代理商回调占座成功后，并且未出票情况下取消订单才会触发
//				OrderInfo orderInfo = orderService.queryOrderInfoById(orderNo);
//				订单状态 11、支付成功 33、预订成功 44、出票成功 45、出票失败 99、取消成功
//				if("45".equals(orderInfo.getOrder_status()) || "11".equals(orderInfo.getOrder_status()) || "99".equals(orderInfo.getOrder_status())){//订单状态 11、支付成功 33、预订成功 44、出票成功 45、出票失败
//					logger.info("【取消占座接口】取消订单失败，订单状态不正确，订单号为："+orderNo+"，order_status："+orderInfo.getOrder_status()+"，返回false");
//					result.put("errCode", "111");
//					result.put("ret", false);
//					result.put("errMsg", "取消订单失败");
//				}else{
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("order_id", orderNo);
					try {
						String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
						String resultCancel = HttpUtil.sendByPost(notify_cancel_interface_url, params, "UTF-8");
						if("success".equals(resultCancel)){
							logger.info("【取消占座接口】取消订单成功，订单号为："+orderNo);
							result.put("errCode", "");
							result.put("ret", true);
							result.put("errMsg", "取消订单成功");

							Map<String ,String> map = new HashMap<String, String>();
							map.put("order_id", orderNo);
							map.put("order_status", "99");
							orderService.updateOrderWithCpNotify(map);//更新状态--取消成功
							
							//添加日志
							Map<String, String> logMap = new HashMap<String, String>();
							logMap.put("order_id", orderNo);
							logMap.put("content", "qunar发起取消占座订单[成功]，"+orderNo);
							logMap.put("opt_person", "qunar_app");
							orderService.addOrderInfoLog(logMap);
						}else{
							logger.info("【取消占座接口】取消订单失败，订单号为："+orderNo);
							result.put("errCode", "111");
							result.put("ret", false);
							result.put("errMsg", "取消订单失败");
						}
					} catch (Exception e) {
						logger.info("【取消占座接口】发送请求处理异常，订单号为："+orderNo);
						result.put("errCode", "111");
						result.put("ret", false);
						result.put("errMsg", "取消订单失败");
						e.printStackTrace();
					}
				}
//			}
		}
		write2Response(response, result.toString());
		
	}

	/**
	 * Qunar调用代理商，用户支付成功后，qunar通知代理商支付12306出票，代理商只用返回是否接收到此通知消息
     * 接收到消息则qunar扭转订单状态，否则不扭转（该通知失败的订单，代理商可通过补充的拉单机制拉取）
     * 实际的出票结果不在此返回中返回，通过2.2的出票结果接口返回。
	 * @param request
	 * @param response
	 */
	@RequestMapping("/notifyOutticket.jhtml")
	public void notifyOutticket(HttpServletRequest request, HttpServletResponse response){
		/**获取各账号下的数据**/
		for(InterAccount account : SysConfig.accountContainer){
			notifyOutticketMethod(request, response, account);
		}
	}
	
	private void notifyOutticketMethod(HttpServletRequest request,
			HttpServletResponse response, InterAccount account) {
		/**获取各账号下的数据**/
		String orderNo = this.getParam(request, "orderNo");//订单号
		String type = this.getParam(request, "Type");//通知类型: 1代表先占座后支付
		String reqFrom = this.getParam(request, "reqFrom");//请求来源：qunar
		String reqTime = this.getParam(request, "reqTime");//请求时间，格式：yyyyMMddhhmmss
		String HMAC = this.getParam(request, "HMAC");//商户密钥+订单号，将所有字段值拼接,做MD5运算,运算结果为大写
		
		String hMac = DigestUtils.md5Hex(account.getMd5Key() + orderNo + type + reqFrom + reqTime).toUpperCase();
		JSONObject result = new JSONObject();
		if(HMAC.equals(hMac)){
			logger.info("【通知出票接口】qunar通知出票占座订单[成功]"+orderNo+",发起支付订单的请求");
			//向出票系统发起支付请求
			String resultStr = payBookTicket(orderNo);
			if("success".equals(resultStr)){
				Map<String ,String> map = new HashMap<String, String>();
				map.put("order_id", orderNo);
				map.put("is_pay", "00");
				map.put("order_status", "33");
				orderService.updateOrderWithCpNotify(map);//更新状态--已支付
				
				//添加日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id", orderNo);
				logMap.put("content", "qunar通知出票占座订单[成功]，"+orderNo);
				logMap.put("opt_person", "qunar_app");
				orderService.addOrderInfoLog(logMap);
				
				result.put("ret", true);
			}
		}else{
			logger.info("【通知出票接口】密钥不匹配，去哪密钥："+HMAC+"，我们密钥："+hMac);
			result.put("errCode", "111");
			result.put("ret", false);
			result.put("errMsg", "密钥不匹配");
		}
		write2Response(response, result.toString());
	}

	public String payBookTicket(String orderNo){
		OrderInfo orderInfo = orderService.queryOrderInfoById(orderNo);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", orderNo);
		paramMap.put("pay_money", String.valueOf(orderInfo.getPay_money()));
		String params;
		String resultPay;
		String result = "";
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			resultPay = HttpUtil.sendByPost(notify_pay_interface_url, params, "UTF-8");
			if("success".equals(resultPay)){
				result = "success";
				logger.info("【通知出票接口支付】去哪订单"+orderNo+"支付成功");
			}else{
				params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				resultPay = HttpUtil.sendByPost(notify_pay_interface_url, params, "UTF-8");
				if("success".equals(resultPay)){
					result = "success";
					logger.info("【通知出票接口支付】去哪订单"+orderNo+"支付成功");
				}else{
					result = "fail";
					logger.info("【通知出票接口支付】去哪订单"+orderNo+"支付失败");
				}
			}
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	public static void main(String[] args) {
		//{"orderNo":"qunar000001","reqFrom":"qunar","reqTime":"20150129150212";"trainNo":"C6301";"from":"德阳";"to":"成都东";"date":"2015-09-02";"retUrl":"";"passengers":"";"hmac":""};
//		JSONArray arr = new JSONArray();
//		JSONObject passer = new JSONObject();
//		//passer.put("num", 1);
////		passer.put("ticketNo", "");
//		passer.put("name", "刘声");
//		passer.put("certNo", "510603199212066347");
//		passer.put("certType", "1");
//		passer.put("certName", "二代身份证");
//		passer.put("ticketType", "1");
//		passer.put("ticketPrice", "26.00");
//		passer.put("ticketName", "成人票");
//		passer.put("seatCode", "O");
//		passer.put("seatName", "二等座");
////		passer.put("seat", "");
//		passer.put("status", null);
//		arr.add(passer);
//		
//		
//		JSONObject jsonStr = new JSONObject();
//		jsonStr.put("orderNo", "qunar000001");
//		jsonStr.put("reqFrom", "qunar");
//		jsonStr.put("reqTime", "20150129150212");
//		jsonStr.put("trainNo", "C6301");
//		jsonStr.put("from", "德阳");
//		jsonStr.put("to", "成都东");
//		jsonStr.put("date", "2015-09-02");
//		jsonStr.put("retUrl", "");
//		jsonStr.put("passengers", arr.toString());
//		jsonStr.put("HMAC", "91c707af3f2a29f9c4146aee80db850c");
//		String sign = Md5Encrypt.md5("616951661AE746158A78A3E3ABBD97A3qunar000001qunar20150129150212C6301德阳成都东2015-02-02", "gbk");
//		System.out.println(sign);
//		String sign1 = Md5Encrypt.md5("27316DD7742B441CB705D210FCA8FC0Axcslw150202171557098qunar20150202171557G3北京南上海虹桥2015-02-20http://api.pub.train.dev.qunar.com/api/pub/lockseat.do", "gbk");
//		System.out.println(sign1);
//		String hMac = DigestUtils.md5Hex("27316DD7742B441CB705D210FCA8FC0Axcslw150202171557098qunar20150202171557G3北京南上海虹桥2015-02-20http://api.pub.train.dev.qunar.com/api/pub/lockseat.do").toUpperCase();
//		System.out.println(hMac);
//		System.out.println("-----"+jsonStr.toString());
////		try {
////			System.out.println(URLEncoder.encode(jsonStr.toString(), "utf-8"));
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}
		String date = "2015_10_12";
		date = DateUtil.dateToString(DateUtil.stringToDate(date, "yyyy_MM_dd"), DateUtil.DATE_FMT1);
		System.out.println(date);
	}
}
