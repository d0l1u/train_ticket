package com.train.system.booking.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

/**
 * Passenger
 *
 * @author taokai3
 * @date 2018/6/17
 */
public class Passenger {

    private String orderId;
    private String passengerNo;
    private String name;
    private String cardType;
    private String cardNo;
    private String ticketType;
    private String seatType;

    /**
     * 省名
     */
    private String provinceName;

    /**
     * 省简码
     */
    private String provinceCode;

    /**
     * 学校名
     */
    private String schoolName;

    /**
     * 学校简码
     */
    private String schoolCode;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学制
     */
    private String system;

    /**
     * 入学年份
     */
    private String enterYear;

    private String limitBeginName;
    private String limitBeginCode;
    private String limitEndName;
    private String limitEndCode;

    private String subSequence;

    /** 车厢名称 */
    private String boxNo;
    private String boxName;

    private String seatNo;
    private String seatName;

    /** 单价 */
    private BigDecimal price;


    /** 订单失效时间 */
    private String loseTime;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPassengerNo() {
        return passengerNo;
    }

    public void setPassengerNo(String passengerNo) {
        this.passengerNo = passengerNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getEnterYear() {
        return enterYear;
    }

    public void setEnterYear(String enterYear) {
        this.enterYear = enterYear;
    }

    public String getLimitBeginName() {
        return limitBeginName;
    }

    public void setLimitBeginName(String limitBeginName) {
        this.limitBeginName = limitBeginName;
    }

    public String getLimitBeginCode() {
        return limitBeginCode;
    }

    public void setLimitBeginCode(String limitBeginCode) {
        this.limitBeginCode = limitBeginCode;
    }

    public String getLimitEndName() {
        return limitEndName;
    }

    public void setLimitEndName(String limitEndName) {
        this.limitEndName = limitEndName;
    }

    public String getLimitEndCode() {
        return limitEndCode;
    }

    public void setLimitEndCode(String limitEndCode) {
        this.limitEndCode = limitEndCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getSubSequence() {
        return subSequence;
    }

    public void setSubSequence(String subSequence) {
        this.subSequence = subSequence;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLoseTime() {
        return loseTime;
    }

    public void setLoseTime(String loseTime) {
        this.loseTime = loseTime;
    }
}
