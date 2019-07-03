package com.l9e.transaction.vo;
/**
 * 删除常用联系人
 * @author wangxg
 *
 */
public class TuniuDeleteContact {

	private int id;//序列号
	private String trainAccount;//联系人账号
	private String pass;//密码
	private String identy;//身份证号码
	private String identyType;//身份类型
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTrainAccount() {
		return trainAccount;
	}
	public void setTrainAccount(String trainAccount) {
		this.trainAccount = trainAccount;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getIdenty() {
		return identy;
	}
	public void setIdenty(String identy) {
		this.identy = identy;
	}
	public String getIdentyType() {
		return identyType;
	}
	public void setIdentyType(String identyType) {
		this.identyType = identyType;
	}
	public TuniuDeleteContact(int id, String trainAccount, String pass,
			String identy, String identyType) {
		super();
		this.id = id;
		this.trainAccount = trainAccount;
		this.pass = pass;
		this.identy = identy;
		this.identyType = identyType;
	}
	public TuniuDeleteContact() {
		super();
	}
	
}
