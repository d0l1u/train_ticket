package com.kuyou.train.kyfw.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.kuyou.train.common.Constant;
import com.kuyou.train.common.enums.CodeEnum;
import com.kuyou.train.common.enums.KeyWordEnum;
import com.kuyou.train.common.exception.TrainException;
import com.kuyou.train.common.util.RegexUtil;
import com.kuyou.train.entity.dto.ConfirmDto;
import com.kuyou.train.entity.kyfw.*;
import com.kuyou.train.kyfw.api.ConfirmApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * ConfirmApiRobot
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Slf4j
@Service
public class ConfirmApiRobot {

    private static final Pattern SUBMIT_TOKEN = Pattern.compile("var globalRepeatSubmitToken = '([^']+)';");
    private static final Pattern TICKET_INFO = Pattern.compile("var ticketInfoForPassengerForm=([^;]+);");

    @Resource
    private ConfirmApi confirmApi;

    @Value("${order.wait.max}")
    private int waitMax;

    @Value("${order.wait.sleep}")
    private int waitSleep;

    @Value("${order.wait.sleep.incr}")
    private int waitSleepIncr;


    @Value("${order.sleep}")
    private int sleep;

    @Value("${order.sleep.incr}")
    private int sleepIncr;

    @Resource
    private CaptchaApiRobot captchaApiRobot;

    /**
     * 下单页面初始化
     *
     * @return
     * @throws IOException
     */
    public ConfirmDto initXc(boolean isBook) throws IOException {
        String initPage = "";
        if(isBook){
            initPage = confirmApi.initDc("").execute().body();
        }else{
            initPage = confirmApi.initGc("").execute().body();
        }

        //获取token
        String submitToken = RegexUtil.matcher(initPage, SUBMIT_TOKEN);
        log.info("initGc-submitToken:{}", submitToken);
        if (StringUtils.isBlank(submitToken)) {
            throw new TrainException(KeyWordEnum.REQUEST_ORDER_PARAMETER_EMPTY, "submitToken");
        }

        String ticketInfoStr = RegexUtil.matcher(initPage, TICKET_INFO);
        log.info("initGc-ticketInfo:{}", ticketInfoStr);
        OrderFormData orderFormData = JSON.parseObject(ticketInfoStr, OrderFormData.class);

        return ConfirmDto.builder().isAsync(orderFormData.getIsAsync()).tourFlag(orderFormData.getTour_flag())
                .submitToken(submitToken).innerTrainCode(orderFormData.innerTrainCode())
                .trainCode(orderFormData.trainCode()).fromStationCode(orderFormData.fromStationCode())
                .toStationCode(orderFormData.toStationCode()).leftTicket(orderFormData.getLeftTicketStr())
                .purposeCodes(orderFormData.getPurpose_codes()).trainLocation(orderFormData.getTrain_location())
                .keyCheckIsChange(orderFormData.getKey_check_isChange()).build();
    }

    /**
     * 订单信息检查
     *
     * @param passengerTicketStr
     * @param oldPassengerStr
     * @param confirmDto
     * @return
     * @throws IOException
     */
    public CheckOrderInfoData checkOrderInfo(String passengerTicketStr, String oldPassengerStr, ConfirmDto confirmDto)
            throws IOException {
        String tourFlag = confirmDto.getTourFlag();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("cancel_flag", "2");
        map.put("bed_level_order_num", "000000000000000000000000000000");
        map.put("passengerTicketStr", passengerTicketStr);
        map.put("oldPassengerStr", oldPassengerStr);
        map.put("tour_flag", tourFlag);
        map.put("randCode", "");
        map.put("whatsSelect", "1");
        map.put("_json_att", "");
        map.put("REPEAT_SUBMIT_TOKEN", confirmDto.getSubmitToken());
        CheckOrderInfoData data = confirmApi.checkOrderInfo(map, "gc".equals(tourFlag) ? Constant.INIT_GC :
                Constant.INIT_DC).execute().body().getData();
        log.info("检查订单信息 checkOrderInfo data:{}", data);
        if (!data.isSubmitStatus()) {
            throw new TrainException(data.getErrMsg());
        }
        return data;
    }

    /**
     * 获取队列信息
     *
     * @param fromDate
     * @param seatType
     * @param confirmDto
     * @param size
     * @param isBook
     * @throws IOException
     */
    public void getQueueCount(Date fromDate, String seatType, ConfirmDto confirmDto, int size, boolean isBook)throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy 00:00:00 'GMT'Z ", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Chongqing"));
        String train_date = sdf.format(fromDate);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("train_date", train_date + " (中国标准时间)");
        map.put("train_no", confirmDto.getInnerTrainCode());
        map.put("stationTrainCode", confirmDto.getTrainCode());
        map.put("seatType", seatType);
        map.put("fromStationTelecode", confirmDto.getFromStationCode());
        map.put("toStationTelecode", confirmDto.getToStationCode());
        map.put("leftTicket", confirmDto.getLeftTicket());
        map.put("purpose_codes", confirmDto.getPurposeCodes());
        map.put("train_location", confirmDto.getTrainLocation());
        map.put("_json_att", "");
        map.put("REPEAT_SUBMIT_TOKEN", confirmDto.getSubmitToken());
        QueueCountData data = confirmApi.getQueueCount(map, isBook ? Constant.INIT_DC : Constant.INIT_GC).execute()
                .body().getData();
        log.info("获取队列信息 getQueueCount data:{}", data);

        String ticketStr = data.getTicket();
        String[] split = ticketStr.split(",");
        int haveSeatCount = 0;
        int noSeatCount = 0;
        int queueCount = data.getCountT();
        if (split.length == 1) {
            haveSeatCount = Integer.valueOf(split[0]);
        } else {
            haveSeatCount = Integer.valueOf(split[0]);
            noSeatCount = Integer.valueOf(split[1]);
        }

        log.info("当前有座:{}张，无票:{}张，排队人数:{}，本次预订人数:{}", haveSeatCount, noSeatCount, queueCount, size);
        if (haveSeatCount - queueCount < size && noSeatCount - queueCount < size) {
            throw new TrainException(KeyWordEnum.LEFT_TICKET_NOT_ENOUGH);
        }
    }

    /**
     * 提交订单进入队列
     *
     * @param passengerTicketStr
     * @param oldPassengerStr
     * @param code
     * @param chooseSeats
     * @param confirmDto
     */
    public void confirmResignForQueue(String passengerTicketStr, String oldPassengerStr, String code,
            String chooseSeats, ConfirmDto confirmDto) throws IOException {
        LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
        map.put("passengerTicketStr", passengerTicketStr);
        map.put("oldPassengerStr", oldPassengerStr);
        map.put("randCode", code);
        map.put("purpose_codes", confirmDto.getPurposeCodes());
        map.put("key_check_isChange", confirmDto.getKeyCheckIsChange());
        map.put("leftTicketStr", confirmDto.getLeftTicket());
        map.put("train_location", confirmDto.getTrainLocation());
        map.put("choose_seats", chooseSeats);
        map.put("seatDetailType", "000");
        map.put("whatsSelect", "1");
        map.put("roomType", "00");
        map.put("_json_att", "");
        map.put("REPEAT_SUBMIT_TOKEN", confirmDto.getSubmitToken());
        ConfirmData data = confirmApi.confirmResignForQueue(map).execute().body().getData();
        log.info("改签确认下单 confirmResignForQueue data:{}", data);
        if (!data.getSubmitStatus()) {
            throw new TrainException(data.getErrMsg());
        }
    }

    /**
     * 等待下单结果
     *
     * @param isBook
     * @param submitToken
     * @throws IOException
     */
    public String queryOrderWaitTime(boolean isBook, String submitToken) throws IOException{
        String referer = Constant.INIT_DC;
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        if (isBook) {
            map.put("random", System.currentTimeMillis());
        } else {
            map.put("random", System.currentTimeMillis());
            map.put("tourFlag", "gc");
            map.put("_json_att", "");
            map.put("REPEAT_SUBMIT_TOKEN", submitToken);
            referer = Constant.INIT_GC;
        }

        //先睡5s
        int index = 0;
        int waitTime = 0;
        while (index++ < waitMax) {
            try {
                //进行睡眠
                int sleepTime = new Random().nextInt(waitSleepIncr) + waitSleep;
                log.info("第 {} 次 queryOrderWaitTime 睡眠:{}ms", index, sleepTime);
                Thread.sleep(sleepTime);

                OrderWaitData data = confirmApi.queryOrderWaitTime(map, referer).execute().body().getData();
                log.info("等待下单结果 queryOrderWaitTime data:{}", data);
                waitTime = data.getWaitTime();
                if (waitTime == -1) {
                    //占位成功
                    String orderId = data.getOrderId();
                    log.info("占位成功:{}", orderId);
                    return orderId;
                } else if (waitTime == -2) {
                    //占位失败
                    String errorCode = data.getErrorcode();
                    String msg = data.getMsg();
                    log.info("占位失败code:{}, message:{}", errorCode, msg);
                    throw new TrainException(msg);
                } else if (waitTime < 0) {
                    //排队中
                    log.info("占位排队中, waitTime:{}出现其他负值, 进行反查", waitTime);
                    return "";
                } else {
                    //排队中
                    int waitCount = data.getWaitCount();
                    log.info("占位排队中waitCount:{}, waitTime:{}", waitCount, waitTime);
                }
            } catch (TrainException e) {
                String message = e.getMessage();
                CodeEnum codeEnum = KeyWordEnum.getCodeByKeyword(message);
                if (!codeEnum.isRetry()) {
                    throw e;
                } else {
                    //重试
                }
            } catch (Exception e) {
                //重试
                e.printStackTrace();
            }
        }

        throw new TrainException("订单排队中waitTime:" + waitTime);
    }

    /**
     * 改签
     * @param passengerTicketStr
     * @param oldPassengerStr
     * @param code
     * @param chooseSeats
     * @param confirmDto
     * @throws IOException
     */
    public void confirmResign(String passengerTicketStr, String oldPassengerStr, String code, String chooseSeats, ConfirmDto confirmDto) throws IOException {
        String tourFlag = confirmDto.getTourFlag();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("passengerTicketStr", passengerTicketStr);
        map.put("oldPassengerStr", oldPassengerStr);
        map.put("tour_flag", tourFlag);
        map.put("randCode", code);
        map.put("purpose_codes", confirmDto.getPurposeCodes());
        map.put("key_check_isChange", confirmDto.getKeyCheckIsChange());
        map.put("train_location", confirmDto.getTrainLocation());
        map.put("choose_seats", chooseSeats);
        map.put("seatDetailType", "000");
        map.put("whatsSelect", "1");
        map.put("_json_att", "");
        map.put("REPEAT_SUBMIT_TOKEN", confirmDto.getSubmitToken());
        ConfirmData data = confirmApi.confirmResign(map).execute().body().getData();
        log.info("确认订单 confirmResignForQueue data:{}", data);
        if (!data.getSubmitStatus()) {
            throw new TrainException(data.getErrMsg());
        }
    }

    /**
     * isAsync = 0
     *
     * @param seatType
     * @param chooseSeats
     * @param confirmDto
     * @param passengerTicketStr
     * @param oldPassengerStr
     * @param isBook
     * @throws IOException
     */
    public void confirmOrder0(String seatType, String chooseSeats, ConfirmDto confirmDto,
            String passengerTicketStr, String oldPassengerStr, boolean isBook) throws IOException {
        //检查订单参数
        CheckOrderInfoData checkData = checkOrderInfo(passengerTicketStr, oldPassengerStr, confirmDto);

        //判断是否需要验证码
        String answer = captchaApiRobot.crackCaptcha4Order(checkData.getIfShowPassCode(), confirmDto.getSubmitToken(), isBook);

        //选座处理
        if (!checkData.getChoose_Seats().contains(seatType) || chooseSeats == null) {
            chooseSeats = "";
        }

        //睡眠
        int sleepTime = new Random().nextInt(sleepIncr) + sleep;
        log.info("下单前睡眠 {} ms", sleepTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //确认下单
        if(isBook){
            confirmSingle(passengerTicketStr, oldPassengerStr, answer, chooseSeats, confirmDto);
        }else{
            confirmResign(passengerTicketStr, oldPassengerStr, answer, chooseSeats, confirmDto);
        }

        /**
         * 获取下单结果
         */
        queryOrderWaitTime(isBook, confirmDto.getSubmitToken());
    }



    /**
     * isAsync = 1
     *
     * @param fromDate
     * @param seatType
     * @param size
     * @param chooseSeats
     * @param confirmDto
     * @param passengerTicketStr
     * @param oldPassengerStr
     * @param isBook
     * @throws IOException
     */
    public void confirmOrder1(Date fromDate, String seatType, int size, String chooseSeats, ConfirmDto confirmDto,
            String passengerTicketStr, String oldPassengerStr, boolean isBook) throws IOException{
        //检查订单参数
        CheckOrderInfoData checkData = checkOrderInfo(passengerTicketStr, oldPassengerStr, confirmDto);

        //判断是否需要验证码
        String answer = captchaApiRobot.crackCaptcha4Order(checkData.getIfShowPassCode(), confirmDto.getSubmitToken(), isBook);

        //选座处理
        if (!checkData.getChoose_Seats().contains(seatType) || chooseSeats == null) {
            chooseSeats = "";
        }

        //获取下单队里中的排队信息
        getQueueCount(fromDate, seatType, confirmDto, size, isBook);

        //睡眠
        int sleepTime = new Random().nextInt(sleepIncr) + sleep;
        log.info("下单前睡眠 {} ms", sleepTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //确认下单
        if (isBook) {
            confirmSingleForQueue(passengerTicketStr, oldPassengerStr, answer, chooseSeats, confirmDto);
        } else {
            confirmResignForQueue(passengerTicketStr, oldPassengerStr, answer, chooseSeats, confirmDto);
        }

        /**
         * 获取下单结果
         */
        queryOrderWaitTime(isBook, confirmDto.getSubmitToken());
    }

    /**
     * 预订占位下单
     * @param passengerTicketStr
     * @param oldPassengerStr
     * @param code
     * @param chooseSeats
     * @param confirmDto
     * @throws IOException
     */
    private void confirmSingleForQueue(String passengerTicketStr, String oldPassengerStr, String code, String chooseSeats,
            ConfirmDto confirmDto) throws IOException {
        LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
        map.put("passengerTicketStr", passengerTicketStr);
        map.put("oldPassengerStr", oldPassengerStr);
        map.put("randCode", code);
        map.put("purpose_codes", confirmDto.getPurposeCodes());
        map.put("key_check_isChange", confirmDto.getKeyCheckIsChange());
        map.put("leftTicketStr", confirmDto.getLeftTicket());
        map.put("train_location", confirmDto.getTrainLocation());
        map.put("choose_seats", chooseSeats);
        map.put("seatDetailType", "000");
        map.put("whatsSelect", "1");
        map.put("roomType", "00");
        map.put("dwAll", "N");
        map.put("_json_att", "");
        map.put("REPEAT_SUBMIT_TOKEN", confirmDto.getSubmitToken());
        ConfirmData data = confirmApi.confirmSingleForQueue(map).execute().body().getData();
        log.info("改签确认下单 confirmResignForQueue data:{}", data);
        if (!data.getSubmitStatus()) {
            throw new TrainException(data.getErrMsg());
        }
    }

    /**
     * 预订下单 isAsync=0
     * @param passengerTicketStr
     * @param oldPassengerStr
     * @param code
     * @param chooseSeats
     * @param confirmDto
     * @throws IOException
     */
    private void confirmSingle(String passengerTicketStr, String oldPassengerStr, String code, String chooseSeats,
            ConfirmDto confirmDto) throws IOException {
        String tourFlag = confirmDto.getTourFlag();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("passengerTicketStr", passengerTicketStr);
        map.put("oldPassengerStr", oldPassengerStr);
        map.put("tour_flag", tourFlag);
        map.put("randCode", code);
        map.put("purpose_codes", confirmDto.getPurposeCodes());
        map.put("key_check_isChange", confirmDto.getKeyCheckIsChange());
        map.put("train_location", confirmDto.getTrainLocation());
        map.put("choose_seats", chooseSeats);
        map.put("seatDetailType", "000");
        map.put("whatsSelect", "1");
        map.put("_json_att", "");
        map.put("REPEAT_SUBMIT_TOKEN", confirmDto.getSubmitToken());
        ConfirmData data = confirmApi.confirmSingle(map).execute().body().getData();
        log.info("确认订单 confirmResignForQueue data:{}", data);
        if (!data.getSubmitStatus()) {
            throw new TrainException(data.getErrMsg());
        }
    }

}
