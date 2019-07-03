package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 火车票新接口根节点信息
 * @author zuoyuxing
 *
 */
@XStreamAlias("OTResponse")
public class OuterSoukdNewData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("Sdate")
	private String sdate;
	
	@XStreamAlias("SType")
	private String stype;
	
	@XStreamAlias("Code")
	private String code;
	
	@XStreamAlias("ErrInfo")
	private String errInfo;
	
	@XStreamImplicit(itemFieldName="Datajson")
	private List<TrainNewData> datajson;

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrInfo() {
		return errInfo;
	}

	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}

	public List<TrainNewData> getDatajson() {
		return datajson;
	}

	public void setDatajson(List<TrainNewData> datajson) {
		this.datajson = datajson;
	}

}
