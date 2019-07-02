package com.kuyou.train.kyfw.api;

import com.kuyou.train.entity.kyfw.QueryTicketData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.http.annotation.HttpService;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * LeftTicketApi
 *
 * @author taokai3
 * @date 2018/11/15
 */
@HttpService
public interface LeftTicketApi {


    @FormUrlEncoded
    @POST("/otn/leftTicket/init")
    @Headers({"Upgrade-Insecure-Requests: 1",//
            "Origin: https://kyfw.12306.cn",//
            "Content-Type: application/x-www-form-urlencoded",//
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",//
            "Referer: https://kyfw.12306.cn/otn/view/train_order.html"})
    Call<String> init4Gc(@Field("_json_att") String jsonAtt, @Field("pre_step_flag") String preStepFlag);

    @GET("/otn/leftTicket/init")
    Call<String> init4Dc(@Query("linktypeid") String type);

    @GET
    @Headers({"Accept: */*",//
            "Cache-Control: no-cache",//
            "X-Requested-With: XMLHttpRequest",//
            "If-Modified-Since: 0",//
            "Content-Type: application/json;charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/leftTicket/init"})
    Call<KyfwCommonResponse<QueryTicketData>> query(@Url String queryUrl,
            @QueryMap Map<String, Object> map);
}
