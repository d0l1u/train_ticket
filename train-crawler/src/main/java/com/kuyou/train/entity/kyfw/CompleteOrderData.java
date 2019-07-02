package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuyou.train.entity.kyfw.order.OrderDbDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CompleteOrderData
 *
 * @author taokai3
 * @date 2018/11/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteOrderData {

    private String order_total_number;

    @JSONField(name = "OrderDTODataList")
    private List<OrderDbDto> orderDtoDataList;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
