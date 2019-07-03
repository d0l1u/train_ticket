package com.cqz.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

public class JsonUtil
{

	public static Map getMapFromJson(String jsonString)
	{
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Map map = new HashMap();
		for (Iterator iter = jsonObject.keys(); iter.hasNext();)
		{
			String key = (String) iter.next();
			map.put(key, jsonObject.get(key));
		}
		return map;
	}

}
