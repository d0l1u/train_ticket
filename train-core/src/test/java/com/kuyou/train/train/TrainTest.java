package com.kuyou.train.train;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * TrainTest
 *
 * @author taokai3
 * @date 2018/11/8
 */
@Slf4j
public class TrainTest {

    @Test
    public void test() {
        String message = "BaGugDIyvs%3D|预订|2400000K230O|K23|BJP|RLC|BJP|RLC|07:27|20:18|12:51|Y|OsWNDlveJxPn6rdA%2B7xV1g%3D%3D|20181110|0|PA|01|05|0|0|||18||||||||||||50|5|0";
        int length = message.split("\\|").length;
        // 将 ||||| 情况转成 |x|x|x|情况
        if (length == 20) {
            message = "$" + message + "$";
            message = message.replaceAll("\\|\\|", "\\|\\$\\|");
            message = message.replaceAll("\\|\\|", "\\|\\$\\|");
        }
        String[] arr = message.split("\\|");

        //高级软/动卧:21
        //其他:包厢硬卧/一人软包:22
        //软 卧:23
        //软座:24
        //特等座:25
        //无 座:26
        //硬 卧:28
        //硬 座:29
        //二等座:30
        //一等座:31
        //商务座:32
        //动 卧:33
        for (int i = 0; i < arr.length; i++) {
            String train = arr[i];
            System.err.println(i + "-" + train);
        }
    }

    @Test
    public void zhifubao(){
        String name = "jike29@19e.com.cn";
        log.info("支付宝:{}", name.split("@")[0]);
    }
}
