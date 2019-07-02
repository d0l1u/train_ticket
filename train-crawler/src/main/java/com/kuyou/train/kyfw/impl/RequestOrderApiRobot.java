package com.kuyou.train.kyfw.impl;

import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.dto.CompleteOrderDto;
import com.kuyou.train.entity.dto.TrainTicketDto;
import com.kuyou.train.entity.kyfw.RequestData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.kyfw.api.RequestOrderApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * RequestOrderApiImpl
 *
 * @author taokai3
 * @date 2018/11/15
 */
@Slf4j
@Service
public class RequestOrderApiRobot {

    @Resource
    private RequestOrderApi requestOrderApi;

    public void resginTicket(String sequence, List<CompleteOrderDto> completeOrders, Boolean isChangeTo)
            throws IOException {
        StringBuffer sb = new StringBuffer();
        completeOrders.stream().forEach(dto -> sb.append(dto.getTicketKey(sequence)));

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("ticketkey", sb.toString());
        map.put("sequenceNo", sequence);
        map.put("changeTSFlag", isChangeTo ? "Y" : "N");
        map.put("_json_att", "");

        RequestData data = requestOrderApi.resginTicket(map).execute().body().getData();
        log.info("申请改签resginTicket data:{}", data);

        if ("Y".equals(data.getExistError())) {
            throw new TrainException(data.getErrorMsg());
        }
    }

    /**
     * 申请下单
     *
     * @param newDate
     * @param oldDate
     * @param trainTicketDto
     * @param isBook
     */
    public void submitOrderRequest(Date newDate, Date oldDate, TrainTicketDto trainTicketDto, boolean isBook)
            throws IOException {
        //组装参数
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("secretStr", URLDecoder.decode(trainTicketDto.getSecretStr(), "utf-8"));
        map.put("train_date", DateUtil.format(newDate));
        map.put("back_train_date", DateUtil.format(oldDate));
        map.put("tour_flag", isBook ? "dc" : "gc");
        map.put("purpose_codes", trainTicketDto.getTicketType().getValue());
        map.put("query_from_station_name", trainTicketDto.getFromStationName());
        map.put("query_to_station_name", trainTicketDto.getToStationName());
        map.put("undefined", "");

        //{"validateMessagesShowId":"_validatorMessage","status":false,"httpstatus":200,"messages":["该车次暂不办理业务"],"validateMessages":{}}
        //{"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":"N","messages":[],"validateMessages":{}}
        KyfwCommonResponse<String> body = requestOrderApi.submitOrderRequest(map).execute().body();
        log.info("申请下单 submitOrderRequest data:{}", body);
        String data = body.getData();

        if (!body.isStatus() || !"N".equals(data)) {
            if (body.getMessages().size() > 0) {
                throw new TrainException(body.getMessages().get(0));
            } else {
                log.info("申请下单，返回false，但是message为空");
            }
        }
    }
}
