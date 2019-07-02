package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.entity.kyfw.refund.RefundInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RefundInfoData
 *
 * @author taokai3
 * @date 2018/11/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundInfoData {

    private boolean flag;
    private RefundInfo data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
