package com.train.system.center.dao;

import com.train.system.center.entity.Account;
import org.apache.ibatis.annotations.Param;

/**
 * AccountDao
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface AccountDao {

    int updateAccount(@Param("account") Account account);

    Account selectById(@Param("accountId") Integer accountId);

    int insertFilter(@Param("account") Account account);
}
