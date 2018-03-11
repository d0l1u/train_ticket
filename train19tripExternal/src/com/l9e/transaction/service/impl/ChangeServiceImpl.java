package com.l9e.transaction.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.ChangeConsts;
import com.l9e.transaction.dao.ChangeDao;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLogVO;
import com.l9e.transaction.vo.ChangePassengerInfo;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.HttpsUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.StrUtil;

@Service("changeService")
public class ChangeServiceImpl implements ChangeService{
	private static final Logger logger = Logger.getLogger(ChangeServiceImpl.class);
	
	@Resource
	private ChangeDao changeDao;
	@Resource
	private OrderDao orderDao;
	@Resource
	private CommonDao commonDao;
	@Override
	public ChangeInfo getChangeInfoByReqtoken(String reqtoken) {
		return changeDao.selectChangeInfoByReqtoken(reqtoken);
	}
	@Override
	public ChangePassengerInfo getChangeCpById(String cpId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("cp_id", cpId);
		List<ChangePassengerInfo> changePassengers = changeDao.selectChangeCp(param);
		if(changePassengers!=null &&changePassengers.size()>0){
			return changePassengers.get(0);
		}else{
			return null;
		}
	}
	@Override
	public int addChangeInfo(ChangeInfo changeInfo) {
		int result = changeDao.insertChangeInfo(changeInfo);
		int change_id = changeInfo.getChange_id();
		List<ChangePassengerInfo> cPassengers = changeInfo.getcPassengers();
		if (cPassengers != null && cPassengers.size() != 0) {
			for (ChangePassengerInfo cPassenger : cPassengers) {
				cPassenger.setChange_id(change_id);
				changeDao.insertChangeCp(cPassenger);
			}
		}
		return result;
	}
	@Override
	public void addChangeLog(ChangeLogVO log) {
		changeDao.insertChangeLog(log);
	}
	@Override
	public List<ChangeInfo> getNoticeChangeInfo() {
		return changeDao.selectNoticeChangeInfo();
	}
	@Override
	public void changeNotice(List<ChangeInfo> notifyList) {
		logger.info("改签回调");
		for(ChangeInfo changeInfo:notifyList){
			try {
				//先将通知信息更新状态
				ChangeInfo updateInfo = new ChangeInfo();
				updateInfo.setChange_id(changeInfo.getChange_id());
				updateInfo.setChange_notify_status("111");
				updateInfo.setChange_notify_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
				logger.info(changeInfo.getOrder_id()+"改签回调，通知第"+changeInfo.getChange_notify_count()+"次");
				int count_row=changeDao.updateChangeInfo(updateInfo);//更新通知状态
				logger.info("改签订单号："+changeInfo.getOrder_id()+",回调地址："+changeInfo.getCallbackurl()+",count:"+count_row);	
				//回调
				JSONObject parameter = new JSONObject();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("change_id", changeInfo.getChange_id());
				/*获取改签车票信息*/
				List<ChangePassengerInfo> cPassengers = changeDao.selectChangeCp(param);
				//返回json
				String callbackurl = changeInfo.getCallbackurl();
				String reqtoken = changeInfo.getReqtoken();
				String order_id = changeInfo.getOrder_id();
				/*通用参数*/
				String method = "";
				parameter.put("reqtoken", reqtoken);
				String changeStatus = changeInfo.getChange_status();
				//改签占座
				if(changeStatus.startsWith("1")) {
					parameter.put("order_id", order_id);
					method = "requestChange";
					/*请求改签*/
					//parameter.put("from_station_code", changeInfo.getFrom_station_code());
					//parameter.put("from_station_name", changeInfo.getFrom_city());
					//parameter.put("to_station_code", changeInfo.getTo_station_code());
					//parameter.put("to_station_name", changeInfo.getTo_city());
					//parameter.put("change_train_no", changeInfo.getChange_train_no());
					//parameter.put("change_from_time", changeInfo.getChange_from_time());
					//车票信息
					JSONArray newTickets = new JSONArray();
					Date book_ticket_time;
					if(changeInfo.getBook_ticket_time()!=null){
						book_ticket_time=DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
					}else{
						book_ticket_time=new Date();
					}
					//费率
					Double diffRate= ChangeConsts.getDiffRate(changeInfo.getFrom_time(), book_ticket_time);
					Double totalBuyMoney = 0.0;
					Double totalChangeBuyMoney = 0.0;
					Double totalDiff = 0.0;
					Double totalPriceDiff = 0.0;
					Double fee = 0.0;
					Integer priceInfoType = null;
					String priceInfo = "";
					String helpInfo = "";
					if(changeStatus.equals("14")) {
						//改签成功
						logger.info(order_id+"改签成功");
						parameter.put("success", true);
						parameter.put("return_code", "000");
						parameter.put("message", "改签预订成功");
					} else if(changeStatus.equals("15")){
						logger.info(order_id+"改签失败");
						String code = changeInfo.getFail_reason();
						helpInfo = ChangeConsts.getChangeErrorInfo(code);
						if(StrUtil.isEmpty(helpInfo)) {
							logger.info(order_id+"改签回调查询失败原因没有匹配上");
							code = "301";
							helpInfo = "没有余票";
						}
						if("9991".equals(code)){
							code="999";
							helpInfo="旅游票,请到车站办理";
						}
						parameter.put("success", false);
						parameter.put("return_code", code);
						parameter.put("message", helpInfo);
					}
					int cp_num=cPassengers.size();
					for(ChangePassengerInfo cPassenger : cPassengers) {
						JSONObject newTicket = new JSONObject();
						newTicket.put("user_ids", cPassenger.getUser_ids());
						if(changeStatus.equals("14")) {
							/*成功*/
							newTicket.put("price", Double.valueOf(cPassenger.getChange_buy_money()));
							newTicket.put("cxin", cPassenger.getChange_train_box() + "车厢," + cPassenger.getChange_seat_no());
							totalBuyMoney += Double.parseDouble(cPassenger.getBuy_money());//改签之前总成本价
							totalChangeBuyMoney += Double.parseDouble(cPassenger.getChange_buy_money());//改签之后总成本价
							
						} else if(changeStatus.equals("15")) {
							/*失败*/
							newTicket.put("price", 0);
							newTicket.put("cxin", "");
							priceInfoType = 0;		
						}
						newTickets.add(newTicket);
					}
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
					parameter.put("pricedifference", totalDiff);
					if(priceInfoType != null) {
						/*改签成功后生成流水号、计算手续费*/
						if(priceInfoType == 3) {//新票款低于原票款
							String ticketPriceDiffChangeSerial = CreateIDUtil.createID("EXC");
							updateInfo.setTicket_price_diff_change_serial(ticketPriceDiffChangeSerial);
							updateInfo.setChange_diff_money(totalDiff + "");
							
							if(diffRate==0){
								fee=0.0;
							}else{
								fee = AmountUtil.quarterConvert(Math.abs(AmountUtil.mul(totalDiff, diffRate)));//手续费=退款差额 * 费率
								int less_fee=2*cp_num;
								fee=fee<less_fee?less_fee:fee;
								
								if(fee > Math.abs(totalDiff)) {
									fee = Math.abs(totalDiff);
								}
							}
							totalPriceDiff = AmountUtil.sub(Math.abs(totalDiff), fee);//实际退款=退款差额-手续费
							
							updateInfo.setFee(fee+"");
							updateInfo.setDiffrate(diffRate+"");
							updateInfo.setTotalpricediff(totalPriceDiff+"");
						} else if(priceInfoType == 1) {//新票款大于原票款
							String oldTicketChangeSerial = CreateIDUtil.createID("EXC");
							String newTicketChangeSerial = CreateIDUtil.createID("EXC");
							updateInfo.setOld_ticket_change_serial(oldTicketChangeSerial);
							updateInfo.setNew_ticket_change_serial(newTicketChangeSerial);
							updateInfo.setChange_refund_money(totalBuyMoney + "");
							updateInfo.setChange_receive_money(totalChangeBuyMoney + "");
						}
						parameter.put("diffrate", diffRate);
						parameter.put("totalpricediff", totalPriceDiff);
						parameter.put("fee", fee);
					}
					parameter.put("newtickets", newTickets);
					parameter.put("priceinfotype", priceInfoType);
					parameter.put("priceinfo", priceInfo);
					
					changeDao.updateChangeInfo(updateInfo); //更新必要的票号和金额
					
				} else if(changeStatus.startsWith("2")) {
					//改签取消
					method = "cancelChange";
					/*取消改签*/
					if(changeStatus.equals("23")) {
						/*成功*/
						parameter.put("success", true);
						parameter.put("return_code", "000");
						parameter.put("message", "取消改签成功");
					} else if(changeStatus.equals("24")) {
						/*失败*/
						String code = changeInfo.getFail_reason();
						String helpInfo = ChangeConsts.getChangeErrorInfo(code);
						if(StrUtil.isEmpty(helpInfo)) {
							code = "506";
							helpInfo = "未知系统异常,取消失败";
						}
						parameter.put("success", false);
						parameter.put("return_code",code);
						parameter.put("message", helpInfo);
					}
				} else if(changeStatus.startsWith("3")) {
					//改签确认
					method = "confirmChange";
					/*确认改签*/
					if(changeStatus.equals("34")) {
						/*成功*/
						parameter.put("success", true);
						parameter.put("return_code", "000");
						parameter.put("message", "确认改签成功");
						for(ChangePassengerInfo cPassenger : cPassengers) {
							cPassenger.setIs_changed("Y");
						    changeDao.updateChangeCp(cPassenger);
						}
					} else if(changeStatus.equals("35")) {
						/*失败*/
						String code = changeInfo.getFail_reason();
						String helpInfo = ChangeConsts.getChangeErrorInfo(code);
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
						parameter.put("success", false);
						parameter.put("return_code",code);
						parameter.put("message", helpInfo);
					}
				}
				parameter.put("method", method);
				
				/*回调*/
				ChangeLogVO log = new ChangeLogVO();
				log.setOrder_id(order_id);
				log.setChange_id(changeInfo.getChange_id());
				log.setOpt_person(changeInfo.getMerchant_id());
				
				//拼接请求参数
				String timestamp = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2);
				Map<String,String> merchantInfoMap = orderDao.queryMerchantInfoByOrderId(order_id);
				Map<String,String> merchantSetting = commonDao.queryMerchantInfo(merchantInfoMap.get("merchant_id"));
				String md5_str = merchantInfoMap.get("merchant_id")+timestamp+merchantInfoMap.get("merchant_version")+parameter.toString();
				logger.info("md5_str: "+md5_str);
				String hmac = Md5Encrypt.getKeyedDigestFor19Pay(md5_str+merchantSetting.get("sign_key"), "", "utf-8");
				StringBuffer params = new StringBuffer();
				params.append("merchant_id=").append(merchantInfoMap.get("merchant_id"))
					.append("&timestamp=").append(timestamp)
					.append("&version=").append(merchantInfoMap.get("merchant_version"));
				try {
					params.append("&json_param=").append(URLEncoder.encode(parameter.toString(),"UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				params.append("&hmac=").append(hmac);
				logger.info("改签异步通知业务参数："+parameter.toString());
				logger.info("改签异步通知加密参数："+params.toString());
				String result = "";
				long start=System.currentTimeMillis();
				try {
					if(callbackurl.contains("https")){
						result = HttpsUtil.sendHttps(callbackurl+"?"+params.toString());
					}else{
						result = HttpUtil.sendByPost(callbackurl, params.toString(), "utf-8");
					}
				} catch (Exception e) {
					logger.info("改签异步通知发生异常",e);
				}
				ChangeInfo noticeChangeInfo = new ChangeInfo();
				noticeChangeInfo.setChange_id(changeInfo.getChange_id());
				noticeChangeInfo.setChange_notify_count(changeInfo.getChange_notify_count()+1);
				if(result!=null){
					logger.info("改签回调返回结果,耗时"+(System.currentTimeMillis()-start)+"ms,result:"+result);
					result = result.trim().toLowerCase();
					if("success".equals(result)) {
						logger.info("改签回调请求成功,耗时"+(System.currentTimeMillis()-start)+"ms,order_id:"+order_id+",result:"+result);
						/*回调成功*/
						noticeChangeInfo.setChange_notify_finish_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
						noticeChangeInfo.setChange_notify_status("222");
						log.setContent("改签回调成功，result:" + result);
					} else {
						/*回调失败*/
						logger.info("改签回调请求失败,order_id:"+order_id+",result:"+result);
						int count = changeInfo.getChange_notify_count()+1;
						if(count == 5) {
							noticeChangeInfo.setChange_notify_status("333");
							noticeChangeInfo.setChange_notify_finish_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
						}
						log.setContent("改签回调失败，result:" + result);
					}		
				}else{
					logger.info("改签回调请求失败,order_id:"+order_id+",result返回null");
					int count = changeInfo.getChange_notify_count()+1;
					if(count == 5) {
						noticeChangeInfo.setChange_notify_status("333");
						noticeChangeInfo.setChange_notify_finish_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
					}
					log.setContent("改签回调失败，result:" + result);
				}
				changeDao.updateChangeInfo(noticeChangeInfo);
				changeDao.insertChangeLog(log);
			} catch (NumberFormatException e) {
				logger.info("改签回调请求失败，异常" + e.getMessage(),e);
			
			} catch (Exception e) {
				logger.info("改签回调请求失败，异常" + e.getMessage(),e);
			
			}
		}
	}
	@Override
	public int updateChangeInfo(ChangeInfo changeInfo) {
		return changeDao.updateChangeInfo(changeInfo);
	}
	@Override
	public List<String> getNewCpListByParam(Map<String, Object> param){
		return changeDao.getNewCpListByParam(param);
	}
	@Override
	public Map<String, Object> queryChangeCpInfo(Map<String, Object> param) {
		return changeDao.queryChangeCpInfo(param);
	}
	@Override
	public int selectNoCompleteChangeOrderLastOneCount(String order_id) {
		return changeDao.selectNoCompleteChangeOrderLastOneCount(order_id);
	}
}
