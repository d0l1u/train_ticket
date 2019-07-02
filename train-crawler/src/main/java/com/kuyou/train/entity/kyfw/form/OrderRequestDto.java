package com.kuyou.train.entity.kyfw.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderRequestDto
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    private int adult_num;
    private String apply_order_no;
    private String bed_level_order_num;
    private String bureau_code;
    private String cancel_flag;
    private String card_num;
    private String channel;
    private int child_num;
    private String choose_seat;
    private int disability_num;
    private String exchange_train_flag;
    private String from_station_name;
    private String from_station_telecode;
    private String get_ticket_pass;
    private String id_mode;
    private String isShowPassCode;
    private String leftTicketGenTime;
    private String order_date;
    private String passengerFlag;
    private String realleftTicket;
    private String reqIpAddress;
    private String reqTimeLeftStr;
    private String reserve_flag;
    private String seat_detail_type_code;
    private String seat_type_code;
    private String sequence_no;
    private String start_time_str;
    private String station_train_code;
    private int student_num;
    private int ticket_num;
    private String ticket_type_order_num;
    private String to_station_name;
    private String to_station_telecode;
    private String tour_flag;
    private String trainCodeText;
    private String train_date_str;
    private String train_location;
    private String train_no;
    private String train_order;
    private String varStr;
}
