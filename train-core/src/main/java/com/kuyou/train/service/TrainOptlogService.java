package com.kuyou.train.service;

import com.kuyou.train.entity.po.TrainOptlog;
import org.apache.ibatis.annotations.Param;

/**
 * TrainOptlogService
 *
 * @author taokai3
 * @date 2018/12/26
 */
public interface TrainOptlogService {

    /**
     * 获取操作mapping
     * @param message
     * @return
     */
    TrainOptlog select4Book( String message);
}
