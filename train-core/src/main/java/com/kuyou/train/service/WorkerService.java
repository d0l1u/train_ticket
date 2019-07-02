package com.kuyou.train.service;

import com.kuyou.train.entity.po.WorkerPo;

import java.util.Date;
import java.util.List;

/**
 * WorkerService
 *
 * @author taokai3
 * @date 2018/11/21
 */
public interface WorkerService {

    /**
     * 查询可用机器人
     *
     * @param type
     * @return
     */
    WorkerPo available(String type);

    /**
     * 更新状态
     *
     * @param workerId
     * @param status
     * @return
     */
    int updateStatus(Integer workerId, String status);

    /**
     * 查询卡状态
     *
     * @param status
     * @param add
     * @return
     */
    List<WorkerPo> selectStuck(String status, Date add);
}
