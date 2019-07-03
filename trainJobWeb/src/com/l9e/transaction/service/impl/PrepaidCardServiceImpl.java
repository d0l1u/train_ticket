package com.l9e.transaction.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.PrepaidCardDao;
import com.l9e.transaction.service.PrepaidCardService;
import com.l9e.transaction.vo.PrepaidCard;

/**
 * <p>
 * Title: PrepaidCardService.java
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年2月24日
 */
@Service("prepaidCardService")
public class PrepaidCardServiceImpl implements PrepaidCardService{


	@Resource
	private PrepaidCardDao prepaidCardDao;
	
	@Override
	public PrepaidCard jdSelectCard() {
		PrepaidCard card = prepaidCardDao.jdSelectCard();
		return card;
	}

	@Override
	public void jdUpdateCardMoney(PrepaidCard card) {
		prepaidCardDao.jdUpdateCardMoney(card);
	}

	@Override
	public void jdUpdateCardStatus() {
		prepaidCardDao.jdUpdateCardStatus();
	}
	
}
