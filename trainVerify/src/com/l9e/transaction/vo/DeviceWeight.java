package com.l9e.transaction.vo;

/**
 * 设备权重记录实体
 * @author licheng
 *
 */
public class DeviceWeight {
	
	/**
	 * 推送权重
	 */
	public static final String WEIGHT_PC = "pc";
	/**
	 * 拉取权重
	 */
	public static final String WEIGHT_APP = "app";
	
	private String category;
	private Integer weight;

	public DeviceWeight() {
		super();
	}

	public DeviceWeight(String category, Integer weight) {
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
