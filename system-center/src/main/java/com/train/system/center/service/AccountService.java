package com.train.system.center.service;

import com.train.system.center.entity.Account;

/**
 * AccountService
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface AccountService {

    int updateAccount(Account account);

    Account selectById(Integer accountId);

    int insertFilter(Account account);
}
