package com.kuyou.train.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kuyou.train.common.enums.CardType;
import com.kuyou.train.common.enums.SeatType;
import com.kuyou.train.common.enums.TicketType;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.common.util.DateFormat;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.common.util.HttpUtil;
import com.kuyou.train.common.util.MD5Util;
import com.kuyou.train.entity.dto.HthySyncDto;
import com.kuyou.train.entity.dto.TicketDetailDto;
import com.kuyou.train.entity.dto.TicketInfoDto;
import com.kuyou.train.entity.po.OrderPo;
import com.kuyou.train.entity.po.ServicePo;
import com.kuyou.train.entity.req.*;
import com.kuyou.train.entity.resp.HthyTicketDetailResp;
import com.kuyou.train.entity.resp.HthyTicketInfoResp;
import com.kuyou.train.service.ChangeService;
import com.kuyou.train.service.HistoryService;
import com.kuyou.train.service.HthyService;
import com.kuyou.train.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * HthyServiceImpl
 *
 * @author taokai3
 * @date 2018/11/5
 */
@Slf4j
@Service
public class HthyServiceImpl implements HthyService {

    private static final Integer ASYNC = 10 * 1000;
    private static final Integer SYNC = 60 * 1000;

    @Value("${hthy.partnerId}")
    private String partnerId;

    @Value("${hthy.key}")
    private String key;

    @Value("${hthy.order}")
    private String url;

    @Value("${callback.order}")
    private String callbackOrderUrl;

    @Value("${callback.changeOrder}")
    private String callbackChangeOrderUrl;

    @Value("${callback.refund}")
    private String callbackRefundUrl;

    @Value("${callback.cancel}")
    private String callbackCancelUrl;

    @Value("${callback.changeCancel}")
    private String callbackChangeCancelUrl;

    @Resource
    private HistoryService historyService;

    @Resource
    private ChangeService changeService;

    @Resource
    private OrderService orderService;

    @Override
    public String bookCancel(ServicePo orderPo) {
        JSONObject requestJson = createParameter("train_cancel");
        log.info("orderId:{}, myOrderId:{}", orderPo.getOrderId(), orderPo.getMyOrderId());
        String orderId = orderPo.getOrderId();
        requestJson.put("orderid", orderPo.getMyOrderId());
        requestJson.put("transactionid", orderPo.getSupplierOrderId());
        requestJson.put("callbackurl",callbackCancelUrl);
        String requestStr = "jsonStr=" + requestJson.toJSONString();
        log.info("取消接口参数:{}", requestStr);
        String httpResult = new HttpUtil().doHttpPost(url, requestStr, 1000 * 30, false);
        log.info("取消接口响应结果:{}", httpResult);
        return httpResult;

    }


    @Override
    public String changeCancel(ServicePo servicePo) {
        JSONObject requestJson = createParameter("train_cancel_change");
        Integer changeId = servicePo.getChangeId();
        String orderId = servicePo.getOrderId();
        requestJson.put("orderid", servicePo.getMyOrderId());
        requestJson.put("transactionid", servicePo.getSupplierOrderId());
        requestJson.put("callbackurl", callbackChangeCancelUrl);
        requestJson.put("reqtoken", changeId.toString());
        String requestStr = "jsonStr=" + requestJson.toJSONString();
        log.info("改签取消接口参数:{}", requestStr);
        String httpResult = new HttpUtil().doHttpPost(url, requestStr, 1000 * 30, false);
        log.info("改签取消接口响应结果:{}", httpResult);
        return httpResult;
    }


    @Override
    public void bookOrder(BookOrderReq bookOrderReq) {
        JSONObject requestJson = createParameter("train_order");
        String trainCode = bookOrderReq.getTrainCode();
        String orderId = bookOrderReq.getOrderId();
        requestJson.put("orderid", bookOrderReq.getMyOrderId());
        requestJson.put("checi", trainCode);
        requestJson.put("from_station_code", bookOrderReq.getFromStationCode());
        requestJson.put("from_station_name", bookOrderReq.getFromStationName());
        requestJson.put("to_station_code", bookOrderReq.getToStationCode());
        requestJson.put("to_station_name", bookOrderReq.getToStationName());
        requestJson.put("train_date", DateUtil.format(bookOrderReq.getFromDate()));
        requestJson.put("callbackurl", callbackOrderUrl);
        requestJson.put("LoginUserName", bookOrderReq.getUsername());
        requestJson.put("LoginUserPassword", bookOrderReq.getPassword());

        String chooseSeat = bookOrderReq.getChooseSeats();
        if (StringUtils.isNotBlank(chooseSeat)) {
            requestJson.put("is_choose_seats", true);
            requestJson.put("choose_seats", chooseSeat);
        } else {
            requestJson.put("is_choose_seats", false);
            requestJson.put("choose_seats", "");
        }
        requestJson.put("is_accept_standing", bookOrderReq.isAcceptNoSeat());

        //乘客数据
        JSONArray passengersArray = new JSONArray();
        List<BookOrderTicketReq> tickets = bookOrderReq.getTickets();
        for (BookOrderTicketReq ticketReq : tickets) {
            JSONObject passengerJson = new JSONObject(true);
            passengerJson.put("passengersename", ticketReq.getName());
            passengerJson.put("passportseno", ticketReq.getCardNo());
            // ----获取证件类型进行转换
            CardType cardType = CardType.getByKy(ticketReq.getCardType());
            passengerJson.put("passporttypeseidname", cardType.getTitle());
            passengerJson.put("passporttypeseid", cardType.getKyfw());
            passengerJson.put("passengerid", ticketReq.getCpId());
            // ----获取坐席类型进行格式转换
            String seatType = ticketReq.getSeatType();
            if (seatType.equals("9")) {
                // 无座，判断 是 二等座 还是 硬座
                if (trainCode.startsWith("C") || trainCode.startsWith("D") || trainCode.startsWith("G")) {
                    passengerJson.put("zwname", SeatType.GAO_TIE_WU_ZUO.getTitle());
                    passengerJson.put("zwcode", SeatType.GAO_TIE_WU_ZUO.getKyfw());
                } else {
                    passengerJson.put("zwname", SeatType.QIT_TA_WU_ZUO.getTitle());
                    passengerJson.put("zwcode", SeatType.QIT_TA_WU_ZUO.getKyfw());
                }
            } else {
                passengerJson.put("zwname", SeatType.getKyfwByl9e(seatType).getTitle());
                passengerJson.put("zwcode", SeatType.getKyfwByl9e(seatType).getKyfw());
            }
            passengerJson.put("price", 0);
            // -----获取票类型进行转换-------
            TicketType ticketType = TicketType.getByKy(ticketReq.getTicketType());
            passengerJson.put("piaotype", ticketType.getKyfw());
            passengerJson.put("cxin", "");
            // 学生票
            if (ticketType.equals(TicketType.XUE_SHENG)) {
                passengerJson.put("province_name", ticketReq.getProvinceName());
                passengerJson.put("province_code", ticketReq.getProvinceName());
                passengerJson.put("school_code", ticketReq.getSchoolCode());
                passengerJson.put("school_name", ticketReq.getProvinceName());
                passengerJson.put("student_no", ticketReq.getStudentNo());
                passengerJson.put("school_system", ticketReq.getSystem());
                passengerJson.put("preference_from_station_name", ticketReq.getStuFromStationName());
                passengerJson.put("preference_from_station_code", ticketReq.getStuFromStationCode());
                passengerJson.put("preference_to_station_name", ticketReq.getStuToStationName());
                passengerJson.put("preference_to_station_code", ticketReq.getStuToStationCode());
                passengerJson.put("enter_year", ticketReq.getEnterYear());
            }
            passengersArray.add(passengerJson);
        }
        requestJson.put("passengers", passengersArray);
        String requestStr = "jsonStr=" + requestJson.toJSONString();
        //请求航天华有
        String httpResult = new HttpUtil().doHttpPost(url, requestStr, ASYNC, false);
        //解析响应结果
        if (StringUtils.isBlank(httpResult) || httpResult.startsWith("<") || httpResult.toLowerCase().equals("false") || httpResult.equals("EXCEPTION")) {
            log.info("请求航天华有预订占位，返回结果异常");
            historyService.insertBookLog(orderId, "请求航天华有预订占位，返回结果异常");
            orderService.updateStatusPre(orderId, OrderStatus.BOOK_ING, OrderStatus.BOOK_MANUAL);
            return;
        }

        JSONObject httpJson = JSONObject.parseObject(httpResult);
        Boolean success = httpJson.getBoolean("success");
        String code = httpJson.getString("code");
        String msg = httpJson.getString("msg");
        String supplierOrderId = httpJson.getString("orderid");

        if ("802".equals(code) && success) {
            historyService.insertBookLog(orderId, "航天华有占位下单成功,等待回调结果");
            OrderPo update = new OrderPo();
            update.setSupplierOrderId(supplierOrderId);
            update.setCanSwitchSupplier(false);
            orderService.updateByOrderId(update, orderId, OrderStatus.BOOK_ING);
        } else {
            historyService.insertBookLog(orderId, "航天华有占位下单失败,Msg:" + msg);
            orderService.updateStatusPre(orderId, OrderStatus.BOOK_ING, OrderStatus.BOOK_MANUAL);
        }
    }



    @Override
    public void changeOrder(ChangeOrderReq changeDto) {
        JSONObject requestJson = createParameter("train_request_change");
        requestJson.put("orderid", changeDto.getMyOrderId());
        requestJson.put("transactionid", changeDto.getSupplierOrderId());
        requestJson.put("ordernumber", changeDto.getSequence());
        requestJson.put("reqtoken", String.format("%s_%s", changeDto.getChangeId(), System.currentTimeMillis()));
        requestJson.put("callbackurl", callbackChangeOrderUrl);
        requestJson.put("LoginUserName", changeDto.getUsername());
        requestJson.put("LoginUserPassword", changeDto.getPassword());
        // 是否变更到站，true/false
        requestJson.put("isTs", changeDto.getIsChangeTo());
        String trainCode = changeDto.getTrainCode();
        requestJson.put("change_checi", trainCode);
        // 改签发车时间，例：yyyy-MM-dd HH:mm:ss
        requestJson.put("change_datetime", DateUtil.format(changeDto.getFromDate(), DateFormat.DATE_HMS));
        // 出发站简码(非必选)
        requestJson.put("from_station_code", changeDto.getFromStationCode());
        // 出发站名称
        requestJson.put("from_station_name", changeDto.getFromStationName());
        // 到达站简码(非必选)
        requestJson.put("to_station_code", changeDto.getToStationCode());
        // 到达站名称
        requestJson.put("to_station_name", changeDto.getToStationName());
        // 异步改签，固定值（Y）
        requestJson.put("isasync", "Y");

        // 乘客信息
        String oldSeatType = "";
        String newSeatType = "";
        JSONArray passengerList = new JSONArray();
        List<ChangeOrderTicketReq> tickets = changeDto.getTickets();
        for (int i = 0; i < tickets.size(); i++) {
            ChangeOrderTicketReq ticketDto = tickets.get(i);
            JSONObject passenger = new JSONObject(true);
            passenger.put("passengersename", ticketDto.getName());
            passenger.put("passportseno", ticketDto.getCardNo());
            CardType cardType = CardType.getByKy(ticketDto.getCardType());
            passenger.put("passporttypeseid", cardType.getKyfw());
            passenger.put("piaotype", TicketType.getByKy(ticketDto.getTicketType()).getKyfw());
            passenger.put("old_ticket_no", ticketDto.getOldSubSequence());
            String newSeat = ticketDto.getChangeSeatType();
            String seat = ticketDto.getSeatType();
            newSeatType = wuZuo2Resources(trainCode, newSeat);
            oldSeatType = wuZuo2Resources(trainCode, seat);
            passengerList.add(passenger);
        }
        requestJson.put("change_zwcode", newSeatType);
        requestJson.put("old_zwcode", oldSeatType);
        requestJson.put("ticketinfo", passengerList);
        String requestStr = "jsonStr=" + requestJson.toJSONString();
        String httpResult = new HttpUtil().doHttpPost(url, requestStr, ASYNC, false);

        String orderId = changeDto.getOrderId();
        Integer changeId = changeDto.getChangeId();
        if (StringUtils.isBlank(httpResult) || httpResult.startsWith("<") || httpResult.toLowerCase().equals("false") || httpResult.equals("EXCEPTION")) {
            log.info("请求航天华有改签，返回结果异常");
            historyService.insertChangeLog(orderId, changeId, "请求航天华有改签，返回结果异常");
            changeService.updateStatusPre(changeId, ChangeStatus.CHANGE_ING, ChangeStatus.CHANGE_MANAUL);
            return;
        }

        JSONObject httpJson = JSON.parseObject(httpResult);
        String msg = httpJson.getString("msg");
        Boolean success = httpJson.getBoolean("success");
        if (success) {
            log.info("请求航天华有改签成功，等待回调结果");
            historyService.insertChangeLog(orderId, changeId, "请求航天华有改签成功，等待回调结果");
        } else {
            log.info("请求航天华有改签失败，重置订单状态为人工改签");
            historyService.insertChangeLog(orderId, changeId, String.format("请求航天华有改签失败,Msg:%s", msg));
            changeService.updateStatusPre(changeId, ChangeStatus.CHANGE_ING, ChangeStatus.CHANGE_MANAUL);
        }
    }

    @Override
    public HthySyncDto refund(RefundReq refundReq) {
        JSONObject requestJson = createParameter("return_ticket");
        requestJson.put("orderid", refundReq.getMyOrderId());
        requestJson.put("transactionid", refundReq.getSupperOrderId());
        requestJson.put("ordernumber", refundReq.getSequence());
        requestJson.put("reqtoken", refundReq.getCpId());
        requestJson.put("callbackurl", callbackRefundUrl);
        String username = refundReq.getUsername();
        if (StringUtils.isNotBlank(username)) {
            requestJson.put("LoginUserName", username);
            requestJson.put("LoginUserPassword", refundReq.getPassword());
        } else {
            requestJson.put("LoginUserName", "");
            requestJson.put("LoginUserPassword", "");
        }
        JSONObject passengerJson = new JSONObject(true);
        passengerJson.put("passengername", refundReq.getName());
        passengerJson.put("ticket_no", refundReq.getSubSequence());
        passengerJson.put("passportseno", refundReq.getCardNo());
        passengerJson.put("passporttypeseid", CardType.getByKy(refundReq.getCardType()).getKyfw());

        requestJson.put("tickets", Lists.newArrayList(passengerJson));
        String requestStr = "jsonStr=" + requestJson.toJSONString();
        //{"code":"118","msg":"票号[E205279954102010B]对应的车票状态变化，暂不可退","success":false}
        //{"reqtoken":"40006540195905478","ordernumber":"E032931310","orderid":"ky18110831936951","code":"802","msg":"退票请求已接收","success":true,"tooltip":"退票请求已接收，正在处理"}
        String httpResult = new HttpUtil().doHttpPost(url, requestStr, 1000 * 30, false);
        log.info("退票接口响应结果:{}", httpResult);
        //解析响应结果
        if (StringUtils.isBlank(httpResult) || httpResult.startsWith("<") || httpResult.toLowerCase().equals("false") || httpResult.equals("EXCEPTION")) {
            return HthySyncDto.builder().success(false).message("调用航天华有退票异常").build();
        }
        JSONObject json = JSON.parseObject(httpResult);
        return HthySyncDto.builder().success(json.getBoolean("success")).message(json.getString("msg")).build();
    }


    /**
     * 将无座还原
     *
     * @param trainCode
     * @param newSeat
     * @return
     */
    private String wuZuo2Resources(String trainCode, String newSeat) {
        String newSeatType;
        if (newSeat.equals("9")) {
            // 无座，判断 是 二等座 还是 硬座
            if (trainCode.startsWith("C") || trainCode.startsWith("D") || trainCode.startsWith("G")) {
                newSeatType = SeatType.GAO_TIE_WU_ZUO.getKyfw();
            } else {
                newSeatType = SeatType.QIT_TA_WU_ZUO.getKyfw();
            }
        } else {
            newSeatType = SeatType.getKyfwByl9e(newSeat).getKyfw();
        }
        return newSeatType;
    }

    /**
     * 组装基本参数
     *
     * @param method
     * @return
     */
    private JSONObject createParameter(String method) {
        JSONObject requestJson = new JSONObject(true);
        requestJson.put("partnerid", partnerId);
        requestJson.put("method", method);
        // 获取当前时间戳
        String reqTimes = DateUtil.format(DateFormat.DATE_HMS2);
        requestJson.put("reqtime", reqTimes);
        // 签名加密
        String sign = MD5Util.md5(partnerId + method + reqTimes + MD5Util.md5(key).toLowerCase()).toLowerCase();
        requestJson.put("sign", sign);
        return requestJson;
    }


    @Override
    public TicketInfoDto queryTicket(TicketReq ticketReq) {
        JSONObject requestJson = createParameter("train_query");
        requestJson.put("train_date", ticketReq.getFromDate());
        requestJson.put("from_station", ticketReq.getFromStationCode());
        requestJson.put("to_station", ticketReq.getToStationCode());
        requestJson.put("purpose_codes", ticketReq.getTicketType());
        requestJson.put("need_train_type", "");
        String requestStr = "jsonStr=" + requestJson.toJSONString();
        String httpResult = new HttpUtil().doHttpPost(url, requestStr, 1000 * 10, false);
        HthyTicketInfoResp infoResp = JSON.parseObject(httpResult, HthyTicketInfoResp.class);

        TicketInfoDto.TicketInfoDtoBuilder infoDtoBuilder = TicketInfoDto.builder();
        //将航天华有 数据转成 19e 数据
        if (infoResp.isSuccess()) {
            List<TicketDetailDto> list = Lists.newArrayList();
            //车票数据
            List<HthyTicketDetailResp> dataList = infoResp.getData();
            for (HthyTicketDetailResp hthy : dataList) {
                TicketDetailDto detailDto = new TicketDetailDto();
                BeanUtils.copyProperties(hthy, detailDto);
                list.add(detailDto);
            }
            infoDtoBuilder.code("000").message(infoResp.getMsg()).tickets(list);
        } else {
            infoDtoBuilder.code("001").message(infoResp.getMsg()).tickets(Lists.<TicketDetailDto>newArrayList());
        }
        return infoDtoBuilder.build();
    }


}
