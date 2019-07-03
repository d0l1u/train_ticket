package com.l9e.transaction.controller;

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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseController;
import com.l9e.common.Consts;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ElongOrderService;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.RefundService;
import com.l9e.transaction.thread.BookResultNoticeThread;
import com.l9e.transaction.thread.OrderResultNoticeThread;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.DBPassengerInfo;
import com.l9e.transaction.vo.ElongOrderLogsVo;

/**
 * 获取出票系统结果主控制器
 * @author liuyi02
 *
 */
@Controller
@RequestMapping("/outTicket")
public class OutTicketController  extends BaseController{
private static Logger logger= Logger.getLogger(OutTicketController.class);
	
	/*@Resource
	private ElongOrderService elongOrderService;
	*/
	
	@Resource
	private OrderService orderService;
	
	
	
	@Resource
	private ElongOrderService elongOrderService;
	
	
	@Resource
	private RefundService refundService;
	
	@Resource
	private NoticeService noticeService;
	
	/**
	 * 出票结果通知
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderNotice.jhtml")
	public void orderNotice(HttpServletRequest request, 
			HttpServletResponse response) {
		try {
			String result = this.getParam(request, "result");
			String orderId = this.getParam(request, "orderid");
			String billNo = this.getParam(request, "billno");
			String buyMoney = this.getParam(request, "buymoney");
			String seatTrains = this.getParam(request, "seattrains");
			String status = this.getParam(request, "status");//00：出票成功 11预定成功
			String level = this.getParam(request, "level");//vip用户不为空
			String from_time=this.getParam(request, "from_time");
			String to_time=this.getParam(request, "to_time");//09:11   09:11
			String pay_limit_time=this.getParam(request, "pay_limit_time");
			String zz_time=this.getParam(request, "out_ticket_time");
			logger.info("[接收出票结果通知接口]参数orderId=" + orderId 
					+ ",result=" + result + ",billNo=" + billNo
					+ ",buyMoney=" + buyMoney + ",seatTrains=" + seatTrains + ",status=" + status+",level="+level+",from_time="+from_time+",to_time="+to_time+",pay_limit_time"+pay_limit_time+",out_ticket_time"+zz_time);
			//Map<String, Object> paramMap = new HashMap<String, Object>();//主订单参数
			List<Map<String, String>> cpMapList = new ArrayList<Map<String, String>>();//车票订单参数
			
			if(StringUtils.isEmpty(result) || StringUtils.isEmpty(orderId)){
				//参数为空
				logger.info("exception:参数为空！");
				write2Response(response, "failed");
			}else if(TrainConsts.SUCCESS.equalsIgnoreCase(result)){//成功
				if(StringUtils.isEmpty(billNo) || StringUtils.isEmpty(buyMoney) 
						|| StringUtils.isEmpty(status) || StringUtils.isEmpty(seatTrains)){
					//参数为空
					logger.info("exception:明细参数为空！");
					write2Response(response, "failed");
					return;
				}else if(!"11".equals(status) && !"00".equals(status)){
					logger.info("exception:状态有误！status="+status);
					write2Response(response, "failed");
					return;
				}
				//查询订单信息
				DBOrderInfo orderInfo=orderService.queryOrderInfo(orderId);
				
				if(StringUtils.isEmpty(orderInfo.getOrder_status())){
					logger.info("exception:订单查询异常，订单状态为空！");
					write2Response(response, "failed");
					return;
				}
				if("11".equals(status)){//预订成功
					if(Consts.ELONG_ORDER_MAKED.equals(orderInfo.getOrder_status())||Consts.ELONG_ORDER_WAITPAY.equals(orderInfo.getOrder_status())){
						logger.info("[接收出票结果通知接口]本次出票成功通知为重复通知,orderId=" + orderId);
						write2Response(response, "success");
						return;
					}
					if(Consts.ELONG_OUT_TIME.equals(orderInfo.getOrder_status())){
						logger.info("订单已经超时,不再更新订单内容以及结果,orderId=" + orderId);
						write2Response(response, "success");
						return;
					}
					
					if(Consts.ELONG_ORDER_CANCELED.equals(orderInfo.getOrder_status())){
						logger.info("订单已经取消,不再更新订单内容以及结果,orderId=" + orderId);
						write2Response(response, "success");
						return;
					}
					/*paramMap.put("order_id", orderId);
					paramMap.put("buy_money", buyMoney);
					paramMap.put("out_ticket_billno", billNo);
					paramMap.put("order_status",Consts.ELONG_ORDER_MAKED);//预订成功
*/					
					String errorinfo = this.getParam(request, "errorinfo");//错误信息
					
					orderInfo.setBuy_money(buyMoney);
					orderInfo.setOut_ticket_billno(billNo);
					orderInfo.setOrder_status(Consts.ELONG_ORDER_MAKED);
					orderInfo.setFrom_time(from_time);
					orderInfo.setTo_time(to_time);
					orderInfo.setPay_limit_time(pay_limit_time);
					orderInfo.setOut_ticket_time(zz_time);
					orderInfo.setOut_fail_reason(errorinfo);
					
					
					if("".equals(pay_limit_time)||"".equals(zz_time)){
						/**插入订单操作 日记*/
						ElongOrderLogsVo log=new ElongOrderLogsVo();
						log.setOpt_person("elong_app");
						log.setOrder_id(orderInfo.getOrder_id());
						log.setOrder_id("出票系统返回pay_limit_time:"
						+pay_limit_time+",out_ticket_time:"+zz_time);
						elongOrderService.insertElongOrderLogs(log);
					}
					//明细数据处理
					this.detailDataPacking(seatTrains, cpMapList, response);
					
					/**更新订单信息*/
					orderService.updateOrderInfo(cpMapList,orderInfo);
					write2Response(response, "success");
					
					//预定结果通知
					BookResultNoticeThread bookNoticeThread = new BookResultNoticeThread(orderInfo,noticeService,orderService);
					Thread thread = new Thread(bookNoticeThread); 
					thread.start();
					
				}
				if("00".equals(status)){//出票成功    //
					/**重复通知处理*/
					if(Consts.ELONG_ORDER_SUCCESS.equals(orderInfo.getOrder_status())){
						logger.info("[接收出票结果通知接口]本次出票成功通知为重复通知,orderId=" + orderId);
						write2Response(response, "success");
						return;
					}if(Consts.ELONG_OUT_TIME.equals(orderInfo.getOrder_status())){
						logger.info("订单已经超时,不再更新订单内容以及结果,orderId=" + orderId);
						write2Response(response, "success");
						return;
					}
					
					if(Consts.ELONG_ORDER_CANCELED.equals(orderInfo.getOrder_status())){
						logger.info("订单已经取消,不再更新订单内容以及结果,orderId=" + orderId);
						write2Response(response, "success");
						return;
					}
					/*paramMap.put("order_id", orderId);
					paramMap.put("buy_money", buyMoney);
					paramMap.put("out_ticket_time", "now");//保证出票时间值非空，数据库进行处理
					paramMap.put("out_ticket_billno", billNo);
					paramMap.put("order_status",Consts.ELONG_ORDER_SUCCESS);//出票成功
*/					orderInfo.setBuy_money(buyMoney);
					orderInfo.setOut_ticket_time(zz_time);
					orderInfo.setOut_ticket_billno(billNo);
					orderInfo.setOrder_status(Consts.ELONG_ORDER_SUCCESS);
					orderInfo.setFrom_time(from_time);//
					orderInfo.setTo_time(to_time);
					//明细数据处理
					this.detailDataPacking(seatTrains, cpMapList, response);
					
					/**是否为补单通知 席别校验开始*/
					boolean reOrder=false;
					if("tongcheng".equals(orderInfo.getChannel())){
						ElongOrderLogsVo log=new ElongOrderLogsVo();
						log.setOpt_person("elong_app");
						log.setOrder_id(orderInfo.getOrder_id());
						for(Map<String,String> cpInfo:cpMapList){
							String firstSeatNo=orderService.querySeatNo(cpInfo);
							if(!cpInfo.get("seat_no").equals(firstSeatNo)){
								//
								reOrder=true;
								log.setContent("检测到预订坐席和出票坐席内容不一致:开启补单通知"+orderInfo.getOrder_id()+","+firstSeatNo+"|"+cpInfo.get("seat_no"));
								elongOrderService.insertElongOrderLogs(log);
								break;
							}else{
								logger.info("检测坐席类型" + firstSeatNo+"|"+cpInfo.get("seat_no")+"，一致"+orderInfo.getOrder_id()+","+cpInfo.get("cp_id"));
							}
						}
					}
					
					/**更新订单信息*/
					orderService.updateOrderInfo(cpMapList,orderInfo);
					write2Response(response, "success");
					
					
					//出票结果通知
					OrderResultNoticeThread orderNoticeThread = new OrderResultNoticeThread(orderInfo,reOrder,noticeService,orderService);
					Thread thread = new Thread(orderNoticeThread); 
					thread.start();
					
				}
			}else if(TrainConsts.FAILURE.equalsIgnoreCase(result)){//失败
				//查询订单信息
				DBOrderInfo orderInfo=orderService.queryOrderInfo(orderId);
				List<DBPassengerInfo> dbPassengers=orderInfo.getPassengers();
				if(!StringUtils.isEmpty(orderInfo.getOrder_status())
						&& Consts.ELONG_ORDER_FAIL.equals(orderInfo.getOrder_status())){
					logger.info("[接收出票结果通知接口]本次出票失败通知为重复请求，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				if(Consts.ELONG_OUT_TIME.equals(orderInfo.getOrder_status())){
					logger.info("订单已经超时,不再更新订单内容以及结果,orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				
				if(Consts.ELONG_ORDER_CANCELED.equals(orderInfo.getOrder_status())){
					logger.info("订单已经取消,不再更新订单内容以及结果,orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				
				String passengers = this.getParam(request, "passengers");//乘客审核信息
				String errorinfo = this.getParam(request, "errorinfo");//错误信息
				
				//乘车人信息错误
				String passenger_reason = "";
				JSONArray pJa = new JSONArray();
				JSONObject jsobj = null;
				logger.info("失败返回乘客审核信息：passengers=" + passengers + ", order_id : " + orderId);
				passengers = URLDecoder.decode(passengers, "UTF-8");
				if(StringUtils.isNotEmpty(passengers)){
					String certType = "1";//身份证
					String certNo = null;
					String name = null;
					String reason = null;
					String cp_id = null;
					
					String[] arrayPassenger = passengers.split("#");
					for(String passenger : arrayPassenger){
						String[] element = passenger.split("\\|");
						if(element == null || element.length<4){
							//参数为空
							logger.info("exception：参数拆分失败！");
							write2Response(response, "failed");
							return;
						}
						cp_id = element[0];
						name = element[1];
						certNo = element[2];
						reason = element[3];
						if(StringUtils.isEmpty(reason)){
							//参数为空
							logger.info("exception：乘客审核参数异常！, order_id : " + orderId);
							write2Response(response, "failed");
							return;
						}else if("1".equals(reason) || "2".equals(reason)){//待审核，未通过
							jsobj = new JSONObject(); 
							for(DBPassengerInfo cp : dbPassengers){
								if(cp.getCp_id().equals(cp_id)){
									certType = cp.getElong_ids_type();
									break;
								}
							}
							jsobj.put("certNo", certNo);
							jsobj.put("certType", certType);
							jsobj.put("name", name);
							jsobj.put("reason", reason);
							pJa.add(jsobj);
						}else if("0".equals(reason)&&"8".equals(errorinfo)) {//失败原因为：乘客身份信息核验，且出票系统通知的乘客是已通过，接口做拦截处理。
							jsobj = new JSONObject();
							for(DBPassengerInfo cp : dbPassengers){
								if(cp.getCp_id().equals(cp_id)){
									certType = cp.getElong_ids_type();
									break;
								}
							}
							jsobj.put("certNo", certNo);
							jsobj.put("certType", certType);
							jsobj.put("name", name);
							jsobj.put("reason", "2");
							pJa.add(jsobj);
						}
					}
					if(pJa != null && pJa.size()>0){
						passenger_reason = pJa.toString();
						logger.info("passenger_reason=" + passenger_reason + ", order_id : " + orderId);
					}
				}
				//错误信息：1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验  【qunar】错误信息：0、其他 1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符 4、车次数据与12306不一致 5、乘客信息错误 6、12306乘客身份信息核验失败
				/*paramMap.put("order_id", orderId);
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", Consts.ELONG_ORDER_FAIL);//出票失败
				paramMap.put("passenger_reason", passenger_reason);
				paramMap.put("out_fail_reason", errorinfo);*/
				write2Response(response, "success");
				orderInfo.setBuy_money(buyMoney);
				orderInfo.setOut_ticket_billno(billNo);
				orderInfo.setOrder_status(Consts.ELONG_ORDER_FAIL);
				orderInfo.setPassenger_reason(passenger_reason);
				orderInfo.setOut_fail_reason(errorinfo);
				
				/**更新订单表*/
				/**更新订单信息*/
				orderService.updateOrderInfo(cpMapList,orderInfo);
			}else{//异常
				logger.info("exception：订单" + orderId + "，接口返回未知状态码！");
				write2Response(response, "failed");
			}
		}catch (Exception e) {
			logger.info("获取出票结果通知异常"+e);
			write2Response(response, "failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * 明细数据组合
	 */
	private void detailDataPacking(String seatTrains, List<Map<String, String>> cpMapList,
			HttpServletResponse response){
		//CP0120212|133|12|058号#CP0120213|133|12|059号
		logger.info("明细拆分内容"+seatTrains);
		String[] seatMsgs = seatTrains.split("#");
		Map<String, String> cpMap = null;
		for (String seatMsg : seatMsgs) {//CP0120212|133|12|058号
			String[] str = seatMsg.split("\\|");
			if(str == null ){
				//参数为空
				logger.info("exception：参数拆分失败！");
				write2Response(response, "failed");
				return;
			}
			cpMap = new HashMap<String, String>(5);
			cpMap.put("cp_id",str[0]);
			cpMap.put("buy_money",str[1]);//成本价格
			cpMap.put("train_box",str[2]);//车厢
			cpMap.put("seat_no",str[3]);//座位号
			if(str.length == 5){
				cpMap.put("seat_type",str[4]);//座位类型
			}
			cpMapList.add(cpMap);
		}
	}
	
	/**
	 * 接收退票结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/refundNotify_no.jhtml")
	public void refundNotify_no(HttpServletRequest request,
			HttpServletResponse response){
		String result = this.getParam(request, "result");//成功
		String orderId = this.getParam(request, "orderid");
		String cpid = this.getParam(request, "cpid");
		String alterdiffmoney = this.getParam(request, "alterdiffmoney");
		String refundmoney = this.getParam(request, "refundmoney");
		String refund12306money = this.getParam(request, "refund12306money");
		String refund12306seq = this.getParam(request, "refund12306seq");
		String status = this.getParam(request, "status");//0、改签通知 1、退票通知
		String our_remark = this.getParam(request, "our_remark");//备注
		String refuse_reason = this.getParam(request, "refuse_reason");//拒绝退票原因
		
		logger.info("【接收退票系统通知】参数orderId=" + orderId + "，cpid=" + cpid + 
				"，alterdiffmoney=" + alterdiffmoney + "，refund12306money=" + refund12306money +
				"，refundmoney=" + refundmoney +
				"，refund12306seq=" + refund12306seq + "，status=" + status + "，result=" + result + 
				"，our_remark=" + our_remark + "，refuse_reason=" + refuse_reason);
		Map<String,String> map = new HashMap<String,String>();
		map.put("order_id", orderId);
		map.put("cp_id", cpid);
		if(StringUtil.isEmpty(alterdiffmoney)){
			map.put("alter_tickets_money", "0.0");//改签差价
		}else{
			map.put("alter_tickets_money", alterdiffmoney);//改签差价
		}
		if(StringUtil.isEmpty(refund12306money)){
			map.put("actual_refund_money", "0.0");//12306实际退款金额
		}else{
			map.put("actual_refund_money", refund12306money);//12306实际退款金额
		}
		map.put("refund_12306_seq", refund12306seq); //12306退款流水单号
		map.put("our_remark", our_remark);//出票方备注
		map.put("refuse_reason", refuse_reason);
		map.put("refund_money", refundmoney);
		try{
			if("0".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)){
				refundService.updateCPAlterInfo(map);
				logger.info("更改elong_orderinfo_cp表的alter_money="+alterdiffmoney);
				write2Response(response, "success");
			}else if("1".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)){
				//11：退票完成   22：拒绝退票 
				map.put("order_status", "11");
				map.put("refund_status", Consts.ELONG_REFUNDSTATUS_SUCESS);
				refundService.updateRefundInfo(map);
				logger.info("更改elong_orderinfo_refundstream表的refund_status=11");
				write2Response(response, "success");
			}else if("1".equals(status) && TrainConsts.FAILURE.equalsIgnoreCase(result)){
				map.put("order_status", "22");
				map.put("refund_status", Consts.ELONG_REFUNDSTATUS_FAIL);//22、拒绝退款
				refundService.updateRefundInfo(map);
				logger.info("更改elong_orderinfo_refundstream表的refund_status=22");
				write2Response(response, "success");
			}
		}catch(Exception e){
			logger.error("接收退票结果异常！", e);
			write2Response(response, "failed");
		}
	}

}
