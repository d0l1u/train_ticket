package com.kuyou.train.http;

import com.kuyou.train.common.util.MD5Util;
import com.kuyou.train.common.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * CtripTest
 *
 * @author taokai3
 * @date 2018/10/30
 */
@Slf4j
public class CtripTest {

    @Test
    public void searchS2S() {
        String fromStationName = "上海";
        String toStationName = "重庆";
        String fromDateStr = "2018-11-03";
        String trainCode = "D952";
        // 从携程获取车次信息
        StringBuffer sb = new StringBuffer();
        String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
        sb.append("From=").append(fromStationName)//
                .append("&To=").append(toStationName)//
                .append("&DepartDate=").append(fromDateStr)//
                .append("&TrainNo=").append(trainCode)//
                .append("&User=19e")//
                .append("&timeStamp=").append(timeStamp)//
                .append("&Sign=").append(MD5Util.md5(timeStamp + "7a692b08bb10a9c0681cc54697e8447d", "utf-8"));
        String result = OkHttpUtil.httpPost("http://m.ctrip.com/restapi/soa2/12976/json/SearchS2S", sb.toString());
        log.info("携程返回结果:{}", result);
    }

}
