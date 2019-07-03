package com.l9e.transaction.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.TongChengChangeDao;
import com.l9e.transaction.service.TongChengChangeService;
import com.l9e.transaction.vo.DBChangeInfo;
import com.l9e.transaction.vo.DBPassengerChangeInfo;
import com.l9e.transaction.vo.SInfo;
import com.l9e.transaction.vo.TongchengChangeLogVO;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.elong.ElongMd5Util;
import com.l9e.util.elong.ElongUrlFormatUtil;
import com.l9e.util.elong.StrUtil;
import com.l9e.util.tongcheng.TongChengConsts;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 同城同步、异步改签service实现
 * 
 * @author licheng
 * 
 */
@Service("tongChengChangeService")
public class TongChengChangeServiceImpl implements TongChengChangeService {
	
	private static final Logger logger = Logger.getLogger(TongChengChangeServiceImpl.class);

	@Resource
	private TongChengChangeDao tongChengChangeDao;
	@Resource
	private CommonDao commonDao;
	
	@Override
	public List<DBChangeInfo> getNotifyList() {
		return tongChengChangeDao.selectNotifyList();
	}

	@Override
	public void notifyChange(Integer change_id) {

		try {
			Map<String, String> parameter = new HashMap<String, String>();
			Map<String, Object> param = new HashMap<String, Object>();
			JSONObject json = new JSONObject();
			param.put("change_id", change_id);
			
			/*拉取改签车票信息*/
			DBChangeInfo changeInfo = tongChengChangeDao.getChangeInfo(param);
			List<DBPassengerChangeInfo> cPassengers = tongChengChangeDao.selectChangeCp(param);
			String callbackurl = changeInfo.getCallbackurl();
			String reqtoken = changeInfo.getReqtoken();
			String orderid = changeInfo.getOrder_id();
			String transactionid = changeInfo.getOrder_id();
			/*通用参数*/
			String reqtime = DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
			String key = Consts.TC_SIGNKEY;
			String partnerid = Consts.TC_PARTNERID;
			String method = "";
			json.put("reqtoken", reqtoken);
			json.put("orderid", orderid);
			json.put("partnerid", partnerid);
			json.put("reqtime", reqtime);//yyyyMMddHHmmss
			String sign = ElongMd5Util.md5_32(partnerid+reqtime+(ElongMd5Util.md5_32(key, "UTF-8").toLowerCase()),"UTF-8").toLowerCase();
			json.put("sign", sign);
			
			String changeStatus = changeInfo.getChange_status();
			if(changeStatus.startsWith("1")) {
				method = "train_request_change";
				/*请求改签*/
				//from_station_code 和to_station_code 为空时查询数据库
				json.put("from_station_code", changeInfo.getFrom_station_code()!=null?changeInfo.getFrom_station_code():commonDao.queryStationCode(changeInfo.getFrom_city())!=null?commonDao.queryStationCode(changeInfo.getFrom_city()):"");
				json.put("from_station_name", changeInfo.getFrom_city());
				json.put("to_station_code", changeInfo.getTo_station_code()!=null?changeInfo.getTo_station_code():commonDao.queryStationCode(changeInfo.getTo_city())!=null?commonDao.queryStationCode(changeInfo.getTo_city()):"");
				json.put("to_station_name", changeInfo.getTo_city());
				json.put("checi", changeInfo.getChange_train_no());
				json.put("train_date", changeInfo.getChange_from_time().split(" ")[0]);
				json.put("start_time", changeInfo.getChange_from_time().split(" ")[1].substring(0,5));
				if(changeInfo.getChange_to_time()!=null && !changeInfo.getChange_to_time().equals("")){
					json.put("arrive_time", changeInfo.getChange_to_time().split(" ")[1].substring(0,5));
				}else{
					SInfo toSInfo = commonDao.getSInfo(changeInfo.getChange_train_no(), changeInfo.getTo_city());//查询车次信息
					if(toSInfo!=null){
						json.put("arrive_time", toSInfo.getArriveTime());
					}else{
						json.put("arrive_time", "");
					}
				}
				JSONArray newTickets = new JSONArray();
				json.put("transactionid", transactionid);
				Date book_ticket_time;
				if(changeInfo.getBook_ticket_time()!=null){
					book_ticket_time=DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
				}else{
					book_ticket_time=new Date();
				}
				double diffRate= TongChengConsts.getDiffRate(changeInfo.getFrom_time(), book_ticket_time);
				//double diffRate_before = TongChengConsts.getDiffRate(changeInfo.getFrom_time(), book_ticket_time);
				//double diffRate_change = TongChengConsts.getDiffRate(changeInfo.getChange_from_time(), book_ticket_time);
				//diffRate=diffRate_before>=diffRate_change?diffRate_before:diffRate_change;
				//logger.info("diffRate about order_id is:"+changeInfo.getOrder_id()+",diffRate_before:"+diffRate_before+",diffRate_change:"+diffRate_change+",diffRate:"+diffRate);
				double totalBuyMoney = 0.0;
				double totalChangeBuyMoney = 0.0;
				double totalDiff = 0.0;
				double totalPriceDiff = 0.0;
				double fee = 0.0;
				Integer priceInfoType = null;
				String priceInfo = "";
				String helpInfo = "";
				if(changeStatus.equals(TongChengChangeService.TRAIN_REQUEST_CHANGE_SUCCESS)) {
					json.put("success", true);
					json.put("code", 100);
					json.put("msg", "改签预订成功");
				} else if(changeStatus.equals(TongChengChangeService.TRAIN_REQUEST_CHANGE_FAIL)) {
					String code = changeInfo.getFail_reason();
					helpInfo = TongChengConsts.getTcChangeErrorInfo(code);
					if(StrUtil.isEmpty(helpInfo)) {
						code = "506";
						helpInfo = "未知系统异常";
					}
					if("9991".equals(code)){
						code="999";
						helpInfo="旅游票,请到车站办理";
					}
					
					json.put("code", Integer.valueOf(code));
					json.put("msg", helpInfo);
				}
				int cp_num=cPassengers.size();
				for(DBPassengerChangeInfo cPassenger : cPassengers) {
					JSONObject newTicket = new JSONObject();
					if(changeStatus.equals(TongChengChangeService.TRAIN_REQUEST_CHANGE_SUCCESS)) {
						/*成功*/
						newTicket.put("new_ticket_no", cPassenger.getNew_cp_id());
						newTicket.put("price", cPassenger.getChange_buy_money());
						newTicket.put("cxin", cPassenger.getChange_train_box() + "车厢," + cPassenger.getChange_seat_no());
						totalBuyMoney += Double.parseDouble(cPassenger.getBuy_money());//改签之前总成本价
						totalChangeBuyMoney += Double.parseDouble(cPassenger.getChange_buy_money());//改签之后总成本价
						logger.info("totalBuyMoney : " + totalBuyMoney);
						logger.info("totalChangeBuyMoney : " + totalChangeBuyMoney);
						totalDiff = totalChangeBuyMoney - totalBuyMoney;//总差价
						
						if(totalDiff < 0.0) {//新票款低于原票款
							priceInfoType = 3;
							priceInfo = "退还票款差额:" + totalDiff + "元";
						} else if(totalDiff == 0.0) {//新票款等于原票款
							priceInfoType = 2;
							priceInfo = "改签票款差价:0.0元";
						} else if(totalDiff > 0.0) {//新票款大于原票款
							priceInfoType = 1;
							priceInfo = "收取新票款:" + totalChangeBuyMoney + "元,退还原票票款:" + totalBuyMoney + "元";
						}
						json.put("pricedifference", totalDiff);
						
						//'flagid':'100','flagmsg':'改签占座成功'}]
						/*100：改签成功
						1005：已出票，请到车站办理
						1006：已改签，不能再次改签
						1007：已退票，无法改签
						1008：旅游票，请到车站办理*/
						newTicket.put("flagid", "100");
						newTicket.put("flagmsg", "改签成功");
					} else if(changeStatus.equals(TongChengChangeService.TRAIN_REQUEST_CHANGE_FAIL)) {
						/*失败*/
						newTicket.put("new_ticket_no", "");
						newTicket.put("price", "");
						newTicket.put("cxin", "");
						priceInfoType = 0;
					

						//3.2版本
						/*String code = changeInfo.getFail_reason();
						if("9991".equals(code)){
							newTicket.put("flagid", "1008");
							newTicket.put("flagmsg", "旅游票,请到车站办理");
						}else if("9992".equals(code)){
							newTicket.put("flagid", "1005");
							newTicket.put("flagmsg", "已出票,请到车站办理");
						}else if("9993".equals(code)){
							newTicket.put("flagid", "1006");
							newTicket.put("flagmsg", "已改签,不能再次改签");
						}else if("9994".equals(code)){
							newTicket.put("flagid", "1007");
							newTicket.put("flagmsg", "已退票,无法改签");
						}else{
							newTicket.put("flagid", "");
							newTicket.put("flagmsg", "");
						}*/
						String code = changeInfo.getFail_reason();
						helpInfo = TongChengConsts.getTcChangeErrorInfo(code);
						newTicket.put("flagid", Integer.valueOf(code));
						newTicket.put("flagmsg",helpInfo );
					}
					newTicket.put("piaotype", cPassenger.getTc_ticket_type());
					newTicket.put("passportseno", cPassenger.getUser_ids());
					newTicket.put("old_ticket_no", cPassenger.getCp_id());
					newTicket.put("zwcode", cPassenger.getTc_change_seat_type());
					newTicket.put("zwname", TongChengConsts.getZwname(cPassenger.getTc_change_seat_type()));
					newTickets.add(newTicket);
				}
				if(priceInfoType != null) {
					/*改签成功后生成流水号、计算手续费*/
					if(priceInfoType == 3) {//新票款低于原票款
						String ticketPriceDiffChangeSerial = CreateIDUtil.createID("TCC");
						changeInfo.setTicket_price_diff_change_serial(ticketPriceDiffChangeSerial);
						changeInfo.setChange_diff_money(totalDiff + "");
						
						if(diffRate==0){
							fee=0;
						}else{
							fee = AmountUtil.quarterConvert(Math.abs(AmountUtil.mul(totalDiff, diffRate)));//手续费=退款差额 * 费率
							int less_fee=2*cp_num;
							fee=fee<less_fee?less_fee:fee;
							
							if(fee > Math.abs(totalDiff)) {
								fee = Math.abs(totalDiff);
							}
						}
						//fee = AmountUtil.quarterConvert(AmountUtil.mul(totalDiff, diffRate));//手续费=退款差额 * 费率
						totalPriceDiff = AmountUtil.sub(Math.abs(totalDiff), fee);//实际退款=退款差额-手续费
						
						changeInfo.setFee(fee+"");
						changeInfo.setDiffrate(diffRate+"");
						changeInfo.setTotalpricediff(totalPriceDiff+"");
					} else if(priceInfoType == 1) {//新票款大于原票款
						String oldTicketChangeSerial = CreateIDUtil.createID("TCC");
						String newTicketChangeSerial = CreateIDUtil.createID("TCC");
						changeInfo.setOld_ticket_change_serial(oldTicketChangeSerial);
						changeInfo.setNew_ticket_change_serial(newTicketChangeSerial);
						changeInfo.setChange_refund_money(totalBuyMoney + "");
						changeInfo.setChange_receive_money(totalChangeBuyMoney + "");
					}
					json.put("diffrate", diffRate);
					json.put("totalpricediff", totalPriceDiff);
					json.put("fee", fee);
				}
				json.put("newtickets", newTickets);
				json.put("priceinfotype", priceInfoType);
				json.put("priceinfo", priceInfo);
				json.put("help_info", helpInfo);
				tongChengChangeDao.updateChangeInfo(changeInfo);
			} else if(changeStatus.startsWith("2")) {
				method = "train_cancel_change";
				
				callbackurl = "";//同程还没有提供取消异步回调地址,引导出异常
				/*取消改签*/
				if(changeStatus.equals(TongChengChangeService.TRAIN_CANCEL_CHANGE_SUCCESS)) {
					/*成功*/
					json.put("success", true);
					json.put("code", 100);
					json.put("msg", "取消改签成功");
				} else if(changeStatus.equals(TongChengChangeService.TRAIN_CANCEL_CHANGE_FAIL)) {
					/*失败*/
					String code = changeInfo.getFail_reason();
					String helpInfo = TongChengConsts.getTcChangeErrorInfo(code);
					if(StrUtil.isEmpty(helpInfo)) {
						code = "506";
						helpInfo = "未知系统异常,取消失败";
					}
					json.put("success", false);
					json.put("code",Integer.valueOf(code));
					json.put("msg", helpInfo);
				}
				//3.2版本//json.put("changereqtoken", reqtoken);
			} else if(changeStatus.startsWith("3")) {
				method = "train_confirm_change";
				JSONArray tickets = new JSONArray();
				/*确认改签*/
				if(changeStatus.equals(TongChengChangeService.TRAIN_CONFIRM_CHANGE_SUCCESS)) {
					/*成功*/
					json.put("success", true);
					json.put("code", 100);
					json.put("msg", "确认改签成功");
					for(DBPassengerChangeInfo cPassenger : cPassengers) {
						JSONObject ticket = new JSONObject();
						ticket.put("new_ticket_no", cPassenger.getNew_cp_id());
						ticket.put("old_ticket_no", cPassenger.getCp_id());
						ticket.put("cxin", cPassenger.getChange_train_box() + "车厢," + cPassenger.getChange_seat_no());
						tickets.add(ticket);
						cPassenger.setIs_changed("Y");
						tongChengChangeDao.updateChangeCp(cPassenger);
					}
					json.put("oldticketchangeserial", changeInfo.getOld_ticket_change_serial() == null ? "" : changeInfo.getOld_ticket_change_serial());
					json.put("newticketchangeserial", changeInfo.getNew_ticket_change_serial() == null ? "" : changeInfo.getNew_ticket_change_serial());
					json.put("ticketpricediffchangeserial", changeInfo.getTicket_price_diff_change_serial() == null ? "" : changeInfo.getTicket_price_diff_change_serial());
				} else if(changeStatus.equals(TongChengChangeService.TRAIN_CONFIRM_CHANGE_FAIL)) {
					/*失败*/
					String code = changeInfo.getFail_reason();
					String helpInfo = TongChengConsts.getTcChangeErrorInfo(code);
					if(StrUtil.isEmpty(helpInfo)) {
						code = "506";
						helpInfo = "未知系统异常";
					}
					if("999".equals(code)){
						helpInfo=changeInfo.getFail_msg();
					}
					if("9991".equals(code)){
						code="999";
						helpInfo="旅游票,请到车站办理";
					}
					json.put("success", false);
					json.put("code",Integer.valueOf(code));
					json.put("msg", helpInfo);
				}
				json.put("newticketcxins", tickets);
			}
			json.put("method", method);
			
			/*回调*/
			TongchengChangeLogVO log = new TongchengChangeLogVO();
			log.setOrder_id(orderid);
			log.setChange_id(change_id);
			log.setOpt_person("tongcheng_app");
			
			parameter.put("backjson", json.toString());
			logger.info("同程改签回调backjson:"+json.toString());
			String sendParams = ElongUrlFormatUtil.createGetUrl("", parameter, "utf-8");
			long start=System.currentTimeMillis();
			String result = HttpUtil.sendByPost(callbackurl, sendParams, "utf-8");
			if(result!=null){
				logger.info("同程改签回调返回结果,耗时"+(System.currentTimeMillis()-start)+"ms,result:"+result);
				result = result.trim().toLowerCase();
				if("success".equals(result)){
					logger.info("同程改签回调请求成功,耗时"+(System.currentTimeMillis()-start)+"ms,order_id:"+orderid+",result:"+result);
					updateNotify(changeInfo, true);
					log.setContent("同程改签占座/支付回调成功，result:" + result);
				}else{
					logger.info("同程改签回调请求失败,order_id:"+orderid+",result:"+result);
					updateNotify(changeInfo, false);
					log.setContent("同程改签占座/支付回调失败，result:" + result);
				}
			}else{
				logger.info("同程改签回调请求失败,order_id:"+orderid+",result返回null");
				updateNotify(changeInfo, false);
				log.setContent("同程改签占座/支付回调失败，result:" + result);
			}
			
			tongChengChangeDao.insertChangeLog(log);
		} catch (Exception e) {
			logger.info("同程改签回调请求失败，异常" + e);
			e.printStackTrace();
		} 
		
	}
	
	private void updateNotify(DBChangeInfo changeInfo, boolean success) {
		if(success) {
			/*回调成功*/
			changeInfo.setChange_notify_finish_time("finish");
			changeInfo.setChange_notify_status(TongChengChangeService.CHANGE_NOTIFY_OVER);
		} else {
			/*回调失败*/
			int count = changeInfo.getChange_notify_count() + 1;
			if(count == 5) {
				changeInfo.setChange_notify_status(TongChengChangeService.CHANGE_NOTIFY_FAIL);
				changeInfo.setChange_notify_finish_time("finish");
			}
			changeInfo.setChange_notify_count(count);
		}
		tongChengChangeDao.updateNotifyEnd(changeInfo);
	}
	
	@Override
	public int updateNotifyBegin(DBChangeInfo changeInfo) {
		return tongChengChangeDao.updateNotifyBegin(changeInfo);
	}
	/* 改签 */
	

	@Override
	public void addChangeInfo(DBChangeInfo changeInfo, boolean cascade) {
		tongChengChangeDao.insertChangeInfo(changeInfo);
		int change_id = changeInfo.getChange_id();
		if (cascade) {
			List<DBPassengerChangeInfo> cPassengers = changeInfo
					.getcPassengers();
			if (cPassengers != null && cPassengers.size() != 0) {
				for (DBPassengerChangeInfo cPassenger : cPassengers) {
					cPassenger.setChange_id(change_id);
					tongChengChangeDao.insertChangeCp(cPassenger);
				}
			}
		}
	}

	// @Override
	// public void addChangeCp(List<DBPassengerChangeInfo> cPassengers) {
	//		
	// for(DBPassengerChangeInfo cPassenger : cPassengers) {
	// elongOrderDao.insertChangeCp(cPassenger);
	// }
	// }
	@Override
	public List<DBPassengerChangeInfo> findRequestChangeCp(
			Map<String, Object> param) {
		return tongChengChangeDao.selectChangeCp(param);
	}

	@Override
	public void updateChangeCp(List<DBPassengerChangeInfo> cPassengers) {
		if (cPassengers == null || cPassengers.size() == 0)
			return;
		for (DBPassengerChangeInfo cPassenger : cPassengers) {
			tongChengChangeDao.updateChangeCp(cPassenger);
		}
	}

	// @Override
	// public List<DBPassengerChangeInfo> getChangeResult(
	// Map<String, Object> param, Long timeout) {
	// List<DBPassengerChangeInfo> cPassengers = null;
	// long interval = 1000L;
	// long start = 0L;
	// while(start < timeout) {
	// cPassengers = tongChengChangeDao.selectChangeCp(param);
	// if(cPassengers != null && cPassengers.size() != 0) break;
	// try {
	// Thread.sleep(interval);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// start += interval;
	// }
	// return cPassengers;
	// }

	@Override
	public DBPassengerChangeInfo getChangeCp(String cpId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cp_id", cpId);
		return tongChengChangeDao.selectChangePassenger(param);
	}

	@Override
	public DBChangeInfo getChangeInfo(Map<String, Object> param, boolean cascade) {
		DBChangeInfo changeInfo = tongChengChangeDao.getChangeInfo(param);
		if (cascade) {
			if(changeInfo != null) {
				param.clear();
				param.put("change_id", changeInfo.getChange_id());
				List<DBPassengerChangeInfo> cPassengers = tongChengChangeDao
				.selectChangeCp(param);
				changeInfo.setcPassengers(cPassengers);
			}
		}
		return changeInfo;
	}

	@Override
	public void updateChangeInfo(DBChangeInfo changeInfo, boolean cascade) {
		tongChengChangeDao.updateChangeInfo(changeInfo);
		if (cascade) {
			List<DBPassengerChangeInfo> cPassengers = changeInfo
					.getcPassengers();
			if (cPassengers != null && cPassengers.size() != 0) {
				for (DBPassengerChangeInfo cPassenger : cPassengers) {
					tongChengChangeDao.updateChangeCp(cPassenger);
				}
			}
		}
	}

	@Override
	public String getAccountId(String orderId) {
		return tongChengChangeDao.selectAccountId(orderId);
	}
	
	@Override
	public List<String> getChangeReqtokens(String orderId) {
		return tongChengChangeDao.selectReqtokens(orderId);
	}
	
	@Override
	public void changeManual4Timeout() {
		
		TongchengChangeLogVO log = new TongchengChangeLogVO();
		log.setOpt_person("tongcheng_app");
		
		/*正在预订超过5分钟，转人工*/
		List<DBChangeInfo> requestChanges = tongChengChangeDao.selectTimeoutRequestChange();
		log.setContent("改签占座超时，转为人工预订！");
		if(requestChanges != null && requestChanges.size() > 0) {
			logger.info("改签预订超时size : " + requestChanges.size());
			for(DBChangeInfo changeInfo : requestChanges) {
				changeInfo.setChange_status(TongChengChangeService.TRAIN_REQUEST_CHANGE_ARTIFICIAL);
				tongChengChangeDao.updateChangeInfo(changeInfo);
				log.setChange_id(changeInfo.getChange_id());
				log.setOrder_id(changeInfo.getOrder_id());
				tongChengChangeDao.insertChangeLog(log);
			}
		}
		
		/*正在支付超过5分钟，转人工*/
		List<DBChangeInfo> confirmChanges = tongChengChangeDao.selectTimeoutConfirmChange();
		log.setContent("改签支付超时，转为人工支付！");
		if(confirmChanges != null && confirmChanges.size() > 0) {
			logger.info("改签支付超时size : " + confirmChanges.size());
			for(DBChangeInfo changeInfo : confirmChanges) {
				changeInfo.setChange_status(TongChengChangeService.TRAIN_CONFIRM_CHANGE_PAY_ARTIFICIAL);
				tongChengChangeDao.updateChangeInfo(changeInfo);
				log.setChange_id(changeInfo.getChange_id());
				log.setOrder_id(changeInfo.getOrder_id());
				tongChengChangeDao.insertChangeLog(log);
			}
		}
	}
	
	
	/*日志*/


	@Override
	public void addTongChengChangeLog(TongchengChangeLogVO log) {
		tongChengChangeDao.insertChangeLog(log);
	}
}
