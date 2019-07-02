package com.kuyou.train.kyfw.api;

import com.kuyou.train.entity.kyfw.RequestData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.http.annotation.HttpService;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.LinkedHashMap;

/**
 * RequestOrderApi
 *
 * @author taokai3
 * @date 2018/11/15
 */
@HttpService
public interface RequestOrderApi {


    @FormUrlEncoded
    @POST("/otn/queryOrder/resginTicket")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<KyfwCommonResponse<RequestData>> resginTicket(@FieldMap LinkedHashMap<String, Object> map);

    @FormUrlEncoded
    @POST("/otn/leftTicket/submitOrderRequest")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/leftTicket/init"})
    Call<KyfwCommonResponse<String>> submitOrderRequest(@FieldMap LinkedHashMap<String, Object> map);
}
