package com.kuyou.train.service;

import com.alibaba.fastjson.JSONObject;
import com.kuyou.train.MvcBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * CallBackServiceTest
 *
 * @author taokai3
 * @date 2018/11/9
 */
@Slf4j
public class CallBackServiceTest extends MvcBaseTest {

    @Resource
    private CallBackService callBackService;

    @Test
    public void bookPay() {
        JSONObject json = new JSONObject();
        json.put("", "");
        json.put("", "");
        json.put("", "");
        json.put("", "");
        callBackService.bookPay(json.toJSONString());
    }
}
