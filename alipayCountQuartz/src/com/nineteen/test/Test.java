package com.nineteen.test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.nineteen.util.Tools;
import com.nineteen.util.Utils;

public class Test {
	public static void main(String[] args) throws UnsupportedEncodingException,
			Exception {		
		int nums = Integer.parseInt(Utils.currentDay());
		
		
		String month = Utils.currentMonth(-1);
		int i = Integer.parseInt(month);
		int num = Utils.dy(i);
		System.err.println(num+"--"+month);
//		String origValue = "aGNtMjM0Nzg5"; // pasier
//		String k1 =Utils.decBASE64(origValue);
//		System.out.println("解密后：" + k1);
//
//		String k2 = "pasiecsa";
//		// 将k2进行加密
//		String s2 = Utils.encryptBASE64(k2.getBytes());
//		System.out.println("加密后：" + s2);
//		String accout_balance ="可用余额：2000.00元";
//		String regEx = "可用余额：(.*?)元";
//        Pattern pattern = Pattern.compile(regEx);  
//        Matcher matcher = pattern.matcher(accout_balance);
//        if(!matcher.find()){  
//            System.out.println("格式错误!");  
//            return;  
//        }  
//        System.out.println(matcher.group(1)); 
		
//		String str = "2016-03-15 15:02:56";
//		str = str.replace("-", "").replace(":", "").replace(" ", "");
//		System.out.println(str);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
//	
//		long millionSeconds = 0;
//		try {
//			millionSeconds = sdf.parse(str).getTime();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}//毫秒
//		
//		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
//		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//		String datas = dfs.format(new Date());
//		System.out.println(datas);
//		String data = df.format(new Date());
//		long end =sdf.parse(data).getTime()-millionSeconds;
//		System.out.println(end/1000);
		
//		System.err.println(Utils.getHour());
//		
//		File dir = new File("D:\\alipayBill\\"+Utils.currentDate(-1));  
//		if(!dir.exists()){
//			dir.mkdir();
//		}
		//数字字符串   
//		String StrBd="18,061.50".replace(",", "");   
//		//构造以字符串内容为值的BigDecimal类型的变量bd   
//		BigDecimal bd=new BigDecimal(StrBd);   
//		//设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)   
//		bd=bd.setScale(2, BigDecimal.ROUND_HALF_UP);   
//		//转化为字符串输出   
//		String OutString=bd.toString();
//		System.err.println(OutString);
//		int digit=Integer.valueOf("101001011",2);//根据2进制形式转换成10进制
//		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
//		convertWeekByDate(sf.parse("2016-05-01"));
//		print(sf.parse("2016-04"));
//		String month = Utils.currentMonth();
//		System.err.println(month);
//		String str = "huochepiao19e04@163.com";
//
//		System.err.println(str.substring(str.indexOf("19e")+3, str.indexOf("@")));
//		System.out.println("2015年05月04日".substring(8, 10));
		
	
		
//		boolean files = false;
//		do {
//			boolean rs = Tools.isFile("d:\\authcodes.png");
//			if(rs==true){
//				files=true;
//			}
//		} while (!files);
	}
	
	private static void convertWeekByDate(Date time) {  
		    
		          SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式  
		          Calendar cal = Calendar.getInstance();  
		          cal.setTime(time);  
		         //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
		         int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
		         if(1 == dayWeek) {  
		             cal.add(Calendar.DAY_OF_MONTH, -1);  
		         }  
		         System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期  
		         cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
		         int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
		         cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   
		         String imptimeBegin = sdf.format(cal.getTime());  
		         System.out.println("所在周星期一的日期："+imptimeBegin);  
		         cal.add(Calendar.DATE, 6);  
		         String imptimeEnd = sdf.format(cal.getTime());  
		         System.out.println("所在周星期日的日期："+imptimeEnd);  
		           
		     }  
	private static void print(Date nowDate) {
        int intervalTime = 1;//与中国星期计算方式的差值
        Calendar cad=Calendar.getInstance();
		cad.setTime(nowDate);
        int day_month=cad.getActualMaximum(Calendar.DAY_OF_MONTH); //获取当月天数
        System.err.println(day_month);
        int weekDay = 7;//一周
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E:yyyy-MM-dd");
        int beginDayOfWeek = calendar.getActualMinimum(Calendar.DAY_OF_WEEK) + intervalTime;
        System.out.println(beginDayOfWeek);
        calendar.set(Calendar.DAY_OF_WEEK, beginDayOfWeek);
        for(int i = 0 ; i < weekDay; i++) {
            System.out.println(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
    }
}
