package com.l9e.transaction.vo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.l9e.util.MemcachedUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * DGTrain 票价余票信息
 *
 */
@XStreamAlias("Data")
public class TrainData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ID")
	private String id;//排序编号
	
	@XStreamAlias("TrainCode")
	private String trainCode;//车次
	
	@XStreamAlias("TrainType")
	private String trainType;//列车等级
	
	@XStreamAlias("StartCity")
	private String startCity;//出发车站
	
	@XStreamAlias("StartTime")
	private String startTime;//出发时间
	
	@XStreamAlias("EndCity")
	private String endCity;//到达车站
	
	@XStreamAlias("EndTime")
	private String endTime;//到达时间
	
	@XStreamAlias("Limit")
	private String limit;
	
	@XStreamAlias("LimitTime")
	private String limitTime;
	
	@XStreamAlias("Distance")
	private String distance;//站站距离
	
	@XStreamAlias("CostTime")
	private String costTime;//运行时间
	
	@XStreamAlias("YZ")
	private String yz;//硬座票价
	
	@XStreamAlias("RZ")
	private String rz;//软座票价
	
	@XStreamAlias("RZ2")
	private String rz2;//二等软座票价 
	
	@XStreamAlias("RZ1")
	private String rz1;//一等软座票价
	
	@XStreamAlias("YWS")
	private String yws;//硬卧上铺票价
	
	@XStreamAlias("YWZ")
	private String ywz;//硬卧中铺票价
	
	@XStreamAlias("YWX")
	private String ywx;//硬卧下铺票价
	
	@XStreamAlias("RWS")
	private String rws;//软卧上铺票价
	
	@XStreamAlias("RWX")
	private String rwx;//软卧下铺票价
	
	@XStreamAlias("GWS")
	private String gws;//高级软卧上铺票价 
	
	@XStreamAlias("GWX")
	private String gwx;//高级软卧下铺票价
	
	@XStreamAlias("WZ_YP")
	private String wz_yp;
	
	@XStreamAlias("YZ_YP")
	private String yz_yp;
	
	@XStreamAlias("RZ_YP")
	private String rz_yp;
	
	@XStreamAlias("RZ1_YP")
	private String rz1_yp;
	
	@XStreamAlias("RZ2_YP")
	private String rz2_yp;
	
	@XStreamAlias("YW_YP")
	private String yw_yp;
	
	@XStreamAlias("RW_YP")
	private String rw_yp;
	
	@XStreamAlias("GW_YP")
	private String gw_yp;
	
	private String tdz="-";//特等座
	
	private String swz="-";//商务座
	
	private String tdz_yp="-";
	
	private String swz_yp="-";
	
	//页面展示余票 有/无
	private String wz_yp_show="0";
	private String yz_yp_show="0";
	private String rz_yp_show="0";
	private String rz1_yp_show="0";	
	private String rz2_yp_show="0";	
	private String yw_yp_show="0";
	private String rw_yp_show="0";
	private String gw_yp_show="0";
	private String tdz_yp_show="0";
	private String swz_yp_show="0";
	private String canBook="0";//是否可以预定 1/0 能/否
	

	private String replaceVal(String str){
		if(!StringUtils.isEmpty(str)){
			return str.replace("无", "-").replace("*", "-");
		}else{
			return "-";
		}
		
	}
	
	private String replaceYpShow(String str){
		if(StringUtils.isEmpty(str)){
			return "0";
		}else if("-".equals(str)){
			return "0";
		}else if(Double.parseDouble(str)<=MemcachedUtil.spareTicket){
			return "0";
		}else{
			this.canBook = "1";
			//return "有";
			return (Integer.parseInt(str)-MemcachedUtil.spareTicket)+"";
		}
	}

	public String getId() {
		return id;
	}

	public String getTrainCode() {
		return trainCode;
	}

	public String getTrainType() {
		return trainType;
	}

	public String getStartCity() {
		return startCity;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndCity() {
		return endCity;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getLimit() {
		return limit;
	}

	public String getLimitTime() {
		return limitTime;
	}

	public String getDistance() {
		return distance;
	}

	public String getCostTime() {
		return costTime;
	}

	public String getYz() {
		return yz;
	}

	public String getRz() {
		return rz;
	}

	public String getRz2() {
		return rz2;
	}

	public String getRz1() {
		return rz1;
	}

	public String getYws() {
		return yws;
	}

	public String getYwz() {
		return ywz;
	}

	public String getYwx() {
		return ywx;
	}

	public String getRws() {
		return rws;
	}

	public String getRwx() {
		return rwx;
	}

	public String getGws() {
		return gws;
	}

	public String getGwx() {
		return gwx;
	}

	public String getWz_yp() {
		return wz_yp;
	}

	public String getYz_yp() {
		return yz_yp;
	}

	public String getRz_yp() {
		return rz_yp;
	}

	public String getRz1_yp() {
		return rz1_yp;
	}

	public String getRz2_yp() {
		return rz2_yp;
	}

	public String getYw_yp() {
		return yw_yp;
	}

	public String getRw_yp() {
		return rw_yp;
	}

	public String getGw_yp() {
		return gw_yp;
	}

	public String getTdz() {
		return tdz;
	}

	public String getSwz() {
		return swz;
	}

	public String getTdz_yp() {
		return tdz_yp;
	}

	public String getSwz_yp() {
		return swz_yp;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = replaceVal(trainCode);
	}

	public void setTrainType(String trainType) {
		this.trainType = replaceVal(trainType);
	}

	public void setStartCity(String startCity) {
		this.startCity = startCity;
	}

	public void setStartTime(String startTime) {
		this.startTime = replaceVal(startTime);
	}

	public void setEndCity(String endCity) {
		this.endCity = endCity;
	}

	public void setEndTime(String endTime) {
		this.endTime = replaceVal(endTime);
	}

	public void setLimit(String limit) {
		this.limit = replaceVal(limit);
	}

	public void setLimitTime(String limitTime) {
		this.limitTime = replaceVal(limitTime);
	}

	public void setDistance(String distance) {
		this.distance = replaceVal(distance);
	}

	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}

	public void setYz(String yz) {
		this.yz = replaceVal(yz);
	}

	public void setRz(String rz) {
		this.rz = replaceVal(rz);
	}

	public void setRz2(String rz2) {
		this.rz2 = replaceVal(rz2);
	}

	public void setRz1(String rz1) {
		this.rz1 = replaceVal(rz1);
	}

	public void setYws(String yws) {
		this.yws = replaceVal(yws);
	}

	public void setYwz(String ywz) {
		this.ywz = replaceVal(ywz);
	}

	public void setYwx(String ywx) {
		this.ywx = replaceVal(ywx);
	}

	public void setRws(String rws) {
		this.rws = replaceVal(rws);
	}

	public void setRwx(String rwx) {
		this.rwx = replaceVal(rwx);
	}

	public void setGws(String gws) {
		this.gws = replaceVal(gws);
	}

	public void setGwx(String gwx) {
		this.gwx = replaceVal(gwx);
	}

	public void setWz_yp(String wz_yp) {
		this.wz_yp = replaceVal(wz_yp);
		this.wz_yp_show = replaceYpShow(this.wz_yp);
	}

	public void setYz_yp(String yz_yp) {
		this.yz_yp = replaceVal(yz_yp);
		this.yz_yp_show = replaceYpShow(this.yz_yp);
	}

	public void setRz_yp(String rz_yp) {
		this.rz_yp = replaceVal(rz_yp);
		this.rz_yp_show = replaceYpShow(this.rz_yp);
	}

	public void setRz1_yp(String rz1_yp) {
		this.rz1_yp = replaceVal(rz1_yp);
		this.rz1_yp_show = replaceYpShow(this.rz1_yp);
	}

	public void setRz2_yp(String rz2_yp) {
		this.rz2_yp = replaceVal(rz2_yp);
		this.rz2_yp_show = replaceYpShow(this.rz2_yp);
	}

	public void setYw_yp(String yw_yp) {
		this.yw_yp = replaceVal(yw_yp);
		this.yw_yp_show = replaceYpShow(this.yw_yp);
	}

	public void setRw_yp(String rw_yp) {
		this.rw_yp = replaceVal(rw_yp);
		this.rw_yp_show = replaceYpShow(this.rw_yp);
	}

	public void setGw_yp(String gw_yp) {
		this.gw_yp = replaceVal(gw_yp);
		this.gw_yp_show = replaceYpShow(this.gw_yp);
	}

	public void setTdz(String tdz) {
		this.tdz = replaceVal(tdz);
	}

	public void setSwz(String swz) {
		this.swz = replaceVal(swz);
	}

	public void setTdz_yp(String tdz_yp) {
		this.tdz_yp = replaceVal(tdz_yp);
		this.tdz_yp_show = replaceYpShow(this.tdz_yp);
	}

	public void setSwz_yp(String swz_yp) {
		this.swz_yp = replaceVal(swz_yp);
		this.swz_yp_show = replaceYpShow(this.swz_yp);
	}

	@JsonIgnore
	public String getWz_yp_show() {
		return wz_yp_show;
	}	
	@JsonIgnore
	public String getYz_yp_show() {
		return yz_yp_show;
	}
	@JsonIgnore
	public String getRz_yp_show() {
		return rz_yp_show;
	}
	@JsonIgnore
	public String getRz1_yp_show() {
		return rz1_yp_show;
	}
	@JsonIgnore
	public String getRz2_yp_show() {
		return rz2_yp_show;
	}
	@JsonIgnore
	public String getYw_yp_show() {
		return yw_yp_show;
	}
	@JsonIgnore
	public String getRw_yp_show() {
		return rw_yp_show;
	}
	@JsonIgnore
	public String getGw_yp_show() {
		return gw_yp_show;
	}
	@JsonIgnore
	public String getTdz_yp_show() {
		return tdz_yp_show;
	}
	@JsonIgnore
	public String getSwz_yp_show() {
		return swz_yp_show;
	}
	@JsonIgnore
	public String getCanBook() {
		return canBook;
	}

	public void setWz_yp_show(String wz_yp_show) {
		this.wz_yp_show = wz_yp_show;
	}

	public void setYz_yp_show(String yz_yp_show) {
		this.yz_yp_show = yz_yp_show;
	}

	public void setRz_yp_show(String rz_yp_show) {
		this.rz_yp_show = rz_yp_show;
	}

	public void setRz1_yp_show(String rz1_yp_show) {
		this.rz1_yp_show = rz1_yp_show;
	}

	public void setRz2_yp_show(String rz2_yp_show) {
		this.rz2_yp_show = rz2_yp_show;
	}

	public void setYw_yp_show(String yw_yp_show) {
		this.yw_yp_show = yw_yp_show;
	}

	public void setRw_yp_show(String rw_yp_show) {
		this.rw_yp_show = rw_yp_show;
	}

	public void setGw_yp_show(String gw_yp_show) {
		this.gw_yp_show = gw_yp_show;
	}

	public void setTdz_yp_show(String tdz_yp_show) {
		this.tdz_yp_show = tdz_yp_show;
	}

	public void setSwz_yp_show(String swz_yp_show) {
		this.swz_yp_show = swz_yp_show;
	}

	public void setCanBook(String canBook) {
		this.canBook = canBook;
	}

}
