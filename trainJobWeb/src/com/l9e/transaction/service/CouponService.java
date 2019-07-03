package com.l9e.transaction.service;

import java.util.List;

import com.l9e.transaction.vo.Coupon;

/**
 * <p>
 * Title: CouponService.java
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年3月7日
 */

public interface CouponService {
	
	public void insertCoupons(List<Coupon> list);

	public List<Integer> selectExpiredCouponsOfJdAccount(String channal);

	public void disableExpiredCoupons(); 
}
