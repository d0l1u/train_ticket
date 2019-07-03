package com.l9e.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.l9e.util.DateUtil;

/**
 * 改签常量
 * @author caona
 *
 */
public class ChangeConsts {
	private static Map<String, String> changeErrorInfo = new HashMap<String, String>();
	static {
		changeErrorInfo.put("1002","距离开车时间太近无法改签");
		changeErrorInfo.put("1004","取消改签次数超过上限,无法继续操作");
		changeErrorInfo.put("301","没有余票");
		changeErrorInfo.put("305","乘客已经预定过该车次");
		changeErrorInfo.put("309","没有足够的票");
		changeErrorInfo.put("310","本次购票与其他订单冲突");
		changeErrorInfo.put("506","系统异常,无法正常占座操作");
		changeErrorInfo.put("313","订单内乘客已被法院依法限制高消费，禁止乘坐当前预订席别");
		changeErrorInfo.put("314","12306账号异常_该账号需要核验，请确认");
		changeErrorInfo.put("998","订单所属账号被封，无法处理");
		changeErrorInfo.put("999","未定义的12306错误");
		changeErrorInfo.put("9991","旅游票,请到车站办理");
		changeErrorInfo.put("320","未找到改签待支付订单");
		changeErrorInfo.put("308","存在未完成订单");
		changeErrorInfo.put("316","不允许改签到指定时间的车票");
		changeErrorInfo.put("318","已退票，不可改签");
		changeErrorInfo.put("315","未找到要改签的车次");
		changeErrorInfo.put("317","已出票，不可改签");
		changeErrorInfo.put("319","已改签，不可改签");
		changeErrorInfo.put("322","前的排队购票人数过多，请稍后重试");
		changeErrorInfo.put("324","已确认改签，不可取消");
	}
	public static String getChangeErrorInfo(String errorCode) {
		String errorInfo = changeErrorInfo.get(errorCode);
		return errorInfo;
	}
	//改签结算费率
	public static Double getDiffRate(String from_time, Date current) {
		Double rate = 0.0;//默认值
		
		Date from = DateUtil.stringToDate(from_time, DateUtil.DATE_FMT3);
		Date from_24hour = DateUtil.dateAddDays(from, -1);//24小时以内
		Date from_48hour = DateUtil.dateAddDays(from, -2);//48小时以内
		Date from_15Day = DateUtil.dateAddDays(from, -15);
		
		if(current.after(from_24hour)) {
			rate = 0.2;
		} else if(current.before(from_24hour) && current.after(from_48hour)) {
			rate = 0.1;
		} else if(current.before(from_48hour) && current.after(from_15Day)) {
			rate = 0.05;
		} else {
			
		}
		return rate;
	}

}
