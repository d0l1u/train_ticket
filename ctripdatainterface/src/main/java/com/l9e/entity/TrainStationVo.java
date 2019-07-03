package com.l9e.entity;

import com.l9e.util.DateUtil;

import java.io.Serializable;
import java.io.Serializable;

import com.l9e.util.DateUtil;

/**
 *
 * @author zuoyx
 * 途经车站信息
 */
public class TrainStationVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String stationno;

    private String name;

    private String costtime;

    private String arrtime;

    private String starttime;

    private String interval;	 //停车时间

    private String distance;

    private String costday;

    private String checi;

    public String getCostday() {
        return costday;
    }

    public void setCostday(String costday) {
        this.costday = costday;
    }

    public String getCheci() {
        return checi;
    }

    public void setCheci(String checi) {
        this.checi = checi;
    }

    public void reSetInterval(){
        String temp = DateUtil.minuteDiff_2(this.arrtime,this.starttime);
        this.interval = ("0").equals(temp) ? "---" : temp;
    }
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCosttime() {
        return costtime;
    }

    public void setCosttime(String costtime) {
        this.costtime = costtime;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getStationno() {
        return stationno;
    }

    public void setStationno(String stationno) {
        this.stationno = stationno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArrtime() {
        return arrtime;
    }

    public void setArrtime(String arrtime) {
        this.arrtime = arrtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

}

