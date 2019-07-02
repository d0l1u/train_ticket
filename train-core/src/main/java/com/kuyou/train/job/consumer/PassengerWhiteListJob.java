package com.kuyou.train.job.consumer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.enums.CardType;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.entity.dto.AccountDto;
import com.kuyou.train.entity.po.AccountPo;
import com.kuyou.train.entity.po.PassengerPo;
import com.kuyou.train.service.AccountService;
import com.kuyou.train.service.PassengerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * PassengerWhiteListJob : 20 秒一次，获取30个
 *
 * @author taokai3
 * @date 2018/12/5
 */
@Slf4j
@Component("passengerWhiteListJob")
public class PassengerWhiteListJob {

    @Resource
    protected JedisClient orderJedisClient;

    @Resource
    private AccountService accountService;

    @Resource
    private PassengerService passengerService;

    @MDCLog
    @Scheduled(cron = "0/2 * 5-23 * * ?")
    public void account() {

        //从redis中获取数据
        String rpop = orderJedisClient.rpop(KeyConstant.WHITE_lIST);
        if (StringUtils.isBlank(rpop)) {
            return;
        }

        log.info("白名单更新数据:{}", rpop);
        AccountDto accountDto = JSON.parseObject(rpop, AccountDto.class);


        //根据accountId 账号
        AccountPo accountPo = accountService.selectByUsername(accountDto.getUsername());
        Integer accountId = accountPo.getAccountId();
        String channel = accountPo.getChannel();
        log.info("accountId:{}, channel:{}", accountId, channel);

        if(!"19e".equals(channel)){
            log.info("分销商传入账号，不进行更新操作");
            return;
        }

        List<PassengerPo> passengers = accountDto.getPassengers();
        int size = passengers.size();

        //更新账号表乘客数量
        AccountPo update = new AccountPo();
        update.setContactsNum(size);
        accountService.update(update, accountId);

        List<String> cardNoList = Lists.newArrayList();
        for (PassengerPo po : passengers) {
            cardNoList.add(po.getCardNo());
            po.setAccountId(accountId);
            po.setContactsNum(size);
            po.setUsername(accountDto.getUsername());
            po.setPassword(accountDto.getPassword());
            String cardType = po.getCardType();
            CardType byKyfw = CardType.getByKyfw(cardType);
            po.setCardType(byKyfw.getKy());
        }

        //删除不在列表中的乘客
        log.info("乘客ID集合:{}", cardNoList);
        int delete = passengerService.deleteNotExist(accountId, cardNoList);
        log.info("accountId:{} 不存在乘客删除结果:{}", accountId, delete);

        //插入or更新常旅白名单表
        int insertOrUpdate = passengerService.insertOrUpdate(passengers);
        log.info("accountId:{} 白名单更新结果:{}", accountId, insertOrUpdate);
    }
}
