package com.l9e.transaction.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.TuniuConstant;
import com.l9e.transaction.dao.TuniuCommonDao;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.vo.Station;
import com.l9e.util.DateUtil;
import com.l9e.util.MD5Util;

@Service("tuniuCommonService")
public class TuniuCommonServiceImpl implements TuniuCommonService {

	private static final Logger logger = Logger
			.getLogger(TuniuCommonServiceImpl.class);
	
	@Resource
	private TuniuCommonDao tuniuCommonDao;

	@Override
	public String generateSign(Map<String, String> params, String signKey) {
		if (params == null || params.size() == 0)
			return null;

		String sign = "";

		List<String> sortList = new ArrayList<String>(params.keySet());
		Collections.sort(sortList);

		StringBuilder builder = new StringBuilder();
		String str4md5 = "";
		for (String key : sortList) {
			String value = params.get(key);
			if (value == null || value.equals(""))
				continue;
			builder.append(key).append(value);
		}
		str4md5 = builder.toString();

		builder.setLength(0);
		builder.append(signKey).append(str4md5).append(signKey);
		str4md5 = builder.toString();

		sign = MD5Util.md5(str4md5, "UTF-8").toUpperCase();
		return sign;
	}

	@Override
	public String getErrorMessage(String code) {
		if (code == null || code.equals(""))
			return null;

		try {
			Integer codeValue = Integer.valueOf(code);
			switch (codeValue) {
			/* 通用code返回信息 */
			
			case 231000:
				return "请求成功";
			case 231008:
				return "入参异常";
			case 231006:
				return "时间戳异常";
			case 231001:
				return "请求用户不存在";
			case 231007:
				return "签名异常";
			case 231099:
				return "未知异常";
			case 231009:
				return "接口不存在";
			
			/* 业务code返回信息 */
			/* 预订 */
			case 110000: 
				return "所购买的车次坐席已无票";
			case 110001: 
				return "乘客信息有误";
			case 110002: 
				return "行程冲突";
			case 110003: 
				return "乘客身份信息待核验";
			case 110005: 
				return "乘客身份信息冒用";
			case 110012:
				return "限制高消费";
			case 110006:
				return "12036账号未通过手机核验";
			case 110011:
				return "12306账号所添加的常旅数量已达到上限";
			case 110013:
				return "存在未完成订单";
			case 110014:
				return "取消次数过多";
			case 1002:
				return "账号用户名不存在";
			case 110022:
				return "账号密码错误";
			case 110024:
				return "系统异常,操作失败";
			case 110017:
				return "该车次未到起售时间，请稍后再试";
			case 110021:
				return "12306暂未查询到该车次信息或车次已经停运";
			case 110023:
				return "12306账号已被锁定，请稍后再试";
				/* 出票 */
			case 120002:
				return "请求时间已超时，出票失败";
			case 120000:
				return "出票失败";
			case 120001:
				return "出票成功";
			case 120005:
				return "您已在12306取消订单，出票失败";
			case 120006:
				return "12306账号密码错误";
			case 120008:
				return "系统异常,操作失败";
			case 120007:
				return "12306账号已被锁定，请稍后再试";
				/* 取消 */
			case 130000:
				return "订单已出票不能取消";
			case 130001:
				return "取消失败";
			case 130002:
				return "取消成功";
			case 130005:
				return "当前的订单状态不能取消订单";
			case 130004:
				return "订单正在取消中";
			case 130003:
				return "订单已取消不能取消";
			case 130010:
				return "系统异常,操作失败";
			case 130009:
				return "12306账号已被锁定，请稍后再试";
			case 130008:
				return "12306账号密码错误";
			/* 退款 */
			case 140000:
				return "距离开车时间太近无法退票" ;
			case 140001:
				return "退票成功" ;
			case 140002:
				return "退票失败" ;
			case 140003:
				return "已取纸质票，不能退票" ;
			case 140004:
				return "订单已经退票成功，请勿重复提交";
			case 140005:
				return "订单正在退票，请勿重复提交";
			case 140007:
				return "尊敬的旅客，为防止网上囤票倒票，给广大旅客创造一个公平的购票环境，凡通过互联网或手机购买的本次列车车票，如需办理退票、改签和变更到站等变更业务，请持乘车人身份证件原件到就近车站办理，代办时还需持代办人的身份证件原件";
			case 140010:
				return "2306账号已被锁定，请稍后再试";
			case 140011:
				return "系统异常";
				
			case 1600101:
				return "请求改签失败";
			case 1600102:
				return "取消改签次数超过上限，无法改签";
			case 1600103:
				return "距离开车时间太近，无法改签";
			case 1600104:
				return "未找到要改签的车次";
			case 1600105:
				return "未找到要改签的车票";
			case 1600106:
				return "账号登陆失败";
			case 1600107:
				return "已出票，请到车站办理";
			case 1600108:
				return "已改签，不能再次改签";
			case 1600109:
				return "已退票，无法改签";	
			case 1600110:
				return "旅游票，请到站办理";
			case 1600111:
				return "订单状态不正确";
			case 1600112:
				return "重复提交的改签信息";
			case 1600113:
				return "批量改签新票和原车票座位都不能为卧铺";
			case 1600114:
				return "没有余票";
			case 1600115:
				return "当前的排队购票人数过多，请稍后重试";
			case 1600116:
				return "限制高消费铺";
			case 1600117:
				return "存在未完成订单";
			case 1600118:
				return "用户已在其他地点登录";
			case 1600119:
				return "您好，开车前48小时（不含）以上，可改签预售期内的其它列车；开车前48小时以内且非开车当日，可改签票面日期当日及之前的其它列车；开车当日，可改签票面日期当日的其它列车";
			case 1600120:
				return "行程冲突";
			case 1600000:
				return "账号信息有误，和下单账号不一致或不是自带账号下单";
			case 1600201:
				return "确认改签失败";
			case 1600202:
				return "确认改签超时";
			case 1600203:
				return "未找到订单";
			case 1600301:
				return	"取消改签失败";
			case 1600302:
				return "订单已确认改签，不能取消改签";
			case 1600303:
				return "未找到要取消改签的订单";
			case 1600304:
				return "取消改签超时";
			
			case 200001:
				return "查询订单状态失败";
			case 200002:
				return "查询订单状态，未找到订单";
			case 2001:
				return "催退款失败";
			/*请求验证码*/
			case 250001:
				return "12306账号不存在";
			case 250002:
				return "12306账号密码错误";
			case 250003:
				return "用户被锁定，20分钟后重试";
			case 250004:
				return "乘客身份信息冒用";
			case 250005:
				return "乘客身份信息未通过核验";
			case 250006:
				return "该12306账号所添加的常旅数量已达到上限";
			case 250007:
				return "您的身份信息正在进行网上核验，您也可持居民身份证原件到车站售票窗口即时办理核验";
			case 250008:
				return "登陆失败,请重新尝试！您的手机号尚未进行核验,目前暂无法用于登陆,请您先使用用户名或邮箱登录,然后选择手机核验,核验通过后即可使用手机号码登录功能,谢谢";
			case 250009:
				return "证件号码输入有误！";
			case 250010:
				return "您还有未处理的订单!";
			case 250011:
				return "请勿重复下单";//未找到匹配改签信息!
			case 250012:
				return "当前系统繁忙。请稍后再试";
			case 260008:
				return "验证码超时,请重新提交";
			case 260010:
				return "对不起,由于您的取消次数过多,今日将不能继续受理您的请求";
			case 260011:
				return "您还有未处理的订单";
			default:
				return null;
			}
		} catch (NumberFormatException e) {
			logger.info("订单状态码异常，无法匹配信息，code：" + code);
			return null;
		}
	}

	@Override
	public boolean validateTimestamp(String timestamp) {
		/*测试返回true*/
		SimpleDateFormat format = new SimpleDateFormat(TuniuConstant.TIMESTAMP_FORMAT);
		Date time = null;

		try {
			time = format.parse(timestamp);
		} catch (ParseException e) {
			logger.info("时间戳格式无效");
		}

		if (time == null)
			return false;

		if (DateUtil.minuteDiff(time, new Date()) > 5) {
			logger.info("时间戳误差超时");
			return false;
		}
		return true;
	}

	@Override
	public String sysIdsType(String idsType) {
		char type = singleString2Char(idsType);
		switch (type) {
		case '1':
			return "2";// 二代身份证
		case '2':
			return "1";// 一代身份证
		case 'C':
			return "3";// 港澳通行证
		case 'G':
			return "4";// 台湾通行证
		case 'B':
			return "5";// 护照
		case 'H':// 外国人居留证
			
		default:
			return null;
		}
	}

	@Override
	public String sysSeatType(String seatType) {
		char type = singleString2Char(seatType);
		switch (type) {
		case '1':
			return "8";// 硬座
		case '2':
			return "7";// 软座
		case '3':
			return "6";// 硬卧
		case '4':
			return "5";// 软卧
		case '6':
			return "4";// 高级软卧
		case '9':
			return "0";// 商务座
		case 'P':
			return "1";// 特等座
		case 'M':
			return "2";// 一等座
		case 'O':
			return "3";// 二等座
		case '0':
		case '5':
		case '7':
		case '8':
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':			
			return "20"; //动卧
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'Q':
		case 'S':
			return "10";// 其他
		default:
			return null;
		}
	}

	@Override
	public String sysTicketType(String ticketType) {
		char type = singleString2Char(ticketType);
		switch (type) {
		case '1':
			return "0";// 成人
		case '2':
			return "1";// 儿童
		case '3':
			return "3";// 学生
		case '4':
			return "2";// 残军
		default:
			return null;
		}
	}

	@Override
	public String tuniuIdsName(String tuniuIdsTypeId) {
		char type = singleString2Char(tuniuIdsTypeId);
		switch (type) {
		case '1':
			return "二代身份证";
		case '2':
			return "一代身份证";
		case 'C':
			return "港澳通行证";
		case 'G':
			return "台湾通行证";
		case 'B':
			return "护照";
		case 'H':
			return "外国人居留证";
		default:
			return null;
		}
	}

	@Override
	public String tuniuSeatName(String tuniuSeatTypeId) {
		char type = singleString2Char(tuniuSeatTypeId);
		switch (type) {
		case '0':return "棚车";
		case '1':return "硬座";
		case '2':return "软座";
		case '3':return "硬卧";
		case '4':return "软卧";
		case '5':return "包厢硬卧";
		case '6':return "高级软卧";
		case '7':return "一等软座";
		case '8':return "二等软座";
		case '9':return "商务座";
		case 'A':return "高级动卧";
		case 'B':return "混编硬座";
		case 'C':return "混编硬卧";
		case 'D':return "包厢软座";
		case 'E':return "特等软座";
		case 'F':return "动卧";
		case 'G':return "二人软包";
		case 'H':return "一人软包";
		case 'I':return "一等双软";
		case 'J':return "二等双软";
		case 'k':return "混编软座";
		case 'L':return "混编软卧";
		case 'M':return "一等座";
		case 'O':return "二等座";
		case 'P':return "特等座";
		case 'Q':return "观光座";
		case 'S':return "一等包座";
		default:
			return null;
		}
	}

	@Override
	public String tuniuTicketName(String tuniuTicketTypeId) {
		char type = singleString2Char(tuniuTicketTypeId);
		switch (type) {
		case '1':
			return "成人票";
		case '2':
			return "儿童票";
		case '3':
			return "学生票";
		case '4':
			return "残军票";
		default:
			return null;
		}
	}
	
	@Override
	public List<Station> queryStations(String trainCode) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("trainCode", trainCode);
		List<Station> stations = tuniuCommonDao.selectStations(queryParam);
		for(int i = 0;i < stations.size();i++) {
			Station station = stations.get(i);
			if(i == 0) {
				station.setStayTime("始发站");
			} else if(i == (stations.size() - 1)) {
				station.setStayTime("终点站");
			} else {
				String startTime = station.getStartTime();
				String arriveTime = station.getArriveTime();
				station.setStayTime(stayTime(startTime, arriveTime));
			}
		}
		return stations;
	}

	/**
	 * 单字符字串转char
	 * 
	 * @param single
	 * @return
	 */
	protected Character singleString2Char(String single) {
		Character c = null;
		if (StringUtils.isEmpty(single))
			return c;
		if (single.length() > 1)
			return c;

		char[] cArray = single.toCharArray();
		c = cArray[0];
		return c;
	}

	/**
	 * 停留时间  始发站/xx分钟/终点站
	 * @param startTime
	 * @param arriveTime
	 * @return
	 */
	private String stayTime(String startTime, String arriveTime) {
		String regex = "HH:mm";
		Date startDate = DateUtil.stringToDate(startTime, regex);
		Date arriveDate = DateUtil.stringToDate(arriveTime, regex);
		
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar arrive = Calendar.getInstance();
		arrive.setTime(arriveDate);
		if(arrive.after(start)) {
			start.add(Calendar.DATE, 1);
		}
		long diffMinute = DateUtil.minuteDiff(start.getTime(), arrive.getTime());
		return diffMinute+ "分钟";
	}
}
