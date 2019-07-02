package com.kuyou.train.service.impl;

import com.google.common.collect.Lists;
import com.kuyou.train.common.status.AccountStatus;
import com.kuyou.train.dao.AccountMapper;
import com.kuyou.train.entity.po.AccountPo;
import com.kuyou.train.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AccountServiceImpl
 *
 * @author taokai3
 * @date 2018/11/6
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public AccountPo selectByAccountId(Integer accountId) {
        return accountMapper.selectByAccountId(accountId);
    }

    @Override
    public List<AccountPo> selectUpperLimit4Jd(int limit) {
        return accountMapper.selectUpperLimit4Jd(limit);
    }


    @Override
    public int updateStatusByIds(String status, List<Integer> ids) {
        //修改白名单中的状态为临时停用
        accountMapper.updateWhiteListStatus(ids, AccountStatus.TEMP_STOP);

        return accountMapper.updateStatusByIds(status, ids);
    }

    @Override
    public int deletePassenger(List<Integer> ids) {
        return accountMapper.deletePassenger(ids);
    }

    @Override
    public List<AccountPo> selectByAccountIds(List<Integer> ids) {
        return accountMapper.selectByAccountIds(ids);
    }

    @Override
    public int stop(Integer accountId, String stopCode) {
        //删除白名单
        if (!stopCode.equals("3")) {
            int delete = accountMapper.deletePassenger(Lists.newArrayList(accountId));
            log.info("白名单删除结果:{}", delete);
        }
        return accountMapper.stop(accountId, stopCode);
    }

    @Override
    public AccountPo selectByUsername(String username) {
        return accountMapper.selectByUsername(username);
    }

    @Override
    public int update(AccountPo accountPo, int accountId) {
        return accountMapper.update(accountPo, accountId);
    }

    @Override
    public List<AccountPo> getAccountByWhiteList(List<String> idcardNoList) {

        return null;
    }
}
