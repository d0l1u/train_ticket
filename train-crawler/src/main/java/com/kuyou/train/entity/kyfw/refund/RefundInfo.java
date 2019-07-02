package com.kuyou.train.entity.kyfw.refund;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * RefundInfo
 *
 * @author taokai3
 * @date 2018/11/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundInfo {

    private String trade_no;
    private String trade_mode;
    private String query_flag;
    private String start_time;
    private String stop_time;
    private String tradeNo;
    private String transNo;
    private String bankCode;
    private String transType;
    private int transAmount;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date transDate;
    private int transStatus;
    private String merVAR;
    private int currencyId;
    private String bankTransNo;
}
