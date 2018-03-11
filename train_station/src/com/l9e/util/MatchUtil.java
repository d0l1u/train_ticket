package com.l9e.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtil {

	public static String DATEREGEX = "\\d{4}-[0-1][0-2]-[0-3][0-9]";
	public static String WORDORNUMBER = "\\d+|\\w+";
	public static String matchDay(String str) {
		Pattern pattern = Pattern.compile(DATEREGEX);
		Matcher matcher = pattern.matcher(str);
		boolean b = matcher.find(0);
		String date = matcher.group();
		return date;
	}
	
	public static String matWordOrNumber(String input) {
		Pattern pattern = Pattern.compile(WORDORNUMBER);
		Matcher matcher = pattern.matcher(input);
		boolean b = matcher.find(0);
		String wordOrNumber = matcher.group();
		return wordOrNumber;
	}
	
	
	public static String match(String input, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		boolean b = matcher.find(0);
		String match = matcher.group();
		return match;
	}
	
	public static void main(String[] args) {
		System.out.println(match("{\"2014-11-06\":{\"D\":[", "\\d{4}-[0-1][0-2]-[0-3][0-9]"));
		System.out.println(matchDay("{\"2014-11-06\":{\"D\":["));
	}
}
