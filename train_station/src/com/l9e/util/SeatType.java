package com.l9e.util;


/**
 * @Description: 携程与酷游坐席映射枚举
 *
 * @author taokai
 *
 * 2016年6月6日
 */
public enum SeatType {

	/** ["0"]="9",-- 商务座 */
	BUSINESS_SEAT("0","221","商务座"),
	
	/** ["1"]="P",--特等座 */
	SUPER_SEAT("1","205","特等座"),
	
	/** ["2"]="M",--一等座 */
	FIRST_SEAT("2","207","一等座"),
	
	/** ["3"]="O",--二等座 */
	SECOND_SEAT("3","209","二等座"),
	
	/** ["4"]="6",--高级软座 */
	SENIORSOFT_SEAT("4","226","高级软座"),
	
	/** ["5"]="4",--软卧 */
	SOFT_SLEEPER("5","225","软卧"),
	
	/** ["6"]="3",--硬卧 */
	HARD_SLEEPER("6","224","硬卧"),
	
	/** ["7"]="2",--软座 */
	SOFT_SEAT("7","203","软座"),
	
	/** ["8"]="1",--硬座 */
	HARD_SEAT("8","201","硬座"),

	/** ["9"]="1",--无座 动车无座为二等座 */
	NO_SEAT("9","227","无座"),
	
	/** ["9"]="1",--无座 动车无座为二等座 */
	DW_SEAT("20","304","动卧"),

	/** ["11"]="5",--包厢硬卧 */
	HARD_SLEEPER_BOX("11","BK3-0","包厢硬卧");
	
	/** 酷游编号 */
	private String name;
	/** 携程编号 */
	private String value;
	private String title;
	
	public static String getValue(String name){
		for(SeatType st : values()){
			if(st.getName().equals(name)){
				return st.getValue();
			}
		}
		return "";
	}
	
	public static String getTitle(String name){
		for(SeatType st : values()){
			if(st.getName().equals(name)){
				return st.getTitle();
			}
		}
		return "";
	}
	
	public static String getTitleByValue(String value){
		for(SeatType st : values()){
			if(st.getValue().equals(value)){
				return st.getTitle();
			}
		}
		return "";
	}
	
	SeatType(String name, String value, String title){
		this.name = name;
		this.value = value;
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
