package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.common.util.DateFormat;
import com.kuyou.train.common.util.DateUtil;
import lombok.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * FromTimeDto：出发时间，到达时间
 *
 * @author taokai3
 * @date 2018/10/26
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class TrainTimeDto {

    private transient Date fromTime;
    private transient Date toTime;

    private String fromTimeStr;
    private Integer runTime;

    public TrainTimeDto(Date fromTime, Date toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public void mathRunTime(Date fromTime, Date toTime) {
        this.runTime = (int) ((toTime.getTime() - fromTime.getTime()) / 1000 / 60);
        this.fromTimeStr = DateUtil.format(fromTime, DateFormat.TIME);
    }

    public void mathTime(Date fromDate) {
        this.fromTime = DateUtil.add(fromDate, DateUtil.mathMinutes(fromTimeStr), TimeUnit.MINUTES);
        this.toTime = DateUtil.add(fromTime, runTime, TimeUnit.MINUTES);
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static void main(String[] args) {
        TrainTimeDto build = TrainTimeDto.builder().build();
        build.mathRunTime(new Date(), new Date());
        System.err.println(build.getFromTimeStr());

        build = TrainTimeDto.builder().build();
        build.setFromTimeStr("2:10");
        build.setRunTime(20);
        build.mathTime(new Date());
        System.err.println(build.getFromTime());
        System.err.println(build.getToTime());
    }
}
