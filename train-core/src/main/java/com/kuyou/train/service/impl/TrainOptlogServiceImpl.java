package com.kuyou.train.service.impl;

import com.kuyou.train.dao.TrainOptlogMapper;
import com.kuyou.train.entity.po.TrainOptlog;
import com.kuyou.train.service.TrainOptlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TrainOptlogServiceImpl
 *
 * @author taokai3
 * @date 2018/12/26
 */
@Slf4j
@Service
public class TrainOptlogServiceImpl implements TrainOptlogService {

    @Resource
    private TrainOptlogMapper trainOptlogMapper;

    @Override
    public TrainOptlog select4Book(String message) {
        TrainOptlog trainOptlog = trainOptlogMapper.select4Book(message);
        log.info("根据message查出optLog:{}-message:{}", trainOptlog, message);
        return trainOptlog;
    }
}
