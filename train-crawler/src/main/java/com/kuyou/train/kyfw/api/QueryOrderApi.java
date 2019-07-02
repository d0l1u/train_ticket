package com.kuyou.train.kyfw.api;

import com.kuyou.train.entity.kyfw.CompleteOrderData;
import com.kuyou.train.entity.kyfw.NoCompleteOrderData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.http.annotation.HttpService;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.LinkedHashMap;

/**
 * QueryOrderApi
 *
 * @author taokai3
 * @date 2018/11/12
 */
@HttpService
public interface QueryOrderApi {

    @FormUrlEncoded
    @POST("/otn/queryOrder/queryMyOrderNoComplete")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<KyfwCommonResponse<NoCompleteOrderData>> queryMyOrderNoComplete(@Field("_json_att") String jsonAtt);

    @FormUrlEncoded
    @POST("/otn/queryOrder/queryMyOrder")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<KyfwCommonResponse<CompleteOrderData>> queryMyOrder(@FieldMap LinkedHashMap<String, String> map);

    @GET("/otn/view/train_order.html")
    @Headers({"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",//
            "X-Requested-With: XMLHttpRequest",//
            "Referer: https://kyfw.12306.cn/otn/view/index.html"})
    Call<String> trainOrder();
}
