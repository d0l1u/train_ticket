package com.l9e.transaction.service;

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

public interface PrepaidCardService {
	public PrepaidCard jdSelectCard();
	public void jdUpdateCardStatus();
	
	public void jdUpdateCardMoney(PrepaidCard card);
}
