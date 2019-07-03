package com.l9e.transaction.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.l9e.common.Consts;
import com.l9e.transaction.channel.request.IRequest;
import com.l9e.transaction.channel.request.RequestFactory;
import com.l9e.transaction.service.impl.TrainServiceImpl;
import com.l9e.transaction.vo.DeviceWeight;
import com.l9e.transaction.vo.ErrorInfo;
import com.l9e.transaction.vo.InterfaceOrder;
import com.l9e.transaction.vo.InterfaceOrderCP;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.Result;
import com.l9e.transaction.vo.Worker;
import com.l9e.transaction.vo.WorkerWeight;
import com.l9e.util.HttpUtil;
import com.l9e.util.StrUtil;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

@SuppressWarnings("serial")
public class OrderServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);
	private static final Random WEIGHT_RANDOM = new Random();
	// private static boolean immedResendFlag = false;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 设置编码
		req.setCharacterEncoding("UTF-8");
		long benginTime = System.currentTimeMillis();

		String uuid = req.getParameter("uuid");
		if (StringUtils.isBlank(uuid)) {
			uuid = "[";
			Random random = new Random();
			for (int i = 0; i < 5; i++) {
				uuid = uuid + random.nextInt(9);
			}
			uuid = uuid + "] ";
		}

		logger.info(uuid + "--------- OrderServlet doPost ---------");

		String channel = req.getParameter("channel");
		logger.info(uuid + "channel:" + channel);

		String queueName = StrUtil.getOrderQueue(channel);
		logger.info(uuid + "queueName:" + queueName);
		// 总数+1
		Consts.redisDao.INCR(queueName);

		String param = req.getParameter("param");
		logger.info(uuid + "param:" + param);
		resp.getWriter().print("ok");
		resp.getWriter().flush();
		resp.getWriter().close();
		if (StringUtils.isBlank(param) || "".equals(param.trim())) {
			logger.info(uuid + "param参数为空,不做处理");
			// 总数-1
			Consts.redisDao.DECR(queueName);
			return;
		}

		TrainServiceImpl dao = new TrainServiceImpl();
		ObjectMapper mapper = new ObjectMapper();
		InterfaceOrder inorder = null;
		// 测试用
		// InterfaceOrder inorder=new InterfaceOrder();
		// inorder.setOrderid("14617187910001");
		// inorder.setOrderstatus("05");
		// inorder.setPaymoney("93");
		// inorder.setTraveltime("2016-07-26 00:00:00");
		// inorder.setSeattype("8");
		// inorder.setFromtime("2016-07-26 19:05:00");
		// inorder.setChannel("meituan");
		// inorder.setLevel("0");
		// inorder.setExtseattype("3#无");
		// inorder.setFromcity("郑州");
		// inorder.setTocity("北京西");
		// inorder.setTrainno("T290");
		// inorder.setAccountId(1265814);
		// inorder.setAccountFromWay(1);

		try {
			// JSON字符串转成实体类
			inorder = mapper.readValue(param, InterfaceOrder.class);
		} catch (Exception e) {
			logger.info(uuid + "Json转实体类出现异常:" + e.getClass().getSimpleName(), e);
			logger.info(uuid + "OrderServlet流程结束，耗时:" + (System.currentTimeMillis() - benginTime) + " ms");
			Consts.redisDao.DECR(queueName);
			return;
		}
		inorder.setUuid(uuid);

		try {
			String orderId = inorder.getOrderid();
			logger.info(uuid + "参数转换...");
			Order order = changeOrder(inorder);
			order.setUuid(uuid);

			int isnotlocked = dao.startOrder(orderId);
			if (isnotlocked == 1) {
				logger.info(uuid + "订单[" + orderId + "]过期的消息队列消息,过滤掉此次预订请求");
				dao.insertHistory(orderId, "过期的消息队列消息,过滤掉此次预订请求");
				return;
			}

			String orderChannel = order.getChannel();
			logger.info(uuid + "orderChannel:" + orderChannel);
			// 同程的单用新代码出票，已做迁移前的测试
			/** 同程渠道已经停用
			if ("test".equals(orderChannel)) {
				int accountId = 0;
				int workId = -1;
				String accountStr = null;
				// 往账号服务 请求获取账号
				logger.info(uuid + "调用账号系统获取账号");
				try {
					long startTime = System.currentTimeMillis();
					String url = Consts.GETACCURL + "?channel=" + orderChannel;
					logger.info(uuid + "account url:" + url);
					accountStr = HttpUtil.sendByGet(url, "UTF-8", 5000, 5000);
					logger.info(uuid + "账号系统返回账号信息:" + accountStr);
					logger.info(uuid + "获取账号耗时:" + (System.currentTimeMillis() - startTime));
				} catch (Exception e) {
					logger.info(uuid + "【获取账号发生异常】", e);
					accountStr = null;
				}

				if (StringUtils.isBlank(accountStr)) {
					// 账号处理 545214|223 12306账号id|预订机器人id
					if (accountStr.contains("|")) {
						String[] strArr = accountStr.split("\\|");
						accountId = Integer.parseInt(strArr[0]);
						if (strArr.length > 1) {
							// 分离账号内绑定预订机器人worker_id
							workId = Integer.parseInt(strArr[1]);
						}
					} else {
						accountId = Integer.parseInt(accountStr);
					}
				} else {
					logger.info(uuid + "获取账号信息为空");
					// TODO
				}
				order.setAcc_id(accountId);
				order.setWorker_id(workId);

				logger.info(uuid + orderId + "绑定账号ID:" + accountId + ", 机器人ID:" + workId);
			} else {
				logger.info(uuid + "跳过旧帐户获得");
			}
			*/

			// 获取出票模式权重 pc or app
			String weight = deviceWeight(dao, uuid);
			logger.info(uuid + "出票模式最终权重:" + weight);

			// 获取各个脚本机器的权重
			String workerWeight = workerWeight(dao, uuid);
			logger.info(uuid + "机器人最终权重:" + weight);

			// 开始订票
			logger.info(uuid + "开始预订逻辑处理");
			boolean result = consume(order, dao, weight, workerWeight, null);

			logger.info(uuid + "预订结果:" + result);

		} catch (Exception e) {
			logger.info(uuid + "【OrderServlet进行预订发生异常】", e);
		} finally {
			Consts.redisDao.DECR(queueName);
			logger.info(uuid + "OrderServlet流程结束，耗时:" + (System.currentTimeMillis() - benginTime) + " ms");
		}
	}

	/**
	 * @Title: changeOrder
	 * @Description: 参数转换
	 * @author: taokai
	 * @date: 2017年9月6日 下午3:51:19
	 * @param orderId
	 * @param inorder
	 * @return Order
	 */
	private Order changeOrder(InterfaceOrder inorder) {
		String uuid = inorder.getUuid();
		String orderId = inorder.getOrderid();
		logger.info(uuid + "转换前接口参数:" + JSONObject.toJSONString(inorder));
		Order order = new Order();
		order.setOrderId(orderId);
		order.setOrderStatus(inorder.getOrderstatus());
		// order.setAccountId(inorder.get);
		order.setPaymoney(inorder.getPaymoney());
		String traveltime = inorder.getTraveltime();/* %Y-%m-%d */
		traveltime = traveltime.substring(0, 10);
		order.setOrderstr(orderId + "|" + inorder.getFromcity() + "|" + inorder.getTocity() + "|" + traveltime + "|" + inorder.getTrainno());
		order.setSeatType(inorder.getSeattype());
		order.setSeattime(inorder.getFromtime());
		order.setChannel(inorder.getChannel());
		order.setLevel(StringUtils.isEmpty(inorder.getLevel()) ? "0" : inorder.getLevel());
		order.setExtSeatType(inorder.getExtseattype());
		order.setTravel_time(traveltime);
		// 在此新增订单中的出发城市，到达城市,出发城市三字码，到达城市三字码4个字段
		order.setFrom(inorder.getFromcity());
		order.setTo(inorder.getTocity());
		if (null != inorder.getFromCity3c() && "" != inorder.getFromCity3c()) {
			order.setFromCity_3c(inorder.getFromCity3c());
		}
		if (null != inorder.getToCity3c() && "" != inorder.getToCity3c()) {
			order.setToCity_3c(inorder.getToCity3c());
		}

		// 账号来源： 0：公司自有账号 ； 1：12306自带账号
		if (null != inorder.getAccountFromWay() && 0 != inorder.getAccountFromWay()) {
			order.setAccountFromWay(inorder.getAccountFromWay());
		} else {
			order.setAccountFromWay(0);
		}
		// 在此判断订单传过来的数据中accountId是否有值
		if (null != inorder.getAccountId() && 0 != inorder.getAccountId()) {
			order.setAccountId(inorder.getAccountId());
		}

		// mq中传递过来的客户预选的卧铺位置或座位号
		order.setSeatDetailType(inorder.getSeatDetailType());
		order.setChoose_seats(inorder.getChoose_seats());

		String channel = order.getChannel();
		List<InterfaceOrderCP> inorderCPs = inorder.getOrderCPs();
		for (InterfaceOrderCP inOrderCP : inorderCPs) {
			StringBuffer cp = new StringBuffer();
			String cpId = inOrderCP.getCpId();
			String username = inOrderCP.getUsername();
			Integer trainType = inOrderCP.getTrainType();
			Integer certType = inOrderCP.getCertType();
			String certNo = inOrderCP.getCertNo().toUpperCase().trim();
			Integer seatType = inOrderCP.getSeatType();
			if ("tongcheng".equals(channel)) {
				cp.append(cpId).append("|").append(username).append("|").append(trainType).append("|").append(certType).append("|").append(certNo).append("|")
						.append(seatType);
			} else {
				cp.append(cpId).append("|").append(username).append("|").append(trainType).append("|").append(certType).append("|").append(certNo);
			}
			order.addOrderCp(cp.toString());
		}
		// 测试用
		// StringBuffer cp=new StringBuffer();
		// cp.append("mt160614083505258").append("|")
		// .append("周雪君").append("|")
		// .append("3").append("|")
		// .append("2").append("|")
		// .append("411502199509107344");
		// order.addOrderCp(cp.toString());

		if (Order.CHANNEL_QUNAR.equals(channel) || Order.CHANNEL_TC.equals(channel) || Order.CHANNEL_MEITUAN.equals(channel)
				|| Order.CHANNEL_GT.equals(channel)) {
			order.setChannelGroup(Order.CHANNEL_GROUP_1);
		} else {
			order.setChannelGroup(Order.CHANNEL_GROUP_2);
		}
		logger.info(uuid + "转换后我方参数:" + JSONObject.toJSONString(order));
		return order;
	}

	/**
	 * 订单处理逻辑
	 * 
	 * @author: taoka
	 * @date: 2018年1月5日 下午3:07:13
	 * @param order
	 * @param service
	 * @param weight
	 * @param workerWeight
	 * @param request
	 * @return boolean
	 */
	public boolean consume(Order order, TrainServiceImpl service, String weight, String workerWeight, IRequest request) {
		boolean immedResendFlag = false;
		String uuid = order.getUuid();
		String orderId = order.getOrderId();
		String orderStatus = order.getOrderStatus();
		logger.info(uuid + "orderStr:" + order.getOrderstr());
		logger.info(uuid + "outTypeWeight:" + weight);
		logger.info(uuid + "workerWeight:" + workerWeight);
		logger.info(uuid + "orderStatus:" + orderStatus);
		try {
			String optlog = "";
			if ("05".contains(orderStatus)) {// 如果状态为消息队列中状态
				logger.info(uuid + "正在预定，选择预定方式...");
				optlog = "train_fh正在预定，选择预定方式";
			} else {
				logger.info(uuid + "预定状态异常,订单状态:" + orderStatus);
				optlog = "train_fh预定状态异常,订单状态:" + orderStatus;
			}
			service.insertHistory(orderId, optlog);

			// 选择账号、处理人员和处理类
			logger.info(uuid + "选择账号、处理人员和处理类...");
			if (request == null) {
				request = new RequestFactory().select(order, workerWeight);
			}
			if (request == null) {
				service.orderIsManual(order, null);
				logger.info(uuid + "train_fh没有空闲帐号或可用机器人");
				optlog = "train_fh没有空闲帐号或可用机器人";
				service.insertHistory(orderId, optlog);
				return false;
			}

			// ============ 处理坐席信息
			logger.info(uuid + "处理坐席信息...");
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("seattype", order.getSeatType());
			list.add(map);
			boolean w_qunar = false;
			String channel = order.getChannel();
			String extSeatType = order.getExtSeatType();
			logger.info(uuid + "channel:" + channel + ", extSeatType:" + extSeatType);
			if ("qunar".equals(channel)) {
				w_qunar = true;
			}
			String tempExtSeatType = "";
			if (null != extSeatType && extSeatType.split("#").length > 1) {
				tempExtSeatType = extSeatType.split("#")[1];
			}
			logger.info(uuid + "tempExtSeatType:" + tempExtSeatType);
			order.setWea_wz(false);
			if (null != tempExtSeatType && tempExtSeatType.length() > 0 && !("无").equals(tempExtSeatType)) {
				String[] ext_type = tempExtSeatType.split("\\|");
				for (String str : ext_type) {
					if (("9").equals(str.split(",")[0])) {
						// 备选无座
						logger.info(uuid + "设置备选无座");
						order.setWea_wz(true);
						continue;
					}
					if (w_qunar) {
						if (((str.split(",")[0]).contains("4")) || ((str.split(",")[0]).contains("5")) || ((str.split(",")[0]).contains("6"))) {
							continue;
						}
					} else {
						map = new HashMap<String, String>();
						map.put("seattype", str.split(",")[0]);
						// map.put("price",str.split(",")[1]);
						list.add(map);
					}
				}
			}
			// //63#9,18|62,115|61,112
			Result result = null;
			for (Map<String, String> m : list) {
				order.setSeatType(m.get("seattype"));

				// orderbill.setPaymoney(m.get("price"));
				// start 利用处理类和其它信息进行订单的处理
				Integer workerReportId = null;
				try {
					logger.info(uuid + "设置机器人使用记录...");
					workerReportId = service.startWorkerReport(request.getWorker(), order, "1");

					logger.info(uuid + "###### 开始请求机器人,进行占座请求");
					long startTime = System.currentTimeMillis();
					result = request.request(order, weight);
					logger.info(uuid + "###### 机器人占座耗时:" + (System.currentTimeMillis() - startTime));
				} finally {
					service.endWorkerReport(workerReportId);
				}
				String retValue = result.getRetValue();
				String workerName = result.getWorker().getWorkerName();
				String errorInfo = result.getErrorInfo();
				logger.info(uuid + "机器人返回结果,orderId:" + orderId + ", workerName:" + workerName + ", status:" + retValue);

				// ================= 预定中取消检测
				logger.info(uuid + "预定中取消检测...");
				orderStatus = service.getOrderStatus(orderId);
				if (Order.STATUS_CANCEL_PRE.equals(orderStatus)) {
					logger.info(uuid + "订单需要取消,停止占座请求");
					optlog = "订单申请取消,停止占座转开始取消";
					service.insertHistory(orderId, optlog);
					service.updateOrderStatus(orderId, Order.STATUS_CANCEL_START);
					return true;
				}

				// start 对处理后的结果的返回值，进行订单的调整 TODO
				/**
				 * accountFromWay 账号来源： 0：公司自有账号 ； 1：12306自带账号
				 * 通过accountFromWay标识来判断该订单是否自带12306账号
				 * 如果自带12306账号，账号登录失败时，直接失败，其它错误，直接重发
				 */
				Integer accountFromWay = order.getAccountFromWay();
				logger.info(uuid + "账号来源标识:" + accountFromWay + " [0:公司自有账号 , 1:乘客自带账号]");
				if (StringUtils.equals(retValue, Result.QUEUE)) {
					optlog = workerName + "排队订单，等待重发！【" + errorInfo + "】";
					logger.info(uuid + "排队订单:" + errorInfo);
					service.orderIsQueue(order, result);
					break;
				} else if (StringUtils.equals(retValue, Result.SUCCESS)) {// 订单表改为成功，通知表改为正在通知
					optlog = workerName + "预定成功，进入支付过程！【" + errorInfo + "】";
					logger.info(uuid + "预定成功，进入支付过程！");
					int PAY_STATUS = service.orderIsSuccess(order, result);
					/*
					 * 支付预登入 if(PAY_STATUS==1){ try { long
					 * start=System.currentTimeMillis();
					 * logger.info(orderbill.getOrderId()+
					 * "the order is ready to pay start"); Account
					 * acc=request.getAccount(); StringBuffer param=new
					 * StringBuffer();
					 * param.append("order_id=").append(orderbill.getOrderId())
					 * .append("&channel=").append(orderbill.getChannel())
					 * .append("&accUsername=").append(acc.getAccUsername())
					 * .append("&accPassword=").append(acc.getAccPassword());
					 * HttpPostUtil.sendAndRecive(Consts.READYPAYURL,
					 * param.toString(),200,1000);
					 * logger.info(orderbill.getOrderId()+
					 * "the order is ready to pay end "
					 * +orderbill.getOrderId()+"|"+acc.getAccUsername()+"|"+acc.
					 * getAccPassword()+"|"+orderbill.getChannel()+"|"+(System.
					 * currentTimeMillis()-start)+"ms"); } catch (Exception e) {
					 * logger.info(orderbill.getOrderId()+
					 * "the order is ready to pay,Exception"+e);
					 * e.printStackTrace(); } }
					 */
					break;
				} else if (StringUtils.equals(retValue, Result.RESEND)) {
					if (1 == accountFromWay) { // 乘客自带
						if (errorInfo.contains("登录名不存在")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号不存在！");
							result.setErrorInfo("自带12306账号订单，传入的12306账号不存在！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setFailReason(ErrorInfo.ACCOUNT_NO_EXIST);
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("密码输入错误")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号登录密码错误！");
							result.setErrorInfo("自带12306账号订单，传入的12306账号登录密码错误！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setFailReason(ErrorInfo.PASSWORD_WRONG);
							result.setRetValue(Result.FAILURE);
							break;
						} else {// 其他错误
							logger.info(uuid + "自带12306账号订单:" + errorInfo);
							result.setErrorInfo("自带12306账号订单！" + errorInfo);
							optlog = workerName + "预定重发！【" + errorInfo + "】";
							immedResendFlag = true;
							break;
						}
					} else if (accountFromWay == 0) { // 公司
						// 订单表和通知表不做调整
						if (errorInfo != null && errorInfo.indexOf("脚本执行异常") > -1) {
							optlog = workerName + "脚本执行异常 立即自动重发";
							logger.info(uuid + "脚本执行异常 立即自动重发");
							immedResendFlag = true;
							break;
						} else {
							optlog = workerName + "预定重发！【" + errorInfo + "】";
							logger.info(uuid + "预定重发:" + errorInfo);
							service.orderIsResend(order, result);
							break;
						}
					} else {
						// TODO 账号来源出现其他值
						throw new RuntimeException("RESEND AccountFromWay Other Value!!!");
					}
				} else if (StringUtils.equals(retValue, Result.WAIT)) {// 排队，订单表和通知表不做调整
					logger.info(uuid + "订单正在排队:" + errorInfo);
					optlog = workerName + "排队订单！【" + errorInfo + "】";
					service.orderIsWait(order, result);
					break;
				} else if (StringUtils.equals(retValue, Result.FAILURE)) {// 失败,订单表改为失败，通知表改为正在通知
					logger.info(uuid + "预定失败:" + errorInfo);
					optlog = workerName + "预定失败！【" + errorInfo + "】";
					// continue;
					break; // 2016年6月1号改成break;
				} else if (StringUtils.equals(retValue, Result.CANCEL)) {// 取消，预订车票信息更新，订单表改为开始取消
					logger.info(uuid + "预订取消:" + errorInfo);
					optlog = workerName + "预订取消！【" + errorInfo + "】";
					service.orderIsCancel(order, result);
					break;
				} else if (StringUtils.equals(retValue, Result.MANUAL)) {// 人工，如果异常，在25分钟内重查，超时变成人工处理
					logger.info(uuid + "进入转人工流程...");
					if (1 == accountFromWay) { // 自带账号
						if (errorInfo.contains("未完成订单") || errorInfo.contains("请等待其支付完成后")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号中存在未完成订单！");
							result.setErrorInfo("自带12306账号订单，传入的12306账号中存在未完成订单！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setFailReason(ErrorInfo.EXIST_UNFINISHED_ORDER);
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("登录名不存在")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号不存在！");
							result.setErrorInfo("自带12306账号订单，传入的12306账号不存在！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setFailReason(ErrorInfo.ACCOUNT_NO_EXIST);
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("密码输入错误")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号登录密码错误！");
							result.setErrorInfo("自带12306账号订单，传入的12306账号登录密码错误！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setFailReason(ErrorInfo.PASSWORD_WRONG);
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("app扣票失败")) {
							logger.info(uuid + "自带12306账号订单， app扣票失败 立即自动重发");
							optlog = workerName + "自带12306账号订单，app扣票失败 立即自动重发";
							immedResendFlag = true;
							break;
						} else if (errorInfo.contains("姓名格式错误")) {
							logger.info(uuid + "自带12306账号订单，姓名格式错误 立即自动转pc重发");
							optlog = workerName + "自带12306账号订单，姓名格式错误 立即自动转pc重发";
							immedResendFlag = true;
							break;
						} else if (errorInfo.contains("排队")) {
							logger.info(uuid + "自带12306账号订单，排队异常 立即自动转pc重发");
							optlog = workerName + "自带12306账号订单，排队异常， 立即自动转pc重发";
							immedResendFlag = true;
							break;
						} else if (errorInfo.contains("耐心等待 ")) {
							logger.info(uuid + "自带12306账号订单,您的订单已经提交,请耐心等待, 立即自动转pc重发");
							optlog = workerName + "自带12306账号订单，您的订单已经提交，请耐心等待 ， 立即自动转pc重发";
							immedResendFlag = true;
							break;
						} else {
							logger.info(uuid + "自带12306账号订单其他异常:" + errorInfo);
							result.setErrorInfo("自带12306账号订单！" + errorInfo);
							optlog = workerName + "预定人工！【" + errorInfo + "】";
							service.orderIsManual(order, result);
							break;
						}
					} else if (accountFromWay == 0) { // 公司账号
						// 扣票失败转PC端重发
						if (errorInfo != null && result.getErrorInfo().indexOf("app扣票失败") > -1) {
							logger.info(uuid + "app扣票失败 立即自动重发");
							optlog = workerName + "app扣票失败 立即自动重发";
							immedResendFlag = true;
							break;
						} else if (errorInfo.contains("姓名格式错误")) {
							logger.info(uuid + "姓名格式错误 立即自动转pc重发");
							optlog = workerName + "姓名格式错误 立即自动转pc重发";
							immedResendFlag = true;
							break;
						} else if (errorInfo.contains("排队")) {
							logger.info(uuid + "app排队异常 立即自动转pc重发");
							optlog = workerName + "app排队异常， 立即自动转pc重发";
							immedResendFlag = true;
							break;
						} else if (errorInfo.contains("耐心等待 ")) {
							logger.info(uuid + "您的订单已经提交,请耐心等待, 立即自动转pc重发");
							optlog = workerName + "您的订单已经提交，请耐心等待 ， 立即自动转pc重发";
							immedResendFlag = true;
							break;
						} else {
							logger.info(uuid + "Manual Order Other ErrorInfo:" + errorInfo);
							optlog = workerName + "预定人工处理！【" + errorInfo + "】";
							service.orderIsManual(order, result);
							break;
						}
					} else {
						throw new RuntimeException("MANUAL AccountFromWay Other Value!!!");
					}
				} else if (StringUtils.equals(retValue, Result.STOP)) { // 临时停用
					if (1 == accountFromWay) {
						if (errorInfo.contains("取消次数过多")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号取消次数过多！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setErrorInfo("自带12306账号订单，传入的12306账号取消次数过多！");
							result.setFailReason(ErrorInfo.CANCEL_NUMBER_TOMUCH);
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("上限")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号联系人个数达上限！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setErrorInfo("自带12306账号订单，传入的12306账号联系人个数达上限！");
							result.setFailReason(ErrorInfo.USERNUMBERS_OVER_TOPLIMIT);
							result.setRetValue(Result.FAILURE);
							break;

						} else if (errorInfo.contains("手机核验")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号联系人手机核验不通过！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setErrorInfo("自带12306账号订单，传入的12306账号联系人手机核验不通过！");
							result.setFailReason(ErrorInfo.MOBILEPHONE_NO_VERIFY);
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("重新注册")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号联系人**需要重新注册！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setErrorInfo("自带12306账号订单，传入的12306账号联系人**需要重新注册！");
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("账号状态为：未通过")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号状态为未通过！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setErrorInfo("自带12306账号订单，传入的12306账号状态为未通过！");
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("未通过核验，不能添加常用联系人")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号未通过核验，不能添加常用联系人！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setErrorInfo("自带12306账号订单，传入的12306账号未通过核验，不能添加常用联系人！");
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("通过后即可在网上购票")) {
							logger.info(uuid + "自带12306账号订单，传入的12306账号**通过后即可在网上购票！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setErrorInfo("自带12306账号订单，传入的12306账号**通过后即可在网上购票！");
							result.setRetValue(Result.FAILURE);
							break;
						} else if (errorInfo.contains("身份证件号码填写是否正确")) {
							logger.info(uuid + "自带12306账号订单，请确认传入的12306账号联系人的身份证件号码填写是否正确！");
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setErrorInfo("自带12306账号订单，请确认传入的12306账号联系人的身份证件号码填写是否正确！");
							result.setRetValue(Result.FAILURE);
							break;
						} else {
							logger.info(uuid + " 自带12306账号订单其他错误:" + errorInfo);
							optlog = workerName + "预定失败！【" + errorInfo + "】";
							result.setErrorInfo("自带12306账号订单！" + errorInfo);
							result.setRetValue(Result.FAILURE);
							break;
						}
					} else if (0 == accountFromWay) { // 公司自有
						// 取消订单过多，暂时停用
						logger.info(uuid + "order is stop!");
						optlog = workerName + "预定失败！【" + errorInfo + "】";
						service.orderIsStop(order, result);
						break;
					} else {
						throw new RuntimeException("STOP AccountFromWay Other Value!!!");
					}
				} else if (StringUtils.equals(retValue, Result.END)) {
					if (1 == accountFromWay) {
						logger.info(uuid + "自带12306账号订单，传入的12306账号待核验！");
						optlog = workerName + "预定失败！【" + errorInfo + "】";
						result.setErrorInfo("自带12306账号订单，传入的12306账号待核验！");
						result.setFailReason(ErrorInfo.ACCOUNT_WAIT_VERIFY);
						result.setRetValue(Result.FAILURE);
						break;
					} else {
						// 封停
						logger.info(uuid + "order is end!");
						optlog = workerName + "预定失败！【" + errorInfo + "】";
						service.orderIsEnd(order, result);
						break;
					}
				} else if (StringUtils.equals(retValue, Result.NOPASS)) {
					StringBuffer sb = new StringBuffer();
					// 身份证,成人,姓名，状态 0、已通过 1、待审核 2、未通过
					logger.info(uuid + "passenger info error:" + errorInfo);
					String[] passengers = errorInfo.split("\\|");

					for (String passenger : passengers) {
						if (StringUtils.isNotEmpty(passenger)) {
							String[] str = passenger.split(",");
							if (!"0".equals(str[3])) {
								if (sb.length() == 0) {
									sb.append(workerName).append("，预定人工处理！【").append(str[2]).append("（").append(str[0]).append("）")
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
					logger.info(uuid + " order is nopass!");
					result.setErrorInfo(errorInfo);
					// service.orderIsFailure(orderbill, result);
					break;

				} else {// 异常
					optlog = workerName + "预定异常！【" + result.getErrorInfo() + "】";
					logger.info(uuid + " find order is exception, restor find!");
					break;
				}
			}

			String retValue = result.getRetValue();
			if (StringUtils.equals(retValue, Result.FAILURE) || StringUtils.equals(retValue, Result.NOPASS)) {
				logger.info(uuid + " Order is FAILURE or NOPASS");
				service.orderIsFailure(order, result);
			}

			// start 记录日志
			service.insertHistory(orderId, optlog);
			logger.info(uuid + "modify orderbill cpid=" + orderId + " status:" + retValue + " end!");

			// 账号是否需要加入账号过滤表cp_accountinfo_filter
			if (result.isInsertFilter()) {
				logger.info(uuid + "加入账号过滤表cp_accountinfo_filter...");
				service.addAccountFilter(order, result);
			}

			// 更新常用联系人个数
			String contactsNumStr = result.getContactsNum();
			if (StringUtils.isNotEmpty(contactsNumStr)) {
				Integer contactsNum = Integer.valueOf(contactsNumStr);
				logger.info(uuid + "开始更新常用联系人个数 ,返回的联系人个数为：" + contactsNum);

				// 如果联系人个数为0，则不更新
				if (contactsNum.intValue() != 0) {
					service.updateContactsNum(result);
					logger.info(uuid + "update contacts num success");
				}
			}

			// 是否需要更新基础车票价格
			if (StringUtils.equals(retValue, Result.SUCCESS) || StringUtils.equals(retValue, Result.CANCEL)) {
				// 更新常用联系人对应账号信息
				if (StringUtils.equals(retValue, Result.CANCEL)) {
					result.setFilterScope(Result.FILTER_ALL);
					try {
						service.addAccountFilter(order, result);
					} catch (Exception e) {
						logger.info(uuid + "update the AccountFilter error", e);
					}
				}

				logger.info(uuid + "需要更新基础车票价格 begin");
				try {
					if (!order.isWea_price()) {
						int re = service.updateBasePrice(order, result);
						if (re == 1) {
							logger.info(uuid + "don't need update price!");
						} else {
							service.insertHistory(orderId, "更新车票基础票价完成！");
						}
					} else {
						logger.info(uuid + "has children don't need update price!");
					}
				} catch (Exception e) {
					logger.info("更新基础车票价格异常:" + e.getClass().getSimpleName(), e);
				}
				logger.info("更新基础车票价格 end");
			}

			// 停用被封停机器人
			if (result != null) {
				String errorInfo = result.getErrorInfo();
				if (errorInfo.contains("操作频率过快") || errorInfo.contains("机器ip可能被封锁")) {
					if (request != null && request.getWorker() != null) {
						logger.info(uuid + "机器人被封停。停用机器人 workerId : " + request.getWorker().getWorkerId());
						Consts.workerService.stopWorker(request.getWorker().getWorkerId());
						// service.stopWorker(request.getWorker());
					}
				}
			}
		} catch (Exception e) {
			logger.info(uuid + "exception!!:" + e);
			try {
				service.orderToRg(orderId);
			} catch (Exception e1) {
			}
			return false;
		} finally {
			logger.info(uuid + orderId + " Finally Request:" + request);
			if (request != null) {
				Worker worker = request.getWorker();
				Integer workerId = order.getWorker_id();
				logger.info(uuid + "workerId : " + workerId + ", worker : " + worker.toString());
				try {
					if (workerId > 0) {
						logger.info(uuid + "释放机器人至空闲状态 workerId : " + workerId);
						Consts.workerService.releaseWorker(workerId);
					}
					if (worker != null && !worker.getWorkerId().equals("" + workerId)) {
						workerId = Integer.valueOf(worker.getWorkerId());
						logger.info(uuid + "释放机器人至空闲状态 workerId : " + workerId);
						Consts.workerService.releaseWorker(workerId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (immedResendFlag) {
				logger.info(uuid + "### 进入立即重发...订单号" + orderId);
				consume(order, service, "pc", "lua", request);
			} else {
				/**
				 * accountFromWay 账号来源： 0：公司自有账号 ； 1：12306自带账号
				 * 通过accountFromWay标识来判断该订单是否自带12306账号
				 * 如果不是自带12306账号，则把该账号插入白名单账号表
				 */
				Integer accountFromWay = null;
				accountFromWay = order.getAccountFromWay();
				if (accountFromWay != 1) {
					insertPassAcc(request, service);
				}
			}
		}
		return true;
	}

	/**
	 * 权重
	 * 
	 * @param uuid
	 * @param uuid
	 */
	private String deviceWeight(TrainServiceImpl service, String uuid) {
		// PC&APP 模式权重分配
		String defaultWeightMode = DeviceWeight.WEIGHT_PC;

		try {
			String pcWeightValue = service.getSysSettingValue("device_mode_" + DeviceWeight.WEIGHT_PC);// pc权重
			String appWeightValue = service.getSysSettingValue("device_mode_" + DeviceWeight.WEIGHT_APP);// app权重

			logger.info(uuid + "权重系统设置PC端:" + pcWeightValue + ", APP移动端:" + appWeightValue);
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
			logger.info(uuid + "权重最大边界 count:" + count);
			Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0,
																// weightSum)
			logger.info(uuid + "权重随机值 nchannel:" + nchannel);
			Integer mchannel = 0;
			for (DeviceWeight weightCategory : modeCategories) {
				if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
					defaultWeightMode = weightCategory.getCategory();
					break;
				}
				mchannel += weightCategory.getWeight();
			}
		} catch (Exception e) {
			logger.info(uuid + "【获取出票模式权重异常】", e);
		}

		return defaultWeightMode;
	}

	/**
	 * 脚本机器模式权重
	 * 
	 * @param uuid
	 * @throws DatabaseException
	 * @throws RepeatException
	 */
	private String workerWeight(TrainServiceImpl service, String uuid) {
		/* LUA&JAVA 机器脚本模式权重分配 */
		String defaultWeightMode = WorkerWeight.WEIGHT_LUA;

		try {
			String luaWeightValue = service.getSysSettingValue("worker_mode_" + WorkerWeight.WEIGHT_LUA);// lua脚本机器权重
			String javaWeightValue = service.getSysSettingValue("worker_mode_" + WorkerWeight.WEIGHT_JAVA);// java脚本机器权重

			logger.info(uuid + "脚本机器权重系统设置lua脚本:" + luaWeightValue + ", java脚本:" + javaWeightValue);
			/* 设置权重置 */
			List<WorkerWeight> modeCategories = new ArrayList<WorkerWeight>();// 放各个权重的集合

			WorkerWeight luaMode = new WorkerWeight(WorkerWeight.WEIGHT_LUA, Integer.parseInt(luaWeightValue));// lua脚本机器权重
			WorkerWeight javaMode = new WorkerWeight(WorkerWeight.WEIGHT_JAVA, Integer.parseInt(javaWeightValue));// java脚本机器权重

			modeCategories.add(luaMode);
			modeCategories.add(javaMode);

			int count = 0;
			for (WorkerWeight category : modeCategories) {
				count += category.getWeight();
			}
			logger.info(uuid + "权重最大边界 count : " + count);
			Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0,
																// weightSum)
			logger.info(uuid + "权重随机值 nchannel : " + nchannel);
			Integer mchannel = 0;
			for (WorkerWeight weightCategory : modeCategories) {
				if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
					defaultWeightMode = weightCategory.getCategory();
					break;
				}
				mchannel += weightCategory.getWeight();
			}
		} catch (Exception e) {
			logger.info(uuid + "【获取机器人权重发生异常】", e);
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
			String minute = "20";
			try {
				service.updatePassAcc(acc_id, minute, acc_username, acc_password, worker_ext);
			} catch (RepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
