package com.l9e.util.hcpjar.util;

import java.util.HashMap;
/**前端 站信息数组生成类*/
public class StationInfoUtil {

	public static String[][] getStationInfo(String station_names){
		String[] stationNamesArr=station_names.split("@");
		int count=stationNamesArr.length-1;
		String[][] stationInfoArr;
		stationInfoArr=new String[count][];
		
		/*	['艾不盖','abg','aibugai','5']]
		 * 	0     1    2    3        4   5
		 * @bjb|北京北|VAP|beijingbei|bjb|0*/
		for(int i=0;i<count;i++){
			String[] keyValue=stationNamesArr[i+1].split("\\|");
			stationInfoArr[i] = new String[] {keyValue[1],keyValue[4],keyValue[3],"5"};
		}
		return stationInfoArr;
		
	}

	public static void main(String[] args) {
		String station_names=HttpsUtil.sendHttps("https://kyfw.12306.cn/otn/resources/js/framework/station_name.js");
		String[][] stationInfoArr;
		stationInfoArr=getStationInfo(station_names);
		
		int size=stationInfoArr.length;
		for(int i=0;i<size;i++){
			System.out.println(stationInfoArr[i][0]+"_"+stationInfoArr[i][1]+"_"+stationInfoArr[i][2]+"_"+stationInfoArr[i][3]);
		}
	}
}
