package com.l9e.train.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

	public static boolean containRegex(String input, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		if(m.find())
			return true;
		return false;
	}
	
	public static void main(String[] args) {
		String input = "常用联系人上限调整为15人";
		String regex = "联系人.*上限";
		boolean result = containRegex(input, regex);
		System.out.println(result);
	}
}
