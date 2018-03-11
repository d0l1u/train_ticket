package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiexun.iface.util.StringUtil;
import com.l9e.common.ExternalBase;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookInfo;
import com.l9e.transaction.vo.CreateNewRetrunOrderVo;
import com.l9e.transaction.vo.CreateOrderReturnVo;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;

/**
 * 下单
 * 
 * @author zuoyuxing
 * 
 */
@Component
public class CreateOrderController extends ExternalBase {

	private static final Logger logger = Logger.getLogger(CreateOrderController.class);

	@Resource
	private OrderService orderService;

	/**
	 * 订购下单
	 * @param bookInfo
	 * @param request
	 * @param response
	 * @return
	 */
	public void createTrainOrder(Map<String,String> merchantInfo,BookInfo bookInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		logger.info("向火车票库下单，返回火车票订单号");
		CreateOrderReturnVo corv = new CreateOrderReturnVo();
		String book_flow = String.valueOf(merchantInfo.get("book_flow"));
		String method =merchantInfo.get("method");
		String do_type =merchantInfo.get("do_type");
		String  reqtoken =merchantInfo.get("reqtoken");
		
		try{
			OrderInfo orderInfo = new OrderInfo();
			List<OrderInfoCp> orderInfoCpList = new ArrayList<OrderInfoCp>();
			List<OrderInfoBx> orderInfoBxList = new ArrayList<OrderInfoBx>();
			
			logger.info("组装数据");
			this.groupData(merchantInfo, bookInfo, orderInfo, orderInfoCpList, orderInfoBxList, request);//组合封装数据
			
			Map<String, String> bxfpMap = null;//保险发票
			if("1".equals(bookInfo.getBx_invoice())){
				bxfpMap = new HashMap<String, String>(4);
				bxfpMap.put("order_id", orderInfo.getOrder_id());
				bxfpMap.put("fp_receiver", bookInfo.getBx_invoice_receiver());
				bxfpMap.put("fp_phone", bookInfo.getBx_invoice_phone());
				bxfpMap.put("fp_zip_code", bookInfo.getBx_invoice_zipcode());
				bxfpMap.put("fp_address", bookInfo.getBx_invoice_address());
			}
			orderInfo.setAgent_id(merchantInfo.get("agent_id"));
			orderInfo.setAgent_name(merchantInfo.get("agent_name"));
			
			logger.info("代理商agent_id:"+merchantInfo.get("agent_id"));
			orderInfo.setPay_type((merchantInfo.get("pay_type")));
			orderInfo.setReqtoken(reqtoken);
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("merchant_id", merchantInfo.get("merchant_id"));
			paramMap.put("merchant_order_id", bookInfo.getMerchant_order_id());
			
			logger.info("通过count判断是否重复下单"); 
			int count = orderService.queryOrderListCount(paramMap);
			
			logger.info("查询我方订单ID");
			String  old_order_id = orderService.queryOrderId(paramMap);
			logger.info("19e订单号:"+old_order_id+",对应的高铁订单号："+bookInfo.getMerchant_order_id());
			
			if(count>0){
				if ("book".equals(method) && "1".equals(do_type)) {
					//先预定后支付，结果返回
					CreateNewRetrunOrderVo cnro=new CreateNewRetrunOrderVo();
					Gson gson=new Gson();
					cnro.setGtgjOrderId(bookInfo.getMerchant_order_id());
					cnro.setSupplierOrderId(old_order_id);
					cnro.setReqtoken("");
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("201"));
					String result_json=gson.toJson(cnro);
					logger.info("先预定后支付,下单接口，返回结果："+result_json);
					printJson(response, result_json.toString());		
					return;
				} else {
					logger.info("重复下单异常");
					printJson(response, getJson("201").toString());
					return;
				}
			}
		
			Map<String,String>  setting=new HashMap<String, String>();
			setting.put("book_flow",book_flow);
			setting.put("method", method);
			setting.put("do_type", do_type);
			orderInfo.setOrder_type("book".equals(method)&&"1".equals(do_type)?"11":"22");//设置订单类型，11：先预定后支付
			
			logger.info(bookInfo.getMerchant_order_id()+",method:"+method+",do_type:"+do_type+",book_flow:"+book_flow+",order_type:"+orderInfo.getOrder_type());
			logger.info("数据入库-insert");
			String order_id = orderService.addOrder(orderInfo, orderInfoCpList, orderInfoBxList, bxfpMap ,book_flow);
			logger.info(order_id+",method:"+method+",do_type:"+do_type+",book_flow:"+book_flow+",order_type:"+orderInfo.getOrder_type());
			
			if("no_money".equals(order_id)){
				if ("book".equals(method) && "1".equals(do_type)) {
					//先预定后支付，结果返回
					CreateNewRetrunOrderVo cnro=new CreateNewRetrunOrderVo();
					Gson gson=new Gson();
					cnro.setGtgjOrderId(bookInfo.getMerchant_order_id());
					cnro.setSupplierOrderId(order_id);
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken)?"":reqtoken);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("007"));
					String result_json=gson.toJson(cnro);
					logger.info("先预定后支付，下单接口，返回结果："+result_json);
					printJson(response, result_json.toString());
				 }else{
					JSONObject noReturn = new JSONObject();
					noReturn.put("return_code","007");
					noReturn.put("message","合作商户余额不足，暂时停用，请充值后通知服务商重新启用！");
					printJson(response,noReturn.toString());
					return;
				}
				
			}

			//先预订后支付，添加到预定结果通知表
			if("book".equals(method) && "1".equals(do_type)){
				orderInfo.setOrder_book_url(orderInfo.getOrder_result_url());//设置占座通知地址
				paramMap.put("order_id", orderInfo.getOrder_id());
				paramMap.put("notify_url", orderInfo.getOrder_book_url());
				orderService.addOrderBookNotifyInfo(paramMap);//订单	
		    }

			corv.setOrder_id(order_id);
			corv.setArrive_station(orderInfo.getArrive_station());
			corv.setArrive_time(orderInfo.getArrive_time());
			corv.setBx_pay_money(orderInfo.getBx_pay_money());
			corv.setFrom_station(orderInfo.getFrom_station());
			corv.setFrom_time(orderInfo.getFrom_time());
			corv.setMerchant_order_id(orderInfo.getMerchant_order_id());
			corv.setOrder_id(orderInfo.getOrder_id());
			corv.setPay_money(orderInfo.getPay_money());
			corv.setSeat_type(orderInfo.getSeat_type());
			corv.setSpare_pro1("");
			corv.setSpare_pro2("");
			corv.setTicket_pay_money(orderInfo.getTicket_pay_money());
			corv.setTrain_code(orderInfo.getTrain_no());
			corv.setTravel_time(orderInfo.getTravel_time());
			corv.setReturn_code("000");
			corv.setMessage("");
					
			if ("book".equals(method) && "1".equals(do_type)) {
				logger.info("[火车票先下单后付款]：火车票asp_order_id:" + order_id);
				//构造先下单后付款的同步返回信息
				Gson gson=new Gson();//格式化时，属性值为null,json无输出
				CreateNewRetrunOrderVo  cnro=new CreateNewRetrunOrderVo();
				cnro.setGtgjOrderId(bookInfo.getMerchant_order_id());
				cnro.setSupplierOrderId(order_id);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken)?"":reqtoken);
				cnro.setSuccess("true");;
				cnro.setMsg("创建订单成功");
				String result_json=gson.toJson(cnro);
				logger.info("火车票先下单后付款,订票下单返回结果："+result_json);
				printJson(response, result_json);
			} else {
				//先付款后买票
				logger.info("[火车票下单]：火车票asp_order_id:" + order_id);
				JSONObject json = JSONObject.fromObject(corv);
				logger.info("订票下单返回结果："+json.toString());
				printJson(response, json.toString());		
			}
			
			String orderOptlog="请求占座成功,method:"+method+",do_type:"+do_type+"订单类型："+orderInfo.getOrder_type();
			logInsert(order_id, orderOptlog);

		}catch(Exception e){
			logger.info(bookInfo.getMerchant_order_id()+",下单异常：",e);
			if ("book".equals(method) && "1".equals(do_type)) {
				Gson gson=new Gson();
				CreateNewRetrunOrderVo  cnro=new CreateNewRetrunOrderVo();
				cnro.setGtgjOrderId(bookInfo.getMerchant_order_id());
				cnro.setSupplierOrderId("");
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken)?"":reqtoken);
				cnro.setSuccess("false");
				cnro.setMsg(TrainConsts.getReturnCode().get("001"));
				String result_json=gson.toJson(cnro);
				logger.info("火车票先下单后付款,订票下单返回结果："+result_json);
				printJson(response, result_json);
			}else {
				logger.info("对外接口下单失败！",e);
				JSONObject errReturn = new JSONObject();
				errReturn.put("return_code","001");
				errReturn.put("message","系统错误，未知服务异常。");
				printJson(response,errReturn.toString());
			}
			
		}
	}
	
	/**
	 * 组合封装数据
	 * @param bookInfo
	 * @param orderInfo
	 * @param orderInfoCpList
	 * @param orderInfoBxList
	 */
	private void groupData(Map<String,String> merchantInfo,BookInfo bookInfo, OrderInfo orderInfo,
			List<OrderInfoCp> orderInfoCpList, List<OrderInfoBx> orderInfoBxList, HttpServletRequest request) {
		String order_id = CreateIDUtil.createID("GTGJ");
		String merchant_id=merchantInfo.get("merchant_id");
		//订单
		orderInfo.setOrder_id(order_id);
		orderInfo.setOrder_pro1(bookInfo.getOrder_pro1());
		orderInfo.setMerchant_id(merchantInfo.get("merchant_id"));
		orderInfo.setMerchant_order_id(bookInfo.getMerchant_order_id());
		orderInfo.setOrder_name(bookInfo.getFrom_station() + "/" + bookInfo.getArrive_station());
		orderInfo.setOrder_level(bookInfo.getOrder_level());
		orderInfo.setLink_name(bookInfo.getLink_name());
		orderInfo.setLink_phone(bookInfo.getLink_phone());
		orderInfo.setFrom_station(bookInfo.getFrom_station());
		orderInfo.setArrive_station(bookInfo.getArrive_station());
		orderInfo.setFrom_time(bookInfo.getFrom_time());
		orderInfo.setArrive_time(bookInfo.getArrive_time());
		orderInfo.setTrain_no(bookInfo.getTrain_code());
		orderInfo.setTravel_time(bookInfo.getTravel_time());
		orderInfo.setOut_ticket_type("11");
		orderInfo.setSms_notify(bookInfo.getSms_notify());
		orderInfo.setSeat_type(bookInfo.getSeat_type());
		orderInfo.setOrder_result_url(bookInfo.getOrder_result_url());
		orderInfo.setOrder_book_url(bookInfo.getBook_result_url());
		orderInfo.setPay_result_url(bookInfo.getPay_result_url());
		orderInfo.setOrder_pro1(bookInfo.getOrder_pro1());
		orderInfo.setBx_pay_money(this.getBxTotalPay(bookInfo));
		orderInfo.setTicket_pay_money(this.getTicketTotalPay(bookInfo));
		orderInfo.setPay_money(this.getTotalPay(bookInfo,merchantInfo));//计算支付金额=保险总额+票价总额
		orderInfo.CalculateSum();
		orderInfo.setPay_type(merchantInfo.get("pay_type"));//支付方式，22、商户自行收费，11、代付
		
		orderInfo.setIsChooseSeats(bookInfo.isIsChooseSeats()?1:0); //是否选座, 1:选, 0:非选
		orderInfo.setChooseSeats(bookInfo.getChooseSeats());  //选座信息 ,如：1A1D2A2B2F
		
		
		if(!StringUtils.isEmpty(bookInfo.getSeat_type())
				&& TrainConsts.SEAT_8.equals(bookInfo.getSeat_type()) 
				&& "1".equals(bookInfo.getWz_ext())){//硬座选择无座备选
			String ext_seat = "" + TrainConsts.SEAT_9 + "," + bookInfo.getTicket_price();
			orderInfo.setExt_seat(ext_seat);
		}
		for (BookDetailInfo bookDetailInfo : bookInfo.getBook_detail_list()) {
			//车票
			OrderInfoCp orderInfoCp = new OrderInfoCp();			
//			String cp_id = CreateIDUtil.createID("EXCP");//车票id
//			orderInfoCp.setCp_id(cp_id);
			orderInfoCp.setCp_id(bookDetailInfo.getCp_id());
			orderInfoCp.setOrder_id(order_id);			
			orderInfoCp.setPay_money(bookInfo.getTicket_price());
			orderInfoCp.setTicket_type(bookDetailInfo.getTicket_type());//车票类型
			orderInfoCp.setUser_name(bookDetailInfo.getUser_name().trim());//姓名
			orderInfoCp.setIds_type(bookDetailInfo.getIds_type());
			orderInfoCp.setUser_ids(bookDetailInfo.getUser_ids().trim());//证件号
			orderInfoCp.setSeat_type(bookInfo.getSeat_type());//坐席
			orderInfoCpList.add(orderInfoCp);
			
			//保险单价大于0则保存该保险记录
			String buy_bx = bookDetailInfo.getBx();//保险单价
			if("1".equals(buy_bx)){
				if("1".equals(bookDetailInfo.getTicket_type())){
					continue;
				}
				//查询保险渠道 保险渠道: 1、快保 2、合众
//				String bx_channel = commonService.querySysSettingByKey("bx_channel");
				//保险
				OrderInfoBx orderInfoBx = new OrderInfoBx();
				orderInfoBx.setBx_id(CreateIDUtil.createID("GTBX"));
				orderInfoBx.setOrder_id(order_id);
				orderInfoBx.setCp_id(bookDetailInfo.getCp_id());//车票id
				orderInfoBx.setUser_name(bookDetailInfo.getUser_name().trim());//姓名
				orderInfoBx.setIds_type(bookDetailInfo.getIds_type());
				orderInfoBx.setUser_ids(bookDetailInfo.getUser_ids().trim());
				orderInfoBx.setFrom_name(bookInfo.getFrom_station());//出发城市
				orderInfoBx.setTo_name(bookInfo.getArrive_station());//到达城市
				//orderInfoBx.setBx_status("0");//未发送
				if("BX_10".equals(bookInfo.getOrder_pro1())){
					orderInfoBx.setPay_money("10");
					orderInfoBx.setProduct_id("BX_10");
				}else{
					orderInfoBx.setPay_money("20");
					orderInfoBx.setProduct_id("BX_20");
				}
				//orderInfoBx.setEffect_date(bookInfo.getTravelTime());//生效日期
				orderInfoBx.setEffect_date(bookInfo.getTravel_time()+" "+bookInfo.getFrom_time()+":00");
				orderInfoBx.setTrain_no(bookInfo.getTrain_code());//车次
				orderInfoBx.setTelephone(bookInfo.getLink_phone());//联系人电话
				orderInfoBx.setBx_channel(merchantInfo.get("bx_company"));
				orderInfoBx.setMerchant_id(merchant_id);
				orderInfoBx.setOrder_channel("ext");
				orderInfoBxList.add(orderInfoBx);
			}
		}
	}
	
	/**
	 * 票价总额
	 * @param bookInfo
	 * @return
	 */
	private String getTicketTotalPay(BookInfo bookInfo){
		return	String.valueOf(AmountUtil.mul(Double.parseDouble(bookInfo.getTicket_price()), bookInfo.getBook_detail_list().size()));
	}
	
	/**
	 * 保险总额
	 * @param bookInfo
	 * @return
	 */
	private String getBxTotalPay(BookInfo bookInfo){
		double bx_total_pay = 0;//保险总计
		for(BookDetailInfo detail : bookInfo.getBook_detail_list()){
			if("1".equals(detail.getBx())){
				if("BX_10".equals(bookInfo.getOrder_pro1())){
					bx_total_pay = 10 * bookInfo.getBook_detail_list().size();
				}else{
					bx_total_pay = 20 * bookInfo.getBook_detail_list().size();
				}
				break;
			}
		}
		return String.valueOf(bx_total_pay);
	}
	
	//获取订单需要支付的总金额
	private String getTotalPay(BookInfo bookInfo,Map<String,String> map){
		double bx_total_pay = 0;//保险总计
		double ticket_total_pay = 0;//票价总计(包括手续费)
		for(BookDetailInfo detail : bookInfo.getBook_detail_list()){
			if("1".equals(detail.getBx())){
				if("BX_10".equals(bookInfo.getOrder_pro1())){
					bx_total_pay = Double.valueOf(map.get("bx_10_fee")) * bookInfo.getBook_detail_list().size();
					break;
				}else{
					bx_total_pay = Double.valueOf(map.get("bx_20_fee")) * bookInfo.getBook_detail_list().size();
					break;
				}
			}
		}
//		Double.valueOf(map.get("merchant_fee")) * bookInfo.getBook_detail_list().size();
		Map<String,String> fee_map = orderService.queryFeeModel(map.get("merchant_id"));
		String fee_type = fee_map.get("fee_type");
		if("order".equals(fee_type)){
			ticket_total_pay = Double.valueOf(fee_map.get("order_fee")) + Double.valueOf(getTicketTotalPay(bookInfo));
		}else if("ticket".equals(fee_type)){
			ticket_total_pay = Double.valueOf(fee_map.get("ticket_fee")) * bookInfo.getBook_detail_list().size() + Double.valueOf(getTicketTotalPay(bookInfo));
		}else if("percent".equals(fee_type)){
			ticket_total_pay = Double.valueOf(getTicketTotalPay(bookInfo)) * Double.valueOf(fee_map.get("percent_fee")) + Double.valueOf(getTicketTotalPay(bookInfo));
		}else if("echelon1".equals(fee_type)){
			String[] echelon_fir = map.get("echelon_fir").split(":");
			if(map.get("ticket_num").compareTo(echelon_fir[0])>0){
				ticket_total_pay = Double.valueOf(echelon_fir[1]) * bookInfo.getBook_detail_list().size() + Double.valueOf(getTicketTotalPay(bookInfo));
			}else{
				ticket_total_pay = Double.valueOf(fee_map.get("echelon_zeo")) * bookInfo.getBook_detail_list().size() + Double.valueOf(getTicketTotalPay(bookInfo));
			}
		}else if("echelon2".equals(fee_type)){
			String[] echelon_fir = fee_map.get("echelon_fir").split(":");
			String[] echelon_sec = fee_map.get("echelon_sec").split(":");
			if(map.get("ticket_num").compareTo(echelon_fir[0])<=0){
				ticket_total_pay = Double.valueOf(fee_map.get("echelon_zeo")) * bookInfo.getBook_detail_list().size() + Double.valueOf(getTicketTotalPay(bookInfo));
			}else if(map.get("ticket_num").compareTo(echelon_sec[0])>0){
				ticket_total_pay = Double.valueOf(echelon_sec[1]) * bookInfo.getBook_detail_list().size() + Double.valueOf(getTicketTotalPay(bookInfo));
			}else{
				ticket_total_pay = Double.valueOf(echelon_fir[1]) * bookInfo.getBook_detail_list().size() + Double.valueOf(getTicketTotalPay(bookInfo));
			}
		}else{
			ticket_total_pay = Double.valueOf(getTicketTotalPay(bookInfo));
		}		
		return String.valueOf(bx_total_pay+ticket_total_pay);
	}
	
	/**
	 * @param supplierOrderId
	 * @param orderOptlog
	 */
	public void logInsert(String supplierOrderId, String orderOptlog) {
		ExternalLogsVo logs=new ExternalLogsVo();
		logs.setOrder_id(supplierOrderId);
		logs.setOpter("gt_app");
		logs.setOrder_optlog(orderOptlog);
		orderService.insertOrderLogs(logs);
	}

}
