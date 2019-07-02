package com.train.system.booking.em;

/**
 * @ClassName: SeatType
 * @Description: 坐席类型映射关系
 * @author: taokai
 * @date: 2017年7月20日 下午1:32:49
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
public enum SeatType {

	/** 商务座 */
	BUSINESS_SEAT("0", "9"),

	/** 特等座 */
	SUPER_SEAT("1", "P"),

	/** 一等座 */
	FIRST_SEAT("2", "M"),

	/** 二等座 */
	SECOND_SEAT("3", "O"),

	/** 高级软座 */
	SENIORSOFT_SEAT("4", "6"),

	/** 软卧 */
	SOFT_SLEEPER("5", "4"),

	/** 硬卧 */
	HARD_SLEEPER("6", "3"),

	/** 软座 */
	SOFT_SEAT("7", "2"),

	/** 硬座 */
	HARD_SEAT("8", "1"),

	/** 无座 动车/高铁(C,G,D)无座为二等座 */
	GAOTIE_NO_SEAT("9", "O"),

	/** 无座 其他无座为硬座座 */
	COMMONLY_NO_SEAT("9", "1"),

	/** 包厢硬卧 */
	HARD_SLEEPER_BOX("11", "5"),

	/** 高级动卧 */
	GAO_JI_DONG_WO("16", "A"),

	/** 动卧 */
	DONG_WO("20", "F");

	private String l9e;
	private String kyfw;

	SeatType(String l9e, String kyfw) {
		this.l9e = l9e;
		this.kyfw = kyfw;
	}

	public static String chooseKyfw(String l9e, String trainCode) {
		if (GAOTIE_NO_SEAT.l9e.equals(l9e)) {
			if (trainCode.startsWith("C") || trainCode.startsWith("D") || trainCode.startsWith("G")) {
				return GAOTIE_NO_SEAT.kyfw;
			} else {
				return COMMONLY_NO_SEAT.kyfw;
			}
		}
		return getKyfwByl9e(l9e);
	}

	public static String getKyfwByl9e(String l9e) {
		for (SeatType st : values()) {
			if (st.l9e.equals(l9e)) {
				return st.getKyfw();
			}
		}
		return null;
	}

	public static String getL9eByKyfw(String kyfw) {
		for (SeatType st : values()) {
			if (st.kyfw.equals(kyfw)) {
				return st.getL9e();
			}
		}
		return null;
	}

	public String getL9e() {
		return l9e;
	}

	public void setL9e(String l9e) {
		this.l9e = l9e;
	}

	public String getKyfw() {
		return kyfw;
	}

	public void setKyfw(String kyfw) {
		this.kyfw = kyfw;
	}

}