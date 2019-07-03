package com.l9e.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

public class JSONUtil {
	/** 
	* 从json HASH表达式中获取一个map，该map支持嵌套功能 
	* 如：{"id" : "123456", "name" : "19e"} 
	* 注意commons-collections版本，必须包含org.apache.commons.collections.map.MultiKeyMap 
	* @param object 
	* @return 
	*/ 
	public static Map getMapFromJson(String jsonString) { 
	        JSONObject jsonObject = JSONObject.fromObject(jsonString); 
	        Map map = new HashMap(); 
	        for(Iterator iter = jsonObject.keys(); iter.hasNext();){ 
	            String key = (String)iter.next(); 
	            map.put(key, jsonObject.get(key)); 
	        } 
	        return map; 
	    } 
}
