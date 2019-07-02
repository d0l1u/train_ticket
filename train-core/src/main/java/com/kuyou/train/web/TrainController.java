package com.kuyou.train.web;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.enums.LeftTicketEnum;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.entity.req.TicketReq;
import com.kuyou.train.service.HthyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * TrainController
 *
 * @author taokai3
 * @date 2018/12/13
 */
@Slf4j
@Controller
@RequestMapping("/core")
public class TrainController {

    @Resource
    private JedisClient dataJedisClient;

    @Resource
    private HthyService hthyService;

    @RequestMapping(value = "/queryTicket4Hthy")
    @ResponseBody
    public String queryTicket4Hthy(@RequestParam("fromStationName") String fromStationName,
            @RequestParam("toStationName") String toStationName, @RequestParam("fromDate") String fromDate) {
        TicketReq ticketReq = TicketReq.builder().fromDate(fromDate).toStationName(toStationName).fromStationName(fromStationName)
                .ticketType(LeftTicketEnum.ADULT.getValue()).build();
        log.info("航天华有查询余票入参:{}", ticketReq);

        //根据name 获取code
        String fromStationCode = dataJedisClient.hget(KeyConstant.STATION_NAME2CODE, fromStationName);
        String toStationCode = dataJedisClient.hget(KeyConstant.STATION_NAME2CODE, toStationName);
        ticketReq.setFromStationCode(fromStationCode);
        ticketReq.setToStationCode(toStationCode);
        return JSON.toJSONString(hthyService.queryTicket(ticketReq));
    }
}
