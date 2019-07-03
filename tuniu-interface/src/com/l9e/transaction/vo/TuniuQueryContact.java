package com.l9e.transaction.vo;

/**
 * 查询常用联系人
 * @author wangxg
 *
 */
public class TuniuQueryContact {

	private int id;//序列号
	private String name; //姓名
	private int isUserSelf;//是否是账号本人 0是 1不是
	private int sex ;//性别 0 男 1 女
	private String birthday;//生日
	private String country; //国家 CN中国
	private int identyType;//证件类型
	private String identy ;//证件号码
	private int personType; //旅客类型 0 成人 1儿童
	private String phone;//手机
	private String tel; //固话
	private String email;//邮件
	private String address;//地址
	private int checkStatus;//身份是否校验通过 0通过1不通过
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsUserSelf() {
		return isUserSelf;
	}
	public void setIsUserSelf(int isUserSelf) {
		this.isUserSelf = isUserSelf;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getIdentyType() {
		return identyType;
	}
	public void setIdentyType(int identyType) {
		this.identyType = identyType;
	}
	public String getIdenty() {
		return identy;
	}
	public void setIdenty(String identy) {
		this.identy = identy;
	}
	public int getPersonType() {
		return personType;
	}
	public void setPersonType(int personType) {
		this.personType = personType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}
	public TuniuQueryContact(int id, String name, int isUserSelf, int sex,
			String birthday, String country, int identyType, String identy,
			int personType, String phone, String tel, String email,
			String address, int checkStatus) {
		super();
		this.id = id;
		this.name = name;
		this.isUserSelf = isUserSelf;
		this.sex = sex;
		this.birthday = birthday;
		this.country = country;
		this.identyType = identyType;
		this.identy = identy;
		this.personType = personType;
		this.phone = phone;
		this.tel = tel;
		this.email = email;
		this.address = address;
		this.checkStatus = checkStatus;
	}
	public TuniuQueryContact() {
		super();
	}
	
}
