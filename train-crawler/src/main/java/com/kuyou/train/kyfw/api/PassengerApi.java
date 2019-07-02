package com.kuyou.train.kyfw.api;

import com.kuyou.train.entity.kyfw.PassengerCommonData;
import com.kuyou.train.entity.kyfw.PassengerParentData;
import com.kuyou.train.entity.kyfw.common.KyfwCommonResponse;
import com.kuyou.train.http.annotation.HttpService;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.Map;

/**
 * PassengerApi
 *
 * @author taokai3
 * @date 2018/12/10
 */
@HttpService
public interface PassengerApi {

    /**
     * 查询联系人列表
     *
     * @param parameters 查询参数：分页参数，乘客姓名
     * @return PassengerParentData
     */
    @FormUrlEncoded
    @POST("/otn/passengers/query")
    @Headers({"Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8", //
            "Origin:https://kyfw.12306.cn", //
            "X-Requested-With:XMLHttpRequest",//
            "Content-Type:application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer:https://kyfw.12306.cn/otn/view/passengers.html"})
    Call<KyfwCommonResponse<PassengerParentData>> queryPassenger(@FieldMap Map<String, Object> parameters);


    /**
     * 删除联系人
     *
     * @param parameters
     * @return PassengerParentData
     */
    @FormUrlEncoded
    @POST("/otn/passengers/delete")
    @Headers({"Accept: */*", //
            "Origin:https://kyfw.12306.cn", //
            "X-Requested-With:XMLHttpRequest",//
            "Content-Type:application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer:https://kyfw.12306.cn/otn/view/passengers.html"})
    Call<KyfwCommonResponse<PassengerCommonData>> deletePassenger(@FieldMap Map<String, String> parameters);

    /**
     * 添加联系人
     *
     * @param parameters
     * @return PassengerParentData
     */
    @FormUrlEncoded
    @POST("/otn/passengers/add")
    @Headers({"Accept: */*", //
            "Origin:https://kyfw.12306.cn", //
            "X-Requested-With:XMLHttpRequest",//
            "Content-Type:application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer:https://kyfw.12306.cn/otn/view/passengers.html"})
    Call<KyfwCommonResponse<PassengerCommonData>> addPassenger(@FieldMap Map<String, Object> parameters);


    /**
     * 修改常用联系人
     *
     * @param parameters
     * @return PassengerParentData
     */
    @FormUrlEncoded
    @POST("/otn/passengers/edit")
    @Headers({"Accept: */*", //
            "Origin:https://kyfw.12306.cn", //
            "X-Requested-With:XMLHttpRequest",//
            "Content-Type:application/x-www-form-urlencoded; charset=UTF-8",//
            "https://kyfw.12306.cn/otn/view/passenger_edit.html?type=edit"})
    Call<KyfwCommonResponse<PassengerCommonData>> editPassenger(@FieldMap Map<String, Object> parameters);


    /**
     * 修改账号所属人
     *
     * @param parameters
     * @return PassengerParentData
     */
    @FormUrlEncoded
    @POST("/otn/modifyUser/saveModifyUserInfo")
    @Headers({"Accept: application/json, text/javascript, */*; q=0.01", //
            "Origin:https://kyfw.12306.cn", //
            "X-Requested-With:XMLHttpRequest",//
            "Content-Type:application/x-www-form-urlencoded; charset=UTF-8",//
            "Referer: https://kyfw.12306.cn/otn/view/information.html"})
    Call<KyfwCommonResponse<PassengerCommonData>> editUser(@FieldMap Map<String, Object> parameters);
}
