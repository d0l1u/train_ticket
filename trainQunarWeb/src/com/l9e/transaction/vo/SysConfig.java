package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SysConfig implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static List<InterAccount> accountContainer = new ArrayList<InterAccount>();
	
	public static InterAccount getAccountByName(String name){
		if(StringUtils.isEmpty(name)){
			return null;
		}else{
			for(InterAccount account : accountContainer){
				if(name.equalsIgnoreCase(account.getName())){
					return account;
				}
			}
			return null;
		}
	}

}
