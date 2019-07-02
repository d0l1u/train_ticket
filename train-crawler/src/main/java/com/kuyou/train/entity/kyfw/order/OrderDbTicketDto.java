package com.kuyou.train.entity.kyfw.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * OrderDbTicketDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDbTicketDto {

    @JSONField(name = "stationTrainDTO")
    private StationTrainDto stationTrainDto;

    @JSONField(name = "passengerDTO")
    private PassengerDto passengerDto;
    private String ticket_no;
    private String sequence_no;
    private String batch_no;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date train_date;
    private String coach_no;
    private String coach_name;
    private String seat_no;
    private String seat_name;
    private String seat_flag;
    private String seat_type_code;
    private String seat_type_name;
    private String ticket_type_code;
    private String ticket_type_name;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date reserve_time;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date limit_time;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date lose_time;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date pay_limit_time;
    private Integer ticket_price;
    private String print_eticket_flag;
    private String resign_flag;
    private String return_flag;
    private String confirm_flag;
    private String pay_mode_code;
    private String ticket_status_code;
    private String ticket_status_name;
    private String cancel_flag;
    private Integer amount_char;
    private String trade_mode;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date start_train_date_page;
    private String str_ticket_price_page;
    private String come_go_traveller_ticket_page;
    private String return_deliver_flag;
    private String deliver_fee_char;
    private boolean is_need_alert_flag;
    private String is_deliver;
    private String dynamicProp;
    private String fee_char;
    private String insure_query_no;
    private String column_nine_msg;
    private String lc_flag;
    private String integral_pay_flag;

    private String trms_price_rate;
    private String trms_price_number;
    private String trms_service_code;


    public String name() {
        return passengerDto.getPassenger_name();
    }

    public String cardType() {
        return passengerDto.getPassenger_id_type_code();
    }

    public String cardNo() {
        return passengerDto.getPassenger_id_no();
    }

    public String trainCode() {
        return stationTrainDto.getStation_train_code();
    }

    public String trainNo() {
        return stationTrainDto.getTrainDto().getTrain_no();
    }

    public String fromStationName() {
        return stationTrainDto.getFrom_station_name();
    }

    public String toStationName() {
        return stationTrainDto.getTo_station_name();
    }

    public String fromStationCode() {
        return stationTrainDto.getFrom_station_telecode();
    }

    public String toStationCode() {
        return stationTrainDto.getTo_station_telecode();
    }



    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
