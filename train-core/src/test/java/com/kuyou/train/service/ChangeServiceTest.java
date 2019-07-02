package com.kuyou.train.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.entity.po.ChangePo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * ChangeServiceTest
 *
 * @author taokai3
 * @date 2018/11/2
 */
@Slf4j
public class ChangeServiceTest extends MvcBaseTest {

    @Resource
    private ChangeService changeService;

    @Test
    public void selectByStatus() {
        List<ChangePo> changePoList = changeService
                .selectByStatus(Lists.newArrayList(ChangeStatus.CHANGE_WAIT, ChangeStatus.CHANGE_INIT), 3);
        log.info("selectByStatus结果:{}", JSON.toJSONString(changePoList));
    }

    @Test
    public void selectByChangeId() {
        ChangePo changePo = changeService.selectByChangeId(Integer.valueOf(487618));
        log.info("selectByStatus结果:{}", changePo);
    }
}
