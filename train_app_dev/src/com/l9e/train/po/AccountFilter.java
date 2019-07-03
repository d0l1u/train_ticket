package com.l9e.train.po;

import java.io.Serializable;

public class AccountFilter implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ids_card;
	
	private int account_id;
	
	private String real_name;

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getIds_card() {
		return ids_card;
	}

	public void setIds_card(String ids_card) {
		this.ids_card = ids_card;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	
}
