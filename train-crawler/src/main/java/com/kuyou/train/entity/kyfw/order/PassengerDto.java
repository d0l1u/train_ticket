package com.kuyou.train.entity.kyfw.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PassengerDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {

    private String passenger_name;
    private String passenger_id_type_code;
    private String passenger_id_type_name;
    private String passenger_id_no;
    private String total_times;
    private String gat_born_date;
    private String gat_valid_date_start;
    private String gat_valid_date_end;
    private String gat_version;
}
