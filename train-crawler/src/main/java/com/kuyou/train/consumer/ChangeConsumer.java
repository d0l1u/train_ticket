package com.kuyou.train.consumer;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.common.enums.*;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.util.IpUtil;
import com.kuyou.train.entity.req.ChangeReq;
import com.kuyou.train.entity.req.ChangeTicketReq;
import com.kuyou.train.entity.resp.ChangeResp;
import com.kuyou.train.http.cookie.CacheCookieJar;
import com.kuyou.train.service.ChangeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ChangeOrderJob:
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Slf4j
@Component("changeConsumer")
public class ChangeConsumer {

    @Resource
    private JedisClient orderJedisClient;

    @Resource
    private ChangeService changeOrderService;

    @MDCLog
    @Scheduled(cron = "* * 5-23 * * ?")
    public void consumer() {
        String rpopResult = orderJedisClient.rpop(KeyEnum.CHANGE_REQ.getValue());
        if (StringUtils.isBlank(rpopResult)) {
            return;
        }
        log.info("rpopResult:{}", rpopResult);

        ChangeReq changeReq = JSON.parseObject(rpopResult, ChangeReq.class);

        //将19e参数转成12306参数
        List<ChangeTicketReq> collect = changeReq.getTickets().stream().map(ticket -> {
            String ticketType = ticket.getTicketType();
            String changeSeatType = ticket.getChangeSeatType();
            String cardType = ticket.getCardType();
            ChangeTicketReq ticketReq = new ChangeTicketReq();
            BeanUtils.copyProperties(ticket, ticketReq);
            ticketReq.setTicketType(PassengerTypeEnum.getByKy(ticketType).getKyfw());
            ticketReq.setChangeSeatType(SeatTypeEnum.getByKy(changeSeatType).getKyfw());
            ticketReq.setCardType(CardTypeEnum.getByKy(cardType).getKyfw());
            return ticketReq;
        }).collect(Collectors.toList());
        changeReq.setTickets(collect);

        ChangeResp changeResp = ChangeResp.builder().changeId(changeReq.getChangeId())
                .orderId(changeReq.getOrderId()).sequence(changeReq.getSequence()).build();
        try {
            changeResp = changeOrderService.changeOrder(changeReq);
            changeResp.setCode(CodeEnum.SUCCESS.getCode());
            changeResp.setMessage(CodeEnum.SUCCESS.getMessage());
        } catch (TrainException e) {
            //解析错误信息
            String message = e.getMessage();
            CodeEnum codeEnum = KeyWordEnum.getCodeByKeyword(message);
            log.info("关键字匹配，CodeEnum:{}, message:{}", codeEnum, message);
            changeResp.setCode(codeEnum.getCode());
            changeResp.setMessage(message);
        } catch (Exception e) {
            //系统异常
            String simpleName = e.getClass().getSimpleName();
            log.info("改签占位发生异常:{}", simpleName, e);
            changeResp.setCode(CodeEnum.SYSTEM_EXCEPTION.getCode());
            changeResp.setMessage(CodeEnum.SYSTEM_EXCEPTION.getMessage() + ":" + simpleName);
        } finally {
            new CacheCookieJar().clearCookie();
            changeResp.setRobotIp(IpUtil.getInnetIp());
            changeResp.setFromDate(changeReq.getFromDate());
            log.info("改签占位结果:{}", changeResp);
            //推送到redis
            Long lpush = orderJedisClient.lpush(KeyEnum.CHANGE_RESP.getValue(), changeResp.toString());
            log.info("改签占位结果LPUSH:{}", lpush);
        }
    }

}
