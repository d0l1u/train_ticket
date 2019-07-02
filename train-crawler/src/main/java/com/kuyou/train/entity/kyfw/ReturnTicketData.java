package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ReturnTicketData
 *
 * @author taokai3
 * @date 2018/11/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnTicketData {

    @JSONField(ordinal = 1)
    private boolean submitStatus = true;

    @JSONField(ordinal = 2)
    private String errMsg;

    /**
     * 总票价
     */
    @JSONField(ordinal = 3)
    private BigDecimal ticket_price;

    /**
     * 实际退款金额
     */
    @JSONField(ordinal = 4)
    private BigDecimal return_price;

    /**
     * 手续费
     */
    @JSONField(ordinal = 5)
    private BigDecimal return_cost;

    /**
     * 退票手续费率
     */
    @JSONField(ordinal = 6)
    private String return_rate;

    private String ticket_no;
    private String sequence_no;
    private String batch_no;
    private Date train_date;
    private String coach_no;
    private String coach_name;
    private String seat_no;
    private String seat_name;
    private String seat_type_name;
    private Date pay_limit_time;
    private Date realize_time_char;
    private String amount;
    private int amount_char;
    private String start_train_date_page;
    private String str_ticket_price_page;
    private String come_go_traveller_ticket_page;
    private String rate;
    private String return_deliver_flag;
    private String deliver_fee_char;
    private boolean is_need_alert_flag;
    private String is_deliver;
    private String dynamicProp;
    private String fee_char;
    private String insure_query_no;
    private String column_nine_msg;
    private String trms_price_rate;
    private String trms_price_number;
    private String trms_service_code;
    private String book_user_name;
    private String sale_mode_type;
    private String if_cash;
    private String inform_flag;
    private String inform_text;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
