package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * IP实体类
 * 
 * @author wangsf
 * 
 */
public class IpInfo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5279588438405002705L;
	/**
	 * IP状态：空闲
	 */
	public static final String STATUS_FREE = "00";
	/**
	 * IP状态：工作中
	 */
	public static final String STATUS_WORKING = "11";
	/**
	 * IP状态：停用
	 */
	public static final String STATUS_STOP = "22";
	/**
	 * IP状态：备用
	 */
	public static final String STATUS_PREPARED = "33";
	
	/**
	 * IP类型：1-携程代出票ip
	 */
	public static final Integer TYPE_CTRIP = 1;
	
	/**
	 * IP类型：  2-预定代理ip
	 */
	public static final Integer TYPE_BOOKIP = 2;
	
	/**
	 * IP-id 
	 */
	private Integer ipId;

	/**
	 * IP类型
	 */
	private Integer ipType;
	
	/**
	 * IP状态
	 */
	private String ipStatus;	
	
	/**
	 * IP地址
	 */
	private String ipExt;
	
	/**
	 * 操作时间(用于控制队列IP顺序)
	 */
	private String optionTime;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 *  本IP下单成功的次数 
	 */
	private Integer requestNum;
	
	/**
	 *  标识某个机器是否支持切换新旧IP的功能，
	 *  只有美团云的机器支持切换IP操作
	 */
	private String ifSupportChange;

	public Integer getIpId() {
		return ipId;
	}

	public void setIpId(Integer ipId) {
		this.ipId = ipId;
	}

	public Integer getIpType() {
		return ipType;
	}

	public void setIpType(Integer ipType) {
		this.ipType = ipType;
	}

	public String getIpStatus() {
		return ipStatus;
	}

	public void setIpStatus(String ipStatus) {
		this.ipStatus = ipStatus;
	}

	public String getIpExt() {
		return ipExt;
	}

	public void setIpExt(String ipExt) {
		this.ipExt = ipExt;
	}
	
	
	public String getOptionTime() {
		return optionTime;
	}

	public void setOptionTime(String optionTime) {
		this.optionTime = optionTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	

	public Integer getRequestNum() {
		return requestNum;
	}

	public void setRequestNum(Integer requestNum) {
		this.requestNum = requestNum;
	}
	
	public String getIfSupportChange() {
		return ifSupportChange;
	}

	public void setIfSupportChange(String ifSupportChange) {
		this.ifSupportChange = ifSupportChange;
	}

	@Override
	public String toString() {
		return "IpInfo [ipId=" + ipId + ", ipType=" + ipType + ", ipStatus="
				+ ipStatus + ", ipExt=" + ipExt + ", optionTime=" + optionTime
				+ ", createTime=" + createTime + ", requestNum=" + requestNum
				+ ", ifSupportChange=" + ifSupportChange + "]";
	}

}
