package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.entity.kyfw.order.OrderCacheDto;
import com.kuyou.train.entity.kyfw.order.OrderDbDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * OrderData
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoCompleteOrderData {

    private OrderCacheDto orderCacheDTO;

    private List<OrderDbDto> orderDBList;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
