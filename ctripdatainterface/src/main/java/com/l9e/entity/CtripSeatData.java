package com.l9e.entity;

/**
 * @author meizs
 * 坐席数据实体
 */
public class CtripSeatData {
	
	private String SeatName; //坐席名称
	private double Price; //坐席票价
	private  int TicketLeft;//余票数量
	public String getSeatName() {
		return SeatName;
	}
	public void setSeatName(String seatName) {
		SeatName = seatName;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public int getTicketLeft() {
		return TicketLeft;
	}
	public void setTicketLeft(int ticketLeft) {
		TicketLeft = ticketLeft;
	}
	
}
