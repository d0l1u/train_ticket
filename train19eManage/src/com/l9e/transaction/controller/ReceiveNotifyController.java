package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ReceiveNotifyService;

/**
 * 接收通知接口
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/receiveNotify")
public class ReceiveNotifyController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(ReceiveNotifyController.class);
	
	@Resource
	private ReceiveNotifyService receiveNotifyService;
	
	/**
	 * 接收出票结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/cpNotify.do")
	public void cpNotify(HttpServletRequest request,
			HttpServletResponse response){
		String result = this.getParam(request, "result");
		String orderId = this.getParam(request, "orderid");
		String billNo = this.getParam(request, "billno");
		String buyMoney = this.getParam(request, "buymoney");
		String seatTrains = this.getParam(request, "seattrains");
		
		
		logger.info("[接收出票结果通知接口]参数orderId=" + orderId 
				+ "，result=" + result + "，billNo=" + billNo
				+ "，buyMoney=" + buyMoney + "，seatTrains=" + seatTrains);
		
		Map<String, String> paramMap = new HashMap<String, String>(3);//主订单参数
		List<Map<String, String>> cpMapList = new ArrayList<Map<String, String>>();//车票订单参数
		Map<String, String> cpMap = null;
		
		if(StringUtils.isEmpty(result) || StringUtils.isEmpty(orderId)
				|| StringUtils.isEmpty(billNo) || StringUtils.isEmpty(buyMoney)){
			//参数为空
			logger.info("exception：参数为空！");
			writeN2Response(response, "exception");
			
		}else if(TrainConsts.SUCCESS.equalsIgnoreCase(result)){//成功
			if(StringUtils.isEmpty(seatTrains)){
				//参数为空
				logger.info("exception：明细参数为空！");
				writeN2Response(response, "exception");
				return;
			}
			paramMap.put("order_id", orderId);
			paramMap.put("buy_money", buyMoney);
			paramMap.put("out_ticket_billno", billNo);
			paramMap.put("order_status", TrainConsts.OUT_SUCCESS);//出票成功
			
			//CP0120212|133|12|058号#CP0120213|133|12|059号
			String[] seatMsgs = seatTrains.split("#");
			for (String seatMsg : seatMsgs) {//CP0120212|133|12|058号
				String[] str = seatMsg.split("\\|");
				if(str == null || str.length != 4){
					//参数为空
					logger.info("exception：参数拆分失败！");
					writeN2Response(response, "exception");
					return;
				}
				for (int i = 0; i < str.length; i++) {
					cpMap = new HashMap<String, String>(4);
					cpMap.put("cp_id", str[0]);
					cpMap.put("buy_money", str[1]);//成本价格
					cpMap.put("train_box", str[2]);//车厢
					cpMap.put("seat_no", str[3]);//座位号
					cpMapList.add(cpMap);
				}
			}
			
			int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList);
			if (count == 1){
				writeN2Response(response, "success");
			}else{
				logger.info("failed：修改订单" + orderId + "失败！");
				writeN2Response(response, "failed");
			}
			
		}else if(TrainConsts.FAILURE.equalsIgnoreCase(result)){//失败
			
			paramMap.put("order_id", orderId);
			paramMap.put("buy_money", buyMoney);
			paramMap.put("out_ticket_billno", billNo);
			paramMap.put("order_status", TrainConsts.OUT_FAIL);//出票失败
			
			int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, null);
			if (count == 1){
				writeN2Response(response, "success");
			}else{
				logger.info("failed：修改订单" + orderId + "失败！");
				writeN2Response(response, "failed");
			}
			
		}else{//异常
			logger.info("exception：订单" + orderId + "，接口返回未知状态码！");
			writeN2Response(response, "exception");
		}
	}

}
