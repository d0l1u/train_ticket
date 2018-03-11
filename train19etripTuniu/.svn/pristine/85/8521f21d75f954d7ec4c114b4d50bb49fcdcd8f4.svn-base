package com.l9e.util;



import java.util.List;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

public class ut {

	public static DateFormat df_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateFormat df_file_time = new SimpleDateFormat("yyyyMMddHHmmss");
	public static DateFormat df_day = new SimpleDateFormat("yyyy-MM-dd");
	public static DecimalFormat df = new DecimalFormat("0.00"); 
	public static Pattern int_pattern = Pattern.compile("[0-9]*");
	
	public static ArrayList<Map.Entry<String,String>> sortStringMap(Map map){  
	    List<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>(map.entrySet());  
	    Collections.sort(entries, new Comparator<Map.Entry<String, String>>() {  
	        public int compare(Map.Entry<String, String> obj1 , Map.Entry<String, String> obj2) {  
	            return obj2.getValue().toString().compareTo(obj1.getValue().toString());  
	        }  
	    });  
	    return (ArrayList<Entry<String, String>>) entries;  
	}
	
	public static ArrayList<Map.Entry<String,Integer>> sortIntegerMap(Map map){  
	    List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());  
	    Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {  
	        public int compare(Map.Entry<String, Integer> obj1 , Map.Entry<String, Integer> obj2) {  
	            return obj2.getValue() - obj1.getValue();  
	        }  
	    });  
	    return (ArrayList<Entry<String, Integer>>) entries;  
	} 
	
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
	
	public static int passDay(String date_1,String date_2){
		int min = 0;
		try {
			Date date1 = df_day.parse(date_1);
			Date date2 = df_day.parse(date_2);
			long diff = date2.getTime() - date1.getTime() ;
		    long m = diff / (1000 * 60 * 60 * 24);
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
	
	public static String currentMonth(String this_month , int i){
		Date date;
		Calendar c = Calendar.getInstance();
		try {
			date = df_day.parse(this_month+"-01");
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
			c.set(Calendar.MONTH,month+i);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return df_day.format( c.getTime() ).substring(0, 7);
	};
	
	public static int passMonth(String start, String end){
		  Date date_start;
		  Date date_end;
		  int pass = 0;
		  Calendar  c1 = Calendar.getInstance();
		  Calendar  c2 = Calendar.getInstance();
		  
		  try {
	          date_start = df_day.parse(start+"-01");
		      date_end = df_day.parse(end+"-01");
			c1.setTime(date_start); 
			c2.setTime(date_end); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  while (c1.compareTo(c2)<0)
		  {
			  log("pass="+pass);
			  pass++;
			  c1.add(Calendar.MONTH,1);// 开始日期加一个月直到等于结束日期为止
		  }
		  return pass;
		 }
	
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
	 *  判断字符串是否为�?
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
    public static String getTextFromHtml(String htmlStr){  
        htmlStr = delHTMLTag(htmlStr);  
        htmlStr = htmlStr.replaceAll("&nbsp;", "");  
//        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);  
        return htmlStr;  
    }
 // 对传入参数的Map对象进行value值转换和转码
	public static Map changeParaMap(Map paraMap) {
		// 参数Map
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = paraMap.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			try {
				// value =new String(value.getBytes("ISO-8859-1"), "UTF-8");
				value = encodeStr(value);
				URLDecoder.decode(value, "UTF-8");
				returnMap.put(name, value);
				// log.info("接收供货平台推送——对传入参数的Map对象进行value值转换和转码KEY--"+name);
				// log.info("接收供货平台推送——对传入参数的Map对象进行value值转换和转码VALUE--"+value);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnMap;
	}

	// 乱码
	public static String encodeStr(String str) {
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** 
     * 上传图片 
     *  
     * @param urlStr 
     * @param textMap 
     * @param fileMap 
     * @return 
     */  
    public static String formUpload(String urlStr, Map<String, String> textMap,  
            Map<String, String> fileMap) {  
        String res = "";  
        HttpURLConnection conn = null;  
        String BOUNDARY = "---------------------------"; //boundary就是request头和上传文件内容的分隔符  
        try {  
            URL url = new URL(urlStr);  
            conn = (HttpURLConnection) url.openConnection();  
            conn.setConnectTimeout(5000);  
            conn.setReadTimeout(30000);  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            conn.setRequestMethod("POST");  
            conn.setRequestProperty("Connection", "Keep-Alive");  
            conn  
                    .setRequestProperty("User-Agent",  
                            "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");  
            conn.setRequestProperty("Content-Type",  
                    "multipart/form-data; boundary=" + BOUNDARY);  
  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
            // text  
            if (textMap != null) {  
                StringBuffer strBuf = new StringBuffer();  
                Iterator iter = textMap.entrySet().iterator();  
                while (iter.hasNext()) {  
                    Map.Entry entry = (Map.Entry) iter.next();  
                    String inputName = (String) entry.getKey();  
                    String inputValue = (String) entry.getValue();  
                    if (inputValue == null) {  
                        continue;  
                    }  
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(  
                            "\r\n");  
                    strBuf.append("Content-Disposition: form-data; name=\""  
                            + inputName + "\"\r\n\r\n");  
                    strBuf.append(inputValue);  
                }  
                out.write(strBuf.toString().getBytes());  
            }  
  
            // file  
            if (fileMap != null) {  
                Iterator iter = fileMap.entrySet().iterator();  
                while (iter.hasNext()) {  
                    Map.Entry entry = (Map.Entry) iter.next();  
                    String inputName = (String) entry.getKey();  
                    String inputValue = (String) entry.getValue();  
                    if (inputValue == null) {  
                        continue;  
                    }  
                    File file = new File(inputValue);  
                    String filename = file.getName();  
                    String contentType = new MimetypesFileTypeMap()  
                            .getContentType(file);  
                    if (filename.endsWith(".png")) {  
                        contentType = "image/png";  
                    }  
                    if (contentType == null || contentType.equals("")) {  
                        contentType = "application/octet-stream";  
                    }  
  
                    StringBuffer strBuf = new StringBuffer();  
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(  
                            "\r\n");  
                    strBuf.append("Content-Disposition: form-data; name=\""  
                            + inputName + "\"; filename=\"" + filename  
                            + "\"\r\n");  
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");  
  
                    out.write(strBuf.toString().getBytes());  
  
                    DataInputStream in = new DataInputStream(  
                            new FileInputStream(file));  
                    int bytes = 0;  
                    byte[] bufferOut = new byte[1024];  
                    while ((bytes = in.read(bufferOut)) != -1) {  
                        out.write(bufferOut, 0, bytes);  
                    }  
                    in.close();  
                }  
            }  
  
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
            out.write(endData);  
            out.flush();  
            out.close();  
  
            // 读取返回数据  
            StringBuffer strBuf = new StringBuffer();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(  
                    conn.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                strBuf.append(line).append("\n");  
            }  
            res = strBuf.toString();  
            reader.close();  
            reader = null;  
        } catch (Exception e) {  
        	e.printStackTrace();  
            System.out.println("发送POST请求出错。" + urlStr);  
            return "error";
//            
        } finally {  
            if (conn != null) {  
                conn.disconnect();  
                conn = null;  
            }  
        }  
        return res;  
    }  
}
