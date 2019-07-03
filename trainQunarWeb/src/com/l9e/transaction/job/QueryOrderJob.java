package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.InterAccount;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoTrip;
import com.l9e.transaction.vo.PassengerStudentExt;
import com.l9e.transaction.vo.QunarJointTrip;
import com.l9e.transaction.vo.QunarOrder;
import com.l9e.transaction.vo.QunarOrderPack;
import com.l9e.transaction.vo.QunarPassenger;
import com.l9e.transaction.vo.SysConfig;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.DecodeJson;
import com.l9e.util.HttpUtil;
import com.l9e.util.TrainPropUtil;

/**
 * 获取订单job
 * @author zhangjun
 *
 */
@Component("queryOrderJob")
public class QueryOrderJob {
	
	private static final Logger logger = Logger.getLogger(QueryOrderJob.class);
	
	@Resource
	private OrderService orderService;
	
	
	@Resource
	private CommonService commonService;
	
	@Value("#{propertiesReader[qunarReqUrl]}")
	private String qunarReqUrl;//Qunar请求地址
	
	public void queryOrder() throws Exception{
		
		/**获取各账号下的数据**/
		for(InterAccount account : SysConfig.accountContainer){
			queryFromPerMerchant(account);
		}
		
	}
	
	private void queryFromPerMerchant(InterAccount account) throws Exception{
		
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "054500", "yyyyMMddHHmmss");//6:40
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
		if(date.before(begin) || date.after(end)){
			return;
		}
		String md5Key = account.getMd5Key();
		String merchantCode = account.getMerchantCode();
		String order_source = account.getName();
		
		String logPre = "【获取订单<"+order_source+">】";

		String type = "WAIT_TICKET";
		String hMac = DigestUtils.md5Hex(md5Key+merchantCode+type).toUpperCase();
		
		StringBuffer reqUrl = new StringBuffer();
		reqUrl.append(qunarReqUrl).append("QueryOrders.do?merchantCode=")
			.append(merchantCode).append("&type=").append(type).append("&HMAC=").append(hMac);
		logger.info("query order param="+ reqUrl.toString());
		
		String jsonStr = HttpUtil.sendByGet(reqUrl.toString(), "UTF-8", "10000", "30000");
		logger.info("query order back"+jsonStr);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		jsonStr = jsonStr.replace("null", "\"15\"");
		QunarOrderPack orderPack = mapper.readValue(jsonStr, QunarOrderPack.class);
		
		if(orderPack != null && !orderPack.isRet()){
			logger.info(logPre+"获取订单失败，errCode="+orderPack.getErrCode()+"&errMsg="+orderPack.getErrMsg());
			return;
		}
		
		logger.info(logPre+"本次获取订单的数为" + orderPack.getTotal());
		
		if(orderPack != null && orderPack.getData() != null 
				&& orderPack.getData().size()>0){
			
			for(QunarOrder qunarOrder : orderPack.getData()){
				if(StringUtils.isEmpty(qunarOrder.getOrderNo())){
					continue;
				}
				int count = orderService.queryOrderCountByNo(qunarOrder.getOrderNo());
				if(count == 0){
					List<OrderInfoCp> cpList = new ArrayList<OrderInfoCp>();
					OrderInfo orderInfo = new OrderInfo();
					
					orderInfo.setOrder_id(qunarOrder.getOrderNo());
					orderInfo.setOrder_type(qunarOrder.getOrderType());
					orderInfo.setOrder_name(qunarOrder.getDptStation()+"/"+qunarOrder.getArrStation());
					orderInfo.setPay_money(String.valueOf(qunarOrder.getTicketPay()));
					orderInfo.setOrder_status("11");//支付成功
					orderInfo.setOrder_time(qunarOrder.getOrderDate());
					orderInfo.setOut_ticket_type("11");//电子票
					
					orderInfo.setTrain_no(qunarOrder.getTrainNo());
					if("乌鲁木齐".equals(qunarOrder.getDptStation())){
						qunarOrder.setDptStation("乌鲁木齐南");
					}
					if("乌鲁木齐".equals(qunarOrder.getArrStation())){
						qunarOrder.setArrStation("乌鲁木齐南");
					}
					orderInfo.setFrom_city(qunarOrder.getDptStation());
					orderInfo.setTo_city(qunarOrder.getArrStation());
					orderInfo.setFrom_time(qunarOrder.getTrainStartTime());
					orderInfo.setTo_time(qunarOrder.getTrainEndTime());
					orderInfo.setChannel("qunar");
					orderInfo.setOrder_source(order_source);
					
					String order_type = orderInfo.getOrder_type();//0、普通订单 1、联程订单
					orderInfo.setOrder_type(order_type);
					
					//0非极速退款订单，1极速退款订单
					orderInfo.setExt_field2(qunarOrder.getSecondRefund()==null?"0":qunarOrder.getSecondRefund());
					
					String pay_money = null;//qunar给的默认坐席价格
					//解析坐席和面值
					String seat = qunarOrder.getSeat().toString();//数字---坐席
					String seatMap = qunarOrder.getSeatMap().toString();//中文坐席
					if(seat!=null && StringUtils.isNotEmpty(seat)){
						//{"0": 93}
						Map<String, String> seatNumMap = DecodeJson.decode(seat);
						for(String key : seatNumMap.keySet()){
							orderInfo.setQunar_seat_type(key);
							orderInfo.setSeat_type(TrainPropUtil.get19eSeatType(key));
							pay_money = seatNumMap.get(key);
						}
					}else{
						//{"无座": 93}
						Map<String, String> seatNumMap = DecodeJson.decode(seatMap);
						for(String key : seatNumMap.keySet()){
							orderInfo.setQunar_seat_type(key);//无座
							orderInfo.setSeat_type(TrainPropUtil.getHanzi19eSeatType(key));
							pay_money = seatNumMap.get(key);
						}
					}
					
					String ext_seat = qunarOrder.getExtSeat().toString();//[{ "0": 93}, {"1": 93}]
					String extSeatMap = qunarOrder.getExtSeatMap().toString();//[{ "无座": 93}, {"硬座": 93}]
					if(!StringUtils.isEmpty(ext_seat) && !"[]".equals(ext_seat)){
						Map<String, String> extMap = DecodeJson.decode(ext_seat);
						StringBuffer extSb = new StringBuffer();
						StringBuffer qunarExtSb = new StringBuffer();
						for(String key : extMap.keySet()){
							qunarExtSb.append(key).append(",").append(extMap.get(key)).append("|");
							extSb.append(TrainPropUtil.get19eSeatType(key)).append(",").append(extMap.get(key)).append("|");
						}
						orderInfo.setExt_seat(extSb.toString().substring(0, extSb.toString().length()-1));
						orderInfo.setQunar_ext_seat(qunarExtSb.toString().substring(0, qunarExtSb.toString().length()-1));
					}else if(!StringUtils.isEmpty(extSeatMap) && !"[]".equals(extSeatMap)){
						Map<String, String> extMap = DecodeJson.decode(extSeatMap);
						StringBuffer extSb = new StringBuffer();
						StringBuffer qunarExtSb = new StringBuffer();
						for(String key : extMap.keySet()){
							qunarExtSb.append(key).append(",").append(extMap.get(key)).append("|");
							extSb.append(TrainPropUtil.getHanzi19eSeatType(key)).append(",").append(extMap.get(key)).append("|");
						}
						orderInfo.setExt_seat(extSb.toString().substring(0, extSb.toString().length()-1));
						orderInfo.setQunar_ext_seat(qunarExtSb.toString().substring(0, qunarExtSb.toString().length()-1));
					}
					
					//普通订单
					if(StringUtils.isEmpty(order_type) || "0".equals(order_type)){
						logger.info("order_id="+orderInfo.getOrder_id()+"，普通订单");
						
						List<DBStudentInfo> students=new ArrayList<DBStudentInfo>();//学生票
						for(QunarPassenger passenger : qunarOrder.getPassengers()){
							OrderInfoCp cpInfo = new OrderInfoCp();
							cpInfo.setCp_id(CreateIDUtil.createID("CP"));
							cpInfo.setOrder_id(qunarOrder.getOrderNo());
							cpInfo.setUser_name(passenger.getName());
							cpInfo.setTicket_type(TrainPropUtil.get19eTicketType(passenger.getTicketType()));//0成人票  1儿童票   3:学生票
							cpInfo.setIds_type(TrainPropUtil.get19eIdsType(passenger.getCertType(), passenger.getCertNo()));
							cpInfo.setQunar_certtype(passenger.getCertType());
							cpInfo.setUser_ids(passenger.getCertNo());
							cpInfo.setPay_money(pay_money);
							cpList.add(cpInfo);
							
							if("3".equals(cpInfo.getTicket_type())){//19e:3学生票
								PassengerStudentExt passengerStudentExt = passenger.getPassengerStudentExt();
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
						}
						//保存订单信息
						orderService.addQunarOrder(orderInfo, cpList, null, students);
						
						
						/**通知出票系统出票*/
						try {
							String weather_work = commonService.queryQunarSysValue("weather_work");
							if(!StringUtils.isEmpty(orderInfo.getSeat_type())&&!"1".equals(weather_work)){
								//接单情况+坐席不为null情况下 直接通知出票系统
								long start=System.currentTimeMillis();
								orderService.sendCpSysOutTicket(TrainConsts.ORDER_TYPE_COMMON,orderInfo.getOrder_id());
								logger.info("qunar send to CpSysOutTicket lose time "+(System.currentTimeMillis()-start)+","+orderInfo.getOrder_id());
							}
						} catch (Exception e) {
							logger.info("qunar send to CpSysOutTicket"+e);
							e.printStackTrace();
						}
					}else{
						logger.info("order_id="+orderInfo.getOrder_id()+"，联程订单");
						List<OrderInfoTrip> tripList = new ArrayList<OrderInfoTrip>();
						List<DBStudentInfo> students=new ArrayList<DBStudentInfo>();//学生票
						
						//联程订单
						for(QunarJointTrip jointTrip : qunarOrder.getJointTrip()){
							OrderInfoTrip trip = new OrderInfoTrip();
							String trip_id = qunarOrder.getOrderNo()+"_"+jointTrip.getSeq();
							//trip.setTrip_id(CreateIDUtil.createID("QLC"));
							trip.setTrip_id(trip_id);
							trip.setOrder_id(qunarOrder.getOrderNo());
							trip.setTrip_seq(jointTrip.getSeq());
							trip.setOrder_name(jointTrip.getDptStation()+"/"+jointTrip.getArrStation());
							//trip.setPay_money(String.valueOf(qunarOrder.getTicketPay()));
							trip.setOrder_status("11");//支付成功
							trip.setTrain_no(jointTrip.getTrainNo());
							trip.setFrom_city(jointTrip.getDptStation());
							trip.setTo_city(jointTrip.getArrStation());
							trip.setFrom_time(jointTrip.getTrainStartTime());
							trip.setTo_time(jointTrip.getTrainEndTime());
							trip.setChannel("qunar");
							trip.setOrder_source(order_source);
							
							//解析坐席和面值
							String trip_seat = jointTrip.getSeat().toString();
							Map<String, String> trip_seatMap = DecodeJson.decode(trip_seat);
							for(String key : trip_seatMap.keySet()){
								trip.setQunar_seat_type(key);							
								trip.setSeat_type(TrainPropUtil.get19eSeatType(key));
								pay_money = trip_seatMap.get(key);
							}
							String trip_ext_seat = jointTrip.getExtSeat().toString();
							if(!StringUtils.isEmpty(trip_ext_seat) && !"[]".equals(trip_ext_seat)){
								Map<String, String> extMap = DecodeJson.decode(trip_ext_seat);
								StringBuffer extSb = new StringBuffer();
								StringBuffer qunarExtSb = new StringBuffer();
								for(String key : extMap.keySet()){
									qunarExtSb.append(key).append(",").append(extMap.get(key)).append("|");
									extSb.append(TrainPropUtil.get19eSeatType(key)).append(",").append(extMap.get(key)).append("|");
								}
								trip.setExt_seat(extSb.toString().substring(0, extSb.toString().length()-1));
								trip.setQunar_ext_seat(qunarExtSb.toString().substring(0, qunarExtSb.toString().length()-1));
							}
							tripList.add(trip);
							
							double money = 0;
							for(QunarPassenger passenger : qunarOrder.getPassengers()){
								OrderInfoCp cpInfo = new OrderInfoCp();
								cpInfo.setCp_id(CreateIDUtil.createID("CP"));
								cpInfo.setOrder_id(trip_id);
								cpInfo.setUser_name(passenger.getName());
								cpInfo.setTicket_type(TrainPropUtil.get19eTicketType(passenger.getTicketType()));//0成人票  1儿童票   3:学生票
								cpInfo.setIds_type(TrainPropUtil.get19eIdsType(passenger.getCertType(), passenger.getCertNo()));
								cpInfo.setQunar_certtype(passenger.getCertType());
								cpInfo.setUser_ids(passenger.getCertNo());
								cpInfo.setPay_money(pay_money);
								money += Double.parseDouble(pay_money);
								cpList.add(cpInfo);
								
								if("3".equals(cpInfo.getTicket_type())){//19e:3学生票
									PassengerStudentExt passengerStudentExt = passenger.getPassengerStudentExt();
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
							}
							trip.setPay_money(String.valueOf(money));
						}
						
						
						
						//保存订单信息
						orderService.addQunarOrder(orderInfo, cpList, tripList, students);
					}
				}
					
				
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		String str = "{\"Y\": 120.00}";
		//String str ="[ {\" Y\": 120.00}, {\"E\": 18.00} ]";
		Map<String, String> map = DecodeJson.decode(str);
		for(String a : map.keySet()){
			System.out.println(a);
			System.out.println(map.get(a));
		}
	}
	
	public String ticketType(String type){
		return "0".equals(type) ? "1" : "0"; 
	}
	
}
