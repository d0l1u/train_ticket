package com.nineteen.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.UnsupportedEncodingException;

import java.io.InputStreamReader;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.util.Date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Utils {
	
	public static DateFormat df_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateFormat df_file_time = new SimpleDateFormat("yyyyMMddHHmmss");
	public static DateFormat df_day = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat df_day2 = new SimpleDateFormat("yyyyMMdd");
	public static DecimalFormat df = new DecimalFormat("0.00"); 
	public static Pattern int_pattern = Pattern.compile("[0-9]*");
	
	public static String eL1 = "([0-9]{4})[年/-]([0-9]{1,2})[月/-]([0-9]{1,2})日?[\\s\\S]{0,15}([0-9]{2}:[0-9]{2}:[0-9]{2})";
	public static String eL2 = "([0-9]{4})[年/-]([0-9]{1,2})[月/-]([0-9]{1,2})日?[\\s\\S]{0,15}([0-9]{2}:[0-9]{2})";
	public static String eL4 = "([0-9]{4})[年/-]([0-9]{1,2})[月/-]([0-9]{1,2})日?";
	public static String eL5 = "([0-9]{2})[年/-]([0-9]{1,2})[月/-]([0-9]{1,2})日?";
	
	public static Pattern pd1 = Pattern.compile(eL1);
	public static Pattern pd2 = Pattern.compile(eL2);
	public static Pattern pd4 = Pattern.compile(eL4);
	public static Pattern pd5 = Pattern.compile(eL5);
	
	public static void p(Object o){
		System.out.println(o);
	};
	
	public static void log(Object o){
		System.out.println(o);
	};
	
	public static String getDoubleString(double d){
		return df.format(d); 
	};
	
	public static double doubled(String d){
		return Double.parseDouble(d); 
	};
	
	public static double formatDouble(double d){
		String temp = getDoubleString(d);
		return Double.parseDouble(temp); 
	};
	
	public static int passMin(String time){
		int min = 0;
		try {
			Date date = df_time.parse(time);
			long diff = new Date().getTime() - date.getTime() ;
		    long m = diff / (1000 * 60);
			min = (int)m;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return min;
		}//转换成功的Date对象
		return min;
	};
	
	public static int passMin(String time1,String time2){
		int min = 0;
		try {
			Date date1 = df_time.parse(time1);
			Date date2 = df_time.parse(time2);
			long diff = date2.getTime() - date1.getTime() ;
		    long m = diff / (1000 * 60);
			min = (int)m;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return min;
		}//转换成功的Date对象
		return min;
	};
	
	/**
	 *  获取当前系统时间
	 *  字符串的格式�? �? yyyy-MM-dd HH:mm:ss
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String currentTime(){
		return df_time.format(new Date());
	};
	
	public static String currentFileTime(){
		return df_file_time.format(new Date());
	};
	
	/**
	 *  获取当前系统日期
	 *  字符串的格式�? �? yyyy-MM-dd
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String currentDate(){
		return df_day.format(new Date());
	};
	
	/**
	 *  获取当前系统日期
	 *  字符串的格式�? �? yyyy-MM-dd
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String currentYear(){
		return df_day.format(new Date()).substring(0,4);
	};
	
	/**
	 *  获取与当前日期相差i天的日期
	 * @param   i   相差天数
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String currentYear(int i){
		Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
		//设置代表的日期为1�?
        c.set(Calendar.YEAR,year+i);
		return df_day.format( c.getTime() ).substring(0, 4);
	};
	
	/**
	 *  获取当前系统日期
	 *  字符串的格式�? �? yyyy-MM-dd
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String currentMonth(){
		return df_day.format(new Date()).substring(5,7);//2011-12-07
	};
	
	public static String currentMonth(int i){
		Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
		//设置代表的日期为1�?
        c.set(Calendar.MONTH,month+i);
		return df_day.format( c.getTime() ).substring(5, 7);
	};
	
	/**
	 *  获取当前系统日期
	 *  字符串的格式�? �? yyyy-MM-dd
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String currentDay(){
		return currentDate().substring(8,10);
	};
	
	public static String currentDay(int i){
		return currentDate(i).substring(8,10);
	};
	
	/**
	 *  获取指定返回格式的当前系统日�?
	 * @param   str   日期格式字符�?
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String currentDate(String str){
		DateFormat _df = new SimpleDateFormat( str );
		return _df.format(new Date());
	};
	
	/**
	 *  获取与当前日期相差i天的日期
	 * @param   i   相差天数
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String currentDate(int i){
		Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
		//设置代表的日期为1�?
        c.set(Calendar.DATE,day+i);
		return df_day.format( c.getTime() );
	};
	
	/**
	 *  获取与当前日期相差i天的日期
	 * @param   i   相差天数
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String currentDate(String date , int i){
		Date day;
		Calendar c = Calendar.getInstance();
		try {
			day = df_day.parse(date);
			c.setTime(day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int n = c.get(Calendar.DATE);
		//设置代表的日期为1�?
        c.set(Calendar.DATE,n+i);
		return df_day.format( c.getTime() );
	};
	
	/**
	 *  获取与当前日期相差i天的日期
	 * @param   i   相差天数
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String getTimeAfterMimute(String time , int i){
		Date day;
		Calendar c = Calendar.getInstance();
		try {
			day = df_time.parse(time);
			c.setTime(day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int n = c.get(Calendar.MINUTE);
        c.set(Calendar.MINUTE, n+i);
		return df_time.format( c.getTime() );
	};
	
	/**
	 *  获取本月的第�?�?
	 * @param
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String firstDayOfMonth(){
		Calendar c = Calendar.getInstance();
        //设置代表的日期为1�?
        c.set(Calendar.DATE,1);
		return df_day.format(c.getTime());
	};
	
	/**
	 *  获取本月的最后一�?
	 * @param
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static int daysOfMonth(String day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			Date date = sdf.parse(day);
			c.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		//获得当前月的�?大日期数
		int maxDay = c.getActualMaximum(Calendar.DATE); 
		return maxDay;
	};
	
	/**
	 *  获取本月的最后一�?
	 * @param
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static int daysOfWeek(String day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			Date date = sdf.parse(day);
			c.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		//获得当前月的�?大日期数
		int dayofweek = c.get(Calendar.DAY_OF_WEEK)-1; 
		if(dayofweek==0){
			dayofweek = 7;
		};
		return dayofweek;
	};
	
	
	/**
	 *  获取本月的最后一�?
	 * @param
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String lastDayOfMonth(){
		Calendar c = Calendar.getInstance();
		//获得当前月的�?大日期数
		int maxDay = c.getActualMaximum(Calendar.DATE); 
        c.set(Calendar.DATE,maxDay);
		return df_day.format(c.getTime());
	};
	
	/**
	 *  获取�?�?7年的字符串数�?
	 * @param
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String[] lastSevenYears(){
		String[] years = new String[7];
		Calendar c = Calendar.getInstance();
		//获得当前月的�?大日期数
		int curr_year = c.get(Calendar.YEAR); 
		years[0] = String.valueOf(curr_year-3);
		years[1] = String.valueOf(curr_year-2);
		years[2] = String.valueOf(curr_year-1);
		years[3] = String.valueOf(curr_year);
		years[4] = String.valueOf(curr_year+1);
		years[5] = String.valueOf(curr_year+2);
		years[6] = String.valueOf(curr_year+3);
		return years;
	};
	
	/**
	 *  判断字符串是否为null
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static boolean isEmpty(Object str) {
		if(str==null||"".equals(str.toString().trim())||"-".equals(str.toString().trim())){
			return true;
		}
		return false;
	}
	
	/**
	 *  返回前台的成功信息的json字符�?
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String suc(String str) {
		// TODO Auto-generated method stub
		String s = "";
		if(str!=null){
			s = str;
		}
		return "{\"success\":\"true\",\"msg\":\""+s+"\"}";
	}
	
	/**
	 *  返回前台的成功信息的json字符�?
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String suc(String str,String key,String value) {
		// TODO Auto-generated method stub
		String s = "";
		if(str!=null){
			s = str;
		}
		return "{\"success\":\"true\",\"msg\":\""+s+"\",\""+key+"\":\""+value+"\"}";
	}
	
	/**
	 *  返回前台的成功信息的json字符�?
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String suc(String str,String k1,String v1,String k2,String v2) {
		// TODO Auto-generated method stub
		String s = "";
		if(str!=null){
			s = str;
		}
		return "{\"success\":\"true\",\"msg\":\""+s+"\",\""+k1+"\":\""+v1+"\",\""+k2+"\":\""+v2+"\"}";
	}
	
	/**
	 *  返回前台的成功信息的json字符�?
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String suc(String str,String k1,String v1,String k2,String v2,String k3,String v3,String k4,String v4) {
		// TODO Auto-generated method stub
		String s = "";
		if(str!=null){
			s = str;
		}
		return "{\"success\":\"true\",\"msg\":\""+s+"\",\""+k1+"\":\""+v1+"\",\""+k2+"\":\""+v2+"\",\""+k3+"\":\""+v3+"\",\""+k4+"\":\""+v4+"\"}";
	}
	
	/**
	 *  返回前台的成功信息的json字符�?
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String suc(String str,String k1,String v1,String k2,String v2,String k3,String v3,String k4,String v4,String k5,String v5) {
		// TODO Auto-generated method stub
		String s = "";
		if(str!=null){
			s = str;
		}
		return "{\"success\":\"true\",\"msg\":\""+s+"\",\""+k1+"\":\""+v1+"\",\""+k2+"\":\""+v2+"\",\""+k3+"\":\""+v3+"\",\""+k4+"\":\""+v4+"\",\""+k5+"\":\""+v5+"\"}";
	}
	
	/**
	 *  返回前台的失败信息的json字符�?
	 * @param 
	 * @return 
	 *     add time �?2011-10-07
	 *  modify time �?2011-10-07
	 */
	public static String err(String str) {
		// TODO Auto-generated method stub
		String s = "";
		if(str!=null){
			s = str;
		}
		return "{\"success\":\"false\",\"msg\":\""+s+"\"}";
	}
	
	public static String err(String str,String key,String value) {
		// TODO Auto-generated method stub
		String s = "";
		if(str!=null){
			s = str;
		}
		return "{\"success\":\"false\",\"msg\":\""+s+"\",\""+key+"\":\""+value+"\"}";
	}
	
	public static String err(String str,String k1,String v1,String k2,String v2) {
		// TODO Auto-generated method stub
		String s = "";
		if(str!=null){
			s = str;
		}
		return "{\"success\":\"false\",\"msg\":\""+s+"\",\""+k1+"\":\""+v1+"\",\""+k2+"\":\""+v2+"\"}";
	}
	
	
	public static String lpad(String str , int length ) {
		return lpad( str ,  length ,  ' ' );
	}
	
	public static String rpad(String str , int length ) {
		return rpad( str ,  length ,  ' ' );
	}
	
	
	public static String lpad(String str , int length , char dot ) {
		// TODO Auto-generated method stub
		int size = str.getBytes().length;
		if(size == length)  return str;
		if(size > length){
			return str;
		}
		int n = length - size;
		char[] dots = new char[n];
		for(int i=0;i<n;i++){
			dots[i] = dot;
		}
		return String.valueOf(dots)+str;
	}
	
	public static String rpad(String str , int length , char dot ) {
		// TODO Auto-generated method stub
		int size = str.getBytes().length;
		if(size == length)  return str;
		if(size > length){
			return str;
		}
		int n = length - size;
		char[] dots = new char[n];
		for(int i=0;i<n;i++){
			dots[i] = dot;
		}
		return str+String.valueOf(dots);
	}
	
	public static String readTxt(String path){
		StringBuffer sb = new StringBuffer();
		try {
            String encoding="UTF-8";
            File file = new File(path);
            if(true){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
//                    log.info(new String(lineTxt.getBytes("UTF-8"),"GBK"));
                    sb.append(lineTxt);
                }
                read.close();
		    }else{
		        System.out.println("找不到指定的文件:"+path+", 或者不是txt文件 ");
		    }
	    } catch (Exception e) {
	        System.out.println("文件读取出错  path = "+path);
	        e.printStackTrace();
	    }
		return sb.toString();
	}
	
	
	public static String readTxt(File file) {
		StringBuffer sb = new StringBuffer();
		try {
            String encoding="UTF-8";
            if(file!=null&&file.exists()&&file.getName().toLowerCase().endsWith(".txt")){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
//                    log.info(new String(lineTxt.getBytes("UTF-8"),"GBK"));
                    sb.append(lineTxt);
                }
                read.close();
		    }else{
		        System.out.println("找不到指定的文件:"+file.getPath()+", 或者不是txt文件 ");
		    }
	    } catch (Exception e) {
	        System.out.println("文件读取出错  path = "+file.getPath());
	        e.printStackTrace();
	    }
		return sb.toString();
	}
	
	public static String readTxt2(File file) {
		StringBuffer sb = new StringBuffer();
		try {
            if(file!=null&&file.exists()&&file.getName().toLowerCase().endsWith(".txt")){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
//                    log.info(new String(lineTxt.getBytes("UTF-8"),"GBK"));
                    sb.append(lineTxt);
                }
                read.close();
		    }else{
		        System.out.println("找不到指定的文件:"+file.getPath()+", 或者不是txt文件 ");
		    }
	    } catch (Exception e) {
	        System.out.println("文件读取出错  path = "+file.getPath());
	        e.printStackTrace();
	    }
		return sb.toString();
	}
	
	public static String readTxt(File file,String encoding) {
		StringBuffer sb = new StringBuffer();
		try {
            if(file!=null&&file.exists()&&file.getName().toLowerCase().endsWith(".txt")){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
//                    log.info(new String(lineTxt.getBytes("UTF-8"),"GBK"));
                    sb.append(lineTxt);
                }
                read.close();
		    }else{
		        System.out.println("找不到指定的文件:"+file.getPath()+", 或者不是txt文件 ");
		    }
	    } catch (Exception e) {
	        System.out.println("文件读取出错  path = "+file.getPath());
	        e.printStackTrace();
	    }
		return sb.toString();
	}
	
	public  static void  copyFile(String  oldPath,  String  newPath)  {  
       try  {  
    	   System.out.println( " copyFile    oldPath  =  "+oldPath);
           File  oldfile  =  new  File(oldPath);  
           File file = new File(newPath);
	           if(!file.getParentFile().exists()){
	        	   file.getParentFile().mkdirs();
   			   System.out.println( " 创建文件夹成功    mkdirs  =  "+file.getParentFile().getPath());
   		   }
           if  (oldfile.exists())  {  //文件存在时  
        	   BufferedInputStream inBuff = null;
               BufferedOutputStream outBuff = null;
               try {
                   // 新建文件输入流并对它进行缓冲
                   inBuff = new BufferedInputStream(new FileInputStream(oldfile));
                   // 新建文件输出流并对它进行缓冲
                   outBuff = new BufferedOutputStream(new FileOutputStream(file));
                   // 缓冲数组
                   byte[] b = new byte[1024 * 5];
                   int len;
                   while ((len = inBuff.read(b)) != -1) {
                       outBuff.write(b, 0, len);
                   }
                   // 刷新此缓冲的输出流
                   outBuff.flush();
               } finally {
                   // 关闭流
                   if (inBuff != null)
                       inBuff.close();
                   if (outBuff != null)
                       outBuff.close();
               }
//        	   
//        	   
//        	   
//               InputStream  inStream  =  new  FileInputStream(oldPath);  //读入原文件 
//               OutputStream  fs  =  new  FileOutputStream(file);  
//               byte[]  buffer  =  new  byte[1024];  
//               int  byteread  =  -1; 
//               while  (  (byteread  =  inStream.read(buffer))  !=  -1)  {  
//                   fs.write(buffer);  
//               }  
//               fs.flush();
//               fs.close();
//               inStream.close(); 
           }  
       }  
       catch  (Exception  e)  {  
           System.out.println("复制单个文件操作出错");  
           e.printStackTrace();  
       }  
   }
	
	
	public static String EncodeHtml(String content) {
		if(content==null) return "";       
	    String html = content;
	    html = StringUtils.replace(html, "'", "&apos;");
	    html = StringUtils.replace(html, "\"", "&quot;");
	    html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
	    //html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
	    html = StringUtils.replace(html, "<", "&lt;");
	    html = StringUtils.replace(html, ">", "&gt;");
	    return html;

	}
	
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式  
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式  
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
    
    
    public static String delHTMLTag(String htmlStr) {  
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
        Matcher m_script = p_script.matcher(htmlStr);  
        htmlStr = m_script.replaceAll(""); // 过滤script标签  
  
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
        Matcher m_style = p_style.matcher(htmlStr);  
        htmlStr = m_style.replaceAll(""); // 过滤style标签  
  
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
        Matcher m_html = p_html.matcher(htmlStr);  
        htmlStr = m_html.replaceAll(""); // 过滤html标签  
  
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);  
        Matcher m_space = p_space.matcher(htmlStr);  
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签  
        return htmlStr.trim(); // 返回文本字符串  
    }  
      
    public static String getTextFromHtml(String htmlStr){  
        htmlStr = delHTMLTag(htmlStr);  
        htmlStr = htmlStr.replaceAll("&nbsp;", "");  
//        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);  
        return htmlStr;  
    }   
    
    
    
    
    public static String matchDateFromString(String text) {
		Matcher matcher = pd1.matcher(text);
		if(matcher.find()){
			String date = matcher.group();
			String year = matcher.group(1);
			String month = matcher.group(2);
			String day = matcher.group(3);
			if(month.length()==1){
				month = "0"+month;
			}
			if(day.length()==1){
				day = "0"+day;
			}
			String time = matcher.group(4);
			String new_date = year+"-"+month+"-"+day+" "+time;
//			log.info("new_date1="+new_date+" , pubdate = "+pubdate);
			return new_date;
		}
		
		matcher = pd2.matcher(text);
		if(matcher.find()){
			String date = matcher.group();
			String year = matcher.group(1);
			String month = matcher.group(2);
			String day = matcher.group(3);
			if(month.length()==1){
				month = "0"+month;
			}
			if(day.length()==1){
				day = "0"+day;
			}
			String time = matcher.group(4);
			String new_date = year+"-"+month+"-"+day+" "+time+":00";
//			log.info("new_date2="+new_date+" , pubdate = "+pubdate);
			return new_date;
		}
		matcher = pd4.matcher(text);
		if(matcher.find()){
			String date = matcher.group();
			String year = matcher.group(1);
			String month = matcher.group(2);
			String day = matcher.group(3);
			if(year.length()==2){
				if(year.startsWith("9")){
					year = "19"+year;
				}else{
					year = "20"+year;
				}
			}
			if(month.length()==1){
				month = "0"+month;
			}
			if(day.length()==1){
				day = "0"+day;
			}
			String new_date = year+"-"+month+"-"+day+" 07:00:00";
//			log.info("new_date4="+new_date+" , pubdate = "+pubdate);
			return new_date;
		}
		matcher = pd5.matcher(text);
		if(matcher.find()){
			String date = matcher.group();
			String year = matcher.group(1);
			String month = matcher.group(2);
			String day = matcher.group(3);
			if(year.length()==2){
				if(year.startsWith("9")){
					year = "19"+year;
				}else{
					year = "20"+year;
				}
			}
			if(month.length()==1){
				month = "0"+month;
			}
			if(day.length()==1){
				day = "0"+day;
			}
			String new_date = year+"-"+month+"-"+day+" 00:00:00";
//			log.info("new_date5="+new_date+" , pubdate = "+pubdate);
			return new_date;
		}
//		log.info(" not match    pubdate = "+pubdate);
		return "";
	}
    /**
	 * 功能描述：返回小
	 *
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour() {
		Date date = new Date();
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.HOUR_OF_DAY);
	}
	/**
	 * 功能描述：返回分
	 *
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.MINUTE);
	}
	/**
	 * 返回秒钟
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.SECOND);
	}
	 
	/**
	 * 功能描述：返回毫
	 *
	 * @param date
	 *            日期
	 * @return 返回毫
	 */
	public static long getMillis() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.getTimeInMillis();
	} 

	public static String currentDate2() {
		return df_day2.format(new Date());
	}
	/**
	 * base64 解密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	 public static byte[] decryptBASE64(String key) throws Exception {
		          return (new BASE64Decoder()).decodeBuffer(key);
		 }
	 public static String decBASE64(String key) {
		 String k = null;
		try {
			k = new String(Utils.decryptBASE64(key), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return k;
	 }
	 //解密 不带编码
	 public static String encode(byte[] buff) {
			return Base64.encodeBase64URLSafeString(buff);
		}
	 /**
	  * base64 加密
	  */
	 public static String encryptBASE64(byte[] key) {
		  return (new BASE64Encoder()).encodeBuffer(key);
	}
	 
	//删除指定文件夹下所有文件
	public static boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	            
	             flag = true;
	          }
	       }
	       return flag;
	     }
	
	//获取月份的天数
	/**
	 * i 月份
	 */
	public static int dy(int i){
		i = i +1;
		//获取当前时间  
        Calendar cal = Calendar.getInstance();  
        //下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推  
        cal.set(Calendar.MONTH, i-1);  
        //调到上个月  
        cal.add(Calendar.MONTH, -1);  
        //得到一个月最最后一天日期(31/30/29/28)  
        int MaxDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH); 
        return MaxDay;
	}

	public static void main(String[] args) throws ParseException {
		  String path="C:\\Users\\wangxg\\Downloads";
////		  delAllFile(path); //删除完里面所有内容
		  File file=new File(path);
//		  
		  File[] tempList = file.listFiles();
//		  
		  for (int i = 0; i < tempList.length; i++) {
//		   if (tempList[i].isFile()) {
//		   //读取某个文件夹下的所有文件
		    String paths = file.list()[i];
		    System.out.println(paths);
		    if(paths.contains("zip")){
//		    	tempList[i].renameTo(new File("d:\\alipayBill\\2014.zip"));
//		    	System.out.println(paths+"文件："+tempList[i]);
//		    	copyFile(path+"\\"+paths, "d:\\");
		    }else if(paths.contains("csv")){
		    	tempList[i].renameTo(new File("d:\\alipayBill\\2014.csv"));
		    }else{
		    	tempList[i].delete();
		    }
		   }
////		   if (tempList[i].isDirectory()) {
////		    //读取某个文件夹下的所有文件夹
////		    System.out.println("文件夹："+tempList[i]);
////		   }
//		  }
//		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
//		System.err.println("请输入查询月份");
//		Scanner sc=new Scanner(System.in);  //键盘输入
//		String dateValue=sc.next();
//		Date date=sf.parse(dateValue);
//		System.err.println(sf.format(date));
//		if(sc!=null){
//		sc.close();
//		}
//		getDate(sf.parse("2016-04"));
	}
	public static void getDate(Date nowDate){
		Calendar cad=Calendar.getInstance();
		cad.setTime(nowDate);
		int day_month=cad.getActualMaximum(Calendar.DAY_OF_MONTH); //获取当月天数
		int[][] array=new int[6][7];
		List list = new ArrayList();
		for(int i=1;i<=day_month;i++){               //循环遍历每天
		cad.set(Calendar.DAY_OF_MONTH,i);
		int week_month=cad.get(Calendar.WEEK_OF_MONTH);  //获取改天在本月的第几个星期，也就是第几行
		int now_day_month=cad.get(Calendar.DAY_OF_WEEK);   //获取该天在本星期的第几天  ，也就是第几列
//		if(week_month==1){
//			list.add(week_month);
//		}
		System.err.println(i-1+"--i--"+week_month+"***"+now_day_month);
		array[week_month-1][now_day_month-1]=i-1;              //将改天存放到二位数组中
		}
//		int num = Integer.parseInt(Utils.currentDay());
////		System.err.println(list.size());
////		System.err.println(Utils.getHour());
////		System.err.println(7-(list.size()+1)+num+7+"***"+num+"---"+Utils.currentYear()+"-"+Utils.currentMonth());
//		String[] weeks = {"一", "二", "三", "四", "五", "六","日" };
//		for(String w:weeks){
//		System.err.print(w+"\t");
//		}
//		System.err.println();
//		   for(int i=0;i<=array.length-1;i++){
//		      for(int j=0;j<=array[i].length-1;j++){
//		      if(array[i][j]!=0){                     //没有存放的数字默认为0，过滤
//		      System.err.print(array[i][j]+"**");
//		      }
//		      System.err.print("\t");
//		      if((j+1)%7==0){
//		      System.err.println();
//		      }
//		      }
//		     }
	}
}
