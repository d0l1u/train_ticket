package com.kuyou.train.entity.kyfw.order;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * OrderDbDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDbDto {

    private String sequence_no;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date order_date;
    private Integer ticket_totalnum;
    private Integer ticket_price_all;
    private String cancel_flag;
    private String resign_flag;
    private String return_flag;
    private String print_eticket_flag;
    private String pay_flag;
    private String pay_resign_flag;
    private String confirm_flag;
    private List<OrderDbTicketDto> tickets;
    private String reserve_flag_query;
    private String if_show_resigning_info;
    private String recordCount;
    private String isNeedSendMailAndMsg;
    private List<String> array_passser_name_page;
    private List<String> from_station_name_page;
    private List<String> to_station_name_page;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date start_train_date_page;
    private String start_time_page;
    private String arrive_time_page;
    private String train_code_page;
    private String ticket_total_price_page;
    private String come_go_traveller_order_page;
    private String canOffLinePay;
    private String if_deliver;
    private String insure_query_no;
}
