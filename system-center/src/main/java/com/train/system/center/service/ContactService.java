package com.train.system.center.service;

import com.train.system.center.entity.Contact;

import java.util.List;

/**
 * ContactService
 *
 * @author taokai3
 * @date 2018/7/4
 */
public interface ContactService {

    int deleteByAccountId(Integer accountId, List<String> cardList);

    int insertOrUpdate(Contact contact);

    int deleteAllByAccountId(Integer accountId);
}
