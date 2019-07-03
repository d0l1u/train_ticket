package com.l9e.train.po;

/**
 * 脚本机器权重记录实体
 * @author wangsf
 *
 */
public class WorkerWeight {
	
	/**
	 * lua脚本机器权重
	 */
	public static final String WEIGHT_LUA = "lua";
	/**
	 * java脚本机器权重
	 */
	public static final String WEIGHT_JAVA = "java";
	
	private String category;
	private Integer weight;

	public WorkerWeight() {
		super();
	}

	public WorkerWeight(String category, Integer weight) {
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
