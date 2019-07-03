package com.l9e.train.supplier.po;

/**
 * 权重记录实体
 * @author licheng
 *
 */
public class WeightCategory {
	
	/**
	 * 推送权重
	 */
	public static final String WEIGHT_PROPELL = "propell";
	/**
	 * 拉取权重
	 */
	public static final String WEIGHT_PULL = "pull";
	
	private String category;
	private Integer weight;

	public WeightCategory() {
		super();
	}

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
