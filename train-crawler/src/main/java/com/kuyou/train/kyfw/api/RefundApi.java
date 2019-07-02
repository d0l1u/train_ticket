package com.kuyou.train.kyfw.api;

import com.alibaba.fastjson.JSONObject;
import com.kuyou.train.entity.kyfw.RefundInfoData;
import com.kuyou.train.entity.kyfw.ReturnTicketData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.http.annotation.HttpService;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * RefundApi
 *
 * @author taokai3
 * @date 2018/11/22
 */
@HttpService
public interface RefundApi {

    @FormUrlEncoded
    @POST("/otn/queryRefund/queryRefundInfo")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<KyfwCommonResponse<RefundInfoData>> queryRefundInfo(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("/otn/queryOrder/returnTicketAffirm")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<KyfwCommonResponse<ReturnTicketData>> returnTicketAffirm(@FieldMap Map<String, Object> map);

    @POST("/otn/queryOrder/returnTicket")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<KyfwCommonResponse<JSONObject>> returnTicket();

    @GET("/otn/queryOrder/returnTicketRedirect")
    @Headers({"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",//
            "Upgrade-Insecure-Requests: 1",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<String> returnTicketRedirect();
}
