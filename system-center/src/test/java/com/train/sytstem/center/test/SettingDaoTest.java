package com.train.sytstem.center.test;

import com.train.system.center.dao.HistoryDao;
import com.train.system.center.entity.Contact;
import com.train.system.center.service.ContactService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SettingDaoTest
 *
 * @author taokai3
 * @date 2018/6/25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-application.xml"})
public class SettingDaoTest {

    @Resource
    private HistoryDao historyDao;

    @Test
    public void queryValueByName() {
        int insert = historyDao.insertBookingHistory("123", "message", "system");
        System.err.println("insert:"+insert);
    }
}
