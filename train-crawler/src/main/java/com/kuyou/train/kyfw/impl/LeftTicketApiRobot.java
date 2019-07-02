package com.kuyou.train.kyfw.impl;

import com.kuyou.train.common.enums.KeyWordEnum;
import com.kuyou.train.common.enums.SeatTypeEnum;
import com.kuyou.train.common.enums.TicketTypeEnum;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.util.DateFormat;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.common.util.RegexUtil;
import com.kuyou.train.entity.dto.TrainTicketDto;
import com.kuyou.train.entity.kyfw.QueryTicketData;
import com.kuyou.train.kyfw.api.LeftTicketApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * LeftTicketApiRobot
 *
 * @author taokai3
 * @date 2018/11/15
 */
@Slf4j
@Service
public class LeftTicketApiRobot {

    private static final Pattern CLEFT_TICKET_URL = Pattern.compile("var CLeftTicketUrl = '([^']+)';");

    @Resource
    private LeftTicketApi leftTicketApi;

    /**
     * 预先查询
     */
    public void queryTicket4Beforehand() {

    }

    /**
     * 事后查询
     * @param isBook
     * @param trainCode
     * @param trainDate
     * @param fromStationCode
     * @param toStationCode
     * @param ticketType
     * @throws IOException
     */
    public TrainTicketDto queryTicket4Order(boolean isBook, String trainCode, Date trainDate, String fromStationCode,
            String toStationCode, TicketTypeEnum ticketType) throws IOException {
        String queryUrl = "";
        if (isBook) {
            queryUrl = init4Dc();
        } else {
            queryUrl = init4Gc();
        }
        Map<String, TrainTicketDto> queryMap = null;
        //查询车次信息
        try {
            queryMap = query(queryUrl, trainDate, fromStationCode, toStationCode, ticketType);
        }catch (Exception e){
            log.info("查询余票失败", e);
            throw new TrainException("查询余票失败");
        }
        if (queryMap == null) {
            throw new TrainException("查询余票结果为空");
        }

        TrainTicketDto trainTicketDto = queryMap.get(trainCode);
        if (trainTicketDto == null) {
            throw new TrainException("车次不存在");
        }

        //判断是否可以购票
        trainTicketDto.canBuy();

        //计算无座
        String allSeat = trainTicketDto.getAllSeat();
        log.info("{}可选所有坐席:{}", trainTicketDto.getTrainCode(), allSeat);
        SeatTypeEnum wuZuoSources = SeatTypeEnum.getWuZuoSources(allSeat);
        log.info("无座对应坐席:{}", wuZuoSources);
        trainTicketDto.setWuZuoSeat(wuZuoSources);
        trainTicketDto.setTicketType(ticketType);
        return trainTicketDto;
    }

    /**
     * 预订占位查询余票
     *
     * @return
     * @throws IOException
     */
    private String init4Dc() throws IOException {
        String initPage = leftTicketApi.init4Dc("dc").execute().body();
        //解析出，查询路径
        String queryUrl = RegexUtil.matcher(initPage, CLEFT_TICKET_URL);
        log.info("12306查询余票路径:{}", queryUrl);
        return queryUrl;
    }

    /**
     * 查询余票
     *
     * @param queryUrl
     * @param trainDate
     * @param fromStationCode
     * @param toStationCode
     * @param ticketTypeEnum
     */
    private Map<String, TrainTicketDto> query(String queryUrl, Date trainDate, String fromStationCode,
            String toStationCode, TicketTypeEnum ticketTypeEnum) throws IOException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("leftTicketDTO.train_date", DateUtil.format(trainDate, DateFormat.DATE));
        map.put("leftTicketDTO.from_station", fromStationCode);
        map.put("leftTicketDTO.to_station", toStationCode);
        map.put("purpose_codes", ticketTypeEnum.getValue());

        QueryTicketData data = leftTicketApi.query("/otn/" + queryUrl, map).execute().body().getData();
        //log.info("查询余票leftTicket/query data:{}", data);

        List<String> result = data.getResult();
        if (result == null || result.isEmpty()) {
            return null;
        }

        //格式化result
        List<TrainTicketDto> list = result.stream().map(message -> {
            int length = message.split("\\|").length;
            if (length == 20) {
                message = "$" + message + "$";
                message = message.replaceAll("\\|\\|", "\\|\\$\\|");
                message = message.replaceAll("\\|\\|", "\\|\\$\\|");
            }
            String[] arr = message.split("\\|");

            /*
            高级软/动卧:21
            其他:包厢硬卧/一人软包:22
            软 卧:23
            软座:24
            特等座:25
            无 座:26
            硬 卧:28
            硬 座:29
            二等座:30
            一等座:31
            商务座:32
            动 卧:33
            */
            //[35:OM9]
            Date fromTime = DateUtil.incr(trainDate, DateUtil.mathMinutes(arr[8]), TimeUnit.MINUTES);
            Date toTime = DateUtil.incr(fromTime, DateUtil.mathMinutes(arr[10]), TimeUnit.MINUTES);
            return TrainTicketDto.builder().secretStr(arr[0]).message(arr[1]).innerTrainCode(arr[2]).trainCode(arr[3])
                    .fromTime(fromTime).toTime(toTime).fromStationCode(arr[6]).toStationCode(arr[7]).leftTicket(arr[12])
                    .trainLocation(arr[15]).allSeat(arr[35]).build();
        }).collect(Collectors.toList());

        //将 list 转成map， key：车次， value:this
        return list.stream().collect(Collectors.toMap(TrainTicketDto::getTrainCode, Function.identity(),(oldValue,
                newValue) -> oldValue));
    }


    /**
     * 改签init
     *
     * @return
     * @throws IOException
     */
    private String init4Gc() throws IOException {
        String initPage = leftTicketApi.init4Gc("", "gcInit").execute().body();
        //解析出，查询路径
        String queryUrl = RegexUtil.matcher(initPage, CLEFT_TICKET_URL);
        log.info("12306查询余票路径:{}", queryUrl);
        return queryUrl;
    }


    private int left2Int(String numStr) {
        if (StringUtils.isBlank(numStr)) {
            return -1;
        } else if ("有".equals(numStr)) {
            return 99;
        } else if ("无".equals(numStr)) {
            return 0;
        } else if ("-".equals(numStr)) {
            return -1;
        } else {
            return Integer.valueOf(numStr);
        }
    }
}
