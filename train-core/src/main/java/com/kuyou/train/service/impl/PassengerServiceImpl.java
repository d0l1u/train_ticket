package com.kuyou.train.service.impl;

import com.kuyou.train.dao.PassengerMapper;
import com.kuyou.train.entity.po.PassengerPo;
import com.kuyou.train.service.PassengerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * PassengerServiceImpl
 *
 * @author taokai3
 * @date 2018/12/29
 */
@Slf4j
@Service
public class PassengerServiceImpl implements PassengerService {

    @Resource
    private PassengerMapper passengerMapper;


    @Override
    public int deleteNotExist(Integer accountId, List<String> cardNoList) {
        return passengerMapper.deleteNotExist(accountId, cardNoList);
    }

    @Override
    public int insertOrUpdate(List<PassengerPo> passengers) {
        return passengerMapper.insertOrUpdate(passengers);
    }
}
