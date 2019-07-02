package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TrainOptlog
 *
 * @author taokai3
 * @date 2018/12/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainOptlog {

    private String logId;
    private String message;
    private String keyword;
    private String type;
    private String failCode;
}
