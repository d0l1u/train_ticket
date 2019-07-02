package com.kuyou.train.service;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.entity.po.ChangeCpPo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * ChangeCpServiceTest
 *
 * @author taokai3
 * @date 2018/11/5
 */
@Slf4j
public class ChangeCpServiceTest extends MvcBaseTest {

    @Resource
    private ChangeCpService changeCpService;

    //"newCpId":"EXCP180109160122101"
    //"cpId":"EXCP1708121626261073"

    @Test
    public void selectByChangeId() {
        List<ChangeCpPo> changeCpPos = changeCpService.selectByChangeId(Integer.valueOf(442619));
        log.info("selectByStatus结果:{}", JSON.toJSONString(changeCpPos));
    }

    @Test
    public void selectByStatus() {

    }
}
