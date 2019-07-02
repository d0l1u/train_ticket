package com.train.system.center.service.impl;

import com.train.system.center.dao.AccountDao;
import com.train.system.center.entity.Account;
import com.train.system.center.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AccountServiceImpl
 *
 * @author taokai3
 * @date 2018/6/25
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Override
    public int updateAccount(Account account) {
        return accountDao.updateAccount(account);
    }

    @Override
    public Account selectById(Integer accountId) {
        return accountDao.selectById(accountId);
    }

    @Override
    public int insertFilter(Account account) {
        return accountDao.insertFilter(account);
    }
}
