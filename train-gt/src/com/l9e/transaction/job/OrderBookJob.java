package com.l9e.transaction.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.l9e.transaction.thread.OrderBookThread;
import com.l9e.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;

/**
 * 向合作商户发送订单预定成功通知job
 *
 * @author zuoyuxing
 */
@Component("orderBookJob")
public class OrderBookJob {
    private static final Logger logger = Logger.getLogger(OrderBookJob.class);
    @Resource
    ReceiveNotifyService receiveService;

    @Resource
    private OrderService orderService;

    @Resource
    private CommonService commonService;

    @Resource
    private ThreadPoolTaskExecutor taskExecutorOrderBook;//占座结果回调线程池

    @Resource
    private ApplicationContext applicationContext;

    public void sendMerchantBookData() {

        String timestamp = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2);
        List<Map<String, String>> listNotify = receiveService.findOrderBookNotify();
        gtBookNotify(timestamp, listNotify);// 预定通知

    }

    /**
     * @param timestamp
     * @param listNotify 高铁的预定通知
     */
    public void gtBookNotify(String timestamp, List<Map<String, String>> listNotify) {

        List<Map<String, String>> listNotifyNew = new ArrayList<Map<String, String>>();
        try {

            for (Map<String, String> map : listNotify) {

                String order_id = map.get("order_id");
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("order_id", order_id);
                paramMap.put("notify_status", "12");// 00、未通知 11、准备通知 12、开始通知  22、通知完成
                paramMap.put("current_notify_status", "11");
                Integer num = receiveService.updateOrderGtBookNotifyStatus(paramMap);// 更新状态为11->12
                logger.info(order_id + ",----" + num);
                if (num <= 0) {
                    logger.info("更新失败：" + order_id);
                } else {
                    logger.info("更新成功：" + order_id);
                    listNotifyNew.add(map);
                }

            }

            logger.info("bookList," + listNotifyNew.size() + "," + listNotify.size());

            if (listNotifyNew.size() > 0) {
                for (Map<String, String> map : listNotifyNew) {
                    OrderBookThread orderBookThread=ContextUtil.getBean("orderBookThread",OrderBookThread.class);
                   // OrderBookThread orderBookThread = applicationContext.getBean("orderBookThread", OrderBookThread.class);
                    orderBookThread.setParamMap(map);
                    orderBookThread.setTimestamp(timestamp);
                    taskExecutorOrderBook.submit(orderBookThread);  //异步处理占座结果

                }
            } else {
                logger.info("没有获取到可以处理的订单！");
            }

        } catch (Exception e) {
            logger.info("获取订单出现异常", e);
        }


    }

    /**
     * @param timestamp
     * @param listNotify 原有的预定通知，不作修改
     */
    public void bookNotify(String timestamp, List<Map<String, String>> listNotify) {
        JSONArray orderUsers = null;
        if (listNotify != null && listNotify.size() > 0) {
            for (Map<String, String> map : listNotify) {
                String order_id = "";
                JSONObject json_param = new JSONObject();
                order_id = map.get("order_id");
                logger.info("发起订单预定结果通知！！订单号：" + order_id);
                // 更新订单预定结果通知表通知开始时间和通知次数
                receiveService.updateOrderBookNotifyStartNum(order_id);
                // 添加开始通知日志
                ExternalLogsVo logs = new ExternalLogsVo();
                logs.setOrder_id(order_id);
                logs.setOrder_optlog("发起订单预定结果通知！！订单号：" + order_id);
                logs.setOpter("ext_app");
                orderService.insertOrderLogs(logs);

                orderUsers = orderService.updateBookSuccessInfo("query", order_id, map.get("merchant_id"));

                OrderInfo order = orderService.queryOrderInfo(order_id);

                json_param.put("merchant_order_id", order.getMerchant_order_id());
                json_param.put("merchant_money", order.getPay_money());
                json_param.put("bx_pay_money", order.getBx_pay_money());
                json_param.put("ticket_pay_money", order.getTicket_pay_money());
                json_param.put("order_id", order_id);
                json_param.put("out_ticket_billno", order.getOut_ticket_billno() == null ? "" : order.getOut_ticket_billno());
                json_param.put("out_ticket_time", order.getOut_ticket_time());
                json_param.put("spare_pro1", "");
                json_param.put("spare_pro2", "");
                json_param.put("order_userinfo", orderUsers.toString());
                // 拼接请求参数
                Map<String, String> merchantInfoMap = orderService.queryMerchantInfoByOrderId(order_id);
                Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchantInfoMap.get("merchant_id"));
                String md5_str = merchantInfoMap.get("merchant_id") + timestamp + merchantInfoMap.get("merchant_version") + json_param.toString();
                String hmac = Md5Encrypt.getKeyedDigestFor19Pay(md5_str + merchantSetting.get("sign_key"), "", "utf-8");
                StringBuffer params = new StringBuffer();
                try {
                    params.append("merchant_id=").append(merchantInfoMap.get("merchant_id")).append("&timestamp=").append(timestamp).append("&version=")
                            .append(merchantInfoMap.get("merchant_version")).append("&json_param=").append(URLEncoder.encode(json_param.toString(), "utf-8"))
                            .append("&hmac=").append(hmac);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                logger.info("订单预定结果异步通知参数：" + params.toString());
                String url = map.get("notify_url");
                logger.info("url:" + url);
                String result = "";
                try {
                    if (url.contains("https")) {
                        result = HttpsUtil.sendHttps(url + "?" + params.toString());
                    } else {
                        result = HttpUtil.sendByPost(url, params.toString(), "utf-8");
                    }
                } catch (Exception e) {
                    logger.error("将订单预定结果通知合作商户url异常", e);
                }
                logger.info("result=" + result);
                // 用户接收并处理完成后，更新通知状态为完成
                if (StringUtils.isNotEmpty(result) && "SUCCESS".equals(result.trim())) {
                    receiveService.updateOrderBookNotifyFinish(order_id);
                    logs.setOrder_optlog("通知商户成功[预定结果通知]");
                    orderService.insertOrderLogs(logs);
                }
            }
        }
    }
}
