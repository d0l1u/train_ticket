package com.l9e.train.thread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.l9e.train.channel.request.IRequest;
import com.l9e.train.channel.request.RequestFactory;
import com.l9e.train.main.App;
import com.l9e.train.po.Account;
import com.l9e.train.po.DeviceWeight;
import com.l9e.train.po.ErrorInfo;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.l9e.train.po.WorkerWeight;
import com.l9e.train.producerConsumer.distinct.DistinctConsumer;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class TimeoutConsumer extends DistinctConsumer<Order> {

	private Logger logger = LoggerFactory.getLogger(TimeoutConsumer.class);

	private static final Random WEIGHT_RANDOM = new Random();

	private TrainServiceImpl trainService = new TrainServiceImpl();

	@Override
	public boolean consume(Order order, String logid) {
		// 设备权重 APP or Pc
		String deviceWeight = deviceWeight(logid);
		// 脚本的权重
		String languageWeight = workerWeight(logid);

		logger.info("{}【设备权重】:{}", logid, deviceWeight);
		logger.info("{}【语言权重】:{}", logid, languageWeight);
		return consumeOrder(order, deviceWeight, languageWeight, null, logid);
	}

	/**
	 * 消费定订单
	 * 
	 * @author: taoka
	 * @date: 2018年3月9日 下午3:16:33
	 * @param order
	 * @param deviceWeight
	 * @param languageWeight
	 * @param request
	 * @param logid
	 * @return boolean
	 */
	private boolean consumeOrder(Order order, String deviceWeight, String languageWeight, IRequest request, String logid) {

		boolean resendFlag = false;
		String orderstr = order.getOrderstr();
		String orderId = order.getOrderId();
		Integer if12306Cutover = order.getIf12306CutOver();
		logger.info("{}orderStr:{}", logid, orderstr);

		logger.info("{}非12306出票模式的订单是否要切入12306出票的标识if12306Cutover为:{} [0:否, 1:是]", logid, if12306Cutover);
		if (if12306Cutover != null) {
			if (1 == if12306Cutover.intValue()) {
				order.setManual_order("00");// 00 : 12306出票
			}
		}

		try {
			String orderStatus = order.getOrderStatus();
			String manualOrder = order.getManual_order();
			String channel = order.getChannel();
			logger.info("{}orderStatus:{}", logid, orderStatus);
			logger.info("{}manualOrder:{}", logid, manualOrder);
			logger.info("{}channel:{}", logid, channel);
			String optlog = "";
			if ("00,01".contains(orderStatus)) {
				// 如果状态为开始订购或者重发出票
				if ("22".equals(manualOrder)) {
					optlog = "train_app正在预订，选择预订方式，[携程预订]";
				} else if ("44".equals(manualOrder)) {
					optlog = "train_app正在预订，选择预订方式，[京东预订]";
				} else {
					optlog = "train_app正在预订，选择预订方式";
				}
			} else {
				optlog = "预订状态异常，状态为:" + orderStatus;
			}
			trainService.insertHistory(orderId, optlog);

			// start 选择账号、处理人员和处理类
			logger.info("{}选择账号、处理人员和处理类...", logid);
			// 携程订的处理:是否为人工出票：00普通出票模式 11 手工出票模式 22 携程出票模式（不买保险）33 携程出票模式（买保险）
			if (("22".equals(manualOrder) || "33".equals(manualOrder)) && "19e".equals(channel)) {
				logger.info("{}携程出票模式...", logid);
				request = new RequestFactory().select19e(order);
				return consumel9e(order, request, logid);
			} else if ("44".equals(manualOrder) && "19e".equals(channel)) {
				logger.info("{}京东出票模式...", logid);
				request = new RequestFactory().selectJD(order);
				return consumeJD(order, request);
			} else {
				logger.info("{}机器人出票模式...", logid);
			}

			if (request == null) {
				request = new RequestFactory().select(order, languageWeight, logid);
			}

			if (request == null) {
				trainService.orderIsManual(order, null);
				return false;
			}

			Account account = request.getAccount();
			String username = account.getUsername();
			String password = account.getPassword();

			Worker worker = request.getWorker();
			Integer workerId = worker.getWorkerId();
			String workerName = worker.getWorkerName();
			String requestUrl = worker.getWorkerExt();

			try {
				trainService.insertHistory(orderId, workerName + ",进行预订");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 记录操作关系
			try {
				trainService.recordRelation(orderId, username, workerId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String orderSeatType = order.getSeatType();
			String extSeatType = order.getExtSeatType();
			logger.info("{}首选坐席:{}", logid, orderSeatType);
			logger.info("{}备选坐席:{}", logid, extSeatType);
			order.setWea_wz(false);

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("seattype", orderSeatType);
			list.add(map);
			boolean w_qunar = false;
			if ("qunar".equals(channel)) {
				w_qunar = true;
			}
			String extSeatTypeStr = "";
			if (StringUtils.isNotBlank(extSeatType) && extSeatType.split("#").length > 1) {
				extSeatTypeStr = extSeatType.split("#")[1];
			}

			if (extSeatTypeStr.length() > 0 && !("无").equals(extSeatTypeStr)) {
				String[] extTypeArr = extSeatTypeStr.split("\\|");
				for (String str : extTypeArr) {
					if (("9").equals(str.split(",")[0])) {
						// 备选无座
						order.setWea_wz(true);
						logger.info("{}back up the no seat!!", logid);
						continue;
					}

					String temp = str.split(",")[0];
					if (w_qunar) {
						if (temp.contains("4") || temp.contains("5") || temp.contains("6")) {
							continue;
						}
					} else {
						map = new HashMap<String, String>();
						map.put("seattype", temp);
						// map.put("price",str.split(",")[1]);
						list.add(map);
					}
				}
			}
			// //63#9,18|62,115|61,112

			logger.info("{}LIST:{}", logid, JSONObject.toJSONString(list));
			Result result = null;
			for (Map<String, String> mapInfo : list) {
				order.setSeatType(mapInfo.get("seattype"));

				// orderbill.setPaymoney(map_n.get("price"));

				Integer workerReportId = null;
				try {
					// 记录机器人使用日志
					workerReportId = trainService.startWorkerReport(worker, order, "1");

					// 进行机器人请求
					result = request.request(order, deviceWeight, logid);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					trainService.endWorkerReport(workerReportId);
				}

				// 预定中取消检测
				orderStatus = trainService.getOrderStatus(orderId);
				if (Order.STATUS_CANCEL_PRE.equals(orderStatus)) {
					logger.info("{}预订中检测订单正在取消...", logid);
					optlog = "订单申请取消，停止占座转开始取消";
					trainService.insertHistory(orderId, optlog);
					trainService.updateOrderStatus(orderId, Order.STATUS_CANCEL_START);
					return true;
				}

				// TODO
				Integer accountFromWay = order.getAccountFromWay();
				String retValue = result.getRetValue();
				String errorInfo = result.getErrorInfo();
				optlog = workerName;
				// 对处理后的结果的返回值，进行订单的调整
				if (retValue.equals(Result.QUEUE)) {// 排队
					optlog = optlog + ",排队订单，等待重发！【" + errorInfo + "】";
					trainService.orderIsQueue(order, result);
					break;
				} else if (retValue.equals(Result.SUCCESS)) {// 成功
					optlog = optlog + ",预定成功，进入支付过程！";
					trainService.orderIsSuccess(order, result);
					break;
				} else if (retValue.equals(Result.RESEND)) {// 重发
					if (accountFromWay.intValue() == 1 && errorInfo.contains("登录名不存在")) {
						logger.info("{}自带12306账号订单，传入的12306账号不存在！", logid);
						optlog = optlog + ",预定失败！【" + errorInfo + "】";
						result.setErrorInfo("自带12306账号订单，传入的12306账号不存在！");
						result.setFailReason(ErrorInfo.ACCOUNT_NO_EXIST);
						result.setRetValue(Result.FAILURE);
						break;
					} else if (accountFromWay.intValue() == 1 && errorInfo.contains("密码输入错误")) {
						logger.info("{}自带12306账号订单，传入的12306账号登录密码错误！", logid);
						optlog = optlog + ",预定失败！【" + errorInfo + "】";
						result.setErrorInfo("自带12306账号订单，传入的12306账号登录密码错误！");
						result.setFailReason(ErrorInfo.PASSWORD_WRONG);
						result.setRetValue(Result.FAILURE);
						break;
					} else if (accountFromWay == 0) {
						if (errorInfo.indexOf("脚本执行异常") > -1) {
							logger.info("{}脚本执行异常 立即自动重发", logid);
							optlog = optlog + ",脚本执行异常 立即自动重发";
							resendFlag = true;
							break;
						} else {
							logger.info("{}order is resend!", logid);
							optlog = optlog + ",预定重发！【" + errorInfo + "】";
							trainService.orderIsResend(order, result);
							break;
						}
					} else {
						logger.info("{}自带12306账号订单，{}", logid, errorInfo);
						result.setErrorInfo("自带12306账号订单！" + errorInfo);
						optlog = optlog + ",预定重发！【" + errorInfo + "】";
						resendFlag = true;
						break;
					}
				} else if (retValue.equals(Result.WAIT)) {// 排队
					optlog = optlog + ",排队订单！【" + errorInfo + "】";
					logger.info("{}order is wait!", logid);
					trainService.orderIsWait(order, result);
					break;
				} else if (retValue.equals(Result.FAILURE)) {// 失败
					optlog = optlog + ",预定失败！【" + errorInfo + "】";
					logger.info("{}order is failure!", logid);
					break; // 2016年6月1号改为break；
				} else if (retValue.equals(Result.CANCEL)) {// 取消
					optlog = optlog + ",预订取消！【" + errorInfo + "】";
					logger.info("{}order is cancel!", logid);
					trainService.orderIsCancel(order, result);
					break;
				} else if (retValue.equals(Result.MANUAL)) {// 如果异常，在25分钟内重查，超时变成人工处理
					logger.info("{}进入转人工流程...", logid);
					optlog = optlog + ",预定人工处理！【" + errorInfo + "】";
					if (accountFromWay.intValue() == 1 && (errorInfo.contains("未完成订单") || errorInfo.contains("请等待其支付完成后"))) {
						logger.info("{}自带12306账号订单，传入的12306账号中存在未完成订单！", logid);
						optlog = optlog + ",预定失败！【" + errorInfo + "】";
						result.setErrorInfo("自带12306账号订单，传入的12306账号中存在未完成订单！");
						result.setFailReason(ErrorInfo.EXIST_UNFINISHED_ORDER);
						result.setRetValue(Result.FAILURE);
						break;
					} else if (accountFromWay.intValue() == 1 && errorInfo.contains("登录名不存在")) {
						logger.info("{}自带12306账号订单，传入的12306账号不存在！", logid);
						optlog = optlog + ",预定失败！【" + result.getErrorInfo() + "】";
						result.setErrorInfo("自带12306账号订单，传入的12306账号不存在！");
						result.setFailReason(ErrorInfo.ACCOUNT_NO_EXIST);
						result.setRetValue(Result.FAILURE);
						break;
					} else if (accountFromWay.intValue() == 1 && errorInfo.contains("密码输入错误")) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号登录密码错误！");
						result.setFailReason(ErrorInfo.PASSWORD_WRONG);
						result.setRetValue(Result.FAILURE);
						logger.info(logid + " 自带12306账号订单，传入的12306账号登录密码错误！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;
					} else if (accountFromWay == 0) {
						// 扣票失败转PC端重发
						if (result.getErrorInfo() != null && result.getErrorInfo().indexOf("app扣票失败") > -1) {
							optlog = result.getWorker().getWorkerName() + "，app扣票失败 立即自动重发";
							logger.info(logid + " app扣票失败 立即自动重发");
							resendFlag = true;
							break;
						} else if (errorInfo.contains("姓名格式错误")) {
							optlog = result.getWorker().getWorkerName() + "，姓名格式错误 立即自动转pc重发";
							logger.info(logid + " 姓名格式错误 立即自动转pc重发");
							resendFlag = true;
							break;
						} else if (errorInfo.contains("排队异常")) {
							// TODO
							optlog = result.getWorker().getWorkerName() + "，app排队异常， 立即自动转pc重发";
							logger.info(logid + " app排队异常 立即自动转pc重发");
							resendFlag = true;
							break;
						} else if (errorInfo.contains("耐心等待 ")) {
							optlog = result.getWorker().getWorkerName() + "，您的订单已经提交，请耐心等待 ， 立即自动转pc重发";
							logger.info(logid + " 您的订单已经提交,请耐心等待, 立即自动转pc重发");
							resendFlag = true;
							break;
						} else {
							logger.info(logid + " order is manual!");
							trainService.orderIsManual(order, result);
							break;
						}
					} else {
						if (errorInfo.contains("app扣票失败")) {
							optlog = result.getWorker().getWorkerName() + "，自带12306账号订单，app扣票失败 立即自动重发";
							logger.info(logid + " 自带12306账号订单， app扣票失败 立即自动重发");
							resendFlag = true;
							break;
						} else if (errorInfo.contains("姓名格式错误")) {
							optlog = result.getWorker().getWorkerName() + "，自带12306账号订单，姓名格式错误 立即自动转pc重发";
							logger.info(logid + " 自带12306账号订单，姓名格式错误 立即自动转pc重发");
							resendFlag = true;
							break;
						} else if (errorInfo.contains("排队异常")) {
							optlog = result.getWorker().getWorkerName() + " 自带12306账号订单，app排队异常， 立即自动转pc重发";
							logger.info(logid + " 自带12306账号订单，app排队异常 立即自动转pc重发");
							resendFlag = true;
							break;
						} else if (errorInfo.contains("耐心等待 ")) {
							optlog = result.getWorker().getWorkerName() + "自带12306账号订单，您的订单已经提交，请耐心等待 ， 立即自动转pc重发";
							logger.info(logid + " 自带12306账号订单,您的订单已经提交,请耐心等待, 立即自动转pc重发");
							resendFlag = true;
							break;
						} else {
							result.setErrorInfo("自带12306账号订单！" + errorInfo);
							logger.info(logid + " 自带12306账号订单，" + errorInfo);
							optlog = result.getWorker().getWorkerName() + "，预定人工！【" + result.getErrorInfo() + "】";
							trainService.orderIsManual(order, result);
							break;
						}
					}
				} else if (retValue.equals(Result.STOP)) {
					if (null != accountFromWay && 1 == accountFromWay && (errorInfo.contains("密码输入错误") || errorInfo.contains("锁定"))) {
						result.setErrorInfo("自带12306账号订单，密码输入错误");
						result.setFailReason(ErrorInfo.PASSWORD_WRONG);
						result.setRetValue(Result.FAILURE);
						logger.info(logid + " 自带12306账号订单，密码输入错误");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;
					} else if (null != accountFromWay && 1 == accountFromWay && errorInfo.contains("取消次数过多")) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号取消次数过多！");
						result.setFailReason(ErrorInfo.CANCEL_NUMBER_TOMUCH);
						result.setRetValue(Result.FAILURE);
						logger.info(logid + " 自带12306账号订单，传入的12306账号取消次数过多！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;
					} else if (null != accountFromWay && 1 == accountFromWay && errorInfo.contains("上限")) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号联系人个数达上限！");
						result.setFailReason(ErrorInfo.USERNUMBERS_OVER_TOPLIMIT);
						result.setRetValue(Result.FAILURE);
						logger.info(logid + " 自带12306账号订单，传入的12306账号联系人个数达上限！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;

					} else if (null != accountFromWay && 1 == accountFromWay && errorInfo.contains("手机核验")) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号联系人手机核验不通过！");
						result.setFailReason(ErrorInfo.MOBILEPHONE_NO_VERIFY);
						result.setRetValue(Result.FAILURE);
						logger.info(logid + " 自带12306账号订单，传入的12306账号联系人手机核验不通过！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;

					} else if (null != accountFromWay && 1 == accountFromWay && errorInfo.contains("重新注册")) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号联系人**需要重新注册！");
						result.setRetValue(Result.FAILURE);
						result.setFailReason(ErrorInfo.MOBILEPHONE_NO_VERIFY);
						logger.info(logid + " 自带12306账号订单，传入的12306账号联系人**需要重新注册！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;

					} else if (null != accountFromWay && 1 == accountFromWay && errorInfo.contains("账号状态为：未通过")) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号状态为未通过！");
						result.setRetValue(Result.FAILURE);
						result.setFailReason(ErrorInfo.ACCOUNT_WAIT_VERIFY);
						logger.info(logid + " 自带12306账号订单，传入的12306账号状态为未通过！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;
					} else if (null != accountFromWay && 1 == accountFromWay && errorInfo.contains("未通过核验，不能添加常用联系人")) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号未通过核验，不能添加常用联系人！");
						result.setRetValue(Result.FAILURE);
						result.setFailReason(ErrorInfo.ACCOUNT_WAIT_VERIFY);
						logger.info(logid + " 自带12306账号订单，传入的12306账号未通过核验，不能添加常用联系人！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;

					} else if (null != accountFromWay && 1 == accountFromWay && errorInfo.contains("通过后即可在网上购票")) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号**通过后即可在网上购票！");
						result.setRetValue(Result.FAILURE);
						result.setFailReason(ErrorInfo.ACCOUNT_WAIT_VERIFY);
						logger.info(logid + " 自带12306账号订单，传入的12306账号**通过后即可在网上购票！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;

					} else if (null != accountFromWay && 1 == accountFromWay && errorInfo.contains("身份证件号码填写是否正确")) {
						result.setErrorInfo("自带12306账号订单，请确认传入的12306账号联系人的身份证件号码填写是否正确！");
						result.setRetValue(Result.FAILURE);
//						result.setFailReason(ErrorInfo.PAPERS_WRONG);
						logger.info(logid + " 自带12306账号订单，请确认传入的12306账号联系人的身份证件号码填写是否正确！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;

					} else if (0 == accountFromWay) {
						// 取消订单过多，暂时停用
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						logger.info(logid + " order is stop!");
						trainService.orderIsStop(order, result);
						break;
					} else {
						result.setErrorInfo("自带12306账号订单！" + errorInfo);
						result.setRetValue(Result.FAILURE);
						logger.info(logid + " 自带12306账号订单，" + errorInfo);
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;
					}
				} else if (retValue.equals(Result.END)) {
					if (null != accountFromWay && 1 == accountFromWay) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号待核验！");
						result.setFailReason(ErrorInfo.ACCOUNT_WAIT_VERIFY);
						result.setRetValue(Result.FAILURE);
						logger.info(logid + " 自带12306账号订单，传入的12306账号待核验！");
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						break;
					} else {
						// 封停
						optlog = result.getWorker().getWorkerName() + "，预定失败！【" + result.getErrorInfo() + "】";
						logger.info(logid + " order is end!");
						trainService.orderIsEnd(order, result);
						break;
					}
				} else if (retValue.equals(Result.NOPASS)) {
					StringBuffer sb = new StringBuffer();
					// 身份证,成人,姓名，状态 0、已通过 1、待审核 2、未通过
					logger.info(logid + "passenger info error:" + result.getErrorInfo());
					String[] passengers = result.getErrorInfo().split("\\|");

					for (String passenger : passengers) {
						if (StringUtils.isNotEmpty(passenger)) {
							String[] str = passenger.split(",");
							if (!"0".equals(str[3])) {
								if (sb.length() == 0) {
									sb.append(result.getWorker().getWorkerName()).append("，预定人工处理！【").append(str[2]).append("（").append(str[0]).append("）")
											.append(str[3].equals("1") ? "待审核" : "未通过").append("】");
									optlog = sb.toString();
								}
							} else {// 包含添加成功的常用联系人
									// 常用联系人添加成功需要添加到账号过滤表中
								result.setInsertFilter(true);
								result.setFilterScope(Result.FILTER_PART);
							}
						}
					}
					logger.info(logid + " order is nopass!");
					result.setErrorInfo(result.getErrorInfo());
					// service.orderIsFailure(orderbill, result);
					break;
				} else {// 异常
					optlog = result.getWorker().getWorkerName() + "，预定异常！【" + result.getErrorInfo() + "】";
					logger.warn(result.getSelfId() + " find order is exception, restor find!");
					break;
				}
			}

			String retValue = result.getRetValue();
			logger.info("{}发送失败后订单的处理-begin", logid);
			if (retValue.equals(Result.FAILURE) || retValue.equals(Result.NOPASS)) {
				trainService.orderIsFailure(order, result, logid);
			}

			logger.info("{}Insert History ...", logid);
			trainService.insertHistory(orderId, optlog);

			// 账号是否需要加入账号过滤表cp_accountinfo_filter
			if (result.isInsertFilter()) {
				logger.info("{}Add Account Filter ...", logid);
				trainService.addAccountFilter(order, result);
			}

			// 更新常用联系人个数
			String contactsNumStr = result.getContactsNum();
			if (StringUtils.isNotBlank(contactsNumStr)) {
				logger.info("{}开始更新常用联系人个数 ,返回的联系人个数为:{}", logid, contactsNumStr);
				Integer contactsNum = Integer.valueOf(contactsNumStr);
				if (contactsNum.intValue() != 0) {
					trainService.updateContactsNum(result);
				}
			}

			// 是否需要更新基础车票价格
			logger.info("{}是否需要更新基础车票价格 ...", logid);
			if (retValue.equals(Result.SUCCESS) || retValue.equals(Result.CANCEL)) {
				// 更新常用联系人对应账号信息
				if (retValue.equals(Result.CANCEL)) {
					result.setFilterScope(Result.FILTER_ALL);
					try {
						trainService.addAccountFilter(order, result);
					} catch (Exception e) {
						logger.error("{}【Add Account Filter Exception】", logid, e);
					}
				}

				logger.info("{}Update Base Price ...", logid);
				try {
					if (!order.isWea_price()) {
						int re = trainService.updateBasePrice(order, result);
						if (re != 1) {
							trainService.insertHistory(orderId, "更新车票基础票价完成！");
						}
					} else {
						logger.info("{}has children don't need update price!", logid);
					}
				} catch (Exception e) {
					logger.info("{}【Update Base Price Exception】", logid, e);
				}
			}

			/* 停用被封停机器人 */
			String errorInfo = result.getErrorInfo();
			if (errorInfo.contains("操作频率过快") || errorInfo.contains("机器ip可能被封锁")) {
				logger.info("{}机器人被封停。停用机器人 workerId:{}", logid, workerId);
				App.workerService.stopWorker(workerId);
			}
		} catch (Exception e) {
			logger.info("{}【consumeOrder() Exception】", logid, e);
		} finally {
			if (request != null) {
				Integer orderWorkerId = order.getWorkerId();
				try {
					if (orderWorkerId > 0) {
						logger.info("{}释放机器人至空闲状态 Order Worker ID:{}", logid, orderWorkerId);
						App.workerService.releaseWorker(orderWorkerId);
					}
					Worker worker = request.getWorker();
					if (worker != null) {
						Integer workerId = worker.getWorkerId();
						if (!workerId.equals(orderWorkerId)) {
							logger.info("{}释放机器人至空闲状态 Worker ID:{}", logid, workerId);
							App.workerService.releaseWorker(workerId);
						}
					}
				} catch (Exception e) {
					logger.info("{}【Release Worker Exception】", logid, e);
				}
			}

			if (resendFlag) {
				logger.info("{}进入立即重发,订单号:{}", logid, orderId);
				consumeOrder(order, "pc", "lua", request, logid);
			} else {
				Integer accountFromWay = order.getAccountFromWay();
				if (accountFromWay.intValue() != 1) {
					logger.info("{}Insert Pass Acc ...", logid);
					insertPassAcc(request, trainService);
				}
			}
		}
		return true;
	}

	private boolean consumel9e(Order order, IRequest request, String logid) {
		String optlog = "";
		logger.info("{}consumel9e...", logid);
		String orderId = order.getOrderId();
		try {
			if (request == null) {
				trainService.orderIsManual(order, null);
				logger.info("{}没有空闲账号...", logid);
				optlog = "没有空闲帐号！";
				trainService.insertHistory(orderId, optlog);
				return false;
			}

			Result result = request.request(order, null, logid);
			String retValue = result.getRetValue();
			String errorInfo = result.getErrorInfo();

			logger.info("{}result.getRetValue():{}", logid, retValue);
			if (retValue.equals(Result.SUCCESS)) {// 订单表改为成功，通知表改为正在通知
				optlog = "携程预定请求成功,等待出票结果";
				logger.info("{}order is success!", logid);
				trainService.orderIsSuccessByCtrip(order, result);
				// service.orderIsSuccess(orderbill, result);
			} else if (retValue.equals(Result.MANUAL)) {
				optlog = "携程预定人工处理！【" + errorInfo + "】";
				logger.info("{}order is fail!", logid, errorInfo);
				trainService.orderIsManualByCtrip(order, result);
			} else {
				optlog = "返回未知的结果状态【" + errorInfo + "】";
				logger.info("{}返回未知的结果状态:{}", logid, retValue);
				trainService.orderIsManualByCtrip(order, result);
			}
			trainService.insertHistory(orderId, optlog);
		} catch (Exception e) {
			logger.info("{}【系统异常】:consumel9e()发生异常", logid, e);
		}
		return true;
	}

	/**
	 * 京东出票模式预定结果处理
	 * 
	 * @param order
	 * @param service
	 * @param request
	 * @return
	 */
	private boolean consumeJD(Order order, IRequest request) {
		String optlog = "";
		String logid = "0000";
		logger.info(logid + "-consumeJD...");
		try {
			if (request == null) {
				trainService.orderIsManualByJD(order, null);
				logger.info(logid + " not Account or not jdAccount");
				optlog = "12306账号，京东账号，京东预付卡可能没有获取到，请确定！";
				trainService.insertHistory(order.getOrderId(), optlog);
				return false;
			}

			Result result = request.request(order, null, logid);
			String retValue = result.getRetValue();
			logger.info(logid + "-result.getRetValue():" + retValue);
			if (StringUtils.equals(retValue, Result.SUCCESS)) {// 订单表改为成功，通知表改为正在通知
				optlog = result.getWorker().getWorkerName() + "，京东预定请求成功,等待出票结果【" + result.getErrorInfo() + "】";
				logger.info(logid + " order is success!");
				trainService.orderIsSuccessByJD(order, result);
			} else if (StringUtils.equals(retValue, Result.MANUAL)) {
				optlog = result.getWorker().getWorkerName() + "，京东预定失败,转人工！【" + result.getErrorInfo() + "】";
				logger.info(logid + " order is fail!" + result.getRetValue());
				trainService.orderIsManualByJD(order, result);
			} else if (StringUtils.equals(retValue, Result.RESEND)) {
				optlog = result.getWorker().getWorkerName() + "，京东预定无变化重发！【" + result.getErrorInfo() + "】";
				logger.info(logid + " order is resend!" + result.getRetValue());
				trainService.orderIsResendByJD(order, result, 0);
			} else if (StringUtils.equals(retValue, Result.CHANGE_ACCOUNT_RESEND)) {
				optlog = result.getWorker().getWorkerName() + "，京东预定重发,先切换12306账号再重发！【" + result.getErrorInfo() + "】";
				logger.info(logid + " order is resend!" + result.getRetValue());
				trainService.orderIsResendByJD(order, result, 1);
			} else if (StringUtils.equals(retValue, Result.CHANGE_JDACCOUNT_RESEND)) {
				optlog = result.getWorker().getWorkerName() + "，京东预定重发,先切换京东账号再重发！【" + result.getErrorInfo() + "】";
				logger.info(logid + " order is resend!" + result.getRetValue());
				trainService.orderIsResendByJD(order, result, 2);
			} else if (StringUtils.equals(retValue, Result.CHANGE_JDCARD_RESEND)) {
				optlog = result.getWorker().getWorkerName() + "，京东预定重发,先切换京东预付卡号再重发！【" + result.getErrorInfo() + "】";
				logger.info(logid + " order is resend!" + result.getRetValue());
				trainService.orderIsResendByJD(order, result, 3);
			} else {
				optlog = "返回未知的结果状态【" + result.getRetValue() + "】";
				logger.info(logid + " order is fail!" + result.getRetValue());
				trainService.orderIsManualByJD(order, result);
			}
			trainService.insertHistory(order.getOrderId(), optlog);

			/**
			 * 插入cp_pass_acc表新数据，以此更新白名单和账号表的联系人个数
			 */
			insertPassAcc(request, trainService);

		} catch (Exception e) {
			logger.info(logid + "-【系统异常】:consumeJD发生异常", e);
		}
		return true;
	}

	@Override
	public String getObjectKeyId(Order t) {
		// TODO Auto-generated method stub
		return t.getOrderId();
	}

	/**
	 * 权重
	 * 
	 * @param logid
	 */
	private String deviceWeight(String logid) {
		/* PC&APP 模式权重分配 */
		String defaultWeightMode = DeviceWeight.WEIGHT_PC;
		logger.info("{}设备权重...", logid);
		try {
			String pcWeightValue = trainService.getSysSettingValue("device_mode_" + DeviceWeight.WEIGHT_PC);// pc权重
			String appWeightValue = trainService.getSysSettingValue("device_mode_" + DeviceWeight.WEIGHT_APP);// app权重

			logger.info("{}权重系统设置PC端:{}; APP移动端:{}", logid, pcWeightValue, appWeightValue);
			/* 设置权重置 */
			List<DeviceWeight> modeCategories = new ArrayList<DeviceWeight>();// 放各个权重的集合

			DeviceWeight pcMode = new DeviceWeight(DeviceWeight.WEIGHT_PC, Integer.parseInt(pcWeightValue));// pc权重
			DeviceWeight appMode = new DeviceWeight(DeviceWeight.WEIGHT_APP, Integer.parseInt(appWeightValue));// app权重

			modeCategories.add(pcMode);
			modeCategories.add(appMode);

			int count = 0;
			for (DeviceWeight category : modeCategories) {
				count += category.getWeight();
			}
			logger.info("{}权重最大边界 count:{}", logid, count);
			Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0,
																// weightSum)
			logger.info("{}权重随机值 nchannel:{}", logid, nchannel);
			Integer mchannel = 0;
			for (DeviceWeight weightCategory : modeCategories) {
				if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
					defaultWeightMode = weightCategory.getCategory();
					break;
				}
				mchannel += weightCategory.getWeight();
			}
		} catch (Exception e) {
			logger.info("{}【系统异常】:获取设备权重发生异常", logid, e);
		}
		return defaultWeightMode;
	}

	// 填充变动账号表，进一步更新白名单表
	private void insertPassAcc(IRequest request, TrainServiceImpl service) {
		if (request != null && request.getAccount() != null && request.getWorker() != null && request.getAccount().getId() != null
				&& request.getAccount().getUsername() != null && request.getAccount().getPassword() != null && request.getWorker().getWorkerExt() != null
				&& !"".equals(request.getWorker().getWorkerExt())) {
			String acc_id = request.getAccount().getId().toString();
			logger.info("acc_id" + acc_id);
			String acc_username = request.getAccount().getUsername();
			logger.info("acc_username" + acc_username);
			String acc_password = request.getAccount().getPassword();
			logger.info("acc_password" + acc_password);
			String worker_ext = request.getWorker().getWorkerExt().replace("8091", "18090");
			logger.info("替换后的worker_ext" + worker_ext);
			String minute = "1200";
			try {
				service.updatePassAcc(acc_id, minute, acc_username, acc_password, worker_ext);
			} catch (RepeatException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 脚本机器模式权重
	 * 
	 * @param logid
	 */
	private String workerWeight(String logid) {
		String defaultWeight = WorkerWeight.WEIGHT_LUA;
		/* LUA&JAVA 机器脚本模式权重分配 */
		logger.info("{}机器人权重...", logid);
		try {
			String luaWeightValue = trainService.getSysSettingValue("worker_mode_" + WorkerWeight.WEIGHT_LUA);// lua脚本机器权重
			String javaWeightValue = trainService.getSysSettingValue("worker_mode_" + WorkerWeight.WEIGHT_JAVA);// java脚本机器权重
			logger.info("{}脚本机器权重系统设置lua脚本:{}; java脚本:{}", logid, luaWeightValue, javaWeightValue);

			// 将两个权重相加，计算各个权重占比
			BigDecimal luaBig = new BigDecimal(luaWeightValue);
			BigDecimal javaBig = new BigDecimal(javaWeightValue);
			BigDecimal total = new BigDecimal(0).add(luaBig).add(javaBig);

			int lua = luaBig.divide(total, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
			int java = javaBig.divide(total, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();

			// List总长度100， 将每个类型的个数添加进List，通过随机数获取类型
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < lua; i++) {
				list.add(WorkerWeight.WEIGHT_LUA);
			}
			for (int i = 0; i < java; i++) {
				list.add(WorkerWeight.WEIGHT_JAVA);
			}
			int random = new Random().nextInt(list.size() - 1);
			String type = list.get(random);
			logger.info("{}WORKER-TYPE:{}", logid, type);
			return type;
		} catch (Exception e) {
			logger.info("{}【系统异常】:获取机器人权重发生异常", logid, e);
		}
		return defaultWeight;
	}

}