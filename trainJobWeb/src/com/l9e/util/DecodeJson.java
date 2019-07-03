package com.l9e.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecodeJson {

	public static Map<String, String> decode(String json) {
		Map<String, String> map = new HashMap<String, String>();
		
	    Pattern p = Pattern.compile("[\\w[^\\[\\],{}]]+");
	    Matcher m = p.matcher(json.replaceAll("'|\"", ""));
	    String[] _strs = null;
	    while (m.find()) {
	      _strs = m.group().split(":");
	      if (_strs.length == 2)
	        map.put(_strs[0].trim(), _strs[1].trim());
	    }
	    return map;
	}

}
