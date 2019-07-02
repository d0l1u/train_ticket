package com.kuyou.train.entity.kyfw.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * KyfwResponse
 *
 * @author taokai3
 * @date 2018/11/11
 */
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractKyfwResponse<T> {

    public String validateMessagesShowId;
    public boolean status;
    public int httpstatus;
    public T data;


    public String getValidateMessagesShowId() {
        return validateMessagesShowId;
    }

    public void setValidateMessagesShowId(String validateMessagesShowId) {
        this.validateMessagesShowId = validateMessagesShowId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getHttpstatus() {
        return httpstatus;
    }

    public void setHttpstatus(int httpstatus) {
        this.httpstatus = httpstatus;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
