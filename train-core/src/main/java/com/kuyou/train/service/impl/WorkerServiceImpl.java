package com.kuyou.train.service.impl;

import com.kuyou.train.common.status.WorkerStatus;
import com.kuyou.train.dao.WorkerMapper;
import com.kuyou.train.entity.po.WorkerPo;
import com.kuyou.train.service.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * WorkerServiceImpl
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Slf4j
@Service
public class WorkerServiceImpl implements WorkerService {

    @Resource
    private WorkerMapper workerMapper;

    @Override
    public WorkerPo available(String type) {
        //查询出全部的可用机器人
        List<WorkerPo> list = workerMapper.selectAllAvailable(type, WorkerStatus.FREE);
        for (WorkerPo po : list) {
            Integer workerId = po.getWorkerId();
            int result = workerMapper.updateStatusPre(workerId, WorkerStatus.ING, WorkerStatus.FREE);
            if (result > 0) {
                return po;
            }
        }
        return null;
    }

    @Override
    public int updateStatus(Integer workerId, String status) {
        int result = workerMapper.updateStatus(workerId, status);
        log.info("WorkerService.updateStatus 更新结果:{}", result);
        return result;
    }

    @Override
    public List<WorkerPo> selectStuck(String status, Date time) {
        return workerMapper.selectStuck(status, time);
    }
}
