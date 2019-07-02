package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * CheckOrderInfoData
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOrderInfoData {

    private String ifShowPassCode;
    private String canChooseBeds;
    private String canChooseSeats;
    private String choose_Seats;
    private String isCanChooseMid;
    private String ifShowPassCodeTime;
    private boolean submitStatus;
    private String smokeStr;
    private String errMsg;

    public String getChoose_Seats() {
        return StringUtils.isBlank(choose_Seats) ? "" : choose_Seats;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
