package com.kuyou.train.service;

import com.kuyou.train.MvcBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * HistoryServiceTest
 *
 * @author taokai3
 * @date 2018/11/6
 */
@Slf4j
public class HistoryServiceTest extends MvcBaseTest {

    @Resource
    private HistoryService historyService;

    @Test
    public void insertBookLog() {
        int result = historyService.insertBookLog("", "");
        log.info("插入预订日志结果:{}", result);
    }

    @Test
    public void insertChangeLog() {
        int result = historyService.insertChangeLog("", 0, "");
        log.info("插入改签日志结果:{}", result);
    }
}
