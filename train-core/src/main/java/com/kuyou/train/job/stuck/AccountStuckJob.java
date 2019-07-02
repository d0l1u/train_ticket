package com.kuyou.train.job.stuck;

import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * AccountStuckJob
 *
 * @author taokai3
 * @date 2018/12/5
 */
@Slf4j
//@Component("accountStuckJob")
public class AccountStuckJob {

    @Resource
    private AccountService accountService;

    @MDCLog
    @Scheduled(cron = "0/10 * 5-23 * * ?")
    public void resetStuckStatus() {
        log.info("********* 重置【账号】卡单 *********");


    }
}
