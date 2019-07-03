package com.l9e.transaction.vo;

public class ReturnError {
	private String ret;
	private String err_code;
	private String err_msg;
	public static String ERRCODE1="000";
	public static String ERRCODE2="001";
	public static String ERRCODE3="002";
	public static String ERRCODE4="003";
	public static String ERRCODE5="004";
	public static String ERRCODE6="005";
	public static String ERRCODE7="100";
	public static String ERRCODE8="101";
	public static String ERRCODE9="200";
	public static String ERRCODE10="201";
	public static String ERRCODE11="202";
	public static String ERRCODE12="301";
	public static String ERRCODE13="444";
	
	public static String ERRMSG1="操作成功";
	public static String ERRMSG2="系统错误,未知服务异常";
	public static String ERRMSG3="安全验证错误,不符合安全校验规则";
	public static String ERRMSG4="非法参数错误";
	public static String ERRMSG5="非授权的合作伙伴";
	public static String ERRMSG6="暂停服务的合作伙伴";//占时未设置权限
	
	public static String ERRMSG7="无效的图片格式";
	public static String ERRMSG8="非法时间设置";
	
	public static String ERRMSG9="请求无效的验证码结果";
	public static String ERRMSG10="请求的验证码尚未被处理";
	public static String ERRMSG11="请求的验证码已经超时";
	
	public static String ERRMSG12="反馈无效的验证状态";
	
	public static String ERRMSG13="图片传输失败";
	
	public static String FAIL="failure";//返回错误标示
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String errCode) {
		err_code = errCode;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String errMsg) {
		err_msg = errMsg;
	}
}
