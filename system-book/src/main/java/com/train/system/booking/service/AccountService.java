package com.train.system.booking.service;

import com.train.system.booking.entity.Account;
import com.train.system.booking.entity.Passenger;

import java.util.List;

/**
 * AccountService
 *
 * @author taokai3
 * @date 2018/6/17
 */
public interface AccountService {

    Account selectByWhiteList(List<Passenger> passengerList);

    Account seletById(int accountId);

}
