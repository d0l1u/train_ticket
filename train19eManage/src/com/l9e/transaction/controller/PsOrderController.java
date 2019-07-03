package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.BookService;
import com.l9e.transaction.service.PsOrderService;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.PsOrderVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.MobileMsgUtil;
import com.l9e.util.MobileMsgUtilNew;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;
import com.l9e.util.SwitchUtils;

/**
 * 配送上门出票管理
 * @author zhangjc02
 *
 */
@Controller
@RequestMapping("/psOrder")
@Deprecated
public class PsOrderController extends BaseController {
	private static final Logger logger = Logger.getLogger(PsOrderController.class);
	
	@Resource
	private PsOrderService psOrderService;
	
	@Resource
	private BookService bookService;
	
	@Resource
	private MobileMsgUtilNew mobileMsgUtilNew;
	
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryPsorderPage.do")
	public String queryPsorderPage(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入配送上门查询页面】queryPsorderPage.do");
		return "redirect:/psOrder/queryPsOrderList.do?order_status=00&order_status=11&order_status=21&order_status=23";
	}
	/**
	 * 配送上门查询列表
	 * @param request
	 * @param response
	 * @param order_status
	 * @return
	 */
	@RequestMapping("queryPsOrderList.do")
	public String queryPsorderList(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
		logger.info(user_level+"进入【配送查询列表】queryPsorderList.do");
		
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		List<String> statusList = this.getParamToList(request, "order_status");
		
		String order_id = this.getParam(request, "order_id");
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(order_id.trim().length()>0){
			paramMap.put("order_id", order_id);
		}else{
		paramMap.put("order_status", statusList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		}
		//按乘车时间排序
		String travel_time_px=this.getParam(request, "travel_time_px");
		if("up".equals(travel_time_px))paramMap.put("travel_time_asc",travel_time_px);
		else if("down".equals(travel_time_px))paramMap.put("travel_time_desc",travel_time_px);
		request.setAttribute("travel_time_px", travel_time_px);
		//按创建时间排序
		String create_time_px=this.getParam(request, "create_time_px");
		if("up".equals(create_time_px))paramMap.put("create_time_asc",create_time_px);
		else if("down".equals(create_time_px))paramMap.put("create_time_desc",create_time_px);
		request.setAttribute("create_time_px", create_time_px);
		//按发车时间排序
		String out_ticket_time_px=this.getParam(request, "out_ticket_time_px");
		if("up".equals(out_ticket_time_px))paramMap.put("out_ticket_time_asc",out_ticket_time_px);
		else if("down".equals(out_ticket_time_px))paramMap.put("out_ticket_time_desc",out_ticket_time_px);
		
		if("".equals(travel_time_px) && "".equals(create_time_px) && "".equals(out_ticket_time_px)){
			paramMap.put("create_time_asc","up");
			request.setAttribute("create_time_px", "up");
		}
		request.setAttribute("out_ticket_time_px", out_ticket_time_px);
		
		int totalCount = psOrderService.queryPsOrderCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String, String>> psOrderList = psOrderService.queryPsOrderList(paramMap);
		
		if(order_id.trim().length()>0){
			request.setAttribute("order_id", order_id);
		}else{
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("statusStr", statusList.toString());
		}
		request.setAttribute("seat_Types", AcquireVo.getSEAT_TYPES());
		request.setAttribute("psOrderStatus", PsOrderVo.getPsOrderStatus());
		request.setAttribute("psOrderList", psOrderList);
		request.setAttribute("isShowList", 1);
		return "psOrder/psOrderList";
	}

	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryPsOrderInfo.do")
	public String queryPsorderInfo(HttpServletRequest request,
			HttpServletResponse response){
		String canOperation = this.getParam(request, "canOperation");
		String order_id = this.getParam(request,"order_id");
		logger.info("【查询明细】queryManualInfo.do 【订单号："+order_id+"】");
		Map<String, String> orderInfo = psOrderService.queryPsOrderInfo(order_id);
		logger.info("查询明细获取orderId"+orderInfo.get("order_id"));
		List<Map<String, Object>> cpList = psOrderService.queryPsOrderInfoCp(order_id);
		
		List<Map<String, Object>> history = psOrderService.queryHistroyByOrderId(order_id);
		
		Map<String, String> orderInfoPssm = psOrderService.queryPsOrderInfoPssm(order_id);
		String query_type = this.getParam(request, "query_type");
		
		/*******************以下为订单与车票信息拼接的信息***********************/
		/***********************拼接结束************************/
		String ext_seattype = orderInfo.get("ext_seattype").toString();// 41#1,23.00|2, 24.00|3,33.00
		List<Map<String, String>> seatList = new ArrayList<Map<String, String>>();
		if(StringUtil.isNotEmpty(ext_seattype) && !SwitchUtils.splitStr1Last(ext_seattype).equals("无")){
//			String sp = SwitchUtils.splitStr1Pre(ext_seattype);//41
//			String sp0 = sp.substring(0,1);//4
			String sp1 = SwitchUtils.splitStr1Last(ext_seattype);//1,23.00|2, 24.00|3,33.00
			
			Map<String, String> seatMap = null;
			if(StringUtil.isNotEmpty(sp1)){
				for(String str : sp1.split("\\|")){
					seatMap = new HashMap<String, String>();
					String type = str.split(",")[0];
					String money = str.split(",")[1];
					seatMap.put("s_type", type);
					seatMap.put("money", money);
					seatList.add(seatMap);
				}
			}
		}
		request.setAttribute("orderInfoPssm", orderInfoPssm);
		request.setAttribute("query_type", query_type);
		request.setAttribute("seat_type", AcquireVo.getSEAT_TYPES());
		request.setAttribute("seatList", seatList);
		request.setAttribute("history", history);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("canOperation", canOperation);
		request.setAttribute("psOrderStatus", PsOrderVo.getPsOrderStatus());
		request.setAttribute("idstype", BookVo.getIdstype());
		request.setAttribute("tickettype", BookVo.getTicketType());
		request.setAttribute("seattype", BookVo.getSeattype());
		request.setAttribute("cpList", cpList);
		
		return "psOrder/psOrderInfo";
	}
	
	//查询订单的操作日志
	@RequestMapping("/queryOrderOperHistory.do")
	@ResponseBody
	public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
		String order_id = this.getParam(request,"order_id");
		List<Map<String, Object>> history = psOrderService.queryHistroyByOrderId(order_id);
		JSONArray jsonArray = JSONArray.fromObject(history);  
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(jsonArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新订单信息
	 * @param acquire
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updatePsOrderInfo.do")
	public void updatePsOrderInfo(AcquireVo acquire, HttpServletRequest request,HttpServletResponse response){
		logger.info("【更新订单信息】updatePsOrderInfo.do-- order_id:"+acquire.getOrder_id());
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		String order_id = this.getParam(request, "order_id");
		String order_status = this.getParam(request, "order_status");
		String error_info = this.getParam(request, "error_info");
		String ps_fail_reason = this.getParam(request, "ps_fail_reason");
		acquire.setOpt_ren(user);
		
//		出票状态 00、开始出票 11、出票成功 12、出票失败 21、正在配送 22、配送成功 23、配送失败
		if(!StringUtils.isEmpty(order_id)){
			String db_order_status = psOrderService.queryDbOrderStatus(order_id);//查询表格中的状态
			Map<String, String> orderInfo = bookService.queryBookOrderInfo(order_id);//查询hc_orderinfo表
			if(order_status!=null && db_order_status.equals("00") && order_status.equals("12")){  //准备出票--》  出票失败
				acquire.setError_info(error_info);
				psOrderService.updatePsOrderStatus(acquire);
				logger.info("【更新订单信息】【准备出票】》》》【出票失败】成功！order_id:"+order_id+"【配送单】");
				String userAccount = user+"点击了【出票失败】按钮!【配送单】";
				
				//更新预订表
				if(!StringUtils.isEmpty(orderInfo.get("order_status"))	&&  "45".equals(orderInfo.get("order_status"))){
					logger.info("本次点击【出票失败】通知为重复请求，orderId=" + order_id+"【配送单】");
					return;
				}
				Map<String, String> hcFail = new HashMap<String, String>();
				hcFail.put("order_id", order_id);
				hcFail.put("order_status", "45");//出票失败
				Map<String, String> failRefundMap = new HashMap<String, String>();//出票失败退款map
				failRefundMap.put("order_id", order_id);
				failRefundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
				failRefundMap.put("refund_type", "3");//出票失败退款
				String refund_money = String.valueOf(Double.parseDouble(String.valueOf(orderInfo.get("pay_money")))+25);
				failRefundMap.put("refund_money", refund_money);
				failRefundMap.put("user_remark", error_info);
				failRefundMap.put("refund_status", "00");//准备退款
				int count = psOrderService.updateOrderWithCpNotify(hcFail, null, failRefundMap);
				if (count == 1){
					//比支付时间超过5min
					String now = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
					String min = DateUtil.getDistanceTime(orderInfo.get("pay_time"), now);
					if( Integer.parseInt(min) >= 5){
						//发送出票失败短信
						if(StringUtils.isEmpty(orderInfo.get("level")) || "0".equals(orderInfo.get("level"))){
							//普通用户不发送出票失败
						}else{
						this.sendCpFailMsn(order_id);
						logger.info("订单号为："+order_id+"，支付时间为："+orderInfo.get("pay_time")+"，距现在超过5分钟，发送出票失败短信!【配送单】");
						userAccount+="；更新预订 表成功。";
						}
					}
				}else{
					logger.info("更新hc_orderinfo 表失败！order_id:"+order_id+"【配送单】");
					userAccount+="；更新预订 表失败。";
				}
				//写入日志
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				bookService.addUserAccount(paramMap);
				logger.info("添加操作日志成功"+userAccount+"订单id为："+order_id);
					
				
			}else if(order_status!=null && db_order_status.equals("00") && order_status.equals("11")){//准备出票--》  出票成功
				String buy_money_total = this.getParam(request, "buy_money_total");
				String out_ticket_billno =this.getParam(request, "out_ticket_billno");
				List<String> cp_id_list = this.getParamToList(request, "cp_id");
				List<String> train_box_list= this.getParamToList(request, "train_box");
				List<String> seat_no_list= this.getParamToList(request, "seat_no");
				List<String> buy_money_list = this.getParamToList(request, "per_buy_money");
				
				logger.info("【出票成功】start！buy_money_total="+buy_money_total+
						",out_ticket_billno="+out_ticket_billno+
						",cp_id_list="+cp_id_list.toString()+
						",train_box_list="+train_box_list.toString()+
						",seat_no_list="+seat_no_list.toString()+
						",buy_money_list"+buy_money_list.toString());
				
				List<Map<String, String>> cpList = new ArrayList<Map<String, String>>();
				for(int i=0;i<cp_id_list.size();i++){
					Map<String, String> temp = new HashMap<String, String>();
					temp.put("cp_id", cp_id_list.get(i));
					temp.put("order_id", order_id);
					temp.put("train_box", train_box_list.get(i));
					temp.put("seat_no", seat_no_list.get(i));
					temp.put("buy_money", buy_money_list.get(i));
					cpList.add(temp);
				}
				
				psOrderService.updatePsOrderCpList(cpList);
				logger.info("【修改子表中车厢，座位号与价钱成功】");
				
				acquire.setOrder_id(order_id);
				acquire.setBuy_money(buy_money_total);
				acquire.setOut_ticket_billno(out_ticket_billno);
				psOrderService.updatePsOrderStatus(acquire);
				String userAccount = user+"点击了【准备出票】》》》【出票完成】按钮!【配送单】";
				logger.info("【更新订单信息】【准备出票】》》》【出票成功】成功！order_id:"+order_id+"【配送单】");
				
				
				//修改hc_orderinfo和hc_orderinfo_cp表
				Map<String, String> hc = new HashMap<String, String>(3);//主订单参数
				hc.put("order_id", order_id);
				hc.put("buy_money", buy_money_total);
				hc.put("out_ticket_billno", out_ticket_billno);
				hc.put("order_status", TrainConsts.OUT_SUCCESS);//出票成功
				int number2 = psOrderService.updateOrderWithCpNotify(hc, cpList, null);
				if (number2 == 1){
					logger.info("更新hc_orderinfo 表成功！order_id:"+order_id+"【配送单】");
					userAccount+="；更新预订 表成功。";
					//发送出票成功短信
					this.sendCpSuccMsn(order_id, out_ticket_billno,cpList);
				}else{
					logger.info("更新hc_orderinfo 表失败！order_id:"+order_id+"【配送单】");
					userAccount+="；更新预订 表失败。";
				}
				
				//写入日志
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				bookService.addUserAccount(paramMap);
				logger.info("添加操作日志成功"+userAccount+"订单id为："+order_id);
			}else if(order_status!=null && db_order_status.equals("11") && order_status.equals("21")){  //出票成功--》开始配送
				acquire.setError_info(error_info);
				psOrderService.updatePsOrderStatus(acquire);
				logger.info("【更新订单信息】【出票成功】》》》【开始配送】成功！order_id:"+order_id+"【配送单】");
				//修改hc_orderinfo和hc_orderinfo_cp表
				Map<String, String> hc = new HashMap<String, String>(3);//主订单参数
				hc.put("order_id", order_id);
				hc.put("order_status", "P1");//开始配送 P1开始配送 P2配送成功 P3配送失败
				psOrderService.updateOrderWithCpNotify(hc, null, null);
				
				String userAccount = user+"点击了【开始配送】按钮!【配送单】";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				bookService.addUserAccount(paramMap);
				logger.info("添加操作日志成功"+userAccount+"订单id为："+order_id);
			}else if(order_status!=null && db_order_status.equals("21")&& order_status.equals("22")){  //开始配送/配送失败--》配送成功
				acquire.setPs_fail_reason(ps_fail_reason);
				psOrderService.updatePsOrderStatus(acquire);
				logger.info("【更新订单信息】【开始配送】》》》【配送成功】成功！order_id:"+order_id+"【配送单】");
				//修改hc_orderinfo和hc_orderinfo_cp表
				Map<String, String> hc = new HashMap<String, String>(3);//主订单参数
				hc.put("order_id", order_id);
				hc.put("order_status", "P2");//开始配送 P1开始配送 P2配送成功 P3配送失败
				psOrderService.updateOrderWithCpNotify(hc, null, null);
				String userAccount = user+"点击了【配送成功】按钮!【配送单】";
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("order_id", order_id);
				paramMap.put("user", user);
				paramMap.put("userAccount", userAccount);
				bookService.addUserAccount(paramMap);
				logger.info("添加操作日志成功"+userAccount+"订单id为："+order_id);
			}
		}else{
			logger.info("order_id为空");
		}
	}
	
	/**
	 * 支付锁
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryPayIsLock.do")
	@ResponseBody
	public void queryPayIsLock(HttpServletResponse response ,HttpServletRequest request){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String order_id = this.getParam(request, "order_id");
//		if(order_id.contains("_")){//“联程”车票订单锁
//			order_id = order_id.substring(0, order_id.length()-2);
//		}
		String opt_person = loginUserVo.getReal_name();
		String key = "Lock_" + order_id;
		String value = "Lock_"+order_id+"&"+opt_person;
		String isLock;
		isLock = (String) MemcachedUtil.getInstance().getAttribute(key); //读值
		if(StringUtils.isEmpty(isLock)){
			MemcachedUtil.getInstance().setAttribute(key, value, 5*60*1000); //写值
			isLock="";
		}else if(isLock.indexOf(opt_person) != -1){
			isLock = "";
		}
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(isLock);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("支付锁时response.getWriter()异常");
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 发送出票失败短信
	 */
	private void sendCpFailMsn(String orderId){
		//发送出票失败短信
//		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		Map<String, String> contact = bookService.queryBookOrderInfo(orderId);//查询hc_orderinfo表
		//【19e火车票】您订购的07月26日13:50（福州—福州南）T101次的火车票出票失败，谢谢！
		//您订购的07月26日13:50（福州—福州南）T101次的火车票出票失败，谢谢！【19易】
		StringBuffer content = new StringBuffer();
		if(contact != null){
			content.append("您");
			content.append("订购的")
				   .append(contact.get("from_time"))
				   .append("（").append(contact.get("from_city")).append("-").append(contact.get("to_city")).append("）")
				   .append(contact.get("train_no")).append("次").append("的火车票出票失败，谢谢！【19易】");
			logger.info("短信内容"+content);
			/*
			 * 发送短信通知出票失败
			 */
			new MobileMsgUtil().send(contact.get("link_phone"), content.toString());
		}
	}
	
	/**
	 * 发送出票成功短信
	 */
	private void sendCpSuccMsn(String orderId, String billNo,List<Map<String, String>> conList){
		//发送出票成功短信
		Map<String, String> contact = bookService.queryBookOrderInfo(orderId);//查询hc_orderinfo表
		if(contact != null && conList != null && conList.size() > 0){
			//【步步高超市】E888888888，提醒您成功订购07月26日13:50（福州—福州南）D6237次2车14D号席位，张XX、李XX请持二代身份证乘车或换票乘车。
			StringBuffer content = new StringBuffer();
			//19e加盟站提醒您，我们非常荣幸的帮助您预定成功编号为E297492020的11月12日20:55（北京西-武昌）Z37次张睿的12车17号下铺、
			//陈迪12车18号上铺、严志有12车20号上铺席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝你旅途愉快！
			if(StringUtils.isEmpty(contact.get("shop_short_name"))){
				content.append("19e加盟站");
			}else{
				content.append(contact.get("shop_short_name"));
			}
			content.append("提醒您，我们非常荣幸的帮助您成功订购编号为")
				   .append(billNo)
				   .append("的")
				   .append(contact.get("from_time"))
				   .append("（").append(contact.get("from_city")).append("-").append(contact.get("to_city")).append("）")
				   .append(contact.get("train_no")).append("次");
			for(int i=0; i<conList.size(); i++){
				Map<String, String> conDetail = conList.get(i);
				if(i > 0){
					content.append("、");
				}
				content.append(conDetail.get("user_name"))
				       .append(conDetail.get("train_box")).append("车").append(conDetail.get("seat_no"));
			}
			content.append("席位的车票，我们将会尽快将送票上门给您，祝您旅途愉快！");
			
			/*
			 * 发送短信通知车票预订成功
			 */
			mobileMsgUtilNew.send(contact.get("link_phone"), content.toString(),"22");
		}
	}
	
	
	
}
