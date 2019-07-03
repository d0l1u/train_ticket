package com.l9e.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.l9e.util.DateUtil;

/**
 * @author meizs
 * 改签相关变量
 */
public class ElongChangeConsts {
	
	public static final String MERCHANT_ID="elong";
	public static final String SIGNKEY="c8f4895204618b8f452cd719be945d52";
	
	//**********************************同步返回码*******************************************************//
	public static final String RETURN_CODE_000 = "000";
	public static final String RETURN_CODE_001 = "001";
	public static final String RETURN_CODE_002 = "002";
	public static final String RETURN_CODE_003 = "003";
	public static final String RETURN_CODE_004 = "004";
	public static final String RETURN_CODE_005 = "005";
	public static final String RETURN_CODE_006 = "006";
	public static final String RETURN_CODE_007 = "007";
	public static final String RETURN_CODE_101 = "101";
	public static final String RETURN_CODE_102 = "102";
	public static final String RETURN_CODE_103 = "103";
	public static final String RETURN_CODE_104 = "104";
	public static final String RETURN_CODE_105 = "105";
	public static final String RETURN_CODE_106 = "106";
	public static final String RETURN_CODE_107 = "107";
	public static final String RETURN_CODE_108 = "108";
	public static final String RETURN_CODE_201 = "201";
	public static final String RETURN_CODE_202 = "202";
	public static final String RETURN_CODE_203 = "203";
	public static final String RETURN_CODE_204 = "204";
	public static final String RETURN_CODE_205 = "205";
	public static final String RETURN_CODE_301 = "301";
	public static final String RETURN_CODE_401 = "401";
	public static final String RETURN_CODE_402 = "402";
	public static final String RETURN_CODE_501 = "501";
	public static final String RETURN_CODE_601 = "601";
	public static final String RETURN_CODE_602 = "602";
	public static final String RETURN_CODE_603 = "603";
	public static final String RETURN_CODE_604 = "604";
	public static final String RETURN_CODE_701 = "701";
	public static final String RETURN_CODE_801 = "801";
	public static final String RETURN_CODE_802 = "802";
	public static final String RETURN_CODE_803 = "803";
	public static final String RETURN_CODE_804 = "804";
	public static final String RETURN_CODE_805 = "805";
	public static final String RETURN_CODE_806 = "806";
	public static final String RETURN_CODE_807 = "807";
	public static final String RETURN_CODE_808 = "808";
	public static final String RETURN_CODE_809 = "809";
	public static final String RETURN_CODE_810 = "810";
	public static final String RETURN_CODE_800= "800";
	public static final String RETURN_CODE_811 = "811";	
	public static final String RETURN_CODE_812 = "812";
	public static final String RETURN_CODE_814 = "814";
	
	private static Map<String, String> RETURNCODE_TYPE = new HashMap<String, String>();//对外接口状态码说明
	
	//同步返回的一些错误码
	static{
		
		RETURNCODE_TYPE.put(RETURN_CODE_000, "");
		RETURNCODE_TYPE.put(RETURN_CODE_001, "系统错误，未知服务异常。");
		RETURNCODE_TYPE.put(RETURN_CODE_002, "安全验证错误，不符合安全校验规则。");
		RETURNCODE_TYPE.put(RETURN_CODE_003, "输入参数为空或错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_004, "非授权合作商户。");
		RETURNCODE_TYPE.put(RETURN_CODE_005, "备注不能超过100个字符。");
		RETURNCODE_TYPE.put(RETURN_CODE_006, "访问限制，访问频率过高，被禁止。");
		RETURNCODE_TYPE.put(RETURN_CODE_007, "合作商户余额不足，暂时停用，请充值后通知服务商重新启用！");
		RETURNCODE_TYPE.put(RETURN_CODE_101, "余票查询参数输入错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_102, "输入车站名为空，请确认!");
		RETURNCODE_TYPE.put(RETURN_CODE_103, "出发车站错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_104, "到达车站错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_105, "输入车站名错误，请确认");
		RETURNCODE_TYPE.put(RETURN_CODE_106, "查询返回数据失败，暂无列车信息");
		RETURNCODE_TYPE.put(RETURN_CODE_107, "车次信息有误，暂无该列车信息");
		RETURNCODE_TYPE.put(RETURN_CODE_108, "实时余票查询接口处于禁用状态，请联系服务商启用");
		RETURNCODE_TYPE.put(RETURN_CODE_201, "重复下单异常");
		RETURNCODE_TYPE.put(RETURN_CODE_202, "不在受理时间内，拒绝下订单操作，请在规定时间执行此操作。");
		RETURNCODE_TYPE.put(RETURN_CODE_203, "一个订单最多允许订购五张票。");
		RETURNCODE_TYPE.put(RETURN_CODE_204, "下单参数为空或者错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_205, "订单内乘客信息参数为空或者错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_301, "订单未找到（订单号错误或不存在）");
		RETURNCODE_TYPE.put(RETURN_CODE_401, "订单已出票成功，无法取消，请走退票退款流程。");
		RETURNCODE_TYPE.put(RETURN_CODE_402, "不在受理退款时间内，拒绝取消订单操作, 请在规定时间执行此操作。");
		RETURNCODE_TYPE.put(RETURN_CODE_501, "暂时没有该途经站信息。");
		RETURNCODE_TYPE.put(RETURN_CODE_601, "支付金额与下单时支付金额不符。");
		RETURNCODE_TYPE.put(RETURN_CODE_602, "该订单已经支付成功，拒绝重复支付。");
		RETURNCODE_TYPE.put(RETURN_CODE_603, "没有开通预定中支付订单权限。");
		RETURNCODE_TYPE.put(RETURN_CODE_604, "超过约定支付有效时间期，该订单已禁止支付。");
		RETURNCODE_TYPE.put(RETURN_CODE_701, "没有找到该订单下的保险信息。");
		RETURNCODE_TYPE.put(RETURN_CODE_800, "请求改签失败");
		RETURNCODE_TYPE.put(RETURN_CODE_801, "拒绝不存在订单改签");
		RETURNCODE_TYPE.put(RETURN_CODE_802, "订单状态不正确。");
		RETURNCODE_TYPE.put(RETURN_CODE_803, "拒绝重复申请订单改签。");
		RETURNCODE_TYPE.put(RETURN_CODE_804, "距离开车时间太近无法改签");
		RETURNCODE_TYPE.put(RETURN_CODE_805, "距离开车时间少于48小时无法变更到站。");
		RETURNCODE_TYPE.put(RETURN_CODE_806, "批量改签新票和原车票座位都不能为卧铺。");
		RETURNCODE_TYPE.put(RETURN_CODE_807, "车票已改签过。");
		RETURNCODE_TYPE.put(RETURN_CODE_808, "已出票，请到车站办理。");
		RETURNCODE_TYPE.put(RETURN_CODE_809, "旅游票，请到站办理");
		RETURNCODE_TYPE.put(RETURN_CODE_810, "确认改签的请求时间已超过规定时间。");
		RETURNCODE_TYPE.put(RETURN_CODE_811, "取消改签的请求时间已超过规定时间。");
		RETURNCODE_TYPE.put(RETURN_CODE_812, "取消改签次数超过上限，无法继续操作。");
		RETURNCODE_TYPE.put(RETURN_CODE_814, "未找到改签待支付订单。");
		
	}

	//异步回调失败原因
	private static Map<String, String> changeErrorInfo = new HashMap<String, String>(); //19e改签错误码
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
	
	private static Map<String, String> changeErrorCode = new HashMap<String, String>();  //转换为渠道需要的返回码
	
	static {
		changeErrorCode.put("1002","804");
		changeErrorCode.put("1004","812");
		changeErrorCode.put("301","1005");
		changeErrorCode.put("305","1007");
		changeErrorCode.put("309","1006");
		changeErrorCode.put("310","1007");
		changeErrorCode.put("506","1011");
		changeErrorCode.put("313","1009");
		changeErrorCode.put("314","");
		changeErrorCode.put("998","");
		changeErrorCode.put("999","1011");
		changeErrorCode.put("9991","809");
		changeErrorCode.put("320","814");
		changeErrorCode.put("308","815");
		changeErrorCode.put("316","816");
		changeErrorCode.put("318","818");
		changeErrorCode.put("315","813");
		changeErrorCode.put("317","808");
		changeErrorCode.put("319","819");
		changeErrorCode.put("322","822");
		changeErrorCode.put("324","824");
	}
	
	/*******************改签状态***********************************/
	public static final String TRAIN_REQUEST_CHANGE = "11";//改签预订
	public static final String TRAIN_REQUEST_CHANGE_ING = "12";//正在改签预订
	public static final String TRAIN_REQUEST_CHANGE_ARTIFICIAL = "13";// 人工改签预订
	public static final String TRAIN_REQUEST_CHANGE_SUCCESS = "14";//改签成功等待确认
	public static final String TRAIN_REQUEST_CHANGE_FAIL = "15";//改签预订失败
	public static final String TRAIN_CANCEL_CHANGE = "21";//改签取消
	public static final String TRAIN_CANCEL_CHANGE_ING = "22";// 正在取消
	public static final String TRAIN_CANCEL_CHANGE_SUCCESS = "23";//取消成功
	public static final String TRAIN_CANCEL_CHANGE_FAIL = "24";//取消失败
	public static final String TRAIN_CONFIRM_CHANGE = "31";//开始支付
	public static final String TRAIN_CONFIRM_CHANGE_PAY = "32";//正在支付
	public static final String TRAIN_CONFIRM_CHANGE_PAY_ARTIFICIAL = "33";//人工支付
	public static final String TRAIN_CONFIRM_CHANGE_SUCCESS = "34";//支付成功
	public static final String TRAIN_CONFIRM_CHANGE_FAIL = "35";//支付失败
	public static final String TRAIN_CONFIRM_CHANGE_START_PAY = "36";//补价支付
	
	/***************************改签回调状态*******************************/
	public static final String CHANGE_NOTIFY_PRE = "000";//准备异步回调
	public static final String CHANGE_NOTIFY_START = "111";//开始异步回调
	public static final String CHANGE_NOTIFY_OVER = "222";//回调完成
	public static final String CHANGE_NOTIFY_FAIL = "333";//回调失败
	
	
	
	
	public static String getChangeErrorInfo(String errorCode) {
		String errorInfo = changeErrorInfo.get(errorCode);
		return errorInfo;
	}
	
	
	public static  String getChangeErrorCode(String errorCode) {
		String errorInfo = changeErrorCode.get(errorCode);
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
