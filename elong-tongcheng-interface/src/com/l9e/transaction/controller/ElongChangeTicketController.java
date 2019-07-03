package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseController;
import com.l9e.common.Consts;
import com.l9e.common.ElongChangeConsts;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.ElongChangeService;
import com.l9e.transaction.service.ElongOrderService;
import com.l9e.transaction.vo.ElongChangeInfo;
import com.l9e.transaction.vo.ElongChangeLogVO;
import com.l9e.transaction.vo.ElongChangePassengerInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.util.DateUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.ParamCheckUtil;
import com.l9e.util.elong.ElongConsts;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/ticket")
public class ElongChangeTicketController extends BaseController {

	private static final Logger logger = Logger.getLogger(ElongChangeTicketController.class);
	
	@Resource
	private CommonService commonService;
	@Resource
	private ElongChangeService changeService;
	@Resource
	private ElongOrderService elongOrderService;
	
	

	@RequestMapping(value = "/externalInterface.do")
	public void ExternalInterface(HttpServletRequest request, HttpServletResponse response) {

		logger.info("请求参数列表：" + getFullURL(request));
		String type = this.getParam(request, "type");

		if ("requestChange".equals(type)) {
			requestChange(request, response);// 请求改签
		} else if ("cancelChange".equals(type)) {
			cancelChange(request, response);// 取消改签
		} else if ("confirmChange".equals(type)) {
			confirmChange(request, response);// 确认改签
		} else {
			logger.info("hcp对外接口-非法的type接口参数 ：type=" + type);
			logger.info("请求参数异常type:" + type);
			printJson(response, getJson("003").toString());
		}

	}

	/**
	 * 请求改签
	 */
	public void requestChange(HttpServletRequest request, HttpServletResponse response) {
		logger.info("elong请求改签开始");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String md_str = merchant_id + timestamp + type + json_param;
		String order_id = "";
		String msg = "";
		// 改签日志
		ElongChangeLogVO log = new ElongChangeLogVO();
		logger.info("elong请求改签接口-用户参数：" + md_str);
		// 检查通用参数
		if (checkUniversalParam(request, response)) {
			try {
				logger.info("elong请求改签接口-用户:登入成功!");

				JSONObject json_params = JSONObject.fromObject(json_param);
				// 验证业务参数
				order_id = json_params.getString("order_id");// 订单号
				String from_station_code = json_params.getString("from_station_code");// 出发站简称
				String from_station_name = json_params.getString("from_station_name");// 出发站名称
				String to_station_code = json_params.getString("to_station_code");// 到达站简称
				String to_station_name = json_params.getString("to_station_name");// 到达站名称
				String isChangeTo = json_params.getString("isChangeTo");// 0：改签
																		// 1：变更到站
				String out_ticket_billno = json_params.getString("out_ticket_billno");// 出票单号
																						// 12306单号
				String change_train_no = json_params.getString("change_train_no");// 改签后车次
				String change_from_time = json_params.getString("change_from_time");// 改签后发车时间
				
				String seat_type = json_params.getString("seat_type");// 座位类型
				seat_type=ElongConsts.get19eSeatType(seat_type);// 转换为系统的坐席类型
				String change_seat_type = json_params.getString("change_seat_type");// 改签后座位类型
				change_seat_type=ElongConsts.get19eSeatType(change_seat_type);// 转换为系统的坐席类型
				
				String callbackurl = json_params.getString("callbackurl");// 回调地址
				String reqtoken = json_params.getString("reqtoken");// 唯一标识
				JSONArray tickets = json_params.getJSONArray("ticketinfo");
				
				Boolean isChooseSeats = false;   //true:选座  ,false：不选座			
				if(json_params.containsKey("isChooseSeats")) {
					isChooseSeats=json_params.getBoolean("isChooseSeats");
				}
				String chooseSeats="";
				if(json_params.containsKey("chooseSeats")) { //选座信息
					chooseSeats=json_params.getString("chooseSeats");
				}
				logger.info("order_id:"+order_id+",isChooseSeats:"+isChooseSeats+",chooseSeats:"+chooseSeats);
				
				msg = isChangeTo.equals("1") ? "变更到站" : "改签";
				logger.info("elong" + msg + ",orderId : " + order_id + "车票信息 : " + tickets);

				/* 业务参数检查 */
				if ("".equals(order_id) || "".equals(out_ticket_billno) || "".equals(change_train_no)
						|| "".equals(change_from_time) || StringUtil.isEmpty(seat_type) || StringUtil.isEmpty(change_seat_type)
						|| "".equals(tickets) || "".equals(from_station_name) || "".equals(to_station_name)) {
					logger.info("elong请求" + msg + "ERROR,参数有空order_id:" + order_id);
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_003).toString());
					return;
				}

				/* 查询订单原票信息 */
				Map<String,Object> orderInfoMap = elongOrderService.queryOrderInfo(order_id);
				
				if (null==orderInfoMap) {
					/* 订单不存在 */
					logger.info("elong请求" + msg + "ERROR,订单不存在order_id:" + order_id);
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_801).toString());
					return;
				}
				
				String orderStatus=(String) orderInfoMap.get("order_status");

				/* 检查订单状态 */
				if (!Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)) {
					/* 出票成功才可以改签 */
					logger.info("elong请求" + msg + "ERROR,订单状态不正确，订单号为" + order_id);
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_802).toString());
					return;
				}
				/* 查询该订单号下的改签特征值，排除重复请求 */
				ElongChangeInfo reqtokenChangeInfo = changeService.getChangeInfoByReqtoken(reqtoken);
				if (reqtokenChangeInfo != null) {
					logger.info("elong请求" + msg + "该请求已存在，reqtoken为" + reqtoken);
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_803).toString());
					return;
				}
				
				String orderName=(String) orderInfoMap.get("order_name");
				String[] fromArrive=orderName.split("/");
				String fromStation=fromArrive[0];
			    String arriveStation=fromArrive[1];
				logger.info(order_id+",出发站:"+fromStation+",到达站："+arriveStation);
				
				// 出发站验证
				if (!from_station_name.equals(fromStation)) {
					logger.info("elong-请求" + msg + "ERROR,出发站不相同");
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_103).toString());
					return;
				}
				
				/* 改签时间验证 */
				Date old_from_time = DateUtil.stringToDate((String)orderInfoMap.get("from_time"), DateUtil.DATE_FMT3);//出发时间
				
				//变更到站
				if (isChangeTo.equals("1")) {
					if (DateUtil.minuteDiff(old_from_time, new Date()) < 48 * 60) {
						/* 距离发车时间小于48小时 */
						logger.info("elong-请求变更到站ERROR,距离开车时间太近无法变更到站");
						printJson(response, getJson(ElongChangeConsts.RETURN_CODE_805).toString());
						return;
					}
				} else {
					if (DateUtil.minuteDiff(old_from_time, new Date()) < 30) {
						/* 距离发车时间小于30分 */
						logger.info("elong-请求改签ERROR,距离开车时间太近无法改签");
						printJson(response, getJson(ElongChangeConsts.RETURN_CODE_804).toString());
						return;
					}

				}

				/* 车票信息 */
				if (tickets.size() == 0) {
					logger.info("elong-请求" + msg + "ERROR,没有车票信息");
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_205).toString());
					return;
				}
				if (tickets.size() > 1) {
					/* 批量改签 */
					if (seat_type.equals(TrainConsts.SEAT_4) || seat_type.equals(TrainConsts.SEAT_5)
							|| seat_type.equals(TrainConsts.SEAT_6) || change_seat_type.equals(TrainConsts.SEAT_4)
							|| change_seat_type.equals(TrainConsts.SEAT_5)
							|| change_seat_type.equals(TrainConsts.SEAT_6)) {
						/* 批量改签原票坐席不能为卧铺 */
						logger.info("elong-请求改签ERROR,批量改签原票或新票坐席不能为卧铺");
						printJson(response, getJson(ElongChangeConsts.RETURN_CODE_806).toString());
						return;
					}
				}
				
				/* 组装改签车票信息 */
				ElongChangeInfo changeInfo = new ElongChangeInfo();
				List<ElongChangePassengerInfo> changePassengers = new ArrayList<ElongChangePassengerInfo>();
				for (int i = 0; i < tickets.size(); i++) {
					/* 传入的参数数据 */
					JSONObject ticket = tickets.getJSONObject(i);
					List<ElongChangePassengerInfo> cps = changeService.getChangeCpById(ticket.getString("old_ticket_no"));
					if (cps != null) {
						for (ElongChangePassengerInfo changeCp : cps) {
							/* 每张车票只能改签一次 */
							if (changeCp.getIs_changed().equals("Y")) {
								logger.info("elong-请求改签ERROR,车票已改签过,车票id:" + changeCp.getCp_id());
								printJson(response, getJson(ElongChangeConsts.RETURN_CODE_807).toString());
								return;
							}
						}
					}
					ElongChangePassengerInfo cp = new ElongChangePassengerInfo();
					String new_cp_id = ticket.getString("new_ticket_no");
					cp.setOrder_id(order_id);// 订单id
					cp.setCp_id(ticket.getString("old_ticket_no"));// 车票id(原票)
					cp.setNew_cp_id(new_cp_id);// 改签后车票id
					cp.setChange_seat_type(change_seat_type);// 19e改签后新座位席别
					cp.setSeat_type(seat_type);
					cp.setIs_changed("N");

					/* 原车票信息 */
					ElongOrderInfoCp p = elongOrderService.queryElongCpInfoByCpId(cp.getCp_id());
					if (p != null) {
						cp.setBuy_money(p.getBuy_money());// 成本价格
						cp.setSeat_no(p.getSeat_no());// 座位号
						cp.setSeat_type(p.getSeat_type());// 座位席别
						cp.setTrain_box(p.getTrain_box());// 车厢
						cp.setTicket_type(p.getTicket_type());// 车票类型
						cp.setIds_type(p.getIds_type());// 证件类型
						cp.setUser_ids(p.getUser_ids());// 证件号码
						cp.setUser_name(p.getUser_name());// 乘客姓名
					}
					changePassengers.add(cp);
				}
				String trainNo=(String) orderInfoMap.get("train_no");
				/* 组装改签记录信息 */
				changeInfo.setChange_travel_time(change_from_time);// 改签后乘车日期
				changeInfo.setTrain_no(trainNo);// 车次
				changeInfo.setChange_train_no(change_train_no);// 改签后车次
				changeInfo.setFrom_time((String)orderInfoMap.get("from_time"));// 发车时间
				changeInfo.setChange_from_time(change_from_time);// 改签后发车时间
				changeInfo.setFrom_city(from_station_name);// 出发车站
				changeInfo.setTo_city(to_station_name);// 到达车站
				changeInfo.setFrom_station_code(from_station_code);
				changeInfo.setTo_station_code(to_station_code);
				changeInfo.setIschangeto(new Integer(isChangeTo));
				changeInfo.setOut_ticket_billno(out_ticket_billno);// 12306单号
				changeInfo.setOrder_id(order_id);
				changeInfo.setIsasync("Y");// 异步
				changeInfo.setCallbackurl(callbackurl);
				changeInfo.setReqtoken(reqtoken);
				changeInfo.setChange_status(ElongChangeConsts.TRAIN_REQUEST_CHANGE);// 11改签预定
				
				changeInfo.setIsChooseSeats(isChooseSeats?1:0);
				changeInfo.setChooseSeats(chooseSeats);
				
				changeInfo.setcPassengers(changePassengers);// 改签、车票关系
				Map<String, String> cpParam = new HashMap<String, String>();
				cpParam.put("order_id", order_id);
				Integer acc_id = (Integer) elongOrderService.queryAccountOrderInfo(cpParam).get("acc_id");
				changeInfo.setAccount_id(acc_id.toString());// 出票账号id
				changeInfo.setChange_notify_count(0);
				changeInfo.setChange_notify_status(ElongChangeConsts.CHANGE_NOTIFY_PRE);
				changeInfo.setMerchant_id(merchant_id);
				/* 改签信息入库 */
				changeService.addChangeInfo(changeInfo);
				int change_id = changeInfo.getChange_id();
				log.setChange_id(change_id);
				logger.info("elong请求" + msg + "异步成功,orderId : " + order_id + "车票信息 : " + tickets);
				log.setContent("elong请求" + msg + "异步success");
				log.setOrder_id(order_id);
				log.setOpt_person(merchant_id);
				changeService.addChangeLog(log);
				// 请求成功同步返回结果
				JSONObject json = new JSONObject();
				json.put("return_code", "000");
				json.put("message", "请求改签请求成功");
				json.put("ticketinfo", tickets);
				printJson(response, json.toString());
			} catch (Exception e) {
				logger.info("elong请求" + msg + "异常" , e);
				e.printStackTrace();
				log.setContent("elong请求" + msg + "异常!");
				log.setOrder_id(order_id);
				log.setOpt_person(merchant_id);
				changeService.addChangeLog(log);
				printJson(response, getJson(TrainConsts.RETURN_CODE_001).toString());
			}
		}
	
	}

	/**
	 * 取消改签
	 * 
	 * @param request
	 * @param response
	 */
	public void cancelChange(HttpServletRequest request, HttpServletResponse response) {

		logger.info("elong取消改签开始");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String md_str = merchant_id + timestamp + type + json_param;
		String order_id = "";
		// 改签日志
		ElongChangeLogVO log = new ElongChangeLogVO();
		logger.info("elong取消改签接口-用户参数：" + md_str);
		if (this.checkUniversalParam(request, response)) {
			try {
				logger.info("elong取消改签接口登入成功!");
				// 业务参数
				JSONObject json_params = JSONObject.fromObject(json_param);
				order_id = json_params.getString("order_id");// 订单号
				String callbackurl = json_params.getString("callbackurl");// 回调地址
				String reqtoken = json_params.getString("reqtoken");// 唯一标识
				logger.info("elong,取消改签,orderId : " + order_id + "reqtoken: " + reqtoken);
				/* 检查业务参数 */
				if ("".equals(order_id) || "".equals(callbackurl)) {
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_003).toString());
					return;
				}
					
				/* 查询订单 */
				Map<String, Object> orderInfoMap = elongOrderService.queryOrderInfo(order_id);
				if (orderInfoMap == null ) {
					/* 订单不存在 */
					logger.info("elong-取消改签ERROR,订单不存在order_id:" + order_id);
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_301).toString());
					return;
				}

				ElongChangeInfo changeInfo = changeService.getChangeInfoByReqtoken(reqtoken);
				if (!changeInfo.getChange_status().equals(ElongChangeConsts.TRAIN_REQUEST_CHANGE_SUCCESS)) {
					/* 订单状态不正确 */
					logger.info("elong-取消改签ERROR,订单状态不正确order_id:" + order_id);
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_802).toString());
					return;
				}

				log.setChange_id(changeInfo.getChange_id());
				log.setOrder_id(order_id);
				log.setOpt_person("elong");
				Date currentTime = new Date();
				/* 预订成功后的30分钟内才能取消改签 */
				Date bookTime = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
				long minuteDiff = DateUtil.minuteDiff(currentTime, bookTime);
				if (minuteDiff > 30) {
					logger.info("elong-取消改签ERROR,距离改签车票预订时间超过30分钟");
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_811).toString());
					return;
				}
				/* 将状态为14、预订成功的改签状态都改为21、改签取消 */
				changeInfo.setChange_status(ElongChangeConsts.TRAIN_CANCEL_CHANGE);
				changeInfo.setChange_notify_count(0);
				changeInfo.setChange_notify_status(ElongChangeConsts.CHANGE_NOTIFY_PRE);
				changeInfo.setCallbackurl(callbackurl);
				changeInfo.setReqtoken(reqtoken);
				/* 更新改签状态 */
				changeService.updateChangeInfo(changeInfo);

				//请求成功同步返回结果
				Map<String, Object> changePassParam = new HashMap<String, Object>();
				changePassParam.put("change_id", changeInfo.getChange_id());
				List<ElongChangePassengerInfo> cPassengers = changeService.getChangePassenger(changePassParam);
				JSONArray tickets = new JSONArray();
				for (ElongChangePassengerInfo cPassenger : cPassengers) {
					JSONObject jsonPass = new JSONObject();
					jsonPass.put("user_name", cPassenger.getUser_name());
					jsonPass.put("user_ids", cPassenger.getUser_name());
					jsonPass.put("old_ticket_no", cPassenger.getCp_id());
					jsonPass.put("new_ticket_no", cPassenger.getNew_cp_id());
					tickets.add(jsonPass);
				}
				JSONObject json = new JSONObject();
				json.put("return_code", "000");
				json.put("message", "取消改签请求成功");
				json.put("ticketinfo", tickets);
				printJson(response, json.toString());
				logger.info("elong取消改签异步成功,orderId : " + order_id);
				log.setContent("elong请求取消改签异步success");
			} catch (Exception e) {
				logger.info("elong取消改签异常" + e);
				e.printStackTrace();
				printJson(response, getJson(ElongChangeConsts.RETURN_CODE_001).toString());
				log.setContent("elong取消改签异常!");
			} finally {
				log.setOpt_person(merchant_id);
				changeService.addChangeLog(log);
			}
		}

	}

	/**
	 * 确认改签
	 */
	public void confirmChange(HttpServletRequest request, HttpServletResponse response) {
		logger.info("elong确认改签开始");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String md_str = merchant_id + timestamp + version + type + json_param;
		String order_id = "";
		//改签日志
		ElongChangeLogVO log = new ElongChangeLogVO();
		logger.info("确认改签接口-用户参数：" + md_str);
		if (this.checkUniversalParam(request, response)) {
			try {
				// 业务参数
				JSONObject json_params = JSONObject.fromObject(json_param);
				order_id = json_params.getString("order_id");// 订单号
				String callbackurl = json_params.getString("callbackurl");// 回调地址
				String reqtoken = json_params.getString("reqtoken");// 唯一标识
				logger.info("elong"+",确认改签,orderId : " + order_id + "reqtoken: " + reqtoken);
				/* 检查业务参数 */
				if ("".equals(order_id) || "".equals(callbackurl)) {
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_003).toString());
					return;
				}
				/* 查询订单 */
				Map<String, Object> orderInfoMap = elongOrderService.queryOrderInfo(order_id);
				if (orderInfoMap == null) {
					/* 订单不存在 */
					logger.info("elong-确认改签ERROR,订单不存在order_id:" + order_id);
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_301).toString());
					return;
				}
				/* 查询改签预订票并更新状态 */
				ElongChangeInfo changeInfo = changeService.getChangeInfoByReqtoken(reqtoken);
				if (changeInfo == null
						|| !changeInfo.getChange_status().equals(ElongChangeConsts.TRAIN_REQUEST_CHANGE_SUCCESS)) {
					/* 订单状态不正确 */
					logger.info("elong-确认改签ERROR,订单状态不正确order_id:" + order_id);
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_802).toString());
					return;
				}

				log.setChange_id(changeInfo.getChange_id());
				log.setOrder_id(changeInfo.getOrder_id());
				log.setOpt_person("elong");
				/* 22:34:59 */
				Calendar time_22_44_59 = Calendar.getInstance();
				time_22_44_59.set(Calendar.HOUR_OF_DAY, 22);
				time_22_44_59.set(Calendar.MINUTE, 44);
				time_22_44_59.set(Calendar.SECOND, 59);
				/* 23:30:00 */
				Calendar time_23_30_00 = Calendar.getInstance();
				time_23_30_00.set(Calendar.HOUR_OF_DAY, 23);
				time_23_30_00.set(Calendar.MINUTE, 30);
				time_23_30_00.set(Calendar.SECOND, 00);
				Calendar currentTime = Calendar.getInstance();
				Calendar bookTime = Calendar.getInstance();
				Date book = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
				bookTime.setTime(book);

				boolean timeOut = false;
				if (bookTime.before(time_22_44_59)) {// 当天22:44:59之前预订
					/* 30分钟的付款时间 */
					System.out.println("current : " + DateUtil.dateToString(currentTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
					System.out.println("book : " + DateUtil.dateToString(book, "yyyy-MM-dd HH:mm:ss"));
					if (DateUtil.minuteDiff(currentTime.getTime(), book) > 30) {
						timeOut = true;
					}
					logger.info("当天22:44:59之前预订,30分钟的付款时间,timeout" + timeOut);
				} else if (bookTime.after(time_22_44_59)) {// 当天22:44:59之后预订
					/* 23:30:00之前付款 */
					if (currentTime.after(time_23_30_00)) {
						timeOut = true;
					}
					logger.info("当天22:44:59之后预订,23:30:00之前付款,timeout" + timeOut);
				}
				if (timeOut) {
					logger.info("elong-确认改签ERROR,确认改签的请求时间已超过规定时间");
					printJson(response, getJson(ElongChangeConsts.RETURN_CODE_810).toString());
					return;
				}
			
				changeInfo.setChange_status(ElongChangeConsts.TRAIN_CONFIRM_CHANGE);
				changeInfo.setCallbackurl(callbackurl);
				changeInfo.setReqtoken(reqtoken);
				changeInfo.setChange_notify_count(0);
				changeInfo.setChange_notify_status(ElongChangeConsts.CHANGE_NOTIFY_PRE);
				logger.info(order_id + ",callbackurl:" + changeInfo.getCallbackurl() + ",reqtoken:"
						+ changeInfo.getReqtoken());
				/* 更新车票改签状态 */
				int count = changeService.updateChangeInfo(changeInfo);
				logger.info(order_id + ",callbackurl:" + changeInfo.getCallbackurl() + ",reqtoken:"
						+ changeInfo.getReqtoken() + ",count:" + count);
				log.setContent("elong,请求改签支付异步success ,orderId" + order_id);
				// 请求成功同步返回结果
				Map<String, Object> changePassParam = new HashMap<String, Object>();
				changePassParam.put("change_id", changeInfo.getChange_id());
				List<ElongChangePassengerInfo> cPassengers = changeService.getChangePassenger(changePassParam);
				JSONArray tickets = new JSONArray();
				for (ElongChangePassengerInfo cPassenger : cPassengers) {
					JSONObject jsonPass = new JSONObject();
					jsonPass.put("user_name", cPassenger.getUser_name());
					jsonPass.put("user_ids", cPassenger.getUser_name());
					jsonPass.put("old_ticket_no", cPassenger.getCp_id());
					jsonPass.put("new_ticket_no", cPassenger.getNew_cp_id());
					tickets.add(jsonPass);
				}
				JSONObject json = new JSONObject();
				json.put("return_code", "000");
				json.put("message", "确认改签请求成功");
				json.put("ticketinfo", tickets);
				printJson(response, json.toString());
				logger.info("elong,确认改签异步成功,orderId : " + order_id);
			} catch (Exception e) {
				logger.info("elong-确认改签异常" + e);
				e.printStackTrace();
				printJson(response, getJson(TrainConsts.RETURN_CODE_001).toString());
				log.setContent("elong,请求改签支付异常!");
			} finally {
				changeService.addChangeLog(log);
			}
		}
	
		
	}
	
	
	// 检查通用参数
	private Boolean checkUniversalParam(HttpServletRequest request, HttpServletResponse response) {
			logger.info("检查elong请求的通用参数");
			
			String merchant_id = this.getParam(request, "merchant_id");
			String timestamp = this.getParam(request, "timestamp");
			String type = this.getParam(request, "type");
			String json_param = this.getParam(request, "json_param");
			String hmac = this.getParam(request, "hmac");
			String md_str = merchant_id + timestamp +  type + json_param;
			if (StringUtil.isEmpty(merchant_id) || StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				    || StringUtil.isEmpty(hmac) || StringUtil.isEmpty(json_param)
					|| ParamCheckUtil.isNotCheck(timestamp, ParamCheckUtil.TIMESTAMP_REGEX)) {
				logger.info("参数校验error:必要参数为空或格式错误!");
				printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
				return false;
			}
			logger.info("用户传递hmac：【" + hmac + "】");
			// 加密明文
			String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str + ElongChangeConsts.SIGNKEY, "", "utf-8");

			logger.info("系统加密hmac：【" + hmac_19 + "】");
			if (!hmac_19.equals(hmac)) {
				logger.info("安全校验error:不符合安全校验规则。");
				printJson(response, getJson(TrainConsts.RETURN_CODE_002).toString());
				return false;
			}
			return true;
	}
	
	
}
