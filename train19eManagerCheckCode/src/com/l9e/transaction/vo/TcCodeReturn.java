package com.l9e.transaction.vo;

public class TcCodeReturn {
	private String cid;//验证码识别请求唯⼀标识（错误反馈时需要），得到结果时有值
	private String ctype;//验证码类别反回，暂不使⽤
	private int ret;//1 表示得到结果；-1 表示无法处理；-2 数据格式错误，请确认
	private String res;//识别结果，识别结果为 1-8 混合序号
	private String desc;//描述信息
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCtype() {
		return ctype;
	}
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
