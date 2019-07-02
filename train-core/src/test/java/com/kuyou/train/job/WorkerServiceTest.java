package com.kuyou.train.job;

import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.dao.WorkerMapper;
import com.kuyou.train.service.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * WorkerServiceTest
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Slf4j
public class WorkerServiceTest extends MvcBaseTest {

    @Resource
    private WorkerService workerService;

    @Resource
    WorkerMapper workerMapper;

    @Test
    public void selectAvailable() {
        log.info("workerService:{}", workerService.available("7"));
    }
}
