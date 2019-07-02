package com.kuyou.train.common.util;

import java.math.BigDecimal;

/**
 * BigDecimalUtil
 *
 * @author taokai3
 * @date 2018/11/22
 */
public class BigDecimalUtil {

    public static BigDecimal dividePrice(int price) {
        return new BigDecimal(price).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal divideTime(int time) {
        return new BigDecimal(time).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal divideTime(BigDecimal time) {
        return time.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
    }
}
