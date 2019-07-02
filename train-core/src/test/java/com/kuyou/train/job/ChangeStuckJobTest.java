package com.kuyou.train.job;

import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.job.stuck.ChangeStuckJob;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * ChangeStuckJobTest
 *
 * @author taokai3
 * @date 2018/11/7
 */
@Slf4j
public class ChangeStuckJobTest extends MvcBaseTest {

    @Resource
    ChangeStuckJob changeStuckJob;

    @Test
    public void resetStuckStatus() {
        changeStuckJob.resetStuckStatus();
    }
}
