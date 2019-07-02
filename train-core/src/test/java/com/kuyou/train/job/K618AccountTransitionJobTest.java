package com.kuyou.train.job;

import com.kuyou.train.MvcBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * K618AccountTransitionJobTest
 *
 * @author taokai3
 * @date 2018/12/5
 */
@Slf4j
public class K618AccountTransitionJobTest extends MvcBaseTest {

    @Resource
    private K618AccountTransitionJob k618AccountTransitionJob;

    @Test
    public void account() {
        k618AccountTransitionJob.account();
    }
}
