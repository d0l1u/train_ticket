package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PassengerDelDto
 *
 * @author taokai3
 * @date 2018/12/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDelDto {

    private String name;

    private String cardType;

    private String cardNo;

    private String userSelf = "N";

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
