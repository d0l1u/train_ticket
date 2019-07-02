package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuyou.train.entity.kyfw.form.OrderRequestDto;
import com.kuyou.train.entity.kyfw.form.QueryLeftNewDetailDto;
import com.kuyou.train.entity.kyfw.form.QueryLeftTicketRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderFormData
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderFormData {

    private String isAsync;
    private String key_check_isChange;
    private String leftTicketStr;
    private String maxTicketNum;

    @JSONField(name = "orderRequestDTO")
    private OrderRequestDto orderRequestDto;

    private String purpose_codes;

    @JSONField(name = "queryLeftNewDetailDTO")
    private QueryLeftNewDetailDto queryLeftNewDetailDto;

    @JSONField(name = "queryLeftTicketRequestDTO")
    private QueryLeftTicketRequestDto queryLeftTicketRequestDto;
    private String tour_flag;
    private String train_location;

    /**
     * 内部车次
     *
     * @return
     */
    public String innerTrainCode() {
        return orderRequestDto.getTrain_no();
    }

    /**
     * 获取车次
     *
     * @return
     */
    public String trainCode() {
        return orderRequestDto.getStation_train_code();
    }

    /**
     * 获取出发站简码
     *
     * @return
     */
    public String fromStationCode() {
        return orderRequestDto.getFrom_station_telecode();
    }


    /**
     * 获取到达站简码
     *
     * @return
     */
    public String toStationCode() {
        return orderRequestDto.getTo_station_telecode();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
