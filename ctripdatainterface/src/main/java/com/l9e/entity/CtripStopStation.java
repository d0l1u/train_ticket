package com.l9e.entity;

/**
 * 携程途径站实体
 * @author meizs
 * @create 2018-05-30 16:26
 **/
public class CtripStopStation {

    private String StationName;//车站名
    private String StationNo;//车站序号（可能不连续）
    private String StartTime;//离站时间
    private String ArrivalTime;//到站时间
    private String StopMinutes;//停靠时间（分钟）

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public String getStationNo() {
        return StationNo;
    }

    public void setStationNo(String stationNo) {
        StationNo = stationNo;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public String getStopMinutes() {
        return StopMinutes;
    }

    public void setStopMinutes(String stopMinutes) {
        StopMinutes = stopMinutes;
    }
}
