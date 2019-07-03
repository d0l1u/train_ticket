package com.l9e.util.hcpjar.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**编号 - 车站名 生成工具类*/
public class StationNamesUtil {
	public static Map<String,String> stationName=null;
	static{
		stationName=new HashMap<String,String>();
		try {
			//BufferedReader in = new BufferedReader(        new InputStreamReader(
			BufferedInputStream bufferedInputStream=new BufferedInputStream(StationNamesUtil.class.getResource("/station_name.js").openStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(bufferedInputStream,"utf-8"));
			int b; 
			String stationNamesStr="";
			while((b=in.read())!=-1){
				stationNamesStr=stationNamesStr+in.readLine(); 
			}
			int len = bufferedInputStream.available();
			byte[] bytes=new byte[len];
		    int r=bufferedInputStream.read(bytes);
		    String[] stationNamesArr=stationNamesStr.split("@");
		    int size=stationNamesArr.length;
		    stationName=new HashMap<String,String>(size);
		    for(int i=1;i<size;i++){
		    	String[] keyValue=stationNamesArr[i].split("\\|");
		    	//System.out.println("key:"+keyValue[1]+"__value:"+keyValue[2]);
		    	stationName.put(keyValue[1], keyValue[2]);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	  
		
	}
	public static String getValueFromStationName(String name){
		return stationName.get(name);
	}
	
	
	public static void main(String[] args) {
		System.out.println(getValueFromStationName("北京"));//BJP
		System.out.println(getValueFromStationName("上海"));//SHH
	}
	
}
