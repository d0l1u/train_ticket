package com.kuyou.train.job;

import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.AccountStatus;
import com.kuyou.train.entity.po.AccountPo;
import com.kuyou.train.entity.po.JdAccountPo;
import com.kuyou.train.service.AccountService;
import com.kuyou.train.service.JdAccountService;
import com.kuyou.train.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * K618AccountTransition
 *
 * @author taokai3
 * @date 2018/12/5
 */
@Slf4j
@Component("k618AccountTransitionJob")
public class K618AccountTransitionJob {

    /**
     * 0:高活跃账号，1:低活跃账号
     */
    private static final Integer TYPE_A = 0;
    private static final Integer COUNT_A = 50000;

    private static final Integer TYPE_B = 1;
    private static final Integer COUNT_B = 300000;

    private static final Integer INCR = 1000;


    @Resource
    private JdAccountService jdAccountService;

    @Resource
    private AccountService accountService;

    @Resource
    private OrderService orderService;

    @MDCLog
    @Scheduled(cron = "0/10 * * * * ?")
    public void account() {
        //先获取A类账号总数
        int count = jdAccountService.selectCountType(TYPE_A);
        log.info("A类账号已有:{}", count);

        //查询ID List
        /*
        if (count < COUNT_A) {
            List<Integer> idList = orderService.selectAccountId4Jd();
            log.info("A类账号查询结果:{}", idList.size());
            if (!idList.isEmpty()) {
                int size = idList.size();
                int index = 0;
                while (index < size) {
                    int temp = index;
                    index += INCR;
                    if (index > size) {
                        index = size;
                    }
                    List<Integer> subList = idList.subList(temp, index);
                    //查询账号密码
                    List<AccountPo> accountPos = accountService.selectByAccountIds(subList);
                    if (CollectionUtils.isEmpty(accountPos)) {
                        log.info("查询结果为空，再次获取");
                        continue;
                    }
                    List<JdAccountPo> convert = convert(accountPos, TYPE_A);
                    //insert into 过渡表
                    int batch = jdAccountService.insertBatch(convert);
                    log.info("A类账号插入结果:{}-{}", subList.size(), batch);
                }
            } else {
                log.info("A类账号不足");
            }
        }
         */

        //在获取B类账号总数
        count = jdAccountService.selectCountType(TYPE_B);
        log.info("B类账号已有:{}", count);
        if (count < COUNT_B) {
            //获取B账号
            int limit = COUNT_B - count < INCR ? COUNT_B - count : INCR;
            //将账号插入过渡表中
            List<AccountPo> accountPos = accountService.selectUpperLimit4Jd(limit);
            if (accountPos.isEmpty()) {
                log.info("B类账号不足");
                return;
            }

            //转换
            List<JdAccountPo> convert = convert(accountPos, TYPE_B);
            int batch = jdAccountService.insertBatch(convert);
            log.info("B类账号插入结果:{}", batch);
        }
    }

    private List<JdAccountPo> convert(List<AccountPo> accountPos, int type) {
        List<JdAccountPo> jdAccountPos = new ArrayList<>(accountPos.size());
        List<Integer> ids = new ArrayList<>(accountPos.size());
        for (AccountPo accountPo : accountPos) {
            ids.add(accountPo.getAccountId());
            JdAccountPo jdAccountPo = JdAccountPo.builder().accountId(Long.valueOf(accountPo.getAccountId()))
                    .username(accountPo.getUsername()).password(accountPo.getPassword())
                    .created(new Date()).modified(new Date()).type(type).build();
            jdAccountPos.add(jdAccountPo);
        }
        // 修改 status = jd
        int update = accountService.updateStatusByIds(AccountStatus.JD, ids);
        log.info("修改状态结果:{}-{}", accountPos.size(), update);
        int delete = accountService.deletePassenger(ids);
        log.info("删除常旅结果:{}-{}", accountPos.size(), delete);
        return jdAccountPos;
    }
}
