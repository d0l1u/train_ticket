package com.kuyou.train.kyfw.api;

import com.kuyou.train.entity.kyfw.CaptchaCheckData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.http.annotation.HttpService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PassCode
 *
 * @author taokai3
 * @date 2018/11/16
 */
@HttpService
public interface CaptchaApi {

    /**
     * 下单获取验证码
     *
     * @param url
     * @return
     */
    @GET
    @Headers({"Accept: image/webp,image/apng,image/*,*/*;q=0.8"})
    Call<ResponseBody> getPassCodeNew(@Url String url);

    /**
     * 登录验证码
     *
     * @param map
     * @return
     */
    @GET("/passport/captcha/captcha-image64")
    @Headers({"Accept: text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01",//
            "X-Requested-With: XMLHttpRequest",//
            "Referer: https://kyfw.12306.cn/otn/resources/login.html"})
    Call<String> captcha_image4Login(@QueryMap Map<String, Object> map);

    /**
     * 登录验证码
     *
     * @param map
     * @return
     */
    @GET("/passport/captcha/captcha-check")
    @Headers({"Accept: */*",//
            "Referer: https://kyfw.12306.cn/otn/resources/login.html"})
    Call<String> captcha_check4Login(@QueryMap Map<String, Object> map);

    /**
     * 下单验证码
     * @param imageMap
     * @param referer
     * @return
     */
    @GET("/otn/passcodeNew/getPassCodeNew")
    @Headers({"Accept: image/png, image/svg+xml, image/jxr, image/*;q=0.8, */*;q=0.5"})
    Call<ResponseBody> captcha_image4Order(@QueryMap Map<String, Object> imageMap, @Header("Referer") String referer);

    /**
     * 校验验证码
     * @param checkMap
     * @param referer
     * @return
     */
    @FormUrlEncoded
    @POST("/otn/passcodeNew/checkRandCodeAnsyn")
    @Headers({"Accept: */*", //
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8"})
    Call<KyfwCommonResponse<CaptchaCheckData>> captcha_check4Order(@FieldMap Map<String, Object> checkMap, @Header("Referer") String referer);

    @FormUrlEncoded
    @POST("/otn/passcodeNew/checkRandCodeAnsyn")
    @Headers({
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Accept: */*",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "Referer: https://kyfw.12306.cn/otn/resources/login.html",
            "X-Requested-With: XMLHttpRequest"
    })
    Call<KyfwCommonResponse<CaptchaCheckData>> checkRandCodeAnsyn(@FieldMap Map<String, Object> params);
}
