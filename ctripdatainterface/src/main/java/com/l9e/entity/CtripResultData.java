package com.l9e.entity;

import java.util.List;

/**
 * @author meizs
 * 返回结果数据实体
 */
public class CtripResultData {
	
	private String ResponseStatus;//响应状态
	private String TimeStamp; //时间戳
	private List<CtripZhanZhanData> Trains;//车次数据
	private String Message; //消息提示
	private String TicketLeftTime;
	private boolean IsRealTicket;
	
	public String getResponseStatus() {
		return ResponseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		ResponseStatus = responseStatus;
	}
	public String getTimeStamp() {
		return TimeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}
	public List<CtripZhanZhanData> getTrains() {
		return Trains;
	}
	public void setTrains(List<CtripZhanZhanData> trains) {
		Trains = trains;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getTicketLeftTime() {
		return TicketLeftTime;
	}
	public void setTicketLeftTime(String ticketLeftTime) {
		TicketLeftTime = ticketLeftTime;
	}
	public boolean isIsRealTicket() {
		return IsRealTicket;
	}
	public void setIsRealTicket(boolean isRealTicket) {
		IsRealTicket = isRealTicket;
	}

}
