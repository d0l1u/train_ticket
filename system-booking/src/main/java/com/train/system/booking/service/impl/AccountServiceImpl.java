package com.train.system.booking.service.impl;

import com.train.system.booking.dao.AccountMapper;
import com.train.system.booking.entity.Account;
import com.train.system.booking.entity.Passenger;
import com.train.system.booking.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountServiceImpl
 *
 * @author taokai3
 * @date 2018/6/17
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Resource
    private AccountMapper accountMapper;

    @Override
    public Account selectByWhiteList(List<Passenger> passengerList) {
        List<String> cardList = new ArrayList<>();
        for (int i = 0; passengerList != null && i < passengerList.size(); i++) {
            Passenger passenger = passengerList.get(i);
            String cardNo = passenger.getCardNo();
            cardNo = cardNo.toUpperCase().replace("\\s", "");
            cardList.add(cardNo);
        }
        logger.info("白名单匹配乘客证件列表:{}", cardList);
        return null;
    }

    @Override
    public Account seletById(int accountId) {

        return null;
    }
}
