package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * WorkerPo
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerPo {

    private Integer workerId;
    private String workerName;
    private Date createTime;
    private Date optionTime;
    private Integer workerType;
    private String workerStatus;
    private String publicIp;
    private String privateIp;
    private String stopReason;

    @Override
    public String toString() {
        return "{\"workerId\":" + workerId + ",\"publicIp\":\"" + publicIp + "\",\"workerType\":" + workerType +
                ",\"workerStatus\":\"" + workerStatus + "\"}";
    }
}
