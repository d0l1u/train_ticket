package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderStudentPo
 *
 * @author taokai3
 * @date 2018/12/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStudentPo {

    private String cpId;
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
