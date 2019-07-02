package com.train.system.center.entity;

import java.util.Date;

/**
 * Contact
 *
 * @author taokai3
 * @date 2018/7/3
 */
public class Contact {

    /**
     * 账号ID
     */
    private Integer accountId;

    /**
     * 账号
     */
    private String username;

    /**
     * 常用联系人个数
     */
    private Integer contactSize;

    private String name;
    private String status;
    private String cardNo;
    private String cardType;
    private String passengerType;
    private Date canDeleteDate;
    private Date lastUseTime;
    private String isUserSelf;


    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getContactSize() {
        return contactSize;
    }

    public void setContactSize(Integer contactSize) {
        this.contactSize = contactSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public Date getCanDeleteDate() {
        return canDeleteDate;
    }

    public void setCanDeleteDate(Date canDeleteDate) {
        this.canDeleteDate = canDeleteDate;
    }

    public Date getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Date lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public String getIsUserSelf() {
        return isUserSelf;
    }

    public void setIsUserSelf(String isUserSelf) {
        this.isUserSelf = isUserSelf;
    }
}
