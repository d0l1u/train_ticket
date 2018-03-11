package test.l9e.tuniu.test;

import java.util.Calendar;
import java.util.Date;

import com.l9e.util.DateUtil;

public class DateTest {

	public static void main(String[] args) {
		String regex = "HH:mm";
		Date startDate = DateUtil.stringToDate("14:05", regex);
		Date arriveDate = DateUtil.stringToDate("13:55", regex);
		
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar arrive = Calendar.getInstance();
		arrive.setTime(arriveDate);
		if(arrive.after(start)) {
			start.add(Calendar.DATE, 1);
		}
		long diffMinute = DateUtil.minuteDiff(start.getTime(), arrive.getTime());
		
		String startStr = DateUtil.dateToString(start.getTime(), "yyyy-MM-dd HH:mm:ss");
		String arriveStr = DateUtil.dateToString(arrive.getTime(), "yyyy-MM-dd HH:mm:ss");
		System.out.println(startStr);
		System.out.println(arriveStr);
		System.out.println(diffMinute + "分钟");
	}
}
