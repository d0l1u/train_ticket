package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * 代理商Vo
 * @author zhangjun
 *
 */
public class AgentVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String user_id;//代理商id
	
	private String province_id;//省id
	
	private String city_id;//市id
	
	private String district_id;//区（县）id
	
	private String shop_type;//店铺类型
	
	private String shop_name;//店铺名称
	
	private String shop_short_name;//店铺简称	
	
	private String user_name;//代理商姓名
	
	private String user_phone;//联系电话
	
	private String user_qq;//qq
	
	private String user_address;//配送地址
	
	private String user_level;//级别 0 普通用户 1 vip用户
	
	private String estate;//状态 00需要付费 11等待审核 22审核未通过 33审核通过 44需要续费
	
	private String apply_time;//申请时间
	
	private String begin_time;//开始时间
	
	private String end_time;//结束时间
	
	private String opt_person;//操作人
	
	private String product_id;//当前购买产品id
	
	private String jm_order_id;//最近购买加盟订单号
	
	private String province_name;
	
	private String city_name;
	
	private String district_name;
	
	private String is_probation;//是否为试用期
	
	private String ext_channel;//合作商户的渠道

	public String getIs_probation() {
		return is_probation;
	}

	public void setIs_probation(String is_probation) {
		this.is_probation = is_probation;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_qq() {
		return user_qq;
	}

	public void setUser_qq(String user_qq) {
		this.user_qq = user_qq;
	}

	public String getUser_address() {
		return user_address;
	}

	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}

	public String getUser_level() {
		return user_level;
	}

	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}

	public String getEstate() {
		return estate;
	}

	public void setEstate(String estate) {
		this.estate = estate;
	}

	public String getApply_time() {
		return apply_time;
	}

	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getOpt_person() {
		return opt_person;
	}

	public void setOpt_person(String opt_person) {
		this.opt_person = opt_person;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getJm_order_id() {
		return jm_order_id;
	}

	public void setJm_order_id(String jm_order_id) {
		this.jm_order_id = jm_order_id;
	}

	public String getShop_type() {
		return shop_type;
	}

	public void setShop_type(String shop_type) {
		this.shop_type = shop_type;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_short_name() {
		return shop_short_name;
	}

	public void setShop_short_name(String shop_short_name) {
		this.shop_short_name = shop_short_name;
	}

	public String getExt_channel() {
		return ext_channel;
	}

	public void setExt_channel(String extChannel) {
		ext_channel = extChannel;
	}

}
