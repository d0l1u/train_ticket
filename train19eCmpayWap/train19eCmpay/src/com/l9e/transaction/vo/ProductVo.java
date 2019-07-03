package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品信息Vo
 * @author zhangjun
 *
 */
public class ProductVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String product_id;//产品id
	
	private Integer type;//产品类别
	
	private String name;//产品名称
	
	private Integer status;//产品状态
	
	private String province_id;//所属省份id
	
	private String city_id;//所属城市id
	
	private String sale_type;//计费方式 0：元/件 1：元/月 2：元/年
	
	private Float sale_price;//售价
	
	private Date create_time;//创建时间
	
	private String describe;//描述
	
	private String level;//级别 0：普通 1：vip

	public String getSale_type() {
		return sale_type;
	}

	public void setSale_type(String sale_type) {
		this.sale_type = sale_type;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getProvince_id() {
		return province_id;
	}

	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public Float getSale_price() {
		return sale_price;
	}

	public void setSale_price(Float sale_price) {
		this.sale_price = sale_price;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
