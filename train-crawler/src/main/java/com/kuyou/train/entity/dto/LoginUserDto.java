package com.kuyou.train.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginUserDto
 *
 * @author taokai3
 * @date 2018/12/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserDto {

    private String center;
    private String login_channel;
    private String login_site;
    private String login_id;
    private String order_type;
    private String agent_contact;
    private String user_type;
    private String user_name;
    private String name;
    private String id_type_code;
    private String id_type_name;
    private String id_no;
    private String member_id;
    private String member_level;
    private String userIpAddress;
    private String gat_born_date;
    private String gat_valid_date_start;
    private String gat_valid_date_end;
    private String gat_version;
    private String refundLogin;
}
