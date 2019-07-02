package com.kuyou.train.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * WeightUtil
 *
 * @author taokai3
 * @date 2018/6/18
 */
@Slf4j
public class WeightUtil {

    public String randomWeight(WeightCategory... categorys) {
        log.info("WeightCategory Length:{}", categorys.length);

        Integer weightSum = 0;
        for (WeightCategory wc : categorys) {
            weightSum += wc.getWeight();
        }

        if (weightSum <= 0) {
            throw new RuntimeException("Weight Sum <= 0");
        }

        // n in [0, weightSum)
        Integer n = new Random().nextInt(weightSum);
        Integer m = 0;
        for (WeightCategory wc : categorys) {
            if (m <= n && n < m + wc.getWeight()) {

                return wc.getCategory();
            }
            m += wc.getWeight();
        }
        return "";
    }

    @Data
    public static class WeightCategory {

        private String category;
        private Integer weight;

        public WeightCategory(String category, Integer weight) {
            super();
            this.setCategory(category);
            this.setWeight(weight);
        }


    }
}

