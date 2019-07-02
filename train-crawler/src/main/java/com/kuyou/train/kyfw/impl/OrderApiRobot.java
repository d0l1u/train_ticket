package com.kuyou.train.kyfw.impl;

import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.entity.kyfw.RequestData;
import com.kuyou.train.kyfw.api.OrderApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * OrderApiRobot
 *
 * @author taokai3
 * @date 2018/11/19
 */
@Slf4j
@Service
public class OrderApiRobot {

    @Resource
    private OrderApi orderApi;

    public void cancelQueueOrder(boolean isBook) throws IOException {
        RequestData data = orderApi.cancelQueueNoCompleteMyOrder(isBook ? "dc" : "gc").execute().body().getData();
        log.info("取消排队订单 cancelQueueNoCompleteMyOrder data:{}", data);
        if ("Y".equals(data.getExistError())) {
            throw new TrainException(data.getErrorMsg());
        }
    }

    public void cancelOrder(String sequence) throws IOException {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("sequence_no", sequence);
        map.put("cancel_flag", "cancel_order");
        RequestData data = orderApi.cancelNoCompleteMyOrder(map).execute().body().getData();
        log.info("取消订单 cancelNoCompleteMyOrder data:{}", data);
        if ("Y".equals(data.getExistError())) {
            throw new TrainException(data.getErrorMsg());
        }
    }
}
