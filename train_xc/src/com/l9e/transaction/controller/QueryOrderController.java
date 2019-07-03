package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.l9e.common.ExternalBase;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.ExternalOrderInfo;
import com.l9e.transaction.vo.ExternalTicketInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;

/**
 * 预订查询
 * @author zhangjun
 *
 */
@Component
public class QueryOrderController extends ExternalBase {
	
	@Resource
	private OrderService orderService;
	
	private static final int PAGE_SIZE = 10;//每页显示的条数
	/**
	 * 查询订单列表
	 * @param request
	 * @param response
	 * @return
	 */
	public void queryOrderList(HttpServletRequest request, 
			HttpServletResponse response,String order_id,String merchant_order_id){
		ExternalOrderInfo foi = new ExternalOrderInfo();
		JSONObject errReturn = new JSONObject();
		try{
			order_id =  orderService.queryOrderIdById(merchant_order_id);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("order_id", order_id);//订单号
			paramMap.put("merchant_order_id", merchant_order_id);//合作商户订单号
			int count = orderService.queryOrderListCount(paramMap);
			if(count==0){
				errReturn.put("return_code","301");
				errReturn.put("message","订单未找到（订单号错误或该订单不存在）");
				printJson(response,errReturn.toString());
				return;
			}
			logger.info("查询出订单" + count + "条");
			//分页
			PageVo page = PageUtil.getInstance().paging(request, PAGE_SIZE, count);
			
			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			List<OrderInfo> orderList = orderService.queryOrderList(paramMap);
			foi.setOrder_id(order_id);
			foi.setMerchant_order_id(merchant_order_id);
			ExternalTicketInfo ft = null;
			for(OrderInfo order: orderList){
				ft = new ExternalTicketInfo();
				ft.setArrive_station(order.getArrive_station());
				ft.setArrive_time(order.getArrive_time());
				ft.setTicket_price(order.getTicket_pay_money());
				ft.setFrom_station(order.getFrom_station());
				ft.setFrom_time(order.getFrom_time());
				ft.setOrder_status(TrainConsts.getExtBookStatus().get(order.getOrder_status()));
				ft.setSeat_type(order.getSeat_type());
				ft.setPay_time(order.getPay_time());
				ft.setPay_money(order.getPay_money());
				ft.setOut_ticket_time(order.getOut_ticket_time());
				ft.setTrain_code(order.getTrain_no());
				ft.setTravel_time(order.getTravel_time());
				ft.setRefund_status(order.getRefund_status());
				List<BookDetailInfo> book_detail_list = new ArrayList<BookDetailInfo>();
				book_detail_list = orderService.queryOrderCpList(order_id);
				for(BookDetailInfo bd:book_detail_list){
					Map<String, String> bdMap = new HashMap<String, String>();
					bdMap.put("order_id", order_id);
					bdMap.put("ids_type", bd.getIds_type());
					bdMap.put("user_ids", bd.getUser_ids());
					int cot = orderService.queryOrderBxCount(bdMap);
					if(order.getOrder_level().equals("1")){
						Map<String,String> bx_map = orderService.queryOrderBxInfo(bdMap);
						bd.setBx(String.valueOf(cot));
						bd.setBx_code(bx_map.get("bx_code"));
					}else{
						bd.setBx_code("");
					}
				}
				ft.setBook_detail_list(book_detail_list);
			}
			foi.setTicket_list(ft);
			foi.setReturn_code("000");
			JSONObject jsonObject = new JSONObject();
			jsonObject = JSONObject.fromObject(foi);
			System.out.println("订单查询返回结果："+jsonObject.toString());
			printJson(response, jsonObject.toString());
		}catch(Exception e){
			logger.error("对外查询订单异常！",e);
			errReturn.put("return_code","001");
			errReturn.put("message","系统错误，未知服务异常。");
			printJson(response,errReturn.toString());
			return;
		}
	}
	
	/**
	 * 查询订单信息
	 * @param request
	 * @param response
	 * @return
	 */
	public void queryOrderInfo(HttpServletRequest request, 
			HttpServletResponse response,String order_id,String merchant_order_id){
		JSONObject errReturn = new JSONObject();
		try{
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("order_id", order_id);//订单号
			paramMap.put("merchant_order_id", merchant_order_id);//合作商户订单号
			List<BookDetailInfo> book_detail_list = new ArrayList<BookDetailInfo>();
			book_detail_list = orderService.queryOrderCpList(order_id);
			for(BookDetailInfo bd:book_detail_list){
				Map<String, String> bdMap = new HashMap<String, String>();
				bdMap.put("order_id", order_id);
				bdMap.put("ids_type", bd.getIds_type());
				bdMap.put("user_ids", bd.getUser_ids());
				int cot = orderService.queryOrderBxCount(bdMap);
				bd.setBx(String.valueOf(cot));
			}
			JSONObject jsonObject = new JSONObject();
			System.out.println("订单查询返回结果："+jsonObject.toString());
			printJson(response, jsonObject.toString());
		}catch(Exception e){
			logger.error("对外查询订单异常！",e);
			errReturn.put("return_code","001");
			errReturn.put("message","系统错误，未知服务异常。");
			printJson(response,errReturn.toString());
			return;
		}
	}
}
