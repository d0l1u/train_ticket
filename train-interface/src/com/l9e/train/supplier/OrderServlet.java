package com.l9e.train.supplier;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.mq.MqService;
import com.l9e.train.supplier.po.Order;
import com.l9e.train.supplier.po.WeightCategory;
import com.l9e.train.supplier.service.impl.OrderService;
import com.l9e.train.util.Contes;
import com.l9e.train.util.MemcachedUtil;
import com.l9e.train.util.StrUtil;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class SupplierServlet
 * 
 * 为系统提供供货服务
 */
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());

	private static final Random WEIGHT_RANDOM = new Random();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OrderServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		logger.info("----start insert order----receive url:" + request.getQueryString());
		String returnMsg = null;
		OrderUtil util = new OrderUtil();

		Order supOrder = util.verifyRequest(request);
		// Order supOrder=new Order();
		// supOrder.setOrderid("10004");
		// supOrder.setOrdername("杭州");
		// supOrder.setPaymoney("22");
		// supOrder.setOrderstatus("01");
		// supOrder.setTrainno("G41");
		// supOrder.setFromcity("杭州");
		// supOrder.setTocity("北京南");
		// supOrder.setFromtime("2016-06-01 09:33:00");
		// supOrder.setTotime("2016-06-08 09:33:00");
		// supOrder.setTraveltime("2016-06-08 09:33:00");
		// supOrder.setSeattype("3");
		// supOrder.setOuttickettype("11");
		// supOrder.setChannel("12306");
		// supOrder.setExtseattype("5#无");
		// supOrder.setLevel("0");
		// supOrder.setIspay("00");
		// supOrder.setManualorder("00");
		// supOrder.setWaitfororder("11");
		// supOrder.setUsername("wsf");
		// supOrder.setPassword("12345678");

		if (util.verifyCode() != "0000") {
			// 参数错误
			returnMsg = supOrder.responstValue(supOrder, util.verifyCode());
			write(response, returnMsg);
			return;
		}

		logger.info("orderid:" + supOrder.getOrderid());

		OrderService orderService = new OrderService();

		// 在此获得把12306用户名和密码插入表中成功后，对应记录的accID
		Integer accID = null;
		try {
			orderService.bind12306Account(supOrder);
			accID = orderService.getAccID();
		} catch (RepeatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (null != accID && 0 != accID) {
			supOrder.setAccountId(accID);
			supOrder.setAccountFromWay(1); // 当12306自带账号时，给这个订单设个标识；账号来源：
											// 0：公司自有账号 ； 1：12306自带账号
		} else {
			supOrder.setAccountFromWay(0); // 当12306自带账号时，给这个订单设个标识；账号来源：
											// 0：公司自有账号 ； 1：12306自带账号
		}

		if (StringUtils.isEmpty(supOrder.getChannel())) {
			supOrder.setChannel("19e");
		}
		try {
			// 预订方式权重
			String modeWeight = weight(orderService);
			if (hasExtSeat(supOrder)) {
				/* 含有备选坐席，只能走推送模式 */
				logger.info("含有备选坐席，转为推送模式 orderId : " + supOrder.getOrderid() + ", extSeat = "
						+ supOrder.getExtseattype());
				modeWeight = WeightCategory.WEIGHT_PROPELL;
			}

			logger.info(supOrder.getOrderid() + "模式权重随机结果 mode : " + modeWeight);
			/*
			 * 2018-06-08 10：06 禁用模式判断，拉取模式并未实现
			 */
			/*
			 * if(WeightCategory.WEIGHT_PROPELL.equals(modeWeight)) { //推送模式
			 * propellMode(supOrder, orderService); } else
			 * if(WeightCategory.WEIGHT_PULL.equals(modeWeight)) { //拉取模式
			 * pullMode(supOrder, orderService); }
			 */

			propellMode(supOrder, orderService);

			returnMsg = supOrder.responstValue(supOrder, Order.ORDER_SUCCESS);
			write(response, returnMsg);
		} catch (RepeatException e) {
			// 订单重复
			logger.warn("order repateException" + supOrder.getOrderid() + " exception:" + e);
			returnMsg = supOrder.responstValue(supOrder, Order.ORDER_SUCCESS);
			write(response, returnMsg);
		} catch (DatabaseException e) {
			// 数据库异常
			logger.warn("order DownBillFailure" + supOrder.getOrderid() + " exception:" + e);
			returnMsg = supOrder.responstValue(supOrder, Order.ORDER_ERROR);
			write(response, returnMsg);
			return;
		} catch (Exception e) {
			// 数据库异常
			logger.warn("order DownBill Exception" + supOrder.getOrderid() + " exception:" + e);
			returnMsg = supOrder.responstValue(supOrder, Order.ORDER_ERROR);
			write(response, returnMsg);
			return;
		}

		logger.info("----end insert order----");

	}

	/**
	 * 权重
	 * 
	 * @throws DatabaseException
	 * @throws RepeatException
	 */
	@Deprecated
	private String weight(OrderService orderService) throws RepeatException, DatabaseException {
		/* 推送及拉取 模式权重分配 */
		String defaultWeightMode = WeightCategory.WEIGHT_PROPELL;

		String propellWeightValue = orderService.querySysVal("book_mode_" + WeightCategory.WEIGHT_PROPELL);// 推送权重
		String pullWeightValue = orderService.querySysVal("book_mode_" + WeightCategory.WEIGHT_PULL);// 拉取权重

		// String propellWeightValue = "0";
		// String pullWeightValue = "100";

		logger.info("权重系统设置 propell_weight ,推送: " + propellWeightValue + "pull_weight ,拉取: " + pullWeightValue);
		/* 设置权重置 */
		List<WeightCategory> modeCategories = new ArrayList<WeightCategory>();// 放各个权重的集合

		WeightCategory propellMode = new WeightCategory(WeightCategory.WEIGHT_PROPELL,
				Integer.parseInt(propellWeightValue));// 推送权重
		WeightCategory pullMode = new WeightCategory(WeightCategory.WEIGHT_PULL, Integer.parseInt(pullWeightValue));// 拉取权重

		modeCategories.add(propellMode);
		modeCategories.add(pullMode);

		int count = 0;
		for (WeightCategory category : modeCategories) {
			count += category.getWeight();
		}
		logger.info("权重最大边界 count : " + count);
		Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0, weightSum)
		logger.info("权重随机值 nchannel : " + nchannel);
		Integer mchannel = 0;
		for (WeightCategory weightCategory : modeCategories) {
			if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
				defaultWeightMode = weightCategory.getCategory();
				break;
			}
			mchannel += weightCategory.getWeight();
		}

		return defaultWeightMode;

	}

	/**
	 * 推送模式
	 * 
	 * @param supOrder
	 * @param orderService
	 * @return
	 * @throws DatabaseException
	 * @throws RepeatException
	 */
	private void propellMode(Order supOrder, OrderService orderService) throws RepeatException, DatabaseException {

		logger.info("推送模式出单 : orderId=" + supOrder.getOrderid() + ", channel=" + supOrder.getChannel());
		// 发送至消息队列
		/*
		 * 2018-06-07 取消走MQ逻辑 if("elong".equals(supOrder.getChannel()) ||
		 * "tongcheng".equals(supOrder.getChannel())
		 * ||"qunar".equals(supOrder.getChannel()) ||
		 * "301030".equals(supOrder.getChannel())){
		 * supOrder.setOrderstatus(Order.STATUS_ORDER_INMQ); } else {
		 */
		/* 分发预订关闭后直接初始化人工 */
		/* 消费开关限制start */
		String train_fh_status = "1";// 默认开启状态
		if (null != MemcachedUtil.getInstance().getAttribute("train_fh_status")) {
			train_fh_status = String.valueOf(MemcachedUtil.getInstance().getAttribute("train_fh_status"));
			logger.info(" train_fh_status:" + train_fh_status);
		} else {
			try {
				train_fh_status = orderService.querySysVal("train_fh_status");
			} catch (Exception e) {
				e.printStackTrace();
			}
			MemcachedUtil.getInstance().setAttribute("train_fh_status", train_fh_status, 30 * 1000);
		}
		if ("0".equals(train_fh_status)) {
			logger.info("train_fh 预订分发功能已关闭,非mq订单转人工预订");
			supOrder.setOrderstatus(Order.STATUS_ORDER_MANUAL);
		} else {
			supOrder.setOrderstatus(Order.STATUS_ORDER_START);
		}
		/* 消费开关限制end */
		/*
		 * }
		 */

		// 订单入库
		String format = "ky";
		String nanoTime = String.valueOf(System.nanoTime());
		format += new SimpleDateFormat("yyMMdd").format(new Date()) + nanoTime.substring(nanoTime.length() - 8);
		supOrder.setMyOrderId(format);
		orderService.downBill(supOrder);

		/*
		 * 2018-06-07 取消走MQ逻辑 if("elong".equals(supOrder.getChannel()) ||
		 * "tongcheng".equals(supOrder.getChannel()) ||
		 * "qunar".equals(supOrder.getChannel()) ||
		 * "301030".equals(supOrder.getChannel())){ try {
		 * 
		 * logger.info("send to mq start:"+supOrder.getOrderid()); MqService
		 * helloService = Contes.mqUtil.get("mq"); JSONObject msgJson =
		 * JSONObject.fromObject(supOrder);
		 * helloService.sendMqMsg(StrUtil.getOrderQueue(supOrder.getChannel()),
		 * msgJson.toString()); logger.info("send to mq end,order info:"
		 * +msgJson.toString()); } catch (Exception e) {
		 * logger.info(supOrder.getOrderid()+"send to mq Exception:"+e);
		 * logger.info("resend to mq start:"+supOrder.getOrderid()); MqService
		 * helloService = Contes.mqUtil.get("mq"); JSONObject msgJson =
		 * JSONObject.fromObject(supOrder);
		 * helloService.sendMqMsg(StrUtil.getOrderQueue(supOrder.getChannel()),
		 * msgJson.toString()); logger.info("send to mq end,order info:"
		 * +msgJson.toString()); e.printStackTrace(); } }
		 */
	}

	/**
	 * 拉取模式
	 * 
	 * @param supOrder
	 * @param orderService
	 * @throws DatabaseException
	 * @throws RepeatException
	 */
	private void pullMode(Order supOrder, OrderService orderService) throws RepeatException, DatabaseException {

		logger.info("拉取模式出单:orderId = " + supOrder.getOrderid() + ", channel = " + supOrder.getChannel());

		supOrder.setOrderstatus(Order.STATUS_ORDER_INMQ);

		// 订单入库
		orderService.downBill(supOrder);

		try {
			logger.info("send to distribute mq start:" + supOrder.getOrderid());
			MqService helloService = Contes.mqUtil.get("mq");
			JSONObject msgJson = JSONObject.fromObject(supOrder);
			logger.info("wsfssss:" + msgJson.toString());
			helloService.sendMqMsg(StrUtil.getOrderQueue("distribute"), msgJson.toString());
			logger.info("send to distribute mq end,order info:" + msgJson.toString());
		} catch (Exception e) {
			logger.info(supOrder.getOrderid() + "send to mq Exception:" + e);
			logger.info("resend to distribute mq start:" + supOrder.getOrderid());
			MqService helloService = Contes.mqUtil.get("mq");
			JSONObject msgJson = JSONObject.fromObject(supOrder);
			helloService.sendMqMsg(StrUtil.getOrderQueue("distribute"), msgJson.toString());
			logger.info("send to distribute mq end,order info:" + msgJson.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 查看订单是否有备用坐席
	 * 
	 * @param supOrder
	 * @return
	 */
	private Boolean hasExtSeat(Order supOrder) {

		String ext_seat_type = "";
		String[] extSeatArray = null;
		if (null != supOrder.getExtseattype() && (extSeatArray = supOrder.getExtseattype().split("#")).length > 1) {
			ext_seat_type = extSeatArray[1];
		}
		if (ext_seat_type.length() > 0 && !("无").equals(ext_seat_type)) {
			return true;
		}

		return false;
	}

	private void write(HttpServletResponse response, String returnMsg) {
		try {
			response.getWriter().write(returnMsg);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("response write failure:" + e);

		} finally {

		}
	}

	public static void main(String[] args) throws RepeatException, DatabaseException {
		for (int i = 0; i < 1000; i++) {

			String result = new OrderServlet().weight(null);
			System.out.println(result);
		}

	}
}
