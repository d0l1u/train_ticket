package com.l9e.transaction.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CouponDao;
import com.l9e.transaction.vo.Coupon;

/**
 * <p>
 * Title: CouponDaoImpl.java
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年3月7日
 */
@Repository
public class CouponDaoImpl  extends BaseDao implements CouponDao {

	@Override
	public void insertCoupons(List<Coupon> list) {
		this.getSqlMapClientTemplate().insert("coupon.insertCoupons", list);
	}

	@Override
	public List<Integer> selectExpiredCouponsOfJdAccount(String channal) {
		@SuppressWarnings("unchecked")
		List<Integer> list = this.getSqlMapClientTemplate().queryForList("coupon.selectExpiredCouponsOfJdAccount",channal);
		return list;
	}

	@Override
	public void disableExpiredCoupons() {
		this.getSqlMapClientTemplate().update("coupon.disableExpiredCoupons");
	}

}
