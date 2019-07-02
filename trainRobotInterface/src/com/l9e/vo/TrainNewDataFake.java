package com.l9e.vo;

import java.io.Serializable;

/**
 * @author zuoyuxing
 *
 */
public class TrainNewDataFake implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String xh;//排序编号
	
	private String cc;//车次
	
	private String fz;//发车站
	
	private String dz;//出发车站
	
	private String wz;//无座票价
	
	private String yz;//硬座票价
	
	private String rz;//软座票价
	
	private String yws;//硬卧上票价
	
	private String ywz;//硬卧中票价
	
	private String ywx;//硬卧下票价
	
	private String rws;//软卧上票价
	
	private String rwx;//软卧下票价
	
	private String rz2;//二等座票价
	
	private String rz1;//一等软座票价 
	
	private String swz;//商务座
	
	private String tdz;//特等座票价
	
	private String gws;//高级软卧上票价
	
	private String gwx;//高级软卧下票价 
	
	private String dws; //动卧上票价
	
	private String dwx; //动卧下票价

	public String parse(String str){
		return Double.valueOf(str)==0 ? "-" :str;
	}
	
	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getFz() {
		return fz;
	}

	public void setFz(String fz) {
		this.fz = fz;
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz;
	}

	public String getWz() {
		return wz;
	}

	public void setWz(String wz) {
		this.wz = wz;
	}

	public String getYz() {
		return yz;
	}

	public void setYz(String yz) {
		this.yz = parse(yz);
	}

	public String getRz() {
		return rz;
	}

	public void setRz(String rz) {
		this.rz = parse(rz);
	}
	
	public String getDws() {
		return dws;
	}

	public void setDws(String dws) {
		this.dws = parse(dws);
	}

	public String getDwx() {
		return dwx;
	}

	public void setDwx(String dwx) {
		this.dwx = parse(dwx);
	}

	public String getYws() {
		return yws;
	}

	public void setYws(String yws) {
		this.yws = parse(yws);
	}

	public String getYwz() {
		return ywz;
	}

	public void setYwz(String ywz) {
		this.ywz = parse(ywz);
	}

	public String getYwx() {
		return ywx;
	}

	public void setYwx(String ywx) {
		this.ywx = parse(ywx);
	}

	public String getRws() {
		return rws;
	}

	public void setRws(String rws) {
		this.rws = parse(rws);
	}

	public String getRwx() {
		return rwx;
	}

	public void setRwx(String rwx) {
		this.rwx = parse(rwx);
	}

	public String getRz2() {
		return rz2;
	}

	public void setRz2(String rz2) {
		this.rz2 = parse(rz2);
	}

	public String getRz1() {
		return rz1;
	}

	public void setRz1(String rz1) {
		this.rz1 = parse(rz1);
	}

	public String getSwz() {
		return swz;
	}

	public void setSwz(String swz) {
		this.swz = parse(swz);
	}

	public String getTdz() {
		return tdz;
	}

	public void setTdz(String tdz) {
		this.tdz = parse(tdz);
	}

	public String getGws() {
		return gws;
	}

	public void setGws(String gws) {
		this.gws = parse(gws);
	}

	public String getGwx() {
		return gwx;
	}

	public void setGwx(String gwx) {
		this.gwx = parse(gwx);
	}
}
