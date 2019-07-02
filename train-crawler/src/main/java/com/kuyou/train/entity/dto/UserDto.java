package com.kuyou.train.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDto
 *
 * @author taokai3
 * @date 2018/11/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String is_receive;
    private String password;
    private String password_new;
    private String pwd_question;
    private String pwd_answer;
    private String sex_code;
    private String country_code;
    private String mobile_no;
    private String phone_no;
    private String email;
    private String address;
    private String postalcode;
    private String is_active;
    private String revSm_code;
    private String last_login_time;
    private long user_id;
    private String phone_flag;
    private String encourage_flag;
    private String user_status;
    private String check_id_flag;
    private String is_valid;
    private String display_control_flag;
    private String needModifyEmail;
    private String flag_member;
    private String pic_control_flag;
    private String regist_time;
    private LoginUserDto loginUserDTO;

}
