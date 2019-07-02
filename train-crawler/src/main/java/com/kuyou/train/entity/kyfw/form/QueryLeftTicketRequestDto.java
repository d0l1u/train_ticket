package com.kuyou.train.entity.kyfw.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QueryLeftTicketRequestDto
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryLeftTicketRequestDto {

    private String arrive_time;
    private String bigger20;
    private String exchange_train_flag;
    private String from_station;
    private String from_station_name;
    private String from_station_no;
    private String lishi;
    private String login_id;
    private String login_mode;
    private String login_site;
    private String purpose_codes;
    private String query_type;
    private String seatTypeAndNum;
    private String seat_types;
    private String start_time;
    private String start_time_begin;
    private String start_time_end;
    private String station_train_code;
    private String ticket_type;
    private String to_station;
    private String to_station_name;
    private String to_station_no;
    private String train_date;
    private String train_flag;
    private String train_headers;
    private String train_no;
    private boolean useMasterPool;
    private boolean useWB10LimitTime;
    private boolean usingGemfireCache;
    private String ypInfoDetail;
}
