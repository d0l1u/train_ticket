package com.kuyou.train.service;

import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.entity.po.AccountPo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * AccountService
 *
 * @author taokai3
 * @date 2018/11/6
 */
@Slf4j
public class AccountServiceTest extends MvcBaseTest {

    @Resource
    private AccountService accountService;

    @Test
    public void selectByAccountId() {
        AccountPo accountPo = accountService.selectByAccountId(846);
        log.info("账号信息:{}", accountPo);
    }
}
