package com.kuyou.train.service;

import com.google.common.collect.Lists;
import com.kuyou.train.common.enums.OrderStatusEnum;
import com.kuyou.train.common.enums.SeatTypeEnum;
import com.kuyou.train.common.enums.TicketTypeEnum;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.util.BigDecimalUtil;
import com.kuyou.train.common.util.TrainTimeUtil;
import com.kuyou.train.entity.dto.*;
import com.kuyou.train.entity.kyfw.CheckOrderInfoData;
import com.kuyou.train.entity.req.ChangeReq;
import com.kuyou.train.entity.req.ChangeTicketReq;
import com.kuyou.train.entity.resp.ChangeResp;
import com.kuyou.train.entity.resp.ChangeTicketResp;
import com.kuyou.train.kyfw.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ChangeOrderService
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Slf4j
@Service
public class ChangeService {

    @Resource
    private LoginApiRobot loginApiRobot;

    @Resource
    private QueryOrderApiRobot queryOrderApiRobot;

    @Resource
    private RequestOrderApiRobot requestOrderApiRobot;

    @Resource
    private LeftTicketApiRobot leftTicketApiRobot;

    @Resource
    private ConfirmApiRobot confirmApiRobot;

    @Resource
    private OrderApiRobot orderApiRobot;

    @Resource
    private CaptchaApiRobot captchaApiRobot;

    /**
     * 进行改签
     *
     * @param changeReq
     */
    public ChangeResp changeOrder(ChangeReq changeReq) throws IOException {
        Integer changeId = changeReq.getChangeId();
        String orderId = changeReq.getOrderId();
        String username = changeReq.getUsername();
        String password = changeReq.getPassword();
        String sequence = changeReq.getSequence();
        List<ChangeTicketReq> tickets = changeReq.getTickets();
        log.info("changeId:{}, orderId:{}, sequence:{}, username:{}, password:{}", changeId, orderId, sequence,
                username, password);

        //检查登录状态，如果redis存在登录cookie，则判断cookie是否失效，如果失效，则重新登录
        loginApiRobot.checkLoginStatus(username, password);

        Boolean isChangeTo = changeReq.getIsChangeTo();
        OrderStatusEnum status = OrderStatusEnum.CHANGE_WAIT_PAY;
        if (isChangeTo) {
            status = OrderStatusEnum.ALTER_WAIT_PAY;
        }

        String trainInfo = changeReq.trainInfo();
        List<String> ticketInfo = changeReq.ticketInfo();
        //查询未完成订单
        NoCompleteOrderDto orderDto = queryOrderApiRobot.queryNoComplete(sequence, trainInfo, ticketInfo, Lists.newArrayList(status));
        if (orderDto != null) {
            if (OrderStatusEnum.WAIT_ING.equals(orderDto.getStatusEnum())) {
                //取消排队订单
                try {
                    orderApiRobot.cancelQueueOrder(false);
                } catch (Exception e) {
                    log.info("取消排队订单异常", e);
                }
            } else {
                //解析未完成订单结果
                return parseOrder(changeReq, orderDto);
            }
        }

        //判断是否是学生票
        final TicketTypeEnum[] ticketTypeEnum = {TicketTypeEnum.STUDENT};
        //提取乘客票号
        List<String> subList = tickets.stream().map(ro -> {
            if (!TicketTypeEnum.STUDENT.getValue().equals(ro.getTicketType())) {
                ticketTypeEnum[0] = TicketTypeEnum.ADULT;
            }
            return ro.getOldSubSequence();
        }).collect(Collectors.toList());

        //查询已完成订单，预期状态‘已支付’
        List<CompleteOrderDto> completeOrders = queryOrderApiRobot.queryNotTravelOrder(sequence, subList);
        for (CompleteOrderDto dto : completeOrders) {
            OrderStatusEnum statusEnum = dto.getStatusEnum();
            if (OrderStatusEnum.ALTER_FINISH.equals(statusEnum) || OrderStatusEnum.CHANGE_FINISH.equals(statusEnum)) {
                throw new TrainException(dto.getSubSequence() + " 车票已改签");
            }
            if (OrderStatusEnum.ALTER_ING.equals(statusEnum) || OrderStatusEnum.CHANGE_ING.equals(statusEnum)) {
                throw new TrainException(dto.getSubSequence() + " 车票改签中");
            }
            if (OrderStatusEnum.REFUND_FINISH.equals(statusEnum)) {
                throw new TrainException(dto.getSubSequence() + " 车票已退票");
            }
            if (!OrderStatusEnum.PAY_FINISH.equals(statusEnum)) {
                throw new TrainException(dto.getSubSequence() + " 车票状态不可以改签:" + dto.getStatusName());
            }
        }

        //申请改签
        requestOrderApiRobot.resginTicket(sequence, completeOrders, isChangeTo);

        //查询余票
        log.info("查询余票类型:{}", ticketTypeEnum[0]);
        Date fromDate = changeReq.getFromDate();
        TrainTicketDto trainTicketDto = leftTicketApiRobot.queryTicket4Order(false, changeReq.getTrainCode(), fromDate,
                        changeReq.getFromStationCode(), changeReq.getToStationCode(), ticketTypeEnum[0]);
        trainTicketDto.setFromStationName(changeReq.getFromStationName());
        trainTicketDto.setToStationName(changeReq.getToStationName());

        //根据出发站到达站简码，获取站名


        //判断，该车次是否可以预订无座票
        String changeSeatType = tickets.get(0).getChangeSeatType();
        log.info("本次占位坐席:{}", changeSeatType);
        if (SeatTypeEnum.WU_ZUO.getKyfw().equals(changeSeatType)) {
            //将无座 还原成对应的坐席
            changeSeatType = trainTicketDto.getWuZuoSeat().getKyfw();
        }

        //缓存出发时间到本地
        TrainTimeUtil.set(trainTicketDto.getTrainTime());

        //点击预订按钮
        requestOrderApiRobot.submitOrderRequest(fromDate, completeOrders.get(0).getFromTime(), trainTicketDto, false);

        //填单初始页面
        ConfirmDto confirmDto = confirmApiRobot.initXc(false);

        //设置 乘客字符串
        StringBuffer passengerTicketStrBf = new StringBuffer();
        StringBuffer oldPassengerStrBf = new StringBuffer();
        String finalChangeSeatType = changeSeatType;
        tickets.forEach(ticket -> {
            passengerTicketStrBf.append(ticket.getPassengerTicketStr(finalChangeSeatType));
            oldPassengerStrBf.append(ticket.getOldPassengerStr());
        });
        String passengerTicketStr = passengerTicketStrBf.toString();
        passengerTicketStr = passengerTicketStr.substring(0, passengerTicketStr.length() - 1);
        String oldPassengerStr = oldPassengerStrBf.toString();

        String isAsync = confirmDto.getIsAsync();
        log.info("下单模式:{}", isAsync);
        if ("1".equals(isAsync)) {
            confirmApiRobot.confirmOrder1(changeReq.getFromDate(), changeSeatType, tickets.size(),
                    changeReq.getChooseSeats(), confirmDto, passengerTicketStr, oldPassengerStr, false);
        } else {
            confirmApiRobot.confirmOrder0(changeSeatType, changeReq.getChooseSeats(), confirmDto, passengerTicketStr,
                    oldPassengerStr, false);
        }

        //反查获取订单信息
        orderDto = queryOrderApiRobot.queryNoCompleteBack(sequence, trainInfo, ticketInfo, status);
        //判断订单结果
        String statusCode = orderDto.getStatus();
        if (OrderStatusEnum.BOOK_SEAT_FAIL.getCode().equals(statusCode)) {
            throw new TrainException(orderDto.getMessage());
        }

        //解析参数
        return parseOrder(changeReq, orderDto);
    }

    /**
     * 解析订单
     * @param changeReq
     * @param orderDto
     * @return
     */
    private ChangeResp parseOrder(ChangeReq changeReq, NoCompleteOrderDto orderDto) {
        //计算原票总价
        List<ChangeTicketReq> sTickets = changeReq.getTickets();
        int newTotalPrice = orderDto.getTotalPrice();
        final BigDecimal[] priceArr = {new BigDecimal(0)};

        //新单转map
        Map<String, List<OrderTicketDto>> listMap = orderDto.getTickets().stream().collect(Collectors
                .toMap(OrderTicketDto::getKey, ticket -> Lists.newArrayList(ticket),
                        (List<OrderTicketDto> newValueList, List<OrderTicketDto> oldValueList) -> {
                            newValueList.addAll(oldValueList);
                            return newValueList;
                        }));

        List<ChangeTicketResp> ticketResps = sTickets.stream().map(ticket -> {
            priceArr[0] = priceArr[0].add(ticket.getPrice());
            log.info("ticket.getKey():{}", ticket.getKey());
            OrderTicketDto dto = listMap.get(ticket.getKey()).remove(0);
            return ChangeTicketResp.builder().cpId(ticket.getCpId()).name(ticket.getName())
                    .oldSubSequence(ticket.getOldSubSequence()).ticketType(ticket.getTicketType())
                    .price(BigDecimalUtil.dividePrice(dto.getPrice()))
                    .newSubSequence(dto.getSubSequence()).coachNo(dto.getCoachNo()).coachName(dto.getCoachName())
                    .seatName(dto.getSeatName()).seatNo(dto.getSeatNo()).build();
        }).collect(Collectors.toList());

        //判断改签类型
        int totalPrice = priceArr[0].multiply(new BigDecimal(100)).intValue();
        int changeType = 0;
        if (totalPrice - newTotalPrice > 0) {
            changeType = 1;
        } else if (totalPrice - newTotalPrice < 0) {
            changeType = -1;
        }
        log.info("改签类型:{}[1:高->低，0:平价，-1:低->高]", changeType);

        //获取出发到达时间
        TrainTimeDto timeDto = TrainTimeUtil.get();
        Date fromTime = timeDto.getFromTime();
        if (fromTime == null) {
            fromTime = orderDto.getFromTime();
        }

        //订单信息
        return ChangeResp.builder().tickets(ticketResps).changeId(changeReq.getChangeId())
                .orderId(changeReq.getOrderId()).toTime(timeDto.getToTime()).fromTime(fromTime)
                .payLimitTime(orderDto.getPayLimitTime()).sequence(orderDto.getSequence()).changeType(changeType)
                .totalPrice(BigDecimalUtil.dividePrice(newTotalPrice))
                .fromStationName(orderDto.getFromStationName()).fromStationCode(orderDto.getFromStationCode())
                .toStationName(orderDto.getToStationName()).toStationCode(orderDto.getToStationCode()).build();
    }
}
