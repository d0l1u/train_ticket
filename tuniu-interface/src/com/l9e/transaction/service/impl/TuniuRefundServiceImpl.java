package com.l9e.transaction.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.l9e.common.TuniuConstant;
import com.l9e.transaction.component.OutTicketNotifier;
import com.l9e.transaction.component.TuniuCallback;
import com.l9e.transaction.component.model.NoticeObserver;
import com.l9e.transaction.dao.TuniuChangeDao;
import com.l9e.transaction.dao.TuniuNoticeDao;
import com.l9e.transaction.dao.TuniuOrderDao;
import com.l9e.transaction.dao.TuniuRefundDao;
import com.l9e.transaction.exception.TuniuException;
import com.l9e.transaction.exception.TuniuOrderException;
import com.l9e.transaction.exception.TuniuRefundException;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.service.TuniuOrderService;
import com.l9e.transaction.service.TuniuRefundService;
import com.l9e.transaction.thread.SimpleRequest;
import com.l9e.transaction.thread.TuniuThreadPool;
import com.l9e.transaction.vo.*;
import com.l9e.transaction.vo.model.Parameter;
import com.l9e.transaction.vo.model.Result;
import com.l9e.util.*;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Repository("tuniuRefundService")
public class TuniuRefundServiceImpl extends TuniuCommonServiceImpl implements TuniuRefundService {

    private static final Logger logger = Logger.getLogger(TuniuRefundServiceImpl.class);

    @Resource
    private TuniuOrderDao tuniuOrderDao;

    @Resource
    private TuniuRefundDao tuniuRefundDao;

    @Resource
    private TuniuNoticeDao tuniuNoticeDao;
    @Resource
    private TuniuChangeDao tuniuChangeDao;
    
    @Value("#{config}")
    private Properties config;

    @Override
    public Result trainRefund(Parameter parameter) {
        String orderId = parameter.getString("orderId");//途牛订单号
        String callBackUrl = parameter.getString("callBackUrl");//回调地址
        String orderNumber = parameter.getString("orderNumber");//取票单号
        String tuniuRefundId = parameter.getString("refundId");//退牛退票流水号
        String userName = parameter.getString("userName");//账号名
        String userPassword = parameter.getString("userPassword");//账号密码
        logger.info("途牛线上退款开始，orderId：" + orderId);
        Result result = new TuniuResult();
        result.putData("vendorOrderId", orderId);
        result.putData("orderNumber", orderNumber);

        try {
            long ansynStart = System.currentTimeMillis();
            /*查询订单并检查状态*/
            Map<String, Object> queryParam = new HashMap<String, Object>();
            queryParam.put("orderId", orderId);
            TuniuOrder order = tuniuOrderDao.selectOneOrder(queryParam);
            if (order == null) {
                logger.info("途牛退款订单不存在，orderId : " + orderId);
                throw new TuniuOrderException(RETURN_CODE_REFUND_FAILURE);
            }
            if (userName != "") {
                //传账号信息
                if (!userName.equals(order.getUserName()) || userPassword.equals("")) {
                    logger.info("途牛退票传参账号有误，数据库userName:" + order.getOrderName() + ",传参userName:" + userName + ",userPassword:" + userPassword);
                    throw new TuniuOrderException(RETURN_CODE_PARAM_ERROR);
                } else {
                    //更新订单系统
                    order.setUserPassword(userPassword);
                    tuniuOrderDao.updateOrder(order);
                    //更新出票系统账号
                    //参数拼接整合
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userName", userName);
                    params.put("userPassWord", userPassword);
                    String uri = UrlFormatUtil.createUrl("", params, "utf-8");
                    //post请求
                    String editAccountresult = HttpUtil.sendByPost(config.getProperty("cp_edit_account"), uri, "UTF-8");
                    logger.info("途牛退票，userName" + order.getUserName() + "，修改账号结果：" + editAccountresult);
                    //解析返回参数
                    JSONObject strObject = JSON.parseObject(editAccountresult);
                    Boolean flag = strObject.getBoolean("success");
                    if (flag) {
                        logger.info("途牛退票，修改账号成功，userName" + order.getUserName());
                    } else {
                        logger.info("途牛退票，修改账号失败 ，userName" + order.getUserName());
                    }
                }
            }
            String status = order.getOrderStatus();
            if (TuniuOrderService.STATUS_OUT_SUCCESS.equals(status)) {//出票成功退票
                List<TuniuPassenger> passengers = tuniuOrderDao.selectPassengers(queryParam);
                List<TuniuRefundTicket> tickets = parameter.getList("tickets", TuniuRefundTicket.class);
                logger.info("途牛退款信息,orderId: " + orderId + ", tickets : " + tickets.toString());
                if (tickets == null || passengers == null || tickets.size() == 0 || passengers.size() == 0) {
                    logger.info("途牛退款车票不存在或车票异常, orderId : " + orderId);
                    throw new TuniuRefundException(RETURN_CODE_REFUND_FAILURE);
                }

                List<TuniuRefund> refunds = tuniuRefund(tickets, passengers,
                        order, callBackUrl, orderNumber, tuniuRefundId);
                for (TuniuRefund refund : refunds) {
                    queryParam.put("cpId", refund.getCpId());
                    TuniuRefund refund_old = tuniuRefundDao.selectOneRefund(queryParam);
                    if (refund_old == null) {
                        /*退款信息入库*/
                        addRefund(refund);
                        /*通知入库*/
                        Notice notice = refundNotice(refund, callBackUrl);
                        addNotice(notice);
                    } else if (refund_old != null && "22".equals(refund_old.getRefundStatus())) {
                        logger.info("途牛异步请求退款,已拒绝退款请求,重置orderId：" + orderId + "|cpId：" + refund.getCpId());
                        //删除旧的订单信息
                        tuniuRefundDao.deleteRefund(refund_old);
                        Notice notice_old = new Notice();
                        notice_old.setOrderId(orderId);
                        notice_old.setCpId(refund_old.getCpId());
                        notice_old.setRefundId(refund_old.getRefundId());
                        tuniuNoticeDao.deleteNotice(notice_old);
                        //添加新的信息
                        addRefund(refund);
                        Notice notice = refundNotice(refund, callBackUrl);
                        addNotice(notice);

                    } else if ("11".equals(refund_old.getRefundStatus())) {
                        logger.info("途牛异步请求退款,已退款成功！重复orderId：" + orderId + "|cpId：" + refund.getCpId() + "当前退票状态：" + refund_old.getRefundStatus());
                        throw new TuniuRefundException(RETURN_CODE_REFUND_ALREADY_REFUND);
                    } else {
                        logger.info("途牛异步请求退款,请求失败！重复orderId：" + orderId + "|cpId：" + refund.getCpId() + "当前退票状态：" + refund_old.getRefundStatus());
                        throw new TuniuRefundException(RETURN_CODE_REFUND_REFUNDING);
                    }
                }
                long ansynEnd = System.currentTimeMillis();
                logger.info("途牛异步请求退款成功，orderId：" + orderId + ", 耗时:" + (ansynEnd - ansynStart) + " ms");
            } else {
                logger.info("途牛退票状态异常，订单状态：" + status + ", orderId : " + orderId);
                throw new TuniuRefundException(RETURN_CODE_REFUND_FAILURE);
            }
        } catch (TuniuException e) {
            result.setCode(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("途牛退款异常 orderId : " + orderId + ",e: " + e.getMessage());
            e.printStackTrace();
            result.setCode("140011");
        }
        return result;
    }

    @Override
    public void addRefund(TuniuRefund refund) {
        tuniuRefundDao.insertRefund(refund);
    }


    @Override
    public TuniuRefund getRefundById(Integer refundId) {
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("refundId", refundId);
        return tuniuRefundDao.selectOneRefund(queryParam);
    }

    @Override
    public void sendRefundOrder(TuniuRefund refund, Notice notice) {

        String orderId = refund.getOrderId();
        String cpId = refund.getCpId();
        logger.info("途牛异步请求退票系统退票，orderId：" + orderId + ", cpId: " + cpId);
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("orderId", orderId);
        queryParam.put("cpId", cpId);

        TuniuOrder order = tuniuOrderDao.selectOneOrder(queryParam);
        TuniuPassenger passenger = tuniuOrderDao.selectOnePassenger(queryParam);
        Account account = tuniuRefundDao.selectOneAccount(queryParam);

        String accountUsername = "";
        String accountPassword = "";
        if (account != null) {
            accountUsername = account.getUsername();
            accountPassword = account.getPassword();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderid", orderId);
        params.put("cpid", cpId);
        params.put("refundmoney", refund.getRefundMoney().toString());
        params.put("outticketbillno", order.getOutTicketBillno());
        params.put("channel", "tuniu");
        params.put("backurl", TuniuConstant.CP_REFUND_CALLBACK);// 回调地址
        params.put("accountname", accountUsername);
        params.put("accountpwd", accountPassword);
        params.put("refundseq", refund.getRefundSeq());
        params.put("userremark", refund.getUserRemark());
        params.put("refundpercent", refundPercent(order.getFromTime()).toString());
        if (passenger == null) {
            Map<String, Object> changeCpParam = new HashMap<String, Object>();
            changeCpParam.put("new_cp_id", cpId);
            changeCpParam.put("order_id", refund.getOrderId());
            TuniuChangePassengerInfo cp = tuniuChangeDao.selectChangeCp(changeCpParam).get(0);
            params.put("buymoney", cp.getChange_buy_money().toString());
            params.put("username", cp.getUser_name());
            params.put("tickettype", cp.getTicket_type());
            params.put("idstype", cp.getIds_type());
            params.put("userids", cp.getUser_ids());
            params.put("seattype", cp.getChange_seat_type());
            params.put("trainbox", cp.getChange_train_box());
            params.put("seatno", cp.getChange_seat_no());
            Map<String, Object> changeParam = new HashMap<String, Object>();
            changeParam.put("change_id", cp.getChange_id());
            TuniuChangeInfo changeInfo = tuniuChangeDao.selectChangeInfo(changeParam);
            params.put("trainno", changeInfo.getChange_train_no());
            params.put("fromstation", changeInfo.getFrom_city());
            params.put("arrivestation", changeInfo.getTo_city());
            params.put("traveltime", changeInfo.getChange_from_time().split(" ")[0]);
            params.put("fromtime", changeInfo.getChange_from_time());
            params.put("outtickettime", changeInfo.getOption_time());
            params.put("refundtype", "55");
        } else {
            params.put("buymoney", order.getBuyMoney().toString());
            params.put("username", passenger.getUserName());
            params.put("tickettype", passenger.getTicketType());
            params.put("idstype", passenger.getIdsType());
            params.put("userids", passenger.getUserIds());
            params.put("seattype", passenger.getSeatType());
            params.put("trainbox", passenger.getTrainBox());
            params.put("seatno", passenger.getSeatNo());
            params.put("trainno", order.getTrainNo());
            params.put("fromstation", order.getFromCity());
            params.put("arrivestation", order.getToCity());
            params.put("traveltime", DateUtil.dateToString(order.getTravelDate(), DateUtil.DATE_FMT1));
            params.put("fromtime", DateUtil.dateToString(order.getFromTime(), DateUtil.DATE_FMT3));
            params.put("outtickettime", DateUtil.dateToString(order.getOutTicketTime(), DateUtil.DATE_FMT3));
            params.put("refundtype", "11");
        }
        logger.info("paramMap:" + params);

		/* 处理请求返回结果对象 */
        OutTicketNotifier notifier = (OutTicketNotifier) BeanUtil.getBean("outTicketNotifier");
        notifier.setNotice(notice);
        notifier.setParameters(params);
        notifier.setService(NoticeObserver.REFUND);
        /* 请求线程 */
        SimpleRequest request = new SimpleRequest(SimpleRequest.METHOD_POST,
                TuniuConstant.CP_REFUND_URL, notifier);

        TuniuThreadPool.cpNoticeThreadSubmit(request);
    }

    @Override
    public void addNotice(Notice notice) {
        tuniuNoticeDao.insertRefundNotice(notice);
    }

    @Override
    public void updateRefund(TuniuRefund refund) {
        tuniuRefundDao.updateRefund(refund);
    }

    @Override
    public TuniuRefund getRefundByOrderIdAndCpId(String orderId, String cpId) {
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("orderId", orderId);
        queryParam.put("cpId", cpId);
        return tuniuRefundDao.selectOneRefund(queryParam);
    }

    @Override
    public void callbackRefundOrder(TuniuRefund refund, Notice notice) {

        String orderId = refund.getOrderId();
        String cpId = refund.getCpId();
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("orderId", orderId);
        queryParam.put("cpId", cpId);
        TuniuPassenger passenger = tuniuOrderDao.selectOnePassenger(queryParam);
        String refundStatus = refund.getRefundStatus();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> ticketMap = new HashMap<String, Object>();


        String returnCode = null;
        String ourErrorMsg = null;//退款失败原因匹配不上是，返回通用码未知异常，并返回真实原因信息
        /*ticket*/
        String ticketNo = refund.getCpId();
        String passengerName;
        String passportTypeId;
        String passportNo;
        if (passenger == null) {
            //查询改签乘客信息
            Map<String, Object> changeCpParam = new HashMap<String, Object>();
            changeCpParam.put("new_cp_id", ticketNo);
            changeCpParam.put("order_id", refund.getOrderId());
            TuniuChangePassengerInfo cp = tuniuChangeDao.selectChangeCp(changeCpParam).get(0);
            passengerName = cp.getUser_name();
            passportTypeId = cp.getTn_ids_type();
            passportNo = cp.getUser_ids();
        } else {
            passengerName = passenger.getUserName();
            passportTypeId = passenger.getTuniuIdsType();
            passportNo = passenger.getUserIds();
        }


        boolean returnSuccess = false;
        String returnTime = null;
        String returnFailId = null;
        String returnFailMsg = null;

		/*data*/
        dataMap.put("vendorOrderId", orderId);//合作订单id和途牛订单id保持一致
        dataMap.put("orderNumber", refund.getOutTicketBillno());
        dataMap.put("refundId", refund.getTuniuRefundId());
        boolean returnState = false;
        Double returnMoney = null;
        String returnMsg = null;
        if (STATUS_REFUND_SUCCESS.equals(refundStatus)) {//退款成功
            returnCode = RETURN_CODE_REFUND_SUCCESS;

            returnSuccess = true;
            returnState = true;
            returnMoney = refund.getRefundMoney();
            returnTime = refund.getVerifyTime() == null ? null : DateUtil.dateToString(refund.getVerifyTime(), DateUtil.DATE_FMT3);
            returnFailId = "0";
        } else if (STATUS_REFUND_FAILURE.equals(refundStatus)) {//退款失败

            returnFailId = tuniuFailReason(refund.getFailReason());
            returnCode = tuniuFailReasonCode(refund.getFailReason());
            returnFailMsg = tuniuFailMsg(refund.getFailReason());
        }

        String refundType = refund.getRefundType();
        String ticketsArrayName = "";
        if (REFUND_TYPE_OFFLINE.equals(refundType) || REFUND_TYPE_STATION.equals(refundType)) {//线下退款 或 车站退款
            ticketMap.put("ticketNo", ticketNo);
            ticketMap.put("passengerName", passengerName);
            ticketMap.put("passportTypeId", passportTypeId);
            ticketMap.put("passportNo", passportNo);

            ticketMap.put("returnSuccess", returnSuccess);
            ticketMap.put("returnMoney", returnMoney);
            ticketMap.put("returnTime", returnTime);
            ticketMap.put("returnFailId", returnFailId);
            ticketMap.put("returnFailMsg", returnFailMsg);
            // TODO:将文档中示例为null的字段尽量有值补上
            ticketsArrayName = "tickets";

            dataMap.put("orderId", orderId);
            dataMap.put("returnMsg", "线下退款");
        } else if (REFUND_TYPE_ONLINE.equals(refundType) || REFUND_TYPE_CHANGE.equals(refundType)) {//线上退款
            ticketMap.put("ticketNo", ticketNo);
            ticketMap.put("passengerName", passengerName);
            ticketMap.put("passportTypeId", passportTypeId);
            ticketMap.put("passportNo", passportNo);

            ticketMap.put("returnSuccess", returnSuccess);
            ticketMap.put("returnMoney", returnMoney);
            ticketMap.put("returnTime", returnTime);
            ticketMap.put("returnFailId", returnFailId);
            ticketMap.put("returnFailMsg", returnFailMsg);
            ticketsArrayName = "returnTickets";

            dataMap.put("returnMsg", returnMsg);
        }

        Object[] objArray = new Object[1];
        objArray[0] = ticketMap;

        List<Integer> offLineNotifyList = new ArrayList<Integer>();
        //线下退款一个车票第二次通知不传tickets
        if (REFUND_TYPE_OFFLINE.equals(refundType) || REFUND_TYPE_STATION.equals(refundType)) {//线下退款 或 车站退款
            //根据车票号查询有没有通知过的退款记录
            Map<String, Object> notifyParam = new HashMap<String, Object>();
            notifyParam.put("order_id", orderId);
            notifyParam.put("cp_id", cpId);
            notifyParam.put("notify_status", "22");
            offLineNotifyList = tuniuRefundDao.checkOfflineNotify(notifyParam);
        }
        if (offLineNotifyList == null || offLineNotifyList.size() == 0) {
            dataMap.put(ticketsArrayName, objArray);
        }

        dataMap.put("returnState", returnState);
        dataMap.put("returnMoney", returnMoney);

        logger.info("途牛退款回调，参数为" + dataMap.toString());
        try {
            String dataJson = JacksonUtil.generateJson(dataMap);
            AsynchronousOutput out = new AsynchronousOutput();

            out.setData(dataJson);
            out.setReturnCode(returnCode);
            if (returnCode.equals(RETURN_CODE_UNKNOWN_ERROR)) {
                out.setErrorMsg(ourErrorMsg);
            }

			/* 处理请求返回结果对象 */
            TuniuCallback callback = (TuniuCallback) BeanUtil.getBean("tuniuCallback");
            callback.setNotice(notice);
            callback.setAsynOutput(out);
            callback.setService(NoticeObserver.REFUND);
            /* 请求线程 */
            SimpleRequest request = new SimpleRequest(SimpleRequest.METHOD_POST,
                    notice.getNotifyUrl(), callback);

            TuniuThreadPool.tuniuCallbackSubmit(request);
        } catch (IOException e) {
            logger.info("途牛退款回调data参数json格式化异常,orderId: " + orderId + ", cpId : " + cpId);
        }

    }

    /**
     * 途牛失败描述
     *
     * @param failId
     * @return
     */
    private String tuniuFailMsg(String failId) {
        if (failId == null || failId.equals(""))
            return null;

        try {
            Integer codeValue = Integer.valueOf(failId);
            switch (codeValue) {
                case 1:
                    return "已取纸制票，不能退票";
                case 2:
                    return "已过发车时间";
                case 3:
                    return "来电取消";
                case 4:
                    return "账号被封";
                case 6:
                    return "退款金额有损失";
                case 7:
                    return "尊敬的旅客，为防止网上囤票倒票，给广大旅客创造一个公平的购票环境，凡通过互联网或手机购买的本次列车车票，如需办理退票、改签和变更到站等变更业务，请持乘车人身份证件原件到就近车站办理，代办时还需持代办人的身份证件原件";
                default:
                    return "退票失败";
            }

        } catch (NumberFormatException e) {
            logger.info("退款错误码异常，无法匹配信息，failId：" + failId);
            return null;
        }
    }

    /**
     * 途牛车票失败原因
     *
     * @param failReason
     * @return
     */
    private String tuniuFailReason(String failReason) {
        /**
         * 19e  1    已取票
         2 已过时间
         3 来电取消
         4 账号被封
         6 退款金额有损失
         7 不可退票
         途牛
         1 未查询到该车票
         2退票操作异常，请与客服联系
         3已改签
         4已退票
         5已出票，只能在窗口办理退票
         6不可退票。
         */
        if (failReason == null || failReason.equals(""))
            return null;

        try {
            Integer codeValue = Integer.valueOf(failReason);
            switch (codeValue) {
                case 1:
                    return "5"; //已取票
                case 7:
                    return "6";//不可退票
                case 2:
                    return "6";//已过发车时间
                case 3: //来电取消
                case 4:
                    return "6";//账号被封
                case 6: //退款金额有损失
                default:
                    return "2";
            }
        } catch (NumberFormatException e) {
            logger.info("退款错误码异常，无法匹配信息，failReason：" + failReason);
            return null;
        }
    }

    /**
     * 途牛失败编码
     *
     * @param failReason
     * @return
     */
    private String tuniuFailReasonCode(String failReason) {
        /**
         * 19e  1    已取票
         2 已过时间
         3 来电取消
         4 账号被封
         6 退款金额有损失
         7 不可退票
         */
        if (failReason == null || failReason.equals(""))
            return null;
        try {
            Integer codeValue = Integer.valueOf(failReason);
            switch (codeValue) {
                case 1:
                    return "140003"; //已取票
                case 7:
                    return "140007";//不可退票
                case 2:
                    return "140000";//已过发车时间
                case 3: //来电取消
                case 4:
                    return "140010";//账号被封
                case 6: //退款金额有损失
                case 8:
                    return "140009"; //12306账号密码错误
                default:
                    return "140002";
            }
        } catch (NumberFormatException e) {
            logger.info("退款错误码异常，无法匹配信息，failReason：" + failReason);
            return null;
        }
    }

    /**
     * 退款通知
     *
     * @param orderId
     * @param refundId
     * @param callbackUrl
     * @return
     */
    private Notice refundNotice(TuniuRefund refund, String callbackUrl) {
        String orderId = refund.getOrderId();
        String cpId = refund.getCpId();
        Integer refundId = refund.getRefundId();
        if (orderId == null || refundId == null || cpId == null || orderId.equals("") || cpId.equals("") || refundId < 1)
            return null;

        Notice notice = new Notice();
        notice.setOrderId(orderId);
        notice.setCpId(cpId);
        notice.setRefundId(refundId);
        if (callbackUrl != null && !callbackUrl.equals(""))
            notice.setNotifyUrl(callbackUrl);

        notice.setCpNotifyStatus(TuniuCommonService.NOTIFY_PREPARED);
        return notice;
    }

    /**
     * 组装退款记录
     *
     * @param tickets
     * @param passengers
     * @return
     * @throws TuniuRefundException
     */
    private List<TuniuRefund> tuniuRefund(List<TuniuRefundTicket> tickets, List<TuniuPassenger> passengers,
                                          TuniuOrder order, String callbackUrl,
                                          String orderNumber, String tuniuRefundId) throws TuniuRefundException {

        List<TuniuRefund> refunds = new ArrayList<TuniuRefund>();
        for (TuniuRefundTicket ticket : tickets) {
            String ticketNo = ticket.getTicketNo();
            Map<String, Object> passParam = new HashMap<String, Object>();
            passParam.put("orderId", order.getOrderId());
            passParam.put("cpId", ticketNo);
            TuniuPassenger passenger = tuniuOrderDao.selectOnePassenger(passParam);
            if (passenger == null) {
                //查询改签乘客信息
                Map<String, Object> changeCpParam = new HashMap<String, Object>();
                changeCpParam.put("new_cp_id", ticketNo);
                changeCpParam.put("order_id", order.getOrderId());
                List<TuniuChangePassengerInfo> cps = tuniuChangeDao.selectChangeCp(changeCpParam);
                if (cps != null) {
                    for (TuniuChangePassengerInfo changeCp : cps) {
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("change_id", changeCp.getChange_id());
                        TuniuChangeInfo changeInfo = tuniuChangeDao.selectChangeInfo(param);
                        if (changeCp.getIs_changed().equals("Y")) {
                            logger.info("途牛改签退票" + order.getOrderId());
                            TuniuRefund refund = new TuniuRefund();
                            String fromTime = changeInfo.getChange_from_time();
                            String from_15d = DateUtil.dateAddDaysFmt3(fromTime, "-15");
                            if (new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd HH:mm:ss"))) {
                                //如果改签票在15天之外，查询原票时间
                                fromTime = DateUtil.dateToString(order.getFromTime(), DateUtil.DATE_FMT3);
                            }
                            Double buyMoney = changeCp.getChange_buy_money();
                            Double refundPercent = refundPercent(DateUtil.stringToDate(fromTime, DateUtil.DATE_FMT3));//费率
                            Double refundMoney = refundMoney(buyMoney, refundPercent);
                            refund.setCpId(ticketNo);
                            refund.setOrderId(order.getOrderId());
                            refund.setRefundSeq(IDGenerator.generateID("TNTK_"));
                            refund.setOutTicketBillno(orderNumber);
                            refund.setRefundMoney(refundMoney);
                            refund.setRefundStatus(STATUS_PRE_ROBOT_ALTER);
                            refund.setRefundType(REFUND_TYPE_CHANGE);
                            refund.setCallbackUrl(callbackUrl);
                            refund.setTuniuRefundId(tuniuRefundId);
                            refund.setOptPerson("tuniu_refund");
                            refunds.add(refund);
                            break;
                        }
                    }
                }
            } else {
                TuniuRefund refund = new TuniuRefund();
                Date fromTime = order.getFromTime();
                Double buyMoney = passenger.getBuyMoney();
                Double refundPercent = refundPercent(fromTime);//费率
                Double refundMoney = refundMoney(buyMoney, refundPercent);
                refund.setCpId(ticketNo);
                refund.setOrderId(order.getOrderId());
                refund.setRefundSeq(IDGenerator.generateID("TNTK_"));
                refund.setOutTicketBillno(orderNumber);
                refund.setRefundMoney(refundMoney);
                refund.setRefundStatus(STATUS_PRE_ROBOT_ALTER);
                refund.setRefundType(REFUND_TYPE_ONLINE);
                refund.setCallbackUrl(callbackUrl);
                refund.setTuniuRefundId(tuniuRefundId);
                refund.setOptPerson("tuniu_refund");
                refunds.add(refund);
            }
        }
/*		for(TuniuRefundTicket ticket : tickets) {
            String ticketNo = ticket.getTicketNo();
			for(TuniuPassenger passenger : passengers) {
				String cpId = passenger.getCpId();
				if(cpId.equals(ticketNo)) {//匹配到车票信息和退票信息
					
					TuniuRefund refund = new TuniuRefund();
					Date fromTime = order.getFromTime();
					Double buyMoney = passenger.getBuyMoney();
					Double refundPercent = refundPercent(fromTime);//费率
					Double refundMoney = refundMoney(buyMoney,refundPercent);
					
					refund.setCpId(cpId);
					refund.setOrderId(order.getOrderId());
					refund.setRefundSeq(IDGenerator.generateID("TNTK_"));
					refund.setOutTicketBillno(orderNumber);
					refund.setRefundMoney(refundMoney);
					refund.setRefundStatus(STATUS_PRE_ROBOT_ALTER);
					refund.setRefundType(REFUND_TYPE_ONLINE);
					refund.setCallbackUrl(callbackUrl);
					refund.setTuniuRefundId(tuniuRefundId);
					
					refund.setOptPerson("tuniu_refund");
					
					refunds.add(refund);
					break;
				}
			}
		}*/
        if (tickets.size() != refunds.size())
            throw new TuniuRefundException(RETURN_CODE_REFUND_FAILURE);

        return refunds;
    }

    /**
     * 获取费率
     * 春运期间：
     * 1、改签到春运期间，退票时收取20%手续费
     * 2、其他情况参照非春运期间
     * <p>
     * 非春运期间
     * ①开车前15天以上的，退票时不收退票费；
     * ②开车前48小时以上、不足15天的，退票时收取票价5%的退票费；
     * ③开车前24以上，不足48小时的，退票时收取票价10%退票费。
     * 4开车前不足24小时，退票时收取20%退票费
     *
     * @param fromTime
     * @return
     */
    private Double refundPercent(Date fromTime) {
        Date from15d = DateUtil.dateAddDays(fromTime, -15);
        Date from48 = DateUtil.dateAddDays(fromTime, -2);
        Date from24 = DateUtil.dateAddDays(fromTime, -1);
        Double feePercent = 0.0;

        if (new Date().before(from15d)) {
            feePercent = 0.0;
        } else if (new Date().before(from48)) {
            feePercent = 0.05;
        } else if (new Date().before(from24)) {
            feePercent = 0.1;
        } else {
            feePercent = 0.2;
        }
        return feePercent;
    }

    /**
     * 退款金额=支付金额-手续费(支付金额X费率)
     *
     * @param payMoney
     * @param refundPercent
     * @return
     */
    private Double refundMoney(Double buyMoney, Double refundPercent) {
        Double refundMoney = 0.0;
        if (buyMoney == null || buyMoney.equals(0.0))
            return refundMoney;

        Double fee = 0.0;
        if (refundPercent != 0) {
            fee = AmountUtil.quarterConvert(AmountUtil.mul(buyMoney, refundPercent));//手续费
            fee = fee > 2 ? fee : 2;
        }
        refundMoney = AmountUtil.ceil(AmountUtil.sub(buyMoney, fee));

        return refundMoney;
    }

    @Override
    public void queryRefundNotice(Parameter parameter, SynchronousOutput synOutput) {
        String orderId = parameter.getString("orderId");//途牛订单号
        String orderNumber = parameter.getString("orderNumber");//取票单号
        JSONArray tickets = parameter.getJSONArray("tickets");
        JSONArray tickets_norefund = CloneUtils.clone(tickets);//克隆一个对象
        logger.info("途牛催退款通知开始，orderId：" + orderId);
        JSONArray resultTickets = new JSONArray();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("vendorOrderId", orderId);
        resultMap.put("orderId", orderId);
        resultMap.put("orderNumber", orderNumber);
        synOutput.setSuccess(true);
        synOutput.setReturnCode(RETURN_CODE_SUCCESS);
        synOutput.setErrorMsg("请求成功");
        try {
            if (orderId.equals("") || orderNumber.equals("") || tickets == null || tickets.size() == 0) {
                logger.info("途牛催退款通知参数有误，orderId : " + orderId);
                synOutput.setReturnCode(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
                synOutput.setErrorMsg("参数有误");
                synOutput.setSuccess(false);
                logger.info("同步输出结果：" + JacksonUtil.generateJson(synOutput));
                return;
            }
            /*查询订单并检查状态*/
            Map<String, Object> queryParam = new HashMap<String, Object>();
            queryParam.put("orderId", orderId);
            TuniuOrder order = tuniuOrderDao.selectOneOrder(queryParam);
            if (order == null) {
                logger.info("途牛催退款通知订单不存在，orderId : " + orderId);
                synOutput.setErrorMsg("参数有误，订单不存在");
                synOutput.setSuccess(false);
                synOutput.setReturnCode(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
                logger.info("同步输出结果：" + JacksonUtil.generateJson(synOutput));
                return;
            }
            String status = order.getOrderStatus();
            if (TuniuOrderService.STATUS_OUT_SUCCESS.equals(status)) {//出票成功退票
                for (int i = 0; i < tickets.size(); i++) {  //查询退款记录表
                    String ticketNo = (String) tickets.get(i);
                    queryParam.put("cpId", ticketNo);
                    TuniuPassenger passenger = tuniuOrderDao.selectOnePassenger(queryParam);
                    if (passenger == null) {
                        logger.info("途牛催退款通知查询车票不存在或车票异常, orderId : " + orderId);
                        synOutput.setErrorMsg("参数有误，车票不存在");
                        synOutput.setSuccess(false);
                        synOutput.setReturnCode(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
                        logger.info("同步输出结果：" + JacksonUtil.generateJson(synOutput));
                        return;
                    } else {
                        JSONObject jsonPass = new JSONObject();
                        Map<String, Object> refundParam = new HashMap<String, Object>();
                        refundParam.put("orderId", orderId);
                        refundParam.put("cpId", ticketNo);
                        refundParam.put("refundStatus", TuniuRefundService.STATUS_REFUND_SUCCESS);//退款完成
                        TuniuRefund refundInfo = tuniuRefundDao.selectOneRefund(refundParam);
                        if (refundInfo != null) {
                            jsonPass.put("ticketNo", ticketNo);
                            jsonPass.put("passengerName", passenger.getUserName());
                            jsonPass.put("passportTypeId", passenger.getTuniuIdsType());
                            jsonPass.put("passportseNo", passenger.getUserIds());
                            jsonPass.put("returnSuccess", true);
                            jsonPass.put("returnMoney", refundInfo.getRefundMoney());
                            jsonPass.put("returnTime", refundInfo.getCreateTime());
                            jsonPass.put("returnFailId", "");
                            jsonPass.put("returnFailMsg", "");
                            jsonPass.put("returnStatus", "2");
                            jsonPass.put("returnMsg", "已退款");
                            resultTickets.add(jsonPass);
                            tickets_norefund.remove(ticketNo);
                        }
                        logger.info("途牛催退款通知查询退款记录完成，orderId：" + orderId);
                    }
                }

                logger.info(orderId + ",tickets_norefund:" + tickets_norefund);

                for (int i = 0; i < tickets_norefund.size(); i++) {  //未退款的生成催退款记录
                    String ticketNo = (String) tickets_norefund.get(i);
                    queryParam.put("cpId", ticketNo);
                    TuniuPassenger passenger = tuniuOrderDao.selectOnePassenger(queryParam);
                    if (passenger == null) {
                        logger.info("途牛催退款通知查询车票不存在或车票异常, orderId : " + orderId);
                        synOutput.setErrorMsg("参数有误，车票不存在");
                        synOutput.setSuccess(false);
                        synOutput.setReturnCode(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
                        logger.info("同步输出结果：" + JacksonUtil.generateJson(synOutput));
                        return;
                    } else {
                        JSONObject jsonPass = new JSONObject();
                        Map<String, Object> urgeRefundParam = new HashMap<String, Object>();
                        urgeRefundParam.put("order_id", orderId);
                        urgeRefundParam.put("cp_id", ticketNo);
                        TuniuUrgeRefund urgeRefundInfo = tuniuRefundDao.getUrgeRefundInfo(urgeRefundParam);
                        if (urgeRefundInfo == null) {
                            //插入
                            urgeRefundInfo = new TuniuUrgeRefund();
                            urgeRefundInfo.setOrder_id(orderId);
                            urgeRefundInfo.setCp_id(ticketNo);
                            urgeRefundInfo.setOut_ticket_billno(orderNumber);
                            urgeRefundInfo.setUrge_status(STATUS_URGE_ING);
                            tuniuRefundDao.insertUrgeRefund(urgeRefundInfo);
                        } else if (urgeRefundInfo.getUrge_status().equals(STATUS_URGE_OTHER) && urgeRefundInfo.getRemark().equals("暂未核实到退款")) {
                            Date opt_time = DateUtil.stringToDate(urgeRefundInfo.getOpt_time(), DateUtil.DATE_FMT3);
                            if (DateUtil.minuteDiff(new Date(), opt_time) > 30) {
                                urgeRefundInfo.setRemark("再次查询暂未退款订单");
                                urgeRefundInfo.setUrge_status(STATUS_URGE_ING);
                                tuniuRefundDao.updateUrgeRefund(urgeRefundInfo);
                            }
                        }
                        jsonPass.put("ticketNo", ticketNo);
                        jsonPass.put("passengerName", passenger.getUserName());
                        jsonPass.put("passportTypeId", passenger.getTuniuIdsType());
                        jsonPass.put("passportseNo", passenger.getUserIds());
                        jsonPass.put("returnSuccess", getReturnSuccess(urgeRefundInfo.getUrge_status()));
                        jsonPass.put("returnMoney", urgeRefundInfo.getRefund_money() == null ? "" : urgeRefundInfo.getRefund_money());
                        jsonPass.put("returnTime", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
                        jsonPass.put("returnFailId", "");
                        jsonPass.put("returnFailMsg", "");
                        jsonPass.put("returnStatus", urgeRefundInfo.getUrge_status().substring(1));
                        jsonPass.put("returnMsg", getReturnMsg(urgeRefundInfo.getUrge_status(), urgeRefundInfo.getRemark()));
                        resultTickets.add(jsonPass);
                        logger.info("途牛催退款通知查询成功，orderId：" + orderId);
                    }
                }

            } else {
                logger.info("途牛催退款通知订单状态不是出票成功，orderId : " + orderId);
                logger.info("订单状态是" + status);
                synOutput.setErrorMsg("订单状态不正确");
                synOutput.setReturnCode(RETURN_CODE_REFUND_NOTICE_FAILURE);
                synOutput.setSuccess(false);
                logger.info("同步输出结果：" + JacksonUtil.generateJson(synOutput));
                return;
            }
            resultMap.put("tickets", resultTickets);
            synOutput.setData(resultMap);

            logger.info("同步输出结果：" + JacksonUtil.generateJson(synOutput));

        } catch (Exception e) {
            logger.info("途牛退款异常 orderId : " + orderId + ",e: " + e.getMessage());
            synOutput.setErrorMsg("催退款通知查询发生异常");
            synOutput.setReturnCode(RETURN_CODE_REFUND_NOTICE_FAILURE);
            synOutput.setSuccess(false);
            try {
                logger.info("同步输出结果：" + JacksonUtil.generateJson(synOutput));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public Boolean getReturnSuccess(String status) {
        if (status.equals(STATUS_URGE_ING) || status.equals(STATUS_URGE_SUCCESS)) {
            return true;
        }
        return false;
    }

    public String getReturnMsg(String status, String remark) {
        if (status.equals(STATUS_URGE_ING)) {
            return "退款处理中";
        } else if (status.equals(STATUS_URGE_SUCCESS)) {
            return "已退款";
        } else if (status.equals(STATUS_URGE_FAIL)) {
            return "退款失败";
        } else {
            return remark;
        }

    }
}
