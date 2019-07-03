package com.l9e.transaction.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CouponDao;
import com.l9e.transaction.service.CouponService;
import com.l9e.transaction.vo.Coupon;

/**
 * <p>
 * Title: CouponServiceImpl.java
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年3月7日
 */

@Service
public class CouponServiceImpl implements CouponService{

	@Resource
	private CouponDao couponDao;
	
	@Override
	public void insertCoupons(List<Coupon> list) {
		couponDao.insertCoupons(list);
	}

	@Override
	public List<Integer> selectExpiredCouponsOfJdAccount(String channal) {
		return couponDao.selectExpiredCouponsOfJdAccount(channal);
	}

	@Override
	public void disableExpiredCoupons() {
		couponDao.disableExpiredCoupons();
	}

}
