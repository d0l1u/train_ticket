package com.kuyou.train.kyfw.impl;

import com.kuyou.train.common.code.Message;
import com.kuyou.train.common.enums.KeyWordEnum;
import com.kuyou.train.common.enums.OrderStatusEnum;
import com.kuyou.train.common.enums.PassengerTypeEnum;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.util.DateFormat;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.dto.CompleteOrderDto;
import com.kuyou.train.entity.dto.NoCompleteOrderDto;
import com.kuyou.train.entity.dto.OrderTicketDto;
import com.kuyou.train.entity.kyfw.CompleteOrderData;
import com.kuyou.train.entity.kyfw.NoCompleteOrderData;
import com.kuyou.train.entity.kyfw.order.OrderCacheDto;
import com.kuyou.train.entity.kyfw.order.OrderDbDto;
import com.kuyou.train.entity.kyfw.order.OrderDbTicketDto;
import com.kuyou.train.kyfw.api.QueryOrderApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * QueryOrderApiImpl
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Slf4j
@Service
public class QueryOrderApiRobot {

    @Value("${back.no.order.max}")
    private int noOrderMax;

    @Value("${back.no.order.sleep}")
    private int noOrderSleep;

    @Resource
    private QueryOrderApi queryOrderApi;

    /**
     * 改签查询未完成订单
     * @param statusEnums：预期订单状态
     * @return
     * @throws IOException
     */
    public NoCompleteOrderDto queryNoComplete(String sequence, String trainInfoReq, List<String> ticketInfoReq,
            List<OrderStatusEnum> statusEnums) throws IOException {
        NoCompleteOrderDto noCompleteOrderDto = queryMyOrderNoComplete(false);
        if (noCompleteOrderDto == null) {
            return null;
        }
        OrderStatusEnum status = noCompleteOrderDto.getStatusEnum();

        //判断车次信息是否一致
        String message = equalOrder(noCompleteOrderDto, trainInfoReq, ticketInfoReq);
        //错误订单
        if (OrderStatusEnum.BOOK_SEAT_FAIL.equals(status)) {
            log.info("前置查询，忽略所有错误订单");
            return null;
        }

        //排队订单
        if (OrderStatusEnum.WAIT_ING.equals(status)) {
            if (!StringUtils.isBlank(message)) {
                log.info("账号内存在其他排队订单");
                throw new TrainException("未完成订单, 排队订单不一致");
            } else {
                return noCompleteOrderDto;
            }
        }

        //对比车票号
        String noCompleteSequence = noCompleteOrderDto.getSequence();
        if (StringUtils.isNotBlank(sequence) && !sequence.equals(noCompleteSequence)) {
            throw new TrainException(KeyWordEnum.OTHER_NO_ORDER,
                    String.format("未完成订单, 订单号不一致input:%s, already:%s", sequence, noCompleteSequence));
        }

        //信息是否一致
        if (StringUtils.isNotBlank(message)) {
            throw new TrainException(KeyWordEnum.OTHER_NO_ORDER, message);
        }

        //判断订单状态是否是期望
        if (!statusEnums.contains(status)) {
            throw new TrainException(KeyWordEnum.OTHER_NO_ORDER,
                    String.format("未完成订单, 状态不一致input:%s, already:%s", statusEnums.get(0).getTitle(), status.getTitle()));
        }
        log.info("成功匹配到未完成订单:{}", noCompleteOrderDto);
        return noCompleteOrderDto;
    }

    /**
     * 反查改签订单
     *
     * @param statusReq
     * @return
     * @throws IOException
     */
    public NoCompleteOrderDto queryNoCompleteBack(String sequence, String trainInfoReq, List<String> ticketInfoReq,
            OrderStatusEnum statusReq) throws IOException {
        NoCompleteOrderDto noCompleteOrderDto = queryMyOrderNoComplete(true);
        if (noCompleteOrderDto == null) {
            throw new TrainException("占位成功，反查未完成订单结果为空");
        }

        OrderStatusEnum status = noCompleteOrderDto.getStatusEnum();
        String message = equalOrder(noCompleteOrderDto, trainInfoReq, ticketInfoReq);
        //错误订单
        if (OrderStatusEnum.BOOK_SEAT_FAIL.equals(status)) {
            if (StringUtils.isBlank(message)) {
                //反查到错误订单抛出异常
                throw new TrainException(noCompleteOrderDto.getMessage());
            } else {
                //反查到错误订单,但订单信息不一致，抛出异常 TODO
                throw new TrainException("反查占位结果，出现其他占位失败订单:" + noCompleteOrderDto.getMessage());
            }
        }

        //排队订单
        if (OrderStatusEnum.WAIT_ING.equals(status)) {
            int waitTime = noCompleteOrderDto.getWaitTime();
            if (StringUtils.isBlank(message)) {
                log.info("订单排队waitTime:{}", waitTime);
                throw new TrainException(String.format("订单排队waitTime:%s", waitTime));
            } else {
                throw new TrainException("反查占位结果，出现其他占位排队订单:" + message);
            }
        }

        //对比车票号
        String noCompleteSequence = noCompleteOrderDto.getSequence();
        if (StringUtils.isNotBlank(sequence) && !sequence.equals(noCompleteSequence)) {
            log.info("未完成订单不一致input:{}, already:{}", sequence, noCompleteSequence);
            throw new TrainException(String.format("未完成订单不一致input:%s, already:%s", sequence, noCompleteSequence));
        }

        //信息是否一致
        if (StringUtils.isNotBlank(message)) {
            throw new TrainException(message);
        }

        //判断订单状态是否是期望
        if (!statusReq.equals(status)) {
            log.info("未完成订单不一致input:{}, already:{}", statusReq.getTitle(), status.getTitle());
            throw new TrainException(
                    String.format("未完成订单不一致input:%s, already:%s", statusReq.getTitle(), status.getTitle()));
        }
        log.info("成功匹配未完成订单:{}", noCompleteOrderDto);
        return noCompleteOrderDto;
    }


    /**
     * 查询 ‘已完成-未出行’ 订单
     * @param sequence
     * @param subSequenceList
     */
    public List<CompleteOrderDto> queryNotTravelOrder(String sequence, List<String> subSequenceList)throws IOException {
        //查询已完成订单
        int index = 1;
        int sleep = 500;
        List<CompleteOrderDto> completeOrderDtos = null;
        do {
            if (index != 1) {
                try {
                    Thread.sleep(sleep += 500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("第 {} 次查询未出行订单列表", index);
            completeOrderDtos = queryMyOrder(sequence, false);
            if (completeOrderDtos != null && !completeOrderDtos.isEmpty()) {
                break;
            }
        } while (++index <= 3);
        if (completeOrderDtos == null) {
            throw new TrainException(Message.NOT_TRAVEL_ORDER_EMPTY);
        }
        //过滤条件
        Map<String, CompleteOrderDto> map = completeOrderDtos.stream().collect(Collectors.toMap(dto -> dto.getSubSequence(), Function.identity()));
        List<CompleteOrderDto> resultList = new ArrayList<>();
        for (String subSequence : subSequenceList) {
            CompleteOrderDto orderDto = map.get(subSequence);
            if (orderDto == null) {
                throw new TrainException(String.format(Message.NOT_TRAVEL_ORDER_ERROR, "车票不存在:" + subSequence));
            }

            OrderStatusEnum statusEnum = orderDto.getStatusEnum();
            if (statusEnum == null) {
                throw new TrainException(String.format(Message.NOT_TRAVEL_ORDER_ERROR, String.format("未知的车票状态%s-%s:%s", subSequence, orderDto.getStatus(), orderDto.getStatusName())));
            }
            resultList.add(orderDto);
        }
        return resultList;
    }


    //查询已完成订单
    private List<CompleteOrderDto> queryMyOrder(String sequence, boolean isHistory) throws IOException {
        //默认，按照订票日期查询， 起始时间:当天， 结束时间:2个月后
        String queryWhere = "G";
        String queryStartDate = DateUtil.format4Incr(-29, TimeUnit.DAYS, DateFormat.DATE);
        String queryEndDate = DateUtil.format(DateFormat.DATE);
        if (isHistory) {
            //修改区间，以当前时间为准，前推2个月
            queryWhere = "H";
            queryEndDate = DateUtil.format4Incr(-1, TimeUnit.DAYS, DateFormat.DATE);
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("come_from_flag", "my_order");
        map.put("pageIndex", "0");
        map.put("pageSize", "8");
        map.put("query_where", queryWhere);
        map.put("queryStartDate", queryStartDate);
        map.put("queryEndDate", queryEndDate);
        map.put("queryType", "1");
        map.put("sequeue_train_name", sequence);

        CompleteOrderData data = queryOrderApi.queryMyOrder(map).execute().body().getData();
        log.info("查询已完成订单queryMyOrder data:{}", data);
        if (data == null) {
            return null;
        }

        //
        List<OrderDbDto> orderDtoDataList = data.getOrderDtoDataList();
        if (orderDtoDataList == null || orderDtoDataList.isEmpty()) {
            return null;
        }

        //返回订单信息 CompleteOrderDto
        List<OrderDbTicketDto> tickets = orderDtoDataList.get(0).getTickets();
        return tickets.stream().map(ticket -> {
            return CompleteOrderDto.builder().sequence(ticket.getSequence_no()).name(ticket.name())
                    .subSequence(ticket.getTicket_no()).status(ticket.getTicket_status_code())
                    .fromDate(ticket.getTrain_date()).statusName(ticket.getTicket_status_name())
                    .batchNo(ticket.getBatch_no()).coachNo(ticket.getCoach_no()).seatNo(ticket.getSeat_no())
                    .fromTime(ticket.getStart_train_date_page()).price(ticket.getTicket_price())
                    .trainCode(ticket.getStationTrainDto().getStation_train_code()).seatName(ticket.getSeat_name())
                    .coachName(ticket.getCoach_name()).seatTypeName(ticket.getSeat_type_name())
                    .fromStationName(ticket.getStationTrainDto().getFrom_station_name())
                    .fromStationCode(ticket.getStationTrainDto().getFrom_station_telecode())
                    .toStationName(ticket.getStationTrainDto().getTo_station_name())
                    .toStationCode(ticket.getStationTrainDto().getTo_station_telecode())
                    .innerTrainCode(ticket.getStationTrainDto().getTrainDto().getTrain_no())
                    .idcardType(ticket.getPassengerDto().getPassenger_id_type_code())
                    .idcardNo(ticket.getPassengerDto().getPassenger_id_no()).build();
        }).collect(Collectors.toList());
    }

    /**
     * 查询 ‘未完成’ 订单
     * @return
     * @throws IOException
     * @param isBackCheck
     */
    private NoCompleteOrderDto queryMyOrderNoComplete(boolean isBackCheck) throws IOException {

        int index = 0;
        OrderCacheDto orderCacheDto = null;
        List<OrderDbDto> orderDbList = null;
        boolean querySuccess = false;
        while (index++ < noOrderMax) {
            if (index != 1) {
                try {
                    Thread.sleep(new Random().nextInt(noOrderSleep));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            NoCompleteOrderData data = queryOrderApi.queryMyOrderNoComplete("").execute().body().getData();
            log.info("第 {} 次查询未完成订单queryMyOrderNoComplete data:{}", index, data);
            if (data == null) {
                //不存在订单
                if (!isBackCheck) {
                    return null;
                } else {
                    continue;
                }
            }
            orderCacheDto = data.getOrderCacheDTO();
            orderDbList = data.getOrderDBList();
            if (orderCacheDto == null && (orderDbList == null || orderDbList.isEmpty())) {
                //不存在订单
                if (!isBackCheck) {
                    return null;
                } else {
                    continue;
                }
            } else {
                querySuccess = true;
                break;
            }
        }

        if (!querySuccess) {
            return null;
        }

        NoCompleteOrderDto.NoCompleteOrderDtoBuilder builder = NoCompleteOrderDto.builder();
        if (orderCacheDto != null) {
            int waitTime = orderCacheDto.getWaitTime();
            log.info("订单排队时间:{}", waitTime);
            if (waitTime == -2) {
                //占位失败
                builder.status(OrderStatusEnum.BOOK_SEAT_FAIL.getCode()).message(orderCacheDto.message());
            } else {
                //排队订单
                builder.status(OrderStatusEnum.WAIT_ING.getCode()).waitTime(waitTime);
            }

            //获取车次信息
            builder.trainCode(orderCacheDto.getStationTrainCode()).fromDate(orderCacheDto.getTrainDate())
                    .fromStationName(orderCacheDto.getFromStationName()).fromStationCode(orderCacheDto.getFromStationCode())
                    .toStationName(orderCacheDto.getToStationName()).toStationCode(orderCacheDto.getToStationCode());

            //获取乘客信息
            List<OrderTicketDto> tickets = orderCacheDto.getTickets().stream().map(cacheTicket -> {
                PassengerTypeEnum byTitle = PassengerTypeEnum.getByTitle(cacheTicket.getTicketTypeName());
                return OrderTicketDto.builder().name(cacheTicket.getPassengerName())
                        .seatType(cacheTicket.getSeatTypeCode()).ticketType(byTitle.getKyfw()).build();
            }).collect(Collectors.toList());
            return builder.tickets(tickets).build();
        }

        //占位成功的订单
        List<OrderDbTicketDto> tickets = orderDbList.get(0).getTickets();
        AtomicInteger totalPrice = new AtomicInteger();
        List<OrderTicketDto> tiketList = tickets.stream().map(ticket -> {
            Integer price = ticket.getTicket_price();
            totalPrice.addAndGet(price);
            return OrderTicketDto.builder()
                    //姓名
                    .name(ticket.name())
                    //证件类型
                    .cardType(ticket.cardType())
                    //证件号
                    .cardNo(ticket.cardNo())
                    //坐席类型
                    .seatType(ticket.getSeat_type_code())
                    //车票类型
                    .ticketType(ticket.getTicket_type_code())
                    //票价
                    .price(price)
                    //取票号
                    .subSequence(ticket.getTicket_no())
                    //车厢
                    .coachNo(ticket.getCoach_no()).coachName(ticket.getCoach_name())
                    //座位号
                    .seatNo(ticket.getSeat_no()).seatName(ticket.getSeat_name()).build();
        }).collect(Collectors.toList());
        OrderDbTicketDto ticket = tickets.get(0);

        return builder.trainCode(ticket.trainCode()).fromDate(ticket.getTrain_date()).innerTrainCode(ticket.trainNo())
                .toStationName(ticket.toStationName()).status(ticket.getTicket_status_code()).tickets(tiketList)
                .sequence(ticket.getSequence_no()).fromStationName(ticket.fromStationName())
                .totalPrice(totalPrice.get()).fromStationCode(ticket.fromStationCode())
                .toStationCode(ticket.toStationCode()).fromTime(ticket.getStart_train_date_page())
                .payLimitTime(ticket.getPay_limit_time()).build();
    }

    private String equalOrder(NoCompleteOrderDto orderDto, String trainInfoReq, List<String> ticketInfoReq) {
        String trainInfo = orderDto.trainInfo();
        log.info("车次信息:{}-{}", trainInfoReq, trainInfo);
        if (!trainInfo.contains(trainInfoReq)) {
            return String.format("未完成订单, 信息不一致input:%s, already:%s", trainInfoReq, trainInfo);
        }

        List<String> seqInfo = orderDto.ticketSeqInfo();
        log.info("子车票信息:{}-{}", ticketInfoReq, seqInfo);
        if (seqInfo.equals(ticketInfoReq)) {
            //当，子票号全部一致，则可以忽略乘客信息
            return "";
        }

        //子票号不一致，对比乘客信息
        List<String> ticketInfo = orderDto.ticketInfo();
        log.info("乘客信息:{}-{}", ticketInfoReq, ticketInfo);
        if (!ListUtils.isEqualList(ticketInfo, ticketInfoReq)) {
            return String.format("未完成订单, 车票信息不一致input:%s, already:%s", ticketInfoReq, ticketInfo);
        }
        return "";
    }


	/**
	 * 查询待支付订单
	 * @param sequence
	 * @param subSequences
	 * @return
	 * @throws IOException
	 */
    public NoCompleteOrderDto queryWaitPay(String sequence, List<String> subSequences) throws IOException {
		NoCompleteOrderDto noOrderDto = queryMyOrderNoComplete(false);
		if (noOrderDto == null) {
			log.info("查询已完成订单为空");
			return null;
		}
		OrderStatusEnum status = noOrderDto.getStatusEnum();
		if(!status.equals(OrderStatusEnum.WAIT_PAY) && !status.equals(OrderStatusEnum.CHANGE_WAIT_PAY)
				&& !status.equals(OrderStatusEnum.ALTER_WAIT_PAY)){
			return null;
		}

		//对比订单号
		subSequences = subSequences.stream().sorted().collect(Collectors.toList());
		String dtoSequence = noOrderDto.getSequence();
		List<String> dtoSubSequences = noOrderDto.getTickets().stream().map(OrderTicketDto::getSubSequence).sorted().collect(Collectors.toList());

		if(!sequence.equals(dtoSequence)){
			log.info(String.format("待支付订单不一致 %s:%s", sequence, dtoSequence));
			throw new TrainException(String.format("待支付订单不一致 %s:%s", sequence, dtoSequence));
		}

		if(!CollectionUtils.isEqualCollection(subSequences, dtoSubSequences)){
			log.info(String.format("待支付订单不一致 %s:%s", subSequences, dtoSubSequences));
			throw new TrainException(String.format("待支付订单不一致 %s:%s", subSequences, dtoSubSequences));
		}
		return noOrderDto;
	}
}
