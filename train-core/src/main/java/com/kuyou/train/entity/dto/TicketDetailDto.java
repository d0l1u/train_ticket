package com.kuyou.train.entity.dto;

/**
 * TicketDetailDto
 *
 * @author taokai3
 * @date 2018/12/13
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TicketInfoDto：查询余票返回结果
 *
 * @author taokai3
 * @date 2018/12/13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailDto {

    /**
     * 车次
     */
    @JSONField(name = "train_code", ordinal = 1)
    private String trainCode;

    /**
     * 始发站
     */
    @JSONField(name = "start_station", ordinal = 2)
    private String startStationName;

    /**
     * 始发站时间 HH:MM
     */
    @JSONField(name = "start_time", ordinal = 3)
    private String startTime;

    /**
     * 终点站
     */
    @JSONField(name = "end_station", ordinal = 4)
    private String endStationName;

    /**
     * 终点站时间 HH:MM
     */
    @JSONField(name = "end_time", ordinal = 5)
    private String endTime;


    /**
     * 出发站名
     */
    @JSONField(name = "from_station", ordinal = 6)
    private String fromStationName;

    /**
     * 出发时间
     */
    @JSONField(name = "from_time", ordinal = 7)
    private String fromTime;

    /**
     * 到达站
     */
    @JSONField(name = "arrive_station", ordinal = 8)
    private String toStationName;

    /**
     * 到达时间
     */
    @JSONField(name = "arrive_time", ordinal = 9)
    private String toTime;

    /**
     * 历时，分钟数
     */
    @JSONField(name = "cost_time", ordinal = 10)
    private String runTime;

    /**
     * 商务座票价
     */
    @JSONField(name = "swz", ordinal = 11)
    private String shangWuZuoPrice;

    /**
     * 商务座数量
     */
    @JSONField(name = "swz_num", ordinal = 12)
    private String shangWuZuoNum;


    /**
     * 特等票价
     */
    @JSONField(name = "tdz", ordinal = 13)
    private String teDengZuoPrice;

    /**
     * 特等数量
     */
    @JSONField(name = "tdz_num", ordinal = 14)
    private String teDengZuoNum;

    /**
     * 一等票价
     */
    @JSONField(name = "rz1", ordinal = 15)
    private String yiDengZuoPrice;

    /**
     * 一等数量
     */
    @JSONField(name = "rz1_num", ordinal = 16)
    private String yiDengZuoNum;

    /**
     * 二等票价
     */
    @JSONField(name = "rz2", ordinal = 17)
    private String erDengZuoPrice;

    /**
     * 二等数量
     */
    @JSONField(name = "rz2_num", ordinal = 18)
    private String erDengZuoNum;


    /**
     * 高级软卧上铺票价
     */
    @JSONField(name = "gws", ordinal = 19)
    private String gaoJiDongWuoShangPrice;

    /**
     * 高级软卧下铺票价
     */
    @JSONField(name = "gwx", ordinal = 20)
    private String gaoJiDongWuoXiaPrice;

    /**
     * 高级软卧票数
     */
    @JSONField(name = "gw_num", ordinal = 21)
    private String gaoJiDongWuoNum;

    /**
     * 动卧上票价
     */
    @JSONField(name = "dws", ordinal = 22)
    private String dongWoShangPrice;

    /**
     * 动卧下票价
     */
    @JSONField(name = "dwx", ordinal = 23)
    private String dongWoXiaPrice;

    /**
     * 动卧票数
     */
    @JSONField(name = "dw_num", ordinal = 24)
    private String dongWoNum;

    /**
     * 软卧上铺票价
     */
    @JSONField(name = "rws", ordinal = 25)
    private String ruanWoShangPrice;

    /**
     * 软卧下铺票价
     */
    @JSONField(name = "rwx", ordinal = 26)
    private String ruanWoXiaPrice;

    /**
     * 软卧票数
     */
    @JSONField(name = "rw_num", ordinal = 27)
    private String ruanWoNum;

    /**
     * 硬卧上票价
     */
    @JSONField(name = "yws", ordinal = 28)
    private String yingWoShangPrice;

    /**
     * 硬卧中票价
     */
    @JSONField(name = "ywz", ordinal = 29)
    private String yingWoZhongPrice;

    /**
     * 硬卧下票价
     */
    @JSONField(name = "ywx", ordinal = 30)
    private String yingWoXiaPrice;

    /**
     * 硬卧票数
     */
    @JSONField(name = "yw_num", ordinal = 31)
    private String yingWoNum;


    /**
     * 软座票价
     */
    @JSONField(name = "rz", ordinal = 32)
    private String ruanZuoPrice;

    /**
     * 软座票数
     */
    @JSONField(name = "rz_num", ordinal = 33)
    private String ruanZuoNum;

    /**
     * 硬座票价
     */
    @JSONField(name = "yz", ordinal = 34)
    private String yingZuoPrice;

    /**
     * 硬座票数
     */
    @JSONField(name = "yz_num", ordinal = 35)
    private String yingZuoNum;

    /**
     * 无座票价
     */
    @JSONField(name = "wz", ordinal = 36)
    private String wuZuoPrice;

    /**
     * 无座票数
     */
    @JSONField(name = "wz_num", ordinal = 37)
    private String wuZuoNum;

    /**
     * 保留字段1
     */
    @JSONField(name = "train_pro1", ordinal = 38)
    @Builder.Default
    private String train_pro1 = "";

    /**
     * 保留字段2
     */
    @JSONField(name = "train_pro2", ordinal = 39)
    @Builder.Default
    private String train_pro2 = "";

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
