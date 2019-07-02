package com.kuyou.train.entity.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * BookOrderTicketReq
 *
 * @author taokai3
 * @date 2018/11/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookOrderTicketReq {

    private String cpId;
    private String name;
    private String cardType;
    private String cardNo;
    private String ticketType;
    private String seatType;
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
}
