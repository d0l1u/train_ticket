package com.l9e.transaction.dao;

import com.l9e.transaction.vo.PrepaidCard;

/**
 * <p>
 * Title: PrepaidCardDao.java
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年2月24日
 */

public interface PrepaidCardDao {

	public PrepaidCard jdSelectCard();
	
	public void jdUpdateCardMoney(PrepaidCard card);
	public void jdUpdateCardStatus();
}
