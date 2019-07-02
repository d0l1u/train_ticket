package com.kuyou.train.common.util;

import com.kuyou.train.entity.dto.TrainTimeDto;

/**
 * TicketUtil
 *
 * @author taokai3
 * @date 2018/11/15
 */
public class TrainTimeUtil {

    private static final ThreadLocal<TrainTimeDto> LOCAL = new ThreadLocal<>();


    public static void set(TrainTimeDto trainTimeDto) {
        LOCAL.set(trainTimeDto);
    }

    public static TrainTimeDto get() {
        TrainTimeDto trainTimeDto = LOCAL.get();
        LOCAL.remove();
        return trainTimeDto == null ? new TrainTimeDto() : trainTimeDto;
    }

}
