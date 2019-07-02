package com.kuyou.train.kyfw.api;

import com.kuyou.train.entity.kyfw.CheckOrderInfoData;
import com.kuyou.train.entity.kyfw.ConfirmData;
import com.kuyou.train.entity.kyfw.OrderWaitData;
import com.kuyou.train.entity.kyfw.QueueCountData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.http.annotation.HttpService;
import retrofit2.Call;
import retrofit2.http.*;

import javax.sql.rowset.CachedRowSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * ConfirmApi
 *
 * @author taokai3
 * @date 2018/11/16
 */
@HttpService
public interface ConfirmApi {


    @FormUrlEncoded
    @POST("/otn/confirmPassenger/initGc")
    @Headers({"Upgrade-Insecure-Requests: 1",//
            "Origin: https://kyfw.12306.cn",//
            "Content-Type: application/x-www-form-urlencoded",//
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",//
            "Referer: https://kyfw.12306.cn/otn/leftTicket/init"})
    Call<String> initGc(@Field("_json_att") String jsonAtt);

    @FormUrlEncoded
    @POST("/otn/confirmPassenger/initDc")
    @Headers({"Upgrade-Insecure-Requests: 1",//
            "Origin: https://kyfw.12306.cn",//
            "Content-Type: application/x-www-form-urlencoded",//
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",//
            "Referer: https://kyfw.12306.cn/otn/leftTicket/init"})
    Call<String> initDc(@Field("_json_att") String jsonAtt);

    @FormUrlEncoded
    @POST("/otn/confirmPassenger/checkOrderInfo")
    @Headers({"X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Accept: application/json, text/javascript, */*; q=0.01"})
    Call<KyfwCommonResponse<CheckOrderInfoData>> checkOrderInfo(@FieldMap Map<String, Object> map,
            @Header("Referer") String referer);

    @FormUrlEncoded
    @POST("/otn/confirmPassenger/getQueueCount")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8"})
    Call<KyfwCommonResponse<QueueCountData>> getQueueCount(@FieldMap Map<String, Object> map,
            @Header("Referer") String referer);

    @FormUrlEncoded
    @POST("/otn/confirmPassenger/confirmSingleForQueue")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc" })
    Call<KyfwCommonResponse<ConfirmData>> confirmSingleForQueue(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("/otn/confirmPassenger/confirmResignForQueue")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initGc" })
    Call<KyfwCommonResponse<ConfirmData>> confirmResignForQueue(@FieldMap Map<String, Object> map);

    @GET("/otn/confirmPassenger/queryOrderWaitTime")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "X-Requested-With: XMLHttpRequest"})
    Call<KyfwCommonResponse<OrderWaitData>> queryOrderWaitTime(@QueryMap Map<String, Object> map,
            @Header("Referer") String referer);

    @FormUrlEncoded
    @POST("/otn/confirmPassenger/confirmResign")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initGc"})
    Call<KyfwCommonResponse<ConfirmData>> confirmResign(@FieldMap Map<String, Object> map);


    @FormUrlEncoded
    @POST("/otn/confirmPassenger/confirmSingle")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/confirmPassenger/initDc"})
    Call<KyfwCommonResponse<ConfirmData>> confirmSingle(@FieldMap Map<String,Object> map);
}
