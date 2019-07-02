package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

/**
 * PassengerReq：乘客列表，重写hashcode和equals方法，通过cardNo进行唯一
 *
 * @author taokai3
 * @date 2018/12/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerReq {

    private String name;
    private String cardType;
    private String cardNo;
    private String ticketType;

    private String sexCode;
    @JSONField(format = "yyyy-MM-dd")
    private Date bornDate;

    //================= 外国人永久居留证

    private String countryCode;
    @JSONField(format = "yyyy-MM-dd")
    private Date validDateEnd;

    //================= 学生票信息

    private String provinceName;
    private String provinceCode;
    private String schoolName;
    private String schoolCode;
    private String studentNo;
    private String system;
    private String enterYear;
    private String stuFromStationName;
    private String stuFromStationCode;
    private String stuToStationName;
    private String stuToStationCode;

    @JSONField(serialize = false)
    public String getPassengerTicketStr(String seatType) {
        return String.format("%s,0,%s,%s,%s,%s,,Y_", seatType, ticketType, name, cardType, cardNo);
    }

    @JSONField(serialize = false)
    public String getOldPassengerStr() {
        return String.format("%s,%s,%s,%s_", name, cardType, cardNo, ticketType);
    }


    @JSONField(serialize = false)
    public String getKey() {
        return String.format("%s_%s", cardNo.toUpperCase().trim(), ticketType);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PassengerReq)) {
            return false;
        }
        PassengerReq that = (PassengerReq) o;
        return Objects.equals(cardNo, that.cardNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNo);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
