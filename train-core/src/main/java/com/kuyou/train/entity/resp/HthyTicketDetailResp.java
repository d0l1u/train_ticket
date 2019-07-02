package com.kuyou.train.entity.resp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * HthyTicketDetailResp
 *
 * @author taokai3
 * @date 2018/12/13
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HthyTicketDetailResp {

    @JSONField(name = "train_code")
    private String trainCode;

    @JSONField(name = "train_type")
    private String trainType;

    @JSONField(name = "can_buy_now")
    private String canBuyNow;

    @JSONField(name = "start_station_name")
    private String startStationName;

    @JSONField(name = "end_station_name")
    private String endStationName;

    @JSONField(name = "from_station_name")
    private String fromStationName;

    @JSONField(name = "from_station_code")
    private String fromStationCode;

    @JSONField(name = "start_time")
    private String fromTime;

    @JSONField(name = "to_station_name")
    private String toStationName;

    @JSONField(name = "to_station_code")
    private String toStationCode;

    @JSONField(name = "arrive_time")
    private String toTime;

    @JSONField(name = "run_time_minute")
    private String runTime;


    @JSONField(name = "arrive_days")
    private String runDay;

    @JSONField(name = "swz_price")
    private String shangWuZuoPrice;

    @JSONField(name = "swz_num")
    private String shangWuZuoNum;

    @JSONField(name = "tdz_price")
    private String teDengZuoPrice;

    @JSONField(name = "tdz_num")
    private String teDengZuoNum;

    @JSONField(name = "ydz_price")
    private String yiDengZuoPrice;

    @JSONField(name = "ydz_num")
    private String yiDengZuoNum;

    @JSONField(name = "edz_price")
    private String erDengZuoPrice;

    @JSONField(name = "edz_num")
    private String erDengZuoNum;

    @JSONField(name = "gjrws_price")
    private String gaoJiDongWuoShangPrice;

    @JSONField(name = "gjrw_price")
    private String gaoJiDongWuoXiaPrice;

    @JSONField(name = "gjrw_num")
    private String gaoJiDongWuoNum;

    @JSONField(name = "dw_price")
    private String dongWoShangPrice;

    @JSONField(name = "dwx_price")
    private String dongWoXiaPrice;

    @JSONField(name = "dw_num")
    private String dongWoNum;

    @JSONField(name = "rw_price")
    private String ruanWoShangPrice;

    @JSONField(name = "rwx_price")
    private String ruanWoXiaPrice;

    @JSONField(name = "rw_num")
    private String ruanWoNum;

    @JSONField(name = "yw_price")
    private String yingWoShangPrice;

    @JSONField(name = "ywz_price")
    private String yingWoZhongPrice;

    @JSONField(name = "ywx_price")
    private String yingWoXiaPrice;

    @JSONField(name = "yw_num")
    private String yingWoNum;

    @JSONField(name = "rz_price")
    private String ruanZuoPrice;

    @JSONField(name = "rz_num")
    private String ruanZuoNum;

    @JSONField(name = "yz_price")
    private String yingZuoPrice;

    @JSONField(name = "yz_num")
    private String yingZuoNum;

    @JSONField(name = "wz_price")
    private String wuZuoPrice;

    @JSONField(name = "wz_num")
    private String wuZuoNum;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
