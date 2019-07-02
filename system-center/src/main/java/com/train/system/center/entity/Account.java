package com.train.system.center.entity;

/**
 * Account
 *
 * @author taokai3
 * @date 2018/6/17
 */
public class Account {

    private Integer accountId;

    private String username;
    private String password;
    private String status;
    private String stopReason;
    private Integer contactSize;

    public Integer getContactSize() {
        return contactSize;
    }

    public void setContactSize(Integer contactSize) {
        this.contactSize = contactSize;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
