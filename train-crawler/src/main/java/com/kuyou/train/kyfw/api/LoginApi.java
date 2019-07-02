package com.kuyou.train.kyfw.api;

import com.alibaba.fastjson.JSONObject;
import com.kuyou.train.entity.kyfw.LoginConfData;
import com.kuyou.train.entity.kyfw.LoginData;
import com.kuyou.train.entity.kyfw.UserInfoData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.entity.kyfw.LoginAysnData;
import com.kuyou.train.http.annotation.HttpService;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * LoginApi
 *
 * @author taokai3
 * @date 2018/11/11
 */
@HttpService
public interface LoginApi {

    /**
     * 检查登录状态
     *
     * @return
     */
    @POST("/otn/login/conf")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Referer: https://kyfw.12306.cn/otn/resources/login.html"})
    Call<KyfwCommonResponse<LoginConfData>> login_conf();




    /**
     * web_login
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/passport/web/login")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/resources/login.html"})
    Call<LoginData> web_login(@FieldMap Map<String, Object> map);

    /**
     * web_auth_uamtk
     *
     * @param appid
     * @return
     */
    @FormUrlEncoded
    @POST("/passport/web/auth/uamtk")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin"})
    Call<LoginData> web_auth_uamtk(@Field("appid") String appid);

    @FormUrlEncoded
    @POST("/otn/uamauthclient")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",//
            "https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin"})
    Call<LoginData> uamauthclient(@Field("tk") String tk);

    @POST("/otn/modifyUser/initQueryUserInfoApi")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Referer: https://kyfw.12306.cn/otn/view/information.html"})
    Call<KyfwCommonResponse<UserInfoData>> initQueryUserInfoApi();

    @GET("/otn/index12306/getLoginBanner")
    @Headers({"Accept: text/plain, */*; q=0.01",//
            "X-Requested-With: XMLHttpRequest",//
            "Referer: https://kyfw.12306.cn/otn/resources/login.html"})
    Call<JSONObject> getLoginBanner();

    @FormUrlEncoded
    @POST("/otn/login/loginAysnSuggest")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Referer: https://kyfw.12306.cn/otn/resources/login.html"})
    Call<KyfwCommonResponse<LoginAysnData>> loginAysnSuggest(@Field("loginUserDTO.user_name") String username,
            @Field("userDTO.password")String password);

    @FormUrlEncoded
    @POST("/otn/login/loginAysnSuggest")
    @Headers({"Accept: */*",//
            "Origin: https://kyfw.12306.cn",//
            "X-Requested-With: XMLHttpRequest",//
            "Referer: https://kyfw.12306.cn/otn/resources/login.html"})
    Call<KyfwCommonResponse<LoginAysnData>> loginAysnSuggest(@Field("loginUserDTO.user_name") String username,
            @Field("userDTO.password")String password, @Field("userDTO.password")String answer);
}
