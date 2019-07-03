package com.l9e.transaction.service.impl;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.ChangeConsts;
import com.l9e.transaction.dao.ChangeDao;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLogVO;
import com.l9e.transaction.vo.ChangePassengerInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("changeService")
public class ChangeServiceImpl implements ChangeService {
    private static final Logger logger = Logger.getLogger(ChangeServiceImpl.class);

    @Resource
    private OrderDao orderDao;
    @Resource
    private ChangeDao changeDao;
    @Resource
    private CommonDao commonDao;
    
    @Value("${config.queryTicket}")
    private String queryTicketUrl;

    @Override
    public ChangeInfo getChangeInfoByReqtoken(String reqtoken) {
        return changeDao.selectChangeInfoByReqtoken(reqtoken);
    }

    @Override
    public List<ChangePassengerInfo> getChangeCpById(String cpId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("cp_id", cpId);
        return changeDao.selectChangeCp(param);
    }

    @Override
    public int addChangeInfo(ChangeInfo changeInfo) {
        int result = changeDao.insertChangeInfo(changeInfo);
        logger.info(changeInfo.getMerchant_id() + ",增加改签elong_orderinfo_change返回：" + result);
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
    public List<ChangeInfo> getNoticeChangeInfo(String merchantId) {
        return changeDao.selectNoticeChangeInfo(merchantId);
    }

    @Override
    public List<ChangePassengerInfo> getChangePassenger(Map<String, Object> param) {
        return changeDao.selectChangeCp(param);
    }

    @Override
    public void changeNotice(List<ChangeInfo> notifyList) {
        logger.info("高铁管家改签回调");
        for (ChangeInfo changeInfo : notifyList) {
            try {
                // 先将通知信息更新状态
                ChangeInfo updateInfo = new ChangeInfo();
                updateInfo.setChange_id(changeInfo.getChange_id());
                updateInfo.setChange_notify_status("111");
                updateInfo.setChange_notify_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
                int count_row = changeDao.updateChangeInfo(updateInfo);// 更新通知状态
                logger.info("改签订单号：" + changeInfo.getOrder_id() + ",回调地址：" + changeInfo.getCallbackurl() + ",count:" + count_row + ",通知时间："
                        + changeInfo.getChange_notify_time());
                // 回调
                JSONObject parameter = new JSONObject();
                Map<String, Object> changeCpParam = new HashMap<String, Object>();
                changeCpParam.put("change_id", changeInfo.getChange_id());
                /* 获取改签车票信息 */
                List<ChangePassengerInfo> cPassengers = changeDao.selectChangeCp(changeCpParam);
                // 返回json
                String callbackurl = changeInfo.getCallbackurl();
                String reqtoken = changeInfo.getReqtoken();
                String order_id = changeInfo.getOrder_id();
                /* 通用参数 */
                String method = "";
                parameter.put("reqtoken", reqtoken);
                parameter.put("order_id", order_id);
                OrderInfo orderInfo = orderDao.queryOrderInfo(order_id);
                parameter.put("merchant_order_id", orderInfo.getMerchant_order_id());
                String changeStatus = changeInfo.getChange_status();
                // 改签占座
                if (changeStatus.startsWith("1")) {
                    method = "requestChange";
                    /* 请求改签 */
                    // parameter.put("from_station_code",
                    // changeInfo.getFrom_station_code());
                    parameter.put("from_station_name", changeInfo.getFrom_city());
                    // parameter.put("to_station_code",
                    // changeInfo.getTo_station_code());
                    parameter.put("to_station_name", changeInfo.getTo_city());
                    parameter.put("change_train_no", changeInfo.getChange_train_no());
                    parameter.put("change_from_time", changeInfo.getChange_from_time());
                    parameter.put("pay_limit_time", changeInfo.getPay_limit_time());

                    // 车票信息
                    JSONArray newTickets = new JSONArray();
                    Date book_ticket_time;
                    if (changeInfo.getBook_ticket_time() != null) {
                        book_ticket_time = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
                    } else {
                        book_ticket_time = new Date();
                    }
                    // 费率
                    double diffRate = ChangeConsts.getDiffRate(changeInfo.getFrom_time(), book_ticket_time);
                    double totalBuyMoney = 0.0;
                    double totalChangeBuyMoney = 0.0;
                    double totalDiff = 0.0;
                    double totalPriceDiff = 0.0;
                    double fee = 0.0;
                    Integer priceInfoType = null;
                    String priceInfo = "";
                    String helpInfo = "";
                    if (changeStatus.equals("14")) {
                        // 改签成功
                        logger.info(order_id + "改签成功");
                        parameter.put("success", true);
                        parameter.put("return_code", "000");
                        parameter.put("message", "改签预订成功");
                        /* 查询到达时间 */
                        parameter.put("arrive_time", changeInfo.getChange_to_time() == null ? this.queryTime(changeInfo) : changeInfo.getChange_to_time());
                    } else if (changeStatus.equals("15")) {
                        logger.info(order_id + "改签失败");
                        String code = changeInfo.getFail_reason();
                        String return_code = ChangeConsts.getChangeErrorCode(code);
                        helpInfo = ChangeConsts.getChangeErrorInfo(code);
                        logger.info(order_id + "return_code:" + return_code);
                        if (return_code == null) {
                            return_code = "301";
                            helpInfo = "没有余票";
                        }
                        parameter.put("success", false);
                        parameter.put("return_code", return_code);
                        parameter.put("message", helpInfo);
                        parameter.put("arrive_time", "");
                    }
                    int cp_num = cPassengers.size();
                    for (ChangePassengerInfo cPassenger : cPassengers) {
                        JSONObject newTicket = new JSONObject();
                        newTicket.put("user_ids", cPassenger.getUser_ids());
                        newTicket.put("old_ticket_no", cPassenger.getCp_id());
                        newTicket.put("change_seat_type", cPassenger.getChange_seat_type());
                        newTicket.put("new_ticket_no", cPassenger.getNew_cp_id());
                        if (changeStatus.equals("14")) {
                            /* 成功 */
                            newTicket.put("price", cPassenger.getChange_buy_money());
                            newTicket.put("cxin", cPassenger.getChange_train_box() + "车厢," + cPassenger.getChange_seat_no());
                            totalBuyMoney += Double.parseDouble(cPassenger.getBuy_money());// 改签之前总成本价
                            totalChangeBuyMoney += Double.parseDouble(cPassenger.getChange_buy_money());// 改签之后总成本价

                        } else if (changeStatus.equals("15")) {
                            /* 失败 */
                            // newTicket.put("new_ticket_no", "");//高铁要求，失败加上新票号
                            newTicket.put("price", ""); // 0
                            newTicket.put("cxin", ""); // 空
                            priceInfoType = 0;
                        }
                        newTickets.add(newTicket);
                    }
                    logger.info("totalBuyMoney : " + totalBuyMoney);
                    logger.info("totalChangeBuyMoney : " + totalChangeBuyMoney);
                    totalDiff = totalChangeBuyMoney - totalBuyMoney;// 总差价
                    if (totalDiff < 0.0) {// 新票款低于原票款
                        priceInfoType = 3;
                        priceInfo = "退还票款差额:" + totalDiff + "元";
                    } else if (totalDiff == 0.0) {// 新票款等于原票款
                        priceInfoType = 2;
                        priceInfo = "改签票款差价:0.0元";
                    } else if (totalDiff > 0.0) {// 新票款大于原票款
                        priceInfoType = 1;
                        priceInfo = "收取新票款:" + totalChangeBuyMoney + "元,退还原票票款:" + totalBuyMoney + "元";
                    }
                    parameter.put("pricedifference", totalDiff);
                    if (priceInfoType != null) {
                        /* 改签成功后生成流水号、计算手续费 */
                        if (priceInfoType == 3) {// 新票款低于原票款
                            updateInfo.setChange_diff_money(totalDiff + "");
                            if (diffRate == 0) {
                                fee = 0;
                            } else {
                                fee = AmountUtil.quarterConvert(Math.abs(AmountUtil.mul(totalDiff, diffRate)));// 手续费=退款差额
                                // *
                                // 费率
                                int less_fee = 2 * cp_num;
                                fee = fee < less_fee ? less_fee : fee;

                                if (fee > Math.abs(totalDiff)) {
                                    fee = Math.abs(totalDiff);
                                }
                            }
                            totalPriceDiff = AmountUtil.sub(Math.abs(totalDiff), fee);// 实际退款=退款差额-手续费

                            updateInfo.setFee(fee + "");
                            updateInfo.setDiffrate(diffRate + "");
                            updateInfo.setTotalpricediff(totalPriceDiff + "");
                        } else if (priceInfoType == 1) {// 新票款大于原票款
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
                    parameter.put("method", method);

                    changeDao.updateChangeInfo(updateInfo); // 更新必要的金额和通知状态

                } else if (changeStatus.startsWith("2")) {

                    // ****************************************//
                    // 改签取消
                    method = "cancelChange";
                    // 取消改签
                    if (changeStatus.equals("23")) {
                        // 成功
                        parameter.put("success", true);
                        parameter.put("code", 100);
                        parameter.put("msg", "取消改签成功");
                    } else if (changeStatus.equals("24")) {
                        // 失败
                        String code = changeInfo.getFail_reason();
                        String return_code = ChangeConsts.getChangeErrorCode(code);
                        String helpInfo = ChangeConsts.getChangeErrorInfo(code);
                        parameter.put("success", false);
                        parameter.put("return_code", return_code);
                        parameter.put("message", helpInfo);
                    }

                    // 车票信息
                    JSONArray newTickets = new JSONArray();
                    for (ChangePassengerInfo cPassenger : cPassengers) {
                        JSONObject newTicket = new JSONObject();
                        newTicket.put("user_ids", cPassenger.getUser_ids());
                        newTicket.put("old_ticket_no", cPassenger.getCp_id());
                        newTicket.put("change_seat_type", cPassenger.getChange_seat_type());
                        newTicket.put("new_ticket_no", cPassenger.getNew_cp_id());
                        newTickets.add(newTicket);
                    }
                    parameter.put("newtickets", newTickets);
                    parameter.put("method", method);

                    // ****************************************//

                    ChangeLogVO log = new ChangeLogVO();
                    log.setOrder_id(order_id);
                    log.setChange_id(changeInfo.getChange_id());
                    log.setOpt_person(changeInfo.getMerchant_id());
                    log.setContent("高铁渠道,取消改签不回调,直接修改通知完成！");
                    changeDao.insertChangeLog(log);
                    updateInfo.setChange_notify_status("222");
                    changeDao.updateChangeInfo(updateInfo); // 更新通知状态为完成,结束本次循环
                    continue;

                } else if (changeStatus.startsWith("3")) {
                    // 改签确认
                    method = "confirmChange";
                    /* 确认改签 */
                    if (changeStatus.equals("34")) {
                        /* 成功 */
                        parameter.put("success", true);
                        parameter.put("return_code", "000");
                        parameter.put("message", "确认改签成功");
                        for (ChangePassengerInfo cPassenger : cPassengers) {
                            cPassenger.setIs_changed("Y");
                            changeDao.updateChangeCp(cPassenger);
                        }
                    } else if (changeStatus.equals("35")) {
                        /* 失败 */
                        String code = changeInfo.getFail_reason();
                        String return_code = ChangeConsts.getChangeErrorCode(code);
                        String helpInfo = ChangeConsts.getChangeErrorInfo(code);

                        parameter.put("success", false);
                        parameter.put("return_code", return_code);
                        parameter.put("message", helpInfo);
                    }

                    //查询改签检票口信息
                    String ticket_entrance = "";
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("order_id", changeInfo.getOrder_id());
                    paramMap.put("change_id", changeInfo.getChange_id());
                    Map<String, Object> ticket_entranceMap = changeDao.selectChangeTicketEntrance(paramMap);//查询改签订单的检票口数据
                    if (null != ticket_entranceMap && !ticket_entranceMap.isEmpty()) {
                        if (null != ticket_entranceMap.get("entrance")) {
                            ticket_entrance = (String) ticket_entranceMap.get("entrance");
                        } else {
                            ticket_entrance = "";
                        }
                    } else {
                        ticket_entrance = "";
                    }
                    logger.info(order_id + "," + reqtoken + ",改签检票口信息：：" + paramMap + ",ticket_entranceMap::" + ticket_entranceMap + ",ticket_entrance::" + ticket_entrance);

                    // 车票信息
                    JSONArray newTickets = new JSONArray();
                    for (ChangePassengerInfo cPassenger : cPassengers) {
                        JSONObject newTicket = new JSONObject();
                        newTicket.put("user_ids", cPassenger.getUser_ids());
                        newTicket.put("old_ticket_no", cPassenger.getCp_id());
                        newTicket.put("change_seat_type", cPassenger.getChange_seat_type());
                        newTicket.put("new_ticket_no", cPassenger.getNew_cp_id());
                        newTickets.add(newTicket);
                    }
                    parameter.put("newtickets", newTickets);
                    parameter.put("ticket_entrance", ticket_entrance);
                    parameter.put("method", method);
                }
                /* 回调 */
                ChangeLogVO log = new ChangeLogVO();
                log.setOrder_id(order_id);
                log.setChange_id(changeInfo.getChange_id());
                log.setOpt_person(changeInfo.getMerchant_id());

                // 拼接请求参数
                String timestamp = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2);
                Map<String, String> merchantInfoMap = orderDao.queryMerchantInfoByOrderId(order_id);
                Map<String, String> merchantSetting = commonDao.queryMerchantInfo(merchantInfoMap.get("merchant_id"));
                String md5_str = merchantInfoMap.get("merchant_id") + timestamp + merchantInfoMap.get("merchant_version") + parameter.toString();
                logger.info("md5_str: " + md5_str);
                String hmac = Md5Encrypt.getKeyedDigestFor19Pay(md5_str + merchantSetting.get("sign_key"), "", "utf-8");
                StringBuffer params = new StringBuffer();
                params.append("merchant_id=").append(merchantInfoMap.get("merchant_id")).append("&timestamp=").append(timestamp).append("&version=")
                        .append(merchantInfoMap.get("merchant_version"));
                try {
                    params.append("&json_param=").append(URLEncoder.encode(parameter.toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                params.append("&hmac=").append(hmac);
                logger.info("高铁改签异步通知业务参数：" + parameter.toString());
                logger.info("高铁改签异步通知加密参数：" + params.toString());
                String result = "";
                try {
                    if (callbackurl.contains("https")) {
                        result = HttpsUtil.sendHttps(callbackurl + "?" + params.toString());
                    } else {
                        result = HttpUtil.sendByPost(callbackurl, params.toString(), "utf-8");
                    }
                } catch (Exception e) {
                    logger.info("高铁改签异步通知发生异常", e);
                }
                ChangeInfo noticeChangeInfo = new ChangeInfo();
                noticeChangeInfo.setChange_id(changeInfo.getChange_id());
                long start = System.currentTimeMillis();
                if (result != null) {
                    logger.info("改签回调返回结果,耗时" + (System.currentTimeMillis() - start) + "ms,result:" + result);
                    result = result.trim().toLowerCase();
                    if ("success".equals(result)) {
                        logger.info("改签回调请求成功,耗时" + (System.currentTimeMillis() - start) + "ms,order_id:" + order_id + ",result:" + result);
                        /* 回调成功 */
                        noticeChangeInfo.setChange_notify_finish_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
                        noticeChangeInfo.setChange_notify_status("222");
                        noticeChangeInfo.setChange_notify_count(changeInfo.getChange_notify_count() + 1);
                        log.setContent("改签回调成功，result:" + result);
                    } else {
                        /* 回调失败 */
                        logger.info("改签回调请求失败,order_id:" + order_id + ",result:" + result);
                        int count = changeInfo.getChange_notify_count() + 1;
                        if (count == 5) {
                            noticeChangeInfo.setChange_notify_status("333");
                            noticeChangeInfo.setChange_notify_finish_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
                        }
                        noticeChangeInfo.setChange_notify_count(count);
                        log.setContent("高铁改签回调失败，result:" + result);
                    }
                } else {
                    logger.info("高铁改签回调请求失败,order_id:" + order_id + ",result返回null");
                    int count = changeInfo.getChange_notify_count() + 1;
                    if (count == 5) {
                        noticeChangeInfo.setChange_notify_status("333");
                        noticeChangeInfo.setChange_notify_finish_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
                    }
                    noticeChangeInfo.setChange_notify_count(count);
                    log.setContent("高铁改签回调请求失败，result:" + result);
                }
                changeDao.updateChangeInfo(noticeChangeInfo); // 更新通知状态和次数
                changeDao.insertChangeLog(log);
            } catch (Exception e) {
                logger.info("高铁改签回调请求失败，异常" + e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public int updateChangeInfo(ChangeInfo changeInfo) {
        return changeDao.updateChangeInfo(changeInfo);
    }

    public String queryTime(ChangeInfo changeInfo) {

        // 针对没有to_time采用查询补全

        String arrive_time = "";
        String start_query_time = null;
        String arrive_query_time = null;
        String arrive_days = null;
        try {
            //针对没有to_time采用查询补全
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("channel", "ext_gt");//高铁渠道,改签补全时间,渠道名必须以ext开头
            paramMap.put("from_station", changeInfo.getFrom_city());
            paramMap.put("arrive_station", changeInfo.getTo_city());
            paramMap.put("travel_time", changeInfo.getChange_from_time().split(" ")[0]);
            paramMap.put("purpose_codes", "ADULT");
            paramMap.put("train_code", changeInfo.getChange_train_no());

            String params = "";
            params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
            logger.info(changeInfo.getOrder_id() + " 高铁管家发起余票查询" + paramMap.toString() + "url" + queryTicketUrl);
            String result = HttpUtil.sendByPost(queryTicketUrl, params, "UTF-8");
            logger.info(changeInfo.getOrder_id() + " 高铁管家发起余票查询，查询结果" + result);

            String runtime = null;
            int j = 0;
            while (j < 3 && (result == null || result == "" || result.equalsIgnoreCase("STATION_ERROR") || result.equalsIgnoreCase("ERROR") || result.equalsIgnoreCase("NO_DATAS"))) {
                result = HttpUtil.sendByPost(queryTicketUrl, params, "UTF-8");
                j++;
                logger.info("第" + j + "次," + result);
            }

            if (result == null || result == "" || result.equalsIgnoreCase("STATION_ERROR") || result.equalsIgnoreCase("ERROR") || result.equalsIgnoreCase("NO_DATAS")) {
                logger.info(changeInfo.getOrder_id() + "高铁管家改签回调通知,针对没有to_time采用查询补全_查询失败");
            } else {
                try {
                    JSONObject dataTicket = JSONObject.fromObject(result);
                    JSONArray arr = dataTicket.getJSONArray("datajson");
                    int index = arr.size();
                    for (int i = 0; i < index; i++) {
                        if (arr.getJSONObject(i).get("station_train_code").equals(changeInfo.getChange_train_no())) {
                            runtime = arr.getJSONObject(i).getString("lishiValue");
                            start_query_time = arr.getJSONObject(i).getString("start_time");
                            arrive_query_time = arr.getJSONObject(i).getString("arrive_time");
                            arrive_days = arr.getJSONObject(i).getString("day_difference");
                            break;
                        }

                    }
                    if (start_query_time == null || arrive_query_time == null || runtime == null || arrive_days == null) {
                        logger.info("高铁管家改签回调通知,针对没有to_time采用查询补全_查询失败,未匹配到结果");
                    } else {
                        logger.info("高铁管家改签回调通知,针对没有to_time采用查询补全_查询成功" + start_query_time + "_" + arrive_query_time + "_" + runtime);
                        String travel_time = changeInfo.getChange_from_time().split(" ")[0];
                        arrive_time = DateUtil.dateAddDays(travel_time, arrive_days) + " " + arrive_query_time + ":00";
                    }
                } catch (JSONException e1) {
                    logger.info("高铁管家通知,针对没有to_time采用查询补全_查询异常, result=" + result, e1);

                }
            }
        } catch (Exception e) {
            logger.info("高铁管家改签回调补全时间发生异常", e);
        }

        if (StringUtil.isEmpty(arrive_time)) { // 查询余票没有获取到,查询途径站的时间
            arrive_time = queryTimeBySinfo(changeInfo);
            logger.info(changeInfo.getOrder_id() + ",arrive_time:" + arrive_time);
        }

        logger.info(changeInfo.getOrder_id() + ",arrive_time:" + arrive_time);

        return arrive_time;
    }

    //查询途径站的时间
    public String queryTimeBySinfo(ChangeInfo changeInfo) {

        String arrive_time = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("checi", changeInfo.getChange_train_no());
        map.put("name", changeInfo.getTo_city());
        Map<String, String> resultMap = changeDao.querySinfo(map);// 查询途经站

        if (null != resultMap) {
            String arrtime = resultMap.get("arrtime");
            String costDay = resultMap.get("costday");
            Date str = DateUtil.stringToDate(changeInfo.getChange_from_time(), DateUtil.DATE_FMT1);
            Date str1 = DateUtil.dateAddDays(str, Integer.valueOf(costDay));
            String str2 = DateUtil.dateToString(str1, DateUtil.DATE_FMT1);
            StringBuffer dateStr1 = new StringBuffer();
            dateStr1.append(str2).append(" ").append(arrtime).append(":00");
            logger.info("计算出的改签到达时间:" + dateStr1);// 计算出的到达时间
            arrive_time = dateStr1.toString();
        } else {
            logger.info(changeInfo.getOrder_id() + ",resultMap:" + resultMap);
        }

        return arrive_time;
    }

    @Override
    public List<String> getCpListByOrderId(String orderId) {
        return changeDao.getCpListByOrderId(orderId);
    }

    @Override
    public Map<String, Object> queryChangeCpInfo(Map<String, Object> param) {
        return changeDao.queryChangeCpInfo(param);
    }
}
