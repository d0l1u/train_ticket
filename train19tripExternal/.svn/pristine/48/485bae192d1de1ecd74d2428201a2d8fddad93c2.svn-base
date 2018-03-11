package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.TextService;
import com.l9e.transaction.service.impl.RefundServiceImpl;
import com.l9e.util.TextUtil;
import com.l9e.util.hcpjar.HcpTicketQuery;


/**
 * 自动出票
 * @author liuyi02 仅用于测试
 */
@Component("textOutTicket")
public class TextOutTicket {
	private static final String QUERY_URL="http://10.3.12.95:18086/tcQueryTicketPrice";
	/**
	 * 发货
	 */
	@Resource
	TextService extService;
	private static final Logger logger = Logger.getLogger(TextOutTicket.class);
	@Resource
	protected CommonService commonService;
	
	
	public int numm=1;
	public int outnumm=1;
	
	public int errorInfo=1;
	
	public int outerrorInfo=1;
	
	public void outsend(){
//		System.setProperty("http.proxyHost", "192.168.65.126");
//		System.setProperty("http.proxyPort", "3128");
		logger.info("----------执行out_send------------");
		doSend();
		doOut();
		dofail();
		String refund_type=commonService.querySysSettingByKey("refund_type");
		if("11".equals(refund_type)){
//			logger.info("测试专用  关闭关闭关闭 自动退票");
		}
		else{
			//**自动退票的搞起来*//
			List<Map<String,Object>> refundOrderIds=extService.queryRefundTicket();
			if(refundOrderIds!=null&&refundOrderIds.size()>0){
				for(Map<String,Object> param:refundOrderIds){
					logger.info("自动退票"+param.get("order_id")+"__"+param.get("cp_id"));
					param.put("actual_refund_money",param.get("refund_money"));
					param.put("refund_12306_seq",TextUtil.get12306tk());
					param.put("refund_status","11");
					extService.updateRefundOrderInfo(param);
					
					Map<String,String> map=new HashMap<String,String>();
					map.put("order_id", param.get("order_id")+"");
					map.put("refund_seq", param.get("refund_seq")+"");
					extService.updateRefundNoticeInfo(map);
				}
			}
		}
	}
	private void doOut() {
		String pay_to_sucess=commonService.querySysSettingByKey("pay_to_sucess");
		if("11".equals(pay_to_sucess)){
//			logger.info("测试专用  关闭关闭关闭  先预订后支付的 支付----》成功");
		}
		else{
//			logger.info("测试专用  先预订后支付的 支付----》成功");
			List<String> orderIds=extService.queryPaySuccess();//cp_orderinfo 待支付状态55 
			if(orderIds!=null&&orderIds.size()>0){
				for(String order_id:orderIds){
//					if(outnumm==1){
						Map<String,Object> orderInfo=new HashMap<String, Object>();
						orderInfo.put("order_id", order_id);
						orderInfo.put("order_status","99");
						extService.updateOrderInfo(orderInfo);
						extService.updateNoticeInfo(orderInfo);
						outnumm=2;
//					}else{
//						Map<String,Object> orderInfo=extService.queryOrderInfo(order_id);
//						orderInfo.put("order_id", order_id);
//						outerrorInfo=outerrorInfo+1;
//						if(outerrorInfo>6){
//							outerrorInfo=1;
//						}
//						orderInfo.put("error_info", outerrorInfo);
//						orderInfo.put("order_status","10");
//						extService.updateOrderInfo(orderInfo);
//						extService.updateNoticeInfo(orderInfo);
//						outnumm=1;
//					}
				}
			}
		}
	}
	private void doSend(){
		String type=commonService.querySysSettingByKey("out_ticket_type");
		if("11".equals(type)){
//			logger.info("测试专用  关闭关闭关闭  测试自动出票and自动退票");
		}
		else{
			logger.info("测试专用  测试自动出票and自动退票");
			List<String> orderIds=extService.queryOutTicket();//cp_orderinfo  00  ext 
			if(orderIds!=null&&orderIds.size()>0){
				for(String order_id:orderIds){
					logger.info("测试专用  测试自动出票"+order_id);
					Map<String,Object> orderInfo=extService.queryOrderInfo(order_id);
					List<String> cpids=extService.queryOutTicketCpId(order_id);
//					String channel=orderInfo.get("channel")+"";
//					if("elong".equals(channel)){
//						String is_pay =orderInfo.get("is_pay")+"";
//						//先预订后支付的
//						String fail_ticket_type=commonService.querySysSettingByKey("fail_ticket_type");
//						if("11".equals(is_pay)){
//							System.out.println(order_id);
//							long orderIdNum=Long.parseLong(order_id);
//							long nummm=orderIdNum%2;
//							if(nummm==1){
//								int i=new Random().nextInt(6);
//								errorInfo=errorInfo+1;
//								if(errorInfo>6){
//									errorInfo=1;
//								}
//								orderInfo.put("error_info", errorInfo);
//								orderInfo.put("order_status","10");
//								extService.updateOrderInfo(orderInfo);
//								extService.updateNoticeInfo(orderInfo);
//								numm=2;
//							}else if(nummm==0){
//								String out_ticket_billno=TextUtil.getBillno();
//								String bank_pay_seq=TextUtil.getPaySeq();
//								int seat_no=TextUtil.getSeatNo();
//								int train_box=TextUtil.getTrainbox();
//								double all_buy_money=0;
//								double buy_money=new Random().nextInt(500)+10;
//								//from_city to_city
//								//queryPrice(String from_station,String to_station,String train_no,String seat_type)
//								String from_time=orderInfo.get("from_time")+"";
//								String trainDate=from_time.substring(0, from_time.indexOf(" "));                        								//
//								String elong_seat_type=	extService.selectSeatType(order_id);
//								buy_money=queryPrice(orderInfo.get("from_city")+"",orderInfo.get("to_city")+"",orderInfo.get("train_no")+"",elong_seat_type,trainDate);
//								logger.info("query the buy_money is "+buy_money);
//								for(String cp_id:cpids){
//									Map<String,Object> cpInfo=extService.queryCpInfo(cp_id);
//									if(cpInfo.get("seat_type").toString().equals("5")||cpInfo.get("seat_type").toString().equals("6")){
//										cpInfo.put("buy_money",buy_money-(new Random().nextInt(5)));
//										cpInfo.put("seat_no", seat_no);
//									}else if("1".equals(cpInfo.get("ticket_type").toString())){
//										cpInfo.put("buy_money",buy_money/2);
//										cpInfo.put("seat_no", seat_no);
//									}else{
//										cpInfo.put("buy_money", buy_money);
//										cpInfo.put("seat_no", seat_no);
//									}
//									all_buy_money=all_buy_money+Double.parseDouble(cpInfo.get("buy_money").toString());
//									cpInfo.put("train_box", train_box);
//									extService.updateCpInfo(cpInfo);
//									seat_no++;
//								}
//								orderInfo.put("buy_money",all_buy_money);
//								orderInfo.put("out_ticket_billno", out_ticket_billno);
//								orderInfo.put("bank_pay_seq", bank_pay_seq);
//								orderInfo.put("order_status","45");
//								extService.updateOrderInfo(orderInfo);
//								extService.updateNoticeInfo(orderInfo);
//								numm=1;
//							}
//						}else{
//							long orderIdNum=Long.parseLong(order_id);
//							long nummm=orderIdNum%2;
//							if(nummm==1){
//								int i=new Random().nextInt(6);
//								errorInfo=errorInfo+1;
//								if(errorInfo>6){
//									errorInfo=1;
//								}
//								if(errorInfo==3){
//									errorInfo=4;
//								}
//								orderInfo.put("error_info", errorInfo);
//								orderInfo.put("order_status","10");
//								extService.updateOrderInfo(orderInfo);
//								extService.updateNoticeInfo(orderInfo);
//								numm=2;
//							}else if(nummm==0){
//								String out_ticket_billno=TextUtil.getBillno();
//								String bank_pay_seq=TextUtil.getPaySeq();
//								int seat_no=TextUtil.getSeatNo();
//								int train_box=TextUtil.getTrainbox();
//								double all_buy_money=0;
//								for(String cp_id:cpids){
//									Map<String,Object> cpInfo=extService.queryCpInfo(cp_id);
//									if(cpInfo.get("seat_type").toString().equals("5")||cpInfo.get("seat_type").toString().equals("6")){
//										//cpInfo.put("buy_money",Double.parseDouble(cpInfo.get("pay_money").toString())-5);
//										cpInfo.put("buy_money",Double.parseDouble(cpInfo.get("pay_money").toString()));
//										cpInfo.put("seat_no", seat_no);
//									}else if("1".equals(cpInfo.get("ticket_type").toString())){
//										cpInfo.put("buy_money",Double.parseDouble(cpInfo.get("pay_money").toString())/2);
//										cpInfo.put("seat_no", seat_no);
//									}else{
//										cpInfo.put("buy_money", cpInfo.get("pay_money"));
//										cpInfo.put("seat_no", seat_no);
//									}
//									all_buy_money=all_buy_money+Double.parseDouble(cpInfo.get("buy_money").toString());
//									cpInfo.put("train_box", train_box);
//									extService.updateCpInfo(cpInfo);
//									seat_no++;
//								}
//								orderInfo.put("buy_money",all_buy_money);
//								orderInfo.put("out_ticket_billno", out_ticket_billno);
//								orderInfo.put("bank_pay_seq", bank_pay_seq);
//							
//								orderInfo.put("order_status","99");
//								extService.updateOrderInfo(orderInfo);
//								extService.updateNoticeInfo(orderInfo);
//							}
//						}
//					
//					
//					}else{
						//其他渠道订单
						/**先预定 后支付测试*/
						Map<String,Object> ext_merchantinfo=extService.queryMerchantinfo(orderInfo.get("channel")+"");
						String out_ticket_billno=TextUtil.getBillno();
						String bank_pay_seq=TextUtil.getPaySeq();
						int seat_no=TextUtil.getSeatNo();
						int train_box=TextUtil.getTrainbox();
						double all_buy_money=0;
						double buy_money=new Random().nextInt(500)+10;
						for(String cp_id:cpids){
							Map<String,Object> cpInfo=extService.queryCpInfo(cp_id);
							/*if("1".equals(cpInfo.get("ticket_type").toString())){
								cpInfo.put("buy_money",Double.parseDouble(cpInfo.get("pay_money").toString())/2);
							}else{1
								cpInfo.put("buy_money", cpInfo.get("pay_money"));
							}*/
							if("11".equals(String.valueOf(ext_merchantinfo.get("book_flow")))){
								if(cpInfo.get("seat_type").toString().equals("5")||cpInfo.get("seat_type").toString().equals("6")){
									cpInfo.put("buy_money",buy_money-5);
									cpInfo.put("seat_no", seat_no);
								}else if("1".equals(cpInfo.get("ticket_type").toString())){
									cpInfo.put("buy_money",buy_money/2);
									cpInfo.put("seat_no", seat_no);
								}else{
									cpInfo.put("buy_money", buy_money);
									cpInfo.put("seat_no", seat_no);
								}
								all_buy_money=all_buy_money+Double.parseDouble(cpInfo.get("buy_money").toString());
							}else{
								if(cpInfo.get("seat_type").toString().equals("5")||cpInfo.get("seat_type").toString().equals("6")){
									cpInfo.put("buy_money",Double.parseDouble(cpInfo.get("pay_money").toString())-5);
									cpInfo.put("seat_no", seat_no);
								}else if("1".equals(cpInfo.get("ticket_type").toString())){
									cpInfo.put("buy_money",Double.parseDouble(cpInfo.get("pay_money").toString())/2);
									cpInfo.put("seat_no", seat_no);
								}else{
									cpInfo.put("buy_money", cpInfo.get("pay_money"));
									cpInfo.put("seat_no", seat_no);
								}
								all_buy_money=all_buy_money+Double.parseDouble(cpInfo.get("buy_money").toString());
							}
							cpInfo.put("train_box", train_box);
							extService.updateCpInfo(cpInfo);
							seat_no++;
						}
						orderInfo.put("buy_money",all_buy_money);
						orderInfo.put("out_ticket_billno", out_ticket_billno);
						orderInfo.put("bank_pay_seq", bank_pay_seq);
						//pay_type 支付方式：11、自动代扣；22、商户自行扣费; 33、先预定后支付  
						if(ext_merchantinfo!=null){
							logger.info("pay_type="+ext_merchantinfo.get("pay_type").toString());
							logger.info("book_flow="+ext_merchantinfo.get("book_flow").toString());
							orderInfo.put("order_status","11".equals(ext_merchantinfo.get("book_flow").toString())?"33":"99");
						}else{
							orderInfo.put("order_status","99");
						}
						extService.updateOrderInfo(orderInfo);
						int num=0;
						for(int i=0;i<4;i++){
							num =extService.selectCountNumFromNotice(orderInfo);
							int count=extService.updateNoticeInfo(orderInfo);
							logger.info("查询出num:"+num+",更新条数:"+count);
							if(count==1){
								break;
							}
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
//					}
					
					
				}
			}
//			//**预订成功 转出票成功 测试 2分钟后自动出票成功！*//
//			List<String> ydOrderIds=extService.queryYdToOutTicket();
//			if(ydOrderIds!=null&&ydOrderIds.size()>0){
//				for(String order_id:ydOrderIds){
//					Map<String,Object> orderInfo=new HashMap<String, Object>();
//					orderInfo.put("order_id", order_id);
//					orderInfo.put("order_status","99");
//					extService.updateOrderInfo(orderInfo);
//					extService.updateNoticeInfo(orderInfo);
//				}
//			}
		}
	}
	
	
	private void dofail() {
			String fail_ticket_type=commonService.querySysSettingByKey("fail_ticket_type");
			if("11".equals(fail_ticket_type)){
			}
			else{
				List<String> orderIds=extService.queryOutTicket();//cp_orderinfo  00  ext 
				if(orderIds!=null&&orderIds.size()>0){
					for(String order_id:orderIds){
//						if(outnumm==1){
//							Map<String,Object> orderInfo=new HashMap<String, Object>();
//							orderInfo.put("order_id", order_id);
//							orderInfo.put("order_status","99");
//							extService.updateOrderInfo(orderInfo);
//							extService.updateNoticeInfo(orderInfo);
//							outnumm=2;
//						}else{
							Map<String,Object> orderInfo=extService.queryOrderInfo(order_id);
							orderInfo.put("order_id", order_id);
							outerrorInfo=outerrorInfo+1;
							if(outerrorInfo>6){
								outerrorInfo=1;
							}
							orderInfo.put("error_info", outerrorInfo);
							orderInfo.put("order_status","10");
							extService.updateOrderInfo(orderInfo);
							extService.updateNoticeInfo(orderInfo);
//							outnumm=1;
//						}
					}
				}
			}
	}
	
}
