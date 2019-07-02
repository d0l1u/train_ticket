package com.train.system.center.service.impl;

import com.train.system.center.dao.ContactDao;
import com.train.system.center.entity.Contact;
import com.train.system.center.service.ContactService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ContactServiceImpl
 *
 * @author taokai3
 * @date 2018/7/4
 */
@Service("contactService")
public class ContactServiceImpl implements ContactService {


    @Resource
    private ContactDao contactDao;

    @Override
    public int deleteByAccountId(Integer accountId, List<String> cardList) {


        return contactDao.deleteByAccountId(accountId, cardList);
    }

    @Override
    public int insertOrUpdate(Contact contact) {
        return contactDao.insertOrUpdate(contact);
    }

    @Override
    public int deleteAllByAccountId(Integer accountId) {
        return contactDao.deleteAllByAccountId(accountId);
    }
}
