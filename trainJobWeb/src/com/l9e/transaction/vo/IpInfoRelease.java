package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * IP释放实体类
 * 
 * @author wangsf
 * 
 */
public class IpInfoRelease  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5279588438405002705L;
	/**
	 * IP的释放状态：待释放
	 */
	public static final String RELEASE_PREPARE = "0";
	/**
	 * IP的释放状态：释放成功
	 */
	public static final String RELEASE_SUCCESS = "1";
	/**
	 * IP的释放状态：释放失败
	 */
	public static final String RELEASE_FAILURE = "2";
	/**
	 * IP的释放状态：释放中
	 */
	public static final String RELEASE_UNDERWAY = "3";
	
	
	/**
	 * IP-id 
	 */
	private Integer ipId;

	
	/**
	 * IP释放状态
	 */
	private String releaseStatus;	
	
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
	 *  本IP的释放次数 
	 */
	private Integer releaseNum;

	public Integer getIpId() {
		return ipId;
	}

	public void setIpId(Integer ipId) {
		this.ipId = ipId;
	}

	public String getReleaseStatus() {
		return releaseStatus;
	}

	public void setReleaseStatus(String releaseStatus) {
		this.releaseStatus = releaseStatus;
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

	public Integer getReleaseNum() {
		return releaseNum;
	}

	public void setReleaseNum(Integer releaseNum) {
		this.releaseNum = releaseNum;
	}
	
	@Override
	public String toString() {
		return "IpInfoRelease [ipId=" + ipId + ", releaseStatus="
				+ releaseStatus + ", ipExt=" + ipExt + ", optionTime="
				+ optionTime + ", createTime=" + createTime + ", releaseNum="
				+ releaseNum + "]";
	}

}
