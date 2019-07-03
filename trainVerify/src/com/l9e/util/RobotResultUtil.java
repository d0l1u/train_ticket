package com.l9e.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RobotResultUtil {

	/**
	 * @param obj2
	 * @return 机器人通用参数返回
	 */
	public static String ResultChange(JSONObject obj2) {
		JSONArray arr1 = new JSONArray();
		if (null != obj2) {
			arr1.add(obj2);
		}
		JSONObject obj1 = new JSONObject();
		obj1.put("ErrorInfo", arr1);
		obj1.put("ErrorCode", 0);
		return obj1.toString();
	}

}
