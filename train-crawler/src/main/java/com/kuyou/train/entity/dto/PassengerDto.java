package com.kuyou.train.entity.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.kuyou.train.common.util.DateFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * PassengerDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {

    @JSONField(name = "passenger_name", ordinal = 1)
    private String name;

    @JSONField(name = "sex_code", ordinal = 2)
    private String sexCode = "M";

    @JSONField(name = "passenger_id_no", ordinal = 3)
    private String cardNo;

    @JSONField(name = "mobile_no", ordinal = 4)
    private String mobileNo = "";

    @JSONField(name = "phone_no", ordinal = 5)
    private String phoneNo = "";

    @JSONField(name = "email", ordinal = 6)
    private String email = "";

    @JSONField(name = "address", ordinal = 7)
    private String address = "";

    @JSONField(name = "postalcode", ordinal = 8)
    private String postalCode = "";

    @JSONField(name = "studentInfoDTO.school_code", ordinal = 9)
    private String schoolCode = "";

    @JSONField(name = "studentInfoDTO.school_name", ordinal = 10)
    private String schoolName = "";

    @JSONField(name = "studentInfoDTO.department", ordinal = 11)
    private String department = "";

    @JSONField(name = "studentInfoDTO.school_class", ordinal = 12)
    private String schoolClass = "";

    @JSONField(name = "studentInfoDTO.student_no", ordinal = 13)
    private String studentNo = "";

    @JSONField(name = "studentInfoDTO.preference_card_no", ordinal = 14)
    private String preferenceCardNo = "";

    @JSONField(name = "GAT_valid_date_end", format = "yyyy-MM-dd", ordinal = 15)
    private Date validDateEnd = new DateTime("2010-01-01").toDate();

    @JSONField(name = "GAT_born_date", format = "yyyy-MM-dd", ordinal = 16)
    private Date bornDate = new DateTime("1990-01-01").toDate();

    @JSONField(name = "old_passenger_name", ordinal = 17)
    private String oldName = "";

    @JSONField(name = "country_code", ordinal = 18)
    private String countryCode = "CN";

    @JSONField(name = "_birthDate", format = "yyyy-MM-dd", ordinal = 19)
    private Date birthDate = new DateTime("1990-01-01").toDate();

    @JSONField(name = "old_passenger_id_type_code", ordinal = 20)
    private String oldCardType = "";

    @JSONField(name = "passenger_id_type_code", ordinal = 21)
    private String cardType;

    @JSONField(name = "old_passenger_id_no", ordinal = 22)
    private String oldCardNo = "";

    @JSONField(name = "passenger_type", ordinal = 23)
    private String passengerType;

    @JSONField(name = "studentInfoDTO.province_code", ordinal = 24)
    private String provinceCode = "11";

    @JSONField(name = "studentInfoDTO.school_system", ordinal = 25)
    private String system = "1";

    @JSONField(name = "studentInfoDTO.enter_year", ordinal = 26)
    private String enterYear = "2018";

    @JSONField(name = "studentInfoDTO.preference_from_station_name", ordinal = 27)
    private String stuFromStationName = "简码/汉字";

    @JSONField(name = "studentInfoDTO.preference_from_station_code", ordinal = 28)
    private String stuFromStationCode = "";

    @JSONField(name = "studentInfoDTO.preference_to_station_name", ordinal = 29)
    private String stuToStationName = "简码/汉字";

    @JSONField(name = "studentInfoDTO.preference_to_station_code", ordinal = 30)
    private String stuToStationCode = "";

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private String sexCode = "M";
        private String cardNo;
        private String mobileNo = "";
        private String phoneNo = "";
        private String email = "";
        private String address = "";
        private String postalCode = "";
        private String schoolCode = "";
        private String schoolName = "";
        private String department = "";
        private String schoolClass = "";
        private String studentNo = "";
        private String preferenceCardNo = "";
        private Date validDateEnd = new DateTime("2010-01-01").toDate();
        private Date bornDate = new DateTime("1990-01-01").toDate();
        private String oldName = "";
        private String countryCode = "CN";
        private Date birthDate = new DateTime("1990-01-01").toDate();
        private String oldCardType = "";
        private String cardType;
        private String oldCardNo = "";
        private String passengerType;
        private String provinceCode = "11";
        private String system = "1";
        private String enterYear = new DateTime().toString(DateFormat.DATE_YYYY.getFormat());
        private String stuFromStationName = "简码/汉字";
        private String stuFromStationCode = "";
        private String stuToStationName = "简码/汉字";
        private String stuToStationCode = "";
        private String status;


        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder sexCode(String sexCode) {
            this.sexCode = sexCode;
            return this;
        }

        public Builder cardNo(String cardNo) {
            this.cardNo = cardNo;
            return this;
        }

        public Builder mobileNo(String mobileNo) {
            if (StringUtils.isNotBlank(mobileNo)) {
                this.mobileNo = mobileNo;
            }
            return this;
        }

        public Builder phoneNo(String phoneNo) {
            if (StringUtils.isNotBlank(phoneNo)) {
                this.phoneNo = phoneNo;
            }
            return this;
        }

        public Builder email(String email) {
            if (StringUtils.isNotBlank(email)) {
                this.email = email;
            }
            return this;
        }

        public Builder address(String address) {
            if (StringUtils.isNotBlank(address)) {
                this.address = address;
            }
            return this;
        }

        public Builder postalCode(String postalCode) {
            if (StringUtils.isNotBlank(postalCode)) {
                this.postalCode = postalCode;
            }
            return this;
        }

        public Builder schoolCode(String schoolCode) {
            if (StringUtils.isNotBlank(schoolCode)) {
                this.schoolCode = schoolCode;
            }
            return this;
        }

        public Builder schoolName(String schoolName) {
            if (StringUtils.isNotBlank(schoolName)) {
                this.schoolName = schoolName;
            }
            return this;
        }

        public Builder department(String department) {
            if (StringUtils.isNotBlank(department)) {
                this.department = department;
            }
            return this;
        }

        public Builder schoolClass(String schoolClass) {
            if (StringUtils.isNotBlank(schoolClass)) {
                this.schoolClass = schoolClass;
            }
            return this;
        }

        public Builder studentNo(String studentNo) {
            if (StringUtils.isNotBlank(studentNo)) {
                this.studentNo = studentNo;
            }
            return this;
        }

        public Builder preferenceCardNo(String preferenceCardNo) {
            if (StringUtils.isNotBlank(preferenceCardNo)) {
                this.preferenceCardNo = preferenceCardNo;
            }
            return this;
        }

        public Builder validDateEnd(Date validDateEnd) {
            if (validDateEnd != null) {
                this.validDateEnd = validDateEnd;
            }
            return this;
        }

        public Builder bornDate(Date bornDate) {
            if (bornDate != null) {
                this.bornDate = bornDate;
            }
            return this;
        }

        public Builder oldName(String oldName) {
            if (StringUtils.isNotBlank(oldName)) {
                this.oldName = oldName;
            }
            return this;
        }

        public Builder countryCode(String countryCode) {
            if (StringUtils.isNotBlank(countryCode)) {
                this.countryCode = countryCode;
            }
            return this;
        }

        public Builder birthDate(Date birthDate) {
            if (birthDate != null) {
                this.birthDate = birthDate;
            }
            return this;
        }

        public Builder oldCardType(String oldCardType) {
            if (StringUtils.isNotBlank(oldCardType)) {
                this.oldCardType = oldCardType;
            }
            return this;
        }

        public Builder cardType(String cardType) {
            if (StringUtils.isNotBlank(cardType)) {
                this.cardType = cardType;
            }
            return this;
        }

        public Builder oldCardNo(String oldCardNo) {
            if (StringUtils.isNotBlank(oldCardNo)) {
                this.oldCardNo = oldCardNo;
            }
            return this;
        }

        public Builder passengerType(String passengerType) {
            if (StringUtils.isNotBlank(passengerType)) {
                this.passengerType = passengerType;
            }
            return this;
        }

        public Builder provinceCode(String provinceCode) {
            if (StringUtils.isNotBlank(provinceCode)) {
                this.provinceCode = provinceCode;
            }
            return this;
        }

        public Builder system(String system) {
            if (StringUtils.isNotBlank(system)) {
                this.system = system;
            }
            return this;
        }

        public Builder enterYear(String enterYear) {
            if (StringUtils.isNotBlank(enterYear)) {
                this.enterYear = enterYear;
            }
            return this;
        }

        public Builder stuFromStationName(String stuFromStationName) {
            if (StringUtils.isNotBlank(stuFromStationName)) {
                this.stuFromStationName = stuFromStationName;
            }
            return this;
        }

        public Builder stuFromStationCode(String stuFromStationCode) {
            if (StringUtils.isNotBlank(stuFromStationCode)) {
                this.stuFromStationCode = stuFromStationCode;
            }
            return this;
        }

        public Builder stuToStationName(String stuToStationName) {
            if (StringUtils.isNotBlank(stuToStationName)) {
                this.stuToStationName = stuToStationName;
            }
            return this;
        }

        public Builder stuToStationCode(String stuToStationCode) {
            if (StringUtils.isNotBlank(stuToStationCode)) {
                this.stuToStationCode = stuToStationCode;
            }
            return this;
        }

        public Builder status(String status) {
            if (StringUtils.isNotBlank(status)) {
                this.status = status;
            }
            return this;
        }

        public PassengerDto build() {
            return new PassengerDto(name, sexCode, cardNo, mobileNo, phoneNo, email, address, postalCode, schoolCode,
                    schoolName, department, schoolClass, studentNo, preferenceCardNo, validDateEnd, bornDate, oldName,
                    countryCode, birthDate, oldCardType, cardType, oldCardNo, passengerType, provinceCode, system,
                    enterYear, stuFromStationName, stuFromStationCode, stuToStationName, stuToStationCode, status);
        }
    }

    /**
     * ===================== 其他参数
     */
    @JSONField(serialize = false)
    private String status;

}
