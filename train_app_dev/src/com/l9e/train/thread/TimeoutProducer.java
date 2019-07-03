package com.l9e.train.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.CtripAcc;
import com.l9e.train.po.Order;
import com.l9e.train.producerConsumer.distinct.DistinctProducer;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.Consts;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;
import com.l9e.train.util.MemcachedUtil;
import com.l9e.train.util.WorkIdNum;

public class TimeoutProducer extends DistinctProducer<Order> {

	private Logger logger = LoggerFactory.getLogger(TimeoutProducer.class);

	@Override
	public String getObjectKeyId(Order orderbill) {
		return orderbill.getOrderId();
	}

	@Override
	public List<Order> getProducts() {

		TrainServiceImpl dao = new TrainServiceImpl();

		logger.info("get Timeout list start");
		List<Order> list = null;
		// 查询需要发送的类
		int ret = -1;
		try {
			int num = 10;
			if (null == MemcachedUtil.getInstance().getAttribute("robot_app_product_num")) {
				num = dao.queryTrainSystemSettingNum(6, "robot_app_book_num");
				MemcachedUtil.getInstance().setAttribute("robot_app_product_num", num, 2 * 60 * 1000);
			} else {
				num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("robot_app_product_num")));
			}
			logger.info("one time book orders: " + num);

			// 帐号未购票天数
			if (null == MemcachedUtil.getInstance().getAttribute("robot_no_order_day")) {
				WorkIdNum.no_order_day = dao.queryTrainSystemSettingNum(0, "robot_no_order_day");
				MemcachedUtil.getInstance().setAttribute("robot_no_order_day", WorkIdNum.no_order_day, 5 * 60 * 1000);
			} else {
				WorkIdNum.no_order_day = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("robot_no_order_day")));
			}
			logger.info("robot_no_order_day: " + WorkIdNum.no_order_day);
			// 限制帐号下单次数
			if (null == MemcachedUtil.getInstance().getAttribute("robot_app_book_num")) {
				WorkIdNum.book_num = dao.queryTrainSystemSettingNum(0, "robot_app_book_num");
				MemcachedUtil.getInstance().setAttribute("robot_app_book_num", WorkIdNum.book_num, 5 * 60 * 1000);
			} else {
				WorkIdNum.book_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("robot_app_book_num")));
			}
			logger.info("robot_app_book_num: " + WorkIdNum.book_num);

			// 常用联系人上限个数
			if (null == MemcachedUtil.getInstance().getAttribute("contact_num")) {
				WorkIdNum.contact_num = dao.queryTrainSystemSettingNum(0, "contact_num");
				MemcachedUtil.getInstance().setAttribute("contact_num", WorkIdNum.contact_num, 60 * 1000);
			} else {
				WorkIdNum.contact_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("contact_num")));
			}
			logger.info("contact_num: " + WorkIdNum.contact_num);

			// 19e订单是否开启携程预订模式
			/*
			 * if(null ==
			 * MemcachedUtil.getInstance().getAttribute("l9e_order_channel")){
			 * WorkIdNum.l9e_order_channel =
			 * dao.queryTrainSystemSettingNum(0,"l9e_order_channel");
			 * MemcachedUtil.getInstance().setAttribute("l9e_order_channel",
			 * WorkIdNum.l9e_order_channel, 60*1000); }else{
			 * WorkIdNum.l9e_order_channel =
			 * Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().
			 * getAttribute("l9e_order_channel"))); }
			 */
			WorkIdNum.l9e_order_channel = dao.queryTrainSystemSettingNum(0, "l9e_order_channel");
			logger.info("l9e_order_channel: " + WorkIdNum.l9e_order_channel);

			ret = dao.orderbillByList(num);

			if (ret == 0) {
				list = dao.getOrderbill();
			}

			// 测试用
			// list = new ArrayList<Order>();
			// Order orderTest = new Order();
			// orderTest.setOrderId("20140928206372865");
			// orderTest.setPaymoney("14");
			// orderTest.setBuymoney("2222.2");
			// orderTest.setOrderStatus("00");
			// orderTest.setChannel("19e");
			// orderTest.setManual_order("22");
			// orderTest.setOutTicketBillNo("E350768");
			// orderTest.setTrainno("6951");
			// orderTest.setFrom("哈尔滨");
			// orderTest.setTo("哈尔滨东");
			// orderTest.setTravel_time("2016-07-21 00:00:00");
			// orderTest.setSeatType("8");
			//
			// StringBuffer cp=new StringBuffer();
			// cp.append("20140928206372866").append("|")
			// .append("张玉朋").append("|")
			// .append("0").append("|")
			// .append("2").append("|")
			// .append("330724197905107126");
			// orderTest.addOrderCp(cp.toString());
			//
			// list.add(orderTest);

			// //分配账号ID
			// ElongTcAccData data = new ElongTcAccData();
			// //补充elong,tongcheng账号池
			// data.startPoolLoad(dao);
			// NorQunAccData nqdata = new NorQunAccData();
			// //补充qunar,19e账号池
			// nqdata.startPoolLoad(dao);

			// 先获取出票模式
			SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
			String l9eOutTicketSelect = "00"; // 出票模式：00：普通出票模式 11： 手工出票模式 22 ：
												// 携程APP出票模式（不买保险） 33
												// ：携程PC出票模式（买保险） 44：京东出票模式

			try {
				sysImpl.querySysVal("19e_OutTicket_Select");

				if (StringUtils.isNotEmpty(sysImpl.getSysVal())) {
					l9eOutTicketSelect = sysImpl.getSysVal();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("生产者出票模式为：" + l9eOutTicketSelect);

			List<String> orderCps = null;
			for (Order order : list) {

				/** 19e出票模式匹配 */
				if ("19e".equals(order.getChannel())
						&& ("00".equals(order.getOrderStatus()) || ("01".equals(order.getOrderStatus()) && !"00".equals(order.getManual_order())))) {

					// 当出票模式为携程APP出票时， 每次间隔5秒重新查询携程账号，大于3次后还是查不到可用的携程账号就走12306流程
					boolean isCtrip22Model = false; // 预订时是否走携程app端标识，默认走12306
					// int queryNum = 0;// 重复查询次数
					CtripAcc ctripAcc = null; // 携程账号实体

					if ("22".equals(l9eOutTicketSelect)) {

						// 当订单为预订时，才查询携程账号
						if ("00".equals(order.getOrderStatus())) {
							Map<String, String> paramMap = new HashMap<String, String>();
							paramMap.put("order_id", order.getOrderId());
							paramMap.put("pay_money", order.getPaymoney());// 订单支付金额

							String ctrip_balance = dao.getSysSettingValue("ctrip_balance");// 携程订单金额和余额最小差值
							String ctrip_order_num_min = dao.getSysSettingValue("ctrip_order_num_min");// 携程已出票订单个数最小值
							String ctrip_order_num_max = dao.getSysSettingValue("ctrip_order_num_max");// 携程已出票订单个数最大值

							paramMap.put("ctrip_balance", ctrip_balance);
							paramMap.put("ctrip_order_num_min", ctrip_order_num_min);
							paramMap.put("ctrip_order_num_max", ctrip_order_num_max);

							dao.getCtripAccInfo(paramMap);
							ctripAcc = dao.getCtripAcc();
							logger.info("携程账号ctripAcc为：" + ctripAcc);
						}

						if (ctripAcc != null) {
							isCtrip22Model = true;
						} else {
							isCtrip22Model = false;
						}

					}

					orderCps = order.getOrderCps();
					logger.info("车票信息orderCps为：" + orderCps);
					Double payMoney = Double.parseDouble(order.getPaymoney());// 支付金额
					logger.info("车票支付金额payMoney为：" + payMoney);
					String seatType = order.getSeatType();// 座位类别
					logger.info("车票座位类别seatType为：" + seatType);

					String fromTime = order.getSeattime();// 列车发车时间
					logger.info("列车发车时间为：" + fromTime);
					Date date = new Date();// 当前时间
					String format = "yyyy-MM-dd HH:mm:ss"; // 转换的格式
					long diffDays = 0;// 相差的天数
					DateUtil dateUtil = new DateUtil();
					if (null != fromTime && !"".equals(fromTime)) {
						diffDays = dateUtil.twoDateDiffDays(date, fromTime, format);
						logger.info("列车发车时间与当前时间相差的天数为：" + diffDays);
					}

					for (String orderCp : orderCps) {
						// 车票类型0：成人票 1：儿童票
						// cp_id|user_name|ticket_type|cert_type..
						// 证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
						String[] cps = orderCp.split("\\|");
						if ("22".equals(l9eOutTicketSelect)) {
							if ("0".equals(cps[2]) && ("1".equals(cps[3]) || "2".equals(cps[3]))) {
								if (isCtrip22Model) {
									// 当订单为预订时，才查询携程账号，如果有可用的账号，则改出票模式为22，并赋值
									order.setManual_order("22");// 携程APP出票
									order.setCtripAcc(ctripAcc);// 给携程账号赋值

								} else {
									// 当订单为重发时，不用做任何操作；当订单为预订时，并且没有查询到可用的携程账号，则走12306出票
									if (!"01".equals(order.getOrderStatus())) {
										order.setManual_order("00"); // 12306出票
										break;
									}
								}
							} else {
								// 如果携程订单中有非成人票和非一，二代身份证的情况，则需要解锁携程账号
								dao.updateCtripAccStatus(order.getOrderId()); // 携程账号解锁操作
								order.setManual_order("00");
								break;
							}
						} else if ("33".equals(l9eOutTicketSelect)) {
							if ("0".equals(cps[2]) && ("1".equals(cps[3]) || "2".equals(cps[3])) && diffDays > 3) {
								order.setManual_order("33");// 携程PC出票
							} else {
								order.setManual_order("00");
								break;
							}

						} else if ("44".equals(l9eOutTicketSelect)) {
							if ("0".equals(cps[2]) && !("5".equals(seatType) || "6".equals(seatType)) && payMoney > 100) {
								order.setManual_order("44");// 京东出票
							} else {
								order.setManual_order("00");
								break;
							}
						} else {
							order.setManual_order("00");// 12306出票
							break;
						}

					}

					/** 身份证 成人票 筛选 */
					// if(WorkIdNum.l9e_order_channel==2){
					// orderCps=order.getOrderCps();
					// for(String orderCp: orderCps){
					// //车票类型0：成人票 1：儿童票
					// //cp_id|user_name|ticket_type|cert_type..
					// //证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
					// String[] cps=orderCp.split("\\|");
					// if("0".equals(cps[2])&&("1".equals(cps[3])||"2".equals(cps[3]))){
					// order.setManual_order("22");//携程出票
					// }else{
					// order.setManual_order("00");//12306出票
					// break;
					// }
					// }
					// }else{
					// order.setManual_order("00");//12306出票
					// }
				} else {
					order.setManual_order("00");// 12306出票
				}
				if ("19e".equals(order.getChannel())) {
					logger.info("l9e order channel order_id:" + order.getOrderId() + "status: " + order.getManual_order());
				}

				if (order.getChannel().equals("test")) {
					/* 从账号服务获取账号 */
					int acc_id = 0;
					int worker_id = -1;

					boolean accAbled = true;
					if (order.getAcc_id() != 0) {
						// 验证acc_id存在
						if (0 == dao.weatherUsed(order.getAcc_id())) {
							accAbled = false;
							acc_id = order.getAcc_id();
						}
					}

					if (accAbled) {
						String get_acc_str = null;
						/** 往账号服务 请求获取账号 */
						try {
							long get_acc_start = System.currentTimeMillis();
							get_acc_str = HttpUtil.sendByGet(Consts.GET_ACCOUNT_URL + "?channel=" + order.getChannel(), "UTF-8", "5000", "5000");
							logger.info("get a account:" + get_acc_str + ",lose time:" + (System.currentTimeMillis() - get_acc_start) + "ms");
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("send acc_service Exception " + e);
							get_acc_str = null;
						}

						if (get_acc_str != null && !"".equals(get_acc_str)) {
							// 账号处理 545214|223 12306账号id|预订机器人id
							if (get_acc_str.contains("|")) {
								String[] get_acc_str_arr = get_acc_str.split("\\|");
								acc_id = Integer.parseInt(get_acc_str_arr[0]);
								if (get_acc_str_arr.length > 1) {
									// 分离账号内绑定预订机器人worker_id
									worker_id = Integer.parseInt(get_acc_str_arr[1]);
								}
							} else {
								acc_id = Integer.parseInt(get_acc_str);
							}
						} else {
							logger.info("get acc from acc_service error :" + get_acc_str);
						}
						order.setAcc_id(acc_id);
						order.setWorkerId(worker_id);
					}
					/* 从账号服务获取账号end */
					logger.info("---------->>>order_id:" + order.getOrderId() + " : acc_id:" + acc_id);
				} else {
					logger.info("test_tongcheng -- skip old account obtain");
				}

			}
			logger.info("end get Timeout list:" + list.size());
		} catch (Exception e) {
			logger.info("getProducts Repate exception" + e);
		}
		return list;

	}

}
