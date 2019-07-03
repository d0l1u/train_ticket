package com.l9e.train.util;

import java.util.Random;

public class WeightRandomUtil {

	public String random(WeightCategory... weightCategorys) {
		
		//权重总和
		Integer weightSum = 0;
		for (WeightCategory wc : weightCategorys) {
			weightSum += wc.getWeight();
		}

		// if (weightSum <= 0) {
		// System.err.println("Error: weightSum=" + weightSum.toString());
		// return;
		// }

		//根据权重区间获取一个随机数。 random in [0, weightSum]
		Integer random = new Random().nextInt(weightSum);  
		Integer temp = 0;
		//第一次计算 random between temp and weight。 temp为0
		// 如果走到第二次或者更多次，需要将temp + 上一次的weight，以便在本次计算中适用  random between temp and weight 这个公式
		for (WeightCategory wc : weightCategorys) {
			if (temp <= random && random < temp + wc.getWeight()) {
				return wc.getCategory();
			}
			temp += wc.getWeight();
		}
		return "";
	}
	
	public static class WeightCategory {
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
