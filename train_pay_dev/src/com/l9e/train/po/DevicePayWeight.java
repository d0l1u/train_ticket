package com.l9e.train.po;

/**
 * 设备权重记录实体
 * @author qinsg
 *
 */
public class DevicePayWeight {
	
	/**
	 * pc权重
	 */
	public static final String PAY_WEIGHT_PC = "pc";
	/**
	 * app权重
	 */
	public static final String PAY_WEIGHT_APP = "app";
	
	private String category;
	private Integer weight;
		  
	public DevicePayWeight() {
		super();
	}

	public DevicePayWeight(String category, Integer weight) {
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
