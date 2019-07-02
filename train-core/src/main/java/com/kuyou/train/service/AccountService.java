package com.kuyou.train.service;

import com.kuyou.train.entity.po.AccountPo;

import java.util.List;

/**
 * AccountService
 *
 * @author taokai3
 * @date 2018/11/6
 */
public interface AccountService {

    /**
     * 根据ID查询账号信息
     *
     * @param accountId
     * @return
     */
    AccountPo selectByAccountId(Integer accountId);

    /**
     * 查询达上限账号
     *
     * @param limit
     * @return
     */
    List<AccountPo> selectUpperLimit4Jd(int limit);


    /**
     * 更新账号状态
     *
     * @param status
     * @param ids
     * @return
     */
    int updateStatusByIds(String status, List<Integer> ids);

    /**
     * 删除乘客
     *
     * @param ids
     * @return
     */
    int deletePassenger(List<Integer> ids);

    /**
     * 查询
     *
     * @param ids
     * @return
     */
    List<AccountPo> selectByAccountIds(List<Integer> ids);

    /**
     * 停用账号
     * @param accountId
     * @param stopCode
     * @return
     */
	int stop(Integer accountId, String stopCode);

    /**
     * 根据username
     *
     * @param username
     * @return
     */
    AccountPo selectByUsername(String username);

    /**
     * 更新常用联系人个数
     *
     * @param accountPo
     * @param accountId
     * @return
     */
    int update(AccountPo accountPo, int accountId);

    /**
     * 根据身份证号，获取账号
     * @param idcardNoList
     * @return
     */
    List<AccountPo> getAccountByWhiteList(List<String> idcardNoList);
}
