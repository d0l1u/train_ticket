package com.l9e.transaction.dao.impl;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.PrepaidCardDao;
import com.l9e.transaction.vo.PrepaidCard;

/**
 * <p>
 * Title: PrepaidCardDaoImpl.java
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年2月24日
 */
@Repository("prepaidCardDao")
public class PrepaidCardDaoImpl extends BaseDao implements PrepaidCardDao  {

	@Override
	public PrepaidCard jdSelectCard() {
		PrepaidCard card = (PrepaidCard) this.getSqlMapClientTemplate().queryForObject("prepaidCard.jdSelectCard");
		return card;
	}

	@Override
	public void jdUpdateCardMoney(PrepaidCard card) {
		this.getSqlMapClientTemplate().update("prepaidCard.jdUpdateCardMoney", card);
	}

	@Override
	public void jdUpdateCardStatus() {
		this.getSqlMapClientTemplate().update("prepaidCard.jdUpdateCardStatus");
	}
}
