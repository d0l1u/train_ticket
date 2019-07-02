package com.kuyou.train.dao;

import com.kuyou.train.entity.po.AccountPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ChangeCpMapper
 *
 * @author taokai3
 * @date 2018/10/28
 */
public interface AccountMapper {

    /**
     * 根据ID查询账号信息
     *
     * @param accountId
     * @return
     */
    AccountPo selectByAccountId(@Param("accountId") Integer accountId);

    /**
     * 达上限账号
     *
     * @param limit
     * @return
     */
    List<AccountPo> selectUpperLimit4Jd(@Param("limit") int limit);

    /**
     * 更新状态
     *
     * @param status
     * @param ids
     * @return
     */
    int updateStatusByIds(@Param("status") String status, @Param("ids") List<Integer> ids);

    /**
     * 删除乘客
     *
     * @param ids
     * @return
     */
    int deletePassenger(@Param("ids") List<Integer> ids);

    /**
     * 查询
     *
     * @param ids
     * @return
     */
    List<AccountPo> selectByAccountIds(@Param("ids") List<Integer> ids);

    /**
     * 停用账号
     * @param accountId
     * @param stopCode
     * @return
     */
    int stop(@Param("accountId")Integer accountId, @Param("stopCode")String stopCode);

    /**
     * 根据username
     *
     * @param username
     * @return
     */
    AccountPo selectByUsername(@Param("username") String username);

    /**
     * 更新账号数据
     *
     * @param accountPo
     * @param accountId
     * @return
     */
    int update(@Param("accountPo") AccountPo accountPo, @Param("accountId") int accountId);

    int updateWhiteListStatus(@Param("ids")List<Integer> ids,@Param("status") String status);
}