package com.train.system.booking.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {

    private String orderId;

    /**
     * 订单状态：00:开始占位, 01:占位重发, 02:队列中, 11:正在占位, 44:占位人工
     */
    private String orderStatus;

    /**
     * 分销商支付金额
     */
    private BigDecimal payMoney;


    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    private Integer accountId;

    /**
     * 账号来源：0:公司，1:乘客自有
     */
    private String accountFromWay;
    private String channel;

    /**
     * 分销商支付状态，00：已支付；11：未支付
     */
    private String isPay;

    private String sequence;



    private String trainCode;
    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private Date departureDate;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date departureTime;
    private Date arrivalDate;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date arrivalTime;
    private Date payLimitTime;
    private Date finishTime;
    private String extraSeatType;
    private String chooseBed;
    private String chooseSeat;
    private String errorCode;
    private Integer resendNum;


    public Integer getResendNum() {
        return resendNum;
    }

    public void setResendNum(Integer resendNum) {
        this.resendNum = resendNum;
    }

    private List<Passenger> passengers;

    private String failReason;

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountFromWay() {
        return accountFromWay;
    }

    public void setAccountFromWay(String accountFromWay) {
        this.accountFromWay = accountFromWay;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getFromStationName() {
        return fromStationName;
    }

    public void setFromStationName(String fromStationName) {
        this.fromStationName = fromStationName;
    }

    public String getFromStationCode() {
        return fromStationCode;
    }

    public void setFromStationCode(String fromStationCode) {
        this.fromStationCode = fromStationCode;
    }

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public String getToStationCode() {
        return toStationCode;
    }

    public void setToStationCode(String toStationCode) {
        this.toStationCode = toStationCode;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getExtraSeatType() {
        return extraSeatType;
    }

    public void setExtraSeatType(String extraSeatType) {
        this.extraSeatType = extraSeatType;
    }

    public String getChooseBed() {
        return chooseBed;
    }

    public void setChooseBed(String chooseBed) {
        this.chooseBed = chooseBed;
    }

    public String getChooseSeat() {
        return chooseSeat;
    }

    public void setChooseSeat(String chooseSeat) {
        this.chooseSeat = chooseSeat;
    }

    public Date getPayLimitTime() {
        return payLimitTime;
    }

    public void setPayLimitTime(Date payLimitTime) {
        this.payLimitTime = payLimitTime;
    }
    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
