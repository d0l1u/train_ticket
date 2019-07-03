package com.train.system.booking.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * WeightUtil
 *
 * @author taokai3
 * @date 2018/6/18
 */
public class WeightUtil {

    private Logger logger = LoggerFactory.getLogger(WeightUtil.class);

    public String randomWeight(WeightCategory... categorys) {
        logger.info("WeightCategory Length:{}", categorys.length);

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

    public static
    class WeightCategory {

        private String category;
        private Integer weight;

        public WeightCategory(String category, Integer weight) {
            super();
            this.setCategory(category);
            this.setWeight(weight);
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}

