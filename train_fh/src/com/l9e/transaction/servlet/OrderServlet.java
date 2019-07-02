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
public class OrderServlet extends HttpServlet{
	private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);
	private static final Random WEIGHT_RANDOM = new Random();
//	private static boolean immedResendFlag = false;
		
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("get--------------");
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		long servlet_start=System.currentTimeMillis();
		String channel = req.getParameter("channel");
		Consts.redisDao.INCR(StrUtil.getOrderQueue(channel));
		String param=req.getParameter("param");
		logger.info("param: "+param);
		resp.getWriter().print("ok");
		resp.getWriter().flush();
		resp.getWriter().close();
		if(param==null||"".equals(param.trim())){
			/*测试模块
			 * try {
				String text_get_acc=HttpUtil.sendByGet(Consts.GETACCURL+"?channel=elong", "UTF-8", "1000", "1000");
				logger.info("get a acc_id"+text_get_acc);
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			Consts.redisDao.DECR(StrUtil.getOrderQueue(channel));
			return;
		}
		TrainServiceImpl dao = new TrainServiceImpl();
		ObjectMapper mapper = new ObjectMapper();
		InterfaceOrder inorder=null;
		//测试用
//		InterfaceOrder inorder=new InterfaceOrder();
//		inorder.setOrderid("14617187910001");
//		inorder.setOrderstatus("05");
//		inorder.setPaymoney("93");
//		inorder.setTraveltime("2016-07-26 00:00:00");
//		inorder.setSeattype("8");
//		inorder.setFromtime("2016-07-26 19:05:00");
//		inorder.setChannel("meituan");
//		inorder.setLevel("0");
//		inorder.setExtseattype("3#无");
//		inorder.setFromcity("郑州");
//		inorder.setTocity("北京西");
//		inorder.setTrainno("T290");
//		inorder.setAccountId(1265814);
//		inorder.setAccountFromWay(1);
		
		try {
			/**老版本获取账号*/
			/*ElongTcAccData data = new ElongTcAccData();
			data.startPoolLoad(dao);
			NorQunAccData nqdata = new NorQunAccData();
			nqdata.startPoolLoad(dao);
			if("qunar".equals(order.getChannel())){
				//logger.info("qunar acc pool size==>："+nqdata.getQunQueueSize());
				//acc_id = nqdata.getQunAccId();
			}else if("elong".equals(order.getChannel())){
				logger.info("elong acc pool size==>："+data.getEloQueueSize());
				acc_id = data.getEloAccId();
			}else if("tongcheng".equals(order.getChannel())){
				logger.info("tongcheng acc pool size==>："+data.getTcQueueSize());
				acc_id = data.getTcAccId();
			}else{
				//logger.info("19e acc pool size==>："+nqdata.getNorQueueSize());
				//acc_id = nqdata.getNorAccId();
			}
			*/
			inorder = mapper.readValue(param, InterfaceOrder.class);
			/**参数转换*/
			Order order=changeOrder(inorder);
			int isnotlocked=dao.startOrder(order.getOrderId());
			if(isnotlocked==1){
				dao.insertHistory(order.getOrderId(), "过期的消息队列消息,过滤掉此次预订请求");
				return;
			}
			/*同程的单用新代码出票，已做迁移前的测试*/
			if(order.getChannel().equals("test")) {

				int acc_id = 0;
				int worker_id=-1;
				String get_acc_str=null;
				/**往账号服务 请求获取账号*/
				try {
					long get_acc_start=System.currentTimeMillis();
					get_acc_str=HttpUtil.sendByGet(Consts.GETACCURL+"?channel="+order.getChannel(), "UTF-8", "5000", "5000");
					logger.info("get a account:"+get_acc_str+",lose time:"+(System.currentTimeMillis()-get_acc_start)+"ms");
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("send acc_service Exception "+e);
					get_acc_str=null;
				}
				
				if(get_acc_str!=null&&!"".equals(get_acc_str))
				{
					//账号处理   545214|223             12306账号id|预订机器人id
					if(get_acc_str.contains("|")){
						String[] get_acc_str_arr=get_acc_str.split("\\|");
						acc_id=Integer.parseInt(get_acc_str_arr[0]);
						if(get_acc_str_arr.length>1){
							//分离账号内绑定预订机器人worker_id
							worker_id=Integer.parseInt(get_acc_str_arr[1]);
						}
					}else{
						acc_id=Integer.parseInt(get_acc_str);
					}
				}else{
					logger.info("get acc from acc_service error :"+get_acc_str);
				}
				order.setAcc_id(acc_id);
				order.setWorker_id(worker_id);
				
				logger.info("order start---------->>>order_id:"+order.getOrderId()+" : acc_id:"+acc_id+" : worker_id:"+worker_id+",Servlet lose time："+(System.currentTimeMillis()-servlet_start));
			
			} else {
				logger.info("test_tongcheng ---- skip old account obtain");
			}
			
			
			String weight = deviceWeight(dao);
			//String weight="app";
			//获取各个脚本机器的权重
			String workerWeight = workerWeight(dao);
			//String workerWeight = "java";
			/**开始预订逻辑*/
			boolean result =consume(order, dao,weight,workerWeight,null);
			if (!result){
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally{
			Consts.redisDao.DECR(StrUtil.getOrderQueue(channel));
		}
	}
	
	private Order changeOrder(InterfaceOrder inorder) {
		Order order=new Order();
		order.setOrderId(inorder.getOrderid());
		order.setOrderStatus(inorder.getOrderstatus());
		//order.setAccountId(inorder.get);
		order.setPaymoney(inorder.getPaymoney());
		String traveltime=inorder.getTraveltime();/*%Y-%m-%d*/
		traveltime=traveltime.substring(0,10);
		order.setOrderstr(inorder.getOrderid()+"|"+inorder.getFromcity()+"|"+inorder.getTocity()+"|"+
				traveltime+"|"+inorder.getTrainno());
		order.setSeatType(inorder.getSeattype());
		order.setSeattime(inorder.getFromtime());
		order.setChannel(inorder.getChannel());
		order.setLevel(StringUtils.isEmpty(inorder.getLevel())?"0":inorder.getLevel());
		order.setExtSeatType(inorder.getExtseattype());
		order.setTravel_time(traveltime);
		//在此新增订单中的出发城市，到达城市,出发城市三字码，到达城市三字码4个字段
		order.setFrom(inorder.getFromcity());
		order.setTo(inorder.getTocity());
		if(null != inorder.getFromCity3c() && "" != inorder.getFromCity3c()){
			order.setFromCity_3c(inorder.getFromCity3c());
		}
		if(null != inorder.getToCity3c() && "" != inorder.getToCity3c()){
			order.setToCity_3c(inorder.getToCity3c());
		}
		
		//账号来源： 0：公司自有账号 ； 1：12306自带账号
		if(null != inorder.getAccountFromWay() && 0 != inorder.getAccountFromWay()){
		order.setAccountFromWay(inorder.getAccountFromWay());
		}else{
			order.setAccountFromWay(0);
		}
		//在此判断订单传过来的数据中accountId是否有值
		if(null != inorder.getAccountId() && 0 != inorder.getAccountId() ){
			order.setAccountId(inorder.getAccountId());
		}
		
		List<InterfaceOrderCP> inorderCPs=inorder.getOrderCPs();
		for(InterfaceOrderCP inOrderCP:inorderCPs){
			StringBuffer cp=new StringBuffer();
			if("tongcheng".equals(order.getChannel())){
				cp.append(inOrderCP.getCpId()).append("|")
				.append(inOrderCP.getUsername()).append("|")
				.append(inOrderCP.getTrainType()).append("|")
				.append(inOrderCP.getCertType()).append("|")
				.append(inOrderCP.getCertNo()).append("|")
				.append(inOrderCP.getSeatType());
			}else{
				cp.append(inOrderCP.getCpId()).append("|")
				.append(inOrderCP.getUsername()).append("|")
				.append(inOrderCP.getTrainType()).append("|")
				.append(inOrderCP.getCertType()).append("|")
				.append(inOrderCP.getCertNo());
			}
			order.addOrderCp(cp.toString());
		}
		//测试用
//		StringBuffer cp=new StringBuffer();
//		cp.append("mt160614083505258").append("|")
//		.append("周雪君").append("|")
//		.append("3").append("|")
//		.append("2").append("|")
//		.append("411502199509107344");		
//		order.addOrderCp(cp.toString());
		
		if(Order.CHANNEL_QUNAR.equals(order.getChannel()) 
				|| Order.CHANNEL_TC.equals(order.getChannel()) 
				|| Order.CHANNEL_MEITUAN.equals(order.getChannel())
				|| Order.CHANNEL_GT.equals(order.getChannel())) {
			order.setChannelGroup(Order.CHANNEL_GROUP_1);
		} else {
			order.setChannelGroup(Order.CHANNEL_GROUP_2);
		}
		return order;
	}

	public boolean consume(Order orderbill, TrainServiceImpl service,String weight,String workerWeight,IRequest request) {
		boolean immedResendFlag = false;
		logger.info("order selfid:"+orderbill.getOrderId()+" orderStr:"+orderbill.getOrderstr());
		try {
			String optlog = "";
			if("05".contains(orderbill.getOrderStatus())){//如果状态为消息队列中状态
				optlog = "train_fh_order正在预定，选择预定方式";
			}else{
				optlog = "预定状态异常，状态为："+orderbill.getOrderStatus();
			}
			service.insertHistory(orderbill.getOrderId(), optlog);
			//end
			
			//start 选择账号、处理人员和处理类
			logger.info("select request="+orderbill.getOrderId()+" start");
			if(request == null){
				request = new RequestFactory().select(orderbill,workerWeight);
			}
			if(request == null){
				service.orderIsManual(orderbill, null);
				logger.error(orderbill.getOrderId()+" not Account");
				optlog = "没有空闲帐号或可用机器人！";
				service.insertHistory(orderbill.getOrderId(), optlog);	
				return false;
			}
			
			logger.info("select request="+orderbill.getOrderId()+" end requestImpl:"+request);
			
			
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("seattype", orderbill.getSeatType());
			list.add(map);
			boolean w_qunar = false;
			if("qunar".equals(orderbill.getChannel())){
				w_qunar = true;
			}
			String ext_seat_type = "";
			if(null!=orderbill.getExtSeatType() && orderbill.getExtSeatType().split("#").length>1){
				ext_seat_type = orderbill.getExtSeatType().split("#")[1];
			}
			orderbill.setWea_wz(false);
			if(null!=ext_seat_type && ext_seat_type.length()>0 && !("无").equals(ext_seat_type)) {
				String[] ext_type = ext_seat_type.split("\\|");
				for(String str:ext_type){
					if(("9").equals(str.split(",")[0])){
						//备选无座
						orderbill.setWea_wz(true);
						logger.info("order_id:"+orderbill.getOrderId()+" back up the no seat!!");
						continue;
					}
					if(w_qunar){
						if(((str.split(",")[0]).contains("4"))||((str.split(",")[0]).contains("5"))||((str.split(",")[0]).contains("6"))){
							continue;
						}
					}else{
						map = new HashMap<String,String>();
						map.put("seattype", str.split(",")[0]);
//						map.put("price",str.split(",")[1]);
						list.add(map);
					}
				}
			}
//			//63#9,18|62,115|61,112
			Result result = null;
			for(Map<String,String> map_n:list){
				orderbill.setSeatType(map_n.get("seattype"));
				
//				orderbill.setPaymoney(map_n.get("price"));
				//start 利用处理类和其它信息进行订单的处理
				Integer workerReportId = null;
				try {
					logger.info("request="+orderbill.getOrderId()+" start");
					workerReportId = service.startWorkerReport(request.getWorker(), orderbill, "1");
					result = request.request(orderbill,weight);
					logger.info("request="+orderbill.getOrderId()+" end retValue:"+result.getRetValue());
				} finally {
					service.endWorkerReport(workerReportId);
				}
			
				logger.info("modify orderbill cpid="+orderbill.getOrderId()+" workerName:"+result.getWorker().getWorkerName()+" status:"+result.getRetValue()+" start!");
				//end
				
				/*预定中取消检测*/
				//start
				String orderStatus = service.getOrderStatus(orderbill.getOrderId());
				if(Order.STATUS_CANCEL_PRE.equals(orderStatus)) {
					logger.info("order cancel , stop order_id : " + orderbill.getOrderId());
					optlog = "订单申请取消，停止占座转开始取消";
					service.insertHistory(orderbill.getOrderId(), optlog);
					service.updateOrderStatus(orderbill.getOrderId(), Order.STATUS_CANCEL_START);
					return true;
				}
				//end
				
				//start 对处理后的结果的返回值，进行订单的调整
				if(StringUtils.equals(result.getRetValue(), Result.QUEUE)) {
					optlog = result.getWorker().getWorkerName()+"，排队订单，等待重发！【"+result.getErrorInfo()+"】";
					logger.info(orderbill.getOrderId()+" order is queue!");
					service.orderIsQueue(orderbill, result);
					break;
				} else if (StringUtils.equals(result.getRetValue(), Result.SUCCESS)) {// 订单表改为成功，通知表改为正在通知
					optlog = result.getWorker().getWorkerName()+"，预定成功，进入支付过程！【"+result.getErrorInfo()+"】";
					logger.info(orderbill.getOrderId()+" order is success!");
					int PAY_STATUS=service.orderIsSuccess(orderbill, result);
						/*支付预登入
						 * if(PAY_STATUS==1){
							try {
								long start=System.currentTimeMillis();
								logger.info(orderbill.getOrderId()+"the order is ready to pay start");
								Account acc=request.getAccount();
								StringBuffer param=new StringBuffer();
								param.append("order_id=").append(orderbill.getOrderId())
								.append("&channel=").append(orderbill.getChannel())
								.append("&accUsername=").append(acc.getAccUsername())
								.append("&accPassword=").append(acc.getAccPassword());
								HttpPostUtil.sendAndRecive(Consts.READYPAYURL, param.toString(),200,1000);
								logger.info(orderbill.getOrderId()+"the order is ready to pay end "+orderbill.getOrderId()+"|"+acc.getAccUsername()+"|"+acc.getAccPassword()+"|"+orderbill.getChannel()+"|"+(System.currentTimeMillis()-start)+"ms");
							} catch (Exception e) {
								logger.info(orderbill.getOrderId()+"the order is ready to pay,Exception"+e);
								e.printStackTrace();
							}
						}*/
					break;
				} else if (StringUtils.equals(result.getRetValue(), Result.RESEND)) {// 订单表和通知表不做调整
					optlog = result.getWorker().getWorkerName()+"，预定重发！【"+result.getErrorInfo()+"】";				
					if(result.getErrorInfo()!=null && result.getErrorInfo().indexOf("脚本执行异常")>-1){
						optlog = result.getWorker().getWorkerName()+"，脚本执行异常 立即自动重发";
						logger.info(orderbill.getOrderId()+" 脚本执行异常 立即自动重发");
						immedResendFlag = true;
						break;
					}else{
						logger.info(orderbill.getOrderId()+" order is resend!");
						service.orderIsResend(orderbill, result);
					}
					break;
				} else if (StringUtils.equals(result.getRetValue(), Result.WAIT)) {// 订单表和通知表不做调整
					optlog = result.getWorker().getWorkerName()+"，排队订单！【"+result.getErrorInfo()+"】";
					logger.info(orderbill.getOrderId()+" order is wait!");
					service.orderIsWait(orderbill, result);
					break;
				} else if (StringUtils.equals(result.getRetValue(), Result.FAILURE)) {// 订单表改为失败，通知表改为正在通知
					optlog = result.getWorker().getWorkerName()+"，预定失败！【"+result.getErrorInfo()+"】";
					logger.info(orderbill.getOrderId()+" order is failure!");
					//continue;
					break; //2016年6月1号改成break;
				} else if (StringUtils.equals(result.getRetValue(), Result.CANCEL)){//预订车票信息更新，订单表改为开始取消
					optlog = result.getWorker().getWorkerName()+"，预订取消！【"+result.getErrorInfo()+"】";
					logger.info(orderbill.getOrderId()+" order is cancel!");
					service.orderIsCancel(orderbill, result);
					break;
				}else  if(StringUtils.equals(result.getRetValue(), Result.MANUAL)){//如果异常，在25分钟内重查，超时变成人工处理
					logger.info("进入转人工流程");
					optlog = result.getWorker().getWorkerName()+"，预定人工处理！【"+result.getErrorInfo()+"】";
					
					/**
					 * accountFromWay 账号来源： 0：公司自有账号 ； 1：12306自带账号
					 * 通过accountFromWay标识来判断该订单是否自带12306账号
					 * 如果自带12306账号，账号中存在未完成订单时，直接失败
					 */
					Integer accountFromWay = null;
					accountFromWay = orderbill.getAccountFromWay();
					String errorInfo=result.getErrorInfo();
					if (null != accountFromWay && 1 == accountFromWay && (errorInfo.contains("未完成订单") || errorInfo.contains("请等待其支付完成后"))) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号中存在未完成订单！");
						result.setFailReason(ErrorInfo.EXIST_UNFINISHED_ORDER);
						result.setRetValue(Result.FAILURE);
						optlog = result.getWorker().getWorkerName() + "，预定失败！【"+ result.getErrorInfo() + "】";
						break;
					}
					
					//扣票失败转PC端重发
					if(result.getErrorInfo()!=null && result.getErrorInfo().indexOf("app扣票失败")>-1){
						optlog = result.getWorker().getWorkerName()+"，app扣票失败 立即自动重发";
						logger.info(orderbill.getOrderId()+" app扣票失败 立即自动重发");
						immedResendFlag = true;
						break;
					}else{
						logger.info(orderbill.getOrderId()+" order is manual!");
						service.orderIsManual(orderbill, result);
						break;
					}					
				}else  if(StringUtils.equals(result.getRetValue(), Result.STOP)){
					/**
					 * add by wangsf
					 * accountFromWay 账号来源： 0：公司自有账号 ； 1：12306自带账号
					 * 通过accountFromWay标识来判断该订单是否自带12306账号
					 * 如果自带12306账号，在自动切换账号时，直接失败
					 */
					Integer accountFromWay = null;
					accountFromWay = orderbill.getAccountFromWay();
					String errorInfo=result.getErrorInfo();
					if (null != accountFromWay && 1 == accountFromWay && errorInfo.contains("取消次数过多")) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号取消次数过多！");
						result.setFailReason(ErrorInfo.CANCEL_NUMBER_TOMUCH);
						result.setRetValue(Result.FAILURE);
						optlog = result.getWorker().getWorkerName() + "，预定失败！【"+ result.getErrorInfo() + "】";
						break;
					} else {
						// 取消订单过多，暂时停用
						optlog = result.getWorker().getWorkerName() + "，预定失败！【"+ result.getErrorInfo() + "】";
						logger.info(orderbill.getOrderId()+ " order is manual!");
						service.orderIsStop(orderbill, result);
						break;
					}
				}else  if(StringUtils.equals(result.getRetValue(), Result.END)){
					/**
					 * accountFromWay 账号来源： 0：公司自有账号 ； 1：12306自带账号
					 * 通过accountFromWay标识来判断该订单是否自带12306账号
					 * 如果自带12306账号，在自动切换账号时，直接失败
					 */
					Integer accountFromWay = null;
					accountFromWay = orderbill.getAccountFromWay();
					if (null != accountFromWay && 1 == accountFromWay) {
						result.setErrorInfo("自带12306账号订单，传入的12306账号待核验！");
						result.setFailReason(ErrorInfo.ACCOUNT_WAIT_VERIFY);
						result.setRetValue(Result.FAILURE);
						optlog = result.getWorker().getWorkerName() + "，预定失败！【"+ result.getErrorInfo() + "】";
						break;
					} else {
						// 封停
						optlog = result.getWorker().getWorkerName() + "，预定失败！【"+ result.getErrorInfo() + "】";
						logger.info(orderbill.getOrderId() + " order is end!");
						service.orderIsEnd(orderbill, result);
						break;
					}
				}else if(StringUtils.equals(result.getRetValue(), Result.NOPASS)){
					StringBuffer sb = new StringBuffer();
					//身份证,成人,姓名，状态 0、已通过 1、待审核 2、未通过
					logger.info("passenger info error:"+result.getErrorInfo());
					String[] passengers = result.getErrorInfo().split("\\|");
					
					for(String passenger : passengers){
						if(StringUtils.isNotEmpty(passenger)){
							String[] str = passenger.split(",");
							if(!"0".equals(str[3])){
								if(sb.length()==0){
									sb.append(result.getWorker().getWorkerName())
									  .append("，预定人工处理！【")
									  .append(str[2]).append("（").append(str[0]).append("）")
									  .append(str[3].equals("1")?"待审核":"未通过").append("】");
									optlog = sb.toString();
								}
							}else{//包含添加成功的常用联系人
								//常用联系人添加成功需要添加到账号过滤表中
								result.setInsertFilter(true);
								result.setFilterScope(Result.FILTER_PART);
							}
						}
					}
					logger.info(orderbill.getOrderId()+" order is nopass!");
					result.setErrorInfo(result.getErrorInfo());
//					service.orderIsFailure(orderbill, result);
					break;
				}else{// 异常
					optlog = result.getWorker().getWorkerName()+"，预定异常！【"+result.getErrorInfo()+"】";
					logger.warn(result.getSelfId()+" find order is exception, restor find!");
					break;
				}
			}
			if (StringUtils.equals(result.getRetValue(), Result.FAILURE) || StringUtils.equals(result.getRetValue(), Result.NOPASS)){
				service.orderIsFailure(orderbill, result);
			}
			//end
			//start 记录日志
			service.insertHistory(orderbill.getOrderId(), optlog);	
			logger.info("modify orderbill cpid="+orderbill.getOrderId()+" status:"+result.getRetValue()+" end!");
			//end
			
			//账号是否需要加入账号过滤表cp_accountinfo_filter
			if(result.isInsertFilter()){
				service.addAccountFilter(orderbill, result);
			}
			
			//更新常用联系人个数
			if(StringUtils.isNotEmpty(result.getContactsNum())){
				service.updateContactsNum(result);
				logger.info("update contacts num success");
			}
			
			//是否需要更新基础车票价格
			if(StringUtils.equals(result.getRetValue(), Result.SUCCESS) || StringUtils.equals(result.getRetValue(), Result.CANCEL)){
				//更新常用联系人对应账号信息
				if(StringUtils.equals(result.getRetValue(), Result.CANCEL)) {
					result.setFilterScope(Result.FILTER_ALL);
					try{
						service.addAccountFilter(orderbill, result);
					}catch(Exception e){
						logger.error("update the AccountFilter error");
					}
				}
				
				logger.info("update base price begin");
				try{
					if(!orderbill.isWea_price()){
						int re = service.updateBasePrice(orderbill, result);
						if(re == 1){
							logger.info("don't need update price!");
						}else{
							service.insertHistory(orderbill.getOrderId(), "更新车票基础票价完成！");	
						}
					}else{
						logger.info("has children don't need update price!");
					}
				}catch(Exception we){
					logger.info("update base price error",we);
				}
				logger.info("update base price end");
			}
			
			/*停用被封停机器人*/
			if(result != null) {
				String errorInfo = result.getErrorInfo();
				if(errorInfo.contains("操作频率过快") || errorInfo.contains("机器ip可能被封锁")) {
					
					if(request != null && request.getWorker() != null) {
						logger.info("机器人被封停。停用机器人 workerId : " + request.getWorker().getWorkerId());
						Consts.workerService.stopWorker(request.getWorker().getWorkerId());
//						service.stopWorker(request.getWorker());
					}
				}
			}
		} catch (Exception e) {
			logger.info(orderbill.getOrderId() + "exception!!:"+e);
			try {
				service.orderToRg(orderbill.getOrderId() );
			} catch (Exception e1) {
			}
			return false;
		} finally {
			logger.info("finally request : " + request);
			logger.info("订单号"+orderbill.getOrderId());
			if(request != null) {
				Worker worker = request.getWorker();
				Integer workerId = orderbill.getWorker_id();
				logger.info("workerId : " + workerId + ", worker : " + worker.toString());
				try {
					if(workerId > 0) {
						logger.info("释放机器人至空闲状态 workerId : " + workerId);
						Consts.workerService.releaseWorker(workerId);
					}
					if(worker != null && !worker.getWorkerId().equals("" + workerId)) {
						workerId = Integer.valueOf(worker.getWorkerId());
						logger.info("释放机器人至空闲状态 workerId : " + workerId);
						Consts.workerService.releaseWorker(workerId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			if(immedResendFlag){
				logger.info("进入立即重发...订单号"+orderbill.getOrderId());
				consume(orderbill, service,"pc","lua",request);
			}else{
				insertPassAcc(request,service);
			}
		}
		return true;
	}
	/**
	 * 权重
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	private String deviceWeight(TrainServiceImpl service) {
		/*PC&APP 模式权重分配*/
		String defaultWeightMode = DeviceWeight.WEIGHT_PC;
		
		try {
			String pcWeightValue = service.getSysSettingValue("device_mode_" + DeviceWeight.WEIGHT_PC);//pc权重
			String appWeightValue = service.getSysSettingValue("device_mode_" + DeviceWeight.WEIGHT_APP);//app权重
			
			logger.info("权重系统设置pc_weight ,PC端: " + pcWeightValue + "app_weight ,移动端: " + appWeightValue);
			/*设置权重置*/
			List<DeviceWeight> modeCategories = new ArrayList<DeviceWeight>();//放各个权重的集合
			
			DeviceWeight pcMode = new DeviceWeight(DeviceWeight.WEIGHT_PC, Integer.parseInt(pcWeightValue));//pc权重
			DeviceWeight appMode = new DeviceWeight(DeviceWeight.WEIGHT_APP, Integer.parseInt(appWeightValue));//app权重
			
			modeCategories.add(pcMode);
			modeCategories.add(appMode);
			
			int count = 0;
			for(DeviceWeight category : modeCategories) {
				count += category.getWeight();
			}
			logger.info("权重最大边界 count : " + count);
			Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0, weightSum)
			logger.info("权重随机值 nchannel : " + nchannel);
			Integer mchannel = 0;
			for (DeviceWeight weightCategory : modeCategories) {
				if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
					defaultWeightMode = weightCategory.getCategory();
					break;
				}
				mchannel += weightCategory.getWeight();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		return defaultWeightMode;
	}
	
	/**
	 * 脚本机器模式权重
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	private String workerWeight(TrainServiceImpl service) {
		/*LUA&JAVA 机器脚本模式权重分配*/
		String defaultWeightMode = WorkerWeight.WEIGHT_LUA;
		
		try {
			String luaWeightValue = service.getSysSettingValue("worker_mode_" + WorkerWeight.WEIGHT_LUA);//lua脚本机器权重
			String javaWeightValue = service.getSysSettingValue("worker_mode_" + WorkerWeight.WEIGHT_JAVA);//java脚本机器权重
			
			logger.info("脚本机器权重系统设置lua_weight ,lua脚本: " + luaWeightValue + "java_weight ,java脚本: " + javaWeightValue);
			/*设置权重置*/
			List<WorkerWeight> modeCategories = new ArrayList<WorkerWeight>();//放各个权重的集合
			
			WorkerWeight luaMode = new WorkerWeight(WorkerWeight.WEIGHT_LUA, Integer.parseInt(luaWeightValue));//lua脚本机器权重
			WorkerWeight javaMode = new WorkerWeight(WorkerWeight.WEIGHT_JAVA, Integer.parseInt(javaWeightValue));//java脚本机器权重
			
			modeCategories.add(luaMode);
			modeCategories.add(javaMode);
			
			int count = 0;
			for(WorkerWeight category : modeCategories) {
				count += category.getWeight();
			}
			logger.info("权重最大边界 count : " + count);
			Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0, weightSum)
			logger.info("权重随机值 nchannel : " + nchannel);
			Integer mchannel = 0;
			for (WorkerWeight weightCategory : modeCategories) {
				if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
					defaultWeightMode = weightCategory.getCategory();
					break;
				}
				mchannel += weightCategory.getWeight();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		return defaultWeightMode;
	}
	
	//填充变动账号表，进一步更新白名单表
	private void insertPassAcc(IRequest request,TrainServiceImpl service){
		if(request!=null && request.getAccount()!=null && request.getWorker()!=null &&
				request.getAccount().getId()!=null && 
				request.getAccount().getUsername()!=null && request.getAccount().getPassword()!=null &&
				request.getWorker().getWorkerExt()!=null && !"".equals(request.getWorker().getWorkerExt())){
			String acc_id = request.getAccount().getId().toString();
			logger.info("acc_id"+acc_id);
			String acc_username = request.getAccount().getUsername();
			logger.info("acc_username"+acc_username);
			String acc_password = request.getAccount().getPassword();
			logger.info("acc_password"+acc_password);
			String worker_ext = request.getWorker().getWorkerExt().replace("8091", "18090");
			logger.info("替换后的worker_ext"+worker_ext);
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
