package com.train.system.center.dao;

import com.train.system.center.entity.Contact;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ContactDao
 *
 * @author taokai3
 * @date 2018/7/4
 */
public interface ContactDao {

    int deleteByAccountId(@Param("accountId") Integer accountId, @Param("cardList") List<String> cardList);

    int insertOrUpdate(@Param("contact") Contact contact);

    int deleteAllByAccountId(@Param("accountId") Integer accountId);
}
