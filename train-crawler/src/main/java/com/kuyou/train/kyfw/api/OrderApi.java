package com.kuyou.train.kyfw.api;

import com.kuyou.train.entity.kyfw.RequestData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.http.annotation.HttpService;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * OrderApi
 *
 * @author taokai3
 * @date 2018/11/19
 */
@HttpService
public interface OrderApi {

    @FormUrlEncoded
    @POST("/otn/queryOrder/cancelQueueNoCompleteMyOrder")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<KyfwCommonResponse<RequestData>> cancelQueueNoCompleteMyOrder(@Field("tourFlag") String parameter);


    @FormUrlEncoded
    @POST("/otn/queryOrder/cancelNoCompleteMyOrder")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<KyfwCommonResponse<RequestData>> cancelNoCompleteMyOrder(@FieldMap Map<String, String> map);
}
