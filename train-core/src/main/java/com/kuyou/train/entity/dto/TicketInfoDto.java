package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TicketInfoDto：查询余票返回结果
 *
 * @author taokai3
 * @date 2018/12/13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketInfoDto {

    @JSONField(name = "return_code", ordinal = 1)
    private String code;

    @JSONField(ordinal = 2)
    private String message;

    @JSONField(name = "train_data", ordinal = 3)
    private List<TicketDetailDto> tickets;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
