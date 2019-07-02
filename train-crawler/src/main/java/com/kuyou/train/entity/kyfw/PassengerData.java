package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.Date;

/**
 * PassengerKyfwQueryData
 * 乘客信息
 *
 * @author liujia33
 * @date 2018/9/29
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerData {

    /**
     * 乘客姓名
     */
    private String code;

    /**
     * 乘客姓名
     */
    @JSONField(name = "passenger_name")
    private String passengerName;

    /**
     * 乘客性别Code
     */
    @JSONField(name = "sex_code")
    private String sexCode;

    /**
     * 乘客性别
     */
    @JSONField(name = "sex_name")
    private String sexName;

    /**
     * 出生日期
     */
    @JSONField(name = "born_date", format = "yyyy-MM-dd")
    private Date bornDate;

    /**
     * 国家简码
     */
    @JSONField(name = "country_code")
    private String countryCode;

    /**
     * 证件类型Code
     */
    @JSONField(name = "passenger_id_type_code")
    private String passengerIdTypeCode;

    /**
     * 证件类型名称
     */
    @JSONField(name = "passenger_id_type_name")
    private String passengerIdTypeName;

    /**
     * 证件号
     */
    @JSONField(name = "passenger_id_no")
    private String passengerIdNo;

    /**
     * 乘客类型Code
     */
    @JSONField(name = "passenger_type")
    private String passengerType;

    /**
     * 乘客类型名称
     */
    @JSONField(name = "passenger_type_name")
    private String passengerTypeName;

    /**
     * 乘客标记
     */
    @JSONField(name = "passenger_flag")
    private String passengerFlag;

    /**
     * 手机号
     */
    @JSONField(name = "mobile_no")
    private String mobileNo;

    /**
     * 电话号
     */
    @JSONField(name = "phone_no")
    private String phoneNo;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String postalcode;

    /**
     * 姓名拼音 首字母拼接（大写）
     */
    @JSONField(name = "first_letter")
    private String firstLetter;

    /**
     * 记录数量
     */
    private String recordCount;

    /**
     * 是否是用户自己
     */
    private String isUserSelf;

    /**
     * 核验状态
     */
    @JSONField(name = "total_times")
    private String totalTimes;

    /**
     *
     */
    @JSONField(name = "delete_time", format = "yyyy/MM/dd")
    private Date deleteTime;

    /**
     * 港澳台-出生日期
     */
    @JSONField(name = "gat_born_date")
    private String gatBornDate;

    /**
     * 港澳台-校验开始日期
     */
    @JSONField(name = "gat_valid_date_start")
    private String gatValidDateStart;

    /**
     * 港澳台-校验结束日期
     */
    @JSONField(name = "gat_valid_date_end")
    private String gatValidDateEnd;

    /**
     * 港澳台-版本
     */
    @JSONField(name = "gat_valid_date_end")
    private String gatVersion;

    private Date lastUseTime;

    /**
     * 学生信息
     */
    @JSONField(name = "studentInfoDTO")
    PassengerStudentData passengerStudentData;

}
