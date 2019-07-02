package com.train.system.booking.entity;

/**
 * LogRetrun
 *
 * @author taokai3
 * @date 2018/6/19
 */
public class LogReturn {

    private Integer returnId;
    private String returnName;
    private String returnType;
    private String returnFailReason;

    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        this.returnId = returnId;
    }

    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnFailReason() {
        return returnFailReason;
    }

    public void setReturnFailReason(String returnFailReason) {
        this.returnFailReason = returnFailReason;
    }
}
