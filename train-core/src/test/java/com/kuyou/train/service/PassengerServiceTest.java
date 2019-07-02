package com.kuyou.train.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.common.enums.CardType;
import com.kuyou.train.entity.dto.AccountDto;
import com.kuyou.train.entity.po.PassengerPo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * PassengerService
 *
 * @author taokai3
 * @date 2018/12/29
 */
@Slf4j
public class PassengerServiceTest extends MvcBaseTest {

    @Resource
    private PassengerService passengerService;

    @Test
    public void test(){
    String rpop =
        "{\"username\":\"ldrp5373\",\"password\":\"kuom0208\",\"passengers\":[{\"name\":\"黄文婷\",\"cardType\":\"1\",\"cardNo\":\"441622198101244988\",\"isUserSelf\":\"N\",\"deleteTime\":1534089600000},{\"name\":\"胡卫莉\",\"cardType\":\"1\",\"cardNo\":\"342401198704012662\",\"isUserSelf\":\"N\",\"deleteTime\":1545148800000},{\"name\":\"廖竞藩\",\"cardType\":\"1\",\"cardNo\":\"441425195102075658\",\"isUserSelf\":\"Y\",\"deleteTime\":-580896000000},{\"name\":\"龙乔珍\",\"cardType\":\"1\",\"cardNo\":\"522229200009130068\",\"isUserSelf\":\"N\",\"deleteTime\":1559145600000},{\"name\":\"王文竞\",\"cardType\":\"1\",\"cardNo\":\"23012819940527302X\",\"isUserSelf\":\"N\",\"deleteTime\":1561564800000,\"lastUseTime\":1546070921141},{\"name\":\"杨昕\",\"cardType\":\"1\",\"cardNo\":\"320402197412310015\",\"isUserSelf\":\"N\",\"deleteTime\":1529078400000},{\"name\":\"余文胜\",\"cardType\":\"1\",\"cardNo\":\"51352319731108371X\",\"isUserSelf\":\"N\",\"deleteTime\":1559664000000},{\"name\":\"余展辉\",\"cardType\":\"1\",\"cardNo\":\"441425197811282333\",\"isUserSelf\":\"N\",\"deleteTime\":1534089600000},{\"name\":\"袁张凯\",\"cardType\":\"1\",\"cardNo\":\"330683199610050410\",\"isUserSelf\":\"N\",\"deleteTime\":1552233600000},{\"name\":\"张楠\",\"cardType\":\"1\",\"cardNo\":\"330602199611092542\",\"isUserSelf\":\"N\",\"deleteTime\":1552233600000},{\"name\":\"朱章菊\",\"cardType\":\"1\",\"cardNo\":\"513523197408123925\",\"isUserSelf\":\"N\",\"deleteTime\":1538582400000}]}";
        AccountDto accountDto = JSON.parseObject(rpop, AccountDto.class);
        List<PassengerPo> passengers = accountDto.getPassengers();
        int size = passengers.size();
        List<String> cardNoList = Lists.newArrayList();
        for (PassengerPo po : passengers) {
            cardNoList.add(po.getCardNo());
            po.setAccountId(12345);
            po.setContactsNum(size);
            po.setUsername(accountDto.getUsername());
            po.setPassword(accountDto.getPassword());
            String cardType = po.getCardType();
            CardType byKyfw = CardType.getByKyfw(cardType);
            po.setCardType(byKyfw.getKy());
        }
        log.info("乘客数据:{}", passengers);
        int insertOrUpdate = passengerService.insertOrUpdate(passengers);
        log.info("白名单更新结果:{}", insertOrUpdate);
    }
}
