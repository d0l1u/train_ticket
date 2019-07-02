package com.kuyou.train.entity.kyfw;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * PassengerKyfwStudentData
 *
 * @author zhaoliang92
 * @date 2018/11/7
 */
@Getter
@Setter
@ToString
public class PassengerStudentData {

    @JSONField(name = "province_name")
    private String provinceName;

    @JSONField(name = "province_code")
    private String provinceCode;

    @JSONField(name = "city_name")
    private String cityName;

    @JSONField(name = "city_code")
    private String cityCode;

    @JSONField(name = "school_name")
    private String schoolName;

    @JSONField(name = "school_code")
    private String schoolCode;

    @JSONField(name = "department")
    private String department;

    @JSONField(name = "school_class")
    private String schoolClass;

    @JSONField(name = "student_no")
    private String studentNo;

    @JSONField(name = "enter_year")
    private String enterYear;

    @JSONField(name = "school_system")
    private String schoolSystem;

    @JSONField(name = "preference_from_station_name")
    private String preferenceFromStationName;

    @JSONField(name = "preference_to_station_name")
    private String preferenceToStationName;

    @JSONField(name = "preference_from_province_code")
    private String preferenceFromProvinceCode;

    @JSONField(name = "preference_to_province_code")
    private String preferenceToProvinceCode;

    @JSONField(name = "preference_from_station_code")
    private String preferenceFromStationCode;

    @JSONField(name = "preference_to_station_code")
    private String preferenceToStationCode;

    @JSONField(name = "preference_card_no")
    private String preferenceCardNo;
}
