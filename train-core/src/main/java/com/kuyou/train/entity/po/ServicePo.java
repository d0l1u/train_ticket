package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * CancelPo
 *
 * @author taokai3
 * @date 2018/11/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePo {

    private String orderId;
    private Integer changeId;
    private String myOrderId;
    private String supplierOrderId;
    private Integer accountId;
    private String sequence;
    private boolean bookFlag;
    private Date payLimitTime;
    private Date createTime;
    private String supplierType;
}
