package com.kuyou.train.service;

/**
 * CallBackService
 *
 * @author taokai3
 * @date 2018/11/8
 */
public interface CallBackService {

    /**
     * 预订占位回调
     *
     * @param dataJson
     */
    void bookOrder(String dataJson);

    /**
     * 预订支付回调
     *
     * @param data
     */
    void bookPay(String data);

    /**
     * 退票回调
     *
     * @param dataJson
     */
    void refund(String dataJson);

    /**
     * 改签占位回调
     *
     * @param backJson
     */
    void changeOrder(String backJson);

    /**
     * 改签支付回调
     *
     * @param backJson
     */
    void changePay(String backJson);
}
