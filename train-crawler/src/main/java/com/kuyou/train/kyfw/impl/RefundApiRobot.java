package com.kuyou.train.kyfw.impl;

import com.alibaba.fastjson.JSONObject;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.util.DateFormat;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.common.util.RegexUtil;
import com.kuyou.train.entity.dto.CompleteOrderDto;
import com.kuyou.train.entity.dto.RefundInfoDto;
import com.kuyou.train.entity.kyfw.RefundInfoData;
import com.kuyou.train.entity.kyfw.ReturnTicketData;
import com.kuyou.train.entity.kyfw.refund.RefundInfo;
import com.kuyou.train.kyfw.api.RefundApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * RefundApiRobot
 *
 * @author taokai3
 * @date 2018/11/22
 */
@Slf4j
@Service
public class RefundApiRobot {

    private static final Pattern TRADE_NO = Pattern.compile("业务流水号：[^>]+>([0-9a-zA-Z]+)</span>");

    @Resource
    private RefundApi refundApi;

    /**
     * 获取退票信息
     *
     * @param tradeNo
     * @param fromDate
     * @return
     */
    public RefundInfo queryRefundInfo(String tradeNo, Date fromDate) throws IOException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("trade_no", tradeNo);
        map.put("trade_mode", "1");
        map.put("query_flag", "2");
        map.put("start_time", DateUtil.format(fromDate, DateFormat.DATE_HMS2));
        Date incr = DateUtil.incr(fromDate, 3, TimeUnit.DAYS);
        incr = DateUtil.incr(incr, -1, TimeUnit.SECONDS);
        map.put("stop_time", DateUtil.format(incr, DateFormat.DATE_HMS2));
        RefundInfoData data = refundApi.queryRefundInfo(map).execute().body().getData();
        log.info("查询退票信息 queryRefundInfo data:{}", data);
        return data.getData();
    }

    public RefundInfoDto returnTicketAffirm(CompleteOrderDto orderDto) throws IOException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("sequence_no", orderDto.getSequence());
        map.put("batch_no", orderDto.getBatchNo());
        map.put("coach_no", orderDto.getCoachNo());
        map.put("seat_no", orderDto.getSeatNo());
        map.put("start_train_date_page", DateUtil.format(orderDto.getFromTime(), DateFormat.DATE_HM));
        map.put("train_code", orderDto.getTrainCode());
        map.put("coach_name", orderDto.getCoachName());
        map.put("seat_name", orderDto.getSeatName());
        map.put("seat_type_name", orderDto.getSeatTypeName());
        map.put("train_date", DateUtil.format(orderDto.getFromDate(), DateFormat.DATE_HMS));
        map.put("from_station_name", orderDto.getFromStationName());
        map.put("to_station_name", orderDto.getToStationName());
        map.put("start_time", "1970-01-01 " + DateUtil.format(orderDto.getFromTime(), DateFormat.TIME_HMS));
        map.put("passenger_name", orderDto.getName());
        map.put("from_station_telecode", orderDto.getFromStationCode());
        map.put("to_station_telecode", orderDto.getToStationCode());
        map.put("train_no", orderDto.getInnerTrainCode());
        map.put("id_type", orderDto.getIdcardType());
        map.put("id_no", orderDto.getIdcardNo());
        map.put("_json_att", "");
        ReturnTicketData data = refundApi.returnTicketAffirm(map).execute().body().getData();
        log.info("申请退票 returnTicketAffirm data:{}", data);
        if (!data.isSubmitStatus()) {
            throw new TrainException(data.getErrMsg());
        }

        return RefundInfoDto.builder().ticketPrice(data.getTicket_price()).returnPrice(data.getReturn_price())
                .returnCost(data.getReturn_cost()).returnRate(data.getReturn_rate()).build();
    }

    public void returnTicket() throws IOException {
        JSONObject data = refundApi.returnTicket().execute().body().getData();
        log.info("确认退票 returnTicket data:{}", data);
    }

    public String returnTicketRedirect() throws IOException {
        String page = refundApi.returnTicketRedirect().execute().body();
        String tradeNo = RegexUtil.matcher(page, TRADE_NO);
        log.info("退票流水号:{}", tradeNo);
        return tradeNo;
    }


    public static void main(String[] args) {
        String str = "ssss业务流水号：<span class=\"colorA\">2E032380677001001173416070</span>\n";
        System.err.println("TRADE_NO:" + RegexUtil.matcher(str, TRADE_NO));
    }
}
