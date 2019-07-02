package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SettingPo
 *
 * @author taokai3
 * @date 2018/12/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingPo {

    private String id;
    private String name;
    private String value;
    private String status;


}
