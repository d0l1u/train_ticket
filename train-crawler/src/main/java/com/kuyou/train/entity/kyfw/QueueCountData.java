package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QueueCountData
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueueCountData {

    private int count;
    private String ticket;
    private Boolean op_2;
    private int countT;
    private Boolean op_1;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
