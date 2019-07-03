package com.l9e.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 专门用来做切换的Util
 * @author liht
 *
 */


  

public class SwitchUtils {
	/**
	 * double类型转换为String类型的0.00%格式
	 * @param args
	 * @return
	 */
	public static String format_perCent(double args) {
        DecimalFormat df = new DecimalFormat("0.00%");
        String str = df.format(args);
        return str;
    }
	/**
	 * 切换  如果传过来的参数为0 则转换为1 其他的都转换为0
	 * @param str
	 * @return
	 */
    public static String switch1Or0(String str){
    	if(str.equals("0")){
    		str="1";
    		return str;
    	}else{
    		str="0";
    	return str;
    	}
    }
    
    public static String splitStr1Pre(String str){
    	if(StringUtil.isNotEmpty(str)){
    	String str1[] = str.split("#");
    	return str1[0];
    	}else{
    		return str;
    	}
    }
    
    public static String splitStr1Last(String str){
    	if(StringUtil.isNotEmpty(str)){
    	String str1[] = str.split("#");
    	return str1[1];
    	}else{
    		return str;
    	}
    }
    
    
    public static String splitStr2(String str,int total){
    	if(StringUtil.isNotEmpty(str)){
    	String str2[] = str.split("\\|");
    	return str2[total];
    	}else{
    		return str;
    	}
    }
    
    public static String splitStr3Pre(String str){
    	if(StringUtil.isNotEmpty(str)){
    		String str3[] = str.split(",");
    		return str3[0];
    	}else{
    		return str;
    	}
    }
    
    public static String splitStr3Last(String str){
    	if(StringUtil.isNotEmpty(str)){
    		String str3[] = str.split(",");
    		return str3[1];
    	}else{
    		return str;
    	}
    }
    
    public static String splitStr_Pre(String str){
    	if(StringUtil.isNotEmpty(str)){
    		String str3[] = str.split("_");
    		return str3[0];
    	}else{
    		return str;
    	}
    }
    public static String splitStr_Last(String str){
    	if(StringUtil.isNotEmpty(str)){
    		String str3[] = str.split("_");
    		return str3[1];
    	}else{
    		return str;
    	}
    }
  
    
    public static Integer queryTotal(String str){
    	int total=0;
    	char[] a = str.toCharArray();
    	if(StringUtil.isNotEmpty(str)){
    		for(int i=0;i<a.length;i++){
    			if(a[i] == '#'){
    				total++;
    			}
    		}
    	}
    	return total; 
    }
    
    public static List<String> strToList(String str){
    	List list= new ArrayList();
    	if(StringUtil.isNotEmpty(str)){
	    	str = str.replaceAll("\\[","");
			str = str.replaceAll("\\]","");
			String[] strs = str.split(",");
			for(int i = 0 ;i <strs.length; i++){
			list.add(strs[i].trim());
			}
    	}
    	return list;
    }
    /**
     * 获取给定时间的前一天
     * @param str
     * @param i
     * @return
     */
    public static String dateSub(String str,int i){
    	String s[]=str.split("-");
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Integer.parseInt(s[0]), Integer.parseInt(s[1])-1, Integer.parseInt(s[2]));
		calendar.add(Calendar.DATE, -1);    //得到前一天
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	return df.format(date);
    }
    
    /**
     * 获取当前一天的前一天时间
     * @param date
     * @return
     */
    public static String getPreDate(){
    	Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	return df.format(date);
    }
    
    public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}


}
