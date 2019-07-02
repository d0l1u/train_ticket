package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuyou.train.common.enums.KeyWordEnum;
import com.kuyou.train.common.enums.SeatTypeEnum;
import com.kuyou.train.common.enums.TicketTypeEnum;
import com.kuyou.train.common.exception.TrainException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * TrainTicketDto
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainTicketDto {

    String trainCode;
    String secretStr;
    String message;
    String innerTrainCode;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date toTime;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date fromTime;

    private String leftTicket;
    private String trainLocation;

    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private TicketTypeEnum ticketType;
    private SeatTypeEnum wuZuoSeat;
    private transient String allSeat;

    /**
     * 高级软卧、高级动卧
     */
    private int gaoJi_RwOrDw;


    @JSONField(serialize = false)
    public TrainTimeDto getTrainTime() {
        return TrainTimeDto.builder().trainCode(trainCode).fromTime(fromTime).toTime(toTime)
                .fromStationName(fromStationName).toStationName(toStationName).build();
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public boolean canBuy() throws TrainException {
        if (!"预订".equals(message)) {
            if (message.contains("起售")) {
                throw new TrainException("未到起售时间");
            }
            throw new TrainException(message);
        }
        if (StringUtils.isBlank(secretStr)) {
            throw new TrainException("余票不足");
        }
        return true;
    }
}
