package com.kuyou.train.entity.kyfw.order;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * StationTrainDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StationTrainDto {

    @JSONField(name = "trainDTO")
    private TrainDto trainDto;
    private String station_train_code;
    private String from_station_telecode;
    private String from_station_name;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date start_time;
    private String to_station_telecode;
    private String to_station_name;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date arrive_time;
    private String distance;
}
