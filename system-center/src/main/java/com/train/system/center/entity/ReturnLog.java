package com.train.system.center.entity;

/**
 * LogRetrun
 *
 * @author taokai3
 * @date 2018/6/19
 */
public class ReturnLog {

    private String returnId;
    private String returnName;
    private String returnType;
    private String returnFailReason;

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
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
