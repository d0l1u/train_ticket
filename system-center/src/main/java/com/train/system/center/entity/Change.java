package com.train.system.center.entity;

import java.util.Date;
import java.util.List;

public class Change {
	
	private Integer changeId;
	private String orderId;
	private String myOrderId;
	private String supplierOrderId;
	private String changeStatus;
	private String sequence;
	private String trainCode;
	private String newTrainCode;
	private Date departureDate;
	private Date newDepartureDate;
	private Date departureTime;
	private Date newDepartureTime;
	private Date arrivalDate;
	private Date newArrivalDate;
	private Date arrivalTime;
	private Date bookingTime;
	private Date payTime;
	private Date newArrivalTime;
	private String fromStationName;
	private String newFromStationName;
	private String toStationName;
	private String newToStationName;
	private Integer accountId;
	/**true:变更到站, false:改签*/
	private Boolean isChangeTo;
	
	/** true:不改到无座票,false:允许改到无座票 */
	private Boolean hasSeat;
	
	/**  1：平改   2：高改低   3：低改高*/
	private Integer payType;
	private String chooseSeats;
	private Date payLimitTime;
	private String supplierType;
	private String failReason;
	private String serialnumber;

	private List<Passenger> passengerList;
	
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public Date getBookingTime() {
		return bookingTime;
	}
	public void setBookingTime(Date bookingTime) {
		this.bookingTime = bookingTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public List<Passenger> getPassengerList() {
		return passengerList;
	}
	public void setPassengerList(List<Passenger> passengerList) {
		this.passengerList = passengerList;
	}
	public Integer getChangeId() {
		return changeId;
	}
	public void setChangeId(Integer changeId) {
		this.changeId = changeId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getMyOrderId() {
		return myOrderId;
	}
	public void setMyOrderId(String myOrderId) {
		this.myOrderId = myOrderId;
	}
	public String getSupplierOrderId() {
		return supplierOrderId;
	}
	public void setSupplierOrderId(String supplierOrderId) {
		this.supplierOrderId = supplierOrderId;
	}
	public String getChangeStatus() {
		return changeStatus;
	}
	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getTrainCode() {
		return trainCode;
	}
	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}
	public String getNewTrainCode() {
		return newTrainCode;
	}
	public void setNewTrainCode(String newTrainCode) {
		this.newTrainCode = newTrainCode;
	}
	public Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	public Date getNewDepartureDate() {
		return newDepartureDate;
	}
	public void setNewDepartureDate(Date newDepartureDate) {
		this.newDepartureDate = newDepartureDate;
	}
	public Date getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}
	public Date getNewDepartureTime() {
		return newDepartureTime;
	}
	public void setNewDepartureTime(Date newDepartureTime) {
		this.newDepartureTime = newDepartureTime;
	}
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public Date getNewArrivalDate() {
		return newArrivalDate;
	}
	public void setNewArrivalDate(Date newArrivalDate) {
		this.newArrivalDate = newArrivalDate;
	}
	public Date getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public Date getNewArrivalTime() {
		return newArrivalTime;
	}
	public void setNewArrivalTime(Date newArrivalTime) {
		this.newArrivalTime = newArrivalTime;
	}
	public String getFromStationName() {
		return fromStationName;
	}
	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}
	public String getNewFromStationName() {
		return newFromStationName;
	}
	public void setNewFromStationName(String newFromStationName) {
		this.newFromStationName = newFromStationName;
	}
	public String getToStationName() {
		return toStationName;
	}
	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}
	public String getNewToStationName() {
		return newToStationName;
	}
	public void setNewToStationName(String newToStationName) {
		this.newToStationName = newToStationName;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Boolean getIsChangeTo() {
		return isChangeTo;
	}
	public void setIsChangeTo(Boolean isChangeTo) {
		this.isChangeTo = isChangeTo;
	}
	public Boolean getHasSeat() {
		return hasSeat;
	}
	public void setHasSeat(Boolean hasSeat) {
		this.hasSeat = hasSeat;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getChooseSeats() {
		return chooseSeats;
	}
	public void setChooseSeats(String chooseSeats) {
		this.chooseSeats = chooseSeats;
	}
	public Date getPayLimitTime() {
		return payLimitTime;
	}
	public void setPayLimitTime(Date payLimitTime) {
		this.payLimitTime = payLimitTime;
	}
	public String getSupplierType() {
		return supplierType;
	}
	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}


	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
}
